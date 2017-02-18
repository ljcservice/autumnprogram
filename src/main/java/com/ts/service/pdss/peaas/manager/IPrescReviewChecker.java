package com.ts.service.pdss.peaas.manager;

import java.util.List;

import com.hitzd.DBUtils.TCommonRecord;

/**
 * 处方点评信息
 * @author Administrator
 *
 */
public interface IPrescReviewChecker
{

    /**
     * 点评方法
     * @param po
     * @return
     */
    public TCommonRecord PrescReviewCheck(TCommonRecord t,List<TCommonRecord> presc);
}
