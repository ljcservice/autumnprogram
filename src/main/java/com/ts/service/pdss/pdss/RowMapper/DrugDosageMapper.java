package com.ts.service.pdss.pdss.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.pdss.Beans.TDrugDosage;

/**
 *  药品剂量使用字典
 * @author liujc
 *
 */
public class DrugDosageMapper extends MapperBase implements RowMapper
{

    public DrugDosageMapper()
    {
     
    }
    @Override
    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
        TDrugDosage entity = new TDrugDosage();
        entity.setDRUG_DOSAGE_ID(rs.getString("DRUG_DOSAGE_ID"));
        entity.setDOSE_CLASS_ID(rs.getString("DOSE_CLASS_ID"));
        entity.setADMINISTRATION_ID(rs.getString("ADMINISTRATION_ID"));
        entity.setDRUG_FORM(rs.getString("DRUG_FORM"));
        entity.setDOSE_UNITS(rs.getString("DOSE_UNITS"));
        entity.setAGE_INDI(rs.getString("AGE_INDI"));
        entity.setAGE_LOW(rs.getString("AGE_LOW"));
        entity.setAGE_HIGH(rs.getString("AGE_HIGH"));
        entity.setWEIGHT_INDI(rs.getString("WEIGHT_INDI"));
        entity.setWEIGHT_LOW(rs.getString("WEIGHT_LOW"));
        entity.setWEIGHT_HIGH(rs.getString("WEIGHT_HIGH"));
        entity.setCAL_INDI(rs.getString("CAL_INDI"));
        entity.setDOSE_EACH_LOW(rs.getString("DOSE_EACH_LOW"));
        entity.setDOSE_EACH_HIGH(rs.getString("DOSE_EACH_HIGH"));
        entity.setDOSE_EACH_UNIT(rs.getString("DOSE_EACH_UNIT"));
        entity.setDOSE_DAY_LOW(rs.getString("DOSE_DAY_LOW"));
        entity.setDOSE_DAY_HIGH(rs.getString("DOSE_DAY_HIGH"));
        entity.setDOSE_DAY_UNIT(rs.getString("DOSE_DAY_UNIT"));
        entity.setDOSE_MAX_HIGH(rs.getString("DOSE_MAX_HIGH"));
        entity.setDOSE_MAX_UNIT(rs.getString("DOSE_MAX_UNIT"));
        entity.setDOSE_FREQ_LOW(rs.getString("DOSE_FREQ_LOW"));
        entity.setDOSE_FREQ_HIGH(rs.getString("DOSE_FREQ_HIGH"));
        entity.setDUR_LOW(rs.getString("DUR_LOW"));
        entity.setDUR_HIGH(rs.getString("DUR_HIGH"));
        entity.setSPECIAL_DESC(rs.getString("SPECIAL_DESC"));
        entity.setREFERENCE_INFO(rs.getString("REFERENCE_INFO"));
        return entity;
    }

}
