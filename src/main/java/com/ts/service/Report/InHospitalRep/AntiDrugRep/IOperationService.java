package com.ts.service.Report.InHospitalRep.AntiDrugRep;

import java.util.List;

import com.ts.entity.Page;
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
    
    /**
     * 手术信息维护
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> DRANO007(Page page) throws Exception;
    
    /**
     * 手术信息 ID 获取
     * @param pd
     * @return
     * @throws Exception
     */
    public PageData DRANO007OperationById(PageData pd)throws Exception;
    
    /**
     * 手术信息维护更新
     * @param pd
     * @throws Exception
     */
    public void updateDRANO007(PageData pd) throws Exception;
    
    /**
     * 科室手术信息使用率
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> DRANO008(PageData pd) throws Exception;
    
    
    /**
     * 医生手术信息使用率
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> DRANO009(PageData pd) throws Exception;
}
