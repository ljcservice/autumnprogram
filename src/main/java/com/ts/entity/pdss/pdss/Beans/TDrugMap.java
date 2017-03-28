package com.ts.entity.pdss.pdss.Beans;

import com.hitzd.his.Beans.TBaseBean;

/**
 * 药品表 (适用于 抗菌药物 )
 * 
 * @author Administrator
 * 
 */
public class TDrugMap extends TBaseBean
{
	private static final long serialVersionUID = 1L;
	/**/
    private String DRUG_NO_LOCAL;
    /**/
    private String DRUG_NAME_LOCAL;
    /**/
    private String DRUG_ID;
    /**/
    private String OPER_USER;
    /**/
    private String OPER_DATE;
    /**/
    private String OPER_TYPE;
    /* 是否抗菌药物，0是非抗菌药，1是抗菌药 */
    private String IS_ANTI;
    /* 是否医保药物，0是非医保，1是甲类医保，2是乙类医保 */
    private String IS_MEDCARE;
    /* 是否国家基本药物，0是非基本药物，1是基本药物 */
    private String IS_BASEDRUG;
    /* 毒麻精神用药，可以是文字 */
    private String TOXI_PROPERTY;
    /* 抗菌药基本单位 */
    private String DDD_VALUE;
    /* DDD的计量单位 */
    private String DDD_UNIT;
    /* 是否是兴奋剂，0是非兴奋剂，1是兴奋剂 */
    private String IS_EXHILARANT;
    /* 是否是注射剂，0是非注射剂，1是注射剂 */
    private String IS_INJECTION;
    /* 是否是口服制剂，同上 */
    private String IS_ORAL;
    /* 抗菌药级别，0是非限制用药，1是限制用药，2是特殊用药 */
    private String ANTI_LEVEL;
    /* 是否是溶剂，同上 */
    private String IS_IMPREGNANT;
    /* 药理分类 */
    private String PHARM_CATALOG;
    /* 药品分类，分为中成药、西药、化学药等等 */
    private String DRUG_CATALOG;
    /* 是否是预防用药 */
    private String IS_PREDRUG;
    /* 规格 */
    private String DRUG_SPEC;
    /* 可以使用该药品的科室 */
    private String USE_DEPT;
    /* 说明书ID */
    private String DIRECT_ID;

    public String getDRUG_NO_LOCAL()
    {
        return DRUG_NO_LOCAL;
    }

    public void setDRUG_NO_LOCAL(String dRUG_NO_LOCAL)
    {
        DRUG_NO_LOCAL = dRUG_NO_LOCAL;
    }

    public String getDRUG_NAME_LOCAL()
    {
        return DRUG_NAME_LOCAL;
    }

    public void setDRUG_NAME_LOCAL(String dRUG_NAME_LOCAL)
    {
        DRUG_NAME_LOCAL = dRUG_NAME_LOCAL;
    }

    public String getDRUG_ID()
    {
        return DRUG_ID;
    }

    public void setDRUG_ID(String dRUG_ID)
    {
        DRUG_ID = dRUG_ID;
    }

    public String getOPER_USER()
    {
        return OPER_USER;
    }

    public void setOPER_USER(String oPER_USER)
    {
        OPER_USER = oPER_USER;
    }

    public String getOPER_DATE()
    {
        return OPER_DATE;
    }

    public void setOPER_DATE(String oPER_DATE)
    {
        OPER_DATE = oPER_DATE;
    }

    public String getOPER_TYPE()
    {
        return OPER_TYPE;
    }

    public void setOPER_TYPE(String oPER_TYPE)
    {
        OPER_TYPE = oPER_TYPE;
    }

    public String getIS_ANTI()
    {
        return IS_ANTI;
    }

    public void setIS_ANTI(String iS_ANTI)
    {
        IS_ANTI = iS_ANTI;
    }

    public String getIS_MEDCARE()
    {
        return IS_MEDCARE;
    }

    public void setIS_MEDCARE(String iS_MEDCARE)
    {
        IS_MEDCARE = iS_MEDCARE;
    }

    public String getIS_BASEDRUG()
    {
        return IS_BASEDRUG;
    }

    public void setIS_BASEDRUG(String iS_BASEDRUG)
    {
        IS_BASEDRUG = iS_BASEDRUG;
    }

    public String getTOXI_PROPERTY()
    {
        return TOXI_PROPERTY;
    }

    public void setTOXI_PROPERTY(String tOXI_PROPERTY)
    {
        TOXI_PROPERTY = tOXI_PROPERTY;
    }

    public String getDDD_VALUE()
    {
        return DDD_VALUE;
    }

    public void setDDD_VALUE(String dDD_VALUE)
    {
        DDD_VALUE = dDD_VALUE;
    }

    public String getDDD_UNIT()
    {
        return DDD_UNIT;
    }

    public void setDDD_UNIT(String dDD_UNIT)
    {
        DDD_UNIT = dDD_UNIT;
    }

    public String getIS_EXHILARANT()
    {
        return IS_EXHILARANT;
    }

    public void setIS_EXHILARANT(String iS_EXHILARANT)
    {
        IS_EXHILARANT = iS_EXHILARANT;
    }

    public String getIS_INJECTION()
    {
        return IS_INJECTION;
    }

    public void setIS_INJECTION(String iS_INJECTION)
    {
        IS_INJECTION = iS_INJECTION;
    }

    public String getIS_ORAL()
    {
        return IS_ORAL;
    }

    public void setIS_ORAL(String iS_ORAL)
    {
        IS_ORAL = iS_ORAL;
    }

    public String getANTI_LEVEL()
    {
        return ANTI_LEVEL == null || "".equals(ANTI_LEVEL) ? "0" : this.ANTI_LEVEL;
    }

    public void setANTI_LEVEL(String aNTI_LEVEL)
    {
        ANTI_LEVEL = aNTI_LEVEL;
    }

    public String getIS_IMPREGNANT()
    {
        return IS_IMPREGNANT;
    }

    public void setIS_IMPREGNANT(String iS_IMPREGNANT)
    {
        IS_IMPREGNANT = iS_IMPREGNANT;
    }

    public String getPHARM_CATALOG()
    {
        return PHARM_CATALOG;
    }

    public void setPHARM_CATALOG(String pHARM_CATALOG)
    {
        PHARM_CATALOG = pHARM_CATALOG;
    }

    public String getDRUG_CATALOG()
    {
        return DRUG_CATALOG;
    }

    public void setDRUG_CATALOG(String dRUG_CATALOG)
    {
        DRUG_CATALOG = dRUG_CATALOG;
    }

    public String getIS_PREDRUG()
    {
        return IS_PREDRUG;
    }

    public void setIS_PREDRUG(String iS_PREDRUG)
    {
        IS_PREDRUG = iS_PREDRUG;
    }

    public String getDRUG_SPEC()
    {
        return DRUG_SPEC;
    }

    public void setDRUG_SPEC(String dRUG_SPEC)
    {
        DRUG_SPEC = dRUG_SPEC;
    }

    public String getUSE_DEPT()
    {
        return USE_DEPT;
    }

    public void setUSE_DEPT(String uSE_DEPT)
    {
        USE_DEPT = uSE_DEPT;
    }

    public String getDIRECT_ID()
    {
        return DIRECT_ID;
    }

    public void setDIRECT_ID(String dIRECT_ID)
    {
        DIRECT_ID = dIRECT_ID;
    }
}
