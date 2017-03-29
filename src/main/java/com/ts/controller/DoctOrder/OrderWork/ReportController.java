package com.ts.controller.DoctOrder.OrderWork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.system.User;
import com.ts.service.DoctOrder.OrderWork.CommonService;
import com.ts.service.DoctOrder.OrderWork.IOrderWorkService;
import com.ts.service.DoctOrder.OrderWork.PrescService;
import com.ts.service.system.user.UserManager;
import com.ts.util.DateUtil;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.app.AppUtil;
import com.ts.util.doctor.DoctorConst;

@Controller
@RequestMapping(value="/report")
public class ReportController extends BaseController{
	@Autowired
	private CommonService commonService;
	@Autowired
	private PrescService prescService;
	@Autowired
	private UserManager userService;
	@Autowired
	private IOrderWorkService orderWorkService;
	
	/**
	 * 处方列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/prescReport")
	public ModelAndView prescListPage(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			// 当前登录专家
			User user = getCurrentUser();
			mv.addObject("pd", pd);
			//处方问题统计
			List<PageData>	reportList = prescService.prescReportList(pd);

			mv.addObject("reportList", reportList);
			mv.addObject("checktypeMap", commonService.getCheckTypeDict()); 
			mv.setViewName("DoctOrder/report/prescReport");
			
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}


	
}