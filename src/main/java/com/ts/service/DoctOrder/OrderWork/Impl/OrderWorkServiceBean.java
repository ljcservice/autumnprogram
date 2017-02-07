package com.ts.service.DoctOrder.OrderWork.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.service.DoctOrder.OrderWork.IOrderWorkService;
import com.ts.util.PageData;

@Service
public class OrderWorkServiceBean implements IOrderWorkService {

	@Resource(name="daoSupport_PH")
	private DAO daoph;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> patientList(Page  page) throws Exception 
	{
		return (List<PageData>) daoph.findForList("PatVistMapper.patvisitlistPage", page);
	}

	@Override
	public PageData findByPatient(String pid, String vid) throws Exception {
		PageData pd = new PageData();
		pd.put("patient_id", pid);
		pd.put("visit_id", vid);
		return (PageData) daoph.findForObject("PatVistMapper.patVisitById", pd);
	}

	@Override
	public PageData findByCheckResult(String pid, String vid)  throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int saveCheckResult(PageData pd) throws Exception{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteCheckReulst(String crid)throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PageData orderList(String pid, String vid)throws Exception {
		PageData pd = new PageData();
		pd.put("patient_id", pid);
		pd.put("visit_id", vid);
		return (PageData) daoph.findForObject("OrdersMapper.OrdersByIdlistPage", pd);
	}

	@Override
	public PageData orderWhereList(PageData pd)throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateChecResult(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
}
