package com.ts.service.DoctOrder.OrderWork.Impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.service.DoctOrder.OrderWork.IOrderWorkService;
import com.ts.util.PageData;

@Service
public class OrderWorkServiceBean implements IOrderWorkService {

	@Resource(name="daoSupportPH")
	private DAO daoph;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> patientList(Page  page) throws Exception 
	{
		return (List<PageData>) daoph.findForList("PatVisitMapper.patvisitlistPage", page); 
	}

	@Override
	public PageData findByPatient(Page page) throws Exception {
		
		return (PageData) daoph.findForObject("PatVisitMapper.patVisitById", page);
	}
	
	@Override
	public int updatePatVisitNgroupnum(PageData pd) throws Exception {
		daoph.update("PatVisitMapper.UpdatePatVisitNgroupnum", pd);
		return 1;
	}

	@Override
	public int saveCheckResult(PageData pd) throws Exception{
		daoph.save("rsDrugCheckRsltMapper.saveCheckRS", pd);
		return 1;
	}

	@Override
	public List<PageData> findByCheckResultsByNgroupnum(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("rsDrugCheckRsltMapper.selectCheckRsByNgroupnum", page);
	}
	
	@Override
	public void deleteCheckRsById(PageData pd) throws Exception{
		daoph.update("rsDrugCheckRsltMapper.deleteCheckRsById", pd);
	}
	
	@Override
	public void delCheckRsAll(PageData pd)throws Exception{
		String RS_IDS = pd.getString("RS_IDS");
		if(RS_IDS == null){
			throw new Exception("parama H_IDS is null.");
		}
		String[] RSIDS= RS_IDS.split(";");
		for(String RS_ID:RSIDS){
			pd.put("RS_ID", RS_ID);
			deleteCheckRsById(pd);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> orderList(Page page)throws Exception {
		List<PageData> pdOrders  = (List<PageData>) daoph.findForList("OrdersMapper.ordersByIdlistPage", page);
		return pdOrders;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> ordersPageByDate(Page page)throws Exception {
		List<PageData> pdOrders  = (List<PageData>) daoph.findForList("OrdersMapper.ordersPageByDate", page);
		return pdOrders;
	}
	
	/**
	 * 按日图查看医嘱
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> OrdersPicture(PageData pd) throws Exception{
		return (List<PageData>) daoph.findForList("OrdersMapper.ordersByPicture", pd);
	}
	

	@Override
	public List<PageData> orderWhereList(PageData pd)throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateChecResult(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<PageData> operationList(Page page) throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>) daoph.findForList("OperationMapper.OperationById", page);
	}

	@SuppressWarnings("unchecked")  
	@Override
	public List<PageData> selectRsTypeDict() throws Exception {
		
		return (List<PageData>) daoph.findForList("RS_TYPE_DICTMapper.reTypeDcit","");
	}
	
	public Map ordersListSpecial() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		List<PageData>	list =	(List<PageData>) daoph.findForList("OrdersMapper.ordersListSpecial",new PageData());
		if(list!=null){
			for(PageData p:list){
				map.put(p.getString("key"), p.getString("value"));
			}
		}
		return map;
	}

	@Override
	public PageData getCheckRsById(PageData pd) throws Exception {
		return (PageData) daoph.findForObject("rsDrugCheckRsltMapper.getCheckRsById", pd);
	}
	public void updateCheckResult(PageData pd) throws Exception{
		daoph.update("rsDrugCheckRsltMapper.updateCheckResult", pd);
	}

	@Override
	public void setCheckRsStatus(PageData pd) throws Exception {
		//如果为合理，删除点评
		if("0".equals(pd.getString("CHECK_STATUS"))){
			delCheckRsByNgroupnum(pd);
		}
		//更新病人住院记录pat_visit的状态为0合理1不合理
		//pd.put("NGROUPNUM", "");
		pd.put("ISCHECKTRUE", 1);
		pd.put("ISORDERCHECK", 0);
		pd.put("CHECK_STATUS", pd.getString("CHECK_STATUS"));
		updatePatVisitNgroupnum(pd);
	}

	@Override
	public void delCheckRsByNgroupnum(PageData pd) throws Exception {
		daoph.delete("rsDrugCheckRsltMapper.delCheckRsByNgroupnum", pd);
	}
	@Override
	public PageData queryPatVisit(PageData pd) throws Exception {
		return (PageData) daoph.findForObject("PatVisitMapper.queryPatVisit" ,pd);
	}
	
	@Override
	public String queryOrdersStartDate(PageData pd) throws Exception {
		return  (String) daoph.findForObject("OrdersMapper.queryOrdersStartDate" ,pd);
	}
}
