package com.hitzd.his.Beans;

import com.hitzd.his.Beans.TBaseBean;

/**
 * 预防用药信息记录
 * 
 * @author Administrator
 * 
 */
public class TPreveUseDrug extends TBaseBean
{
    private static final long serialVersionUID = -1971327703516467313L;

    /* 预防用药id */
    private String            YF_ID;
    /* 病人id号(住院号) */
    private String            PATIENT_ID;
    /* 出院次数 */
    private String            VISIT_ID;
    /* 医嘱主序号 */
    private String 			  order_no;
    /* 医嘱子序号 */
    private String  		  order_sub_no;
    /* 科室代码 */
    private String            DEPT_CODE;
    /* 科室名称 */
    private String            DEPT_NAME;
    /* 医生名称 */
    private String            DOCTOR_NAME;
    /* 姓名 */
    private String            NAME;
    /* 性别 */
    private String            SEX;
    /* 年龄 */
    private String            AGE;
    /* 体重 */
    private String            WEIGHT;
    /* 本地药品码 */
    private String            DRUG_CODE;
    /* 本地药品名称 */
    private String            DRUG_NAME;
    /* 最小剂量 */
    private String            DOSAGE;
    /* 最小单位 */
    private String            DOSAGE_UNITS;
    /* 用药途径 */
    private String            ADMINISTRATION;
    /* 手术时间 */
    private String            OPERTOR_DATE;
    /* 手术名称 */
    private String            OPERTOR_NAME;
    /* 手术类型 */
    private String            OPERTOR_TYPE;
    /* 手术时长 */
    private String            OPERTOR_USE_TIME;
    /* 使用时间点 */
    private String            DRUG_USE_TIME;
    /* 过敏史 */
    private String            GMS;
    /* β过敏史 */
    private String            BTGMS;
    /* 危险因素 */
    private String            WXYS;
    /* 可能的治病菌 */
    private String            KNDZBJ;
    /* 克林剂量氨南剂量 */
    private String            KLJLANJL;
    /* 预防使用抗菌药物的依据 */
    private String            YF_USE_DRUG_YJ;
    /* 特殊要求 */
    private String            TSYQ;
    /* 记录时间 */
    private String REC_DATE;
    /* 指明审核数据的来源，1，门诊事实 2，门诊处方点评，3，临床医嘱事实 4，临床医嘱点评  */
    private String  patType;
    
    public String getREC_DATE()
    {
        return REC_DATE;
    }

    public void setREC_DATE(String rEC_DATE)
    {
        REC_DATE = rEC_DATE;
    }

    public String getYF_ID()
    {
        return YF_ID;
    }

    public void setYF_ID(String yF_ID)
    {
        YF_ID = yF_ID;
    }

    public String getPATIENT_ID()
    {
        return PATIENT_ID;
    }

    public void setPATIENT_ID(String pATIENT_ID)
    {
        PATIENT_ID = pATIENT_ID;
    }

    public String getVISIT_ID()
    {
        return VISIT_ID;
    }

    public void setVISIT_ID(String vISIT_ID)
    {
        VISIT_ID = vISIT_ID;
    }

    public String getDEPT_CODE()
    {
        return DEPT_CODE;
    }

    public void setDEPT_CODE(String dEPT_CODE)
    {
        DEPT_CODE = dEPT_CODE;
    }

    public String getDEPT_NAME()
    {
        return DEPT_NAME;
    }

    public void setDEPT_NAME(String dEPT_NAME)
    {
        DEPT_NAME = dEPT_NAME;
    }

    public String getDOCTOR_NAME()
    {
        return DOCTOR_NAME;
    }

    public void setDOCTOR_NAME(String dOCTOR_NAME)
    {
        DOCTOR_NAME = dOCTOR_NAME;
    }

    public String getNAME()
    {
        return NAME;
    }

    public void setNAME(String nAME)
    {
        NAME = nAME;
    }

    public String getSEX()
    {
        return SEX;
    }

    public void setSEX(String sEX)
    {
        SEX = sEX;
    }

    public String getAGE()
    {
        return AGE;
    }

    public void setAGE(String aGE)
    {
        AGE = aGE;
    }

    public String getWEIGHT()
    {
        return WEIGHT;
    }

    public void setWEIGHT(String wEIGHT)
    {
        WEIGHT = wEIGHT;
    }

    public String getDRUG_CODE()
    {
        return DRUG_CODE;
    }

    public void setDRUG_CODE(String dRUG_CODE)
    {
        DRUG_CODE = dRUG_CODE;
    }

    public String getDRUG_NAME()
    {
        return DRUG_NAME;
    }

    public void setDRUG_NAME(String dRUG_NAME)
    {
        DRUG_NAME = dRUG_NAME;
    }

    public String getDOSAGE()
    {
        return DOSAGE;
    }

    public void setDOSAGE(String dOSAGE)
    {
        DOSAGE = dOSAGE;
    }

    public String getDOSAGE_UNITS()
    {
        return DOSAGE_UNITS;
    }

    public void setDOSAGE_UNITS(String dOSAGE_UNITS)
    {
        DOSAGE_UNITS = dOSAGE_UNITS;
    }

    public String getADMINISTRATION()
    {
        return ADMINISTRATION;
    }

    public void setADMINISTRATION(String aDMINISTRATION)
    {
        ADMINISTRATION = aDMINISTRATION;
    }

    public String getOPERTOR_DATE()
    {
        return OPERTOR_DATE;
    }

    public void setOPERTOR_DATE(String oPERTOR_DATE)
    {
        OPERTOR_DATE = oPERTOR_DATE;
    }

    public String getOPERTOR_NAME()
    {
        return OPERTOR_NAME;
    }

    public void setOPERTOR_NAME(String oPERTOR_NAME)
    {
        OPERTOR_NAME = oPERTOR_NAME;
    }

    public String getOPERTOR_TYPE()
    {
        return OPERTOR_TYPE;
    }

    public void setOPERTOR_TYPE(String oPERTOR_TYPE)
    {
        OPERTOR_TYPE = oPERTOR_TYPE;
    }

    public String getOPERTOR_USE_TIME()
    {
        return OPERTOR_USE_TIME;
    }

    public void setOPERTOR_USE_TIME(String oPERTOR_USE_TIME)
    {
        OPERTOR_USE_TIME = oPERTOR_USE_TIME;
    }

    public String getDRUG_USE_TIME()
    {
        return DRUG_USE_TIME;
    }

    public void setDRUG_USE_TIME(String dRUG_USE_TIME)
    {
        DRUG_USE_TIME = dRUG_USE_TIME;
    }

    public String getGMS()
    {
        return GMS;
    }

    public void setGMS(String gMS)
    {
        GMS = gMS;
    }

    public String getBTGMS()
    {
        return BTGMS;
    }

    public void setBTGMS(String bTGMS)
    {
        BTGMS = bTGMS;
    }

    public String getWXYS()
    {
        return WXYS;
    }

    public void setWXYS(String wXYS)
    {
        WXYS = wXYS;
    }

    public String getKNDZBJ()
    {
        return KNDZBJ;
    }

    public void setKNDZBJ(String kNDZBJ)
    {
        KNDZBJ = kNDZBJ;
    }

    public String getKLJLANJL()
    {
        return KLJLANJL;
    }

    public void setKLJLANJL(String kLJLANJL)
    {
        KLJLANJL = kLJLANJL;
    }

    public String getYF_USE_DRUG_YJ()
    {
        return YF_USE_DRUG_YJ;
    }

    public void setYF_USE_DRUG_YJ(String yF_USE_DRUG_YJ)
    {
        YF_USE_DRUG_YJ = yF_USE_DRUG_YJ;
    }

    public String getTSYQ()
    {
        return TSYQ;
    }

    public void setTSYQ(String tSYQ)
    {
        TSYQ = tSYQ;
    }

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getOrder_sub_no() {
		return order_sub_no;
	}

	public void setOrder_sub_no(String order_sub_no) {
		this.order_sub_no = order_sub_no;
	}

    public String getPatType()
    {
        return patType;
    }

    public void setPatType(String patType)
    {
        this.patType = patType;
    }
}
