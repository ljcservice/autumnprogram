package com.hitzd.his.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.Beans.Middle.TDBUrlConfig;
import com.hitzd.his.Beans.Middle.TFieldConfig;
import com.hitzd.his.Beans.Middle.TQueryConfig;
import com.hitzd.his.Beans.Middle.TTableConfig;
import com.hitzd.his.RowMapperBeans.Middle.DBUrlConfigMapper;
import com.hitzd.his.RowMapperBeans.Middle.FieldConfigMapper;
import com.hitzd.his.RowMapperBeans.Middle.QueryConfigMapper;
import com.hitzd.his.RowMapperBeans.Middle.TableConfigMapper;
import com.hitzd.DBUtils.CommonMapper;

/**
 *  配置参数 
 * @author Administrator
 *
 */
public class Config 
{
    /* 规则 */
    private static HashMap<String, String>            paramMap = null;
    /* 对照表配置 */
    private static HashMap<String, TTableConfig> tableMap = null;
    private static HashMap<String,TDBUrlConfig>  dbUrlMap = null; 
    public static boolean paramLoaded = false;
    public static boolean tableConfigLoaded = false;
    
    /**
     * 参数初始化
     */
    public static void initParam()
    {
    	try
    	{
    		System.out.println("Config参数初始化开始！");
        	JDBCQueryImpl peaasQuery = DBQueryFactory.getQuery("");
        	// 初始化参数信息
        	paramMap = new HashMap<String, String>(); 
            @SuppressWarnings("unchecked")
    		List<TCommonRecord> list = peaasQuery.query("select rulecode,rulevalue from ruleparameter", new CommonMapper());
            for (TCommonRecord cr: list)
            	paramMap.put(cr.get("rulecode").toUpperCase(), cr.get("rulevalue"));
            peaasQuery = null;
            paramLoaded = true;
        	System.out.println("Config参数初始化结束！");
    	}
    	catch(Exception e )
    	{
    		e.printStackTrace();
    	}
    }
    
    /**
     * 表和字段配置初始化
     */
    @SuppressWarnings ("unchecked")
    public static void initTableConfig()
    {
    	try
    	{
    		System.out.println("Config表和字段配置初始化开始！");
        	JDBCQueryImpl peaasQuery = DBQueryFactory.getQuery("");
            // 初始化对照表配置信息
            tableMap = new HashMap<String, TTableConfig>();
            @SuppressWarnings("unchecked")
    		List<TTableConfig> tableList = peaasQuery.query("select * from table_config", new TableConfigMapper());
            if (tableList != null && tableList.size() > 0)
            {
    	        for (TTableConfig table : tableList)
    	        {
    	        	@SuppressWarnings("unchecked")
    				List<TFieldConfig> fieldList = peaasQuery.query("select * from field_config where table_id='" + table.getTableId() + "'", new FieldConfigMapper());
    	        	if (fieldList != null && fieldList.size() > 0)
    	        	{
    		        	TFieldConfig[] fields = new TFieldConfig[fieldList.size()];
    		        	Map<String, TFieldConfig> fieldMap = new HashMap<String, TFieldConfig>();
    		        	for (int i = 0; i < fieldList.size(); i++)
    		        	{
    		        		fields[i] = fieldList.get(i);
    		        		fieldMap.put(fieldList.get(i).getOriginalField(), fieldList.get(i));
    		        	}
    		        	table.setFields(fields);
    		        	table.setFieldMap(fieldMap);
    	        	}
    	        	@SuppressWarnings("unchecked")
    				List<TQueryConfig> queryList = peaasQuery.query("select * from query_config where table_id='" + table.getTableId() + "'", new QueryConfigMapper());
    	        	if (queryList != null && queryList.size() > 0)
    	        	{
    	        		TQueryConfig[] queries = new TQueryConfig[queryList.size()];
    		        	Map<String, TQueryConfig> queryMap = new HashMap<String, TQueryConfig>();
    	        		for (int i = 0; i < queryList.size(); i++)
    	        		{
    	        			queries[i] = queryList.get(i);
    	        			queryMap.put(queryList.get(i).getQueryId(), queryList.get(i));
    	        		}
    	        		table.setQueries(queries);
    	        		table.setQueryMap(queryMap);
    	        	}
    	        	tableMap.put(table.getTableId(), table);
    	        }
            }
            dbUrlMap = new HashMap<String, TDBUrlConfig>();
            List<TDBUrlConfig>  list = peaasQuery.query("select * from db_url_config ", new DBUrlConfigMapper());
            for(TDBUrlConfig t : list)
            {
                dbUrlMap.put(t.getDb_url(), t);
            }
            peaasQuery = null;
            tableConfigLoaded = true;
        	System.out.println("Config表和字段配置初始化结束！");
    	}
    	catch(Exception e )
    	{
    		e.printStackTrace();
    	}
    }
    
    /**
     *  重置参数 
     */
    public static void ReSetParam()
    {
        initParam();
    }
    
    /**
     * 重置表和字段配置
     */
    public static void ReSetTableConfig()
    {
    	initTableConfig();
    }
    
    /**
     * 重置整个Config
     */
    public static void ReSetConfig()
    {
        initParam();
        initTableConfig();
    }
    
    /**
     * 重新设置系统参数
     * @param key
     * @param value
     */
    public static void setParamValue(String key ,String value)
    {
        
         // 2014-09-02 liujc 修改，   
        if (!paramLoaded) initParam();
//        if (paramMap.containsKey(key.toUpperCase()))
//        {
            paramMap.put(key.toUpperCase(), value); 
//        }
//        else
//        {
//            System.out.println("参数中没有该参数：" + key);
//        }
    }
    
    /**
     * 
     * @param paraCode
     * @return
     */
    private static String getSelectParamValue(String paraCode)
    {
//        System.out.println("Config 获取参数 " + paraCode ); 
    	JDBCQueryImpl query =  DBQueryFactory.getQuery("");
        CommonMapper  cmr   = new CommonMapper();
        TCommonRecord  rs   = new TCommonRecord();
        try
        {
            rs = (TCommonRecord) query.queryForObject("select RULEVALUE from ruleparameter t where upper(t.RULECODE) = ?　", new Object[]{paraCode}, cmr);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //query = null;
            cmr   = null;
        }
        if(rs == null) new RuntimeException("该参数" + paraCode + " 为算数表中不存在 ");
//        System.out.println("Config 获取参数成功 " + paraCode + ":" + rs.get("RULEVALUE")); 
        return rs.get("RULEVALUE");
    }
    
    /**
     * 获得系统参数
     * @param name
     * @return
     */
    public static String getParamValue(String name)
    {
    	
        if (!paramLoaded) initParam();
    	if (paramMap != null && paramMap.containsKey(name.toUpperCase()))
    	{
    		return paramMap.get(name.toUpperCase());
    	}
    	return getSelectParamValue(name.toUpperCase());
    }
    
    public static int getIntParamValue(String name)
    {
   		try
   		{
   			return Integer.parseInt(getParamValue(name));
   		}
   		catch (Exception ex)
   		{
   			System.out.println("获取系统参数整型值错误！" + name);
   			return 0;
   		}
    }

    /**
     * 构建中间层对照表
     * @return
     */
	public static HashMap<String, TTableConfig> getTableMap()
	{
    	if (!tableConfigLoaded) initTableConfig();
		return tableMap;
	}
	
	/**
	 * 获得中间层参照表
	 * @param key
	 * @return
	 */
	public static TTableConfig getTableCofig(String key)
	{
	    if (!tableConfigLoaded) initTableConfig();
	    if(!tableMap.containsKey(key.toUpperCase()))
	    {
	        new RuntimeException("中间层 table_Config 中没有找到目标“" + key + "”表");
	    }
	    return tableMap.get(key.toUpperCase());
	}
	
	/**
	 * 数据元表
	 * @return
	 */
	public static HashMap<String, TDBUrlConfig> getDBUrlMap()
	{
	    if (!tableConfigLoaded) initTableConfig();
        return dbUrlMap;
	}
	
	/**
     * 获得数据元表
     * @param key
     * @return
     */
    public static TDBUrlConfig getDBUrlConfig(String key)
    {
        if (!tableConfigLoaded) initTableConfig();
        if(!dbUrlMap.containsKey(key))
        {
            new RuntimeException("中间层 db_url_config 中没有找到目标“" + key + "”表");
        }
        return dbUrlMap.get(key);
    }
}
