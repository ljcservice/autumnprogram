package com.ts.service.ontology.manager;

import java.util.List;
import java.util.Map;

import com.ts.entity.Page;
import com.ts.util.PageData;

/**本体处理接口
 * @author xsl
 */
public interface CommonManager {
	
	
	/**
	 * 获取序列值
	 * @param seq_name
	 * @return
	 * @throws Exception
	 */
	public Integer querySeqValue(String seq_name)  throws Exception;
	
	/**
	 * 本体树插件
	 * @param page
	 * @param sqlName
	 * @return
	 * @throws Exception
	 */
	public List<PageData> ontoWidgetPage(Page page, String sqlName) throws Exception ;
	
	/**
	 * 根据本体类型和序列类型查询序列值
	 * @param ontoType
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Integer getSequenceID(String ontoType,int type) throws Exception;

	/**
	 * 
	 * @param newOntology 新本体
	 * @param ontoType	本体类型
	 * @param pids 父节点ID集合，诊断最多2个，手术与科室为一个 ，药品待定
	 */
	public void setMainAddCode( String ontoType,PageData ontology,String[] pids)throws Exception;
	
	/**
	 * 
	 * @param newOntology 新本体
	 * @param ontoType	本体类型
	 * @param pids 父节点ID集合，诊断最多2个，手术与科室为一个 ，药品待定
	 */
	public void setMainAddCode( String ontoType,PageData ontology,List<String> pids)throws Exception;
	
	/**
	 * 根据parentID 找到父节点的所有下级节点最大编码，然后继续编码
	 * @param ontoType 本体类型
	 * @param parentId 父节点ID
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getCode(String ontoType,PageData ontology,String parentId) throws Exception;
	
}
