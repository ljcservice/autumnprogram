package com.ts.service.pdss.ias.manager;
import com.hitzd.his.Beans.TLabTest;
import com.hitzd.his.Beans.TOperationDict;
import com.hitzd.his.Beans.TPatOperation;
import com.hitzd.his.Beans.TPatVitalSigns;

public interface IAntiDrugMM
{
    
    public TOperationDict[] getOperationDict();
    
    /**
     * 返回体检信息 
     */
    public TPatVitalSigns[] getpatVsVisitSigns(String patID , String Visitid);
    /**
     * 返回检验信息 
     * @param patid
     * @param visitid
     * @return
     */
    public TLabTest[] getPatLabTest(String patid , String visitid);
    
    public TLabTest[] getpatLabTestNoDetail(String patid , String visitid);
    
    public TLabTest getpatLabTestDetail(String TestNO);
    
    /**
     * 返回手术信息 
     * @param patid
     * @param visitid
     * @return
     */
    public TPatOperation[] getPatVsVisitOperation(String patid , String visitid);

}
