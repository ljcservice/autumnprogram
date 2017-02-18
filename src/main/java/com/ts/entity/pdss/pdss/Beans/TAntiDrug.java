package com.ts.entity.pdss.pdss.Beans;

/**
 * 抗菌药物
 * @author Administrator
 *
 */
public class TAntiDrug
{
	/*序号*/
	private String SERIAL_NO;     
	/* */
	private String ITEM_CLASS;        
	/* 药品代码 */
	private String DRUG_NO_LOCAL;   
	/* 本地药名称 */
	private String DRUG_NAME_LOCAL;
	/* 规格 */
	private String ITEM_SPEC;    
	/* 单位 */
	private String UNITS;
	/* 单位剂量 */
	private String DOSE_PER_UNIT;     
	/* 剂量单位 */
	private String DOSE_UNITS;        
	/* DDD数 */
	private String STD_DOSAGE;        
	/*  */
	private String STD_INDICATOR;     
	/*  */
	private String INFECT_CLASS;      
	/*药品等级（A一级B二级C三级） */
	private String DRUG_CLASS;        
	/* */
	private String DRUG_SENS;         
	/* */
	private String DRUG_USE_CONDITION;
	/* 是否是特殊用药 */
	private String ISPRE;
	/* 天数限制 最大天数*/
	private String Max_Days;
	/* 天数限制 最小天数*/
	private String Min_Days;
	
	
	public String getDRUG_NAME_LOCAL() 
	{
		return DRUG_NAME_LOCAL;
	}

	public void setDRUG_NAME_LOCAL(String dRUG_NAME_LOCAL) 
	{
		DRUG_NAME_LOCAL = dRUG_NAME_LOCAL;
	}

	public String getMax_Days()
	{
		return Max_Days;
	}
	
	public void setMax_Days(String max_Days) 
	{
		Max_Days = max_Days;
	}
	
	public String getMin_Days() 
	{
		return Min_Days;
	}
	
	public void setMin_Days(String min_Days) 
	{
		Min_Days = min_Days;
	}
	public String getSERIAL_NO() 
	{
		return SERIAL_NO;
	}
	public void setSERIAL_NO(String sERIAL_NO)
	{
		SERIAL_NO = sERIAL_NO;
	}
	public String getITEM_CLASS() 
	{
		return ITEM_CLASS;
	}
	public void setITEM_CLASS(String iTEM_CLASS)
	{
		ITEM_CLASS = iTEM_CLASS;
	}
	public String getDRUG_NO_LOCAL()
	{
		return DRUG_NO_LOCAL;
	}
	public void setDRUG_NO_LOCAL(String dRUG_NO_LOCAL)
	{
		DRUG_NO_LOCAL = dRUG_NO_LOCAL;
	}
	public String getITEM_SPEC() 
	{
		return ITEM_SPEC;
	}
	public void setITEM_SPEC(String iTEM_SPEC)
	{
		ITEM_SPEC = iTEM_SPEC;
	}
	public String getUNITS()
	{
		return UNITS;
	}
	public void setUNITS(String uNITS)
	{
		UNITS = uNITS;
	}
	public String getDOSE_PER_UNIT() 
	{
		return DOSE_PER_UNIT;
	}
	public void setDOSE_PER_UNIT(String dOSE_PER_UNIT)
	{
		DOSE_PER_UNIT = dOSE_PER_UNIT;
	}
	public String getDOSE_UNITS() 
	{
		return DOSE_UNITS;
	}
	public void setDOSE_UNITS(String dOSE_UNITS)
	{
		DOSE_UNITS = dOSE_UNITS;
	}
	public String getSTD_DOSAGE() 
	{
		return STD_DOSAGE;
	}
	public void setSTD_DOSAGE(String sTD_DOSAGE)
	{
		STD_DOSAGE = sTD_DOSAGE;
	}
	public String getSTD_INDICATOR()
	{
		return STD_INDICATOR;
	}
	public void setSTD_INDICATOR(String sTD_INDICATOR)
	{
		STD_INDICATOR = sTD_INDICATOR;
	}
	public String getINFECT_CLASS()
	{
		return INFECT_CLASS;
	}
	public void setINFECT_CLASS(String iNFECT_CLASS)
	{
		INFECT_CLASS = iNFECT_CLASS;
	}
	public String getDRUG_CLASS()
	{
		return DRUG_CLASS;
	}
	public void setDRUG_CLASS(String dRUG_CLASS)
	{
		DRUG_CLASS = dRUG_CLASS;
	}
	public String getDRUG_SENS()
	{
		return DRUG_SENS;
	}
	public void setDRUG_SENS(String dRUG_SENS)
	{
		DRUG_SENS = dRUG_SENS;
	}
	public String getDRUG_USE_CONDITION() 
	{
		return DRUG_USE_CONDITION;
	}
	public void setDRUG_USE_CONDITION(String dRUG_USE_CONDITION)
	{
		DRUG_USE_CONDITION = dRUG_USE_CONDITION;
	}
	public String getISPRE()
	{
		return ISPRE;
	}
	public void setISPRE(String iSPRE) 
	{
		ISPRE = iSPRE;
	}    
}
