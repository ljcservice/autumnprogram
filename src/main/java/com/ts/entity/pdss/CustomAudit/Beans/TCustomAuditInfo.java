package com.ts.entity.pdss.CustomAudit.Beans;

import com.hitzd.his.Beans.TBaseBean;

/**
 * 用户自定义维护
 * 
 * @author Administrator
 * 
 */
public class TCustomAuditInfo extends TBaseBean
{
    private static final long serialVersionUID = 1L;
    /* */
    private String            CA_ID;
    /* 药品码1 */
    private String            DRUG_NO1;
    /* 药品码2 */
    private String            DRUG_NO2;
    /* 药品码3 */
    private String            DRUG_NO3;
    /* 药品码4 */
    private String            DRUG_NO4;
    /* 用药途径 */
    private String            ADMIN_ID;
    /* 诊断信息 */
    private String            DIAGNOSIS;
    /* 过敏信息 */
    private String            ALLERG;
    /* 不良反应信息 */
    private String            SIDE;
    /* 问题类型 */
    private String            EVENT_TYPE;
    /* 警示类型 */
    private String            EVENT_RES;
    /* 问题描述 */
    private String            EVENT_DESC;
    /* 文献来源 */
    private String            REF_INFO;
    /* 每次最小剂量 */
    private String            MIN_DOS_PER;
    /* 每次最大剂量 */
    private String            MAX_DOS_PER;
    /* 每天最小剂量 */
    private String            MIN_DOS_DAY;
    /* 每天最大剂量 */
    private String            MAX_DOS_DAY;
    /* 每天最小频次 */
    private String            MIN_FEQ_DAY;
    /* 每天最大频次 */
    private String            MAX_FEQ_DAY;
    /* 最小用药天数 */
    private String            MIN_DAYS;
    /* 最大用药天数 */
    private String            MAX_DAYS;
    /* 更新人 */
    private String            LAST_UPDATER;
    /* 更新时间 */
    private String            LAST_UPDATE_TIME;
    /* 校验人 */
    private String            LAST_CHECKER;
    /* 校验时间 */
    private String            LAST_CHECK_TIME;

    public String getCA_ID()
    {
        return CA_ID;
    }

    public void setCA_ID(String cA_ID)
    {
        CA_ID = cA_ID;
    }

    public String getDRUG_NO1()
    {
        return DRUG_NO1;
    }

    public void setDRUG_NO1(String dRUG_NO1)
    {
        DRUG_NO1 = dRUG_NO1;
    }

    public String getDRUG_NO2()
    {
        return DRUG_NO2;
    }

    public void setDRUG_NO2(String dRUG_NO2)
    {
        DRUG_NO2 = dRUG_NO2;
    }

    public String getDRUG_NO3()
    {
        return DRUG_NO3;
    }

    public void setDRUG_NO3(String dRUG_NO3)
    {
        DRUG_NO3 = dRUG_NO3;
    }

    public String getDRUG_NO4()
    {
        return DRUG_NO4;
    }

    public void setDRUG_NO4(String dRUG_NO4)
    {
        DRUG_NO4 = dRUG_NO4;
    }

    public String getADMIN_ID()
    {
        return ADMIN_ID;
    }

    public void setADMIN_ID(String aDMIN_ID)
    {
        ADMIN_ID = aDMIN_ID;
    }

    public String getDIAGNOSIS()
    {
        return DIAGNOSIS;
    }

    public void setDIAGNOSIS(String dIAGNOSIS)
    {
        DIAGNOSIS = dIAGNOSIS;
    }

    public String getALLERG()
    {
        return ALLERG;
    }

    public void setALLERG(String aLLERG)
    {
        ALLERG = aLLERG;
    }

    public String getSIDE()
    {
        return SIDE;
    }

    public void setSIDE(String sIDE)
    {
        SIDE = sIDE;
    }

    public String getEVENT_TYPE()
    {
        return EVENT_TYPE;
    }

    public void setEVENT_TYPE(String eVENT_TYPE)
    {
        EVENT_TYPE = eVENT_TYPE;
    }

    public String getEVENT_RES()
    {
        return EVENT_RES;
    }

    public void setEVENT_RES(String eVENT_RES)
    {
        EVENT_RES = eVENT_RES;
    }

    public String getEVENT_DESC()
    {
        return EVENT_DESC;
    }

    public void setEVENT_DESC(String eVENT_DESC)
    {
        EVENT_DESC = eVENT_DESC;
    }

    public String getREF_INFO()
    {
        return REF_INFO;
    }

    public void setREF_INFO(String rEF_INFO)
    {
        REF_INFO = rEF_INFO;
    }

    public String getMIN_DOS_PER()
    {
        return MIN_DOS_PER;
    }

    public void setMIN_DOS_PER(String mIN_DOS_PER)
    {
        MIN_DOS_PER = mIN_DOS_PER;
    }

    public String getMAX_DOS_PER()
    {
        return MAX_DOS_PER;
    }

    public void setMAX_DOS_PER(String mAX_DOS_PER)
    {
        MAX_DOS_PER = mAX_DOS_PER;
    }

    public String getMIN_DOS_DAY()
    {
        return MIN_DOS_DAY;
    }

    public void setMIN_DOS_DAY(String mIN_DOS_DAY)
    {
        MIN_DOS_DAY = mIN_DOS_DAY;
    }

    public String getMAX_DOS_DAY()
    {
        return MAX_DOS_DAY;
    }

    public void setMAX_DOS_DAY(String mAX_DOS_DAY)
    {
        MAX_DOS_DAY = mAX_DOS_DAY;
    }

    public String getMIN_FEQ_DAY()
    {
        return MIN_FEQ_DAY;
    }

    public void setMIN_FEQ_DAY(String mIN_FEQ_DAY)
    {
        MIN_FEQ_DAY = mIN_FEQ_DAY;
    }

    public String getMAX_FEQ_DAY()
    {
        return MAX_FEQ_DAY;
    }

    public void setMAX_FEQ_DAY(String mAX_FEQ_DAY)
    {
        MAX_FEQ_DAY = mAX_FEQ_DAY;
    }

    public String getMIN_DAYS()
    {
        return MIN_DAYS;
    }

    public void setMIN_DAYS(String mIN_DAYS)
    {
        MIN_DAYS = mIN_DAYS;
    }

    public String getMAX_DAYS()
    {
        return MAX_DAYS;
    }

    public void setMAX_DAYS(String mAX_DAYS)
    {
        MAX_DAYS = mAX_DAYS;
    }

    public String getLAST_UPDATER()
    {
        return LAST_UPDATER;
    }

    public void setLAST_UPDATER(String lAST_UPDATER)
    {
        LAST_UPDATER = lAST_UPDATER;
    }

    public String getLAST_UPDATE_TIME()
    {
        return LAST_UPDATE_TIME;
    }

    public void setLAST_UPDATE_TIME(String lAST_UPDATE_TIME)
    {
        LAST_UPDATE_TIME = lAST_UPDATE_TIME;
    }

    public String getLAST_CHECKER()
    {
        return LAST_CHECKER;
    }

    public void setLAST_CHECKER(String lAST_CHECKER)
    {
        LAST_CHECKER = lAST_CHECKER;
    }

    public String getLAST_CHECK_TIME()
    {
        return LAST_CHECK_TIME;
    }

    public void setLAST_CHECK_TIME(String lAST_CHECK_TIME)
    {
        LAST_CHECK_TIME = lAST_CHECK_TIME;
    }
}
