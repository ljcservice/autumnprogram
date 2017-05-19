package com.ts.controller.DoctOrder.OrderWork;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.DoctOrder.OrderWork.AllHospitalService;
import com.ts.util.MyDecimalFormat;
import com.ts.util.PageData;
import com.ts.util.ontology.HelpUtil;

@Controller
@RequestMapping(value="/allHospital")
public class AllHospital  extends BaseController{
	@Autowired
	private AllHospitalService allHospitalService ;
	/**
	 * 门急诊抗菌药使用统计   
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/allHospital1")
	public ModelAndView allHospital3(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			HelpUtil.setDefaultDate(pd);
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = null;
			List<PageData> reportList2 = null; 
			String type = pd.getString("type");
			//汇总(住院)
			PageData count = new PageData();
			BigDecimal med_all =  new BigDecimal(0);
			BigDecimal drug_all =   new BigDecimal(0);
			BigDecimal anti_all =   new BigDecimal(0);
			//汇总（门诊）
			PageData count2 = new PageData();
			BigDecimal med_all2 =  new BigDecimal(0);
			BigDecimal drug_all2 =   new BigDecimal(0);
			BigDecimal anti_all2 =   new BigDecimal(0);
			if("1".equals(type)){
				reportList = allHospitalService.allHospital11(page);
			}else if("2".equals(type)){
				reportList2 = allHospitalService.allHospital12(page);
			}else{
				reportList = allHospitalService.allHospital11(page);
				reportList2 = allHospitalService.allHospital12(page);
			}
			if(reportList!=null){
				for(PageData pp:reportList){
					BigDecimal med =  (BigDecimal) pp.get("med");
					BigDecimal drug =  (BigDecimal) pp.get("drug");
					BigDecimal anti =  (BigDecimal) pp.get("anti");
					if(med==null||med.doubleValue()==0){
						pp.put("anti_persents", 0);
						pp.put("drug_persents", 0);
					}else{
						pp.put("drug_persents", MyDecimalFormat.format(drug.divide(med,4,4).doubleValue()*100));
						pp.put("anti_persents", MyDecimalFormat.format(anti.divide(med,4,4).doubleValue()*100));
					}
					med_all=med_all.add(med);
					drug_all=drug_all.add(drug);
					anti_all=anti_all.add(anti);
				}
				count.put("med_all", med_all);
				count.put("drug_all", drug_all);
				count.put("anti_all", anti_all);
				if(med_all==null||med_all.doubleValue()==0){
					count.put("anti_persents", 0);
					count.put("drug_persents", 0);
				}else{
					count.put("drug_persents", MyDecimalFormat.format(drug_all.divide(med_all,4,4).doubleValue()*100));
					count.put("anti_persents", MyDecimalFormat.format(anti_all.divide(med_all,4,4).doubleValue()*100));
				}
			}
			if(reportList2!=null){
				for(PageData pp:reportList2){
					BigDecimal med =  (BigDecimal) pp.get("med");
					BigDecimal drug =  (BigDecimal) pp.get("drug");
					BigDecimal anti =  (BigDecimal) pp.get("anti");
					if(med==null||med.doubleValue()==0){
						pp.put("anti_persents", 0);
						pp.put("drug_persents", 0);
					}else{
						pp.put("drug_persents", MyDecimalFormat.format(drug.divide(med,4,4).doubleValue()*100));
						pp.put("anti_persents", MyDecimalFormat.format(anti.divide(med,4,4).doubleValue()*100));
					}
					med_all2=med_all2.add(med);
					drug_all2=drug_all2.add(drug);
					anti_all2=anti_all2.add(anti);
				}
				count2.put("med_all", med_all2);
				count2.put("drug_all", drug_all2);
				count2.put("anti_all", anti_all2);
				if(med_all2==null||med_all2.doubleValue()==0){
					count2.put("drug_persents", 0);
					count2.put("anti_persents", 0);
				}else{
					count2.put("drug_persents", MyDecimalFormat.format(drug_all2.divide(med_all2,4,4).doubleValue()*100));
					count2.put("anti_persents", MyDecimalFormat.format(anti_all2.divide(med_all2,4,4).doubleValue()*100));
				}
			}
			mv.addObject("reportList", reportList);
			mv.addObject("reportList2", reportList2);
			//总合计
			PageData all = new PageData();
			if("1".equals(type)){
				all.put("med_all", count.get("med_all")  );
				all.put("drug_all", count.get("drug_all") );
				all.put("anti_all", count.get("anti_all") );
			}else if("2".equals(type)){
				all.put("med_all", count2.get("med_all")  );
				all.put("drug_all", count2.get("drug_all") );
				all.put("anti_all", count2.get("anti_all") );
			}else{
				all.put("med_all", ((BigDecimal)count.get("med_all")).add( (BigDecimal)count2.get("med_all"))  );
				all.put("drug_all", ((BigDecimal)count.get("drug_all")).add( (BigDecimal)count2.get("drug_all"))  );
				all.put("anti_all", ((BigDecimal)count.get("anti_all")).add( (BigDecimal)count2.get("anti_all"))  );
			}
			BigDecimal all_med_all = (BigDecimal)all.get("med_all");
			BigDecimal all_drug_all = (BigDecimal)all.get("drug_all");
			BigDecimal all_anti_all = (BigDecimal)all.get("anti_all");
			//总合计百分比
			if(all_med_all==null||all_med_all.doubleValue()==0){
				all.put("anti_persents", 0);
				all.put("drug_persents", 0);
			}else{
				all.put("drug_persents", MyDecimalFormat.format(all_drug_all.divide(all_med_all,4,4).doubleValue()*100));
				all.put("anti_persents", MyDecimalFormat.format(all_anti_all.divide(all_med_all,4,4).doubleValue()*100));
			}
			mv.addObject("count", count);
			mv.addObject("count2", count2);
			mv.addObject("all", all);
			mv.setViewName("DoctOrder/allHospital/allHospital1");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 门急诊抗菌药使用统计   
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/allHospital2")
	public ModelAndView allHospital4(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			HelpUtil.setDefaultDate(pd);
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = null;
			String type = pd.getString("type");
			reportList = allHospitalService.allHospital2(page);
			mv.addObject("reportList", reportList);
			mv.setViewName("DoctOrder/allHospital/allHospital2");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
}
