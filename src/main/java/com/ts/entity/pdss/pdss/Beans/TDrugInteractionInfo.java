package com.ts.entity.pdss.pdss.Beans;

import com.hitzd.his.Beans.TBaseBean;


/**
 *  药物相互作用信息
 * @author liujc
 */
public class TDrugInteractionInfo extends TBaseBean
{
	private static final long serialVersionUID = 1L;

	public TDrugInteractionInfo()
    {
    }

    /* 药物相互作用信息ID */
    private String DRUG_INTERACTION_INFO_ID;
    /* 药品成分1 */
    private String INGR_CLASS_ID1;
    /* 药品成分2 */
    private String INGR_CLASS_ID2;
    /* 严重程度 */
    private String INTER_INDI;
    /* 警示信息 */
    private String WAR_INFO;
    /* 作用机理 */
    private String MEC_INFO;
    /* 同用说明 */
    private String MAN_INFO;
    /* 临床试验 */
    private String DIS_INFO;
    /* 文献来源 */
    private String REF_SOURCE;

    public String getDRUG_INTERACTION_INFO_ID()
    {
        return DRUG_INTERACTION_INFO_ID;
    }

    public void setDRUG_INTERACTION_INFO_ID(String dRUG_INTERACTION_INFO_ID)
    {
        DRUG_INTERACTION_INFO_ID = dRUG_INTERACTION_INFO_ID;
    }

    public String getINGR_CLASS_CODE1()
    {
        return INGR_CLASS_ID1;
    }

    public void setINGR_CLASS_CODE1(String iNGR_CLASS_CODE1)
    {
        INGR_CLASS_ID1 = iNGR_CLASS_CODE1;
    }

    public String getINGR_CLASS_CODE2()
    {
        return INGR_CLASS_ID2;
    }

    public void setINGR_CLASS_CODE2(String iNGR_CLASS_CODE2)
    {
        INGR_CLASS_ID2 = iNGR_CLASS_CODE2;
    }

    public String getINTER_INDI()
    {
        return INTER_INDI;
    }

    public void setINTER_INDI(String iNTER_INDI)
    {
        INTER_INDI = iNTER_INDI;
    }

    public String getWAR_INFO()
    {
        return WAR_INFO;
    }

    public void setWAR_INFO(String wAR_INFO)
    {
        WAR_INFO = wAR_INFO;
    }

    public String getMEC_INFO()
    {
        return MEC_INFO;
    }

    public void setMEC_INFO(String mEC_INFO)
    {
        MEC_INFO = mEC_INFO;
    }

    public String getMAN_INFO()
    {
        return MAN_INFO;
    }

    public void setMAN_INFO(String mAN_INFO)
    {
        MAN_INFO = mAN_INFO;
    }

    public String getDIS_INFO()
    {
        return DIS_INFO;
    }

    public void setDIS_INFO(String dIS_INFO)
    {
        DIS_INFO = dIS_INFO;
    }

    public String getREF_SOURCE()
    {
        return REF_SOURCE;
    }

    public void setREF_SOURCE(String rEF_SOURCE)
    {
        REF_SOURCE = rEF_SOURCE;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((INGR_CLASS_ID1 == null) ? 0 : INGR_CLASS_ID1.hashCode());
        result = prime
                * result
                + ((INGR_CLASS_ID2 == null) ? 0 : INGR_CLASS_ID2.hashCode());
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
        TDrugInteractionInfo other = (TDrugInteractionInfo) obj;
        if (INGR_CLASS_ID1 == null) {
            if (other.INGR_CLASS_ID1 != null)
                return false;
        } else if (!INGR_CLASS_ID1.equals(other.INGR_CLASS_ID1))
            return false;
        if (INGR_CLASS_ID2 == null) {
            if (other.INGR_CLASS_ID2 != null)
                return false;
        } else if (!INGR_CLASS_ID2.equals(other.INGR_CLASS_ID2))
            return false;
        return true;
    }

}
