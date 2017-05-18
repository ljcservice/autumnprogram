package com.ts.service.Report.InHospitalRep.AntiDrugRep;

import java.util.List;

import com.ts.util.PageData;

/**
 * 手术信息
 * @author autumn
 *
 */
public interface IOperationService
{

    /**
     * 围手术期抗菌药物使用情况
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> DRANO004(PageData pd) throws Exception;
    
}
