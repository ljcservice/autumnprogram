package com.ts.service.pdss.peaas.manager;

import com.ts.entity.pdss.peaas.RSBeans.TPrescCheckRslt;

/**
 * 不合理处方
 * @author Administrator
 *
 */
public interface IPrescTroubleService
{

    /**
     * 查询不合理处方 
     * @param dept
     * @param doctor
     * @param beginDate
     * @param endDate
     * @param bak
     * @return
     */
    public TPrescCheckRslt[] QueryPrescTrouble(String dept, String doctor , String beginDate , String endDate , String[] bak);
}
