package com.ts.entity.pdss.pdss.RSBeans.DrugUserAuth;

import java.io.Serializable;

import com.ts.entity.pdss.pdss.Beans.DrugUseAuth.TCkDrugUserAuth;

/**
 * 药品授权控制
 * @author autumn
 *
 */
public class TDrugUserAuthRslt implements Serializable
{

    private static final long serialVersionUID = 1L;
   
    private String QAInfo;
    
    private TCkDrugUserAuth  dua ;

    public String getQAInfo()
    {
        return QAInfo;
    }

    public void setQAInfo(String qAInfo)
    {
        QAInfo = qAInfo;
    }

    public TCkDrugUserAuth getDua()
    {
        return dua;
    }

    public void setDua(TCkDrugUserAuth dua)
    {
        this.dua = dua;
    }

}
