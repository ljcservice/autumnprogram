package com.hitzd.his.RowMapperBeans;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hitzd.his.Beans.TLabTest;

/**
 * ¼ìÑéÖ÷¼ÇÂ¼
 * @author Administrator
 *
 */
public class LabTestMapper implements RowMapper
{

    @Override
    public Object mapRow(ResultSet t, int rowNum) throws SQLException
    {
        TLabTest l = new TLabTest();
        l.setTEST_NO(t.getString("TEST_NO"));                    
        l.setPRIORITY_INDICATOR(t.getString("PRIORITY_INDICATOR"));         
        l.setPATIENT_ID(t.getString("PATIENT_ID"));                 
        l.setVISIT_ID(t.getString("VISIT_ID"));                   
        l.setWORKING_ID(t.getString("WORKING_ID"));                 
        l.setEXECUTE_DATE(t.getString("EXECUTE_DATE"));               
        l.setNAME(t.getString("NAME"));                       
        l.setNAME_PHONETIC(t.getString("NAME_PHONETIC"));              
        l.setCHARGE_TYPE(t.getString("CHARGE_TYPE"));                
        l.setSEX(t.getString("SEX"));                        
        l.setAGE(t.getString("AGE"));                       
        l.setTEST_CAUSE(t.getString("TEST_CAUSE"));                 
        l.setRELEVANT_CLINIC_DIAG(t.getString("RELEVANT_CLINIC_DIAG"));         
        l.setSPECIMEN(t.getString("SPECIMEN"));                      
        l.setNOTES_FOR_SPCM(t.getString("NOTES_FOR_SPCM"));             
        l.setSPCM_RECEIVED_DATE_TIME(t.getString("SPCM_RECEIVED_DATE_TIME"));    
        l.setSPCM_SAMPLE_DATE_TIME(t.getString("SPCM_SAMPLE_DATE_TIME"));      
        l.setREQUESTED_DATE_TIME(t.getString("REQUESTED_DATE_TIME"));        
        l.setORDERING_DEPT(t.getString("ORDERING_DEPT"));              
        l.setORDERING_PROVIDER(t.getString("ORDERING_PROVIDER"));          
        l.setPERFORMED_BY(t.getString("PERFORMED_BY"));                 
        l.setRESULT_STATUS(t.getString("RESULT_STATUS"));              
        l.setRESULTS_RPT_DATE_TIME(t.getString("RESULTS_RPT_DATE_TIME"));      
        l.setTRANSCRIPTIONIST(t.getString("TRANSCRIPTIONIST"));           
        l.setVERIFIED_BY(t.getString("VERIFIED_BY"));                
        l.setCOSTS(t.getString("COSTS"));   
        l.setCHARGES(t.getString("CHARGES"));                    
        l.setBILLING_INDICATOR(t.getString("BILLING_INDICATOR"));          
        l.setPRINT_INDICATOR(t.getString("PRINT_INDICATOR"));            
        l.setSUBJECT(t.getString("SUBJECT"));                    
//        l.setLINK_DATE(t.getString("LINK_DATE"));                  
//        l.setPERFORM_INTERNAL_OR_SERGERY(t.getString("PERFORM_INTERNAL_OR_SERGERY"));
//        l.setPERFORM_OUTP_OR_INP(t.getString("PERFORM_OUTP_OR_INP"));        
//        l.setPERFORM_CLINIC_ATTR(t.getString("PERFORM_CLINIC_ATTR"));        
//        l.setPERFORM_DEPT_NAME(t.getString("PERFORM_DEPT_NAME"));          
//        l.setORDER_INTERNAL_OR_SERGERY(t.getString("ORDER_INTERNAL_OR_SERGERY"));  
//        l.setORDER_OUTP_OR_INP(t.getString("ORDER_OUTP_OR_INP"));          
//        l.setORDER_CLINIC_ATTR(t.getString("ORDER_CLINIC_ATTR"));          
//        l.setORDER_DEPT_NAME(t.getString("ORDER_DEPT_NAME"));
        return l;
    }
}
