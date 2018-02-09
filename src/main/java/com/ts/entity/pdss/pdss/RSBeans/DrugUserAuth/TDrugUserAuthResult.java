package com.ts.entity.pdss.pdss.RSBeans.DrugUserAuth;

import java.util.ArrayList;
import java.util.List;

import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.RSBeans.TBaseResult;

/**
 * 药物授权控制 结果实体
 * @author autumn
 *
 */
public class TDrugUserAuthResult extends TBaseResult
{
    private static final long serialVersionUID = 1L;
    
    private TDrug tdrug ; 
    
    /* 结果集 */
    private List<TDrugUserAuthRslt> duarslt =  new ArrayList<TDrugUserAuthRslt>();

    public void addDrugUserAuth(TDrug tdrug , TDrugUserAuthRslt duaRslt )
    {
        this.tdrug = tdrug ; 
        duarslt.add(duaRslt);
        this.alertLevel = "R";
    }
    
    public void addDrugUserAuth(TDrugUserAuthRslt duaRslt )
    {
        this.duarslt.add(duaRslt);
        this.alertLevel = "R";
    }
    
    public TDrug getTdrug()
    {
        return tdrug;
    }

    public void setTdrug(TDrug tdrug)
    {
        this.tdrug = tdrug;
    }

    public List<TDrugUserAuthRslt> getDuarslt()
    {
        return duarslt;
    }

    public void setDuarslt(List<TDrugUserAuthRslt> duarslt)
    {
        this.duarslt = duarslt;
    }
    
}
