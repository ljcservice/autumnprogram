package com.ts.controller.DoctOrder.OrderWork;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.ts.util.ObjectExcelView;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.app.AppUtil;
import com.ts.util.doctor.DoctorConst;

/**
 * 处方点评
 * @author silong.xing
 *
 */
@Controller
@RequestMapping(value="/presc")
public class PrescController extends BaseController {
	@Resource(name="commonServicePdss")
	private CommonService commonService;
	@Autowired
	private PrescService prescService;
	@Autowired
	private UserManager userService;
	@Autowired
	private IOrderWorkService orderWorkService;
	
	/**
	 * 处方工作表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/prescWorkListPage")
	public ModelAndView prescWorkListPage(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.setViewName("DoctOrder/presc/prescWorkList");
			// 当前登录专家
			User user = getCurrentUser();
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
			
			String DRUG_TYPE = pd.getString("DRUG_TYPE");	//药品种类
			if(!Tools.isEmpty(DRUG_TYPE)){
				pd.put(DRUG_TYPE, 1);
			}
			page.setPd(pd);
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
			List<PageData>	prescList = prescService.prescListPage(page); 
			if("1".equals(pd.getString("randomflag"))){
				getRequest().getSession().setAttribute("prescExportList", prescList);
			}
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
			mv.addObject("pd", pd);
			BigDecimal ALL_COUNT = new BigDecimal(prescList==null?0:prescList.size());
			BigDecimal DRUG_COUNT_SUM = new BigDecimal(0);BigDecimal HASZS_SUM = new BigDecimal(0);
			BigDecimal HASKJ_SUM = new BigDecimal(0);BigDecimal BASEDRUG_COUNT_SUM = new BigDecimal(0);
			BigDecimal AMOUNT_AVG = new BigDecimal(0);BigDecimal DRUG_COUNT_AVG = new BigDecimal(0);
			BigDecimal AMOUNT_SUM = new BigDecimal(0);BigDecimal HASZS_PERSENTS = new BigDecimal(0);
			BigDecimal HASKJ_PERSENTS = new BigDecimal(0);BigDecimal BASEDRUG_COUNT_PERSENTS = new BigDecimal(0);
			if("1".equals(pd.getString("randomflag"))){
				//手动统计
				for(PageData pp:prescList){
					DRUG_COUNT_SUM = DRUG_COUNT_SUM.add((BigDecimal) pp.get("DRUG_COUNT") );
					HASKJ_SUM = HASKJ_SUM.add( Tools.isEmpty(pp.getString("HASKJ"))?new BigDecimal(0):new BigDecimal(pp.getString("HASKJ")) );
					HASZS_SUM = HASZS_SUM.add( Tools.isEmpty(pp.getString("HASZS"))?new BigDecimal(0):new BigDecimal(pp.getString("HASZS")) );
					BASEDRUG_COUNT_SUM = BASEDRUG_COUNT_SUM.add( (BigDecimal) pp.get("BASEDRUG_COUNT")  );
					AMOUNT_SUM = AMOUNT_SUM.add((BigDecimal) pp.get("AMOUNT"));
				}
				DRUG_COUNT_AVG = DRUG_COUNT_SUM.divide(ALL_COUNT,2,4);
				AMOUNT_AVG = AMOUNT_SUM.divide(ALL_COUNT,2,4);
				HASZS_PERSENTS=HASZS_SUM.divide(ALL_COUNT,4,4).multiply(new BigDecimal(100));
				HASKJ_PERSENTS=HASKJ_SUM.divide(ALL_COUNT,4,4).multiply(new BigDecimal(100));
				BASEDRUG_COUNT_PERSENTS=BASEDRUG_COUNT_SUM.divide(ALL_COUNT,4,4).multiply(new BigDecimal(100));
				PageData report = new PageData();
				report.put("ALL_COUNT", ALL_COUNT.longValue());
				report.put("DRUG_COUNT_SUM",DRUG_COUNT_SUM.longValue());
				report.put("HASZS_SUM",  HASZS_SUM.longValue());
				report.put("HASKJ_SUM", HASKJ_SUM.longValue() );
				report.put("BASEDRUG_COUNT_SUM", BASEDRUG_COUNT_SUM.longValue() );
				report.put("AMOUNT_SUM",  AMOUNT_SUM.doubleValue());
				report.put("DRUG_COUNT_AVG",  DRUG_COUNT_AVG.doubleValue());
				report.put("AMOUNT_AVG",  AMOUNT_AVG.doubleValue());
				report.put("HASZS_PERSENTS",  HASZS_PERSENTS.doubleValue()+" %");
				report.put("HASKJ_PERSENTS",  HASKJ_PERSENTS.doubleValue()+" %");
				report.put("BASEDRUG_COUNT_PERSENTS",  BASEDRUG_COUNT_PERSENTS.doubleValue()+" %");
				getRequest().getSession().setAttribute("prescReportCount", report);
				mv.addObject("report", report);
			}else{
				//统计
				PageData report = prescService.prescCountReport(pd); 
				mv.addObject("report", report);
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 导出到excel
	 * @return
	 */
	@RequestMapping(value="/prescListExport")
	public ModelAndView prescListExport(Page page){
		logBefore(logger, "导出Textmsg到excel");
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
		String DRUG_TYPE = pd.getString("DRUG_TYPE");	//药品种类
		if(!Tools.isEmpty(DRUG_TYPE)){
			pd.put(DRUG_TYPE, 1);
		}
		page.setPd(pd);
		page.setShowCount(1000);
		if("1".equals(pd.getString("randomflag"))){
			return prescListExport2(mv);
		}
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("处方号");//1
			titles.add("处方日期");//2
			titles.add("患者");	//3
			titles.add("性别 	");	//4
			titles.add("科室");	//5
			titles.add("医生");	//5
			titles.add("诊断");	//5
			titles.add("点评");	//5
			titles.add("是否合理");	//5
			titles.add("品种");	//5
			titles.add("抗菌");	//5
			titles.add("注射");	//5
			titles.add("基药数");	//5
			titles.add("金额");	//5
			titles.add("存在问题");	//5
			dataMap.put("titles", titles);
			int TotalPage = 1;
			List<PageData> varList = null;
			//分批查询,最大查询2万条
			for(int pag = 1;pag<=TotalPage&&pag<=20;pag++){
				page.setPd(pd);
				page.setCurrentPage(pag);
				List<PageData> varOList =  prescService.prescListPage(page);
				TotalPage = page.getTotalPage();
				if(varList==null){
					varList = new ArrayList<PageData>(TotalPage*page.getShowCount());
				}
				if(varOList!=null){
					for(int i=0;i<varOList.size();i++){
						PageData vpd = new PageData();
						vpd.put("var1", varOList.get(i).getString("PRESC_NO"));		//1
						vpd.put("var2", varOList.get(i).getString("ORDER_DATE"));	//2
						vpd.put("var3", varOList.get(i).getString("PATIENT_NAME"));	//3
						vpd.put("var4", varOList.get(i).getString("PATIENT_SEX"));	//4
						vpd.put("var5", varOList.get(i).getString("ORG_NAME"));		//5
						vpd.put("var6", varOList.get(i).getString("DOCTOR_NAME"));	//6
						vpd.put("var7", varOList.get(i).getString("DIAGNOSIS_NAMES"));	//8
						vpd.put("var8", varOList.get(i).get("ISORDERCHECK")==null?"未点评":("0".equals(varOList.get(i).get("ISORDERCHECK").toString())?"未点评":"已点评"));	//10
						vpd.put("var9", varOList.get(i).get("ISCHECKTRUE")==null?"待定":("0".equals(varOList.get(i).get("ISCHECKTRUE").toString())?"合理":("1".equals(varOList.get(i).get("ISCHECKTRUE").toString())?"不合理":"待定")));	//11
						vpd.put("var10", varOList.get(i).get("DRUG_COUNT").toString());	//6
						vpd.put("var11", "0".equals(varOList.get(i).getString("HASKJ"))?"否":"是");		//7
						vpd.put("var12", varOList.get(i).getString("HASZS"));		//5
						vpd.put("var13", varOList.get(i).get("BASEDRUG_COUNT").toString());	
						vpd.put("var14", "￥ "+varOList.get(i).getDouble("AMOUNT"));			//9
						
						String RS_DRUG_TYPES = varOList.get(i).getString("RS_DRUG_TYPES");
						StringBuffer sb = new StringBuffer();
						if(!Tools.isEmpty(RS_DRUG_TYPES)){
							String[] RS_DRUG_TYPE = RS_DRUG_TYPES.split("@;@");
							for(String ss:RS_DRUG_TYPE){
								String w = DoctorConst.rstypeMap.get(ss );
								sb.append(w).append(";");
							}
						}
						vpd.put("var15", sb.length()>0?sb.substring(0, sb.length()-1):"");	//12
						varList.add(vpd);
					}
				}
			}
			if("1".equals(pd.getString("work"))){
				//统计
				PageData report = prescService.prescCountReport(pd); 
				mv.addObject("report", report);
				PageData p1 = new PageData();
				p1.put("var1", "总 计：");	 
				p1.put("var2", report.get("ALL_COUNT").toString()); 
				p1.put("var10", report.get("DRUG_COUNT_SUM").toString()); 
				p1.put("var11", report.get("HASZS_SUM").toString()); 
				p1.put("var12", report.get("HASKJ_SUM").toString()); 
				p1.put("var13", report.get("BASEDRUG_COUNT_SUM").toString()); 
				p1.put("var14", report.get("AMOUNT_SUM").toString()); 
				PageData p2 = new PageData();
				p2.put("var1", "平 均：");	
				p2.put("var10", report.get("DRUG_COUNT_AVG").toString()); 
				p2.put("var14", report.get("AMOUNT_AVG").toString()); 
				PageData p3 = new PageData();
				p3.put("var1", "% ");	
				p3.put("var11", report.get("HASZS_PERSENTS").toString()); 
				p3.put("var12", report.get("HASKJ_PERSENTS").toString()); 
				p3.put("var13", report.get("BASEDRUG_COUNT_PERSENTS").toString()); 
				varList.add(p1);
				varList.add(p2);
				varList.add(p3);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	public ModelAndView prescListExport2(ModelAndView mv){
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("处方号");//1
			titles.add("处方日期");//2
			titles.add("患者");	//3
			titles.add("性别 	");	//4
			titles.add("科室");	//5
			titles.add("医生");	//5
			titles.add("诊断");	//5
			titles.add("点评");	//5
			titles.add("是否合理");	//5
			titles.add("品种");	//5
			titles.add("抗菌");	//5
			titles.add("注射");	//5
			titles.add("基药数");	//5
			titles.add("金额");	//5
			titles.add("存在问题");	//5
			dataMap.put("titles", titles);
			int TotalPage = 1;
			List<PageData> varList  = new ArrayList<PageData>();
			//分批查询,最大查询2万条
				List<PageData> varOList =   (List<PageData>) getRequest().getSession().getAttribute("prescExportList" );
				if(varOList!=null){
					for(int i=0;i<varOList.size();i++){
						PageData vpd = new PageData();
						vpd.put("var1", varOList.get(i).getString("PRESC_NO"));		//1
						vpd.put("var2", varOList.get(i).getString("ORDER_DATE"));	//2
						vpd.put("var3", varOList.get(i).getString("PATIENT_NAME"));	//3
						vpd.put("var4", varOList.get(i).getString("PATIENT_SEX"));	//4
						vpd.put("var5", varOList.get(i).getString("ORG_NAME"));		//5
						vpd.put("var6", varOList.get(i).getString("DOCTOR_NAME"));	//6
						vpd.put("var7", varOList.get(i).getString("DIAGNOSIS_NAMES"));	//8
						vpd.put("var8", varOList.get(i).get("ISORDERCHECK")==null?"未点评":("0".equals(varOList.get(i).get("ISORDERCHECK").toString())?"未点评":"已点评"));	//10
						vpd.put("var9", varOList.get(i).get("ISCHECKTRUE")==null?"待定":("0".equals(varOList.get(i).get("ISCHECKTRUE").toString())?"合理":("1".equals(varOList.get(i).get("ISCHECKTRUE").toString())?"不合理":"待定")));	//11
						vpd.put("var10", varOList.get(i).get("DRUG_COUNT").toString());	//6
						vpd.put("var11", "0".equals(varOList.get(i).getString("HASKJ"))?"否":"是");		//7
						vpd.put("var12", varOList.get(i).getString("HASZS"));		//5
						vpd.put("var13", varOList.get(i).get("BASEDRUG_COUNT").toString());	
						vpd.put("var14", "￥ "+varOList.get(i).getDouble("AMOUNT"));			//9
						
						String[] RS_DRUG_TYPES = (String[]) varOList.get(i).get("RS_DRUG_TYPES");
						StringBuffer sb = new StringBuffer();
						if(RS_DRUG_TYPES!=null){
							for(String ss:RS_DRUG_TYPES){
								String w = DoctorConst.rstypeMap.get(ss );
								sb.append(w).append(";");
							}
							vpd.put("var15", sb.length()>0?sb.substring(0, sb.length()-1):"");	//12
						}
						varList.add(vpd);
					}
			}
				//统计
				PageData report = (PageData) getRequest().getSession().getAttribute("prescReportCount" );
				mv.addObject("report", report);
				PageData p1 = new PageData();
				p1.put("var1", "总 计：");	 
				p1.put("var2", report.get("ALL_COUNT").toString()); 
				p1.put("var10", report.get("DRUG_COUNT_SUM").toString()); 
				p1.put("var11", report.get("HASZS_SUM").toString()); 
				p1.put("var12", report.get("HASKJ_SUM").toString()); 
				p1.put("var13", report.get("BASEDRUG_COUNT_SUM").toString()); 
				p1.put("var14", report.get("AMOUNT_SUM").toString()); 
				PageData p2 = new PageData();
				p2.put("var1", "平 均：");	
				p2.put("var10", report.get("DRUG_COUNT_AVG").toString()); 
				p2.put("var14", report.get("AMOUNT_AVG").toString()); 
				PageData p3 = new PageData();
				p3.put("var1", "% ");	
				p3.put("var11", report.get("HASZS_PERSENTS").toString()); 
				p3.put("var12", report.get("HASKJ_PERSENTS").toString()); 
				p3.put("var13", report.get("BASEDRUG_COUNT_PERSENTS").toString()); 
				varList.add(p1);
				varList.add(p2);
				varList.add(p3);
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
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
	@RequestMapping(value="/prescListPage")
	public ModelAndView prescListPage(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			mv.setViewName("DoctOrder/presc/prescList");
			// 当前登录专家
			User user = getCurrentUser();
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
			if(Tools.isEmpty(beginDate)&&Tools.isEmpty(endDate)){
				return mv;
			}
			String DRUG_TYPE = pd.getString("DRUG_TYPE");	//药品种类
			if(!Tools.isEmpty(DRUG_TYPE)){
				pd.put(DRUG_TYPE, 1);
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
			mv.addObject("pd", pd);
			
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}


	/**
	 * 处方工作主页 单个
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/prescDetail")
	public ModelAndView prescDetail(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		//查询处方中的病人信息
		PageData presc = prescService.findPrescById(pd);
		String expert_id = presc.getString("expert_id");
		mv.addObject("pat", presc);
		if(!Tools.isEmpty(expert_id) ){
			User expert = userService.findUserById(expert_id) ;
			if(expert!=null){
				mv.addObject("expert_name", expert.getNAME());
			}
		}
		//点评查询结果ByNgroupnum
		if(!Tools.isEmpty(pd.getString("ngroupnum"))){
			List<PageData> checkRss = orderWorkService.findByCheckResultsByNgroupnum(page);
			mv.addObject("checkRss", checkRss);
		}
		mv.setViewName("DoctOrder/presc/prescDetail");
		return mv;
	}
	
	/**
	 * 处方列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/prescDetailList")
	public ModelAndView prescDetailList(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		// 当前登录专家
		User user = getCurrentUser();
		page.setPd(pd);
		//处方信息
		PageData presc = prescService.findPrescById(pd);
		//当前处方的详情
		List<PageData> prescDetailList = prescService.prescDetailList(pd);
		mv.addObject("prescDetailList", prescDetailList);
		//当日其他药师开具的处方（排除当前处方）
		List<PageData> otherPrescList = prescService.otherPrescList(presc);
		mv.addObject("otherPrescList",otherPrescList);
		//当日其他药师开具的处方详细（排除当前处方）
		List<PageData> otherPrescDetailList = prescService.otherPrescDetailList(presc);
		Map<String, List<PageData>> otherDetailMap = new HashMap<String , List<PageData>>();
		if(otherPrescDetailList!=null){
			for(PageData pp :otherPrescDetailList){
				String PRESC_ID = pp.get("PRESC_ID").toString();
				if(otherDetailMap.containsKey(PRESC_ID)){
					List<PageData> detailList = otherDetailMap.get(PRESC_ID);
					detailList.add(pp);
				}else{
					List<PageData> detailList = new ArrayList<PageData>();
					detailList.add(pp);
					otherDetailMap.put(PRESC_ID, detailList);
				}
			}
		}
		mv.addObject("otherPrescDetailMap",otherDetailMap);
		
		
		
		if(!Tools.isEmpty(pd.getString("ngroupnum"))){
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
			mv.addObject("CheckRss",map);
		}
		mv.addObject("rsTypeDict",commonService.getCheckTypeDict());
		mv.addObject("pd", pd);
		mv.setViewName("DoctOrder/presc/prescDetailList");
		
		String expert_id = presc.getString("expert_id");
		mv.addObject("modifyFlag", 1);
		//
		if(!Tools.isEmpty(expert_id) &&!user.getUSER_ID().equals(expert_id)){
			//专家点评则只有专家能修改
			mv.addObject("modifyFlag", 0);
		}
		return mv;
	}
	
	/**
	 * 返回点评结果
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkRsView")
	public ModelAndView CheckRsViewUI(Page page) throws Exception{
		ModelAndView  mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		PageData presc = prescService.findPrescById(pd);
		//默认待定
		mv.addObject("ISCHECKTRUE", presc.get("ISCHECKTRUE")==null?2:presc.get("ISCHECKTRUE"));
		
		List<PageData> checkRss = this.orderWorkService.findByCheckResultsByNgroupnum(page);
		mv.addObject("checkRss", checkRss);

		mv.setViewName("DoctOrder/presc/checkRsView");
		mv.addObject("rsTypeDict",commonService.getCheckTypeDict());
		
		// 当前登录专家
		User user = getCurrentUser();
		String expert_id = presc.getString("expert_id");
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
	 * 医嘱保存快捷点评结果
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveShortcut")
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
			//查询住院记录，找到ngroupnum
			PageData Presc = prescService.findPrescById(pd);
			if(Tools.isEmpty(Presc.getString("ngroupnum"))){
				pd.put("ngroupnum", this.get32UUID());
			}else{
				pd.put("ngroupnum", Presc.getString("ngroupnum"));
			}
		}
		pd.put("in_rs_type", 2);
		//更新处方的关联问题字段
		this.prescService.updatePrescNgroupnum(pd);
		pd.put("rs_id", this.get32UUID());
		pd.put("alert_hint", pd.getString("checkText"));
		pd.put("alert_cause", "药师自审");
		pd.put("alert_level", "r");
//		pd.put("rs_drug_type", "");
		pd.put("checkdate", DateUtil.getDay());
		pd.put("RS_DRUG_TYPE", pd.get("checkType"));
		if("1".equals(count)){
			pd.put("RELATION_ID1", pd.getString("tmpPresc_id"));
			pd.put("drug_id1", pd.getString("order_code"));
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
			pd.put("RELATION_ID1", pd.getString("tmpPresc_id"));
			pd.put("RELATION_ID2", pd.getString("presc_id"));
			
		}
		int i = orderWorkService.saveCheckResult(pd);
		map.put("result", "ok");
		return  AppUtil.returnObject(pd, map);
	}
	
	/**
	 * 确定处方录是否合理
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
			prescService.setCheckRsStatus(pd);
			errInfo="success";
		} catch(Exception e){
			logger.error(e.toString(), e);
			errInfo = "操作失败！";
		}
		map.put("result",errInfo);
		return map;
	}
	
}