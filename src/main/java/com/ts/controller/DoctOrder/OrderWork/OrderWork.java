package com.ts.controller.DoctOrder.OrderWork;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.system.User;
import com.ts.service.DoctOrder.OrderWork.IOrderWorkService;
import com.ts.util.DateUtil;
import com.ts.util.PageData;
import com.ts.util.ontology.HelpUtil;


/**
 * 医嘱点评工作表
 * @author autumn
 *
 */
@Controller
@RequestMapping(value="/DoctOrder")
public class OrderWork extends BaseController
{
	@Resource(name="orderWorkServiceBean")
	private IOrderWorkService orderWorkService;
	
	/**
	 * 检索医嘱点评工作信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/OrderWorkUI")
	public ModelAndView OrderWorkUI()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			mv.setViewName("DoctOrder/OrderWork/OrderWorkView");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	
	/**
	 * 检索医嘱点评工作信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/OrderWork")
	public ModelAndView OrderWorkSelect(Page page){
		
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		
		try
		{
			String keywords = pd.getString("keywords");				//关键词检索条件
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.trim());
			}
			String beginDate = pd.getString("beginDate");	//开始时间
			String endDate = pd.getString("endDate");		//结束时间
			if(endDate != null && !"".equals(endDate))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.setTime(DateUtil.fomatDate(endDate));
				cal.add(Calendar.DAY_OF_MONTH, 1);
				pd.put("endDate", sdf.format(cal.getTime()));
			} 
			page.setPd(pd);
			List<PageData> entity =  this.orderWorkService.patientList(page);
			mv.addObject("patVisits", entity);
		}catch(Exception e )
		{
			logger.error(e.toString(), e);
		}
		mv.setViewName("DoctOrder/OrderWork/OrderWorkView");
		return  mv; 
	}
	
	
	/**
	 * 医嘱点评工作主页详细信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/OrderWorkDetailUI")
	public ModelAndView OrderWorkDetailUI(String patId , String visitId) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pdPat   =  this.orderWorkService.findByPatient(patId, visitId);
		PageData pdOrder = this.orderWorkService.orderList(patId, visitId);
		mv.setViewName("DoctOrder/OrderWork/OrderWorkDetail");
		mv.addObject("pat", pdPat);
		return mv;
	}
	
	/**
	 * 用户费用明细
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	public ModelAndView OWConst(String pid) throws Exception {
		ModelAndView mv = this.getModelAndView();
		return  mv;
	}
	
}
