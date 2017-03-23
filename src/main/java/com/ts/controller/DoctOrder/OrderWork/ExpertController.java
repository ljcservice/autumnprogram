package com.ts.controller.DoctOrder.OrderWork;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.system.User;
import com.ts.service.DoctOrder.OrderWork.ExpertService;
import com.ts.service.system.user.UserManager;
import com.ts.util.Const;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.app.AppUtil;

@Controller
@RequestMapping(value="/expert")
public class ExpertController extends BaseController{

	@Autowired
	private ExpertService expertService;
	@Autowired
	private UserManager userService;
	
	/**
	 * 专家点评列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/selectExpertList")
	public ModelAndView expertList(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			// 当前登录专家
			User user = getCurrentUser();
			List<PageData>	expertList = expertService.listExperts(page);	//列出专家列表
			mv.addObject("expertList", expertList);
			mv.setViewName("DoctOrder/expert/selectExpertList");
			mv.addObject("pd", pd);
			
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 * 专家点评
	 * @return
	 */
	@RequestMapping(value="/setExpertPatVisit")
	@ResponseBody
	public Object setExpertPatVisit(){
		PageData pd = this.getPageData();
		//设置住院病历为专家点评
		try {
			expertService.updateExpertPatVisit(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "OK";
	}
	
	
	/**显示专家专家列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/listExperts")
	public ModelAndView listUsers(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	expertList = expertService.listExperts(page);	//列出专家列表
		mv.setViewName("DoctOrder/expert/expertList");
		mv.addObject("expertList", expertList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**去新增专家页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toAdd")
	public ModelAndView toAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd =  this.getPageData();
		Page pp = new Page();
		pp.setShowCount(9999);
		pd.put("showAdmin", 1);
		pp.setPd(pd);
		List<PageData>	userList = userService.listUsers(pp);	//列出用户列表
		mv.addObject("userList", userList);
		mv.setViewName("DoctOrder/expert/expertEdit");
		mv.addObject("MSG", "add");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**保存专家
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	public ModelAndView add() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增user");
		ModelAndView mv = this.getModelAndView();
		try {
			mv.setViewName("save_result");
			User user = getCurrentUser();
			PageData pd = this.getPageData();
			PageData exsit = expertService.findExpertById(pd);
			if(exsit!=null && exsit.getString("USER_ID")!=null){
				mv.addObject("msg","该用户已经是专家。无法重复添加。");
				return mv;
			}
			pd.put("UPDATE_USER", user.getUSER_ID());
			pd.put("UPDATE_TIME", new Date());
			expertService.saveExpert(pd);
		} catch (Exception e) {
			mv.addObject("msg","failed");
		}
		return mv;
	}
	
	/**删除专家
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/delete")
	public void deleteU(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除user");
		PageData pd = new PageData();
		pd = this.getPageData();
		expertService.deleteExpert(pd);
		out.write("success");
		out.close();
	}
	/**
	 * 批量删除
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAllU() throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"批量删除user");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String USER_IDS = pd.getString("USER_IDS");
		if(null != USER_IDS && !"".equals(USER_IDS)){
			String ArrayUSER_IDS[] = USER_IDS.split(",");
			expertService.deleteAllExpert(ArrayUSER_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		map.put("result", "success");
		return AppUtil.returnObject(pd, map);
	}
	
	/**去修改专家页面(系统专家列表修改)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toEdit")
	public ModelAndView goEditU(HttpServletResponse response,HttpServletRequest request) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		//根据ID读取
		pd = expertService.findExpertById(pd);
		mv.setViewName("DoctOrder/expert/expertEdit");
		mv.addObject("MSG", "edit");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 修改专家
	 */
	@RequestMapping(value="/edit")
	public ModelAndView editU(HttpServletResponse response,HttpServletRequest request ) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改ser");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			User user = getCurrentUser();
			pd = this.getPageData();
			pd.put("UPDATE_USER", user.getUSER_ID());
			pd.put("UPDATE_TIME", new Date());
			expertService.updateExpert(pd);	//执行修改
			mv.addObject("msg","success");
		} catch (Exception e) {
			mv.addObject("msg","failed");
		}
		mv.setViewName("save_result");
		return mv;
	}
}
