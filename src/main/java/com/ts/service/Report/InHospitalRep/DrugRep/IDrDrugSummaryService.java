package com.ts.service.Report.InHospitalRep.DrugRep;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

/**
 * 药品表
 * @author autumn
 *
 */
public interface IDrDrugSummaryService
{

    /**
     * 住院药物使用统计
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> DRNO001(Page page) throws Exception;
    
    /**
     * 科室药物使用统计
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> DRNO002(Page page) throws Exception;
    
    /**
     * 医生药物使用统计
     * @param page
     * @return
     * @throws Exception
     */
    public List<PageData> DRNO004(Page page) throws Exception;
    
    /**
     * 医生用药排名
     * @param page
     * @return
     * @throws Exception
     */
    public List<PageData> DRNO005(Page page) throws Exception;
    
    /**
     * 医生用药排名
     * @param page
     * @return
     * @throws Exception
     */
    public List<PageData> DRNO006(Page page) throws Exception;
    
    /**
     * 药物使用按日统计
     * @param page
     * @return
     * @throws Exception
     */
    public List<PageData> DRNO007(Page page) throws Exception;
}
