package com.ts.service.Config.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ts.dao.DaoSupportPH;import com.ts.entity.Page;
import com.ts.service.Config.IConfigDictService;
import com.ts.util.PageData;

@Transactional
@Service("configDictServiceBean")
public class ConfigDictServiceBean implements IConfigDictService
{

    @Resource(name="daoSupportPH")
    private DaoSupportPH daoph;
    
    @Override
    public List<PageData> findConfigDictByType(String type) throws Exception 
    {
        return ( List<PageData>) daoph.findForList("Config_DictMapper.findConfigDictByType", type);
    }

}
