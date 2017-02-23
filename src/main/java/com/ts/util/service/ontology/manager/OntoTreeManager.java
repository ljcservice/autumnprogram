package com.ts.service.ontology.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ts.util.PageData;

/**DiagService 诊断本体处理接口
 * @author xsl
 */
public interface OntoTreeManager {


	public List<PageData> treeRootList(PageData pd) throws Exception;	

	public String treePidsById(PageData pd) throws Exception;
	
	/**
	 * 保存本体父节点副本
	 * @param request
	 * @param pd
	 * @throws Exception
	 */
	public String saveOntoParentTree(HttpServletRequest request, PageData pd) throws Exception;
}
