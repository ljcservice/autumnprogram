package com.ts.controller.ontology;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.system.User;
import com.ts.service.ontology.impl.CommonService;
import com.ts.service.ontology.impl.OsynHisService;
import com.ts.service.ontology.manager.OsynManager;
import com.ts.service.system.user.UserManager;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.UuidUtil;
import com.ts.util.app.AppUtil;
import com.ts.util.ontology.HelpUtil;
import com.ts.util.ontology.OsynConst;

/** 
 * 类名称：术语标准词同义词管理
 * 创建人：xsl
 * 更新时间：2016年10月10日
 * @version
 */
@Controller
@RequestMapping(value="/osyn")
public class OsynController extends BaseController {
	
	@Resource(name="osynService")
	private OsynManager osynService;
	
	@Resource(name="commonService")
	private CommonService commonService;
	
	@Resource(name="osynHisService")
	private OsynHisService osynHisService;

	@Resource(name="userService")
	private UserManager userService;

	/**
	 * 同义词列表 - 本体维护页面同义词列表显示
	 */
	@RequestMapping(value="/osynList")
	public ModelAndView osynList(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			//诊断本体列表
			page.setPd(pd);
			//为page的url查询赋值
			page.setWindowType(2);
			//同义词
			pd.put("osynFlag", "0");
			List<PageData> list = null;

			//初始化不执行查询
			if("0".equals(pd.getString("initFlag"))){
				mv.setViewName("ontology/onto/query/osynList");
				return mv;
			}
			
			//本体类型
			String ontoType = pd.getString("ontoType");
			String sqlName = null;
			if("51001".equals(ontoType) ){
//				list = diagService.osynList(page);
			}else if ("51003".equals(ontoType) ){
				//手术名称
				sqlName = "op";
			}else if ("51005".equals(ontoType) ){
				//诊断通用名
				sqlName = "diag";
			}
			mv.setViewName("ontology/onto/query/osynList");
			list = osynService.osynPage(page,sqlName);
			mv.addObject("pd", pd);
			mv.addObject("ontoType", ontoType);
			mv.addObject("standardId", pd.getString("standardId"));
			mv.addObject("resultList",list);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 弹窗-标准词查询,新增本体时使用
	 */
	@RequestMapping(value="/osynStand")
	public ModelAndView osynStand(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			//标准词
			pd.put("osynFlag", "1");
			page.setPd(pd);
			List<PageData> list = null;
			String sqlName = null;
			//标准词类别
			Integer osynType = Integer.valueOf(pd.getString("osynType"));
			//初始化不执行查询
			if(osynType == 10402|| osynType == 10403 || osynType == 10404 || osynType == 10405){
				//10402药品通用名；10403药品产品名；10404药品商品名；10405药品化学名
				sqlName = "drugName";
			}else if (osynType == 10301){
				//诊断通用名
				sqlName = "diag";
			}else if (osynType == 10501){
				//手术通用名
				sqlName = "op";
			}
			list = osynService.osynPage(page,sqlName);
			mv.setViewName("ontology/onto/query/standList");
			mv.addObject("pd", pd);
			mv.addObject("osynType", osynType);
			mv.addObject("resultList",list);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 术语列表，用于查询术语维护主界面显示的信息
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/osynAllList")
	@Rights(code="osyn/osynAllList")
	public ModelAndView osynAllList(Page page)throws Exception{
		ModelAndView mv = new ModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			page.setPd(pd);	
			//查询类型 同义词  标准词
			String wordTypeO = pd.getString("wordType");
			String termCategoryO = pd.getString("termCategory");
			User user = getCurrentUser();
			String ontoType="";
			//得到所有的下拉菜单项
			Map<String,String> typeMap = HelpUtil.getOsyntypeMap(user,0);
			if (!"".equals(termCategoryO)&&termCategoryO!=null) {
				ontoType=termCategoryO;
			}else{//若获取到的术语类型为空，则获取该用户所有权限中的第一个权限
				for (Entry<String,String> entry : typeMap.entrySet()) {
					ontoType = entry.getKey();
		            if (ontoType != null &&!"".equals(ontoType)) {
		                break;
		            }
		        }
			}
			if(Tools.isEmpty(ontoType)){
				return mv;
			}
			Map<String,String> wordMap = HelpUtil.getWordtypeMap(user, ontoType);
			
			Integer wordType =null;
			Integer termCategory = null;
			if("".equals(wordTypeO) || wordTypeO==null||wordMap.get(wordTypeO)==null){//判断查询的是同义词、标准词
				for (Entry<String,String> osynStand : wordMap.entrySet()) {
					wordTypeO = osynStand.getKey();
		            if (wordTypeO != null &&!"".equals(wordTypeO)) {
		                break;
		            }
		        }
			}
			wordType=Integer.valueOf(wordTypeO);
			if(!"".equals(ontoType) && ontoType!=null){
				termCategory = Integer.valueOf(ontoType);
			}
			pd.put("WORD_TYPE",wordType);
			pd.put("TERM_CATEGORY",ontoType);
			List<PageData> list = null;
			String tableName = "";
			String listPage = "";
			if(OsynConst.DIAG_NAME_NUM_CODE.equals(termCategory)){
				//诊断词语列表
				tableName="diag";
				if(wordType==0){//同义词
					listPage="osynAll";
				}else{//诊断标准词页面
					listPage="diagStandAll";
				}
			}else if (OsynConst.DRUG_FACTORY_NUM_CODE.equals(termCategory)) {//药品生产企业
				tableName="drugFactory";
				listPage="drug/factory/drug_factory_list";
			}else if (OsynConst.PACK_SPEC_NUM_CODE.equals(termCategory)) {//药品包装规格
				tableName="packSpec";
				listPage="drug/packspec/drug_pack_spec_list";
			}else if (OsynConst.DRUG_SPEC_NUM_CODE.equals(termCategory)) {//药品 规格
				tableName="drugSpec";
				listPage="drug/spec/drug_spec_list";
			}else if (OsynConst.DRUG_PACK_NUM_CODE.equals(termCategory)) {//药品包装材质
				tableName="drugPack";
				listPage="drug/pack/drug_pack_list";
			}else if (OsynConst.DRUG_FORM_NUM_CODE.equals(termCategory)) {//药品剂型
				tableName="drugForm";
				listPage="drug/form/drug_form_list";
			}else if (OsynConst.DRUG_ROUTE_NUM_CODE.equals(termCategory)) {//药品给药途径
				tableName="drugRoute";
				listPage="drug/route/drug_route_list";
			}else if (OsynConst.PACK_SPEC_UNIT_NUM_CODE.equals(termCategory)) {//药品 规格单位
				tableName="packSpecUnit";
				listPage="drug/packspecUnit/drug_packspec_unit_list";
			} else if(OsynConst.DRUG_NAME_COMMON_NUM_CODE.equals(termCategory)||OsynConst.DRUG_NAME_CHEMI_NUM_CODE.equals(termCategory)||OsynConst.DRUG_NAME_GOODS_NUM_CODE.equals(termCategory)||OsynConst.DRUG_NAME_PRODUCT_NUM_CODE.equals(termCategory)){
				//药品名称
				tableName="drugName";
				listPage="drug/name/drug_name_list";
			}else if (OsynConst.DEPT_NAME_NUM_CODE.equals(termCategory)) {
				tableName="dep";
				listPage="dep/dep_list";
			}else if (OsynConst.OPERATION_NAME_NUM_CODE.equals(termCategory)) {
				tableName="op";
				listPage="op/op_list";
			}
			list = osynService.osynListPage(page,tableName);
			mv.setViewName("ontology/osyn/"+listPage);
			mv.addObject("RIGHTS", tableName.toUpperCase());
			
			List<PageData> users = userService.listSimpleUser(pd);
			mv.addObject("userList", users);
			mv.addObject("wordType",wordType);
			mv.addObject("termCategory",termCategory);
			mv.addObject("osynList",list);
			//术语类型
			mv.addObject("osynType", ontoType);
			mv.addObject("typeMap", typeMap);
			mv.addObject("wordMap", wordMap);
			mv.addObject("pd",pd);
		}catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**去新增同义词页面-同义词副本
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toAdd")
	public ModelAndView toAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//获取当前登录用户
		User user = getCurrentUser();
		
		String termCategory = pd.getString("termCategory");
		String tableName="";
		if(OsynConst.DIAG_NAME_CODE.equals(termCategory)){
			//添加诊断词语
			tableName="diag";
			mv.setViewName("ontology/osyn/osyn_diag_edit");
		}else if (OsynConst.DRUG_FACTORY_CODE.equals(termCategory)) {//药品生产企业
			tableName="drugFactory";
			mv.setViewName("ontology/osyn/drug/factory/drug_factory_edit");
		}else if (OsynConst.DRUG_SPEC_CODE.equals(termCategory)) {//药品规格
			tableName="drugSpec";
			mv.setViewName("ontology/osyn/drug/spec/drug_spec_edit");
		}else if (OsynConst.DRUG_PACK_CODE.equals(termCategory)) {//药品包装材质
			tableName="drugPack";
			mv.setViewName("ontology/osyn/drug/pack/drug_pack_edit");
		}else if (OsynConst.PACK_SPEC_CODE.equals(termCategory)) {//药品包装规格
			tableName="packSpec";
			mv.setViewName("ontology/osyn/drug/packspec/drug_pack_spec_edit");
		}else if (OsynConst.PACK_SPEC_UNIT_CODE.equals(termCategory)) {//药品规格单位
			tableName="packSpecUnit";
			mv.setViewName("ontology/osyn/drug/packspecUnit/drug_packspec_unit_edit");
		}else if (OsynConst.DRUG_FORM_CODE.equals(termCategory)) {//药品剂型
			tableName="drugForm";
			mv.setViewName("ontology/osyn/drug/form/drug_form_edit");
		}else if (OsynConst.DRUG_ROUTE_CODE.equals(termCategory)) {//药品给药途径
			tableName="drugRoute";
			mv.setViewName("ontology/osyn/drug/route/drug_route_edit");
		}else if(OsynConst.DRUG_NAME_COMMON_CODE.equals(termCategory)||OsynConst.DRUG_NAME_PRODUCT_CODE.equals(termCategory)||OsynConst.DRUG_NAME_GOODS_CODE.equals(termCategory)||OsynConst.DRUG_NAME_CHEMI_CODE.equals(termCategory)){
			//添加药品名称：通用名、化学名、商品名、产品名
			tableName="drugName";
			mv.setViewName("ontology/osyn/drug/name/drug_name_edit");
		}else if(OsynConst.DEPT_NAME_CODE.equals(termCategory)) {
			tableName="dep";
			mv.setViewName("ontology/osyn/dep/dep_edit");
		}else if (OsynConst.OPERATION_NAME_CODE.equals(termCategory)) {
			tableName="op";
			mv.setViewName("ontology/osyn/op/op_edit");
		}
		
		mv.addObject("upuser",user);
		mv.addObject("MSG", "saveDiagOsyn");
		mv.addObject("RIGHTS", tableName.toUpperCase());
		mv.addObject("pd", pd);
		return mv;
	} 
	
	/**
	 * 新增标准词-同义词 副本
	 * @param nameHist
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveDiagOsyn")
	@ResponseBody
	public Object saveDiagOsyn() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"诊断同义词");
		String errInfo=null;
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			Date date = new Date();
			String wordType=pd.getString("WORD_TYPE");//0同义词；1标准词
			String ontoType = pd.getString("ONTO_TYPE");//词语类型
			String operation = pd.getString("OP_TYPE");//操作类型:0新增；1修改；7级联新增
			
			pd.put("UPDATE_TIME",date);//更新时间
			pd.put("H_ID",UuidUtil.get32UUID());	//新增历史表ID
			//添加或修改的是药品标准词时，存放的药品标准中文名、药品标准英文名、来源药品标准中文名、来源药品标准英文名与添加的名称中英文和来源名称中英文相同
			if("1".equals(wordType)){
				pd.put("STAD_CHN",pd.get("DN_CHN"));			//药品标准中文名
				pd.put("STAD_ENG",pd.get("DN_ENG"));			//药品标准英文名
				pd.put("ORG_STAD_CHN",pd.get("ORG_DN_CHN"));	//来源药品标准中文名
				pd.put("ORG_STAD_ENG",pd.get("ORG_DN_ENG"));	//来源药品标准中文名
				pd.put("STAD_FAC_ABB",pd.get("FAC_ABB"));	//药品标准生产企业简称
				pd.put("ORG_STAD_FAC_ABB",pd.get("ORG_FAC_ABB"));	//来源药品标准生产企业简称
			}
			String dn_id ="0";			//词语ID
			//词语类型为新增时，获取词语ID保存到副本中，审核通过后保存到词语表中
			if("0".equals(operation)||"7".equals(operation)){
				if(OsynConst.DIAG_NAME_CODE.equals(ontoType)){
					//诊断词语
					pd.put("VERSION","");	//版本，暂时为空
					dn_id=commonService.querySeqValue("seq_on_diag_name").toString();
				}else if(OsynConst.OPERATION_NAME_CODE.equals(ontoType)){
					//手术词语
					dn_id = commonService.querySeqValue("seq_on_operation_name").toString();
				}else if (OsynConst.DEPT_NAME_CODE.equals(ontoType)) {
					dn_id=commonService.querySeqValue("seq_on_dep_name").toString();
				} else{
					//获取药品同义词信息（药品名称、包装规格、生产企业等）
					dn_id = UuidUtil.get32UUID();
					
				}
				pd.put("DN_ID",dn_id);//词语ID
				//如果添加的是标准词，添加标准名称ID
				if("1".equals(wordType)){
					pd.put("STAD_DN_ID",dn_id);
				}
				errInfo=osynHisService.saveDiagOsyn(pd);
			}else if("1".equals(operation)){
				String tableName = "";		//词语表名,根据表名不同查询不同的信息表
				if(OsynConst.DIAG_NAME_CODE.equals(ontoType)){
					tableName="diag";
				}else if (OsynConst.OPERATION_NAME_CODE.equals(ontoType)) {//手术
					tableName="operation";
				}else if (OsynConst.DEPT_NAME_CODE.equals(ontoType)) {
					tableName="dep";
				} else if (OsynConst.DRUG_FACTORY_CODE.equals(ontoType)) {//药品生产企业
					tableName="drugFactory";
				}else if (OsynConst.DRUG_SPEC_CODE.equals(ontoType)) {//药品规格
					tableName="drugSpec";
				}else if (OsynConst.DRUG_PACK_CODE.equals(ontoType)) {//药品包装材质
					tableName="drugPack";
				}else if (OsynConst.PACK_SPEC_CODE.equals(ontoType)) {//药品包装规格
					tableName="packSpec";
				}else if (OsynConst.PACK_SPEC_UNIT_CODE.equals(ontoType)) {//药品规格单位
					tableName="packSpecUnit";
				}else if (OsynConst.DRUG_FORM_CODE.equals(ontoType)) {//药品剂型
					tableName="drugForm";
				}else if (OsynConst.DRUG_ROUTE_CODE.equals(ontoType)) {//药品给药途径
					tableName="drugRoute";
				}else {//药品名称
					tableName="drugName";
				}
				pd.put("ON_ID",pd.get("DN_ID"));
				pd.put("ON_NAME_ID",pd.get("DN_ID"));
				PageData oldOnto = osynService.findDiagOsynById(pd, tableName);
				String changeDesc = HelpUtil.setOsynUpdateDesc(ontoType,oldOnto,pd);//获取修改项
				if(changeDesc==null){
					errInfo="没有更改任何内容！";
				}else{
					errInfo=osynHisService.saveDiagOsyn(pd);
				}
			}
		} catch (Exception e) {
			errInfo="操作失败！";
			logger.error(e.toString(), e);
			// TODO: handle exception
		}
		map.put("result",errInfo);
		
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 查询单个同义词
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toEdit")
	@Rights(code="osyn/toEdit")
	public ModelAndView toEdit()throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//获取当前登录用户信息
		User user = getCurrentUser();
		try {
			Integer termCate = null;//词语类型： 10301诊断名称同义词
			String termCateO = pd.getString("termCategory");
			if(!"".equals(termCateO) && termCateO!=null){
				termCate = Integer.valueOf(termCateO);
			}
			String wordType=pd.getString("WORDTYPE");
			String typeName="";//同义词类型，如：诊断名称、药品名称等
			String pagePath="";//返回结果页面
			if(termCate == 10301){
				typeName="diag";
				pagePath="osyn_diag_edit";
				//类型转换，当为诊断时，dn_id为int类型
				Integer onId = Integer.valueOf(pd.getString("ON_ID"));
				pd.put("ON_NAME_ID",onId);
			}else if (OsynConst.DEPT_NAME_NUM_CODE.equals(termCate)) {//科室
				typeName="dep";
				pagePath="dep/dep_edit";
				Integer onId = Integer.valueOf(pd.getString("ON_ID"));
				pd.put("ON_NAME_ID",onId);
			}else if (OsynConst.OPERATION_NAME_NUM_CODE.equals(termCate)) {//手术
				typeName="operation";
				pagePath="op/op_edit";
				Integer onId = Integer.valueOf(pd.getString("ON_ID"));
				pd.put("ON_NAME_ID",onId);
			} else if (termCate == 10801) {//药品生产企业
				typeName="drugFactory";
				pagePath="drug/factory/drug_factory_edit";
			}else if (termCate == 10202) {//药品包装规格
				typeName="packSpec";
				pagePath="drug/packspec/drug_pack_spec_edit";
			}else if (termCate == 10204) {//药品规格表
				typeName="drugSpec";
				pagePath="drug/spec/drug_spec_edit";
			}else if (termCate == 10205) {//药品包装材质
				typeName="drugPack";
				pagePath="drug/pack/drug_pack_edit";
			}else if (termCate == 10206) {//药品包装规格单位
				typeName="packSpecUnit";
				pagePath="drug/packspecUnit/drug_packspec_unit_edit";
			}else if (OsynConst.DRUG_FORM_NUM_CODE.equals( termCate)) {//药品剂型
				typeName="drugForm";
				pagePath="drug/form/drug_form_edit";
			}else if (OsynConst.DRUG_ROUTE_NUM_CODE.equals( termCate)) {//药品给药途径
				typeName="drugRoute";
				pagePath="drug/route/drug_route_edit";
			}else{//药品名称：通用名、产品名、商品名、化学名
				typeName="drugName";
				pagePath="drug/name/drug_name_edit";
			}
			pd=osynService.findDiagOsynById(pd,typeName);//查询诊断同义词信息
			mv.setViewName("ontology/osyn/"+pagePath);
			pd.put("termCategory",termCate);
			pd.put("wordType",wordType);
			mv.addObject("RIGHTS", typeName.toUpperCase());
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString(), e);
		}
		mv.addObject("pd",pd);
		mv.addObject("upuser",user);
		mv.addObject("MSG", "edit");
		return mv;
	}
	
	/**
	 * 单个审核
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="pass")
	@ResponseBody
	public Object passAlter()throws Exception{
		String errInfo = null;
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			String status = pd.getString("status");
			User user = getCurrentUser();//获取当前登录用户信息
			String checkName = user.getUSERNAME();
			pd.put("CHECK_TIME", new Date());
			pd.put("CHECK_USER",checkName);
			pd.put("CHECK_MEMO","审批通过");
			pd.put("STATUS",status);
			if ("1".equals(status)) {//通过
				errInfo=osynService.updateDiagName(getRequest(),pd);
			}else{//拒绝
				errInfo = osynHisService.updateAlterById(pd);
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
			errInfo = "操作失败！";
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 单个审核,审核拒绝
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="refuse")
	@ResponseBody
	public Object refuseAlter()throws Exception{
		String errInfo = null;
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			pd = this.getPageData();
			//修改AI_ALTER_NAME_HIST表（审核拒绝时只执行修改副本表操作）
			User user = getCurrentUser();//获取当前登录用户信息
			String checkName = user.getUSERNAME();
			pd.put("CHECK_TIME", new Date());
			pd.put("CHECK_USER",checkName);
			pd.put("STATUS","2");
			pd.put("CHECK_MEMO","拒绝通过");
			errInfo = osynHisService.updateAlterById(pd);
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString(), e);
			errInfo = "操作失败！";
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 批量审核通过
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/passAll")
	@ResponseBody
	public Object batchPass(Page page)throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = null;
		PageData pd = new PageData();
		try {
			pd= this.getPageData();
			// 当前登录用户
			User user = getCurrentUser();
			pd = this.getPageData();
			pd.put("STATUS", 1);//0审批中1审批通过2拒绝
			pd.put("CHECK_USER", user.getUSERNAME());
			pd.put("CHECK_TIME", new Date());
			errInfo = osynService.checkPassAll(pd);
		} catch (Exception e) {
			errInfo="failed";
			logger.error(e.toString(), e);
		}
		map.put("result",errInfo);
		return AppUtil.returnObject(pd, map);
	}
	/**
	 * 审核通过共用
	 * @param h_id历史表ID
	 * @param status审核状态
	 */
	private String passInfo(String h_id,String errInfo){
		PageData pd = new PageData();
		try {
			pd = osynHisService.findAlterById(h_id);
			//修改AI_ALTER_NAME_HIST表（审核拒绝时只执行修改副本表操作）
			User user = getCurrentUser();//获取当前登录用户信息
			String checkName = user.getUSERNAME();
			Date date = new Date();
			pd.put("CHECK_TIME", date);
			pd.put("CHECK_USER",checkName);
			pd.put("STATUS","1");
			
			String ontoType =String.valueOf(pd.get("ONTO_TYPE"));
			String operation =String.valueOf(pd.get("OP_TYPE"));
			String typeName = "";
			if(OsynConst.DIAG_NAME_CODE.equals(ontoType)){//诊断
				typeName="diag";
			}else if (OsynConst.OPERATION_NAME_CODE.equals(ontoType)) {
				typeName="op";
			}else if (OsynConst.DEPT_NAME_CODE.equals(ontoType)) {
				typeName="dep";
			} else if (OsynConst.DRUG_FACTORY_CODE.equals(ontoType)) {
				typeName="drugFactory";//药品生产企业
			}else if (OsynConst.DRUG_SPEC_CODE.equals(ontoType)) {
				typeName="drugSpec";//药品规格
			}else if (OsynConst.DRUG_PACK_CODE.equals(ontoType)) {
				typeName="drugPack";//药品包装材质
			}else if (OsynConst.PACK_SPEC_CODE.equals(ontoType)) {
				typeName="packSpec";//药品包装规格
			}else if (OsynConst.PACK_SPEC_UNIT_CODE.equals(ontoType)) {
				typeName="packSpecUnit";//药品包装规格单位
			}else{//药品名称
				typeName="drug";//药品名称
			}
			//查询副本表信息
			if("0".equals(operation)||"7".equals(operation)){
				//若为“新增”或“级联新增”，添加诊断词语表信息ON_DIAG_NAME
				errInfo = osynService.insertOsynName(pd,typeName);
			}else{
				//若为修改,根据DN_ID修改词语表信息
				errInfo = osynService.updateDiagName(null,pd);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.toString(), e);
			errInfo="failed";
		}
		return errInfo;
	}
	
	/**
	 * 批量审批拒绝
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/refuseAll")
	@ResponseBody
	public Object batchRefuse()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = null;
		PageData pd = new PageData();
		pd= this.getPageData();
		try {
			String ids = pd.getString("H_IDS");
			String[] h_ids = ids.split(",");
			for (int i = 0; i < h_ids.length; i++) {
				String h_id=h_ids[i];//单个副本ID
				pd.put("H_ID",h_id);
				//修改AI_ALTER_NAME_HIST表（审核拒绝时只执行修改副本表操作）
				User user = getCurrentUser();//获取当前登录用户信息
				String checkName = user.getUSERNAME();
				pd.put("CHECK_TIME", new Date());
				pd.put("CHECK_USER",checkName);
				pd.put("STATUS","2");
				pd.put("CHECK_MEMO","拒绝通过");
				errInfo = osynHisService.updateAlterById(pd);
				if(!"success".equals(errInfo)){
					errInfo="failed";
					break;
				}
			}
		} catch (Exception e) {
			// e
			errInfo="failed";
			logger.error(e.toString(), e);
		}
		map.put("result",errInfo);
		return AppUtil.returnObject(pd, map);
	}
	
	/**
	 * 校验中文和英文名称是否存在
	 */
	@RequestMapping(value="/checkExistName")
	@ResponseBody
	public Object checkExistName() throws Exception{
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			pd = this.getPageData();
			String sqlName = null;
			//本体类型
			String ontoType = pd.getString("ontoType");
			ontoType = HelpUtil.getOsynType(ontoType);
			if(OsynConst.DRUG_NAME_COMMON_CODE.equals(ontoType)){
				sqlName = "drug";//药品名称
			}else if (OsynConst.DRUG_FACTORY_CODE.equals(ontoType)){
				sqlName="drugFactory";//药品生产企业
			}else if (OsynConst.DRUG_PACK_CODE.equals(ontoType)) {
				sqlName="drugPack";//药品包装材质
			}else if (OsynConst.DRUG_SPEC_CODE.equals(ontoType)) {
				sqlName="drugSpec";//药品规格
			}else if (OsynConst.PACK_SPEC_CODE.equals(ontoType)) {
				sqlName="packSpec";//药品包装规格
			}else if (OsynConst.PACK_SPEC_UNIT_CODE.equals(ontoType)) {
				sqlName="packSpecUnit";//药品规格单位
			}else if (OsynConst.OPERATION_NAME_CODE.equals(ontoType) ){
				sqlName = "op";//手术
			}else if (OsynConst.DIAG_NAME_CODE.equals(ontoType) ){
				sqlName = "diag";//诊断
			}else if (OsynConst.DEPT_NAME_CODE.equals(ontoType) ){//科室
				sqlName = "dep";
			}else if (OsynConst.DRUG_FORM_CODE.equals(ontoType) ){//药品剂型
				sqlName = "drugForm";
			}else if (OsynConst.DRUG_ROUTE_CODE.equals(ontoType) ){//药品给药途径
				sqlName = "drugRoute";
			}else if (OsynConst.DEPT_NAME_CODE.equals(ontoType)) {
				sqlName="dep";
			}else if (OsynConst.OPERATION_NAME_CODE.equals(ontoType)) {
				sqlName="op";
			}
			//设置standFlag是否为标准词，0为同义词1为标准词
			boolean exist = osynService.checkExistName(sqlName,pd);
			if(exist){
				map.put("result","名称已经存在");
			}else{
				map.put("result","success");
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
			map.put("result","系统繁忙，请稍后再试！");
		}
		return AppUtil.returnObject(pd, map);
	}
	/**
	 * 判断同义词对应的本体是否停用
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/isOntoDisable")
	@ResponseBody
	public Object isOntoDisable() throws Exception{
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd= this.getPageData();
			String sqlName = null;
			//本体类型
			String osynType = pd.getString("osynType");
			if (OsynConst.OPERATION_NAME_CODE.equals(osynType) ){
				sqlName = "op";//手术
			}else if (OsynConst.DIAG_NAME_CODE.equals(osynType) ){
				sqlName = "diag";//诊断
			}else if (OsynConst.DEPT_NAME_CODE.equals(osynType) ){
				sqlName = "dep";//科室
			}
			boolean isDisable= osynService.checkOntoDisable(sqlName, pd);
			if(isDisable){
				map.put("result","success");
			}else{
				map.put("result","本体停用，不能改为使用状态");
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
			map.put("result","系统繁忙，请稍后再试！");
		}
		return AppUtil.returnObject(pd, map);
	}
	
}
