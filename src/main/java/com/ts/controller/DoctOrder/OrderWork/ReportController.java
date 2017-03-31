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
import com.ts.entity.system.User;
import com.ts.service.DoctOrder.OrderWork.CommonService;
import com.ts.service.DoctOrder.OrderWork.IOrderWorkService;
import com.ts.service.DoctOrder.OrderWork.PrescService;
import com.ts.service.system.user.UserManager;
import com.ts.util.ApplicationUtil;
import com.ts.util.MyDecimalFormat;
import com.ts.util.PageData;

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
	 * 处方列表
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
	
}