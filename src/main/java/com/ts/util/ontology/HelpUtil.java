package com.ts.util.ontology;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.util.CollectionUtils;

import com.ts.entity.system.User;
import com.ts.util.Const;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.Tools;

/**
 * 本体管理帮助类
 * @author silong.xing
 *
 */
public class HelpUtil {
	private static Map<String,String> IS_DISABLE_MAP = new HashMap<String, String>();
	private static Map<String,String> TERM_TYPE_MAP = new HashMap<String, String>();
	//部位分类
	private static Map<String,String> PART_MAP = new HashMap<String, String>();
	//人群分类
	private static Map<String,String> MAIN_MAP = new HashMap<String, String>();
	//病种类型
	private static Map<String,String> DIS_MAP = new HashMap<String, String>();
	//词语类型，药品名称
	private static Map<String,String> WORD_TYPE_MAP = new HashMap<String,String>();
	//同义词类型
	private static Map<String,String> OSYN_TYPE = new HashMap<String,String>();
	
	static {
		TERM_TYPE_MAP.put("1", "症状");
		TERM_TYPE_MAP.put("2", "疾病");
		IS_DISABLE_MAP.put("0", "否");
		IS_DISABLE_MAP.put("1", "是");
		PART_MAP=getPartBodyMap(0);
		MAIN_MAP=getPartBodyMap(1);
		DIS_MAP.put("0", "否");
		DIS_MAP.put("1", "是");
		WORD_TYPE_MAP.put(OsynConst.DRUG_NAME_COMMON_CODE,"药品通用名");
		WORD_TYPE_MAP.put(OsynConst.DRUG_NAME_CHEMI_CODE, "药品化学名");
		WORD_TYPE_MAP.put(OsynConst.DRUG_NAME_GOODS_CODE, "药品商品名");
		WORD_TYPE_MAP.put(OsynConst.DRUG_NAME_PRODUCT_CODE, "药品产品名");
		OSYN_TYPE.put(OsynConst.OSYN_OTHER_TYPE,"其他");
		OSYN_TYPE.put(OsynConst.OSYN_LANGUAGE,"语用同义词");
		OSYN_TYPE.put(OsynConst.OSYN_SPECIAL_TYPE,"专指同义词");
		OSYN_TYPE.put(OsynConst.OSYN_FAULT_TYPE,"同音字/错别字");
		OSYN_TYPE.put(OsynConst.OSYN_FIRST_LETTER,"拼音首字母");
		OSYN_TYPE.put(OsynConst.OSYN_COMMON_CALLED,"俗称");
		OSYN_TYPE.put(OsynConst.OSYN_ABB,"缩略语");
		
	}
	
	/**
	 * 根据权限设置下拉框值
	 * @param user
	 * @param i
	 * @return
	 */
	public static Map<String, String> getOntotypeMap(User user, int i) {
		Map<String,String> typeMap = new LinkedHashMap<String,String>();
		Integer roleMax = (Integer)Jurisdiction.getSession().getAttribute(user.getUSERNAME()+Const.SESSION_USER_MAX_ROLE);
		if(roleMax == 0){
			typeMap.put(OsynConst.DIAG, "诊断");
			typeMap.put(OsynConst.OP, "手术");
			typeMap.put(OsynConst.DEP, "科室");
			typeMap.put(OsynConst.DRUG, "药品");
		}else{
			if(i==0){
				//本体查询权限判断
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_QUERY_DIAG)){
					typeMap.put(OsynConst.DIAG, "诊断");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_QUERY_OP)){
					typeMap.put(OsynConst.OP, "手术");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_QUERY_DEP)){
					typeMap.put(OsynConst.DEP, "科室");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_QUERY_DRUG)){
					typeMap.put(OsynConst.DRUG, "药品");
				}
			}else if(i==1){
				//本体审核权限判断
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_CHECK_DRUG)){
					typeMap.put(OsynConst.DRUG, "药品");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_CHECK_OP)){
					typeMap.put(OsynConst.OP, "手术");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_CHECK_DIAG)){
					typeMap.put(OsynConst.DIAG, "诊断");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_CHECK_DEP)){
					typeMap.put(OsynConst.DEP, "科室");
				}
			}else if(i==2){
				//本体历史权限判断
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_HIS_DRUG)){
					typeMap.put(OsynConst.DRUG, "药品");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_HIS_OP)){
					typeMap.put(OsynConst.OP, "手术");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_HIS_DIAG)){
					typeMap.put(OsynConst.DIAG, "诊断");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_HIS_DEP)){
					typeMap.put(OsynConst.DEP, "科室");
				}
			}else if(i==99){
				//本体导入权限判断
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_IMPORT_DRUG)){
					typeMap.put(OsynConst.DRUG, "药品");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_IMPORT_OP)){
					typeMap.put(OsynConst.OP, "手术");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_IMPORT_DIAG)){
					typeMap.put(OsynConst.DIAG, "诊断");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_IMPORT_DEP)){
					typeMap.put(OsynConst.DEP, "科室");
				}
			}
		}
		return typeMap;
	}
	
	public static String setUpdateDesc(String ontoType,PageData oldOnto, PageData newOnto) {
		StringBuffer str  =new StringBuffer("修改信息：");
		if(OsynConst.DRUG.equals(ontoType)){
			//药品
			
		}else if (OsynConst.OP.equals(ontoType)){
			//手术
			setCompareDescParent(str,oldOnto,newOnto);
			setCompareDesc(str,oldOnto,"STAD_OP_CHN",newOnto,"STAD_DN_CHN","规范名称中文",null);
			setCompareDesc(str,oldOnto,"STAD_OP_ENG",newOnto,"STAD_DN_ENG","规范名称英文",null);
			setCompareDesc(str,oldOnto,"ORG_STAD_OP_CHN",newOnto,"ORG_STAD_DN_CHN","来源规范名称中文",null);
			setCompareDesc(str,oldOnto,"ORG_STAD_OP_ENG",newOnto,"ORG_STAD_DN_ENG","来源规范名称英文",null);
			setCompareDesc(str,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","停用标记",IS_DISABLE_MAP);
			setCompareDesc(str,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","停用描述",null);
		}else if (OsynConst.DIAG.equals(ontoType)){
			setCompareDescParent(str,oldOnto,newOnto);
			setCompareDesc(str,oldOnto,"STAD_DN_CHN",newOnto,"STAD_DN_CHN","规范名称中文",null);
			setCompareDesc(str,oldOnto,"STAD_DN_ENG",newOnto,"STAD_DN_ENG","规范名称英文",null);
			setCompareDesc(str,oldOnto,"ORG_STAD_DN_CHN",newOnto,"ORG_STAD_DN_CHN","来源规范名称中文",null);
			setCompareDesc(str,oldOnto,"ORG_STAD_DN_ENG",newOnto,"ORG_STAD_DN_ENG","来源规范名称英文",null);
			setCompareDesc(str,oldOnto,"TERM_DEFIN",newOnto,"TERM_DEFIN","术语定义",null);
			setCompareDesc(str,oldOnto,"TERM_TYPE",newOnto,"TERM_TYPE","术语类型",TERM_TYPE_MAP);
			setCompareDesc(str,oldOnto,"DEP_CATEGORY_NAME",newOnto,"DEP_CATEGORY_NAME","科室分类",null);
			setCompareDesc(str,oldOnto,"PART_CATEGORY",newOnto,"PART_CATEGORY","部位分类",PART_MAP);
			setCompareDesc(str,oldOnto,"MAN_CATEGORY",newOnto,"MAN_CATEGORY","人群分类",MAIN_MAP);
			setCompareDesc(str,oldOnto,"DIS_CATEGORY",newOnto,"DIS_CATEGORY","病种类型",DIS_MAP);
			setCompareDesc(str,oldOnto,"IS_CHRONIC",newOnto,"IS_CHRONIC","是否慢性病",IS_DISABLE_MAP);
			setCompareDesc(str,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","停用标记",IS_DISABLE_MAP);
			setCompareDesc(str,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","停用描述",null);
		}else if (OsynConst.DEP.equals(ontoType)){
			//手术
			setCompareDescParent(str,oldOnto,newOnto);
			setCompareDesc(str,oldOnto,"DEP_STAD_NAME",newOnto,"STAD_DN_CHN","规范名称中文",null);
			setCompareDesc(str,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","停用标记",IS_DISABLE_MAP);
			setCompareDesc(str,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","停用描述",null);
			setCompareDesc(str,oldOnto,"DEFINITION",newOnto,"TERM_DEFIN","科室定义",null);
		}
		if(str.length()>5){
			newOnto.put("UPD_DESC",str.toString());
			return newOnto.getString("UPD_DESC");
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void setCompareDescParent(StringBuffer str,PageData oldOnto, PageData newOnto) {
		String[] newParent = null;
		if(newOnto.get("PARENT_IDS") instanceof String){
			newParent = new String[]{newOnto.getString("PARENT_IDS")};
		}else{
			newParent = (String[])newOnto.get("PARENT_IDS");
		}
		List<String> oldParent = (List<String>)oldOnto.get("PARENT_IDS");
		if(!checkModifyParent(newParent,oldParent)){
			str.append("本体父节点:").append(newOnto.getString("PARENT_NAMES")).append(";");
		}
	}

	public static boolean setCompareDesc(StringBuffer str,PageData oldOnto,String oldKey,PageData newOnto,String newKey, String desc,Map<String,String> map) {
		//旧值
		Object a = oldOnto.get(oldKey);
		String s1 = null;
		if(a==null){
			s1 = "";
		}else if(a instanceof String){
			s1 = a==null?"":a.toString().trim();
		}else if (a instanceof BigDecimal ) {
			s1 = (a==null?"":((BigDecimal)a).toString());
		}
		//新值
		String s2 = newOnto.get(newKey)==null?"":newOnto.get(newKey).toString();
		//对比
		if(!s1.equals(s2)){
			str.append(desc).append(":").append(map==null?s2:map.get(s2)).append(";");
			return true;
		}
		return false;
	}

	/**
	 * 根据本体类型 找到 同义词类型
	 * @param ontoType
	 * @return
	 */
	public static String getOsynType(String ontoType) {
		if(OsynConst.DIAG.equals(ontoType)){
			//诊断通用名类型
			return OsynConst.DIAG_NAME_CODE;
		}else if(OsynConst.OP.equals(ontoType)){
			//手术通用名类型
			return OsynConst.OPERATION_NAME_CODE;
		}else if(OsynConst.DEP.equals(ontoType)){
			//科室通用名类型
			return OsynConst.DEPT_NAME_CODE;
		}
		
		return ontoType;
	}

	/**
	 * 比较两个数组值是否相等，相等返回true，不同返回false
	 * @param pids
	 * @param parentIds
	 * @return
	 */
	public static boolean checkModifyParent(String[] pids, List<String> parentIds) {
		if((pids==null||pids.length==0) && CollectionUtils.isEmpty(parentIds)){
			return true;
		}
		if((pids==null||pids.length==0) && !CollectionUtils.isEmpty(parentIds)){
			return false;
		}
		if(pids!=null && pids.length>0 && CollectionUtils.isEmpty(parentIds)){
			return false;
		}
		if(pids.length != parentIds.size()){
			return false;
		}
		for(String parentId:pids){
			if(!parentIds.contains(parentId)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 比较两个数组值是否相等，相等返回true，不同返回false
	 * @param pids
	 * @param parentIds
	 * @return
	 */
	public static boolean checkModifyParent2(List<PageData> pd,List<String> parentIds) {
		List<String> pids = new ArrayList<String>();
		for(PageData p:pd){
			if(p.get("PARENT_ID")!=null){
				pids.add(p.get("PARENT_ID").toString());
			}
		}
		if(pids.size()>0){
			String[] ids = new String[pids.size()];
			for(int i = 0 ;i<pids.size();i++){
				ids[i] = pids.get(i);
			}
			return checkModifyParent(ids,parentIds);
		}
		return checkModifyParent(null,parentIds);
	}
	
	public static String setOsynUpdateDesc(String ontoType,PageData oldOnto, PageData newOnto) {
		StringBuffer str  =new StringBuffer("修改信息：");
		if(OsynConst.DRUG_NAME_COMMON_CODE.equals(ontoType)||OsynConst.DRUG_NAME_CHEMI_CODE.equals(ontoType)||OsynConst.DRUG_NAME_GOODS_CODE.equals(ontoType)||OsynConst.DRUG_NAME_PRODUCT_CODE.equals(ontoType)){
			//药品名称
			setCompareOsynDesc(str,oldOnto,"CHN_NAME",newOnto,"DN_CHN","药品名称_中文",null);
			setCompareOsynDesc(str,oldOnto,"ENG_NAME",newOnto,"DN_ENG","药品名称_英文",null);
			setCompareOsynDesc(str,oldOnto,"STAD_CHN_NAME",newOnto,"STAD_CHN","药品标准名称",null);
			setCompareOsynDesc(str,oldOnto,"ORG_CHN_NAME",newOnto,"ORG_DN_CHN","来源-药品名称_中文",null);
			setCompareOsynDesc(str,oldOnto,"ORG_ENG_NAME",newOnto,"ORG_DN_ENG","来源-药品名称_英文",null);
			setCompareOsynDesc(str,oldOnto,"WORD_TYPE",newOnto,"ONTO_TYPE","词语类型",WORD_TYPE_MAP);
			setCompareOsynDesc(str,oldOnto,"REMARK",newOnto,"DRUG_MEMO","备注",null);
			setCompareOsynDesc(str,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","停用标记",IS_DISABLE_MAP);
			setCompareOsynDesc(str,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","停用描述",null);
		}else if (OsynConst.OPERATION_NAME_CODE.equals(ontoType)){
			//手术
			setCompareOsynDesc(str,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","停用标记",IS_DISABLE_MAP);
			setCompareOsynDesc(str,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","停用描述",null);
			setCompareOsynDesc(str,oldOnto,"ON_CHN",newOnto,"DN_CHN","手术名称中文",null);
			setCompareOsynDesc(str,oldOnto,"ON_ENG",newOnto,"DN_ENG","手术名称英文",null);
			setCompareOsynDesc(str,oldOnto,"ORG_OP_CHN",newOnto,"ORG_DN_CHN","来源-手术名称中文",null);
			setCompareOsynDesc(str,oldOnto,"ORG_OP_ENG",newOnto,"ORG_DN_ENG","来源-手术名称英文",null);
			setCompareOsynDesc(str,oldOnto,"STAD_ON_ID",newOnto,"STAD_DN_ID","标准手术名称",null);
			setCompareOsynDesc(str,oldOnto,"SYNO_TYPE",newOnto,"SYNO_TYPE","同义词类型",OSYN_TYPE);
		}else if (OsynConst.DEPT_NAME_CODE.equals(ontoType)){//科室
			setCompareOsynDesc(str,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","停用标记",IS_DISABLE_MAP);
			setCompareOsynDesc(str,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","停用描述",null);
			setCompareOsynDesc(str,oldOnto,"DEP_NAME",newOnto,"DN_CHN","科室名称",null);
			setCompareOsynDesc(str,oldOnto,"STAD_ID",newOnto,"STAD_DN_ID","标准科室名称",null);
			setCompareOsynDesc(str,oldOnto,"SYNO_TYPE",newOnto,"SYNO_TYPE","同义词类型",OSYN_TYPE);
		} else if (OsynConst.DIAG_NAME_CODE.equals(ontoType)){//诊断名称同义词
			setCompareOsynDesc(str,oldOnto,"DN_CHN",newOnto,"DN_CHN","诊断名称_中文",null);
			setCompareOsynDesc(str,oldOnto,"DN_ENG",newOnto,"DN_ENG","诊断名称_英文",null);
			setCompareOsynDesc(str,oldOnto,"STAD_DN_ID",newOnto,"STAD_DN_ID","标准诊断名称ID",null);
			setCompareOsynDesc(str,oldOnto,"TERM_TYPE",newOnto,"TERM_TYPE","术语类型",null);
			setCompareOsynDesc(str,oldOnto,"SYNO_TYPE",newOnto,"SYNO_TYPE","同义词类型",OSYN_TYPE);
			setCompareOsynDesc(str,oldOnto,"ORG_DN_CHN",newOnto,"ORG_DN_CHN","来源-诊断名称_中文",null);
			setCompareOsynDesc(str,oldOnto,"ORG_DN_ENG",newOnto,"ORG_DN_ENG","来源-诊断名称_英文",null);
			setCompareOsynDesc(str,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","停用标记",IS_DISABLE_MAP);
			setCompareOsynDesc(str,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","停用描述",null);
		}else if(OsynConst.PACK_SPEC_CODE.equals(ontoType)){//药品包装规格
			setCompareOsynDesc(str,oldOnto,"PS_CHN",newOnto,"DN_CHN","药品包装规格_中文",null);
			setCompareOsynDesc(str,oldOnto,"PS_ENG",newOnto,"DN_ENG","药品包装规格_英文",null);
//			setCompareOsynDesc(str,oldOnto,"STAD_PS_ID",newOnto,"STAD_DN_ID","药品标准包装规格ID",null);
			setCompareOsynDesc(str,oldOnto,"STAD_PS_CHN",newOnto,"STAD_CHN","药品标准包装规格",null);
			setCompareOsynDesc(str,oldOnto,"STAD_PS_VALUE_CHN",newOnto,"STAD_PACK_SPEC_CHN","药品标准包装规格数值_中文",null);
			setCompareOsynDesc(str,oldOnto,"STAD_PS_UNIT_CHN",newOnto,"STAD_PACK_SPEC_UNIT_CHN","药品标准包装规格单位",null);
//			setCompareOsynDesc(str,oldOnto,"STAD_PS_ENG",newOnto,"STAD_ENG","药品标准包装规格_英文",null);
			setCompareOsynDesc(str,oldOnto,"STAD_PS_VALUE_ENG",newOnto,"STAD_PACK_SPEC_ENG","药品标准包装规格数值_英文",null);
//			setCompareOsynDesc(str,oldOnto,"STAD_PS_UNIT_ENG",newOnto,"STAD_PACK_SPEC_UNIT_ENG","药品标准包装规格单位_英文",null);
//			setCompareOsynDesc(str,oldOnto,"PSU_ID",newOnto,"PSU_ID","药品标准规格单位ID",null);
			setCompareOsynDesc(str,oldOnto,"ORG_PS_CHN",newOnto,"ORG_DN_CHN","来源-药品包装规格_中文",null);
			setCompareOsynDesc(str,oldOnto,"ORG_PS_ENG",newOnto,"ORG_DN_ENG","来源-药品包装规格_英文",null);
//			setCompareOsynDesc(str,oldOnto,"ORG_STAD_PS_CHN",newOnto,"ORG_STAD_CHN","来源-药品标准包装规格_中文",null);
//			setCompareOsynDesc(str,oldOnto,"ORG_STAD_PS_ENG",newOnto,"ORG_STAD_ENG","来源-药品标准包装规格_英文",null);
			setCompareOsynDesc(str,oldOnto,"REMARK",newOnto,"DRUG_MEMO","备注",null);
			setCompareOsynDesc(str,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","停用标记 ",IS_DISABLE_MAP);
			setCompareOsynDesc(str,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","停用描述",null);
		}else if (OsynConst.PACK_SPEC_UNIT_CODE.equals(ontoType)) {//药品包装规格单位
			setCompareOsynDesc(str,oldOnto,"PSU_CHN",newOnto,"DN_CHN","药品规格单位_中文",null);
			setCompareOsynDesc(str,oldOnto,"PSU_ENG",newOnto,"DN_ENG","药品规格单位_英文",null);
//			setCompareOsynDesc(str,oldOnto,"PSU_STAD_ID",newOnto,"STAD_DN_ID","药品标准规格单位ID",null);
//			setCompareOsynDesc(str,oldOnto,"PSU_STAD_ENG",newOnto,"STAD_ENG","药品标准规格单位_英文",null);
			setCompareOsynDesc(str,oldOnto,"PSU_STAD_CHN",newOnto,"STAD_CHN","药品标准规格单位",null);
			setCompareOsynDesc(str,oldOnto,"ORG_PSU_CHN",newOnto,"ORG_DN_CHN","来源-药品规格单位_中文",null);
			setCompareOsynDesc(str,oldOnto,"ORG_PSU_ENG",newOnto,"ORG_DN_ENG","来源-药品规格单位_英文",null);
//			setCompareOsynDesc(str,oldOnto,"ORG_PSU_STAD_CHN",newOnto,"ORG_STAD_CHN","来源-药品标准规格单位_中文",null);
//			setCompareOsynDesc(str,oldOnto,"ORG_PSU_STAD_ENG",newOnto,"ORG_STAD_ENG","来源-药品标准规格单位_英文",null);
			setCompareOsynDesc(str,oldOnto,"REMARK",newOnto,"DRUG_MEMO","备注",null);
			setCompareOsynDesc(str,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","停用标记",IS_DISABLE_MAP);
			setCompareOsynDesc(str,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","停用描述",null);
		}else if (OsynConst.DRUG_FACTORY_CODE.equals(ontoType)) {//药品生产企业
			setCompareOsynDesc(str,oldOnto,"FAC_CHN",newOnto,"DN_CHN","生产企业名称_中文",null);
			setCompareOsynDesc(str,oldOnto,"FAC_ENG",newOnto,"DN_ENG","生产企业名称_英文",null);
			setCompareOsynDesc(str,oldOnto,"FAC_ABB",newOnto,"FAC_ABB","生产企业名称_简称",null);
//			setCompareOsynDesc(str,oldOnto,"STAD_FAC_ID",newOnto,"STAD_DN_ID","药品标准生产企业ID",null);
			setCompareOsynDesc(str,oldOnto,"STAD_FAC_CHN",newOnto,"STAD_CHN","药品标准生产企业",null);
//			setCompareOsynDesc(str,oldOnto,"STAD_FAC_ENG",newOnto,"STAD_ENG","药品标准生产企业名称_英文",null);
//			setCompareOsynDesc(str,oldOnto,"STAD_FAC_ABB",newOnto,"STAD_FAC_ABB","药品标准生产企业名称_简称",null);
			setCompareOsynDesc(str,oldOnto,"ORG_FAC_CHN",newOnto,"ORG_DN_CHN","来源-生产企业名称_中文",null);
			setCompareOsynDesc(str,oldOnto,"ORG_FAC_ENG",newOnto,"ORG_DN_ENG","来源-生产企业名称_英文",null);
//			setCompareOsynDesc(str,oldOnto,"ORG_STAD_FAC_CHN",newOnto,"ORG_STAD_CHN","来源-药品标准生产企业名称_中文",null);
//			setCompareOsynDesc(str,oldOnto,"ORG_STAD_FAC_ENG",newOnto,"ORG_STAD_ENG","来源-药品标准生产企业名称_英文",null);
			setCompareOsynDesc(str,oldOnto,"ORG_FAC_ABB",newOnto,"ORG_FAC_ABB","来源-生产企业名称_简称",null);
//			setCompareOsynDesc(str,oldOnto,"ORG_STAD_FAC_ABB",newOnto,"ORG_STAD_FAC_ABB","来源-药品标准生产企业名称_简称",null);
			setCompareOsynDesc(str,oldOnto,"COUNTRY",newOnto,"COUNTRY","国家",null);
			setCompareOsynDesc(str,oldOnto,"AREA",newOnto,"DISTRICT","地区",null);
			setCompareOsynDesc(str,oldOnto,"AREA_CODE",newOnto,"AREA_CODE","区号",null);
			setCompareOsynDesc(str,oldOnto,"REMARK",newOnto,"DRUG_MEMO","备注",null);
			setCompareOsynDesc(str,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","停用标记",IS_DISABLE_MAP);
			setCompareOsynDesc(str,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","停用描述",null);
		}else if (OsynConst.DRUG_PACK_CODE.equals(ontoType)) {//药品包装材质
			setCompareOsynDesc(str,oldOnto,"DP_CHN",newOnto,"DN_CHN","包装材质_中文",null);
			setCompareOsynDesc(str,oldOnto,"DP_ENG",newOnto,"DN_ENG","包装材质_英文",null);
//			setCompareOsynDesc(str,oldOnto,"STAD_DP_ID",newOnto,"STAD_DN_ID","药品标准包装材质ID",null);
			setCompareOsynDesc(str,oldOnto,"STAD_DP_CHN",newOnto,"STAD_CHN","药品标准包装材质",null);
//			setCompareOsynDesc(str,oldOnto,"STAD_DP_ENG",newOnto,"STAD_ENG","药品标准包装材质_英文",null);
			setCompareOsynDesc(str,oldOnto,"ORG_DP_CHN",newOnto,"ORG_DN_CHN","来源-包装材质_中文",null);
			setCompareOsynDesc(str,oldOnto,"ORG_DP_ENG",newOnto,"ORG_DN_ENG","来源-包装材质_英文",null);
//			setCompareOsynDesc(str,oldOnto,"ORG_STAD_DP_CHN",newOnto,"ORG_STAD_CHN","来源-药品标准包装材质_中文",null);
//			setCompareOsynDesc(str,oldOnto,"ORG_STAD_DP_ENG",newOnto,"ORG_STAD_ENG","来源-药品标准包装材质_英文",null);
			setCompareOsynDesc(str,oldOnto,"REMARK",newOnto,"DRUG_MEMO","备注",null);
			setCompareOsynDesc(str,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","停用标记",IS_DISABLE_MAP);
			setCompareOsynDesc(str,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","停用描述",null);
		}else if (OsynConst.DRUG_SPEC_CODE.equals(ontoType)) {//药品规格表
			setCompareOsynDesc(str,oldOnto,"DRUG_SP",newOnto,"DN_CHN","药品规格",null);
			//setCompareOsynDesc(str,oldOnto,"DRUG_STAD_ID",newOnto,"STAD_DN_ID","药品标准规格ID",null);
			setCompareOsynDesc(str,oldOnto,"DRUG_STAD",newOnto,"STAD_CHN","药品标准规格",null);
			setCompareOsynDesc(str,oldOnto,"ORG_DRUG_SP",newOnto,"ORG_DN_CHN","来源-药品规格",null);
			setCompareOsynDesc(str,oldOnto,"ORG_DRUG_STAD_SP",newOnto,"ORG_STAD_CHN","来源-药品标准规格",null);
			setCompareOsynDesc(str,oldOnto,"REMARK",newOnto,"DRUG_MEMO","备注",null);
			setCompareOsynDesc(str,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","停用标记",IS_DISABLE_MAP);
			setCompareOsynDesc(str,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","停用描述",null);
		}else if (OsynConst.DRUG_FORM_CODE.equals(ontoType)) {
			setCompareOsynDesc(str,oldOnto,"F_CHN_NAME",newOnto,"DN_CHN","药品剂型中文",null);
			setCompareOsynDesc(str,oldOnto,"F_ENG_NAME",newOnto,"DN_ENG","药品剂型英文",null);
			setCompareOsynDesc(str,oldOnto,"STAD_DOS_CHN",newOnto,"STAD_CHN","药品标准剂型",null);
			setCompareOsynDesc(str,oldOnto,"ORG_DOS_CHN",newOnto,"ORG_DN_CHN","来源-药品剂型中文",null);
			setCompareOsynDesc(str,oldOnto,"ORG_DOS_ENG",newOnto,"ORG_DN_ENG","来源-药品剂型英文",null);
			setCompareOsynDesc(str,oldOnto,"REMARK",newOnto,"DRUG_MEMO","备注",null);
			setCompareOsynDesc(str,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","停用标记",IS_DISABLE_MAP);
			setCompareOsynDesc(str,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","停用描述",null);
		}else if (OsynConst.DRUG_ROUTE_CODE.equals(ontoType)) {
			setCompareOsynDesc(str,oldOnto,"DR_CHN",newOnto,"DN_CHN","给药途径名称_中文",null);
			setCompareOsynDesc(str,oldOnto,"DR_ENG",newOnto,"DN_ENG","给药途径名称_英文",null);
			setCompareOsynDesc(str,oldOnto,"DR_ENG_ABB",newOnto,"FAC_ABB","给药途径名称_英文缩写",null);
			setCompareOsynDesc(str,oldOnto,"STAD_DR_CHN",newOnto,"STAD_CHN","药品标准给药途径",null);
			setCompareOsynDesc(str,oldOnto,"ORG_DR_CHN",newOnto,"ORG_DN_CHN","来源-给药途径名称_中文",null);
			setCompareOsynDesc(str,oldOnto,"ORG_DR_ENG",newOnto,"ORG_DN_ENG","来源-给药途径名称_英文",null);
			setCompareOsynDesc(str,oldOnto,"ORG_DR_ENG",newOnto,"ORG_DN_ENG","来源-给药途径名称_英文缩写",null);
			setCompareOsynDesc(str,oldOnto,"REMARK",newOnto,"DRUG_MEMO","备注",null);
			setCompareOsynDesc(str,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","停用标记",IS_DISABLE_MAP);
			setCompareOsynDesc(str,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","停用描述",null);
			setCompareOsynDesc(str,oldOnto,"CONCEPT",newOnto,"COUNTRY","概念",null);
			setCompareOsynDesc(str,oldOnto,"CONCEPT_ORG",newOnto,"DISTRICT","概念来源",null);
		}
		if(str.length()>5){
			newOnto.put("UPD_DESC",str.toString());
			return newOnto.getString("UPD_DESC");
		}else{
			return null;
		}
	}
	
	/*
	 * 比较更改前后的值，确认字段值是否修改
	 */
	private static void setCompareOsynDesc(StringBuffer str, PageData oldOnto,String oldKey,PageData newOnto, String key, String desc,Map<String,String> map) {
		//旧值
		Object a = oldOnto.get(oldKey);
		String s1 = null;
		if(a==null){
			s1 = "";
		}else if(a instanceof String){
			s1 = (String)(a==null?"":a);
		}else if (a instanceof BigDecimal ) {
			s1 = (a==null?"":((BigDecimal)a).toString());
		}
		
		String s2 = newOnto.getString(key)==null?"":newOnto.getString(key);
		//对比
		if(!s1.equals(s2)){
			str.append(desc).append(":").append(map==null?s2:map.get(s2)).append(";");
		}
	}
	
	/**
	 * 判断修改的标准词内容是否为需要修改对应的同义词信息的内容(药品)
	 * @param ontoType
	 * @param oldOnto
	 * @param newOnto
	 * @return
	 */
	public static PageData compareUpd(String ontoType,PageData oldOnto,PageData newOnto){
		PageData pd = new PageData();
		if(OsynConst.DRUG_FACTORY_CODE.equals(ontoType)){//药品生产企业
			setCompareOsynPageData(pd,oldOnto,"FAC_CHN",newOnto,"DN_CHN","STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"FAC_ENG",newOnto,"DN_ENG","STAD_ENG");
			setCompareOsynPageData(pd,oldOnto,"FAC_ABB",newOnto,"FAC_ABB","STAD_FAC_ABB");
			setCompareOsynPageData(pd,oldOnto,"ORG_FAC_CHN",newOnto,"ORG_DN_CHN","ORG_STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"ORG_FAC_ENG",newOnto,"ORG_DN_ENG","ORG_STAD_ENG");
			setCompareOsynPageData(pd,oldOnto,"ORG_FAC_ABB",newOnto,"ORG_FAC_ABB","ORG_STAD_FAC_ABB");
			setCompareOsynPageData(pd,oldOnto,"COUNTRY",newOnto,"COUNTRY","COUNTRY");
			setCompareOsynPageData(pd,oldOnto,"AREA",newOnto,"DISTRICT","DISTRICT");
			setCompareOsynPageData(pd,oldOnto,"AREA_CODE",newOnto,"AREA_CODE","AREA_CODE");
			setCompareOsynPageData(pd,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","IS_DISABLE");
			setCompareOsynPageData(pd,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","DESCRIPTION");
		}else if (OsynConst.PACK_SPEC_UNIT_CODE.equals(oldOnto)) {//药品规格单位
			setCompareOsynPageData(pd,oldOnto,"PSU_CHN",newOnto,"DN_CHN","STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"PSU_ENG",newOnto,"DN_ENG","STAD_ENG");
			setCompareOsynPageData(pd,oldOnto,"ORG_PSU_CHN",newOnto,"ORG_DN_CHN","ORG_STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"ORG_PSU_ENG",newOnto,"ORG_DN_ENG","ORG_STAD_ENG");
			setCompareOsynPageData(pd,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","IS_DISABLE");
			setCompareOsynPageData(pd,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","DESCRIPTION");
		}else if (OsynConst.PACK_SPEC_CODE.equals(ontoType)) {//药品包装规格
			setCompareOsynPageData(pd,oldOnto,"PS_CHN",newOnto,"DN_CHN","STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"PS_ENG",newOnto,"DN_ENG","DN_ENG,STAD_ENG");
			setCompareOsynPageData(pd,oldOnto,"STAD_PS_VALUE_CHN",newOnto,"STAD_PACK_SPEC_CHN","STAD_PACK_SPEC_CHN");
			setCompareOsynPageData(pd,oldOnto,"STAD_PS_UNIT_CHN",newOnto,"STAD_PACK_SPEC_UNIT_CHN","STAD_PACK_SPEC_UNIT_CHN");
			setCompareOsynPageData(pd,oldOnto,"STAD_PS_VALUE_ENG",newOnto,"STAD_PACK_SPEC_ENG","STAD_PACK_SPEC_ENG");
			setCompareOsynPageData(pd,oldOnto,"STAD_PS_UNIT_ENG",newOnto,"STAD_PACK_SPEC_UNIT_ENG","STAD_PACK_SPEC_UNIT_ENG");
			setCompareOsynPageData(pd,oldOnto,"PSU_ID",newOnto,"PSU_ID","PSU_ID");
			setCompareOsynPageData(pd,oldOnto,"ORG_PS_CHN",newOnto,"ORG_DN_CHN","ORG_STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"ORG_PS_ENG",newOnto,"ORG_DN_ENG","ORG_STAD_ENG");
			setCompareOsynPageData(pd,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","IS_DISABLE");
			setCompareOsynPageData(pd,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","DESCRIPTION");
		}else if (OsynConst.DRUG_SPEC_CODE.equals(ontoType)) {//药品规格表
			setCompareOsynPageData(pd,oldOnto,"DRUG_SP",newOnto,"DN_CHN","STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"ORG_DRUG_SP",newOnto,"ORG_DN_CHN","ORG_STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","IS_DISABLE");
			setCompareOsynPageData(pd,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","DESCRIPTION");
		}else if (OsynConst.DRUG_PACK_CODE.equals(ontoType)){//药品包装材质
			setCompareOsynPageData(pd,oldOnto,"DP_CHN",newOnto,"DN_CHN","STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"DP_ENG",newOnto,"DN_ENG","STAD_ENG");
			setCompareOsynPageData(pd,oldOnto,"ORG_DP_CHN",newOnto,"ORG_DN_CHN","ORG_STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"ORG_DP_ENG",newOnto,"ORG_DN_ENG","ORG_STAD_ENG");
			setCompareOsynPageData(pd,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","IS_DISABLE");
			setCompareOsynPageData(pd,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","DESCRIPTION");
		}else if(OsynConst.DRUG_NAME_CHEMI_CODE.equals(ontoType)||OsynConst.DRUG_NAME_COMMON_CODE.equals(ontoType)||OsynConst.DRUG_NAME_GOODS_CODE.equals(ontoType)||OsynConst.DRUG_NAME_PRODUCT_CODE.equals(ontoType)){//药品名称
			setCompareOsynPageData(pd,oldOnto,"CHN_NAME",newOnto,"DN_CHN","STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"ENG_NAME",newOnto,"DN_ENG","STAD_ENG");
			setCompareOsynPageData(pd,oldOnto,"ORG_CHN_NAME",newOnto,"ORG_DN_CHN","ORG_STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"ORG_ENG_NAME",newOnto,"ORG_DN_ENG","ORG_STAD_ENG");
			setCompareOsynPageData(pd,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","IS_DISABLE");
			setCompareOsynPageData(pd,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","DESCRIPTION");
		}else if(OsynConst.DRUG_FORM_CODE.equals(ontoType)){//药品剂型
			setCompareOsynPageData(pd,oldOnto,"F_CHN_NAME",newOnto,"DN_CHN","STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"F_ENG_NAME",newOnto,"DN_ENG","STAD_ENG");
			setCompareOsynPageData(pd,oldOnto,"ORG_DOS_CHN",newOnto,"ORG_DN_CHN","ORG_STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"ORG_DOS_ENG",newOnto,"ORG_DN_ENG","ORG_STAD_ENG");
			setCompareOsynPageData(pd,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","IS_DISABLE");
			setCompareOsynPageData(pd,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","DESCRIPTION");
		}else if (OsynConst.DRUG_ROUTE_CODE.equals(ontoType)) {
			setCompareOsynPageData(pd,oldOnto,"DR_CHN",newOnto,"DN_CHN","STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"DR_ENG",newOnto,"DN_ENG","STAD_ENG");
			setCompareOsynPageData(pd,oldOnto,"ORG_DR_CHN",newOnto,"ORG_DN_CHN","ORG_STAD_CHN");
			setCompareOsynPageData(pd,oldOnto,"ORG_DR_ENG",newOnto,"ORG_DN_ENG","ORG_STAD_ENG");
			setCompareOsynPageData(pd,oldOnto,"IS_DISABLE",newOnto,"IS_DISABLE","IS_DISABLE");
			setCompareOsynPageData(pd,oldOnto,"DESCRIPTION",newOnto,"DESCRIPTION","DESCRIPTION");
		}
		
		return pd;
	}
	/*
	 * 比较更改前后的值，确认字段值是否修改
	 */
	private static void setCompareOsynPageData(PageData pd, PageData oldOnto,String oldKey,PageData newOnto, String key, String desc) {
		//旧值
		Object a = oldOnto.get(oldKey);
		String s1 = null;
		if(a==null){
			s1 = "";
		}else if(a instanceof String){
			s1 = (String)(a==null?"":a);
		}else if (a instanceof BigDecimal ) {
			s1 = (a==null?"":((BigDecimal)a).toString());
		}
		//新值
		Object b = newOnto.get(key);
		String s2 = null;
		if(b==null){
			s2 = "";
		}else if(b instanceof String){
			s2 = (String)(b==null?"":b);
		}else if (b instanceof BigDecimal ) {
			s2 = (b==null?"":((BigDecimal)b).toString());
		}
		//String s2 = newOnto.getString(key)==null?"":newOnto.getString(key);
		//对比
		if(!s1.equals(s2)){
			pd.put(desc,s2);
		}
	}
	/**
	 * 获取同义词新增历史信息项
	 * @param oldOnto
	 * @param newOnto
	 * @param ontoType
	 * @return
	 */
	public static PageData insertOsynHisOption(PageData oldOnto,PageData newOnto,String ontoType){
		PageData pd = new PageData();
		if ("10801".equals(ontoType)) {//药品生产企业
			pd.put("DN_ID",oldOnto.get("FAC_ID"));
			pd.put("DN_CHN",oldOnto.get("FAC_CHN"));
			pd.put("DN_ENG",oldOnto.get("FAC_ENG"));
			pd.put("FAC_ABB",oldOnto.get("FAC_ABB"));
			pd.put("STAD_DN_ID",oldOnto.get("STAD_FAC_ID"));
			pd.put("ORG_DN_CHN",oldOnto.get("ORG_FAC_CHN"));
			pd.put("ORG_DN_ENG",oldOnto.get("ORG_FAC_ENG"));
			pd.put("ORG_FAC_ABB",oldOnto.get("ORG_FAC_ABB"));
			pd.put("DRUG_MEMO",oldOnto.get("REMARK"));
			pd.put("ONTO_TYPE",newOnto.get("ONTO_TYPE"));
			pd.put("STAD_CHN",newOnto.get("DN_CHN"));
			pd.put("STAD_ENG",newOnto.get("DN_ENG"));
			pd.put("STAD_FAC_ABB",newOnto.get("FAC_ABB"));
			pd.put("ORG_STAD_CHN",newOnto.get("ORG_DN_CHN"));
			pd.put("ORG_STAD_ENG",newOnto.get("ORG_DN_ENG"));
			pd.put("ORG_STAD_FAC_ABB",newOnto.get("ORG_FAC_ABB"));
			pd.put("COUNTRY",newOnto.get("COUNTRY"));
			pd.put("DISTRICT",newOnto.get("DISTRICT"));
			pd.put("AREA_CODE",newOnto.get("AREA_CODE"));
			pd.put("VERSION",newOnto.get("VERSION"));
		}else if ("10202".equals(ontoType)) {//药品包装规格
			pd.put("DN_ID",oldOnto.get("PS_ID"));
			pd.put("DN_CHN",oldOnto.get("PS_CHN"));
			pd.put("DN_ENG",oldOnto.get("PS_ENG"));
			pd.put("STAD_DN_ID",oldOnto.get("STAD_PS_ID"));
			pd.put("ORG_DN_CHN",oldOnto.get("ORG_PS_CHN"));
			pd.put("ORG_DN_ENG",oldOnto.get("ORG_PS_ENG"));
			pd.put("DRUG_MEMO",oldOnto.get("REMARK"));
			pd.put("ONTO_TYPE",newOnto.get("ONTO_TYPE"));
			pd.put("STAD_CHN",newOnto.get("DN_CHN"));
			pd.put("STAD_ENG",newOnto.get("DN_ENG"));
			pd.put("ORG_STAD_CHN",newOnto.get("ORG_DN_CHN"));
			pd.put("ORG_STAD_ENG",newOnto.get("ORG_DN_ENG"));
			pd.put("PSU_ID",newOnto.get("PSU_ID"));
			pd.put("STAD_PACK_SPEC_UNIT_CHN",newOnto.get("STAD_PS_UNIT_CHN"));
			pd.put("STAD_PACK_SPEC_UNIT_ENG",newOnto.get("STAD_PS_VALUE_ENG"));
			pd.put("STAD_PACK_SPEC_CHN",newOnto.get("STAD_PS_VALUE_CHN"));
			pd.put("STAD_PACK_SPEC_ENG",newOnto.get("STAD_PS_VALUE_ENG"));
			pd.put("VERSION",newOnto.get("VERSION"));
		}else if ("10204".equals(ontoType)) {//药品规格表
			pd.put("DN_ID",oldOnto.get("SP_ID"));
			pd.put("DN_CHN",oldOnto.get("DRUG_SP"));
			pd.put("ORG_DN_CHN",oldOnto.get("ORG_DRUG_SP"));
			pd.put("STAD_DN_ID",oldOnto.get("DRUG_STAD_ID"));
			pd.put("DRUG_MEMO",oldOnto.get("REMARK"));
			pd.put("ONTO_TYPE",newOnto.get("ONTO_TYPE"));
			pd.put("STAD_CHN",newOnto.get("DN_CHN"));
			pd.put("ORG_STAD_CHN",newOnto.get("ORG_DN_CHN"));
			pd.put("VERSION",newOnto.get("VERSION"));
		}else if ("10205".equals(ontoType)) {//药品包装材质
			pd.put("DN_ID",oldOnto.get("DP_ID"));
			pd.put("DN_CHN",oldOnto.get("DP_CHN"));
			pd.put("DN_ENG",oldOnto.get("DP_ENG"));
			pd.put("STAD_DN_ID",oldOnto.get("STAD_DP_ID"));
			pd.put("ORG_DN_CHN",oldOnto.get("ORG_DP_CHN"));
			pd.put("ORG_DN_ENG",oldOnto.get("ORG_DP_ENG"));
			pd.put("DRUG_MEMO",oldOnto.get("REMARK"));
			pd.put("ONTO_TYPE",newOnto.get("ONTO_TYPE"));
			pd.put("STAD_CHN",newOnto.get("DN_CHN"));
			pd.put("STAD_ENG",newOnto.get("DN_ENG"));
			pd.put("ORG_STAD_CHN",newOnto.get("ORG_DN_CHN"));
			pd.put("ORG_STAD_ENG",newOnto.get("ORG_DN_ENG"));
			pd.put("VERSION",newOnto.get("VERSION"));
		}else if ("10206".equals(ontoType)) {//药品规格单位
			pd.put("DN_ID",oldOnto.get("PSU_ID"));
			pd.put("DN_CHN",oldOnto.get("PSU_CHN"));
			pd.put("DN_ENG",oldOnto.get("PSU_ENG"));
			pd.put("STAD_DN_ID",oldOnto.get("PSU_STAD_ID"));
			pd.put("ORG_DN_CHN",oldOnto.get("ORG_PSU_CHN"));
			pd.put("ORG_DN_ENG",oldOnto.get("ORG_PSU_ENG"));
			pd.put("DRUG_MEMO",oldOnto.get("REMARK"));
			pd.put("ONTO_TYPE",newOnto.get("ONTO_TYPE"));
			pd.put("STAD_CHN",newOnto.get("DN_CHN"));
			pd.put("STAD_ENG",newOnto.get("DN_ENG"));
			pd.put("ORG_STAD_CHN",newOnto.get("ORG_DN_CHN"));
			pd.put("ORG_STAD_ENG",newOnto.get("ORG_DN_ENG"));
			pd.put("VERSION",newOnto.get("VERSION"));
		} else{//药品名称
			pd.put("DN_ID",oldOnto.get("DN_ID"));
			pd.put("DN_CHN",oldOnto.get("CHN_NAME"));
			pd.put("DN_ENG",oldOnto.get("ENG_NAME"));
			pd.put("STAD_DN_ID",oldOnto.get("STAD_NAME_ID"));
			pd.put("ORG_DN_CHN",oldOnto.get("ORG_CHN_NAME"));
			pd.put("ORG_DN_ENG",oldOnto.get("ORG_ENG_NAME"));
			pd.put("DRUG_MEMO",oldOnto.get("MEMO"));
			pd.put("ONTO_TYPE",ontoType);
			pd.put("STAD_CHN",newOnto.get("DN_CHN"));
			pd.put("STAD_ENG",newOnto.get("DN_ENG"));
			pd.put("ORG_STAD_CHN",newOnto.get("ORG_DN_CHN"));
			pd.put("ORG_STAD_ENG",newOnto.get("ORG_DN_ENG"));
		}
		pd.put("IS_DISABLE",newOnto.get("IS_DISABLE"));
		pd.put("DESCRIPTION",newOnto.get("DESCRIPTION"));
		pd.put("UPDATE_MAN",newOnto.get("UPDATE_MAN"));
		pd.put("UPDATE_TIME",newOnto.get("UPDATE_TIME"));
		return pd;
	}
	
	/**
	 * 根据权限设置同义词下拉框值
	 * @param user
	 * @param i
	 * @return
	 */
	public static Map<String, String> getOsyntypeMap(User user, int i) {
		Map<String,String> typeMap = new LinkedHashMap<String,String>();
		Integer roleMax = (Integer)Jurisdiction.getSession().getAttribute(user.getUSERNAME()+Const.SESSION_USER_MAX_ROLE);
		if(roleMax == 0){
//			if(i==1){
				typeMap.put(OsynConst.DIAG_NAME_CODE, "诊断名称同义词");
				typeMap.put(OsynConst.DEPT_NAME_CODE, "科室名称同义词");
				typeMap.put(OsynConst.OPERATION_NAME_CODE, "手术名称同义词");
//			}
			typeMap.put(OsynConst.DRUG_NAME_CHEMI_CODE, "药品化学名");
			typeMap.put(OsynConst.DRUG_NAME_COMMON_CODE, "药品通用名");
			typeMap.put(OsynConst.DRUG_NAME_GOODS_CODE, "药品商品名");
			typeMap.put(OsynConst.DRUG_NAME_PRODUCT_CODE, "药品产品名");
			typeMap.put(OsynConst.DRUG_FACTORY_CODE, "药品生产企业");
			typeMap.put(OsynConst.DRUG_PACK_CODE, "药品包装材质");
			typeMap.put(OsynConst.DRUG_SPEC_CODE, "药品规格");
			typeMap.put(OsynConst.PACK_SPEC_CODE, "药品包装规格");
			typeMap.put(OsynConst.PACK_SPEC_UNIT_CODE, "药品规格单位");
			typeMap.put(OsynConst.DRUG_FORM_CODE, "药品剂型");
			typeMap.put(OsynConst.DRUG_ROUTE_CODE, "药品给药途径");
		}else{
			if(i==0){
				//术语查询权限判断
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_QUERY_DIAG)){
					typeMap.put(OsynConst.DIAG_NAME_CODE, "诊断名称同义词");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_QUERY_DRUG_NAME_CHEMI)){
					typeMap.put(OsynConst.DRUG_NAME_CHEMI_CODE, "药品化学名");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_QUERY_DRUG_NAME_COMMON)){
					typeMap.put(OsynConst.DRUG_NAME_COMMON_CODE, "药品通用名");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_QUERY_DRUG_NAME_GOODS)){
					typeMap.put(OsynConst.DRUG_NAME_GOODS_CODE, "药品商品名");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_QUERY_DRUG_NAME_PRODUCT)){
					typeMap.put(OsynConst.DRUG_NAME_PRODUCT_CODE, "药品产品名");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_QUERY_DRUG_FACTORY)){
					typeMap.put(OsynConst.DRUG_FACTORY_CODE, "药品生产企业");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_QUERY_DRUG_PACK)){
					typeMap.put(OsynConst.DRUG_PACK_CODE, "药品包装材质");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_QUERY_DRUG_SPEC)){
					typeMap.put(OsynConst.DRUG_SPEC_CODE, "药品规格");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_QUERY_PACK_SPEC)){
					typeMap.put(OsynConst.PACK_SPEC_CODE, "药品包装规格");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_QUERY_PACK_SPEC_UNIT)){
					typeMap.put(OsynConst.PACK_SPEC_UNIT_CODE, "药品规格单位");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_QUERY_DRUG_FORM)){
					typeMap.put(OsynConst.DRUG_FORM_CODE, "药品剂型");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_QUERY_DRUG_ROUTE)){
					typeMap.put(OsynConst.DRUG_ROUTE_CODE, "药品给药途径");
				}
			}else if(i==1){
				//术语审核权限判断
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_CHECK_DIAG)){
					typeMap.put(OsynConst.DIAG_NAME_CODE, "诊断名称同义词");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_CHECK_OP)){
					typeMap.put(OsynConst.OPERATION_NAME_CODE, "手术名称同义词");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_CHECK_DEP)){
					typeMap.put(OsynConst.DEPT_NAME_CODE, "科室名称同义词");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_CHECK_DRUG_NAME_CHEMI)){
					typeMap.put(OsynConst.DRUG_NAME_CHEMI_CODE, "药品化学名");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_CHECK_DRUG_NAME_COMMON)){
					typeMap.put(OsynConst.DRUG_NAME_COMMON_CODE, "药品通用名");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_CHECK_DRUG_NAME_GOODS)){
					typeMap.put(OsynConst.DRUG_NAME_GOODS_CODE, "药品商品名");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_CHECK_DRUG_NAME_PRODUCT)){
					typeMap.put(OsynConst.DRUG_NAME_PRODUCT_CODE, "药品产品名");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_CHECK_DRUG_FACTORY)){
					typeMap.put(OsynConst.DRUG_FACTORY_CODE, "药品生产企业");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_CHECK_DRUG_PACK)){
					typeMap.put(OsynConst.DRUG_PACK_CODE, "药品包装材质");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_CHECK_DRUG_SPEC)){
					typeMap.put(OsynConst.DRUG_SPEC_CODE, "药品规格");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_CHECK_PACK_SPEC)){
					typeMap.put(OsynConst.PACK_SPEC_CODE, "药品包装规格");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_CHECK_PACK_SPEC_UNIT)){
					typeMap.put(OsynConst.PACK_SPEC_UNIT_CODE, "药品规格单位");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_CHECK_DRUG_FORM)){
					typeMap.put(OsynConst.DRUG_FORM_CODE, "药品剂型");
				}
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.OSYN_CHECK_DRUG_ROUTE)){
					typeMap.put(OsynConst.DRUG_ROUTE_CODE, "药品给药途径");
				}
			}else if(i==2){
				//术语历史权限判断
				if(Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_HIS_DRUG)){
					typeMap.put(OsynConst.DRUG, "药品");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_HIS_OP)){
					typeMap.put(OsynConst.OP, "手术");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_HIS_DIAG)){
					typeMap.put(OsynConst.DIAG, "诊断");
				}
				if (Jurisdiction.hasJurisdiction(user.getUSERNAME(), RightCode.ONTO_HIS_DEP)){
					typeMap.put(OsynConst.DEP, "科室");
				}
			}
		}
		return typeMap;
	}
	/**
	 * 获取标准词、同义词下拉框
	 * @param user
	 * @param ontoType
	 * @return
	 */
	public static Map<String, String> getWordtypeMap(User user,String ontoType) {
		Map<String,String> wordMap = new HashMap<String,String>();
		wordMap.put(OsynConst.OSYN_WORD,"同义词");
		if(!OsynConst.DIAG_NAME_CODE.equals(ontoType)&&!OsynConst.OPERATION_NAME_CODE.equals(ontoType)&&!OsynConst.DEPT_NAME_CODE.equals(ontoType)){
			wordMap.put(OsynConst.STAND_WORD,"标准词");
		}
		return wordMap;
	}
	
	public static String getOntoName(String ontoType) {
		String ontoName = "diag,DN_ID,D_ID";
		if (OsynConst.OP.equals(ontoType)) {
			ontoName = "op,ON_ID,OP_ID";
		}else if (OsynConst.DEP.equals(ontoType)) {
			ontoName = "dep,ID,ID";
		}else if (OsynConst.DIAG.equals(ontoType)) {
			ontoName = "diag,DN_ID,D_ID";
		}
		return ontoName;
	}
	/**
	 * 当excel文件中存在执行过DEL操作的空行时，不执行操作，需要判断该有效行是否为空行
	 * @param row
	 * @return
	 */
	public static boolean isBlankRow(Row row){
        if(row == null) return true;
        boolean result = true;
        for(int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++){
            Cell cell = row.getCell(i, Row.RETURN_BLANK_AS_NULL);
            String value = "";
            if(cell != null){
                switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    value = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    value = String.valueOf((int) cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    value = String.valueOf(cell.getCellFormula());
                    break;
                //case Cell.CELL_TYPE_BLANK:
                //    break;
                default:
                    break;
                }
                 
                if(!value.trim().equals("")){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
	
	public static String getTermType(String termType){
		String returnVal="";
		if("疾病".equals(termType)){
			returnVal="0";
		}else if ("症状".equals(termType)) {
			returnVal="1";
		}else if("语用同义词".equals(termType)){
			returnVal="23105";
		}else if ("专指同义词".equals(termType)) {
			returnVal="23106";
		}else if ("同音字/错别字".equals(termType)) {
			returnVal="23103";
		}else if ("拼音首字母".equals(termType)) {
			returnVal="23104";
		}else if ("俗称".equals(termType)) {
			returnVal="23101";
		}else if ("缩略语".equals(termType)) {
			returnVal="23102";
		}
		return returnVal;
				
	}
	
	public static List<String> getParentIds(List<PageData> pd_tree_his) {
		if(pd_tree_his==null) return null;
		List<String> pids = new ArrayList<String>();
		for(PageData pd:pd_tree_his){
			if(pd.get("PARENT_ID")!=null){
				pids.add(((BigDecimal)pd.get("PARENT_ID")).toString());
			}
		}
		return pids;
	}
	//获取身体部位列表，用于诊断中的"部位分类","人群分类"下拉框显示
	public static Map<String,String> getPartBodyMap(int i){
		Map<String,String> map = new HashMap<String,String>();
		if(i==0){//部位分类
			map.put(OsynConst.PART_HEAD,"头部");
			map.put(OsynConst.PART_NECK,"颈部");
			map.put(OsynConst.PART_CHEST,"胸部");
			map.put(OsynConst.PART_STOMACH,"腹部");
			map.put(OsynConst.PART_WAIST,"腰部");
			map.put(OsynConst.PART_BACK,"背部");
			map.put(OsynConst.PART_HIPS,"臀部");
			map.put(OsynConst.PART_PELVIC,"盆腔");
			map.put(OsynConst.PART_UPPER_LIMB,"上肢");
			map.put(OsynConst.PART_LEGS,"下肢");
			map.put(OsynConst.PART_SKIN,"皮肤");
			map.put(OsynConst.PART_HANDS,"手");
			map.put(OsynConst.PART_FOOTS,"足");
			map.put(OsynConst.PART_EYES,"眼");
			map.put(OsynConst.PART_NOSE,"鼻");
			map.put(OsynConst.PART_MOUTH,"口");
			map.put(OsynConst.PART_EARS,"耳");
		}else if (i==1) {
			map.put(OsynConst.MALE,"男性");
			map.put(OsynConst.FEMALE, "女性");
		}else if (i==2) {
			map.put(OsynConst.MALE,"传染病");
			map.put(OsynConst.FEMALE, "非传染病");
		}
		return map;
	}

	/**
	 * 设置默认查询时间
	 * @param pd
	 */
	public static void setDefaultDate(PageData pd) {
//		String beginDate = pd.getString("beginDate");
//		String endDate = pd.getString("endDate"); 
//		if(Tools.isEmpty(beginDate)&&Tools.isEmpty(endDate)){
//			Calendar calendar= Calendar.getInstance();
//			int year = calendar.get(Calendar.YEAR);
//			pd.put("beginDate", year+"-01-01");
//			pd.put("endDate", year+"-12-31");
//		}
	}
}
