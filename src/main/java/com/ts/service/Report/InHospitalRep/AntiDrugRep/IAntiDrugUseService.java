package com.ts.service.Report.InHospitalRep.AntiDrugRep;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

/**
 * 抗菌分级管理
 * @author autumn
 *
 */
public interface IAntiDrugUseService
{
    /**
     * 科室医生 
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> DRANO013(Page page)throws Exception; 
    
    /**
     * 科室医生 - 汇总 
     * @param pd
     * @return
     * @throws Exception
     */
    public PageData DRANO013Sum(PageData pd) throws Exception; 
    
    /**
     * 患者
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> DRANO014(Page page)throws Exception; 
}
