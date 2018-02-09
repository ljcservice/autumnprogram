package com.ts.service.pdss.DrugUseAuth.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.entity.pdss.pdss.Beans.DrugUseAuth.TCkDrugUserAuth;
import com.ts.service.pdss.DrugUseAuth.ICKDrugUserAuthService;
import com.ts.util.PageData;

@Transactional
@Service("cKDrugUserAuthServiceBean")
public class CKDrugUserAuthServiceBean implements ICKDrugUserAuthService
{
    @Resource(name="daoSupportPH")  
    private DAO daoPH ;
    
    @Override
    public void addCKDrugUserAuth(PageData pd) throws Exception
    {
        daoPH.save("CKDrugUserAuth.saveDrugUserAuth", pd);
    }

    @Override
    public void deleteCKDrugUserAuth(String id) throws Exception
    {
        daoPH.delete("CKDrugUserAuth.deleteDrugUserAuth", id);
    }

    @Override
    public List<TCkDrugUserAuth> selectDrugUserAuthPage(Page page) throws Exception
    {
        return (List<TCkDrugUserAuth>) daoPH.findForList("CKDrugUserAuth.queryTCkDrugUserAuthPage", page);
    }

    @Override 
    public TCkDrugUserAuth findByid(String id) throws Exception
    {
        return (TCkDrugUserAuth) daoPH.findForObject("CKDrugUserAuth.findDrugUserAuthByid", id);
    }
    
    @Override
    public boolean hasDrugUserAuth(PageData entity) throws Exception
    {
        List<PageData> list = (List<PageData>) daoPH.findForList("CKDrugUserAuth.findDrugUserAuthByIsHavInfo", entity);
        return list== null ||list.size() == 0?false:true;
    }

    @Override
    public PageData modifyByCKDrugUserAuth(PageData entity) throws Exception
    {
        daoPH.update("CKDrugUserAuth.updateDrugUserAuth", entity);
        return entity;
    }

    @Override
    public PageData queryDept(Page page) throws Exception
    {
        return (PageData) daoPH.findForList("CKDrugUserAuth.queryDeptPage", page);
    }

    @Override
    public PageData queryDoctor(Page page) throws Exception
    {
        return (PageData) daoPH.findForList("CKDrugUserAuth.queryStaffPage", page);
    }
    
}
