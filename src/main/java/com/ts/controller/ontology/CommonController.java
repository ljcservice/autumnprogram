package com.ts.controller.ontology;

import java.util.Date;
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
import com.ts.entity.system.User;
import com.ts.service.ontology.manager.CommonManager;
import com.ts.service.ontology.manager.OntoTreeManager;
import com.ts.service.ontology.manager.OntologyManager;
import com.ts.service.ontology.manager.OsynHisManager;
import com.ts.service.ontology.manager.OsynManager;
import com.ts.util.PageData;
import com.ts.util.UuidUtil;
import com.ts.util.app.AppUtil;
import com.ts.util.ontology.HelpUtil;
import com.ts.util.ontology.OsynConst;

/** 
 * 类名称：通用方法处理，本体管理中快捷键处理
 * 创建人：xsl
 * 更新时间：2016年10月10日
 * @version
 */
@Controller
@RequestMapping(value="/common")
public class CommonController extends BaseController {
	
	
	@Resource(name="ontologyService")
	private OntologyManager ontologyService;
	
	@Resource(name="commonService")
	private CommonManager commonService;
	
	@Resource(name="ontoTreeService")
	private OntoTreeManager ontoTreeService;
	
	@Resource(name="osynService")
	private OsynManager osynService;
	
	@Resource(name="osynHisService")
	private OsynHisManager osynHisService;
	
	/**
	 * 本体维护-修改父节点-树显示
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/treeWidget")
	public ModelAndView treeWidget()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		String sqlName = null;
		try{
			pd = this.getPageData();
			//业务类型，用于区分不同页面
			String businessType = pd.getString("businessType");
			//本体类型
			String ontoType = pd.getString("ontoType");
			if(OsynConst.DRUG.equals(ontoType)){
				sqlName = "drug";
			}else if (OsynConst.OP.equals(ontoType)){
				//手术
				sqlName = "op";
			}else if (OsynConst.DIAG.equals(ontoType)){
				//诊断
				sqlName = "diag";
			}else if (OsynConst.DEP.equals(ontoType)){
				//诊断
				sqlName = "dep";
			}else{
				throw new Exception("parama is error.");
			}
			String ONTO_ID = pd.getString("ONTO_ID");
			//获取已经选择的上级信息
			List<PageData> plist = null;
			if("0".equals(businessType)){
				//诊断维护页面-修改父节点
				plist = ontologyService.queryParentOntoById(sqlName,ONTO_ID);
			}else if("1".equals(businessType)){
				//本体审核页面，修改父节点
				plist = ontologyService.queryParentOntoHisInfo(sqlName, pd);
			}else if("2".equals(businessType)){
				//诊断维护页面，科室分类选择
				plist = ontologyService.queryDepCategory(ONTO_ID);
			}else if("3".equals(businessType)){
				//诊断审核页面，科室分类选择
				plist = ontologyService.queryDepCategoryHis(pd);
			}else{
				throw new Exception("parama businessType is error!");
			}
			mv.setViewName("ontology/onto/common/tree_widget");
			mv.addObject("businessType", businessType);
			mv.addObject("pd", pd);
			mv.addObject("ontoType", ontoType);
			mv.addObject("PARENT_LIST", plist);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 本体维护-修改父节点-显示同义词和本体
	 */
	@RequestMapping(value="/ontoWidget")
	public ModelAndView osynWidget(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			//诊断本体列表
			page.setPd(pd);
			//同义词
			pd.put("osynFlag", "0");
			List<PageData> list = null;
			String sqlName = null;
			//本体类型
			String ontoType = pd.getString("ontoType");
			if(OsynConst.DRUG.equals(ontoType)){
				sqlName = "drug";
			}else if (OsynConst.OP.equals(ontoType)){
				//手术
				sqlName = "op";
			}else if (OsynConst.DIAG.equals(ontoType)){
				//诊断
				sqlName = "diag";
			}else if (OsynConst.DEP.equals(ontoType)){
				//诊断
				sqlName = "dep";
			}else{
				throw new Exception("parama ontoType is error!");
			}
			mv.setViewName("ontology/onto/common/onto_widget");
			list = commonService.ontoWidgetPage(page,sqlName);
			mv.addObject("businessType",pd.get("businessType"));
			mv.addObject("pd", pd);
			mv.addObject("ontoType", ontoType);
			mv.addObject("resultList",list);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 诊断本体维护页面-保存修改父节点事件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/editParent")
	@ResponseBody
	public Object editParent()throws Exception{
		String errInfo = null;
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			User user = getCurrentUser();
			pd.put("UPDATE_MAN", user.getUSERNAME());
			pd.put("UPDATE_USER", user.getUSERNAME());
			pd.put("UPDATE_TIME", new Date());
			errInfo = ontoTreeService.saveOntoParentTree(getRequest(),pd);
		} catch (Exception e) {
			errInfo="操作失败！";
			logger.error(e.toString(),e);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	} 
	
	@RequestMapping(value="/stopOntology")
	@ResponseBody
	public Object stopOntology()throws Exception{
		String errInfo = "success";
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			User user = getCurrentUser();
			pd.put("UPDATE_MAN", user.getUSERNAME());
			pd.put("UPDATE_USER", user.getUSERNAME());
			pd.put("UPDATE_TIME", new Date());
			ontologyService.stopOntology(pd);
		} catch (Exception e) {
			errInfo="操作失败！";
			logger.error(e.toString(),e);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	@RequestMapping(value="/stopOsyn")
	@ResponseBody
	public Object stopOsyn()throws Exception{
		String errInfo = "success";
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		String tableName = null;
		try {
			User user = getCurrentUser();
			pd = this.getPageData();
			String ontoType = pd.getString("ontoType");
			ontoType = HelpUtil.getOsynType(ontoType);
			Object ON_NAME_ID = pd.get("OSYN_ID");
			pd.put("ON_NAME_ID", ON_NAME_ID);
			if(OsynConst.DIAG_NAME_CODE.equals(ontoType)){
				tableName="diag";
			}else if (OsynConst.OPERATION_NAME_CODE.equals(ontoType)) {//手术
				tableName="operation";
			}else if (OsynConst.DEPT_NAME_CODE.equals(ontoType)) {//手术
				tableName="dep";
			}
			pd = osynService.findDiagOsynById(pd, tableName);
			//字段名称转换成历史表使用的字段名
			if (OsynConst.OPERATION_NAME_CODE.equals(ontoType)) {
				//手术
				pd.put("DN_CHN", pd.get("ON_CHN"));
				pd.put("DN_ENG", pd.get("ON_ENG"));
				pd.put("ORG_DN_CHN", pd.get("ORG_OP_CHN"));
				pd.put("ORG_DN_ENG", pd.get("ORG_OP_ENG"));
				pd.put("STAD_DN_ID", pd.get("STAD_ON_ID"));
			}else if (OsynConst.DEPT_NAME_CODE.equals(ontoType)) {
				//科室
				pd.put("DN_CHN", pd.get("DEP_NAME"));
				pd.put("DN_ENG", "");
				pd.put("ORG_DN_CHN", "");
				pd.put("ORG_DN_ENG", "");
				pd.put("STAD_DN_ID", pd.get("STAD_ID"));
			}
			pd.put("H_ID",  UuidUtil.get32UUID());
			pd.put("DN_ID", ON_NAME_ID);
			//根据本体类型 获取同义词类型
			pd.put("ONTO_TYPE", ontoType);
			pd.put("OP_TYPE", 4);//停用术语
			pd.put("IS_DISABLE", 1);//停用
			pd.put("STATUS", 0);
			pd.put("OPERATION","");
			pd.put("VERSION", "1.0.0");
			pd.put("UPDATE_MAN", user.getUSERNAME());
			pd.put("UPDATE_USER", user.getUSERNAME());
			pd.put("UPDATE_TIME", new Date());
			pd.put("UPD_DESC","快捷键停用术语");
			//保存新增同义词
			osynHisService.saveDiagOsyn(pd);
		} catch (Exception e) {
			errInfo="操作失败！";
			logger.error(e.toString(),e);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 进入诊断本体新增页面-新增同义词页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toSubOsynAdd")
	public ModelAndView toSubOsynAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//本体类型
		String ontoType = pd.getString("ontoType");
		String sqlName = null;
		if (OsynConst.OP.equals(ontoType)){
			//手术
			sqlName = "op";
		}else if (OsynConst.DIAG.equals(ontoType)){
			//诊断
			sqlName = "diag";
		}else if (OsynConst.DEP.equals(ontoType)){
			//科室
			sqlName = "dep";
		}
		mv.setViewName("ontology/onto/common/"+sqlName+"SubOsynEdit");
		mv.addObject("ontoType", ontoType);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 诊断本体维护页面-新增同义词页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toOsynAdd")
	public ModelAndView toAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//本体类型
		String ontoType = HelpUtil.getOsynType(pd.getString("ontoType"));
		pd.put("ontoType", ontoType);
		String STAND_ID = pd.getString("STAND_ID");
		//查询标准词信息
		pd.put("ON_NAME_ID", STAND_ID);
		String sqlName = null;
		if(OsynConst.DRUG.equals(ontoType)){
			
		}else if (OsynConst.OPERATION_NAME_CODE.equals(ontoType)){
			//手术
			sqlName = "operation";
		}else if (OsynConst.DIAG_NAME_CODE.equals(ontoType)){
			//诊断
			sqlName = "diag";
		}else if (OsynConst.DEPT_NAME_CODE.equals(ontoType)){
			//科室
			sqlName = "dep";
		}else{
			throw new Exception("parama is error.");
		}
		
		//查询单个同义词信息
		PageData stand = osynService.findDiagOsynById(pd, sqlName);
		pd.put("stad_dn_id", STAND_ID);
		pd.put("standard_name", stand.get("standard_name"));
		mv.setViewName("ontology/onto/common/osyn_edit");
		mv.addObject("ontoType", ontoType);
		mv.addObject("pd", pd);
		mv.addObject("msg", "osynAdd");
		return mv;
	}
	
	/**
	 * 诊断本体维护页面-保存新的同义词
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/osynAdd")
	@ResponseBody
	public Object osynAdd()throws Exception{
		String errInfo = "success";
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		try {
			User user = getCurrentUser();
			pd = this.getPageData();
			String ontoType = pd.getString("ontoType");
			String name = null;
			if(OsynConst.DRUG.equals(ontoType)){
				
			}else if (OsynConst.OPERATION_NAME_CODE.equals(ontoType)){
				//手术
				name = "operation";
			}else if (OsynConst.DIAG_NAME_CODE.equals(ontoType)){
				//诊断
				name = "diag";
			}else if (OsynConst.DEPT_NAME_CODE.equals(ontoType)){
				//科室
				name = "dep";
			}else{
				throw new Exception("param ontoType is error.");
			}
			if("0".equals(pd.getString("IS_DISABLE"))){
				//校验标准词是否停用
				pd.put("ON_NAME_ID", pd.get("STAD_DN_ID"));//标准词ID
				PageData stand = osynService.findDiagOsynById(pd, name);
				if("1".equals(stand.get("IS_DISABLE").toString())){
					map.put("result", "标准词"+pd.getString("standard_name")+"已经被停用，所以同义词不能修改为启用。");
					return AppUtil.returnObject(new PageData(), map);
				}
			}
			pd.put("H_ID",  UuidUtil.get32UUID());
			//获取一个同义词序列
			int DN_ID = commonService.getSequenceID(ontoType,1);
			pd.put("DN_ID", DN_ID);
			//根据本体类型 获取同义词类型
			pd.put("ONTO_TYPE", HelpUtil.getOsynType(ontoType));
			pd.put("OP_TYPE", 0);
			pd.put("STATUS", 0);
			pd.put("OPERATION","");
			pd.put("VERSION", "1.0.0");
			pd.put("UPDATE_MAN", user.getUSERNAME());
			pd.put("UPDATE_USER", user.getUSERNAME());
			pd.put("UPDATE_TIME", new Date());
			pd.put("UPD_DESC","新增同义词");
			//保存新增同义词
			osynHisService.saveDiagOsyn(pd);
		} catch (Exception e) {
			errInfo = "操作失败！";
			logger.error(e.toString(),e);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	} 
	
	/**
	 * 诊断本体维护页面-编辑同义词页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toOsynEdit")
	public ModelAndView toOsynEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			//本体类型
			String ontoType = HelpUtil.getOsynType(pd.getString("ontoType"));
			pd.put("ontoType", ontoType);
			String OSYN_ID = pd.getString("OSYN_ID");
			//查询标准词信息
			pd.put("ON_NAME_ID", OSYN_ID);
			String sqlName = null;
			if(OsynConst.DRUG.equals(ontoType)){
				
			}else if (OsynConst.OPERATION_NAME_CODE.equals(ontoType)){
				//手术
				sqlName = "operation";
			}else if (OsynConst.DIAG_NAME_CODE.equals(ontoType)){
				//诊断
				sqlName = "diag";
			}else if (OsynConst.DEPT_NAME_CODE.equals(ontoType)){
				//科室
				sqlName = "dep";
			}else{
				throw new Exception("param ontoType is error.");
			}
			//查询单个同义词信息
			pd = osynService.findDiagOsynById(pd, sqlName);
			//字段转换
			if (OsynConst.OPERATION_NAME_CODE.equals(ontoType)){
				//手术
				pd.put("DN_ID", pd.get("ON_ID"));
				pd.put("DN_CHN", pd.get("ON_CHN"));
				pd.put("DN_ENG", pd.get("ON_ENG"));
				pd.put("ORG_DN_CHN", pd.get("ORG_OP_CHN"));
				pd.put("ORG_DN_ENG", pd.get("ORG_OP_ENG"));
				pd.put("STAD_DN_ID", pd.get("STAD_ON_ID"));
			}else if (OsynConst.DIAG_NAME_CODE.equals(ontoType)){
				//诊断
			}else if (OsynConst.DEPT_NAME_CODE.equals(ontoType)){
				//科室
				pd.put("DN_ID", pd.get("ID"));
				pd.put("DN_CHN", pd.get("DEP_NAME"));
				pd.put("TERM_DEFIN", pd.get("DEFINITION"));
				pd.put("STAD_DN_ID", pd.get("STAD_ID"));
			} 
			mv.setViewName("ontology/onto/common/osyn_edit");
			mv.addObject("ontoType", ontoType);
			mv.addObject("pd", pd);
			mv.addObject("msg", "osynEdit");
		} catch (Exception e) {
			logger.error(e.toString(),e);
		}
		return mv;
	}
	
	/**
	 * 诊断本体维护页面-保存新的同义词
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/osynEdit")
	@ResponseBody
	public Object osynEdit()throws Exception{
		String errInfo = "success";
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		try {
			User user = getCurrentUser();
			pd = this.getPageData();
			String ontoType = pd.getString("ontoType");
			String name = null;
			if(OsynConst.DRUG.equals(ontoType)){
				
			}else if (OsynConst.OPERATION_NAME_CODE.equals(ontoType)){
				//手术
				name = "operation";
			}else if (OsynConst.DIAG_NAME_CODE.equals(ontoType)){
				//诊断
				name = "diag";
			}else if (OsynConst.DEPT_NAME_CODE.equals(ontoType)){
				//科室
				name = "dep";
			}else{
				throw new Exception("param ontoType is error.");
			}
			if("0".equals(pd.getString("IS_DISABLE"))){
				//校验标准词是否停用
				pd.put("ON_NAME_ID", pd.get("STAD_DN_ID"));//标准词ID
				PageData stand = osynService.findDiagOsynById(pd, name);
				if("1".equals(stand.get("IS_DISABLE").toString())){
					map.put("result", "标准词"+pd.getString("standard_name")+"已经被停用，所以同义词不能修改为启用。");
					return AppUtil.returnObject(new PageData(), map);
				}
			}
			
			pd.put("H_ID",  UuidUtil.get32UUID());
			//根据本体类型 获取同义词类型
			pd.put("ONTO_TYPE", HelpUtil.getOsynType(ontoType));
			pd.put("OP_TYPE", 1);
			pd.put("STATUS", 0);
			pd.put("OPERATION","");
			pd.put("UPDATE_MAN", user.getUSERNAME());
			pd.put("UPDATE_USER", user.getUSERNAME());
			pd.put("UPDATE_TIME", new Date());
			pd.put("UPD_DESC","修改同义词");
			//保存新增同义词
			osynHisService.saveDiagOsyn(pd);
		} catch (Exception e) {
			errInfo="保存失败";
			logger.error(e.toString(),e);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	} 
	
	/**
	 * 术语知识编辑页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toKnowledgeEdit")
	public ModelAndView toKnowledgeEdit() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		String sqlName = null;
		try {
			pd = this.getPageData();
			//本体类型
			String ontoType = pd.getString("ontoType");
			if (OsynConst.DIAG.equals(ontoType)){
				//诊断
				sqlName = "diag";
			}else{
				throw new Exception("Parama ontoType is error!");
			}
			mv.setViewName("ontology/onto/common/knowledgeEdit");
			String ONTOLOGY_ID = pd.getString("ONTO_ID");
			pd = ontologyService.selectOntologyById(sqlName,ONTOLOGY_ID);
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(),e);
		}
		return mv;
	}
	/**
	 * 术语知识编辑保存
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/knowledgeEdit")
	@ResponseBody
	public Object knowledgeEdit()throws Exception{
		String errInfo = "success";
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			User user = getCurrentUser();
			pd.put("UPDATE_MAN", user.getUSERNAME());
			pd.put("UPDATE_USER", user.getUSERNAME());
			pd.put("UPDATE_TIME", new Date());
			ontologyService.knowledgeOntoEdit(pd);
		} catch (Exception e) {
			errInfo="操作失败！";
			logger.error(e.toString(),e);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
}
