package com.ts.service.pdss.pdss.manager;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;

/**
 * 抗菌药物审核
 * @author autumn
 *
 */
public interface IAntiDrugSecurityChecker
{

    /**
     *  抗菌药物审核
     * @param po
     * @return
     */
    public TDrugSecurityRslt Check(TPatientOrder po);
    
}
