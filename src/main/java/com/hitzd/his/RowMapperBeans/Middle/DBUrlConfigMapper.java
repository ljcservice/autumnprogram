package com.hitzd.his.RowMapperBeans.Middle;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hitzd.his.Beans.Middle.TDBUrlConfig;

/**
 * his 连接字段表 
 * @author jingcong
 *
 */
public class DBUrlConfigMapper implements RowMapper
{
    @Override
    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
        TDBUrlConfig entity = new TDBUrlConfig();
        entity.setId(rs.getString("id"));
        entity.setDb_url(rs.getString("Db_url"));
        /* 数据库类型 */
        entity.setDb_base(rs.getString("db_base"));
        /* 备注 */
        entity.setRemark(rs.getString("remark"));
        /* 数据库用户名字 */
        entity.setDb_user(rs.getString("db_user"));
        /* 数据库密码 */
        entity.setDb_pwd(rs.getString("db_pwd")); 
        /* 数据库连接地址 */
        entity.setConn_url(rs.getString("Conn_url"));
        /* 配置文件是否存在连接  */
        entity.setFlag(rs.getString("flag"));    
        return entity;
    }

}
