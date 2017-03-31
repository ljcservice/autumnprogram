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
@RequestMapping(value="/presc")
public class PrescController extends BaseController{
	@Resource(name="commonServicePdss")
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
	@RequestMapping(value="/prescListPage")
	public ModelAndView prescListPage(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			// 当前登录专家
			User user = getCurrentUser();
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
			mv.setViewName("DoctOrder/presc/prescList");
			mv.addObject("pd", pd);
			
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}


	/**
	 * 处方工作主页详细信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/prescDetail")
	public ModelAndView prescDetail(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		//查询处方中的病人信息
		PageData pdPat = prescService.findPrescById(pd);
		mv.addObject("pat", pdPat);
		
		//点评查询结果ByNgroupnum
		if(!Tools.isEmpty(pd.getString("ngroupnum"))){
			List<PageData> checkRss = orderWorkService.findByCheckResultsByNgroupnum(page);
			mv.addObject("checkRss", checkRss);
		}
		mv.setViewName("DoctOrder/presc/prescDetail");
		return mv;
	}
	
	/**
	 * 处方工作主页详细信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/prescDetailList")
	public ModelAndView prescDetailList(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		//处方详情
		List<PageData> prescDetailList = prescService.prescDetailList(pd);
		mv.addObject("prescDetailList", prescDetailList);
		//当日其他人开具的处方详情
		List<PageData> otherPrescDetailList = prescService.otherPrescDetailList(pd);
		mv.addObject("otherPrescDetailList",otherPrescDetailList);
		
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
		
		//权限控制
		// 当前登录专家
		User user = getCurrentUser();
		PageData presc = prescService.findPrescById(pd);
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
		if("0".equals(pd.getString("business_type"))){
			pd.put("in_rs_type", 4);
		}else if("1".equals(pd.getString("business_type"))){
			pd.put("in_rs_type", 2);
		}
		//更新处方的关联问题字段
		this.prescService.updatePrescNgroupnum(pd);
		pd.put("rs_id", this.get32UUID());
		pd.put("alert_level", pd.getString("r"));
		pd.put("alert_hint", pd.getString("checkText"));
		pd.put("alert_cause", "药师自审");
		pd.put("alert_level", "r");
//		pd.put("rs_drug_type", "");
		pd.put("checkdate", DateUtil.getDay());
		pd.put("RS_DRUG_TYPE", pd.get("checkType"));
		if("1".equals(count)){
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