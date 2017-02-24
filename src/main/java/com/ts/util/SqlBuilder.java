package com.ts.util;

import java.util.List;

/**
 * Author: apachexiong
 * Date: 7/23/13
 * Time: 6:48 PM
 * Package: com.hitzd.his.PE.Util
 *
 * use builder pattern to build sql
 * help to build sql
 */
public class SqlBuilder {
    private StringBuffer sql = null;
    private String sqlb = null;

    /**
     * 通过sql语句来构建此对象
     * the constructor of the SqlBuilder
     * sql is the result of sql
     * sqlb is search sql
     * @param sql 传入的Sql语句
     */
    public SqlBuilder(String sql){
        this.sql = new StringBuffer(sql);
        sqlb = sql;
    }

    /**通过名称来设置值
     * name is the key of value
     * replace the key with the value
     * search the key mark with "#key#"
     * @param name the string key
     * @param value the replacement of the key
     * @return SqlBuilder instance
     */
    public SqlBuilder setParam(String name, String value){
        String param = "#" + name + "#";
        int begin = sql.indexOf(param);
        int end = begin + param.length();

        sql = sql.replace(begin,end,value);
        return this;
    }

    /**
     * 设置值
     * build sql with index and the value of the index
     * search the key mark with "#key#"
     * @param index begin with 0
     * @param value the replacement of char
     * @return the SqlBuilder instance
     */
    public SqlBuilder setParam(int index, String value){
        int beginOffset = 0;
        int begin = 0;
        int end = 0;
        while(index-->=0){
            begin = sqlb.indexOf("#",beginOffset);
            end = sqlb.indexOf("#",begin+1);
            beginOffset = end+1;
        }
        String subString = sqlb.substring(begin,end+1);
        int rBegin = sql.indexOf(subString);
        int rEnd = rBegin + subString.length();
        sql.replace(rBegin,rEnd,value);
        return this;

    }

    /**当传入的值为空时,就删除以@开始的,以@结束的行
     * if the value is null or ""
     * search the string mark with "@*#key#*@", and remove it
     * else delete the char '@' and replace '#key#' with value
     * @param name the key of value
     * @param value the replacement value
     * @return the SqlBuilder instance
     */
    public SqlBuilder setIsNullParam(String name, String value){
        String param = "#" + name + "#";
        int begin = sql.indexOf(param);
        int end = begin + param.length();
        int ifBegin = lastIndexOf(sql.toString(),begin,"@");
        int ifEnd = sql.indexOf("@",end);

        if(name == null || "".equals(value)){
            sql.delete(ifBegin,ifEnd+1);
        }else{
            sql.deleteCharAt(ifEnd);//先删除end 后删除begin
            sql.deleteCharAt(ifBegin);
            sql = sql.replace(begin-1,end-1,value); //由于前方删除了一个“@”
        }
        return this;

    }

    /**
     * 设置String的List,并以逗号进行分割
     * @param name
     * @param values
     * @return
     */
    public SqlBuilder setStringList(String name, List<String> values){
        StringBuffer valuesBuffer = new StringBuffer();
        for(String value: values){
            valuesBuffer.append("'" + value.trim() + "',");
        }
        String valuesStringT = valuesBuffer.toString();
        String valuesString = valuesStringT.substring(0,valuesStringT.lastIndexOf(","));

        if(valuesString.length()>0){
            return setParam(name, valuesString);
        }else{
            return setParam(name, "");
        }
    }

    /**
     * 设置可选择的值, 当为空时, 则删除@的数据
     * @param name
     * @param values
     * @return
     */
    public SqlBuilder setIsNullStringArray(String name, String[] values){
        StringBuffer valuesBuffer = new StringBuffer();
        for(String value: values){
            if(value != null && !"".equals(value)){
                valuesBuffer.append("'" + value.trim() + "',");
            }
        }
        String valuesStringT = valuesBuffer.toString();
        //判断是否为空
        if(valuesStringT == null || "".equals(valuesStringT)){
            return setIsNullParam(name,"");
        }
        int index = valuesStringT.lastIndexOf(",");
        String valuesString = "";
        if(index != -1){
            valuesString = valuesStringT.substring(0,valuesStringT.lastIndexOf(","));
        }
        return setIsNullParam(name, valuesString);

    }

    /**创建SQL语句
     * builder a sql
     * @return the result of sql string
     */
    public String buildSql(){
        return sql.toString();
    }

    private int lastIndexOf(String sql, int endIndex, String mark){
        String ssql = sql.substring(0,endIndex+1);
        return ssql.lastIndexOf(mark);
    }



}
