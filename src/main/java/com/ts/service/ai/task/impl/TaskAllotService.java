package com.ts.service.ai.task.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.service.ai.task.manager.TaskAllotManager;
import com.ts.util.Jurisdiction;
import com.ts.util.Logger;
import com.ts.util.PageData;
import com.ts.util.ai.AICommMethod;
import com.ts.util.ai.AIConst;

/**
 * 任务分配
 * @ClassName: TaskAllotService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年10月31日 上午9:10:42 
 *
 */
@Service("taskAllotService")
public class TaskAllotService implements TaskAllotManager{

	@Resource(name = "sqlSessionTemplate_PDSS")
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;
	
	protected Logger logger = Logger.getLogger(this.getClass());


	/**
	 * 获取任务的列表
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> questionList(Page page) throws Exception{
		PageData pd=page.getPd();
		List<PageData> list=new ArrayList<PageData>();
		
		if((String.valueOf(AIConst.AI_TASK_DIAG)).equals(pd.get("TASK_TYPE_ID"))){//诊断干预
			if((String.valueOf(AIConst.AI_TASK_CHILD_NLP)).equals(pd.get("TASK_TYPE_CHILD_ID"))){//NLP审核(无审核步骤的限制)
				list=(List<PageData>)dao.findForList("TaskAllotMapper.questionDiaglistPage", page);
			}else{//诊断干预
				list=(List<PageData>)dao.findForList("TaskAllotMapper.questionDiagChildlistPage", page);
			}
		}
		return list;
	}
	/**
	 * 获取当前任务类型对应的角色
	 * @Title: getRole 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param pd
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public String getRole(PageData pd) throws Exception{
		if((String.valueOf((AIConst.AI_TASK_CHILD_NLP)).equals(pd.get("SEARCH_TASK_TYPE_CHILD_ID"))))
		{
			pd.put("SEARCH_EXP_ID", 2);//NLP审核取二审的配置
		}
		return dao.findForObject("TaskAllotMapper.getRoleId", pd).toString();
		
	}
	/**
	 * 执行任务分配的方法
	 * @Title: allot 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param pd
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public String allot(PageData pd) throws Exception{
		logger.info("");
		List<PageData> list=new ArrayList<PageData>();
		String[] users=pd.get("users").toString().split(",");//责任人ID
		String[] q_ids=pd.get("q_ids").toString().split(",");//问题单的ID
		int task_type_id=Integer.valueOf(pd.get("SEARCH_TASK_TYPE_ID").toString());
		int task_type_child_id=Integer.valueOf(pd.get("SEARCH_TASK_TYPE_CHILD_ID").toString());
		int step=Integer.valueOf(pd.get("SEARCH_EXP_ID").toString());
		String userid=Jurisdiction.getCurrentUser().getUSER_ID();
		
		//判断分配类型
		switch (Integer.valueOf(pd.get("allot_type").toString())) {
		case 1://分配选中的问题单
			for (String string : q_ids) {
				for (String string2 : users) {
					PageData pd_task=new PageData();
					pd_task.put("Q_ID", string);
					pd_task.put("USER_ID", string2);
					pd_task.put("TASK_TYPE_ID", task_type_id);
					pd_task.put("TASK_TYPE_CHILD_ID", task_type_child_id);
					pd_task.put("STEP", step);
					pd_task.put("TASK_STAT", 0);
					pd_task.put("ALLOT_USER_ID", userid);
					pd_task.put("UPDATE_Q_STATUS", step);//问题单的状态，一审不更改
					//校验当前的问题单当前步骤是否已经分配给此人，是则不需要再分配
					PageData haveT=(PageData)dao.findForObject("TaskAllotMapper.checkHaveTask", pd_task);
					if(haveT==null)
						list.add(pd_task);
				}
			}
			break;
		case 2://根据隐藏的查询条件查询出的所有的问题单
			List<PageData> querylist=new ArrayList<PageData>();
			
			if((String.valueOf(AIConst.AI_TASK_DIAG)).equals(pd.get("SEARCH_TASK_TYPE_ID"))){//诊断干预
				if((String.valueOf(AIConst.AI_TASK_CHILD_NLP)).equals(pd.get("SEARCH_TASK_TYPE_CHILD_ID"))){//NLP审核(无审核步骤的限制)
					querylist=(List<PageData>)dao.findForList("TaskAllotMapper.questionDiag", pd);
				}else{//诊断干预
					querylist=(List<PageData>)dao.findForList("TaskAllotMapper.questionDiagChild", pd);
				}
			}
			for (PageData pageData : querylist) {
				for (String string2 : users) {
					PageData pd_task=new PageData();
					pd_task.put("Q_ID", pageData.get("Q_NO"));
					pd_task.put("USER_ID", string2);
					pd_task.put("TASK_TYPE_ID", task_type_id);
					pd_task.put("TASK_TYPE_CHILD_ID", task_type_child_id);
					pd_task.put("STEP", step);
					pd_task.put("TASK_STAT", 0);
					pd_task.put("ALLOT_USER_ID", userid);
					pd_task.put("UPDATE_Q_STATUS", step);//问题单的状态，一审不更改
					//校验当前的问题单当前步骤是否已经分配给此人，是则不需要再分配
					PageData haveT=(PageData)dao.findForObject("TaskAllotMapper.checkHaveTask", pd_task);
					if(haveT==null)
						list.add(pd_task);
				}
			}
			break;
		default:
			break;
		}
		//保存任务信息
		String str="TaskAllotMapper.savaTask";
		//保存任务日志
		String str1="AIMapper.savaTaskLog";
		//判断若为二审任务分配则更新问题单的状态为二审，使得问题单不能够再进行任务分配
		String str2=null;
		
		//更新问题的的状态
		if(task_type_id==AIConst.AI_TASK_DIAG){//诊断干预
			if(task_type_child_id==AIConst.AI_TASK_CHILD_NLP){//NLP审核(无审核步骤的限制)
				str2="QuestionMapper.updateDiagQ";
			}else{//诊断干预
				str2="QuestionMapper.updateDiagChildQ";
			}
		}
			
		Tran(str, str1, str2, list);
		return null;
	}
	
	/**
	 * 操作事务控制
	 * @Title: Tran 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param str  保存任务信息
	 * @param @param str1  保存任务日志
	 * @param @param str2  更新问题单的状态信息
	 * @param @param objs
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void Tran(String str,String str1, String str2,List objs)throws Exception {
		SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
		//批量执行器
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
		try{
			if(objs!=null){
				for(int i=0,size=objs.size();i<size;i++){
					PageData pd=(PageData)objs.get(i);
					//插入任务的信息并且返回ID
					sqlSession.insert(str,pd );
					logger.info("插入任务的信息，问题单的ID:"+AICommMethod.nvl(pd.get("Q_ID")));
					//插入任务的日志信息
					sqlSession.insert(str1,pd);
					logger.info("插入任务日志的信息,任务的ID:"+AICommMethod.nvl(pd.get("TASK_ID")));
					if(str2!=null){//更新问题单的状态
						sqlSession.update(str2,pd);
						logger.info("更新问题单的状态信息,问题单的ID:"+AICommMethod.nvl(pd.get("Q_ID")+",问题单的状态："+AICommMethod.nvl(pd.get("UPDATE_Q_STATUS"))));
					}
				}
				sqlSession.flushStatements();
				sqlSession.commit();
				sqlSession.clearCache();
			}
		}finally{
			sqlSession.close();
		}
	}
	/**
	 * 跳过NLP审核
	 * @Title: skipNLP 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param q_ids
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void skipNLP(String[] q_ids) throws Exception{
		
		SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
		//批量执行器
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
		try{
			String userid=Jurisdiction.getCurrentUser().getUSER_ID();
			for (String q_id : q_ids) {
				logger.info("跳过NLP审核，问题单的ID:"+q_id);
				//将问题单分解
				PageData pd=new PageData();
				pd=(PageData) dao.findForObject("TaskAllotMapper.findDiagQ", Integer.valueOf(q_id));
				String NLP_DIAG_NAME=pd.getString("NLP_DIAG_NAME");//NLP切分的结果
				String nlp_names[]=NLP_DIAG_NAME.split(";");
				String MTS_DIAG_CODE=pd.getString("MTS_DIAG_CODE");//MTS匹配的结果
				for (int i = 0; i < nlp_names.length; i++) {
					String nlp_name=nlp_names[i];
					//校验MTS是否已经匹配上 若是则当前的诊断名称跳过生成子问题单
					if(MTS_DIAG_CODE!=null&&MTS_DIAG_CODE!=""&&!AIConst.AI_MTS_CODE_UNM.equals( MTS_DIAG_CODE.split(";")[i]))
					{
						logger.info("问题单："+q_id+"的诊断"+nlp_name+"在MTS中已经匹配上："+MTS_DIAG_CODE);
						continue;
					}
						
					//校验是否存在子问题问题单，存在则建立关联，否则保存子问题单后建立关联
					PageData child_q_pd=new PageData();
					child_q_pd=	(PageData) dao.findForObject("TaskAllotMapper.findDiagChildQ", nlp_name);
					if(child_q_pd ==null){
						child_q_pd=new PageData();
						child_q_pd.put("DIAG_NAME", nlp_name);
						child_q_pd.put("STATUS", 0);
						child_q_pd.put("TASK_TYPE_ID", AIConst.AI_TASK_DIAG);
						child_q_pd.put("TASK_TYPE_CHILD_ID", AIConst.AI_TASK_CHILD_DIAG);
						child_q_pd.put("CREATE_MAN", userid);
						child_q_pd.put("UPDATE_MAN", userid);
						child_q_pd.put("ORIGIN_ID", Long.valueOf(pd.get("ORIGIN_ID").toString()));
						sqlSession.insert("TaskAllotMapper.addChildQ", child_q_pd);
						logger.info("不包含"+nlp_name+"子问题单,强问题单写入数据表中!");
					}else{
						logger.info("已经包含"+nlp_name+"的子问题单,Q_ID:"+child_q_pd.get("CHILD_ID"));
					}
					//写入问题单关系表
					PageData rel_pd=new PageData();
					rel_pd.put("Q_ID", Integer.valueOf(q_id));
					rel_pd.put("CHILD_ID", child_q_pd.get("CHILD_ID"));
					rel_pd.put("ORDER",i+1 );//顺序
					rel_pd.put("CREATE_MAN", userid);
					rel_pd.put("UPDATE_MAN", userid);
					sqlSession.insert("TaskAllotMapper.addRelQ", rel_pd);
					logger.info("问题单的关联数据，主问题单："+q_id+",子问题单："+child_q_pd.get("CHILD_ID"));
				}
				//修改问题单的状态为已经分解
				pd.put("UPDATE_Q_STATUS", 3);
				sqlSession.update("QuestionMapper.updateDiagQ", pd);
				logger.info("更新问题单："+q_id+"的状态为已分解");
			}
			sqlSession.flushStatements();
			sqlSession.commit();
			sqlSession.clearCache();
		}finally{
			sqlSession.close();
		}
	}
}
