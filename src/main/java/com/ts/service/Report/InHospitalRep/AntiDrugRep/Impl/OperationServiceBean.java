package com.ts.service.Report.InHospitalRep.AntiDrugRep.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.service.Report.InHospitalRep.AntiDrugRep.IOperationService;
import com.ts.util.PageData;

@Service("operationServiceBean")
public class OperationServiceBean  implements IOperationService
{
    @Resource(name="daoSupportPH")
    private DAO daoPH ;
    
    @Override
    public List<PageData> DRANO004(PageData pd) throws Exception
    {
        List<PageData> pds = null;
        //人次查询
        if("persion".equals(pd.getString("findType")))
        {
            pds = (List<PageData>)daoPH.findForList("OperationRepMapper.findDeptOperPersionInfo", pd);
        }
        //按例数查询 count
        else{
            pds = (List<PageData>)daoPH.findForList("OperationRepMapper.findDeptOperCountInfo", pd);
        }
        return pds;
    }

    @Override
    public List<PageData> DRANO007(Page page) throws Exception
    {
        List<PageData> pd = (List<PageData>)daoPH.findForList("OperationRepMapper.findDRANO007Page", page);
        return pd;
    }
    
    @Override
    public void updateDRANO007(PageData pd) throws Exception
    {
        daoPH.update("OperationRepMapper.updateDRANO007", pd);
    }
    
    @Override
    public List<PageData> DRANO008(PageData pd) throws Exception
    {
        return (List<PageData>)daoPH.findForList("OperationRepMapper.findDRANO008", pd);
    }
    
    @Override
    public List<PageData> DRANO009(PageData pd) throws Exception
    {
        return (List<PageData>)daoPH.findForList("OperationRepMapper.findDRANO009", pd);
    }

	@Override
	public PageData DRANO007OperationById(PageData pd) throws Exception {
		return (PageData)daoPH.findForObject("OperationRepMapper.findOperationRepById",pd);
	}
}
