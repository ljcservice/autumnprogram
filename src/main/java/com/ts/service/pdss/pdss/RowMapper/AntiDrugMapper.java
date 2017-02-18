package com.ts.service.pdss.pdss.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.pdss.Beans.TAntiDrug;

/**
 * 抗菌药物 mapper
 * @author Administrator
 *
 */
public class AntiDrugMapper implements RowMapper 
{
	@Override 
	public Object mapRow(ResultSet rs, int arg1) throws SQLException
	{
		TAntiDrug  antiDrug = new TAntiDrug();
		antiDrug.setSERIAL_NO(rs.getString("SERIAL_NO")); 
		antiDrug.setITEM_CLASS(rs.getString("ITEM_CLASS"));        
		antiDrug.setDRUG_NO_LOCAL(rs.getString("DRUG_NO_LOCAL"));
		antiDrug.setITEM_SPEC(rs.getString("ITEM_SPEC")); 
		antiDrug.setUNITS(rs.getString("UNITS"));             
		antiDrug.setDOSE_PER_UNIT(rs.getString("DOSE_PER_UNIT"));     
		antiDrug.setDOSE_UNITS(rs.getString("DOSE_UNITS"));        
		antiDrug.setSTD_DOSAGE(rs.getString("STD_DOSAGE"));        
		antiDrug.setSTD_INDICATOR(rs.getString("STD_INDICATOR"));     
		antiDrug.setINFECT_CLASS(rs.getString("INFECT_CLASS"));      
		antiDrug.setDRUG_CLASS(rs.getString("DRUG_CLASS"));        
		antiDrug.setDRUG_SENS(rs.getString("DRUG_SENS"));
		antiDrug.setDRUG_USE_CONDITION(rs.getString("DRUG_USE_CONDITION"));
		antiDrug.setISPRE(rs.getString("ISPRE"));            
		antiDrug.setMax_Days(rs.getString("Max_Days"));          
		antiDrug.setMin_Days(rs.getString("Min_Days"));      
		antiDrug.setDRUG_NAME_LOCAL(rs.getString("drug_name_local"));
		return antiDrug;
	}
}
