package com.ts.entity.pdss.pdss.Beans;

import com.hitzd.his.Beans.TBaseBean;


/**
 * @description 副作用字典类：SideDict 对应数据库表：副作用字典(SIDE_DICT)
 * @author liujc
 */
public class TDrugSideDict extends TBaseBean
{
    private static final long serialVersionUID  = 1L;
    /* 副作用代码 */
    private String            SIDE_ID           = "";
    /* 副作用描述 */
    private String            DIAGNOSIS_DESC    = "";
    /* 副作用组编号 */
    private String            SIDE_GROUP_ID     = "";
    /* 反应严重程度 */
    private String            SEVERITY          = "";
    /* 诊断ID */
    private String            DIAGNOSIS_DICT_ID = "";
    /* 诊断名称 */
    private String            Diagnosis_dict_name = "";
    /* 副作用序号 */
    private String            SEQ_NO            = "";
    /* 给药途径ID */
    private String            ADMINISTRATION_ID = "";
    /* 药品类别  */
    private String            DRUG_CLASS_ID     = "";
    
    public String getSIDE_ID()
    {
        return SIDE_ID == null?"":SIDE_ID;
    }
    public void setSIDE_ID(String sIDE_ID)
    {
        SIDE_ID = sIDE_ID;
    }
    public String getDIAGNOSIS_DESC()
    {
        return DIAGNOSIS_DESC == null?"":DIAGNOSIS_DESC;
    }
    public void setDIAGNOSIS_DESC(String dIAGNOSIS_DESC)
    {
        DIAGNOSIS_DESC = dIAGNOSIS_DESC;
    }
    public String getSIDE_GROUP_ID()
    {
        return SIDE_GROUP_ID == null?"":SIDE_GROUP_ID ;
    }
    public void setSIDE_GROUP_ID(String sIDE_GROUP_ID)
    {
        SIDE_GROUP_ID = sIDE_GROUP_ID;
    }
    public String getSEVERITY()
    {
        return SEVERITY == null?"":SEVERITY;
    }
    public void setSEVERITY(String sEVERITY)
    {
        SEVERITY = sEVERITY;
    }
    public String getDIAGNOSIS_DICT_ID()
    {
        return DIAGNOSIS_DICT_ID == null ? "":DIAGNOSIS_DICT_ID;
    }
    public void setDIAGNOSIS_DICT_ID(String dIAGNOSIS_DICT_ID)
    {
        DIAGNOSIS_DICT_ID = dIAGNOSIS_DICT_ID;
    }
    public String getSEQ_NO()
    {
        return SEQ_NO == null ? "" : SEQ_NO;
    }
    public void setSEQ_NO(String sEQ_NO)
    {
        if(sEQ_NO == null)
            sEQ_NO = "";
        SEQ_NO = sEQ_NO;
    }
    public String getADMINISTRATION_ID()
    {
        return ADMINISTRATION_ID == null?"":ADMINISTRATION_ID;
    }
    public void setADMINISTRATION_ID(String aDMINISTRATION_ID)
    {
        ADMINISTRATION_ID = aDMINISTRATION_ID;
    }
    public String getDRUG_CLASS_ID()
    {
        return DRUG_CLASS_ID == null?"":DRUG_CLASS_ID;
    }
    public void setDRUG_CLASS_ID(String dRUG_CLASS_ID)
    {
        DRUG_CLASS_ID = dRUG_CLASS_ID;
    }
    
    public String getDiagnosis_dict_name()
    {
        return Diagnosis_dict_name == null?"":Diagnosis_dict_name;
    }
    
    public void setDiagnosis_dict_name(String diagnosis_dict_name)
    {
        Diagnosis_dict_name = diagnosis_dict_name;
    }
   
    
}