package com.hitzd.DBUtils;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * 封装spring jdbcTeplate
 * @author liujc
 * 
 */
public interface JDBCQuery
{

    /**
     * sql 执行 无返回；
     * 
     * @param sql
     */
    public void execute(String sql);
    /**
     * 执行大字符
     * @param sql
     * @param lobCreater
     */
    public void execute(String sql , LobCreatingPSCallBeck lobCreater );
   
    /**
     * 返回一个整形
     * 
     * @param sql
     * @return
     */
    public int queryForInt(String sql);

    /**
     * 返回一个整形
     * 
     * @param sql
     * @param _Values
     *            参数数组
     * @return
     */
    public int queryForInt(String sql, Object[] _Values);

    /**
     * 
     * @param sql
     * @return
     */
    public int update(String sql);

    /**
     * 
     * @param sql
     * @param _Values
     * @return
     */
    public int update(String sql, Object[] _Values);

    /**
     * 
     * @param sql
     * @param lobCreater
     */
    public void update(String sql , LobCreatingPSCallBeck lobCreater);
    /**
     * 
     * @param sql
     * @return
     */
    public int[] batchUpdate(String[] sql);

    /**
     * 
     * @param sql
     * @param _Values
     * @param _Types
     * @return
     */
    public int queryForInt(String sql,Object[] _Values,int[] _Types);
    
    /**
     * 
     * @param sql
     * @param rowMapper
     * @return
     */
    public Object queryForObject(String sql, RowMapper rowMapper);
    
    /**
     * 
     * @param sql
     * @param _Values
     * @param rowMapper
     * @return
     */
    public Object queryForObject(String sql,Object[] _Values, RowMapper rowMapper);
    
    /**
     * 
     * @param sql
     * @param _Values
     * @param _Types
     * @param rowMapper
     * @return
     */
    public Object queryForObject(String sql,Object[] _Values,int[] _Types,RowMapper rowMapper);

    /**
     * 
     * @param sql
     * @param rowMapper
     * @return
     */
    public List query(String sql,RowMapper rowMapper);
    
    /**
     * 
     * @param sql
     * @param _Values
     * @param rowMapper
     * @return
     */
    public List query(String sql,Object[] _Values,RowMapper rowMapper);
    
    /**
     * 
     * @param sql
     * @param _Values
     * @param _Types
     * @param rowMapper
     * @return
     */
    public List query(String sql,Object[] _Values,int[] _Types,RowMapper rowMapper);
    
    /**
     * 
     * @param sql
     * @return
     */
    public List queryForList(String sql);

    /**
     * 
     * @param sql
     * @param _Values
     * @return
     */
    public List queryForList(String sql,Object[] _Values);
    
    /**
     * 
     * @param sql
     * @param _Values
     * @param _Types
     * @return
     */
    public List queryForList(String sql,Object[] _Values,int[] _Types);
    
    /**
     * 
     * @param sql
     * @param requiredType
     * @return
     */
    public Object queryForList(String sql,Class requiredType);
 
    /**
     * 
     * @param sql
     * @param _Values
     * @param requiredType
     * @return
     */
    public Object queryForList(String sql, Object[] _Values ,Class requiredType);
   
}
