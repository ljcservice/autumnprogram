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

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findByCheckResultsByNgroupnum(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("rsDrugCheckRsltMapper.selectCheckRsByNgroupnum", page);
	}
	
	@Override
	public int deleteCheckReulst(String crid)throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> orderList(Page page)throws Exception {
		
		return (List<PageData>) daoph.findForList("OrdersMapper.OrdersByIdlistPage", page);
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
	
}
