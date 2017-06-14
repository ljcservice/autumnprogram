package com.ts.service.DoctOrder.OrderWork;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

public interface OpDrugService {

	public List<PageData> list(Page page) throws Exception ;

	public PageData selectOpdrugInfo(PageData pd) throws Exception ;

	public String saveOpDrug(PageData pd) throws Exception ;

	public void deleteAllOpDrug(String[] arrayUSER_IDS) throws Exception ;

	public void deleteOpDrug(PageData pd) throws Exception ;

	public List<PageData> queryOperationPage(Page page) throws Exception ;

	public List<PageData> queryDrugMapPage(Page page) throws Exception ;

	public List<PageData> queryDeptPage(Page page) throws Exception ;

	public List<PageData> queryStaffPage(Page page) throws Exception ;

}
