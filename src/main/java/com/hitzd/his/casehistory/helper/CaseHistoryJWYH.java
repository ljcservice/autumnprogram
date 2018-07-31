package com.hitzd.his.casehistory.helper;

import java.util.ArrayList;
import java.util.List;
import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.his.casehistory.CaseHistory;

public class CaseHistoryJWYH extends CaseHistoryCommon
{

	@Override
	public CaseHistory fetchCaseHistory(String PatientID, String VisitID, JDBCQueryImpl srcQuery) 
	{
		CaseHistory ch = new CaseHistory();
		return ch;
	}

	@Override
	public TCommonRecord fetchCaseHistory2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) 
	{
		// 病人基本信息
		TCommonRecord crPatMasterIndex = null;
		String ErrorInfo = "";
		try
		{
			crPatMasterIndex = fetchPatInfo2CR(PatientID, srcQuery);
		}
		catch (Exception ex)
		{
			ErrorInfo = ex.getMessage();
			ex.printStackTrace();
		}
		if (crPatMasterIndex != null)
		{
			
			TCommonRecord crPatVisit = null;
			try 
			{
				crPatVisit = fetchPatVisit2CR(PatientID, VisitID, srcQuery);
			} 
			catch (Exception e) 
			{
				ErrorInfo += "\n就诊信息：" + e.getMessage();
				e.printStackTrace();
			}
			crPatMasterIndex.setObj(Key_PatVisit, crPatVisit);
			if (crPatVisit != null)
			{
		    	List<TCommonRecord> OperMaster = null;
				try 
				{
					OperMaster = null;// fetchOperationMaster2CR(PatientID, VisitID, srcQuery);
				} 
				catch (Exception e) 
				{
					ErrorInfo += "\nOperationMaster: " + e.getMessage();
					e.printStackTrace();
				}
				crPatVisit.setObj(Key_OperationMaster, OperMaster);

				List<TCommonRecord> Operation = null;
				try 
				{
					Operation = fetchOperation2CR(PatientID, VisitID, srcQuery);
				} 
				catch (Exception e) 
				{
					ErrorInfo += "\nOperation: " + e.getMessage();
					e.printStackTrace();
				}
				crPatVisit.setObj(Key_Operation, Operation);
		    	
				List<TCommonRecord> OperationName = null;
				try 
				{
					OperationName = null ;//fetchOperationName2CR(PatientID, VisitID, srcQuery);
				} 
				catch (Exception e) 
				{
					ErrorInfo += "\nOperationName: " + e.getMessage();
					e.printStackTrace();
				}
				crPatVisit.setObj(Key_OperationName, OperationName);
		    	
				List<TCommonRecord> DrugDispenseRec = null;
				try 
				{
					//DrugDispenseRec = fetchDrugDispenseRec2CR(PatientID, VisitID, srcQuery);
				} 
				catch (Exception e8) 
				{
					ErrorInfo += "\n摆药记录: " + e8.getMessage();
					e8.printStackTrace();
				}
				crPatVisit.setObj(Key_DrugDispenseRec, DrugDispenseRec);
		    	
		    	// fetchPatsInHospital2CR(PatientID, VisitID, crPatVisit, srcQuery);
				
				List<TCommonRecord> orders = null;
				try 
				{
					orders = fetchOrders2CR(PatientID, VisitID, srcQuery);
				} 
				catch (Exception e7) 
				{
					ErrorInfo += "\n医嘱: " + e7.getMessage();
					e7.printStackTrace();
				}
		    	crPatVisit.setObj(Key_Orders, orders);

		    	List<TCommonRecord> diagnosis = null;
				try 
				{
					diagnosis = fetchDiagnosis2CR(PatientID, VisitID, srcQuery);
				} 
				catch (Exception e6) 
				{
					ErrorInfo += "\n诊断: " + e6.getMessage();
					e6.printStackTrace();
				}
		    	crPatVisit.setObj(Key_Diagnosis, diagnosis);
		    	
		    	List<TCommonRecord> InpBillDetail = null;
				try 
				{
					InpBillDetail = fetchInpBillDetail2CR(PatientID, VisitID, srcQuery);
				} 
				catch (Exception e5) 
				{
					ErrorInfo += "\n住院账单: " + e5.getMessage();
					e5.printStackTrace();
				}
		    	crPatVisit.setObj(Key_InpBillDetail, InpBillDetail);
		    	
		    	List<TCommonRecord> LabTestMaster = null;
				try 
				{
//					LabTestMaster = fetchLabTestMaster2CR(PatientID, VisitID, srcQuery);
				} 
				catch (Exception e4) 
				{
					ErrorInfo += "\n检查记录: " + e4.getMessage();
					e4.printStackTrace();
				}
		    	crPatVisit.setObj(Key_LabTestMaster, LabTestMaster);
		    	
		    	List<TCommonRecord> VitalSignsRec = null;
				try 
				{
//					VitalSignsRec = fetchVitalSignsRec2CR(PatientID, VisitID, srcQuery);
				} 
				catch (Exception e3) 
				{
					ErrorInfo += "\n体征信息: " + e3.getMessage();
					e3.printStackTrace();
				}
		    	crPatVisit.setObj(Key_VitalSignsRec, VitalSignsRec);
		    	
		    	List<TCommonRecord> ExamMaster = null;
				try 
				{
//					ExamMaster = fetchExamMaster2CR(PatientID, VisitID, srcQuery);
				} 
				catch (Exception e2) 
				{
					ErrorInfo += "\n检验记录: " + e2.getMessage();
					e2.printStackTrace();
				}
		    	crPatVisit.setObj(Key_ExamMaster, ExamMaster);
		    	
		    	List<TCommonRecord> GermTest = null;
				try 
				{
//					GermTest = fetchGermTest2CR(PatientID, VisitID, srcQuery);
				} 
				catch (Exception e1) 
				{
					ErrorInfo += "\n微生物: " + e1.getMessage();
					e1.printStackTrace();
				}
		    	crPatVisit.setObj(Key_GermTest, GermTest);
		    	
		    	List<TCommonRecord> GermTestResult = null;
				try 
				{
//					GermTestResult = fetchGermTestResult2CR(PatientID, VisitID, srcQuery);
				} 
				catch (Exception e)
				{
					ErrorInfo += "\n涂片检验: " + e.getMessage();
					e.printStackTrace();
				}
		    	crPatVisit.setObj(Key_GermTestResult, GermTestResult);
		    	/*List<TCommonRecord> DrugPrescMaster = null;
		    	try
		    	{
		    		DrugPrescMaster = fetchDrugPrescMaster2CR(PatientID, crPatVisit.get("ADMISSION_DATE_TIME"), crPatVisit.get("DISCHARGE_DATE_TIME"), srcQuery);
		    	}
		    	catch(Exception e)
		    	{
		    		ErrorInfo += "\n处方信息：" + e.getMessage();
		    		e.printStackTrace();
		    	}
		    	crPatVisit.setObj(Key_DrugPrescMaster, DrugPrescMaster);
		    	*/
			}
			if (ErrorInfo.length() > 0)
				crPatMasterIndex.setObj("ErrorInfo", ErrorInfo);
		}
		return crPatMasterIndex;
	}
	
	public TCommonRecord fetchPatInfo2CR(String PatientID, JDBCQueryImpl srcQuery) throws Exception
	{
		// 病人基本信息
		String sql = "Select * from MedRec.Pat_Master_Index where Patient_ID=?";
		String strFields = "*"; 
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> lsGroup  = new ArrayList<TCommonRecord>();
		List<TCommonRecord> lsOrder  = new ArrayList<TCommonRecord>();
		return fetchPatInfo2CR(strFields, lsWheres, lsGroup, lsOrder, srcQuery);
	}
	
	public TCommonRecord fetchPatInfo2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " MedRec.Pat_Master_Index ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
		TCommonRecord crPatMasterIndex = (TCommonRecord) srcQuery.queryForObject(sql, new CommonMapper());
		return crPatMasterIndex;
	}
	
	public List<TCommonRecord> fetchPatVisit2CR(String PatientID, JDBCQueryImpl srcQuery) throws Exception
	{
		// 病人就诊信息
		String sql = "select * from MedRec.pat_visit  where Patient_ID = '" + PatientID + "'";
		@SuppressWarnings("unchecked")
		List<TCommonRecord> list = srcQuery.query(sql, new CommonMapper());
		for (TCommonRecord crPatVisit : list)
		{
			// 入院科室信息
	    	TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, crPatVisit.get("DEPT_ADMISSION_TO"));
	    	crPatVisit.set("IN_DEPT_NAME",            crDept.get("DEPT_NAME"));
	    	crPatVisit.set("IN_INTERNAL_OR_SERGERY",  crDept.get("INTERNAL_OR_SERGERY"));
	    	crPatVisit.set("IN_CLINIC_ATTR",          crDept.get("CLINIC_ATTR"));
	    	crPatVisit.set("IN_OUTP_OR_INP",          crDept.get("OUTP_OR_INP"));
	    	// 出院科室信息
	    	crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, crPatVisit.get("DEPT_DISCHARGE_FROM"));
	    	crPatVisit.set("OUT_DEPT_NAME",           crDept.get("DEPT_NAME"));
	    	crPatVisit.set("OUT_INTERNAL_OR_SERGERY", crDept.get("INTERNAL_OR_SERGERY"));
	    	crPatVisit.set("OUT_CLINIC_ATTR",         crDept.get("CLINIC_ATTR"));
	    	crPatVisit.set("OUT_OUTP_OR_INP",         crDept.get("OUTP_OR_INP"));			
		}
		return list;
	}
	
	public TCommonRecord fetchPatVisit2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		// 病人就诊信息
		String sql = "select * from MedRec.pat_visit  where Patient_ID = '" + PatientID + "' and visit_id = " + VisitID;
		TCommonRecord crPatVisit = null;
		crPatVisit = (TCommonRecord)srcQuery.queryForObject(sql, new CommonMapper());
		if (crPatVisit != null)
		{
			// 入院科室信息
	    	TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, crPatVisit.get("DEPT_ADMISSION_TO"));
	    	crPatVisit.set("IN_DEPT_NAME",           crDept.get("DEPT_NAME"));
	    	crPatVisit.set("IN_INTERNAL_OR_SERGERY", crDept.get("INTERNAL_OR_SERGERY"));
	    	crPatVisit.set("IN_CLINIC_ATTR",         crDept.get("CLINIC_ATTR"));
	    	crPatVisit.set("IN_OUTP_OR_INP",         crDept.get("OUTP_OR_INP"));
	    	// 出院科室信息
	    	crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, crPatVisit.get("DEPT_DISCHARGE_FROM"));
	    	crPatVisit.set("OUT_DEPT_NAME",           crDept.get("DEPT_NAME"));
	    	crPatVisit.set("OUT_INTERNAL_OR_SERGERY", crDept.get("INTERNAL_OR_SERGERY"));
	    	crPatVisit.set("OUT_CLINIC_ATTR",         crDept.get("CLINIC_ATTR"));
	    	crPatVisit.set("OUT_OUTP_OR_INP",         crDept.get("OUTP_OR_INP"));			
		}
		return crPatVisit;
	}
	
	public List<TCommonRecord> fetchOperation2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		//病人手术信息
		/*
		String sql = "Select * from medrec.Operation  where Patient_ID = '" + PatientID + "' and visit_id = " + VisitID;
		List<TCommonRecord> Operation = srcQuery.query(sql, new CommonMapper());
		*/
		String strFields = "*"; 
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> lsGroup  = new ArrayList<TCommonRecord>();
		List<TCommonRecord> lsOrder  = new ArrayList<TCommonRecord>();
		return fetchOperation2CR(strFields, lsWheres, lsGroup, lsOrder, srcQuery);
	}
	
	public List<TCommonRecord> fetchOperation2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " medrec.Operation ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
		@SuppressWarnings("unchecked")
		List<TCommonRecord> Operation = srcQuery.query(sql, new CommonMapper());
		return Operation;
	}
	
	@SuppressWarnings("unchecked")
	public List<TCommonRecord> fetchOperationName2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		//病人手术信息
		//String sql = "Select * from surgery.Operation_Name  where Patient_ID = '" + PatientID + "' and visit_id = " + VisitID;
		String strFields = "*"; 
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> lsGroup  = new ArrayList<TCommonRecord>();
		List<TCommonRecord> lsOrder  = new ArrayList<TCommonRecord>();
		return fetchOperationName2CR(strFields, lsWheres, lsGroup, lsOrder, srcQuery);
	}
	
	public List<TCommonRecord> fetchOperationName2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		//病人手术信息
		String sql = this.genSQL(strFields, " surgery.Operation_Name ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
		@SuppressWarnings("unchecked")
		List<TCommonRecord> OperationName = srcQuery.query(sql, new CommonMapper());
		return OperationName;
	}
	
	public List<TCommonRecord>  fetchDrugDispenseRec2CR(String PatientID, String VisitID,  JDBCQueryImpl srcQuery) throws Exception
	{
		//摆药记录
		String strFields = "*"; 
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> lsGroup  = new ArrayList<TCommonRecord>();
		List<TCommonRecord> lsOrder  = new ArrayList<TCommonRecord>();
		return fetchDrugDispenseRec2CR(strFields, lsWheres, lsGroup, lsOrder, srcQuery);
	}
	
	public List<TCommonRecord>  fetchDrugDispenseRec2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " pharmacy.drug_dispense_rec", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
		@SuppressWarnings("unchecked")
		List<TCommonRecord> DrugDispenseRec = srcQuery.query(sql, new CommonMapper());
		for (TCommonRecord cr: DrugDispenseRec)
		{
			TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, cr.get("ORDERED_BY"));
	       	cr.set("DEPT_NAME",           crDept.get("DEPT_NAME"));
	       	cr.set("Dept_Code",           cr.get("ORDERED_BY"));
	    	cr.set("CLINIC_ATTR",         crDept.get("CLINIC_ATTR"));
	    	cr.set("OUTP_OR_INP",         crDept.get("OUTP_OR_INP"));
	    	cr.set("INTERNAL_OR_SERGERY", crDept.get("INTERNAL_OR_SERGERY"));
	    }
		return DrugDispenseRec;
	}
	@SuppressWarnings("unchecked")
	public List<TCommonRecord>  fetchPatsInHospital2CR(String PatientID, String VisitID,  JDBCQueryImpl srcQuery) throws Exception
	{
		//住院记录
		String sql = "Select * from inpadm.Pats_In_Hospital  where Patient_ID = '" + PatientID + "' and visit_id = " + VisitID;
    	List<TCommonRecord> PatsInHospital = srcQuery.query(sql, new CommonMapper());
    	//crPatVisit.setObj(Key_PatsInHospital, PatsInHospital);
    	for (TCommonRecord cr: PatsInHospital)
    	{
    		TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, cr.get("DEPT_CODE"));
    		cr.set("DEPT_NAME",           crDept.get("DEPT_NAME"));
    		cr.set("CLINIC_ATTR",         crDept.get("CLINIC_ATTR"));
    		cr.set("OUTP_OR_INP",         crDept.get("OUTP_OR_INP"));
    		cr.set("INTERNAL_OR_SERGERY", crDept.get("INTERNAL_OR_SERGERY"));
    	}
		return PatsInHospital;
	}
	
	public List<TCommonRecord>  fetchOrders2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		String strFields = "*";
		
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
		return fetchOrders2CR(strFields, lsWheres, blank, blank, srcQuery);
		/*
    	String sql = "Select * from ordadm.Orders  where Patient_ID = '" + PatientID + "' and visit_id = " + VisitID;
    	List<TCommonRecord> orders = srcQuery.query(sql, new CommonMapper());
    	for (TCommonRecord cr: orders)
    	{
    		TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, cr.get("ORDERING_DEPT"));
    		cr.set("DEPT_NAME",           crDept.get("DEPT_NAME"));
    		cr.set("CLINIC_ATTR",         crDept.get("CLINIC_ATTR"));
    		cr.set("OUTP_OR_INP",         crDept.get("OUTP_OR_INP"));
    		cr.set("INTERNAL_OR_SERGERY", crDept.get("INTERNAL_OR_SERGERY"));
    	}
    	return orders;
    	*/
	}
	
	public List<TCommonRecord> fetchOrders2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " ordadm.orders ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
    	@SuppressWarnings("unchecked")
		List<TCommonRecord> orders = srcQuery.query(sql, new CommonMapper());
    	for (TCommonRecord cr: orders)
    	{
    		TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, cr.get("ORDERING_DEPT"));
    		cr.set("DEPT_NAME",           crDept.get("DEPT_NAME"));
    		cr.set("CLINIC_ATTR",         crDept.get("CLINIC_ATTR"));
    		cr.set("OUTP_OR_INP",         crDept.get("OUTP_OR_INP"));
    		cr.set("INTERNAL_OR_SERGERY", crDept.get("INTERNAL_OR_SERGERY"));
    	}
    	return orders;
	}
	
	public List<TCommonRecord>  fetchDiagnosis2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
    	//诊断记录
		//String sql = "Select * from medrec.Diagnosis  where Patient_ID = '" + PatientID + "' and visit_id = " + VisitID;

		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
		return fetchDiagnosis2CR(strFields, lsWheres, blank, blank, srcQuery);
	}
	
	public List<TCommonRecord>  fetchDiagnosis2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " medrec.Diagnosis ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
    	@SuppressWarnings("unchecked")
    	List<TCommonRecord> diagnosis = srcQuery.query(sql, new CommonMapper());
    	return diagnosis;
	}
	
	public List<TCommonRecord>  fetchInpBillDetail2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
    	//费用明细
    	//String sql = "Select * from inpbill.Inp_Bill_Detail  where Patient_ID = '" + PatientID + "' and visit_id = " + VisitID;
    	String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
    	return fetchInpBillDetail2CR(strFields, lsWheres, blank, blank, srcQuery);
	}
	
	public List<TCommonRecord>  fetchInpBillDetail2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " inpbill.Inp_Bill_Detail ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
    	@SuppressWarnings("unchecked")
		List<TCommonRecord> InpBillDetail = srcQuery.query(sql, new CommonMapper());
    	return InpBillDetail;
	}
	
	@SuppressWarnings("unchecked")
	public List<TCommonRecord>  fetchLabTestMaster2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
    	//检查记录 
    	//String sql = "Select * from lab.Lab_Test_Master  where Patient_ID = '" + PatientID + "' and visit_id = " + VisitID;
		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
    	List<TCommonRecord> LabTestMaster = fetchLabTestMaster2CR(strFields, lsWheres, blank, blank, srcQuery);
    	for (TCommonRecord cr: LabTestMaster)
    	{
    		TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, cr.get("ORDERING_DEPT"));
        	TCommonRecord pmDept = DictCache.getNewInstance().getDeptInfo(srcQuery, cr.get("PERFORMED_BY"));
			cr.set("ORDER_DEPT_NAME",             crDept.get("DEPT_NAME"));
			cr.set("ORDER_CLINIC_ATTR",           crDept.get("CLINIC_ATTR"));
			cr.set("ORDER_OUTP_OR_INP",           crDept.get("OUTP_OR_INP"));
			cr.set("ORDER_INTERNAL_OR_SERGERY",   crDept.get("INTERNAL_OR_SERGERY"));
			cr.set("PERFORM_DEPT_NAME",           pmDept.get("DEPT_NAME"));
			cr.set("PERFORM_CLINIC_ATTR",         pmDept.get("CLINIC_ATTR"));
			cr.set("PERFORM_OUTP_OR_INP",         pmDept.get("OUTP_OR_INP"));
			cr.set("PERFORM_INTERNAL_OR_SERGERY", pmDept.get("INTERNAL_OR_SERGERY"));

			lsWheres.clear();
			crWheres = CaseHistoryHelperUtils.genWhereCR("TEST_NO", cr.get("TEST_NO"), "Char", "", "", "");
			lsWheres.add(crWheres);
	    	//String srcSQL = "Select * from " + Config.getParamValue("Lab_Test_Items") + "Lab_Test_Items where TEST_NO=?";
	    	List<TCommonRecord> ltis = fetchLabTestItems2CR(strFields, lsWheres, blank, blank, srcQuery);
	    	cr.setObj(Key_LabTestItems, ltis);
		    //srcSQL = "Select * from lab.Lab_Result where TEST_NO=?";
	    	List<TCommonRecord> lrs = fetchLabResult2CR(strFields, lsWheres, blank, blank, srcQuery);
	    	cr.setObj(Key_LabResult, lrs);
		}
    	return LabTestMaster;
	}
	
	public List<TCommonRecord>  fetchLabResult2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " lab.Lab_Result ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
    	@SuppressWarnings("unchecked")
	    List<TCommonRecord> lrs = srcQuery.query(sql, new CommonMapper());
    	return lrs;
	}
	
	public List<TCommonRecord>  fetchLabTestItems2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " lab.Lab_Test_Items ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
    	@SuppressWarnings("unchecked")
	    List<TCommonRecord> ltis = srcQuery.query(sql, new CommonMapper());
    	return ltis;
	}
	
	public List<TCommonRecord>  fetchLabTestMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " lab.Lab_Test_Master ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
    	@SuppressWarnings("unchecked")
    	List<TCommonRecord> LabTestMaster = srcQuery.query(sql, new CommonMapper());
    	return LabTestMaster;
	}
	
	public List<TCommonRecord>  fetchVitalSignsRec2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
    	//体征信息
    	//String sql = "Select * from ordadm.Vital_Signs_Rec where Patient_ID=? and VISIT_ID=?";
		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
    	//List<TCommonRecord> VitalSignsRec = srcQuery.query(sql, new Object[]{PatientID, VisitID}, new CommonMapper());
    	return fetchVitalSignsRec2CR( strFields, lsWheres, blank, blank, srcQuery);
	}
	
	public List<TCommonRecord>  fetchVitalSignsRec2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " ordadm.Vital_Signs_Rec ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
    	@SuppressWarnings("unchecked")
    	List<TCommonRecord> VitalSignsRec = srcQuery.query(sql, new CommonMapper());
    	return VitalSignsRec;
	}
	
	public List<TCommonRecord>  fetchExamMaster2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
    	//检验记录
		//String sql = "Select * from exam.Exam_Master where Patient_ID=? and VISIT_ID=?";
		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
    	List<TCommonRecord> ExamMaster = fetchExamMaster2CR(strFields, lsWheres, blank, blank, srcQuery);
    	for (TCommonRecord cr: ExamMaster)
    	{
    		TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, cr.get("PERFORMED_BY"));
        	TCommonRecord rqDept = DictCache.getNewInstance().getDeptInfo(srcQuery, cr.get("REQ_DEPT"));
			cr.set("PERFORM_DEPT_NAME",           crDept.get("DEPT_NAME"));
			cr.set("PERFORM_CLINIC_ATTR",         crDept.get("CLINIC_ATTR"));
			cr.set("PERFORM_OUTP_OR_INP",         crDept.get("OUTP_OR_INP"));
			cr.set("PERFORM_INTERNAL_OR_SERGERY", crDept.get("INTERNAL_OR_SERGERY"));
			cr.set("ORDER_DEPT_NAME",             rqDept.get("DEPT_NAME"));
			cr.set("ORDER_CLINIC_ATTR",           rqDept.get("CLINIC_ATTR"));
			cr.set("ORDER_OUTP_OR_INP",           rqDept.get("OUTP_OR_INP"));
			cr.set("ORDER_INTERNAL_OR_SERGERY",   rqDept.get("INTERNAL_OR_SERGERY"));
	    	
			lsWheres.clear();
			crWheres = CaseHistoryHelperUtils.genWhereCR("EXAM_NO", cr.get("EXAM_NO"), "Char", "", "", "");
			lsWheres.add(crWheres);
			// String srcSQL = "Select * from exam.Exam_Items where EXAM_NO=?";
	    	List<TCommonRecord> ExamItems = fetchExamItems2CR(strFields, lsWheres, blank, blank, srcQuery); //srcQuery.query(srcSQL, new Object[]{cr.get("EXAM_NO")}, new CommonMapper());
	    	cr.setObj(Key_ExamItems, ExamItems);
		    	
	    	//srcSQL = "Select * from exam.Exam_Report where EXAM_NO=?";
	    	List<TCommonRecord> ExamReport = fetchExamReport2CR(strFields, lsWheres, blank, blank, srcQuery); //srcQuery.query(srcSQL, new Object[]{cr.get("EXAM_NO")}, new CommonMapper());
	    	cr.setObj(Key_ExamReport, ExamReport);
		}
    	return ExamMaster;
	}
	
	public List<TCommonRecord>  fetchExamMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " exam.Exam_Master ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
    	@SuppressWarnings("unchecked")
    	List<TCommonRecord> ExamMaster = srcQuery.query(sql, new CommonMapper());
		return ExamMaster;
	}
	public List<TCommonRecord>  fetchExamReport2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " exam.Exam_Report ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
    	@SuppressWarnings("unchecked")
    	List<TCommonRecord> ExamReport = srcQuery.query(sql, new CommonMapper());
		return ExamReport;
	}
	public List<TCommonRecord>  fetchExamItems2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " exam.Exam_Items ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
    	@SuppressWarnings("unchecked")
    	List<TCommonRecord> ExamItems = srcQuery.query(sql, new CommonMapper());
		return ExamItems;
	}
	
	public List<TCommonRecord>  fetchGermTest2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
    	//微生物检验
    	//String srcSQL = "Select * from infect.germ_test where Patient_ID=? and VISIT_ID=?";
		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
    	List<TCommonRecord> GermTest = fetchGermTest2CR( strFields, lsWheres, blank, blank, srcQuery);
    	for (TCommonRecord cr: GermTest)
    	{
    		TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, cr.get("ORDERING_DEPT"));
			cr.set("DEPT_NAME",           crDept.get("DEPT_NAME"));
			cr.set("CLINIC_ATTR",         crDept.get("CLINIC_ATTR"));
			cr.set("OUTP_OR_INP",         crDept.get("OUTP_OR_INP"));
			cr.set("INTERNAL_OR_SERGERY", crDept.get("INTERNAL_OR_SERGERY"));
			
	    	// srcSQL = "Select * from infect.DRUG_SENSIT_RESULT where TEST_NO=?";
			lsWheres.clear();
			crWheres = CaseHistoryHelperUtils.genWhereCR("TEST_NO", cr.get("TEST_NO"), "Char", "", "", "");
			lsWheres.add(crWheres);
	    	List<TCommonRecord> DrugSensitResult = fetchDrugSensitResult2CR( strFields, lsWheres, blank, blank, srcQuery);
	    	cr.setObj(Key_DrugSensitResult, DrugSensitResult);
		}
    	return GermTest;
	}
	
	public List<TCommonRecord>  fetchGermTest2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		/*String sql = this.genSQL(strFields, " infect.GERM_TEST ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
    	@SuppressWarnings("unchecked")*/
    	List<TCommonRecord> GermTest = new ArrayList<TCommonRecord>();// srcQuery.query(sql, new CommonMapper());
    	return GermTest;
	}
	
	public List<TCommonRecord>  fetchDrugSensitResult2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " infect.DRUG_SENSIT_RESULT ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
    	@SuppressWarnings("unchecked")
    	List<TCommonRecord> DrugSensitResult = srcQuery.query(sql, new CommonMapper());
    	return DrugSensitResult;
	}
	
	public List<TCommonRecord>  fetchGermTestResult2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
    	//微生物检验
    	/*
    	String srcSQL = "Select * from infect.GERM_TEST_RESULT where PATIENT_ID=? AND VISIT_ID=?";
    	List<TCommonRecord> GermTestResult = srcQuery.query(srcSQL, new Object[]{PatientID, VisitID}, new CommonMapper());
    	for (TCommonRecord cr: GermTestResult)
    	{
    		TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, cr.get("ORDERING_DEPT"));
			cr.set("DEPT_NAME",           crDept.get("DEPT_NAME"));
			cr.set("CLINIC_ATTR",         crDept.get("CLINIC_ATTR"));
			cr.set("OUTP_OR_INP",         crDept.get("OUTP_OR_INP"));
			cr.set("INTERNAL_OR_SERGERY", crDept.get("INTERNAL_OR_SERGERY"));
    	}
    	*/
		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
    	return fetchOperationMaster2CR(strFields, lsWheres, blank, blank, srcQuery);
	}
	
	public List<TCommonRecord>  fetchGermTestResult2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " infect.GERM_TEST_RESULT ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
    	@SuppressWarnings("unchecked")
    	List<TCommonRecord> GermTestResult = srcQuery.query(sql, new CommonMapper());
    	for (TCommonRecord cr: GermTestResult)
    	{
    		TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, cr.get("ORDERING_DEPT"));
			cr.set("DEPT_NAME",           crDept.get("DEPT_NAME"));
			cr.set("CLINIC_ATTR",         crDept.get("CLINIC_ATTR"));
			cr.set("OUTP_OR_INP",         crDept.get("OUTP_OR_INP"));
			cr.set("INTERNAL_OR_SERGERY", crDept.get("INTERNAL_OR_SERGERY"));
    	}
    	return GermTestResult;
	}
	@SuppressWarnings("unchecked")
	public List<TCommonRecord>  fetchOperationMaster2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		//病人手术信息
		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
		return fetchOperationMaster2CR(strFields, lsWheres, blank, blank, srcQuery);
	}

	public List<TCommonRecord>  fetchOperationMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroup, List<TCommonRecord> lsOrder, JDBCQueryImpl srcQuery) throws Exception
	{
		String sql = this.genSQL(strFields, " surgery.Operation_Master ", lsWheres, lsGroup, lsOrder);
		if (srcQuery == null)
			srcQuery = DBQueryFactory.getQuery("HIS");
    	@SuppressWarnings("unchecked")
		List<TCommonRecord> OperMaster = srcQuery.query(sql, new CommonMapper());
		for (TCommonRecord cr: OperMaster)
    	{
    		TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, cr.get("OPERATING_DEPT"));
        	cr.set("DEPT_NAME",           crDept.get("DEPT_NAME"));
    		cr.set("CLINIC_ATTR",         crDept.get("CLINIC_ATTR"));
    		cr.set("OUTP_OR_INP",         crDept.get("OUTP_OR_INP"));
    		cr.set("INTERNAL_OR_SERGERY", crDept.get("INTERNAL_OR_SERGERY"));
    	}
		return OperMaster;
	}
	
    private String getDateSQL(String src, String fmt)
    {
    	if ((src == null) || (src.length() == 0))
    		src = "1981-01-01 00:00:00";
    		//return "To_Date('1981-01-01 00:00:00', '" + fmt + "')";
    	if (src.length() > 19)
    		src = src.substring(0, 19);
    	if (fmt.length() < 19)
    		src = src.substring(0, fmt.length());
    	return "To_Date('" + src + "', '" + fmt + "')";
    }
    
    private String getDateToCharSQL(String src, String fmt)
    {
    	if ((src == null) || (src.length() == 0))
    		src = "1981-01-01 00:00:00";
    		//return "To_Date('1981-01-01 00:00:00', '" + fmt + "')";
    	if (src.length() > 19)
    		src = src.substring(0, 19);
    	if (fmt.length() < 19)
    		src = src.substring(0, fmt.length());
    	return "To_Char('" + src + "', '" + fmt + "')";
    }
    
	@Override
	public List<TCommonRecord> fetchDrugPrescMaster2CR(String PatientID, String InpDate, String OutDate, JDBCQueryImpl srcQuery) throws Exception 
	{
//		String sql = "select to_char(t.presc_date, 'yyyy-mm-dd') rpt_date, " +
//	       " t.identity, " +
//	       " t.charge_type, " +
//	       " t.ordered_by dept_code, " +
//	       " t.prescribed_by doctor_name,  " +
//	       " d.drug_code,  " +
//	       " d.drug_spec,  " +
//	       " d.drug_name,  " +
//	       " d.firm_id,  " +
//	       " d.package_units drug_units,  " +
//	       " d.quantity amount,  " +
//	       " d.costs,  " +
//	       " d.payments charges " +
//		   " from drug_presc_master t, drug_presc_detail d " +
//		   " where t.presc_source = '1' and t.presc_type = '0' and " +
//		   " to_char(t.presc_date, 'yyyy-mm-dd') = to_char(d.presc_date, 'yyyy-mm-dd') and t.presc_no = d.presc_no ";
		String sql = "select * from outp_presc_master t where t.presc_source = '1' and " +
			" t.visit_date >= " + getDateSQL(InpDate, "yyyy-MM-dd") + " and " +
			" t.visit_date <= " + getDateSQL(OutDate, "yyyy-MM-dd") + " and " +
			" t.patient_id = '" + PatientID + "'";
		@SuppressWarnings("unchecked")
		List<TCommonRecord> lsDrugPrescMaster = srcQuery.query(sql, new CommonMapper());
		for (TCommonRecord crDrugPrescMaster: lsDrugPrescMaster)
		{
			crDrugPrescMaster.set("Dept_Code", crDrugPrescMaster.get("Ordered_By"));
    		TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, crDrugPrescMaster.get("OPERATING_DEPT"));
    		crDrugPrescMaster.set("DEPT_NAME",           crDept.get("DEPT_NAME"));
    		crDrugPrescMaster.set("CLINIC_ATTR",         crDept.get("CLINIC_ATTR"));
    		crDrugPrescMaster.set("OUTP_OR_INP",         crDept.get("OUTP_OR_INP"));
    		crDrugPrescMaster.set("INTERNAL_OR_SERGERY", crDept.get("INTERNAL_OR_SERGERY"));

    		String dt = crDrugPrescMaster.get("visit_Date");
        	if (dt.length() > 10)
        		dt = dt.substring(0, 10);
			
			sql = "select * from outp_Presc_Detail t where " +
				" t.presc_no = '" + crDrugPrescMaster.get("Presc_No") + "'";
			@SuppressWarnings("unchecked")
			List<TCommonRecord> lsDrugPrescDetail = srcQuery.query(sql, new CommonMapper());
			crDrugPrescMaster.setObj(ICaseHistoryHelper.Key_DrugPrescDetail, lsDrugPrescDetail);
		}
		return lsDrugPrescMaster;
	}
}