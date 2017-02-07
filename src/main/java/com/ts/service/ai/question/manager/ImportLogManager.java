package com.ts.service.ai.question.manager;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.Logger;
import com.ts.util.PageData;

/**
 * 导入日志 
 * @ClassName: ImportLogManager 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy
 * @date 2016年10月28日 下午2:00:48 
 *
 */
public interface ImportLogManager {

	public List<PageData> logList(Page page) throws Exception;
	
	public String importData2Base(String filePath,String fileName,Logger logger)throws Exception;
	
	public PageData getLogByFileName(String FILE_NAME) throws Exception;
	
	public String deleteImport(PageData pd)throws Exception;
}
