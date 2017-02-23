package com.ts.service.ontology.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ts.dao.DaoSupportAi;
import com.ts.entity.Page;
import com.ts.service.ontology.manager.CommonManager;
import com.ts.service.ontology.manager.OntologyManager;
import com.ts.service.ontology.manager.OsynHisManager;
import com.ts.service.ontology.manager.OsynManager;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.UuidUtil;
import com.ts.util.ontology.CodeUtil;
import com.ts.util.ontology.HelpUtil;
import com.ts.util.ontology.OsynConst;

/** 
 * 类名称：本体处理
 * 创建人：xingsilong
 * 修改时间：2015年10月27日
 * @version v2
 */
@Service("ontologyService")
@SuppressWarnings("unchecked")
public class OntologyService implements OntologyManager{

	@Resource(name="commonService")
	private CommonManager commonService;
	
	@Resource(name="osynHisService")
	private OsynHisManager osynHisService;
	
	@Resource(name="osynService")
	private OsynManager osynService;
	
	@Resource(name = "daoSupportAi")
	private DaoSupportAi dao;

	//分页查询本体
	public List<PageData> ontologyPage(Page page,String sqlName) throws Exception{
		return (List<PageData>)dao.findForList("OntologyMapper."+sqlName+"OntolistPage", page);
	}

	/**
	 * 保存诊断副本，和上级树
	 */
	public String saveOntoCopy(HttpServletRequest request ,PageData newOntology,int type)  throws Exception{
		String msg = "success";
		String sqlName = null;
		//本体类型
		String ontoType = newOntology.getString("ontoType");
		//字段转换
		if(OsynConst.DRUG.equals(ontoType)){
			//药品
			//转换成历史表使用的字段名
		}else if (OsynConst.OP.equals(ontoType)){
			sqlName = "op";
		}else if (OsynConst.DIAG.equals(ontoType)){
			sqlName = "diag";
		}else if (OsynConst.DEP.equals(ontoType)){
			sqlName = "dep";
		}
		//标准词名称校验
		newOntology.put("DN_CHN", newOntology.get("STAD_DN_CHN"));
		if(osynService.checkExistName(sqlName,newOntology)){
			return "已经存在的标准词名称："+newOntology.getString("STAD_DN_CHN");
		}
		//同义词名称校验
		String[] DN_CHN = request.getParameterValues("DN_CHN");
		if(DN_CHN!=null && DN_CHN.length>0){
			for(int i=0;i<DN_CHN.length;i++){
				if(Tools.isEmpty(DN_CHN[i])){
					continue;
				}
				PageData osyn = new PageData();
				osyn.put("DN_CHN", DN_CHN[i]);
				if(osynService.checkExistName(sqlName,osyn)){
					return "已经存在的同义词名称："+osyn.getString("DN_CHN");
				}
			}
		}
		//父节点校验
		String str = checkParent(request,ontoType,newOntology);
		if(str!=null){
			return str;
		}
		
		//第一步：保存本体副本
		//设置newOntology数据
		if(type == 0){
			//新增情况下设置数据
			//生产一个本体ID，审核通过后即作为本体真正ID
			int D_ID = commonService.getSequenceID(ontoType,0);
			newOntology.put("D_ID", D_ID);
			//标准名称ID
			Integer STAD_DN_ID = commonService.getSequenceID(ontoType,1);
			newOntology.put("VERSION", "1.0.0");
			newOntology.put("DN_ID", STAD_DN_ID);
			newOntology.put("UPD_DESC","新增本体");
		}else{
			//查询当前使用中的本体数据
			PageData oldOnto = this.selectOntologyById(sqlName, newOntology.getString("D_ID"));
			String[] pids = request.getParameterValues("PARENT_IDS");
			if(pids!=null && OsynConst.DEP.equals(ontoType)){
				//如果科室为启用，旧数据为停用，则校验父节点是否停用
				if("1".equals(oldOnto.get("IS_DISABLE").toString()) && "0".equals(newOntology.get("IS_DISABLE").toString())){
					for(String pid:pids){
						PageData parent = queryOntologyById(sqlName,pid);
						if(parent!=null && "1".equals(parent.get("IS_DISABLE").toString())){
							return "当前父节点已经停用，所以该本体不能启用。";
						}
					}
				}
			}
			//对比数据，设置变更描述
			newOntology.put("PARENT_IDS", pids);
			//设置父节点名称，为下面对比使用
			setParentName(request,newOntology);
			String changeDesc = HelpUtil.setUpdateDesc(ontoType,oldOnto,newOntology);
			if(changeDesc == null){
				return "您没有修改任何信息！";
			}
		}
		//历史ID
		newOntology.put("H_ID", UuidUtil.get32UUID());
		//操作类型 0 新曾1修改
		newOntology.put("OP_TYPE", type);
		//待审核
		newOntology.put("STATUS", 0);
		//本体类型
		newOntology.put("ONTO_TYPE", ontoType);
		newOntology.put("OPERATION","");
		dao.save("OntologyMapper.saveOntoCopy", newOntology);
		
		//第二步：保存树副本
		String[] pids = request.getParameterValues("PARENT_IDS");
		//为空则视为修改为顶级目录
		if(pids == null || pids.length==0){
			int TREE_ID = commonService.getSequenceID(ontoType,2);
			PageData pageData = new PageData();
			pageData.putAll(newOntology);
			pageData.put("H_ID", UuidUtil.get32UUID());
			pageData.put("TREE_ID", TREE_ID);
			pageData.put("PARENT_ID", "");
			pageData.put("ONTO_H_ID", newOntology.get("H_ID"));//树历史中放入本体历史ID
			pageData.put("UPD_DESC", type==0?"新增父节点":"修改父节点");
			dao.save ("TreeMapper.saveOntoTreeHis", pageData);
		}else{
			for(String pid:pids){
				int TREE_ID = commonService.getSequenceID(ontoType,2);
				PageData pageData = new PageData();
				pageData.putAll(newOntology);
				pageData.put("H_ID", UuidUtil.get32UUID());
				pageData.put("TREE_ID", TREE_ID);
				pageData.put("PARENT_ID", pid);
				pageData.put("UPD_DESC", type==0?"新增父节点":"修改父节点");
				pageData.put("ONTO_H_ID", newOntology.get("H_ID"));//树历史中放入本体历史ID
				dao.save ("TreeMapper.saveOntoTreeHis", pageData);
			}
		}
		
		//第三步：保存同义词 副本
		if(DN_CHN!=null && DN_CHN.length>0){
			String[] DN_ENG = request.getParameterValues("DN_ENG");
			String[] SYNO_TYPE = request.getParameterValues("SYNO_TYPE");
			String[] ORG_DN_CHN = request.getParameterValues("ORG_DN_CHN");
			String[] ORG_DN_ENG = request.getParameterValues("ORG_DN_ENG");
			String[] IS_DISABLE = request.getParameterValues("IS_DISABLE");
			String[] DESCRIPTION = request.getParameterValues("DESCRIPTION");
			for(int i=0;i<DN_CHN.length;i++){
				if(Tools.isEmpty(DN_CHN[i])){
					continue;
				}
				PageData osyn = new PageData();
				osyn.put("H_ID",  UuidUtil.get32UUID());
				//获取一个同义词序列
				int DN_ID = commonService.getSequenceID(ontoType,1);
				osyn.put("DN_ID", DN_ID);
				//标准名称ID
				osyn.put("STAD_DN_ID", newOntology.get("DN_ID"));
				osyn.put("UPD_DESC","新增本体级联新增同义词");
				osyn.put("OP_TYPE", 2);//及联新增标识
				osyn.put("DN_CHN", DN_CHN[i]);
				if(DN_ENG!=null){
					osyn.put("DN_ENG", DN_ENG[i]);
				}
				if(ORG_DN_CHN!=null){
					osyn.put("ORG_DN_CHN", ORG_DN_CHN[i]);
				}
				if(ORG_DN_ENG!=null){
					osyn.put("ORG_DN_ENG", ORG_DN_ENG[i]);
				}
				osyn.put("SYNO_TYPE", SYNO_TYPE[i]);
				osyn.put("IS_DISABLE", IS_DISABLE[i]);
				osyn.put("DESCRIPTION", DESCRIPTION[i]);
				osyn.put("OP_TYPE", 7);//级联新增，表示新增本体时增加的同义词
				osyn.put("STATUS", 0);
				osyn.put("ONTO_TYPE", ontoType);
				osyn.put("OPERATION","");
				osyn.put("VERSION", "1.0.0");
				osyn.put("UPDATE_MAN", newOntology.getString("UPDATE_MAN"));
				osyn.put("UPDATE_TIME", newOntology.get("UPDATE_TIME"));
				osyn.put("ONTO_H_ID", newOntology.get("H_ID"));//树历史中放入本体历史ID
				dao.save("AiAlterNameHistMapper.saveOsynHis", osyn);
			}
		}
		
		return msg;
	}
	
	private String checkParent(HttpServletRequest request, String ontoType,PageData pd) {
		String str=null;
		String[] p = request.getParameterValues("PARENT_IDS");
		if(OsynConst.DRUG.equals(ontoType)){

		}else if (OsynConst.OP.equals(ontoType)){
			if(p!=null && p.length!=1){
				str = "手术父节点只能有1个";
			}
		}else if (OsynConst.DIAG.equals(ontoType)){
			if(p!=null && p.length>2){
				str = "诊断父节点最多只能有2个";
			}
		}else if(OsynConst.DEP.equals(ontoType)){
			if(p!=null && p.length>1){
				str = "科室父节点最多只能有1个";
			}else if(p!=null && p.length==1){
				//验证是否是自己
				if(pd.getString("D_ID").equals(p[0])){
					str = "父节点不能选择自己！";
				}
			}
		}
		return str;
	}

	//设置父节点名称
	private void setParentName(HttpServletRequest request, PageData newOntology) {
		String[] parent = request.getParameterValues("PARENT_NAMES");
		StringBuffer sb = new StringBuffer();
		if(parent!=null){
			for(String name:parent){
				sb.append(name).append(",");
			}
			newOntology.put("PARENT_NAMES", sb.substring(0, sb.length()-1));
		}
	}

	/**
	 * 查询本体信息，不包含父节点
	 * @param sqlName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PageData queryOntologyById(String sqlName, String id) throws Exception {
		return (PageData)dao.findForObject("OntologyMapper."+sqlName+"ById", id);
	}
	/**
	 *  根据ID 查询本体,及本体上级
	 * @param sqlName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PageData selectOntologyById(String sqlName, String id) throws Exception {
		PageData pd  = queryOntologyById(sqlName, id);
		if(pd == null){
			return null;
		}
		String PARENT_ID = null;
		String PARENT_NAME = null;
		List<String> PARENT_IDS = new ArrayList<String>();
		//获取上级信息
		List<PageData> list = this.queryParentOntoById(sqlName,id);
		if(!CollectionUtils.isEmpty(list)){
			for(PageData p:list){
				Object pid = p.get("ID");
				if(pid!=null){
					if(Tools.isEmpty(PARENT_ID)){
						PARENT_ID = ((BigDecimal)pid).intValue()+"";
					}else{
						PARENT_ID += ";" + ((BigDecimal)pid).intValue();
					}
					PARENT_IDS.add(((BigDecimal)pid).toString());
				}
				//父节点名称
				String name = p.getString("CN");
				if(Tools.isEmpty(PARENT_NAME)){
					PARENT_NAME = name;
				}else{
					PARENT_NAME += ";" + name;
				}
				//主要编码
				String MAIN_CODE = p.getString("MAIN_CODE");
				if(!Tools.isEmpty(MAIN_CODE)){
					PARENT_NAME += "[" + MAIN_CODE +"]";
				}
			}
			pd.put("PARENT_LIST", list);
			pd.put("PARENT_ID", PARENT_ID);
			pd.put("PARENT_NAME", PARENT_NAME);
			Collections.sort(PARENT_IDS);
			pd.put("PARENT_IDS", PARENT_IDS);
		}
		//查询科室分类名称
		Object DEP_CATEGORY_ID = pd.get("DEP_CATEGORY");
		if(DEP_CATEGORY_ID!=null){
			PageData dep  = (PageData)dao.findForObject("OntologyMapper.depById", DEP_CATEGORY_ID);
			if(dep!=null){
				pd.put("DEP_CATEGORY_NAME", dep.get("DEP_STAD_NAME"));
				pd.put("DEP_CATEGORY_CODE", dep.get("DEP_NAME_CODE"));
			}
		}
		return pd;
	}
	
	/**
	 * 获取树上级信息
	 * @param sqlName
	 * @param string
	 * @return
	 * @throws Exception 
	 */
	public List<PageData> queryParentOntoById(String sqlName, String string) throws Exception {
		return (List<PageData>)dao.findForList("TreeMapper."+sqlName+"ParentOntoInfo", string);
	}
	
	/**
	 * 获取历史树上级信息
	 * @param sqlName
	 * @param string
	 * @return
	 * @throws Exception 
	 */
	public List<PageData> queryParentOntoHisInfo(String sqlName, PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("TreeMapper."+sqlName+"ParentOntoHisInfo", pd);
	}

	/**
	 * 审核列表
	 * @param page
	 * @param sqlName
	 * @return
	 * @throws Exception
	 */
	public List<PageData> ontoCheckPage(Page page, String sqlName) throws Exception{
		return (List<PageData>)dao.findForList("OntologyMapper.checklistPage", page);
	}
	/**
	 * 查询单个正在审核中的历史信息，根据本体ID
	 * @param sqlName
	 * @param string
	 * @return
	 * @throws Exception 
	 */
	public List<PageData> selectOntologyHisByOntoId(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("OntologyMapper.queryOntoHisByOntoId", pd);
	}
	/**
	 * 查询单个历史信息，及上级信息，根据历史ID
	 * @param sqlName
	 * @param string
	 * @return
	 * @throws Exception 
	 */
	public PageData selectOntologyHisById(String sqlName, String id) throws Exception {
		PageData pd  = (PageData)dao.findForObject("OntologyMapper.queryOntoHisById", id);
		//获取上级信息
		pd.put("ONTOTYPE", pd.get("ONTO_TYPE"));
		List<PageData> parent = this.queryParentOntoHisInfo(sqlName,pd);
		List<String> PARENT_IDS = new ArrayList<String>();
		String PARENT_ID = null;
		String PARENT_NAME = null;
		//为空则为错误数据
		if(!CollectionUtils.isEmpty(parent)){
			for(PageData p:parent){
				Object pid = p.get("ID");
				if(pid!=null){
					PARENT_IDS.add(((BigDecimal)pid).toString());
					if(Tools.isEmpty(PARENT_ID)){
						PARENT_ID = ((BigDecimal)pid).toString();
					}else{
						PARENT_ID += ";" + ((BigDecimal)pid).intValue();
					}
				}
				String name = p.getString("CN");
				if(Tools.isEmpty(PARENT_NAME)){
					PARENT_NAME = name;
				}else{
					PARENT_NAME += ";" + name;
				}
				//主要编码
				String MAIN_CODE = p.getString("MAIN_CODE");
				if(!Tools.isEmpty(MAIN_CODE)){
					PARENT_NAME += "[" + MAIN_CODE +"]";
				}
			}
		}
		pd.put("PARENT_ID", PARENT_ID);
		pd.put("PARENT_NAME", PARENT_NAME);
		Collections.sort(PARENT_IDS);
		pd.put("PARENT_IDS", PARENT_IDS);
		//查询科室分类名称
		Object DEP_CATEGORY_ID = pd.get("DEP_CATEGORY");
		if(DEP_CATEGORY_ID!=null){
			PageData osynParam = new PageData();
			osynParam.put("ON_NAME_ID", DEP_CATEGORY_ID);
			PageData dep  = (PageData)dao.findForObject("OntologyMapper.depById", DEP_CATEGORY_ID);
			if(dep!=null){
				pd.put("DEP_CATEGORY_NAME", dep.get("DEP_STAD_NAME"));
				pd.put("DEP_CATEGORY_CODE", dep.get("DEP_NAME_CODE"));
			}
		}

		return pd;
	}
	
	/**
	 * 审核拒绝
	 * @param pd
	 */
	public String updateCheckRefuse(PageData pd) throws Exception{
		String msg = "success";
		//更新本体副本历史状态
		dao.update("OntologyMapper.ontoCheckStatus", pd);
		//级联更新树历史
		dao.update("TreeMapper.ontoTreeCheckStatus", pd);
		//级联更新同义词历史（可能不存在）
		pd.put("ONTO_H_ID", pd.getString("H_ID"));
		pd.remove("H_ID");//此条件在本次事件中删掉
		pd.put("CHECK_MEMO", pd.get("CHECK_MEMO")==null?"":pd.get("CHECK_MEMO"));
		dao.update("AiAlterNameHistMapper.upAlterName", pd);
		return msg;
	}
	
	/**
	 * 审核通过
	 * @param pd
	 */
	public String updateCheckPass(HttpServletRequest request,PageData pd) throws Exception{
		String msg = "success";
		//本体类型
		String ontoType = pd.getString("ontoType");
		//sql名称
		String sqlName = "";
		if(OsynConst.DRUG.equals(ontoType)){
			//药品
		}else if (OsynConst.OP.equals(ontoType) ){
			//手术
			sqlName = "op";
		}else if (OsynConst.DIAG.equals(ontoType) ){
			//诊断
			sqlName = "diag";
		}else if (OsynConst.DEP.equals(ontoType) ){
			//科室
			sqlName = "dep";
		}
		
		/**************  数据验证  ************/
		String H_ID =  pd.getString("H_ID");
		//查询本体历史信息
		PageData new_onto = (PageData)this.selectOntologyHisById(sqlName, H_ID);
		if(((BigDecimal)new_onto.get("STATUS")).intValue()!=0){
			//状态不为待审核则跑出异常
			return "该信息已经被其他人审核完毕。";
		}

		/************** 处理审核时专家改的的数据 ****************/
		//后台sql判断使用
		Integer checkModifyFlag = new Integer(0);
		if(request!=null){
			checkModifyFlag += setRequest2Pd(request,new_onto,"STAD_DN_CHN");
			checkModifyFlag += setRequest2Pd(request,new_onto,"STAD_DN_ENG");
			checkModifyFlag += setRequest2Pd(request,new_onto,"ADD_CODE");
			checkModifyFlag += setRequest2Pd(request,new_onto,"ORG_STAD_DN_CHN");
			checkModifyFlag += setRequest2Pd(request,new_onto,"ORG_STAD_DN_ENG");
			checkModifyFlag += setRequest2Pd(request,new_onto,"TERM_TYPE");
			checkModifyFlag += setRequest2Pd(request,new_onto,"TERM_DEFIN");
			checkModifyFlag += setRequest2Pd(request,new_onto,"DEP_CATEGORY");
			checkModifyFlag += setRequest2Pd(request,new_onto,"PART_CATEGORY");
			checkModifyFlag += setRequest2Pd(request,new_onto,"MAN_CATEGORY");
			checkModifyFlag += setRequest2Pd(request,new_onto,"DIS_CATEGORY");
			checkModifyFlag += setRequest2Pd(request,new_onto,"IS_CHRONIC");
			checkModifyFlag += setRequest2Pd(request,new_onto,"IS_DISABLE");
			checkModifyFlag += setRequest2Pd(request,new_onto,"DESCRIPTION");
			pd.put("checkModifyFlag", checkModifyFlag);
			//父节点在后面处理
		}
		int opType = ((BigDecimal)new_onto.get("OP_TYPE")).intValue();
		if(opType<2){
			//验证标准名称是否已经存在
			new_onto.put("DN_CHN", new_onto.get("STAD_DN_CHN"));
			if(osynService.checkExistName(sqlName, new_onto)){
				return "已经存在的标准词名称："+new_onto.getString("STAD_DN_CHN");
			}
		}
		//验证同义词是否存在
		List<PageData> osynList = null;
		if(opType <= 1 ){
			PageData param = new PageData();
			param.put("ONTO_H_ID", H_ID);
			param.put("ontoType", ontoType);
			param.put("STATUS", 0);//待审核
			//查询出级联新增的同义词历史（可能不存在）
			osynList = osynHisService.queryOsynHis(param);
			if(!CollectionUtils.isEmpty(osynList)){
				//新增同义词
				for(PageData p:osynList){
					if(osynService.checkExistName(sqlName, p)){
						return "已经存在的同义词名称："+p.getString("DN_CHN");
					}
				}
			}
		}
		
		//本体ID即节点ID
		String onto_id = new_onto.get("D_ID").toString();
		//专家修改的父节点集合
		String[] PARENT_IDS = request==null?null:request.getParameterValues("PARENT_IDS");
		//编码员提交的父节点集合
		List<PageData> pd_tree_his = (List<PageData>)dao.findForList("TreeMapper.treeHisByOntoId", H_ID);
		//旧本体数据
		PageData old_onto = this.selectOntologyById(sqlName, onto_id);
		
		//如果科室新数据为启用，旧数据为停用，则校验父节点是否停用
		if(OsynConst.DEP.equals(ontoType)){
			if(old_onto!=null && "1".equals(old_onto.get("IS_DISABLE").toString()) && "0".equals(new_onto.get("IS_DISABLE").toString())){
				if(PARENT_IDS!=null && PARENT_IDS.length>0){
					for(String pid:PARENT_IDS){
						PageData parent = queryOntologyById(sqlName,pid);
						if(parent!=null && "1".equals(parent.get("IS_DISABLE").toString())){
							return "当前父节点已经停用，所以该本体不能启用。";
						}
					}
				}else{
					if(pd_tree_his!=null && pd_tree_his.size()>0){
						for(PageData parent:pd_tree_his){
							PageData p = queryOntologyById(sqlName,parent.get("PARENT_ID").toString());
							if(p!=null && "1".equals(p.get("IS_DISABLE").toString())){
								return "当前父节点已经停用，所以该本体不能启用。";
							}
						}
					}
				}
			}
		}

		
		/************** 设置主要编码与附加编码  **************/
		//父节点被修改标志
		boolean parentModifyFlag = false;
		//当操作类型为0新增本体、1修改本体 、2修改父节点这三类时需要对编码进行处理
		if(opType <= 2 ){
			//判断审核页面专家是否修改过父节点信息
			if(PARENT_IDS!=null){
				//审核时，父节点被专家临时修改过
				//比较新旧父节点，设置主要编码和附加编码
				if(opType==0){
					//新增时，直接生成编码
					commonService.setMainAddCode(ontoType, new_onto, PARENT_IDS);
					parentModifyFlag = true;
				}else{
					//修改时，对比父节点是否变化
					if(!HelpUtil.checkModifyParent(PARENT_IDS, (List<String>)old_onto.get("PARENT_IDS"))){
						//父节点有变化重新获取编码
						commonService.setMainAddCode(ontoType, new_onto, PARENT_IDS);
						parentModifyFlag = true;
					}else{
						//父节点没有变化
						if(OsynConst.DRUG.equals(ontoType)){
						}else if (OsynConst.OP.equals(ontoType) ){
							new_onto.put("MAIN_CODE", old_onto.get("OP_CODE"));
						}else if (OsynConst.DIAG.equals(ontoType) ){
							new_onto.put("MAIN_CODE", old_onto.get("MAIN_CODE"));
							new_onto.put("ADD_CODE", old_onto.get("ADD_CODE"));
						}else if (OsynConst.DEP.equals(ontoType) ){
							new_onto.put("MAIN_CODE", old_onto.get("DEP_NAME_CODE"));
						}
					}
				}
			}else{
				//审核时，父节未被专家临时修改过，按编码员提交的数据对比
				//比较新旧父节点，设置主要编码和附加编码
				if(opType==0){
					//新增时，直接生成编码
					commonService.setMainAddCode(ontoType, new_onto, HelpUtil.getParentIds(pd_tree_his));
					parentModifyFlag = true;
				}else{
					//修改时，对比父节点是否变化
					if(!HelpUtil.checkModifyParent2(pd_tree_his, (List<String>)old_onto.get("PARENT_IDS"))){
						//父节点有变化重新获取编码
						commonService.setMainAddCode(ontoType, new_onto, (List<String>)new_onto.get("PARENT_IDS"));
						parentModifyFlag = true;
					}else{
						//父节点没有变化
						if(OsynConst.DRUG.equals(ontoType)){
						}else if (OsynConst.OP.equals(ontoType) ){
							new_onto.put("MAIN_CODE", old_onto.get("OP_CODE"));
						}else if (OsynConst.DIAG.equals(ontoType) ){
							new_onto.put("MAIN_CODE", old_onto.get("MAIN_CODE"));
							new_onto.put("ADD_CODE", old_onto.get("ADD_CODE"));
						}else if (OsynConst.DEP.equals(ontoType) ){
							new_onto.put("MAIN_CODE", old_onto.get("DEP_NAME_CODE"));
						}
					}
				}
			}
		}
		
		/************** 第一步：本体处理,标准词处理  **************/
		new_onto.put("VERSION", new_onto.get("VERSION")==null?"1.0.0":new_onto.get("VERSION"));
		if(opType==0){
			//操作类型为新增
			//根据本体历史新增本体名称标准词
			PageData stand = new PageData();
			stand.putAll(new_onto);
			stand.put("DN_ID", new_onto.get("DN_ID"));
			stand.put("DN_CHN", new_onto.get("STAD_DN_CHN"));
			stand.put("DN_ENG", new_onto.get("STAD_DN_ENG"));
			stand.put("STAD_DN_ID", new_onto.get("DN_ID"));
			stand.put("TERM_TYPE", new_onto.get("TERM_TYPE"));
			stand.put("SYNO_TYPE", "23107"); //设置为其它
			stand.put("ORG_DN_CHN", new_onto.get("ORG_STAD_DN_CHN"));
			stand.put("ORG_DN_ENG", new_onto.get("ORG_STAD_DN_ENG"));
			stand.put("OPERATION", "");//字段无意义
			dao.save("OsynMapper."+sqlName+"InsertOsynName",stand);
			//根据本体历史新增本体
			dao.save("OntologyMapper."+sqlName+"InsertOntoByHis", new_onto);
		}else if(opType == 1){
			//操作类型为修改
			//如果改变了标准名称中文,则原数据变为同义词
			StringBuffer changeMsg = new StringBuffer();
			if (OsynConst.DIAG.equals(ontoType) ){
				HelpUtil.setCompareDesc(changeMsg, old_onto, "STAD_DN_CHN", new_onto, "STAD_DN_CHN", "",null);
			}else if (OsynConst.OP.equals(ontoType) ){
				HelpUtil.setCompareDesc(changeMsg, old_onto, "STAD_OP_CHN", new_onto, "STAD_DN_CHN", "",null);
			}else if (OsynConst.DEP.equals(ontoType) ){
				HelpUtil.setCompareDesc(changeMsg, old_onto, "DEP_STAD_NAME", new_onto, "STAD_DN_CHN", "",null);
			}
			//根据本体历史更新本体
			dao.update("OntologyMapper."+sqlName+"UpdateOntoByHis", new_onto);
			//根据本体历史，更新标准词
			PageData standOsyn = new PageData();
			standOsyn.put("DN_ID", new_onto.get("DN_ID"));
			standOsyn.put("DN_CHN", new_onto.get("STAD_DN_CHN"));
			standOsyn.put("DN_ENG", new_onto.get("STAD_DN_ENG"));
			standOsyn.put("STAD_DN_ID", new_onto.get("DN_ID"));
			standOsyn.put("ORG_DN_CHN", new_onto.get("ORG_STAD_DN_CHN"));
			standOsyn.put("ORG_DN_ENG", new_onto.get("ORG_STAD_DN_ENG"));
			standOsyn.put("TERM_TYPE", new_onto.get("TERM_TYPE"));
			standOsyn.put("UPDATE_MAN", new_onto.get("UPDATE_MAN"));
			standOsyn.put("UPDATE_TIME", new_onto.get("UPDATE_TIME"));
			standOsyn.put("IS_DISABLE", new_onto.get("IS_DISABLE"));
			dao.update("OsynMapper."+ sqlName +"UpdateOsynName",standOsyn);
			if(changeMsg.length()>0){
				//原数据变更为同义词
				PageData newOsyn = new PageData();
				newOsyn.putAll(old_onto);
				newOsyn.put("DN_ID", commonService.getSequenceID(ontoType, 1));
				newOsyn.put("DN_CHN", old_onto.get("STAD_DN_CHN"));
				newOsyn.put("DN_ENG", old_onto.get("STAD_DN_ENG"));
				newOsyn.put("STAD_DN_ID", old_onto.get("DN_ID"));
				newOsyn.put("TERM_TYPE", old_onto.get("TERM_TYPE"));
				newOsyn.put("SYNO_TYPE", old_onto.get("23103"));//同音字/错别字
				newOsyn.put("ORG_DN_CHN", old_onto.get("ORG_STAD_DN_CHN"));
				newOsyn.put("ORG_DN_ENG", old_onto.get("ORG_STAD_DN_ENG"));
				newOsyn.put("IS_DISABLE", new_onto.get("IS_DISABLE"));
				newOsyn.put("DESCRIPTION", new_onto.get("DESCRIPTION"));
				newOsyn.put("VERSION", new_onto.get("VERSION"));
				newOsyn.put("UPDATE_MAN", new_onto.get("UPDATE_MAN"));
				newOsyn.put("UPDATE_TIME", new_onto.get("UPDATE_TIME"));
				newOsyn.put("OPERATION", "");
				if (OsynConst.OP.equals(ontoType) ){
					newOsyn.put("DN_CHN", old_onto.get("STAD_OP_CHN"));
					newOsyn.put("DN_ENG", old_onto.get("STAD_OP_ENG"));
					newOsyn.put("ORG_DN_CHN", old_onto.get("ORG_STAD_OP_CHN"));
					newOsyn.put("ORG_DN_ENG", old_onto.get("ORG_STAD_OP_ENG"));
					newOsyn.put("STAD_DN_ID", old_onto.get("ON_ID"));
				}else if (OsynConst.DEP.equals(ontoType) ){
					newOsyn.put("DN_CHN", old_onto.get("DEP_STAD_NAME"));
					newOsyn.put("STAD_DN_ID", old_onto.get("DN_ID"));
				}
				dao.save("OsynMapper."+sqlName+"InsertOsynName",newOsyn);
				//增加一条同义词历史
				newOsyn.put("CHECK_USER", pd.get("UPDATE_MAN"));
				newOsyn.put("CHECK_TIME", pd.get("UPDATE_TIME"));
				newOsyn.put("CHECK_MEMO", "标准词名称更改，原本标准词变更为同义词");
				newOsyn.put("STATUS", 1);//审核通过
				newOsyn.put("OP_TYPE", 8);//操作类型：被动新增。表示原标准词名称更改被变成同义词
				newOsyn.put("UPD_DESC", "标准词名称更改，原本标准词变更为同义词");
				newOsyn.put("H_ID", UuidUtil.get32UUID());
				newOsyn.put("ONTO_TYPE", ontoType);
				dao.save("AiAlterNameHistMapper.saveOsynHis", newOsyn);
			}
			
			//如果本体变更为 停用，则所有同义词标准词都更为停用
			if("0".equals(old_onto.get("IS_DISABLE").toString()) && "1".equals(new_onto.get("IS_DISABLE").toString())){
				if(OsynConst.DEP.equals(ontoType)){
					//科室停用需要级联所有下级进行操作
					ontoTriggerStop(sqlName,new_onto);
				}else{
					standOsyn.put("DESCRIPTION", "本体停用，所有同义词级联停用");
					dao.update("OsynMapper."+ sqlName +"StopOsynName",standOsyn);
				}
			}
		}else if(opType == 2){
			//操作类型为修改父节点，本体表不做变更
			dao.update("OntologyMapper."+sqlName+"UpdateOntoByHis", new_onto);
		}else if(opType == 3){
			//操作类型为术语知识编辑
			//更新相应数据,根据本体历史更新本体
			dao.update("OntologyMapper."+sqlName+"UpdateOntoByHis", new_onto);
		}else if(opType == 4){
			//操作类型为停用术语
			//判断是否停用
			PageData standOsyn = new PageData();
			standOsyn.put("D_ID", new_onto.get("D_ID"));
			standOsyn.put("IS_DISABLE", new_onto.get("IS_DISABLE"));
			standOsyn.put("DESCRIPTION", new_onto.getString("DESCRIPTION"));
			standOsyn.put("UPDATE_MAN", new_onto.get("UPDATE_MAN"));
			standOsyn.put("UPDATE_TIME", new_onto.get("UPDATE_TIME"));
			dao.update("OntologyMapper."+ sqlName +"StopOntology",standOsyn);
			//如果本体变更为 停用，则所有同义词标准词都更为停用
			standOsyn.put("STAD_DN_ID", new_onto.get("DN_ID"));
			if("0".equals(new_onto.get("IS_DISABLE").toString())){
				standOsyn.put("standFlag", 1);
				//仅更新标准词停用标志
				dao.update("OsynMapper."+ sqlName +"StopOsynName",standOsyn);
			}else if("0".equals(old_onto.get("IS_DISABLE").toString()) && "1".equals(new_onto.get("IS_DISABLE").toString())){
				if(OsynConst.DEP.equals(ontoType)){
					//科室停用需要级联所有下级进行操作 
					ontoTriggerStop(sqlName,new_onto);
				}else{
					standOsyn.put("DESCRIPTION", "本体停用，所有同义词级联停用");
					dao.update("OsynMapper."+ sqlName +"StopOsynName",standOsyn);
				}
			}
		}
		//更新本体历史状态
		pd.put("MAIN_CODE", new_onto.get("MAIN_CODE"));
		pd.put("ADD_CODE", new_onto.get("ADD_CODE"));
		dao.update("OntologyMapper.ontoCheckStatus", pd);
		
		
		/************** 第二步：本体树处理  **************/
		//操作优化，当操作类型为0新增本体、1修改本体 、2修改父节点这三类时需要对树进行操作
		if(opType <= 2 ){
			if(parentModifyFlag){
				//删除旧的树结构，根据节点id（非树id）
				dao.delete("TreeMapper."+sqlName+"DeleteTreeByOntoId", onto_id);
				//审核页面专家修改的父节点信息
				if(PARENT_IDS!=null){
					//审核时，父节点被专家临时修改过，则安新数据执行更新
					//1、删除历史树
					PageData treeParam = new PageData();
					treeParam.put("D_ID", onto_id);
					treeParam.put("ONTO_H_ID", H_ID);
					dao.delete("TreeMapper.deleteTreeHisByOntoId", onto_id);
					for(String parent:PARENT_IDS ){
						//2、根据本体树历史新增本体树
						PageData tree = new PageData();
						int TREE_ID = commonService.getSequenceID(ontoType,2);
						tree.put("TREE_ID", TREE_ID );
						tree.put("D_ID", onto_id);
						tree.put("PARENT_ID", parent);
						dao.save("TreeMapper."+sqlName+"TreeInsert", tree);
						tree.putAll(pd);
						//3、新增历史树，（审核通过的）
						tree.put("H_ID", UuidUtil.get32UUID());
						tree.put("UPD_DESC", "审核时，临时修改父节点");
						//树历史中放入本体历史ID
						tree.put("ONTO_H_ID", H_ID);
						//pd里面含有更新人状态等信息直接放入到tree
						tree.put("UPDATE_MAN", new_onto.get("UPDATE_MAN"));
						tree.put("UPDATE_TIME", new_onto.get("UPDATE_TIME"));
						tree.put("UPDATE_DESC", "");
						tree.put("CHECK_MEMO", "审核时，临修改父节点");
						tree.put("STATUS", 1);//审核通过
						tree.put("ONTO_TYPE", ontoType);
						tree.put("OP_TYPE", new_onto.get("OP_TYPE"));
						dao.save ("TreeMapper.saveOntoTreeHis", tree);
					}
				}else{
					//审核时，父节未被专家临时修改过，按原有数据更新
					if(pd_tree_his!=null && pd_tree_his.size()>0){
						for(PageData tree:pd_tree_his){
							//根据本体树历史新增本体树
							dao.save("TreeMapper."+sqlName+"TreeInsert", tree);
						}
						//级联更新本体树历史状态
						dao.update("TreeMapper.ontoTreeCheckStatus", pd);
					}
				}
			}else{
				//级联更新本体树历史状态成功
				dao.update("TreeMapper.ontoTreeCheckStatus", pd);
			}

		}
		
		/************** 第三步：同义词处理  **************/
		//操作优化，当操作类型为0新增本体、1修改本体 这二类时需要对同义词进行操作
		if(opType <= 1 ){
			//查询出级联新增的同义词历史（可能不存在）
			if(!CollectionUtils.isEmpty(osynList)){
				//新增同义词
				for(PageData p:osynList){
					p.put("IS_DISABLE", new_onto.get("IS_DISABLE"));
					dao.save("OsynMapper."+sqlName+"InsertOsynName",p);
				}
				PageData param = new PageData();
				param.put("ONTO_H_ID", H_ID);
				param.put("CHECK_USER", pd.get("CHECK_USER"));
				param.put("CHECK_TIME", pd.get("CHECK_TIME"));
				param.put("CHECK_MEMO", pd.get("CHECK_MEMO")==null?"":pd.get("CHECK_MEMO"));
				param.put("STATUS", 1);//审核通过
				param.put("IS_DISABLE", new_onto.get("IS_DISABLE"));
				//更新同义词历史
				dao.update("AiAlterNameHistMapper.upAlterName", param);
			}
		}
		
		/************** 第四步：科室下级本体重新编码处理  **************/
		if (OsynConst.DEP.equals(ontoType) && parentModifyFlag){
			//科室
			//查询出当前节点的孩子节点，然后重新编码
			reCodingChildrens(ontoType,"dep",new_onto);
		}
		return msg;
	}
	
	/**
	 * 更新科室的主要编码（目前只有科室使用本方法）
	 * @param ontoType
	 * @param sqlName
	 * @param new_onto
	 * @throws Exception
	 */
	private void reCodingChildrens(String ontoType, String sqlName, PageData new_onto) throws Exception {
		List<PageData> list = queryChildrensById(sqlName, new_onto.get("D_ID").toString());
		if(list!=null && list.size()>0){
			String p_maincode = new_onto.getString("MAIN_CODE");
//			String p_addcode = new_onto.getString("ADD_CODE");
			String mainLastAgCode = null;
//			String addLastAgCode = null;
			for(PageData pd :list){
				if(!Tools.isEmpty(p_maincode)){
					String code = CodeUtil.makeAgCodeByIcd(ontoType, mainLastAgCode, p_maincode);
					mainLastAgCode = code;
					pd.put("MAIN_CODE", code);
				}
//				if(!Tools.isEmpty(p_addcode)){
//					String code = CodeUtil.makeAgCodeByIcd(ontoType, addLastAgCode, p_maincode);
//					addLastAgCode = code;
//					pd.put("ADD_CODE", code);
//				}
				//更新本体编码
				updateDepOnto(pd);
				reCodingChildrens(ontoType,sqlName, pd);
			}
		}
	}

	/**
	 * 更新科室的本体编码
	 * @param pd
	 * @throws Exception 
	 */
	private void updateDepOnto(PageData pd) throws Exception {
		dao.update("OntologyMapper.depUpdateOntoCode", pd);
	}

	/**
	 * 查询出当前节点的孩子节点
	 * @param string
	 * @param onto_id
	 * @return
	 * @throws Exception 
	 */
	public List<PageData> queryChildrensById(String sqlName, String onto_id) throws Exception {
		return (List<PageData>) dao.findForList("OntologyMapper."+sqlName+"SubsOntoByOntoId", onto_id);
	}

	//级联停用所有本体及下级本体，所有标准词，及同义词,目前只有科室有这种情况
	private void ontoTriggerStop(String sqlName, PageData new_onto) throws Exception {
		PageData p = new PageData();
		p.put("D_ID", new_onto.get("D_ID"));
		p.put("DESCRIPTION", "本体停用所有下位词及同义词都停用");
		p.put("UPDATE_MAN", new_onto.get("UPDATE_MAN"));
		p.put("UPDATE_TIME", new_onto.get("UPDATE_TIME"));
		dao.update("OntologyMapper."+sqlName+"StopSubOntoByOntoId", p);
		dao.update("OsynMapper."+sqlName+"StopAllNameByOntoId", p);
	}

	private int setRequest2Pd(HttpServletRequest request, PageData pd,	String name) {
		String value = request.getParameter(name);
		if(value!=null){
			pd.put(name, value);
			return 1;
		}else{
			return 0;
		}
	}

	/**
	 * 批量审核通过
	 */
	public String updateCheckPassAll(PageData pd) throws Exception{
		String H_IDS = pd.getString("H_IDS");
		if(H_IDS == null){
			throw new Exception("parama H_IDS is null.");
		}
		String[] HIDS= H_IDS.split(";");
		for(String H_ID:HIDS){
			pd.put("H_ID", H_ID);
			String msg = updateCheckPass(null,pd);
			if(!"success".equals(msg)){
				return msg;
			}
		}
		return "success";
	}
	/**
	 * 批量审核拒绝
	 */
	public String updateCheckRefuseAll(PageData pd) throws Exception{
		String H_IDS = pd.getString("H_IDS");
		if(H_IDS == null){
			throw new Exception("parama H_IDS is null.");
		}
		String[] HIDS= H_IDS.split(";");
		for(String H_ID:HIDS){
			pd.put("H_ID", H_ID);
			String msg = updateCheckRefuse(pd);
			if(!"success".equals(msg)){
				return msg;
			}
		}
		return "success";
	}
	

	/**
	 * 查询本体历史
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> queryOntoHisPage(String sqlName,Page page) throws Exception{
		return (List<PageData>)dao.findForList("OntologyMapper."+sqlName+"OntoHislistPage", page);
	}
	
	/**
	 * 查询单个本体的所有历史
	 * @param sqlName
	 * @param pd
	 * @return
	 */
	public List<PageData> queryOntoHisDetail(String sqlName, PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("OntologyMapper."+sqlName+"OntoHisDetail", pd);
	}
	

	/**
	 * 快捷键-停用本体
	 * @param pd
	 * @throws Exception
	 */
	public void stopOntology(PageData pd) throws Exception{
		String ONTO_ID = pd.getString("ONTO_ID");
		String sqlName = null;
		//本体类型
		String ontoType = pd.getString("ontoType");
		//字段转换
		if(OsynConst.DRUG.equals(ontoType)){
			//药品
		}else if (OsynConst.OP.equals(ontoType)){
			sqlName = "op";
		}else if (OsynConst.DIAG.equals(ontoType)){
			//诊断
			sqlName = "diag";
		}else if (OsynConst.DEP.equals(ontoType)){
			//科室
			sqlName = "dep";
		}
		//保存本体副本
		PageData oldOnto = this.selectOntologyById(sqlName, ONTO_ID);
		//转换成历史表使用字段
		if (OsynConst.OP.equals(ontoType)){
			oldOnto.put("D_ID", oldOnto.get("OP_ID"));
			oldOnto.put("DN_ID", oldOnto.get("ON_ID"));
			oldOnto.put("STAD_DN_CHN", oldOnto.get("STAD_OP_CHN"));
			oldOnto.put("STAD_DN_ENG", oldOnto.get("STAD_OP_ENG"));
			oldOnto.put("MAIN_CODE", oldOnto.get("OP_CODE"));
			oldOnto.put("ORG_STAD_DN_CHN", oldOnto.get("ORG_STAD_OP_CHN"));
			oldOnto.put("ORG_STAD_DN_ENG", oldOnto.get("ORG_STAD_OP_ENG"));
		}else if(OsynConst.DEP.equals(ontoType)){
			oldOnto.put("D_ID", oldOnto.get("ID"));
			oldOnto.put("DN_ID", oldOnto.get("DN_ID"));
			oldOnto.put("MAIN_CODE", oldOnto.get("DEP_NAME_CODE"));
			oldOnto.put("STAD_DN_CHN", oldOnto.get("DEP_STAD_NAME"));
			oldOnto.put("TERM_DEFIN", oldOnto.get("DEFINITION"));
			oldOnto.put("STAD_DN_ENG", "");
			oldOnto.put("ORG_STAD_DN_CHN", "");
			oldOnto.put("ORG_STAD_DN_ENG", "");
		}
		String H_ID = UuidUtil.get32UUID();
		oldOnto.putAll(pd);
		oldOnto.put("H_ID", H_ID);//历史ID
		oldOnto.put("OP_TYPE", 4);// 操作类型 0 新曾1修改2修改父节点3 术语知识编辑4停用术语
		oldOnto.put("STATUS", 0);//待审核
		oldOnto.put("ONTO_TYPE", ontoType);//本体类型
		oldOnto.put("UPD_DESC","快捷键术语停用");
		oldOnto.put("OPERATION","");
		oldOnto.put("IS_DISABLE", 1);//停用标记
		dao.save("OntologyMapper.saveOntoCopy", oldOnto);
		
		//保存树的副本
		List<PageData> parentList = queryParentOntoById( sqlName,  ONTO_ID);
		if(CollectionUtils.isEmpty(parentList)){
			int TREE_ID = commonService.getSequenceID(pd.getString("ontoType"),2);
			PageData tree = new PageData();
			tree.putAll(pd);
			tree.put("H_ID", UuidUtil.get32UUID());
			tree.put("TREE_ID", TREE_ID);
			tree.put("D_ID", ONTO_ID);
			tree.put("PARENT_ID", "");
			tree.put("UPD_DESC", "快捷键术语停用");
			tree.put("STATUS", 0);//待审核
			tree.put("OP_TYPE", 4);//操作类型：4停用术语
			tree.put("ONTO_TYPE", ontoType);
			tree.put("ONTO_H_ID", H_ID);
			tree.put("IS_DISABLE", 1);//停用标记
			dao.save ("TreeMapper.saveOntoTreeHis", tree);
		}else{
			for(PageData parent:parentList){
				int TREE_ID = commonService.getSequenceID(pd.getString("ontoType"),2);
				PageData tree = new PageData();
				tree.putAll(pd);
				tree.put("H_ID", UuidUtil.get32UUID());
				tree.put("TREE_ID", TREE_ID);
				tree.put("D_ID", ONTO_ID);
				tree.put("PARENT_ID", parent.get("ID"));
				tree.put("UPD_DESC", "快捷键术语停用");
				tree.put("STATUS", 0);//待审核
				tree.put("OP_TYPE", 4);//操作类型：4停用术语
				tree.put("ONTO_TYPE", ontoType);
				tree.put("ONTO_H_ID", H_ID);
				tree.put("IS_DISABLE", 1);//停用标记
				dao.save ("TreeMapper.saveOntoTreeHis", tree);
			}
		}
	}
	
	/**
	 * 诊断维护页面，科室分类选择
	 * @param sqlName
	 * @param oNTO_ID
	 * @return
	 * @throws Exception
	 */
	public List<PageData> queryDepCategory(String ONTO_ID) throws Exception{
		return (List<PageData>)dao.findForList("OntologyMapper.queryDepCategory", ONTO_ID);
	}
	
	/**
	 * 诊断审核页面，科室分类选择
	 * @param sqlName
	 * @param oNTO_ID
	 * @return
	 * @throws Exception
	 */
	public List<PageData> queryDepCategoryHis(PageData pd) throws Exception{
		return (List<PageData>)dao.findForList("OntologyMapper.queryDepCategoryHis", pd);
	}
	
	/**
	 * 知识本体编辑保存
	 * @param pd
	 * @throws Exception
	 */
	public void knowledgeOntoEdit(PageData pd) throws Exception{
		String ONTO_ID = pd.getString("D_ID");
		String ontoType  = OsynConst.DIAG;
		String sqlName = "diag";
		//保存本体副本
		PageData oldOnto = this.selectOntologyById(sqlName, ONTO_ID);
		String H_ID = UuidUtil.get32UUID();
		oldOnto.putAll(pd);
		oldOnto.put("H_ID", H_ID);//历史ID
		oldOnto.put("OP_TYPE", 3);// 操作类型 0 新曾1修改2修改父节点3 术语知识编辑4停用术语
		oldOnto.put("STATUS", 0);//待审核
		oldOnto.put("ONTO_TYPE", ontoType);//本体类型
		oldOnto.put("UPD_DESC","快捷键术语知识编辑");
		oldOnto.put("DEP_CATEGORY", pd.get("DEP_CATEGORY"));
		oldOnto.put("PART_CATEGORY", pd.get("PART_CATEGORY"));
		oldOnto.put("MAN_CATEGORY", pd.get("MAN_CATEGORY"));
		oldOnto.put("DIS_CATEGORY", pd.get("DIS_CATEGORY"));
		oldOnto.put("IS_CHRONIC", pd.get("IS_CHRONIC"));
		dao.save("OntologyMapper.saveOntoCopy", oldOnto);
		
		//保存树的副本
		List<PageData> parentList = queryParentOntoById( sqlName,  ONTO_ID);
		if(CollectionUtils.isEmpty(parentList)){
			int TREE_ID = commonService.getSequenceID(ontoType,2);
			PageData tree = new PageData();
			tree.putAll(pd);
			tree.put("H_ID", UuidUtil.get32UUID());
			tree.put("TREE_ID", TREE_ID);
			tree.put("D_ID", ONTO_ID);
			tree.put("PARENT_ID", "");
			tree.put("UPD_DESC", "快捷键术语知识编辑");
			tree.put("STATUS", 0);//待审核
			tree.put("OP_TYPE", 3);//操作类型：3术语知识编辑
			tree.put("ONTO_TYPE", ontoType);
			tree.put("ONTO_H_ID", H_ID);
			tree.put("IS_DISABLE", 1);//停用标记
			dao.save ("TreeMapper.saveOntoTreeHis", tree);
		}else{
			for(PageData parent:parentList){
				int TREE_ID = commonService.getSequenceID(ontoType,2);
				PageData tree = new PageData();
				tree.putAll(pd);
				tree.put("H_ID", UuidUtil.get32UUID());
				tree.put("TREE_ID", TREE_ID);
				tree.put("D_ID", ONTO_ID);
				tree.put("PARENT_ID", parent.get("ID"));
				tree.put("UPD_DESC", "快捷键术语知识编辑");
				tree.put("STATUS", 0);//待审核
				tree.put("OP_TYPE", 3);//操作类型：3术语知识编辑
				tree.put("ONTO_TYPE", ontoType);
				tree.put("ONTO_H_ID", H_ID);
				dao.save ("TreeMapper.saveOntoTreeHis", tree);
			}
		}
		
	}

	/**
	 * 导入本体信息
	 * @param xwk excel工作区
	 * ontoType 本体类型
	 * isCheck 导入的数据是否需要审核
	 */
	@Override
	public String importOntology(Workbook xwk,PageData pd) throws Exception {
		String ontoType = pd.getString("ontoType");//本体类型
		String msg = "success";
		String idNameStr = HelpUtil.getOntoName(ontoType);
		StringBuffer sbfOsyn = new StringBuffer();
		
		//isCheck 0:不需要审核；1：需要审核；
		Sheet sheet = xwk.getSheetAt(0);
	    //获得文件的有效行数
	    int totalRowNum = sheet.getLastRowNum();
	    List<Row> rowList = new ArrayList<Row>();
	    for (int i = 1; i < totalRowNum; i++) {//从第一行数据开始循环，表头去掉
			Row row = sheet.getRow(i);
			if(row==null||HelpUtil.isBlankRow(row)){//判断该行内容是否为空，若为空直接跳出；
				continue;
			}
			String termName = row.getCell(0).getStringCellValue();//术语名称
			String standName = row.getCell(1).getStringCellValue();//标准术语名称1
			String addStandName = row.getCell(2).getStringCellValue();//标准术语名称2
			String interveneType = row.getCell(3).getStringCellValue();//干预类型：下位词、同义词
			//判断标准术语名称是否为空，若为空则记录术语名称，跳出本次循环
			if("下位词".equals(interveneType)){//若新增标准词
				if("".equals(standName)&&"".equals(addStandName)){//若标准术语名称1、2均为空
					sbfOsyn.append(termName+"；");
					continue;
				}
			}else{//若新增同义词或诊断、科室标准词
				if("".equals(standName)&&standName==null){
					sbfOsyn.append(termName+"；");
					continue;
				}
			}
			rowList.add(row);
		}
	    String returnVal = "";
	    //若导入的数据不为空，则存储数据
	    if(rowList.size()>0){
	    	returnVal=insertImportData(rowList,idNameStr,pd,sbfOsyn);
	    }
	    msg = !"".equals(returnVal)?"错误数据："+returnVal:"success";
		return msg;
	}
	/**
	 * 存储导入数据
	 * @param rowList
	 */
	public String insertImportData(List<Row> rowList,String idNameStr,PageData pd,StringBuffer sbfOsyn)throws Exception{
		String ontoType = pd.getString("ontoType");//本体类型
		String isCheck = pd.getString("isCheck");
		String[] idNames = idNameStr.split(",");
		String typeName = idNames[0];//本体名称
		String osynIdName=idNames[1];//术语ID名
		String idName =idNames[2];//本体ID名
		List<Row> isRow = new ArrayList<Row>();
		String osynOnto = HelpUtil.getOsynType(ontoType);//获取同义词类型
		StringBuffer errorData = new StringBuffer();
		for(Row row:rowList){
			String termName = row.getCell(0).getStringCellValue();//术语名称
			errorData.append(termName+";");
			String standName = row.getCell(1).getStringCellValue();//标准术语名称1
			String addStandName = row.getCell(2).getStringCellValue();//标准术语名称2
			String interveneType = row.getCell(3).getStringCellValue();//干预类型：下位词、同义词
			String termType = row.getCell(4).getStringCellValue();//术语类型：疾病、症状等
			String orgDnChn = row.getCell(5).getStringCellValue();//获取来源
			String updateMan = row.getCell(6).getStringCellValue();//更新人
			Map<String,Object> map = new HashMap<String,Object>();//存储编码(主要编码、附加编码)ID  map
			List<Object> standList = new ArrayList<Object>();//存储本体ID列表
			PageData codePd = new PageData();//存储编码，用于判断编码数量
			PageData namePd = new PageData();//用于存储本体信息
			boolean standExist = false;
			boolean addExist = false;
			try {
				if(!"".equals(standName)&&standName!=null){//判断标准术语名称是否存在
					standExist=isExist(standName,typeName);
					if(!standExist){//若标准术语名称不存在，记录信息，结束本次循环
						isRow.add(row);
						continue;
					}
				}
				
				Map<String,String> addMap=new HashMap<String,String>();
				if(!"".equals(addStandName)&&addStandName!=null&&OsynConst.DIAG.equals(ontoType)){//若为诊断时，附加标准术语是否存在
					addExist = isExist(addStandName, typeName);
					if (!addExist) {//若不存在，记录信息并跳出本次循环
						isRow.add(row);
						continue;
					}
				}
				termType=HelpUtil.getTermType(termType)==""?"0":HelpUtil.getTermType(termType);//术语类型
				String osynType = HelpUtil.getTermType(interveneType)==""?"23107":HelpUtil.getTermType(interveneType);//获取同义词类型
				namePd.put("IS_DISABLE","0");//停用标记
				namePd.put("UPDATE_TIME",new Date());//更新时间
				namePd.put("ORG_DN_CHN",orgDnChn);
				if ("".equals(updateMan)||updateMan!=null) {//若导入人员为空，则当前登录用户默认为更新人
					updateMan=pd.getString("UPDATE_MAN");
				}
				namePd.put("UPDATE_MAN",updateMan);//更新人
				namePd.put("ONTO_TYPE",osynOnto);//历史表中的术语类型
				namePd.put("UPD_DESC","导入数据");
				namePd.put("OP_TYPE","0");//操作类型
				namePd.put("STATUS","0");//未审核
				PageData checkPd = new PageData();
				if ("0".equals(isCheck)) {//若不需要审核，直接将数据存储到术语表中
					checkPd.put("CHECK_USER",pd.getString("UPDATE_MAN"));//审核人
					checkPd.put("CHECK_TIME",new Date());//审核时间
					checkPd.put("CHECK_MEMO","直接导入数据，不需要审核");
					checkPd.put("STATUS","1");//审核通过
				}
				//术语名称可能有多个，当本条数据为同义词时，可能有多个同义词名称以分号分割，因此需要拆分
				String[] termNames = termName.split(";");
				for (int j = 0; j < termNames.length; j++) {
					String osynName = termNames[j];//术语名称
					PageData existData = new PageData();
					existData.put("DN_CHN",osynName);
					existData.put("IS_DISABLE","0");
					boolean nameExist = osynService.checkExistName(typeName, existData);
					if(nameExist){//若其中一个名称存在，则记录存在的名称，不执行新增操作
						sbfOsyn.append(osynName);
						continue;
					}
					/**插入本体、同义词信息**/
					//获取同义词序列
					Integer dn_id = commonService.getSequenceID(osynOnto, 1);
					namePd.put("DN_ID",dn_id);
					namePd.put("H_ID",UuidUtil.get32UUID());
					//判断干预类型：下位词、完全同义词、语用同义词、专指同义词；根据干预类型判断同义词类型
					if("下位词".equals(interveneType)){//若干预类型为下位词，则新增的是标准词
						namePd.put("STAD_DN_ID",dn_id);//标准术语ID
						namePd.put("TERM_TYPE",termType);//术语类型
					}else{//设置同义词类型、标准词ID
						namePd.put("SYNO_TYPE",osynType);
						namePd.put("STAD_DN_ID",map.get("standId"));//标准术语ID
					}
					namePd.put("DN_CHN",osynName);
					PageData addExistPd = new PageData();
					//不需要审核，生成主要编码和附加编码
					addExistPd.put("DN_CHN",standName.trim());
					List<PageData> list = (List<PageData>)dao.findForList("OsynMapper."+typeName+"CheckExistName", addExistPd);
					if("下位词".equals(interveneType)){//新增本体表信息
						int d_id =commonService.getSequenceID(ontoType, 0);
						namePd.put("H_ID",UuidUtil.get32UUID());
						namePd.put("D_ID",d_id);
						namePd.put("STAD_DN_CHN",osynName);
						namePd.put("ONTO_TYPE",ontoType);
						namePd.put("MAIN_CODE","XXX");
						dao.save("OntologyMapper.saveOntoCopy",namePd);//保存本体历史信息
						//若不需要审核更改插入的历史记录状态，将信息插入到本体表中
						if("0".equals(isCheck)){
							PageData addInsertData = (PageData) dao.findForObject("OntologyMapper.queryOntoHisById",namePd.get("H_ID"));//获取插入的历史信息
							//若附加编码对应的标准词存在，则存入附加编码ID到编码map中
							PageData idPd=new PageData();
							if(standExist){
								//若标准名称存在，则获取list中的第一条数据,并获取标准名称ID
								idPd= (PageData) list.get(0);
								Object standId = idPd.get(osynIdName);
								map.put("standId",standId);
								//获取编码：主要编码
								idPd.put("DN_ID",standId);//查询本体ID使用
								idPd = (PageData) dao.findForObject("OntologyMapper."+typeName+"IdFindByNameId",idPd);
								String mainId = String.valueOf(idPd.get(idName));
								standList.add(mainId);//添加主要编码对应的本体ID
							}
							if(addExist&&OsynConst.DIAG.equals(ontoType)){
								//当标准术语名称2不为空时，需要进行编码判断
								addExistPd.put("DN_CHN",addStandName);
								List<PageData> addList = (List<PageData>)dao.findForList("OsynMapper."+typeName+"CheckExistName", addExistPd);
								PageData addCodePd = (PageData) addList.get(0);//附加编码标准词信息
								Object addStandId = addCodePd.get(osynIdName);
								map.put("addStandId",addStandId);//存入附加编码标准词ID
								addCodePd.put("DN_ID",addStandId);//获取编码:附加编码本体信息
								addCodePd = (PageData) dao.findForObject("OntologyMapper."+typeName+"IdFindByNameId",addCodePd);
								if(addCodePd.get("MAIN_CODE")!=null){
									codePd.put("MAIN_CODE2",addCodePd.get("MAIN_CODE"));
								}
								if(addCodePd.get("ADD_CODE")!=null){
									codePd.put("ADD_CODE2",addCodePd.get("ADD_CODE"));
								}
								String addId = String.valueOf(addCodePd.get(idName));//附加编码父ID							
								addMap= commonService.getCode(ontoType, addInsertData,addId);
								standList.add(addCodePd.get(idName));//添加附加编码的本体ID
								if(standExist){//若标准名称存在，则需要比较主要编码，附加编码，判断是否为错误数据
									if(idPd.get("MAIN_CODE")!=null){
										codePd.put("MAIN_CODE1",idPd.get("MAIN_CODE"));
									}
									if(idPd.get("ADD_CODE")!=null){
										codePd.put("ADD_CODE1",idPd.get("ADD_CODE"));
									}
									boolean isError=isErrorData(codePd);
									if(isError){//如果不是错误数据，则根据父节点编码自动生成编码
										//若为2个主要编码，则需要将其中一个主要编码作为本体的附加编码
										if(codePd.get("MAIN_CODE2")!=null&&codePd.get("MAIN_CODE1")!=null){
											namePd.put("ADD_CODE",addMap.get("MAIN_CODE"));
										}else{
											namePd.put("ADD_CODE",addMap.get("ADD_CODE"));
										}
										addMap=commonService.getCode(ontoType, namePd,idPd.getString(idName));
										namePd.put("MAIN_CODE",addMap.get("MAIN_CODE"));
									}else{//若为错误编码数据，删除已经存入的历史信息，记录同义词名称
										sbfOsyn.append(osynName+";");
										dao.delete("OntologyMapper.deleteOntoHist",namePd.get("H_ID"));
										continue;
									}
								}else{
									namePd.put("MAIN_CODE",addMap.get("MAIN_CODE"));
									namePd.put("ADD_CODE",addMap.get("ADD_CODE"));
								}
								
							}else{//若标准术语名称2为空,或导入的不是诊断名称信息，则直接存入主要编码和附加编码
								addMap=commonService.getCode(ontoType, addInsertData,String.valueOf(idPd.get(idName)));
								namePd.put("ADD_CODE",addMap.get("ADD_CODE"));
								namePd.put("MAIN_CODE",addMap.get("MAIN_CODE"));
							}
							checkPd.put("H_ID",namePd.get("H_ID"));
							dao.update("OntologyMapper.ontoCheckStatus", checkPd);//不需要审核直接更改本体历史状态
							dao.save("OsynMapper."+typeName+"InsertOsynName",namePd);//保存术语表标准词信息
							dao.save("OntologyMapper."+typeName+"InsertOntoByHis",namePd);//将历史表信息存储到正式表中，本体
						}
						//存储本体树
						PageData treePd = new PageData();
						treePd.put("D_ID",d_id);
						treePd.put("ONTO_H_ID",namePd.get("H_ID"));
						treePd.put("UPDATE_TIME",new Date());
						treePd.put("UPDATE_MAN",pd.get("UPDATE_MAN"));
						treePd.put("ONTO_TYPE",ontoType);//历史表中的术语类型
						treePd.put("UPD_DESC","导入数据");
						treePd.put("OP_TYPE","0");//操作类型
						treePd.put("STATUS","0");//审核状态
						for (int k = 0; k < standList.size(); k++) {
							int treeId = commonService.getSequenceID(ontoType, 2);
							treePd.put("TREE_ID",treeId);
							treePd.put("H_ID",UuidUtil.get32UUID());
							treePd.put("PARENT_ID",standList.get(k));
							dao.save("TreeMapper.saveOntoTreeHis", treePd);//存储本体树历史表信息
							if("0".equals(isCheck)){//若不需要审核，更改插入的历史树信息，将信息存储到本体树关系表中
								checkPd.put("H_ID",treePd.get("H_ID"));
								dao.update("TreeMapper.ontoTreeCheckStatus", checkPd);//不需要审核直接更改本体树状态
								dao.save("TreeMapper."+typeName+"TreeInsert",treePd);//本体树
							}
						}
					}else{//同义词
						PageData standIdPd = list.get(0);
						standIdPd.put("DN_ID",standIdPd.get(osynIdName));//查询本体ID使用
						standIdPd = (PageData) dao.findForObject("OntologyMapper."+typeName+"IdFindByNameId",standIdPd);
						namePd.put("STAD_DN_ID",standIdPd.get(idName));
						osynHisService.saveDiagOsyn(namePd);//新增术语历史表信息
						if("0".equals(isCheck)){
							//更改审核状态
							checkPd.put("H_ID",namePd.get("H_ID"));
							dao.update("AiAlterNameHistMapper.upAlterName",checkPd);
							//存储同义词表信息
							dao.save("OsynMapper."+typeName+"InsertOsynName",namePd);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(isRow.size()>0&&rowList.size()!=isRow.size()){
			insertImportData(isRow,idNameStr,pd,sbfOsyn);
		}
		String error = sbfOsyn.toString();
		if(rowList.size()==isRow.size()){
			error=sbfOsyn.toString()+errorData.toString();
		}
		return error;
	}
	/**
	 * 判断标准名称是否存在
	 * @return
	 */
	public boolean isExist(String standName,String typeName){
		boolean standExist=true;//默认存在
		try {
			//查询标准术语名称是否存在
			PageData namePd = new PageData();
			namePd.put("DN_CHN",standName.trim());
			standExist = osynService.checkExistName(typeName, namePd);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return standExist;
	}
	
	/**
	 * 判断数据是否可以导入
	 * @param pd
	 * @return
	 */
	public static boolean isErrorData(PageData codePd){
		if(codePd.size()>2){
			return false;
		}
		Object addCode1 = codePd.get("ADD_CODE1");
		Object addCode2 = codePd.get("ADD_CODE2");
		if(addCode1!=null&&addCode2!=null){//若codePd size<=2时，若同时存在两个附加编码，则也是错误数据
			return false;
		}
		return true;
	}
	
	/**
	 * 查询上一条历史信息，以本体ID 和 更新时间为条件
	 * @param sqlName
	 * @param newOnto
	 * @return
	 * @throws Exception
	 */
	public String queryBrotherHisInfo(PageData newOnto) throws Exception{
		List<PageData> list = (List<PageData>) dao.findForList("OntologyMapper.queryBrotherHisInfo", newOnto);
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0).getString("H_ID");
		}
		return null;
	}
	
}
