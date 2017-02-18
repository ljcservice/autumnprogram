package com.ts.service.pdss.peaas.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.Utils.DictCache;
import com.ts.entity.pdss.peaas.Beans.TPrescMasterBean;

/**
 * 处方主信息MapRow 
 * @author Administrator
 *
 */
public class PrescMasterBeanRM implements RowMapper
{
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        TPrescMasterBean pmb = new TPrescMasterBean();
        JDBCQueryImpl hisQuery = DBQueryFactory.getQuery("HIS");
        try
        {
            pmb.setPATIENT_ID(rs.getString("PATIENT_ID"));
            pmb.setVISIT_DATE(rs.getString("VISIT_DATE"));
            pmb.setVISIT_NO(rs.getString("VISIT_NO"));
            pmb.setSERIAL_NO(rs.getString("SERIAL_NO"));
            pmb.setORDERED_BY(rs.getString("ORDERED_BY"));
            pmb.setDOCTOR(rs.getString("DOCTOR"));
            pmb.setORDER_DATE(rs.getString("ORDER_DATE"));
            if(pmb.getORDERED_BY() != null)
            {
                pmb.setOrdered_by_name(DictCache.getNewInstance().getDeptName(hisQuery, pmb.getORDERED_BY()));
            }
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            hisQuery =  null;
        }
        return pmb;
    }
}