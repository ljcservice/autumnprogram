package com.ts.controller.ai.task;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.service.ai.manager.AIManager;
import com.ts.service.ai.task.manager.TaskQueryManager;
import com.ts.service.ontology.manager.OntologyManager;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.ai.AICommMethod;
import com.ts.util.ai.AIConst;
import com.ts.util.ai.AILogs;
import com.ts.util.app.AppUtil;

/**
 * 任务查询
 * @ClassName: TaskQueryController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年11月9日 下午3:27:01 
 *
 */
@Controller
@RequestMapping(value="/taskQuery")
public class TaskQueryController  extends BaseController {
	
	
	@Resource(name="taskQueryService")
	private TaskQueryManager taskQueryService;
	
	@Resource(name="aiService")
	private AIManager aiService;
	
	@Resource(name="ontologyService")
	private OntologyManager ontologyService;
	
	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;
	
	
	
	/**
	 * 任务列表
	 * @Title: listTask 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param page
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/listTask")
	public ModelAndView listTask(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			String login_user_id=Jurisdiction.getCurrentUser().getUSER_ID();
			PageData pd = new PageData();
			pd = this.getPageData();
			String keywords = pd.getString("keywords");				//关键词检索条件
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", "'"+keywords.trim()+"'");
			}
			String ALLOT_START = pd.getString("ALLOT_START");	//开始时间
			String ALLOT_END = pd.getString("ALLOT_END");		//结束时间
			if(ALLOT_START != null && !"".equals(ALLOT_START)){
				pd.put("ALLOT_START", ALLOT_START+" 00:00:00");
			}
			if(ALLOT_END != null && !"".equals(ALLOT_END)){
				pd.put("ALLOT_END", ALLOT_END+" 23:59:59");
			} 
			page.setPd(pd);
			
			if(pd.keySet().isEmpty()){
				pd.put("TASK_TYPE_ID", AIConst.AI_TASK_DIAG);//默认诊断干预
				pd.put("TASK_TYPE_CHILD_ID", AIConst.AI_TASK_CHILD_DIAG);//默认诊断干预
				pd.put("STEP", 1);//审核步骤为一审
				pd.put("TASK_STAT", 0);//未处理
			}
			//校验当前用户的角色，查询任务的类型及子类型，若为管理员则为诊断干预类型的任务信息
			int role=Jurisdiction.getUserMaxRoles();
			
			if(role==0){//管理员
				
			}
			else{//操作员
				pd.put("DUTY_USER_ID", login_user_id);
			}
			
			List<PageData>	taskList = taskQueryService.taskList(page);	
			mv.addObject("taskList", taskList);//列表信息
			
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
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.substring(0, keywords.length()));
			}
			pd.put("login_user_id", login_user_id);
			pd.put("query_task_stat", pd.get("TASK_STAT"));
			pd.put("query_step", pd.get("STEP"));
			pd.put("query_task_type_child_id",pd.get("TASK_TYPE_CHILD_ID") );
			
			pd.put("ALLOT_START", ALLOT_START);
			pd.put("ALLOT_END",ALLOT_END );
			mv.addObject("pd", pd);
			mv.setViewName("ai/task/taskquery");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 * 待处理任务
	 * @Title: listTask4Ctrl 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param page
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/listTask4Ctrl")
	public ModelAndView listTask4Ctrl(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			String login_user_id=Jurisdiction.getCurrentUser().getUSER_ID();
			PageData pd = new PageData();
			pd = this.getPageData();
			String keywords = pd.getString("keywords");				//关键词检索条件
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", "'"+keywords.trim()+"'");
			}
			String ALLOT_START = pd.getString("ALLOT_START");	//开始时间
			String ALLOT_END = pd.getString("ALLOT_END");		//结束时间
			if(ALLOT_START != null && !"".equals(ALLOT_START)){
				pd.put("ALLOT_START", ALLOT_START+" 00:00:00");
			}
			if(ALLOT_END != null && !"".equals(ALLOT_END)){
				pd.put("ALLOT_END", ALLOT_END+" 23:59:59");
			} 
			
			//校验当前用户的角色，查询任务的类型及子类型，若为管理员则为诊断干预类型的任务信息
			int role=Jurisdiction.getUserMaxRoles();
			
			if(role==0){//管理员
				
			}
			else{//操作员
				pd.put("DUTY_USER_ID", login_user_id);
			}
			
			page.setPd(pd);
			
			List<PageData>	taskList = taskQueryService.taskList4Ctrl(page);	
			mv.addObject("taskList", taskList);//列表信息
			
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.substring(0, keywords.length()));
			}
			pd.put("ALLOT_START", ALLOT_START);
			pd.put("ALLOT_END",ALLOT_END );
			pd.put("login_user_id", login_user_id);
			mv.addObject("pd", pd);
			mv.setViewName("ai/task/taskquery4ctrl");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 * 批量提交任务
	 * @Title: submitTaskAll 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/submitTaskAll")
	@ResponseBody
	public Object submitTaskAll() throws Exception {
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"提交任务处理信息",dao, null);
		logBefore(logger, Jurisdiction.getUsername()+"提交任务处理信息");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String TASK_IDS = pd.getString("TASK_IDS");
			if(null != TASK_IDS && !"".equals(TASK_IDS)){
				String ArrayTASK_IDS[] = TASK_IDS.split(",");
				//执行任务提交
				String res=taskQueryService.submitTaskAll(ArrayTASK_IDS,logger);
				map.put("result", res);
			}else{
				map.put("result", "未包含任务信息!");
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
	 * 批量撤销任务
	 * @Title: revokeTaskAll 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/revokeTaskAll")
	@Rights(code="taskQuery/revokeTaskAll")
	@ResponseBody
	public Object revokeTaskAll() throws Exception {
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"批量撤销任务分配信息",dao, null);
		logBefore(logger, Jurisdiction.getUsername()+"批量撤销任务分配信息");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String TASK_IDS = pd.getString("TASK_IDS");
			if(null != TASK_IDS && !"".equals(TASK_IDS)){
				String ArrayTASK_IDS[] = TASK_IDS.split(",");
				String query_task_type_child_id=pd.getString("query_task_type_child_id");//用于更新问题单的状态信息
				String query_step=pd.getString("query_step");
				//执行任务撤销
				taskQueryService.revokeTaskAll(ArrayTASK_IDS,query_step,query_task_type_child_id);
				map.put("result", "success");
			}else{
				map.put("result", "未包含任务信息!");
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
	 * 任务处理对话框
	 * @Title: toCtl 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/toCtl")
	@Rights(code="taskQuery/toCtl")
	public ModelAndView toCtl()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String view_name="taskctl_nlp";
		int EDIT_FLAG=1;//能否编辑的标志
		
		String  nextShow=AICommMethod.nvl(pd.get("nextShow"));//下一条的标志
		String checkFlag=AICommMethod.nvl(pd.get("checkFlag"));//复选框的标志
		String ctrl_flag=AICommMethod.nvl(pd.get("ctrl_flag"));//待处理也入口
		
		
		if(AICommMethod.isNotEmpty(nextShow)){//下一条
			//下一页的选项，根据条件选择第一条在进行当前的处理
			Page page=new Page();
			String keywords = pd.getString("keywords");				//关键词检索条件
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", "'"+keywords.trim()+"'");
			}
			String ALLOT_START = pd.getString("ALLOT_START");	//开始时间
			String ALLOT_END = pd.getString("ALLOT_END");		//结束时间
			if(ALLOT_START != null && !"".equals(ALLOT_START)){
				pd.put("ALLOT_START", ALLOT_START+" 00:00:00");
			}
			if(ALLOT_END != null && !"".equals(ALLOT_END)){
				pd.put("ALLOT_END", ALLOT_END+" 23:59:59");
			} 
			pd.put("DUTY_USER_ID", Jurisdiction.getCurrentUser().getUSER_ID());
			page.setPd(pd);
			
			List<PageData>	list=new ArrayList<>();
			if(AICommMethod.isNotEmpty(ctrl_flag))
				list = taskQueryService.taskList4Ctrl(page);	
			else
				list = taskQueryService.taskList(page);	
			if(!CollectionUtils.isEmpty(list)){
				PageData task = list.get(0);
				//要显示的下一条ID
				pd.put("task_id", task.get("TASK_ID").toString());
				pd.put("task_stat", task.get("TASK_STAT").toString());
			}else{
				//无需要审核的数据，跳转到提示页面
				mv.addObject("msg", "当前查询条件下，没有需要操作的数据！");
				mv.setViewName("save_result");
				return mv;
			}
		}
		
		//NLP处理的相关信息
		List<PageData> list=new ArrayList<>();//原始的NLP处理列表
		List<PageData> task_his_list=new ArrayList<>();//NLP审核处理的Lists
		
		//获取任务的信息，根据任务的类型、子类型、步骤确定任务处理的窗口信息
		String task_id=pd.getString("task_id");
		pd=taskQueryService.getTask(task_id);
		int q_id=Integer.valueOf(pd.get("Q_ID").toString());//问题单的ID
		int task_type_id=Integer.valueOf(pd.get("TASK_TYPE_ID").toString());//任务类型
		int task_type_child_id=Integer.valueOf(pd.get("TASK_TYPE_CHILD_ID").toString());//任务子类型
		int step=Integer.valueOf(pd.get("STEP").toString());//任务步骤信息
		
		String task_stat=pd.get("task_stat").toString();//任务的状态信息，判断按钮的可操作性质
		if("2".equals(task_stat)||"3".equals(task_stat)){
			EDIT_FLAG=0;
		}
		
		//判断操作的页面信息
		switch (task_type_id) {
		case AIConst.AI_TASK_DIAG:
			switch (task_type_child_id) {
			case AIConst.AI_TASK_CHILD_DIAG://诊断干预,一审二审
				view_name="taskctl_one";//页面名称
				//根据问题的ID获取问题单的信息
				PageData child_q_one=taskQueryService.getChildQ(q_id);
				pd.put("DIAG_NAME", child_q_one.get("DIAG_NAME").toString());//页面诊断名称
				pd.put("DIAG_ID", child_q_one.get("CHILD_ID").toString());//页面的诊断的ID
				pd.put("STEP", step);
				break;
			case AIConst.AI_TASK_CHILD_NLP://NLP审核，只有一次审核,为二审
				view_name="taskctl_nlp";//页面名称
				//根据问题单ID获取问题单的信息
				PageData q_pd=taskQueryService.getQ(q_id);
				
				String DIAG_NAME=q_pd.get("O_DIAG_NAME").toString();//原始名称
				pd.put("DIAG_NAME", DIAG_NAME);
				
				//原始的NLP审核的列表，可能为空
				if(AICommMethod.isNotEmpty(q_pd.get("NLP_DIAG_NAME"))){
					String NLP_DIAG_NAME=q_pd.get("NLP_DIAG_NAME").toString();//NLP切词结果，
					String NLP_NAMES[]=NLP_DIAG_NAME.split(";");
					String MTS_DIAG_CODE=(String)q_pd.get("MTS_DIAG_CODE");//MTS匹配结果
					for (int i = 0; i < NLP_NAMES.length; i++) {
						PageData name=new PageData();
						name.put("name", NLP_NAMES[i]);
						if(isNotNull(MTS_DIAG_CODE)){
							String code=MTS_DIAG_CODE.split(";")[i];
							name.put("mts_code",code);
						}else{
							name.put("mts_code", AIConst.AI_MTS_CODE_UNM);
						}
						list.add(name);
					}
				}
				
				//判断当前的任务是否已经包含任务处理信息，若是则将任务的信息列出
				PageData splitRsPd=taskQueryService.getSplitRs(task_id);
				int haveRs=0;
				if(splitRsPd!=null){//拼装结果列表
					haveRs=1;
					String RIGHT_DESC=splitRsPd.getString("RIGHT_DESC");//名称列表
					String trnames[]=RIGHT_DESC.split(";");
					List<PageData> trList=new ArrayList<>();
					
					for (int i = 0; i < trnames.length; i++) {
						String tdname=trnames[i];
						PageData td=new PageData();
						td.put("name", tdname);
						trList.add(td);
					}
					task_his_list.addAll(trList);
				}
				mv.addObject("diagNameList", list);
				mv.addObject("NLPNameList", task_his_list);
				mv.addObject("haveRs",haveRs);//标志是否包含处理结果的状态，1包含0不包含
				break;
			}
			break;

		default:
			break;
		}
		
		pd.put("EDIT_FLAG", EDIT_FLAG);
		if(AICommMethod.isNotEmpty(nextShow)){
			pd.put("nextShow", Integer.valueOf(nextShow));
		}
		if(AICommMethod.isNotEmpty(checkFlag))
			pd.put("checkFlag", Integer.valueOf(checkFlag));
		if(AICommMethod.isNotEmpty(ctrl_flag))
			pd.put("ctrl_flag", Integer.valueOf(ctrl_flag));
		mv.setViewName("ai/task/"+view_name);
		mv.addObject("pd", pd);
		return mv;
	}
	/**
	 * 查询-同义词标准词列表
	 */
	@RequestMapping(value="/ontoDiagList")
	public ModelAndView ontoDiagList(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			if(pd.get("TYPE")!=null&&"0".equals(pd.get("TYPE").toString().trim())){//需要转码
				String diag_name=new String(pd.getString("DIAG_NAME").getBytes("ISO-8859-1"),"UTF-8").trim();
				pd.put("hid_diag_name",diag_name);
				pd.put("DIAG_NAME",diag_name);
			}
			String query_type=pd.getString("query_type");
			String tree_id=pd.getString("tree_id");
			page.setPd(pd);
			List<PageData> list = null;
			if(AICommMethod.isNotEmpty(query_type)&&"1".equals(query_type)){//关键词查询
				list = aiService.ontologyDiagPage(page);
			}else if(AICommMethod.isNotEmpty(query_type)&&"2".equals(query_type)){//NLP查询
				list=aiService.ontologyDiagByNlpPage(page);
			}else if(AICommMethod.isNotEmpty(tree_id)){//树查询
				list = aiService.ontologyDiagByTreePage(page);
			}else{//默认查询
				list = aiService.ontologyDiagPage(page);
			}
			page.setPd(pd);
			page.setWindowType(1);
			
			mv.setViewName("ai/task/onto_diag_List");
			mv.addObject("resultList",list);
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 * 任务处理结果信息
	 */
	@RequestMapping(value="/diagRs")
	public ModelAndView diagRs(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			String task_id=pd.getString("TASK_ID");
			
			page.setPd(pd);
			page.setWindowType(1);
			List<PageData> resList = new ArrayList<>();
			//查询是否已经包含处理结果信息
			PageData res=taskQueryService.getRes(task_id);
			if(res!=null&&!res.isEmpty())
				resList.add(res);
			mv.addObject("resList", resList);
			
			mv.setViewName("ai/task/diag_rs");
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 * 一审处理结果信息
	 */
	@RequestMapping(value="/diagOneRs")
	public ModelAndView diagOneRs(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			String task_id=pd.getString("TASK_ID");
			
			page.setPd(pd);
			page.setWindowType(1);
			List<PageData> oneResList = new ArrayList<>();
			//查询是否已经包含处理结果信息
			oneResList=taskQueryService.getOneRes(task_id);
			mv.addObject("oneResList", oneResList);
			
			mv.setViewName("ai/task/diag_one_rs");
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 * 按钮-无法干预
	 * @Title: toNonTerm 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/toNonTerm")
	@Rights(code="taskQuery/toNonTerm")
	public ModelAndView toNonTerm()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("DIAG_NAME", new String(pd.getString("DIAG_NAME").getBytes("ISO-8859-1"),"UTF-8").trim());
		pd.put("dt_id",AIConst.AI_DICT_DIAG_NONTERM );
		List<PageData>	nonTermTypeList = aiService.dictList(pd);
		
		mv.setViewName("ai/task/diag_nonterm");
		mv.addObject("nonTermTypeList", nonTermTypeList);
		mv.addObject("pd", pd);
		return mv;
	}
	/**
	 * 按钮-同义词
	 * @Title: toTYC 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/toTYC")
	@Rights(code="taskQuery/toTYC")
	public ModelAndView toTYC()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("DIAG_NAME", new String(pd.getString("DIAG_NAME").getBytes("ISO-8859-1"),"UTF-8").trim());
		pd.put("STAD_CN", new String(pd.getString("STAD_CN").getBytes("ISO-8859-1"),"UTF-8").trim());
		
		pd.put("dt_id",AIConst.AI_DICT_DIAG_TYC );//同义词类型
		List<PageData>	tycTypeList = aiService.dictList(pd);
		pd.put("dt_id",AIConst.AI_DICT_DIAG_SY );//术语类型
		List<PageData>	syTypeList = aiService.dictList(pd);
		mv.addObject("tycTypeList", tycTypeList);
		mv.addObject("syTypeList", syTypeList);
		
		mv.setViewName("ai/task/diag_tyc");
		mv.addObject("pd", pd);
		return mv;
	}
	/**
	 * 按钮-下位词
	 * @Title: toXWC 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/toXWC")
	@Rights(code="taskQuery/toXWC")
	public ModelAndView toXWC()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("DIAG_NAME", new String(pd.getString("DIAG_NAME").getBytes("ISO-8859-1"),"UTF-8").trim());
		//pd.put("STAD_CN", new String(pd.getString("STAD_CN").getBytes("ISO-8859-1"),"UTF-8").trim());
		
		String STAD_CNS="";
		String STAD_IDS="";
		//此处只传递IDS，多个上位词的本体ID信息用","DN_ID  STAD_DN_CHN
		String []IDS=pd.getString("D_IDS").split(",");
		pd.put("D_IDS",IDS );
		List<PageData> ontPaList = (List<PageData>)dao.findForList("TaskQueryMapper.onDiagByDIds", IDS);	//获取所有的上位词
		for (PageData pageData : ontPaList) {
			if(AICommMethod.isEmpty(STAD_IDS)){
				STAD_IDS=pageData.get("DN_ID").toString();
				STAD_CNS=pageData.get("STAD_DN_CHN").toString();
			}else{
				STAD_IDS=STAD_IDS+","+pageData.get("DN_ID").toString();
				STAD_CNS=STAD_CNS+","+pageData.get("STAD_DN_CHN").toString();
			}
		}
		pd.put("STAD_IDS", STAD_IDS);
		pd.put("STAD_CNS", STAD_CNS);
		pd.put("dt_id",AIConst.AI_DICT_DIAG_SY );//术语类型
		List<PageData>	syTypeList = aiService.dictList(pd);
		mv.addObject("syTypeList", syTypeList);
		
		mv.setViewName("ai/task/diag_xwc");
		mv.addObject("pd", pd);
		return mv;
	}
	/**
	 * 保存诊断干预的结果
	 * @Title: saveDiagRs 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param page
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/saveDiagRs")
	@Rights(code="taskQuery/saveDiagRs")
	public ModelAndView saveDiagRs(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			AILogs.saveOpLogs(Jurisdiction.getUsername()+"保存诊断"+pd.get("DIAG_NAME")+"干预结果",dao, null);
			logBefore(logger, Jurisdiction.getUsername()+"保存诊断"+pd.get("DIAG_NAME")+"干预结果");
			
			pd.put("DEAL_FLG", 1);
			pd.put("UPDATE_MAN", Jurisdiction.getCurrentUser().getUSER_ID());
			
			//保存任务的处理
			taskQueryService.saveTaskHis(pd);
			
			mv.addObject("msg","success");//执行保存
		} catch (Exception e) {
			mv.addObject("msg","failed");
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	/**删除当前的处理结果
	 * @param out
	 */
	@RequestMapping(value="/deleteRs")
	public void deleteRs(PrintWriter out)throws Exception{
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"删除干预结果",dao, null);
		logBefore(logger, Jurisdiction.getUsername()+"删除当前的干预结果");
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			taskQueryService.deleteTaskHis(pd);
			out.write("success");
			out.close();
		} catch(Exception e){
			logger.error(e.toString(), e);
			out.write("faild");
			out.close();
		}
	}
	/**
	 * 同意一审的处理结果
	 * @Title: agreeRs 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param page
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return ModelAndView    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/agreeRs")
	public void agreeRs(PrintWriter out)throws Exception{
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"同意一审的干预结果",dao, null);
		logBefore(logger,Jurisdiction.getUsername()+"同意一审的干预结果");
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			//获取一审的处理结果，已经设置任务信息TASK_ID
			PageData oneRes=taskQueryService.getResByID(pd.get("HIS_RS_ID").toString());
			if(oneRes==null){
				new Exception("未找到一审的处理结果");
			}
			Object CHILD_ID= oneRes.get("CHILD_ID");
			Object DIS_TYPE=oneRes.get("DIS_TYPE");
			Object STD_ID=oneRes.get("STD_ID");
			Object SYN_TYPE=oneRes.get("SYN_TYPE");
			Object TERM_TYPE=oneRes.get("TERM_TYPE");
			
			if(CHILD_ID!=null)
				pd.put("Q_ID", Integer.valueOf(CHILD_ID.toString()));
			
			if(DIS_TYPE!=null)
				pd.put("DIS_TYPE", Integer.valueOf(DIS_TYPE.toString()));
			
			if(STD_ID!=null)
				pd.put("STD_ID",STD_ID.toString() );
			
			if(SYN_TYPE!=null)
				pd.put("SYN_TYPE",Integer.valueOf(SYN_TYPE.toString() ));
			
			if(TERM_TYPE!=null)
				pd.put("TERM_TYPE",Integer.valueOf(TERM_TYPE.toString() ));
			pd.put("DIAG_NAME", oneRes.get("DIAG_WORD"));
			pd.put("MEMO", oneRes.get("MEMO"));
			pd.put("DEAL_FLG", 1);
			pd.put("STEP", 2);
			pd.put("UPDATE_MAN", Jurisdiction.getCurrentUser().getUSER_ID());
			
			taskQueryService.saveTaskHis(pd);
			out.write("success");
			out.close();
		} catch(Exception e){
			logger.error(e.toString(), e);
			out.write("faild");
			out.close();
		}
	}
	/**
	 * @throws Exception 
	 * 保存NLP结果
	 * @Title: saveNLP 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/saveNLP")
	@ResponseBody
	public Object saveNLP() throws Exception{
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"保存NLP的任务处理",dao, null);
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			pd.put("UPDATE_MAN", Jurisdiction.getCurrentUser().getUSER_ID());

			//判断MTS是否包含处理的结果
			String MTS_CODE=pd.getString("MTS_CODE");
			if(AICommMethod.isEmpty(MTS_CODE)){
				//首先根据问题单的ID查询出问题单的相应详细信息,在根据当前的诊断与原始诊断进行比较，若包含原始切分中的诊断词且切分词已经包含MTS处理结果则取原始处理结果
				PageData q_info=taskQueryService.getQ(Integer.valueOf(pd.get("Q_ID").toString()));
				if(AICommMethod.isNotEmpty(q_info.getString("NLP_DIAG_NAME"))){
					String[] nlp_str=q_info.getString("NLP_DIAG_NAME").split(";");//原始的NLP处理结果
					String mts_old=q_info.getString("MTS_DIAG_CODE");//原始的MTS处理结果,可能为空
					
					Map<String,String> m=new HashMap<>();
					if(AICommMethod.isNotEmpty(mts_old)){
						String[] mts_str=mts_old.split(";");
						for (int i = 0; i < nlp_str.length; i++) {
							if(!mts_str[i].equals(AIConst.AI_MTS_CODE_UNM))
								m.put(nlp_str[i], mts_str[i]);//将已经包含MTS处理信息的词放到map中
						}
					}
					String[] nlp_names=pd.getString("RIGHT_DESC").split(";");//当前的处理词
					String mts_now=null;//当前处理词对应的MTS处理结果
					for (String s : nlp_names) {
						//判断是否包含MTS处理的结果
						String mts_val="";
						if(m.containsKey(s)){
							mts_val=m.get(s);
						}else{
							mts_val=AIConst.AI_MTS_CODE_UNM;
						}
						if(mts_now==null){
							mts_now=mts_val;
						}else{
							mts_now=mts_now+";"+mts_val;
						}
					}
					pd.put("MTS_CODE", mts_now);
				}
			}
			
			//保存NLP任务的处理
			taskQueryService.saveNLPRs(pd);
			
		} catch(Exception e){
			errInfo="error";
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	/**
	 * 多父节点的验证
	                   诊断 1. 诊断最多只能扩展2个编码 (如果2个编码必须1个是主要编码，1个是附加编码) , 父节点肯定不会超过2个， 页面要限制
			   2. 附加编码有M98900/3的格式不扩码， 主要编码末尾可能为+号，附加编码末尾可能是*号  
			   3. 诊断只能挂2个编码 (必须1个是主要编码，1个是附加编码)  或者 1个编码 （ 主要或者附加）, 父节点肯定不会超过2个	
			   4. 只能在6位码下扩	
			   5. * + 号必须成对出现,
	 * @Title: getOntMultiPaChk 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param out
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
//	@RequestMapping(value="/getOntMultiPaChk")
//	public void getOntMultiPaChk(PrintWriter out)throws Exception{
//		String ret_flag="1";
//		logBefore(logger,Jurisdiction.getUsername()+"校验父节点的信息");
//		PageData pd = new PageData();
//		try{
//			pd = this.getPageData();
//			//校验当前的节点是否在树上D_ID
//			ret_flag=taskQueryService.checkOnTree(pd);
//			if("1".equals(ret_flag))
//				ret_flag = taskQueryService.getOntMultiPaChk(pd);//校验能否新增节点
//			out.write(ret_flag);//正确的返回1否则返回错误信息
//			out.close();
//		} catch(Exception e){
//			logger.error(e.toString(), e);
//			out.write("faild");
//			out.close();
//		}
//	}
	@RequestMapping(value="/getOntMultiPaChk")
	@ResponseBody
	public Object getOntMultiPaChk() throws Exception{
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"添加下位词，校验父节点的信息",dao, null);
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			//校验当前的节点是否在树上D_ID
			//errInfo=taskQueryService.checkOnTree(pd);
			//if("success".equals(errInfo))
				errInfo = taskQueryService.getOntMultiPaChk(pd);//校验能否新增节点
		} catch(Exception e){
			errInfo="校验能否作为下位词异常!";
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	/**
	 * 调用NLP获取切词的结果
	 * @Title: nlp_ctrl 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	@RequestMapping(value="/nlpCtrl")
	@ResponseBody
	public Object nlpCtrl() throws Exception{
		AILogs.saveOpLogs(Jurisdiction.getUsername()+"调用NLP获取结果",dao, null);
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try{
			pd = this.getPageData();

			//调用NLP进行切词
			String nlpstr= taskQueryService.nlpCtrl(pd.getString("DIAG_NAME"));
			map.put("nlp_str", nlpstr);
			
		} catch(Exception e){
			errInfo=e.getMessage();
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	
}
