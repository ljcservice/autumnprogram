package com.ts.service.ontology.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ts.entity.Page;
import com.ts.util.PageData;


/**DiagService 同义词处理接口
 * @author xsl
 */
public interface OsynManager {
	
	
	/**
	 * 查询标准词，为本体新增弹窗使用
	 * @param sqlName 
	 * @return
	 * @throws Exception
	 */
	public List<PageData> osynPage(Page page,String sqlName) throws Exception;
	
	
	/**
	 * 查询诊断同义词列表信息
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> osynListPage(Page page,String tableName) throws Exception;
	

	/**
	 * 通过ID查询诊断同义词
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public PageData findDiagOsynById(PageData page,String typeName)throws Exception;
	
	/**
	 * 审核通过添加词语表信息
	 * @param pd
	 * @throws Exception
	 */
	public String insertOsynName(PageData pd,String typeName)throws Exception;
	
	/**
	 * 审核通过修改对应的词语表信息
	 * @param pd
	 * @throws Exception
	 */
	public String updateDiagName(HttpServletRequest request,PageData pd)throws Exception;
	
	/**
	 * 查询药品名称列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> drugNameList(Page page)throws Exception;

	/**
	 * 检查诊断名称是否存在
	 * @param sqlName
	 * @param newOntology
	 * @return
	 * @throws Exception 
	 */
	public boolean checkExistName(String sqlName,PageData pd) throws Exception ;
	/**
	 * 批量审批通过
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String checkPassAll(PageData pd)throws Exception;
	
	/**
	 * 检查同义词对应的本体信息是否停用
	 * @param sqlName
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public boolean checkOntoDisable(String sqlName,PageData pd)throws Exception; 
}
