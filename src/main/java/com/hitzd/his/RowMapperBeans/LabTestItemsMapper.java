package com.hitzd.his.RowMapperBeans;

import java.sql.ResultSet;

import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hitzd.his.Beans.TLabTestItems;

/**
 * ¼ìÑéÏîÄ¿ 
 * @author Administrator
 *
 */
public class LabTestItemsMapper implements RowMapper
{
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        TLabTestItems lti = new TLabTestItems();    
        lti.setTEST_NO(rs.getString("TEST_NO"));
        lti.setITEM_NO(rs.getString("ITEM_NO"));  
        lti.setITEM_NAME(rs.getString("ITEM_NAME"));
        lti.setITEM_CODE(rs.getString("ITEM_CODE"));
        return lti;
    }

}
