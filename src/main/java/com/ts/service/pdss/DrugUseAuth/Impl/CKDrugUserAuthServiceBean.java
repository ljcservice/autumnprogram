package com.ts.service.pdss.DrugUseAuth.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.entity.pdss.DrugUseAuth.TCkDrugUserAuth;
import com.ts.service.pdss.DrugUseAuth.ICKDrugUserAuthService;

@Service("cKDrugUserAuthServiceBean")
@Transactional
public class CKDrugUserAuthServiceBean implements ICKDrugUserAuthService
{
    @Resource(name="daoSupportPH")  
    private DAO daoPH ;
    
    @Override
    public void addCKDrugUserAuth(TCkDrugUserAuth entity)
    {
        

    }

    @Override
    public void deleteCKDrugUserAuth(String id)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public List<TCkDrugUserAuth> selectDrugUserAuthPage(Page page)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TCkDrugUserAuth findByid(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TCkDrugUserAuth modifyByCKDrugUserAuth(TCkDrugUserAuth entity)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
