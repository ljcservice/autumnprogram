package com.ts.entity.pdss.pdss.RSBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ts.entity.pdss.pdss.Beans.TDrug;

/**
 * @description 药物剂量审查结果
 * 
 */
public class TDrugDosageRslt extends TBaseResult implements Serializable
{

    private static final long serialVersionUID = 1L;
    
    private TDrug drug ;
    private List<String> performInfos = new ArrayList<String>();
    public TDrugDosageRslt()
    {
        setVersion(2);
    }
    /**
     * 添加剂量信息
     * @param pod
     * @param performinfo
     */
    public void  addDrugDosage(TDrug _drug ,List<String> performinfo)
    {
        this.performInfos = performinfo;
        this.drug = _drug;
        this.alertLevel = "R";
    }
    
    /**
     * 获得所用药信息
     * @return
     */
    public TDrug getPods()
    {
        return this.drug;
    }
    
    /**
     * 获得药品剂量问题说明 
     * @param index
     * @return
     */
    public String[] getPerformInfo()
    {
        return (String[])this.performInfos.toArray(new String[0] ) ;
    }
}
