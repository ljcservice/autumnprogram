package com.ts.service.Report.InHospitalRep.AntiDrugRep;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

/**
 * 病人住院费用基本信息 (药品, ddd) 
 * @author autumn
 *
 */
public interface IIASPatInfoSCDService
{

    /**
     * 抗菌药使用强度及使用率 
     * @param pd
     * @return
     */
    public List<PageData> DRANO01(PageData pd)throws Exception ;
    
    /**
     * 抗菌药使用强度及使用率 合计
     * @param pd
     * @return
     */
    public PageData DRANO01sum(PageData pd) throws Exception ;
    
    
    /**
     * 抗菌药使用强度 科室医生 (医生抗菌药使用强度及使用)
     * @param page
     * @return
     * @throws Exception
     */
    public List<PageData> DRANO02(Page page) throws Exception;
    
    /**
     * 抗菌药使用强度 科室医生患者 (医生抗菌药使用强度及使用)
     * @param page
     * @return
     * @throws Exception
     */
    public List<PageData> DRANO03(Page page) throws Exception;
    
}
