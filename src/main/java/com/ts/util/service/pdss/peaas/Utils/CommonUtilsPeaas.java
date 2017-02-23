package com.ts.service.pdss.peaas.Utils;

import java.util.ArrayList;
import java.util.List;

import com.ts.entity.pdss.peaas.Beans.TPrescDetailBean;
import com.ts.entity.pdss.peaas.Beans.TPrescMasterBean;
import com.ts.entity.pdss.peaas.Beans.TPrescOutpMrBean;
import com.ts.entity.pdss.peaas.Beans.TPrescPatMasterBean;

/**
 * peaas 组织 处方 bean 
 * @author Administrator
 *
 */
public class CommonUtilsPeaas
{
    /**
     * 病人住信息
     * @param PatMaster
     * @param PrescInfo
     * @param PrescDetails
     * @param PrescDiag
     * @return
     */
    public static TPrescPatMasterBean getPrescPatMasterBean(String[] PatMaster, String[] PrescInfo ,String[][] PrescDetails , String[][] PrescDiag)
    {
        TPrescPatMasterBean ppmb = new TPrescPatMasterBean();
        ppmb.setPATIENT_ID(PatMaster[0]);
        ppmb.setINP_NO(PatMaster[1]);
        ppmb.setNAME(PatMaster[2]);
        ppmb.setNAME_PHONETIC(PatMaster[3]);
        ppmb.setSEX(PatMaster[4]);
        ppmb.setDATE_OF_BIRTH(PatMaster[5]);
        ppmb.setBIRTH_PLACE(PatMaster[6]);
        ppmb.setCITIZENSHIP(PatMaster[7]);
        ppmb.setNATION(PatMaster[8]);
        ppmb.setID_NO(PatMaster[9]);
        ppmb.setIDENTITY(PatMaster[10]);
        ppmb.setCHARGE_TYPE(PatMaster[11]);
        ppmb.setUNIT_IN_CONTRACT(PatMaster[12]);
        ppmb.setMAILING_ADDRESS(PatMaster[13]);
        ppmb.setZIP_CODE(PatMaster[14]);
        ppmb.setPHONE_NUMBER_HOME(PatMaster[15]);
        ppmb.setPHONE_NUMBER_BUSINESS(PatMaster[16]);
        ppmb.setNEXT_OF_KIN(PatMaster[17]);
        ppmb.setRELATIONSHIP(PatMaster[18]);
        ppmb.setNEXT_OF_KIN_ADDR(PatMaster[19]);
        ppmb.setNEXT_OF_KIN_ZIP_CODE(PatMaster[20]);
        ppmb.setNEXT_OF_KIN_PHONE(PatMaster[21]);
        ppmb.setLAST_VISIT_DATE(PatMaster[22]);
        ppmb.setVIP_INDICATOR(PatMaster[23]);
        ppmb.setCREATE_DATE(PatMaster[24]);
        ppmb.setOPERATOR(PatMaster[25]);
        ppmb.setSERVICE_AGENCY(PatMaster[26]);
        ppmb.setBusiness_zip_code(PatMaster[27]);
        ppmb.setPmbs(new TPrescMasterBean[]{getPrescMasterBean(PrescInfo, PrescDetails)});
        ppmb.setPombs((TPrescOutpMrBean[])getPrescOutpMrBean(PrescDiag).toArray(new TPrescOutpMrBean[0]));
        return ppmb;
    }
    
    /**
     * 
     * @param PrescInfo
     * @param PrescDetails
     * @return
     */
    private static TPrescMasterBean  getPrescMasterBean(String[] PrescInfo ,String[][] PrescDetails )
    {
        TPrescMasterBean pmb = new TPrescMasterBean();
        pmb.setPATIENT_ID(PrescInfo[0]);
        pmb.setVISIT_DATE(PrescInfo[1]);
        pmb.setVISIT_NO(PrescInfo[2]);
        pmb.setSERIAL_NO(PrescInfo[3]);
        pmb.setORDERED_BY(PrescInfo[4]);
        pmb.setDOCTOR(PrescInfo[5]);
        pmb.setORDER_DATE(PrescInfo[6]);
        pmb.setPdbs((TPrescDetailBean[])getPrescDetailBean(PrescDetails).toArray(new TPrescDetailBean[0]));
        return pmb;
    }
    
    /**
     * 
     * @param PrescDetails
     * @return
     */
    private static List<TPrescDetailBean> getPrescDetailBean(String[][] PrescDetails)
    {
        List<TPrescDetailBean> pdbs = new ArrayList<TPrescDetailBean>();
        for(int i = 0 ;i < PrescDetails.length ; i++)
        {
            TPrescDetailBean pdb = new TPrescDetailBean();
            pdb.setVISIT_DATE(PrescDetails[i][0]);
            pdb.setVISIT_NO(PrescDetails[i][1]);;
            pdb.setSERIAL_NO(PrescDetails[i][2]);
            pdb.setPRESC_NO(PrescDetails[i][3]);
            pdb.setITEM_NO(PrescDetails[i][4]);
            pdb.setITEM_CLASS(PrescDetails[i][5]);
            pdb.setDRUG_CODE(PrescDetails[i][6]);
            pdb.setDRUG_NAME(PrescDetails[i][7]);
            pdb.setDRUG_SPEC(PrescDetails[i][8]);
            pdb.setFIRM_ID(PrescDetails[i][9]);
            pdb.setUNITS(PrescDetails[i][10]);
            pdb.setAMOUNT(PrescDetails[i][11]);
            pdb.setDOSAGE(PrescDetails[i][12]);
            pdb.setDOSAGE_UNITS(PrescDetails[i][13]);
            pdb.setADMINISTRATION(PrescDetails[i][14]);
            pdb.setFREQUENCY(PrescDetails[i][15]);
            pdb.setPROVIDED_INDICATOR(PrescDetails[i][16]);
            pdb.setCOSTS(PrescDetails[i][17]);
            pdb.setCHARGES(PrescDetails[i][18]);
            pdb.setCHARGE_INDICATOR(PrescDetails[i][19]);
            pdb.setDISPENSARY(PrescDetails[i][20]);
            pdb.setREPETITION(PrescDetails[i][21]);
            pdb.setORDER_NO(PrescDetails[i][22]);
            pdb.setORDER_SUB_NO(PrescDetails[i][23]);
            //FREQ_DETAIL;GETDRUG_FLAG;
            pdbs.add(pdb);
        }
        return pdbs;
    }
    
    /**
     * 
     * @param PrescDiag
     * @return
     */
    private static List<TPrescOutpMrBean> getPrescOutpMrBean(String[][] PrescDiag)
    {
        List<TPrescOutpMrBean> pombs = new ArrayList<TPrescOutpMrBean>();
        for(int i = 0 ;i < PrescDiag.length ; i++)
        {
            TPrescOutpMrBean pomb =  new TPrescOutpMrBean();
            pomb.setPATIENT_ID(PrescDiag[i][0]);
            pomb.setVISIT_DATE(PrescDiag[i][0]);
            pomb.setVISIT_NO(PrescDiag[i][0]);
            pomb.setILLNESS_DESC(PrescDiag[i][0]);
            pomb.setANAMNESIS(PrescDiag[i][0]);
            pomb.setFAMILY_ILL(PrescDiag[i][0]);
            pomb.setMARRITAL(PrescDiag[i][0]);
            pomb.setINDIVIDUAL(PrescDiag[i][0]);
            pomb.setMENSES(PrescDiag[i][0]);
            pomb.setMED_HISTORY(PrescDiag[i][0]);
            pomb.setBODY_EXAM(PrescDiag[i][0]);
            pomb.setDIAG_DESC(PrescDiag[i][0]);
            pomb.setADVICE(PrescDiag[i][0]);
            pomb.setDOCTOR(PrescDiag[i][0]);
            pomb.setORDINAL(PrescDiag[i][0]);
            pomb.setOPERATION_RECORD(PrescDiag[i][0]);
            pomb.setMEDICAL_RECORD(PrescDiag[i][0]);
            pombs.add(pomb);
        }
        return pombs;
    }
}