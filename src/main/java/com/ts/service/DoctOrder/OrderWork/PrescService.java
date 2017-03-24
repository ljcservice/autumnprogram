package com.ts.service.DoctOrder.OrderWork;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

public interface PrescService {

	/**
	 * 分页查询处方信息
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<PageData> prescListPage(Page page) throws Exception;

	/**
	 * 查询单个处方主要信息
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	PageData findPrescById(PageData pd) throws Exception;

	/**
	 * 查询处方详细信息列表
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> prescDetailList(PageData pd) throws Exception;

}
