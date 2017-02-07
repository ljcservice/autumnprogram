package com.ts.service.ai.task.manager;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.ai.TaskParam;
import com.ts.util.PageData;

/**
 * 任务参数配置接口
 * @ClassName: TaskParamManager 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年10月25日 上午9:47:37 
 *
 */
public interface TaskParamManager {


	public List<PageData> taskParamList(Page page) throws Exception;
	
	public PageData findById(PageData pd)throws Exception;

	public void addTP(TaskParam tp)throws Exception;
	
	public void editTP(PageData pd)throws Exception;
	
	public void deleteTP(PageData pd)throws Exception;
	
	public void deleteAllTP(String[] P_IDS)throws Exception;

	public PageData findByType(int TASK_TYPE_CHILD_ID)throws Exception;
	
	public String findEditCan(int P_ID ,String CTL_TYPE)throws Exception;
	
	public String deleteAllCheck(String[] P_IDS)throws Exception;
}
