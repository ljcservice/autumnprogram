package com.ts.service.DoctOrder.OrderWork;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ts.entity.Page;
import com.ts.entity.system.User;
import com.ts.util.PageData;

/**
 * 医嘱点评操作
 * @author autumn
 *
 */

public interface ExpertService {
	/**
	 * 设置住院病历为专家点评
	 */
	public void updateExpertPatVisit(PageData pd) throws Exception ;

	public List<PageData> listExperts(Page page)throws Exception ;
	
	/**保存用户
	 * @param user
	 * @throws Exception
	 */
	public void saveExpert(PageData pd)throws Exception;
	
	/**保存用户
	 * @param user
	 * @throws Exception
	 */
	public void updateExpert(PageData pd)throws Exception;
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteExpert(PageData pd)throws Exception;
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findExpertById(PageData pd)throws Exception;

	public void deleteAllExpert(String[] arrayUSER_IDS)throws Exception;
}
