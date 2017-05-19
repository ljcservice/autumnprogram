package com.ts.service.DoctOrder.OrderWork.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.service.DoctOrder.OrderWork.AllHospitalService;
import com.ts.util.PageData;

@Service(value="allHospitalService")
public class AllHospitalServiceImpl implements AllHospitalService {
	@Resource(name="daoSupportPH")
	private DAO daoph;
	@Override
	public List<PageData> allHospital11(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("AllHospitalMapper.allHospital11", page);
	}
	
	@Override
	public List<PageData> allHospital12(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("AllHospitalMapper.allHospital12", page);
	}
	@Override
	public List<PageData> allHospital2(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("AllHospitalMapper.allHospital2Page", page);
	}
}
