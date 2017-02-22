package com.hitzd.his.Beans.frame.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hitzd.his.Beans.frame.UserRolePower;

/**
 * 用户菜单权限 
 * @author jingcong
 *
 */
public class UserRolePowerMapper implements RowMapper
{
    @Override
    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
        UserRolePower  entity = new UserRolePower();
        entity.setProgfunc(rs.getString("progfunc"));
        entity.setProgid(rs.getString("PROGID"));
        entity.setRoleid(rs.getString("ROLEID"));
        entity.setSysid(rs.getString("Sysid"));
        return entity;
    }

}
