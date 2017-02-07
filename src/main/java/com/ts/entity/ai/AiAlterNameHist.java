package com.ts.entity.ai;

import java.io.Serializable;
import java.util.Date;

import com.ts.entity.Page;

/**
 * 
* 类名称：用户
* 类描述： 
* @author 
* 作者单位： 
* 联系方式：
* 创建时间：2014年6月28日
* @version 1.0
 */
public class AiAlterNameHist implements Serializable{
	
	
	private static final long serialVersionUID = -1655344666885728275L;
	private String H_ID;		//历史ID
	private Integer DN_ID;		//名称ID（药品产品名通用名等、手术、诊断）
	private String DN_CHN;		//名称_中文、药品规格
	private String DN_ENG;		//名称_英文
	private String FAC_ABB;		//生产企业名称_简称
	private String STAD_CHN;	//标准名称_中文（药品的使用、药品规格）
	private String STAD_END;	//标准名称_英文（药品的使用）
	private String STAD_FAC_ABB;	//标准生产企业名称_简称
	private Integer STAD_DN_ID;		//标准名称ID（标准诊断ID、标准企业ID、标准通用名ID等等）
	private String TERM_TYPE;		//术语类型（诊断）、词语类型（药品的使用）
	private String SYNO_TYPE;		//同义词类型（诊断手术使用）
	private String ORG_DN_CHN;		//来源-名称_中文
	private String ORG_DN_ENG;		//来源-名称_英文
	private String ORG_STAD_CHN;		//来源-标准名称_中文（药品的使用）
	private String ORG_STAD_END;		//来源-标准名称_英文（药品的使用）
	private Integer IS_DISABLE;		//停用标记 ：1停用，0未停用
	private String DESCRIPTION;		//停用描述
	private String VERSION;		//版本
	private String UPDATE_MAN;		//更新人
	private String UPDATE_TIME;		//更新时间
	private String OPERATION;		//操作
	private String CHECK_USER;		//审核人
	private Date CHECK_TIME;		//审核时间
	private String CHECK_MEMO;		//审批备注
	private String OP_TYPE;		//操作类型
	private String STATUS;		//状态
	private String UPD_DESC;		//变更描述
	private String DIAG_ID;		//原标准诊断名称ID
	private Integer ONTO_TYPE;		//本体类型：来源字典表
	private String DRUG_MEMO;		//备注（药品的使用）
	private String ORG_FAC_ABB;		//来源-生产企业名称_简称
	private String ORG_STAD_FAC_ABB;		//来源-药品标准生产企业名称_简称
	private String COUNTRY;		//国家（药品生产企业）
	private String DISTRICT;		//地区（药品生产企业）
	private String AREA_CODE;		//区号（药品生产企业）
	private String MEMO;		//备注
	private String STAD_PACK_SPEC_CHN;		//药品标准包装规格数值_中文
	private String STAD_PACK_SPEC_ENG;		//药品标准包装规格数值_英文
	private String STAD_PACK_SPEC_UNIT_CHN;		//药品标准包装规格单位_中文
	private String STAD_PACK_SPEC_UNIT_ENG;		//药品标准包装规格单位_英文
	
	
	
	public String getH_ID() {
		return H_ID;
	}
	public void setH_ID(String h_ID) {
		H_ID = h_ID;
	}
	public Integer getDN_ID() {
		return DN_ID;
	}
	public void setDN_ID(Integer dN_ID) {
		DN_ID = dN_ID;
	}
	public String getDN_CHN() {
		return DN_CHN;
	}
	public void setDN_CHN(String dN_CHN) {
		DN_CHN = dN_CHN;
	}
	public String getORG_DN_ENG() {
		return ORG_DN_ENG;
	}
	public void setORG_DN_ENG(String oRG_DN_ENG) {
		ORG_DN_ENG = oRG_DN_ENG;
	}
	public String getDN_ENG() {
		return DN_ENG;
	}
	public void setDN_ENG(String dN_ENG) {
		DN_ENG = dN_ENG;
	}
	public String getFAC_ABB() {
		return FAC_ABB;
	}
	public void setFAC_ABB(String fAC_ABB) {
		FAC_ABB = fAC_ABB;
	}
	public String getSTAD_CHN() {
		return STAD_CHN;
	}
	public void setSTAD_CHN(String sTAD_CHN) {
		STAD_CHN = sTAD_CHN;
	}
	public String getSTAD_END() {
		return STAD_END;
	}
	public void setSTAD_END(String sTAD_END) {
		STAD_END = sTAD_END;
	}
	public String getSTAD_FAC_ABB() {
		return STAD_FAC_ABB;
	}
	public void setSTAD_FAC_ABB(String sTAD_FAC_ABB) {
		STAD_FAC_ABB = sTAD_FAC_ABB;
	}
	public Integer getSTAD_DN_ID() {
		return STAD_DN_ID;
	}
	public void setSTAD_DN_ID(Integer sTAD_DN_ID) {
		STAD_DN_ID = sTAD_DN_ID;
	}
	public String getTERM_TYPE() {
		return TERM_TYPE;
	}
	public void setTERM_TYPE(String tERM_TYPE) {
		TERM_TYPE = tERM_TYPE;
	}
	public String getSYNO_TYPE() {
		return SYNO_TYPE;
	}
	public void setSYNO_TYPE(String sYNO_TYPE) {
		SYNO_TYPE = sYNO_TYPE;
	}
	public String getORG_DN_CHN() {
		return ORG_DN_CHN;
	}
	public void setORG_DN_CHN(String oRG_DN_CHN) {
		ORG_DN_CHN = oRG_DN_CHN;
	}
	public String getORG_STAD_CHN() {
		return ORG_STAD_CHN;
	}
	public void setORG_STAD_CHN(String oRG_STAD_CHN) {
		ORG_STAD_CHN = oRG_STAD_CHN;
	}
	public String getORG_STAD_END() {
		return ORG_STAD_END;
	}
	public void setORG_STAD_END(String oRG_STAD_END) {
		ORG_STAD_END = oRG_STAD_END;
	}
	public Integer getIS_DISABLE() {
		return IS_DISABLE;
	}
	public void setIS_DISABLE(Integer iS_DISABLE) {
		IS_DISABLE = iS_DISABLE;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public String getVERSION() {
		return VERSION;
	}
	public void setVERSION(String vERSION) {
		VERSION = vERSION;
	}
	public String getUPDATE_MAN() {
		return UPDATE_MAN;
	}
	public void setUPDATE_MAN(String uPDATE_MAN) {
		UPDATE_MAN = uPDATE_MAN;
	}
	public String getUPDATE_TIME() {
		return UPDATE_TIME;
	}
	public void setUPDATE_TIME(String uPDATE_TIME) {
		UPDATE_TIME = uPDATE_TIME;
	}
	public String getOPERATION() {
		return OPERATION;
	}
	public void setOPERATION(String oPERATION) {
		OPERATION = oPERATION;
	}
	public String getCHECK_USER() {
		return CHECK_USER;
	}
	public void setCHECK_USER(String cHECK_USER) {
		CHECK_USER = cHECK_USER;
	}
	public Date getCHECK_TIME() {
		return CHECK_TIME;
	}
	public void setCHECK_TIME(Date cHECK_TIME) {
		CHECK_TIME = cHECK_TIME;
	}
	public String getCHECK_MEMO() {
		return CHECK_MEMO;
	}
	public void setCHECK_MEMO(String cHECK_MEMO) {
		CHECK_MEMO = cHECK_MEMO;
	}
	public String getOP_TYPE() {
		return OP_TYPE;
	}
	public void setOP_TYPE(String oP_TYPE) {
		OP_TYPE = oP_TYPE;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getUPD_DESC() {
		return UPD_DESC;
	}
	public void setUPD_DESC(String uPD_DESC) {
		UPD_DESC = uPD_DESC;
	}
	public String getDIAG_ID() {
		return DIAG_ID;
	}
	public void setDIAG_ID(String dIAG_ID) {
		DIAG_ID = dIAG_ID;
	}
	public Integer getONTO_TYPE() {
		return ONTO_TYPE;
	}
	public void setONTO_TYPE(Integer oNTO_TYPE) {
		ONTO_TYPE = oNTO_TYPE;
	}
	public String getDRUG_MEMO() {
		return DRUG_MEMO;
	}
	public void setDRUG_MEMO(String dRUG_MEMO) {
		DRUG_MEMO = dRUG_MEMO;
	}
	public String getORG_FAC_ABB() {
		return ORG_FAC_ABB;
	}
	public void setORG_FAC_ABB(String oRG_FAC_ABB) {
		ORG_FAC_ABB = oRG_FAC_ABB;
	}
	public String getORG_STAD_FAC_ABB() {
		return ORG_STAD_FAC_ABB;
	}
	public void setORG_STAD_FAC_ABB(String oRG_STAD_FAC_ABB) {
		ORG_STAD_FAC_ABB = oRG_STAD_FAC_ABB;
	}
	public String getCOUNTRY() {
		return COUNTRY;
	}
	public void setCOUNTRY(String cOUNTRY) {
		COUNTRY = cOUNTRY;
	}
	public String getDISTRICT() {
		return DISTRICT;
	}
	public void setDISTRICT(String dISTRICT) {
		DISTRICT = dISTRICT;
	}
	public String getAREA_CODE() {
		return AREA_CODE;
	}
	public void setAREA_CODE(String aREA_CODE) {
		AREA_CODE = aREA_CODE;
	}
	public String getMEMO() {
		return MEMO;
	}
	public void setMEMO(String mEMO) {
		MEMO = mEMO;
	}
	public String getSTAD_PACK_SPEC_CHN() {
		return STAD_PACK_SPEC_CHN;
	}
	public void setSTAD_PACK_SPEC_CHN(String sTAD_PACK_SPEC_CHN) {
		STAD_PACK_SPEC_CHN = sTAD_PACK_SPEC_CHN;
	}
	public String getSTAD_PACK_SPEC_ENG() {
		return STAD_PACK_SPEC_ENG;
	}
	public void setSTAD_PACK_SPEC_ENG(String sTAD_PACK_SPEC_ENG) {
		STAD_PACK_SPEC_ENG = sTAD_PACK_SPEC_ENG;
	}
	public String getSTAD_PACK_SPEC_UNIT_CHN() {
		return STAD_PACK_SPEC_UNIT_CHN;
	}
	public void setSTAD_PACK_SPEC_UNIT_CHN(String sTAD_PACK_SPEC_UNIT_CHN) {
		STAD_PACK_SPEC_UNIT_CHN = sTAD_PACK_SPEC_UNIT_CHN;
	}
	public String getSTAD_PACK_SPEC_UNIT_ENG() {
		return STAD_PACK_SPEC_UNIT_ENG;
	}
	public void setSTAD_PACK_SPEC_UNIT_ENG(String sTAD_PACK_SPEC_UNIT_ENG) {
		STAD_PACK_SPEC_UNIT_ENG = sTAD_PACK_SPEC_UNIT_ENG;
	}
	
}
