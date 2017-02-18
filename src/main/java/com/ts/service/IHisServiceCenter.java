package com.ts.service;

import com.hitzd.his.Beans.TOperationDict;


/**
 * 用于所有关于his数据的操作接口
 * @author Administrator
 *
 */
public interface IHisServiceCenter
{
    /**
     * 获得手术列表
     * @return
     */
    public TOperationDict[] getOperationDict();
}
