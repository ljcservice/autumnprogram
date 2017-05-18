package com.ts.controller.DoctOrder.OrderWork;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.DoctOrder.OrderWork.CommonService;
import com.ts.service.DoctOrder.OrderWork.DrugAmountService;
import com.ts.util.MyDecimalFormat;
import com.ts.util.PageData;
/**
 * 门急诊药品费用统计
 * @author silong.xing
 *
 */
@Controller
@RequestMapping(value="/drugAmount")
public class DrugAmountReport extends BaseController{
	@Resource(name="commonServicePdss")
	private CommonService commonService;
	@Autowired
	private DrugAmountService drugAmountService;
	
	/**
	 * 门急诊药品费用统计
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/drugAmount")
	public ModelAndView drugAmount(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = drugAmountService.drugAmount(page);
			
			mv.addObject("reportList", reportList);
			mv.setViewName("DoctOrder/drugAmount/drugAmount");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 * 门急诊药品费用统计 按照科室
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/drugAmountByDep")
	public ModelAndView drugAmountByDep(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = drugAmountService.drugAmountByDep(page);
			
			mv.addObject("reportList", reportList);
			mv.setViewName("DoctOrder/drugAmount/drugAmountByDep");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 门急诊药品费用统计 按照医生
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/drugAmountByDoctor")
	public ModelAndView drugAmountByDoctor(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = drugAmountService.drugAmountByDoctor(page);
			
			mv.addObject("reportList", reportList);
			mv.setViewName("DoctOrder/drugAmount/drugAmountByDoctor");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 * 门急诊药品费用统计 按照人
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/drugAmountByPerson")
	public ModelAndView drugAmountByPerson(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = drugAmountService.drugAmountByPerson(page);
			
			mv.addObject("reportList", reportList);
			mv.setViewName("DoctOrder/drugAmount/drugAmountByPerson");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 * 门急诊药品费用统计 按药品
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/drugAmountByDrug")
	public ModelAndView drugAmountByDrug(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = drugAmountService.drugAmountByDrug(page);
			mv.addObject("reportList", reportList);
			mv.setViewName("DoctOrder/drugAmount/drugAmountByDrug");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	//门急科室费用占比统计
	@RequestMapping(value="/depAmountPersents")
	public ModelAndView depAmountPersents( )throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			//汇总
			BigDecimal med_all =  new BigDecimal(0);
			BigDecimal drug_all =   new BigDecimal(0);
			BigDecimal anti_all =   new BigDecimal(0);
			//门急诊药品费用统计
			List<PageData>	reportList = drugAmountService.depAmountPersents(pd);
			for(PageData pp:reportList){
				BigDecimal med =  (BigDecimal) pp.get("med");
				BigDecimal drug =  (BigDecimal) pp.get("drug");
				BigDecimal anti =  (BigDecimal) pp.get("anti");
				if(med==null||med.doubleValue()==0){
					pp.put("anti_persents", 0);
					pp.put("drug_persents", 0);
				}else{
					pp.put("anti_persents", MyDecimalFormat.format(anti.divide(med,4,4).doubleValue()*100));
					pp.put("drug_persents", MyDecimalFormat.format(drug.divide(med,4,4).doubleValue()*100));
				}
				med_all=med_all.add(med);
				drug_all=drug_all.add(drug);
				anti_all=anti_all.add(anti);
			}
			mv.addObject("reportList", reportList);
			mv.setViewName("DoctOrder/drugAmount/depAmountPersents");
			PageData count = new PageData();
			count.put("med_all", med_all);
			count.put("drug_all", drug_all);
			count.put("anti_all", anti_all);
			if(med_all==null||med_all.doubleValue()==0){
				count.put("drug_all_persents",0);
				count.put("anti_all_persents",0);
			}else{	
				count.put("drug_all_persents", MyDecimalFormat.format(drug_all.divide(med_all,4,4).doubleValue()*100));
				count.put("anti_all_persents", MyDecimalFormat.format(anti_all.divide(med_all,4,4).doubleValue()*100));
			}
			mv.addObject("count", count);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
}