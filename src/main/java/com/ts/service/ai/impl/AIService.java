package com.ts.service.ai.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.annotation.MyDao;
import com.ts.dao.DAO;
import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.service.ai.manager.AIManager;
import com.ts.util.PageData;
import com.ts.util.ai.AIConst;

/**
 * AI的通用的service
 * @ClassName: AIService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年11月2日 下午2:39:04 
 *
 */
@Service("aiService")
public class AIService implements AIManager{

	@Resource(name = "daoSupportAi")
	public DaoSupportAi dao;
	
	@MyDao(name="sqlSessionTemplate_PDSS")
	public DAO daotest;
	
	/**
	 * 根据类型获取数据字典列表
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> dictList(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("AIMapper.dictList", pd);
	}
	
	/**
	 * 获取角色下的用户
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listUsersByRole(Page page)throws Exception{
		return (List<PageData>)dao.findForList("AIMapper.userlistPage", page);
	}
	
	public void quesInfo(int task_type_id,int task_type_child_id,PageData pd) throws Exception{
		Map<String,String> m=new HashMap<String,String>();
		//获取任务的列表信息
		switch (task_type_id) {
		case AIConst.AI_TASK_DIAG://诊断干预
			switch (task_type_child_id) {
			case AIConst.AI_TASK_CHILD_NLP://NLP审核
				pd.put("TABLE_NAME", "AI_Q_DIAG");
				pd.put("Q_ID", "Q_ID");
				pd.put("DIAG_NAME", "O_DIAG_NAME");
				break;

			case AIConst.AI_TASK_CHILD_DIAG://诊断干预
				pd.put("TABLE_NAME", "AI_Q_DIAG_CHILD");
				pd.put("Q_ID", "CHILD_ID");
				pd.put("DIAG_NAME", "DIAG_NAME");
				break;
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 诊断同义词 标准词
	 * @Title: ontologyPage 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param page
	 * @param @param sqlName
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return List<PageData>    返回类型 
	 * @throws
	 */
	public List<PageData> ontologyDiagPage(Page page) throws Exception{
		return (List<PageData>)dao.findForList("AIMapper.diagOsynlistPage", page);
	}
	
	/**
	 * 根据NLP获取列表
	 * @Title: ontologyDiagByNlpPage 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param page
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return List<PageData>    返回类型 
	 * @throws
	 */
	public List<PageData> ontologyDiagByNlpPage(Page page) throws Exception{
		
		return (List<PageData>)dao.findForList("AIMapper.diagOsynlistPage", page);
	}
	/**
	 * 根据树获取列表，根据树的id获取标准词再获取同义词的信息
	 * @Title: ontologyDiagByTreePage 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param page
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return List<PageData>    返回类型 
	 * @throws
	 */
	public List<PageData> ontologyDiagByTreePage(Page page) throws Exception{
		
		return (List<PageData>)dao.findForList("AIMapper.diagOsynByTreelistPage", page);
	}
	
	
	/**
	 * 校验是否在字典表中
	 * @Title: checkInDict 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param pd
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return boolean    返回类型 
	 * @throws
	 */
	public boolean checkInDict(PageData pd)throws Exception{
		List<PageData> dicts=(List<PageData>)dao.findForList("AIMapper.checkInDict", pd);
		if (dicts.isEmpty())
			return false;
		else
			return true;
	}
}
