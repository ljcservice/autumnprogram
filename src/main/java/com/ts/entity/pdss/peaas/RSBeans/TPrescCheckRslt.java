package com.ts.entity.pdss.peaas.RSBeans;


import java.util.ArrayList;
import java.util.List;

import com.ts.entity.pdss.pdss.RSBeans.TCheckResultCollection;
import com.ts.entity.pdss.peaas.Beans.TPrescPatMasterBean;

/**
 * 返回处方审核结果 
 * @author Administrator
 *
 */
public class TPrescCheckRslt
{
    /* 审查总接口  */
    private TCheckResultCollection TCRC ;
    /* 门诊病人所有处方信息  */
    private TPrescPatMasterBean pmb;
    /* 药品信息 */
    private List<TDrugPrescCheckRslt> DrugPrescChcek = new ArrayList<TDrugPrescCheckRslt>();
    
    private List<String> PrescAllInfo = new ArrayList<String>();
     
//    /* 结果  */
//    private Map<String, String> CheckRL =  new HashMap<String, String>();
//    /*  */
//    private Map<String, String> DoctorRec = new HashMap<String, String>(); 

    public void addDrugPrescCheck(TDrugPrescCheckRslt dpc)
    {
        this.DrugPrescChcek.add(dpc);
    }
    
    public TDrugPrescCheckRslt[]  getTDrugPrescCheckRslt()
    {
        return (TDrugPrescCheckRslt[]) this.DrugPrescChcek.toArray(new TDrugPrescCheckRslt[0]);
    }
    
    public TPrescPatMasterBean getPmb()
    {
        return pmb;
    }

    public void setPmb(TPrescPatMasterBean pmb)
    {
        this.pmb = pmb;
    }

    public TCheckResultCollection getTCRC()
    {
        return TCRC;
    }

    public void setTCRC(TCheckResultCollection tCRC)
    {
        TCRC = tCRC;
    }
    
    public void addPrescAllInfo(String PrescInfo)
    {
        this.PrescAllInfo.add(PrescInfo);
    }
    
    /**
     * 
     * @return
     */
    public String[] getPrescAllInfo()
    {
        return (String[])this.PrescAllInfo.toArray(new String[0]); 
    }
}
