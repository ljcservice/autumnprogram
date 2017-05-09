package com.ts.controller.DoctOrder.OrderWork;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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
import com.ts.util.ApplicationUtil;
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
	private UserManager userService;
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
}