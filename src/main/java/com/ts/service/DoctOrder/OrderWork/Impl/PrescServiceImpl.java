package com.ts.service.DoctOrder.OrderWork.Impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.service.DoctOrder.OrderWork.IOrderWorkService;
import com.ts.service.DoctOrder.OrderWork.PrescService;
import com.ts.util.PageData;

@Service(value="prescService")
public class PrescServiceImpl implements PrescService{
	@Resource(name="daoSupportPH")
	private DAO daoph;
	@Autowired @Qualifier("orderWorkServiceBean")
	private IOrderWorkService orderWorkService;

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


	@Override
	public void updatePrescNgroupnum(PageData pd) throws Exception {
		daoph.update("PrescMapper.updatePrescNgroupnum", pd);
	}
	
	/**
	 * 新增点评时查询处方的列表
	 * @param pd
	 * @return
	 */
	public Map prescListSpecial(PageData pd) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		List<PageData>	list =	(List<PageData>) daoph.findForList("PrescMapper.prescListSpecial",pd);
		if(list!=null){
			for(PageData p:list){
				map.put(p.getString("key"), p.getString("value"));
			}
		}
		return map;
	}
	public Map otherPrescListSpecial(PageData pd)throws Exception{
		Map<String,String> map = new LinkedHashMap<String,String>();
		List<PageData>	list =	(List<PageData>) daoph.findForList("PrescMapper.otherPrescListSpecial",pd);
		if(list!=null){
			for(PageData p:list){
				map.put(p.getString("key"), p.getString("value"));
			}
		}
		return map;
	}
	
	/**
	 * 设置为专家点评
	 * @param pd
	 * @throws Exception
	 */
	public void updateExpertPresc(PageData pd)throws Exception{
		daoph.update("PrescMapper.updateExpertPresc", pd);
	}
	
	@Override
	public void setCheckRsStatus(PageData pd) throws Exception {
		//如果为合理，删除点评
		if("0".equals(pd.getString("ISCHECKTRUE"))){
			orderWorkService.delCheckRsByNgroupnum(pd);
			//无人工点评记录
//			pd.put("ISORDERCHECK", 0);
			pd.put("ISORDERCHECK", "1");
		}
		//pd.put("NGROUPNUM", "");问题组号
		//更新病人住院记录pat_visit的状态为0合理1不合理2待定
		//pd.put("ISCHECKTRUE", pd.getString("ISCHECKTRUE"));
		updatePrescNgroupnum(pd);
	}

	/**
	 * 当日其他人开具的处方
	 * @param pd
	 * @return
	 */
	public List<PageData> otherPrescList(PageData pd)throws Exception{
		return (List<PageData>) daoph.findForList("PrescMapper.otherPrescList", pd);
	}
	/**
	 * 当日其他人开具的处方详情
	 * @param pd
	 * @return
	 */
	public List<PageData> otherPrescDetailList(PageData pd)throws Exception{
		return (List<PageData>) daoph.findForList("PrescMapper.otherPrescDetailList", pd);
	}

	/**
	 * 处方问题统计
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> prescReport(PageData pd)throws Exception{
		return (List<PageData>) daoph.findForList("rsDrugCheckRsltMapper.prescReport", pd);
	}
	

	public PageData prescCountReport(PageData pd)throws Exception{
		return (PageData)daoph.findForObject("PrescMapper.prescCountReport", pd);
	}
	

	public List<PageData> prescListByDoctor(PageData pd)throws Exception{
		return (List<PageData>) daoph.findForList("rsDrugCheckRsltMapper.prescListByDoctor", pd);
	}

	public List<PageData> prescListByDep(PageData pd)throws Exception{
		return (List<PageData>) daoph.findForList("rsDrugCheckRsltMapper.prescListByDep", pd);
	}
	
	public PageData prescStatistics1(PageData pd)throws Exception{
		return (PageData) daoph.findForObject("rsDrugCheckRsltMapper.prescStatistics1", pd);
	}

	public PageData prescStatistics2(PageData pd)throws Exception{
		return (PageData) daoph.findForObject("rsDrugCheckRsltMapper.prescStatistics2", pd);
	}

	public PageData prescStatistics3(PageData pd)throws Exception{
		return (PageData) daoph.findForObject("rsDrugCheckRsltMapper.prescStatistics3", pd);
	}
	public List<PageData> exceedCommonDoctor(PageData pd)throws Exception{
		return (List<PageData>) daoph.findForList("rsDrugCheckRsltMapper.exceedCommonDoctor", pd);
	}
	public List<PageData> exceedCommonDep(PageData pd)throws Exception{
		return (List<PageData>) daoph.findForList("rsDrugCheckRsltMapper.exceedCommonDep", pd);
	}

	public List<PageData> prescCountDoctor(PageData pd)throws Exception{
		return (List<PageData>) daoph.findForList("rsDrugCheckRsltMapper.prescCountDoctor", pd);
	}

	public PageData exceedCommonAll(PageData pd)throws Exception{
		return (PageData) daoph.findForObject("rsDrugCheckRsltMapper.exceedCommonAll", pd);
	}

	public List<PageData> prescCountDep(PageData pd)throws Exception{
		return (List<PageData>) daoph.findForList("rsDrugCheckRsltMapper.prescCountDep", pd);
	}
	
	public List<PageData> exceedCommonOrderDoctor(PageData pd)throws Exception{
		return (List<PageData>) daoph.findForList("rsDrugCheckRsltMapper.exceedCommonOrderDoctor", pd);
	}
	public List<PageData> exceedCommonOrderDep(PageData pd)throws Exception{
		return (List<PageData>) daoph.findForList("rsDrugCheckRsltMapper.exceedCommonOrderDep", pd);
	}

	public List<PageData> orderCountDoctor(PageData pd)throws Exception{
		return (List<PageData>) daoph.findForList("rsDrugCheckRsltMapper.orderCountDoctor", pd);
	}

	public List<PageData> orderCountDep(PageData pd)throws Exception{
		return (List<PageData>) daoph.findForList("rsDrugCheckRsltMapper.orderCountDep", pd);
	}
	
	public PageData exceedCommonOrderAll(PageData pd)throws Exception{
		return (PageData) daoph.findForObject("rsDrugCheckRsltMapper.exceedCommonOrderAll", pd);
	}

    public List<PageData> checkRsDetail(PageData pd) throws Exception {
        return (List<PageData>) daoph.findForList("PrescMapper.selectCheckPrescExcelByNgroupnum", pd);
    }

}
