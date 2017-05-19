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
import com.ts.service.DoctOrder.OrderWork.HaskjDrugService;
import com.ts.service.ontology.manager.CommonManager;
import com.ts.util.MyDecimalFormat;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.ontology.HelpUtil;
/**
 * 门急诊药抗菌药统计
 * @author silong.xing
 *
 */
@Controller
@RequestMapping(value="/haskjDrug")
public class HaskjDrugReport extends BaseController{
	@Autowired
	private HaskjDrugService haskjDrugService;
	
	/**
	 * 门急诊抗菌药使用率   
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/haskjDrug1")
	public ModelAndView haskjDrug1(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			HelpUtil.setDefaultDate(pd);
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = null;
			String type1 = pd.getString("type1");
			if(Tools.isEmpty(type1)){
				type1="1";
				pd.put("type1", type1);
			}
			String type2 = pd.getString("type2");
			if(Tools.isEmpty(type2)){
				type2="1";
				pd.put("type2", type2);
			}
			if("1".equals(type1)){
				if("1".equals(type2)){
					//处方数，含外用
					reportList = haskjDrugService.haskjDrug11(page);
				}else if("2".equals(type2)){
					//处方数，不含外用
					reportList = haskjDrugService.haskjDrug12(page);
				}
			}else if("2".equals(type1)){
				if("1".equals(type2)){
					//处方数(人次) ，含外用
					reportList = haskjDrugService.haskjDrug13(page);
				}else if("2".equals(type2)){
					//处方数(人次) ，不含外用
					reportList = haskjDrugService.haskjDrug14(page);
				}
			}
			mv.addObject("reportList", reportList);
			mv.setViewName("DoctOrder/haskjDrug/haskjDrug1");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 *  门急诊医生抗菌药使用率   
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/haskjDrug2")
	public ModelAndView haskjDrug2(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			HelpUtil.setDefaultDate(pd);
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = haskjDrugService.haskjDrug2(page);
			
			mv.addObject("reportList", reportList);
			mv.setViewName("DoctOrder/haskjDrug/haskjDrug2");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
}