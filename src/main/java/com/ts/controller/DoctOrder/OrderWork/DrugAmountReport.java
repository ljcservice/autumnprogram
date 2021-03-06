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
import com.ts.service.DoctOrder.OrderWork.DrugAmountService;
import com.ts.util.MyDecimalFormat;
import com.ts.util.ObjectExcelView;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.ontology.HelpUtil;
/**
 * 门急诊药品费用统计
 * @author silong.xing
 *
 */
@Controller
@RequestMapping(value="/drugAmount")
public class DrugAmountReport extends BaseController{
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
			mv.setViewName("DoctOrder/drugAmount/drugAmount");
			mv.addObject("pd", pd);
			String beginDate = pd.getString("beginDate");
			String endDate = pd.getString("endDate"); 
			if(Tools.isEmpty(beginDate)&&Tools.isEmpty(endDate)){
				return mv;
			}
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = drugAmountService.drugAmount(page);
			
			mv.addObject("reportList", reportList);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 * 门急诊药品费用统计
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/drugAmountExport")
	public ModelAndView drugAmountExport(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			//设置默认日期
			HelpUtil.setDefaultDate(pd);
			page.setPd(pd);
			//门急诊药品费用统计
			
			page.setShowCount(1000);
			int TotalPage = 1;
			List<PageData>	reportList =  new ArrayList<PageData>();
			for(int pag = 1;pag<=TotalPage&&pag<=60;pag++){
				page.setCurrentPage(pag);
				List<PageData> mylist =  drugAmountService.drugAmount(page);
				if(mylist!=null){
					reportList.addAll(mylist);
				}
				TotalPage = page.getTotalPage();
			}
			
			Map<String,Object> dataMap = new HashMap<String,Object>();
			//门急诊药品费用统计
			List<PageData>	varList = new ArrayList<PageData>();
			List<String> titles = new ArrayList<String>();
			titles.add("药名");//2
			titles.add("规格");	//3
			titles.add("厂家");	//3
			titles.add("金额");//2
			titles.add("使用量");//2
			dataMap.put("titles", titles);
			for(int i=0;i<reportList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", reportList.get(i).get("drug_name"));	//4
				vpd.put("var2", reportList.get(i).get("package_spec"));	//4
				vpd.put("var3", reportList.get(i).get("firm_id"));		//5
				vpd.put("var4","￥ "+ MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("costs")).doubleValue() ));		//5
				vpd.put("var5", reportList.get(i).get("amounts").toString()+reportList.get(i).get("units").toString());		//5
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
	 * 门急诊药品费用统计 按照科室
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/drugAmountByDep")
	public ModelAndView drugAmountByDep(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.setViewName("DoctOrder/drugAmount/drugAmountByDep");
			mv.addObject("pd", pd);
			String beginDate = pd.getString("beginDate");
			String endDate = pd.getString("endDate"); 
			if(Tools.isEmpty(beginDate)&&Tools.isEmpty(endDate)){
				return mv;
			}
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = drugAmountService.drugAmountByDep(page);
			
			mv.addObject("reportList", reportList);
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
	@RequestMapping(value="/drugAmountByDepExport")
	public ModelAndView drugAmountByDepExport(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			//设置默认日期
			HelpUtil.setDefaultDate(pd);
			page.setPd(pd);
			//门急诊药品费用统计
			page.setShowCount(1000);
			int TotalPage = 1;
			List<PageData>	reportList =  new ArrayList<PageData>();
			for(int pag = 1;pag<=TotalPage&&pag<=60;pag++){
				page.setCurrentPage(pag);
				List<PageData> mylist =  drugAmountService.drugAmountByDep(page);
				if(mylist!=null){
					reportList.addAll(mylist);
				}
				TotalPage = page.getTotalPage();
			}
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<PageData>	varList = new ArrayList<PageData>();
			List<String> titles = new ArrayList<String>();
			titles.add("科室");//2
			titles.add("药名");//2
			titles.add("规格");	//3
			titles.add("厂家");	//3
			titles.add("金额");//2
			titles.add("使用量");//2
			titles.add("药品属性");//2
			dataMap.put("titles", titles);
			for(int i=0;i<reportList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", reportList.get(i).get("dept_name"));	//4
				vpd.put("var2", reportList.get(i).get("drug_name"));	//4
				vpd.put("var3", reportList.get(i).get("package_spec"));		//5
				vpd.put("var4", reportList.get(i).get("firm_id"));		//5
				vpd.put("var5", "￥ "+MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("costs")).doubleValue() ));		//5
				vpd.put("var6", reportList.get(i).get("amounts").toString()+reportList.get(i).get("units").toString());		//5
				vpd.put("var7", reportList.get(i).get("TOXI_PROPERTY"));		//5
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
	 * 门急诊药品费用统计 按照医生
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/drugAmountByDoctor")
	public ModelAndView drugAmountByDoctor(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.setViewName("DoctOrder/drugAmount/drugAmountByDoctor");
			mv.addObject("pd", pd);
			String beginDate = pd.getString("beginDate");
			String endDate = pd.getString("endDate"); 
			if(Tools.isEmpty(beginDate)&&Tools.isEmpty(endDate)){
				return mv;
			}
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = drugAmountService.drugAmountByDoctor(page);
			
			mv.addObject("reportList", reportList);
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
	@RequestMapping(value="/drugAmountByDoctorExport")
	public ModelAndView drugAmountByDoctorExport(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			//设置默认日期
			HelpUtil.setDefaultDate(pd);
			page.setPd(pd);
			//门急诊药品费用统计
			page.setShowCount(1000);
			int TotalPage = 1;
			List<PageData>	reportList =  new ArrayList<PageData>();
			for(int pag = 1;pag<=TotalPage&&pag<=60;pag++){
				page.setCurrentPage(pag);
				List<PageData> mylist =  drugAmountService.drugAmountByDoctor(page);
				if(mylist!=null){
					reportList.addAll(mylist);
				}
				TotalPage = page.getTotalPage();
			}
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<PageData>	varList = new ArrayList<PageData>();
			List<String> titles = new ArrayList<String>();
			titles.add("医生");//2
			titles.add("药名");//2
			titles.add("规格");	//3
			titles.add("厂家");	//3
			titles.add("金额");//2
			titles.add("使用量");//2
			titles.add("药品属性");//2
			dataMap.put("titles", titles);
			for(int i=0;i<reportList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", reportList.get(i).get("DOCTOR_NAME"));	//4
				vpd.put("var2", reportList.get(i).get("drug_name"));	//4
				vpd.put("var3", reportList.get(i).get("package_spec"));		//5
				vpd.put("var4", reportList.get(i).get("firm_id"));		//5
				vpd.put("var5", "￥ "+MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("costs")).doubleValue() ) );		//5
				vpd.put("var6", reportList.get(i).get("amounts").toString()+reportList.get(i).get("units").toString() );		//5
				vpd.put("var7", reportList.get(i).get("TOXI_PROPERTY"));		//5
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
	 * 门急诊药品费用统计 按照人
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/drugAmountByPerson")
	public ModelAndView drugAmountByPerson(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.setViewName("DoctOrder/drugAmount/drugAmountByPerson");
			mv.addObject("pd", pd);
			String beginDate = pd.getString("beginDate");
			String endDate = pd.getString("endDate"); 
			if(Tools.isEmpty(beginDate)&&Tools.isEmpty(endDate)){
				return mv;
			}
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = drugAmountService.drugAmountByPerson(page);
			mv.addObject("reportList", reportList);
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
	@RequestMapping(value="/drugAmountByPersonExport")
	public ModelAndView drugAmountByPersonExport(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			//设置默认日期
			HelpUtil.setDefaultDate(pd);
			page.setPd(pd);
			//门急诊药品费用统计
			page.setShowCount(1000);
			int TotalPage = 1;
			List<PageData>	reportList =  new ArrayList<PageData>();
			for(int pag = 1;pag<=TotalPage&&pag<=60;pag++){
				page.setCurrentPage(pag);
				List<PageData> mylist =  drugAmountService.drugAmountByPerson(page);
				if(mylist!=null){
					reportList.addAll(mylist);
				}
				TotalPage = page.getTotalPage();
			}
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<PageData>	varList = new ArrayList<PageData>();
			List<String> titles = new ArrayList<String>();
			titles.add("患者");//2
			titles.add("费别");//2
			titles.add("用药日期");//2
			titles.add("身份");//2
			titles.add("科室");//2
			titles.add("医生");//2
			titles.add("药名");//2
			titles.add("规格");	//3
			titles.add("厂家");	//3
			titles.add("金额");//2
			titles.add("使用量");//2
			titles.add("药品属性");//2
			dataMap.put("titles", titles);
			for(int i=0;i<reportList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", reportList.get(i).get("PAT_NAME"));	//4
				vpd.put("var2", reportList.get(i).get("CHARGE_TYPE"));	//4
				vpd.put("var3", reportList.get(i).get("DR_DATE"));	//4
				vpd.put("var4", reportList.get(i).get("IDENTITY"));	//4
				vpd.put("var5", reportList.get(i).get("DEPT_NAME"));	//4
				vpd.put("var6", reportList.get(i).get("DOCTOR_NAME"));	//4
				vpd.put("var7", reportList.get(i).get("drug_name"));	//4
				vpd.put("var8", reportList.get(i).get("package_spec"));		//5
				vpd.put("var9", reportList.get(i).get("firm_id"));		//5
				vpd.put("var10", "￥ "+MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("costs")).doubleValue() ) );		//5
				vpd.put("var11", reportList.get(i).get("amounts").toString()+reportList.get(i).get("units").toString());		//5
				vpd.put("var12", reportList.get(i).get("TOXI_PROPERTY"));		//5
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
	 * 门急诊药品费用统计 按药品
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/drugAmountByDrug")
	public ModelAndView drugAmountByDrug(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.setViewName("DoctOrder/drugAmount/drugAmountByDrug");
			mv.addObject("pd", pd);
			String beginDate = pd.getString("beginDate");
			String endDate = pd.getString("endDate"); 
			if(Tools.isEmpty(beginDate)&&Tools.isEmpty(endDate)){
				return mv;
			}
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = drugAmountService.drugAmountByDrug(page);
			mv.addObject("reportList", reportList);
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
	@RequestMapping(value="/drugAmountByDrugExport")
	public ModelAndView drugAmountByDrugExport(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			//设置默认日期
			HelpUtil.setDefaultDate(pd);
			page.setPd(pd);
			//门急诊药品费用统计
			page.setShowCount(1000);
			int TotalPage = 1;
			List<PageData>	reportList =  new ArrayList<PageData>();
			for(int pag = 1;pag<=TotalPage&&pag<=60;pag++){
				page.setCurrentPage(pag);
				List<PageData> mylist =  drugAmountService.drugAmountByDrug(page);
				if(mylist!=null){
					reportList.addAll(mylist);
				}
				TotalPage = page.getTotalPage();
			}
			Map<String,Object> dataMap = new HashMap<String,Object>();
			//门急诊药品费用统计
			List<PageData>	varList = new ArrayList<PageData>();
			List<String> titles = new ArrayList<String>();
			titles.add("用药日期");//2
			titles.add("药名");//2
			titles.add("规格");	//3
			titles.add("厂家");	//3
			titles.add("金额");//2
			titles.add("使用量");//2
			titles.add("药品属性");//2
			dataMap.put("titles", titles);
			for(int i=0;i<reportList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", reportList.get(i).get("DR_DATE"));	//4
				vpd.put("var2", reportList.get(i).get("drug_name"));	//4
				vpd.put("var3", reportList.get(i).get("package_spec"));	//4
				vpd.put("var4", reportList.get(i).get("firm_id"));		//5
				vpd.put("var5","￥ "+ MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("costs")).doubleValue() ));		//5
				vpd.put("var6", reportList.get(i).get("amounts").toString()+reportList.get(i).get("units").toString());		//5
				vpd.put("var7", reportList.get(i).get("TOXI_PROPERTY"));	//4
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
	
	//门急科室费用占比统计
	@RequestMapping(value="/depAmountPersents")
	public ModelAndView depAmountPersents( )throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.setViewName("DoctOrder/drugAmount/depAmountPersents");
			mv.addObject("pd", pd);
			String beginDate = pd.getString("beginDate");
			String endDate = pd.getString("endDate"); 
			if(Tools.isEmpty(beginDate)&&Tools.isEmpty(endDate)){
				return mv;
			}
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
	//门急科室费用占比统计
		@RequestMapping(value="/depAmountPersentsExport")
		public ModelAndView depAmountPersentsExport( )throws Exception{
			ModelAndView mv = this.getModelAndView();
			PageData pd = this.getPageData();
			try{
				mv.addObject("pd", pd);
				//设置默认日期
				HelpUtil.setDefaultDate(pd);
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
//				mv.addObject("count", count);
//				mv.addObject("reportList", reportList);
//				mv.setViewName("DoctOrder/drugAmount/depAmountPersents");
				
				Map<String,Object> dataMap = new HashMap<String,Object>();
				//门急诊药品费用统计
				List<PageData>	varList = new ArrayList<PageData>();
				List<String> titles = new ArrayList<String>();
				titles.add("科室");//2
				titles.add("总费用");	//3
				titles.add("药费费用");//2
				titles.add("药费比例");//2
				titles.add("抗菌药费费用");//2
				titles.add("抗菌药费比例");//2
				dataMap.put("titles", titles);
				for(int i=0;i<reportList.size();i++){
					PageData vpd = new PageData();
					vpd.put("var1", reportList.get(i).get("dept_name"));	//2
					vpd.put("var2", "￥ "+ MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("med")).doubleValue()));	//4
					vpd.put("var3", "￥ "+ MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("drug")).doubleValue()));		//5
					vpd.put("var4", reportList.get(i).get("drug_persents") +" %");		//5
					vpd.put("var5", "￥ "+ MyDecimalFormat.format(((BigDecimal)reportList.get(i).get("anti")).doubleValue()));		//5
					vpd.put("var6", reportList.get(i).get("anti_persents") +" %");		//4
					varList.add(vpd);
				}
				PageData vpd1 = new PageData();
				vpd1.put("var1", "合计：");	//2
				vpd1.put("var2", "￥ "+ MyDecimalFormat.format(((BigDecimal)count.get("med_all")).doubleValue()) );	//4
				vpd1.put("var3", "￥ "+ MyDecimalFormat.format(((BigDecimal)count.get("drug_all")).doubleValue()) );		//5
				vpd1.put("var4", count.get("drug_all_persents") +" %");		//5
				vpd1.put("var5", "￥ "+ MyDecimalFormat.format(((BigDecimal)count.get("anti_all")).doubleValue()) );		//5
				vpd1.put("var6", count.get("anti_all_persents") +" %");		//5
				varList.add(vpd1);
				dataMap.put("varList", varList);
				ObjectExcelView erv = new ObjectExcelView();
				mv = new ModelAndView(erv,dataMap);
			} catch(Exception e){
				logger.error(e.toString(), e);
			}
			return mv;
		}
}