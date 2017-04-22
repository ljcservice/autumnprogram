package com.ts.service.pdss.pdss.manager;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;


/**
 * 医院管理类审查
 * @author autumn
 *
 */
public interface IDrugHospitalManageChecker
{
    /**
     * 医院管理
     * @param po
     * @return
     */
    public TDrugSecurityRslt Check(TPatientOrder po);
}
