package com.ts.service.pdss.pdss.manager;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;

/**
 * 审核 药物授权控制
 * @author autumn
 *
 */
public interface IDrugUserAuthChecker
{

    /**
     * 审核
     * @param po
     * @return
     */
    public TDrugSecurityRslt Check(TPatientOrder po);
}
