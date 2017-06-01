package com.ts.controller.DoctOrder.OrderWork;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.DoctOrder.OrderWork.HaskjDrugService;
import com.ts.util.MyDecimalFormat;
import com.ts.util.ObjectExcelView;
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
	 * 门急诊抗菌药使用率   
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/haskjDrug1Export")
	public ModelAndView haskjDrug1Export(Page page)throws Exception{
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
//			mv.addObject("reportList", reportList);
//			mv.setViewName("DoctOrder/haskjDrug/haskjDrug1");
			
			Map<String,Object> dataMap = new HashMap<String,Object>();
			//门急诊药品费用统计
			List<PageData>	varList = new ArrayList<PageData>();
			List<String> titles = new ArrayList<String>();
			titles.add("排名");//2
			titles.add("科室");	//3
			titles.add("处方数");//2
			titles.add("抗菌药物处方数");//2
			titles.add("比例");//2
			dataMap.put("titles", titles);
			for(int i=0;i<reportList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", i+1);	//2
				vpd.put("var2", reportList.get(i).get("dept_name"));	//4
				vpd.put("var3",  MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("c")).doubleValue() ));		//5
				vpd.put("var4", MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("haskj")).doubleValue() )  );		//5
				vpd.put("var5", MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("rate")).doubleValue() )  +"%");		//5
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
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
			String type = pd.getString("type");
			if(Tools.isEmpty(type)){
				type="1";
				pd.put("type", type);
			}
			List<PageData>	reportList = null;
			//门急诊药品费用统计
			if("1".equals(type)){
				//处方数 
				reportList = haskjDrugService.haskjDrug21(page);
			}else if("2".equals(type)){
				//处方数(人次)  
				reportList = haskjDrugService.haskjDrug22(page);
			}
			mv.addObject("reportList", reportList);
			mv.setViewName("DoctOrder/haskjDrug/haskjDrug2");
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
	@RequestMapping(value="/haskjDrug2Export")
	public ModelAndView haskjDrug2Export(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			HelpUtil.setDefaultDate(pd);
			page.setPd(pd);
			String type = pd.getString("type");
			if(Tools.isEmpty(type)){
				type="1";
				pd.put("type", type);
			}
			List<PageData>	reportList = null;
			//门急诊药品费用统计
			if("1".equals(type)){
				//处方数 
				reportList = haskjDrugService.haskjDrug21(page);
			}else if("2".equals(type)){
				//处方数(人次)  
				reportList = haskjDrugService.haskjDrug22(page);
			}
//			mv.addObject("reportList", reportList);
//			mv.setViewName("DoctOrder/haskjDrug/haskjDrug2");
			
			Map<String,Object> dataMap = new HashMap<String,Object>();
			//门急诊药品费用统计
			List<PageData>	varList = new ArrayList<PageData>();
			List<String> titles = new ArrayList<String>();
			titles.add("排名");//2
			titles.add("科室");	//3
			titles.add("医生");	//3
			titles.add("处方数");//2
			titles.add("抗菌药物处方数");//2
			titles.add("比例");//2
			dataMap.put("titles", titles);
			for(int i=0;i<reportList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", i+1);	//2
				vpd.put("var2", reportList.get(i).get("org_name"));	//4
				vpd.put("var3", reportList.get(i).get("doctor_name"));	//4
				vpd.put("var4",  MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("c")).doubleValue() ));		//5
				vpd.put("var5", MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("haskj")).doubleValue() )  );		//5
				vpd.put("var6", MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("rate")).doubleValue() )  +"%");		//5
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
}