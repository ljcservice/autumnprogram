package com.ts.service.pdss.ias.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.ias.Beans.Ticd10;

public class Ticd10Mapper implements RowMapper {

	@Override
	public Object mapRow(ResultSet rs, int arg1) throws SQLException {
		Ticd10 entity = new Ticd10();
		/* ID */
		entity.setSERIAL_NO(rs.getString("SERIAL_NO"));
		/* 序号 */
		entity.setICD_XH(rs.getString("ICD_XH"));
		entity.setICD_FM(rs.getString("ICD_FM"));
		/* 国际ICD编码 */
		entity.setICD_Code(rs.getString("ICD_Code"));
		/* 疾病名称 */
		entity.setICD_Name(rs.getString("ICD_Name"));
		/* 助记码 */
		entity.setICD_ZJM(rs.getString("ICD_ZJM"));
		entity.setICD_SM(rs.getString("ICD_SM"));
		/* 性别限制 */
		entity.setICD_Sex(rs.getString("ICD_Sex"));
		/* 疗效限制 */
		entity.setICD_Res(rs.getString("ICD_Res"));
		entity.setICD_LB(rs.getString("ICD_LB"));
		entity.setICD_FLID(rs.getString("ICD_FLID"));
		entity.setICD_XTBS(rs.getString("ICD_XTBS"));
		/* 手术表示 */
		entity.setICD_IsOper(rs.getString("ICD_IsOper"));
		return entity;
	}

}
