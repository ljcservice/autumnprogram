package com.ts.service.pdss.pdss.manager;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;


public interface IDrugIvEffectChecker
{
    public TDrugSecurityRslt Check(TPatientOrder po);

	public TDrugSecurityRslt Check(String[] drugIds, String[] recMainIds,
			String[] administrationIds);
    
}
