package com.ts.service.DoctOrder.OrderWork;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ts.entity.Page;
import com.ts.util.PageData;

/**
 * 医嘱点评操作
 * @author autumn
 *
 */

public interface IOrderWorkService {

	/**
	 * 查询诊断信息 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> queryDiagnosisByPatVisist(PageData pd ) throws Exception ;
	
	/**
	 * 查询患者信息
	 * @param pd
	 * @return
	 */
	public List<PageData>  patientList(Page page ) throws Exception; 
	
	/**
	 * 解决抽取后刷新的问题
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData  patvisitlistbyRefreshId(PageData pd ) throws Exception;
	
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
	 * 删除点评信息
	 * @param pd
	 * @throws Exception
	 */
	public void deleteCheckRsById(PageData pd) throws Exception ;
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
	 * 批量删除点评结果信息
	 * @param crid
	 * @return
	 */
	public void delCheckRsAll(PageData pd)throws Exception;
	
	/**
	 * 查询患者医嘱信息
	 * @param pid
	 * @param vid
	 * @return
	 */
	public List<PageData> orderList(Page page) throws Exception ;
	
	/**
	 * 导出审核结果
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> checkRsDetail(PageData pd) throws Exception;
	
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

	/**
	 * 查询医嘱单，特殊查询
	 * @return
	 * @throws Exception
	 */
	public Map ordersListSpecial(PageData pd) throws Exception;

	/**
	 * 查询单个点评结果
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getCheckRsById(PageData pd)throws Exception;

	/**
	 * 更新单个点评结果
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public void updateCheckResult(PageData pd) throws Exception;

	/**
	 * 病人住院 记录医嘱是否合理
	 * @param pd
	 * @throws Exception
	 */
	public void setCheckRsStatus(PageData pd) throws Exception;

	/**
	 * 删除滴啊弄结果,根据组号
	 * @param pd
	 * @throws Exception
	 */
	public void delCheckRsByNgroupnum(PageData pd) throws Exception ;

	/**
	 * 查询单个病人住院记录
	 * @param pd
	 * @return
	 */
	public PageData queryPatVisit(PageData pd) throws Exception ;

	/**
	 * 查询单个医嘱开始日期
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public String queryOrdersStartDate(PageData pd) throws Exception ;

	public List<PageData> getOrderClassDict()throws Exception ;

	/**
	 * 手后医嘱查询
	 * @param page
	 * @return
	 */
	public List<PageData> ordersPageByOpDate(Page page)throws Exception ;

	/**
	 * 设置住院病历为专家点评
	 */
	public void updateExpertPatVisit(PageData pd) throws Exception ;

	/**
	 * 医嘱报表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> ordersReport(PageData pd) throws Exception ;

	public List<PageData> orderListByDoctor(PageData pd) throws Exception ;

	public List<PageData> orderListByDep(PageData pd)throws Exception ;

	
}
