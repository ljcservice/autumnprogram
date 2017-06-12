package com.ts.entity.pdss.pdss.RSBeans.ias;

import java.util.ArrayList;
import java.util.List;

import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.RSBeans.TBaseResult;

/**
 * 抗菌药物审核
 * @author autumn
 *
 */
public class TAntiDrugCheckResult extends TBaseResult
{
    private static final long serialVersionUID = 1L;
    
    private TDrug drug ;
    
    List<TAntiDrugRslt> antiRs = new ArrayList<TAntiDrugRslt>();
    
    /**
     * 添加审核结果
     * @param antiRs
     */
    public  void addAntiRs(TDrug drug,TAntiDrugRslt antiRs)
    {
        this.antiRs.add(antiRs);
        this.alertLevel = "R";
        
    }
    
    public void setAntiRsCollection(TDrug drug , List<TAntiDrugRslt> antirs)
    {
        this.drug = drug;
        if(antirs != null) this.antiRs.addAll(antirs);
        this.alertLevel = "R";
    }
    
    public TDrug getDrug()
    {
        return drug;
    }

    public void setDrug(TDrug drug)
    {
        this.drug = drug;
    }

    public List<TAntiDrugRslt> getAntiRs()
    {
        return antiRs;
    }

    public void setAntiRs(List<TAntiDrugRslt> antiRs)
    {
        this.antiRs = antiRs;
    }
}
