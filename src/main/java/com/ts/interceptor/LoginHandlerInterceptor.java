package com.ts.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ts.annotation.Rights;
import com.ts.entity.system.User;
import com.ts.service.system.role.RoleManager;
import com.ts.util.Const;
import com.ts.util.Jurisdiction;
import com.ts.util.Tools;

import net.sf.json.JSONSerializer;
/**
 * 
* 类名称：登录过滤，权限验证
* 类描述： 
* @author 
* 作者单位： 
* 联系方式：
* 创建时间：2015年11月2日
* @version 1.6
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter{

	@Resource(name="roleService")
	private RoleManager roleService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String path = request.getServletPath();
		if(path.matches(Const.NO_INTERCEPTOR_PATH)){
			return true;
		}else{
			Session session = Jurisdiction.getSession();
			User user = (User)session.getAttribute(Const.SESSION_USER);
			if(user!=null){
				Integer roleMax = (Integer)session.getAttribute(user.getUSERNAME()+Const.SESSION_USER_MAX_ROLE);
				if(null == roleMax){
					roleMax = roleService.getUserMaxRolesByUsId(user.getUSER_ID());
					session.setAttribute(user.getUSERNAME()+Const.SESSION_USER_MAX_ROLE, roleMax);
				}
				//超级管理员角色则有全部权限
				if(roleMax==0){ return true;}
				
				//path = path.substring(1, path.length());
				boolean flag = false; //访问权限校验
				if(handler instanceof HandlerMethod){
					Rights rights = ((HandlerMethod) handler).getMethodAnnotation(Rights.class);
					if(rights==null || Tools.isEmpty(rights.code())){
						return true;
					}
					flag = Jurisdiction.hasJurisdiction(user.getUSERNAME(), rights.code());
					if(!flag){
						//无任何权限，跳转到无权限页面
						if("action".equalsIgnoreCase(rights.type())){
							response.sendRedirect(request.getContextPath() + Const.NO_RIGHTS);
						}else{
						    PrintWriter out = null;
						    String result = "当前用户无该操作权限！";
						    try {
								if("json".equalsIgnoreCase(rights.dataType())){
									response.setCharacterEncoding("UTF-8");
									response.setContentType("text/json");
							        Map<String,String> map = new HashMap<String,String>();
							        map.put("result", result);
							        result = JSONSerializer.toJSON(map).toString();
								}
					        	out = response.getWriter();
					            out.println(result);
					        } catch (IOException e) {
					            e.printStackTrace();
					        } finally {
					            if (out != null) {
					                out.close();
					            }
					        }
						}
					}
				}else{
					flag = true;
				}
				return flag;
			}else{
				//登陆过滤
				response.sendRedirect(request.getContextPath() + Const.LOGIN);
				return false;		
			}
		}
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)throws Exception {
		
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ path + "/";
		if(modelAndView!=null){
			modelAndView.addObject("path", path);
			modelAndView.addObject("basePath", basePath);
		}
	}
}
