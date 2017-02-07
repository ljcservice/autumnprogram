package com.ts.controller.ontology;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.system.User;
import com.ts.service.ontology.impl.OsynHisService;
import com.ts.service.ontology.manager.OsynManager;
import com.ts.service.system.user.UserManager;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.ontology.HelpUtil;
import com.ts.util.ontology.OsynConst;

/** 
 * 类名称：词语副本表相关操作
 * 创建人：xsl
 * 更新时间：2016年10月10日
 * @version
 */
@Controller
//@RequestMapping(value="/alterHist")
@RequestMapping(value="/osynHis")
public class OsynHisController extends BaseController {
	
	@Resource(name="osynService")
	private OsynManager osynService;
	
	@Resource(name="osynHisService")
	private OsynHisService osynHisService;
	
	@Resource(name="userService")
	private UserManager userService;
	
	/**
	 * 查询单个词语变更记录
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/nameHist")
	public ModelAndView nameHistList(Page page)throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		String ontoType = pd.getString("ONTO_TYPE");
		String tableName="";
		String viewName="";
		if(OsynConst.DIAG_NAME_CODE.equals(ontoType)){
			tableName="diag";
			viewName="singal_name_hist_list";
		}else{
			tableName="drug";
			viewName="drug_hist_list";
		}
		List<PageData> list =null;
		try{
			list = osynHisService.nameHistPage(page,tableName);
			mv.setViewName("/ontology/osyn/hist/"+viewName);
		}catch(Exception e){
			logger.error(e.toString(), e);
		}
		mv.addObject("histList", list);
		mv.addObject("pd",pd);
		return mv;
	}
	
	/**
	 * 查询审核的词语信息列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkList")
	public ModelAndView checkNameList(Page page)throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//查询返回数据列表
		List<PageData> list = null;
		try{
			//得到所有的下拉菜单项
			User user = getCurrentUser();
			Map<String,String> typeMap = HelpUtil.getOsyntypeMap(user,1);
			
			String wordTypeO = pd.getString("wordType");
			String termCategoryO = pd.getString("onto_type");
			String osynType="";
			if(!"".equals(termCategoryO)&&termCategoryO!=null){
				osynType=termCategoryO;
			}else{//若为初始页面，则获取当前登录用户权限的第一个术语类型
				for (Entry<String,String> entry : typeMap.entrySet()) {
					osynType = entry.getKey();
		            if (osynType != null &&!"".equals(osynType)) {
		                break;
		            }
		        }
			}
			if(Tools.isEmpty(osynType)){
				return mv;
			}
			Map<String,String> wordMap = HelpUtil.getWordtypeMap(user,osynType);
			String status = pd.getString("STATUS");
			Integer wordType =0;
			Integer termCategory = null;
			if("".equals(wordTypeO) || wordTypeO==null||wordMap.get(wordTypeO)==null){
				for (Entry<String,String> osynStand : wordMap.entrySet()) {
					wordTypeO = osynStand.getKey();
		            if (wordTypeO != null &&!"".equals(wordTypeO)) {
		                break;
		            }
		        }
			}
			wordType=Integer.valueOf(wordTypeO);
			if(!"".equals(osynType) && osynType!=null){
				termCategory = Integer.valueOf(osynType);
			}
			if (status==null) {
				status="0";
			}
			pd.put("WORD_TYPE",wordType);
			pd.put("TERM_CATEGORY",osynType);
			pd.put("STATUS",status);
			page.setPd(pd);
			
			list = osynHisService.checkNameList(page);
			String viewName="";
			String sqlName="";
			if(OsynConst.DIAG_NAME_NUM_CODE.equals(termCategory)){
				viewName="osyn_check_list";
				sqlName="diag";
			}else if (OsynConst.OPERATION_NAME_NUM_CODE.equals(termCategory)) {
				viewName="osyn_check_list";
				sqlName="op";
			}else if(OsynConst.DRUG_NAME_COMMON_NUM_CODE.equals(termCategory)||OsynConst.DRUG_NAME_CHEMI_NUM_CODE.equals(termCategory)||OsynConst.DRUG_NAME_GOODS_NUM_CODE.equals(termCategory)||OsynConst.DRUG_NAME_PRODUCT_NUM_CODE.equals(termCategory)){
				viewName="drug_check_list";
				sqlName="drugName";
			}else if (OsynConst.DEPT_NAME_NUM_CODE.equals(termCategory)) {
				viewName="dep_check_list";
				sqlName="dep";
			}else if (OsynConst.DRUG_FACTORY_NUM_CODE.equals(termCategory)) {
				viewName="drug_check_list";
				sqlName="drugFactory";
			}else if (OsynConst.PACK_SPEC_NUM_CODE.equals(termCategory)) {
				viewName="drug_check_list";
				sqlName="packSpec";
			}else if (OsynConst.DRUG_SPEC_NUM_CODE.equals(termCategory)) {
				viewName="drug_check_list";
				sqlName="drugSpec";
			}else if (OsynConst.DRUG_PACK_NUM_CODE.equals(termCategory)) {
				viewName="drug_check_list";
				sqlName="drugPack";
			}else if (OsynConst.PACK_SPEC_UNIT_NUM_CODE.equals(termCategory)) {
				viewName="drug_check_list";
				sqlName="packSpecUnit";
			}else if (OsynConst.DRUG_FORM_NUM_CODE.equals(termCategory)) {
				viewName="drug_check_list";
				sqlName="drugForm";
			}else if (OsynConst.DRUG_ROUTE_NUM_CODE.equals(termCategory)) {
				viewName="drug_check_list";
				sqlName="drugRoute";
			}
			mv.setViewName("/ontology/osyn/check/"+viewName);
			mv.addObject("RIGHTS", sqlName.toUpperCase());
			List<PageData> users = userService.listSimpleUser(pd);
			mv.addObject("userList", users);
			mv.addObject("wordType",wordType);
			mv.addObject("termCategory",termCategory);
			mv.addObject("list",list);
			mv.addObject("pd",pd);
			//术语类型
			mv.addObject("osynType",osynType);
			mv.addObject("typeMap", typeMap);
			mv.addObject("wordMap", wordMap);
		}catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 查看审批详情
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkDetail")
	public ModelAndView checkDetail()throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pd_new = new PageData();
		try {
			
			String ontoType = pd.getString("onto_type");
			String tableName="";//查找的表名，根据词语类型不同，值不同
			String detailPage="";//返回的详细信息页面
			if (OsynConst.DIAG_NAME_CODE.equals(ontoType)) {//诊断
				tableName="diag";
				detailPage="check_detail";
			}else if(OsynConst.DRUG_FACTORY_CODE.equals(ontoType)){//药品生产企业
				tableName="drugFactory";
				detailPage="drug_factory_detail";
			}else if (OsynConst.DRUG_SPEC_CODE.equals(ontoType)) {//药品规格
				tableName="drugSpec";
				detailPage="drug_spec_detail";
			}else if (OsynConst.DRUG_PACK_CODE.equals(ontoType)) {//药品包装材质
				tableName="drugPack";
				detailPage="drug_pack_detail";
			}else if (OsynConst.PACK_SPEC_CODE.equals(ontoType)) {//药品包装规格
				tableName="packSpec";
				detailPage="pack_spec_detail";
			}else if (OsynConst.PACK_SPEC_UNIT_CODE.equals(ontoType)) {//药品包装规格单位
				tableName="packSpecUnit";
				detailPage="packspec_unit_detail";
			}else if (OsynConst.OPERATION_NAME_CODE.equals(ontoType)) {//手术
				tableName="operation";
				detailPage="operation_detail";
			}else if (OsynConst.DEPT_NAME_CODE.equals(ontoType)) {//科室
				tableName="dep";
				detailPage="dep_detail";
			}else if (OsynConst.DRUG_FORM_CODE.equals(ontoType)) {//药品剂型
				tableName="drugForm";
				detailPage="drug_form_detail";
			}else if (OsynConst.DRUG_ROUTE_CODE.equals(ontoType)) {//药品给药途径
				tableName="route";
				detailPage="drug_route_detail";
			}else{//药品名称
				tableName="drugName";
				detailPage="check_detail";
			}
			
			mv.setViewName("/ontology/osyn/check/"+detailPage);
			mv.addObject("nextShow", pd.get("nextShow"));
			
			//显示下一条
			if("1".equals(pd.getString("nextShow"))){
				//点击dialog关闭按钮时刷新页面的标准
				pd.put("refreshFlag", "1");
				pd.put("TERM_CATEGORY",ontoType);
				pd.put("WORD_TYPE",pd.get("WORDTYPE"));
				Page page = new Page();
				page.setPd(pd);
				List<PageData> list = osynHisService.checkNameList(page);
				if(!CollectionUtils.isEmpty(list)){
					PageData osyn = list.get(0);
					//要显示的下一条ID
					pd.put("H_ID", osyn.get("H_ID"));
					pd.put("STAD_DN_ID",osyn.get("STAD_DN_ID"));
					pd.put("dn_id", osyn.get("DN_ID"));
				}else{
					//无需要审核的数据，跳转到提示页面
					mv.addObject("msg", "当前查询条件下，没有需要审核的数据！");
					mv.setViewName("save_result");
					mv.addObject("refreshFlag",pd.get("refreshFlag"));
					return mv;
				}
			}
			if(pd.getString("H_ID")!=null){	
				String dn_id = pd.getString("dn_id");
				pd.put("ON_NAME_ID",dn_id);
				pd.put("ON_ID",dn_id);
				String stad_dn_id = pd.getString("STAD_DN_ID");
				
				String stadFlag = "0";
				if(dn_id.equals(stad_dn_id)){
					stadFlag="1";
				}
				pd.put("STAD_FLAG",stadFlag);
				pd_new=osynHisService.alterNameDetail(pd);
				PageData oldOnto = null;
				if(((BigDecimal)pd_new.get("OP_TYPE")).intValue()!=0){
					//不为新增时，查询未更新前的数据，做对比
					pd.put("ON_NAME_ID",pd.get("DN_ID"));
					oldOnto=osynService.findDiagOsynById(pd,tableName);
				}
				mv.addObject("pd",oldOnto);
				mv.addObject("pd_new",pd_new);
				mv.addObject("RIGHTS",tableName.toUpperCase());
				mv.addObject("refreshFlag",pd.get("refreshFlag"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString(), e);
		}
		return mv;
	}
}
