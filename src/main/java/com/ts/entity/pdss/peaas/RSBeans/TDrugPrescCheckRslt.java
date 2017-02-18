package com.ts.entity.pdss.peaas.RSBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * 药品结果数据集
 * @author Administrator
 *
 */
public class TDrugPrescCheckRslt
{
    /* 药品代码 */
    private String drugCode;
    /* 药品名称 */
    private String drugName;
    /* 主医嘱号 */
    private String rec_main_num;
    /* 子医嘱号 */
    private String rec_sub_num;
    /* 单药品审查结果*/
    private List<String> CheckRslt = new ArrayList<String>();
    
    public String getDrugCode()
    {
        return drugCode;
    }
    public void setDrugCode(String drugCode)
    {
        this.drugCode = drugCode;
    }
    public String getDrugName()
    {
        return drugName;
    }
    public void setDrugName(String drugName)
    {
        this.drugName = drugName;
    }
    
    public void addCheckRslt(String DrugCheckInfo)
    {
        this.CheckRslt.add(DrugCheckInfo);
    }
    
    public String[] getCheckRslt()
    {
        return (String[])this.CheckRslt.toArray(new String[0]);
    }
    
    public String getRec_main_num()
    {
        return rec_main_num;
    }
    
    public void setRec_main_num(String rec_main_num)
    {
        this.rec_main_num = rec_main_num;
    }
    
    public String getRec_sub_num()
    {
        return rec_sub_num;
    }
    public void setRec_sub_num(String rec_sub_num)
    {
        this.rec_sub_num = rec_sub_num;
    }
}
