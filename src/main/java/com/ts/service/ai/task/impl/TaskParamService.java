package com.ts.service.ai.task.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.entity.ai.TaskParam;
import com.ts.service.ai.task.manager.TaskParamManager;
import com.ts.util.PageData;

/**
 * 任务参数配置
 * @ClassName: TaskParamService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年10月25日 上午9:46:32 
 *
 */
@Service("taskParamService")
public class TaskParamService implements TaskParamManager{

	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;


	@SuppressWarnings("unchecked")
	public List<PageData> taskParamList(Page page) throws Exception{
		return (List<PageData>)dao.findForList("TaskParamMapper.taskParamlistPage", page);
	}

	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("TaskParamMapper.findById", pd);
	}
	
	public void addTP(TaskParam tp) throws Exception {
		dao.save("TaskParamMapper.addTP", tp);
	}
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void editTP(PageData pd)throws Exception{
		dao.update("TaskParamMapper.editTP", pd);
	}
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteTP(PageData pd)throws Exception{
		dao.delete("TaskParamMapper.deleteTP", pd);
	}
	/**批量删除
	 * @param USER_IDS
	 * @throws Exception
	 */
	public void deleteAllTP(String[] P_IDS)throws Exception{
		dao.delete("TaskParamMapper.deleteAllTP", P_IDS);
	}
	/**
	 * 通过类型获取配置
	 */
	public PageData findByType(int TASK_TYPE_CHILD_ID )throws Exception{
		return (PageData)dao.findForObject("TaskParamMapper.findByType", TASK_TYPE_CHILD_ID);
	}
	/**
	 * 判断能否编辑
	 */
	public String findEditCan(int P_ID ,String CTL_TYPE)throws Exception{
		if(CTL_TYPE.equals("edit")){
			if((PageData)dao.findForObject("TaskParamMapper.editCan", P_ID)!=null)//判断失效记录的能否修改
				return "已经存在有效的当前配置,不可修改!";
		}
		if(!((List<PageData>)dao.findForList("TaskParamMapper.hasTask", P_ID)).isEmpty())////判断是否包含任务信息 
			return "已经包含当前配置的任务信息,不可操作!";
		else			
			return "success";
	}
	/**
	 * 判断能否删除
	 * @Title: deleteAllCheck 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param P_ID
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public String deleteAllCheck(String[] P_IDS)throws Exception{
		List<PageData> list=(List<PageData>)dao.findForList("TaskParamMapper.deleteAllCheck", P_IDS);
		if(list.isEmpty()){
			return "success";
		}else{
			String info="配置";
			for (PageData pd : list) {
				info=info+"["+pd.get("TASK_TYPE")+","+pd.get("TASK_TYPE_CHILD")+"],";
			}
			info=info+"已经包含任务信息,不可删除!";
			return info;
		}
	}
	
}
