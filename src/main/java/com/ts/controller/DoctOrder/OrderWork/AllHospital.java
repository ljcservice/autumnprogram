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
import com.ts.service.DoctOrder.OrderWork.AllHospitalService;
import com.ts.util.MyDecimalFormat;
import com.ts.util.ObjectExcelView;
import com.ts.util.PageData;
import com.ts.util.Tools;
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
	public ModelAndView allHospital1(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.setViewName("DoctOrder/allHospital/allHospital1");
			mv.addObject("pd", pd);
			String beginDate = pd.getString("beginDate");	//开始时间
			String endDate = pd.getString("endDate");		//结束时间
			if(Tools.isEmpty(beginDate)&&Tools.isEmpty(endDate)){
				return mv;
			}
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
					if(med==null||med.doubleValue()==0||drug==null||drug.doubleValue()==0){
						pp.put("anti_persents", 0);
						pp.put("drug_persents", 0);
					}else{
						pp.put("drug_persents", MyDecimalFormat.format(drug.divide(med,4,4).doubleValue()*100));
						pp.put("anti_persents", MyDecimalFormat.format(anti.divide(drug,4,4).doubleValue()*100));
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
					count.put("anti_persents", MyDecimalFormat.format(anti_all.divide(drug_all,4,4).doubleValue()*100));
				}
			}
			if(reportList2!=null){
				for(PageData pp:reportList2){
					BigDecimal med =  (BigDecimal) pp.get("med");
					BigDecimal drug =  (BigDecimal) pp.get("drug");
					BigDecimal anti =  (BigDecimal) pp.get("anti");
					if(med==null||med.doubleValue()==0||drug==null||drug.doubleValue()==0){
						pp.put("anti_persents", 0);
						pp.put("drug_persents", 0);
					}else{
						pp.put("drug_persents", MyDecimalFormat.format(drug.divide(med,4,4).doubleValue()*100));
						pp.put("anti_persents", MyDecimalFormat.format(anti.divide(drug,4,4).doubleValue()*100));
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
					count2.put("anti_persents", MyDecimalFormat.format(anti_all2.divide(drug_all2,4,4).doubleValue()*100));
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
			if(all_med_all==null||all_med_all.doubleValue()==0||all_drug_all==null||all_drug_all.doubleValue()==0){
				all.put("anti_persents", 0);
				all.put("drug_persents", 0);
			}else{
				all.put("drug_persents", MyDecimalFormat.format(all_drug_all.divide(all_med_all,4,4).doubleValue()*100));
				all.put("anti_persents", MyDecimalFormat.format(all_anti_all.divide(all_drug_all,4,4).doubleValue()*100));
			}
			mv.addObject("count", count);
			mv.addObject("count2", count2);
			mv.addObject("all", all);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	@RequestMapping(value="/allHospital1Export")
	public ModelAndView allHospital1Export(Page page)throws Exception{
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
					if(med==null||med.doubleValue()==0||drug==null||drug.doubleValue()==0){
						pp.put("anti_persents", 0);
						pp.put("drug_persents", 0);
					}else{
						pp.put("drug_persents", MyDecimalFormat.format(drug.divide(med,4,4).doubleValue()*100));
						pp.put("anti_persents", MyDecimalFormat.format(anti.divide(drug,4,4).doubleValue()*100));
					}
					med_all=med_all.add(med);
					drug_all=drug_all.add(drug);
					anti_all=anti_all.add(anti);
				}
				count.put("med_all", med_all);
				count.put("drug_all", drug_all);
				count.put("anti_all", anti_all);
				if(med_all==null||med_all.doubleValue()==0||drug_all==null||drug_all.doubleValue()==0){
					count.put("anti_persents", 0);
					count.put("drug_persents", 0);
				}else{
					count.put("drug_persents", MyDecimalFormat.format(drug_all.divide(med_all,4,4).doubleValue()*100));
					count.put("anti_persents", MyDecimalFormat.format(anti_all.divide(drug_all,4,4).doubleValue()*100));
				}
			}
			if(reportList2!=null){
				for(PageData pp:reportList2){
					BigDecimal med =  (BigDecimal) pp.get("med");
					BigDecimal drug =  (BigDecimal) pp.get("drug");
					BigDecimal anti =  (BigDecimal) pp.get("anti");
					if(med==null||med.doubleValue()==0||drug==null||drug.doubleValue()==0){
						pp.put("anti_persents", 0);
						pp.put("drug_persents", 0);
					}else{
						pp.put("drug_persents", MyDecimalFormat.format(drug.divide(med,4,4).doubleValue()*100));
						pp.put("anti_persents", MyDecimalFormat.format(anti.divide(drug,4,4).doubleValue()*100));
					}
					med_all2=med_all2.add(med);
					drug_all2=drug_all2.add(drug);
					anti_all2=anti_all2.add(anti);
				}
				count2.put("med_all", med_all2);
				count2.put("drug_all", drug_all2);
				count2.put("anti_all", anti_all2);
				if(med_all2==null||med_all2.doubleValue()==0||drug_all2==null||drug_all2.doubleValue()==0){
					count2.put("drug_persents", 0);
					count2.put("anti_persents", 0);
				}else{
					count2.put("drug_persents", MyDecimalFormat.format(drug_all2.divide(med_all2,4,4).doubleValue()*100));
					count2.put("anti_persents", MyDecimalFormat.format(anti_all2.divide(drug_all2,4,4).doubleValue()*100));
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
			if(all_med_all==null||all_med_all.doubleValue()==0||all_drug_all==null||all_drug_all.doubleValue()==0){
				all.put("anti_persents", 0);
				all.put("drug_persents", 0);
			}else{
				all.put("drug_persents", MyDecimalFormat.format(all_drug_all.divide(all_med_all,4,4).doubleValue()*100));
				all.put("anti_persents", MyDecimalFormat.format(all_anti_all.divide(all_drug_all,4,4).doubleValue()*100));
			}
//			mv.addObject("count", count);
//			mv.addObject("count2", count2);
//			mv.addObject("all", all);
//			mv.setViewName("DoctOrder/allHospital/allHospital1");
			
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
				vpd.put("var1", reportList.get(i).get("dept_name").toString());	//2
				vpd.put("var2", "￥ "+ reportList.get(i).get("med").toString());	//4
				vpd.put("var3", "￥ "+ reportList.get(i).get("drug").toString());		//5
				vpd.put("var4", reportList.get(i).get("drug_persents")+" %");		//5
				vpd.put("var5", "￥ "+ reportList.get(i).get("anti").toString());		//5
				vpd.put("var6", reportList.get(i).get("anti_persents")+" %");		//5
				varList.add(vpd);
			}
			PageData vpd1 = new PageData();
			vpd1.put("var1", "住院合计：");	//2
			vpd1.put("var2", "￥ "+ count.get("med_all").toString());	//4
			vpd1.put("var3", "￥ "+ count.get("drug_all").toString());		//5
			vpd1.put("var4", count.get("drug_persents")+" %");		//5
			vpd1.put("var5", "￥ "+ count.get("anti_all").toString());		//5
			vpd1.put("var6", count.get("anti_persents")+" %");		//5
			varList.add(vpd1);
			for(int i=0;i<reportList2.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", "￥ "+ reportList2.get(i).get("dept_name"));	//2
				vpd.put("var2", "￥ "+ reportList2.get(i).get("med").toString());	//4
				vpd.put("var3", "￥ "+ reportList2.get(i).get("drug").toString());		//5
				vpd.put("var4", reportList2.get(i).get("drug_persents")+" %");		//5
				vpd.put("var5", "￥ "+ reportList2.get(i).get("anti").toString());		//5
				vpd.put("var6", reportList2.get(i).get("anti_persents")+" %");		//5
				varList.add(vpd);
			}
			PageData vpd2 = new PageData();
			vpd2.put("var1", "门诊合计：");	//2
			vpd2.put("var2", "￥ "+count2.get("med_all").toString());	//4
			vpd2.put("var3", "￥ "+ count2.get("drug_all").toString());		//5
			vpd2.put("var4", count2.get("drug_persents")+" %");		//5
			vpd2.put("var5", "￥ "+ count2.get("anti_all").toString());		//5
			vpd2.put("var6", count2.get("anti_persents")+" %");		//5
			varList.add(vpd2);
			PageData allpd = new PageData();
			allpd.put("var1", "总合计：");	//2
			allpd.put("var2", "￥ "+ all.get("med_all").toString());	//4
			allpd.put("var3", "￥ "+ all.get("drug_all").toString());		//5
			allpd.put("var4", all.get("drug_persents")+" %");		//5
			allpd.put("var5", "￥ "+ all.get("anti_all").toString());		//5
			allpd.put("var6", all.get("anti_persents")+" %");		//5
			varList.add(allpd);
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
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
			mv.setViewName("DoctOrder/allHospital/allHospital2");
			mv.addObject("pd", pd);
			String beginDate = pd.getString("beginDate");	//开始时间
			String endDate = pd.getString("endDate");		//结束时间
			if(Tools.isEmpty(beginDate)&&Tools.isEmpty(endDate)){
				return mv;
			}
			page.setPd(pd);
			//门急诊药品费用统计
			List<PageData>	reportList = null;
			reportList = allHospitalService.allHospital2(page);
			mv.addObject("reportList", reportList);
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
	@RequestMapping(value="/allHospital2Export")
	public ModelAndView allHospital2Export(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			HelpUtil.setDefaultDate(pd);
			page.setPd(pd);
			Map<String,Object> dataMap = new HashMap<String,Object>();
			//门急诊药品费用统计
			page.setShowCount(1000);
			int TotalPage = 1;
			List<PageData>	varOList =  new ArrayList<PageData>();
			for(int pag = 1;pag<=TotalPage&&pag<=60;pag++){
				page.setCurrentPage(pag);
				List<PageData> mylist =  allHospitalService.allHospital2(page);
				if(mylist!=null){
					varOList.addAll(mylist);
				}
				TotalPage = page.getTotalPage();
			}
			List<PageData>	varList = new ArrayList<PageData>();
			List<String> titles = new ArrayList<String>();
			titles.add("排名");//2
			titles.add("药品名称");//2
			titles.add("规格");	//3
			titles.add("厂家");//2
			titles.add("金额");//2
			titles.add("使用量");//2
			dataMap.put("titles", titles);
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", i+1);	//2
				vpd.put("var2", varOList.get(i).get("drug_name"));	//2
				vpd.put("var3", varOList.get(i).get("drug_spec"));	//4
				vpd.put("var4", varOList.get(i).get("firm_id"));		//5
				vpd.put("var5", "￥ "+varOList.get(i).get("costs").toString());		//5
				vpd.put("var6", varOList.get(i).get("amounts").toString() + varOList.get(i).get("units").toString());		//5
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
