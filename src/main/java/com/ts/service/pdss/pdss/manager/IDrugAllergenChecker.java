package com.ts.service.pdss.pdss.manager;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;


/**
 * 过敏药物审查
 * @author liujc
 *
 */
public interface IDrugAllergenChecker
{
    /**
     *  过敏药物审查
     * @param po
     * @return
     */
    public TDrugSecurityRslt Check(TPatientOrder po);

	public TDrugSecurityRslt Check(String[] drugIds, String[] sensitIds);
	
	
}
