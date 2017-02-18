package com.ts.entity.pdss.pdss.RSBeans;

import java.util.ArrayList;
import java.util.List;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugIvEffect;

/**
 * @description 药品配伍审查结果
 */

public class TDrugIvEffectRslt extends TBaseResult implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    public TDrugIvEffectRslt()
    {
        setVersion(2);
    }
    private TPatOrderDrug       drug1 ;
    private TPatOrderDrug       drug2 ;
    //todo null
    private List<TDrugIvEffect> dies = new ArrayList<TDrugIvEffect>() ;
    /* 医嘱序号2*/
    private String recMainNo2 = "";
    /* 医嘱子序号2 */ 
    private String recSubNo2 = "";
    /**
     * 添加结果项目 
     * @param adrug1
     * @param adrug2
     * @param adies
     */
    public void addIvEffect(TPatOrderDrug adrug1, TPatOrderDrug adrug2, List<TDrugIvEffect> adies)
    {
        drug1 = (adrug1);
        this.recMainNo = adrug1.getRecMainNo();
        this.recSubNo  = adrug1.getRecSubNo();
        drug2 = (adrug2);
        this.recMainNo2 = adrug2.getRecMainNo();
        this.recSubNo2  = adrug2.getRecSubNo();
        dies   = adies;
        alertLevel = "";
        alertHint  = "";
        alertCause = "";
        for(int i = 0 ;i<adies.size() ; i++)
        {
            TDrugIvEffect adie = adies.get(i);
            if ("0".equals(adie.getRESULT_ID()) || "1".equals(adie.getRESULT_ID()))
            {
                alertLevel = "R";
                alertHint  = adie.getREFER_INFO();
                alertCause = adie.getREF_SOURCE();
            }
            else
            {
                if(!"R".equals(alertLevel))
                {
                	alertLevel = "Y";
                	alertHint  = adie.getREFER_INFO();
                    alertCause = adie.getREF_SOURCE();
                }
                    
            }
        }
    }
    
    /**
     * 
     * @return
     */
    public List<TDrugIvEffect> getTDrugIvEffevtList()
    {
    	return this.dies;
    }
    /**
     * 返回数据集
     * @return
     */
    public TDrugIvEffect[] getTDrugIvEffect()
    {
        return (TDrugIvEffect[])this.dies.toArray(new TDrugIvEffect[0]);
    }
    public TPatOrderDrug getPatOrderDrug1()
    {
        return drug1;
    }

    public TPatOrderDrug getPatOrderDrug2()
    {
        return drug2;
    }

    public String getRecMainNo2()
    {
        return recMainNo2;
    }

    public void setRecMainNo2(String recMainNo2)
    {
        this.recMainNo2 = recMainNo2;
    }

    public String getRecSubNo2()
    {
        return recSubNo2;
    }

    public void setRecSubNo2(String recSubNo2)
    {
        this.recSubNo2 = recSubNo2;
    }
}