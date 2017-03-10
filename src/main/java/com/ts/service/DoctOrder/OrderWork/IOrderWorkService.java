package com.ts.service.DoctOrder.OrderWork;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

/**
 * 医嘱点评操作
 * @author autumn
 *
 */

public interface IOrderWorkService {

	/**
	 * 查询患者信息
	 * @param pd
	 * @return
	 */
	public List<PageData>  patientList(Page page ) throws Exception; 
	
	/**
	 * 查询患者详细信息
	 * @param pid
	 * @param vid
	 * @return
	 */
	public PageData findByPatient(Page page) throws Exception ;
	/**
	 *  添加  NGROUPNUM 关联
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public int updatePatVisitNgroupnum(PageData pd) throws Exception;
	
	/**
	 * 查询患者点评结果信息
	 * @param pid
	 * @param vid
	 * @return
	 */
	public List<PageData> findByCheckResultsByNgroupnum(Page page) throws Exception ;
	
	/**
	 * 添加点评结果信息
	 * @param pd
	 * @return
	 */
	public int saveCheckResult(PageData pd) throws Exception ;
	
	/**
	 * 更新点评结果信息
	 * @param pd
	 * @return
	 */
	public int updateChecResult(PageData pd) throws Exception ;
	
	/**
	 * 删除点评结果信息
	 * @param crid
	 * @return
	 */
	public int deleteCheckReulst(String crid) throws Exception ;
	
	/**
	 * 查询患者医嘱信息
	 * @param pid
	 * @param vid
	 * @return
	 */
	public List<PageData> orderList(Page page) throws Exception ;
	/**
	 * 查询患者医嘱信息,按照日期查看
	 * @param pid
	 * @param vid
	 * @return
	 */
	public List<PageData> ordersPageByDate(Page page)throws Exception ;
	/**
	 *  根据过滤条件查询患者医嘱信息
	 * @param pd
	 * @return
	 */
	public List<PageData> orderWhereList(PageData pd) throws Exception ;
	/**
	 * 查询患者手术信息
	 * @param pid
	 * @param vid
	 * @return
	 */
	public List<PageData> operationList(Page page) throws Exception ;
	
	/**
	 * 审核类型字段表
	 * @return
	 * @throws Exception
	 */
	public List<PageData> selectRsTypeDict() throws Exception;

	/**
	 * 按日图查看医嘱
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> OrdersPicture(PageData pd) throws Exception;
	
	
}
