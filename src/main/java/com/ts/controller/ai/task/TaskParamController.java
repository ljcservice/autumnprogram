package com.ts.controller.ai.task;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.entity.ai.TaskParam;
import com.ts.service.ai.manager.AIManager;
import com.ts.service.ai.task.manager.TaskParamManager;
import com.ts.service.system.role.RoleManager;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.ai.AIConst;
import com.ts.util.ai.AILogs;
import com.ts.util.app.AppUtil;

/**
 * 
 * @ClassName: TaskParamController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年10月25日 上午9:41:10 
 *
 */
@Controller
@RequestMapping(value="/taskParam")
public class TaskParamController  extends BaseController {
	
	
	@Resource(name="taskParamService")
	private TaskParamManager taskParamService;
	
	@Resource(name="aiService")
	private AIManager aiService;
	
	@Resource(name="roleService")
	private RoleManager roleService;
	
	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;
	
	/**
	 * 展示任务参数配置的列表页
	 * @Title: listRole 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param page
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/listTaskParams")
	public ModelAndView listTaskParam(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			String keywords = pd.getString("keywords");				//关键词检索条件
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.trim());
			}
			page.setPd(pd);
			List<PageData>	taskParamList = taskParamService.taskParamList(page);	//列出任务参数配置列表
			mv.setViewName("ai/task/taskparam_list");
			mv.addObject("taskParamList", taskParamList);
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 * 新增按钮
	 * @Title: toAdd 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/toAdd")
	@Rights(code="taskParam/toAdd")
	public ModelAndView toAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		//设置任务类型及任务子类型列表
		pd.put("dt_id",AIConst.AI_DICT_TASK_TYPE );
		List<PageData>	taskTypeList = aiService.dictList(pd);	
		pd.put("dt_id",AIConst.AI_DICT_TASK_TYPE_CHILD );
		List<PageData>	taskTypeChildList = aiService.dictList(pd);
		
		mv.addObject("taskTypeList", taskTypeList);
		mv.addObject("taskTypeChildList", taskTypeChildList);
//		mv.addObject("rollList",roleService.listAllRole(string))
		
		mv.setViewName("ai/task/taskparam_edit");
		mv.addObject("MSG", "add");
		mv.addObject("pd", pd);
		return mv;
	}
	/**
	 * 修改按钮
	 * @Title: goEditU 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param response
	 * @param @param request
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/toEdit")
	@Rights(code="taskParam/toEdit")
	public ModelAndView toEdit(HttpServletResponse response,HttpServletRequest request) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		//设置任务类型及任务子类型列表
		pd.put("dt_id",AIConst.AI_DICT_TASK_TYPE );
		List<PageData>	taskTypeList = aiService.dictList(pd);	
		pd.put("dt_id",AIConst.AI_DICT_TASK_TYPE_CHILD );
		List<PageData>	taskTypeChildList = aiService.dictList(pd);
		
		mv.addObject("taskTypeList", taskTypeList);
		mv.addObject("taskTypeChildList", taskTypeChildList);
		
		//根据ID读取信息
		pd = taskParamService.findById(pd);
		
		mv.setViewName("ai/task/taskparam_edit");
		mv.addObject("MSG", "edit");
		mv.addObject("pd", pd);
		return mv;
	}
	/**
	 * 新增保存任务参数配置
	 * @Title: add 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param user
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/add")
	@Rights(code="taskParam/add")
	public ModelAndView add(TaskParam tp) throws Exception{
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"新增任务参数配置",dao, null);
		logBefore(logger, Jurisdiction.getUsername()+"新增任务参数配置");
		ModelAndView mv = this.getModelAndView();
		tp.setCREATE_MAN(Jurisdiction.getCurrentUser().getUSER_ID());
		tp.setCREATE_TIME(new Date());
		tp.setUPDATE_MAN(Jurisdiction.getCurrentUser().getUSER_ID());
		tp.setUPDATE_TIME(new Date());
		try {
			taskParamService.addTP(tp); 
			mv.addObject("msg","success");//执行保存
		} catch (Exception e) {
			mv.addObject("msg","failed");
		}
		mv.setViewName("save_result");
		return mv;
	}
	/**
	 * 修改保存任务参数配置
	 * @Title: editU 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param response
	 * @param @param request
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(HttpServletResponse response,HttpServletRequest request ) throws Exception{
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"修改任务参数配置",dao, null);
		logBefore(logger, Jurisdiction.getUsername()+"修改任务参数配置");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			pd.put("UPDATE_MAN", Jurisdiction.getCurrentUser().getUSER_ID());
			taskParamService.editTP(pd);	//执行修改
			mv.addObject("msg","success");
		} catch (Exception e) {
			mv.addObject("msg","failed");
		}
		mv.setViewName("save_result");
		return mv;
	}
	/**删除
	 * @param out
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out)throws Exception{
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"删除任务参数配置",dao, null);
		logBefore(logger, "删除任务参数配置");
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			taskParamService.deleteTP(pd);
			out.write("success");
			out.close();
		} catch(Exception e){
			logger.error(e.toString(), e);
			out.write("faild");
			out.close();
		}
		
	}
	/**批量删除
	 * @return
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception {
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"批量删除任务参数配置",dao, null);
		logBefore(logger, Jurisdiction.getUsername()+"批量删除任务参数配置");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String P_IDS = pd.getString("P_IDS");
			if(null != P_IDS && !"".equals(P_IDS)){
				String ArrayP_IDS[] = P_IDS.split(",");
				//校验能否都删除
				String checkInfo=taskParamService.deleteAllCheck(ArrayP_IDS);
				if(checkInfo.equals("success")){
					taskParamService.deleteAllTP(ArrayP_IDS);
					map.put("result", "success");
				}
				else
					map.put("result", checkInfo);
					
			}else{
				map.put("result", "未包含删除信息!");
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
	/**
	 * 选择角色列表
	 * @Title: selectRole 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/selectRole")
	@Rights(code="taskParam/selectRole")
	public ModelAndView selectRole(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ROLE_TYPE", 2);
		page.setPd(pd);
		List<PageData> rollList=roleService.listRolesByType(page);
		mv.setViewName("ai/task/taskparam_rolelist");
		mv.addObject("rollList",rollList);
		mv.addObject("pd", pd);
		return mv;
	}
	/**
	 * 根据任务类型及子类型判断是否存在配置
	 * @Title: hasU 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/has")
	@ResponseBody
	public Object has(){
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			if(taskParamService.findByType(Integer.parseInt((String)pd.get("TASK_TYPE_CHILD_ID"))) != null){
				errInfo = "error";
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	/**
	 * 判断是否存在有效的当前配置
	 * @Title: editCan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/editCan")
	@ResponseBody
	public Object editCan(){
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try{
			//判断若为无效记录则首先判断是否已经存在有效的记录，再次判断是否包含任务信息
			pd = this.getPageData();
			errInfo =taskParamService.findEditCan(Integer.parseInt((String)pd.get("P_ID")),(String)pd.get("CTL_TYPE"));
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	
	
}
