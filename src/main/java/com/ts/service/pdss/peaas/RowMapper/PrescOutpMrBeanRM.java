package com.ts.service.pdss.peaas.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.peaas.Beans.TPrescOutpMrBean;

/**
 * 门诊病历记录 rowMapper
 * @author Administrator
 *
 */
public class PrescOutpMrBeanRM implements RowMapper
{
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        TPrescOutpMrBean pom = new TPrescOutpMrBean();
        pom.setPATIENT_ID(rs.getString("PATIENT_ID"));
        pom.setVISIT_DATE(rs.getString("VISIT_DATE"));
        pom.setVISIT_NO(rs.getString("VISIT_NO"));
        pom.setILLNESS_DESC(rs.getString("ILLNESS_DESC"));
        pom.setANAMNESIS(rs.getString("ANAMNESIS"));
        pom.setFAMILY_ILL(rs.getString("FAMILY_ILL"));
        pom.setMARRITAL(rs.getString("MARRITAL"));
        pom.setINDIVIDUAL(rs.getString("INDIVIDUAL"));
        pom.setMENSES(rs.getString("MENSES"));
        pom.setMED_HISTORY(rs.getString("MED_HISTORY"));
        pom.setBODY_EXAM(rs.getString("BODY_EXAM"));
        pom.setDIAG_DESC(rs.getString("DIAG_DESC"));
        pom.setADVICE(rs.getString("ADVICE"));
        pom.setDOCTOR(rs.getString("DOCTOR"));
        pom.setORDINAL(rs.getString("ORDINAL"));
//        pom.setOPERATION_RECORD(rs.getString("OPERATION_RECORD"));
//        pom.setMEDICAL_RECORD(rs.getString("MEDICAL_RECORD"));
        return pom;
    }

}
