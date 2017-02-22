package com.hitzd.his.Beans;

import java.util.ArrayList;

import java.util.List;

/**
 * ¼ìÑé×¡¼ÇÂ¼
 * @author Administrator
 *
 */
public class TLabTest extends TBaseBean
{
    private String TEST_NO;                    
    private String PRIORITY_INDICATOR;         
    private String PATIENT_ID;                 
    private String VISIT_ID;                   
    private String WORKING_ID;                 
    private String EXECUTE_DATE;               
    private String NAME;                       
    private String NAME_PHONETIC;              
    private String CHARGE_TYPE;                
    private String SEX;                        
    private String AGE;                       
    private String TEST_CAUSE;                 
    private String RELEVANT_CLINIC_DIAG;       
    private String SPECIMEN;                   
    private String NOTES_FOR_SPCM;             
    private String SPCM_RECEIVED_DATE_TIME;    
    private String SPCM_SAMPLE_DATE_TIME;      
    private String REQUESTED_DATE_TIME;        
    private String ORDERING_DEPT;              
    private String ORDERING_PROVIDER;          
    private String PERFORMED_BY;               
    private String RESULT_STATUS;              
    private String RESULTS_RPT_DATE_TIME;      
    private String TRANSCRIPTIONIST;           
    private String VERIFIED_BY;                
    private String COSTS;                      
    private String CHARGES;                    
    private String BILLING_INDICATOR;          
    private String PRINT_INDICATOR;            
    private String SUBJECT;                    
    private String LINK_DATE;                  
    private String PERFORM_INTERNAL_OR_SERGERY;
    private String PERFORM_OUTP_OR_INP;        
    private String PERFORM_CLINIC_ATTR;        
    private String PERFORM_DEPT_NAME;          
    private String ORDER_INTERNAL_OR_SERGERY;  
    private String ORDER_OUTP_OR_INP;          
    private String ORDER_CLINIC_ATTR;          
    private String ORDER_DEPT_NAME;
    
    private List<TLabTestItems> labitems = new ArrayList<TLabTestItems>();
    
    public TLabTestItems[] getlabTestItems()
    {
        return (TLabTestItems[])this.labitems.toArray(new TLabTestItems[0]);
    }
    
    public void addLabTestItems(TLabTestItems item)
    {
        labitems.add(item);
    }
    
    public void addLabTestItems(List<TLabTestItems> items)
    {
        labitems.addAll(items);
    }
    
    private List<TLabResult> labresult = new ArrayList<TLabResult>();
    
    public TLabResult[] getLabResult()
    {
        return (TLabResult[])this.labresult.toArray(new TLabResult[0]);
    }
    
    public void addLabResult(TLabResult labrslt)
    {
        this.labresult.add(labrslt);
    }
    
    public void addLabResult(List<TLabResult> labrslts)
    {
        this.labresult.addAll(labrslts);
    }
    
    public String getTEST_NO()
    {
        return TEST_NO;
    }
    public void setTEST_NO(String tEST_NO)
    {
        TEST_NO = tEST_NO;
    }
    public String getPRIORITY_INDICATOR()
    {
        return PRIORITY_INDICATOR;
    }
    public void setPRIORITY_INDICATOR(String pRIORITY_INDICATOR)
    {
        PRIORITY_INDICATOR = pRIORITY_INDICATOR;
    }
    public String getPATIENT_ID()
    {
        return PATIENT_ID;
    }
    public void setPATIENT_ID(String pATIENT_ID)
    {
        PATIENT_ID = pATIENT_ID;
    }
    public String getVISIT_ID()
    {
        return VISIT_ID;
    }
    public void setVISIT_ID(String vISIT_ID)
    {
        VISIT_ID = vISIT_ID;
    }
    public String getWORKING_ID()
    {
        return WORKING_ID;
    }
    public void setWORKING_ID(String wORKING_ID)
    {
        WORKING_ID = wORKING_ID;
    }
    public String getEXECUTE_DATE()
    {
        return EXECUTE_DATE;
    }
    public void setEXECUTE_DATE(String eXECUTE_DATE)
    {
        EXECUTE_DATE = eXECUTE_DATE;
    }
    public String getNAME()
    {
        return NAME;
    }
    public void setNAME(String nAME)
    {
        NAME = nAME;
    }
    public String getNAME_PHONETIC()
    {
        return NAME_PHONETIC;
    }
    public void setNAME_PHONETIC(String nAME_PHONETIC)
    {
        NAME_PHONETIC = nAME_PHONETIC;
    }
    public String getCHARGE_TYPE()
    {
        return CHARGE_TYPE;
    }
    public void setCHARGE_TYPE(String cHARGE_TYPE)
    {
        CHARGE_TYPE = cHARGE_TYPE;
    }
    public String getSEX()
    {
        return SEX;
    }
    public void setSEX(String sEX)
    {
        SEX = sEX;
    }
    public String getAGE()
    {
        return AGE;
    }
    public void setAGE(String aGE)
    {
        AGE = aGE;
    }
    public String getTEST_CAUSE()
    {
        return TEST_CAUSE;
    }
    public void setTEST_CAUSE(String tEST_CAUSE)
    {
        TEST_CAUSE = tEST_CAUSE;
    }
    public String getRELEVANT_CLINIC_DIAG()
    {
        return RELEVANT_CLINIC_DIAG;
    }
    public void setRELEVANT_CLINIC_DIAG(String rELEVANT_CLINIC_DIAG)
    {
        RELEVANT_CLINIC_DIAG = rELEVANT_CLINIC_DIAG;
    }
    public String getSPECIMEN()
    {
        return SPECIMEN;
    }
    public void setSPECIMEN(String sPECIMEN)
    {
        SPECIMEN = sPECIMEN;
    }
    public String getNOTES_FOR_SPCM()
    {
        return NOTES_FOR_SPCM;
    }
    public void setNOTES_FOR_SPCM(String nOTES_FOR_SPCM)
    {
        NOTES_FOR_SPCM = nOTES_FOR_SPCM;
    }
    public String getSPCM_RECEIVED_DATE_TIME()
    {
        return SPCM_RECEIVED_DATE_TIME;
    }
    public void setSPCM_RECEIVED_DATE_TIME(String sPCM_RECEIVED_DATE_TIME)
    {
        SPCM_RECEIVED_DATE_TIME = sPCM_RECEIVED_DATE_TIME;
    }
    public String getSPCM_SAMPLE_DATE_TIME()
    {
        return SPCM_SAMPLE_DATE_TIME;
    }
    public void setSPCM_SAMPLE_DATE_TIME(String sPCM_SAMPLE_DATE_TIME)
    {
        SPCM_SAMPLE_DATE_TIME = sPCM_SAMPLE_DATE_TIME;
    }
    public String getREQUESTED_DATE_TIME()
    {
        return REQUESTED_DATE_TIME;
    }
    public void setREQUESTED_DATE_TIME(String rEQUESTED_DATE_TIME)
    {
        REQUESTED_DATE_TIME = rEQUESTED_DATE_TIME;
    }
    public String getORDERING_DEPT()
    {
        return ORDERING_DEPT;
    }
    public void setORDERING_DEPT(String oRDERING_DEPT)
    {
        ORDERING_DEPT = oRDERING_DEPT;
    }
    public String getORDERING_PROVIDER()
    {
        return ORDERING_PROVIDER;
    }
    public void setORDERING_PROVIDER(String oRDERING_PROVIDER)
    {
        ORDERING_PROVIDER = oRDERING_PROVIDER;
    }
    public String getPERFORMED_BY()
    {
        return PERFORMED_BY;
    }
    public void setPERFORMED_BY(String pERFORMED_BY)
    {
        PERFORMED_BY = pERFORMED_BY;
    }
    public String getRESULT_STATUS()
    {
        return RESULT_STATUS;
    }
    public void setRESULT_STATUS(String rESULT_STATUS)
    {
        RESULT_STATUS = rESULT_STATUS;
    }
    public String getRESULTS_RPT_DATE_TIME()
    {
        return RESULTS_RPT_DATE_TIME;
    }
    public void setRESULTS_RPT_DATE_TIME(String rESULTS_RPT_DATE_TIME)
    {
        RESULTS_RPT_DATE_TIME = rESULTS_RPT_DATE_TIME;
    }
    public String getTRANSCRIPTIONIST()
    {
        return TRANSCRIPTIONIST;
    }
    public void setTRANSCRIPTIONIST(String tRANSCRIPTIONIST)
    {
        TRANSCRIPTIONIST = tRANSCRIPTIONIST;
    }
    public String getVERIFIED_BY()
    {
        return VERIFIED_BY;
    }
    public void setVERIFIED_BY(String vERIFIED_BY)
    {
        VERIFIED_BY = vERIFIED_BY;
    }
    public String getCOSTS()
    {
        return COSTS;
    }
    public void setCOSTS(String cOSTS)
    {
        COSTS = cOSTS;
    }
    public String getCHARGES()
    {
        return CHARGES;
    }
    public void setCHARGES(String cHARGES)
    {
        CHARGES = cHARGES;
    }
    public String getBILLING_INDICATOR()
    {
        return BILLING_INDICATOR;
    }
    public void setBILLING_INDICATOR(String bILLING_INDICATOR)
    {
        BILLING_INDICATOR = bILLING_INDICATOR;
    }
    public String getPRINT_INDICATOR()
    {
        return PRINT_INDICATOR;
    }
    public void setPRINT_INDICATOR(String pRINT_INDICATOR)
    {
        PRINT_INDICATOR = pRINT_INDICATOR;
    }
    public String getSUBJECT()
    {
        return SUBJECT;
    }
    public void setSUBJECT(String sUBJECT)
    {
        SUBJECT = sUBJECT;
    }
    public String getLINK_DATE()
    {
        return LINK_DATE;
    }
    public void setLINK_DATE(String lINK_DATE)
    {
        LINK_DATE = lINK_DATE;
    }
    public String getPERFORM_INTERNAL_OR_SERGERY()
    {
        return PERFORM_INTERNAL_OR_SERGERY;
    }
    public void setPERFORM_INTERNAL_OR_SERGERY(String pERFORM_INTERNAL_OR_SERGERY)
    {
        PERFORM_INTERNAL_OR_SERGERY = pERFORM_INTERNAL_OR_SERGERY;
    }
    public String getPERFORM_OUTP_OR_INP()
    {
        return PERFORM_OUTP_OR_INP;
    }
    public void setPERFORM_OUTP_OR_INP(String pERFORM_OUTP_OR_INP)
    {
        PERFORM_OUTP_OR_INP = pERFORM_OUTP_OR_INP;
    }
    public String getPERFORM_CLINIC_ATTR()
    {
        return PERFORM_CLINIC_ATTR;
    }
    public void setPERFORM_CLINIC_ATTR(String pERFORM_CLINIC_ATTR)
    {
        PERFORM_CLINIC_ATTR = pERFORM_CLINIC_ATTR;
    }
    public String getPERFORM_DEPT_NAME()
    {
        return PERFORM_DEPT_NAME;
    }
    public void setPERFORM_DEPT_NAME(String pERFORM_DEPT_NAME)
    {
        PERFORM_DEPT_NAME = pERFORM_DEPT_NAME;
    }
    public String getORDER_INTERNAL_OR_SERGERY()
    {
        return ORDER_INTERNAL_OR_SERGERY;
    }
    public void setORDER_INTERNAL_OR_SERGERY(String oRDER_INTERNAL_OR_SERGERY)
    {
        ORDER_INTERNAL_OR_SERGERY = oRDER_INTERNAL_OR_SERGERY;
    }
    public String getORDER_OUTP_OR_INP()
    {
        return ORDER_OUTP_OR_INP;
    }
    public void setORDER_OUTP_OR_INP(String oRDER_OUTP_OR_INP)
    {
        ORDER_OUTP_OR_INP = oRDER_OUTP_OR_INP;
    }
    public String getORDER_CLINIC_ATTR()
    {
        return ORDER_CLINIC_ATTR;
    }
    public void setORDER_CLINIC_ATTR(String oRDER_CLINIC_ATTR)
    {
        ORDER_CLINIC_ATTR = oRDER_CLINIC_ATTR;
    }
    public String getORDER_DEPT_NAME()
    {
        return ORDER_DEPT_NAME;
    }
    public void setORDER_DEPT_NAME(String oRDER_DEPT_NAME)
    {
        ORDER_DEPT_NAME = oRDER_DEPT_NAME;
    }
}
