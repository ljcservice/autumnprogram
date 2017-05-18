package com.ts.service.Report.InHospitalRep.DrugRep.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.service.Report.InHospitalRep.DrugRep.IDrDrugSummaryService;
import com.ts.util.PageData;

/**
 * 住院药品信息表
 * @author autumn
 *
 */
@Service("drDrugSummaryServiceBean")
public class DrDrugSummaryServiceBean implements IDrDrugSummaryService
{
    @Resource(name="daoSupportPH")
    private DAO daoPH ;
    
    @Override
    public List<PageData> DRNO001(Page page) throws Exception
    {
        return (List<PageData>) daoPH.findForList("DRDrugSummaryMapper.findDRNO001Page", page);
    }

    @Override
    public List<PageData> DRNO002(Page page) throws Exception
    {
        return (List<PageData>) daoPH.findForList("DRDrugSummaryMapper.findDRNO002Page", page);
    }

    
    @Override
    public List<PageData> DRNO004(Page page) throws Exception
    {
        return (List<PageData>) daoPH.findForList("DRDrugSummaryMapper.findDRNO004Page", page);
    }
    
    @Override
    public List<PageData> DRNO005(Page page) throws Exception
    {
        return (List<PageData>) daoPH.findForList("DRDrugSummaryMapper.findDRNO005Page", page);
    }
    
    @Override
    public List<PageData> DRNO006(Page page) throws Exception
    {
        return (List<PageData>) daoPH.findForList("DRDrugSummaryMapper.findDRNO006Page", page);
    }
    
    @Override
    public List<PageData> DRNO007(Page page) throws Exception
    {
        return (List<PageData>) daoPH.findForList("DRDrugSummaryMapper.findDRNO007Page", page);
    }
}
