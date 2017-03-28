package com.ts.entity.pdss.pdss.Beans;

import com.hitzd.his.Beans.TBaseBean;


/**
 * 药品用药信息
 * @author liujc
 *
 */
public class TDrugUseDetail extends TBaseBean
{
	private static final long serialVersionUID = 1L;
	/* 药品用药信息ID */
    private String DRUG_USE_DETAIL_ID;
    /* 药品类码ID */
    private String DRUG_CLASS_ID;
    /* 孕妇用药 */
    private String PREGNANT_INDI;
    /* 孕妇用药说明 */
    private String PREGNANT_INFO;
    /* 孕妇用药说明参考` */
    private String PREGNANT_INFO_REF;
    /* 哺乳期用药 */
    private String LACT_INDI;
    /* 哺乳期用药说明 */
    private String LACT_INFO;
    /* 哺乳期用药说明参考` */
    private String LACT_INFO_REF;
    /* 儿童用药 */
    private String KID_INDI;
    /* 儿童年龄下限 */
    private String KID_LOW  = "0" ;
    /* 儿童年龄上限 */
    private String KID_HIGH = "0";
    /* 儿童用药说明 */
    private String KID_INFO;
    /* 儿童用药说明参考 */
    private String KID_INFO_REF;
    /* 老年用药 */
    private String OLD_INDI;
    /* 老年用药说明 */
    private String OLD_INFO;
    /* 老年用药说明参考` */
    private String OLD_INFO_REF;
    /* 肝功能禁用标志 */
    private String HEPATICAL_INDI;
    /* 肾功能禁用标志 */
    private String RENAL_INDI;
    /* 禁止的用药途径 */
    private String FORBID_RUID;
    /* 用药途径禁止的原因 */
    private String FORBID_CAUSE;
    /* 不宜的用药途径 */
    private String INADVIS_RTID;
    /* 用药途径不宜的原因 */
    private String INADVIS_CAUSE;
    /* 需注意的用药途径 */
    private String ADVERT_RTID;
    /* 用药途径需注意的原因 */
    private String ADVERT_CAUSE;
    /* 最后更新时间 */
    private String LAST_DATE_TIME;
    
    public String getDRUG_USE_DETAIL_ID()
    {
        return DRUG_USE_DETAIL_ID == null ?"":DRUG_USE_DETAIL_ID;
    }
    public void setDRUG_USE_DETAIL_ID(String dRUG_USE_DETAIL_ID)
    {
        DRUG_USE_DETAIL_ID = dRUG_USE_DETAIL_ID;
    }
    public String getDRUG_CLASS_ID()
    {
        return DRUG_CLASS_ID == null ?"":DRUG_CLASS_ID;
    }
    public void setDRUG_CLASS_ID(String dRUG_CLASS_ID)
    {
        DRUG_CLASS_ID = dRUG_CLASS_ID;
    }
    public String getPREGNANT_INDI()
    {
        return PREGNANT_INDI == null ?"":PREGNANT_INDI;
    }
    public void setPREGNANT_INDI(String pREGNANT_INDI)
    {
        PREGNANT_INDI = pREGNANT_INDI;
    }
    public String getPREGNANT_INFO()
    {
        return PREGNANT_INFO == null ?"":PREGNANT_INFO;
    }
    public void setPREGNANT_INFO(String pREGNANT_INFO)
    {
        PREGNANT_INFO = pREGNANT_INFO;
    }
    public String getPREGNANT_INFO_REF()
    {
        return PREGNANT_INFO_REF == null ?"":PREGNANT_INFO_REF;
    }
    public void setPREGNANT_INFO_REF(String pREGNANT_INFO_REF)
    {
        PREGNANT_INFO_REF = pREGNANT_INFO_REF;
    }
    public String getLACT_INDI()
    {
        return LACT_INDI == null ?"":LACT_INDI;
    }
    public void setLACT_INDI(String lACT_INDI)
    {
        LACT_INDI = lACT_INDI;
    }
    public String getLACT_INFO()
    {
        return LACT_INFO == null ?"":LACT_INFO;
    }
    public void setLACT_INFO(String lACT_INFO)
    {
        LACT_INFO = lACT_INFO;
    }
    public String getLACT_INFO_REF()
    {
        return LACT_INFO_REF == null ?"":LACT_INFO_REF;
    }
    public void setLACT_INFO_REF(String lACT_INFO_REF)
    {
        LACT_INFO_REF = lACT_INFO_REF;
    }
    public String getKID_INDI()
    {
        return KID_INDI == null ?"":KID_INDI;
    }
    public void setKID_INDI(String kID_INDI)
    {
        KID_INDI = kID_INDI;
    }
    public String getKID_LOW()
    {
        return KID_LOW == null ?"0":KID_LOW;
    }
    public void setKID_LOW(String kID_LOW)
    {
        KID_LOW = kID_LOW;
    }
    public String getKID_HIGH()
    { 
        return KID_HIGH == null?"0":KID_HIGH;
    }
    public void setKID_HIGH(String kID_HIGH)
    {
        KID_HIGH = kID_HIGH;
    }
    public String getKID_INFO()
    {
        return KID_INFO;
    }
    public void setKID_INFO(String kID_INFO)
    {
        KID_INFO = kID_INFO;
    }
    public String getKID_INFO_REF()
    {
        return KID_INFO_REF;
    }
    public void setKID_INFO_REF(String kID_INFO_REF)
    {
        KID_INFO_REF = kID_INFO_REF;
    }
    public String getOLD_INDI()
    {
        return OLD_INDI;
    }
    public void setOLD_INDI(String oLD_INDI)
    {
        OLD_INDI = oLD_INDI;
    }
    public String getOLD_INFO()
    {
        return OLD_INFO;
    }
    public void setOLD_INFO(String oLD_INFO)
    {
        OLD_INFO = oLD_INFO;
    }
    public String getOLD_INFO_REF()
    {
        return OLD_INFO_REF;
    }
    public void setOLD_INFO_REF(String oLD_INFO_REF)
    {
        OLD_INFO_REF = oLD_INFO_REF;
    }
    public String getHEPATICAL_INDI()
    {
        return HEPATICAL_INDI == null ?"":HEPATICAL_INDI;
    }
    public void setHEPATICAL_INDI(String hEPATICAL_INDI)
    {
        HEPATICAL_INDI = hEPATICAL_INDI;
    }
    public String getRENAL_INDI()
    {
        return RENAL_INDI;
    }
    public void setRENAL_INDI(String rENAL_INDI)
    {
        RENAL_INDI = rENAL_INDI;
    }
    public String getFORBID_RUID()
    {
        return FORBID_RUID;
    }
    public void setFORBID_RUID(String fORBID_RUID)
    {
        FORBID_RUID = fORBID_RUID;
    }
    public String getFORBID_CAUSE()
    {
        return FORBID_CAUSE == null ? "" : FORBID_CAUSE;
    }
    public void setFORBID_CAUSE(String fORBID_CAUSE)
    {
        FORBID_CAUSE = fORBID_CAUSE;
    }
    public String getINADVIS_RTID()
    {
        return INADVIS_RTID;
    }
    public void setINADVIS_RTID(String iNADVIS_RTID)
    {
        INADVIS_RTID = iNADVIS_RTID;
    }
    public String getINADVIS_CAUSE()
    {
        return INADVIS_CAUSE == null ? "" : INADVIS_CAUSE;
    }
    public void setINADVIS_CAUSE(String iNADVIS_CAUSE)
    {
        INADVIS_CAUSE = iNADVIS_CAUSE;
    }
    public String getADVERT_RTID()
    {
        return ADVERT_RTID;
    }
    public void setADVERT_RTID(String aDVERT_RTID)
    {
        ADVERT_RTID = aDVERT_RTID;
    }
    public String getADVERT_CAUSE()
    {
        return ADVERT_CAUSE == null?"":ADVERT_CAUSE;
    }
    public void setADVERT_CAUSE(String aDVERT_CAUSE)
    {
        ADVERT_CAUSE = aDVERT_CAUSE;
    }
    public String getLAST_DATE_TIME()
    {
        return LAST_DATE_TIME;
    }
    public void setLAST_DATE_TIME(String lAST_DATE_TIME)
    {
        LAST_DATE_TIME = lAST_DATE_TIME;
    }
    
}
