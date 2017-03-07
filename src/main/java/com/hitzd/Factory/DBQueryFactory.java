package com.hitzd.Factory;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.JdbcTemplateHander;
import com.hitzd.springBeanManager.SpringBeanUtil;

/**
 * 数据源工厂
 * @author Administrator
 *
 */
@Service
public final class DBQueryFactory
{
	
    private DBQueryFactory()
    {
    }

    /**
     * 获得数据源
     * @param resourceID
     * @return
     */
    
    public static DataSource getDataSource(String resourceID)
    {
        DataSource ds = null;
        try
        {
            //2014-10-21 liujc 修改 使用tomcat  数据连接池
//            Context  initContext = new InitialContext();
//            Context envContext = (Context)initContext.lookup("java:/comp/env");
//            ds = (DataSource)envContext.lookup(resourceID);
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return ds ;
    }
    
    /**
     * spring 原始处理sql JdbcTemplate  
     * @param resourceID
     * @return
     */
    public  static JdbcTemplate getDBQuery(String resourceID)
    {
        synchronized(DBQueryFactory.class)
        {
            DataSource ds = null;
            try
            {
                //2014-10-21 liujc 修改 使用tomcat  数据连接池
//                ds = getDataSource(resourceID);
                
                // 2014-10-21 liujc 修改   将数据连接池不托管在spring容器中
                ds = (DataSource) SpringBeanUtil.getBean("dataSource" + ("".equals(resourceID) ? "":"_" + resourceID ));    
            }
            catch(NullPointerException nullPoiE)
            {
                new RuntimeException("NullPointerException 空指针异常");
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }
            return new JdbcTemplate(ds);
        }
    }
    
    /**
     * 直接获得数据库访问对象
     * @param resourceID 如果是空字符串 则为plat 用户
     * @return
     */
    @SuppressWarnings ("unused")
    public static JDBCQueryImpl getQuery(String resourceID)
	{
    	JDBCQueryImpl query = new JDBCQueryImpl((new JdbcTemplateHander()).bind(getDBQuery(resourceID),resourceID),resourceID) ;
	    if(query == null)
    	    new RuntimeException("The code name is not true , can't load datasource : " + resourceID);
	    return query;
	}
}
