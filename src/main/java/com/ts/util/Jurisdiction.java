package com.ts.util;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.util.CollectionUtils;
import com.ts.entity.system.User;

/**
 * 权限处理
 * @author: xsl
 * 修改日期：2016/10/10
*/
public class Jurisdiction {

	/**
	 * 访问权限
	 * @param menuUrl  菜单路径
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasJurisdiction(String USER_NAME,String code){
		//
		if(Tools.isEmpty(USER_NAME) || Tools.isEmpty(code)){
			return true;
		}
		//判断是否拥有当前点击菜单的权限（内部过滤,防止通过url进入跳过菜单权限）
		/**
		 * 根据点击的菜单的xxx.do去菜单中的URL去匹配，当匹配到了此菜单，判断是否有此菜单的权限，没有的话跳转到404页面
		 * 根据按钮权限，授权按钮(当前点的菜单和角色中各按钮的权限匹对)
		 */
		boolean flag = false;
		List<String> mf = (List<String>)Jurisdiction.getSession().getAttribute(USER_NAME+Const.SESSION_ROLE_RIGHTS);
		if(mf == null || mf.isEmpty()){
			//无任何权限，跳转到无权限页面
			flag = false;
		}else{
			String url = code;
			if(url.indexOf(".do")!=-1){
				url = url.split(".do")[0];
			}
			if(mf.contains(url)){
				flag = true;
			}
		}
		return flag ;
	}

	
	/**获取当前登录的用户名
	 * @return
	 */
	public static String getUsername(){
		return getSession().getAttribute(Const.SESSION_USERNAME).toString();
	}
	
	
	/**shiro管理的session
	 * @return
	 */
	public static Session getSession(){
		//Subject currentUser = SecurityUtils.getSubject();  
		return SecurityUtils.getSubject().getSession();
	}
	
	
	public static boolean checkRights(String url,List<String> validMenuFunCodes){
		boolean result = false;
		if(Tools.isEmpty(url) || CollectionUtils.isEmpty(validMenuFunCodes)){
			return result;
		}
		String newUrl = url.split(".do")[0];
		for(String funCode:validMenuFunCodes){
			if(newUrl.equals(funCode)){
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 获取当前用户最大角色类型
	 * @return
	 */
	public static int getUserMaxRoles() {
		return (Integer)getSession().getAttribute(getUsername()+Const.SESSION_USER_MAX_ROLE);
	}
	
	/**获取当前登录的用户
	 * @return
	 */
	public static User getCurrentUser(){
		return (User)getSession().getAttribute(Const.SESSION_USER);
	}
}
