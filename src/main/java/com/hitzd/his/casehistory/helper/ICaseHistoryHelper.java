package com.hitzd.his.casehistory.helper;

import java.util.List;
import java.util.Map;

import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.WebPage.PageView;
import com.hitzd.his.Beans.Middle.TTableConfig;
import com.hitzd.his.casehistory.CaseHistory;

public interface ICaseHistoryHelper 
{
	/**
	 * 就诊记录主键
	 */
	public static String Key_PatVisit         = "PatVisit";

	public static String Key_OperationMaster  = "OperationMaster";
	public static String Key_Operation        = "Operation";
	public static String Key_OperationName    = "OperationName";
	
	public static String Key_DrugDispenseRec  = "DrugDispenseRec";
	public static String Key_PatsInHospital   = "PatsInHospital";
	public static String Key_Orders           = "Orders";
	public static String Key_Diagnosis        = "Diagnosis";
	public static String Key_InpBillDetail    = "InpBillDetail";
	public static String Key_VitalSignsRec    = "VitalSignsRec";
	public static String Key_DrugPrescMaster  = "DrugPrescMaster";
	public static String Key_DrugPrescDetail  = "DrugPrescDetail";
	
	public static String Key_LabTestMaster    = "LabTestMaster";
	public static String Key_LabTestItems     = "LabTestItems";
	public static String Key_LabResult        = "LabResult";
	
	public static String Key_ExamMaster       = "ExamMaster";
	public static String Key_ExamItems        = "ExamItems";
	public static String Key_ExamReport       = "ExamReport";

	public static String Key_GermTest         = "GermTest";
	public static String Key_DrugSensitResult = "DrugSensitResult";
	public static String Key_GermTestResult   = "GermTestResult";
	public static String ROWID = "rowid";
	public static Map<String, TTableConfig> tables = CaseHistoryFactory.getTableMap();

    
    /**
     * 获取his数据连接
     * @param originalTable
     * @return
     */
	public abstract String getDbUrl(String originalTable);
	
	/**
	 * 获取指定病案号和住院号的病人的病历，暂时返回空值
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 */
	public abstract CaseHistory fetchCaseHistory(String PatientID, String VisitID, JDBCQueryImpl srcQuery);
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
	public abstract TCommonRecord fetchCaseHistory2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery);
	
	/**
	 * 获得病人的基本信息，通过PatientID取得用户基本信息
	 * @param PatientID
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract TCommonRecord fetchPatInfo2CR(String PatientID, JDBCQueryImpl srcQuery) throws Exception;
	public abstract TCommonRecord fetchPatInfo2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchPatInfo2List(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	/**
	 * 获得病人的就诊信息，包括多次住院的信息
	 * @param PatientID
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchPatVisit2CR(String PatientID, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchPatVisit2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	public abstract TCommonRecord fetchPatVisit2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 查询病人的就诊信息，通过附带条件拼成特殊的sql语句
	 * @param strFields 字段列表，一般是FieldName1, FieldName2,...FieldNameN，也可以是*，或者是count(*)等等
	 * @param lsWheres 条件列表，是一个TCommonRecord的列表，每一项TCommonRecord包括FieldName, GroupNo, FieldValue, FieldType, Relation, Condition
	 * 其中
	 * FieldName : 表示该条件与哪个字段相关
	 * FieldValue: 表示该条件的字段取值，如果FieldValue的第一个或最后一个字符为%，则生成like语句
	 * FieldType : 表示该字段的类型，如果是Char，则拼好的sql中包括'，否则不包括'，默认值为非char，即不带'
	 * Relation  : 表示字段名与字段值之间的关系，=、>=、<=等等，默认值为空值，表示关系为=
	 * GroupNo   : 表示该条件的组号，如果2个条件为一组，需要联动查询，例如：
	 * ((StartDateTime <= to_date('2011-01-01', 'yyyy-mm-dd')) and (StopDateTime >= to_date('2011-02-01', 'yyyy-mm-dd')))
	 * 上面的2个条件就必须一起用，因此以上2个字段为一组，GroupNo不应该发生重复，默认为空值，表示该条件单独算是一个条件
	 * Condition : 表示该条件前面跟的是and 还是 or，默认值为空值，表示前面跟 and 
	 * @param strGroup group by 语句后面跟着的字段列表，包含1项内容，FieldName
	 * @param strOrder order by 后面跟着的字段列表，包含2项内容，一项是FieldName，一项是By，By的取值为空，asc或desc
	 * 
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
	 * 
	 */
	public abstract List<TCommonRecord> fetchPatVisitInfo(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 生成sql语句
	 * @param strFields
	 * @param strTables
	 * @param lsWheres
	 * @param strGroup
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 */
	public abstract String genSQL(String strFields, String strTables, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders);
	/**
	 * 生成sql语句
	 * @param strFields
	 * @param strTables
	 * @param lsWheres
	 * @param strGroup
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 */
	public abstract String genSQLComm(String strFields, String strTables, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders);
	
	/**
	 * 获得病人的手术信息，指定了病人id和就诊id
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchOperationMaster2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchOperationMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
     * 获得 病人在科记录 信息，指定了病人id和就诊id
     * @param PatientID
     * @param VisitID
     * @param srcQuery
     * @return
     * @throws Exception
     */
    public abstract List<TCommonRecord> fetchTransfer2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception;
    public abstract List<TCommonRecord> fetchTransfer2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchOperation2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchOperation2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	/**
	 * 
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchOperationName2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchOperationName2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	/**
	 * 获得病人的摆药记录
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchDrugDispenseRec2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchDrugDispenseRec2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	/**
	 * 获得病人的在院信息
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchPatsInHospital2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchPatsInHospital2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	/**
	 * 获得病人的医嘱记录
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchOrders2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchOrders2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	/**
	 * 获得病人的诊断记录
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchDiagnosis2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchDiagnosis2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	/**
	 * 获得病人的费用明细
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchInpBillDetail2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchInpBillDetail2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	/**
	 * 获得病人的检查信息
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchLabTestMaster2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchLabTestMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchLabResult2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchLabTestItems2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	/**
	 * 获得病人的体征信息
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchVitalSignsRec2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchVitalSignsRec2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	/**
	 * 获得病人的检验信息
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchExamMaster2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchExamReport2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchExamItems2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchExamMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	/**
	 * 获得病人的微生物检验
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchGermTest2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchGermTest2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord>  fetchDrugSensitResult2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	/**
	 * 获得病人的涂片检验信息
	 * @param PatientID
	 * @param VisitID
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchGermTestResult2CR(String PatientID, String VisitID, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchGermTestResult2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	/**
	 * 获得病人的住院处方信息
	 * @param PatientID
	 * @param InpDate
	 * @param OutDate
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchDrugPrescMaster2CR(String PatientID, String InpDate, String OutDate, JDBCQueryImpl srcQuery) throws Exception;
	public abstract List<TCommonRecord> fetchDrugPrescMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	public abstract List<TCommonRecord> fetchDrugPrescDetail2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	/**
	 * 查询病人的医嘱信息
	 * @param PatientID
	 * @param VisitID
	 * @param OrderClass
	 * @param StartDateTime
	 * @param EndDateTime
	 * @param RepeatIndicator
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> queryOrders(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 查询药品字典
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchDrugDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 病案索引
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchMrIndex2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 病历文件索引
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchMrFileIndex2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 频次字典
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchPerformFreqDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 途径字典
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchAdministrationDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 诊断字典
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchDiagnosisDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 药品类别字典
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchDrugClassDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 细菌药敏字典
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchGermdrugsensitDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 微生物字典表
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchGermCodeDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 标本信息
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchSpecimanDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 医生信息
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchStaffDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 部门信息
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchDeptDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 费别信息
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchChargeTypeDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 身份信息
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchIdentityDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 剂型信息
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchDrugFormDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 分页查询
	 * @param maxresult
	 * @param currentpage
	 * @param fields
	 * @param tableName
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @return
	 */
	public abstract PageView<TCommonRecord> fetchTable2PV(int maxresult, int currentpage, String strFields, String strTables, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery);
	
	/**
	 * 病人状态变化字典
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchPatientStatusChgDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 药品供应商目录
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchDrugSupplierCatalog2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 药品毒理分类字典
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchDrugToxiPropertyDict2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 药品出库主表
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchDrugExportMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 药品出库明细表
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchDrugExportDetail2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 药品入库主表
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchDrugImportMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 药品入库明细表
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchDrugImportDetail2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 门诊就诊记录
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchClinicMaster2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 门诊医嘱主记录
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchOutpOrders2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 处方医嘱明细记录
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchOutpPresc2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 门诊病历记录
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchOutpMr2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 门诊诊断记录
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchClinicDiagnosis2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 等床病人记录
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchWaitBedPats2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 病人入出转及状态变化日志
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchAdtLog2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 准备出院病人记录
	 * @param strFields
	 * @param lsWheres
	 * @param lsGroups
	 * @param lsOrders
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> fetchPreDischgedPats2CR(String strFields, List<TCommonRecord> lsWheres, List<TCommonRecord> lsGroups, List<TCommonRecord> lsOrders, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 预编译查询列表
	 * @param sql
	 * @param objects
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract List<TCommonRecord> queryForListByPS(String sql, JDBCQueryImpl srcQuery) throws Exception;
	
	/**
	 * 预编译查询对象
	 * @param sql
	 * @param objects
	 * @param srcQuery
	 * @return
	 * @throws Exception
	 */
	public abstract TCommonRecord queryForObjectByPS(String sql, JDBCQueryImpl srcQuery) throws Exception;
}
