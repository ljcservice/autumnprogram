package com.ts.service.pdss.peaas.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.peaas.Beans.TPrescDetailBean;

/**
 * 处方药品明细
 * @author Administrator
 *
 */
public class PrescDetailBeanRM implements RowMapper
{

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        TPrescDetailBean tdb = new TPrescDetailBean();
        tdb.setVISIT_DATE(rs.getString("VISIT_DATE"));
        tdb.setVISIT_NO(rs.getString("VISIT_NO"));
        tdb.setSERIAL_NO(rs.getString("SERIAL_NO"));
        tdb.setPRESC_NO(rs.getString("PRESC_NO"));
        tdb.setITEM_NO(rs.getString("ITEM_NO"));
        tdb.setITEM_CLASS(rs.getString("ITEM_CLASS"));
        tdb.setDRUG_CODE(rs.getString("DRUG_CODE"));
        tdb.setDRUG_NAME(rs.getString("DRUG_NAME"));
        tdb.setDRUG_SPEC(rs.getString("DRUG_SPEC"));
        tdb.setFIRM_ID(rs.getString("FIRM_ID"));
        tdb.setUNITS(rs.getString("UNITS"));
        tdb.setAMOUNT(rs.getString("AMOUNT"));
        tdb.setDOSAGE(rs.getString("DOSAGE"));
        tdb.setDOSAGE_UNITS(rs.getString("DOSAGE_UNITS"));
        tdb.setADMINISTRATION(rs.getString("ADMINISTRATION"));
        tdb.setFREQUENCY(rs.getString("FREQUENCY"));
        tdb.setPROVIDED_INDICATOR(rs.getString("PROVIDED_INDICATOR"));
        tdb.setCOSTS(rs.getString("COSTS"));
        tdb.setCHARGES(rs.getString("CHARGES"));
        tdb.setCHARGE_INDICATOR(rs.getString("CHARGE_INDICATOR"));
        tdb.setDISPENSARY(rs.getString("DISPENSARY"));
        tdb.setREPETITION(rs.getString("REPETITION"));
        tdb.setORDER_NO(rs.getString("ORDER_NO"));
        tdb.setORDER_SUB_NO(rs.getString("ORDER_SUB_NO"));
//        tdb.setFREQ_DETAIL(rs.getString("FREQ_DETAIL"));
//        tdb.setGETDRUG_FLAG(rs.getString("GETDRUG_FLAG"));
        return tdb;
    }

}
