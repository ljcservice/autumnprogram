package com.ts.controller.ontology;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.system.User;
import com.ts.service.ontology.manager.OntologyManager;
import com.ts.service.ontology.manager.OsynHisManager;
import com.ts.service.system.user.UserManager;
import com.ts.util.Const;
import com.ts.util.FileDownload;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.PathUtil;
import com.ts.util.Tools;
import com.ts.util.app.AppUtil;
import com.ts.util.ontology.HelpUtil;
import com.ts.util.ontology.OsynConst;


/** 
 * 类名称：本体管理
 * 创建人：xsl
 * 更新时间：2016年10月10日
 * @version
 */
@Controller
@RequestMapping(value="/ontology")
public class OntologyController extends BaseController {
	
	@Resource(name="ontologyService")
	private OntologyManager ontologyService;
	
	@Resource(name="userService")
	private UserManager userService;
	
	@Resource(name="osynHisService")
	private OsynHisManager osynHisService;
	
	
	/***************************** 本体维护 sart ********************************/
	
	/**
	 * 本体维护-初始页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/show")
	public ModelAndView listRole()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			User user = getCurrentUser();
			//得到有的的下拉菜单
			Map<String,String> typeMap = HelpUtil.getOntotypeMap(user,0);
			List<PageData> users = userService.listSimpleUser(pd);
			mv.addObject("userList", users);
			//本体类型
			mv.addObject("ontoType", typeMap.keySet().iterator().next());
			mv.setViewName("ontology/onto/query/all");
			mv.addObject("typeMap", typeMap);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	

	/**
	 * 本体维护-查询列表
	 */
	@RequestMapping(value="/ontologyList")
	public ModelAndView listDiag(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			if(pd.getString("keywords")!=null){
				String key = new String(pd.getString("keywords").getBytes("ISO-8859-1"),"UTF-8");
				pd.put("keywords", key);
			}
			page.setPd(pd);
			page.setWindowType(1);
			List<PageData> list = null;
			String sqlName = null;
			//本体类型
			String ontoType = pd.getString("ontoType");
			pd.put("OSYNTYPE", HelpUtil.getOsynType(ontoType));
			if(Tools.isEmpty(ontoType)){
				return mv;
			}
			if(OsynConst.DRUG.equals(ontoType) ){
				//药品
				mv.addObject("ontoName", "药品 ");
				sqlName = "drug";
			}else if (OsynConst.OP.equals(ontoType) ){
				//手术
				mv.addObject("ontoName", "手术");
				sqlName = "op";
			}else if (OsynConst.DIAG.equals(ontoType) ){
				//诊断
				mv.addObject("ontoName", "诊断");
				sqlName = "diag";
				Map<String,String> partMap = HelpUtil.getPartBodyMap(0);//获取部位分类信息
				Map<String,String> crowsMap = HelpUtil.getPartBodyMap(1);//获取人群分类信息
				Map<String,String> disMap = HelpUtil.getPartBodyMap(2);
				mv.addObject("partMap",partMap);
				mv.addObject("crowsMap",crowsMap);
				mv.addObject("disMap",disMap);
			}else if (OsynConst.DEP.equals(ontoType) ){
				//科室
				mv.addObject("ontoName", "科室");
				sqlName = "dep";
			}
			mv.setViewName("ontology/onto/query/ontoList");
			list = ontologyService.ontologyPage(page,sqlName);
			mv.addObject("ontoType", ontoType);
			mv.addObject("resultList",list);
			mv.addObject("pd", pd);
			mv.addObject("RIGHTS", sqlName.toUpperCase());
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 本体维护-去新增本体页面
	 */
	@RequestMapping(value="/toAdd")
	public ModelAndView toAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			String sqlName = null;
			//本体类型
			String ontoType = pd.getString("ontoType");
			if(OsynConst.DRUG.equals(ontoType)){
				//药品 TODO
			}else if (OsynConst.OP.equals(ontoType)){
				//手术
				sqlName = "op";
			}else if (OsynConst.DIAG.equals(ontoType)){
				//诊断
				sqlName = "diag";
				Map<String,String> partMap = HelpUtil.getPartBodyMap(0);
				Map<String,String> crowsMap = HelpUtil.getPartBodyMap(1);
				Map<String,String> disMap = HelpUtil.getPartBodyMap(2);
				mv.addObject("partMap",partMap);
				mv.addObject("crowsMap",crowsMap);
				mv.addObject("disMap",disMap);
			}else if (OsynConst.DEP.equals(ontoType)){
				//诊断
				sqlName = "dep";
			}
			mv.setViewName("ontology/onto/query/"+sqlName+"Edit");
			mv.addObject("ontoType", ontoType);
			mv.addObject("EDIT_FLAG", 0);
			mv.addObject("MSG", "add");
			mv.addObject("pd", pd);
			mv.addObject("RIGHTS", sqlName.toUpperCase());
		} catch (Exception e) {
			logger.error(e.toString(),e);
		}
		return mv;
	}
	
	/**
	 * 本体维护-新增本体（保存副本）
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add( ) throws Exception{
		String errInfo = null;
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			User user = getCurrentUser();
			//设置通用数据
			pd.put("UPDATE_MAN", user.getUSERNAME());
			pd.put("UPDATE_USER", user.getUSERNAME());
			pd.put("UPDATE_TIME", new Date());
			errInfo = ontologyService.saveOntoCopy(getRequest(),pd,0);
		} catch (Exception e) {
			errInfo="保存失败！";
			logger.error(e.toString(),e);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	
	/**
	 * 本体维护-去修改本体页面
	 */
	@RequestMapping(value="/toEdit")
	public ModelAndView toEdit() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		String sqlName = null;
		try {
			pd = this.getPageData();
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
				Map<String,String> partMap = HelpUtil.getPartBodyMap(0);
				Map<String,String> crowsMap = HelpUtil.getPartBodyMap(1);
				Map<String,String> disMap = HelpUtil.getPartBodyMap(2);
				mv.addObject("partMap",partMap);
				mv.addObject("crowsMap",crowsMap);
				mv.addObject("disMap",disMap);
			}else if (OsynConst.DEP.equals(ontoType)){
				//诊断
				sqlName = "dep";
			}else{
				throw new Exception("parama ontoType is error.");
			}
			mv.setViewName("ontology/onto/query/"+sqlName+"Edit");
			String ONTOLOGY_ID = pd.getString("ONTO_ID");
			//如果含有正在编辑的本体历史信息，不允许修改
			List<PageData> list = ontologyService.selectOntologyHisByOntoId(pd);
			if(CollectionUtils.isEmpty(list)){
				mv.addObject("EDIT_FLAG", 0);
			}else {
				mv.addObject("EDIT_FLAG", 1);
			}
			pd = ontologyService.selectOntologyById(sqlName,ONTOLOGY_ID);
			if(OsynConst.DRUG.equals(ontoType)){
				
			}else if (OsynConst.OP.equals(ontoType)){
				if(pd.getString("OP_CODE")!=null && pd.getString("OP_CODE").length()<8){
					mv.addObject("EDIT_FLAG", 1);
				}
			}else if (OsynConst.DIAG.equals(ontoType)){
				//只有8位的主要编码的 本体才允许编辑，其他本体不允许编辑
				if(pd.getString("MAIN_CODE")!=null && pd.getString("MAIN_CODE").length()<9){
					mv.addObject("EDIT_FLAG", 1);
				}else if(pd.getString("ADD_CODE")!=null && pd.getString("ADD_CODE").length()<9){
					mv.addObject("EDIT_FLAG", 1);
				}
			}else if (OsynConst.DEP.equals(ontoType)){
				//都允许修改
			}
			mv.addObject("ontoType", ontoType);
			mv.addObject("MSG", "edit");
			mv.addObject("pd", pd);
			mv.addObject("RIGHTS", sqlName.toUpperCase());
		} catch (Exception e) {
			logger.error(e.toString(),e);
		}
		return mv;
	}

	/**
	 * 本体维护-更新本体（保存副本）
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit() throws Exception{
		String errInfo = null;
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		try {
			User user = getCurrentUser();
			pd = this.getPageData();
			//设置pd数据
			pd.put("UPDATE_MAN", user.getUSERNAME());
			pd.put("UPDATE_TIME", new Date());
			errInfo = ontologyService.saveOntoCopy(getRequest(),pd,1);
		} catch (Exception e) {
			errInfo = "操作失败！";
			logger.error(e.toString(),e);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 本体维护-单个本体信息(目前只有诊断需要次功能)
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/ontoDetail")
	@ResponseBody
	public Object ontoDetail() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		String errInfo = "success";
		try{
			pd = this.getPageData();
			//本体类型
			String ontoType = pd.getString("ontoType");
			String sqlName = null;
			//字段转换
			if(OsynConst.DRUG.equals(ontoType)){
				//药品
			}else if (OsynConst.OP.equals(ontoType)){
				//判断诊断本体中手术名称是否存在
			}else if (OsynConst.DIAG.equals(ontoType)){
				//诊断
				sqlName = "diag";
			}
			pd = ontologyService.selectOntologyById(sqlName,pd.getString("ID"));
			if (OsynConst.DIAG.equals(ontoType)){
				//诊断
				Map<String,String> partMap = HelpUtil.getPartBodyMap(0);
				Map<String,String> crowsMap = HelpUtil.getPartBodyMap(1);
				Map<String,String> disMap = HelpUtil.getPartBodyMap(2);
				if(pd.get("PART_CATEGORY")!=null){
					pd.put("PART_CATEGORY", partMap.get(pd.getString("PART_CATEGORY")));
				}
				if(pd.get("MAN_CATEGORY")!=null){
					pd.put("MAN_CATEGORY", crowsMap.get(pd.getString("MAN_CATEGORY")));
				}
				if(pd.get("DIS_CATEGORY")!=null){
					pd.put("DIS_CATEGORY", disMap.get(pd.getString("DIS_CATEGORY")));
				}
			}
		} catch(Exception e){
			errInfo = "请稍后再试！";		
			logger.error(e.toString(), e);
		}
		map.putAll(pd);
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	
	/***************************** 本体维护 end ********************************/
	
	
	/***************************** 本体审核  start********************************/

	/**
	 * 本体审核-初始页面
	 */
	@RequestMapping(value="/check")
	public ModelAndView check(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			page.setPd(pd);
			User user = getCurrentUser();
			//得到有的的下拉菜单
			Map<String,String> typeMap = HelpUtil.getOntotypeMap(user,1);
			List<PageData> users = userService.listSimpleUser(pd);
			mv.addObject("userList", users);
			mv.setViewName("ontology/onto/check/all");
			//本体类型
			mv.addObject("ontoType", typeMap.keySet().iterator().next());
			mv.addObject("typeMap", typeMap);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 本体审核-查询列表
	 */
	@RequestMapping(value="/checkList")
	public ModelAndView checkList(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			if(pd.getString("keywords")!=null){
				String key = new String(pd.getString("keywords").getBytes("ISO-8859-1"),"UTF-8");
				pd.put("keywords", key);
			}
			//诊断本体列表
			page.setPd(pd);
			page.setWindowType(1);
			List<PageData> list = null;
			String sqlName = null;
			//本体类型
			String ontoType = pd.getString("ontoType");
			if(Tools.isEmpty(ontoType)){
				mv.setViewName("ontology/onto/check/diagList");
				return mv;
			}
			if(OsynConst.DRUG.equals(ontoType) ){
				//药品
				mv.addObject("ontoName", "药品 ");
				sqlName = "drug";
			}else if (OsynConst.OP.equals(ontoType) ){
				//诊断
				mv.addObject("ontoName", "手术");
				sqlName = "op";
			}else if (OsynConst.DIAG.equals(ontoType) ){
				//诊断
				mv.addObject("ontoName", "诊断");
				sqlName = "diag";
			}else if (OsynConst.DEP.equals(ontoType) ){
				//诊断
				mv.addObject("ontoName", "科室");
				sqlName = "dep";
			}
			mv.setViewName("ontology/onto/check/ontoList");
			list = ontologyService.ontoCheckPage(page,"diag");
			mv.addObject("ontoType", ontoType);
			mv.addObject("resultList",list);
			mv.addObject("pd", pd);
			mv.addObject("RIGHTS", sqlName.toUpperCase());
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 本体审核-单个详情
	 */
	@RequestMapping(value="/detailInfo")
	public ModelAndView detailInfo() throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			//诊断本体列表
			String sqlName = null;
			//本体类型
			String ontoType = pd.getString("ontoType");
			if(OsynConst.DRUG.equals(ontoType) ){
				//药品
				sqlName = "drug";
			}else if (OsynConst.OP.equals(ontoType) ){
				//手术
				sqlName = "op";
			}else if (OsynConst.DIAG.equals(ontoType) ){
				//诊断
				sqlName = "diag";
				Map<String,String> partMap = HelpUtil.getPartBodyMap(0);
				Map<String,String> crowsMap = HelpUtil.getPartBodyMap(1);
				Map<String,String> disMap = HelpUtil.getPartBodyMap(2);
				mv.addObject("partMap",partMap);
				mv.addObject("crowsMap",crowsMap);
				mv.addObject("disMap",disMap);
			}else if (OsynConst.DEP.equals(ontoType) ){
				//科室
				sqlName = "dep";
			}
			mv.setViewName("ontology/onto/check/"+sqlName+"Info");
			mv.addObject("ontoType", ontoType);
			mv.addObject("nextShow", pd.get("nextShow"));
			//显示下一条
			if("1".equals(pd.getString("nextShow"))){
				//点击dialog关闭按钮时刷新页面的标准
				pd.put("refreshFlag", "1");
				Page page = new Page();
				page.setPd(pd);
				List<PageData> list = ontologyService.ontoCheckPage(page,sqlName);
				if(!CollectionUtils.isEmpty(list)){
					PageData ontology = list.get(0);
					//要显示的下一条ID
					pd.put("H_ID", ontology.get("H_ID"));
				}else{
					//无需要审核的数据，跳转到提示页面
					mv.addObject("msg", "当前查询条件下，没有需要审核的数据！");
					mv.setViewName("save_result");
					mv.addObject("refreshFlag",pd.get("refreshFlag"));
					return mv;
				}
			}
			if(pd.getString("H_ID")!=null){	
				PageData newOnto = ontologyService.selectOntologyHisById(sqlName,pd.getString("H_ID"));
				PageData oldOnto = null;
				if(((BigDecimal)newOnto.get("OP_TYPE")).intValue()!=0){
					//不为新增时，查找上一条历史，做对比
					String old_h_id = ontologyService.queryBrotherHisInfo(newOnto);
					if(!Tools.isEmpty(old_h_id)){
						oldOnto = ontologyService.selectOntologyHisById(sqlName,old_h_id);
					}
				}
				PageData param = new PageData();
				param.put("ONTO_H_ID", newOnto.getString("H_ID"));
				param.put("ontoType", ontoType);
				List<PageData> osynList = osynHisService.queryOsynHis(param);
				mv.addObject("osynList", osynList);
				mv.addObject("oldOnto",oldOnto);
				mv.addObject("newOnto",newOnto);
			}
			mv.addObject("refreshFlag",pd.get("refreshFlag"));
			mv.addObject("pd", pd);
			mv.addObject("RIGHTS", sqlName.toUpperCase());
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 本体审核-审核通过
	 */
	@RequestMapping(value="/checkPass")
	@ResponseBody
	public Object pass() throws Exception{
		String errInfo = null;
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			// 当前登录用户
			User user = getCurrentUser();
			pd = this.getPageData();
			pd.put("STATUS", 1);//0审批中1审批通过2拒绝
			pd.put("CHECK_USER", user.getUSERNAME());
			pd.put("CHECK_TIME", new Date());
			//本体类型
			String ontoType = pd.getString("ontoType");
			if(OsynConst.DIAG.equals(ontoType) ){
				//诊断
			}else if (OsynConst.OP.equals(ontoType) ){
				//手术
			}else if (OsynConst.DEP.equals(ontoType) ){
				//科室
			}else{
				throw new Exception("parama ontoType is error.");
			}
			errInfo = ontologyService.updateCheckPass(getRequest(),pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
			errInfo = "操作失败！";
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 本体审核-审核拒绝
	 */
	@RequestMapping(value="/checkRefuse")
	@ResponseBody
	public Object refuse() throws Exception{
		String errInfo = null;
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			// 当前登录用户
			User user = getCurrentUser();
			pd = this.getPageData();
			pd.put("STATUS", 2);//0审批中1审批通过2拒绝
			pd.put("CHECK_USER", user.getUSERNAME());
			pd.put("CHECK_TIME", new Date());
			errInfo = ontologyService.updateCheckRefuse(pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
			errInfo = "操作失败！";
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 本体审核-审核通过所有
	 */
	@RequestMapping(value="/checkPassAll")
	@ResponseBody
	public Object passAll() throws Exception{
		String errInfo = null;
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			// 当前登录用户
			User user = getCurrentUser();
			pd = this.getPageData();
			pd.put("STATUS", 1);//0审批中1审批通过2拒绝
			pd.put("CHECK_USER", user.getUSERNAME());
			pd.put("CHECK_TIME", new Date());
			errInfo = ontologyService.updateCheckPassAll(pd);
			map.put("result","success");
		} catch(Exception e){
			logger.error(e.toString(), e);
			errInfo = "操作失败！";
		}
		map.put("result",errInfo);
		return AppUtil.returnObject(pd, map);
	}
	
	/**
	 * 本体审核-审核拒绝所有
	 */
	@RequestMapping(value="/checkRefuseAll")
	@ResponseBody
	public Object refuseAll() throws Exception{
		String errInfo = null;
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			// 当前登录用户
			User user = getCurrentUser();
			pd = this.getPageData();
			pd.put("STATUS", 2);//0审批中1审批通过2拒绝
			pd.put("CHECK_USER", user.getUSERNAME());
			pd.put("CHECK_TIME", new Date());
			errInfo = ontologyService.updateCheckRefuseAll(pd);
			map.put("result","success");
		} catch(Exception e){
			logger.error(e.toString(), e);
			errInfo = "操作失败！";
		}
		map.put("result",errInfo);
		return AppUtil.returnObject(pd, map);
	}
	
	/************************* 本体历史   *************************/
	
	
	/**
	 * 本体历史首页
	 */
	@RequestMapping(value="/history")
	public ModelAndView history() throws Exception{
		PageData pd = new PageData();
		ModelAndView mv = this.getModelAndView();
		try{
			User user = getCurrentUser();
			List<PageData> users = userService.listSimpleUser(pd);
			Map<String,String> typeMap = HelpUtil.getOntotypeMap(user,1);
			mv.addObject("userList", users);
			mv.addObject("ontoType", typeMap.keySet().iterator().next());
			mv.addObject("typeMap", typeMap);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		mv.setViewName("ontology/onto/his/all");
		return mv;
	}
	
	/**
	 * 本体历史首页
	 */
	@RequestMapping(value="/hisList")
	public ModelAndView hisList(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			page.setPd(pd);
			page.setWindowType(1);
			//诊断本体列表
			String sqlName = null;
			//本体类型
			String ontoType = pd.getString("ontoType");
			if(Tools.isEmpty(ontoType)){
				mv.setViewName("ontology/onto/his/diagList");
				return mv;
			}
			if(OsynConst.DRUG.equals(ontoType) ){
				//药品
				sqlName = "drug";
			}else if (OsynConst.OP.equals(ontoType) ){
				//诊断
				sqlName = "op";
			}else if (OsynConst.DIAG.equals(ontoType) ){
				//诊断
				sqlName = "diag";
			}
			List<PageData> list = ontologyService.queryOntoHisPage(sqlName,page);
			mv.addObject("resultList",list);
			mv.addObject("ontoType", ontoType);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		mv.setViewName("ontology/onto/his/diagList");
		return mv;
	}
	
	
	@RequestMapping(value="/hisDetail")
	public ModelAndView hisDetail() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			//诊断本体列表
			String sqlName = null;
			//本体类型
			String ontoType = pd.getString("ontoType");
			if(OsynConst.DRUG.equals(ontoType) ){
				//药品
				sqlName = "drug";
			}else if (OsynConst.OP.equals(ontoType) ){
				//手术
				sqlName = "op";
			}else if (OsynConst.DIAG.equals(ontoType) ){
				//诊断
				sqlName = "diag";
			}else if (OsynConst.DEP.equals(ontoType) ){
				//诊断
				sqlName = "dep";
			}
			mv.setViewName("ontology/onto/his/"+sqlName+"Detail");
			List<PageData> list = ontologyService.queryOntoHisDetail(sqlName,pd);
			mv.addObject("resultList",list);
			mv.addObject("ontoType", ontoType);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	@RequestMapping(value="/importOntology")
	public ModelAndView importOntology() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			User user = getCurrentUser();
			//得到有的的下拉菜单
			Map<String,String> typeMap = HelpUtil.getOntotypeMap(user,99);
			mv.addObject("typeMap", typeMap);
			mv.setViewName("ontology/onto/query/importOnto");
			mv.addObject("ontoType", pd.getString("ontoType"));
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	
	/**本体excel导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/uploadOntology")
	public ModelAndView uploadOntology(@RequestParam(value="filename",required=false) MultipartFile file,String ontoType,String IS_CHECK) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		Workbook xwb = null;
		InputStream inputStream = null;
		String erroInfo=null;
		User user = getCurrentUser();//获取当前登录用户
		try {
			inputStream = file.getInputStream();
//			xwb = new 	HSSFWorkbook(inputStream);
			xwb = WorkbookFactory.create(inputStream);
			pd.put("ontoType",ontoType);
			pd.put("isCheck",IS_CHECK);
			pd.put("UPDATE_MAN", user.getUSERNAME());
			erroInfo=ontologyService.importOntology(xwb, pd);
			Thread.sleep(1000);
		} catch (Exception e) {
			erroInfo="failed";
			e.printStackTrace();
		}
		inputStream.close();
		mv.addObject("msg",erroInfo);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**下载模版 问题单的实例文件
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downTemplate")
	public void downTemplate(String ontoType,HttpServletResponse response)throws Exception{
		logger.info(Jurisdiction.getUsername()+"下载模板");
		String en = null;
		//本体类型
		if(OsynConst.DRUG.equals(ontoType)){
			en = "DRUG";
		}else if (OsynConst.OP.equals(ontoType)){
			//手术
			en = "OP";
		}else if (OsynConst.DIAG.equals(ontoType)){
			//诊断
			en = "DIAG";
		}else if (OsynConst.DEP.equals(ontoType)){
			//科室
			en = "DEP";
		}else{
			throw new Exception("parama ontoType is error.");
		}
		FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.EXCEL + "ONTOLOGY_"+en+".xls", en+"_TEMPLATE.xls");
	}
	
}