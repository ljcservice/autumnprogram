package com.ts.service.pdss.ias.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.ias.Beans.TAntiDrugUseRule;

public class TAntiDrugUseRuleMapper implements RowMapper 
{

	@Override
	public Object mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		TAntiDrugUseRule entity = new TAntiDrugUseRule();
		entity.setSERIAL_NO(rs.getString("SERIAL_NO"));
		entity.setDrug_ID(rs.getString("Drug_ID"));
		entity.setUse_Dept(rs.getString("Use_Dept"));
		entity.setUse_Level(rs.getString("Use_Level"));
		entity.setAuditor(rs.getString("Auditor"));
		entity.setAudit_Date(rs.getString("Audit_Date"));
		entity.setAudit_Res(rs.getString("Audit_Res"));
		entity.setMemo(rs.getString("Memo"));
		return entity;
	}

}
