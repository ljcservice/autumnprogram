package com.ts.service.ai.question.manager;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

/**
 * 问题单查询
 * @ClassName: QueryQuestionManager 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年12月1日 上午11:10:06 
 *
 */
public interface QueryQuestionManager {

	public List<PageData> questionList(Page page) throws Exception;
	
	public PageData getNLPRs(PageData pd)throws Exception;
	
	public List<PageData> childList(PageData pd) throws Exception;
	
	public List<PageData> resList(PageData pd) throws Exception;
	
	
	/**
	 * 问题单验证
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> checkList(PageData pd) throws Exception;

	/**
	 * 查询实时的MTS更新信息
	 * @return
	 * @throws Exception
	 */
	public PageData queryMtsInfo() throws Exception;

	/**
	 * 更新mts信息
	 * @param pd
	 * @throws Exception
	 */
	public void updateMtsInfo(PageData pd) throws Exception;

	/**
	 * 原始问题数据分页
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List originalQuestionPage(Page page) throws Exception;

	/**
	 * 重新开启问题单
	 * @param string
	 * @param string2
	 */
	public void reOpenQuestion(List<PageData> errorList ) throws Exception ;
	
	/**
	 * 更新原始问题单
	 * @param pd
	 * @throws Exception
	 */
	public void updateOriginalQue(String BATCH_NUMBER) throws Exception;
}
