package com.ts.controller.DoctOrder.OrderWork;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.system.User;
import com.ts.service.DoctOrder.OrderWork.IOrderWorkService;
import com.ts.util.DateUtil;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.app.AppUtil;

import net.sf.json.JSONArray;


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
			for(PageData pp:entity){
				String RS_DRUG_TYPES = pp.getString("RS_DRUG_TYPES");
				if(!Tools.isEmpty(RS_DRUG_TYPES)){
					String[] RS_DRUG_TYPE = RS_DRUG_TYPES.split("@;@");
					pp.put("RS_DRUG_TYPES", RS_DRUG_TYPE);
				}
			}
			mv.addObject("rstypeMap", rstypeMap); 
			mv.addObject("rstypeColorMap", rstypeColorMap); 
			mv.addObject("checktypeMap", getCheckTypeDict()); 
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
		mv.addObject("checkRss", checkRss);
		mv.setViewName("DoctOrder/OrderWork/OrderWorkDetail");
		mv.addObject("pat", pdPat);
		mv.addObject("oper",pdOper);
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
		PageData PatVisit = orderWorkService.queryPatVisit(pd);
		mv.addObject("check_status", PatVisit.get("check_status")==null?0:PatVisit.get("check_status"));
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
		Integer show_type = page.getPd().getInt("show_type");
		//常规查看 获取医嘱信息
		List<PageData> pdOrders =null;
		if(show_type==null || show_type==0){
			pdOrders = this.orderWorkService.orderList(page);
		}else if( show_type==1){
			//按日分解查看
			pdOrders = this.orderWorkService.ordersPageByDate(page); 
			Map<String,Integer> datestrMap = new LinkedHashMap<String,Integer>();
			//分组统计
			for(PageData pp:pdOrders){
				if(datestrMap.containsKey(pp.getString("datestr"))){
					datestrMap.put(pp.getString("datestr"), datestrMap.get(pp.getString("datestr"))+1);
				}else{
					datestrMap.put(pp.getString("datestr"), 1);
				}
			}
			mv.addObject("datestrMap",datestrMap);
		}else if( show_type==2){
			//按日图分解查看
			Map<Integer,String> classmap = new HashMap<Integer,String>();
			classmap.put(0, "label-info");
			classmap.put(1, "label-success");
			classmap.put(2, "label-danger");
			classmap.put(3, "label-purple");
			classmap.put(4, "label-yellow");
			classmap.put(5, "label-pink");
			classmap.put(6, "label-grey");
			List<PageData> treeList = orderWorkService.OrdersPicture(pd);
			int  i = 0;
			for(PageData pp:treeList){
				pp.put("title",pp.getString("order_Text")+"["+getOrderClassDict().get( pp.getString("order_class"))+"]");
				pp.put("CLASSNAME", classmap.get(i));
				if(i>=6){
					i=0;
				}else{
					i++;
				}
			}
			JSONArray arr = JSONArray.fromObject(treeList);
			String json = arr.toString();
			json = json.replaceAll("TITLE", "title").replaceAll("STARTDATE", "start").replaceAll("ENDDATE", "end").replaceAll("CLASSNAME", "className");
			mv.addObject("dateNodes",json);
			String dateStart = orderWorkService.queryOrdersStartDate(pd);
			mv.addObject("dateStart",dateStart);
			mv.setViewName("DoctOrder/OrderWork/calendar");
			return mv;
		}else  if( show_type==3){
			//术后医嘱日期查看
			pdOrders = this.orderWorkService.ordersPageByOpDate(page);
			Map<String,Integer> datestrMap = new LinkedHashMap<String,Integer>();
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
		mv.addObject("orderClassMap",getOrderClassDict());
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
	
	/**
	 * 获得字段数据
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> getOrderClassDict() throws Exception {
		Map<String, String> rs = new HashMap<>();
		// 审核字典
		List<PageData> rsTypeDict = this.orderWorkService.getOrderClassDict();
		for(PageData pd : rsTypeDict){
			rs.put(pd.getString("ORDER_CLASS_CODE"), pd.getString("ORDER_CLASS_NAME"));
		}
		return rs;
	}
	/**
	 * 删除单个快捷点评结果
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delCheckRs")
	@ResponseBody
	public Object delCheckRs() throws Exception {
		String errInfo = null;
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			// 当前登录用户
			User user = getCurrentUser();
			pd = this.getPageData();
			pd.put("CHECK_USER", user.getUSERNAME());
			pd.put("CHECK_TIME", new Date());
			orderWorkService.deleteCheckRsById(pd);
			errInfo="success";
		} catch(Exception e){
			logger.error(e.toString(), e);
			errInfo = "操作失败！";
		}
		map.put("result",errInfo);
		return map;
	}
	/**
	 * 删除单个快捷点评结果
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delCheckRsAll")
	@ResponseBody
	public Object delCheckRsAll() throws Exception {
		String errInfo = null;
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			// 当前登录用户
			User user = getCurrentUser();
			pd = this.getPageData();
			orderWorkService.delCheckRsAll(pd);
			errInfo="success";
		} catch(Exception e){
			logger.error(e.toString(), e);
			errInfo = "操作失败！";
		}
		map.put("result",errInfo);
		return map;
	}
	
	/**
	 * 新增点评页面
	 * @return
	 */
	@RequestMapping(value="/toAddCheckRs")
	public ModelAndView toAddCheckRs(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			mv.addObject("pd", pd);
			mv.addObject("checkType", getCheckTypeDict());
			Map orderMap = orderWorkService.ordersListSpecial(pd);
			mv.addObject("orderMap", orderMap);
			mv.setViewName("DoctOrder/OrderWork/checkRsAdd");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**
	 * 新增点评
	 * @return
	 */
	@RequestMapping(value="/addCheckRs")
	@ResponseBody
	public Object addCheckRs() throws Exception {
		String errInfo = null;
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			// 当前登录用户
			User user = getCurrentUser();
			pd = this.getPageData();
			String ngroupnum = pd.getString("ngroupnum");
			//人工是否点评
			pd.put("ISORDERCHECK", 1);
			// 是否为正确医嘱
			pd.put("ISCHECKTRUE", 0);
			//设置为不合理
			pd.put("CHECK_STATUS", 1);
			pd.put("CHECKPEOPLE", user.getUSER_ID());
			if("".equals(ngroupnum)) {
				pd.put("ngroupnum", this.get32UUID());
			}
			this.orderWorkService.updatePatVisitNgroupnum(pd);
			pd.put("rs_id", this.get32UUID());
			pd.put("in_rs_type", 4);
			pd.put("alert_level", pd.getString("r"));
			String ALERT_HINT =  new String(pd.getString("alert_hint").getBytes("ISO-8859-1"),"UTF-8");
			pd.put("alert_hint", ALERT_HINT);
			pd.put("alert_cause", "药师自审");
			pd.put("alert_level", "r");
			pd.put("checkdate", DateUtil.getDay());
			String orderDrug1 = new String(pd.getString("orderDrug1").getBytes("ISO-8859-1"),"UTF-8");
			String[] drug1 = orderDrug1.split("@;@");
			pd.put("drug_id1", drug1[2]);
			pd.put("drug_id1_name", drug1[3]);
			pd.put("rec_main_no1", drug1[0]);
			pd.put("rec_sub_no1", drug1[1]);
			String orderDrug2 = new String(pd.getString("orderDrug2").getBytes("ISO-8859-1"),"UTF-8");
			if(!Tools.isEmpty(orderDrug2))
			{
				pd.put("drug_id2", drug1[2]);
				pd.put("drug_id2_name", drug1[3]);
				pd.put("rec_main_no2", drug1[0]);
				pd.put("rec_sub_no2", drug1[1]);
			}
			int i = orderWorkService.saveCheckResult(pd);
			map.put("ngroupnum", pd.get("ngroupnum"));
			errInfo="success";
		} catch(Exception e){
			logger.error(e.toString(), e);
			errInfo = "操作失败！";
		}
		map.put("result",errInfo);
		return map;
	}
	/**
	 * 编辑点评页面
	 * @return
	 */
	@RequestMapping(value="/toEditCheckRs")
	public ModelAndView toEditCheckRs(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			mv.addObject("checkType", getCheckTypeDict());
			PageData order = orderWorkService.getCheckRsById(pd);
			order.putAll(pd);
			mv.setViewName("DoctOrder/OrderWork/checkRsEdit");
			mv.addObject("pd", order);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}

	/**
	 * 新增点评
	 * @return
	 */
	@RequestMapping(value="/editCheckRs")
	@ResponseBody
	public Object editCheckRs() throws Exception {
		String errInfo = null;
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			// 当前登录用户
			User user = getCurrentUser();
			pd = this.getPageData();
//			String ALERT_HINT =  new String(pd.getString("alert_hint").getBytes("ISO-8859-1"),"UTF-8");
			pd.put("alert_hint", new String(pd.getString("alert_hint").getBytes("ISO-8859-1"),"UTF-8"));
			orderWorkService.updateCheckResult(pd);
			errInfo="success";
		} catch(Exception e){
			logger.error(e.toString(), e);
			errInfo = "操作失败！";
		}
		map.put("result",errInfo);
		return map;
	}
	
	/**
	 * 确定住院记录是否合理
	 * @return
	 */
	@RequestMapping(value="/setCheckRsStatus")
	@ResponseBody
	public Object setCheckRsStatus(){
		String errInfo = null;
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			// 当前登录用户
			User user = getCurrentUser();
			pd = this.getPageData();
			orderWorkService.setCheckRsStatus(pd);
			errInfo="success";
		} catch(Exception e){
			logger.error(e.toString(), e);
			errInfo = "操作失败！";
		}
		map.put("result",errInfo);
		return map;
	}
	
//	private static Map<String,String> orderClassMap = new HashMap<String,String>(); 
//	static{
//		orderClassMap.put("A","药疗");
//		orderClassMap.put("1","处置");
//		orderClassMap.put("2","检查");
//		orderClassMap.put("3","化验");
//		orderClassMap.put("4","手术");
//		orderClassMap.put("5","护理");
//		orderClassMap.put("6","膳食");
//		orderClassMap.put("7","其他");
//	}
	private static Map<String,String> rstypeMap = new HashMap<String,String>(); 
	static{
		rstypeMap.put("diaginfo","禁");
		rstypeMap.put("dosage","法");
		rstypeMap.put("ingredien","重");
		rstypeMap.put("interaction","相");
		rstypeMap.put("iv_effect","配");
		rstypeMap.put("side","反");
		rstypeMap.put("administrator","途");
		rstypeMap.put("specpeople","特");
		rstypeMap.put("manager","管");
	}
	private static Map<String,String> rstypeColorMap = new HashMap<String,String>(); 
	static{
		rstypeColorMap.put("diaginfo","btn-pink");
		rstypeColorMap.put("dosage","btn-warning");
		rstypeColorMap.put("ingredien","btn-success");
		rstypeColorMap.put("interaction","btn-yellow");
		rstypeColorMap.put("iv_effect","btn-grey");
		rstypeColorMap.put("side","btn-danger");
		rstypeColorMap.put("administrator","btn-info");
		rstypeColorMap.put("specpeople","btn-purple");
		rstypeColorMap.put("manager","btn-success");
	}
}
