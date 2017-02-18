package com.ts.service.pdss.pdss.manager;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;

/**
 * 特殊人群
 * @author liujc
 *
 */
public interface IDrugSpecPeopleChecker
{
    /**
     * 
     * 特殊人群审查
     * @param po
     * @return
     */
    public TDrugSecurityRslt Check(TPatientOrder po);

	public TDrugSecurityRslt Check(String[] drugIds, String birthDay,
			String patType, String isLiverWhole, String isKidneyWhole);
    
}
