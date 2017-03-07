package com.ts.entity.pdss.pdss.RSBeans;

import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugSideDict;

/**
 * @description 病人不良反应结果类：PatSideRslt 对应数据库表：病人不良反应记录(PAT_SIDE_RSLT)
 * @author
 */
public class TDrugHarmfulRslt extends TBaseResult implements
        java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    /* 药品  */
    private TDrug drug ;
    /* 不良反应信息 */
    private TDrugSideDict     drugside;
    public TDrugHarmfulRslt()
    {
        setVersion(2);
    }
    /**
     * 添加不良反应信息
     * 
     * @param dsd
     */
    public void addDrugSide(TDrugSideDict dsd,TDrug _drug)
    {
        if ("严重".equals(dsd.getSEVERITY()))
        {
            this.alertLevel = "R";
        }
        else
        	this.alertLevel = "Y";
        this.drugside = dsd;
        this.drug = _drug;
    }

    /**
     * 返回不良信息
     * 
     * @return
     */
    public TDrugSideDict getDrugSide()
    {
        return  this.drugside;
    }
    
    /**
     * 返回药品
     * @return
     */
    public TDrug getDrug()
    {
        return this.drug;
    }
}
