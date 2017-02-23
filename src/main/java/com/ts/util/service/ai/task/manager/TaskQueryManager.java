package com.ts.service.ai.task.manager;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.ts.entity.Page;
import com.ts.util.Logger;
import com.ts.util.PageData;

/**
 * 任务查询
 * @ClassName: TaskQueryManager 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年11月9日 下午3:30:12 
 *
 */
public interface TaskQueryManager {


	public List<PageData> taskList(Page page) throws Exception;
	
	public List<PageData> taskList4Ctrl(Page page) throws Exception;
	
	public PageData getTask(String task_id) throws Exception;
	
	public PageData getQ(int q_id) throws Exception;
	
	public PageData getChildQ(int q_id) throws Exception;

	public PageData getRes(String task_id)throws Exception;
	
	public PageData getSplitRs(String task_id)throws Exception;
	
	public List<PageData> getOneRes(String task_id)throws Exception;

	public void saveTaskHis(PageData pd)throws Exception;

	public void saveNLPRs(PageData pd)throws Exception;
	
	public void deleteTaskHis(PageData pd)throws Exception;
	
	public String submitTaskAll(String[] ArrayTASK_IDS,Logger logger)throws Exception;

	public void submitDiagTwo(SqlSession sqlSession,String TASK_ID)throws Exception;
	
	public void submitNLP(SqlSession sqlSession,String TASK_ID)throws Exception;
	
	public void revokeTaskAll(String[] ArrayTASK_IDS,String query_step,String query_task_type_child_id)throws Exception;
	
	public PageData getResByID(String HIS_RS_ID)throws Exception;
	
	public void addDiagName(PageData pd,SqlSession sqlSession)throws Exception;
	
	public String checkOnTree(PageData pd) throws Exception;
	
	public String getOntMultiPaChk(PageData pd) throws Exception;
	
	public String chkDiagMultiPa(PageData pd) throws Exception ;
	
	public boolean getOntIsLeaf( PageData pd) throws Exception ;
	
	public String nlpCtrl(String DIAG_NAME)throws Exception;
}
