package com.ts.service.DoctOrder.OrderWork;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

public interface DrugAmountService {

	List<PageData> drugAmount(Page page)throws Exception ;

	List<PageData> drugAmountByDep(Page page)throws Exception ;

	List<PageData> depAmountPersents(PageData pd)throws Exception ;

	List<PageData> drugAmountByDoctor(Page page)throws Exception ;

	List<PageData> drugAmountByPerson(Page page)throws Exception ;

	List<PageData> drugAmountByDrug(Page page)throws Exception ;


}
