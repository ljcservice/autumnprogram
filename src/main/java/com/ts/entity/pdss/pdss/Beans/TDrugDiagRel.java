package com.ts.entity.pdss.pdss.Beans;

import com.hitzd.his.Beans.TBaseBean;


/**
 * 药物禁忌症对应
 * 
 * @author liujc
 * 
 */
public class TDrugDiagRel extends TBaseBean
{
    private static final long serialVersionUID = 5327913630932288376L;
    /* 药物禁忌症对应ID */
    private String DRUG_DIAG_REL_ID;
    /* 处置不当码 */
    private String CONTRAIND_ID;
    /* 药品码 */
    private String DRUG_CLASS_ID;
    /* 用药途径 */
    private String ADMINISTRATION_ID;
    /* 检索码 */
    private String INDEX_ID;

    public String getDRUG_DIAG_REL_ID()
    {
        return DRUG_DIAG_REL_ID;
    }

    public void setDRUG_DIAG_REL_ID(String dRUG_DIAG_REL_ID)
    {
        DRUG_DIAG_REL_ID = dRUG_DIAG_REL_ID;
    }

    public String getCONTRAIND_ID()
    {
        return CONTRAIND_ID;
    }

    public void setCONTRAIND_ID(String cONTRAIND_ID)
    {
        CONTRAIND_ID = cONTRAIND_ID;
    }

    public String getDRUG_CLASS_ID()
    {
        return DRUG_CLASS_ID;
    }

    public void setDRUG_CLASS_ID(String dRUG_CLASS_ID)
    {
        DRUG_CLASS_ID = dRUG_CLASS_ID;
    }

    public String getADMINISTRATION_ID()
    {
        return ADMINISTRATION_ID;
    }

    public void setADMINISTRATION_ID(String aDMINISTRATION_ID)
    {
        ADMINISTRATION_ID = aDMINISTRATION_ID;
    }

    public String getINDEX_ID()
    {
        return INDEX_ID;
    }

    public void setINDEX_ID(String iNDEX_ID)
    {
        INDEX_ID = iNDEX_ID;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((ADMINISTRATION_ID == null) ? 0 : ADMINISTRATION_ID
                        .hashCode());
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
        TDrugDiagRel other = (TDrugDiagRel) obj;
        if (ADMINISTRATION_ID == null)
        {
            if (other.ADMINISTRATION_ID != null)
                return false;
        }
        else if (!ADMINISTRATION_ID.equals(other.ADMINISTRATION_ID))
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
