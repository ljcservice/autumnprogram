package com.ts.service.Report.InHospitalRep.AntiDrugRep.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.service.Report.InHospitalRep.AntiDrugRep.IIASPatInfoSCDService;
import com.ts.util.PageData;

@Service("iASPatInfoSCDServiceBean")
public class IASPatInfoSCDServiceBean implements IIASPatInfoSCDService
{
    @Resource(name="daoSupportPH")
    private DAO daoPH ;
  
    @Override
    public List<PageData> DRANO01(PageData pd) throws Exception
    {
        List<PageData> entity =  null;
        if("".equals(pd.getString("antitype")))
        {
            entity =  (List<PageData>) daoPH.findForList("IASPatInfoSCD.findDeptDDD", pd);
        }
        if("1".equals(pd.getString("antitype")))//限制级抗菌药
        {
            entity =  (List<PageData>) daoPH.findForList("IASPatInfoSCD.findDeptDDDLimit", pd);
        }
        if("2".equals(pd.getString("antitype")))//特殊级抗菌药
        {
            entity =  (List<PageData>) daoPH.findForList("IASPatInfoSCD.findDeptDDDSpec", pd);
        }
        return entity;
    }

    @Override
    public PageData DRANO01sum(PageData pd) throws Exception 
    {
        PageData entity = null;
        if("".equals(pd.getString("antitype")))
        {
            entity =   (PageData)daoPH.findForObject("IASPatInfoSCD.findDeptDDDBySum", pd);
        }
        if("1".equals(pd.getString("antitype")))//限制级抗菌药
        {
            entity =   (PageData)daoPH.findForObject("IASPatInfoSCD.findDeptDDDLimitBySum", pd);
        }
        if("2".equals(pd.getString("antitype")))//特殊级抗菌药
        {
            entity =   (PageData)daoPH.findForObject("IASPatInfoSCD.findDeptDDDSpecBySum", pd);
        }
        return entity;
    }

    @Override
    public List<PageData> DRANO02(Page page) throws Exception
    {
        return (List<PageData>)daoPH.findForList("IASPatInfoSCD.findDeptDoctDDDPage", page);
    }

    @Override
    public List<PageData> DRANO03(Page page) throws Exception
    {
        return (List<PageData>)daoPH.findForList("IASPatInfoSCD.findDeptDoctPatDDDPage", page);
    }
}
