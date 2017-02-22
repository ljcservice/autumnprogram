package com.hitzd.WebPage.Impl;

import java.util.LinkedHashMap;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.WebPage.PageView;
import com.hitzd.WebPage.QueryResult;
import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.persistent.Persistent4DB;

/**
 * 分页基类 
 * @author Administrator
 *
 */
public class BasePageBean extends Persistent4DB
{
    
    /**
     *  分页操作
     * @param maxresult    一页显示记录数
     * @param currentpage  页码
     * @param queryCode    系统代码
     * @param wheres       条件 
     * @param value        参数值
     * @param orders       排序
     * @param tableName    表名
     * @param fields       字段名称 
     * @return
     */
    @SuppressWarnings ("unchecked")
    public PageView<TCommonRecord> getScrollData(int maxresult, int currentpage, String queryCode
            ,String wheres,Object[] value ,LinkedHashMap<String, String> orders,String tableName,String fields)
    {
        setQueryCode(queryCode);
        /* 分页数据集合  */
        QueryResult<TCommonRecord> qr = new QueryResult<TCommonRecord>();
        /* 页码 */
        currentpage = currentpage < 1 ? 1 : currentpage;
        /* 一页显示记录数 */
        maxresult   = maxresult < 1? 12 : maxresult;
        PageView<TCommonRecord>  pageView = new PageView<TCommonRecord>(maxresult,currentpage);
        /* 数据总数  */
        StringBuffer countSql = new StringBuffer();
        countSql.append("select count(*) total from ").append(tableName).append(" where 1=1 ").append(wheres);
        /* 数据总数 */
        qr.setTotalrecord(((TCommonRecord)query.queryForObject(countSql.toString(),value == null ? new Object[]{} : value,new CommonMapper())).getInt("total"));
        /* 当总页数小于当前页  当前页设置为 第一页 */
        if(((qr.getTotalrecord() / maxresult) + ((qr.getTotalrecord() % maxresult) > 0 ? 1 : 0 )) < currentpage)
        {
            pageView.setCurrentpage(1);
        }
        Integer firstindex = (pageView.getCurrentpage()-1) * pageView.getMaxresult();
        Integer lastindex  = (pageView.getCurrentpage()) * pageView.getMaxresult();
        /* 是否指定字段查询  */
        fields  = fields == null? "*": fields;
        /* 分页数据 */
        StringBuffer pageSql = new StringBuffer("select * from (select t.* ,rownum rn from (select ").append(fields).append(" from ");
        pageSql.append(tableName).append(" where 1=1 ").append(wheres);
        pageSql.append(getOrder(orders));
        pageSql.append(") t where rownum <= ").append(lastindex.toString()).append(") b where b.rn > ").append(firstindex.toString());
        /*  分页数据  */
        qr.setResultlist(query.query(pageSql.toString(),value == null?new Object[]{}:value, new CommonMapper()));
        pageView.setQueryResult(qr);
        return pageView;
    }
    
    public PageView<TCommonRecord> getScrollData(int maxresult, int currentpage, String queryCode
            ,String wheres,Object[] value ,LinkedHashMap<String, String> orders,String tableName)
    {
        return getScrollData( maxresult,  currentpage,  queryCode
                , wheres, value , orders, tableName ,null);
    }

    public PageView<TCommonRecord> getScrollData(int maxresult, int currentpage,String queryCode, String tableName)
    {
        return getScrollData(maxresult, currentpage, queryCode,null,null ,null, tableName,null);
    }
    
    public PageView<TCommonRecord> getScrollData(int maxresult, int currentpage,String queryCode,String wheres, String tableName)
    {
        return getScrollData(maxresult, currentpage, queryCode,wheres,null ,null, tableName,null);
    }
    
    public PageView<TCommonRecord> getScrollData(int maxresult, int currentpage,String queryCode,String wheres,LinkedHashMap<String
            , String> orders, String tableName)
    {
        return getScrollData(maxresult, currentpage, queryCode,wheres,null ,orders, tableName,null);
    }
    
    public PageView<TCommonRecord> getScrollData(int maxresult, int currentpage,String queryCode,String wheres
                ,Object[] value , String tableName)
    {
        return getScrollData(maxresult, currentpage, queryCode,wheres,value ,null, tableName,null);
    }
    
    /**
     * 构建排序方法 
     * @param orders
     * @return
     */
    private  String getOrder(LinkedHashMap<String, String> orders)
    {
        StringBuffer strOrder = new StringBuffer("");
        if(orders!=null&&orders.size()>0)
        {
            strOrder.append(" order by ");
            for(String order :orders.keySet())
            {
                strOrder.append(order).append(" ").append(orders.get(order)).append(",");
            }
            strOrder.deleteCharAt(strOrder.length()-1);
        }
        return strOrder.toString();
    }
    
}
