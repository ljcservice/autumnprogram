package com.ts.entity.pdss.mas.Beans;

import java.io.Serializable;

/**
 * 医保审核字典表
 * @author liujc
 *
 */
public class TMemo implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	// ID
    private String        ID;
    // 医保用药ID
    private String        DRUG_ID;
    // 审核项目名称
    private String        CHECK_ITEM_NAME;
    // 审核项目编码
    private String        CHECK_ITEM_CODE;
    // 审核类型
    private String        CHECK_TYPE;

    private static String Prefix = "Memo_";
	
    public String getID()
    {
        return ID;
    }

    public void setID(String iD)
    {
        ID = iD;
    }

    public String getDRUG_ID()
    {
        return DRUG_ID;
    }

    public void setDRUG_ID(String dRUG_ID)
    {
        DRUG_ID = dRUG_ID;
    }

    public String getCHECK_ITEM_NAME()
    {
        return CHECK_ITEM_NAME;
    }

    public void setCHECK_ITEM_NAME(String cHECK_ITEM_NAME)
    {
        CHECK_ITEM_NAME = cHECK_ITEM_NAME;
    }

    public String getCHECK_ITEM_CODE()
    {
        return CHECK_ITEM_CODE;
    }

    public void setCHECK_ITEM_CODE(String cHECK_ITEM_CODE)
    {
        CHECK_ITEM_CODE = cHECK_ITEM_CODE;
    }

    public String getCHECK_TYPE()
    {
        return CHECK_TYPE;
    }

    public void setCHECK_TYPE(String cHECK_TYPE)
    {
        CHECK_TYPE = cHECK_TYPE;
    }
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((DRUG_ID == null) ? 0 : (Prefix + DRUG_ID).hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TMemo other = (TMemo) obj;
        if (DRUG_ID == null) {
            if (other.DRUG_ID != null)
                return false;
        } else if (!(Prefix + DRUG_ID).equals(Prefix + other.DRUG_ID))
            return false;
        return true;
    }
}
