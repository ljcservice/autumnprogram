package com.hitzd.his.RowMapperBeans.Middle;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hitzd.his.Beans.Middle.TFieldConfig;

public class FieldConfigMapper implements RowMapper
{
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		TFieldConfig field = new TFieldConfig();
		field.setFieldId(rs.getString("field_id") == null ? "" : rs.getString("field_id").toUpperCase());
		field.setTableId(rs.getString("table_id") == null ? "" : rs.getString("table_id").toUpperCase());
		field.setFieldDesc(rs.getString("field_desc") == null ? "" : rs.getString("field_desc"));
		field.setOriginalField(rs.getString("original_field") == null ? "" : rs.getString("original_field").toUpperCase());
		field.setTargetField(rs.getString("target_field") == null ? "''" : rs.getString("target_field").toUpperCase());
		field.setRemark(rs.getString("remark") == null ? "" : rs.getString("remark"));
		field.setHisName(rs.getString("his_name") == null ? "JWYH-304" : rs.getString("his_name"));
		return field;
	}
}