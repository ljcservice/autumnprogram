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
    public List<PageData> DRANO001(PageData pd) throws Exception
    {
        List<PageData> entity =  null;
        if("".equals(pd.getString("antitype")))
        {
            entity =  (List<PageData>) daoPH.findForList("IASPatInfoSCDMapper.findDeptDDD", pd);
        }
        if("1".equals(pd.getString("antitype")))//限制级抗菌药
        {
            entity =  (List<PageData>) daoPH.findForList("IASPatInfoSCDMapper.findDeptDDDLimit", pd);
        }
        if("2".equals(pd.getString("antitype")))//特殊级抗菌药
        {
            entity =  (List<PageData>) daoPH.findForList("IASPatInfoSCDMapper.findDeptDDDSpec", pd);
        }
        return entity;
    }

    @Override
    public PageData DRANO001sum(PageData pd) throws Exception 
    {
        PageData entity = null;
        if("".equals(pd.getString("antitype")))
        {
            entity =   (PageData)daoPH.findForObject("IASPatInfoSCDMapper.findDeptDDDBySum", pd);
        }
        if("1".equals(pd.getString("antitype")))//限制级抗菌药
        {
            entity =   (PageData)daoPH.findForObject("IASPatInfoSCDMapper.findDeptDDDLimitBySum", pd);
        }
        if("2".equals(pd.getString("antitype")))//特殊级抗菌药
        {
            entity =   (PageData)daoPH.findForObject("IASPatInfoSCDMapper.findDeptDDDSpecBySum", pd);
        }
        return entity;
    }

    @Override
    public List<PageData> DRANO002(Page page) throws Exception
    {
        return (List<PageData>)daoPH.findForList("IASPatInfoSCDMapper.findDeptDoctDDDPage", page);
    }

    @Override
    public List<PageData> DRANO003(Page page) throws Exception
    {
        return (List<PageData>)daoPH.findForList("IASPatInfoSCDMapper.findDeptDoctPatDDDPage", page);
    }

    @Override
    public List<PageData> DRANO005(PageData pd) throws Exception
    {
        return (List<PageData>)daoPH.findForList("IASPatInfoSCDMapper.findDRANO005", pd);
    }

    @Override
    public PageData DRANO005sum(PageData pd) throws Exception
    {
        return  (PageData)daoPH.findForObject("IASPatInfoSCDMapper.findDRANO005sum", pd);
    }

    @Override
    public List<PageData> DRANO006(PageData pd) throws Exception
    {
        return (List<PageData>)daoPH.findForList("IASPatInfoSCDMapper.findDRANO006", pd);
    }

    @Override
    public PageData DRANO006sum(PageData pd) throws Exception
    {
        return  (PageData)daoPH.findForObject("IASPatInfoSCDMapper.findDRANO006sum", pd);
    }

    @Override
    public List<PageData> DRNO003(PageData pd) throws Exception
    {
        
        return (List<PageData>)daoPH.findForList("IASPatInfoSCDMapper.findDRNO003", pd);
    }
}
