package com.ts.service.pdss.ias.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Beans.TLabResult;
import com.hitzd.his.Beans.TLabTest;
import com.hitzd.his.Beans.TLabTestItems;
import com.hitzd.his.Beans.TOperationDict;
import com.hitzd.his.Beans.TPatOperation;
import com.hitzd.his.Beans.TPatVitalSigns;
import com.hitzd.his.RowMapperBeans.LabResultMapper;
import com.hitzd.his.RowMapperBeans.LabTestItemsMapper;
import com.hitzd.his.RowMapperBeans.LabTestMapper;
import com.hitzd.his.casehistory.helper.CaseHistoryFactory;
import com.hitzd.his.casehistory.helper.CaseHistoryHelperBase;
import com.hitzd.his.casehistory.helper.CaseHistoryHelperUtils;
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;
import com.hitzd.persistent.Persistent4DB;
import com.ts.dao.DaoSupportPdss;
import com.ts.service.pdss.ias.manager.IAntiDrugMM;
import com.ts.util.PageData;

/**
 * 得到his中数据 
 * @author Administrator
 *
 */
@Service
public class AntiDrugMMBean extends Persistent4DB implements IAntiDrugMM{
	@Resource(name = "daoSupportPdss")
	public DaoSupportPdss dao;
	
	
//	ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
	
    @SuppressWarnings ("unchecked")
    @Override
    public TOperationDict[] getOperationDict()
    {
        //setQueryCode("HIS");
        try
        {
        	ICaseHistoryHelper ichh = new CaseHistoryHelperBase();
        	List<TCommonRecord> orderBy =  new ArrayList<TCommonRecord>();
        	orderBy.add(CaseHistoryHelperUtils.genOrderCR("input_code", ""));
        	String sql = ichh.genSQL("*", "comm.operation_dict", null, null, orderBy);
            List<TOperationDict> tod = query.query(sql, new RowMapper(){
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException
                {
                    TOperationDict o = new TOperationDict();
                    o.setOperationCode(rs.getString("operation_code"));
                    o.setOperationName(rs.getString("operation_name"));
                    o.setInputCode(rs.getString("input_code"));
                    return o;
                }
            });
            return (TOperationDict[])tod.toArray(new TOperationDict[0]);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return new TOperationDict[0];
    }
    
    @SuppressWarnings ("unchecked")
    public TPatVitalSigns[] getpatVsVisitSigns(String patId , String visitId)
    {
        List<TPatVitalSigns> patVSs = new ArrayList<TPatVitalSigns>();
        try
        {
//            ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
//            String strFields = "*"; 
//            List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
//            TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", patID, "Char", "", "", "");
//            lsWheres.add(crWheres);
//            crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", Visitid , "Char", "", "", "");
//            lsWheres.add(crWheres);
//            List<TCommonRecord> lsGroup  = new ArrayList<TCommonRecord>();
//            List<TCommonRecord> lsOrder  = new ArrayList<TCommonRecord>();
            //List<TCommonRecord> result = ichh.fetchVitalSignsRec2CR(strFields, lsWheres, lsGroup, lsOrder, query);
//            List<PageData> result = query.query("select * from ordadm.Vital_Signs_Rec t where t.Patient_ID = '" + patID 
//                    + "' and Visit_ID = '" + Visitid + "'", new CommonMapper());
        	PageData pd =new PageData();
        	pd.put("patId", patId);
        	pd.put("visitId", visitId);
        	List<PageData> result = (List<PageData>) dao.findForList("PdssMapper.getpatVsVisitSigns",pd);
            String strFlag = "";
            TPatVitalSigns patv = null;
            for(PageData t : result)
            {
                if(!strFlag.equals(t.get("RECORDING_DATE")))
                {
                    if(patv != null)patVSs.add(patv);
                    patv = new TPatVitalSigns();
                    patv.setSVDate(t.getString("RECORDING_DATE"));
                    strFlag = t.getString("RECORDING_DATE");
                }
                if("脉搏".equals(t.get("VITAL_SIGNS")))
                {
                    patv.setPulse("脉搏");
                    patv.setPulseValue(t.getString("VITAL_SIGNS_VALUES"));
                }
                if("血压Low".equals(t.get("VITAL_SIGNS")))
                {
                    patv.setBloodLow("血压Low");
                    patv.setBloodLowValue(t.getString("VITAL_SIGNS_VALUES"));
                }
                if("血压high".equals(t.get("VITAL_SIGNS")))
                {
                    patv.setBloodhigh("血压high");
                    patv.setBloodhighValue(t.getString("VITAL_SIGNS_VALUES"));
                }
                if("体温".equals(t.get("VITAL_SIGNS")))
                {
                    patv.setTemperature("体温");
                    patv.setTemperatureValue(t.getString("VITAL_SIGNS_VALUES"));
                }
            }
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            query = null;
        }
        return (TPatVitalSigns[]) patVSs.toArray(new TPatVitalSigns[0]);
    }
    
    /**
     * 返回检验信息 
     * @param patid
     * @param visitid
     * @return
     */
    @SuppressWarnings ("unchecked")
    public TLabTest[] getPatLabTest(String patid , String visitid)
    {
        List<TLabTest> labTest = new ArrayList<TLabTest>();
        setQueryCode("HIS");
        try
        {
//            ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
            String strfields = " * ";
            String strtabls  = "select * from lab.lab_test_master d where d.patient_id = '" + patid + "' and d.visit_id = '" + visitid + "'";
//            List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
//            TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("d.patient_id", patid, "Char", "", "", ""); 
//            lsWheres.add(crWheres);
//            crWheres = CaseHistoryHelperUtils.genWhereCR("d.visit_id", visitid, "Char", "", "", ""); 
//            lsWheres.add(crWheres);
//            List<TCommonRecord> lsGroups = new ArrayList<TCommonRecord>();
//            List<TCommonRecord> lsOrders = new ArrayList<TCommonRecord>();
//            labTest = query.query(ichh.genSQL(strfields, strtabls, lsWheres, lsGroups, lsOrders), new LabTestMapper());
            labTest = query.query(strtabls, new LabTestMapper());
            for(TLabTest labt : labTest)
            {
                strtabls = "select * from lab.lab_test_items t where t.test_no = '" + labt.getTEST_NO() + "'";
//                lsWheres = new ArrayList<TCommonRecord>();
//                crWheres = CaseHistoryHelperUtils.genWhereCR("t.test_no",labt.getTEST_NO(),"Char", "", "", ""); 
//                lsWheres.add(crWheres);
//                List<TLabTestItems> labTestItems = query.query(ichh.genSQL(strfields, strtabls, lsWheres, lsGroups, lsOrders), new LabTestItemsMapper());
                List<TLabTestItems> labTestItems = query.query(strtabls, new LabTestItemsMapper());
                if(labTestItems != null && labTestItems.size() > 0 ) 
                {
                    labt.addLabTestItems(labTestItems);
                }
                strtabls = "select * from lab.Lab_Result t where t.test_no = '" + labt.getTEST_NO() + "'";
//                lsWheres = new ArrayList<TCommonRecord>();
//                crWheres = CaseHistoryHelperUtils.genWhereCR("t.test_no",labt.getTEST_NO(),"Char", "", "", ""); 
//                lsWheres.add(crWheres);
//                List<TLabResult> labrslts = query.query(ichh.genSQL(strfields, strtabls, lsWheres, lsGroups, lsOrders), new LabResultMapper());
                List<TLabResult> labrslts = query.query(strtabls, new LabResultMapper());
                if(labrslts != null && labrslts.size() > 0 )
                {
                    labt.addLabResult(labrslts);
                }    
            }
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            query = null;
        }
        return (TLabTest[])labTest.toArray(new TLabTest[0]);
    }
    
    @SuppressWarnings ("unchecked")
    public TLabTest[] getpatLabTestNoDetail(String patid , String visitid)
    {
        List<TLabTest> labTest = new ArrayList<TLabTest>();
        setQueryCode("HIS");
        try
        {
            ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
            String strfields = " * ";
            String strtabls  = "select * from lab.lab_test_master d where d.patient_id = '" + patid + "' and d.visit_id = '" + visitid + "'";
            List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
            TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("d.patient_id", patid, "Char", "", "", ""); 
            lsWheres.add(crWheres);
            crWheres = CaseHistoryHelperUtils.genWhereCR("d.visit_id", visitid, "Char", "", "", ""); 
            lsWheres.add(crWheres);
            List<TCommonRecord> lsGroups = new ArrayList<TCommonRecord>();
            List<TCommonRecord> lsOrders = new ArrayList<TCommonRecord>();
            labTest = query.query(ichh.genSQL(strfields, strtabls, lsWheres, lsGroups, lsOrders), new LabTestMapper());
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            query = null;
        }
        return (TLabTest[])labTest.toArray(new TLabTest[0]);
    }
    
    @SuppressWarnings ("unchecked")
    public TLabTest getpatLabTestDetail(String TestNO)
    {
        TLabTest labt = new TLabTest();
        setQueryCode("HIS");
        try
        {
            ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
            String strfields = "*";
            String strtabls = "select * from lab.lab_test_items t where t.test_no = '" + TestNO + "'";
            List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
            TCommonRecord crWheres       = CaseHistoryHelperUtils.genWhereCR("t.test_no",labt.getTEST_NO(),"Char", "", "", ""); 
            lsWheres.add(crWheres);
            List<TCommonRecord> lsGroups = new ArrayList<TCommonRecord>();
            List<TCommonRecord> lsOrders = new ArrayList<TCommonRecord>();
//            List<TLabTestItems> labTestItems = query.query(ichh.genSQL(strfields, strtabls, lsWheres, lsGroups, lsOrders), new LabTestItemsMapper());
            List<TLabTestItems> labTestItems = query.query(strtabls, new LabTestItemsMapper());
            if(labTestItems != null && labTestItems.size() > 0 ) 
            {
                labt.addLabTestItems(labTestItems);
            }
            strtabls = "select * from lab.Lab_Result t where t.test_no = '" + TestNO + "'";
            lsWheres = new ArrayList<TCommonRecord>();
            crWheres = CaseHistoryHelperUtils.genWhereCR("t.test_no",labt.getTEST_NO(),"Char", "", "", ""); 
            lsWheres.add(crWheres);
//            List<TLabResult> labrslts = query.query(ichh.genSQL(strfields, strtabls, lsWheres, lsGroups, lsOrders), new LabResultMapper());
            List<TLabResult> labrslts = query.query(strtabls, new LabResultMapper());
            if(labrslts != null && labrslts.size() > 0 )
            {
                labt.addLabResult(labrslts);
            } 
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            query = null;
        }
        return labt;
    }
    
    /**
     * 返回手术信息 
     * @param patid
     * @param visitid
     * @return
     */
    @SuppressWarnings ("unchecked")
    public TPatOperation[] getPatVsVisitOperation(String patid , String visitid)
    {
        setQueryCode("HIS");
        List<TPatOperation> patopers = new ArrayList<TPatOperation>();
        try
        {

          ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
          String strFields = "*"; 
          List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
          TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", patid, "Char", "", "", "");
          lsWheres.add(crWheres);
          crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", visitid , "Char", "", "", "");
          lsWheres.add(crWheres);
          List<TCommonRecord> lsGroup  = new ArrayList<TCommonRecord>();
          List<TCommonRecord> lsOrder  = new ArrayList<TCommonRecord>();
          List<TCommonRecord> operList =  ichh.fetchOperation2CR(strFields, lsWheres, lsGroup, lsOrder, query);
//            List<TCommonRecord> operList =  query.query("select * from medrec.Operation t where t.Patient_ID = '" 
//                        + patid + "' and t.Visit_ID = '" + visitid + "'", new CommonMapper());
            if(operList == null) return (TPatOperation[]) patopers.toArray(new TPatOperation[0]);
            for(TCommonRecord t : operList)
            {
                TPatOperation patOper = new TPatOperation();
                patOper.setOperCode(t.get("OPERATION_CODE"));
                patOper.setOperName(t.get("OPERATION_DESC"));
                patOper.setOperLevel(t.get("WOUND_GRADE"));
                patOper.setOperStartTime(t.get("OPERATING_DATE"));
                patOper.setOperEndTime(t.get("OPERATION_NO"));
                patopers.add(patOper);
            }
        }
        catch(Exception  e )
        {
            e.printStackTrace();
        }
        finally
        {
            query = null;
        }
        return (TPatOperation[]) patopers.toArray(new TPatOperation[0]);
    }
    public static void main(String[] args) {
    	new AntiDrugMMBean().getOperationDict();
	}
}
