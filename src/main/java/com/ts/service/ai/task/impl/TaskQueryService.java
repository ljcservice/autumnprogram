package com.ts.service.ai.task.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.entity.system.Role;
import com.ts.plugin.MTSHttpService;
import com.ts.plugin.TsHttpClient;
import com.ts.service.ai.impl.AIService;
import com.ts.service.ai.task.manager.TaskQueryManager;
import com.ts.service.ontology.impl.CommonService;
import com.ts.service.system.role.impl.RoleService;
import com.ts.util.DbFH;
import com.ts.util.Jurisdiction;
import com.ts.util.Logger;
import com.ts.util.PageData;
import com.ts.util.UuidUtil;
import com.ts.util.ai.AICommMethod;
import com.ts.util.ai.AIConst;
import com.ts.util.ai.AIRulesTool;
import com.ts.util.ontology.OsynConst;

/**
 * 任务查询
 * @ClassName: TaskQueryService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年11月9日 下午3:29:34 
 *
 */
@Service("taskQueryService")
public class TaskQueryService implements TaskQueryManager{

	@Resource(name = "sqlSessionTemplate_PDSS")
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;

	@Resource(name="aiService")
	private AIService aiService;
	
	@Resource(name="roleService")
	private RoleService roleService;

	@Resource(name="commonService")
	private CommonService commonService;
	
	protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 获取任务的列表
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> taskList(Page page) throws Exception{
		List<PageData> list=new ArrayList<PageData>();
		PageData pd=page.getPd();
		int task_type_id=Integer.valueOf(pd.get("TASK_TYPE_ID").toString());
		int task_type_child_id=Integer.valueOf(pd.get("TASK_TYPE_CHILD_ID").toString());
		aiService.quesInfo(task_type_id, task_type_child_id, pd);
		page.setPd(pd);
		list=(List<PageData>)dao.findForList("TaskQueryMapper.tasklistPage", page);
		return list;
	}
	/**
	 * 待处理任务
	 */
	@Override
	public List<PageData> taskList4Ctrl(Page page) throws Exception {
		return (List<PageData>)dao.findForList("TaskQueryMapper.4CtrlTasklistPage", page);
	}
	/**
	 * 根据ID获取任务的信息
	 * @Title: getTask 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param pd
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public PageData getTask(String task_id) throws Exception{
		return (PageData)dao.findForObject("TaskQueryMapper.getTask", Integer.valueOf(task_id));
	}
	
	/**
	 * 获取问题单的信息根据ID
	 */
	public PageData getQ(int q_id) throws Exception{
		return (PageData)dao.findForObject("TaskAllotMapper.findDiagQ", q_id);
	}
	/**
	 * 获取子问题单的信息根据ID
	 */
	public PageData getChildQ(int q_id) throws Exception{
		return (PageData)dao.findForObject("TaskAllotMapper.findDiagChildQByID", q_id);
	}
	
	/**
	 * 得到AI干预的结果 根据任务的ID信息
	 * @Title: getOneRes 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param task_id
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return PageData    返回类型 
	 * @throws
	 */
	public PageData getRes(String task_id)throws Exception{
		PageData rs=(PageData) dao.findForObject("TaskQueryMapper.getResByTask",Integer.valueOf(task_id));
		//设置标准词信息
		if(AICommMethod.isNotEmpty(rs)&&AICommMethod.isNotEmpty(rs.get("STD_ID"))){
			String STD_CN="";
			String STD_CODE="";
			String[] stds=rs.get("STD_ID").toString().split(",");//多个标准词用","分隔
			for (String string : stds) {
				PageData qpd=new PageData();
				qpd.put("STAD_DN_ID", string);
				PageData ontoPd=(PageData)dao.findForObject("AIMapper.getOnDiagByStd", qpd);
				if(AICommMethod.isEmpty(ontoPd)){
					STD_CN=STD_CN==""?"本体数据有误!":STD_CN+",本体数据有误!";
					STD_CODE=STD_CODE==""?"本体数据有误!":STD_CODE+",本体数据有误!";
				}else{
					STD_CN=STD_CN==""?AICommMethod.nvl(ontoPd.get("STAD_DN_CHN")):STD_CN+","+AICommMethod.nvl(ontoPd.get("STAD_DN_CHN"));
					STD_CODE=STD_CODE==""?AICommMethod.nvl(ontoPd.get("MAIN_CODE")):STD_CODE+","+AICommMethod.nvl(ontoPd.get("MAIN_CODE"));
				}
			}
			rs.put("STD_CN", STD_CN);
			rs.put("STD_CODE", STD_CODE);
		}
		return rs;
	}
	
	/**
	 * 根据任务的ID信息获取NLP结果信息
	 * @Title: getSplitRs 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param task_id
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return PageData    返回类型 
	 * @throws
	 */
	public PageData getSplitRs(String task_id)throws Exception{
		return (PageData) dao.findForObject("TaskQueryMapper.getSplitResByTask",Integer.valueOf(task_id));
	}
	
	/**
	 * 获取一审的处理结果
	 * @Title: getOneRes 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param task_id
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return List<PageData>    返回类型 
	 * @throws
	 */
	public List<PageData> getOneRes(String task_id)throws Exception{
		List<PageData> rsList= (List<PageData>) dao.findForList("TaskQueryMapper.getOneResByTask", Integer.valueOf(task_id));
		for (PageData rs : rsList) {
			//设置标准词信息
			if(AICommMethod.isNotEmpty(rs)&&AICommMethod.isNotEmpty(rs.get("STD_ID"))){
				String STD_CN="";
				String STD_CODE="";
				String[] stds=rs.get("STD_ID").toString().split(",");//多个标准词用","分隔
				for (String string : stds) {
					PageData qpd=new PageData();
					qpd.put("STAD_DN_ID", string);
					PageData ontoPd=(PageData)dao.findForObject("AIMapper.getOnDiagByStd", qpd);
					if(AICommMethod.isEmpty(ontoPd)){
						STD_CN=STD_CN==""?"本体数据有误!":STD_CN+",本体数据有误!";
						STD_CODE=STD_CODE==""?"本体数据有误!":STD_CODE+",本体数据有误!";
					}else{
						STD_CN=STD_CN==""?AICommMethod.nvl(ontoPd.get("STAD_DN_CHN")):STD_CN+","+AICommMethod.nvl(ontoPd.get("STAD_DN_CHN"));
						STD_CODE=STD_CODE==""?AICommMethod.nvl(ontoPd.get("MAIN_CODE")):STD_CODE+","+AICommMethod.nvl(ontoPd.get("MAIN_CODE"));
					}
				}
				rs.put("STD_CN", STD_CN);
				rs.put("STD_CODE", STD_CODE);
			}
		}
		return rsList;
		
	}
	/**
	 * 保存任务处理的结果历史信息-诊断
	 * @Title: saveTaskHis 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param pd
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void saveTaskHis(PageData pd)throws Exception{
		logger.info(Jurisdiction.getUsername()+"保存任务处理的历史信息，若已经存在则先删除，TASK_ID:"+pd.get("task_id"));
		//根据任务的ID删除任务的处理信息
		dao.delete("TaskQueryMapper.deleteRsByTaskId", Integer.valueOf(pd.get("task_id").toString()));
		//保存任务的处理信息
		dao.save("TaskQueryMapper.saveTaskHis", pd);
		//更新任务的状态信息为已处理
		pd.put("TASK_STAT", 1);
		dao.update("TaskQueryMapper.updateTaskStat", pd);
		logger.info(Jurisdiction.getUsername()+"更新任务的状态信息为'已经处理'，TASK_ID:"+pd.get("task_id"));
		
	}
	/**
	 * 保存NLP处理的结果
	 * @Title: saveNLPRs 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param pd
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void saveNLPRs(PageData pd)throws Exception{
		logger.info(Jurisdiction.getUsername()+"保存NLP审核任务处理处理的信息，若已经存在则先删除，TASK_ID:"+pd.get("task_id"));
		//根据任务的ID删除任务的处理信息
		dao.delete("TaskQueryMapper.deleteSplitRsByTaskId", Integer.valueOf(pd.get("task_id").toString()));
		//保存任务的处理信息
		dao.save("TaskQueryMapper.saveNLPRs", pd);
		//更新任务的状态信息为已处理
		pd.put("TASK_STAT", 1);
		dao.update("TaskQueryMapper.updateTaskStat", pd);
		logger.info(Jurisdiction.getUsername()+"更新任务的状态信息为'已处理'，TASK_ID:"+pd.get("task_id"));
	}
	/**
	 * 删除任务的处理结果
	 * @Title: deleteRs 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param HIS_RS_ID
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void deleteTaskHis(PageData pd)throws Exception{
		logger.info(Jurisdiction.getUsername()+"删除任务的处理的历史结果信息，HIS_RS_ID:"+pd.get("HIS_RS_ID"));
		dao.delete("TaskQueryMapper.deleteRs", Integer.valueOf(pd.get("HIS_RS_ID").toString()));
		//更新任务的状态信息为未处理
		pd.put("TASK_STAT", 0);
		dao.update("TaskQueryMapper.updateTaskStat", pd);
		logger.info(Jurisdiction.getUsername()+"更新任务的状态信息为'未处理'，TASK_ID:"+pd.get("task_id"));
	}
	/**
	 * 同意一审的处理信息
	 * @Title: agreeTaskHis 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param pd
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	
	public void agreeTaskHis(PageData pd)throws Exception{
		//根据任务的ID删除任务的处理信息
		dao.delete("TaskQueryMapper.deleteRsByTaskId", Integer.valueOf(pd.get("task_id").toString()));
		//保存任务的处理信息根据一审的处理结果
		dao.save("TaskQueryMapper.saveTaskHisByOne", pd);
		//更新任务的状态信息为已经处理
		pd.put("TASK_STAT", 1);
		dao.update("TaskQueryMapper.updateTaskStat", pd);
	} */
//	public void submitTaskAll(String[] ArrayTASK_IDS,String STEP)throws Exception{
//		//更新任务的状态为"已提交"
//		dao.update("TaskQueryMapper.updateAllTask", ArrayTASK_IDS);
//		//更新任务操作历史结果状态信息为"已提交"
//		dao.update("TaskQueryMapper.updateAllHisRs",ArrayTASK_IDS);
//	}
	/**
	 * 批量提交任务信息
	 * @Title: deleteAllTP 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param P_IDS
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public String submitTaskAll(String[] ArrayTASK_IDS,Logger logger)throws Exception{
		String flag="success";
		SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
		//批量执行器
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
		try{
		
			logger.info(Jurisdiction.getUsername()+"提交任务信息开始!");
			
			//二审任务提交：遍历任务的处理历史信息
//			if("2".equals(STEP)){
				for (String TASK_ID : ArrayTASK_IDS) {
					//获取任务的信息
					logger.info(Jurisdiction.getUsername()+"提交任务，TASK_ID："+TASK_ID);
					PageData task=(PageData)dao.findForObject("TaskQueryMapper.getTask", Integer.parseInt(TASK_ID));
					String STEP=task.get("STEP").toString();
					if("2".equals(STEP)){
						int task_type_id=Integer.valueOf(task.get("TASK_TYPE_ID").toString());
						int task_type_id_child=Integer.valueOf(task.get("TASK_TYPE_CHILD_ID").toString());
						switch (task_type_id) {
						case AIConst.AI_TASK_DIAG:
							switch (task_type_id_child) {
							case AIConst.AI_TASK_CHILD_DIAG://诊断干预
								submitDiagTwo(sqlSession, TASK_ID);
								break;
							case AIConst.AI_TASK_CHILD_NLP://NLP审核
								//生成子问题单，更新问题单的状态为已分解
								submitNLP(sqlSession,TASK_ID);
								break;
							}
							break;
	
						default:
							break;
						}
					}
				}
//			}
			//更新任务操作历史结果状态信息为"已提交"，若为NLP则不会存在干预历史信息
			sqlSession.update("TaskQueryMapper.updateAllHisRs",ArrayTASK_IDS);
			//更新任务的状态为"已提交"
			sqlSession.update("TaskQueryMapper.updateAllTask", ArrayTASK_IDS);
			logger.info(Jurisdiction.getUsername()+"更新任务的状态信息为'已处理'，ArrayTASK_IDS:"+ArrayTASK_IDS);
			sqlSession.flushStatements();
			sqlSession.commit();
			sqlSession.clearCache();
	}catch(Exception e){
		logger.info("提交任务失败:"+e.getMessage());
		flag=e.getMessage();
		sqlSession.rollback();
		sqlSession.clearCache();
	}finally{
		logger.info("提交任务结束");
		sqlSession.close();
	}
		return flag;
		
	}
	/**
	 * 诊断干预二审的任务提交-更新本体
	 * @Title: submitDiagTwo 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param sqlSession
	 * @param @param TASK_ID
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void submitDiagTwo(SqlSession sqlSession,String TASK_ID)throws Exception{
		logger.info(Jurisdiction.getUsername()+"提交诊断二审的任务，TASK_ID："+TASK_ID);
		String user_id=Jurisdiction.getCurrentUser().getUSER_ID();
		//1、将任务的处理信息更新到本体信息中2、将任务处理信息保存到问题单处理结果中
		PageData hisRes=(PageData) dao.findForObject("TaskQueryMapper.getResByTaskID", Integer.valueOf(TASK_ID));//获取任务的处理信息
		Object Q_ID=hisRes.get("CHILD_ID");//诊断的子问题单ID
		Object DIAG_WORD=hisRes.get("DIAG_WORD");//处理的诊断词
		Object DIS_TYPE=hisRes.get("DIS_TYPE");//干预类型
//		Object DEAL_FLG=hisRes.get("DEAL_FLG");//处理的状态
		Object STD_ID=hisRes.get("STD_ID");//标准词ID，此处可能为多个用","分隔
		Object SYN_TYPE=hisRes.get("SYN_TYPE");//同义词类型
		Object TERM_TYPE=hisRes.get("TERM_TYPE");//术语类型
		Object MEMO=hisRes.get("MEMO");//备注
		int res_id=0;//诊断的干预结果ID，同义词则为标准词对应的本体的ID，下位词则为增加的本体的ID，无法干预则为增加的无法干预的ID
		
		PageData mts_pd=new PageData();//MTS更新实体
		
		PageData pd=new PageData();
		switch (Integer.valueOf(DIS_TYPE.toString())) {
		case 1://同义词
			int dn_id=commonService.querySeqValue("seq_on_diag_name");//获取序列
			pd.put("DN_ID", dn_id);
			pd.put("DN_CHN",DIAG_WORD );
			pd.put("DN_ENG", "");
			pd.put("STAD_DN_ID",STD_ID );
			pd.put("TERM_TYPE",TERM_TYPE );
			pd.put("SYNO_TYPE", SYN_TYPE);
			pd.put("ORG_DN_CHN","" );
			pd.put("ORG_DN_ENG", "");
			pd.put("IS_DISABLE", 0);
			pd.put("DESCRIPTION", "");
			pd.put("VERSION","" );
			pd.put("UPDATE_MAN",user_id );
			pd.put("UPDATE_TIME",new Date() );
			pd.put("OPERATION","诊断人工干预更新本体-增加同义词" );
			addDiagName(pd, sqlSession);
			PageData on_d=(PageData)dao.findForObject("TaskQueryMapper.onDiagByDnId", STD_ID.toString());//本体信息
			res_id=Integer.valueOf(on_d.get("D_ID").toString());
			
			//设置更新MTS信息
			mts_pd.put("DIAG_NAME", pd.get("DN_CHN"));
			mts_pd.put("STAD_DIAG_NAME", on_d.get("STAD_DN_CHN"));
			mts_pd.put("ADD_CODE",AICommMethod.isEmpty(on_d.get("ADD_CODE"))?" ":on_d.get("ADD_CODE").toString() );
			mts_pd.put("MAIN_CODE",AICommMethod.isEmpty(on_d.get("MAIN_CODE"))?" ":on_d.get("MAIN_CODE").toString() );
			
			logger.info(Jurisdiction.getUsername()+"提交诊断二审的任务-诊断人工干预更新本体-增加同义词"+DIAG_WORD);
			break;
		case 2://下位词
			//增加标准词
			int stad_dn_id=commonService.querySeqValue("seq_on_diag_name");//获取序列
			pd.put("DN_ID", stad_dn_id);
			pd.put("DN_CHN",DIAG_WORD);
			pd.put("DN_ENG", "");
			pd.put("STAD_DN_ID",stad_dn_id );
			pd.put("TERM_TYPE",TERM_TYPE );
			pd.put("SYNO_TYPE", "");
			pd.put("ORG_DN_CHN","" );
			pd.put("ORG_DN_ENG", "");
			pd.put("IS_DISABLE", 0);
			pd.put("DESCRIPTION", "");
			pd.put("VERSION","" );
			pd.put("UPDATE_MAN",user_id );
			pd.put("UPDATE_TIME",new Date() );
			pd.put("OPERATION","诊断人工干预更新本体-增加标准词" );
			addDiagName(pd, sqlSession);//保存标准词信息
			logger.info(Jurisdiction.getUsername()+"提交诊断二审的任务-诊断人工干预更新本体-增加标准词"+DIAG_WORD);
			//增加本体，首先校验标准词对应的本体是否存在，不存在则写入
			PageData ontoPd=(PageData)dao.findForObject("AIMapper.getOnDiagByStd", pd);
			int d_id=0;
			if(ontoPd!=null){
				d_id=Integer.valueOf(ontoPd.get("D_ID").toString());
			}else{
				ontoPd=new PageData();
				d_id=commonService.querySeqValue("seq_on_diag");//获取序列
				ontoPd.put("D_ID", d_id);
				ontoPd.put("DN_ID", pd.get("STAD_DN_ID"));
				ontoPd.put("STAD_DN_CHN",DIAG_WORD );
				ontoPd.put("STAD_DN_ENG","" );
				ontoPd.put("ORG_STAD_DN_CHN", "");
				ontoPd.put("ORG_STAD_DN_ENG","" );
				ontoPd.put("TERM_TYPE",TERM_TYPE );
				ontoPd.put("TERM_DEFIN","" );
				ontoPd.put("DEP_CATEGORY","" );
				ontoPd.put("PART_CATEGORY","" );
				ontoPd.put("MAN_CATEGORY", "");
				ontoPd.put("DIS_CATEGORY","" );
				ontoPd.put("IS_CHRONIC", "");
				
				ontoPd.put("IS_DISABLE", 0);
				ontoPd.put("DESCRIPTION","" );
				ontoPd.put("VERSION","" );
				ontoPd.put("OPERATION","诊断人工干预更新本体-增加本体" );
				ontoPd.put("UPDATE_MAN",user_id );
				ontoPd.put("UPDATE_TIME", new Date());
				
				//根据标准词的ID获取上位词本体的ID
//				List<PageData> swc_list=(List<PageData>)dao.findForList("TaskQueryMapper.onDiagByDnId", STD_ID.toString());
				String[] parentIds=getParentIDS(STD_ID.toString());
				
				//生产一个主要编码和附加编码
				commonService.setMainAddCode(OsynConst.DIAG,ontoPd,parentIds);
				
				sqlSession.insert("OntologyMapper.diagInsertOntoByHis",ontoPd);//保存本体的信息
				addToOntoHis(sqlSession, ontoPd, 2);
				logger.info(Jurisdiction.getUsername()+"提交诊断二审的任务-诊断人工干预更新本体-增加本体"+DIAG_WORD);
				
				//设置更新MTS信息
				mts_pd.put("DIAG_NAME", pd.get("DN_CHN"));
				mts_pd.put("STAD_DIAG_NAME", pd.get("DN_CHN"));
				mts_pd.put("ADD_CODE",AICommMethod.isEmpty(ontoPd.get("ADD_CODE"))?" ":ontoPd.get("ADD_CODE").toString() );
				mts_pd.put("MAIN_CODE", AICommMethod.isEmpty(ontoPd.get("MAIN_CODE"))?" ":ontoPd.get("MAIN_CODE").toString() );
				
				//增加树信息,可能为两条树信息
				for (String parentId : parentIds) {
					PageData treePd=new PageData();
					treePd.put("TREE_ID", commonService.querySeqValue("seq_on_diag_tree"));//诊断树序列
					treePd.put("D_ID",d_id );//子节点即增加的本体的信息
					treePd.put("PARENT_ID", Integer.valueOf(parentId));//上位节点的信息
					treePd.put("ONTO_H_ID", ontoPd.get("H_ID"));
					sqlSession.insert("TreeMapper.diagTreeInsert", treePd);//保存树的信息
					addToOntoHis(sqlSession, treePd, 3);
					logger.info(Jurisdiction.getUsername()+"提交诊断二审的任务-诊断人工干预更新本体-增加本体树信息，D_ID："+d_id+",PARENT_ID:"+parentId);
				}
			}
			res_id=d_id;
			break;
		case 3://无法干预
			//校验无用术语表是否存在,不存在则写入
			PageData notermPd=(PageData)dao.findForObject("AIMapper.getNonTermByName", DIAG_WORD);
			if(notermPd==null){
				notermPd=new PageData();
				notermPd.put("NO_TYPE_ID", AIConst.AI_TASK_DIAG);//诊断干预
				notermPd.put("TERM_NAME", DIAG_WORD);
				notermPd.put("TERM_TYPE",TERM_TYPE );
				notermPd.put("UPDATE_MAN",user_id );
				notermPd.put("MEMO",MEMO==null?"":MEMO );
				sqlSession.insert("AIMapper.addNonTerm",notermPd);//保存无法干预
				logger.info(Jurisdiction.getUsername()+"提交诊断二审的任务-诊断人工干预更新本体-增加无法干预："+DIAG_WORD);
			}
			//提交到MTS信息
			mts_pd.put("TERM_NAME",DIAG_WORD );
			
			res_id= Integer.valueOf((notermPd.get("NO_ID").toString()) );
			break;
		}
		
		//将信息增加到MTS中
		String res=MTSHttpService.addToMTS(mts_pd);//增加本体信息
		if(!"1".equals(res))
		{
			logger.error(Jurisdiction.getUsername()+"操作失败：本体更新到MTS失败");
			new Exception(("操作失败：本体更新到MTS失败"));
		}
		
		//保存问题单的处理结果信息
		PageData resPd=new PageData();
		resPd.put("CHILD_ID", Q_ID);
		resPd.put("DIAG_WORD",DIAG_WORD );
		resPd.put("DIAG_ID",res_id );//诊断的处理结果
		resPd.put("DIS_TYPE",DIS_TYPE );
		resPd.put("STAD_ID",STD_ID );
		resPd.put("SYN_TYPE",SYN_TYPE==null?"":SYN_TYPE );
		resPd.put("TERM_TYPE",TERM_TYPE==null?"":TERM_TYPE );
		resPd.put("REMARK", MEMO==null?"":MEMO);
		resPd.put("UPDATE_MAN",user_id );
		sqlSession.insert("AIMapper.addQDiagRs",resPd);
		logger.info(Jurisdiction.getUsername()+"提交诊断二审的任务-保存问题单处理结果信息，Q_ID："+Q_ID+",DIAG_WORD:"+DIAG_WORD);
		//根据二审结果标记一审的采纳标志
		sqlSession.update("TaskQueryMapper.updateAllTaskAdoptFlag", hisRes);
	}
	/**
	 * @throws Exception 
	 * 根据标准词获取本体的ID
	 * @Title: getParentIDS 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param STD_ID ','分隔的字符串
	 * @param @return    设定文件 
	 * @return String[]    返回类型 
	 * @throws
	 */
	public String[] getParentIDS(String STD_ID) throws Exception{
		String[] STD_IDS=STD_ID.split(",");
		String[] rs=new String[STD_IDS.length];
		for (int i = 0; i < STD_IDS.length; i++) {
			PageData diag=(PageData)dao.findForObject("TaskQueryMapper.onDiagByDnId", STD_IDS[i]);
			rs[i]=diag.get("D_ID").toString();
		}
		return rs;
	}
	/**
	 * 提交NLP的处理结果
	 * @Title: submitNLP 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param sqlSession
	 * @param @param TASK_ID    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void submitNLP(SqlSession sqlSession,String TASK_ID)throws Exception{
		logger.info(Jurisdiction.getUsername()+"提交诊断NLP审核任务，TASK_ID"+TASK_ID);
		String user_id=Jurisdiction.getCurrentUser().getUSER_ID();
		PageData splitRes=(PageData) dao.findForObject("TaskQueryMapper.getSplitResByTask", Integer.valueOf(TASK_ID));//获取任务的处理信息
		String RIGHT_DESC=splitRes.getString("RIGHT_DESC");
		String MTS_CODE=splitRes.getString("MTS_CODE");
		String Q_ID=splitRes.get("Q_ID").toString();
		PageData pd=(PageData) dao.findForObject("TaskAllotMapper.findDiagQ", Integer.valueOf(Q_ID));
		String tr_names[]=RIGHT_DESC.split(";");
		String tr_codes[]=MTS_CODE.split(";");
		logger.info(Jurisdiction.getUsername()+"提交诊断NLP审核任务-处理的信息，正确的描述："+RIGHT_DESC+",MTS匹配"+MTS_CODE);
		for (int i = 0; i < tr_names.length; i++) {
			String name=tr_names[i].trim();//名称
			String code=tr_codes[i].trim();//编码
			if(AIConst.AI_MTS_CODE_UNM.equals(code)){
				//校验诊断名称的问题单是否已经存在
				//校验是否存在子问题问题单，存在则建立关联，否则存入子问题单后建立关联
				PageData child_q_pd=new PageData();
				child_q_pd=	(PageData) dao.findForObject("TaskAllotMapper.findDiagChildQ", name);
				if(child_q_pd ==null){
					child_q_pd=new PageData();
					child_q_pd.put("DIAG_NAME", name);
					child_q_pd.put("STATUS", 0);
					child_q_pd.put("TASK_TYPE_ID", AIConst.AI_TASK_DIAG);
					child_q_pd.put("TASK_TYPE_CHILD_ID", AIConst.AI_TASK_CHILD_DIAG);
					child_q_pd.put("CREATE_MAN", user_id);
					child_q_pd.put("UPDATE_MAN", user_id);
					child_q_pd.put("ORIGIN_ID", Long.valueOf(pd.get("ORIGIN_ID").toString()));
					sqlSession.insert("TaskAllotMapper.addChildQ", child_q_pd);
					logger.info(Jurisdiction.getUsername()+"提交诊断NLP审核任务-增加子问题单的信息，DIAG_NAME："+name);
				}
				//写入问题单关系表
				PageData rel_pd=new PageData();
				rel_pd.put("Q_ID", Integer.valueOf(Q_ID));
				rel_pd.put("CHILD_ID", child_q_pd.get("CHILD_ID"));
				rel_pd.put("ORDER",i+1 );//顺序
				rel_pd.put("CREATE_MAN", user_id);
				rel_pd.put("UPDATE_MAN", user_id);
				sqlSession.insert("TaskAllotMapper.addRelQ", rel_pd);
				logger.info(Jurisdiction.getUsername()+"提交诊断NLP审核任务-写入问题单的关联表，Q_ID："+Q_ID+",CHILD_ID："+child_q_pd.get("CHILD_ID")+",ORDER："+i+1);
			}
		}
		//更新问题单的状态信息为已分解
		pd.put("UPDATE_Q_STATUS", 3);
		sqlSession.update("QuestionMapper.updateDiagQ", pd);
		logger.info(Jurisdiction.getUsername()+"提交诊断NLP审核任务-更新问题单的状态为'已分解'，Q_ID："+pd.get("Q_ID"));
	}
	/**
	 * 批量撤销任务信息
	 * @Title: revokeTaskAll 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param ArrayTASK_IDS
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void revokeTaskAll(String[] ArrayTASK_IDS,String query_step,String query_task_type_child_id)throws Exception{
		logger.info(Jurisdiction.getUsername()+"批量撤销任务的信息，"+ArrayTASK_IDS);
		//更新问题单的状态信息
		switch (Integer.valueOf(query_step)) {
		case 1://一审
			//校验任务处理的问题单是否包含其他的任务，若是则状态不变，否则状态变为初始状态
			//由于此处问题单初始及一审的状态不会影响任务的分配及继续处理所以此处暂时不做处理，后期有必要可以增加
			break;

		case 2://二审，将问题单的状态设置为一审
			
			logger.info(Jurisdiction.getUsername()+"批量撤销任务的信息-二审撤销的同时更新问题单的步骤为'一审'或者'初始'");
			if(query_task_type_child_id.equals(String.valueOf(AIConst.AI_TASK_CHILD_NLP))){
				dao.update("QuestionMapper.updateAllDiagQ", ArrayTASK_IDS);
			}
			if(query_task_type_child_id.equals(String.valueOf(AIConst.AI_TASK_CHILD_DIAG))){
				dao.update("QuestionMapper.updateAllDiagChildQ", ArrayTASK_IDS);
			}
			break;
		}
		//将任务删除
		logger.info(Jurisdiction.getUsername()+"批量撤销任务的信息-删除任务信息");
		dao.delete("TaskQueryMapper.deleteAllTask", ArrayTASK_IDS);
	}
	/**
	 * 根据ID获取处理的历史
	 * @Title: getResByID 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param task_id
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return PageData    返回类型 
	 * @throws
	 */
	public PageData getResByID(String HIS_RS_ID)throws Exception{
		return (PageData) dao.findForObject("TaskQueryMapper.getResByID",Integer.valueOf(HIS_RS_ID));
	}
	
	/**
	 * 增加诊断名称：标准词或者同义词
	 * @Title: addDiagName 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param pd
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void addDiagName(PageData pd,SqlSession sqlSession)throws Exception{
		//校验是否已经包含当前名称的同义词
		PageData phave=(PageData)dao.findForObject("AIMapper.checkDiagName", pd);
		if(phave==null){
			sqlSession.insert("OsynMapper.diagInsertOsynName", pd);
			//将同义词信息写入历史记录表
			addToOntoHis(sqlSession, pd, 1);
		}
		else{
			//以下信息在下位词的时候会用到
			pd.put("DN_ID",phave.get("DN_ID") );
			pd.put("STAD_DN_ID",phave.get("STAD_DN_ID") );
		}
	}
	/**
	 * 添加历史信息，包含本体历史，同义词历史，树历史
	 * @Title: addToOntoHis 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param sqlSession
	 * @param @param pd
	 * @param @param op_type 1 同义词 2 本体 3 树
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public void addToOntoHis(SqlSession sqlSession,PageData pd,int op_type){
		switch (op_type) {
		case 1:
			pd.put("H_ID", UuidUtil.get32UUID());
			pd.put("CHECK_USER",pd.get("UPDATE_MAN") );
			pd.put("CHECK_TIME", pd.get("UPDATE_TIME"));
			pd.put("CHECK_MEMO","AI人工干预操作结果" );
			pd.put("OP_TYPE", 0);//操作类型0新增
			pd.put("STATUS",1 );//审核通过
			pd.put("ONTO_TYPE", OsynConst.DIAG);//本体类型
			pd.put("UPD_DESC",pd.get("OPERATION") );//变更备注
			sqlSession.insert("AiAlterNameHistMapper.saveOsynHis",pd);
			break;
		case 2:
			pd.put("H_ID", UuidUtil.get32UUID());
			pd.put("OP_TYPE", 0);//操作类型 0 新增1修改
			pd.put("STATUS", 1);//审核通过
			pd.put("ONTO_TYPE", OsynConst.DIAG);//本体类型
			pd.put("UPD_DESC",pd.get("OPERATION") );//变更备注
			sqlSession.insert("OntologyMapper.saveOntoCopy", pd);
			break;
		case 3:
			pd.put("UPDATE_MAN",Jurisdiction.getCurrentUser().getUSER_ID() );
			pd.put("UPDATE_TIME", new Date());
			pd.put("H_ID", UuidUtil.get32UUID());
			pd.put("UPD_DESC", "诊断人工干预更新本体-增加树信息");
			pd.put("STATUS", 1);//审核通过
			pd.put("OP_TYPE", 0);//操作类型：0新增
			pd.put("ONTO_TYPE", OsynConst.DIAG);
			sqlSession.insert("TreeMapper.saveOntoTreeHis", pd);
			break;	
		default:
			break;
		}
	}
	/**
	 * 校验节点是否在树上
	 * @Title: checkOnTree 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param pd
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public String checkOnTree(PageData pd) throws Exception{
		String res="success";
		List<PageData> list=(List<PageData>)dao.findForList("TaskQueryMapper.getDiagOnTree", pd);
		if(list.isEmpty())
			return "该词不在树节点上，不能扩充下位词!";
		return res;
	}
	/**
	 * 
	 * 多父节点的验证
	 * 
	 * <p> 诊断 1. 诊断最多只能扩展2个编码 (如果2个编码必须1个是主要编码，1个是附加编码) , 父节点肯定不会超过2个， 页面要限制
			   2. 附加编码有M98900/3的格式不扩码， 主要编码末尾可能为+号，附加编码末尾可能是*号  
			   3. 诊断只能挂2个编码 (必须1个是主要编码，1个是附加编码)  或者 1个编码 （ 主要或者附加）, 父节点肯定不会超过2个	
			   4. 只能在6位码下扩 (如果4位码下无子节点，也可以扩，6位码补位00)	
			   5. * + 号必须成对出现,
	 	</p>
	 * @Title: getOntMultiPaChk 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param pd
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public String getOntMultiPaChk(PageData pd) throws Exception{
		String pa_ids =pd.getString("D_IDS");
		if (AICommMethod.isEmpty(pa_ids.toString())) return "请选择节点";
		return chkDiagMultiPa(pd);
	}
	
	public String chkDiagMultiPa(PageData pd) throws Exception {
		String ret = "success";
		String []IDS=pd.getString("D_IDS").split(",");
		pd.put("D_IDS",IDS );
		List<PageData> ontPaList = (List<PageData>)dao.findForList("TaskQueryMapper.onDiagByDIds", IDS);	//获取所有的本体列表
		int mainCodeCnt = 0;
		int addiCodeCnt = 0;
		int forbitCodeCnt = 0;
		String op_main_code = "";
		String op_addi_code = "";
		String ont_id = "";
		boolean isLeaf = false;
		
		if ( AICommMethod.isNotEmpty(ontPaList)) {
			for ( PageData pagedata: ontPaList  ) {
				isLeaf = this.getOntIsLeaf(pagedata);
				String main_code = AICommMethod.nvl(pagedata.get("MAIN_CODE"));								
				String addi_code = AICommMethod.nvl(pagedata.get("ADDI_CODE"));
				op_main_code = AICommMethod.nvl(main_code, op_main_code);
				op_addi_code = AICommMethod.nvl(addi_code, op_addi_code);
				if ( AIRulesTool.isAgDiagExt(pagedata) ) {//亚通扩展的码不允许扩
					// 最后必须跟上  __ + ont_id， 前台校验需要该ID
					return AICommMethod.getS(main_code, addi_code, "__") +"   为亚通扩展的词码，该节点不允许被扩展";
				}
				
				if ( AICommMethod.isNotEmpty(main_code)) {
					int avlen = AIRulesTool.getAvalidIcdCodeLen(main_code) ;
					if ( AIRulesTool.isCodeRange(pagedata) ) {
							return "主要编码为编码范围不能进行扩码";
					}
					else if ( avlen == 6) {// 只能在6位码下扩
						mainCodeCnt ++;
					} 
					else if ( avlen == 4 &&  isLeaf ) {//4位码下没有6位码
						mainCodeCnt ++;
					}
					else if (avlen==3 && isLeaf){//类目下没有6位码
						mainCodeCnt ++;
					}
					else {
						forbitCodeCnt ++;//只能在6位码下扩 (如果4位码下无子节点，也可以扩，6位码补位00)	
						return "父节点主要编码只能为6位码或者3、4位码,3、4位码时必须是叶子节点";
					}
				}
							
				
				//形态学编码判断
				if ( AIRulesTool.isXintaixueCode(addi_code) ) {
					if ( isLeaf ) {//如果父节点下没有编码,则形态学编码码可以扩
						addiCodeCnt ++;
						addi_code = "100";
					}						
				} else if ( AICommMethod.isNotEmpty(addi_code)&&addi_code.indexOf("*") > -1&&AICommMethod.isEmpty(main_code)) {// 父节点为带星号的附加码，不允许增加下位词
					forbitCodeCnt ++;
					return "父节点为带星号的附加码，则必须再选则一个带主要编码的父节点，此处去本体维护进行操作!";
				}else if ( AICommMethod.isNotEmpty(addi_code)) {// 只能在非AG扩展码下扩
					addiCodeCnt ++;
				}
			}
		}
		//诊断只能挂2个编码 (必须1个是主要编码，1个是附加编码)  或者 1个编码 （ 主要或者附加）, 父节点肯定不会超过2个
		if ( mainCodeCnt > 1  ) {
			return "父节点主要编码不能超过2个";
		}
		if ( addiCodeCnt > 1  ) {
			return "父节点附加编码不能超过2个";
		}
		
		if ( AICommMethod.isEmpty(op_main_code) && AICommMethod.isNotEmpty(op_addi_code) ) {
			if ( AIRulesTool.isXintaixueCode(op_addi_code) ) {
				
				return "【M98900/3  M90-99】格式的为形态学编码不能被扩展";
			}			
		}
		//* + 号必须成对出现,  NEED_NEXT,开头，表示前台JS显示提示但不阻止该选择，
		if ( op_main_code.indexOf("+") > -1 && AICommMethod.isEmpty(op_addi_code) ) {
			return "NEED_NEXT,父节点主要编码带+, 需要再指定一个附加编码";
		}
		
		if ( op_addi_code.indexOf("*") > -1 && AICommMethod.isEmpty(op_main_code)  ) {
			return "NEED_NEXT,父节点附加编码带*, 需要再指定一个主要编码";
		}
				
		return ret;
	}
	/**
	 * 判断是否是叶子节点
	 * @Title: getOntIsLeaf 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param pd
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return boolean    返回类型 
	 * @throws
	 */
	public boolean getOntIsLeaf( PageData pd) throws Exception {
		boolean ifLeaf = false;
		
		List<PageData> list=(List<PageData>)dao.findForList("TaskQueryMapper.getDiagTreeByPa", pd) ;
		if ( AICommMethod.isEmpty(list)) {
			ifLeaf = true;
		}			
		return ifLeaf;
	}

	/**
	 * 调用NLP进行切词
	 */
	public String nlpCtrl(String o_name) throws Exception {
		String res="";
    	Properties prop = new Properties();//属性集合对象    
		try {
    	    prop=dao.getAiPp();
    	    String url=prop.getProperty("NLP_URL");//String url = "http://10.10.50.38:10011/";
    	    logger.info("NLP实体识别的NLP_URL=" + url+"需要识别的词s="+o_name);    
	        //返回结果
	        res=AICommMethod.nlpCtrl(url, o_name);
	        logger.info("NLP识别的结果："+res);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
        return res;
	}

}
