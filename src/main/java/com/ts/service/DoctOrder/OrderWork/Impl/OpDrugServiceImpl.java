package com.ts.service.DoctOrder.OrderWork.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.service.DoctOrder.OrderWork.OpDrugService;
import com.ts.util.PageData;

@Service(value="opDrugService")
public class OpDrugServiceImpl implements OpDrugService {
	@Resource(name="daoSupportPH")
	private DAO daoph;
	
	public List<PageData> list(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("CKOperationDrug.ckOperationDrugPage", page);
	}
	

	public PageData selectOpdrugInfo(PageData pd) throws Exception {
		return ( PageData ) daoph.findForObject("CKOperationDrug.selectOpdrugInfo", pd);
	}

	public String saveOpDrug(PageData pd) throws Exception {
		daoph.update("CKOperationDrug.saveOpDrug", pd);
		return "success";
	}
	

	public String updateOpDrug(PageData pd)throws Exception {
		daoph.update("CKOperationDrug.updateOpDrug", pd);
		return "success";
	}	

	public void deleteAllOpDrug(String[] arrayUSER_IDS) throws Exception {
		for(String o_id:arrayUSER_IDS ){
			PageData pd = new PageData();
			pd.put("o_id", o_id);
			deleteOpDrug(pd);
		}
	}
	

	public void deleteOpDrug(PageData pd) throws Exception {
		daoph.delete("CKOperationDrug.deleteOpDrug", pd);
	}
	
	public List<PageData> queryOperationPage(Page page) throws Exception{
		return (List<PageData>) daoph.findForList("CKOperationDrug.queryOperationPage", page);
	}

	public List<PageData> queryDrugMapPage(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("CKOperationDrug.queryDrugMapPage", page);
	}

	public List<PageData> queryDeptPage(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("CKOperationDrug.queryDeptPage", page);
	}

	public List<PageData> queryStaffPage(Page page) throws Exception {
		return (List<PageData>) daoph.findForList("CKOperationDrug.queryStaffPage", page);
	}
}
