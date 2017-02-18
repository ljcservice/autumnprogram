package com.ts.service.pdss.ias.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.ias.RSBeans.TAntiDrugAuditorRuleDetail;

public class TAntiDrugAuditorRuleDetailMapper implements RowMapper
{
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		TAntiDrugAuditorRuleDetail antiDrugAuditorRuleDetail = new TAntiDrugAuditorRuleDetail();
		antiDrugAuditorRuleDetail.setRuleId(rs.getString("rule_id"));
		antiDrugAuditorRuleDetail.setDeptCode(rs.getString("dept_code"));
		antiDrugAuditorRuleDetail.setDeptName(rs.getString("dept_name"));
		antiDrugAuditorRuleDetail.setDoctorCode(rs.getString("doctor_code"));
		antiDrugAuditorRuleDetail.setDoctorName(rs.getString("doctor_name"));
		antiDrugAuditorRuleDetail.setDoctorTitle(rs.getString("doctor_title"));
		antiDrugAuditorRuleDetail.setTitleDesc(rs.getString("title_desc"));
		return antiDrugAuditorRuleDetail;
	}
}