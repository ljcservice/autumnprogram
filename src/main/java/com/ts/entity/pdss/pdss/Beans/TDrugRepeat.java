package com.ts.entity.pdss.pdss.Beans;

import com.hitzd.his.Beans.TBaseBean;

/**
 * 重复给药d
 * @author autumn
 *
 */
public class TDrugRepeat extends TBaseBean
{
    
    private static final long serialVersionUID = 1L;
    
    private String drug_name_1; 
    private String drug_name_2; 
    private String refer_info;  
    private String effect;      
    private String effect_indi; 
    private String source;      
    private String source_tag;  
    private String drug_class_1;
    private String drug_class_2;
    private String id;
    public String getDrug_name_1()
    {
        return drug_name_1;
    }
    public void setDrug_name_1(String drug_name_1)
    {
        this.drug_name_1 = drug_name_1;
    }
    public String getDrug_name_2()
    {
        return drug_name_2;
    }
    public void setDrug_name_2(String drug_name_2)
    {
        this.drug_name_2 = drug_name_2;
    }
    public String getRefer_info()
    {
        return refer_info;
    }
    public void setRefer_info(String refer_info)
    {
        this.refer_info = refer_info;
    }
    public String getEffect()
    {
        return effect;
    }
    public void setEffect(String effect)
    {
        this.effect = effect;
    }
    public String getEffect_indi()
    {
        return effect_indi;
    }
    public void setEffect_indi(String effect_indi)
    {
        this.effect_indi = effect_indi;
    }
    public String getSource()
    {
        return source;
    }
    public void setSource(String source)
    {
        this.source = source;
    }
    public String getSource_tag()
    {
        return source_tag;
    }
    public void setSource_tag(String source_tag)
    {
        this.source_tag = source_tag;
    }
    public String getDrug_class_1()
    {
        return drug_class_1;
    }
    public void setDrug_class_1(String drug_class_1)
    {
        this.drug_class_1 = drug_class_1;
    }
    public String getDrug_class_2()
    {
        return drug_class_2;
    }
    public void setDrug_class_2(String drug_class_2)
    {
        this.drug_class_2 = drug_class_2;
    }
    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }          
    
}
