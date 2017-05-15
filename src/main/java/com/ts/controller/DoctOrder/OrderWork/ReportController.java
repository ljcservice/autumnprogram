package com.ts.controller.DoctOrder.OrderWork;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.DoctOrder.OrderWork.CommonService;
import com.ts.service.DoctOrder.OrderWork.IOrderWorkService;
import com.ts.service.DoctOrder.OrderWork.PrescService;
import com.ts.util.DateUtil;
import com.ts.util.MyDecimalFormat;
import com.ts.util.ObjectExcelView;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.doctor.DoctorConst;

@Controller
@RequestMapping(value="/report")
public class ReportController extends BaseController{
	@Resource(name="commonServicePdss")
	private CommonService commonService;
	@Autowired
	private PrescService prescService;
	@Autowired
	private IOrderWorkService orderWorkService;
	
	/**
	 * 医嘱列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/ordersReport")
	public ModelAndView orderReport()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			//处方问题统计
			List<PageData>	reportList = orderWorkService.ordersReport(pd);
			long total = 0;
			for(PageData p:reportList){
				Object count = p.get("count");
				total += Long.valueOf(count.toString());
			}
			for(PageData p:reportList){
				BigDecimal count =  (BigDecimal) p.get("count");
				if(count==null||count.doubleValue()==0){
					p.put("percent", "0.00 %");
				}else{
					BigDecimal percent = count.multiply(new BigDecimal(100)).divide(new BigDecimal(total),2, BigDecimal.ROUND_HALF_UP);
					String percentv = MyDecimalFormat.format(percent.doubleValue());
					p.put("percent", percentv +" %");
				}
			}
			mv.addObject("reportList", reportList);
			mv.addObject("checktypeMap", commonService.getCheckTypeDict()); 
			mv.setViewName("DoctOrder/report/orderReport");
			
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 导出到excel
	 * @return
	 */
	@RequestMapping(value="/ordersReportExport")
	public ModelAndView ordersReportExport(Page page){
		logBefore(logger, "导出orderListExport到excel");
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		page.setShowCount(1000);
		try{
			Map<String,PageData> map = commonService.getCheckTypeDict();
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("问题");//1
			titles.add("问题数");//2
			titles.add("问题占比");	//3
			dataMap.put("titles", titles);
			int TotalPage = 1;
			List<PageData> varList = null;
			//分批查询,最大查询2万条
			for(int pag = 1;pag<=TotalPage&&pag<=20;pag++){
				page.setCurrentPage(pag);
				List<PageData> varOList = orderWorkService.ordersReport(pd);
				long total = 0;
				for(PageData p:varOList){
					Object count = p.get("count");
					total += Long.valueOf(count.toString());
				}
				for(PageData p:varOList){
					BigDecimal count =  (BigDecimal) p.get("count");
					if(count==null||count.doubleValue()==0){
						p.put("percent", "0.00 %");
					}else{
						BigDecimal percent = count.multiply(new BigDecimal(100)).divide(new BigDecimal(total),2, BigDecimal.ROUND_HALF_UP);
						String percentv = MyDecimalFormat.format(percent.doubleValue());
						p.put("percent", percentv +" %");
					}
				}
						
				TotalPage = page.getTotalPage();
				if(varList==null){
					varList = new ArrayList<PageData>(TotalPage*page.getShowCount());
				}
				if(varOList!=null){
					for(int i=0;i<varOList.size();i++){
						PageData vpd = new PageData();
						vpd.put("var1", varOList.get(i).get("RS_TYPE_NAME")==null?"":varOList.get(i).get("RS_TYPE_NAME").toString());	//2
						vpd.put("var2", varOList.get(i).getDouble("count"));	//4
						vpd.put("var3", varOList.get(i).get("percent"));		//5
						varList.add(vpd);
					}
				}
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
	 * 医嘱报表-医嘱详细
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/orderList")
	public ModelAndView orderList(Page page){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		try
		{
			String beginDate = pd.getString("beginDate");		//开始时间
			String endDate = pd.getString("endDate");			//结束时间
			if(endDate != null && !"".equals(endDate))
			{
				pd.put("end_Date", endDate);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.setTime(DateUtil.fomatDate(endDate));
				cal.add(Calendar.DAY_OF_MONTH, 1);
				pd.put("endDate", sdf.format(cal.getTime()));
			}
			mv.addObject("pd", pd);
			page.setPd(pd);
			page.setShowCount(9999);
			List<PageData> entity =  this.orderWorkService.patientList(page);
			for(PageData pp:entity){
				String RS_DRUG_TYPES = pp.getString("RS_DRUG_TYPES");
				if(!Tools.isEmpty(RS_DRUG_TYPES)){
					String[] RS_DRUG_TYPE = RS_DRUG_TYPES.split("@;@");
					pp.put("RS_DRUG_TYPES", RS_DRUG_TYPE);
				}
				String DIAGNOSIS_DESC = pp.getString("DIAGNOSIS_DESC");
				if(!Tools.isEmpty(DIAGNOSIS_DESC)){
					String[] DIAGNOSIS_DESCS = DIAGNOSIS_DESC.split("@;@");
					pp.put("DIAGNOSIS_DESC", DIAGNOSIS_DESCS);
				}
			}
			mv.addObject("rstypeMap", DoctorConst.rstypeMap); 
			mv.addObject("rstypeColorMap", DoctorConst.rstypeColorMap); 
			mv.addObject("checktypeMap", commonService.getCheckTypeDict()); 
			mv.addObject("patVisits", entity);
		}catch(Exception e )
		{
			logger.error(e.toString(), e);
		}
		mv.setViewName("DoctOrder/report/orderList");
		return  mv; 
	}
	/**
	 * 医嘱医生维度报表
	 * @return
	 */
	@RequestMapping(value="/orderListByDoctor")
	public ModelAndView orderListByDoctor(){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		try {
			List<PageData> list =  this.orderWorkService.orderListByDoctor(pd);
			long total = 0;
			for(PageData p:list){
				Object count = p.get("count");
				total += Long.valueOf(count.toString());
			}
			for(PageData p:list){
				BigDecimal count =  (BigDecimal) p.get("count");
				if(count==null||count.doubleValue()==0){
					p.put("percent", "0.00 %");
				}else{
					BigDecimal percent = count.multiply(new BigDecimal(100)).divide(new BigDecimal(total),2, BigDecimal.ROUND_HALF_UP);
					String percentv = MyDecimalFormat.format(percent.doubleValue());
					p.put("percent", percentv +" %");
				}
			}
			mv.addObject("pd", pd);
			mv.addObject("resultList", list);
			mv.addObject("checktypeMap", commonService.getCheckTypeDict()); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("DoctOrder/report/orderListByDoctor");
		return  mv; 
	}
	/**
	 * 医嘱 出院科室报表
	 * @return
	 */
	@RequestMapping(value="/orderListByDep")
	public ModelAndView orderListByDep(){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		try {
			List<PageData> list =  this.orderWorkService.orderListByDep(pd);
			long total = 0;
			for(PageData p:list){
				Object count = p.get("count");
				total += Long.valueOf(count.toString());
			}
			for(PageData p:list){
				BigDecimal count =  (BigDecimal) p.get("count");
				if(count==null||count.doubleValue()==0){
					p.put("percent", "0.00 %");
				}else{
					BigDecimal percent = count.multiply(new BigDecimal(100)).divide(new BigDecimal(total),2, BigDecimal.ROUND_HALF_UP);
					String percentv = MyDecimalFormat.format(percent.doubleValue());
					p.put("percent", percentv +" %");
				}
			}
			mv.addObject("pd", pd);
			mv.addObject("resultList", list);
			mv.addObject("checktypeMap", commonService.getCheckTypeDict()); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("DoctOrder/report/orderListByDep");
		return  mv; 
	}
	
	
	
/*	-------------------------------------------------------------   */
	/**
	 * 处方报表列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/prescReport")
	public ModelAndView prescReport()throws Exception{
//		PrescService s = (PrescService) ApplicationUtil.getBean("prescService");
//		System.out.println(s.prescListPage(new Page()));
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			// 当前登录专家
			mv.addObject("pd", pd);
			//处方问题统计
			List<PageData>	reportList = prescService.prescReport(pd);
			long total = 0;
			for(PageData p:reportList){
				Object count = p.get("count");
				total += Long.valueOf(count.toString());
			}
			for(PageData p:reportList){
				BigDecimal count =  (BigDecimal) p.get("count");
				if(count==null||count.doubleValue()==0){
					p.put("percent", "0.00 %");
				}else{
					BigDecimal percent = count.multiply(new BigDecimal(100)).divide(new BigDecimal(total),2, BigDecimal.ROUND_HALF_UP);
					String percentv = MyDecimalFormat.format(percent.doubleValue());
					p.put("percent", percentv +" %");
				}
			}
			mv.addObject("reportList", reportList);
			mv.addObject("checktypeMap", commonService.getCheckTypeDict()); 
			mv.setViewName("DoctOrder/report/prescReport");
			
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 * 处方报表列表导出
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/prescReportExport")
	public ModelAndView prescReportExport(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		page.setShowCount(1000);
		try{
			Map<String,PageData> map = commonService.getCheckTypeDict();
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("问题");//1
			titles.add("问题数");//2
			titles.add("问题占比");	//3
			dataMap.put("titles", titles);
			int TotalPage = 1;
			List<PageData> varList = null;
			//分批查询,最大查询2万条
			for(int pag = 1;pag<=TotalPage&&pag<=20;pag++){
				page.setCurrentPage(pag);
				List<PageData> varOList = prescService.prescReport(pd);
				long total = 0;
				for(PageData p:varOList){
					Object count = p.get("count");
					total += Long.valueOf(count.toString());
				}
				for(PageData p:varOList){
					BigDecimal count =  (BigDecimal) p.get("count");
					if(count==null||count.doubleValue()==0){
						p.put("percent", "0.00 %");
					}else{
						BigDecimal percent = count.multiply(new BigDecimal(100)).divide(new BigDecimal(total),2, BigDecimal.ROUND_HALF_UP);
						String percentv = MyDecimalFormat.format(percent.doubleValue());
						p.put("percent", percentv +" %");
					}
				}
						
				TotalPage = page.getTotalPage();
				if(varList==null){
					varList = new ArrayList<PageData>(TotalPage*page.getShowCount());
				}
				if(varOList!=null){
					for(int i=0;i<varOList.size();i++){
						PageData vpd = new PageData();
						vpd.put("var1", varOList.get(i).get("RS_TYPE_NAME")==null?"":varOList.get(i).get("RS_TYPE_NAME").toString());	//2
						vpd.put("var2", varOList.get(i).getDouble("count"));	//4
						vpd.put("var3", varOList.get(i).get("percent"));		//5
						varList.add(vpd);
					}
				}
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
	 * 处方报表-处方详细
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/prescList")
	public ModelAndView prescList(Page page){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		try
		{
			String keywords = pd.getString("keywords");			//关键词检索条件
			String beginDate = pd.getString("beginDate");		//开始时间
			String endDate = pd.getString("endDate");			//结束时间
			if(endDate != null && !"".equals(endDate))
			{
				pd.put("end_Date", endDate);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.setTime(DateUtil.fomatDate(endDate));
				cal.add(Calendar.DAY_OF_MONTH, 1);
				pd.put("endDate", sdf.format(cal.getTime()));
			}
			page.setPd(pd);
			List<PageData>	prescList = prescService.prescListPage(page);	//列出专家列表
			for(PageData pp:prescList){
				String RS_DRUG_TYPES = pp.getString("RS_DRUG_TYPES");
				if(!Tools.isEmpty(RS_DRUG_TYPES)){
					String[] RS_DRUG_TYPE = RS_DRUG_TYPES.split("@;@");
					pp.put("RS_DRUG_TYPES", RS_DRUG_TYPE);
				}
			}
			mv.addObject("rstypeMap", DoctorConst.rstypeMap); 
			mv.addObject("rstypeColorMap", DoctorConst.rstypeColorMap); 
			mv.addObject("checktypeMap", commonService.getCheckTypeDict()); 
			mv.addObject("prescList", prescList);
			mv.setViewName("DoctOrder/presc/prescWorkList");
			mv.addObject("pd", pd);
		}catch(Exception e )
		{
			logger.error(e.toString(), e);
		}
		mv.setViewName("DoctOrder/report/prescList");
		return  mv; 
	}
	/**
	 * 处方医生维度报表
	 * @return
	 */
	@RequestMapping(value="/prescListByDoctor")
	public ModelAndView prescListByDoctor(){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		try {
			List<PageData> list =  this.prescService.prescListByDoctor(pd);
			long total = 0;
			for(PageData p:list){
				Object count = p.get("count");
				total += Long.valueOf(count.toString());
			}
			for(PageData p:list){
				BigDecimal count =  (BigDecimal) p.get("count");
				if(count==null||count.doubleValue()==0){
					p.put("percent", "0.00 %");
				}else{
					BigDecimal percent = count.multiply(new BigDecimal(100)).divide(new BigDecimal(total),2, BigDecimal.ROUND_HALF_UP);
					String percentv = MyDecimalFormat.format(percent.doubleValue());
					p.put("percent", percentv +" %");
				}
			}
			mv.addObject("pd", pd);
			mv.addObject("resultList", list);
			mv.addObject("checktypeMap", commonService.getCheckTypeDict()); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("DoctOrder/report/prescListByDoctor");
		return  mv; 
	}
	/**
	 * 处方 出院科室报表
	 * @return
	 */
	@RequestMapping(value="/prescListByDep")
	public ModelAndView prescListByDep(){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		try {
			List<PageData> list =  this.prescService.prescListByDep(pd);
			long total = 0;
			for(PageData p:list){
				Object count = p.get("count");
				total += Long.valueOf(count.toString());
			}
			for(PageData p:list){
				BigDecimal count =  (BigDecimal) p.get("count");
				if(count==null||count.doubleValue()==0){
					p.put("percent", "0.00 %");
				}else{
					BigDecimal percent = count.multiply(new BigDecimal(100)).divide(new BigDecimal(total),2, BigDecimal.ROUND_HALF_UP);
					String percentv = MyDecimalFormat.format(percent.doubleValue());
					p.put("percent", percentv +" %");
				}
			}
			mv.addObject("pd", pd);
			mv.addObject("resultList", list);
			mv.addObject("checktypeMap", commonService.getCheckTypeDict()); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("DoctOrder/report/prescListByDep");
		return  mv; 
	}
	
	/**
	 * 门(急)诊处方点评汇总
	 * @return
	 */
	@RequestMapping(value="/prescStatistics")
	public ModelAndView prescStatistics(){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		try {
			
			mv.addObject("pd", pd);
			PageData p1 =  this.prescService.prescStatistics1(pd);
			BigDecimal HASKJ_SUM = (BigDecimal) p1.get("HASKJ_SUM");
			BigDecimal HASZS_SUM = (BigDecimal) p1.get("HASZS_SUM");
			BigDecimal CHECKFALSE = (BigDecimal) p1.get("CHECKFALSE");
			BigDecimal CHECKTRUE = (BigDecimal) p1.get("CHECKTRUE");
			BigDecimal CHECKPEND = (BigDecimal) p1.get("CHECKPEND");
			BigDecimal COUNT = (BigDecimal) p1.get("COUNT");
			//计算百分比  
			if(COUNT==null||COUNT.doubleValue()==0){
				p1.put("HASKJ_PERSENTS",0);
				p1.put("HASZS_PERSENTS",0);
				p1.put("CHECKFALSE_PERSENTS",0);
				p1.put("CHECKTRUE_PERSENTS",0);
				p1.put("CHECKPEND_PERSENTS",0);
			}else{
				p1.put("HASKJ_PERSENTS", MyDecimalFormat.format(HASKJ_SUM.divide(COUNT,4,4).doubleValue()*100));
				p1.put("HASZS_PERSENTS", MyDecimalFormat.format(HASZS_SUM.divide(COUNT,4,4).doubleValue()*100));
				p1.put("CHECKFALSE_PERSENTS",MyDecimalFormat.format(CHECKFALSE.divide(COUNT,4,4).doubleValue()*100));
				p1.put("CHECKTRUE_PERSENTS", MyDecimalFormat.format(CHECKTRUE.divide(COUNT,4,4).doubleValue()*100));
				p1.put("CHECKPEND_PERSENTS", MyDecimalFormat.format(CHECKPEND.divide(COUNT,4,4).doubleValue()*100));
			}
			//国家基本药物品种百分比
			BigDecimal BASEDRUG_COUNT_SUM = (BigDecimal) p1.get("BASEDRUG_COUNT_SUM");
			BigDecimal DRUG_COUNT_SUM = (BigDecimal) p1.get("DRUG_COUNT_SUM");
			if(DRUG_COUNT_SUM==null||DRUG_COUNT_SUM.doubleValue()==0){
				p1.put("BASEDRUG_COUNT_PERSENTS", 0);
			}else {
				p1.put("BASEDRUG_COUNT_PERSENTS", MyDecimalFormat.format(BASEDRUG_COUNT_SUM.divide(DRUG_COUNT_SUM,4,4).doubleValue()*100));
			}
			
			//抗菌药金额比例百分比(
			BigDecimal ANTIDRUGCOSTS_SUM = (BigDecimal) p1.get("ANTIDRUGCOSTS_SUM");
			BigDecimal AMOUNT_SUM = (BigDecimal) p1.get("AMOUNT_SUM");
			if(AMOUNT_SUM==null||AMOUNT_SUM.doubleValue()==0){
				p1.put("ANTIDRUGCOSTS_PERSENTS", 0);
			}else{
				p1.put("ANTIDRUGCOSTS_PERSENTS", MyDecimalFormat.format(ANTIDRUGCOSTS_SUM.divide(AMOUNT_SUM,4,4).doubleValue()*100));
			}
			
			PageData p2 =  this.prescService.prescStatistics2(pd);
			BigDecimal DRUG_COUNT = (BigDecimal) p2.get("DRUG_COUNT");
			BigDecimal ONE_LEVEL = (BigDecimal) p2.get("ONE_LEVEL");
			BigDecimal TWO_LEVEL = (BigDecimal) p2.get("TWO_LEVEL");
			BigDecimal THREE_LEVEL = (BigDecimal) p2.get("THREE_LEVEL");
			if(DRUG_COUNT==null||DRUG_COUNT.doubleValue()==0){
				p2.put("ONE_LEVEL_PERSENTS",0);
				p2.put("TWO_LEVEL_PERSENTS",0);
				p2.put("THREE_LEVEL_PERSENTS",0);
			}else{
				p2.put("ONE_LEVEL_PERSENTS", MyDecimalFormat.format(ONE_LEVEL.divide(DRUG_COUNT,4,4).doubleValue()*100));
				p2.put("TWO_LEVEL_PERSENTS", MyDecimalFormat.format(TWO_LEVEL.divide(DRUG_COUNT,4,4).doubleValue()*100));
				p2.put("THREE_LEVEL_PERSENTS", MyDecimalFormat.format(THREE_LEVEL.divide(DRUG_COUNT,4,4).doubleValue()*100));
			}
			BigDecimal MAXUSEDAY_SUM = (BigDecimal) p1.get("MAXUSEDAY_SUM");
			//平均每张处方金额：
			//计算百分比  
			if(COUNT==null||COUNT.doubleValue()==0){
				p1.put("AMOUNT_AVG",0);
				p1.put("MAXUSEDAY_AVG", 0);
				p1.put("BASEDRUG_COUNT_AVG", 0);
			}else {
				p1.put("AMOUNT_AVG", MyDecimalFormat.format(AMOUNT_SUM.divide(COUNT,4,4).doubleValue()*100));
				p1.put("MAXUSEDAY_AVG", MyDecimalFormat.format(MAXUSEDAY_SUM.divide(COUNT,4,4).doubleValue()*100));
				p1.put("BASEDRUG_COUNT_AVG", MyDecimalFormat.format(BASEDRUG_COUNT_SUM.divide(COUNT,4,4).doubleValue()*100));
			}
			
			PageData p3 =  this.prescService.prescStatistics3(pd);
			BigDecimal PATIENT_ID_COUNT = (BigDecimal) p3.get("PATIENT_ID_COUNT");
			if(PATIENT_ID_COUNT==null||PATIENT_ID_COUNT.doubleValue()==0){
				p3.put("PATIENT_ID_AVG",0);
			}else {
				p3.put("PATIENT_ID_AVG", MyDecimalFormat.format(BASEDRUG_COUNT_SUM.divide(PATIENT_ID_COUNT,4,4).doubleValue()*100));
			}
			
			mv.addObject("p1", p1);
			mv.addObject("p2", p2);
			mv.addObject("p3", p3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("DoctOrder/report/prescStatistics");
		return  mv; 
	}
	
	/**
	 * 门(急)诊处方点评汇总,导出
	 * @return
	 */
	@RequestMapping(value="/prescStatisticsExport")
	public void prescStatisticsExport(HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		try {
			
			mv.addObject("pd", pd);
			PageData p1 =  this.prescService.prescStatistics1(pd);
			BigDecimal HASKJ_SUM = (BigDecimal) p1.get("HASKJ_SUM");
			BigDecimal HASZS_SUM = (BigDecimal) p1.get("HASZS_SUM");
			BigDecimal CHECKFALSE = (BigDecimal) p1.get("CHECKFALSE");
			BigDecimal CHECKTRUE = (BigDecimal) p1.get("CHECKTRUE");
			BigDecimal CHECKPEND = (BigDecimal) p1.get("CHECKPEND");
			BigDecimal COUNT = (BigDecimal) p1.get("COUNT");
			//计算百分比  
			if(COUNT==null||COUNT.doubleValue()==0){
				p1.put("HASKJ_PERSENTS",0);
				p1.put("HASZS_PERSENTS",0);
				p1.put("CHECKFALSE_PERSENTS",0);
				p1.put("CHECKTRUE_PERSENTS",0);
				p1.put("CHECKPEND_PERSENTS",0);
			}else{
				p1.put("HASKJ_PERSENTS", MyDecimalFormat.format(HASKJ_SUM.divide(COUNT,4,4).doubleValue()*100));
				p1.put("HASZS_PERSENTS", MyDecimalFormat.format(HASZS_SUM.divide(COUNT,4,4).doubleValue()*100));
				p1.put("CHECKFALSE_PERSENTS",MyDecimalFormat.format(CHECKFALSE.divide(COUNT,4,4).doubleValue()*100));
				p1.put("CHECKTRUE_PERSENTS", MyDecimalFormat.format(CHECKTRUE.divide(COUNT,4,4).doubleValue()*100));
				p1.put("CHECKPEND_PERSENTS", MyDecimalFormat.format(CHECKPEND.divide(COUNT,4,4).doubleValue()*100));
			}
			//国家基本药物品种百分比
			BigDecimal BASEDRUG_COUNT_SUM = (BigDecimal) p1.get("BASEDRUG_COUNT_SUM");
			BigDecimal DRUG_COUNT_SUM = (BigDecimal) p1.get("DRUG_COUNT_SUM");
			if(DRUG_COUNT_SUM==null||DRUG_COUNT_SUM.doubleValue()==0){
				p1.put("BASEDRUG_COUNT_PERSENTS", 0);
			}else {
				p1.put("BASEDRUG_COUNT_PERSENTS", MyDecimalFormat.format(BASEDRUG_COUNT_SUM.divide(DRUG_COUNT_SUM,4,4).doubleValue()*100));
			}
			
			//抗菌药金额比例百分比(
			BigDecimal ANTIDRUGCOSTS_SUM = (BigDecimal) p1.get("ANTIDRUGCOSTS_SUM");
			BigDecimal AMOUNT_SUM = (BigDecimal) p1.get("AMOUNT_SUM");
			if(AMOUNT_SUM==null||AMOUNT_SUM.doubleValue()==0){
				p1.put("ANTIDRUGCOSTS_PERSENTS", 0);
			}else{
				p1.put("ANTIDRUGCOSTS_PERSENTS", MyDecimalFormat.format(ANTIDRUGCOSTS_SUM.divide(AMOUNT_SUM,4,4).doubleValue()*100));
			}
			
			PageData p2 =  this.prescService.prescStatistics2(pd);
			BigDecimal DRUG_COUNT = (BigDecimal) p2.get("DRUG_COUNT");
			BigDecimal ONE_LEVEL = (BigDecimal) p2.get("ONE_LEVEL");
			BigDecimal TWO_LEVEL = (BigDecimal) p2.get("TWO_LEVEL");
			BigDecimal THREE_LEVEL = (BigDecimal) p2.get("THREE_LEVEL");
			if(DRUG_COUNT==null||DRUG_COUNT.doubleValue()==0){
				p2.put("ONE_LEVEL_PERSENTS",0);
				p2.put("TWO_LEVEL_PERSENTS",0);
				p2.put("THREE_LEVEL_PERSENTS",0);
			}else{
				p2.put("ONE_LEVEL_PERSENTS", MyDecimalFormat.format(ONE_LEVEL.divide(DRUG_COUNT,4,4).doubleValue()*100));
				p2.put("TWO_LEVEL_PERSENTS", MyDecimalFormat.format(TWO_LEVEL.divide(DRUG_COUNT,4,4).doubleValue()*100));
				p2.put("THREE_LEVEL_PERSENTS", MyDecimalFormat.format(THREE_LEVEL.divide(DRUG_COUNT,4,4).doubleValue()*100));
			}
			BigDecimal MAXUSEDAY_SUM = (BigDecimal) p1.get("MAXUSEDAY_SUM");
			//平均每张处方金额：
			//计算百分比  
			if(COUNT==null||COUNT.doubleValue()==0){
				p1.put("AMOUNT_AVG",0);
				p1.put("MAXUSEDAY_AVG", 0);
				p1.put("BASEDRUG_COUNT_AVG", 0);
			}else {
				p1.put("AMOUNT_AVG", MyDecimalFormat.format(AMOUNT_SUM.divide(COUNT,4,4).doubleValue()*100));
				p1.put("MAXUSEDAY_AVG", MyDecimalFormat.format(MAXUSEDAY_SUM.divide(COUNT,4,4).doubleValue()*100));
				p1.put("BASEDRUG_COUNT_AVG", MyDecimalFormat.format(BASEDRUG_COUNT_SUM.divide(COUNT,4,4).doubleValue()*100));
			}
			
			PageData p3 =  this.prescService.prescStatistics3(pd);
			BigDecimal PATIENT_ID_COUNT = (BigDecimal) p3.get("PATIENT_ID_COUNT");
			if(PATIENT_ID_COUNT==null||PATIENT_ID_COUNT.doubleValue()==0){
				p3.put("PATIENT_ID_AVG",0);
			}else {
				p3.put("PATIENT_ID_AVG", MyDecimalFormat.format(BASEDRUG_COUNT_SUM.divide(PATIENT_ID_COUNT,4,4).doubleValue()*100));
			}
			
			//构建EXCEL
			HSSFWorkbook workbook = new HSSFWorkbook();
			Sheet sheet = workbook.createSheet();
			Row titileRow = sheet.createRow(0);
			titileRow.createCell(0).setCellValue("统计指标");
			titileRow.createCell(1).setCellValue("数量");
			titileRow.createCell(2).setCellValue("总数");
			titileRow.createCell(3).setCellValue("比率");
			Row row1 = sheet.createRow(1);
			row1.createCell(0).setCellValue("抗菌药处方百分比(抗菌药处方数/处方总数)：");
			row1.createCell(1).setCellValue( p1.get("HASKJ_SUM").toString() );
			row1.createCell(2).setCellValue(p1.get("COUNT").toString() );
			row1.createCell(3).setCellValue(p1.get("HASKJ_PERSENTS").toString() +"%");
			
			Row row2 = sheet.createRow(2);
			row2.createCell(0).setCellValue("注射剂处方百分比(注射剂处方数/处方总数)：");
			row2.createCell(1).setCellValue( p1.get("HASZS_SUM").toString() );
			row2.createCell(2).setCellValue(p1.get("COUNT").toString() );
			row2.createCell(3).setCellValue(p1.get("HASZS_PERSENTS").toString() +"%");
			
			Row row3 = sheet.createRow(3);
			row3.createCell(0).setCellValue("国家基本药物品种百分比(国家基本药物数/使用药物总数)：");
			row3.createCell(1).setCellValue( p1.get("BASEDRUG_COUNT_SUM").toString() );
			row3.createCell(2).setCellValue(p1.get("DRUG_COUNT_SUM").toString() );
			row3.createCell(3).setCellValue(p1.get("BASEDRUG_COUNT_PERSENTS").toString()+"%" );
			
			Row row4 = sheet.createRow(4);
			row4.createCell(0).setCellValue("合理处方百分比(不合格处方数/处方总数)：");
			row4.createCell(1).setCellValue( p1.get("CHECKTRUE").toString() );
			row4.createCell(2).setCellValue(p1.get("COUNT").toString() );
			row4.createCell(3).setCellValue(p1.get("CHECKTRUE_PERSENTS").toString() +"%");
			
			Row row5 = sheet.createRow(5);
			row5.createCell(0).setCellValue("不合理处方百分比(不合格处方数/处方总数)：");
			row5.createCell(1).setCellValue( p1.get("CHECKFALSE").toString() );
			row5.createCell(2).setCellValue(p1.get("COUNT").toString() );
			row5.createCell(3).setCellValue(p1.get("CHECKFALSE_PERSENTS").toString()+"%");
			
			Row row6 = sheet.createRow(6);
			row6.createCell(0).setCellValue("待定处方百分比(待定处方数/处方总数)：");
			row6.createCell(1).setCellValue( p1.get("CHECKPEND").toString() );
			row6.createCell(2).setCellValue(p1.get("COUNT").toString() );
			row6.createCell(3).setCellValue(p1.get("CHECKPEND_PERSENTS").toString()+"%" );
			
			Row row7 = sheet.createRow(7);
			row7.createCell(0).setCellValue("非限制级抗菌药百分比(非限制抗菌药/抗菌药总数)：");
			row7.createCell(1).setCellValue( p2.get("ONE_LEVEL").toString() );
			row7.createCell(2).setCellValue(p2.get("DRUG_COUNT").toString() );
			row7.createCell(3).setCellValue(p2.get("ONE_LEVEL_PERSENTS").toString()+"%" );
			
			Row row8 = sheet.createRow(8);
			row8.createCell(0).setCellValue("限制级抗菌药百分比（限制抗菌药/抗菌药总数)：");
			row8.createCell(1).setCellValue( p2.get("TWO_LEVEL").toString() );
			row8.createCell(2).setCellValue(p2.get("DRUG_COUNT").toString() );
			row8.createCell(3).setCellValue(p2.get("TWO_LEVEL_PERSENTS").toString()+"%" );

			Row row9 = sheet.createRow(9);
			row9.createCell(0).setCellValue("特殊级抗菌药百分比(特殊级抗菌药/抗菌药总数)：");
			row9.createCell(1).setCellValue( p2.get("THREE_LEVEL").toString() );
			row9.createCell(2).setCellValue(p2.get("DRUG_COUNT").toString() );
			row9.createCell(3).setCellValue(p2.get("THREE_LEVEL_PERSENTS").toString()+"%" );

			Row row10 = sheet.createRow(10);
			row10.createCell(0).setCellValue("抗菌药金额比例百分比(使用抗菌药总金额/使用药品总金额)：");
			row10.createCell(1).setCellValue( p1.get("ANTIDRUGCOSTS_SUM").toString() );
			row10.createCell(2).setCellValue(p1.get("AMOUNT_SUM").toString() );
			row10.createCell(3).setCellValue(p1.get("ANTIDRUGCOSTS_PERSENTS").toString()+"%" );
			
			Row row11 = sheet.createRow(11);//空白行
			
			Row row12 = sheet.createRow(12);
			row12.createCell(0).setCellValue("统计指标");
			row12.createCell(1).setCellValue("总数");
			row12.createCell(2).setCellValue("总处方数");
			row12.createCell(3).setCellValue("平均值");
			
			Row row13 = sheet.createRow(13);
			row13.createCell(0).setCellValue("平均每张处方用药品种数：");
			row13.createCell(1).setCellValue( p1.get("BASEDRUG_COUNT_SUM").toString() );
			row13.createCell(2).setCellValue(p1.get("COUNT").toString() );
			row13.createCell(3).setCellValue(p1.get("BASEDRUG_COUNT_AVG").toString() );
			
			Row row14 = sheet.createRow(14);
			row14.createCell(0).setCellValue("平均每张处方金额：");
			row14.createCell(1).setCellValue( p1.get("AMOUNT_SUM").toString() );
			row14.createCell(2).setCellValue(p1.get("COUNT").toString() );
			row14.createCell(3).setCellValue(p1.get("AMOUNT_AVG").toString() );
			
			Row row15 = sheet.createRow(15);
			row15.createCell(0).setCellValue("平均处方用药天数：");
			row15.createCell(1).setCellValue( p1.get("MAXUSEDAY_SUM").toString() );
			row15.createCell(2).setCellValue(p1.get("COUNT").toString() );
			row15.createCell(3).setCellValue(p1.get("MAXUSEDAY_AVG").toString() );
			
			Row row16 = sheet.createRow(16);//空白行
			
			Row row17 = sheet.createRow(17);
			row17.createCell(0).setCellValue("统计指标");
			row17.createCell(1).setCellValue("总数");
			row17.createCell(2).setCellValue("总人次数");
			row17.createCell(3).setCellValue("平均值");
			
			Row row18 = sheet.createRow(18);
			row18.createCell(0).setCellValue("平均每人次处方金额：");
			row18.createCell(1).setCellValue( p1.getDouble("AMOUNT_SUM") );
			row18.createCell(2).setCellValue(p3.getDouble("PATIENT_ID_COUNT") );
			row18.createCell(3).setCellValue(p3.getDouble("PATIENT_ID_AVG") );
			
			
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-disposition", "attachment; " + "filename="
					+ "prescExport" + ".xlsx" );
			OutputStream response_out = response.getOutputStream();
			workbook.write(response_out);
			response_out.flush();
			response_out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 处方超常规统计（科室）
	 * @return
	 */
	@RequestMapping(value="/exceedCommonDep")
	public ModelAndView exceedCommonDep(){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("pd", pd);
		try {
			//超常规统计（科室）按照问题类型分组
			List<PageData> list =  this.prescService.exceedCommonDep(pd);
			//按照医生分组
			Map<String,PageData> map = new HashMap<String,PageData>();
			for(PageData px:list){
				String ORG_CODE = px.getString("ORG_CODE");
				map.put(ORG_CODE, px);
			}
			BigDecimal checkfalse_sum_all = new BigDecimal(0);//不合格处方数汇总
			BigDecimal presc_count_all =  new BigDecimal(0);//总处方统计-汇总
			//（科室分组）不合格处方数,总处方统计
			List<PageData> countlist =  this.prescService.prescCountDep(pd);
			for(PageData py:countlist){
				String ORG_CODE = py.getString("ORG_CODE");
				PageData px = map.get(ORG_CODE);
				px.put("checkfalse_sum", py.get("checkfalse_sum"));
				px.put("presc_count", py.get("presc_count"));
				BigDecimal checkfalse_sum = (BigDecimal) py.get("checkfalse_sum");
				BigDecimal presc_count = (BigDecimal) py.get("presc_count");
				checkfalse_sum_all= checkfalse_sum_all.add(checkfalse_sum);
				presc_count_all = presc_count_all.add(presc_count);
			}
			//汇总统计
			PageData all = this.prescService.exceedCommonAll(pd);
			all.put("checkfalse_sum_all", checkfalse_sum_all);
			all.put("presc_count_all", presc_count_all);
			mv.addObject("all", all);
			
			//计算平均值
			for(PageData px:list){
				if(checkfalse_sum_all==null||checkfalse_sum_all.doubleValue()==0){
					px.put("checkfalse_persents1", 0);
				}else{
					String checkfalse_persents1 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(checkfalse_sum_all,4,4).doubleValue()*100);
					px.put("checkfalse_persents1", checkfalse_persents1);
				}
				BigDecimal presc_count = (BigDecimal) px.get("presc_count");
				if(presc_count==null||presc_count.doubleValue()==0){
					px.put("checkfalse_persents2", 0);
				}else{
					String checkfalse_persents2 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(presc_count,4,4).doubleValue()*100);
					px.put("checkfalse_persents2", checkfalse_persents2);
				}
			}
			
			mv.addObject("resultList", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("DoctOrder/report/exceedCommonDep");
		return  mv; 
	}
	/**
	 * 处方超常规统计（科室）导出 excel
	 * @return
	 */
	@RequestMapping(value="/exceedCommonDepExport")
	public ModelAndView exceedCommonDepExport(){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("pd", pd);
		try {
			//超常规统计（科室）按照问题类型分组
			List<PageData> list =  this.prescService.exceedCommonDep(pd);
			//按照医生分组
			Map<String,PageData> map = new HashMap<String,PageData>();
			for(PageData px:list){
				String ORG_CODE = px.getString("ORG_CODE");
				map.put(ORG_CODE, px);
			}
			BigDecimal checkfalse_sum_all = new BigDecimal(0);//不合格处方数汇总
			BigDecimal presc_count_all =  new BigDecimal(0);//总处方统计-汇总
			//（科室分组）不合格处方数,总处方统计
			List<PageData> countlist =  this.prescService.prescCountDep(pd);
			for(PageData py:countlist){
				String ORG_CODE = py.getString("ORG_CODE");
				PageData px = map.get(ORG_CODE);
				px.put("checkfalse_sum", py.get("checkfalse_sum"));
				px.put("presc_count", py.get("presc_count"));
				BigDecimal checkfalse_sum = (BigDecimal) py.get("checkfalse_sum");
				BigDecimal presc_count = (BigDecimal) py.get("presc_count");
				checkfalse_sum_all= checkfalse_sum_all.add(checkfalse_sum);
				presc_count_all = presc_count_all.add(presc_count);
			}
			//汇总统计
			PageData all = this.prescService.exceedCommonAll(pd);
			all.put("checkfalse_sum_all", checkfalse_sum_all);
			all.put("presc_count_all", presc_count_all);
			//计算平均值
			for(PageData px:list){
				if(checkfalse_sum_all==null||checkfalse_sum_all.doubleValue()==0){
					px.put("checkfalse_persents1", 0);
				}else{
					String checkfalse_persents1 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(checkfalse_sum_all,4,4).doubleValue()*100);
					px.put("checkfalse_persents1", checkfalse_persents1);
				}
				BigDecimal presc_count = (BigDecimal) px.get("presc_count");
				if(presc_count==null||presc_count.doubleValue()==0){
					px.put("checkfalse_persents2", 0);
				}else{
					String checkfalse_persents2 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(presc_count,4,4).doubleValue()*100);
					px.put("checkfalse_persents2", checkfalse_persents2);
				}
			}
			
			//导出excel
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("科室");//1
			titles.add("用法用量");//2
			titles.add("禁忌症");	//3
			titles.add("重复给药 	");	//4
			titles.add("相互作用");	//5
			titles.add("配伍禁忌");	//5
			titles.add("不良反应");	//5
			titles.add("给药途径");	//5
			titles.add("特殊人群");	//5
			titles.add("管理");	//5
			titles.add("不合格医嘱数");	//5
			titles.add("医嘱总医嘱数");	//5
			titles.add("不合格医嘱数占总不合格医嘱百分比");	//5
			titles.add("不合格医嘱数占（查询日期内）医生总医嘱数");	//5
			dataMap.put("titles", titles);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<list.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", list.get(i).getString("ORG_NAME"));		//1
				vpd.put("var2", list.get(i).getDouble("DOSAGE_SUM"));	//2
				vpd.put("var3", list.get(i).getDouble("DIAGINFO_SUM"));	//3
				vpd.put("var4", list.get(i).getDouble("INGREDIEN_SUM"));	//4
				vpd.put("var5", list.get(i).getDouble("INTERACTION_SUM"));	//4
				vpd.put("var6", list.get(i).getDouble("IV_EFFECT_SUM"));	//4
				vpd.put("var7", list.get(i).getDouble("SIDE_SUM"));	//4
				vpd.put("var8", list.get(i).getDouble("ADMINISTRATOR_SUM"));	//4
				vpd.put("var9", list.get(i).getDouble("SPECPEOPLE_SUM"));	//4
				vpd.put("var10", list.get(i).getDouble("MANAGER_SUM"));	//4
				vpd.put("var11", list.get(i).getDouble("CHECKFALSE_SUM"));	//4
				vpd.put("var12", list.get(i).getDouble("PRESC_COUNT"));	//4
				vpd.put("var13", list.get(i).get("CHECKFALSE_PERSENTS1").toString() + "%");	//4
				vpd.put("var14", list.get(i).get("CHECKFALSE_PERSENTS2").toString() + "%");	//4
				varList.add(vpd);
			}
			PageData vpd = new PageData();
			vpd.put("var1", "总计：");		//1
			vpd.put("var2", all.getDouble("DOSAGE_SUM"));
			vpd.put("var3", all.getDouble("DIAGINFO_SUM"));	//3
			vpd.put("var4", all.getDouble("INGREDIEN_SUM"));	//4
			vpd.put("var5", all.getDouble("INTERACTION_SUM"));	//4
			vpd.put("var6", all.getDouble("IV_EFFECT_SUM"));	//4
			vpd.put("var7", all.getDouble("SIDE_SUM"));	//4
			vpd.put("var8", all.getDouble("ADMINISTRATOR_SUM"));	//4
			vpd.put("var9", all.getDouble("SPECPEOPLE_SUM"));	//4
			vpd.put("var10", all.getDouble("MANAGER_SUM"));	//4
			vpd.put("var11", all.getDouble("CHECKFALSE_SUM_ALL"));	//4
			vpd.put("var12", all.getDouble("PRESC_COUNT_ALL"));	//4
			vpd.put("var13", "");	//4
			vpd.put("var14", "");	//4
			varList.add(vpd);
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  mv; 
	}
	/**
	 * 处方超常规统计（医生）
	 * @return
	 */
	@RequestMapping(value="/exceedCommonDoctor")
	public ModelAndView exceedCommonDoctor(){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("pd", pd);
		try {
			//超常规统计（医生）按照问题类型分组
			List<PageData> list =  this.prescService.exceedCommonDoctor(pd);
			//按照医生分组
			Map<String,PageData> map = new HashMap<String,PageData>();
			for(PageData px:list){
				String DOCTOR_CODE = px.getString("DOCTOR_CODE");
				map.put(DOCTOR_CODE, px);
			}
			BigDecimal checkfalse_sum_all = new BigDecimal(0);//不合格处方数汇总
			BigDecimal presc_count_all =  new BigDecimal(0);//总处方统计-汇总
			//（医生分组）不合格处方数,总处方统计
			List<PageData> countlist =  this.prescService.prescCountDoctor(pd);
			for(PageData py:countlist){
				String DOCTOR_CODE = py.getString("DOCTOR_CODE");
				PageData px = map.get(DOCTOR_CODE);
				px.put("checkfalse_sum", py.get("checkfalse_sum"));
				px.put("presc_count", py.get("presc_count"));
				BigDecimal checkfalse_sum = (BigDecimal) py.get("checkfalse_sum");
				BigDecimal presc_count = (BigDecimal) py.get("presc_count");
				checkfalse_sum_all= checkfalse_sum_all.add(checkfalse_sum);
				presc_count_all = presc_count_all.add(presc_count);
			}
			//汇总统计
			PageData all = this.prescService.exceedCommonAll(pd);
			all.put("checkfalse_sum_all", checkfalse_sum_all);
			all.put("presc_count_all", presc_count_all);
			mv.addObject("all", all);
			
			//计算平均值
			for(PageData px:list){
				if(checkfalse_sum_all==null||checkfalse_sum_all.doubleValue()==0){
					px.put("checkfalse_persents1", 0);
				}else{
					String checkfalse_persents1 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(checkfalse_sum_all,4,4).doubleValue()*100);
					px.put("checkfalse_persents1", checkfalse_persents1);
				}
				BigDecimal presc_count = (BigDecimal) px.get("presc_count");
				if(presc_count==null||presc_count.doubleValue()==0){
					px.put("checkfalse_persents2", 0);
				}else{
					String checkfalse_persents2 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(presc_count,4,4).doubleValue()*100);
					px.put("checkfalse_persents2", checkfalse_persents2);
				}
			}
			
			mv.addObject("resultList", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("DoctOrder/report/exceedCommonDoctor");
		return  mv; 
	}
	
	/**
	 * 处方超常规统计（医生） excel 导出
	 * @return
	 */
	@RequestMapping(value="/exceedCommonDoctorExport")
	public ModelAndView exceedCommonDoctorExport(){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("pd", pd);
		try {
			//超常规统计（医生）按照问题类型分组
			List<PageData> list =  this.prescService.exceedCommonDoctor(pd);
			//按照医生分组
			Map<String,PageData> map = new HashMap<String,PageData>();
			for(PageData px:list){
				String DOCTOR_CODE = px.getString("DOCTOR_CODE");
				map.put(DOCTOR_CODE, px);
			}
			BigDecimal checkfalse_sum_all = new BigDecimal(0);//不合格处方数汇总
			BigDecimal presc_count_all =  new BigDecimal(0);//总处方统计-汇总
			//（医生分组）不合格处方数,总处方统计
			List<PageData> countlist =  this.prescService.prescCountDoctor(pd);
			for(PageData py:countlist){
				String DOCTOR_CODE = py.getString("DOCTOR_CODE");
				PageData px = map.get(DOCTOR_CODE);
				px.put("checkfalse_sum", py.get("checkfalse_sum"));
				px.put("presc_count", py.get("presc_count"));
				BigDecimal checkfalse_sum = (BigDecimal) py.get("checkfalse_sum");
				BigDecimal presc_count = (BigDecimal) py.get("presc_count");
				checkfalse_sum_all= checkfalse_sum_all.add(checkfalse_sum);
				presc_count_all = presc_count_all.add(presc_count);
			}
			//汇总统计
			PageData all = this.prescService.exceedCommonAll(pd);
			all.put("checkfalse_sum_all", checkfalse_sum_all);
			all.put("presc_count_all", presc_count_all);
			//计算平均值
			for(PageData px:list){
				if(checkfalse_sum_all==null||checkfalse_sum_all.doubleValue()==0){
					px.put("checkfalse_persents1", 0);
				}else{
					String checkfalse_persents1 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(checkfalse_sum_all,4,4).doubleValue()*100);
					px.put("checkfalse_persents1", checkfalse_persents1);
				}
				BigDecimal presc_count = (BigDecimal) px.get("presc_count");
				if(presc_count==null||presc_count.doubleValue()==0){
					px.put("checkfalse_persents2", 0);
				}else{
					String checkfalse_persents2 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(presc_count,4,4).doubleValue()*100);
					px.put("checkfalse_persents2", checkfalse_persents2);
				}
			}

			//导出excel
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("医生名称");//1
			titles.add("用法用量");//2
			titles.add("禁忌症");	//3
			titles.add("重复给药 	");	//4
			titles.add("相互作用");	//5
			titles.add("配伍禁忌");	//5
			titles.add("不良反应");	//5
			titles.add("给药途径");	//5
			titles.add("特殊人群");	//5
			titles.add("管理");	//5
			titles.add("不合格医嘱数");	//5
			titles.add("医嘱总医嘱数");	//5
			titles.add("不合格医嘱数占总不合格医嘱百分比");	//5
			titles.add("不合格医嘱数占（查询日期内）医生总医嘱数");	//5
			dataMap.put("titles", titles);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<list.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", list.get(i).getString("DOCTOR_NAME"));		//1
				vpd.put("var2", list.get(i).getDouble("DOSAGE_SUM"));	//2
				vpd.put("var3", list.get(i).getDouble("DIAGINFO_SUM"));	//3
				vpd.put("var4", list.get(i).getDouble("INGREDIEN_SUM"));	//4
				vpd.put("var5", list.get(i).getDouble("INTERACTION_SUM"));	//4
				vpd.put("var6", list.get(i).getDouble("IV_EFFECT_SUM"));	//4
				vpd.put("var7", list.get(i).getDouble("SIDE_SUM"));	//4
				vpd.put("var8", list.get(i).getDouble("ADMINISTRATOR_SUM"));	//4
				vpd.put("var9", list.get(i).getDouble("SPECPEOPLE_SUM"));	//4
				vpd.put("var10", list.get(i).getDouble("MANAGER_SUM"));	//4
				vpd.put("var11", list.get(i).getDouble("CHECKFALSE_SUM"));	//4
				vpd.put("var12", list.get(i).getDouble("PRESC_COUNT"));	//4
				vpd.put("var13", list.get(i).get("CHECKFALSE_PERSENTS1").toString() + "%");	//4
				vpd.put("var14", list.get(i).get("CHECKFALSE_PERSENTS2").toString() + "%");	//4
				varList.add(vpd);
			}
			PageData vpd = new PageData();
			vpd.put("var1", "总计：");						//1
			vpd.put("var2", all.getDouble("DOSAGE_SUM"));
			vpd.put("var3", all.getDouble("DIAGINFO_SUM"));	//3
			vpd.put("var4", all.getDouble("INGREDIEN_SUM"));	//4
			vpd.put("var5", all.getDouble("INTERACTION_SUM"));	//4
			vpd.put("var6", all.getDouble("IV_EFFECT_SUM"));	//4
			vpd.put("var7", all.getDouble("SIDE_SUM"));	//4
			vpd.put("var8", all.getDouble("ADMINISTRATOR_SUM"));	//4
			vpd.put("var9", all.getDouble("SPECPEOPLE_SUM"));	//4
			vpd.put("var10", all.getDouble("MANAGER_SUM"));	//4
			vpd.put("var11", all.getDouble("CHECKFALSE_SUM_ALL"));	//4
			vpd.put("var12", all.getDouble("PRESC_COUNT_ALL"));	//4
			vpd.put("var13", "");	//4
			vpd.put("var14", "");	//4
			varList.add(vpd);
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  mv; 
	}
	
	/**
	 * 医嘱超常规统计（科室）
	 * @return
	 */
	@RequestMapping(value="/exceedCommonOrderDep")
	public ModelAndView exceedCommonOrderDep(){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("pd", pd);
		try {
			//超常规统计（科室）按照问题类型分组
			List<PageData> list =  this.prescService.exceedCommonOrderDep(pd);
			//按照医生分组
			Map<String,PageData> map = new HashMap<String,PageData>();
			for(PageData px:list){
				String ORG_CODE = px.getString("out_dept_name");
				map.put(ORG_CODE, px);
			}
			BigDecimal checkfalse_sum_all = new BigDecimal(0);//不合格处方数汇总
			BigDecimal order_count_all =  new BigDecimal(0);//总处方统计-汇总
			//（科室分组）不合格处方数,总处方统计
			List<PageData> countlist =  this.prescService.orderCountDep(pd);
			for(PageData py:countlist){
				String ORG_CODE = py.getString("out_dept_name");
				PageData px = map.get(ORG_CODE);
				px.put("checkfalse_sum", py.get("checkfalse_sum"));
				px.put("order_count", py.get("order_count"));
				BigDecimal checkfalse_sum = (BigDecimal) py.get("checkfalse_sum");
				BigDecimal order_count = (BigDecimal) py.get("order_count");
				checkfalse_sum_all= checkfalse_sum_all.add(checkfalse_sum);
				order_count_all = order_count_all.add(order_count);
			}
			//汇总统计
			PageData all = this.prescService.exceedCommonOrderAll(pd);
			all.put("checkfalse_sum_all", checkfalse_sum_all);
			all.put("order_count_all", order_count_all);
			mv.addObject("all", all);
			
			//计算平均值
			for(PageData px:list){
				if(checkfalse_sum_all==null||checkfalse_sum_all.doubleValue()==0){
					px.put("checkfalse_persents1", 0);
				}else{
					String checkfalse_persents1 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(checkfalse_sum_all,4,4).doubleValue()*100);
					px.put("checkfalse_persents1", checkfalse_persents1);
				}
				BigDecimal order_count = (BigDecimal) px.get("order_count");
				if(order_count ==null||order_count.doubleValue()==0){
					px.put("checkfalse_persents2", 0);
				}else{
					String checkfalse_persents2 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(order_count,4,4).doubleValue()*100);
					px.put("checkfalse_persents2", checkfalse_persents2);
				}
			}
			
			mv.addObject("resultList", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("DoctOrder/report/exceedCommonOrderDep");
		return  mv; 
	}
	
	/**
	 * 医嘱超常规统计（科室）
	 * @return
	 */
	@RequestMapping(value="/exceedCommonOrderDepExport")
	public ModelAndView exceedCommonOrderDepExport(){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("pd", pd);
		try {
			//超常规统计（科室）按照问题类型分组
			List<PageData> list =  this.prescService.exceedCommonOrderDep(pd);
			//按照医生分组
			Map<String,PageData> map = new HashMap<String,PageData>();
			for(PageData px:list){
				String ORG_CODE = px.getString("out_dept_name");
				map.put(ORG_CODE, px);
			}
			BigDecimal checkfalse_sum_all = new BigDecimal(0);//不合格处方数汇总
			BigDecimal order_count_all =  new BigDecimal(0);//总处方统计-汇总
			//（科室分组）不合格处方数,总处方统计
			List<PageData> countlist =  this.prescService.orderCountDep(pd);
			for(PageData py:countlist){
				String ORG_CODE = py.getString("out_dept_name");
				PageData px = map.get(ORG_CODE);
				px.put("checkfalse_sum", py.get("checkfalse_sum"));
				px.put("order_count", py.get("order_count"));
				BigDecimal checkfalse_sum = (BigDecimal) py.get("checkfalse_sum");
				BigDecimal order_count = (BigDecimal) py.get("order_count");
				checkfalse_sum_all= checkfalse_sum_all.add(checkfalse_sum);
				order_count_all = order_count_all.add(order_count);
			}
			//汇总统计
			PageData all = this.prescService.exceedCommonOrderAll(pd);
			all.put("checkfalse_sum_all", checkfalse_sum_all);
			all.put("order_count_all", order_count_all);
			
			//计算平均值
			for(PageData px:list){
				if(checkfalse_sum_all==null||checkfalse_sum_all.doubleValue()==0){
					px.put("checkfalse_persents1", 0);
				}else{
					String checkfalse_persents1 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(checkfalse_sum_all,4,4).doubleValue()*100);
					px.put("checkfalse_persents1", checkfalse_persents1);
				}
				BigDecimal order_count = (BigDecimal) px.get("order_count");
				if(order_count ==null||order_count.doubleValue()==0){
					px.put("checkfalse_persents2", 0);
				}else{
					String checkfalse_persents2 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(order_count,4,4).doubleValue()*100);
					px.put("checkfalse_persents2", checkfalse_persents2);
				}
			}
			
			//导出excel
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("科室");//1
			titles.add("用法用量");//2
			titles.add("禁忌症");	//3
			titles.add("重复给药 	");	//4
			titles.add("相互作用");	//5
			titles.add("配伍禁忌");	//5
			titles.add("不良反应");	//5
			titles.add("给药途径");	//5
			titles.add("特殊人群");	//5
			titles.add("管理");	//5
			titles.add("不合格医嘱数");	//5
			titles.add("医嘱总医嘱数");	//5
			titles.add("不合格医嘱数占总不合格医嘱百分比");	//5
			titles.add("不合格医嘱数占（查询日期内）医生总医嘱数");	//5
			dataMap.put("titles", titles);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<list.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", list.get(i).getString("OUT_DEPT_NAME"));		//1
				vpd.put("var2", list.get(i).getDouble("DOSAGE_SUM"));	//2
				vpd.put("var3", list.get(i).getDouble("DIAGINFO_SUM"));	//3
				vpd.put("var4", list.get(i).getDouble("INGREDIEN_SUM"));	//4
				vpd.put("var5", list.get(i).getDouble("INTERACTION_SUM"));	//4
				vpd.put("var6", list.get(i).getDouble("IV_EFFECT_SUM"));	//4
				vpd.put("var7", list.get(i).getDouble("SIDE_SUM"));	//4
				vpd.put("var8", list.get(i).getDouble("ADMINISTRATOR_SUM"));	//4
				vpd.put("var9", list.get(i).getDouble("SPECPEOPLE_SUM"));	//4
				vpd.put("var10", list.get(i).getDouble("MANAGER_SUM"));	//4
				vpd.put("var11", list.get(i).getDouble("CHECKFALSE_SUM"));	//4
				vpd.put("var12", list.get(i).getDouble("ORDER_COUNT"));	//4
				vpd.put("var13", list.get(i).get("CHECKFALSE_PERSENTS1").toString() + "%");	//4
				vpd.put("var14", list.get(i).get("CHECKFALSE_PERSENTS2").toString() + "%");	//4
				varList.add(vpd);
			}
			PageData vpd = new PageData();
			vpd.put("var1", "总计：");		//1
			vpd.put("var2", all.getDouble("DOSAGE_SUM"));
			vpd.put("var3", all.getDouble("DIAGINFO_SUM"));	//3
			vpd.put("var4", all.getDouble("INGREDIEN_SUM"));	//4
			vpd.put("var5", all.getDouble("INTERACTION_SUM"));	//4
			vpd.put("var6", all.getDouble("IV_EFFECT_SUM"));	//4
			vpd.put("var7", all.getDouble("SIDE_SUM"));	//4
			vpd.put("var8", all.getDouble("ADMINISTRATOR_SUM"));	//4
			vpd.put("var9", all.getDouble("SPECPEOPLE_SUM"));	//4
			vpd.put("var10", all.getDouble("MANAGER_SUM"));	//4
			vpd.put("var11", all.getDouble("CHECKFALSE_SUM_ALL"));	//4
			vpd.put("var12", all.getDouble("ORDER_COUNT_ALL"));	//4
			vpd.put("var13", "");	//4
			vpd.put("var14", "");	//4
			varList.add(vpd);
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  mv; 
	}
	/**
	 * 医嘱超常规统计（医生）
	 * @return
	 */
	@RequestMapping(value="/exceedCommonOrderDoctor")
	public ModelAndView exceedCommonOrderDoctor(){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("pd", pd);
		try {
			//超常规统计（医生）按照问题类型分组
			List<PageData> list =  this.prescService.exceedCommonOrderDoctor(pd);
			//按照医生分组
			Map<String,PageData> map = new HashMap<String,PageData>();
			for(PageData px:list){
				String DOCTOR_CODE = px.getString("ATTENDING_DOCTOR");
				map.put(DOCTOR_CODE, px);
			}
			BigDecimal checkfalse_sum_all = new BigDecimal(0);//不合格处方数汇总
			BigDecimal order_count_all =  new BigDecimal(0);//总处方统计-汇总
			//（医生分组）不合格处方数,总处方统计
			List<PageData> countlist =  this.prescService.orderCountDoctor(pd);
			for(PageData py:countlist){
				String DOCTOR_CODE = py.getString("ATTENDING_DOCTOR");
				PageData px = map.get(DOCTOR_CODE);
				px.put("checkfalse_sum", py.get("checkfalse_sum"));
				px.put("order_count", py.get("order_count"));
				BigDecimal checkfalse_sum = (BigDecimal) py.get("checkfalse_sum");
				BigDecimal order_count = (BigDecimal) py.get("order_count");
				checkfalse_sum_all= checkfalse_sum_all.add(checkfalse_sum);
				order_count_all = order_count_all.add(order_count);
			}
			//汇总统计
			PageData all = this.prescService.exceedCommonOrderAll(pd);
			all.put("checkfalse_sum_all", checkfalse_sum_all);
			all.put("order_count_all", order_count_all);
			mv.addObject("all", all);
			
			//计算平均值
			for(PageData px:list){
				if(checkfalse_sum_all==null||checkfalse_sum_all.doubleValue()==0){
					px.put("checkfalse_persents1", 0);
				}else{
					String checkfalse_persents1 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(checkfalse_sum_all,4,4).doubleValue()*100);
					px.put("checkfalse_persents1", checkfalse_persents1);
				}
				BigDecimal order_count = (BigDecimal) px.get("order_count");
				if(order_count ==null||order_count.doubleValue()==0){
					px.put("checkfalse_persents2", 0);
				}else{
					String checkfalse_persents2 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(order_count,4,4).doubleValue()*100);
					px.put("checkfalse_persents2", checkfalse_persents2);
				}
			}
			
			mv.addObject("resultList", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("DoctOrder/report/exceedCommonOrderDoctor");
		return  mv; 
	}
	/**
	 * 医嘱超常规统计（医生） 导出excel
	 * @return
	 */
	@RequestMapping(value="/exceedCommonOrderDoctorExport")
	public ModelAndView exceedCommonOrderDoctorExport(){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("pd", pd);
		try {
			//超常规统计（医生）按照问题类型分组
			List<PageData> list =  this.prescService.exceedCommonOrderDoctor(pd);
			//按照医生分组
			Map<String,PageData> map = new HashMap<String,PageData>();
			for(PageData px:list){
				String DOCTOR_CODE = px.getString("ATTENDING_DOCTOR");
				map.put(DOCTOR_CODE, px);
			}
			BigDecimal checkfalse_sum_all = new BigDecimal(0);//不合格处方数汇总
			BigDecimal order_count_all =  new BigDecimal(0);//总处方统计-汇总
			//（医生分组）不合格处方数,总处方统计
			List<PageData> countlist =  this.prescService.orderCountDoctor(pd);
			for(PageData py:countlist){
				String DOCTOR_CODE = py.getString("ATTENDING_DOCTOR");
				PageData px = map.get(DOCTOR_CODE);
				px.put("checkfalse_sum", py.get("checkfalse_sum"));
				px.put("order_count", py.get("order_count"));
				BigDecimal checkfalse_sum = (BigDecimal) py.get("checkfalse_sum");
				BigDecimal order_count = (BigDecimal) py.get("order_count");
				checkfalse_sum_all= checkfalse_sum_all.add(checkfalse_sum);
				order_count_all = order_count_all.add(order_count);
			}
			//汇总统计
			PageData all = this.prescService.exceedCommonOrderAll(pd);
			all.put("checkfalse_sum_all", checkfalse_sum_all);
			all.put("order_count_all", order_count_all);
			mv.addObject("all", all);
			
			//计算平均值
			for(PageData px:list){
				if(checkfalse_sum_all==null||checkfalse_sum_all.doubleValue()==0){
					px.put("checkfalse_persents1", 0);
				}else{
					String checkfalse_persents1 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(checkfalse_sum_all,4,4).doubleValue()*100);
					px.put("checkfalse_persents1", checkfalse_persents1);
				}
				BigDecimal order_count = (BigDecimal) px.get("order_count");
				if(order_count ==null||order_count.doubleValue()==0){
					px.put("checkfalse_persents2", 0);
				}else{
					String checkfalse_persents2 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(order_count,4,4).doubleValue()*100);
					px.put("checkfalse_persents2", checkfalse_persents2);
				}
			}
			
			//导出excel
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("医生名称");//1
			titles.add("用法用量");//2
			titles.add("禁忌症");	//3
			titles.add("重复给药 	");	//4
			titles.add("相互作用");	//5
			titles.add("配伍禁忌");	//5
			titles.add("不良反应");	//5
			titles.add("给药途径");	//5
			titles.add("特殊人群");	//5
			titles.add("管理");	//5
			titles.add("不合格医嘱数");	//5
			titles.add("医嘱总医嘱数");	//5
			titles.add("不合格医嘱数占总不合格医嘱百分比");	//5
			titles.add("不合格医嘱数占（查询日期内）医生总医嘱数");	//5
			dataMap.put("titles", titles);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<list.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", list.get(i).getString("ATTENDING_DOCTOR"));		//1
				vpd.put("var2", list.get(i).getDouble("DOSAGE_SUM"));	//2
				vpd.put("var3", list.get(i).getDouble("DIAGINFO_SUM"));	//3
				vpd.put("var4", list.get(i).getDouble("INGREDIEN_SUM"));	//4
				vpd.put("var5", list.get(i).getDouble("INTERACTION_SUM"));	//4
				vpd.put("var6", list.get(i).getDouble("IV_EFFECT_SUM"));	//4
				vpd.put("var7", list.get(i).getDouble("SIDE_SUM"));	//4
				vpd.put("var8", list.get(i).getDouble("ADMINISTRATOR_SUM"));	//4
				vpd.put("var9", list.get(i).getDouble("SPECPEOPLE_SUM"));	//4
				vpd.put("var10", list.get(i).getDouble("MANAGER_SUM"));	//4
				vpd.put("var11", list.get(i).getDouble("CHECKFALSE_SUM"));	//4
				vpd.put("var12", list.get(i).getDouble("ORDER_COUNT"));	//4
				vpd.put("var13", list.get(i).get("CHECKFALSE_PERSENTS1").toString() + "%");	//4
				vpd.put("var14", list.get(i).get("CHECKFALSE_PERSENTS2").toString() + "%");	//4
				varList.add(vpd);
			}
			PageData vpd = new PageData();
			vpd.put("var1", "总计：");		//1
			vpd.put("var2", all.getDouble("DOSAGE_SUM"));
			vpd.put("var3", all.getDouble("DIAGINFO_SUM"));	//3
			vpd.put("var4", all.getDouble("INGREDIEN_SUM"));	//4
			vpd.put("var5", all.getDouble("INTERACTION_SUM"));	//4
			vpd.put("var6", all.getDouble("IV_EFFECT_SUM"));	//4
			vpd.put("var7", all.getDouble("SIDE_SUM"));	//4
			vpd.put("var8", all.getDouble("ADMINISTRATOR_SUM"));	//4
			vpd.put("var9", all.getDouble("SPECPEOPLE_SUM"));	//4
			vpd.put("var10", all.getDouble("MANAGER_SUM"));	//4
			vpd.put("var11", all.getDouble("CHECKFALSE_SUM_ALL"));	//4
			vpd.put("var12", all.getDouble("ORDER_COUNT_ALL"));	//4
			vpd.put("var13", "");	//4
			vpd.put("var14", "");	//4
			varList.add(vpd);
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  mv; 
	}
}