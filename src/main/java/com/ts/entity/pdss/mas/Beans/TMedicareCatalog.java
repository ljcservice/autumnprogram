package com.ts.entity.pdss.mas.Beans;

import java.io.Serializable;

/**
 * 医保用药目录
 * @author liujc
 *
 */
public class TMedicareCatalog implements Serializable
{
	private static final long serialVersionUID = 1L;
	public static String Prefix = "MedicareCatalog_";
	/*药品ID	 */
	private String DRUG_ID;
	/*药品名称	*/
	private String DRUG_NAME;
	/*医保说明	*/
	private String REMARK;
	/*适用类型	*/
	private String APPLY_TYPE;
	/*费别	*/
	private String FEE_TYPE;
	
    public String getDRUG_ID()
    {
        return DRUG_ID;
    }

    public void setDRUG_ID(String dRUG_ID)
    {
        DRUG_ID = dRUG_ID;
    }

    public String getDRUG_NAME()
    {
        return DRUG_NAME;
    }

    public void setDRUG_NAME(String dRUG_NAME)
    {
        DRUG_NAME = dRUG_NAME;
    }

    public String getREMARK()
    {
        return REMARK;
    }

    public void setREMARK(String rEMARK)
    {
        REMARK = rEMARK;
    }

    public String getAPPLY_TYPE()
    {
        return APPLY_TYPE;
    }

    public void setAPPLY_TYPE(String aPPLY_TYPE)
    {
        APPLY_TYPE = aPPLY_TYPE;
    }

    public String getFEE_TYPE()
    {
        return FEE_TYPE;
    }

    public void setFEE_TYPE(String fEE_TYPE)
    {
        FEE_TYPE = fEE_TYPE;
    }
    
	@Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((DRUG_ID == null) ? 0 : (Prefix + DRUG_ID).hashCode());
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
        TMedicareCatalog other = (TMedicareCatalog) obj;
        if (DRUG_ID == null)
        {
            if (other.DRUG_ID != null)
                return false;
        }
        else if (!(Prefix + DRUG_ID).equals(Prefix + other.DRUG_ID))
            return false;
        return true;
    }
}
