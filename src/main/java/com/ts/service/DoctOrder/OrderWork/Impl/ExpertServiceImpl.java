package com.ts.service.DoctOrder.OrderWork.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.service.DoctOrder.OrderWork.ExpertService;
import com.ts.util.PageData;

@Service(value="expertService")
public class ExpertServiceImpl implements ExpertService{
	@Resource(name="daoSupportPH")
	private DAO daoph;

	/**
	 * 设置住院病历为专家点评
	 */
	@Override
	public void updateExpertPatVisit(PageData pd) throws Exception {
		daoph.update("PatVisitMapper.updateExpertPatVisit", pd);
	}
	@Override
	public List<PageData> listExperts(Page page)throws Exception {
		return (List<PageData>) daoph.findForList("ExpertMapper.expertListPage", page);
	}

	@Override
	public void saveExpert(PageData pd) throws Exception {
		daoph.save("ExpertMapper.saveExpert", pd);
	}

	@Override
	public void deleteExpert(PageData pd) throws Exception {
		daoph.delete("ExpertMapper.deleteExpert", pd);
	}

	public void deleteAllExpert(String[] arrayUSER_IDS)throws Exception{
		for(String user_id:arrayUSER_IDS){
			PageData pd = new PageData();
			pd.put("user_id", user_id);
			deleteExpert(pd);
		}
		
	}
	@Override
	public PageData findExpertById(PageData pd) throws Exception {
		return (PageData) daoph.findForObject("ExpertMapper.findExpertById", pd);
	}

	@Override
	public void updateExpert(PageData pd) throws Exception {
		daoph.update("ExpertMapper.updateExpert", pd);
	}
	
	
}
