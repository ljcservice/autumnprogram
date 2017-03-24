package com.ts.controller.DoctOrder.OrderWork;

import java.util.ArrayList;
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
import com.ts.service.DoctOrder.OrderWork.IOrderWorkService;
import com.ts.service.DoctOrder.OrderWork.PrescService;
import com.ts.service.system.user.UserManager;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.doctor.DoctorConst;

@Controller
@RequestMapping(value="/presc")
public class PrescController extends BaseController{

	@Autowired
	private PrescService prescService;
	@Autowired
	private UserManager userService;
	@Resource(name="orderWorkServiceBean")
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
			mv.addObject("checktypeMap", getCheckTypeDict()); 
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
		//其他人开具的处方详情
		
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
		mv.addObject("pd", pd);
		mv.setViewName("DoctOrder/presc/prescDetailList");
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
		mv.addObject("rsTypeDict",getCheckTypeDict());
		// 当前登录专家
		User user = getCurrentUser();
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