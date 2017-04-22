package com.ts.FetcherHander.InHospital;



import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.hitzd.Annotations.MHPerformProp;
import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.Utils.StringUtils;
import com.hitzd.his.DDD.DDDUtils;
import com.hitzd.his.Scheduler.IScheduler;
import com.hitzd.his.Scheduler.ReportScheduler;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.his.casehistory.helper.CaseHistoryFactory;
import com.hitzd.his.casehistory.helper.CaseHistoryFunction;
import com.hitzd.his.casehistory.helper.CaseHistoryHelperUtils;
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;
import com.hitzd.his.task.Task;


/**
 * @author Administrator
 * 该类可定时抽取出院病人的病历
 * 并对病历进行分析和统计，生成原始的统计数据
 *       
 */
@Transactional
public class iasDataFetcherEx extends ReportScheduler implements  IScheduler
{
    public iasDataFetcherEx() 
    {
    	try
    	{
    		setDebugLevel(Config.getIntParamValue("IASDebugLevel"));
    	}
    	catch (Exception ex)
    	{
    		setDebugLevel(50);
    		ex.printStackTrace();
    	}
    }

    // 全局变量，保存整体导入信息
    private TCommonRecord crCount = null;
    // 保存错误的sql语句列表
    private List<String> errorList = null;
    /* 厂家 */
    private List<TCommonRecord>    supplier  = new ArrayList<TCommonRecord>();
    //获取当前日期的前一天
    private String getPrevDate()
    {
    	return DateUtils.getCertainDate(DateUtils.getDate(), -1);
    }
    
    /**
     * 数据抽取时，先从住院主记录中查找编目日期CATALOG_DATE不为空且等于前一天日期的病人住院主记录，
	 * 从中获得病人标识PATIENT_ID和本次住院标识VISIT_ID，
	 * 以这两个标识从所有与之关联的表中将数据原样抽取到本地系统，进行统计分析。
     * @param hisQuery
     * @param prevDate
     */
    public void FetchData(JDBCQueryImpl hisQuery, String prevDate)
    {
    	beforeBuild(prevDate, "IAS");
    	crCount = new TCommonRecord();
    	errorList = new ArrayList<String>();
        JDBCQueryImpl patQuery = DBQueryFactory.getQuery("PatientHistory");
        beforeFetch(patQuery, prevDate);
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
    	List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		TCommonRecord where = CaseHistoryHelperUtils.genWhereCR(CaseHistoryFunction.genTrunc("MedRec.pat_visit", "catalog_date", null)
				,CaseHistoryFunction.genRToDate("MedRec.pat_visit", "catalog_date", "'" + prevDate + "'", "yyyy-mm-dd"), "", "", "", "");
		lsWheres.add(where);
    	String parBfr = chhr.genSQL("*", "MedRec.pat_visit", lsWheres, null, null);
        @SuppressWarnings("unchecked")
		List<TCommonRecord> patVisits = hisQuery.query(parBfr.toString(), new CommonMapper());

        Log(50, prevDate + "出院病人病历共有：" + patVisits.size() + "个");
        if (getOwner() != null)
        {
        	getOwner().setTotalCount(patVisits.size());
        }
        int C = 0;
	    for (TCommonRecord patV: patVisits)
	    {
	    	try
	        {
		    	C++;
		    	if (getOwner() != null)
		    		getOwner().setCurCount(C);
		    	TCommonRecord PatientInfo = new TCommonRecord();
	        	Log(50, "=====================[" + C + "/" + patVisits.size() +"]=====================");
	        	Log(40, "正在保存" + patV.get("PATIENT_ID") + "的基本信息...");
	        	ICaseHistoryHelper ch = CaseHistoryFactory.getCaseHistoryHelper();
	        	TCommonRecord crPatMasterIndex = ch.fetchCaseHistory2CR(patV.get("PATIENT_ID"), patV.get("Visit_ID"), hisQuery);
	        	ch = null;
	        	PatientInfo.set("LINK_DATE", prevDate);
	            // 保存病人主索引
	            SavePatMasterIndex(hisQuery, patQuery, patV.get("PATIENT_ID"), crPatMasterIndex, PatientInfo);
	            
	            // 这里计算了病人的年龄，摆药记录中计算ddd要判断病人年龄，小于18岁的不予计算ddd
	            PatVisit2PatientInfo(patV, PatientInfo);
	        	
	        	Log(40, "正在保存" + patV.get("PATIENT_ID") + "的就诊信息...");
	            // 保存病人就诊记录               
	            SavePatVisit(hisQuery, patQuery, patV, crPatMasterIndex, PatientInfo);
//	        	Log(40, "正在保存" + patV.get("PATIENT_ID") + "的手术记录...");
//	            // 保存手术记录，必须在摆药记录之前
//	            SaveOperation(hisQuery, patQuery, patV.get("PATIENT_ID"), patV.get("VISIT_ID"), crPatMasterIndex, PatientInfo);
	            // 保存手术名称
	            SaveOperationName(hisQuery, patQuery, patV.get("PATIENT_ID"), patV.get("Visit_ID"), crPatMasterIndex, PatientInfo);
	            
	        	Log(40, "正在保存" + patV.get("PATIENT_ID") + "的摆药记录...");
	            // 保存摆药记录，必须在医嘱之前
	            SaveDrugDispenseRec(hisQuery, patQuery, patV.get("PATIENT_ID"), patV.get("VISIT_ID"), crPatMasterIndex, PatientInfo);
	        	Log(40, "正在保存" + patV.get("PATIENT_ID") + "的在院记录...");
	            // 保存在院病人记录
	            SavePatsInHospital(hisQuery, patQuery, patV.get("PATIENT_ID"), patV.get("VISIT_ID"), crPatMasterIndex, PatientInfo);
	        	Log(40, "正在保存" + patV.get("PATIENT_ID") + "的医嘱信息...");
	            // 保存医嘱
	            SaveOrders(hisQuery, patQuery, patV.get("PATIENT_ID"), patV.get("VISIT_ID"), crPatMasterIndex, PatientInfo);
	        	Log(40, "正在保存" + patV.get("PATIENT_ID") + "的诊断信息...");
	            // 保存诊断表
	            SaveDiagnosis(hisQuery, patQuery, patV.get("PATIENT_ID"), patV.get("VISIT_ID"), crPatMasterIndex, PatientInfo);
	        	Log(40, "正在保存" + patV.get("PATIENT_ID") + "的费用明细记录...");
	            // 保存住院病人费用明细记录
	            SaveInpBillDetail(hisQuery, patQuery, patV.get("PATIENT_ID"), patV.get("VISIT_ID"), crPatMasterIndex, PatientInfo);
	        	Log(40, "正在保存" + patV.get("PATIENT_ID") + "的检验信息...");
	            // 保存检验主记录
	            SaveLabTestInfo(hisQuery, patQuery, patV.get("PATIENT_ID"), patV.get("VISIT_ID"), crPatMasterIndex, PatientInfo);
	        	Log(40, "正在保存" + patV.get("PATIENT_ID") + "的体症信息...");
	            // 保存病人体症记录
	            SaveVitalSignsRec(hisQuery, patQuery, patV.get("PATIENT_ID"), patV.get("VISIT_ID"), crPatMasterIndex, PatientInfo);
	        	Log(40, "正在保存" + patV.get("PATIENT_ID") + "的检查信息...");
	            // 保存检查记录
	            SaveExamInfo(hisQuery, patQuery, patV.get("PATIENT_ID"), patV.get("VISIT_ID"), crPatMasterIndex, PatientInfo);
	        	Log(40, "正在保存" + patV.get("PATIENT_ID") + "的微生物检验信息...");
	            // 保存微生物检验信息
	            SaveGermTestInfo(hisQuery, patQuery, patV.get("PATIENT_ID"), patV.get("VISIT_ID"), crPatMasterIndex, PatientInfo);
	            // 保存处方信息
	            //SaveDrugPresc(hisQuery, patQuery, patV.get("Patient_ID"), patV.get("Visit_ID"), crPatMasterIndex, PatientInfo);
	            
	            Log(40, "正在保存" + patV.get("PATIENT_ID") + "的抗菌药使用信息...");
	            drugDispenseOrders2PatientInfo(hisQuery, patV, PatientInfo);

	            insetPatientInfo(PatientInfo);
	            PatientInfo = null;
	            
	            onBuild(prevDate, crPatMasterIndex);
	        }
	    	catch(Exception e)
	        {
	    		Log(40, "抓取病人" + patV.get("PATIENT_ID") + "信息出现错误");
	        }
        }
	    
	    printSummary(patQuery, prevDate);
	    crCount = null;
	    patQuery = null;
    	errorList = null;
        parBfr = null;
        patVisits = null;
        if (getOwner() != null)
        	getOwner().setTaskStatusDesc("开始保存日报数据！");
        afterBuild(prevDate);
    }
    
    private void beforeFetch(JDBCQueryImpl patQuery, String prevDate)
    {
    	clearData(patQuery,prevDate);
		String sql = "Insert into Imp_Summary(Imp_Date, Start_Date) "
				   + "values (To_Date('" + prevDate + "', 'yyyy-mm-dd'), To_Date('" + DateUtils.getDateTime() + "', '" + StringUtils.DTFormat + "'))";
		patQuery.execute(sql);
    }
    
    /* 输出统计信息 */
    private void printSummary(JDBCQueryImpl patQuery, String prevDate)
    {
    	// keys没有顺序，不能用来按顺序输出
    	// List<String> keys = crCount.getKeys();
    	String info = "新病人信息:"             + crCount.get("新病人信息") + "; \n" +
			"总计就诊信息:"           + crCount.get("总计就诊信息") + "; \n" +
			"导入住院记录:"           + crCount.get("导入住院记录")           + "/" + crCount.get("总计住院记录")           + "; \n" +
			"导入摆药记录:"           + crCount.get("导入摆药记录")           + "/" + crCount.get("总计摆药记录")           + "; \n" +
			"导入医嘱信息:"           + crCount.get("导入医嘱信息")           + "/" + crCount.get("总计医嘱信息")           + "; \n" +
			"导入诊断信息:"           + crCount.get("导入诊断信息")           + "/" + crCount.get("总计诊断信息")           + "; \n" +
			"导入费用明细:"           + crCount.get("导入费用明细")           + "/" + crCount.get("总计费用明细")           + "; \n" +
			"导入手术记录:"           + crCount.get("导入手术记录")           + "/" + crCount.get("总计手术记录")           + "; \n" +
			"导入检验主记录:"         + crCount.get("导入检验主记录")         + "/" + crCount.get("总计检验主记录")         + "; \n" +
			"导入检验项目:"           + crCount.get("导入检验项目")           + "/" + crCount.get("总计检验项目")           + "; \n" +
			"导入检验结果:"           + crCount.get("导入检验结果")           + "/" + crCount.get("总计检验结果")           + "; \n" +
			"导入体症记录:"           + crCount.get("导入体症记录")           + "/" + crCount.get("总计体症记录")           + "; \n" +
			"导入检查记录:"           + crCount.get("导入检查记录")           + "/" + crCount.get("总计检查记录")           + "; \n" +
			"导入检查项目记录:"       + crCount.get("导入检查项目记录")       + "/" + crCount.get("总计检查项目记录")        + "; \n" +
			"导入检查项目结果记录:"   + crCount.get("导入检查项目结果记录")    + "/" + crCount.get("总计检查项目结果记录")    + "; \n" +
			"导入微生物检验记录:"     + crCount.get("导入微生物检验记录")     + "/" + crCount.get("总计微生物检验记录")      + "; \n" +
			"导入微生物检验结果:"     + crCount.get("导入微生物检验结果")     + "/" + crCount.get("总计微生物检验结果")      + "; \n" +
			"导入微生物涂片检验项目:" + crCount.get("导入微生物涂片检验项目")  + "/" + crCount.get("总计微生物涂片检验项目")  + "; \n" +  
			"发生错误:" + errorList.size();
    	int totalCount = crCount.getInt("新病人信息") + 
    		crCount.getInt("总计就诊信息") + 
    		crCount.getInt("总计住院记录")           + 
			crCount.getInt("总计摆药记录")           + 
			crCount.getInt("总计医嘱信息")           + 
			crCount.getInt("总计诊断信息")           + 
			crCount.getInt("总计费用明细")           + 
			crCount.getInt("总计手术记录")           + 
			crCount.getInt("总计检验主记录")         + 
			crCount.getInt("总计检验项目")           + 
			crCount.getInt("总计检验结果")           + 
			crCount.getInt("总计体症记录")           + 
			crCount.getInt("总计检查记录")           + 
			crCount.getInt("总计检查项目记录")        +
			crCount.getInt("总计检查项目结果记录")    +
			crCount.getInt("总计微生物检验记录")      +
			crCount.getInt("总计微生物检验结果")      +
			crCount.getInt("总计微生物涂片检验项目");  
		Log(50, info);
		String sql = "update Imp_Summary set" +
			" End_Date = To_Date('" + DateUtils.getDateTime() + "', '" + this.DTFormat + "'), " +
			" Imp_Total_Count = '" + totalCount       + "', " +
			" Imp_Error_Count = '" + errorList.size() + "'  " +
			" where To_Char(Imp_Date, 'yyyy-mm-dd') = '" + prevDate + "'";
		try
		{
			patQuery.execute(sql);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		sql = null;
		for (String err: errorList)
		{
			sql = "Insert into Imp_Error_List(Imp_Date_Time, Error_Info, Error_Date,error_code) values (" +
				"To_Date('" + prevDate + "', 'yyyy-mm-dd'), " +
				"'" + err.replaceAll("'", "''")               + "', " +
				"To_Date('" + DateUtils.getDateTime()         + "', '" + this.DTFormat + "'),  " +
				"'" + UUID.randomUUID().toString()   + "' " +
				")";
			try
			{
				patQuery.execute(sql);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		sql = null;
		info = null;
    }

    private String DTFormat = "yyyy-mm-dd hh24:mi:ss";
    
    /**
     * 保存病人信息至病历系统
     * @param hisQuery
     * @param patQuery
     * @param PatientID
     */
	public void SavePatMasterIndex(JDBCQueryImpl hisQuery, JDBCQueryImpl patQuery, String PatientID, TCommonRecord crPatMasterIndex, TCommonRecord PatientInfo)
    {
		//清理老的数据
    	String srcSQL = "delete from Pat_Master_Index where Patient_ID=?";
    	patQuery.update(srcSQL, new Object[]{PatientID});
    	Log(30, srcSQL);
    	
		TCommonRecord cr = crPatMasterIndex;
        /* 病人标识号 */
        PatientInfo.set("PATIENT_ID", cr.get("PATIENT_ID"));
        PatientInfo.set("SEX", cr.get("SEX"));
        PatientInfo.set("DATE_OF_BIRTH", cr.get("DATE_OF_BIRTH"));
        List<Object> sqlParams = new ArrayList<Object>();
		String dstSQL = "Insert into Pat_Master_Index(PATIENT_ID, INP_NO, NAME, NAME_PHONETIC, SEX, DATE_OF_BIRTH, " +
			"BIRTH_PLACE, CITIZENSHIP, NATION, ID_NO, IDENTITY, CHARGE_TYPE, UNIT_IN_CONTRACT, MAILING_ADDRESS, " +
			"ZIP_CODE, PHONE_NUMBER_HOME, PHONE_NUMBER_BUSINESS, NEXT_OF_KIN, RELATIONSHIP, NEXT_OF_KIN_ADDR, " +
			"NEXT_OF_KIN_ZIP_CODE, NEXT_OF_KIN_PHONE, LAST_VISIT_DATE, VIP_INDICATOR, CREATE_DATE, OPERATOR, " +
			"INSURANCE_ID, INSURANCE_CARD_NO, INSURANCE_ID_NO, LINK_DATE) "
			+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		sqlParams.add(cr.get("PATIENT_ID")                        );
		sqlParams.add(cr.get("INP_NO")                            );
		sqlParams.add(cr.get("NAME")                              ); 
		sqlParams.add(cr.get("NAME_PHONETIC")                     ); 
		sqlParams.add(cr.get("SEX")                               ); 
		Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("DATE_OF_BIRTH")).getTime());
		sqlParams.add(dateTime);
		sqlParams.add(cr.get("BIRTH_PLACE")                       ); 
		sqlParams.add(cr.get("CITIZENSHIP")                       ); 
		sqlParams.add(cr.get("NATION")                            ); 
		sqlParams.add(cr.get("ID_NO")                             ); 
		sqlParams.add(cr.get("IDENTITY")                          ); 
		sqlParams.add(cr.get("CHARGE_TYPE")                       ); 
		sqlParams.add(cr.get("UNIT_IN_CONTRACT")                  ); 
		sqlParams.add(cr.get("MAILING_ADDRESS")                   ); 
		sqlParams.add(cr.get("ZIP_CODE")                          ); 
		sqlParams.add(cr.get("PHONE_NUMBER_HOME")                 ); 
		sqlParams.add(cr.get("PHONE_NUMBER_BUSINESS")             ); 
		sqlParams.add(cr.get("NEXT_OF_KIN")                       ); 
		sqlParams.add(cr.get("RELATIONSHIP")                      ); 
		sqlParams.add(cr.get("NEXT_OF_KIN_ADDR")                  ); 
		sqlParams.add(cr.get("NEXT_OF_KIN_ZIP_CODE")              ); 
		sqlParams.add(cr.get("NEXT_OF_KIN_PHONE")                 ); 
		dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("LAST_VISIT_DATE"),DateUtils.FORMAT_DATETIME).getTime());
		sqlParams.add(dateTime);
		sqlParams.add(cr.get("VIP_INDICATOR")                     ); 
		dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("CREATE_DATE"),DateUtils.FORMAT_DATETIME).getTime());
		sqlParams.add(dateTime);
		sqlParams.add(cr.get("OPERATOR")                          ); 
		sqlParams.add(cr.get("INSURANCE_ID")                      ); 
		sqlParams.add(cr.get("INSURANCE_CARD_NO")                 ); 
		sqlParams.add(cr.get("INSURANCE_ID_NO")                   );
		dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
		sqlParams.add(dateTime);
    	Log(30, dstSQL);
		try
		{
			if (patQuery.update(dstSQL,sqlParams.toArray()) == 0)
			{
				errorList.add(dstSQL);
				this.Log(40, "病人信息保存失败，PatientID:" + PatientID);
			}
			else
			{
				crCount.set("新病人信息", (crCount.getInt("新病人信息") + 1) + "");
			}
		}
		catch (Exception ex)
		{
			errorList.add(dstSQL);
			this.Log(40, "病人信息保存失败，PatientID:" + PatientID + ", Exception:" + ex.getMessage());
			ex.printStackTrace();
		}
		dstSQL = null;
    }
    
    /**
     * 保存病人就诊信息至病历系统
     * @param hisQuery
     * @param patQuery
     * @param patV
     */
    public void SavePatVisit(JDBCQueryImpl hisQuery, JDBCQueryImpl patQuery, TCommonRecord patV, TCommonRecord crPatMasterIndex, TCommonRecord PatientInfo)
    {
    	// 入院科室信息
    	TCommonRecord cr = DictCache.getNewInstance().getDeptInfo(hisQuery, patV.get("DEPT_ADMISSION_TO"));
    	PatientInfo.set("IN_DEPT_NAME", cr.get("DEPT_NAME"));
    	PatientInfo.set("IN_INTERNAL_OR_SERGERY", cr.get("INTERNAL_OR_SERGERY"));
    	PatientInfo.set("IN_CLINIC_ATTR", cr.get("CLINIC_ATTR"));
    	PatientInfo.set("IN_OUTP_OR_INP", cr.get("OUTP_OR_INP"));
    	
    	// 出院科室信息
    	cr = DictCache.getNewInstance().getDeptInfo(hisQuery, patV.get("DEPT_DISCHARGE_FROM"));
    	PatientInfo.set("OUT_DEPT_NAME", cr.get("DEPT_NAME"));
    	PatientInfo.set("OUT_INTERNAL_OR_SERGERY", cr.get("INTERNAL_OR_SERGERY"));
    	PatientInfo.set("OUT_CLINIC_ATTR", cr.get("CLINIC_ATTR"));
    	PatientInfo.set("OUT_OUTP_OR_INP", cr.get("OUTP_OR_INP"));
    	List<Object> sqlParams = new ArrayList<Object>();
    	String dstSQL = "Insert into Pat_Visit(PATIENT_ID, VISIT_ID, DEPT_ADMISSION_TO, IN_DEPT_NAME, IN_CLINIC_ATTR, IN_OUTP_OR_INP, IN_INTERNAL_OR_SERGERY, " +
    			"ADMISSION_DATE_TIME, DEPT_DISCHARGE_FROM, OUT_DEPT_NAME, OUT_CLINIC_ATTR, OUT_OUTP_OR_INP, OUT_INTERNAL_OR_SERGERY, " +
    			"DISCHARGE_DATE_TIME, OCCUPATION, MARITAL_STATUS, IDENTITY, ARMED_SERVICES, DUTY, UNIT_IN_CONTRACT, CHARGE_TYPE, " +
    			"WORKING_STATUS, INSURANCE_TYPE, INSURANCE_NO, SERVICE_AGENCY, MAILING_ADDRESS, ZIP_CODE, NEXT_OF_KIN, RELATIONSHIP, " +
    			"NEXT_OF_KIN_ADDR, NEXT_OF_KIN_ZIPCODE, NEXT_OF_KIN_PHONE, PATIENT_CLASS, ADMISSION_CAUSE, CONSULTING_DATE, " +
    			"PAT_ADM_CONDITION, CONSULTING_DOCTOR, ADMITTED_BY, EMER_TREAT_TIMES, ESC_EMER_TIMES, SERIOUS_COND_DAYS, " +
    			"CRITICAL_COND_DAYS, ICU_DAYS, CCU_DAYS, SPEC_LEVEL_NURS_DAYS, FIRST_LEVEL_NURS_DAYS, SECOND_LEVEL_NURS_DAYS, " +
    			"AUTOPSY_INDICATOR, BLOOD_TYPE, BLOOD_TYPE_RH, BLOOD_TRAN_TIMES, BLOOD_TRAN_VOL, BLOOD_TRAN_REACT_TIMES, " +
    			"DECUBITAL_ULCER_TIMES, ALERGY_DRUGS, ADVERSE_REACTION_DRUGS, MR_VALUE, MR_QUALITY, FOLLOW_INDICATOR, FOLLOW_INTERVAL, " +
    			"FOLLOW_INTERVAL_UNITS, DIRECTOR, ATTENDING_DOCTOR, DOCTOR_IN_CHARGE, DISCHARGE_DISPOSITION, TOTAL_COSTS, TOTAL_PAYMENTS, " +
    			"CATALOG_DATE, CATALOGER, CATALOG_WRITER, INFUSION_REACT_TIMES, TOP_UNIT, SERVICE_SYSTEM_INDICATOR, HEALTH_LEVEL, " +
    			"MR_INFECT_REPORT, INFECT_INDICATOR, BODY_WEIGHT, BODY_HEIGHT, INTERNAL_NO, IDENTITY_CLASS, HBSAG_INDICATOR, HCV_AB_INDICATOR, " +
    			"HIV_AB_INDICATOR, CHIEF_DOCTOR, ADVANCED_STUDIES_DOCTOR, PRACTICE_DOCTOR_OF_GRADUATE, PRACTICE_DOCTOR, " +
    			"DOCTOR_OF_CONTROL_QUALITY, NURSE_OF_CONTROL_QUALITY, DATE_OF_CONTROL_QUALITY, FIRST_CASE_INDICATOR, THIRD_LEVEL_NURS_DAYS, " +
    			"X_EXAM_NO, MEDICAL_PAY_WAY, FIRST_AID_INDICATOR, TRAINING_INJURY_INDICATOR, SETTLE_INDI, LINK_DATE) " + 
    			"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" + 
    			",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		sqlParams.add(patV.get("PATIENT_ID")                     );
		sqlParams.add(patV.get("VISIT_ID")                       );
		sqlParams.add(patV.get("DEPT_ADMISSION_TO")              );
		sqlParams.add(PatientInfo.get("IN_DEPT_NAME")            );
		sqlParams.add(PatientInfo.get("IN_CLINIC_ATTR")          );
		sqlParams.add(PatientInfo.get("IN_OUTP_OR_INP")          );
		sqlParams.add(PatientInfo.get("IN_INTERNAL_OR_SERGERY")  );
		Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(patV.get("ADMISSION_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
		sqlParams.add(dateTime);
		sqlParams.add(patV.get("DEPT_DISCHARGE_FROM")            );
		sqlParams.add(PatientInfo.get("OUT_DEPT_NAME")           );
		sqlParams.add(PatientInfo.get("OUT_CLINIC_ATTR")         );
		sqlParams.add(PatientInfo.get("OUT_OUTP_OR_INP")         );
		sqlParams.add(PatientInfo.get("OUT_INTERNAL_OR_SERGERY") );
		dateTime = new Timestamp(DateUtils.getDateFromString(patV.get("DISCHARGE_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
		sqlParams.add(dateTime);
		sqlParams.add(patV.get("OCCUPATION")                     );
		sqlParams.add(patV.get("MARITAL_STATUS")                 );
		sqlParams.add(patV.get("IDENTITY")                       );
		sqlParams.add(patV.get("ARMED_SERVICES")                 );
		sqlParams.add(patV.get("DUTY")                           );
		sqlParams.add(patV.get("UNIT_IN_CONTRACT")               );
		sqlParams.add(patV.get("CHARGE_TYPE")                    );
		sqlParams.add(patV.get("WORKING_STATUS")                 );
		sqlParams.add(patV.get("INSURANCE_TYPE")                 );
		sqlParams.add(patV.get("INSURANCE_NO")                   );
		sqlParams.add(patV.get("SERVICE_AGENCY")                 );
		sqlParams.add(patV.get("MAILING_ADDRESS")                );
		sqlParams.add(patV.get("ZIP_CODE")                       );
		sqlParams.add(patV.get("NEXT_OF_KIN")                    );
		sqlParams.add(patV.get("RELATIONSHIP")                   );
		sqlParams.add(patV.get("NEXT_OF_KIN_ADDR")               );
		sqlParams.add(patV.get("NEXT_OF_KIN_ZIPCODE")            );
		sqlParams.add(patV.get("NEXT_OF_KIN_PHONE")              );
		sqlParams.add(patV.get("PATIENT_CLASS")                  );
		sqlParams.add(patV.get("ADMISSION_CAUSE")                );
		dateTime = new Timestamp(DateUtils.getDateFromString(patV.get("CONSULTING_DATE"),DateUtils.FORMAT_DATETIME).getTime());
		sqlParams.add(dateTime);
		sqlParams.add(patV.get("PAT_ADM_CONDITION")              );
		sqlParams.add(patV.get("CONSULTING_DOCTOR")              );
		sqlParams.add(patV.get("ADMITTED_BY")                    );
		sqlParams.add(patV.get("EMER_TREAT_TIMES")               );
		sqlParams.add(patV.get("ESC_EMER_TIMES")                 );
		sqlParams.add(patV.get("SERIOUS_COND_DAYS")              );
		sqlParams.add(patV.get("CRITICAL_COND_DAYS")             );
		sqlParams.add(patV.get("ICU_DAYS")                       );
		sqlParams.add(patV.get("CCU_DAYS")                       );
		sqlParams.add(patV.get("SPEC_LEVEL_NURS_DAYS")           );
		sqlParams.add(patV.get("FIRST_LEVEL_NURS_DAYS")          );
		sqlParams.add(patV.get("SECOND_LEVEL_NURS_DAYS")         );
		sqlParams.add(patV.get("AUTOPSY_INDICATOR")              );
		sqlParams.add(patV.get("BLOOD_TYPE")                     );
		sqlParams.add(patV.get("BLOOD_TYPE_RH")                  );
		sqlParams.add(patV.get("BLOOD_TRAN_TIMES")               );
		sqlParams.add(patV.get("BLOOD_TRAN_VOL")                 );
		sqlParams.add(patV.get("BLOOD_TRAN_REACT_TIMES")         );
		sqlParams.add(patV.get("DECUBITAL_ULCER_TIMES")          );
		sqlParams.add(patV.get("ALERGY_DRUGS")                   );
		sqlParams.add(patV.get("ADVERSE_REACTION_DRUGS")         );
		sqlParams.add(patV.get("MR_VALUE")                       );
		sqlParams.add(patV.get("MR_QUALITY")                     );
		sqlParams.add(patV.get("FOLLOW_INDICATOR")               );
		sqlParams.add(patV.get("FOLLOW_INTERVAL")                );
		sqlParams.add(patV.get("FOLLOW_INTERVAL_UNITS")          );
		sqlParams.add(patV.get("DIRECTOR")                       );
		sqlParams.add(patV.get("ATTENDING_DOCTOR")               );
		sqlParams.add(patV.get("DOCTOR_IN_CHARGE")               );
		sqlParams.add(patV.get("DISCHARGE_DISPOSITION")          );
		sqlParams.add(patV.get("TOTAL_COSTS")                    );
		sqlParams.add(patV.get("TOTAL_PAYMENTS")                 );
		dateTime = new Timestamp(DateUtils.getDateFromString(patV.get("CATALOG_DATE"),DateUtils.FORMAT_DATETIME).getTime());
		sqlParams.add(dateTime);
		sqlParams.add(patV.get("CATALOGER")                      );
		sqlParams.add(patV.get("CATALOG_WRITER")                 );
		sqlParams.add(patV.get("INFUSION_REACT_TIMES")           );
		sqlParams.add(patV.get("TOP_UNIT")                       );
		sqlParams.add(patV.get("SERVICE_SYSTEM_INDICATOR")       );
		sqlParams.add(patV.get("HEALTH_LEVEL")                   );
		sqlParams.add(patV.get("MR_INFECT_REPORT")               );
		sqlParams.add(patV.get("INFECT_INDICATOR")               );
		sqlParams.add(patV.get("BODY_WEIGHT")                    );
		sqlParams.add(patV.get("BODY_HEIGHT")                    );
		sqlParams.add(patV.get("INTERNAL_NO")                    );
		sqlParams.add(patV.get("IDENTITY_CLASS")                 );
		sqlParams.add(patV.get("HBSAG_INDICATOR")                );
		sqlParams.add(patV.get("HCV_AB_INDICATOR")               );
		sqlParams.add(patV.get("HIV_AB_INDICATOR")               );
		sqlParams.add(patV.get("CHIEF_DOCTOR")                   );
		sqlParams.add(patV.get("ADVANCED_STUDIES_DOCTOR")        );
		sqlParams.add(patV.get("PRACTICE_DOCTOR_OF_GRADUATE")    );
		sqlParams.add(patV.get("PRACTICE_DOCTOR")                );
		sqlParams.add(patV.get("DOCTOR_OF_CONTROL_QUALITY")      );
		sqlParams.add(patV.get("NURSE_OF_CONTROL_QUALITY")       );
		dateTime = new Timestamp(DateUtils.getDateFromString(patV.get("DATE_OF_CONTROL_QUALITY"),DateUtils.FORMAT_DATETIME).getTime());
		sqlParams.add(dateTime);
		sqlParams.add(patV.get("FIRST_CASE_INDICATOR")           );
		sqlParams.add(patV.get("THIRD_LEVEL_NURS_DAYS")          );
		sqlParams.add(patV.get("X_EXAM_NO")                      );
		sqlParams.add(patV.get("MEDICAL_PAY_WAY")                );
		sqlParams.add(patV.get("FIRST_AID_INDICATOR")            );
		sqlParams.add(patV.get("TRAINING_INJURY_INDICATOR")      );
		sqlParams.add(patV.get("SETTLE_INDI")                    );
		dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
		sqlParams.add(dateTime);
    	Log(30, dstSQL);
		try
		{
			if (patQuery.update(dstSQL,sqlParams.toArray()) == 0)
			{
				errorList.add(dstSQL);
				this.Log(40, "病人就诊信息保存失败，PatientID:" + patV.get("PATIENT_ID") + ", VisitID: " + patV.get("VISIT_ID") );
			}
			else
			{
				crCount.set("总计就诊信息", (crCount.getInt("总计就诊信息") + 1) + "");
				Log(40, "病人就诊信息保存成功，PatientID:" + patV.get("PATIENT_ID") + ", VisitID: " + patV.get("VISIT_ID") );
			}
		}
		catch (Exception ex)
		{
			errorList.add(dstSQL);
			this.Log(40, "病人就诊信息保存失败，PatientID:" + patV.get("PATIENT_ID") + ", VisitID: " + patV.get("VISIT_ID") + ", Exception:" + ex.getMessage());
			ex.printStackTrace();
		}
		dstSQL = null;
    }

    @SuppressWarnings("unchecked")
	public void SavePatsInHospital(JDBCQueryImpl hisQuery, JDBCQueryImpl patQuery, String PatientID, String VisitID, TCommonRecord crPatMasterIndex, TCommonRecord PatientInfo)
    {
    	TCommonRecord crVisit = (TCommonRecord)crPatMasterIndex.getObj(ICaseHistoryHelper.Key_PatVisit);
    	if (crVisit == null) return;
    	List<TCommonRecord> pihs = (List<TCommonRecord>)crVisit.getObj(ICaseHistoryHelper.Key_PatsInHospital);
    	if (pihs == null)
    	{
        	Log(40, "共有住院记录0个");
    		crCount.set("总计住院记录", (crCount.getInt("总计住院记录") + 0) + "");
    		return;
    	}
    	Log(40, "共有住院记录" + pihs.size() + "个");
		crCount.set("总计住院记录", (crCount.getInt("总计住院记录") + pihs.size()) + "");
    	int Count = 0;
    	for (TCommonRecord cr: pihs)
    	{
    		List<Object> sqlParams = new ArrayList<Object>();
    		String dstSQL = "Insert into Pats_In_Hospital(PATIENT_ID, VISIT_ID, WARD_CODE, DEPT_CODE, DEPT_NAME, CLINIC_ATTR, OUTP_OR_INP, INTERNAL_OR_SERGERY, BED_NO, ADMISSION_DATE_TIME, " +
    			"ADM_WARD_DATE_TIME, DIAGNOSIS, PATIENT_CONDITION, NURSING_CLASS, DOCTOR_IN_CHARGE, OPERATING_DATE, BILLING_DATE_TIME, " +
    			"PREPAYMENTS, TOTAL_COSTS, TOTAL_CHARGES, GUARANTOR, GUARANTOR_ORG, GUARANTOR_PHONE_NUM, BILL_CHECKED_DATE_TIME, " +
    			"SETTLED_INDICATOR, LINK_DATE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" +
			sqlParams.add(cr.get("PATIENT_ID")             );
			sqlParams.add(cr.get("VISIT_ID")               );
			sqlParams.add(cr.get("WARD_CODE")              );
			sqlParams.add(cr.get("DEPT_CODE")              );
			sqlParams.add(cr.get("DEPT_NAME")          );
			sqlParams.add(cr.get("CLINIC_ATTR")        );
			sqlParams.add(cr.get("OUTP_OR_INP")        );
			sqlParams.add(cr.get("INTERNAL_OR_SERGERY"));
			sqlParams.add(cr.get("BED_NO")                 );
			Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("ADMISSION_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("ADM_WARD_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("DIAGNOSIS")              );
			sqlParams.add(cr.get("PATIENT_CONDITION")      );
			sqlParams.add(cr.get("NURSING_CLASS")          );
			sqlParams.add(cr.get("DOCTOR_IN_CHARGE")       );
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("OPERATING_DATE"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("BILLING_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("PREPAYMENTS")            );
			sqlParams.add(cr.get("TOTAL_COSTS")            );
			sqlParams.add(cr.get("TOTAL_CHARGES")          );
			sqlParams.add(cr.get("GUARANTOR")              );
			sqlParams.add(cr.get("GUARANTOR_ORG")          );
			sqlParams.add(cr.get("GUARANTOR_PHONE_NUM")    );
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("BILL_CHECKED_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("SETTLED_INDICATOR")      );
			dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
			sqlParams.add(dateTime);
	    	Log(30, dstSQL);
    		try
    		{
    			if (patQuery.update(dstSQL,sqlParams.toArray()) == 0)
    			{
    				errorList.add(dstSQL);
    				this.Log(40, "病人住院信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
    			}
    			else
    			{
    				Count++;
    				//Log("病人住院信息保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
    			}
    		}
    		catch (Exception ex)
    		{
				errorList.add(dstSQL);
    			this.Log(40, "病人住院信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
    			ex.printStackTrace();
    		}
    		dstSQL = null;
    	}
		crCount.set("导入住院记录", (crCount.getInt("导入住院记录") + Count) + "");
    	Log(40, "保存住院记录" + Count + "个");
    	pihs = null;
    }
    
    // 保存摆药记录	
    @SuppressWarnings("unchecked")
    public void SaveDrugDispenseRec(JDBCQueryImpl hisQuery, JDBCQueryImpl patQuery, String PatientID, String VisitID, TCommonRecord crPatMasterIndex, TCommonRecord PatientInfo)
    {
    	//清理老数据
    	patQuery.update("delete from Drug_Dispense_Rec where patient_id = '" + PatientID + "' and visit_id = '" + VisitID + "'");
    	
    	TCommonRecord crVisit = (TCommonRecord)crPatMasterIndex.getObj(ICaseHistoryHelper.Key_PatVisit);
    	if (crVisit == null) return;
    	List<TCommonRecord> ddrs = (List<TCommonRecord>)crVisit.getObj(ICaseHistoryHelper.Key_DrugDispenseRec);
    	if (ddrs == null) return;
    	Log(40, "共有摆药记录" + ddrs.size() + "个");
		crCount.set("总计摆药记录", (crCount.getInt("总计摆药记录") + ddrs.size()) + "");
    	// 计算抗菌药物1、2、多联用
        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        // 术前用抗菌药
        boolean UseBefore = false;
        // 术后用抗菌药
        boolean UseAfter = false;
        // 药品数量
        List<String> drugList = new ArrayList<String>();
        // 抗菌药药品数量
        List<String> antiDrugList = new ArrayList<String>();
        // 药品费用总和
        int Count = 0;
    	for (TCommonRecord cr: ddrs)
    	{
    		try
    		{
	    		if (!drugList.contains(cr.get("DRUG_CODE")))
	    			drugList.add(cr.get("DRUG_CODE"));
	    		String curDate = DateUtils.getDateFromString(cr.get("DISPENSING_DATE_TIME"),DateUtils.FORMAT_DATETIME).toString();
	    		List<String> list = map.get(curDate);
	            if (list == null)
	            {
	            	list = new ArrayList<String>();
	              	map.put(curDate, list);
	            }
	            
	            if (DrugUtils.isKJDrug(cr.get("DRUG_CODE")))
	            {
	            	cr.set("is_anti", "1");
					double ddds = PatientInfo.getDouble("DDD_Value");
					double ddd = DDDUtils.CalcDDD(cr.get("DRUG_CODE"), cr.get("DRUG_SPEC"), cr.get("DRUG_UNITS"), cr.get("FIRM_ID"), cr.get("DISPENSE_AMOUNT"), cr.get("COSTS"));
					ddds += ddd;
					PatientInfo.set("DDD_Value", ddds + "");
					// 特殊抗菌药
					if (DrugUtils.isSpecDrug(cr.get("Drug_Code"), cr.get("DRUG_SPEC")))
					{
						PatientInfo.set("特殊用药", "1");
						double specDDDS = PatientInfo.getDouble("Spec_DDD_Value");
						specDDDS += ddd;
						PatientInfo.set("Spec_DDD_Value", specDDDS + "");
					}
					// 限制抗菌药
					if (DrugUtils.isLimitDrug(cr.get("Drug_Code"), cr.get("DRUG_SPEC")))
					{
	        			PatientInfo.set("限制用药", "1");
						double limitDDDS = PatientInfo.getDouble("Limit_DDD_Value");
						limitDDDS += ddd;
						PatientInfo.set("Limit_DDD_Value", limitDDDS + "");
					}
					cr.set("DDD_Value", ddds + "");
	            	if (!antiDrugList.contains(cr.get("Drug_Code")))
	            		antiDrugList.add(cr.get("DRUG_CODE"));
	            	if (!list.contains(cr.get("Drug_Code")))
	            		list.add(cr.get("DRUG_CODE"));
	            	if (PatientInfo.get("手术开始时间").length() > 0)
	            	{
	            		String[] OperStarts = PatientInfo.get("手术开始时间").split(";");
	            		String[] OperEnds   = PatientInfo.get("手术结束时间").split(";");
	            		for (int i = 0; i < OperStarts.length; i++)
	            		{
		            		if (!UseBefore)
		            		{
		            			// 此处要判断术前用药时机是否合理
		            			// 大于术前2h为过早
		            			// 术前0.5h-2h为恰当
		            			// 小于术前0.5h为过晚
		            			double dd = DateUtils.getDiffHoureFormatDateTime(cr.get("DISPENSING_DATE_TIME"), OperStarts[i]);
		            			UseBefore = dd <= Config.getIntParamValue("Hours_Before_Oper");
		            		}
		            		if (!UseAfter)
		            		{
		            			if (i < OperEnds.length)
		            			{
		            				double dd = DateUtils.getDiffHoureFormatDateTime(cr.get("DISPENSING_DATE_TIME"), OperEnds[i]);
		            				UseAfter = dd <= Config.getIntParamValue("Hours_After_Oper");
		            			}
		            		}
	            			double dd = DateUtils.getDiffHoureFormatDateTime(cr.get("DISPENSING_DATE_TIME"), OperStarts[i]);
	            			if (dd > 2)
	            				PatientInfo.set("手术用药过早", "1");
	            			else if (dd > 0.5)
	            				PatientInfo.set("手术用药恰当", "1");
	            			else
	            				PatientInfo.set("手术用药过晚", "1");
	            		}
	            	}
	            }
    		}
    		catch (Exception ex)
    		{
    			if (this.getOwner() != null )
    				this.getOwner().setErrorInfo(ex.getMessage());
    			ex.printStackTrace();
    		}
    		List<Object> sqlParams = new ArrayList<Object>();
    	    String dstSQL = "Insert into Drug_Dispense_Rec(DISPENSARY, DISPENSING_DATE_TIME, ORDERED_BY, DEPT_NAME, CLINIC_ATTR, OUTP_OR_INP, INTERNAL_OR_SERGERY, PATIENT_ID, VISIT_ID, ORDER_NO, " +
    			"ORDER_SUB_NO, DRUG_CODE, DRUG_SPEC, DRUG_UNITS, FIRM_ID, DISPENSE_AMOUNT, DISPENSING_PROVIDER, COSTS, CHARGES, " +
    			"CHARGE_INDICATOR, IS_ANTI, LINK_DATE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            sqlParams.add(cr.get("DISPENSARY")             );
            Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("DISPENSING_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("ORDERED_BY")             );
			sqlParams.add(cr.get("DEPT_NAME")          );
			sqlParams.add(cr.get("CLINIC_ATTR")        );
			sqlParams.add(cr.get("OUTP_OR_INP")        );
			sqlParams.add(cr.get("INTERNAL_OR_SERGERY"));
			sqlParams.add(cr.get("PATIENT_ID")             );
			sqlParams.add(cr.get("VISIT_ID")               );
			sqlParams.add(cr.get("ORDER_NO")               );
			sqlParams.add(cr.get("ORDER_SUB_NO")           );
			sqlParams.add(cr.get("pham_CODE")              );
			sqlParams.add(cr.get("DRUG_SPEC")              );
			sqlParams.add(cr.get("DRUG_UNITS")             );
			sqlParams.add(cr.get("FIRM_ID")                );
			sqlParams.add(cr.get("DISPENSE_AMOUNT")        );
			sqlParams.add(cr.get("DISPENSING_PROVIDER")    );
			sqlParams.add(cr.get("COSTS")                  );
			sqlParams.add(cr.get("CHARGES")                );
			sqlParams.add(cr.get("CHARGE_INDICATOR")       );
			sqlParams.add((DrugUtils.isKJDrug(cr.get("DRUG_CODE")) ? "1" : "") );
			dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
			sqlParams.add(dateTime);
	    	Log(30, dstSQL);
    		try
    		{
    			if (patQuery.update(dstSQL,sqlParams.toArray()) == 0)
    			{
    				errorList.add(dstSQL);
    				this.Log(40, "病人摆药记录保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
    			}
    			else
    			{
    				Count++;
    				Log("病人摆药记录保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
    			}
    		}
    		catch (Exception ex)
    		{
				errorList.add(dstSQL);
    			this.Log(40, "病人摆药记录保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
    			ex.printStackTrace();
    		}
    	}
    	/* PATIENT_INFO 表中 用药天数 */
        PatientInfo.set("DRUG_DAY", map.size() + "");
        // 一联
        Integer oneDay   = 0;
        // 二联
        Integer twoDay   = 0;
        // 三联
        Integer threeDay = 0;
        // 四联
        Integer fourDay  = 0;
        // 多联
        Integer morDay   = 0;
        for (List<String> list: map.values())
        {
       		switch (list.size())
       		{
       			case 0: break;
       			case 1: oneDay++;   break; 
       			case 2: twoDay++;   break;
       			case 3: threeDay++; break;
       			case 4: fourDay++;  break;
      			default: morDay++;  break;
       		}
        }
        Integer maxDrugADay = 0;
        if (oneDay   > 0) maxDrugADay = 1;
        if (twoDay   > 0) maxDrugADay = 2;
        if (threeDay > 0) maxDrugADay = 3;
        if (fourDay  > 0) maxDrugADay = 4;
        if (morDay   > 0) maxDrugADay = 5;
        
        /* PATIENT_INFO 表中 单联用药天数 */
        PatientInfo.set("ONE_DRUG", oneDay.toString());
        /* PATIENT_INFO 表中 两联用药天数 */
        PatientInfo.set("TWO_DRUG", twoDay.toString());
        /* PATIENT_INFO 表中 三联用药天数 */
        PatientInfo.set("THREE_DRUG", threeDay.toString());
        /* PATIENT_INFO 表中 四联用药天数 */
        PatientInfo.set("FOUR_DRUG", fourDay.toString());
        /* PATIENT_INFO 表中 多联用药天数 */
        PatientInfo.set("MOR_DRUG", morDay.toString());
        /* 病人的最大用药联数  */
        PatientInfo.set("MAX_DRUG_ADAY", maxDrugADay.toString());
        /* 抗菌药用药天数 */
        PatientInfo.set("ANTI_DAY", (oneDay + twoDay + morDay) + "");
        /* 抗菌药用药品种数 */
        PatientInfo.set("ANTI_KIND", antiDrugList.size() + "");
        // 术前用药
        if (UseBefore)
        	PatientInfo.set("术前用药", "1");
        // 术后用药
        if (UseAfter)
        	PatientInfo.set("术后用药", "1");
        /* 用药品种数     */
        PatientInfo.set("DRUG_KIND", drugList.size() + "");
		crCount.set("导入摆药记录", (crCount.getInt("导入摆药记录") + Count) + "");
    	Log(40, "保存摆药记录" + Count + "个");
    	ddrs = null;
    	antiDrugList = null;
    	drugList = null;
    	map = null;
    }
    
    // 保存医嘱
    @SuppressWarnings("unchecked")
    public void SaveOrders(JDBCQueryImpl hisQuery, JDBCQueryImpl patQuery, String PatientID, String VisitID, TCommonRecord crPatMasterIndex, TCommonRecord PatientInfo)
    {
    	boolean UseMiddle = false;
    	TCommonRecord crVisit = (TCommonRecord)crPatMasterIndex.getObj(ICaseHistoryHelper.Key_PatVisit);
    	if (crVisit == null) return;
    	List<TCommonRecord> orders = (List<TCommonRecord>)crVisit.getObj(ICaseHistoryHelper.Key_Orders);
    	if (orders == null) return;
    	Log(40, "共有医嘱信息" + orders.size() + "个");
		crCount.set("总计医嘱信息", (crCount.getInt("总计医嘱信息") + orders.size()) + "");
    	int Count = 0;
    	for (TCommonRecord cr: orders)
    	{
    		if (!UseMiddle)
    			UseMiddle = DrugUtils.isUseMiddle(cr.get("ADMINISTRATION"));
        	if (DrugUtils.isKJDrug(cr.get("ORDER_CODE")))
        	{
        		cr.set("is_anti", "1");
        	}
    		List<Object> sqlParams = new ArrayList<Object>();
        	String dstSQL = "Insert into Orders(PATIENT_ID, VISIT_ID, ORDER_NO, ORDER_SUB_NO, REPEAT_INDICATOR, ORDER_CLASS, ORDER_TEXT, " +
    			"ORDER_CODE, DOSAGE, DOSAGE_UNITS, ADMINISTRATION, DURATION, DURATION_UNITS, START_DATE_TIME, STOP_DATE_TIME, " +
    			"FREQUENCY, FREQ_COUNTER, FREQ_INTERVAL, FREQ_INTERVAL_UNIT, FREQ_DETAIL, PERFORM_SCHEDULE, PERFORM_RESULT, " +
    			"ORDERING_DEPT, DEPT_NAME, CLINIC_ATTR, OUTP_OR_INP, INTERNAL_OR_SERGERY, DOCTOR, STOP_DOCTOR, NURSE, ENTER_DATE_TIME, " +
    			"ORDER_STATUS, BILLING_ATTR, LAST_PERFORM_DATE_TIME, LAST_ACCTING_DATE_TIME, TREAT_SHEET_FLAG, PHAM_STD_CODE, AMOUNT, " +
    			"DRUG_BILLING_ATTR, STOP_NURSE, STOP_ORDER_DATE_TIME, LINK_DATE, DDD_Value" +
    			") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			sqlParams.add(cr.get("PATIENT_ID")             );
			sqlParams.add(cr.get("VISIT_ID")               );
			sqlParams.add(cr.get("ORDER_NO")               );
			sqlParams.add(cr.get("ORDER_SUB_NO")           );
			sqlParams.add(cr.get("REPEAT_INDICATOR")       );
			sqlParams.add(cr.get("ORDER_CLASS")            );
			sqlParams.add(cr.get("ORDER_TEXT")             );
			sqlParams.add(cr.get("ORDER_CODE")             );
			sqlParams.add(cr.get("DOSAGE")                 );
			sqlParams.add(cr.get("DOSAGE_UNITS")           );
			sqlParams.add(cr.get("ADMINISTRATION")         );
			sqlParams.add(cr.get("DURATION")               );
			sqlParams.add(cr.get("DURATION_UNITS")         );
			Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("START_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("STOP_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("FREQUENCY")              );
			sqlParams.add(cr.get("FREQ_COUNTER")           );
			sqlParams.add(cr.get("FREQ_INTERVAL")          );
			sqlParams.add(cr.get("FREQ_INTERVAL_UNIT")     );
			sqlParams.add(cr.get("FREQ_DETAIL").replace("'","")            );
			sqlParams.add(cr.get("PERFORM_SCHEDULE")       );
			sqlParams.add(cr.get("PERFORM_RESULT")         );
			sqlParams.add(cr.get("ORDERING_DEPT")          );
			sqlParams.add(cr.get("DEPT_NAME")          );
			sqlParams.add(cr.get("CLINIC_ATTR")        );
			sqlParams.add(cr.get("OUTP_OR_INP")        );
			sqlParams.add(cr.get("INTERNAL_OR_SERGERY"));
			sqlParams.add(cr.get("DOCTOR")                 );
			sqlParams.add(cr.get("STOP_DOCTOR")            );
			sqlParams.add(cr.get("NURSE")                  );
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("ENTER_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("ORDER_STATUS")           );
			sqlParams.add(cr.get("BILLING_ATTR")           );
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("LAST_PERFORM_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("LAST_ACCTING_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("TREAT_SHEET_FLAG")       );
			sqlParams.add(cr.get("PHAM_STD_CODE")          );
			sqlParams.add(cr.get("AMOUNT")                 );
			sqlParams.add(cr.get("DRUG_BILLING_ATTR")      );
			sqlParams.add(cr.get("STOP_NURSE")             );
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("STOP_ORDER_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
			sqlParams.add(dateTime);
			sqlParams.add("0");
				
	    	Log(30, dstSQL);
    		try
    		{
    			if (patQuery.update(dstSQL,sqlParams.toArray()) == 0)
    			{
    				errorList.add(dstSQL);
    				this.Log(40, "病人医嘱信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
    			}
    			else
    			{
    				Count++;
    				//Log("病人医嘱信息保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
    			}
    		}
    		catch (Exception ex)
    		{
				errorList.add(dstSQL);
    			this.Log(40, "病人医嘱信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
    			ex.printStackTrace();
    		}
    		dstSQL = null;
    	}
    	if (UseMiddle)
    		PatientInfo.set("术中用药", "1");
		crCount.set("导入医嘱信息", (crCount.getInt("导入医嘱信息") + Count) + "");
    	Log(40, "保存医嘱信息" + Count + "个");
    	orders = null;
    }
    
    // 保存诊断表
    @SuppressWarnings("unchecked")
    public void SaveDiagnosis(JDBCQueryImpl hisQuery, JDBCQueryImpl patQuery, String PatientID, String VisitID, TCommonRecord crPatMasterIndex, TCommonRecord PatientInfo)
    {
    	TCommonRecord crVisit = (TCommonRecord)crPatMasterIndex.getObj(ICaseHistoryHelper.Key_PatVisit);
    	if (crVisit == null) return;
    	List<TCommonRecord> diagnosis = (List<TCommonRecord>)crVisit.getObj(ICaseHistoryHelper.Key_Diagnosis);
    	if (diagnosis == null) return;
    	Log(40, "共有诊断信息" + diagnosis.size() + "个");
		crCount.set("总计诊断信息", (crCount.getInt("总计诊断信息") + diagnosis.size()) + "");
    	int Count = 0;
    	for (TCommonRecord cr: diagnosis)
    	{
    		
    		if (PatientInfo.getInt("INFECT_INDI") == 0) {
    			if (DrugUtils.isInfect(cr.get("DIAGNOSIS_TYPE"))) PatientInfo.set("INFECT_INDI", "1");
    		}
    		List<Object> sqlParams = new ArrayList<Object>();
    		String dstSQL = "Insert into Diagnosis(PATIENT_ID, VISIT_ID, DIAGNOSIS_TYPE, DIAGNOSIS_NO, DIAGNOSIS_DESC, DIAGNOSIS_DATE, TREAT_DAYS, " +
    			       "TREAT_RESULT, OPER_TREAT_INDICATOR, BASIC_DIAG, DIAGNOSIS_CODE, LINK_DATE) values (?,?,?,?,?,?,?,?,?,?,?,?)";
			sqlParams.add(cr.get("PATIENT_ID")           );
			sqlParams.add(cr.get("VISIT_ID")             );
			sqlParams.add(cr.get("DIAGNOSIS_TYPE")       );
			sqlParams.add(cr.get("DIAGNOSIS_NO")         );
			sqlParams.add(cr.get("DIAGNOSIS_DESC")       );
			Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("DIAGNOSIS_DATE"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("TREAT_DAYS")           );
			sqlParams.add(cr.get("TREAT_RESULT")         );
			sqlParams.add(cr.get("OPER_TREAT_INDICATOR") );
			sqlParams.add(cr.get("BASIC_DIAG")           );
			sqlParams.add(cr.get("DIAGNOSIS_CODE")       );
			dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
			sqlParams.add(dateTime);
			
	    	Log(30, dstSQL);
			try
			{
				if (patQuery.update(dstSQL,sqlParams.toArray()) == 0)
				{
    				errorList.add(dstSQL);
					this.Log(40, "病人诊断信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
				else
				{
					Count++;
					// Log("病人诊断信息保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
			}
			catch (Exception ex)
			{
				errorList.add(dstSQL);
				this.Log(40, "病人诊断信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
				ex.printStackTrace();
			}
			dstSQL = null;
		}
		crCount.set("导入诊断信息", (crCount.getInt("导入诊断信息") + Count) + "");
    	Log(40, "保存诊断信息" + Count + "个");
    	diagnosis = null;
    }
    
    // 保存住院病人费用明细记录
    @SuppressWarnings("unchecked")
    public void SaveInpBillDetail(JDBCQueryImpl hisQuery, JDBCQueryImpl patQuery, String PatientID, String VisitID, TCommonRecord crPatMasterIndex, TCommonRecord PatientInfo)
    {
    	TCommonRecord crVisit = (TCommonRecord)crPatMasterIndex.getObj(ICaseHistoryHelper.Key_PatVisit);
    	if (crVisit == null) return;
    	List<TCommonRecord> ipds = (List<TCommonRecord>)crVisit.getObj(ICaseHistoryHelper.Key_InpBillDetail);
    	if (ipds == null) return;
    	Log(40, "共有费用明细记录" + ipds.size() + "个");
		crCount.set("总计费用明细", (crCount.getInt("总计费用明细") + ipds.size()) + "");
    	/* 总药费*/
    	Float drugCosts   = 0f;
    	/* 抗菌药费 */
        Float drugAntCost = 0f;
        int Count = 0;
      	for (TCommonRecord cr: ipds)
     	{
      	    if(DrugUtils.isDrugInOrder(cr.get("item_class")))
      	    {
      	        drugCosts += Float.parseFloat(cr.get("COSTS"));
                if (DrugUtils.isKJDrug(cr.get("ITEM_CODE")))
      	        {
                	cr.set("is_anti", "1");
      	            drugAntCost += Float.parseFloat(cr.get("COSTS")); 
      	        }
      	    }
      	    // 2014-04-23 add liujc 用于拆分规格和厂家在一起的情况。
      	    if(supplier == null || supplier.size() == 0)
      	    {
      	        supplier = StringUtils.getSupplier();
      	    }
      	    StringUtils.execCF(supplier,cr);
    		List<Object> sqlParams = new ArrayList<Object>();
    		String dstSQL = "Insert into Inp_Bill_Detail(PATIENT_ID, VISIT_ID, ITEM_NO, ITEM_CLASS, ITEM_NAME, ITEM_CODE, ITEM_SPEC, AMOUNT, " +
    			"UNITS, ORDERED_BY, ORDER_DEPT_NAME, ORDER_CLINIC_ATTR, ORDER_OUTP_OR_INP, ORDER_INTERNAL_OR_SERGERY, " +
    			"PERFORMED_BY, PERFORM_DEPT_NAME, PERFORM_CLINIC_ATTR, PERFORM_OUTP_OR_INP, PERFORM_INTERNAL_OR_SERGERY, " +
    			"COSTS, CHARGES, BILLING_DATE_TIME, OPERATOR_NO, RCPT_NO, SPECIAL_CHARGES, FREE_LIMIT,Firm_ID, LINK_DATE" +
    			") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			sqlParams.add(cr.get("PATIENT_ID")              );
			sqlParams.add(cr.get("VISIT_ID")                );
			sqlParams.add(cr.get("ITEM_NO")                 );
			sqlParams.add(cr.get("ITEM_CLASS")              );
			sqlParams.add(cr.get("ITEM_NAME").replace("'", ""));
			sqlParams.add(cr.get("ITEM_CODE")               );
			sqlParams.add(cr.get("ITEM_SPEC")               );
			sqlParams.add(cr.get("AMOUNT")                  );
			sqlParams.add(cr.get("UNITS")                   );
			sqlParams.add(cr.get("ORDERED_BY")              );
			sqlParams.add(cr.get("DEPT_NAME")          );
			sqlParams.add(cr.get("CLINIC_ATTR")        );
			sqlParams.add(cr.get("OUTP_OR_INP")        );
			sqlParams.add(cr.get("INTERNAL_OR_SERGERY"));
			sqlParams.add(cr.get("PERFORMED_BY")            );
			sqlParams.add(cr.get("DEPT_NAME")          );
			sqlParams.add(cr.get("CLINIC_ATTR")        );
			sqlParams.add(cr.get("OUTP_OR_INP")        );
			sqlParams.add(cr.get("INTERNAL_OR_SERGERY"));
			sqlParams.add(cr.get("COSTS")                   );
			sqlParams.add(cr.get("CHARGES")                 );
			Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("BILLING_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("OPERATOR_NO")             );
			sqlParams.add(cr.get("RCPT_NO")                 );
			sqlParams.add(cr.get("SPECIAL_CHARGES")         );
			sqlParams.add(cr.get("FREE_LIMIT")              );
			sqlParams.add(cr.get("Firm_ID")              );
			dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
			sqlParams.add(dateTime);
			
	    	Log(30, dstSQL);
			try
			{
				if (patQuery.update(dstSQL,sqlParams.toArray()) == 0)
				{
    				errorList.add(dstSQL);
					this.Log(40, "病人费用明细保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
				else
				{
					Count++;
					// Log("病人费用明细保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
			}
			catch (Exception ex)
			{
				errorList.add(dstSQL);
				this.Log(40, "病人费用明细保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
				ex.printStackTrace();
			}
			dstSQL = null;
		}
    	/* PATIENT_INFO 表中 药品费用   */
        PatientInfo.set("DRUG_COSTS", drugCosts.toString());
        /* PATIENT_INFO 表中 抗菌药品费用 */
        PatientInfo.set("ANTI_COSTS", drugAntCost.toString());
		crCount.set("导入费用明细", (crCount.getInt("导入费用明细") + Count) + "");
    	Log(40, "保存费用明细记录" + Count + "个");
    	ipds = null;
    }
    
    @SuppressWarnings("unchecked")
    public void SaveOperation(JDBCQueryImpl hisQuery, JDBCQueryImpl patQuery, String PatientID, String VisitID, TCommonRecord crPatMasterIndex, TCommonRecord PatientInfo)
    {
    	TCommonRecord crVisit = (TCommonRecord)crPatMasterIndex.getObj(ICaseHistoryHelper.Key_PatVisit);
    	if (crVisit == null) return;
    	List<TCommonRecord> Operation = (List<TCommonRecord>)crVisit.getObj(ICaseHistoryHelper.Key_Operation);
    	if (Operation == null) return;
    	Log(40, "共有手术记录" + Operation.size() + "个");
		crCount.set("总计手术记录", (crCount.getInt("总计手术记录") + Operation.size()) + "");
    	PatientInfo.set("OPERATION_INDI", "0");
        /* 手术切口 */
    	PatientInfo.set("OPERATION_HEAL","-1");
        /* 愈合等级 */
    	PatientInfo.set("WOUND_GRADE","-1");
    	int Count = 0;
    	for (TCommonRecord cr: Operation)
    	{
    		/*String dstSQL = "Insert into Operation (PATIENT_ID, VISIT_ID, OPERATION_NO, OPERATION_DESC, OPERATION_CODE, HEAL, WOUND_GRADE, " +
    			"OPERATING_DATE, ANAESTHESIA_METHOD, OPERATOR, FIRST_ASSISTANT, SECOND_ASSISTANT, ANESTHESIA_DOCTOR, LINK_DATE) values (" +
				"'" + cr.get("PATIENT_ID")                             + "', " +
				"'" + cr.get("VISIT_ID")                               + "', " +
				"'" + cr.get("OPERATION_NO")                           + "', " +
				"'" + cr.get("OPERATION_DESC")                         + "', " +
				"'" + cr.get("OPERATION_CODE")                         + "', " +
				"'" + cr.get("HEAL")                                   + "', " +
				"'" + cr.get("WOUND_GRADE")                            + "', " +
				getDateSQL(cr.get("OPERATING_DATE"), DTFormat)         + " , " +
				"'" + cr.get("ANAESTHESIA_METHOD")                     + "', " +
				"'" + cr.get("OPERATOR")                               + "', " +
				"'" + cr.get("FIRST_ASSISTANT")                        + "', " +
				"'" + cr.get("SECOND_ASSISTANT")                       + "', " +
				"'" + cr.get("ANESTHESIA_DOCTOR")                      + "', " +
    			getDateSQL(PatientInfo.get("LINK_DATE"), "yyyy-mm-dd") +
				")";
	    	Log(30, dstSQL);
			try
			{
				if (patQuery.update(dstSQL) == 0)
				{
    				errorList.add(dstSQL);
					this.Log(40, "病人手术信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
				else
				{
					Count++;
					// Log("病人手术信息保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
			}
			catch (Exception ex)
			{
				errorList.add(dstSQL);
				this.Log(40, "病人手术信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
				ex.printStackTrace();
			}
			dstSQL = null;*/
            /* 手术次数 */
            PatientInfo.set("OPERATION_INDI", (PatientInfo.getInt("OPERATION_INDI") + 1) + "");
            /* 手术切口              */
            if (cr.get("HEAL").trim().length() > 0)
            	PatientInfo.set("OPERATION_HEAL", cr.get("HEAL"));
            /* 愈合等级 */
            if (cr.get("WOUND_GRADE").trim().length() > 0)
            {
            	if(!"Ⅰ".equals(PatientInfo.get("WOUND_GRADE")))
            	{
            		PatientInfo.set("WOUND_GRADE", cr.get("WOUND_GRADE"));
            	}
            }
            // 保存手术时间
            // PatientInfo.set("手术时间", cr.get("OPERATING_DATE"));
		}
		crCount.set("导入手术记录", (crCount.getInt("导入手术记录") + Count) + "");
    	Log(40, "保存手术记录" + Count + "个");
    	Operation = null;
    }
    
    public boolean isInterventionOperation(String OperationName, String OperationCode)
    {
    	if (OperationName.length() == 0)
    		return false;
    	JDBCQueryImpl pdssQuery = DBQueryFactory.getQuery("PDSS");
    	String sql = "select * from Intervention_Operation_Dict where IOD_Name like '%" + OperationName + "%'";
    	@SuppressWarnings("unchecked")
		List<TCommonRecord> list = pdssQuery.query(sql, new CommonMapper());
    	pdssQuery = null;
    	return (list != null) && (list.size() > 0);
    }
    
    // 保存手术记录名称
    public void SaveOperationName(JDBCQueryImpl hisQuery, JDBCQueryImpl patQuery, String PatientID, String VisitID, TCommonRecord crPatMasterIndex, TCommonRecord PatientInfo)
    {
    	TCommonRecord crVisit = (TCommonRecord)crPatMasterIndex.getObj(ICaseHistoryHelper.Key_PatVisit);
    	if (crVisit == null) return;
    	@SuppressWarnings("unchecked")
		List<TCommonRecord> Operation = (List<TCommonRecord>)crVisit.getObj(ICaseHistoryHelper.Key_OperationName);
    	if (Operation == null) return;
    	Log(40, "共有手术名称记录" + Operation.size() + "个");
		crCount.set("总计手术名称记录", (crCount.getInt("总计手术名称记录") + Operation.size()) + "");
    	int Count = 0;
    	boolean isInterventionOper = false;
    	for (TCommonRecord cr: Operation)
    	{
    		if (isInterventionOperation(cr.get("OPERATION"), cr.get("OPERATION_CODE")))
    			isInterventionOper = true;
    		List<Object> sqlParams = new ArrayList<Object>();
    		String dstSQL = "Insert into Operation_Name (PATIENT_ID, VISIT_ID, OPER_ID, OPERATION_NO, OPERATION, OPERATION_CODE, " +
    			"OPERATION_SCALE, WOUND_GRADE, SERIAL_NO, LINK_DATE) values (?,?,?,?,?,?,?,?,?,?)";
			sqlParams.add(cr.get("PATIENT_ID"));
			sqlParams.add(cr.get("VISIT_ID"));
			sqlParams.add(cr.get("OPER_ID"));
			sqlParams.add(cr.get("OPERATION_NO"));
			sqlParams.add(cr.get("OPERATION"));
			sqlParams.add(cr.get("OPERATION_CODE"));
			sqlParams.add(cr.get("OPERATION_SCALE"));
			sqlParams.add(cr.get("WOUND_GRADE"));
			sqlParams.add(cr.get("SERIAL_NO") );
			Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
			sqlParams.add(dateTime);
			
	    	Log(30, dstSQL);
			try
			{
				if (patQuery.update(dstSQL,sqlParams.toArray()) == 0)
				{
    				errorList.add(dstSQL);
					this.Log(40, "病人手术名称信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
				else
				{
					Count++;
				}
			}
			catch (Exception ex)
			{
				errorList.add(dstSQL);
				this.Log(40, "病人手术名称信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
				ex.printStackTrace();
			}
			dstSQL = null;
		}
    	if (isInterventionOper)
    		PatientInfo.set("介入手术", "1");
		crCount.set("导入手术名称记录", (crCount.getInt("导入手术名称记录") + Count) + "");
    	Log(40, "保存手术名称记录" + Count + "个");
    	Operation = null;
    }

    
    // 保存检验主记录
    @SuppressWarnings("unchecked")
    public void SaveLabTestInfo(JDBCQueryImpl hisQuery, JDBCQueryImpl patQuery, String PatientID, String VisitID, TCommonRecord crPatMasterIndex, TCommonRecord PatientInfo)
    {
    	TCommonRecord crVisit = (TCommonRecord)crPatMasterIndex.getObj(ICaseHistoryHelper.Key_PatVisit);
    	if (crVisit == null) return;
    	List<TCommonRecord> ltms = (List<TCommonRecord>)crVisit.getObj(ICaseHistoryHelper.Key_LabTestMaster);
    	if (ltms == null) return;
    	Log(40, "共有检验主记录" + ltms.size() + "个");
		crCount.set("总计检验主记录", (crCount.getInt("总计检验主记录") + ltms.size()) + "");
    	int Count = 0;
    	int Count1 = 0, Count1S = 0;
    	int Count2 = 0, Count2S = 0;
    	for (TCommonRecord cr: ltms)
    	{
    		List<Object> sqlParams = new ArrayList<Object>();
    		String dstSQL = "insert into Lab_Test_Master(TEST_NO, PRIORITY_INDICATOR, PATIENT_ID, VISIT_ID, WORKING_ID, EXECUTE_DATE, NAME, " +
    			"NAME_PHONETIC, CHARGE_TYPE, SEX, AGE, TEST_CAUSE, RELEVANT_CLINIC_DIAG, SPECIMEN, NOTES_FOR_SPCM, SPCM_RECEIVED_DATE_TIME, " +
    			"SPCM_SAMPLE_DATE_TIME, REQUESTED_DATE_TIME, " +
    			"ORDERING_DEPT, ORDER_DEPT_NAME, ORDER_CLINIC_ATTR, ORDER_OUTP_OR_INP, ORDER_INTERNAL_OR_SERGERY, ORDERING_PROVIDER, " +
    			"PERFORMED_BY, PERFORM_DEPT_NAME, PERFORM_CLINIC_ATTR, PERFORM_OUTP_OR_INP, PERFORM_INTERNAL_OR_SERGERY, RESULT_STATUS, " +
    			"RESULTS_RPT_DATE_TIME, TRANSCRIPTIONIST, VERIFIED_BY, COSTS, CHARGES, BILLING_INDICATOR, PRINT_INDICATOR, SUBJECT, LINK_DATE" +
    			") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			sqlParams.add(cr.get("TEST_NO")                                 );
			sqlParams.add(cr.get("PRIORITY_INDICATOR")                      );
			sqlParams.add(cr.get("PATIENT_ID")                              );
			sqlParams.add(cr.get("VISIT_ID")                                );
			sqlParams.add(cr.get("WORKING_ID")                              );
			Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("EXECUTE_DATE"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("NAME")                                    );
			sqlParams.add(cr.get("NAME_PHONETIC")                           );
			sqlParams.add(cr.get("CHARGE_TYPE")                             );
			sqlParams.add(cr.get("SEX")                                     );
			sqlParams.add(cr.get("AGE")                                     );
			sqlParams.add(cr.get("TEST_CAUSE")                              );
			sqlParams.add(cr.get("RELEVANT_CLINIC_DIAG")                    );
			sqlParams.add(cr.get("SPECIMEN")                                );
			sqlParams.add(cr.get("NOTES_FOR_SPCM")                          );
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("SPCM_RECEIVED_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("SPCM_SAMPLE_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("REQUESTED_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("ORDERING_DEPT")                           );
			sqlParams.add(cr.get("DEPT_NAME")                               );
			sqlParams.add(cr.get("CLINIC_ATTR")                             );
			sqlParams.add(cr.get("OUTP_OR_INP")                             );
			sqlParams.add(cr.get("INTERNAL_OR_SERGERY")                     );
			sqlParams.add(cr.get("ORDERING_PROVIDER")                       );
			sqlParams.add(cr.get("PERFORMED_BY")                            );
			sqlParams.add(cr.get("DEPT_NAME")                               );
			sqlParams.add(cr.get("CLINIC_ATTR")                             );
			sqlParams.add(cr.get("OUTP_OR_INP")                             );
			sqlParams.add(cr.get("INTERNAL_OR_SERGERY")                     );
			sqlParams.add(cr.get("RESULT_STATUS")                           );
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("RESULTS_RPT_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("TRANSCRIPTIONIST")                        );
			sqlParams.add(cr.get("VERIFIED_BY")                             );
			sqlParams.add(cr.get("COSTS")                                   );
			sqlParams.add(cr.get("CHARGES")                                 );
			sqlParams.add(cr.get("BILLING_INDICATOR")                       );
			sqlParams.add(cr.get("PRINT_INDICATOR")                         );
			sqlParams.add(cr.get("SUBJECT")                                 );
			dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
			sqlParams.add(dateTime);
			
	    	Log(30, dstSQL);
			try
			{
				if (patQuery.update(dstSQL,sqlParams.toArray()) == 0)
				{
    				errorList.add(dstSQL);
					this.Log(40, "病人检验信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
				else
				{
					Count++;
					// Log("病人检验信息保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
			}
			catch (Exception ex)
			{
				errorList.add(dstSQL);
				this.Log(40, "病人检验信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
				ex.printStackTrace();
			}
			dstSQL = null;
			
	    	List<TCommonRecord> ltis = (List<TCommonRecord>)cr.getObj(ICaseHistoryHelper.Key_LabTestItems);
	    	if (ltis != null)
	    	{
		    	Count1 += ltis.size();
		    	for (TCommonRecord crs: ltis)
		    	{
			        // 保存检验项目 
		    		List<Object> labParams = new ArrayList<Object>();
					dstSQL = "Insert into Lab_Test_Items(TEST_NO, ITEM_NO, ITEM_NAME, ITEM_CODE, LINK_DATE) values (?,?,?,?,?)";
					
					labParams.add(crs.get("TEST_NO"));
					labParams.add(crs.get("ITEM_NO"));
					labParams.add(crs.get("ITEM_NAME"));
					labParams.add(crs.get("ITEM_CODE")); 
					Timestamp labDateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
					labParams.add(labDateTime);
	    	    	Log(30, dstSQL);
					try
					{
						if (patQuery.update(dstSQL,labParams.toArray()) == 0)
						{
		    				errorList.add(dstSQL);
							this.Log(40, "病人检验项目保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
						}
						else
						{
							Count1S++;
							//Log("病人检验项目保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
						}
					}
					catch (Exception ex)
					{
	    				errorList.add(dstSQL);
						this.Log(40, "病人检验项目保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
						ex.printStackTrace();
					}
					dstSQL = null;
		    	}
	    	}
	    	List<TCommonRecord> lrs = (List<TCommonRecord>)cr.getObj(ICaseHistoryHelper.Key_LabResult);
	    	if (lrs != null)
	    	{
		    	Count2 += lrs.size();
		    	for (TCommonRecord crs: lrs)
		    	{
			        // 保存检验结果
		    		List<Object> labParams = new ArrayList<Object>();
					dstSQL = "Insert into Lab_Result(TEST_NO, ITEM_NO, PRINT_ORDER, REPORT_ITEM_NAME, REPORT_ITEM_CODE, RESULT, UNITS, " +
		    			"ABNORMAL_INDICATOR, INSTRUMENT_ID, RESULT_DATE_TIME, PRINT_CONTEXT, LINK_DATE) values (?,?,?,?,?,?,?,?,?,?,?,?)";
					labParams.add(crs.get("TEST_NO")                          );
					labParams.add(crs.get("ITEM_NO")                          );
					labParams.add(crs.get("PRINT_ORDER")                      );
					labParams.add(crs.get("REPORT_ITEM_NAME").replace("'","") );
					labParams.add(crs.get("REPORT_ITEM_CODE")                 );
					labParams.add(crs.get("RESULT")                           );
					labParams.add(crs.get("UNITS")                            );
					labParams.add(crs.get("ABNORMAL_INDICATOR")               );
					labParams.add(crs.get("INSTRUMENT_ID")                    );
					Timestamp labDateTime = new Timestamp(DateUtils.getDateFromString(crs.get("RESULT_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
					labParams.add(labDateTime);
	    			labParams.add(crs.get("PRINT_CONTEXT")                    );
	    			labDateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
	    			labParams.add(labDateTime);
	    	    	Log(30, dstSQL);
					try
					{
						if (patQuery.update(dstSQL,labParams.toArray()) == 0)
						{
		    				errorList.add(dstSQL);
							this.Log(40, "病人检验结果保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
						}
						else
						{
							Count2S++;
							// Log("病人检验结果保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
						}
					}
					catch (Exception ex)
					{
	    				errorList.add(dstSQL);
						this.Log(40, "病人检验结果保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
						ex.printStackTrace();
					}
					dstSQL = null;
		    	}
	    	}
		}
		crCount.set("导入检验主记录", (crCount.getInt("导入检验主记录") + Count) + "");
    	Log(40, "保存检验主记录" + Count + "个");
    	
    	crCount.set("总计检验项目", (crCount.getInt("总计检验项目") + Count1) + "");
    	Log(40, "共有检验项目" + Count1 + "个");
		crCount.set("导入检验项目", (crCount.getInt("导入检验项目") + Count1S) + "");
    	Log(40, "保存检验项目" + Count1S + "个");
    	
		crCount.set("总计检验结果", (crCount.getInt("总计检验结果") + Count2) + "");
    	Log(40, "共有检验结果" + Count2 + "个");
		crCount.set("导入检验结果", (crCount.getInt("导入检验结果") + Count2S) + "");
    	Log(40, "保存检验结果" + Count2S + "个");
    	ltms = null;
    }
    
    // 保存病人体症记录
    @SuppressWarnings("unchecked")
    public void SaveVitalSignsRec(JDBCQueryImpl hisQuery, JDBCQueryImpl patQuery, String PatientID, String VisitID, TCommonRecord crPatMasterIndex, TCommonRecord PatientInfo)
    {	
    	TCommonRecord crVisit = (TCommonRecord)crPatMasterIndex.getObj(ICaseHistoryHelper.Key_PatVisit);
    	if (crVisit == null) return;
    	List<TCommonRecord> vsrs = (List<TCommonRecord>)crVisit.getObj(ICaseHistoryHelper.Key_VitalSignsRec); 
    	if (vsrs == null) return;
    	Log(40, "共有体症记录" + vsrs.size() + "个");
		crCount.set("总计体症记录", (crCount.getInt("总计体症记录") + vsrs.size()) + "");
    	int Count = 0;
    	for (TCommonRecord cr: vsrs)
    	{
    		List<Object> sqlParams = new ArrayList<Object>();
    		String dstSQL = "Insert into Vital_Signs_Rec(PATIENT_ID, VISIT_ID, RECORDING_DATE, TIME_POINT, VITAL_SIGNS, VITAL_SIGNS_VALUES, UNITS, LINK_DATE" +
    			") values (?,?,?,?,?,?,?,?)";
    		sqlParams.add(cr.get("PATIENT_ID"));
    		sqlParams.add(cr.get("VISIT_ID"));
    		Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("RECORDING_DATE")).getTime());
			sqlParams.add(dateTime);
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("TIME_POINT"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("VITAL_SIGNS"));
			sqlParams.add(cr.get("VITAL_SIGNS_VALUES"));
			sqlParams.add(cr.get("UNITS"));
			dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
			sqlParams.add(dateTime);
	    	Log(30, dstSQL);
			try
			{
				if (patQuery.update(dstSQL,sqlParams.toArray()) == 0)
				{
    				errorList.add(dstSQL);
					this.Log(40, "病人体症记录保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
				else
				{
					Count++;
					//Log("病人体症记录保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
			}
			catch (Exception ex)
			{
				errorList.add(dstSQL);
				this.Log(40, "病人体症记录保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
				ex.printStackTrace();
			}
			dstSQL = null;
    	}
		crCount.set("导入体症记录", (crCount.getInt("导入体症记录") + Count) + "");
    	Log(40, "共有体症记录" + Count + "个");
    	vsrs = null;
    }
    
    // 保存检查记录
    @SuppressWarnings("unchecked")
    public void SaveExamInfo(JDBCQueryImpl hisQuery, JDBCQueryImpl patQuery, String PatientID, String VisitID, TCommonRecord crPatMasterIndex, TCommonRecord PatientInfo)
    {
    	TCommonRecord crVisit = (TCommonRecord)crPatMasterIndex.getObj(ICaseHistoryHelper.Key_PatVisit);
    	if (crVisit == null) return;
    	List<TCommonRecord> ems = (List<TCommonRecord>)crVisit.getObj(ICaseHistoryHelper.Key_ExamMaster);
    	if (ems == null) return;
    	Log(40, "共有检查记录" + ems.size() + "个");
		crCount.set("总计检查记录", (crCount.getInt("总计检查记录") + ems.size()) + "");
    	int Count = 0;
    	int Count1 = 0, Count1S = 0;
    	int Count2 = 0, Count2S = 0;
    	for (TCommonRecord cr: ems)
    	{
    		List<Object> sqlParams = new ArrayList<Object>();
    		String dstSQL = "Insert into Exam_Master(EXAM_NO, LOCAL_ID_CLASS, PATIENT_LOCAL_ID, PATIENT_ID, VISIT_ID, NAME, SEX, DATE_OF_BIRTH, " +
    			"EXAM_CLASS, EXAM_SUB_CLASS, SPM_RECVED_DATE, CLIN_SYMP, PHYS_SIGN, RELEVANT_LAB_TEST, RELEVANT_DIAG, CLIN_DIAG, EXAM_MODE, " +
    			"EXAM_GROUP, DEVICE, PERFORMED_BY, PERFORM_DEPT_NAME, PERFORM_CLINIC_ATTR, PERFORM_OUTP_OR_INP, PERFORM_INTERNAL_OR_SERGERY, PATIENT_SOURCE, FACILITY, REQ_DATE_TIME, " +
    			"REQ_DEPT, ORDER_DEPT_NAME, ORDER_CLINIC_ATTR, ORDER_OUTP_OR_INP, ORDER_INTERNAL_OR_SERGERY, REQ_PHYSICIAN, REQ_MEMO, " +
    			"SCHEDULED_DATE_TIME, NOTICE, EXAM_DATE_TIME, REPORT_DATE_TIME, TECHNICIAN, REPORTER, RESULT_STATUS, COSTS, CHARGES, " +
    			"CHARGE_INDICATOR, PRIORITY_INDICATOR, IDENTITY, CHARGE_TYPE, EXAM_REASON, VERIFIER, LINK_DATE) " + 
    			"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			sqlParams.add(cr.get("EXAM_NO")                             );
			sqlParams.add(cr.get("LOCAL_ID_CLASS")                      );
			sqlParams.add(cr.get("PATIENT_LOCAL_ID")                    );
			sqlParams.add(cr.get("PATIENT_ID")                          );
			sqlParams.add(cr.get("VISIT_ID")                            );
			sqlParams.add(cr.get("NAME")                                );
			sqlParams.add(cr.get("SEX")                                 );
			Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("DATE_OF_BIRTH")).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("EXAM_CLASS")                          );
			sqlParams.add(cr.get("EXAM_SUB_CLASS")                      );
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("SPM_RECVED_DATE"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("CLIN_SYMP")                           );
			sqlParams.add(cr.get("PHYS_SIGN")                           );
			sqlParams.add(cr.get("RELEVANT_LAB_TEST")                   );
			sqlParams.add(cr.get("RELEVANT_DIAG")                       );
			sqlParams.add(cr.get("CLIN_DIAG")                           );
			sqlParams.add(cr.get("EXAM_MODE")                           );
			sqlParams.add(cr.get("EXAM_GROUP")                          );
			sqlParams.add(cr.get("DEVICE")                              );
			sqlParams.add(cr.get("PERFORMED_BY")                        );
			sqlParams.add(cr.get("PERFORM_DEPT_NAME")                   );
			sqlParams.add(cr.get("PERFORM_CLINIC_ATTR")                 );
			sqlParams.add(cr.get("PERFORM_OUTP_OR_INP")                 );
			sqlParams.add(cr.get("PERFORM_INTERNAL_OR_SERGERY")         );
			sqlParams.add(cr.get("PATIENT_SOURCE")                      );
			sqlParams.add(cr.get("FACILITY")                            );
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("REQ_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("REQ_DEPT")                            );
			sqlParams.add(cr.get("ORDER_DEPT_NAME")                     );
			sqlParams.add(cr.get("ORDER_CLINIC_ATTR")                   );
			sqlParams.add(cr.get("ORDER_OUTP_OR_INP")                   );
			sqlParams.add(cr.get("ORDER_INTERNAL_OR_SERGERY")           );
			sqlParams.add(cr.get("REQ_PHYSICIAN")                       );
			sqlParams.add(cr.get("REQ_MEMO")                            );
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("SCHEDULED_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("NOTICE")                              );
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("EXAM_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("REPORT_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("TECHNICIAN")                          );
			sqlParams.add(cr.get("REPORTER")                            );
			sqlParams.add(cr.get("RESULT_STATUS")                       );
			sqlParams.add(cr.get("COSTS")                               );
			sqlParams.add(cr.get("CHARGES")                             );
			sqlParams.add(cr.get("CHARGE_INDICATOR")                    );
			sqlParams.add(cr.get("PRIORITY_INDICATOR")                  );
			sqlParams.add(cr.get("IDENTITY")                            );
			sqlParams.add(cr.get("CHARGE_TYPE")                         );
			sqlParams.add(cr.get("EXAM_REASON")                         );
			sqlParams.add(cr.get("VERIFIER")                            );
			dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
			sqlParams.add(dateTime);
			
	    	Log(30, dstSQL);
			try
			{
				if (patQuery.update(dstSQL,sqlParams.toArray()) == 0)
				{
    				errorList.add(dstSQL);
					this.Log(40, "病人检查记录保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
				else
				{
					Count++;
					//Log("病人检查记录保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
			}
			catch (Exception ex)
			{
				errorList.add(dstSQL);
				this.Log(40, "病人检查记录保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
				ex.printStackTrace();
			}
			dstSQL = null;

	    	List<TCommonRecord> eis = (List<TCommonRecord>)cr.getObj(ICaseHistoryHelper.Key_ExamItems);
	    	if (eis != null)
	    	{
		    	Count1 += eis.size();
		    	for (TCommonRecord crs: eis)
		    	{
		    		List<Object> sqlParams1 = new ArrayList<Object>();
					dstSQL = "Insert into Exam_Items(EXAM_NO, EXAM_ITEM_NO, EXAM_ITEM, EXAM_ITEM_CODE, COSTS, LINK_DATE) values (?,?,?,?,?,?)";
					sqlParams1.add(crs.get("EXAM_NO")        );
			    	sqlParams1.add(crs.get("EXAM_ITEM_NO")   );
			    	sqlParams1.add(crs.get("EXAM_ITEM")      );
			    	sqlParams1.add(crs.get("EXAM_ITEM_CODE") );
			    	sqlParams1.add(crs.get("COSTS")          );
			    	Timestamp dateTime1 = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
					sqlParams1.add(dateTime1);
	    	    	Log(30, dstSQL);
					try
					{
						if (patQuery.update(dstSQL,sqlParams1.toArray()) == 0)
						{
		    				errorList.add(dstSQL);
							this.Log(40, "病人检查记录保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
						}
						else
						{
							Count1S++;
							//Log("病人检查记录保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
						}
					}
					catch (Exception ex)
					{
	    				errorList.add(dstSQL);
						this.Log(40, "病人检查记录保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
						ex.printStackTrace();
					}
					dstSQL = null;
		    	}
	    	}
    	
	    	List<TCommonRecord> ers = (List<TCommonRecord>)cr.getObj(ICaseHistoryHelper.Key_ExamReport);
	    	if (ers != null)
	    	{
		    	Count2 += ers.size();
		    	for (TCommonRecord crs: ers)
		    	{
		    		List<Object> sqlParams2 = new ArrayList<Object>();
					dstSQL = "Insert into Exam_Report(EXAM_NO, EXAM_PARA, DESCRIPTION, IMPRESSION, RECOMMENDATION, IS_ABNORMAL, USE_IMAGE, " +
		    			"MEMO, DEVICE, LINK_DATE) values (?,?,?,?,?,?,?,?,?,?)";
					sqlParams2.add(crs.get("EXAM_NO")        );
					sqlParams2.add(crs.get("EXAM_PARA")      );
					sqlParams2.add(crs.get("DESCRIPTION")    );
					sqlParams2.add(crs.get("IMPRESSION")     );
					sqlParams2.add(crs.get("RECOMMENDATION") );
					sqlParams2.add(crs.get("IS_ABNORMAL")    );
					sqlParams2.add(crs.get("USE_IMAGE")      );
					sqlParams2.add(crs.get("MEMO")           );
	    			sqlParams2.add(crs.get("DEVICE")         );
	    			Timestamp dateTime2 = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
	    			sqlParams2.add(dateTime2);
	    	    	Log(30, dstSQL);
					try
					{
						if (patQuery.update(dstSQL,sqlParams2.toArray()) == 0)
						{
		    				errorList.add(dstSQL);
							this.Log(40, "病人检查记录保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
						}
						else
						{
							Count2S++;
							//Log("病人检查记录保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
						}
					}
					catch (Exception ex)
					{
	    				errorList.add(dstSQL);
						this.Log(40, "病人检查记录保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
						ex.printStackTrace();
					}
					dstSQL = null;
		    	}
	    	}
		}
		crCount.set("导入检查记录", (crCount.getInt("导入检查记录") + Count) + "");
    	Log(40, "保存检查记录" + Count + "个");

		crCount.set("总计检查项目记录", (crCount.getInt("总计检查项目记录") + Count1) + "");
    	Log(40, "共有检查项目记录" + Count1 + "个");
		crCount.set("导入检查项目记录", (crCount.getInt("导入检查项目记录") + Count1S) + "");
    	Log(40, "保存检查项目记录" + Count1S + "个");
    	
		crCount.set("总计检查项目结果记录", (crCount.getInt("总计检查项目结果记录") + Count2) + "");
    	Log(40, "共有检查项目结果记录" + Count2 + "个");
		crCount.set("导入检查项目结果记录", (crCount.getInt("导入检查项目结果记录") + Count2S) + "");
    	Log(40, "保存检查项目结果记录" + Count2S + "个");
    	ems = null;
    }
    
    // 保存微生物检验信息
    @SuppressWarnings("unchecked")
    public void SaveGermTestInfo(JDBCQueryImpl hisQuery, JDBCQueryImpl patQuery, String PatientID, String VisitID, TCommonRecord crPatMasterIndex, TCommonRecord PatientInfo)
    {
    	TCommonRecord crVisit = (TCommonRecord)crPatMasterIndex.getObj(ICaseHistoryHelper.Key_PatVisit);
    	if (crVisit == null) return;
    	List<TCommonRecord> ems = (List<TCommonRecord>)crVisit.getObj(ICaseHistoryHelper.Key_GermTest);
    	if (ems == null) return;
    	PatientInfo.set("病原学检测", ems.size() > 0 ? "1" : "0");
    	Log(40, "共有微生物检验记录" + ems.size() + "个");
		crCount.set("总计微生物检验记录", (crCount.getInt("总计微生物检验记录") + ems.size()) + "");
    	int Count = 0;
    	int Count1 = 0, Count1S = 0;
    	int Count2 = 0, Count2S = 0;
    	for (TCommonRecord cr: ems)
    	{
    		if (PatientInfo.getInt("病原菌检出标志") == 0)
    		{
    			if (cr.get("TEST_RESULT").equalsIgnoreCase(Config.getParamValue("Germ_Test_V1")))
    				PatientInfo.set("病原菌检出标志", "1");
    		}
    		if (cr.get("TEST_METHOD").equalsIgnoreCase(Config.getParamValue("Germ_Test_Method")))
    			PatientInfo.set("药敏试验", "1");
    		
        	TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(hisQuery, cr.get("ORDERING_DEPT"));
    		List<Object> sqlParams = new ArrayList<Object>();
    		String dstSQL = "Insert into germ_test(PATIENT_ID, VISIT_ID, TEST_NO, ORDERING_DEPT, " +
    			"DEPT_NAME, CLINIC_ATTR, OUTP_OR_INP, INTERNAL_OR_SERGERY, TEST_DATE, SPECIMEN_TYPE, ITEM_CODE, TEST_METHOD, " +
    			"ENZYME_ABDUCTION, ENZYME_SPECTRUM, ENZYME_BETA, TEST_RESULT, SPCM_RECEIVED_DATE, SPCM_COLLECTION_DATE, " +
    			"CULTURE_COMPLETION_DATE, HOURS_POSITIVE, CULTURE_TYPE, CULTURE_COMMENT_CODE, NAME, SEX, AGE, LINK_DATE) " + 
    			"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			sqlParams.add(cr.get("PATIENT_ID")                              );
			sqlParams.add(cr.get("VISIT_ID")                                );
			sqlParams.add(cr.get("TEST_NO")                                 );
			sqlParams.add(cr.get("ORDERING_DEPT")                           );
			sqlParams.add(crDept.get("DEPT_NAME")                           );
			sqlParams.add(crDept.get("CLINIC_ATTR")                         );
			sqlParams.add(crDept.get("OUTP_OR_INP")                         );
			sqlParams.add(crDept.get("INTERNAL_OR_SERGERY")                 );
			Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("TEST_DATE")).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("SPECIMEN_TYPE")                           );
			sqlParams.add(cr.get("ITEM_CODE")                               );
			sqlParams.add(cr.get("TEST_METHOD")                             );
			sqlParams.add(cr.get("ENZYME_ABDUCTION")                        );
			sqlParams.add(cr.get("ENZYME_SPECTRUM")                         );
			sqlParams.add(cr.get("ENZYME_BETA")                             );
			sqlParams.add(cr.get("TEST_RESULT")                             );
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("SPCM_RECEIVED_DATE"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("SPCM_COLLECTION_DATE"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("CULTURE_COMPLETION_DATE"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("HOURS_POSITIVE")                          );
			sqlParams.add(cr.get("CULTURE_TYPE")                            );
			sqlParams.add(cr.get("CULTURE_COMMENT_CODE")                    );
			sqlParams.add(cr.get("NAME")                                    );
			sqlParams.add(cr.get("SEX")                                     );
			sqlParams.add(cr.get("AGE")                                     );
			dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
			sqlParams.add(dateTime);
    		
	    	Log(30, dstSQL);
			try
			{
				if (patQuery.update(dstSQL,sqlParams.toArray()) == 0)
				{
    				errorList.add(dstSQL);
					this.Log(40, "病人微生物检验信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
				else
				{
					Count++;
					//Log("病人微生物检验信息保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
			}
			catch (Exception ex)
			{
				errorList.add(dstSQL);
				this.Log(40, "病人微生物检验信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
				ex.printStackTrace();
			}
			dstSQL = null;
    	
	    	List<TCommonRecord> dsrs = (List<TCommonRecord>)cr.getObj(ICaseHistoryHelper.Key_DrugSensitResult);
	    	if (dsrs != null)
	    	{
		    	Count2 += dsrs.size();
		    	for (TCommonRecord crs: dsrs)
		    	{
		    		List<Object> sqlParams1 = new ArrayList<Object>();
		    		dstSQL = "Insert into DRUG_SENSIT_RESULT(TEST_NO, ITEM_NO, GERM_NO, ANTI_NO, TEST_RESULT, TEST_VALUE, VITEK_RESULT, LINK_DATE) "
		    				+ "values (?,?,?,?,?,?,?,?)";
					sqlParams1.add(crs.get("TEST_NO")      );
			    	sqlParams1.add(crs.get("ITEM_NO")      );
			    	sqlParams1.add(crs.get("GERM_NO")      );
			    	sqlParams1.add(crs.get("ANTI_NO")      );
			    	sqlParams1.add(crs.get("TEST_RESULT")  );
			    	sqlParams1.add(crs.get("TEST_VALUE")   );
			    	sqlParams1.add(crs.get("VITEK_RESULT") );
			    	Timestamp dateTime1 = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
					sqlParams1.add(dateTime1);
	    	    	Log(30, dstSQL);
					try
					{
						if (patQuery.update(dstSQL,sqlParams1.toArray()) == 0)
						{
		    				errorList.add(dstSQL);
							this.Log(40, "病人微生物检验信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
						}
						else
						{
							Count2S ++;
							//Log("病人微生物检验信息保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
						}
					}
					catch (Exception ex)
					{
	    				errorList.add(dstSQL);
						this.Log(40, "病人微生物检验信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
						ex.printStackTrace();
					}
					dstSQL = null;
		    	}
	    	}
		}
    	List<TCommonRecord> gtrs = (List<TCommonRecord>)crVisit.getObj(ICaseHistoryHelper.Key_GermTestResult);
    	if (gtrs != null)
    	{
	    	Count1 += gtrs.size();
	    	for (TCommonRecord crs: gtrs)
	    	{
	    		if(!"".equals(crs.get("TEST_NO")))
	    		{
		    		TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(hisQuery, crs.get("ORDERING_DEPT"));
		    		List<Object> sqlParams = new ArrayList<Object>();
					String dstSQL = "Insert into GERM_TEST_RESULT(PATIENT_ID, VISIT_ID, TEST_NO, ORDERING_DEPT, " +
						"DEPT_NAME, CLINIC_ATTR, OUTP_OR_INP, INTERNAL_OR_SERGERY, TEST_DATE, SPECIMEN_TYPE, TEST_RESULT, " +
		    			"GRAM_POS_BAC, GRAM_NEG_BAC, FUNGUS, GRAM_POS_COC, GRAM_NEG_COC, FUSIFORMIS, EPITH_CELL, WHITE_B_CELL, VAGINA_CLEAR, " +
		    			"GRAM_POS_BAC_DESC, GRAM_POS_NEG_DESC, FUNGUS_DESC, GRAM_POS_COC_DESC, GRAM_NEG_COC_DESC, EPITH_CELL_DESC, WHITE_B_CELL_DESC, " +
		    			"OTHER_ITEM_DESC, VAGINA_CLEAR_DESC, NAME, SEX, AGE, LINK_DATE) " + 
		    			"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					sqlParams.add(crs.get("PATIENT_ID")                );
	    			sqlParams.add(crs.get("VISIT_ID")                  );
	    			sqlParams.add(crs.get("TEST_NO")                   );
	    			sqlParams.add(crs.get("ORDERING_DEPT")             );
					sqlParams.add(crDept.get("DEPT_NAME")              );
					sqlParams.add(crDept.get("CLINIC_ATTR")            );
					sqlParams.add(crDept.get("OUTP_OR_INP")            );
					sqlParams.add(crDept.get("INTERNAL_OR_SERGERY")    );	
					Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(crs.get("TEST_DATE")).getTime());
					sqlParams.add(dateTime);
	    			sqlParams.add(crs.get("SPECIMEN_TYPE")             );
	    			sqlParams.add(crs.get("TEST_RESULT")               );
	    			sqlParams.add(crs.get("GRAM_POS_BAC")              );
	    			sqlParams.add(crs.get("GRAM_NEG_BAC")              );
	    			sqlParams.add(crs.get("FUNGUS")                    );
	    			sqlParams.add(crs.get("GRAM_POS_COC")              );
	    			sqlParams.add(crs.get("GRAM_NEG_COC")              );
	    			sqlParams.add(crs.get("FUSIFORMIS")                );
	    			sqlParams.add(crs.get("EPITH_CELL")                );
	    			sqlParams.add(crs.get("WHITE_B_CELL")              );
	    			sqlParams.add(crs.get("VAGINA_CLEAR")              );
	    			sqlParams.add(crs.get("GRAM_POS_BAC_DESC")         );
	    			sqlParams.add(crs.get("GRAM_POS_NEG_DESC")         );
	    			sqlParams.add(crs.get("FUNGUS_DESC")               );
	    			sqlParams.add(crs.get("GRAM_POS_COC_DESC")         );
	    			sqlParams.add(crs.get("GRAM_NEG_COC_DESC")         );
	    			sqlParams.add(crs.get("EPITH_CELL_DESC")           );
	    			sqlParams.add(crs.get("WHITE_B_CELL_DESC")         );
	    			sqlParams.add(crs.get("OTHER_ITEM_DESC")           );
	    			sqlParams.add(crs.get("VAGINA_CLEAR_DESC")         );
	    			sqlParams.add(crs.get("NAME")                      );
	    			sqlParams.add(crs.get("SEX")                       );
	    			sqlParams.add(crs.get("AGE")                       );
	    			dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
					sqlParams.add(dateTime);
			    	Log(30, dstSQL);
					try
					{
						if (patQuery.update(dstSQL,sqlParams.toArray()) == 0)
						{
		    				errorList.add(dstSQL);
							this.Log(40, "病人微生物检验信息保存失败，PatientID:" + PatientID + ", VisitID: " + VisitID );
						}
						else
						{
							Count1S++;
							//Log("病人微生物检验信息保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
						}
					}
					catch (Exception ex)
					{
						errorList.add(dstSQL);
						this.Log(40, "病人微生物检验信息保存失败，PatientID:" + PatientID + ", VisitID: " + VisitID + ", Exception:" + ex.getMessage());
						ex.printStackTrace();
					}
					dstSQL = null;
	    		}
	    	}
    	}
		crCount.set("导入微生物检验记录", (crCount.getInt("导入微生物检验记录") + Count) + "");
    	Log(40, "保存微生物检验记录" + Count + "个");

		crCount.set("总计微生物检验结果", (crCount.getInt("总计微生物检验结果") + Count2) + "");
    	Log(40, "共有微生物检验结果" + Count2 + "个");
		crCount.set("导入微生物检验结果", (crCount.getInt("导入微生物检验结果") + Count2S) + "");
		Log(40, "保存微生物检验结果" + Count2S + "个");

		crCount.set("总计微生物涂片检验项目", (crCount.getInt("总计微生物涂片检验项目") + Count1) + "");
		Log(40, "共有微生物涂片检验项目" + Count1 + "个");
		crCount.set("导入微生物涂片检验项目", (crCount.getInt("导入微生物涂片检验项目") + Count1S) + "");
    	Log(40, "保存微生物涂片检验项目" + Count1S + "个");
    	ems = null;
   }
    @SuppressWarnings("unchecked")
    public void SaveDrugPresc(JDBCQueryImpl hisQuery, JDBCQueryImpl patQuery, String PatientID, String VisitID, TCommonRecord crPatMasterIndex, TCommonRecord PatientInfo)
    {
    	
    	JDBCQueryImpl iasQuery = DBQueryFactory.getQuery("PEAAS");
    	TCommonRecord crVisit = (TCommonRecord)crPatMasterIndex.getObj(ICaseHistoryHelper.Key_PatVisit);
    	if (crVisit == null) return;
    	List<TCommonRecord> dpm = (List<TCommonRecord>)crVisit.getObj(ICaseHistoryHelper.Key_DrugPrescMaster);
    	if (dpm == null) return;
    	PatientInfo.set("住院处方", dpm.size() > 0 ? "1" : "0");
    	Log(40, "共有住院处方记录" + dpm.size() + "个");
		crCount.set("总计住院处方记录", (crCount.getInt("总计住院处方记录") + dpm.size()) + "");
    	int Count = 0;
    	int Count2 = 0, Count2S = 0;
    	for (TCommonRecord cr: dpm)
    	{
    		List<Object> sqlParams = new ArrayList<Object>();
    		String dstSQL = "Insert into Drug_Presc_Master (PRESC_DATE, PRESC_NO, DISPENSARY, PATIENT_ID, NAME, " +
    			"IDENTITY, CHARGE_TYPE, UNIT_IN_CONTRACT, PRESC_TYPE, PRESC_ATTR, PRESC_SOURCE, REPETITION, COSTS, " + 
    			"PAYMENTS, ORDERED_BY, PRESCRIBED_BY, ENTERED_BY, DISPENSING_PROVIDER, Link_Date)" + 
    			" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    		Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("visit_DATE")).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(cr.get("PRESC_NO")                );       
			sqlParams.add(cr.get("DISPENSARY")              );
			sqlParams.add(cr.get("PATIENT_ID")              );
			sqlParams.add(cr.get("NAME")                    );
			sqlParams.add(cr.get("IDENTITY")                );
			sqlParams.add(cr.get("CHARGE_TYPE")             );
			sqlParams.add(cr.get("UNIT_IN_CONTRACT")        );
			sqlParams.add(cr.get("PRESC_TYPE")              );
			sqlParams.add(cr.get("PRESC_ATTR")              );
			sqlParams.add(cr.get("PRESC_SOURCE")            );
			sqlParams.add(cr.get("REPETITION")              );
			sqlParams.add(cr.get("COSTS")                   );
			sqlParams.add(cr.get("PAYMENTS")                );
			sqlParams.add(cr.get("ORDERED_BY")              );
			sqlParams.add(cr.get("PRESCRIBED_BY")           );
			sqlParams.add(cr.get("ENTERED_BY")              );
			sqlParams.add(cr.get("DISPENSING_PROVIDER")     );
			dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
			sqlParams.add(dateTime);
    			
	    	Log(30, dstSQL);
			try
			{
				if (iasQuery.update(dstSQL,sqlParams.toArray()) == 0)
				{
    				errorList.add(dstSQL);
					this.Log(40, "病人处方信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
				}
				else
				{
					Count++;
				}
			}
			catch (Exception ex)
			{
				errorList.add(dstSQL);
				this.Log(40, "病人处方信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
				ex.printStackTrace();
			}
			dstSQL = null;
	    	List<TCommonRecord> dpds = (List<TCommonRecord>)cr.getObj(ICaseHistoryHelper.Key_DrugPrescDetail);
	    	if (dpds != null)
	    	{
		    	Count2 += dpds.size();
		    	for (TCommonRecord crs: dpds)
		    	{
		    		List<Object> sqlParams1 = new ArrayList<Object>();
		    		dstSQL = "Insert into DRUG_Presc_Detail(" +
		    			"PRESC_DATE, PRESC_NO, ITEM_NO, DRUG_CODE, DRUG_SPEC, DRUG_NAME, FIRM_ID, PACKAGE_SPEC, PACKAGE_UNITS, " +
		    			"QUANTITY, COSTS, PAYMENTS, LINK_DATE) values ()";
		    		Timestamp dateTime1 = new Timestamp(DateUtils.getDateFromString(cr.get("visit_DATE")).getTime());
		    		sqlParams1.add(dateTime1);
		    		sqlParams1.add(crs.get("PRESC_NO")       );
		    		sqlParams1.add(crs.get("ITEM_NO")        );
		    		sqlParams1.add(crs.get("DRUG_CODE")      );
		    		sqlParams1.add(crs.get("DRUG_SPEC")      );
		    		sqlParams1.add(crs.get("DRUG_NAME")      );
		    		sqlParams1.add(crs.get("FIRM_ID")        );
		    		sqlParams1.add(crs.get("PACKAGE_SPEC")   );
		    		sqlParams1.add(crs.get("PACKAGE_UNITS")  );
		    		sqlParams1.add(crs.get("QUANTITY")       );
		    		sqlParams1.add(crs.get("COSTS")          );
		    		sqlParams1.add(crs.get("PAYMENTS")       );
			    	dateTime1 = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
			    	sqlParams1.add(dateTime1);
	    	    	Log(30, dstSQL);
					try
					{
						if (iasQuery.update(dstSQL,sqlParams1.toArray()) == 0)
						{
		    				errorList.add(dstSQL);
							this.Log(40, "病人处方药品信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
						}
						else
						{
							Count2S ++;
							//Log("病人微生物检验信息保存成功，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") );
						}
					}
					catch (Exception ex)
					{
	    				errorList.add(dstSQL);
						this.Log(40, "病人处方药品信息保存失败，PatientID:" + cr.get("PATIENT_ID") + ", VisitID: " + cr.get("VISIT_ID") + ", Exception:" + ex.getMessage());
						ex.printStackTrace();
					}
					dstSQL = null;
		    	}
	    	}
		}
    	dpm = null;
   }
    
    /**
     * 抽取  DRUG_DISPENSE_REC 摆药记录 , ORDERS 医嘱
     * 抽取并整理数据 到PATIENT_USE_ANTIBO 计算后填出 PATIENT_INFO表
     * @param hisQuery
     * @param parVisits
     * @param patientInfo
     */
    private void drugDispenseOrders2PatientInfo(JDBCQueryImpl hisQuery, TCommonRecord patVisits, TCommonRecord patientInfo)
    {
    	List<TCommonRecord> orders = getOrdersByMiddleWare(patVisits.get("patient_id"),patVisits.get("visit_id"),Config.getParamValue("Drug_In_Order"),hisQuery);
    	
        /* 抽取数据到PATIENT_USE_ANTIBO*/
        for(TCommonRecord entry :orders)
        {
            /* 添加数据到  抗生素使用详细信息表中 */
        	double dddValue = 0;
        	if (DrugUtils.isKJDrug(entry.get("order_code")))
        		dddValue = 0; 
        	TCommonRecord crDept = DictCache.getNewInstance().getDeptInfo(hisQuery, entry.get("ORDERING_DEPT"));
    		List<Object> sqlParams = new ArrayList<Object>();
            String sql = "insert into IAS_PATIENT_USE_ANTIBO(PATIENT_ID,VISIT_ID,ORDER_NO,ORDER_SUB_NO,ORDERING_DEPT" +
                            ",DEPT_NAME, CLINIC_ATTR, OUTP_OR_INP, INTERNAL_OR_SERGERY, DOCTOR_IN_CHARGE,PHAM_NAME,PHAM_CODE,START_DATE_TIME,STOP_DATE_TIME" +
                            ",DOSAGE,DOSAGE_UNITS,ADMINISTRATION,FREQUENCY,FREQ_COUNTER,COSTS,REASONABLE,STAND_DOSAGE, IS_ANTI, " +
                            "DDD_Value, LINK_DATE) " +
                            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		    sqlParams.add(entry.get("PATIENT_ID")                     );
            sqlParams.add(entry.get("VISIT_ID")                       );
            sqlParams.add(entry.get("ORDER_NO")                       );
            sqlParams.add(entry.get("ORDER_SUB_NO")                   );
            sqlParams.add(entry.get("ORDERING_DEPT")                  );
			sqlParams.add(crDept.get("DEPT_NAME")                     );
			sqlParams.add(crDept.get("CLINIC_ATTR")                   );
			sqlParams.add(crDept.get("OUTP_OR_INP")                   );
			sqlParams.add(crDept.get("INTERNAL_OR_SERGERY")           );	    		
            sqlParams.add(entry.get("DOCTOR")                         );
            sqlParams.add(entry.get("order_text")                     );
            sqlParams.add(entry.get("order_code")                     );
            Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(entry.get("START_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			dateTime = new Timestamp(DateUtils.getDateFromString(entry.get("STOP_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
            sqlParams.add(entry.get("DOSAGE")                         );
            sqlParams.add(entry.get("DOSAGE_UNITS")                   );
            sqlParams.add(entry.get("ADMINISTRATION")                 );
            sqlParams.add(entry.get("FREQUENCY")                      );
            sqlParams.add(entry.get("FREQ_COUNTER")                   );
            sqlParams.add(entry.get("COSTS")                          );
            sqlParams.add(entry.get("是否使用合理")                    );
            sqlParams.add(entry.get("标准剂量")                        );
            sqlParams.add((DrugUtils.isKJDrug(entry.get("order_code")) ? "1" : "0") );
            sqlParams.add(dddValue                                    );
            dateTime = new Timestamp(DateUtils.getDateFromString(patientInfo.get("LINK_DATE")).getTime());
    		sqlParams.add(dateTime);
        	
	    	Log(30, sql);
            try
            {
                int i = query.update(sql,sqlParams.toArray());
                if(i == 0)
                {
    				errorList.add(sql);
                }
            }
            catch(Exception e)
            {
                /* 错误记录 */
                e.printStackTrace();
            }
        }
    }
    
    private  List<TCommonRecord> getOrdersByMiddleWare(String patientId,String visitId,String DrugInOrder,JDBCQueryImpl hisQuery) {
    	ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
		List<TCommonRecord> lsWheres =  new ArrayList<TCommonRecord>();
		TCommonRecord lsWhere = CaseHistoryHelperUtils.genWhereCR("patient_id",patientId, "Char", "", "", "");
		lsWheres.add(lsWhere);
		lsWhere = CaseHistoryHelperUtils.genWhereCR("visit_id", visitId, "Char", "", "", "");
		lsWheres.add(lsWhere);
		lsWhere = CaseHistoryHelperUtils.genWhereCR("order_class", DrugInOrder, "Char", "", "", "");
		lsWheres.add(lsWhere);
    	String srcSQL = chhr.genSQL("*", "ordadm.ORDERS", lsWheres, null, null);
    	@SuppressWarnings("unchecked")
		List<TCommonRecord> orders = hisQuery.query(srcSQL, new CommonMapper());
    	for(TCommonRecord data:orders) {
    		TCommonRecord lsWhere1 = CaseHistoryHelperUtils.genWhereCR("patient_id",data.get("patient_id"), "Char", "", "", "");
    		lsWheres.add(lsWhere1);
    		lsWhere = CaseHistoryHelperUtils.genWhereCR("visit_id", data.get("visit_id"), "Char", "", "", "");
    		lsWheres.add(lsWhere1);
    		lsWhere = CaseHistoryHelperUtils.genWhereCR("order_no", data.get("order_no"), "Char", "", "", "");
    		lsWheres.add(lsWhere1);
    		lsWhere = CaseHistoryHelperUtils.genWhereCR("order_sub_no", data.get("order_sub_no"), "Char", "", "", "");
    		lsWheres.add(lsWhere1);
        	String srcSQL1 = chhr.genSQL(CaseHistoryFunction.genSum("pharmacy.PHAM_DISPENSE_REC", "costs", "costs"), "pharmacy.PHAM_DISPENSE_REC", lsWheres, null, null);
        	TCommonRecord costs = (TCommonRecord) hisQuery.queryForObject(srcSQL1, new CommonMapper());
        	data.set("costs", costs.get("costs"));
    	}
    	return orders;
    }
    
    /**
     * 填充  抗生素使用综合信息 IAS_PATIENT_INFO 数据集
     */
    private void PatVisit2PatientInfo(TCommonRecord patVisit, TCommonRecord patientInfo)
    {
        /* 出生日期*/
        String dateFoBirth = patientInfo.get("DATE_OF_BIRTH");
        if ((dateFoBirth == null) || (dateFoBirth.length() == 0))
        	dateFoBirth = "1981-01-01";
        String nowDate     = getPrevDate();
        nowDate            = nowDate.substring(0,nowDate.indexOf("-")); 
        dateFoBirth        = dateFoBirth.substring(0,dateFoBirth.indexOf("-"));
        /* 年龄 */
        int strAge         =  Integer.parseInt(nowDate) - Integer.parseInt(dateFoBirth);
        /* 住院天数 */
        long admissionDays = DateUtils.getDiffDay(patVisit.get("ADMISSION_DATE_TIME"), patVisit.get("DISCHARGE_DATE_TIME")) + 1;
        /* 年龄 */
        patientInfo.set("AGE", strAge + "");
        /* 住院号 */
        patientInfo.set("Visit_id", patVisit.get("Visit_id"));
        /* 住院天数 */
        patientInfo.set("ADMISSION_DAYS", admissionDays + "");
        /* 出院科室 */
        patientInfo.set("DISCHARGE_DEPT", patVisit.get("DEPT_DISCHARGE_FROM"));
        /* 出院时间 */
        patientInfo.set("DISCHARGE_DATE_TIME", patVisit.get("DISCHARGE_DATE_TIME"));
        /* 主管医生 */
        patientInfo.set("DOCTOR_IN_CHARGE", patVisit.get("ATTENDING_DOCTOR"));
        /* 感染标志 */
        patientInfo.set("INFECT_INDI", "0"); 
        /* 药敏试验  */
        patientInfo.set("BEFORE_DRUGSENS", "".equals(patVisit.get("BEFORE_DRUGSENS"))?"0":"1");
        /* 医疗费用 */
        patientInfo.set("MED_COSTS", patVisit.get("TOTAL_COSTS"));
    }
    
    /**
     * 将抽取的数据增加到patientInfo中
     * @param addPatientInfoCr
     */
    private void insetPatientInfo(TCommonRecord PatientInfo)
    {           
		try {
    		List<Object> sqlParams = new ArrayList<Object>();
			String sql  = "insert into IAS_PATIENT_INFO(PATIENT_ID,VISIT_ID,SEX,AGE,ADMISSION_DAYS,OPERATION_INDI"
					+ ",OPERATION_HEAL,WOUND_GRADE,DISCHARGE_DEPT,DISCHARGE_DATE_TIME,DOCTOR_IN_CHARGE,INFECT_INDI,USE_BEFORE"
					+ ",USE_MIDDLE,USE_AFTER,BEFORE_GERM,BEFORE_GERM_RES,BEFORE_DRUGSENS,DRUG_DAY,DRUG_KIND,ONE_DRUG,TWO_DRUG"
					+ ",THREE_DRUG, FOUR_DRUG, MOR_DRUG,MAX_DRUG_ADAY,MED_COSTS,DRUG_COSTS,ANTI_COSTS,MEMO, DEPT_NAME, CLINIC_ATTR, OUTP_OR_INP, INTERNAL_OR_SERGERY"
					+ ",ANTI_DAY, ANTI_KIND, " +
					" OPER_USEDRUG_EARLY, OPER_USEDRUG_OK, OPER_USEDRUG_LATER, SPEC_ANTIDRUG, LIMIT_ANTIDRUG, IS_IVOper, DDD_Value, spec_ddd_value, limit_ddd_value," +
					"LINK_DATE)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
			sqlParams.add(PatientInfo.get("PATIENT_ID")              );
			sqlParams.add(PatientInfo.get("VISIT_ID")                );
			sqlParams.add(PatientInfo.get("SEX")                     );
			sqlParams.add(PatientInfo.get("AGE")                     );
			sqlParams.add(PatientInfo.get("ADMISSION_DAYS")          );
			sqlParams.add(PatientInfo.get("OPERATION_INDI")          );
			sqlParams.add(PatientInfo.get("OPERATION_HEAL")          );
			sqlParams.add(PatientInfo.get("WOUND_GRADE")             );
			sqlParams.add(PatientInfo.get("DISCHARGE_DEPT")          );
			Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("DISCHARGE_DATE_TIME"),DateUtils.FORMAT_DATETIME).getTime());
			sqlParams.add(dateTime);
			sqlParams.add(PatientInfo.get("DOCTOR_IN_CHARGE")        );
			sqlParams.add(PatientInfo.get("INFECT_INDI")             );
			sqlParams.add(PatientInfo.get("术前用药")                 );
			sqlParams.add(PatientInfo.get("术中用药")                 );
			sqlParams.add(PatientInfo.get("术后用药")                 );
			sqlParams.add(PatientInfo.get("病原学检测")               );
			sqlParams.add(PatientInfo.get("病原菌检出标志")           );
			sqlParams.add(PatientInfo.get("药敏试验")                 );
			sqlParams.add(PatientInfo.get("DRUG_DAY")                );
			sqlParams.add(PatientInfo.get("DRUG_KIND")               );
			sqlParams.add(PatientInfo.get("ONE_DRUG")                );
			sqlParams.add(PatientInfo.get("TWO_DRUG")                );
			sqlParams.add(PatientInfo.get("THREE_DRUG")              );
			sqlParams.add(PatientInfo.get("FOUR_DRUG")               );
			sqlParams.add(PatientInfo.get("MOR_DRUG")                );
			sqlParams.add(PatientInfo.get("MAX_DRUG_ADAY")           );
			sqlParams.add(PatientInfo.get("MED_COSTS")               );
			sqlParams.add(PatientInfo.get("DRUG_COSTS")              );
			sqlParams.add(PatientInfo.get("ANTI_COSTS")              );
			sqlParams.add(PatientInfo.get("MEMO")                    );
			sqlParams.add(PatientInfo.get("OUT_DEPT_NAME")           );
			sqlParams.add(PatientInfo.get("OUT_CLINIC_ATTR")         );
			sqlParams.add(PatientInfo.get("OUT_OUTP_OR_INP")         );
			sqlParams.add(PatientInfo.get("OUT_INTERNAL_OR_SERGERY") );
			sqlParams.add(PatientInfo.get("ANTI_DAY")                );
			sqlParams.add(PatientInfo.get("ANTI_KIND")               );
			sqlParams.add(PatientInfo.get("手术用药过早")             );
			sqlParams.add(PatientInfo.get("手术用药恰当")             );
			sqlParams.add(PatientInfo.get("手术用药过晚")             );
			sqlParams.add(PatientInfo.get("特殊用药")                 );
			sqlParams.add(PatientInfo.get("限制用药")                 );
			sqlParams.add(PatientInfo.get("介入手术")                 );
			sqlParams.add(StringUtils.formatDouble(PatientInfo.getDouble("DDD_Value"), 4, StringUtils.styleNo￥));
			sqlParams.add(StringUtils.formatDouble(PatientInfo.getDouble("Spec_DDD_Value"), 4, StringUtils.styleNo￥));
			sqlParams.add(StringUtils.formatDouble(PatientInfo.getDouble("Limit_DDD_Value"), 4, StringUtils.styleNo￥));
			dateTime = new Timestamp(DateUtils.getDateFromString(PatientInfo.get("LINK_DATE")).getTime());
			sqlParams.add(dateTime);
					
			query.update(sql,sqlParams.toArray());
		} catch (Exception e) {
			/* 可以记录出错的数据 */
			e.printStackTrace();
		}
    }
    
    public boolean canRun()
    {
    	if (super.canRun())
    	{
    		Calendar cal = Calendar.getInstance();
    		int hour = cal.get(Calendar.HOUR_OF_DAY);
    		JDBCQueryImpl peaasQuery = DBQueryFactory.getQuery("PEAAS");
            @SuppressWarnings("unchecked")
    		List<TCommonRecord> list = peaasQuery.query("select rulecode,rulevalue from ruleparameter where rulecode = 'IASDataFetcherTime'", new CommonMapper());
            peaasQuery = null;
            if ((list != null) && (list.size() > 0))
            {
            	try
            	{
            		TCommonRecord cr = list.get(0);
            		System.out.println("住院IASDataFetcherTime启动时间为：" + cr.get("rulevalue"));
            		return hour == Integer.parseInt(cr.get("rulevalue"));
            	}
            	catch (Exception ex)
            	{
            		ex.printStackTrace();
            	}
            }
    		return false;
    	}
    	return false;
    }

    @MHPerformProp(MethodParam={String.class})
    public void run(String aDate) 
    {
    	try
    	{
    		
    		boolean bool = false;
            if(aDate == null || "".equals(aDate))
            {
                if (!canRun())return;
            }
            else
            {
                bool = true;
            }
            System.out.println(toString() + "开始提取病人病历！！！！！！");
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("prevDate", bool ? aDate : getPrevDate());
    		performTask(map, null);
    		map = null;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
	@Override
	public void performTask(Map<String, String> param, Task AOwner) 
	{
		String prevDate = param.get("prevDate");
		try
    	{
			setOwner(AOwner);
	    	setDebugLevel(Config.getIntParamValue("IASDebugLevel"));
	    	Log(50, "开始提取病人病历信息...");
	    	JDBCQueryImpl hisQuery = DBQueryFactory.getQuery("HIS");
	        getQuery("IAS");
	    	if (getOwner() != null)
	    		getOwner().setTaskStatusDesc("正在抓取" + prevDate + "数据！");
	        FetchData(hisQuery, prevDate);
	    	if (getOwner() != null)
	    		getOwner().setTaskStatusDesc("开始构造报表");
	        BuildReport(prevDate, "IAS", vctLog);
	    	if (getOwner() != null)
	    		getOwner().setTaskStatusDesc("报表构造完成！");
	    	saveLog("APPLOG\\ias\\ias_" + prevDate + ".log");
        	Log(50, "病人病历信息提取结束...");
        	hisQuery = null;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		saveLog("ias_" + prevDate + "_error.log");
    	}
	}
	
	private void clearData(JDBCQueryImpl patQuery, String prevDate) {
		String[] tableNames = new String[]{"Pat_Master_Index","Pat_Visit","Operation_Name","Drug_Dispense_Rec","Pats_In_Hospital",
				"Orders","Diagnosis","Inp_Bill_Detail","Lab_Test_Master","Lab_Test_Items","Lab_Result","Vital_Signs_Rec","Exam_Master",
				"Exam_Items","Exam_Report","germ_test","DRUG_SENSIT_RESULT","GERM_TEST_RESULT"};
		List<String> sqlList = new ArrayList<String>();
		for (String str : tableNames) {
			sqlList.add("delete from " + str + " where LINK_DATE = to_date('" + prevDate + "','yyyy-MM-dd')");
		}
		patQuery.batchUpdate(sqlList.toArray(new String[]{}));
		query.batchUpdate(new String[]{"delete from IAS_PATIENT_USE_ANTIBO where LINK_DATE = to_date('" + prevDate + "','yyyy-MM-dd')",
				"delete from IAS_PATIENT_INFO where LINK_DATE = to_date('" + prevDate + "','yyyy-MM-dd')"});
	}

	@Override
	public void reportStatus(Task owner) {
	}
}