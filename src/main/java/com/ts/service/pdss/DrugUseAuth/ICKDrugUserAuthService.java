package com.ts.service.pdss.DrugUseAuth;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.pdss.DrugUseAuth.TCkDrugUserAuth;

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
    public void addCKDrugUserAuth(TCkDrugUserAuth entity);
    
    /**
     * 删除
     * @param id
     */
    public void deleteCKDrugUserAuth(String id);
    
    /**
     * 检索药物授权
     * @param page
     * @return
     */
    public List<TCkDrugUserAuth> selectDrugUserAuthPage(Page page);
    
    /**
     * 检索用id 
     * @param id
     * @return
     */
    public TCkDrugUserAuth  findByid(String id );
    
    /**
     * 修改
     * @param entity
     * @return
     */
    public TCkDrugUserAuth  modifyByCKDrugUserAuth(TCkDrugUserAuth entity);
}
