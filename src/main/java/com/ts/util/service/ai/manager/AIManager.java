package com.ts.service.ai.manager;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

/**
 * AI通用
 * @ClassName: AIManager 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年11月2日 下午2:39:46 
 *
 */
public interface AIManager {
	
	public List<PageData> dictList(PageData pd)throws Exception;
	
	public List<PageData> listUsersByRole(Page page)throws Exception;
	
	public void quesInfo(int task_type_id,int task_type_child_id,PageData pd) throws Exception;
	
	public List<PageData> ontologyDiagPage(Page page) throws Exception;
	
	public List<PageData> ontologyDiagByNlpPage(Page page) throws Exception;
	
	public List<PageData> ontologyDiagByTreePage(Page page) throws Exception;
	
	public boolean checkInDict(PageData pd)throws Exception;
}
