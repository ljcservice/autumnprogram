package com.hitzd.his.casehistory.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.WebPage.PageView;
import com.hitzd.WebPage.QueryResult;
import com.hitzd.his.Beans.Middle.TFieldConfig;
import com.hitzd.his.Beans.Middle.TQueryConfig;
import com.hitzd.his.Beans.Middle.TTableConfig;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.his.casehistory.CaseHistory;

public class CaseHistoryCommon extends CaseHistoryHelperBase
{
	/**
	 * 获取指定病案号和住院号的病人的病历，暂时返回空值 
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 */ 
	@Override
	public CaseHistory fetchCaseHistory(String PatientID, String VisitID, JDBCQueryImpl srcQuery)
	{
		return super.fetchCaseHistory(PatientID, VisitID, srcQuery);
	}

	/**
	 * 获得指定病案号和住院号的病人的病历，返回TCommonRecord结构，下面以crPatInfo代替该对象，该结构包含病人基本信息，同时包含病人的就诊信息
	 * 病人的就诊信息通过返回结果的crPatInfo.getObj(ICaseHistoryHelper.Key_PatVisit)来获得，就诊信息返回为一个TCommonRecord对象，下面以crVisit代替
	 * 病人的就诊信息中包含病人的医嘱、诊断、手术、摆药、检查、检验、微生物、体征、账单等信息;
	 * 获得病人的医嘱，通过就诊信息的crVisit.getObj(ICaseHistoryHelper.Key_Orders)来获得；
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 */
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
//					OperMaster = fetchOperationMaster2CR(PatientID, VisitID, srcQuery);
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
//					OperationName = fetchOperationName2CR(PatientID, VisitID, srcQuery);
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
//					DrugDispenseRec = fetchDrugDispenseRec2CR(PatientID, VisitID, srcQuery);
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
			}
			if (ErrorInfo.length() > 0)
				crPatMasterIndex.setObj("ErrorInfo", ErrorInfo);
		}
		return crPatMasterIndex;
	}

	/**
	 * 获得病人的基本信息，通过PatientID取得用户基本信息
	 * 单表查询：Pat_Master_Index
	 * @param PatientID 病人标识
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	@Override
	public TCommonRecord fetchPatInfo2CR(String PatientID, JDBCQueryImpl srcQuery) throws Exception
	{
		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank  = new ArrayList<TCommonRecord>();
		return fetchPatInfo2List(strFields, lsWheres, blank, blank, srcQuery).get(0);
	}

	/**
	 * 获得病人的基本信息
	 * 单表查询：Pat_Master_Index
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 * 
	 * 举个例子：
	 * 查询病人的全部信息，条件为2011年1月1日在院的病人，并且是姓刘的病人，并且按病人的住院日期进行升序排列
	 * String strFields = "*";
	 * List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
	 * TCommonRecord crWheres1 = new TCommonRecord();
	 * crWheres1.set("FieldName", "Name");
	 * crWheres1.set("FieldValue", "刘%");
	 * lsWheres.add(crWheres1);
	 * 
	 * crWheres1 = new TCommonRecord();
	 * crWheres1.set("FieldName", "Admission_Date");
	 * crWheres1.set("FieldValue", "to_date('2011-01-01', 'yyyy-mm-dd')");
	 * crWheres1.set("Relation", "<=");
	 * crWheres1.set("GroupNo", "1");
	 * crWheres1.set("Condition", "and");
	 * lsWheres.add(crWheres1);
	 * 
	 * crWheres1 = new TCommonRecord();
	 * crWheres1.set("FieldName", "Discharge_Date");
	 * crWheres1.set("FieldValue", "to_date('2011-01-01', 'yyyy-mm-dd')");
	 * crWheres1.set("Relation", ">=");
	 * crWheres1.set("GroupNo", "1");
	 * crWheres1.set("Condition", "and");
	 * lsWheres.add(crWheres1);
	 * 
	 * List<TCommonRecord> lsPatVisitInfo = fetchPatVisitInfo(strFields, lsWheres, "", "Patient_ID");
	 * 以上条件拼出的sql为
	 * select * from Pat_Visit where 1=1 and Name like '刘%' and 
	 *   ((Admission_Date <= to_date('2011-01-01', 'yyyy-mm-dd')) and (Discharge_Date >= to_date('2011-01-01', 'yyyy-mm-dd')))
	 * order by Patient_ID asc
	 */
	@Override
	public TCommonRecord fetchPatInfo2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("MedRec.Pat_Master_Index", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForObjectByPS(sql, srcQuery);
		TCommonRecord crPatMasterIndex = (TCommonRecord) srcQuery.queryForObject(sql, new CommonMapper());
		return crPatMasterIndex;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchPatInfo2List(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("MedRec.Pat_Master_Index", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
		return srcQuery.query(sql, new CommonMapper());
	}

	/**
	 * 获得病人的就诊信息，包括多次住院的信息
	 * 单表查询：pat_visit
	 * @param PatientID 病人标识
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<TCommonRecord> fetchPatVisit2CR(String PatientID, JDBCQueryImpl srcQuery) throws Exception
	{
		String strFields = "*"; 
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank  = new ArrayList<TCommonRecord>();
		return fetchPatVisit2CR(strFields, lsWheres, blank, blank, srcQuery);
	}

	/**
	 * 获得病人的一次就诊信息
	 * 单表查询：pat_visit
	 * @param PatientID 病人标识
	 * @param VisitID 住院次数
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	@Override
	public TCommonRecord fetchPatVisit2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		String strFields = "*"; 
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank  = new ArrayList<TCommonRecord>();
		return fetchPatVisit2CR(strFields, lsWheres, blank, blank, srcQuery).get(0);
	}

	/**
	 * 获得就诊信息
	 * 单表查询：pat_visit
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchPatVisit2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("MedRec.pat_visit", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
		return srcQuery.query(sql, new CommonMapper());
	}

	@Override
	public List<TCommonRecord> fetchOperation2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		String strFields = "*"; 
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank  = new ArrayList<TCommonRecord>();
		return fetchOperation2CR(strFields, lsWheres, blank, blank, srcQuery);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchOperation2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("medrec.Operation", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
		return srcQuery.query(sql, new CommonMapper());
	}
	
	@Override
    public List<TCommonRecord> fetchTransfer2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
    {
	    String strFields = "*"; 
        List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
        TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
        lsWheres.add(crWheres);
        crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
        lsWheres.add(crWheres);
        List<TCommonRecord> blank  = new ArrayList<TCommonRecord>();
        return fetchTransfer2CR(strFields, lsWheres, blank, blank, srcQuery);
    }

    @Override
    public List<TCommonRecord> fetchTransfer2CR(String strFields,List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,
            List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
            throws Exception
    {
        String tableName = this.getTableName("MEDREC.TRANSFER", lsWheres);
        String dbUrl = getDbUrl(tableName).trim();
        if (dbUrl != null && !"".equals(dbUrl))
            srcQuery = DBQueryFactory.getQuery(dbUrl);
        String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
        if (sql.indexOf("?") >= 0)
            return queryForListByPS(sql, srcQuery);
        return srcQuery.query(sql, new CommonMapper());
    }

	@Override
	public List<TCommonRecord> fetchOperationName2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		String strFields = "*"; 
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank  = new ArrayList<TCommonRecord>();
		return fetchOperationName2CR(strFields, lsWheres, blank, blank, srcQuery);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchOperationName2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("surgery.Operation_Name", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
		return srcQuery.query(sql, new CommonMapper());
	}

	@Override
	public List<TCommonRecord> fetchOperationMaster2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
		return fetchOperationMaster2CR(strFields, lsWheres, blank, blank, srcQuery);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchOperationMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("surgery.Operation_Master", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		List<TCommonRecord> OperMaster = new ArrayList<TCommonRecord>();
		if (sql.indexOf("?") >= 0)
			OperMaster = queryForListByPS(sql, srcQuery);
		else
			OperMaster = srcQuery.query(sql, new CommonMapper());
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

	/**
	 * 获得病人的摆药记录
	 * 单表查询：drug_dispense_rec
	 * @param PatientID 病人标识
	 * @param VisitID 住院次数
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<TCommonRecord> fetchDrugDispenseRec2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		//摆药记录
		String strFields = "*"; 
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank  = new ArrayList<TCommonRecord>();
		return fetchDrugDispenseRec2CR(strFields, lsWheres, blank, blank, srcQuery);
	}

	/**
	 * 获得摆药记录
	 * 单表查询：drug_dispense_rec
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDrugDispenseRec2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("pharmacy.drug_dispense_rec", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		List<TCommonRecord> DrugDispenseRec = new ArrayList<TCommonRecord>();
		if (sql.indexOf("?") >= 0)
			DrugDispenseRec = queryForListByPS(sql, srcQuery);
		else
			DrugDispenseRec = srcQuery.query(sql, new CommonMapper());
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

	/**
	 * 获得病人的在院信息
	 * 单表查询：Pats_In_Hospital
	 * @param PatientID 病人标识
	 * @param VisitID 住院次数
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<TCommonRecord> fetchPatsInHospital2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		String strFields = "*"; 
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
		return fetchPatsInHospital2CR(strFields, lsWheres, blank, blank, srcQuery);
	}

	/**
	 * 获得在院信息
	 * 单表查询：Pats_In_Hospital
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchPatsInHospital2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("inpadm.Pats_In_Hospital", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		List<TCommonRecord> patsInHospital = new ArrayList<TCommonRecord>();
		if (sql.indexOf("?") >= 0)
			patsInHospital = queryForListByPS(sql, srcQuery);
		else
			patsInHospital = srcQuery.query(sql, new CommonMapper());
		for (TCommonRecord cr: patsInHospital)
		{
    		TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, cr.get("DEPT_CODE"));
    		cr.set("DEPT_NAME",           crDept.get("DEPT_NAME"));
    		cr.set("CLINIC_ATTR",         crDept.get("CLINIC_ATTR"));
    		cr.set("OUTP_OR_INP",         crDept.get("OUTP_OR_INP"));
    		cr.set("INTERNAL_OR_SERGERY", crDept.get("INTERNAL_OR_SERGERY"));
	    }
		return patsInHospital;
	}

	/**
	 * 获得病人的医嘱记录
	 * 单表查询：orders
	 * @param PatientID 病人标识
	 * @param VisitID 住院次数
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<TCommonRecord> fetchOrders2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
		return fetchOrders2CR(strFields, lsWheres, blank, blank, srcQuery);
	}

	/**
	 * 获得医嘱记录
	 * 单表查询：orders
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchOrders2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("ordadm.orders", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		List<TCommonRecord> orders = new ArrayList<TCommonRecord>();
		if (sql.indexOf("?") >= 0)
			orders = queryForListByPS(sql, srcQuery);
		else
			orders = srcQuery.query(sql, new CommonMapper());
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

	/**
	 * 获得病人的诊断记录
	 * 单表查询：orders
	 * @param PatientID 病人标识
	 * @param VisitID 住院次数
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<TCommonRecord> fetchDiagnosis2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
		return fetchDiagnosis2CR(strFields, lsWheres, blank, blank, srcQuery);
	}

	/**
	 * 获得诊断记录
	 * 单表查询：orders
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDiagnosis2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("medrec.Diagnosis", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	/**
	 * 获得病人的费用明细
	 * 单表查询：Inp_Bill_Detail
	 * @param PatientID 病人标识
	 * @param VisitID 住院次数
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<TCommonRecord> fetchInpBillDetail2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
    	return fetchInpBillDetail2CR(strFields, lsWheres, blank, blank, srcQuery);
	}

	/**
	 * 获得费用明细
	 * 单表查询：Inp_Bill_Detail
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchInpBillDetail2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("inpbill.Inp_Bill_Detail", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	/**
	 * 获得病人的检查信息
	 * 单表查询：Lab_Test_Master
	 * @param PatientID 病人标识
	 * @param VisitID 住院次数
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<TCommonRecord> fetchLabTestMaster2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
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
	    	List<TCommonRecord> ltis = fetchLabTestItems2CR(strFields, lsWheres, blank, blank, srcQuery);
	    	cr.setObj(Key_LabTestItems, ltis);
	    	List<TCommonRecord> lrs = fetchLabResult2CR(strFields, lsWheres, blank, blank, srcQuery);
	    	cr.setObj(Key_LabResult, lrs);
		}
    	return LabTestMaster;
	}

	/**
	 * 获得检查信息
	 * 单表查询：Lab_Test_Master
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchLabTestMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,  List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("lab.Lab_Test_Master", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchLabResult2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("lab.Lab_Result", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchLabTestItems2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("lab.Lab_Test_Items", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	/**
	 * 获得病人的体征信息
	 * 单表查询：Vital_Signs_Rec
	 * @param PatientID 病人标识
	 * @param VisitID 住院次数
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<TCommonRecord> fetchVitalSignsRec2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
    	return fetchVitalSignsRec2CR( strFields, lsWheres, blank, blank, srcQuery);
	}

	/**
	 * 获得体征信息
	 * 单表查询：Vital_Signs_Rec
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchVitalSignsRec2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("ordadm.Vital_Signs_Rec", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	/**
	 * 获得病人的检验信息
	 * 单表查询：Exam_Master
	 * @param PatientID 病人标识
	 * @param VisitID 住院次数
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<TCommonRecord> fetchExamMaster2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
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
	    	List<TCommonRecord> ExamItems = fetchExamItems2CR(strFields, lsWheres, blank, blank, srcQuery); //srcQuery.query(srcSQL, new Object[]{cr.get("EXAM_NO")}, new CommonMapper());
	    	cr.setObj(Key_ExamItems, ExamItems);
		    	
	    	List<TCommonRecord> ExamReport = fetchExamReport2CR(strFields, lsWheres, blank, blank, srcQuery); //srcQuery.query(srcSQL, new Object[]{cr.get("EXAM_NO")}, new CommonMapper());
	    	cr.setObj(Key_ExamReport, ExamReport);
		}
    	return ExamMaster;
	}

	/**
	 * 获得检验信息
	 * 单表查询：Exam_Master
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchExamMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("exam.Exam_Master", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
		return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchExamItems2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("exam.Exam_Items", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
		return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchExamReport2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("exam.Exam_Report", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
		return srcQuery.query(sql, new CommonMapper());
	}

	/**
	 * 获得病人的微生物检验
	 * 单表查询：GERM_TEST
	 * @param PatientID 病人标识
	 * @param VisitID 住院次数
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<TCommonRecord> fetchGermTest2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
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
			lsWheres.clear();
			crWheres = CaseHistoryHelperUtils.genWhereCR("TEST_NO", cr.get("TEST_NO"), "Char", "", "", "");
			lsWheres.add(crWheres);
	    	List<TCommonRecord> DrugSensitResult = fetchDrugSensitResult2CR( strFields, lsWheres, blank, blank, srcQuery);
	    	cr.setObj(Key_DrugSensitResult, DrugSensitResult);
		}
    	return GermTest;
	}

	/**
	 * 获得微生物检验
	 * 单表查询：GERM_TEST
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchGermTest2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("infect.GERM_TEST", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
		return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDrugSensitResult2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("infect.DRUG_SENSIT_RESULT", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	/**
	 * 获得病人的涂片检验信息
	 * 单表查询：GERM_TEST_RESULT
	 * @param PatientID 病人标识
	 * @param VisitID 住院次数
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<TCommonRecord> fetchGermTestResult2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception
	{
		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Visit_ID", VisitID, "", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
    	return fetchGermTestResult2CR(strFields, lsWheres, blank, blank, srcQuery);
	}

	/**
	 * 获得涂片检验信息
	 * 单表查询：GERM_TEST_RESULT
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchGermTestResult2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("infect.GERM_TEST_RESULT", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		List<TCommonRecord> GermTestResult = new ArrayList<TCommonRecord>();
		if (sql.indexOf("?") >= 0)
			GermTestResult = queryForListByPS(sql, srcQuery);
		else
			GermTestResult = srcQuery.query(sql, new CommonMapper());
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

	/**
	 * 获得病人的住院处方信息
	 * 单表查询：outp_presc_master
	 * @param PatientID 病人标识
	 * @param InpDate
	 * @param OutDate
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<TCommonRecord> fetchDrugPrescMaster2CR(String PatientID, String InpDate, String OutDate, JDBCQueryImpl srcQuery) throws Exception
	{
		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("presc_source", "1", "Char", "", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("visit_date", getDateSQL(InpDate, "yyyy-MM-dd"), "", ">=", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("visit_date", getDateSQL(OutDate, "yyyy-MM-dd"), "", "<=", "", "");
		lsWheres.add(crWheres);
		crWheres = CaseHistoryHelperUtils.genWhereCR("Patient_ID", PatientID, "Char", "", "", "");
		lsWheres.add(crWheres);
		List<TCommonRecord> blank = new ArrayList<TCommonRecord>(); 
    	return fetchDrugPrescMaster2CR(strFields, lsWheres, blank, blank, srcQuery);
	}

	/**
	 * 获得住院处方信息
	 * 单表查询：outp_presc_master
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDrugPrescMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)  throws Exception
	{
//		TCommonRecord dateTcr = null;
//		if (lsWheres != null && lsWheres.size() > 0)
//		{
//			for (TCommonRecord lsWhere : lsWheres)
//			{
//				if ("PRESC_DATE_DETAIL".equals(lsWhere.get("FieldName").trim().toUpperCase()) && !"".equals(lsWhere.get("FieldValue")))
//				{
//					dateTcr = new TCommonRecord();
//					dateTcr.set("ADate", lsWhere.get("FieldValue"));
//					lsWheres.remove(lsWhere);
//					break;
//				}
//			}
//		}
		String tableName = this.getTableName("pharmacy.drug_presc_master", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	List<TCommonRecord> lsDrugPrescMaster = srcQuery.query(sql, new CommonMapper());
//		for (TCommonRecord crDrugPrescMaster: lsDrugPrescMaster)
//		{
//			crDrugPrescMaster.set("Dept_Code", crDrugPrescMaster.get("Ordered_By"));
//    		TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(srcQuery, crDrugPrescMaster.get("OPERATING_DEPT"));
//    		crDrugPrescMaster.set("DEPT_NAME",           crDept.get("DEPT_NAME"));
//    		crDrugPrescMaster.set("CLINIC_ATTR",         crDept.get("CLINIC_ATTR"));
//    		crDrugPrescMaster.set("OUTP_OR_INP",         crDept.get("OUTP_OR_INP"));
//    		crDrugPrescMaster.set("INTERNAL_OR_SERGERY", crDept.get("INTERNAL_OR_SERGERY"));
//
//    		String dt = crDrugPrescMaster.get("visit_Date");
//        	if (dt.length() > 10)
//        		dt = dt.substring(0, 10);
//			List<TCommonRecord> wheres = new ArrayList<TCommonRecord>();
//			TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("presc_no", crDrugPrescMaster.get("Presc_No"), "Char", "", "", "");
//			wheres.add(where);
//			if (dateTcr != null)
//			{
//				where = CaseHistoryHelperUtils.genWhereCR("PRESC_DATE", "to_date(''" + dateTcr.get("ADate") + "'', ''yyyy-mm-dd'')", "", ">=", "", "");
//				wheres.add(where);
//				where = CaseHistoryHelperUtils.genWhereCR("PRESC_DATE", "to_date(''" + DateUtils.getDateAdded(1, dateTcr.get("ADate")) + "'', ''yyyy-mm-dd'')", "", "<", "", "");
//				wheres.add(where);
//			}
//			sql = this.genSQL("*", "pharmacy.drug_presc_detail", wheres, null, null);
//			List<TCommonRecord> lsDrugPrescDetail = srcQuery.query(sql, new CommonMapper());
//			crDrugPrescMaster.setObj(ICaseHistoryHelper.Key_DrugPrescDetail, lsDrugPrescDetail);
//		}
		return lsDrugPrescMaster;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDrugPrescDetail2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("pharmacy.drug_presc_detail", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	/**
	 * 获得药品字典信息
	 * 单表查询：drug_dict
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDrugDict2CR(String strFields,  List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("comm.drug_dict", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	/**
	 * 获得病案索引
	 * 单表查询：MR_INDEX
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchMrIndex2CR(String strFields,  List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,  List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)  throws Exception
	{
		String tableName = this.getTableName("MEDREC.MR_INDEX", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	/**
	 * 获得病历文件索引
	 * 单表查询：MR_FILE_INDEX
	 * @param 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchMrFileIndex2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,  List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("medrec.MR_FILE_INDEX", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchPerformFreqDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)  throws Exception
	{
		String tableName = this.getTableName("comm.perform_freq_dict", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
            return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchAdministrationDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("COMM.ADMINISTRATION_DICT", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDiagnosisDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,  List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("comm.diagnosis_dict", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDrugClassDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("comm.Drug_Class_Dict", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchGermdrugsensitDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,  List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("infect.germdrugsensit_dict", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchGermCodeDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("INFECT.GERM_CODE_DICT", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchSpecimanDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups,  List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("COMM.SPECIMAN_DICT", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchStaffDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("comm.STAFF_DICT", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDeptDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("comm.DEPT_DICT", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchChargeTypeDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("comm.charge_type_dict", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchIdentityDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("comm.identity_dict", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDrugFormDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("comm.drug_form_dict", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchPatientStatusChgDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("comm.Patient_status_chg_dict", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDrugSupplierCatalog2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("comm.drug_supplier_catalog", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDrugToxiPropertyDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("comm.Drug_toxi_property_dict", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDrugExportMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("pharmacy.drug_export_master", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDrugExportDetail2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("pharmacy.drug_export_detail", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDrugImportMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("pharmacy.drug_import_master", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchDrugImportDetail2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("pharmacy.drug_import_detail", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchClinicMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)  throws Exception
	{
		String tableName = this.getTableName("outpdoct.clinic_master", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchOutpOrders2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("outpdoct.outp_orders", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchOutpPresc2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("outpdoct.outp_presc", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchOutpMr2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("outpdoct.outp_mr", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchClinicDiagnosis2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("outpdoct.CLINIC_DIAGNOSIS", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchPatVisitInfo(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("medRec.pat_visit", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchWaitBedPats2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("Inpadm.wait_bed_pats", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchAdtLog2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("Inpadm.adt_log", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TCommonRecord> fetchPreDischgedPats2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception
	{
		String tableName = this.getTableName("Inpadm.pre_dischged_pats", lsWheres);
		String dbUrl = getDbUrl(tableName).trim();
		if (dbUrl != null && !"".equals(dbUrl))
			srcQuery = DBQueryFactory.getQuery(dbUrl);
		String sql = this.genSQL(strFields, tableName, lsWheres, lsGroups, lsOrders);
		if (sql.indexOf("?") >= 0)
			return queryForListByPS(sql, srcQuery);
    	return srcQuery.query(sql, new CommonMapper());
	}

	/**
	 * 分页查询
	 * @param maxresult   每页显示数量
	 * @param currentpage 当前页数
	 * @param fields      查询字段，一般是FieldName1, FieldName2,...FieldNameN
	 * @param tableName   查询表
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PageView<TCommonRecord> fetchTable2PV(int maxresult, int currentpage, String strFields, String strTables, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery)
	{
	    /* 获得连接的类型 */
        String dbBase = Config.getDBUrlConfig(srcQuery.getDbName()).getDb_base();
        /* 分页数据集合  */
        QueryResult<TCommonRecord> qr = new QueryResult<TCommonRecord>();
        /* 页码 */
        currentpage = currentpage < 1 ? 1 : currentpage;
        /* 一页显示记录数 */
        maxresult   = maxresult < 1? 12 : maxresult;
        PageView<TCommonRecord>  pageView = new PageView<TCommonRecord>(maxresult,currentpage);
        //获取分页总时，能带order by 和 group by 的条件               ——Modify by Ivy
        String reusltSetSql = "";
        if("SQLServer".equals(dbBase))
        {
           String  reusltSetSql1 = this.genSQL(strFields, strTables , lsWheres, lsGroups, lsOrders);   
            reusltSetSql = reusltSetSql1.replace("select", "select top 100 percent '' tempTotal ,");
        }
        else
        {
            reusltSetSql = this.genSQL(strFields, strTables, lsWheres, lsGroups, lsOrders);
        }
        
        // 2014-04-22 liujc 修改  增加表别名
        String countSql = this.genSQL("count(*) total ", "[ " + reusltSetSql + " ] pvTotal", null, null, null);
       // String countSql = this.genSQL("count(*) total",strTables , lsWheres, lsGroups, lsOrders);
        /* 数据总数 */
        if (countSql.indexOf("?") >= 0)
        {
	        try
	        {
	            qr.setTotalrecord(((TCommonRecord)queryForObjectByPS(countSql, srcQuery)).getInt("total"));
	        }
	        catch(Exception e )
	        {
	            e.printStackTrace();
	        }
        }
        else
        {
        	qr.setTotalrecord(((TCommonRecord)srcQuery.queryForObject(countSql,new CommonMapper())).getInt("total"));
        }
        /* 当总页数小于当前页  当前页设置为 第一页 */
        if(((qr.getTotalrecord() / maxresult) + ((qr.getTotalrecord() % maxresult) > 0 ? 1 : 0 )) < currentpage)
        {
            pageView.setCurrentpage(1);
        }
        Integer firstindex = (pageView.getCurrentpage()-1) * pageView.getMaxresult();
        Integer lastindex  = (pageView.getCurrentpage()) * pageView.getMaxresult();
        /*  分页数据  */        
        String pageSql = ""; 
        if("SQLServer".equals(dbBase))
        {
            /* sqlserver 分页   */
            pageSql = getSqlServerPage(strFields, strTables, lsWheres, lsGroups, lsOrders, firstindex, lastindex);
        }
        else
        {
            /* oracle  分页   */
            pageSql = getOraclePage(strFields, strTables, lsWheres, lsGroups, lsOrders, firstindex, lastindex);
        }
        // 处理信息占位符情况
        if (pageSql.indexOf("?") >= 0)
        {
	        try
	        {
	            qr.setResultlist(queryForListByPS(pageSql, srcQuery)); ;    
	        }
	        catch(Exception e )
	        {
	            e.printStackTrace();
	        }
        }
        else
        {
        	qr.setResultlist(srcQuery.query(pageSql,new CommonMapper()));
        }
        pageView.setQueryResult(qr);
        return pageView;
	}

	/**
	 * 返回Sqlserver 分页方法
	 * @param strFields
	 * @param strTables
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param firstindex
	 * @param lastindex
	 * @param tablesMap
	 * @return
	 */
	private String getSqlServerPage(String strFields, String strTables, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, Integer firstindex, Integer lastindex)
    {
	    strTables = initTables(strTables);
	    /* 处理表名 */
	    Map<String, String> tablesMap = getTablesMap(strTables);
	    String Orders = "";
        if (lsOrders == null || lsOrders.size() == 0 )
        {
            String field = strFields.substring(0,strFields.indexOf(","));
            TCommonRecord order = CaseHistoryHelperUtils.genOrderCR(field, "");
            lsOrders.add(order);
        }
        for (int x = 0; x < lsOrders.size(); x++) 
        {
            TCommonRecord crOrder = lsOrders.get(x);
            String fieldName = getField(tablesMap, crOrder.get("FieldName"));
            if(fieldName == null || "".equals(fieldName) || "NULL".equals(fieldName.toUpperCase())) continue;
            if (x > 0)
                Orders += ",";
            Orders += fieldName;
            if (crOrder.get("By").length() > 0)
                Orders += " " + crOrder.get("By");
        }
        Orders = (Orders.length() > 0) ? " order by " + Orders : ""; 
//	    String strF    = "top " + lastindex.toString() + " '' tempLast," +  strFields + ", row_number() over (" + Orders + ") rn " ;
	    String pageSql = this.genSQL(strFields, strTables, lsWheres, lsGroups, lsOrders);
	    pageSql = pageSql.replace("select", "select top " + lastindex.toString() + " '' tempLast,");
	    pageSql = pageSql.replace(" from",  ", row_number() over (" + Orders + ") rn from");
	    String tn = "[" + pageSql + "] b ";
	    lsWheres = new ArrayList<TCommonRecord>();
	    TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("b.rn", firstindex.toString(), "", ">", "", "");
	    lsWheres.add(crWheres);
	    pageSql = this.genSQL("b.*", tn, lsWheres, lsGroups, null);  
	    return pageSql;
    }
	
	/**
	 * Oracle 分页方法 
	 * @param strFields
	 * @param strTables
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param firstindex
	 * @param lastindex
	 * @return
	 */
    private String getOraclePage(String strFields, String strTables, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, Integer firstindex, Integer lastindex)
    {
        /* 分页数据 */
        String pageSql = this.genSQL(strFields, strTables, lsWheres, lsGroups, lsOrders);
        String tn = "[" + pageSql + "] t";
        lsWheres = new ArrayList<TCommonRecord>();
        TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("rownum", lastindex.toString(), "", "<=", "", "");
        lsWheres.add(crWheres);
        lsGroups = new ArrayList<TCommonRecord>();
        lsOrders = new ArrayList<TCommonRecord>();
        pageSql = this.genSQL("t.*, rownum rn", tn, lsWheres, lsGroups, lsOrders);
        tn = "[" + pageSql + "] b";
        lsWheres = new ArrayList<TCommonRecord>();
        crWheres = CaseHistoryHelperUtils.genWhereCR("b.rn", firstindex.toString(), "", ">", "", "");
        lsWheres.add(crWheres);
        lsGroups = new ArrayList<TCommonRecord>();
        lsOrders = new ArrayList<TCommonRecord>();
        pageSql = this.genSQL("b.*", tn, lsWheres, lsGroups, lsOrders);
        return pageSql;
    }

	/**
	 * 生成sql语句
	 * @param 字段列表，允许各式如下：
	 * 1、*，这种情况，必须为单表查询
	 * 2、FieldName1, FieldName2,...FieldNameN；单表查询或count(*)，rownum等
	 * 3、Table1.FieldName1, Table1.FieldName2, Table2.FieldName1, Table2.FieldName2, …，多表联合查询，务必为每个表设置别名
	 * @param 表列表，允许各式如下：
	 * 1、单表查询，这种情况可以不为表设置别名
	 * 2、多个简单表联合查询，务必为每个表设置别名，如：table1 t1, table2 t2, table3 t3
	 * 3、含临时表的单临时表或多表查询，临时表要以[]括起，并取别名，如：Table1 a, Table2 b, Table3 c, [select * from Table4] d
	 *    临时表的查询语句也要用genSQL方法生成。
	 * @param lsWheres 条件列表
	 * 是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=；
	 *             另外也可以设置为左右联关系，生成的语句是FieldName = FieldValue(+)，因此这种情况下要注意FieldName和FieldValue的顺序问题
	 *             例如：
	 *             List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
	 *			   TCommonRecord crWheres = CaseHistoryHelperUtils.genWhereCR("v.patient_id", "h.patient_id", "", "join", "", "");
	 *			   lsWheres.add(crWheres);
	 *             生成sql为v.patient_id=h.patient_id(+)
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param lsGroups group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param lsOrders order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * @param srcQuery
	 * @return 返回符合条件的数据结果
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String genSQL(String strFields, String strTables, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders)
	{
		StringBuffer sql = new StringBuffer();
		List<String> fieldsList = initFields(strFields);
		strTables = initTables(strTables);
		/* 处理表名 */
		Map<String, String> tablesMap = getTablesMap(strTables);
		/* 处理字段 */
		//String fields = getFields(tablesMap, strFields);
		String fields = getFields(tablesMap, fieldsList);
		sql.append("select ").append(fields).append(" from ");
		/* 判断表是否在 Oracle 中  */
		boolean isOracle = false;
		Iterator it = tablesMap.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry entry = (Map.Entry) it.next();
//			TCommonRecord tbCom = getTargetTable4TCom(entry.getValue().toString());
			TTableConfig tbCom = getTargetTable2Obj(entry.getValue().toString());
            String tb = tbCom.getTargetTable();
			if (!"".equals(tbCom.getDbName()) && "Oracle".equals(tbCom.getDbName()))
	        {
			    isOracle = true;
	        }
			if ("".equals(tb) || "NULL".equals(tb.toUpperCase()))
			{
			    System.out.println("该语句查询的原始字段为：" + tbCom.getOriginalTable() + ",医院目标字段为：" +  tb);
				sql.append(entry.getValue().toString()).append(" ").append(entry.getKey().toString()).append(", ");
			}
			else
			{
				sql.append(getTargetTable(entry.getValue().toString())).append(" ").append(entry.getKey().toString()).append(", ");
			}
		}
		sql.deleteCharAt(sql.length() - 2);
		String wheres = " where 1=1 ";
		Map<String, List<TCommonRecord>> mapWheres = new HashMap<String, List<TCommonRecord>>();
		if (lsWheres == null)
			lsWheres = new ArrayList<TCommonRecord>();
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
				String fieldName = getField(tablesMap, cr.get("FieldName"));
				String FieldValue = cr.get("FieldValue");
				if (cr.get("Relation").length() == 0)
				{
					if (FieldValue.indexOf("?") >= 0)
						where += fieldName + "=" + FieldValue;
					else
						where += fieldName + "=" + getField(tablesMap, getFieldValue(FieldValue, cr.get("FieldType")));
				}
				else if (CaseHistoryHelperUtils.LIKE.equalsIgnoreCase(cr.get("Relation")))
				{
					if (FieldValue.indexOf("?") >= 0)
						where += fieldName + " like " + FieldValue;
					else
						where += fieldName + " like '" + FieldValue + "'";
				}
				else if (CaseHistoryHelperUtils.BETWEENAND.equalsIgnoreCase(cr.get("Relation")))
				{
					String[] fieldValues = FieldValue.split("&");
					String firstVal = fieldValues[0];
					String secondVal = fieldValues[1];
					if (FieldValue.indexOf("?") >= 0)
						where += fieldName + " between " + firstVal + " and " + secondVal;
					else
						where += fieldName + " between " + getField(tablesMap, getFieldValue(firstVal, cr.get("FieldType"))) + " and " + getField(tablesMap, getFieldValue(secondVal, cr.get("FieldType")));
				}
				else if (CaseHistoryHelperUtils.IS.equals(cr.get("Relation").toUpperCase()))
				{
				    where += fieldName + " is " + FieldValue;
				}
				else if (cr.get("Relation").indexOf("join") >= 0)
				{
					where += fieldName + "=" + getField(tablesMap, FieldValue);
					if(isOracle)  where += "(+)";
				}
				else
				{
					if (FieldValue.indexOf("?") >= 0)
						where += fieldName + " " + cr.get("Relation") + " " + FieldValue;
					else
						where += fieldName + " " + cr.get("Relation") + " " + getField(tablesMap, getFieldValue(FieldValue, cr.get("FieldType")));
				}
				i++;
			}
			if (lsWhereGroup.size() > 1)
				where += " ) ";
			
		}
		if (where.length() > 0)
			wheres += where;
		
		String Group = "";
		if (lsGroups == null)
			lsGroups = new ArrayList<TCommonRecord>();
		for (int x = 0; x < lsGroups.size(); x ++) //TCommonRecord crGroup : lsGroups)
		{
			TCommonRecord crGroup = lsGroups.get(x);
			if (x > 0)
				Group += ", ";
			String fieldName = getField(tablesMap, crGroup.get("FieldName"));
			Group += fieldName;
		}
		if (Group.length() > 0)
			Group = " group by " + Group;
		String Orders = "";
		if (lsOrders == null)
			lsOrders = new ArrayList<TCommonRecord>();
		for (int x = 0; x < lsOrders.size(); x++) //TCommonRecord crOrder : lsOrders)
		{
			TCommonRecord crOrder = lsOrders.get(x);
			if (x > 0)
				Orders += ",";
			String fieldName = getField(tablesMap, crOrder.get("FieldName"));
			if (!"NULL".equalsIgnoreCase(fieldName) || "".equals(fieldName)){
				Orders += fieldName;
				if(crOrder.get("By").length() > 0) {
					Orders += " " + crOrder.get("By");
				}
			}else if(Orders.lastIndexOf(",") == Orders.length()-1) {
				Orders = Orders.substring(0, Orders.length()-1);
			}
		}
		Orders = (Orders.length() > 0) ? " order by " + Orders : "";
		sql.append(wheres).append(Group).append(Orders);
		return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<TCommonRecord> queryForListByPS(String sql, JDBCQueryImpl srcQuery) throws Exception
	{
		String temp = sql;
		Object[] obj = new Object[getCount(temp)];
		int n = 0;
		while (temp.indexOf("?") >= 0)
		{
			String value = temp.substring(temp.indexOf("?") + 1, temp.indexOf("?") + temp.substring(temp.indexOf("?")).indexOf(":end"));
			sql = sql.replaceFirst(value + ":end", "");
			obj[n] = value;
			n++;
			temp = temp.replaceFirst("\\?" + value + ":end", "");
		}
		return srcQuery.query(sql, obj, new CommonMapper());
	}
	
	public TCommonRecord queryForObjectByPS(String sql, JDBCQueryImpl srcQuery) throws Exception
	{
		String temp = sql;
		Object[] obj = new Object[getCount(temp)];
		int n = 0;
		while (temp.indexOf("?") >= 0)
		{
			String value = temp.substring(temp.indexOf("?") + 1, temp.indexOf("?") + temp.substring(temp.indexOf("?")).indexOf(":end"));
			sql = sql.replaceFirst(value + ":end", "");
			obj[n] = value;
			n++;
			temp = temp.replaceFirst("\\?" + value + ":end", "");
		}
		return (TCommonRecord) srcQuery.queryForObject(sql, obj, new CommonMapper());
	}

//	/**
//	 * 获取目标表
//	 * @param originalTable
//	 * @return
//	 */
//	private String getTargetTable(String originalTable)
//	{
//		if (originalTable == null)
//			originalTable = "";
//		if (originalTable.indexOf("(") >= 0 && originalTable.indexOf(")") >= 0)
//			return "";
//		JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
//		String sql = "select target_table from table_config where upper(original_table) = '" + originalTable.toUpperCase().replace("'", "''") + "' and his_name = '" + Config.getParamValue("HIS_NAME") + "'";
//		TCommonRecord entity = (TCommonRecord) query.queryForObject(sql, new CommonMapper());
//		if (entity == null)
//			entity = new TCommonRecord();
//		return entity.get("target_table");
//	}

	/**
	 * 获取目标表
	 * @param originalTable
	 * @return
	 */
	private String getTargetTable(String originalTable)
	{
		if (originalTable == null)
			originalTable = "";
		if (originalTable.indexOf("(") >= 0 && originalTable.indexOf(")") >= 0)
			return "";
		TTableConfig table = tables.get(originalTable.trim().toUpperCase());
		if (table == null)
			table = new TTableConfig();
		return table.getTargetTable();
	}
	
//	/**
//     * 获取目标表
//     * @param originalTable
//     * @return
//     */
//    private TCommonRecord getTargetTable4TCom(String originalTable)
//    {
//        if (originalTable == null)
//            originalTable = "";
//        if (originalTable.indexOf("(") >= 0 && originalTable.indexOf(")") >= 0)
//            return new TCommonRecord();
//        JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS"); 
//        String sql = "select TABLE_ID ,TABLE_DESC ,ORIGINAL_TABLE ,TARGET_TABLE ,REMARK ,HIS_NAME ,DB_URL ,DB_NAME  from table_config where upper(original_table) = '" + originalTable.toUpperCase().replace("'", "''") + "' and his_name = '" + Config.getParamValue("HIS_NAME") + "'";
//        TCommonRecord entity = (TCommonRecord) query.queryForObject(sql, new CommonMapper());
//        if (entity == null)
//            entity = new TCommonRecord();
//        return entity;
//    }
	
	/**
     * 获取目标表对象
     * @param originalTable
     * @return
     */
    private TTableConfig getTargetTable2Obj(String originalTable)
    {
        if (originalTable == null)
            originalTable = "";
        if (originalTable.indexOf("(") >= 0 && originalTable.indexOf(")") >= 0)
            return new TTableConfig();
		TTableConfig table = tables.get(originalTable.trim().toUpperCase());
		if (table == null)
			table = new TTableConfig();
        return table;
    }
    
	
//	/**
//	 * 根据原始表名和字段获取对应的目标字段
//	 * @param originalField
//	 * @param originalTable
//	 * @return
//	 */
//	private String getTargetField(String originalField, String originalTable)
//	{
//		originalField = originalField == null ? "" : originalField.trim();
//		originalTable = originalTable == null ? "" : originalTable.trim();
//		if (CaseHistoryCommon.ROWID.equals(originalField.toLowerCase()))
//			return originalField;
//		JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
//		String sql = "select b.target_field from table_config a, field_config b where a.table_id = b.table_id and upper(a.original_table) = '" + originalTable.toUpperCase().replace("'", "''") + "' and upper(b.original_field) = '" + originalField.toUpperCase().replace("'", "''") + "' and a.his_name = '" + Config.getParamValue("HIS_NAME") + "'";
//		TCommonRecord entity = (TCommonRecord) query.queryForObject(sql, new CommonMapper());
//		if (entity == null)
//			entity = new TCommonRecord();
//		return entity.get("target_field");
//	}
    
	
	/**
	 * 根据原始表名和字段获取对应的目标字段
	 * @param originalField
	 * @param originalTable
	 * @return
	 */
	private String getTargetField(String originalField, String originalTable)
	{
		originalField = originalField == null ? "" : originalField.trim();
		originalTable = originalTable == null ? "" : originalTable.trim();
		if (CaseHistoryCommon.ROWID.equals(originalField.toLowerCase()))
			return originalField;
		TTableConfig table = tables.get(originalTable.toUpperCase());
		if (table == null)
			table = new TTableConfig();
		if (table.getFieldMap() != null && table.getFieldMap().containsKey(originalField.toUpperCase()))
			return table.getFieldMap().get(originalField.toUpperCase()).getTargetField();
		return "''";
	}
	
//	/**
//	 * 根据原始表名获取目标表对应的原始和目标字段
//	 * @param originalTable
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	private List<TCommonRecord> getTargetFields(String originalTable)
//	{
//		originalTable = originalTable.trim();
//		JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
//		String sql = "select b.original_field, b.target_field from table_config a, field_config b where a.table_id = b.table_id and upper(a.original_table) = '" + originalTable.toUpperCase().replace("'", "''") + "' and a.his_name = '" + Config.getParamValue("HIS_NAME") + "'";
//		return query.query(sql, new CommonMapper());
//	}
	
	/**
	 * 根据原始表名获取目标表对应的原始和目标字段
	 * @param originalTable
	 * @return
	 */
	private TFieldConfig[] getTargetFields(String originalTable)
	{
		originalTable = originalTable == null ? "" : originalTable.trim();
		TTableConfig table = tables.get(originalTable.toUpperCase());
		if (table == null)
			table = new TTableConfig();
		return table.getFields();
	}
	
//	/**
//	 * 获取的table_config所有信息
//	 * @param tableid
//	 * @return
//	 */
//	private TCommonRecord getTableConfig(String tableid)
//	{
//	    tableid = tableid.trim();
//	    JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
//	    String sql = "select TABLE_ID ,TABLE_DESC ,ORIGINAL_TABLE ,TARGET_TABLE ,REMARK ,HIS_NAME ,DB_URL ,DB_NAME from table_config  where table_Id = '" + tableid + "'";
//	    return (TCommonRecord)query.queryForObject(sql, new CommonMapper());
//	}
	
	/**
	 * 获取表名与别名的Map：key-别名；value-表名
	 * @param strTables
	 * @return
	 */
	private Map<String, String> getTablesMap(String strTables)
	{
		Map<String, String> tablesMap = new HashMap<String, String>();
		Pattern pattern = Pattern.compile("(\\[)(.*?)(\\])");
		Matcher matcher = pattern.matcher(strTables);
		List<String> list = new ArrayList<String>();
		while (matcher.find()) { 
			strTables = strTables.replace(matcher.group(2), "tempTable");
			list.add(matcher.group(2));
		}
		String[] tablesWithAliases = strTables.split(",");
		int cnt = 0;
		for (String tableWithAliases : tablesWithAliases)
		{
			String [] tables = tableWithAliases.split(" ");
			if (tables.length > 1)
			{
				if (tables[0].indexOf("[") >= 0)
					tablesMap.put(tables[1], "(" + list.get(cnt++) + ")");
				else
					tablesMap.put(tables[1], tables[0]);
			}
			else
				tablesMap.put("imtmp", tables[0]);
		}
		return tablesMap;
	}
	
	/**
	 * 获取单个字段
	 * @param tablesMap
	 * @param field
	 * @return
	 */
	private String getField(Map<String, String>tablesMap, String field)
	{
		String fieldName = "";
		if (field.startsWith("fn:"))
			fieldName = field.substring(field.indexOf("fn:") + 3, field.indexOf(":end"));
		else
		{
			// 表名.字段
			if (field.indexOf(".") >= 0)
			{
				String[] fis = field.split("\\.");
				String tn = getTargetTable(tablesMap.get(fis[0]));
				// 非定义表
				if ("".equals(tn))
					fieldName = field;
				else
					fieldName = "".equals(getTargetField(fis[1], tablesMap.get(fis[0]))) ? "''" : fis[0] + "." + getTargetField(fis[1], tablesMap.get(fis[0]));
			}
			// 无表名字段
			else
			{
				String tn = getTargetTable(tablesMap.get("imtmp") == null ? "" : tablesMap.get("imtmp"));
				// 非定义表
				if ("".equals(tn) || "''".equals(getTargetField(field, tablesMap.get("imtmp") == null ? "" : tablesMap.get("imtmp"))))
					fieldName = field;
				else
					fieldName = "".equals(getTargetField(field, tablesMap.get("imtmp"))) ? "''" : getTargetField(field, tablesMap.get("imtmp"));
			}
		}
		return fieldName;
	}
	
	/**
	 * 获取字段列表
	 * @param tablesMap
	 * @param strFields
	 * @return
	 */
	private String getFields(Map<String, String>tablesMap, List<String> fieldsL)
	{
		String fields = "";
		/* 如果字段为*，必须是单表查询，否则需要写出所查字段，即使是需要所有的字段也要一一写出 */
		for (String field : fieldsL)
		{
			if ("*".equals(field))
			{
//				List<TCommonRecord> fieldsList = getTargetFields(tablesMap.get("imtmp"));
				TFieldConfig[] fieldsList = getTargetFields(tablesMap.get("imtmp"));
				for (TFieldConfig tcr : fieldsList)
				{
					if ("".equals(fields))
						fields += ("".equals(tcr.getTargetField().trim()) ? "''" : tcr.getTargetField()) + " as " + tcr.getOriginalField();
					else
						fields += ", " + ("".equals(tcr.getTargetField().trim()) ? "''" : tcr.getTargetField()) + " as " + tcr.getOriginalField();
				}
			}
			else
			{
				// 如果不包含fn:，说明为正常字段；否则为函数操作
				if (field.indexOf("fn:") < 0)
				{
					// 表名.字段
					if (field.indexOf(".") >= 0)
					{
						String[] fis = field.split("\\.");
						String tn = getTargetTable(tablesMap.get(fis[0]));
						// 非定义表
						if ("".equals(tn))
						{
							if ("".equals(fields))
								fields += field;
							else
								fields += ", " + field;
						}
						else
						{
							// 表名.字段 别名
							if (fis[1].indexOf(" ") >= 0)
							{
								if ("".equals(fields))
									fields += ("".equals(getTargetField(fis[1].split(" ")[0], tablesMap.get(fis[0]))) ? "''" : (fis[0] + "." + getTargetField(fis[1].split(" ")[0], tablesMap.get(fis[0])))) + " as " + fis[1].split(" ")[1];
								else
									fields += ", " + ("".equals(getTargetField(fis[1].split(" ")[0], tablesMap.get(fis[0]))) ? "''" : (fis[0] + "." + getTargetField(fis[1].split(" ")[0], tablesMap.get(fis[0])))) + " as " + fis[1].split(" ")[1];
							}
							// 表名.字段
							else
							{
								if ("*".equals(fis[1]))
								{
//									List<TCommonRecord> fieldsList = getTargetFields(tablesMap.get(fis[0]));
									TFieldConfig[] fieldsList = getTargetFields(tablesMap.get(fis[0]));
									for (TFieldConfig tcr : fieldsList)
									{
										if ("".equals(fields))
											fields += ("".equals(tcr.getTargetField()) ? "''" : tcr.getTargetField()) + " as " + tcr.getOriginalField();
										else
											fields += ", " + ("".equals(tcr.getTargetField()) ? "''" : tcr.getTargetField()) + " as " + tcr.getOriginalField();
									}
								}
								else
								{
									if ("".equals(fields))
										fields += ("".equals(getTargetField(fis[1], tablesMap.get(fis[0]))) ? "''" : (fis[0] + "." + getTargetField(fis[1], tablesMap.get(fis[0])))) + " as " + fis[1];
									else
										fields += ", " + ("".equals(getTargetField(fis[1], tablesMap.get(fis[0]))) ? "''" : (fis[0] + "." + getTargetField(fis[1], tablesMap.get(fis[0])))) + " as " + fis[1];
								}
							}
						}
					}
					// 无表名字段
					// 1.单表查询
					// 2.函数查询
					else
					{
						String tn = getTargetTable(tablesMap.get("imtmp") == null ? "" : tablesMap.get("imtmp"));
						// 非定义表
						if ("".equals(tn) || "".equals(getTargetField(field, tablesMap.get("imtmp") == null ? "" : tablesMap.get("imtmp"))))
						{
							if ("".equals(fields))
								fields += field;
							else
								fields += ", " + field;
						}
						else
						{
							// 字段 别名
							if (field.indexOf(" ") >= 0)
							{
								String[] fis = field.split(" ");
								if ("".equals(fields))
									fields += ("".equals(getTargetField(fis[0], tablesMap.get("imtmp"))) ? "''" : getTargetField(fis[0], tablesMap.get("imtmp"))) + " as " + fis[1];
								else
									fields += ", " + ("".equals(getTargetField(fis[0], tablesMap.get("imtmp"))) ? "''" : getTargetField(fis[0], tablesMap.get("imtmp"))) + " as " + fis[1];
							}
							// 字段
							else
							{
								if ("".equals(fields))
									fields += ("".equals(getTargetField(field, tablesMap.get("imtmp"))) ? "''" : getTargetField(field, tablesMap.get("imtmp"))) + " as " + field;
								else
									fields += ", " + ("".equals(getTargetField(field, tablesMap.get("imtmp"))) ? "''" : getTargetField(field, tablesMap.get("imtmp"))) + " as " + field;
							}
						}
					}
				}
				else
				{
					if ("".equals(fields))
						fields += field.substring(field.indexOf("fn:") + 3, field.indexOf(":end"));
					else
						fields += ", " + field.substring(field.indexOf("fn:") + 3, field.indexOf(":end"));
				}
			}
		}
		return fields;
	}
	
	/**
	 * 转换为oracle日期格式
	 * @param src
	 * @param fmt
	 * @return
	 */
    private String getDateSQL(String src, String fmt)
    {
    	if ((src == null) || (src.length() == 0))
    		src = "1981-01-01 00:00:00";
    	if (src.length() > 19)
    		src = src.substring(0, 19);
    	if (fmt.length() < 19)
    		src = src.substring(0, fmt.length());
    	return "To_Date('" + src + "', '" + fmt + "')";
    }
    
    /**
     * 初始化Tables
     * @param strTables
     * @return
     */
    private String initTables(String strTables)
    {
    	if (strTables == null || "".equals(strTables.trim()))
    		strTables = "*";
    	strTables = strTables.trim();
		while (strTables.indexOf(", ") >= 0)
		{
			strTables = strTables.replace(", ", ",");
		}
		while (strTables.indexOf("  ") >= 0)
		{
			strTables = strTables.replace("  ", " ");
		}
    	return strTables;
    }
    
    /**
     * 初始化Fields
     * @param strFields
     * @return
     */
    private List<String> initFields(String strFields)
    {
    	if (strFields == null || "".equals(strFields.trim()))
    		strFields = "*";
    	strFields = strFields.trim();
		while (strFields.indexOf(", ") >= 0)
		{
			strFields = strFields.replace(", ", ",");
		}
		while (strFields.indexOf("  ") >= 0)
		{
			strFields = strFields.replace("  ", " ");
		}
		List<String> list = new ArrayList<String>();
		String temp = "";
		while (strFields.indexOf(",") >= 0)
		{
			if (strFields.startsWith("fn:"))
			{
				temp = strFields.substring(strFields.indexOf("fn:"), strFields.indexOf(":end") + 4);
				list.add(temp);
				if (strFields.indexOf(":end") + 5 < strFields.length())
					strFields = strFields.substring(strFields.indexOf(":end") + 5, strFields.length());
				else
					strFields = "";
			}
			else
			{
				temp = strFields.substring(0, strFields.indexOf(","));
				list.add(temp);
				strFields = strFields.substring(strFields.indexOf(",") + 1, strFields.length());
			}
		}
		if (!"".equals(strFields))
			list.add(strFields);
    	return list;
    }
    
    /**
     * 获取?在字符串中的个数
     * @param source
     * @return
     */
    private int getCount(String source)
    {
    	int cnt = 0;
    	while (source.indexOf("?") >= 0)
    	{
    		cnt++;
    		source = source.replaceFirst("\\?", "");
    	}
    	return cnt;
    }
    
//    /**
//     * 获取特殊条件
//     * @param originalTable
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//	private List<TCommonRecord> getQueryConfig(String originalTable)
//    {
//		originalTable = originalTable.trim();
//		JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
//    	String sql = "select * from query_config where table_id = '" + originalTable.toUpperCase() + "'";
//    	return query.query(sql, new CommonMapper());
//    }
    
    /**
     * 获取特殊条件
     * @param originalTable
     * @return
     */
	private TQueryConfig[] getQueriesConfig(String originalTable)
    {
		originalTable = originalTable == null ? "" : originalTable.trim().toUpperCase();
		TTableConfig table = tables.get(originalTable);
		if (table == null)
			table = new TTableConfig();
    	return table.getQueries();
    }
    
    /**
     * 获取根据查询条件获取特殊配置原始表
     * @param strTables
     * @param lsWheres
     * @return
     */
    private String getTableName(String strTables, List<TCommonRecord> lsWheres)
    {
    	String tn = strTables;
//    	List<TCommonRecord> lsQueryConfigs = getQueryConfig(strTables);
    	TQueryConfig[] lsQueryConfigs = getQueriesConfig(strTables);
    	if (lsQueryConfigs != null && lsQueryConfigs.length > 0)
    	{
    		for (TQueryConfig lsQueryConfig : lsQueryConfigs)
    		{
    			// 获取条件描述
    			String query_condition = lsQueryConfig.getQueryCondition();
    			// 获取相应字段 ps:条件描述中的关系运算符暂时只支持=
				String field = query_condition.substring(0, query_condition.indexOf("="));
				TCommonRecord where = null;
	        	for (TCommonRecord lsWhere : lsWheres)
	        	{
	        		if (field.trim().equalsIgnoreCase(lsWhere.get("FieldName").trim()))
	        		{
	        			where = lsWhere;
	        		}
	        	}
	        	if (where == null)
	        		return tn;
    			// 判断条件描述是否带SQL ps:带SQL标志为sql:开头，:end结尾
    			if (query_condition.indexOf("sql:") >= 0)
    			{
    				String sql = query_condition.substring(query_condition.indexOf("sql:") + 4, query_condition.indexOf(":end"));
    				// 获取sql中的四个参数：表名、字段名、字段值、结果集类型
    				// 表名：希望因关联字段匹配要查询的目标表
    				// 字段名：需要得到的字段名，或者说有特殊查询条件的字段名
    				// 字段值：特殊的查询字段对应的值
    				// 结果集类型：单一对象查询single或者多结果查询list
    				String[] params = sql.split(",");
    				String tableName = params[0];
    				String fieldName = params[1];
    				String fieldValue = params[2];
    				String resultSet = params[3];
    				List<TCommonRecord> wheres = new ArrayList<TCommonRecord>();
    				wheres.add(where);
    				JDBCQueryImpl query = DBQueryFactory.getQuery(getDbUrl(tableName));
    				sql = genSQL(fieldName, tableName, wheres, new ArrayList<TCommonRecord>(), new ArrayList<TCommonRecord>());
    				// 结果集类型：单一对象查询
    				if ("single".equals(resultSet))
    				{
    					TCommonRecord entity = (TCommonRecord) query.queryForObject(sql, new CommonMapper());
    					if (fieldValue.trim().equals(entity.get(fieldName).trim()))
    					{
    						tn = query_condition.substring(query_condition.lastIndexOf(":") + 1, query_condition.length());
    						return tn;
    					}
    				}
					// 结果集类型：多结果集list
    				else
    				{
    					
    				}
    			}
    			// 固定字段，特殊值，特殊查询：字段名=值 
    			else
    			{
    				
    			}
    		}
    	}
    	return tn;
    }
}