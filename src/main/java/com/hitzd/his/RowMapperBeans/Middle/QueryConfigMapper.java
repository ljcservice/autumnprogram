package com.hitzd.his.RowMapperBeans.Middle;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hitzd.his.Beans.Middle.TQueryConfig;

public class QueryConfigMapper implements RowMapper
{
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		TQueryConfig query = new TQueryConfig();
		query.setQueryId(rs.getString("query_id") == null ? "" : rs.getString("query_id"));
		query.setTableId(rs.getString("table_id") == null ? "" : rs.getString("table_id").toUpperCase());
		query.setQueryCondition(rs.getString("query_condition") == null ? "" : rs.getString("query_condition"));
		query.setRemark(rs.getString("remark") == null ? "" : rs.getString("remark"));
		query.setHisName(rs.getString("his_name") == null ? "JWYH-304" : rs.getString("his_name"));
		return query;
	}
}