package com.ts.service.DoctOrder.OrderWork.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.service.DoctOrder.OrderWork.HaskjDrugService;
import com.ts.util.PageData;

@Service(value="haskjDrugService")
public class HaskjDrugServiceImpl implements HaskjDrugService {
	@Resource(name="daoSupportPH")
	private DAO daoph;
	
	@Override
	public List<PageData> haskjDrug11(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("HaskjDrugMapper.haskjDrug11", page);
	}
	@Override
	public List<PageData> haskjDrug12(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("HaskjDrugMapper.haskjDrug12", page);
	}
	@Override
	public List<PageData> haskjDrug13(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("HaskjDrugMapper.haskjDrug13", page);
	}
	@Override
	public List<PageData> haskjDrug14(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("HaskjDrugMapper.haskjDrug14", page);
	}
	
	@Override
	public List<PageData> haskjDrug21(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("HaskjDrugMapper.haskjDrug21", page);
	}
	
	@Override
	public List<PageData> haskjDrug22(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("HaskjDrugMapper.haskjDrug22", page);
	}
}
