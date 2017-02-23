package com.ts.service.pdss.CustomAudit.services;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.CustomAudit.RSBeans.TDrugCustomSecurityRslt;

/**
 * 自定义审核 
 * @author Administrator
 *
 */
public interface IDrugCustomChkService
{

    /**
     * 客户自定义审核 
     * @param po 医嘱对象 
     * @param Resctype 区分门诊和住院   住院是0 门诊是1 
     * @return
     */
    public TDrugCustomSecurityRslt CheckAll(TPatientOrder po,String Resctype);
}
