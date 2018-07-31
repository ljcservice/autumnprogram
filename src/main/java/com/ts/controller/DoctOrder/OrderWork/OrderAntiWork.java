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

import net.sf.json.JSONArray;

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
import com.ts.util.ObjectExcelView;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.app.AppUtil;
import com.ts.util.doctor.DoctorConst;


/**
 * 医嘱点评工作表
 * @author autumn
 *
 */
@Controller
@RequestMapping(value="/DoctOrderAnti")
public class OrderAntiWork extends BaseController
{
	@Resource(name="commonServicePdss")
	private CommonService commonService;
	@Resource(name="orderAntiWorkServiceBean")
	private IOrderWorkService orderWorkService;
	@Autowired
	private UserManager userService;
	@Autowired
	private PrescService prescService;
	
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
			mv.setViewName("DoctOrder/OrderAntiWork/OrderWorkView");
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
		mv.setViewName("DoctOrder/OrderAntiWork/OrderWorkView");
		PageData pd = this.getPageData();
		
		try
		{
			String keywords = pd.getString("keywords");		//关键词检索条件
			String beginDate = pd.getString("beginDate");	//开始时间
			String endDate = pd.getString("endDate");		//结束时间
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
			
			String DRUG_TYPE = pd.getString("DRUG_TYPE");    //药品种类
            if(!Tools.isEmpty(DRUG_TYPE)){
                pd.put(DRUG_TYPE, 1);
            }
            
			String RANDOM_NUM = pd.getString("RANDOM_NUM");
			if(Tools.isEmpty(RANDOM_NUM)){
				pd.put("RANDOM_NUM", 50);
			}
			if("1".equals(pd.getString("randomflag"))){
				int showCount = Integer.parseInt(RANDOM_NUM);
				page.setShowCount(showCount);
			}else{
				if(Tools.isEmpty(beginDate)&&Tools.isEmpty(endDate)){
					return mv;
				}
			}
			List<PageData> entity =  null;
			//解决抽取样例数据刷新的问题
			if("1".equals(pd.getString("randomrefresh")))
			{
			    String refreshpatid = pd.getString("refreshpatientid");
			    String refreshvisitid = pd.getString("refreshvisitId");
                PageData param = new PageData();
                param.put("patient_id", refreshpatid);
                param.put("visit_id", refreshvisitid);
                
                PageData refreshRs = orderWorkService.patvisitlistbyRefreshId(param);
                
                String RS_DRUG_TYPES = refreshRs.getString("RS_DRUG_TYPES");
                if(!Tools.isEmpty(RS_DRUG_TYPES)){
                    String[] RS_DRUG_TYPE = RS_DRUG_TYPES.split("@;@");
                    refreshRs.put("RS_DRUG_TYPES", RS_DRUG_TYPE);
                }
                String DIAGNOSIS_DESC = refreshRs.getString("DIAGNOSIS_DESC");
                if(!Tools.isEmpty(DIAGNOSIS_DESC)){
                    String[] DIAGNOSIS_DESCS = DIAGNOSIS_DESC.split("@;@");
                    refreshRs.put("DIAGNOSIS_DESC", DIAGNOSIS_DESCS);
                }
                entity = (List<PageData>) getRequest().getSession().getAttribute("ExportAntiList");
                int removeplace = 0;
                for(int  i = 0 ; i < entity.size() ; i ++)
                {
                    PageData p = entity.get(i);
                    if(p.getString("patient_id").equals(refreshpatid) &&p.getString("visit_id").equals(refreshvisitid))
                    {
                        removeplace = i ;
                        break;
                    }
                }
                entity.remove(removeplace);
                entity.add(removeplace, refreshRs);
			}
			else
			{
			    entity =  this.orderWorkService.patientList(page);
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
			}
			if("1".equals(pd.getString("randomflag"))){
				getRequest().getSession().setAttribute("ExportAntiList", entity);
			}
			page = null;
			mv.addObject("rstypeMap", DoctorConst.rstypeMap); 
			mv.addObject("rstypeColorMap", DoctorConst.rstypeColorMap); 
			mv.addObject("checktypeMap", commonService.getCheckTypeDict()); 
			mv.addObject("patVisits", entity);
			mv.addObject("pd", pd);
		}catch(Exception e )
		{
			logger.error(e.toString(), e);
		}
		return  mv; 
	}
	
	private String  checkRsDetail(String ngroupnum) throws Exception 
    {
	    if(ngroupnum== null || "".equals(ngroupnum))return "" ;
        PageData pd  = new PageData();
        pd.put("ngroupnum", ngroupnum);
        Map<String,PageData> rsDict = commonService.getCheckTypeDict();
        List<PageData> rsChecks = orderWorkService.checkRsDetail(pd);
        StringBuffer sbRs = new StringBuffer();
        String rsType = "";
        String drugGroup = ""  ;
        for (PageData entity : rsChecks)
        {
            if(!rsType.equals(entity.getString("rs_drug_type")))
            {
                sbRs.append(rsDict.get(entity.getString("rs_drug_type")).getString("rs_type_name")).append("\r\n");
            }
            String strEmp = entity.getString("drug_id1") + entity.getString("drug_id2");
            if(!strEmp.equals(drugGroup))
            {
                sbRs.append(entity.getString("drugdetail1"));
                if(rsDict.get(entity.getString("rs_drug_type")).getInt("rs_count") == 2 ) sbRs.append(" 与  ");
                sbRs.append(entity.getString("drugdetail2"));
                sbRs.append("\r\n");
            }
            drugGroup = strEmp;
            sbRs.append("  ").append(entity.getString("alert_hint")).append("\r\n");
            rsType = entity.getString("rs_drug_type");
        }
        return sbRs.toString()  ;
    }
	
	/**
	 * 导出到excel
	 * @return
	 */
	@RequestMapping(value="/orderListExport")
	public ModelAndView orderListExport(Page page){
		logBefore(logger, "导出orderListExport到excel");
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String endDate = pd.getString("endDate");		//结束时间
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
		if("1".equals(pd.getString("randomflag"))){
			return orderListExport2(mv);
		}
		page.setShowCount(1000);
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("患者ID");//1
			titles.add("患者名称");//2
			titles.add("诊断结果");	//3
			titles.add("主治医师");	//4
			titles.add("入院科室");	//5
			titles.add("入院时间");	//5
			titles.add("出院科室");	//5
			titles.add("出院时间");	//5
			titles.add("点评");	//5
			titles.add("合理");	//5
			titles.add("结果");	//5
			titles.add("问题详细");
			dataMap.put("titles", titles);
			int TotalPage = 1;
			List<PageData> varList = null;
			//分批查询,最大查询2万条
			for(int pag = 1;pag<=TotalPage&&pag<=20;pag++){
				page.setPd(pd);
				page.setCurrentPage(pag);
				List<PageData> varOList =  orderWorkService.patientList(page);
				TotalPage = page.getTotalPage();
				if(varList==null){
					varList = new ArrayList<PageData>(TotalPage*page.getShowCount());
				}
				if(varOList!=null){
					for(int i=0;i<varOList.size();i++){
						PageData vpd = new PageData();
						vpd.put("var1", varOList.get(i).get("PATIENT_ID").toString()+"（"+varOList.get(i).get("VISIT_ID").toString()+"）");		//1
						vpd.put("var2", varOList.get(i).getString("NAME"));	//2
						vpd.put("var3", varOList.get(i).get("DIAGNOSIS_DESC").toString());	//3
						vpd.put("var4", varOList.get(i).getString("ATTENDING_DOCTOR"));	//4
						vpd.put("var5", varOList.get(i).getString("in_dept_name"));		//5
						vpd.put("var6", varOList.get(i).get("admission_date_time"));	//6
						vpd.put("var7", varOList.get(i).getString("out_dept_name"));		//7
						vpd.put("var8", varOList.get(i).get("discharge_date_time"));	//8
						vpd.put("var9", varOList.get(i).get("ISORDERCHECK")==null?"未点评":("0".equals(varOList.get(i).get("ISORDERCHECK").toString())?"未点评":"已点评"));	//10
						vpd.put("var10", varOList.get(i).get("ISCHECKTRUE")==null?"待定":("0".equals(varOList.get(i).get("ISCHECKTRUE").toString())?"合理":("1".equals(varOList.get(i).get("ISCHECKTRUE").toString())?"不合理":"待定")));	//11
						String RS_DRUG_TYPES = varOList.get(i).getString("RS_DRUG_TYPES");
						StringBuffer sb = new StringBuffer();
						if(!Tools.isEmpty(RS_DRUG_TYPES)){
							String[] RS_DRUG_TYPE = RS_DRUG_TYPES.split("@;@");
							for(String ss:RS_DRUG_TYPE){
								String w = DoctorConst.rstypeMap.get(ss );
								sb.append(w+";");
							}
						}
						vpd.put("var11", sb.length()>0?sb.substring(0, sb.length()-1):"");	//12
						vpd.put("var12", this.checkRsDetail(varOList.get(i).getString("ngroupnum")));
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
	private ModelAndView orderListExport2(ModelAndView mv) {
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("患者ID");//1
		titles.add("患者名称");//2
		titles.add("诊断结果");	//3
		titles.add("主治医师");	//4
		titles.add("入院科室");	//5
		titles.add("入院时间");	//5
		titles.add("出院科室");	//5
		titles.add("出院时间");	//5
		titles.add("点评");	//5
		titles.add("合理");	//5
		titles.add("结果");	//5
		titles.add("问题详细");
		dataMap.put("titles", titles);
		try
		{
		  //分批查询,最大查询2万条
	        List<PageData> varOList =  (List<PageData>) getRequest().getSession().getAttribute("ExportAntiList" );
	        List<PageData> varList = new ArrayList<PageData>();
	        if(varOList!=null){
	            for(int i=0;i<varOList.size();i++){
	                PageData vpd = new PageData();
	                vpd.put("var1", varOList.get(i).get("PATIENT_ID").toString()+"（"+varOList.get(i).get("VISIT_ID").toString()+"）");       //1
	                vpd.put("var2", varOList.get(i).getString("NAME")); //2
	                
	                String[] DIAGNOSIS_DESC = (String[]) varOList.get(i).get("DIAGNOSIS_DESC");
                    if(DIAGNOSIS_DESC!=null){
                        StringBuffer sb = new StringBuffer();
                        for(String ss:DIAGNOSIS_DESC){
                            sb.append(ss+";");
                        }
                        vpd.put("var3", sb.length()>0?sb.substring(0, sb.length()-1):"");  //12
                    }
	                vpd.put("var4", varOList.get(i).getString("ATTENDING_DOCTOR")); //4
	                vpd.put("var5", varOList.get(i).getString("in_dept_name"));     //5
	                vpd.put("var6", varOList.get(i).get("admission_date_time"));    //6
	                vpd.put("var7", varOList.get(i).getString("out_dept_name"));        //7
	                vpd.put("var8", varOList.get(i).get("discharge_date_time"));    //8
	                vpd.put("var9", varOList.get(i).get("ISORDERCHECK")==null?"未点评":("0".equals(varOList.get(i).get("ISORDERCHECK").toString())?"未点评":"已点评"));  //10
	                vpd.put("var10", varOList.get(i).get("ISCHECKTRUE")==null?"待定":("0".equals(varOList.get(i).get("ISCHECKTRUE").toString())?"合理":("1".equals(varOList.get(i).get("ISCHECKTRUE").toString())?"不合理":"待定")));    //11
	                String[] RS_DRUG_TYPE = (String[]) varOList.get(i).get("RS_DRUG_TYPES");
	                if(RS_DRUG_TYPE!=null){
	                    StringBuffer sb = new StringBuffer();
	                    for(String ss:RS_DRUG_TYPE){
	                        String w = DoctorConst.rstypeMap.get(ss );
	                        sb.append(w+";");
	                    }
	                    vpd.put("var11", sb.length()>0?sb.substring(0, sb.length()-1):"");  //12
	                }
	                vpd.put("var12", this.checkRsDetail(varOList.get(i).getString("ngroupnum")));
	                varList.add(vpd);
	            }
	        }
	        dataMap.put("varList", varList);
		}
		catch(Exception e )
		{
		    e.printStackTrace();
		}
		
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
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
		PageData pdPat = this.orderWorkService.findByPatient(page);

		List<PageData> pdOper  = this.orderWorkService.operationList(page);
		//查询结果ByNgroupnum
		List<PageData> checkRss = this.orderWorkService.findByCheckResultsByNgroupnum(page);
		mv.addObject("checkRss", checkRss);
		mv.setViewName("DoctOrder/OrderAntiWork/OrderWorkDetail");
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
		String expert_id = PatVisit.getString("expert_id");
		if(!Tools.isEmpty(expert_id) ){
			User expert = userService.findUserById(expert_id) ;
			if(expert!=null){
				mv.addObject("expert_name", expert.getNAME());
			}
		}
		//默认待定
		mv.addObject("ISCHECKTRUE", PatVisit.get("ISCHECKTRUE")==null?2:PatVisit.get("ISCHECKTRUE"));
		mv.addObject("checkRss", checkRss);
		mv.setViewName("DoctOrder/OrderAntiWork/CheckRsView");
		mv.addObject("rsTypeDict",commonService.getCheckTypeDict());
		// 当前登录专家
		User user = getCurrentUser();
		mv.addObject("expert_id",expert_id);
		mv.addObject("modifyFlag", 1);
		//
		if(!Tools.isEmpty(expert_id) &&!user.getUSER_ID().equals(expert_id)){
			//专家点评则只有专家能修改
			mv.addObject("modifyFlag", 0);
		}
		return mv;
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
		//药品种类
		String DRUG_TYPE = pd.getString("DRUG_TYPE");    
        if(!Tools.isEmpty(DRUG_TYPE)){
            pd.put(DRUG_TYPE, 1);
        }
		page.setPd(pd);
		page.setCurrentjztsFlag(1);
		Integer show_type = page.getPd().getInt("show_type");
		//常规查看 获取医嘱信息
		List<PageData> pdOrders =null;
		Map<String, String> orderClassDict = commonService.getOrderClassDict();
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
			classmap.put(6, "label-inverse");
			classmap.put(7, "label-grey");
			classmap.put(8, "label-primary");
			classmap.put(9, "label-warning");
			classmap.put(10, "label-light");
			List<PageData> treeList = orderWorkService.OrdersPicture(pd);
			int  i = -1;
			String temp = "";
			for(PageData pp:treeList){
			    String orderNo = pp.getInt("order_no").toString();
			    String orderSubNo = pp.get("order_sub_no").toString();
			    if(!temp.equals(orderNo))
			    {
			        if(i>=11){
			            i=0;
			        }else{
			            i++;
			        }
			    }
			    temp = orderNo;
				pp.put("title","[" + pp.getString("order_class_name") + "("+ orderNo + "," + orderSubNo + ")] " + pp.getString("order_Text"));
				pp.put("CLASSNAME", classmap.get(i));
			}
			JSONArray arr = JSONArray.fromObject(treeList);
			String json = arr.toString();
			json = json.replaceAll("TITLE", "title").replaceAll("STARTDATE", "start").replaceAll("ENDDATE", "end").replaceAll("CLASSNAME", "className");
			mv.addObject("dateNodes",json);
			String dateStart = orderWorkService.queryOrdersStartDate(pd);
			if(dateStart!=null){
				mv.addObject("dateStart",dateStart);
			}else{
				mv.addObject("dateStart",new Date());
			}
			mv.setViewName("DoctOrder/OrderAntiWork/calendar");
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
		mv.addObject("orderClassMap",orderClassDict);
		mv.addObject("CheckRss",map);
		mv.addObject("DoctOrders", pdOrders);
		mv.addObject("rsTypeDict",commonService.getCheckTypeDict());
		mv.setViewName("DoctOrder/OrderAntiWork/DoctOrders");
		//权限控制
		// 当前登录专家
		User user = getCurrentUser();
		PageData PatVisit = orderWorkService.queryPatVisit(pd);
		String expert_id = PatVisit.getString("expert_id");
		mv.addObject("modifyFlag", 1);
		//
		if(!Tools.isEmpty(expert_id) &&!user.getUSER_ID().equals(expert_id)){
			//专家点评则只有专家能修改
			mv.addObject("modifyFlag", 0);
		}
		
		return mv ; 
		
	}
	
	/**
	 * 医嘱保存快捷点评结果
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/SaveShortcut")
	@ResponseBody
	public Object saveShortcutChehck() throws Exception {
		PageData pd = this.getPageData();
		Map<String, Object> map = new HashMap<String , Object>();
		String count = pd.getString("count");
		String ngroupnum = pd.getString("ngroupnum");
		//人工是否点评
		pd.put("ISORDERCHECK", "1");
		// 是否为正确医嘱,1为不合理
		pd.put("ISCHECKTRUE", "1");
		pd.put("CHECKPEOPLE", this.getCurrentUser().getUSER_ID());
		if(Tools.isEmpty(ngroupnum)) {
			//查询住院记录，找到ngroupnum，此值一定要一致
			Page p = new Page();
			p.setPd(pd);
			PageData patient = orderWorkService.findByPatient(p);
			if(Tools.isEmpty(patient.getString("ngroupnum"))) {
				pd.put("ngroupnum", this.get32UUID());
			}else{
				pd.put("ngroupnum", patient.getString("ngroupnum"));
			}
			this.orderWorkService.updatePatVisitNgroupnum(pd);
		}
		pd.put("in_rs_type", 4);
		pd.put("rs_id", this.get32UUID());
		pd.put("alert_level", pd.getString("r"));
		pd.put("alert_hint", pd.getString("checkText"));
		pd.put("alert_cause", "药师自审");
		pd.put("alert_level", "r");
//		pd.put("rs_drug_type", "");
		pd.put("RS_DRUG_TYPE", pd.get("checkType"));
		pd.put("checkdate", DateUtil.getDay());
		if("1".equals(count)){
			pd.put("drug_id1", pd.getString("tmpOrder_code"));
			pd.put("drug_id1_name", pd.getString("tmpOrder_name"));
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
			
			pd.put("drug_id2", pd.getString("tmpOrder_code"));
			pd.put("drug_id2_name", pd.getString("tmpOrder_name"));
			pd.put("rec_main_no2", pd.getString("tmpOrder_no"));
			pd.put("rec_sub_no2", pd.getString("tmpOrder_sub_no"));
		}
		int i = orderWorkService.saveCheckResult(pd);
		map.put("result", "ok");
		map.put("ngroupnum", pd.getString("ngroupnum"));
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
//			pd.put("CHECK_USER", user.getUSERNAME());
//			pd.put("CHECK_TIME", new Date());
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
			mv.addObject("checkType", commonService.getCheckTypeDict());
			if("0".equals(pd.getString("business_type"))){
				//医嘱点评
				Map orderMap = orderWorkService.ordersListSpecial(pd);
				mv.addObject("orderMap1", orderMap);
				mv.addObject("orderMap2", orderMap);
			}else if("1".equals(pd.getString("business_type"))){
				//处方列表，供给选择
				pd.put("PRESC_ID", pd.get("id"));
				Map orderMap = prescService.prescListSpecial(pd);
				mv.addObject("orderMap1", orderMap);
//				Map orderMap2 = new HashMap();
//				orderMap2.putAll(orderMap);
				PageData presc = prescService.findPrescById(pd);
				Map otherPescMap = prescService.otherPrescListSpecial(presc);
				mv.addObject("orderMap2", otherPescMap);
			}
			mv.setViewName("DoctOrder/checkRsAdd");
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
			//设置为不合理
			pd.put("ISCHECKTRUE", 1);
			pd.put("CHECKPEOPLE", user.getUSER_ID());
			//查询住院记录，找到ngroupnum，此值一定要一致
			if("0".equals(pd.getString("business_type"))){
				pd.put("in_rs_type", 4);
				Page p = new Page();
				p.setPd(pd);
				PageData patient = orderWorkService.findByPatient(p);
				//校验是否为专家点评，如果有专家则校验是否为当前人
				String expert_id = patient.getString("expert_id");
				if(!Tools.isEmpty(expert_id)){
					if(!user.getUSER_ID().equals(expert_id)){
						map.put("result","该信息只能由专家进行点评，当前操作人无权限点评。");
						return map;
					}
				}
				if(Tools.isEmpty(ngroupnum)) {
					pd.put("ngroupnum", this.get32UUID());
				}else{
					pd.put("ngroupnum", patient.getString("ngroupnum"));
				}
				this.orderWorkService.updatePatVisitNgroupnum(pd);
			}else if("1".equals(pd.getString("business_type"))){
				pd.put("in_rs_type", 2);
				//查询处方记录，找到ngroupnum
				PageData Presc = prescService.findPrescById(pd);
				//校验是否为专家点评，如果有专家则校验是否为当前人
				String expert_id = Presc.getString("expert_id");
				if(!Tools.isEmpty(expert_id)){
					if(!user.getUSER_ID().equals(expert_id)){
						map.put("result","该信息只能由专家进行点评，当前操作人无权限点评。");
						return map;
					}
				}
				if(Tools.isEmpty(Presc.getString("ngroupnum"))){
					pd.put("ngroupnum", this.get32UUID());
				}else{
					pd.put("ngroupnum", Presc.getString("ngroupnum"));
				}
				//更新处方的关联问题字段
				this.prescService.updatePrescNgroupnum(pd);
			}
			pd.put("rs_id", this.get32UUID());
			
			pd.put("alert_level", pd.getString("r"));
//			pd.put("alert_hint", ALERT_HINT);
			pd.put("alert_cause", "药师自审");
			pd.put("alert_level", "r");
			pd.put("checkdate", DateUtil.getDay());
			String orderDrug1 = pd.getString("orderDrug1");
			String orderDrug2 = pd.getString("orderDrug2");
			String[] drug1 = orderDrug1.split("@;@");
			String[] drug2 = orderDrug2.split("@;@");
			pd.put("drug_id1", drug1[2]);
			pd.put("drug_id1_name", drug1[3]);
			pd.put("rec_main_no1", drug1[0]);
			pd.put("rec_sub_no1", drug1[1]);
			if(drug1.length>=5){
				pd.put("RELATION_ID1", drug1[4]);
			}
			if(!Tools.isEmpty(pd.getString("orderDrug2")))
			{
				pd.put("drug_id2", drug2[2]);
				pd.put("drug_id2_name", drug2[3]);
				pd.put("rec_main_no2", drug2[0]);
				pd.put("rec_sub_no2", drug2[1]);
				if(drug2.length>=5){
					pd.put("RELATION_ID2", drug2[4]);
				}
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
			mv.addObject("checkType", commonService.getCheckTypeDict());
			PageData order = orderWorkService.getCheckRsById(pd);
			order.putAll(pd);
			mv.setViewName("DoctOrder/checkRsEdit");
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
	private String checkRights(PageData pd,User user) throws Exception{
		if("0".equals(pd.getString("business_type"))){
			Page p = new Page();
			p.setPd(pd);
			PageData patient = orderWorkService.findByPatient(p);
			//校验是否为专家点评，如果有专家则校验是否为当前人
			String expert_id = patient.getString("expert_id");
			if(!Tools.isEmpty(expert_id)){
				if(!user.getUSER_ID().equals(expert_id)){
					return "该信息只能由专家进行点评，当前操作人无权限点评。";
				}
			}
		}else if("1".equals(pd.getString("business_type"))){
			PageData Presc = prescService.findPrescById(pd);
			//校验是否为专家点评，如果有专家则校验是否为当前人
			String expert_id = Presc.getString("expert_id");
			if(!Tools.isEmpty(expert_id)){
				if(!user.getUSER_ID().equals(expert_id)){
					return "该信息只能由专家进行点评，当前操作人无权限点评。";
				}
			}
		}
		return null;
	}
}
