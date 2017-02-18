package com.ts.entity.pdss.pdss.RSBeans;


/**
 * 单药品的审查结果
 * @author Administrator
 *
 */
public class DrugSingleCheckRS
{
    /* 本地药品码 */
    private String drug_no_local = "";
    /* 医嘱序号 */
    private String recMainNo     = "";
    /* 医嘱子序号 */
    private String recSubNo      = "";
    
    /* 药物安全审查  */
    private TCheckResult cr;

    public String getDrug_no_local()
    {
        return drug_no_local;
    }

    public void setDrug_no_local(String drug_no_local)
    {
        this.drug_no_local = drug_no_local;
    }

    public String getRecMainNo()
    {
        return recMainNo;
    }

    public void setRecMainNo(String recMainNo)
    {
        this.recMainNo = recMainNo;
    }

    public String getRecSubNo()
    {
        return recSubNo;
    }

    public void setRecSubNo(String recSubNo)
    {
        this.recSubNo = recSubNo;
    }

    public TCheckResult getCr()
    {
        return cr;
    }

    public void setCr(TCheckResult cr)
    {
        this.cr = cr;
    }
}
