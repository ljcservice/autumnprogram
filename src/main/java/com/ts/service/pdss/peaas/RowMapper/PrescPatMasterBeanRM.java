package com.ts.service.pdss.peaas.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.peaas.Beans.TPrescPatMasterBean;

public class PrescPatMasterBeanRM implements RowMapper
{
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        TPrescPatMasterBean ppmb = new TPrescPatMasterBean();
        ppmb.setPATIENT_ID(rs.getString("PATIENT_ID"));
        ppmb.setINP_NO(rs.getString("INP_NO"));
        ppmb.setNAME(rs.getString("NAME"));
        ppmb.setNAME_PHONETIC(rs.getString("NAME_PHONETIC"));
        ppmb.setSEX(rs.getString("SEX"));
        ppmb.setDATE_OF_BIRTH(rs.getString("DATE_OF_BIRTH"));
        ppmb.setBIRTH_PLACE(rs.getString("BIRTH_PLACE"));
        ppmb.setCITIZENSHIP(rs.getString("CITIZENSHIP"));
        ppmb.setNATION(rs.getString("NATION"));
        ppmb.setID_NO(rs.getString("ID_NO"));
        ppmb.setIDENTITY(rs.getString("IDENTITY"));
        ppmb.setCHARGE_TYPE(rs.getString("CHARGE_TYPE"));
        ppmb.setUNIT_IN_CONTRACT(rs.getString("UNIT_IN_CONTRACT"));
        ppmb.setMAILING_ADDRESS(rs.getString("MAILING_ADDRESS"));
        ppmb.setZIP_CODE(rs.getString("ZIP_CODE"));
        ppmb.setPHONE_NUMBER_HOME(rs.getString("PHONE_NUMBER_HOME"));
        ppmb.setPHONE_NUMBER_BUSINESS(rs.getString("PHONE_NUMBER_BUSINESS"));
        ppmb.setNEXT_OF_KIN(rs.getString("NEXT_OF_KIN"));
        ppmb.setRELATIONSHIP(rs.getString("RELATIONSHIP"));
        ppmb.setNEXT_OF_KIN_ADDR(rs.getString("NEXT_OF_KIN_ADDR"));
        ppmb.setNEXT_OF_KIN_ZIP_CODE(rs.getString("NEXT_OF_KIN_ZIP_CODE"));
        ppmb.setNEXT_OF_KIN_PHONE(rs.getString("NEXT_OF_KIN_PHONE"));
        ppmb.setLAST_VISIT_DATE(rs.getString("LAST_VISIT_DATE"));
        ppmb.setVIP_INDICATOR(rs.getString("VIP_INDICATOR"));
        ppmb.setCREATE_DATE(rs.getString("CREATE_DATE"));
        ppmb.setOPERATOR(rs.getString("OPERATOR"));
        return ppmb;
    }
}
