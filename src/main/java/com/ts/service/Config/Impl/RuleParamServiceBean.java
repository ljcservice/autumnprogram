package com.ts.service.Config.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.service.Config.RuleParamService;
import com.ts.util.PageData;

/**
 * 参数维护
 * @author autumn
 */
@Service("ruleParamServiceBean")
public class RuleParamServiceBean implements RuleParamService
{
    @Resource(name="daoSupport")
    private DAO dao ;

    @Override
    public List<PageData> listRuleParam(Page page) throws Exception
    {
        return (List<PageData>) dao.findForList("RuleParamMapper.findByRuleParam", page);
    }

    @Override
    public void updateRuleParam(PageData pd) throws Exception
    {
        dao.update("RuleParamMapper.edit", pd);
    }

    @Override
    public void insertRuleParam(PageData pd) throws Exception
    {
        dao.save("RuleParamMapper.save", pd);
    }

    @Override
    public void deleteRuleParam(PageData pd) throws Exception
    {
        dao.delete("RuleParamMapper.delete", pd);
    }
}
