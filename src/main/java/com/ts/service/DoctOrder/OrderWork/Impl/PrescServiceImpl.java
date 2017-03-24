package com.ts.service.DoctOrder.OrderWork.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.service.DoctOrder.OrderWork.PrescService;
import com.ts.util.PageData;

@Service(value="prescService")
public class PrescServiceImpl implements PrescService{
	@Resource(name="daoSupportPH")
	private DAO daoph;


	@Override
	public List<PageData> prescListPage(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("PrescMapper.prescListPage", page);
	}


	@Override
	public PageData findPrescById(PageData pd) throws Exception {
		return  (PageData) daoph.findForObject("PrescMapper.findPrescById", pd);
	}


	@Override
	public List<PageData> prescDetailList(PageData pd) throws Exception {
		return (List<PageData>) daoph.findForList("PrescMapper.prescDetailList", pd);
	}

	
}
