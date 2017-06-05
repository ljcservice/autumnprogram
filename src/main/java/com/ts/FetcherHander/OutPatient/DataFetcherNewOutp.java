package com.ts.FetcherHander.OutPatient;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import com.hitzd.Annotations.MHPerformProp;
import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.Transaction.TransaCallback;
import com.hitzd.Transaction.TransactionTemp;
import com.ts.SchedulerHandler.ModelHandler;

import com.hitzd.his.Scheduler.ReportScheduler;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.his.casehistory.helper.CaseHistoryFactory;
import com.hitzd.his.casehistory.helper.CaseHistoryFunction;
import com.hitzd.his.casehistory.helper.CaseHistoryHelperUtils;
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;
import com.hitzd.springBeanManager.SpringBeanUtil;
import com.ts.service.pdss.IReviewResultServ;
import com.ts.service.pdss.ReviewResultBean;
import com.ts.service.pdss.peaas.manager.IPrescReviewChecker;

/**
 * 处方数据抽取 定时器，每天0点从HIS系统中抽取前一天的所有处方信息， 保存到peaas的处方表里。
 * 
 * @author Administrator
 * 
 */
@Transactional
public class DataFetcherNewOutp extends ReportScheduler  {

	/* 评审结果存储 */
	@Resource(name = "reviewResultService")
	private IReviewResultServ reviewResultService;

	public DataFetcherNewOutp()
	{
		setDebugLevel(50);
	}

	/**
	 * 当前日期推前一天
	 */
	private String getPrevDate() {
		return DateUtils.getCertainDate(DateUtils.getDate(), -1);
	}

	/**
	 * 处方类型 1:军人处方 ,2: 医保处方 ,3:自费处方 ,9 :其他 判断处方类型
	 * 
	 * @param type
	 * @return
	 */
	private static String getPrescType(String type) {
		boolean flag = false;
		String result = "9";
		String JR = Config.getParamValue("JZPRESC"); // 军人处方
		String ZF = Config.getParamValue("ZFPRESC"); // 自费处方
		String YB = Config.getParamValue("YBPRESC"); // 医保处方 
		if (!flag) {
			if (JR.indexOf(type + ",") != -1) {
				result = "1";
				flag = true;
			}
		}
		if (!flag) {
			if (ZF.indexOf(type + ",") != -1) {
				result = "2";
				flag = true;
			}
		}
		if (!flag) {
			if (YB.indexOf(type + ",") != -1) {
				result = "3";
				flag = true;
			}
		}
		return result;
	}

	/**
	 * 门诊病历记录
	 * 
	 * outp_mr(病人标识号,,就诊时间,就诊序号,主诉,既往史,家族史,婚姻史,个人史,月经史,简要病史,查体,诊断,,嘱咐,医生,顺序号,
	 * 手术记录,病程记录),
	 * 
	 * @param hisQuery
	 * @param patient_id
	 * @param visit_no
	 * @param ADate
	 * @return
	 */
	private TCommonRecord getOUTP_MR(JDBCQueryImpl hisQuery, String patient_id,String MrDoctor, String ADate) {
		TCommonRecord tCom = new TCommonRecord();
		ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
		String strFields = "diag_code,diag_desc";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		List<TCommonRecord> lsGroup = new ArrayList<TCommonRecord>();
		List<TCommonRecord> lsOrder = new ArrayList<TCommonRecord>();
		TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("patient_id", patient_id, "Char", "", "", "");
		lsWheres.add(where);
		where = CaseHistoryHelperUtils.genWhereGbkCR("doctor", MrDoctor,"Char", "", "", "");
		lsWheres.add(where);
		where = CaseHistoryHelperUtils.genWhereCR(CaseHistoryFunction.genTrunc("outpdoct.outp_mr", "VISIT_DATE", "")
				,CaseHistoryFunction.genRToDate("outpdoct.outp_mr", "VISIT_DATE", "'" + ADate + "'", "yyyy-mm-dd"), "", "", "", "");
		lsWheres.add(where);
		try {
			long time = System.currentTimeMillis();
			List<TCommonRecord> results = chhr.fetchOutpMr2CR(strFields,lsWheres, lsGroup, lsOrder, hisQuery);
			DictCache dCache = DictCache.getNewInstance();
			String diag_desc = "";
			String diag_code = "";
			for (int i = 0; i < results.size(); i++) {
				TCommonRecord t = results.get(i);
				if (i == 0) {
					diag_desc += t.get("diag_desc").trim();
					if (!"".equals(t.get("diag_code"))) {
						diag_code += t.get("diag_code");
					} else {
						diag_code += dCache.getDiagnosisByName(t.get("diag_desc").trim()).get("diagnosis_code");
					}
				} else {
					diag_desc += ";" + t.get("diag_desc").trim();
					if (!"".equals(t.get("diag_code"))) {
						diag_code += ";" + t.get("diag_code");
					} else {
						diag_code += ";" + dCache.getDiagnosisByName(t.get("diag_desc").trim()).get("diagnosis_code");
					}
				}
			}
			tCom.set("diag_desc", diag_desc);
			tCom.set("diag_code", diag_code);
			System.out.println(" getOUTP_MR()方法 所用时间 "+ (System.currentTimeMillis() - time) + tCom.get("diag_desc") + "," + tCom.get("diag_code"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			chhr = null;
		}
		return tCom;
	}

	/**
	 * 门诊
	 * 
	 * @param hisQuery
	 * @param patient_id
	 * @param visit_no
	 * @param ADate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TCommonRecord getOUTP_MRByLastDiagnosis(JDBCQueryImpl hisQuery,String patient_id, String visit_no, String ADate) {
		long time = System.currentTimeMillis();
		ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
		String strFields = "diag_code,diag_desc,visit_date";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		List<TCommonRecord> lsOrders = new ArrayList<TCommonRecord>();
		TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("patient_id",patient_id, "Char", "", "", "");
		lsWheres.add(where);
		where = CaseHistoryHelperUtils.genWhereCR(CaseHistoryFunction.genTrunc("outpdoct.OUTP_MR", "VISIT_DATE", ""),
				CaseHistoryFunction.genRToDate("outpdoct.OUTP_MR", "VISIT_DATE", "'" + ADate + "'", "yyyy-mm-dd"), "", "<=", "", "");
		lsWheres.add(where);
		where = CaseHistoryHelperUtils.genOrderCR("VISIT_DATE", "DESC");
		lsOrders.add(where);
		
		String sql = chhr.genSQL(strFields, "outpdoct.OUTP_MR", lsWheres, null,lsOrders);
		List<TCommonRecord> results = hisQuery.query(sql, new CommonMapper());
		TCommonRecord tCom = new TCommonRecord();
		DictCache dCache = DictCache.getNewInstance();
		String diag_desc = "";
		String diag_code = "";
		String visit_date = "";
		/**
		 * 写的有问题， 需要改进。真能存在一个查询的结果
		 */
		for (int i = 0; i < results.size(); i++) {
			TCommonRecord t = results.get(i);
			diag_desc += t.get("diag_desc").trim()+";";
			if (!"".equals(t.get("diag_code"))) {
				//直接提取
				diag_code += t.get("diag_code")+";";
			}
			else
			{
				//在cache中找寻
				diag_code += dCache.getDiagnosisByName(t.get("diag_desc").trim()).get("diagnosis_code")+";";
			}
			//逻辑处理，选取一天数据
			if(!("".equals(visit_date) || visit_date.equals(t.get("visit_date")))){break;}
			visit_date = t.get("visit_date");
		}
		if(results.size()>0)
		{
			diag_desc = diag_desc.substring(0,diag_desc.length()-1);
			diag_code = diag_code.substring(0,diag_code.length()-1);
		}
		tCom.set("diag_desc", diag_desc);
		tCom.set("diag_code", diag_code);
		System.out.println(" getOUTP_MRByLastDiagnosis()方法 所用时间 " + (System.currentTimeMillis() - time) + tCom.get("diag_desc") + "," + tCom.get("diag_code"));
		return tCom;
	}

	/**
	 * 获得诊断名称
	 * 
	 * @param PatientId
	 * @param VisitNo
	 * @return
	 */
	private String[] getDiagnosisId(JDBCQueryImpl hisQuery, String PatientId,String VisitNo, String ADate)
	{
		ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
		try 
		{
			List<TCommonRecord> results = chhr.fetchDiagnosis2CR(PatientId, VisitNo, hisQuery);
			StringBuffer diagnosisCode = new StringBuffer();
			StringBuffer diagnosisDesc = new StringBuffer();
			for (TCommonRecord result : results) 
			{
				diagnosisCode.append(result.get("DIAGNOSIS_NO")).append(";");
				diagnosisDesc.append(result.get("DIAGNOSIS_DESC")).append(";");
			}
			if (diagnosisCode.length() > 0)
				diagnosisCode.deleteCharAt(diagnosisCode.length() - 1);
			if (diagnosisDesc.length() > 0)
				diagnosisDesc.deleteCharAt(diagnosisDesc.length() - 1);
			String[] values = new String[2];
			values[0] = diagnosisCode.toString();
			values[1] = diagnosisDesc.toString();
			return values;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			chhr = null;
		}
		return new String[0];
	}

	/**
	 * 住院处方药品记录
	 * 
	 * @param id
	 * @param DrugCode
	 * @param DrugName
	 * @param DrugType
	 * @param Adate
	 * @throws Exception
	 */
	public TCommonRecord Data2InsertSub(String id, String DrugCode,String DrugName, String DrugType, String Adate) throws Exception {
		String sql = "insert into PRESC_DETAIL(PRESC_ID, DRUG_CODE, DRUG_NAME, DRUG_TYPE,PRESCDATE) values (?,?,?,?,?)";
		Object[] sqlParams  = new Object[]{id,DrugCode,DrugName,DrugType,Adate};
		if (query.update(sql,sqlParams) == 0) {
			// 此处要记录失败的语句
			// 并抛出异常，使得本次操作回滚
			Log("处方明细插入失败：" + DrugCode + "__" + DrugName);
			throw new Exception("添加数据出现问题");
		} else {
			// 导入计数器+1
			CounterI2++;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public TCommonRecord getMainRecord(JDBCQueryImpl hisQuery,String PatientID, String VisitID, String OrderNo, String OrderSubNo) {
		String sql = "select * from ordadm.orders where " + "patient_id   = '"
				+ PatientID + "' and " + "visit_id     = '" + VisitID
				+ "' and " + "Order_No     = '" + OrderNo + "' and "
				+ "Order_Sub_No = '" + OrderSubNo + "'    ";
		List<TCommonRecord> crMain = (List<TCommonRecord>) hisQuery.query(sql,new CommonMapper());
		if (crMain.size() == 0) {
			Log("获取医嘱主记录发生错误：" + PatientID + "__" + VisitID + "  记录不存在!");
			return null;
		}
		return crMain.get(0);
	}

	/**
	 * 保存错误信息
	 * 
	 * @param crm
	 * @param ExcSql
	 * @param orderType
	 */
	private void InsertDrawException(TCommonRecord crm, String ExcSql,String orderType) {
		String id = UUID.randomUUID().toString();
		String sql = "insert into exceptiondatainfo(exceptionid,patient_id,visit_id,excdate,exceptionsql,orderType) values(?,?,?,?,?,?)";
		String visit = "";
		if ("1".equals(orderType))
			visit = crm.get("VISIT_NO");
		else
			visit = crm.get("VISIT_ID");
		query.update(sql, new Object[] { id, crm.get("patient_id"), visit,getPrevDate(), ExcSql, orderType });
	}

	/**
	 * 插入draw信息,放入到peaas目录下
	 */
	public void InsertDrawInfo() {
		String id = UUID.randomUUID().toString();
		String FileName = saveLog("APPLOG\\peaas\\peaas_" + prevDate + ".log");
		String sql = "insert into drawinfo(drawid,exccounter,finistcounter,drawdate,path) values(?,?,?,?,?)";
		query.update(sql, new Object[] { id, CounterS1 + CounterS2,CounterI1 + CounterI2, getPrevDate(), FileName });
	}

	/**
	 * 用于流水号使用
	 * 
	 * @return
	 */
	private String getDateId(String ADate) {
		String myDate = getPrevDate();
		if (ADate != null) {
			myDate = ADate;
		}
		return myDate.replace("-", "");
	}

	/**
	 * 
	 * 直接输出自定义流水号 ID 2011111600000001\2011111600000002\2011111600000003
	 * 
	 * @return
	 */
	private String getDecimalFormateID(String ADate, String prescType) {
		counter++;
		DecimalFormat df = new DecimalFormat();
		df.applyPattern("0000000");
		return getDateId(ADate) + prescType + df.format(counter);
	}

	/* 用来计算流水号 计数器 */
	private static long counter = 0;
	// 门诊处方主记录计数器
	private long CounterM1 = 0;
	// 门诊处方药品记录计数器
	private long CounterS1 = 0;
	// 门诊处方药品导入计数器
	private long CounterI1 = 0;
	// 住院处方主记录计数器
	private long CounterM2 = 0;
	// 住院处方药品记录计数器
	private long CounterS2 = 0;
	// 住院处方药品导入计数器
	private long CounterI2 = 0;
	private String prevDate = "";

	public boolean canRun() {
		try {
			if (super.canRun()) {
				Calendar cal = Calendar.getInstance();
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				JDBCQueryImpl peaasQuery = DBQueryFactory.getQuery("PEAAS");
				@SuppressWarnings("unchecked")
				List<TCommonRecord> list = peaasQuery.query("select rulecode,rulevalue from ruleparameter where rulecode = 'PEAASDataFetcherTime'",new CommonMapper());
				peaasQuery = null;
				if ((list != null) && (list.size() > 0)) {
					try {
						TCommonRecord cr = list.get(0);
						System.out.println("PEAASDataFetcher启动时间为："+ cr.get("rulevalue"));
						return hour == Integer.parseInt(cr.get("rulevalue"));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 如果有确切时间 那么就标示人为  选取的时间 
	 * @param aDate
	 */
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
			Log("准备开始处理数据...");
			counter = 0;
			getQuery("PEAAS");
			setDebugLevel(Config.getIntParamValue("IASDebugLevel"));
			JDBCQueryImpl hisQuery = DBQueryFactory.getQuery("HIS");
			Log("数据库连接准备完毕...");
			Log("辅助数据准备完毕...");
			prevDate = bool ? aDate : getPrevDate();
			DeleteOldFetcherDate(aDate);
			Log("提取日期：" + prevDate);
			/* 抓取处置和药品详细信息 */
			if (Config.getParamValue("FetchOutpOrders").equals("true"))
			{
				Log("开始提取门诊处置信息");
				try{
					FetchOutpOrders(hisQuery,prevDate);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			/* 是否开启 提取住院处方 */
			if (Config.getParamValue("FetchDataHospital").equals("true")) {
				Log("开始提取住院处方...");
				try {
					FetchDataHospital(hisQuery, prevDate);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			/* 是否开启 提取门诊处方 */
			if (Config.getParamValue("FetchDataOut").equals("true")) {
				Log("开始提取门诊处方...");
				FetchDataOut(hisQuery, prevDate);
			}
			/* 保存操作日志 */
			Log("提取结束...");
			BuildReport(prevDate, "PEAAS", vctLog);
			InsertDrawInfo();
			if (Config.getParamValue("PrescCheckFlag").equals("true")) {
				Log("对处方进行审核开始....");
				PrescCheck(prevDate);
				Log("对处方进行审核结束!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 抓取门诊处置信息
	 * @param hisQuery query接口
	 * @param ADate 日期
	 */
    public void FetchOutpOrders(JDBCQueryImpl hisQuery, String ADate)
	{
	    /* 使用组件的方式进行处理 在不同情况下的门诊费用明细  */
        long time = System.currentTimeMillis();
        Log("开始提取门诊处置..." + time);
        ModelHandler mh =  new ModelHandler("Outp_Cost_info");
        List<Object> writerParam = new ArrayList<Object>();
        writerParam.add(ADate);
        writerParam.add(hisQuery);
        writerParam.add(query);
        mh.setWriterParam(writerParam);
        mh.run();
        Log("门诊处置提取结束");
        System.out.println("门诊处置提取耗时----："+ (System.currentTimeMillis() - time));
	}
	/**
	 * 获取病人信息
	 * 
	 * @param PatientId
	 * @return
	 */
	private TCommonRecord getPatientDetail(JDBCQueryImpl hisQuery,String PatientId, String ADate, TCommonRecord crm) 
	{
		long time = System.currentTimeMillis();
		ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
		TCommonRecord tCom = new TCommonRecord();
		String strFields = "*";
		try 
		{
		    List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		    TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("patient_id", PatientId, "Char", "", "", "");
		    lsWheres.add(where);
			tCom = chhr.fetchPatInfo2CR(strFields,lsWheres,null,null, hisQuery);
			if (tCom == null) 
			{
				Log("获取病人基本信息错误：" + PatientId);
				tCom = new TCommonRecord();
				tCom.set("name", crm.get("name"));
				tCom.set("charge_type", crm.get("charge_type"));
				tCom.set("pat_age", "0");
				return tCom;
			}
			String dateFoBirth = tCom.get("DATE_OF_BIRTH");
			if (dateFoBirth == null || "".equals(dateFoBirth))
			{
				dateFoBirth = ADate;
			}
			try 
			{
				//计算年龄天数
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date checkDay = sdf.parse(ADate); // 检查天数
				Date birthDay = sdf.parse(dateFoBirth.substring(0,10)); // 检查天数
				String ageStr = "";
				//算天数
				long days = (checkDay.getTime()-birthDay.getTime())/(1000*60*60*24);
				long year = days/365;
				long day  = days%365;
				if(year>=1)
				{
					ageStr = year + "";
				}
				else
				{
					ageStr = "0." + day;
				}
				tCom.set("pat_age",	ageStr);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			System.out.println("病人信息提取 patientID : " + PatientId + "-getPatientDetail() 方法耗时 ：" + (System.currentTimeMillis() - time));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return crm;
		}
		finally 
		{
			chhr = null;

		}
		return tCom;
	}

	/**
	 * 新住院处方
	 * 
	 * @param query
	 * @param ADate
	 */
	public void FetchDataHospital(JDBCQueryImpl hisQuery, String ADate) 
	{
		Log("开始提取住院处方...");
		ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
		String strFields = "*";
		if ("SQLServer".equals(Config.getTableCofig("pharmacy.DRUG_PRESC_MASTER").getDbName()))
		{
			strFields = "CHARGE_TYPE,UNIT_IN_CONTRACT,PRESC_TYPE,PRESC_ATTR,PRESC_SOURCE,"
					+ "REPETITION,COSTS,PAYMENTS,ORDERED_BY,PRESCRIBED_BY,PRESCRIBED_BY,ENTERED_BY,"
					+ "DISPENSING_PROVIDER,PRESC_DATE,PRESC_NO,DISPENSARY,PATIENT_ID,"
					+ "NAME,NAME_PHONETIC,VISIT_NO,'IDENTITY'";
		}
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		List<TCommonRecord> lsGroup = new ArrayList<TCommonRecord>();
		List<TCommonRecord> lsOrder = new ArrayList<TCommonRecord>();
		try {
			TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("PRESC_SOURCE", "1", "Char", "", "", "");
			lsWheres.add(where);
			where = CaseHistoryHelperUtils.genWhereCR("PRESC_DATE", CaseHistoryFunction.genRToDate("pharmacy.DRUG_PRESC_MASTER", "PRESC_DATE", "'" + ADate + "'", "yyyy-mm-dd"), 
					"", ">=", "", "");
			lsWheres.add(where);
			where = CaseHistoryHelperUtils.genWhereCR("PRESC_DATE"
                    ,CaseHistoryFunction.genRToDate("pharmacy.DRUG_PRESC_MASTER", "PRESC_DATE", "'" + DateUtils.getDateAdded(1,ADate) + "'","yyyy-MM-dd") 
                    , "", "<", "", "");
			lsWheres.add(where);
			TCommonRecord order = CaseHistoryHelperUtils.genOrderCR("patient_id", "");
			lsOrder.add(order);
			List<TCommonRecord> list = ichh.fetchDrugPrescMaster2CR(strFields,lsWheres, lsGroup, lsOrder, hisQuery);
			CounterM2 = list.size();
			Log(ADate + "共有处方：" + CounterM2 + "个");
			int counter = 1;
			for (TCommonRecord tCom : list) {
				try {
					Log("开始提取住院处方：" + tCom.get("presc_No") + " 导入处方日: " + ADate+ " 导入为:" + counter + "/" + CounterM2);
					double Cost = tCom.getDouble("costs");
					if (Cost < 0) {
						continue;
					}
					/* 整理住院 处方并添加 */
					FetchDataHospitalDetail(tCom, hisQuery, ADate);
					counter++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ichh = null;
			Log("住院处方提取结束，总计药品总数：" + CounterS2 + ",导入：" + CounterI2 + ";处方主记录："+ CounterM2);
		}
	}

	/**
	 * 住院处方用药详细
	 * 
	 * @param tCom
	 * @param hisQuery
	 * @param ADate
	 * @param idx
	 */
	private void FetchDataHospitalDetail(final TCommonRecord crm,final JDBCQueryImpl hisQuery, final String ADate) {

		TransactionTemp tt = new TransactionTemp("PEAAS");
		TCommonRecord parmComm = new TCommonRecord();
		tt.execute(new TransaCallback(parmComm) {
			@Override
			public void ExceuteSqlRecord() {
				String sql = null;
				HashMap<String, String> pzMap = new HashMap<String, String>();
				int impDrugCount = 0;
				/* 处方关联 id */
				String id = getDecimalFormateID(ADate, "2");
				// 药物品种数
				int DrugCount = 0;
				// 基本药品数
				int baseDrugCount = 0;
				// 是否有抗菌药
				boolean HasKJ = false;
				// 是否有注射剂
				boolean HasZS = false;
				// 中药注射剂
				boolean CDZS = false;
				// 检查处方中是否有二类精神药物
				boolean ELJSY = false;
				// 检查处方中 是否毒性药品
				boolean DDrug = false;
				// 检查处方中 是否麻醉性药品
				boolean MDrug = false;
				// 检查处方中 是否放射性药品
				boolean FSDrug = false;
				// 检查处方中 是否一类精神药品
				boolean YLJSY = false;
				// 检查处方中 是否 贵重药品
				boolean GZDrug = false;
				// 检查处方中 毒麻药品
				boolean DMDrug = false;
				// 检查处方中 是否有 外用抗菌药物
				boolean ExteDrug = false;
				/* 总费用 */
				float DrugCosts = 0f;
				/* 发药药房 */
				String DISPENSARY = "";
				/* 中间层查询 处方明细 */
				String strFields = "*";
				List<TCommonRecord> lsGroup = new ArrayList<TCommonRecord>();
				List<TCommonRecord> lsOrder = new ArrayList<TCommonRecord>();
				List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
				TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("presc_no", crm.get("Presc_No"), "Char", "", "", "");
				lsWheres.add(where);
				where = CaseHistoryHelperUtils.genWhereCR("PRESC_DATE"
				        , CaseHistoryFunction.genRToDate("pharmacy.drug_presc_detail", "PRESC_DATE", "'" + ADate + "'", "yyyy-mm-dd"), "", ">=", "", "");
				lsWheres.add(where);
				where = CaseHistoryHelperUtils.genWhereCR("PRESC_DATE"
				        ,CaseHistoryFunction.genRToDate("pharmacy.drug_presc_detail", "PRESC_DATE", "'" + DateUtils.getDateAdded(1,ADate) + "'", "yyyy-mm-dd")
				        , "", "<", "", "");
				lsWheres.add(where);
				ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
				List<TCommonRecord> crsS = new ArrayList<TCommonRecord>();
				try 
				{
					crsS = chhr.fetchDrugPrescDetail2CR(strFields, lsWheres,lsGroup, lsOrder, hisQuery);
				} catch (Exception e) 
				{
					e.printStackTrace();
					throw new RuntimeException("中间层查询错误");
				} 
				finally
				{
					chhr = null;
					lsGroup = null;
					lsOrder = null;
					lsWheres = null;
				}
				CounterS2 += crsS.size();
				if (crsS.size() == 0)
					return;
				Log(40,	"处方日期:" + crm.get("PRESC_DATE") + ","+ crm.get("PRESC_NO") + "下共有药品信息" + crsS.size()+ "个");
				for (TCommonRecord crs : crsS) {
					/* 临时处理 */
					tmpDataDisposal(crs);
					/* 药品代码 */
					String drug = crs.get("DRUG_CODE");
					String DrugToxiProperty = DrugUtils.getDrugToxiProperty(drug, crs.get("Drug_Spec"));
					// 国家基药
					if (DrugUtils.isCountryBase(drug, crs.get("Drug_Spec")))
						baseDrugCount++;
					// 判断二类精神药物
					if (!ELJSY)
						ELJSY = DrugToxiProperty.indexOf("精二") >= 0;
					// 判断毒性药品
					if (!DDrug)
						DDrug = DrugToxiProperty.indexOf("毒药") >= 0;
					// 判断麻醉药品
					if (!MDrug)
						MDrug = DrugToxiProperty.indexOf("麻药") >= 0;
					// 判断放射药品
					if (!FSDrug)
						FSDrug = DrugToxiProperty.indexOf("放射") >= 0;
					// 判断一类精神药品
					if (!YLJSY)
						YLJSY = DrugToxiProperty.indexOf("精一") >= 0;
					// 判断贵重药品
					if (!GZDrug)
						GZDrug = DrugToxiProperty.indexOf("贵重") >= 0;
					// 判断毒麻药品
					if (!DMDrug)
						DMDrug = DrugToxiProperty.indexOf("毒麻") >= 0;
					// 判断是否为 外用抗菌药
					if (!ExteDrug)
						ExteDrug = DrugUtils.isExternalDrug(drug,crs.get("DRUG_Spec"));
					/* 药品类型 */
					String drugType = DrugUtils.getDrugType(crs.get("DRUG_CODE"), crs.get("DRUG_Spec"),crs.get("ADMINISTRATION"));
					List<String> sqlParams = new ArrayList<String>();
					sql = "insert into PRESC_DETAIL(PRESC_ID, DRUG_CODE, DRUG_NAME, DRUG_TYPE,PRESCDATE,ITEM_CLASS,DRUG_SPEC,"
							+ "FIRM_ID,UNITS,AMOUNT,DOSAGE,DOSAGE_UNITS,ADMINISTRATION,FREQUENCY,ORDER_NO,ORDER_SUB_NO,DISPENSARY,FREQ_DETAIL,ToxiProperty,Costs,CHARGES,"
							+ "PACKAGE_SPEC,CENTERDRUGZS,ANTIDRUGFLAG,NORMDRUGZS,is_cbdrug,presc_no,ORDER_TYPE) "
							+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					sqlParams.add(id); 
					sqlParams.add(drug); 
					sqlParams.add(crs.get("DRUG_NAME")); 
					sqlParams.add(drugType); 
					sqlParams.add(ADate); 
					sqlParams.add(getDrugItemType(drug)); 
					sqlParams.add(crs.get("DRUG_SPEC")); // 药品规格
					sqlParams.add(crs.get("FIRM_ID")); 
					sqlParams.add(crs.get("PACKAGE_UNITS")); // 单位
					sqlParams.add(crs.get("QUANTITY")); // 数量
					sqlParams.add(crs.get("dosage_each")); // 单次剂量
					sqlParams.add(crs.get("dosage_units")); // 剂量单位
					sqlParams.add(crs.get("ADMINISTRATION")); 
					sqlParams.add(crs.get("FREQUENCY")); 
					sqlParams.add(crs.get("ORDER_NO")); 
					sqlParams.add(crs.get("ORDER_SUB_NO")); 
					sqlParams.add(("".equals(crs.get("DISPENSARY")) ? crm.get("DISPENSARY") : crs.get("DISPENSARY"))); // 发药药房
					sqlParams.add(crs.get("FREQ_DETAIL")); 
					sqlParams.add(DrugToxiProperty); 
					sqlParams.add(crs.get("Costs")); 
					sqlParams.add(crs.get("PAYMENTS")); 
					sqlParams.add(crs.get("PACKAGE_SPEC")); // 包装规格
					sqlParams.add((DrugUtils.isCenterDrugZS(crs.get("drug_code"),crs.get("DRUG_Spec")) ? "1" : "0")); // 中药注射剂
					sqlParams.add((DrugUtils.isKJDrug(crs.get("drug_code")) ? "1": "0")); // 抗菌药物
					sqlParams.add((DrugUtils.isZSDrug(crs.get("drug_code"),crs.get("DRUG_Spec")) ? "1" : "0")); // 标准注射
					sqlParams.add((DrugUtils.isCountryBase(drug,crs.get("drug_spec")) ? "1" : "0")); // 国家基本
					sqlParams.add(crm.get("presc_no")); 
					sqlParams.add(2+ "");    
					
					DISPENSARY = crs.get("DISPENSARY");
					DrugCosts += crs.getDouble("Costs");
					impDrugCount++;
					if (query.update(sql,sqlParams.toArray()) == 0) {
						// 此处要记录失败的语句
						// 并抛出异常，使得本次操作回滚
						InsertDrawException(crm, sql, "1");
						Log("处方明细插入失败：" + crs.get("DRUG_CODE") + "__"+ crs.get("DRUG_NAME"));
						// throw new Exception("添加数据出现问题");
					} else {
						// 导入计数器+1
						CounterI2++;
					}
					if (drug.length() >= Config.getIntParamValue("PZ"))
						drug = drug.substring(0, Config.getIntParamValue("PZ"));
					pzMap.put(drug, null);
					// 判断抗菌药
					if (!HasKJ)HasKJ = DrugUtils.isKJDrug(crs.get("DRUG_CODE"));
					// 判断注射剂
					if (!HasZS)HasZS = DrugUtils.isZSDrug(crs.get("drug_code"),crs.get("DRUG_Spec"));
					// 中药注射剂
					if (!CDZS)CDZS = DrugUtils.isCenterDrugZS(crs.get("drug_code"),crs.get("DRUG_Spec"));
				}
				/* 药品种数 */
				DrugCount = pzMap.size();
				Log(40, "共导入药品信息" + impDrugCount + "个;药物品种数" + DrugCount + ";基本药物品种数:" + baseDrugCount + ";抗菌药:"+ (HasKJ ? "有" : "无") + ";注射剂:" + (HasZS ? "有" : "无"));
				/* 组织诊断信息 */
				String visit_id = crm.get("visit_id");
				if ("".equals(visit_id)) {
					/* 获得住院号 */
					visit_id = getLastPatVisit(hisQuery, crm.get("patient_id"));
				}
				String[] values = getDiagnosisId(hisQuery,crm.get("PATIENT_ID"), visit_id, ADate);
				/* 病历记录 */
				String dt = crm.get("PRESC_DATE");
				dt = dt.substring(0, 10);
				/* 病人基本信息 */
				TCommonRecord tCom = getPatientDetail(hisQuery,crm.get("PATIENT_ID"), ADate, crm);
				/* 处方类型 (费别) */
				String prescType = getPrescType(tCom.get("CHARGE_TYPE"));
				String patAge = tCom.get("pat_age");
				List<String> sqlParams = new ArrayList<String>();
				sql = "insert into PRESC(ID, PATIENT_ID, PATIENT_AGE, VISIT_NO, ORG_CODE, "
						+ "ORG_NAME, DOCTOR_CODE, DOCTOR_NAME, ORDER_DATE, AMOUNT, DIAGNOSIS_CODES, "
						+ "DIAGNOSIS_NAMES, Drug_Count, BaseDrug_Count, HasKJ, HasZS,CDZS, ELJSY,DDRUG,MDrug,FSDrug,YLJSY,GZDrug,DMDrug,ExteDrug,PRESCTYPE,"
						+ "PRESCTYPENAME,PATIENT_NAME,PATIENT_SEX,PATIENT_BIRTH,CHARGES,DISPENSARY,DISPENSARYNAME,IDENTITY,presc_no,ORDER_TYPE) "
						+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        sqlParams.add(id );                                                                     // ID
						sqlParams.add(crm.get("PATIENT_ID") );                                                     // PATIENT_ID 病人id
						sqlParams.add(patAge );                                                                    // PATIENT_AGE 病人年龄
						sqlParams.add(crm.get("VISIT_NO") );                                                        // VISIT_NO 就诊id
						sqlParams.add(crm.get("ORDERED_BY") );                                                      // ORG_CODE 诊断部门代码
						sqlParams.add(DictCache.getNewInstance().getDeptName(crm.get("ORDERED_BY")) );              // ORG_NAME 诊断部门名称
						sqlParams.add(DictCache.getNewInstance().getDoctorCode(hisQuery,crm.get("PRESCRIBED_BY"))); // DOCTOR_CODE 医生代码
						sqlParams.add(crm.get("PRESCRIBED_BY") );                                                   // DOCTOR_NAME 医生名称
						sqlParams.add(dt );                                                                         // ORDER_DATE 医嘱日期
						sqlParams.add(("".equals(crm.get("Costs")) ? DrugCosts : crm.get("Costs")) +"");               // AMOUNT 总计花费
						sqlParams.add(values[0] );                                                                  // DIAGNOSIS_CODES 诊断代码
						sqlParams.add(values[1] );                                                                  // DIAGNOSIS_NAMES 诊断名称
						sqlParams.add(DrugCount + "");                                                                 // 品种数
						sqlParams.add(baseDrugCount + "");                                                    // 基本药品种数
						sqlParams.add((HasKJ ? "1" : "0")    );                                                     // 是否有抗菌药
						sqlParams.add((HasZS ? "1" : "0")    );                                                     // 是否有注射剂
						sqlParams.add((CDZS ? "1" : "0")     );                                                     // 是否有中药注射剂
						sqlParams.add((ELJSY ? "1" : "0")    );                                                     // 是否有二类精神药物
						sqlParams.add((DDrug ? "1" : "0")    );                                                     // 是否有毒性药物
						sqlParams.add((MDrug ? "1" : "0")    );                                                     // 是否有麻醉药品
						sqlParams.add((FSDrug ? "1" : "0")   );                                                     // 是否有放射药品
						sqlParams.add((YLJSY ? "1" : "0")    );                                                     // 是否有一类精神药品
						sqlParams.add((GZDrug ? "1" : "0")   );                                                     // 是否有贵重药品
						sqlParams.add((DMDrug ? "1" : "0")   );                                                     // 是否有毒麻药品
						sqlParams.add((ExteDrug ? "1" : "0") );                                                     // 是否有外用药品
						sqlParams.add(prescType);                                                            // 处方类型 1:军人处方 ,2: 医保处方 ,3:自费处方 ,9 :其他
						sqlParams.add(crm.get("CHARGE_TYPE") );                                                     // 费别名字
						sqlParams.add(tCom.get("NAME") );                                                           // 病人名称
						sqlParams.add(tCom.get("SEX") );                                                            // 病人性别
						sqlParams.add(tCom.get("DATE_OF_BIRTH") );                                                  // 病人出生日期
						sqlParams.add(crm.get("PAYMENTS") );                                                        // 实际收取费用
						sqlParams.add(("".equals(crm.get("DISPENSARY")) ? DISPENSARY : crm.get("DISPENSARY")) );    // 发药药局
						sqlParams.add(DictCache.getNewInstance().getDeptName(crm.get("DISPENSARY"))  );            // 发药药局
						sqlParams.add(("".equals(crm.get("IDENTITY"))?tCom.get("IDENTITY"):crm.get("IDENTITY")) ); // 病人身份
						sqlParams.add(crm.get("presc_no") ); 
						sqlParams.add("2");                                  // ORDER_TYPE // 医嘱类型，住院or门诊                           // ORDER_TYPE // 医嘱类型，住院or门诊
						
				if (query.update(sql,sqlParams.toArray()) == 0)
				{
					// 此处要记录失败的语句
					// 并抛出异常，使得本次操作回滚
					Log("住院处方记主录插入失败：" + crm.get("PATIENT_ID") + "__" + crm.get("VISIT_NO") + "__" + crm.get("Serial_No"));
					// InsertDrawException(crm, sql, "1");
				} else 
				{
					// 导入计数器+1
					CounterI2++;
				}
				pzMap = null;
			}
		});
	}

	/**
	 * 返回住院visit_ID号.
	 * @param hisQuery
	 * @param patientId
	 * @return
	 */
	private String getLastPatVisit(JDBCQueryImpl hisQuery, String patientId) 
	{
		TCommonRecord tCom = null;
		ICaseHistoryHelper ichh = CaseHistoryFactory.getCaseHistoryHelper();
		String strFields = "*";
		List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
		List<TCommonRecord> lsGroup = new ArrayList<TCommonRecord>();
		List<TCommonRecord> lsOrder = new ArrayList<TCommonRecord>();
		try 
		{
			TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("patient_id", patientId, "Char", "", "", "");
			lsWheres.add(where);
			TCommonRecord order = CaseHistoryHelperUtils.genOrderCR("visit_id","desc");
			lsOrder.add(order);
			List<TCommonRecord> list = ichh.fetchPatVisitInfo(strFields,lsWheres, lsGroup, lsOrder, hisQuery);
			tCom = list.size() > 0 ? list.get(0) : new TCommonRecord();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return tCom.get("Visit_id");
	}

	/**
	 * 
	 * 用于 判断 概要是 属于何类型药品 (中药 中成药 西药 ) 发现字段表中有相应的字段描述
	 * 
	 * @param drugCode
	 * @return
	 */
	private String getDrugItemType(String drugCode) {
		DictCache d = DictCache.getNewInstance();
		TCommonRecord drug = d.getDrugDictInfo(drugCode);
		String indicator = drug.get("drug_indicator");
		if ("1".equals(indicator)) {
			return "A";
		} else if ("3".equals(indicator)) {
			return "B";
		} else if ("2".equals(indicator)) {
			return "C";
		}
		return "";

	}

	/**
	 * 新门诊处方
	 * 
	 * @param query
	 * @param ADate
	 */
	public void FetchDataOut(JDBCQueryImpl hisQuery, String ADate) {
		long time = System.currentTimeMillis();
		Log("开始提取门诊处方..." + time);
		try {

			StringBuffer sql = new StringBuffer();
            sql.append("select * from (" + Config.getParamValue("view_outp_orders") + ")  where PRESC_SOURCE = 0 and PRESC_DATE >= to_date('").append(ADate).append("','yyyy-mm-dd') ");
            sql.append(" and presc_date < to_date('").append(DateUtils.getDateAdded(1, ADate)).append("','yyyy-mm-dd')");
            sql.append(" order by patient_id ");
			@SuppressWarnings("unchecked")
			List<TCommonRecord> list = hisQuery.query(sql.toString(), new CommonMapper());
			CounterM1 = list.size();
			Log(ADate + "共有门诊处方：" + CounterM1 + "个");
			int counter = 1;
			for (TCommonRecord tCom : list) {
				try {
					long time1 = System.currentTimeMillis();
					Log("开始提取门诊处方：" + tCom.get("presc_No") + " 导入处方日: " + ADate + " 导入处方数: " + counter + "/" + CounterM1);
					/* 整理门诊 处方并添加 */
					FetchDataOutDetail(tCom, hisQuery, ADate);
					System.out.println("门诊处方提取耗时：" + (System.currentTimeMillis() - time1));
					counter++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Log("门诊处方提取结束，总计药品总数：" + CounterS1 + ",导入：" + CounterI1 + ";处方主记录："+ CounterM1);
		}
		System.out.println("门诊所有处方提取耗时----："+ (System.currentTimeMillis() - time));
	}

	/**
	 * 住门诊处方用药详细
	 * 
	 * @param tCom
	 * @param hisQuery
	 * @param ADate
	 * @param idx
	 */
	@SuppressWarnings("unchecked")
	private void FetchDataOutDetail(final TCommonRecord crm,
			final JDBCQueryImpl hisQuery, final String ADate) {
		TransactionTemp tt = new TransactionTemp("PEAAS");
		tt.execute(new TransaCallback() {
			@Override
			public void ExceuteSqlRecord() {
				JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
				// DecimalFormat d = new DecimalFormat("#0.00");
				//给药途径  静脉输液标示
                String[] adminName = Config.getParamValue("IntravenousInfusion").split(",");
				String sql = null;
				HashMap<String, String> pzMap = new HashMap<String, String>();
				int impDrugCount = 0;
				/* 处方关联 id */
				String id = getDecimalFormateID(ADate, "2");
				// 药物品种数
				int DrugCount = 0;
				// 基本药品数
				int baseDrugCount = 0;
				// 是否有抗菌药
				boolean HasKJ = false;
				// 是否有注射剂
				boolean HasZS = false;
				// 中药注射剂
				boolean CDZS = false;
				// 检查处方中是否有二类精神药物
				boolean ELJSY = false;
				// 检查处方中 是否 毒性药品
				boolean DDrug = false;
				// 检查处方中 是否 麻醉性药品
				boolean MDrug = false;
				// 检查处方中 是否 放射性药品
				boolean FSDrug = false;
				// 检查处方中 是否 一类精神药品
				boolean YLJSY = false;
				// 检查处方中 是否 贵重药品
				boolean GZDrug = false;
				// 检查处方中 毒麻药品
				boolean DMDrug = false;
				// 检查处方中 是否有外用
				boolean ExteDrug = false;
				//静脉输液
                boolean adminJMSY = false;
                //抗菌药物 静脉输液 
                boolean adminAntiJMSY = false;
				/* 总费用 */
				float DrugCosts = 0f;
				/* 抗菌药药物费用 */
				float AntiDrugCosts = 0f ;
				/* 发药药房 */
				String DISPENSARY = "";
				long x = System.currentTimeMillis();
				/* 从中间层获取处方详细信息 */
				List<TCommonRecord> crsS = new ArrayList<TCommonRecord>();
				try 
				{
				    sql = "select * from (" + Config.getParamValue("view_outp_presc") + ") where presc_no = '" + crm.get("Presc_no") + "' "
				            + " and PRESC_DATE >= to_date('" + ADate + "','yyyy-mm-dd')"
				            + " and presc_date < to_date('" + DateUtils.getDateAdded(1, ADate) + "','yyyy-mm-dd')" ;
					crsS = hisQuery.query(sql, new CommonMapper());
					
				}
				catch (Exception e) 
				{
					e.printStackTrace();
					throw new RuntimeException("中间层查询错误");
				} finally
				{
					
				}
				System.out.println("--查询详细处方 耗时 ： "+ (System.currentTimeMillis() - x));
				CounterS1 += crsS.size();
				if (crsS.size() == 0) return;
				Log(40,"处方日期:" + crm.get("PRESC_DATE") + "," + crm.get("PRESC_NO") + "下共有药品信息" + crsS.size() + "个");
				for (TCommonRecord crs : crsS) 
				{
					/* 临时处理（用于北京军总数据整理） */
					tmpDataDisposal(crs);
					/* 药品代码 */
					String drug = crs.get("DRUG_CODE");
					String DrugToxiProperty = DrugUtils.getDrugToxiProperty(drug, crs.get("Drug_Spec"));
					if (DrugUtils.isCountryBase(drug, crs.get("Drug_Spec")))
						baseDrugCount++;
					// 判断二类精神药物
					if (!ELJSY)
						ELJSY = DrugToxiProperty.indexOf("精二") >= 0;
					// 判断毒性药品
					if (!DDrug)
						DDrug = DrugToxiProperty.indexOf("毒药") >= 0;
					// 判断麻醉药品
					if (!MDrug)
						MDrug = DrugToxiProperty.indexOf("麻药") >= 0;
					// 判断放射药品
					if (!FSDrug)
						FSDrug = DrugToxiProperty.indexOf("放射") >= 0;
					// 判断一类精神药品
					if (!YLJSY)
						YLJSY = DrugToxiProperty.indexOf("精一") >= 0;
					// 判断贵重药品
					if (!GZDrug)
						GZDrug = DrugToxiProperty.indexOf("贵重") >= 0;
					// 判断毒麻药品
					if (!DMDrug)
						DMDrug = DrugToxiProperty.indexOf("毒麻") >= 0;
					// 判断是否为外用药物
					if (!ExteDrug)
						ExteDrug = DrugUtils.isExternalDrug(drug,crs.get("DRUG_Spec"));
					/* 基本类型 - 可能不精确 */
					String drugType = DrugUtils.getDrugType(crs.get("DRUG_CODE"), crs.get("DRUG_Spec"),crs.get("ADMINISTRATION"));
					List<String> sqlParams = new ArrayList<String>();
					sql = "insert into PRESC_DETAIL(PRESC_ID, DRUG_CODE, DRUG_NAME, DRUG_TYPE,PRESCDATE,ITEM_CLASS,DRUG_SPEC,"
							+ "FIRM_ID,UNITS,AMOUNT,DOSAGE,DOSAGE_UNITS,ADMINISTRATION,FREQUENCY,ORDER_NO,ORDER_SUB_NO,DISPENSARY,FREQ_DETAIL,"
							+ "ToxiProperty,Costs,CHARGES,PACKAGE_SPEC,CENTERDRUGZS,ANTIDRUGFLAG,NORMDRUGZS,is_cbdrug,presc_no,ORDER_TYPE) "
							+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					sqlParams.add(id); 
					sqlParams.add(drug); 
					sqlParams.add(crs.get("DRUG_NAME")); 
					sqlParams.add(drugType); 
					sqlParams.add(ADate); 
					sqlParams.add(getDrugItemType(drug)); 
					sqlParams.add(crs.get("DRUG_SPEC"));                 // 药品规格
					sqlParams.add(crs.get("FIRM_ID")); 
					sqlParams.add(crs.get("PACKAGE_UNITS"));             // 单位
					sqlParams.add(crs.get("QUANTITY"));                  // 数量
					sqlParams.add(crs.get("dosage_each"));               // 单次剂量
					sqlParams.add(crs.get("dosage_units"));              // 剂量单位
					sqlParams.add(crs.get("ADMINISTRATION")); 
					sqlParams.add(crs.get("FREQUENCY")); 
					sqlParams.add(crs.get("ORDER_NO")); 
					sqlParams.add(crs.get("ORDER_SUB_NO")); 
					sqlParams.add(crm.get("DISPENSARY")); 
					sqlParams.add(crs.get("FREQ_DETAIL")); 
					sqlParams.add(DrugToxiProperty); 
					sqlParams.add(crs.get("Costs")); 
					sqlParams.add(crs.get("PAYMENTS")); 
					sqlParams.add(crs.get("PACKAGE_SPEC"));                                                           // 包装规格
					sqlParams.add((DrugUtils.isCenterDrugZS(crs.get("drug_code"),crs.get("DRUG_Spec")) ? "1" : "0")); // 中药注射剂
					sqlParams.add((DrugUtils.isKJDrug(crs.get("drug_code")) ? "1": "0"));                             // 抗菌药物
					sqlParams.add((DrugUtils.isZSDrug(crs.get("drug_code"),crs.get("DRUG_Spec")) ? "1" : "0"));       // 标准注射
					sqlParams.add((DrugUtils.isCountryBase(drug,crs.get("Drug_Spec")) ? "1" : "0"));                   // 国家基本药物
					sqlParams.add(crm.get("presc_no")); 
					sqlParams.add(1+ ""); 
					
					impDrugCount++;
					DISPENSARY = crs.get("DISPENSARY");
					DrugCosts += crs.getDouble("Costs");
					query.update(sql,sqlParams.toArray());
					CounterI1++;
					if (drug.length() >= Config.getIntParamValue("PZ"))
						drug = drug.substring(0, Config.getIntParamValue("PZ"));
					pzMap.put(drug, null);
					// 判断抗菌药
					if (!HasKJ)	HasKJ = DrugUtils.isKJDrug(crs.get("DRUG_CODE"));
					// 判断注射剂
					if (!HasZS)	HasZS = DrugUtils.isZSDrug(crs.get("drug_code"),crs.get("DRUG_Spec"));
					// 中药注射剂
					if (!CDZS)  CDZS = DrugUtils.isCenterDrugZS(crs.get("drug_code"),crs.get("DRUG_Spec"));
					/* 抗菌药费 */
					if(DrugUtils.isKJDrug(crs.get("drug_code"))) AntiDrugCosts += crs.getDouble("Costs");
					for(String s :adminName)
                    {
                        if(crs.get("administration").contains(s))
                        {
                            adminJMSY = true ;
                            if(HasKJ){
                                adminAntiJMSY = true;
                            }
                            break;
                        }
                    } 
				}
				/* 药品种数 */
				DrugCount = pzMap.size();
				Log(40, "共导入药品信息" + impDrugCount + "个;药物品种数" + DrugCount + ";基本药物品种数:" + baseDrugCount + ";抗菌药:" + (HasKJ ? "有" : "无") + ";注射剂:" + (HasZS ? "有" : "无"));
				
				/**
				 * 组织诊断信息     =========== 采用三层结果查询结果
				 * 1. 采用 病人，医生，日期；
				 * 2. 采用visit_no, 病人， 日期；
				 * 3. 采用visit_no, 病人， 最近日期；
				 */
				TCommonRecord diag = getOUTP_MR(hisQuery,crm.get("patient_id"), crm.get("PRESCRIBED_BY"), ADate);
				if ("".equals(diag.get("diag_code")))
				{
					diag = getOUTP_MRByLastDiagnosis(hisQuery, crm.get("patient_id"),crm.get("visit_no"), ADate);
				}
				String dt = crm.get("PRESC_DATE");
				dt = dt.substring(0, 10);
				/* 病人基本信息 */
				TCommonRecord tCom = getPatientDetail(hisQuery,crm.get("PATIENT_ID"), ADate, crm);
				/* 处方类型 (费别) */
				String prescType = getPrescType(tCom.get("CHARGE_TYPE"));
				String patAge = tCom.get("pat_age");
				List<String> sqlParams = new ArrayList<String>();
				sql = "insert into PRESC"
						+ "(ID, PATIENT_ID, PATIENT_AGE, VISIT_NO, ORG_CODE, ORG_NAME, DOCTOR_CODE, DOCTOR_NAME, ORDER_DATE, AMOUNT, DIAGNOSIS_CODES, "
						+ "DIAGNOSIS_NAMES, Drug_Count, BaseDrug_Count, HasKJ, HasZS, CDZS,ELJSY,DDRUG,MDrug,FSDrug,YLJSY,GZDrug,DMDrug,ExteDrug,PRESCTYPE,"
						+ "PRESCTYPENAME,PATIENT_NAME,PATIENT_SEX,PATIENT_BIRTH,CHARGES,DISPENSARY,DISPENSARYNAME,IDENTITY,presc_no,ORDER_TYPE,allcosts,ANTIDRUGCOSTS,ADMINJMSY,ADMINANTIJMSY) "
						+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				sqlParams.add(id);                                                                                // ID
				sqlParams.add(crm.get("PATIENT_ID"));                                                             // PATIENT_ID 病人id
				sqlParams.add(patAge);                                                                            // PATIENT_AGE 病人年龄
				sqlParams.add(crm.get("VISIT_NO"));                                                               // VISIT_NO 就诊id
				sqlParams.add(crm.get("ORDERED_BY"));                                                             // ORG_CODE 诊断部门代码
				sqlParams.add(DictCache.getNewInstance().getDeptName(crm.get("ORDERED_BY")));                     // ORG_NAME 诊断部门名称
				sqlParams.add(DictCache.getNewInstance().getDoctorCode(hisQuery,crm.get("PRESCRIBED_BY")));       // DOCTOR_CODE 医生代码
				sqlParams.add(crm.get("PRESCRIBED_BY"));                                                          // DOCTOR_NAME 医生名称
				sqlParams.add(dt);                                                                                // ORDER_DATE 医嘱日期
				sqlParams.add(("".equals(crm.get("Costs")) ? DrugCosts : crm.get("Costs")) + "");                 // AMOUNT 总计花费
				sqlParams.add(diag.get("diag_code"));                                                             // DIAGNOSIS_CODES 诊断代码
				sqlParams.add(diag.get("diag_desc"));                                                             // DIAGNOSIS_NAMES 诊断名称
				sqlParams.add(DrugCount + "");                                                                    // 品种数
				sqlParams.add(baseDrugCount + "");                                                                // 基本药品种数
				sqlParams.add((HasKJ ? "1" : "0"));                                                               // 是否有抗菌药
				sqlParams.add((HasZS ? "1" : "0"));                                                               // 是否有注射剂
				sqlParams.add((CDZS ? "1" : "0"));                                                                // 是否有中药注射剂
				sqlParams.add((ELJSY ? "1" : "0"));                                                               // 是否有二类精神药物
				sqlParams.add((DDrug ? "1" : "0"));                                                               // 是否有毒性药物
				sqlParams.add((MDrug ? "1" : "0"));                                                               // 是否有麻醉药品
				sqlParams.add((FSDrug ? "1" : "0"));                                                              // 是否有放射药品
				sqlParams.add((YLJSY ? "1" : "0"));                                                               // 是否有一类精神药品
				sqlParams.add((GZDrug ? "1" : "0"));                                                              // 是否有贵重药品
				sqlParams.add((DMDrug ? "1" : "0"));                                                              // 是否有毒麻药品
				sqlParams.add((ExteDrug ? "1" : "0"));                                                            // 是否有外用药品
				sqlParams.add(prescType);                                                                         // 处方类型 1:军人处方 ,2: 医保处方 ,3:自费处方 ,9 :其他
				sqlParams.add(tCom.get("CHARGE_TYPE"));                                                           // 费别名字
				sqlParams.add(tCom.get("NAME"));                                                                  // 病人名称
				sqlParams.add(tCom.get("SEX"));                                                                   // 病人性别
				sqlParams.add(tCom.get("DATE_OF_BIRTH"));                                                         // 病人出生日期
				sqlParams.add(crm.get("PAYMENTS"));                                                               // 实际收取费用
				sqlParams.add(("".equals(crm.get("DISPENSARY")) ? DISPENSARY : crm.get("DISPENSARY")));           // 发药药局
				sqlParams.add(DictCache.getNewInstance().getDeptName(crm.get("DISPENSARY")));                     // 发药药局
				sqlParams.add(tCom.get("IDENTITY"));                                                              // 病人身份
				sqlParams.add(crm.get("presc_no"));                                                               // 病人身份
				sqlParams.add("1");                                                                               // ORDER_TYP 医嘱类型，住院or门诊
				sqlParams.add((("".equals(crm.get("Costs")) ? DrugCosts: crm.getDouble("Costs"))) + "");          // 治疗费+药费
				sqlParams.add(AntiDrugCosts + "");    
                sqlParams.add((adminJMSY ? "1" : "0"));                                                           //静脉输液
                sqlParams.add((adminAntiJMSY ? "1" : "0"));                                                       // 抗菌药 静脉输液
				
				query.update(sql,sqlParams.toArray());
				CounterI1++;
				pzMap = null;
			}
		});
	}

	/**
	 * 使用于北京军总数据整理
	 * 
	 * @param tCom
	 */
	private void tmpDataDisposal(TCommonRecord tCom) {
		String tmpA = tCom.get("ADMINISTRATION");
		if (tmpA != null) {
			String[] parms = tmpA.split(";");
			if (parms.length > 1) {
				tCom.set("dosage_each", parms[0]);
				tCom.set("dosage_units", parms[1]);
				tCom.set("ADMINISTRATION", parms[2]);
				tCom.set("FREQUENCY", parms[3]);
			}
		}
	}

	private void DeleteOldFetcherDate(String sDate)
	{
	    JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
	    try
	    {
	        String sql = "delete presc where ORDER_DATE = '" + sDate + "'";
	        Log("删除presc:" + query.update(sql));
	        sql = "delete presc_detail where prescDate = '" + sDate + "'";
	        Log("删除presc_detail:" + query.update(sql));
	        /* 抓取处置和药品详细信息 */
            if (Config.getParamValue("FetchOutpOrders").equals("true"))
            {
    	        sql = "delete outp_orders_costs where trunc(visit_date) = to_date('" + sDate + "','yyyy-mm-dd')";
    	        Log("删除outp_orders_Costs:" + query.update(sql));
            }
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }
	    finally
	    {
	        query = null;
	    }
	}
	
	/**
	 * 对提取的处方进行药品审核
	 * 
	 * @param hisQuery
	 * @param ADate
	 */
	int CheckCount = 0;

	@SuppressWarnings("unchecked")
	public void PrescCheck(String ADate) {
		getQuery("PEAAS");
		CheckCount = 0;
		CommonMapper cmr = new CommonMapper();
		String sql = null;
		try
		{
			sql = "select * from presc where order_date = '" + ADate + "' and org_name is not null and amount >= 0 ";
			List<TCommonRecord> listPrescs = (List<TCommonRecord>) query.query(	sql.toString(), cmr);
			Log("审核处方-开方日期 " + ADate + " , 要审查的处方数 " + listPrescs.size());
			int CounterAll = listPrescs.size();
			/* 处方评价 功能 */
			IPrescReviewChecker prescChecker = (IPrescReviewChecker) SpringBeanUtil.getBean("prescReviewCheckerBean");
			for (TCommonRecord t : listPrescs) // 一条一条地处理信息
			{
				try
				{
					Log("审核日期: " + ADate + ",审核进度:  " + CheckCount + "/" + CounterAll);
					sql = "select * from presc_detail where presc_id = '" + t.get("id") + "'";
					List<TCommonRecord> presc = query.query(sql, cmr);
					/* 处方进行评价 */
					TCommonRecord tCom = prescChecker.PrescReviewCheck(t, presc);
					if (tCom.getKeys().size() > 2) 
					{
						String checkUUID = tCom.get("uuid");
						StringBuffer sbfr = new StringBuffer();
						// StringBuffer reviewResult = new StringBuffer();
						for (String key : tCom.getKeys()) 
						{
							// PRESCCHECKINFO PRESCCHECKFLAG 0 没有问题 1 有问题
							if (!"UUID".equals(key) && !"MAXUSEDAY".equals(key)) 
							{
								sbfr.append(key).append(","); // 错误信息
								if (!key.equals(tCom.get(key))) 
								{
									saveReviewResult(t, key, tCom.get(key),checkUUID);
								}

							}
						}
						if (sbfr.length() > 0) sbfr.deleteCharAt(sbfr.length() - 1);
						sql = "update presc set Presccheckid = '"+ checkUUID+ "' , PRESCCHECKFLAG = '1' , PRESCCHECKINFO = '"
								+ sbfr.toString() + "', maxuseday='" + tCom.get("maxuseday") + "'  where id = '"+ t.get("id") + "'";
					} 
					else
					{
						sql = "update presc set PRESCCHECKFLAG = '0' , maxuseday='" + tCom.get("maxuseday") + "' where id = '" + t.get("id") + "'" ;
					}
					if (query.update(sql) == 0) 
					{
						Log("处方id号 : " + t.get("id"));
						new RuntimeException("更新出问题 处方id号 : " + t.get("id"));
					}
					else 
					{
						Log("审核成功 处方id号 : " + t.get("id"));
						CheckCount++;
					}
				} 
				catch (Exception e) 
				{
					Log(" 问题处方id号 ：" + t.get("id"));
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cmr = null;
			Log(" 处方审查完成.... " + CheckCount + "个评价完成 ");
		}
	}

	/**
	 * *评审结果由以下字段组成: 1.patient_review_id: 评审id 2.patient_id: 病人编号
	 * 3.patient_name: 病人的名字 4.org_code: 组织结构代码 5.org_name: 组织机构名称
	 * 6.doctor_code:医生编号 7.doctor_name: 医生的名称 8.order_date: 处方发生的日期
	 * 9.dispensary_code: 取药的药房编号 10. dispensary_name : 取药的药房名称 11
	 * review_error_codes : 错误的编码信息 12 review_error_message : 错误的原因信息 13 state:
	 * 记录的状态
	 * 
	 * 
	 * 存储评审的结果
	 * 
	 * @param patient
	 *            病人记录
	 * @param reviewErrorCode
	 *            评审结果错误编码
	 * @param reviewMessage
	 *            评审结果原因信息
	 * @param patientReviewId
	 *            评审uuid
	 */
	private void saveReviewResult(TCommonRecord patient,String reviewErrorCode, String reviewMessage, String patientReviewId) {
		if (reviewResultService == null)
			reviewResultService = new ReviewResultBean();
		// 边界判断
		if (reviewErrorCode == null || "".equals(reviewErrorCode)) {
			return;
		}
		TCommonRecord reviewResult = new TCommonRecord();
		reviewResult.set("patientReviewId", patientReviewId);
		reviewResult.set("patientId", patient.get("PATIENT_ID"));
		reviewResult.set("patientName", patient.get("PATIENT_NAME"));
		reviewResult.set("orgCode", patient.get("ORG_CODE"));
		reviewResult.set("orgName", patient.get("ORG_NAME"));
		reviewResult.set("doctorCode", patient.get("DOCTOR_CODE"));
		reviewResult.set("doctorName", patient.get("DOCTOR_NAME"));
		reviewResult.set("orderDate", patient.get("ORDER_DATE"));
		reviewResult.set("dispensaryCode", patient.get("DISPENSARY"));
		reviewResult.set("dispensaryName", patient.get("DISPENSARYNAME"));
		reviewResult.set("reviewErrorCode", reviewErrorCode);
		reviewResult.set("reviewErrorMessage", reviewMessage);
		reviewResultService.saveReviewResult(reviewResult);
	}

	public IReviewResultServ getReviewResultService() {
		return reviewResultService;
	}

	public void setReviewResultService(IReviewResultServ reviewResultService) {
		this.reviewResultService = reviewResultService;
	}
}