package com.ts.service.ontology.manager;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

/**词语维护处理接口
 * @author zq
 */
public interface OsynHisManager {
	
	/*
	 * 查询单个词语历史变更记录列表
	 */
	public List<PageData> nameHistPage(Page page,String tableName)throws Exception;
	
	/**
	 * 查询未审核的词语副本信息
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> checkNameList(Page page)throws Exception;
	
	/**
	 * 审核时查看单个词语副本信息
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public PageData alterNameDetail(PageData page)throws Exception;
	
	/**
	 * 根据ID修改词语副本表信息，用于审核后更新审核信息
	 * @param page
	 * @throws Exception
	 */
	public String updateAlterById(PageData page)throws Exception;
	
	/**
	 * 根据ID查询单个副本信息
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public PageData findAlterById(String h_id)throws Exception;
	
	/**
	 * 保存同义词副本信息
	 * @param pd
	 * @throws Exception
	 */
	public String saveDiagOsyn(PageData pd)throws Exception;
	
	/**
	 * 查询同义词历史
	 * @param param
	 * @return
	 */
	public List<PageData> queryOsynHis(PageData param)throws Exception ;
	
}
