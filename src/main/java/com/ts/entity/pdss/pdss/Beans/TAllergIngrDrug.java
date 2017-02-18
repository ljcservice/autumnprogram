package com.ts.entity.pdss.pdss.Beans;


import com.hitzd.his.Beans.TBaseBean;


/**
 * 药物成分、药敏、药物分类与药物对照字典
 * @author liujc
 *
 */
public class TAllergIngrDrug extends TBaseBean
{
    /* 药敏信息代码 */
    private String DRUG_ALLERGEN_ID;
    /* 对照字典ID */
    private String ALLERG_INGR_DRUG_ID;
    /* 药品类码  */
    private String DRUG_CLASS_ID;
    /* 输入码   */
    private String INPUT_CODE;
    /* 过敏名称  */
    private String DRUG_ALLERGEN_NAME;

    public String getDRUG_ALLERGEN_ID()
    {
        return DRUG_ALLERGEN_ID;
    }
    public void setDRUG_ALLERGEN_ID(String dRUG_ALLERGEN_ID)
    {
        DRUG_ALLERGEN_ID = dRUG_ALLERGEN_ID;
    }
    public String getALLERG_INGR_DRUG_ID()
    {
        return ALLERG_INGR_DRUG_ID;
    }
    public void setALLERG_INGR_DRUG_ID(String aLLERG_INGR_DRUG_ID)
    {
        ALLERG_INGR_DRUG_ID = aLLERG_INGR_DRUG_ID;
    }
    public String getDRUG_CLASS_ID()
    {
        return DRUG_CLASS_ID;
    }
    public void setDRUG_CLASS_ID(String dRUG_CLASS_ID)
    {
        DRUG_CLASS_ID = dRUG_CLASS_ID;
    }
    public String getINPUT_CODE()
    {
        return INPUT_CODE;
    }
    public void setINPUT_CODE(String iNPUT_CODE)
    {
        INPUT_CODE = iNPUT_CODE;
    }
    public String getDRUG_ALLERGEN_NAME()
    {
        return DRUG_ALLERGEN_NAME;
    }
    public void setDRUG_ALLERGEN_NAME(String dRUG_ALLERGEN_NAME)
    {
        DRUG_ALLERGEN_NAME = dRUG_ALLERGEN_NAME;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((DRUG_ALLERGEN_ID == null) ? 0 : DRUG_ALLERGEN_ID.hashCode());
        result = prime * result
                + ((DRUG_CLASS_ID == null) ? 0 : DRUG_CLASS_ID.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TAllergIngrDrug other = (TAllergIngrDrug) obj;
        if (DRUG_ALLERGEN_ID == null)
        {
            if (other.DRUG_ALLERGEN_ID != null)
                return false;
        }
        else if (!DRUG_ALLERGEN_ID.equals(other.DRUG_ALLERGEN_ID))
            return false;
        if (DRUG_CLASS_ID == null)
        {
            if (other.DRUG_CLASS_ID != null)
                return false;
        }
        else if (!DRUG_CLASS_ID.equals(other.DRUG_CLASS_ID))
            return false;
        return true;
    }
}
