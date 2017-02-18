package com.ts.service.pdss.ias.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.ias.RSBeans.TAntiDrugAuditorRule;

public class TAntiDrugAuditorRuleMapper implements RowMapper
{
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		TAntiDrugAuditorRule antiDrugAuditorRule = new TAntiDrugAuditorRule();
		antiDrugAuditorRule.setRuleId(rs.getString("rule_id"));
		antiDrugAuditorRule.setDrugCode(rs.getString("drug_code"));
		antiDrugAuditorRule.setDrugName(rs.getString("drug_name"));
		antiDrugAuditorRule.setDrugSpec(rs.getString("drug_spec"));
		antiDrugAuditorRule.setDrugForm(rs.getString("drug_form"));
		antiDrugAuditorRule.setDrugFirm(rs.getString("drug_firm"));
		antiDrugAuditorRule.setAdministration(rs.getString("administration"));
		antiDrugAuditorRule.setAuditSwitch(rs.getString("audit_switch"));
		antiDrugAuditorRule.setRegSwitch(rs.getString("reg_switch"));
		return antiDrugAuditorRule;
	}
}