package com.ts.entity.pdss.pdss.Beans;

import com.hitzd.his.Beans.TBaseBean;


/**
 * 药物禁忌症信息
 * 
 * @author liujc
 * 
 */
public class TDrugDiagInfo extends TBaseBean
{
    /* 药物禁忌症信息ID */
    private String DRUG_DIAG_INFO_ID;
    /* 诊断ID */
    private String DIAGNOSIS_DICT_ID;
    /*诊断name*/
    private String Diagnosis_dict_name;
    /* 药物禁忌症对应ID */
    private String DRUG_DIAG_REL_ID;
    /* 反应序号 */
    private String SEQ_ID;
    /* 严重程度 */
    private String INTER_INDI;
    /* 反应描述 */
    private String DIAG_DESC;
    /* 文献来源 */
    private String DRUG_REF_SOURCE;
    /* 处置不当码 */
    private String CONTRAIND_ID;

    public String getDRUG_DIAG_INFO_ID()
    {
        return DRUG_DIAG_INFO_ID;
    }

    public String getDiagnosis_dict_name()
    {
        return Diagnosis_dict_name;
    }


    public void setDiagnosis_dict_name(String diagnosis_dict_name)
    {
        Diagnosis_dict_name = diagnosis_dict_name;
    }


    public void setDRUG_DIAG_INFO_ID(String dRUG_DIAG_INFO_ID)
    {
        DRUG_DIAG_INFO_ID = dRUG_DIAG_INFO_ID;
    }

    public String getDIAGNOSIS_DICT_ID()
    {
        return DIAGNOSIS_DICT_ID;
    }

    public void setDIAGNOSIS_DICT_ID(String dIAGNOSIS_DICT_ID)
    {
        DIAGNOSIS_DICT_ID = dIAGNOSIS_DICT_ID;
    }

    public String getDRUG_DIAG_REL_ID()
    {
        return DRUG_DIAG_REL_ID;
    }

    public void setDRUG_DIAG_REL_ID(String dRUG_DIAG_REL_ID)
    {
        DRUG_DIAG_REL_ID = dRUG_DIAG_REL_ID;
    }

    public String getSEQ_ID()
    {
        return SEQ_ID;
    }

    public void setSEQ_ID(String sEQ_ID)
    {
        SEQ_ID = sEQ_ID;
    }

    public String getINTER_INDI()
    {
        return INTER_INDI;
    }

    public void setINTER_INDI(String iNTER_INDI)
    {
        INTER_INDI = iNTER_INDI;
    }

    public String getDIAG_DESC()
    {
        return DIAG_DESC;
    }

    public void setDIAG_DESC(String dIAG_DESC)
    {
        DIAG_DESC = dIAG_DESC;
    }

    public String getDRUG_REF_SOURCE()
    {
        return DRUG_REF_SOURCE;
    }

    public void setDRUG_REF_SOURCE(String dRUG_REF_SOURCE)
    {
        DRUG_REF_SOURCE = dRUG_REF_SOURCE;
    }

    public String getCONTRAIND_ID()
    {
        return CONTRAIND_ID;
    }

    public void setCONTRAIND_ID(String cONTRAIND_ID)
    {
        CONTRAIND_ID = cONTRAIND_ID;
    }

}
