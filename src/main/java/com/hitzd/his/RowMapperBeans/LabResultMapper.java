package com.hitzd.his.RowMapperBeans;

import java.sql.ResultSet;

import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hitzd.his.Beans.TLabResult;

/**
 * ºÏ—ÈΩ·ππmapper
 * @author Administrator
 *
 */
public class LabResultMapper implements RowMapper
{

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        TLabResult lr = new TLabResult();
        lr.setTEST_NO(rs.getString("TEST_NO"));             
        lr.setITEM_NO(rs.getString("ITEM_NO"));           
        lr.setPRINT_ORDER(rs.getString("PRINT_ORDER"));       
        lr.setREPORT_ITEM_NAME(rs.getString("REPORT_ITEM_NAME"));  
        lr.setREPORT_ITEM_CODE(rs.getString("REPORT_ITEM_CODE"));  
        lr.setRESULT(rs.getString("RESULT"));            
        lr.setUNITS(rs.getString("UNITS"));             
        lr.setABNORMAL_INDICATOR(rs.getString("ABNORMAL_INDICATOR"));
        lr.setINSTRUMENT_ID(rs.getString("INSTRUMENT_ID"));     
        lr.setRESULT_DATE_TIME(rs.getString("RESULT_DATE_TIME"));  
        lr.setPRINT_CONTEXT(rs.getString("PRINT_CONTEXT"));     
        return lr;
    }

}
