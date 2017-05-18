package com.ts.service.DoctOrder.OrderWork.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.service.DoctOrder.OrderWork.DrugAmountService;
import com.ts.util.PageData;

@Service(value="drugAmountService")
public class DrugAmountServiceImpl implements DrugAmountService {

	@Resource(name="daoSupportPH")
	private DAO daoph;
	
	public List<PageData> drugAmount(Page page)throws Exception {
		return (List<PageData>) daoph.findForList("DrugAmountMapper.drugAmountPage", page);
	}

	public List<PageData> drugAmountByDep(Page page)throws Exception{
		return (List<PageData>) daoph.findForList("DrugAmountMapper.drugAmountPageByDep", page);
	}
	
	public List<PageData> drugAmountByDoctor(Page page)throws Exception{
		return (List<PageData>) daoph.findForList("DrugAmountMapper.drugAmountPageByDoctor", page);
	}
	
	public List<PageData> drugAmountByPerson(Page page)throws Exception{
		return (List<PageData>) daoph.findForList("DrugAmountMapper.drugAmountPageByPerson", page);
	}
	

	public List<PageData> drugAmountByDrug(Page page) throws Exception{
		return (List<PageData>) daoph.findForList("DrugAmountMapper.drugAmountPageByDrug", page);
	}
	
	public List<PageData> depAmountPersents(PageData pd)throws Exception{
		return (List<PageData>) daoph.findForList("DrugAmountMapper.depAmountPersents", pd);
	}
}
