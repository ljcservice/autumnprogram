package com.ts.entity.pdss.pdss.Beans;

import com.hitzd.his.Beans.TBaseBean;



/**
 * @description 药品剂量使用字典类
 * @author 
 */
public class TDrugDosage extends TBaseBean  {
    
	private static final long serialVersionUID = 1L;
	/* 药品剂量使用字典ID */
	private String DRUG_DOSAGE_ID;
	/* 剂型码 */
	private String DOSE_CLASS_ID;
	/* 用药途径ID */
	private String ADMINISTRATION_ID;
	/* 剂型 */
	private String DRUG_FORM;
	/* 剂量单位 */
	private String DOSE_UNITS;
	/* 年龄标志 */
	private String AGE_INDI;
	/* 年龄下限 */
	private String AGE_LOW;
	/* 年龄上限 */
	private String AGE_HIGH;
	/* 体重标志 */
	private String WEIGHT_INDI;
	/* 体重下限 */
	private String WEIGHT_LOW;
	/* 体重上限 */
	private String WEIGHT_HIGH;
	/* 计算标志 */
	private String CAL_INDI;
	/* 每次最小剂量 */
	private String DOSE_EACH_LOW;
	/* 每次最高剂量 */
	private String DOSE_EACH_HIGH;
	/* 剂量单位  */
	private String DOSE_EACH_UNIT;
	/* 每天最小剂量 */
	private String DOSE_DAY_LOW;
	/* 每天最高剂量 */
	private String DOSE_DAY_HIGH;
	/* 每天剂量单位 */
	private String DOSE_DAY_UNIT;
	/* 每天最大剂量 */
	private String DOSE_MAX_HIGH;
	/* 最大剂量单位 */
	private String DOSE_MAX_UNIT;
	/* 每天最底频次 */
	private String DOSE_FREQ_LOW;
	/* 每天最高频次 */
	private String DOSE_FREQ_HIGH;
	/* 最底用药天数 */
	private String DUR_LOW;
	/* 最高用药天数 */
	private String DUR_HIGH;
	/* 特殊用药说明 */
	private String SPECIAL_DESC;
	/* 用药参考 */
	private String REFERENCE_INFO;
	
    public String getDRUG_DOSAGE_ID()
    {
        return DRUG_DOSAGE_ID;
    }
    public void setDRUG_DOSAGE_ID(String dRUG_DOSAGE_ID)
    {
        DRUG_DOSAGE_ID = dRUG_DOSAGE_ID;
    }
    public String getDOSE_CLASS_ID()
    {
        return DOSE_CLASS_ID;
    }
    public void setDOSE_CLASS_ID(String dOSE_CLASS_ID)
    {
        DOSE_CLASS_ID = dOSE_CLASS_ID;
    }
    public String getADMINISTRATION_ID()
    {
        return ADMINISTRATION_ID;
    }
    public void setADMINISTRATION_ID(String aDMINISTRATION_ID)
    {
        ADMINISTRATION_ID = aDMINISTRATION_ID;
    }
    public String getDRUG_FORM()
    {
        return DRUG_FORM;
    }
    public void setDRUG_FORM(String dRUG_FORM)
    {
        DRUG_FORM = dRUG_FORM;
    }
    public String getDOSE_UNITS()
    {
        return DOSE_UNITS;
    }
    public void setDOSE_UNITS(String dOSE_UNITS)
    {
        DOSE_UNITS = dOSE_UNITS;
    }
    public String getAGE_INDI()
    {
        return AGE_INDI;
    }
    public void setAGE_INDI(String aGE_INDI)
    {
        AGE_INDI = aGE_INDI;
    }
    public String getAGE_LOW()
    {
        return AGE_LOW;
    }
    public void setAGE_LOW(String aGE_LOW)
    {
        AGE_LOW = aGE_LOW;
    }
    public String getAGE_HIGH()
    {
        return AGE_HIGH;
    }
    public void setAGE_HIGH(String aGE_HIGH)
    {
        AGE_HIGH = aGE_HIGH;
    }
    public String getWEIGHT_INDI()
    {
        return WEIGHT_INDI;
    }
    public void setWEIGHT_INDI(String wEIGHT_INDI)
    {
        WEIGHT_INDI = wEIGHT_INDI;
    }
    public String getWEIGHT_LOW()
    {
        return WEIGHT_LOW;
    }
    public void setWEIGHT_LOW(String wEIGHT_LOW)
    {
        WEIGHT_LOW = wEIGHT_LOW;
    }
    public String getWEIGHT_HIGH()
    {
        return WEIGHT_HIGH;
    }
    public void setWEIGHT_HIGH(String wEIGHT_HIGH)
    {
        WEIGHT_HIGH = wEIGHT_HIGH;
    }
    public String getCAL_INDI()
    {
        return CAL_INDI;
    }
    public void setCAL_INDI(String cAL_INDI)
    {
        CAL_INDI = cAL_INDI;
    }
    public String getDOSE_EACH_LOW()
    {
        return DOSE_EACH_LOW;
    }
    public void setDOSE_EACH_LOW(String dOSE_EACH_LOW)
    {
        DOSE_EACH_LOW = dOSE_EACH_LOW;
    }
    public String getDOSE_EACH_HIGH()
    {
        return DOSE_EACH_HIGH;
    }
    public void setDOSE_EACH_HIGH(String dOSE_EACH_HIGH)
    {
        DOSE_EACH_HIGH = dOSE_EACH_HIGH;
    }
    public String getDOSE_EACH_UNIT()
    {
        return DOSE_EACH_UNIT;
    }
    public void setDOSE_EACH_UNIT(String dOSE_EACH_UNIT)
    {
        DOSE_EACH_UNIT = dOSE_EACH_UNIT;
    }
    public String getDOSE_DAY_LOW()
    {
        return DOSE_DAY_LOW;
    }
    public void setDOSE_DAY_LOW(String dOSE_DAY_LOW)
    {
        DOSE_DAY_LOW = dOSE_DAY_LOW;
    }
    public String getDOSE_DAY_HIGH()
    {
        return DOSE_DAY_HIGH;
    }
    public void setDOSE_DAY_HIGH(String dOSE_DAY_HIGH)
    {
        DOSE_DAY_HIGH = dOSE_DAY_HIGH;
    }
    public String getDOSE_DAY_UNIT()
    {
        return DOSE_DAY_UNIT;
    }
    public void setDOSE_DAY_UNIT(String dOSE_DAY_UNIT)
    {
        DOSE_DAY_UNIT = dOSE_DAY_UNIT;
    }
    public String getDOSE_MAX_HIGH()
    {
        return DOSE_MAX_HIGH;
    }
    public void setDOSE_MAX_HIGH(String dOSE_MAX_HIGH)
    {
        DOSE_MAX_HIGH = dOSE_MAX_HIGH;
    }
    public String getDOSE_MAX_UNIT()
    {
        return DOSE_MAX_UNIT;
    }
    public void setDOSE_MAX_UNIT(String dOSE_MAX_UNIT)
    {
        DOSE_MAX_UNIT = dOSE_MAX_UNIT;
    }
    public String getDOSE_FREQ_LOW()
    {
        return DOSE_FREQ_LOW;
    }
    public void setDOSE_FREQ_LOW(String dOSE_FREQ_LOW)
    {
        DOSE_FREQ_LOW = dOSE_FREQ_LOW;
    }
    public String getDOSE_FREQ_HIGH()
    {
        return DOSE_FREQ_HIGH;
    }
    public void setDOSE_FREQ_HIGH(String dOSE_FREQ_HIGH)
    {
        DOSE_FREQ_HIGH = dOSE_FREQ_HIGH;
    }
    public String getDUR_LOW()
    {
        return DUR_LOW;
    }
    public void setDUR_LOW(String dUR_LOW)
    {
        DUR_LOW = dUR_LOW;
    }
    public String getDUR_HIGH()
    {
        return DUR_HIGH;
    }
    public void setDUR_HIGH(String dUR_HIGH)
    {
        DUR_HIGH = dUR_HIGH;
    }
    public String getSPECIAL_DESC()
    {
        return SPECIAL_DESC;
    }
    public void setSPECIAL_DESC(String sPECIAL_DESC)
    {
        SPECIAL_DESC = sPECIAL_DESC;
    }
    public String getREFERENCE_INFO()
    {
        return REFERENCE_INFO;
    }
    public void setREFERENCE_INFO(String rEFERENCE_INFO)
    {
        REFERENCE_INFO = rEFERENCE_INFO;
    }
    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }
}