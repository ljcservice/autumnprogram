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
import com.ts.service.DoctOrder.OrderWork.JmsyService;
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
@RequestMapping(value="/jmsy")
public class JmsyReport extends BaseController{
	@Autowired
	private JmsyService jmsyService;
	
	/**
	 * 门急诊抗菌药使用率   
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/jmsy1")
	public ModelAndView haskjDrug1()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.setViewName("DoctOrder/jmsy/jmsy1");
			mv.addObject("pd", pd);
			String beginDate = pd.getString("beginDate");
			String endDate = pd.getString("endDate"); 
			if(Tools.isEmpty(beginDate)&&Tools.isEmpty(endDate)){
				return mv;
			}
			//门急诊药品费用统计
			List<PageData>	reportList = null;
			String type1 = pd.getString("type1");
			if(Tools.isEmpty(type1)){
				type1="1";
				pd.put("type1", type1);
			}
			if("1".equals(type1)){
				reportList = jmsyService.jmsy11(pd);
			}else{
				reportList = jmsyService.jmsy12(pd);
			}
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
	@RequestMapping(value="/jmsy1Export")
	public ModelAndView jmsy1Export(Page page)throws Exception{
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
			if("1".equals(type1)){
				reportList = jmsyService.jmsy11(pd);
			}else{
				reportList = jmsyService.jmsy12(pd);
			}
			
			Map<String,Object> dataMap = new HashMap<String,Object>();
			//门急诊药品费用统计
			List<PageData>	varList = new ArrayList<PageData>();
			List<String> titles = new ArrayList<String>();
			titles.add("排名");//2
			titles.add("科室");	//3
			titles.add("处方数");//2
			titles.add("静脉输液数");//2
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
	 * 门急诊抗菌药使用率   
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/jmsy2")
	public ModelAndView haskjDrug1Export( )throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.setViewName("DoctOrder/jmsy/jmsy2");
			mv.addObject("pd", pd);
			String beginDate = pd.getString("beginDate");
			String endDate = pd.getString("endDate"); 
			if(Tools.isEmpty(beginDate)&&Tools.isEmpty(endDate)){
				return mv;
			}
			//门急诊药品费用统计
			List<PageData>	reportList = null;
			String type1 = pd.getString("type1");
			if(Tools.isEmpty(type1)){
				type1="1";
				pd.put("type1", type1);
			}
			if("1".equals(type1)){
				//处方数， 
				reportList = jmsyService.jmsy21(pd);
			}else {
				//处方数， 
				reportList = jmsyService.jmsy22(pd);
			}
			mv.addObject("reportList", reportList);
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
	@RequestMapping(value="/jmsy2Export")
	public ModelAndView jmsy2Export(Page page)throws Exception{
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
				//处方数， 
				reportList = jmsyService.jmsy21(pd);
			}else {
				//处方数， 
				reportList = jmsyService.jmsy22(pd);
			}
			Map<String,Object> dataMap = new HashMap<String,Object>();
			//门急诊药品费用统计
			List<PageData>	varList = new ArrayList<PageData>();
			List<String> titles = new ArrayList<String>();
			titles.add("排名");//2
			titles.add("科室");	//3
			titles.add("医生");	//3
			titles.add("处方数");//2
			titles.add("抗菌静脉输液数");//2
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