package com.ts.entity.pdss.peaas.Beans;


/**
 * 处方主信息
 * 
 * @author Administrator
 * 
 */
public class TPrescMasterBean
{
    public TPrescMasterBean()
    {
    }

    /* 病人标识号 */
    private String             PATIENT_ID;
    /* 就诊日期 */
    private String             VISIT_DATE;
    /* 就诊序号 */
    private String             VISIT_NO;
    /* 流水号 */
    private String             SERIAL_NO;
    /* 开单科室 */
    private String             ORDERED_BY;
    /* 开单医生 */
    private String             DOCTOR;
    /* 开单日期 */
    private String             ORDER_DATE;
    /* 开单科室名字 */
    private String             ordered_by_name ;
    
    /* 处方药品明细 */
    private TPrescDetailBean[] pdbs;

    public String getPATIENT_ID()
    {
        return PATIENT_ID;
    }

    public void setPATIENT_ID(String pATIENT_ID)
    {
        PATIENT_ID = pATIENT_ID;
    }

    public String getVISIT_DATE()
    {
        return VISIT_DATE;
    }

    public void setVISIT_DATE(String vISIT_DATE)
    {
        VISIT_DATE = vISIT_DATE;
    }

    public String getVISIT_NO()
    {
        return VISIT_NO;
    }

    public void setVISIT_NO(String vISIT_NO)
    {
        VISIT_NO = vISIT_NO;
    }

    public String getSERIAL_NO()
    {
        return SERIAL_NO;
    }

    public void setSERIAL_NO(String sERIAL_NO)
    {
        SERIAL_NO = sERIAL_NO;
    }

    public String getORDERED_BY()
    {
        return ORDERED_BY;
    }

    public void setORDERED_BY(String oRDERED_BY)
    {
        ORDERED_BY = oRDERED_BY;
    }

    public String getDOCTOR()
    {
        return DOCTOR;
    }

    public void setDOCTOR(String dOCTOR)
    {
        DOCTOR = dOCTOR;
    }

    public String getORDER_DATE()
    {
        return ORDER_DATE;
    }

    public void setORDER_DATE(String oRDER_DATE)
    {
        ORDER_DATE = oRDER_DATE;
    }

    public TPrescDetailBean[] getPdbs()
    {
        return pdbs;
    }

    public void setPdbs(TPrescDetailBean[] pdbs)
    {
        this.pdbs = pdbs;
    }

    public String getOrdered_by_name()
    {
        return ordered_by_name;
    }

    public void setOrdered_by_name(String ordered_by_name)
    {
        this.ordered_by_name = ordered_by_name;
    }
}
