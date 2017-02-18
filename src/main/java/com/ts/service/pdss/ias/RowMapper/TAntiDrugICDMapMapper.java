package com.ts.service.pdss.ias.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.ias.Beans.TAntiDrugICDMap;

/**
 * 使用抗菌药物ICD标准
 * @author Administrator
 *
 */
public class TAntiDrugICDMapMapper implements RowMapper {

	@Override
	public Object mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		TAntiDrugICDMap entity = new TAntiDrugICDMap();
		entity.setDrugID(rs.getString("Drug_ID"));
		entity.setSerialNo(rs.getString("SERIAL_NO"));
		entity.setICDCode(rs.getString("ICD_Code"));
		entity.setMemo(rs.getString("Memo"));
		return entity;
	}

}
