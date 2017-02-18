package com.ts.service.pdss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.persistent.Persistent4DB;
import com.ts.util.SqlBuilder;

/**
 * Author: apachexiong
 * Date: 7/26/13
 * Time: 12:04 PM
 * Package: com.hitzd.his.serivceBeans
 */

@Transactional
@Service("whiteAndWhiteFilterBean")
public class WhiteAndWhiteFilterBean extends Persistent4DB implements IWhiteAndBlackFilterServ {
    private static Logger logger = Logger.getLogger("WhiteAndWhiteFilterBean");

    /**
     * 用于验证白名单中是否含过滤值 传入的分级代码不能含有模糊匹配. 该方法只能用于底层的数据校验
     *
     * @param reviewLevelCode
     * @param filterValues
     * @return
     */
    @Override
    public boolean whiteAssert(String reviewLevelCode, String[] filterValues) {
        return whiteAndBlackAssert(reviewLevelCode,filterValues,"1");
    }

    @Override
    public boolean blackAssert(String reviewLevelCode, String[] filterValues) {
        whiteAndBlackAssert(reviewLevelCode, filterValues, "0");
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private boolean whiteAndBlackAssert(String reviewLevelCode, String[] filterValues, String isWhite){
        //分级代码正确性校验
        if(reviewLevelCode == null || reviewLevelCode.indexOf("*") != -1){
            throw new IllegalArgumentException("您传入了错误的分级代码, reviewLevelCode" + reviewLevelCode);
        }
        int indexOfPoint = reviewLevelCode.indexOf(".");
        String level4 = reviewLevelCode;
        String level1 = "*-*";
        String level2 = reviewLevelCode.replaceAll("-[0-9]*$", "-*");
        String level3 = reviewLevelCode.replaceAll("^[0-9]*-","*-");

        //组建查询Sql
        String sql = "select * from w_b_filter " +
                "where " +
                "state='1' " +
                "and is_white='#is_white#' " +
                "and filter_values='#filter_values#' " +
                "and review_level_code in ('#level1#','#level2#','#level3#','#level4#')";

        sql = new SqlBuilder(sql)
                .setParam("is_white",isWhite)
                .setParam("filter_values",constructAndSortList(filterValues))//通过constructAndSortList来构建值
                .setParam("level1", level1)
                .setParam("level2",level2)
                .setParam("level3",level3)
                .setParam("level4",level4)
                .buildSql();

        setQueryCode("PEAAS");
        List<TCommonRecord> queryResult = query.queryForList(sql);

        //结果判断
        if(queryResult.size()>0)return true;
        return false;

    }

    @Override
    public int get1_12CheckDay(String wBParamId) {
        String sql = "select w_b_param_value from W_B_Error_Param where w_b_param_id='#w_b_param_id#'";
        sql = new SqlBuilder(sql)
                .setParam("w_b_param_id",wBParamId)
                .buildSql();

        setQueryCode("PEAAS");
        int days = ((TCommonRecord)query.queryForObject(sql,new CommonMapper())).getInt("w_b_param_value");
        if(days == 0){
            days = 7;
        }

        return days;
    }

    /**
     *构建过滤值.
     * 先将传入的String数组组成List,然后通过Coolections的sort方法进排序
     * 然后以;作为分隔符，将整个list组成字符串
     * @param filterValues
     * @return
     */
    private String constructAndSortList(String[] filterValues){
        //构建list
        List<String> filterValueList = new ArrayList<String>();
        for(String filterValue: filterValues){
            filterValueList.add(filterValue);
        }

        //给filterValues排序
        Collections.sort(filterValueList);

        //构建String,以;来分割
        StringBuffer filterBuffer = new StringBuffer();
        for(String filterValue: filterValueList){
            filterBuffer.append(filterValue + ";");
        }

        //删除最后一个分号
        filterBuffer.deleteCharAt(filterBuffer.length()-1);

        return filterBuffer.toString();
    }
}
