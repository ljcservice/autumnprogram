package com.ts.controller.DoctOrder.OrderWork;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.system.User;
import com.ts.service.DoctOrder.OrderWork.CommonService;
import com.ts.service.DoctOrder.OrderWork.IOrderWorkService;
import com.ts.service.DoctOrder.OrderWork.PrescService;
import com.ts.service.system.user.UserManager;
import com.ts.util.DateUtil;
import com.ts.util.MyDecimalFormat;
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
			// 当前登录专家
			User user = getCurrentUser();
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
				if(count.doubleValue()==0){
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
				if(count.doubleValue()==0){
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
				if(count.doubleValue()==0){
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
			User user = getCurrentUser();
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
				if(count.doubleValue()==0){
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
			mv.addObject("pd", pd);
			page.setPd(pd);
			page.setShowCount(9999);
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
				if(count.doubleValue()==0){
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
				if(count.doubleValue()==0){
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
	 * 处方超常规统计（科室）
	 * @return
	 */
	@RequestMapping(value="/exceedCommonDep")
	public ModelAndView exceedCommonDep(){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
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
				String checkfalse_persents1 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(checkfalse_sum_all,4,4).doubleValue()*100);
				px.put("checkfalse_persents1", checkfalse_persents1);
				String checkfalse_persents2 = MyDecimalFormat.format(((BigDecimal)px.get("presc_count")).divide(presc_count_all,4,4).doubleValue()*100);
				px.put("checkfalse_persents2", checkfalse_persents2);
			}
			
			mv.addObject("resultList", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("DoctOrder/report/exceedCommonDep");
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
				if(presc_count_all==null||presc_count_all.doubleValue()==0){
					px.put("checkfalse_persents2", 0);
				}else{
					String checkfalse_persents2 = MyDecimalFormat.format(((BigDecimal)px.get("presc_count")).divide(presc_count_all,4,4).doubleValue()*100);
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
	 * 医嘱超常规统计（科室）
	 * @return
	 */
	@RequestMapping(value="/exceedCommonOrderDep")
	public ModelAndView exceedCommonOrderDep(){
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		try {
			//超常规统计（科室）按照问题类型分组
			List<PageData> list =  this.prescService.exceedCommonOrderDep(pd);
			//按照医生分组
			Map<String,PageData> map = new HashMap<String,PageData>();
			for(PageData px:list){
				String ORG_CODE = px.getString("ORG_CODE");
				map.put(ORG_CODE, px);
			}
			BigDecimal checkfalse_sum_all = new BigDecimal(0);//不合格处方数汇总
			BigDecimal presc_count_all =  new BigDecimal(0);//总处方统计-汇总
			//（科室分组）不合格处方数,总处方统计
			List<PageData> countlist =  this.prescService.prescCountOrderDep(pd);
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
				String checkfalse_persents1 = MyDecimalFormat.format(((BigDecimal)px.get("checkfalse_sum")).divide(checkfalse_sum_all,4,4).doubleValue()*100);
				px.put("checkfalse_persents1", checkfalse_persents1);
				String checkfalse_persents2 = MyDecimalFormat.format(((BigDecimal)px.get("presc_count")).divide(presc_count_all,4,4).doubleValue()*100);
				px.put("checkfalse_persents2", checkfalse_persents2);
			}
			
			mv.addObject("resultList", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("DoctOrder/report/exceedCommonDep");
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
		try {
			//超常规统计（医生）按照问题类型分组
			List<PageData> list =  this.prescService.exceedCommonOrderDoctor(pd);
			//按照医生分组
			Map<String,PageData> map = new HashMap<String,PageData>();
			for(PageData px:list){
				String DOCTOR_CODE = px.getString("DOCTOR_CODE");
				map.put(DOCTOR_CODE, px);
			}
			BigDecimal checkfalse_sum_all = new BigDecimal(0);//不合格处方数汇总
			BigDecimal presc_count_all =  new BigDecimal(0);//总处方统计-汇总
			//（医生分组）不合格处方数,总处方统计
			List<PageData> countlist =  this.prescService.prescCountOrderDoctor(pd);
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
				if(presc_count_all==null||presc_count_all.doubleValue()==0){
					px.put("checkfalse_persents2", 0);
				}else{
					String checkfalse_persents2 = MyDecimalFormat.format(((BigDecimal)px.get("presc_count")).divide(presc_count_all,4,4).doubleValue()*100);
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
}