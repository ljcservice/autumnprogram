package com.ts.service.matcher;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

/**
 * 基本信息委会
 * @author autumn
 *
 */
public interface IBaseInfoManagerService
{

    /**
     * 科室查询
     * @param page
     * @return
     */
    public List<PageData> showByDeptDict(Page page) throws Exception ;
    
    public List<PageData> showByDeptDict(PageData pd) throws Exception ;
    
    /**
     * 科室添加
     * @param pd
     * @return
     */
    public int insertByDeptDict(PageData pd) throws Exception ;
    
    /**
     * 科室代码查询
     * @param deptCode
     * @return
     */
    public PageData findDeptByCode(String deptCode) throws Exception ;
   
    /**
     * 科室修改
     * @param pd
     * @return
     */
    public int updateByDeptDict(PageData pd) throws Exception ;
    
    /**
     * 科室代码 删除
     * @param deptCode
     * @return
     */
    public int deleteByDeptDict(String deptCode) throws Exception ;
    
    
    /**
     * 医生查询
     * @param page
     * @return
     */
    public List<PageData> showByDoctorDict(Page page) throws Exception ;
    public List<PageData> showByDoctorDict(PageData pd) throws Exception ;
    
    /**
     * 医生添加
     * @param pd
     * @return
     */
    public int insertByDoctorDict(PageData pd) throws Exception ;
    
    /**
     * 医生代码查询
     * @param DoctorCode
     * @return
     */
    public PageData findDoctorByCode(String DoctorCode) throws Exception ;
   
    /**
     * 医生修改
     * @param pd
     * @return
     */
    public int updateByDoctorDict(PageData pd) throws Exception ;
    
    /**
     * 医生代码 删除
     * @param DoctorCode
     * @return
     */
    public int deleteByDoctorDict(String DoctorCode) throws Exception ;
}
