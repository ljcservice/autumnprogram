package com.ts.service.Report.InHospitalRep.AntiDrugRep.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.service.Report.InHospitalRep.AntiDrugRep.IAntiDrugUseService;
import com.ts.util.PageData;

/**
 * 抗菌药物分级管理报表
 * @author autumn
 *
 */
@Service("antiDrugUseServiceBean")
public class AntiDrugUseServiceBean implements IAntiDrugUseService
{

    @Resource(name="daoSupportPH")  
    private DAO daoPH ;
    
    @Override
    public List<PageData> DRANO013(Page page) throws Exception
    {
        List<PageData> listpd = null;
        if("zl".equals(page.getPd().getString("useType")))
        {
            listpd = (List<PageData>) daoPH.findForList("antiDrugUse.findAntiDrugUseZLPage", page);
        }
        else
        {
            listpd = (List<PageData>) daoPH.findForList("antiDrugUse.findAntiDrugUseYFPage", page);
        }
        return listpd;
    }

    @Override
    public PageData DRANO013Sum(PageData pd) throws Exception
    {
        PageData entity = null;
        if("zl".equals(pd.getString("useType")))
        {
            entity = (PageData) daoPH.findForObject("antiDrugUse.findAntiDrugUseZLSum", pd);
        }else
        {
            entity = (PageData) daoPH.findForObject("antiDrugUse.findAntiDrugUseYFSum", pd);
        }
        return entity;
    }
    
    @Override
    public List<PageData> DRANO014(Page page) throws Exception
    {
        List<PageData> listpd = null;
        if("zl".equals(page.getPd().getString("useType")))
        {
            listpd = (List<PageData>) daoPH.findForList("antiDrugUse.findAntiDrugUsePatZLPage", page);
        }
        else
        {
            listpd = (List<PageData>) daoPH.findForList("antiDrugUse.findAntiDrugUsePatYFPage", page);
        }
        return listpd;
    }

}
