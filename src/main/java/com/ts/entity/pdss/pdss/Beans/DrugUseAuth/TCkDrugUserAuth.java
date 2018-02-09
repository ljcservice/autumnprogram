package com.ts.entity.pdss.pdss.Beans.DrugUseAuth;

import java.io.Serializable;
import java.util.Date;
/**
 * 药物使用授权 实体bean
 * @author autumn
 *
 */
public class TCkDrugUserAuth implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /* 主键 */
    private String ID;            
    /* 药品 */
    private String  DRUG_CODE;     
    /* 药品名称 */
    private String DRUG_NAME;     
    /* 科室 */
    private String DEPT_NAME;     
    /* 医生 */
    private String DOCTOR_NAME;   
    /* 规格 */
    private String DRUG_SPEC;     
    /* 剂型 */
    private String DRUG_FORM;     
    /* 控制类型 */
    private String CONTROL_TYPE;  
    /* 阀值  */
    private Integer T_VALUE;       
    /* 当前累计值 */
    private Integer TOTAL_VALUE;   
    /* 更新人 */
    private String UPDATE_PERSION;
    /* 更新时间  */
    private String UPDATE_DATE;   
    /* 诊断名称 */
    private String DIAG_NAME;     
    /* 诊断编码 */
    private String DIAG_CODE;     
    /* 医保类型 */
    private String H_TYPE;
    /* 科室 代码*/
    private String DEPT_CODE;

    public String getID()
    {
        return ID;
    }
    public void setID(String iD)
    {
        ID = iD;
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
    public String getDRUG_SPEC()
    {
        return DRUG_SPEC;
    }
    public void setDRUG_SPEC(String dRUG_SPEC)
    {
        DRUG_SPEC = dRUG_SPEC;
    }
    public String getDRUG_FORM()
    {
        return DRUG_FORM;
    }
    public void setDRUG_FORM(String dRUG_FORM)
    {
        DRUG_FORM = dRUG_FORM;
    }
    public String getCONTROL_TYPE()
    {
        return CONTROL_TYPE;
    }
    public void setCONTROL_TYPE(String cONTROL_TYPE)
    {
        CONTROL_TYPE = cONTROL_TYPE;
    }
    public Integer getT_VALUE()
    {
        return T_VALUE;
    }
    public void setT_VALUE(Integer t_VALUE)
    {
        T_VALUE = t_VALUE;
    }
    public Integer getTOTAL_VALUE()
    {
        return TOTAL_VALUE;
    }
    public void setTOTAL_VALUE(Integer tOTAL_VALUE)
    {
        TOTAL_VALUE = tOTAL_VALUE;
    }
    public String getUPDATE_PERSION()
    {
        return UPDATE_PERSION;
    }
    public void setUPDATE_PERSION(String uPDATE_PERSION)
    {
        UPDATE_PERSION = uPDATE_PERSION;
    }
    public String getUPDATE_DATE()
    {
        return UPDATE_DATE;
    }
    public void setUPDATE_DATE(String uPDATE_DATE)
    {
        UPDATE_DATE = uPDATE_DATE;
    }
    public String getDIAG_NAME()
    {
        return DIAG_NAME;
    }
    public void setDIAG_NAME(String dIAG_NAME)
    {
        DIAG_NAME = dIAG_NAME;
    }
    public String getDIAG_CODE()
    {
        return DIAG_CODE;
    }
    public void setDIAG_CODE(String dIAG_CODE)
    {
        DIAG_CODE = dIAG_CODE;
    }
    public String getH_TYPE()
    {
        return H_TYPE;
    }
    public void setH_TYPE(String h_TYPE)
    {
        H_TYPE = h_TYPE;
    }
    public String getDEPT_CODE()
    {
        return DEPT_CODE;
    }
    public void setDEPT_CODE(String dEPT_CODE)
    {
        DEPT_CODE = dEPT_CODE;
    }
}
