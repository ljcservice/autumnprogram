package com.ts.service;


/**
 * Author: apachexiong
 * Date: 7/26/13
 * Time: 11:58 AM
 * Package: com.hitzd.his.serivces
 * this service is used for white and black list
 */
public interface IWhiteAndBlackFilterServ {
    /**
     * 白名单校验
     * @param reviewLevelCode
     * @param filterValues
     * @return
     */
    public boolean whiteAssert(String reviewLevelCode, String[] filterValues );

    /**
     * 黑名单校验
     * @param reviewLevelCode
     * @param filterValues
     * @return
     */
    public boolean blackAssert(String reviewLevelCode, String[] filterValues );


    /**
     *得到1-12检查的天数
     */
    public int get1_12CheckDay(String wBParamId);




}
