package com.ts.service.pdss.pdss.manager;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;


/**
 * 用药途径审查
 * @author liujc
 *
 */
public interface IDrugAdministrationChecker
{
    /**
     * 用药途径审查
     * @param po 医嘱对象
     * @return
     */
    public TDrugSecurityRslt Check(TPatientOrder po );

	public TDrugSecurityRslt Check(String[] drugIds, String[] adminIds);
	
	
	
}
