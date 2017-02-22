package com.hitzd.his.RowMapperBeans.Middle;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hitzd.his.Beans.Middle.TTableConfig;

public class TableConfigMapper implements RowMapper
{
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		TTableConfig table = new TTableConfig();
		table.setTableId(rs.getString("table_id") == null ? "" : rs.getString("table_id").toUpperCase());
		table.setTableDesc(rs.getString("table_desc") == null ? "" : rs.getString("table_desc"));
		table.setOriginalTable(rs.getString("original_table") == null ? "" : rs.getString("original_table").toUpperCase());
		table.setTargetTable(rs.getString("target_table") == null ? "" : rs.getString("target_table").toUpperCase());
		table.setRemark(rs.getString("remark") == null ? "" : rs.getString("remark"));
		table.setHisName(rs.getString("his_name") == null ? "JWYH-304" : rs.getString("his_name"));
		table.setDbUrl(rs.getString("db_url") == null ? "HIS" : rs.getString("db_url"));
		table.setDbName(rs.getString("db_name") == null ? "Oracle" : rs.getString("db_name"));
		return table;
	}
}