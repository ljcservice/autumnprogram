package com.ts.service.matcher.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPH;
import com.ts.entity.Page;
import com.ts.service.matcher.IBaseInfoManagerService;
import com.ts.util.PageData;

@Service("baseInfoManagerServiceBean")
public class BaseInfoManagerServiceBean implements IBaseInfoManagerService
{
    @Resource(name="daoSupportPH")
    private DaoSupportPH dao;
    
    
    @Override
    public List<PageData> showByDeptDict(Page page) throws Exception 
    {
        return (List<PageData>) dao.findForList("DEPT_DICTMapper.queryDeptDictPage",page);
    }

    @Override
    public List<PageData> showByDeptDict(PageData pd) throws Exception
    {
        return (List<PageData>) dao.findForList("DEPT_DICTMapper.queryDeptDict",pd);
    }
    
    @Override
    public int insertByDeptDict(PageData pd) throws Exception 
    {
        
        return (int) dao.save("DEPT_DICTMapper.insertDeptDict", pd);
    }

    @Override
    public PageData findDeptByCode(String deptCode) throws Exception
    { 
        return (PageData) dao.findForObject("DEPT_DICTMapper.findDeptByCode", deptCode);
    }

    @Override
    public int updateByDeptDict(PageData pd) throws Exception
    {
        return (int) dao.update("DEPT_DICTMapper.updateDeptDict", pd);
    }

    @Override
    public int deleteByDeptDict(String deptCode) throws Exception
    {
        return (int) dao.delete("DEPT_DICTMapper.delDeptDict", deptCode);
    }

    @Override
    public List<PageData> showByDoctorDict(Page page) throws Exception
    {
        return (List<PageData>) dao.findForList("STAFF_DICTMapper.queryDoctorDictPage",page);
    }

    @Override
    public List<PageData> showByDoctorDict(PageData pd) throws Exception
    {
        return (List<PageData>) dao.findForList("STAFF_DICTMapper.queryDoctorDict",pd);
    }
    
    @Override
    public int insertByDoctorDict(PageData pd) throws Exception
    {
        return (int) dao.save("STAFF_DICTMapper.insertDoctorDict", pd);
    }

    @Override
    public PageData findDoctorByCode(String DoctorCode) throws Exception
    {
        return (PageData) dao.findForObject("STAFF_DICTMapper.findDoctorByCode", DoctorCode);
    }

    @Override
    public int updateByDoctorDict(PageData pd) throws Exception
    {
        return (int) dao.update("STAFF_DICTMapper.updateDoctorDict", pd);
    }

    @Override
    public int deleteByDoctorDict(String DoctorCode) throws Exception
    {
        return (int) dao.delete("STAFF_DICTMapper.delDoctorDict", DoctorCode);
    }
}
