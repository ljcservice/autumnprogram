package com.hitzd.his.casehistory.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.WebPage.PageView;
import com.hitzd.his.Beans.Middle.TTableConfig;
import com.hitzd.his.casehistory.CaseHistory;

public class CaseHistoryHelperBase implements ICaseHistoryHelper
{

	@Override
	public CaseHistory fetchCaseHistory(String PatientID, String VisitID, JDBCQueryImpl srcQuery) 
	{
		return null;
	}

	@Override
	public TCommonRecord fetchCaseHistory2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) 
	{
		return null;
	}

	@Override
	public TCommonRecord fetchPatInfo2CR(String PatientID, JDBCQueryImpl srcQuery) throws Exception 
	{
		return null;
	}

	@Override
	public List<TCommonRecord> fetchPatVisit2CR(String PatientID, JDBCQueryImpl srcQuery) throws Exception 
	{
		return null;
	}

	@Override
	public List<TCommonRecord> fetchOperationMaster2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception 
	{
		return null;
	}

	@Override
	public List<TCommonRecord> fetchOperation2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception 
	{
		return null;
	}

	@Override
	public List<TCommonRecord> fetchOperationName2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception 
	{
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDrugDispenseRec2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception 
	{
		return null;
	}

	@Override
	public List<TCommonRecord> fetchPatsInHospital2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception 
	{
		return null;
	}

	@Override
	public List<TCommonRecord> fetchOrders2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception 
	{
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDiagnosis2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception 
	{
		return null;
	}

	@Override
	public List<TCommonRecord> fetchInpBillDetail2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception 
	{
		return null;
	}

	@Override
	public List<TCommonRecord> fetchLabTestMaster2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception 
	{
		return null;
	}

	@Override
	public List<TCommonRecord> fetchVitalSignsRec2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception 
	{
		return null;
	}

	@Override
	public List<TCommonRecord> fetchExamMaster2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception 
	{
		return null;
	}

	@Override
	public List<TCommonRecord> fetchGermTest2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception 
	{
		return null;
	}

	@Override
	public List<TCommonRecord> fetchGermTestResult2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception 
	{
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDrugPrescMaster2CR(String PatientID, String InpDate, String OutDate, JDBCQueryImpl srcQuery) throws Exception {
		return null;
	}

	public static void main(String[] args)
	{
		CaseHistoryHelperBase cb = new CaseHistoryHelperBase();
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres1 = new TCommonRecord();
		crWheres1.set("FieldName", "Name");
		crWheres1.set("FieldType", "Char");
		crWheres1.set("FieldValue", "刘%");
		lsWheres.add(crWheres1);
		
		crWheres1 = new TCommonRecord();
		crWheres1.set("FieldName", "Admission_Date");
		crWheres1.set("FieldValue", "to_date('2011-01-01', 'yyyy-mm-dd')");
		crWheres1.set("Relation", "<=");
		crWheres1.set("GroupNo", "1");
		crWheres1.set("Condition", "and");
		lsWheres.add(crWheres1);
		
		crWheres1 = new TCommonRecord();
		crWheres1.set("FieldName", "Discharge_Date");
		crWheres1.set("FieldValue", "to_date('2011-01-01', 'yyyy-mm-dd')");
		crWheres1.set("Relation", ">=");
		crWheres1.set("GroupNo", "1");
		crWheres1.set("Condition", "and");
		lsWheres.add(crWheres1);
		
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>();
		
		String sql = cb.genSQL("*", " Pat_Visit ", lsWheres, blank, blank);
		System.out.println(sql);
	}
	
	public String getFieldValue(String FieldValue, String FieldType)
	{
		if (FieldType.equalsIgnoreCase("Char"))
			return "'" + FieldValue.replaceAll("''", "'") + "'";
		else
			return FieldValue.replaceAll("''", "'");
	}
	
	/**
	 * 生成sql
	 * strFields: 字段列表，可以是"*"
	 * strTables: 表名
	 * lsWheres : Where条件列表
	 * strGroup : Group by 列表
	 * lsOrders  : Order by 列表
	 */
	@Override
	public String genSQL(String strFields, String strTables, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders) 
	{
		String sql = "select " + strFields + " from " + strTables ;
		String wheres = " where 1=1 ";
		Map<String, List<TCommonRecord>> mapWheres = new HashMap<String, List<TCommonRecord>>();
		for (TCommonRecord cr : lsWheres)
		{
			List<TCommonRecord> lsWhereGroup = mapWheres.get(cr.get("GroupNo"));
			if (lsWhereGroup == null)
			{
				lsWhereGroup = new ArrayList<TCommonRecord>();
				mapWheres.put(cr.get("GroupNo"), lsWhereGroup);
			}
			lsWhereGroup.add(cr);
		}
		String where = "";
		for (Map.Entry<String, List<TCommonRecord>> m : mapWheres.entrySet()) 
		{
			List<TCommonRecord> lsWhereGroup = m.getValue();
			int i = 0;
			// for (i = 0; i < lsWhereGroup.size(); i++);
			for (TCommonRecord cr : lsWhereGroup)
			{
				//TCommonRecord cr = lsWhereGroup.get(i);
				//FieldName, GroupNo, FieldValue, Relation, Condition
				if (cr.get("Condition").length() == 0)
					where += " and ";
				else
					where += " " + cr.get("Condition") + " ";
				if ((lsWhereGroup.size() > 1) && (i == 0))
					where += " ( ";
				String FieldValue = cr.get("FieldValue");
				if ((FieldValue.length() > 0) &&  ((FieldValue.charAt(0) == '%') || (FieldValue.charAt(FieldValue.length() - 1) == '%')))
					where += cr.get("FieldName") + " like '" + cr.get("FieldValue") + "'";
				else
				if (cr.get("Relation").length() == 0)
					where += cr.get("FieldName") + " = " + getFieldValue(FieldValue, cr.get("FieldType"));
				else
					where += cr.get("FieldName") + " " + cr.get("Relation") + " " + getFieldValue(FieldValue, cr.get("FieldType"));
				i++;
			}
			if (lsWhereGroup.size() > 1)
				where += " ) ";
			
		}
		if (where.length() > 0)
			wheres += where;
		
		String Group = "";
		for (int x = 0; x < lsGroups.size(); x ++) //TCommonRecord crGroup : lsGroups)
		{
			TCommonRecord crGroup = lsGroups.get(x);
			if (x > 0)
				Group += ", ";
			Group += crGroup.get("FieldName");
		}
		if (Group.length() > 0)
			Group = " group by " + Group;
		String Orders = "";
		for (int x = 0; x < lsOrders.size(); x++) //TCommonRecord crOrder : lsOrders)
		{
			TCommonRecord crOrder = lsOrders.get(x);
			if (x > 0)
				Orders += ", ";
			Orders += crOrder.get("FieldName");
			if (crOrder.get("By").length() > 0)
				Orders += " " + crOrder.get("By");
		}
		Orders = (Orders.length() > 0) ? " order by " + Orders : "";
		sql += wheres + Group + Orders;
		return sql;
	}

	@Override
	public TCommonRecord fetchPatInfo2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchPatVisit2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchPatVisitInfo(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchOperationMaster2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchOperation2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchOperationName2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDrugDispenseRec2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchPatsInHospital2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchOrders2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDiagnosis2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchInpBillDetail2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchLabTestMaster2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchVitalSignsRec2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchExamMaster2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchGermTest2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchGermTestResult2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDrugPrescMaster2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> queryOrders(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TCommonRecord fetchPatVisit2CR(String PatientID, String VisitID,
			JDBCQueryImpl srcQuery) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchLabResult2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchLabTestItems2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchExamReport2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchExamItems2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDrugSensitResult2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDrugDict2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchMrIndex2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchMrFileIndex2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageView<TCommonRecord> fetchTable2PV(int maxresult,
			int currentpage, String strFields, String strTables,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String genSQLComm(String strFields, String strTables,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders)
	{
		String sql = "select " + strFields + " from " + strTables ;
		String wheres = " where 1=1 ";
		Map<String, List<TCommonRecord>> mapWheres = new HashMap<String, List<TCommonRecord>>();
		for (TCommonRecord cr : lsWheres)
		{
			List<TCommonRecord> lsWhereGroup = mapWheres.get(cr.get("GroupNo"));
			if (lsWhereGroup == null)
			{
				lsWhereGroup = new ArrayList<TCommonRecord>();
				mapWheres.put(cr.get("GroupNo"), lsWhereGroup);
			}
			lsWhereGroup.add(cr);
		}
		String where = "";
		for (Map.Entry<String, List<TCommonRecord>> m : mapWheres.entrySet()) 
		{
			List<TCommonRecord> lsWhereGroup = m.getValue();
			int i = 0;
			// for (i = 0; i < lsWhereGroup.size(); i++);
			for (TCommonRecord cr : lsWhereGroup)
			{
				//TCommonRecord cr = lsWhereGroup.get(i);
				//FieldName, GroupNo, FieldValue, Relation, Condition
				if (cr.get("Condition").length() == 0)
					where += " and ";
				else
					where += " " + cr.get("Condition") + " ";
				if ((lsWhereGroup.size() > 1) && (i == 0))
					where += " ( ";
				String FieldValue = cr.get("FieldValue");
				if ((FieldValue.length() > 0) &&  ((FieldValue.charAt(0) == '%') || (FieldValue.charAt(FieldValue.length() - 1) == '%')))
					where += cr.get("FieldName") + " like '" + cr.get("FieldValue") + "'";
				else
				if (cr.get("Relation").length() == 0)
					where += cr.get("FieldName") + " = " + getFieldValue(FieldValue, cr.get("FieldType"));
				else
					where += cr.get("FieldName") + " " + cr.get("Relation") + " " + getFieldValue(FieldValue, cr.get("FieldType"));
				i++;
			}
			if (lsWhereGroup.size() > 1)
				where += " ) ";
			
		}
		if (where.length() > 0)
			wheres += where;
		
		String Group = "";
		for (int x = 0; x < lsGroups.size(); x ++) //TCommonRecord crGroup : lsGroups)
		{
			TCommonRecord crGroup = lsGroups.get(x);
			if (x > 0)
				Group += ", ";
			Group += crGroup.get("FieldName");
		}
		if (Group.length() > 0)
			Group = " group by " + Group;
		String Orders = "";
		for (int x = 0; x < lsOrders.size(); x++) //TCommonRecord crOrder : lsOrders)
		{
			TCommonRecord crOrder = lsOrders.get(x);
			if (x > 0)
				Orders += ", ";
			Orders += crOrder.get("FieldName");
			if (crOrder.get("By").length() > 0)
				Orders += " " + crOrder.get("By");
		}
		Orders = (Orders.length() > 0) ? " order by " + Orders : "";
		sql += wheres + Group + Orders;
		return sql;
	}

	@Override
	public List<TCommonRecord> fetchPerformFreqDict2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchAdministrationDict2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDiagnosisDict2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDrugClassDict2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchGermdrugsensitDict2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchGermCodeDict2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchSpecimanDict2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchStaffDict2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDeptDict2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchChargeTypeDict2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchIdentityDict2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDrugFormDict2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchPatientStatusChgDict2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDrugSupplierCatalog2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDrugToxiPropertyDict2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDrugExportMaster2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDrugExportDetail2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDrugImportMaster2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDrugImportDetail2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchClinicMaster2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchOutpOrders2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchOutpPresc2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchOutpMr2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchClinicDiagnosis2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchWaitBedPats2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchAdtLog2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchPreDischgedPats2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> fetchDrugPrescDetail2CR(String strFields,
			List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
			List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TCommonRecord> queryForListByPS(String sql, JDBCQueryImpl srcQuery) throws Exception
	{
		return null;
	}
	
	public TCommonRecord queryForObjectByPS(String sql, JDBCQueryImpl srcQuery) throws Exception
	{
		return null;
	}

	@Override
	public String getDbUrl(String originalTable)
	{
		TTableConfig table = tables.get(originalTable.trim().toUpperCase());
		if (table == null)
			table = new TTableConfig();
		return table.getDbUrl();
	}

	@Override
	public List<TCommonRecord> fetchPatInfo2List(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		return null;
	}

    @Override
    public List<TCommonRecord> fetchTransfer2CR(String PatientID,
            String VisitID, JDBCQueryImpl srcQuery) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TCommonRecord> fetchTransfer2CR(String strFields,
            List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
            List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
            throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }
}