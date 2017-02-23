package com.ts.service.pdss.pdss.manager;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.entity.pdss.pdss.RSBeans.TMedicareRslt;

public interface IMedicareChecker 
{
    public TDrugSecurityRslt Check(TPatientOrder po);
    public TMedicareRslt Check(String DrugID, String DiagnoseCode);
}
