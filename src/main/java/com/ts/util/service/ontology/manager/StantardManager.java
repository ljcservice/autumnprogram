package com.ts.service.ontology.manager;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.ai.AiAlterNameHist;
import com.ts.util.PageData;


/**DiagService 诊断本体处理接口
 * @author xsl
 */
public interface StantardManager {
	
	
	/**
	 * 查询标准词，为本体新增弹窗使用
	 * @param sqlName 
	 * @return
	 * @throws Exception
	 */
	public List<PageData> osynPage(Page page,String sqlName) throws Exception;
	
	public List<PageData> chooseStandPage(Page page,String sqlName) throws Exception;
}
