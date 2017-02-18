package com.ts.service.pdss.CustomAudit.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.CustomAudit.Beans.TCustomAuditInfo;

/**
 * 用户自定义bean RowMapper
 * @author Administrator
 *
 */
public class CustomAuditInfoRowMapper implements RowMapper
{
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        TCustomAuditInfo cai = new TCustomAuditInfo();
        cai.setCA_ID("");
        cai.setDRUG_NO1(rs.getString("DRUG_NO1"));
        cai.setDRUG_NO2(rs.getString("DRUG_NO2"));
        cai.setDRUG_NO3(rs.getString("DRUG_NO3"));
        cai.setDRUG_NO4(rs.getString("DRUG_NO4"));
        cai.setADMIN_ID(rs.getString("ADMIN_ID"));
        cai.setDIAGNOSIS(rs.getString("DIAGNOSIS"));
        cai.setALLERG(rs.getString("ALLERG"));
        cai.setSIDE(rs.getString("SIDE"));
        cai.setEVENT_TYPE(rs.getString("EVENT_TYPE"));
        cai.setEVENT_RES(rs.getString("EVENT_RES"));
        cai.setEVENT_DESC(rs.getString("EVENT_DESC"));
        cai.setREF_INFO(rs.getString("REF_INFO"));
        cai.setMIN_DOS_PER(rs.getString("MIN_DOS_PER"));
        cai.setMAX_DOS_PER(rs.getString("MAX_DOS_PER"));
        cai.setMIN_DOS_DAY(rs.getString("MIN_DOS_DAY"));
        cai.setMAX_DOS_DAY(rs.getString("MAX_DOS_DAY"));
        cai.setMIN_FEQ_DAY(rs.getString("MIN_FEQ_DAY"));
        cai.setMAX_FEQ_DAY(rs.getString("MAX_FEQ_DAY"));
        cai.setMIN_DAYS(rs.getString("MIN_DAYS"));
        cai.setMAX_DAYS(rs.getString("MAX_DAYS"));
        cai.setLAST_UPDATER(rs.getString("LAST_UPDATER"));
        cai.setLAST_UPDATE_TIME(rs.getString("LAST_UPDATE_TIME"));
        cai.setLAST_CHECKER(rs.getString("LAST_CHECKER"));
        cai.setLAST_CHECK_TIME(rs.getString("LAST_CHECK_TIME"));
        return cai;
    }
}
