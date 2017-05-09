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
	 * 新增点评时查询处方的列表，同日包括其他处方的信息
	 * @param pd
	 * @return
	 */
	public Map otherPrescListSpecial(PageData pd)throws Exception;

	/**
	 * 设置为专家点评
	 * @param pd
	 * @throws Exception
	 */
	public void updateExpertPresc(PageData pd)throws Exception;

	public void setCheckRsStatus(PageData pd)throws Exception;

	/**
	 * 当日其他人开具的处方
	 * @param pd
	 * @return
	 */
	public List<PageData> otherPrescList(PageData pd)throws Exception;
	/**
	 * 当日其他人开具的处方详情
	 * @param pd
	 * @return
	 */
	public List<PageData> otherPrescDetailList(PageData pd)throws Exception;

	/**
	 * 处方问题统计
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> prescReport(PageData pd)throws Exception;

	public PageData prescCountReport(PageData pd)throws Exception;

	public List<PageData> prescListByDoctor(PageData pd)throws Exception;

	public List<PageData> prescListByDep(PageData pd)throws Exception;



}
