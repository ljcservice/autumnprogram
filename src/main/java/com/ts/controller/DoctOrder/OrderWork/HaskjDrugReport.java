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

import com.hitzd.his.Utils.Config;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.DoctOrder.OrderWork.HaskjDrugService;
import com.ts.util.MyDecimalFormat;
import com.ts.util.NumberUtil;
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
			mv.setViewName("DoctOrder/haskjDrug/haskjDrug1");
			mv.addObject("pd", pd);
			String beginDate = pd.getString("beginDate");
			String endDate = pd.getString("endDate"); 
			if(Tools.isEmpty(beginDate)&&Tools.isEmpty(endDate)){
				return mv;
			}
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = null;
			List<PageData> reportHZ = null;
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
					reportHZ = haskjDrugService.haskjDrug11ByHJ(page);
				}else if("2".equals(type2)){
					//处方数，不含外用
					reportList = haskjDrugService.haskjDrug12(page);
				}
			}else if("2".equals(type1)){
				if("1".equals(type2)){
					//处方数(人次) ，含外用
					reportList = haskjDrugService.haskjDrug13(page);
					reportHZ = haskjDrugService.haskjDrug13ByHJ(page);
				}else if("2".equals(type2)){
					//处方数(人次) ，不含外用
					reportList = haskjDrugService.haskjDrug14(page);
				}
			}
			long  count = 0l;
			long  antiCount = 0l;
//			long  count1 = 0l;
//			long  antiCount1 = 0l;
			for(PageData entity : reportList)
			{
				String deptCode = entity.getString("dept_code");
				int    prescCount = entity.getInt("c");
				int    prescAntiCount = entity.getInt("haskj");
				
				if(Config.getParamValue("EmergencyTreatment").indexOf(deptCode + ",") != -1)
				{
					count += prescCount;
					antiCount += prescAntiCount;
				}
//				else
//				{
//					count1 += prescCount;
//					antiCount1 += prescAntiCount;
//				}
			}
			// 急诊处方
			mv.addObject("prescCount",count);
			mv.addObject("prescAntiCount",antiCount);
			mv.addObject("", antiCount/count);
			// 门诊处方
			mv.addObject("count1",reportHZ.get(0).getInt("c") - count);
			mv.addObject("antiCount1",reportHZ.get(0).getInt("haskj") - antiCount);
			//整体数据集
			mv.addObject("reportHJ", reportHZ.get(0));
			mv.addObject("reportList", reportList);
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
			List<PageData> reportHJ = null;
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
					reportHJ = haskjDrugService.haskjDrug11ByHJ(page);
				}else if("2".equals(type2)){
					//处方数，不含外用
					reportList = haskjDrugService.haskjDrug12(page);
				}
			}else if("2".equals(type1)){
				if("1".equals(type2)){
					//处方数(人次) ，含外用
					reportList = haskjDrugService.haskjDrug13(page);
					reportHJ = haskjDrugService.haskjDrug13ByHJ(page);
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
			long count = 0l;
			long antiCount = 0l;
			for(int i=0;i<reportList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", i+1);	//2
				vpd.put("var2", reportList.get(i).get("dept_name"));	//4
				vpd.put("var3",  MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("c")).doubleValue() ));		//5
				vpd.put("var4", MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("haskj")).doubleValue() )  );		//5
				vpd.put("var5", MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("rate")).doubleValue() )  +"%");		//5
				varList.add(vpd);
				
				String deptCode = reportList.get(i).getString("dept_code");
				int    prescCount = reportList.get(i).getInt("c");
				int    prescAntiCount = reportList.get(i).getInt("haskj");
				
				if(Config.getParamValue("EmergencyTreatment").indexOf(deptCode + ",") != -1)
				{
					count += prescCount;
					antiCount += prescAntiCount;
				}
				
			}
			PageData vpd = new PageData();
            vpd.put("var1", "合计");   //2
            vpd.put("var2", "");    //4
            long  sumCount     = (long) reportHJ.get(0).getInt("c");
            long  sumAntiCount = (long) reportHJ.get(0).getInt("haskj");
            vpd.put("var3", MyDecimalFormat.format(((BigDecimal)reportHJ.get(0).get("c")).doubleValue() ));      //5
            vpd.put("var4", MyDecimalFormat.format(((BigDecimal)reportHJ.get(0).get("haskj")).doubleValue() )  );     //5
            vpd.put("var5", MyDecimalFormat.format(((BigDecimal)reportHJ.get(0).get("rate")).doubleValue() )  +"%");      //5
            varList.add(vpd);
            //急诊
            vpd = new PageData();
            vpd.put("var1","急诊合计");
            vpd.put("var2","");
            vpd.put("var3",MyDecimalFormat.format(count));
            vpd.put("var4",MyDecimalFormat.format(antiCount));
            vpd.put("var5",MyDecimalFormat.format(NumberUtil.divide(antiCount, count) * 100) + "%" );
            varList.add(vpd);
            //门诊
            vpd = new PageData();
            vpd.put("var1","门诊合计");
            vpd.put("var2","");
            vpd.put("var3",NumberUtil.subtract(sumCount, count));
            vpd.put("var4",NumberUtil.subtract(sumAntiCount, antiCount));
            vpd.put("var5",MyDecimalFormat.format(NumberUtil.divide(NumberUtil.subtract(sumAntiCount, antiCount), NumberUtil.subtract(sumCount, count)) * 100) + "%" );
            varList.add(vpd);
            
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
			mv.setViewName("DoctOrder/haskjDrug/haskjDrug2");
			mv.addObject("pd", pd);
			String beginDate = pd.getString("beginDate");
			String endDate = pd.getString("endDate"); 
			if(Tools.isEmpty(beginDate)&&Tools.isEmpty(endDate)){
				return mv;
			}
			page.setPd(pd);
			String type = pd.getString("type");
			if(Tools.isEmpty(type)){
				type="1";
				pd.put("type", type);
			}
			List<PageData>	reportList = null;
			List<PageData> reportHJ = null;
			//门急诊药品费用统计
			if("1".equals(type)){
				//处方数 
				reportList = haskjDrugService.haskjDrug21(page);
				reportHJ  = haskjDrugService.haskjDrug11ByHJ(page);
			}else if("2".equals(type)){
				//处方数(人次)  
				reportList = haskjDrugService.haskjDrug22(page);
				reportHJ = haskjDrugService.haskjDrug13ByHJ(page);
			}
			mv.addObject("reportList", reportList);
			mv.addObject("reportHJ",reportHJ.get(0));
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
			List<PageData>  reportHJ = null;
			//门急诊药品费用统计
			if("1".equals(type)){
				//处方数 
				reportList = haskjDrugService.haskjDrug21(page);
				reportHJ = haskjDrugService.haskjDrug11ByHJ(page);
				
			}else if("2".equals(type)){
				//处方数(人次)  
				reportList = haskjDrugService.haskjDrug22(page);
				reportHJ = haskjDrugService.haskjDrug13ByHJ(page);
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
			PageData vpd = new PageData();
            vpd.put("var1", "合计");   //2
            vpd.put("var2", ""); //4
            vpd.put("var3", "");  //4
            vpd.put("var4",  MyDecimalFormat.format(((BigDecimal)reportHJ.get(0).get("c")).doubleValue() ));      //5
            vpd.put("var5", MyDecimalFormat.format(((BigDecimal)reportHJ.get(0).get("haskj")).doubleValue() )  );     //5
            vpd.put("var6", MyDecimalFormat.format(((BigDecimal)reportHJ.get(0).get("rate")).doubleValue() )  +"%");      //5
            varList.add(vpd);
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
}