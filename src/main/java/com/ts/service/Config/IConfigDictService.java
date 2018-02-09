package com.ts.service.Config;

import java.util.List;

import com.ts.util.PageData;

/**
 * 通用字段 config_dict
 * @author autumn
 *
 */
public interface IConfigDictService
{

    /**
     * 根据 类型 查询 字典信息 
     * @param type
     * @return
     */
    public List<PageData> findConfigDictByType(String type) throws Exception ;
    
    
}
