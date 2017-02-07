package com.ts.service.ai.task.manager;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

/**
 * 任务分配
 * @ClassName: TaskAllotManager 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年10月31日 上午9:05:20 
 *
 */
public interface TaskAllotManager {


	public List<PageData> questionList(Page page) throws Exception;
	
	public String getRole(PageData pd) throws Exception;
	
	public String allot(PageData pd) throws Exception;
	
	public void skipNLP(String[] q_ids) throws Exception;
}
