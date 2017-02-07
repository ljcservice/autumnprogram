package com.ts.controller.ai.task;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.service.ai.manager.AIManager;
import com.ts.service.ai.task.manager.TaskAllotManager;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.ai.AIConst;
import com.ts.util.ai.AILogs;
import com.ts.util.app.AppUtil;

/**
 * 任务分配
 * @ClassName: TaskAllotController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年10月31日 上午8:59:47 
 *
 */
@Controller
@RequestMapping(value="/taskAllot")
public class TaskAllotController  extends BaseController {
	
	
	@Resource(name="taskAllotService")
	private TaskAllotManager taskAllotService;
	
	@Resource(name="aiService")
	private AIManager aiService;
	
	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;
	
	/**
	 * 问题单列表
	 * @Title: listQuestion 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param page
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/listQuestion")
	public ModelAndView listQuestion(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			String keywords = pd.getString("keywords");				//关键词检索条件
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.trim());
			}
			String createStart = pd.getString("createStart");	//开始时间
			String createEnd = pd.getString("createEnd");		//结束时间
			if(createStart != null && !"".equals(createStart)){
				pd.put("createStart", createStart+" 00:00:00");
			}
			if(createEnd != null && !"".equals(createEnd)){
				pd.put("createEnd", createEnd+" 23:59:59");
			} 
			page.setPd(pd);
			
			if(pd.keySet().isEmpty()){
				pd.put("TASK_TYPE_ID", "84002");
				pd.put("TASK_TYPE_CHILD_ID", "85001");
				pd.put("EXP_ID", 1);
			}
			
			//设置查询条件的值，此处为了防止页面更改查询条件，但是并未做查询的处理
			pd.put("SEARCH_ORIGIN_ID",pd.get("ORIGIN_ID") );
			pd.put("SEARCH_TASK_TYPE_ID",pd.get("TASK_TYPE_ID") );
			pd.put("SEARCH_TASK_TYPE_CHILD_ID",pd.get("TASK_TYPE_CHILD_ID") );
			pd.put("SEARCH_EXP_ID",pd.get("EXP_ID") );
			pd.put("SEARCH_createStart",pd.get("createStart") );
			pd.put("SEARCH_createEnd",pd.get("createEnd") );
			pd.put("SEARCH_keywords",pd.get("keywords") );
			
			List<PageData>	questionList = taskAllotService.questionList(page);	
			mv.addObject("questionList", questionList);//列表信息
			
			//设置任务类型及任务子类型列表
			pd.put("dt_id",AIConst.AI_DICT_TASK_TYPE );
			List<PageData>	taskTypeList = aiService.dictList(pd);	
			pd.put("dt_id",AIConst.AI_DICT_TASK_TYPE_CHILD );
			List<PageData>	taskTypeChildList = aiService.dictList(pd);
			//设置数据来源列表
			pd.put("dt_id",AIConst.AI_DICT_ORIGIN );
			List<PageData>	originList = aiService.dictList(pd);
			
			
			mv.addObject("originList", originList);
			mv.addObject("taskTypeList", taskTypeList);
			mv.addObject("taskTypeChildList", taskTypeChildList);
			pd.put("createStart", createStart);
			pd.put("createEnd",createEnd );
			mv.addObject("pd", pd);
			mv.setViewName("ai/task/taskallot");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 * 任务分配的窗口
	 * @Title: toAdd 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/toAllot")
	@Rights(code="taskAllot/toAllot")
	public ModelAndView toAllot(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		//获取角色配置
		String role=taskAllotService.getRole(pd);
		pd.put("ROLE_ID", role);
		
		page.setPd(pd);
		List<PageData> userList=aiService.listUsersByRole(page);
		mv.addObject("userList",userList);
		
		//
		mv.setViewName("ai/task/taskallot_userlist");
		
		mv.addObject("pd", pd);
		return mv;
	}
	/**
	 * @throws Exception 
	 * 执行任务分配的方法
	 * @Title: allot 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/allot")
	@ResponseBody
	public Object allot() throws Exception{
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"分配任务",dao, null);
		
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			if(taskAllotService.allot(pd) != null){
				errInfo = "error";
			}
			logger.info(Jurisdiction.getUsername()+"分配任务");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	/**跳过NLP审核
	 * @return
	 */
	@RequestMapping(value="/skipNLP")
	@ResponseBody
	public Object skipNLP() throws Exception {
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"跳过NLP审核",dao, null);
		logBefore(logger, Jurisdiction.getUsername()+"跳过NLP审核");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String Q_IDS = pd.getString("Q_IDS");
			if(null != Q_IDS && !"".equals(Q_IDS)){
				String ArrayP_IDS[] = Q_IDS.split(",");
				taskAllotService.skipNLP(ArrayP_IDS);
				map.put("result", "success");
					
			}else{
				map.put("result", "未包含相关信息!");
			}
			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}
}
