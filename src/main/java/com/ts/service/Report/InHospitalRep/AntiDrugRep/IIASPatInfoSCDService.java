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
    public List<PageData> DRANO001(PageData pd)throws Exception ;
    
    /**
     * 抗菌药使用强度及使用率 合计
     * @param pd
     * @return
     */
    public PageData DRANO001sum(PageData pd) throws Exception ;
    
    
    /**
     * 抗菌药使用强度 科室医生 (医生抗菌药使用强度及使用)
     * @param page
     * @return
     * @throws Exception
     */
    public List<PageData> DRANO002(Page page) throws Exception;
    
    /**
     * 抗菌药使用强度 科室医生患者 (医生抗菌药使用强度及使用)
     * @param page
     * @return
     * @throws Exception
     */
    public List<PageData> DRANO003(Page page) throws Exception;
    
    
    /**
     * 住院患者抗菌药物费用比 
     * @param pd
     * @return
     */
    public List<PageData> DRANO005(PageData pd)throws Exception ;
    
    /**
     * 住院患者抗菌药物费用比 合计
     * @param pd
     * @return
     */
    public PageData DRANO005sum(PageData pd) throws Exception ;
    
    
    /**
     * 抗菌药品种数及使用率
     * @param pd
     * @return
     */
    public List<PageData> DRANO006(PageData pd)throws Exception;
    
    /**
     * 抗菌药品种数及使用率 合计
     * @param pd
     * @return
     */
    public PageData DRANO006sum(PageData pd) throws Exception;
    
    
    /**
     * 科室费用占比统计
     * @param pd
     * @return
     */
    public List<PageData> DRNO003(PageData pd)throws Exception;
    
    /**
     * 医生费用占比统计
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> DRNO008(PageData pd) throws Exception;
}
