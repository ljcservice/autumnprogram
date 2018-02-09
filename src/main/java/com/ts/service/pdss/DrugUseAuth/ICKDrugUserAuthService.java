package com.ts.service.pdss.DrugUseAuth;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.pdss.pdss.Beans.DrugUseAuth.TCkDrugUserAuth;
import com.ts.util.PageData;

/**
 * 药物授权控制
 * @author autumn
 *
 */
public interface ICKDrugUserAuthService
{
    /**
     * 添加 药物授控
     * @param entity
     */
    public void addCKDrugUserAuth(PageData pd) throws Exception ;
    
    /**
     * 删除
     * @param id
     */
    public void deleteCKDrugUserAuth(String id) throws Exception ;
    
    /** 
     * 检索药物授权
     * @param page
     * @return
     */
    public List<TCkDrugUserAuth> selectDrugUserAuthPage(Page page) throws Exception ;
    
    /**
     * 检索用id 
     * @param id
     * @return
     */
    public TCkDrugUserAuth  findByid(String id ) throws Exception ;
    
    /**
     * 返回是否存在 检索数据 
     * @param entity
     * @return true : 存在 ， false ： 不存在   
     * @throws Exception
     */
    public boolean hasDrugUserAuth(PageData entity) throws Exception;
    
    /**
     * 修改
     * @param entity
     * @return
     */
    public PageData modifyByCKDrugUserAuth(PageData entity) throws Exception;
    
    /**
     * 查询科室
     * @param page
     * @return
     */
    public PageData queryDept(Page page) throws Exception ;
    
    /**
     * 查询医生
     * @param page
     * @return
     */
    public PageData queryDoctor(Page page) throws Exception ;
}
