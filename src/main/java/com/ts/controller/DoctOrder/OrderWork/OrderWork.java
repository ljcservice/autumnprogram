package com.ts.controller.DoctOrder.OrderWork;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.DoctOrder.OrderWork.IOrderWorkService;
import com.ts.util.DateUtil;
import com.ts.util.PageData;
import com.ts.util.app.AppUtil;


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
	public ModelAndView OrderWorkDetailUI(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		PageData pdPat         = this.orderWorkService.findByPatient(page);
		List<PageData> pdOper  = this.orderWorkService.operationList(page);
		//查询结果ByNgroupnum
		List<PageData> checkRss = this.orderWorkService.findByCheckResultsByNgroupnum(page);
		mv.setViewName("DoctOrder/OrderWork/OrderWorkDetail");
		mv.addObject("pat", pdPat);
		mv.addObject("oper",pdOper);
		mv.addObject("checkRss", checkRss);
		return mv;
	}
	
	
	/**
	 * 返回点评结果
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/CheckRsViewUI")
	public ModelAndView CheckRsViewUI(Page page) throws Exception{
		ModelAndView  mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> checkRss = this.orderWorkService.findByCheckResultsByNgroupnum(page);
		mv.addObject("checkRss", checkRss);
		mv.setViewName("DoctOrder/OrderWork/CheckRsView");
		mv.addObject("rsTypeDict",getCheckTypeDict());
		return mv;
	}
	
	public ModelAndView CheckRsDelete(Page page) throws Exception{
		
		PageData pd = this.getPageData();
		
		
		return null;
	}
	
	
	/**
	 * 医嘱信息
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/DoctOrdersDetail")
	public ModelAndView DoctOrdersDetail(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		page.setCurrentjztsFlag(1);
		//查询结果ByNgroupnum
		List<PageData> checkRss = this.orderWorkService.findByCheckResultsByNgroupnum(page);
		Map<String, List<PageData>> map = new HashMap<String , List<PageData>>();
		for(PageData d : checkRss){
			String key1 = d.getString("rec_main_no1") + "_" + d.getString("rec_sub_no1");
			if(!map.containsKey(key1)) map.put(key1, new ArrayList<PageData>()); 
			map.get(key1).add(d);
			//增加第二个药品的识别
			if("".equals(d.getString("rec_main_no2"))) continue;
			String key2 = d.getString("rec_main_no2") + "_" + d.getString("rec_sub_no2");
			if(!map.containsKey(key2)) map.put(key2, new ArrayList<PageData>()); 
			map.get(key2).add(d);
		}
		
		Integer show_type = page.getPd().getInt("show_type");
		//常规查看 获取医嘱信息
		List<PageData> pdOrders =null;
		if(show_type==null || show_type==0){
			pdOrders = this.orderWorkService.orderList(page);
		}else if( show_type==1){
			//按日分解查看
			pdOrders = this.orderWorkService.OrdersByIdPageByDate(page); 
			Map<String,Integer> datestrMap = new HashMap<String,Integer>();
			//分组统计
			for(PageData pp:pdOrders){
				if(datestrMap.containsKey(pp.getString("datestr"))){
					datestrMap.put(pp.getString("datestr"), datestrMap.get(pp.getString("datestr"))+1);
				}else{
					datestrMap.put(pp.getString("datestr"), 1);
				}
			}
			mv.addObject("datestrMap",datestrMap);
		}
		
		mv.addObject("CheckRss",map);
		mv.addObject("DoctOrders", pdOrders);
		mv.addObject("rsTypeDict",getCheckTypeDict());
		mv.setViewName("DoctOrder/OrderWork/DoctOrders");
		
		return mv ; 
		
	}
	
	/**
	 * 保存快捷点评结果
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/SaveShortcut")
	@ResponseBody
	public Object saveShortcutChehck() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		Map<String, Object> map = new HashMap<String , Object>();
		String count = pd.getString("count");
		String ngroupnum = pd.getString("ngroupnum");
		//人工是否点评
		pd.put("ISORDERCHECK", "1");
		// 是否为正确医嘱
		pd.put("ISCHECKTRUE", "0");
		pd.put("CHECKPEOPLE", this.getCurrentUser().getUSER_ID());
		if("".equals(ngroupnum)) {
			pd.put("ngroupnum", this.get32UUID());
			this.orderWorkService.updatePatVisitNgroupnum(pd);
		}
		pd.put("rs_id", this.get32UUID());
		pd.put("in_rs_type", pd.getString("checkType"));
		pd.put("alert_level", pd.getString("r"));
		pd.put("alert_hint", pd.getString("checkText"));
		pd.put("alert_cause", "药师自审");
		pd.put("alert_level", "r");
		pd.put("rs_drug_type", "");
		pd.put("checkdate", DateUtil.getDay());
		if("1".equals(count)){
			pd.put("drug_id1", "");
			pd.put("drug_id1_name", pd.getString("tmpOrder_Name"));
			pd.put("rec_main_no1", pd.getString("tmpOrder_no"));
			pd.put("rec_sub_no1", pd.getString("tmpOrder_sub_no"));
			pd.put("drug_id2", "");
			pd.put("drug_id2_name", "");
			pd.put("rec_main_no2", "");
			pd.put("rec_sub_no2", "");
		}
		else if("2".equals(count))
		{
			pd.put("drug_id1", pd.getString("order_code"));
			pd.put("drug_id1_name", pd.getString("order_name"));
			pd.put("rec_main_no1", pd.getString("order_no"));
			pd.put("rec_sub_no1", pd.getString("order_sub_no"));
			pd.put("drug_id2", pd.getString("tmpOrder_Code"));
			pd.put("drug_id2_name", pd.getString("tmpOrder_Name"));
			pd.put("rec_main_no2", pd.getString("tmpOrder_no"));
			pd.put("rec_sub_no2", pd.getString("tmpOrder_sub_no"));
		}
		int i = orderWorkService.saveCheckResult(pd);
		map.put("result", "ok");
		return  AppUtil.returnObject(pd, map);
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
	
	/**
	 * 获得字段数据
	 * @return
	 * @throws Exception
	 */
	private Map<String, PageData> getCheckTypeDict() throws Exception
	{
		Map<String, PageData> rs = new HashMap<>();
		// 审核字典
		List<PageData> rsTypeDict = this.orderWorkService.selectRsTypeDict();
		for(PageData pd : rsTypeDict){
			rs.put(pd.getString("RS_TYPE_CODE"), pd);
		}
		return rs;
	}
}
