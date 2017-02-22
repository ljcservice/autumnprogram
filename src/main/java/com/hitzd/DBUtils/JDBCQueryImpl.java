package com.hitzd.DBUtils;

import java.util.List;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * 封装spring jdbctemplate类
 * @author liujc
 *
 */
public final class  JDBCQueryImpl implements JDBCQuery
{

    private JdbcOperations query = null;
    private String DBName        = null;
    public JDBCQueryImpl(JdbcOperations jdbcOperations,String dbName)
    {
        if (jdbcOperations == null)
            new RuntimeException(" The JdbcTemplate can't null");
        this.query = jdbcOperations;
        this.DBName = dbName;
    }
    
    /* 得到数据源的代码 */
    public String getDbName()
    {
        return this.DBName;
    }
    
    @Override
    public void execute(String sql)
    {
        query.execute(sql);
        
    }

    public void execute(String sql , LobCreatingPSCallBeck lobCreater )
    {
        query.execute(sql, lobCreater.LobCreatingPreparedSCallback());
    }
    
    public void update(String sql , LobCreatingPSCallBeck lobCreater )
    {
        query.execute(sql, lobCreater.LobCreatingPreparedSCallback());
    }
    
    public int queryForInt(String sql)
    {
        return query.queryForInt(sql);
    }
    
    public int queryForInt(String sql,Object[] _Values)
    {
        return query.queryForInt(sql, _Values);
    }
    
    public int queryForInt(String sql,Object[] _Values,int[] _Types)
    {
        return query.queryForInt(sql, _Values,_Types);
    }
    
    public Object queryForObject(String sql, RowMapper rowMapper)
    {
        List<Object> objs = query.query(sql, rowMapper);
        return objs != null && objs.size() > 0 ? objs.get(0):null;
    }
    
    public Object queryForObject(String sql,Object[] _Values, RowMapper rowMapper)
    {
        List<Object> objs = query.query(sql,_Values, rowMapper);
        return objs != null && objs.size() > 0 ? objs.get(0):null;
    }
    
    public Object queryForObject(String sql,Object[] _Values,int[] _Types,RowMapper rowMapper)
    {
        List<Object> objs = query.query(sql,_Values,_Types, rowMapper);
        return objs != null && objs.size() > 0 ? objs.get(0):null;
    }

    public List query(String sql,RowMapper rowMapper)
    {
        return query.query(sql, rowMapper); 
    }
    
    public List query(String sql,Object[] _Values,RowMapper rowMapper)
    {
        return query.query(sql, _Values, rowMapper);
    }
    
    public List query(String sql,Object[] _Values,int[] _Types,RowMapper rowMapper)
    {
        return query.query(sql, _Values,_Types, rowMapper);
    }
    
    public List queryForList(String sql)
    {
        return query.queryForList(sql);
    }

    public List queryForList(String sql,Object[] _Values)
    {
        return query.queryForList(sql, _Values);
    }
    
    public List queryForList(String sql,Object[] _Values,int[] _Types)
    {
        return query.queryForList(sql, _Values,_Types);
    }
    
    public Object queryForList(String sql,Class requiredType)
    {
        return query.queryForList(sql, requiredType);
    }
    
    public Object queryForList(String sql, Object[] _Values ,Class requiredType)
    {
        return query.queryForList(sql, _Values, requiredType);
    }
    
    public int update(String sql)
    {
        return query.update(sql);
    }

    public int update(String sql, Object[] _Values)
    {
        return query.update(sql, _Values);
    }

    public int[] batchUpdate(String[] sql)
    {
        return query.batchUpdate(sql);
    }
}
