package com.ts.service.Config;

import java.util.List;

import com.ts.entity.Page;
import com.ts.util.PageData;

/**
 * 参数维护
 * @author autumn
 *
 */
public interface RuleParamService
{
    /**
     * 分页查询参数
     * @param page
     * @return
     */
    public List<PageData> listRuleParam(Page page) throws Exception;
    
    /**
     * 更新参数
     * @param pd
     */
    public void updateRuleParam(PageData pd) throws Exception ;
    
    /**
     * ruleCode 查询 
     * @param pd
     * @throws Exception
     */
    public PageData findRuleParmByRuleCode(PageData pd) throws Exception;
    
    /**
     * 添加 系统参数表
     * @param pd
     */
    public void insertRuleParam(PageData pd) throws Exception ;
    
    /**
     * 删除 系统参数表
     * @param pd
     */
    public void deleteRuleParam(PageData pd) throws Exception ;
}
