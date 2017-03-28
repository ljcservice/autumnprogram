package com.ts.service.DoctOrder.OrderWork;

import java.util.List;
import java.util.Map;

import com.ts.entity.Page;
import com.ts.util.PageData;

public interface PrescService {

	/**
	 * 分页查询处方信息
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> prescListPage(Page page) throws Exception;

	/**
	 * 查询单个处方主要信息
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findPrescById(PageData pd) throws Exception;

	/**
	 * 查询处方详细信息列表
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> prescDetailList(PageData pd) throws Exception;

	/**
	 * 更新处方的关联问题字段
	 * @param pd
	 * @throws Exception
	 */
	public void updatePrescNgroupnum(PageData pd) throws Exception;

	/**
	 * 新增点评时查询处方的列表
	 * @param pd
	 * @return
	 */
	public Map prescListSpecial(PageData pd) throws Exception;

	/**
	 * 设置为专家点评
	 * @param pd
	 * @throws Exception
	 */
	public void updateExpertPresc(PageData pd)throws Exception;

	public void setCheckRsStatus(PageData pd)throws Exception;

}
