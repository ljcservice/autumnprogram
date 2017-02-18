package com.ts.entity.pdss.CustomAudit.RSBeans;

import java.util.ArrayList;
import java.util.List;

import com.ts.entity.pdss.CustomAudit.Beans.TCustomAuditInfo;

public class TCustomAuditRslt
{

    private List<TCustomAuditInfo> CA = new ArrayList<TCustomAuditInfo>();
    
    private int ChkRedCount    = 0;
    private int ChkYellowCount = 0;
    private int ChkBlueCount   = 0;
    
    public void addCustomAudi(TCustomAuditInfo cai)
    {
        CA.add(cai);
    }
    
    public TCustomAuditInfo[] getTCustomAuditInfo()
    {
        return (TCustomAuditInfo[]) CA.toArray(new TCustomAuditInfo[0]) ;
    }
}
