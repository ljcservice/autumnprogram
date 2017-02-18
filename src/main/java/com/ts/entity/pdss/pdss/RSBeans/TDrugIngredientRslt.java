package com.ts.entity.pdss.pdss.RSBeans;

import java.io.Serializable;
import java.util.List;

import com.ts.entity.pdss.pdss.Beans.TDrug;

/**
 * @description 药品重复成份审查结果
 * 
 */
public class TDrugIngredientRslt extends TBaseResult implements Serializable
{

    private static final long serialVersionUID = 1L;

    private TDrug drug ;
    private List<TDrug> drugs;
    public TDrugIngredientRslt()
    {
        setVersion(2);
    }
    /**
     *  添加重复药品
     * @param drugMap
     */
    public void addDrugRepeat(TDrug drug,List<TDrug> drugs)
    {
        /* 大于1个的情况下加入重复药品集合中 */
        if(drugs.size()>0)
        {
            this.drug = drug;
            this.drugs = drugs;
            this.alertLevel = "R";
            this.alertHint = "重复";
        }
    }
    public TDrug getDrug()
    {
    	return drug;
    }
    
    public TDrug[] getDrugs()
    {
    	return drugs==null? null: (TDrug[])drugs.toArray(new TDrug[0]);
    }
}