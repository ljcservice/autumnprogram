package com.ts.entity.pdss.pdss.RSBeans;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugRepeat;

/**
 * @description 药品重复成份审查结果
 * 
 */
public class TDrugIngredientRslt extends TBaseResult implements Serializable
{

    private static final long serialVersionUID = 1L;

    private TDrug drug ;
    private List<TDrug> drugs;
    private List<TDrugRepeat> drugRep;
    
    public TDrugIngredientRslt()
    {
        setVersion(2);
    }
    /**
     *  添加重复药品
     * @param drugMap
     */
    public void addDrugRepeat(TDrug drug,List<TDrug> drugs,List<TDrugRepeat> drugRep)
    {
        /* 大于1个的情况下加入重复药品集合中 */
        if(drugs.size()>0)
        {
            this.drug = drug;
            this.drugs = drugs;
            // 重复信息 
            this.drugRep = drugRep;
            this.alertLevel = "R";
            this.alertHint = "重复";
        }
    }
    @XmlElement(name="getDrug")
    public TDrug getDrug()
    {
    	return drug;
    }
    
    @XmlElement(name="getDrugs")
    public TDrug[] getDrugs()
    {
    	return drugs==null? null: (TDrug[])drugs.toArray(new TDrug[0]);
    }
    
    @XmlElement(name="getDrugRepeat")
    public TDrugRepeat[] getDrugRepeat()
    {
        return drugRep == null ? null :(TDrugRepeat[]) this.drugRep.toArray(new TDrugRepeat[0]);
    }
}