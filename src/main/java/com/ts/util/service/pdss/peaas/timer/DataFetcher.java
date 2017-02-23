package com.ts.service.pdss.peaas.timer;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.Scheduler.ReportScheduler;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.springBeanManager.SpringBeanUtil;
import com.ts.service.pdss.peaas.manager.IPrescReviewChecker;

/**
 * 处方数据抽取
 * 定时器，每天0点从HIS系统中抽取前一天的所有处方信息，
 * 保存到peaas的处方表里。
 * @author Administrator
 *
 */  
@Transactional
@Deprecated
public class DataFetcher extends ReportScheduler implements Runnable
{
	
    public DataFetcher() 
    {
    	setDebugLevel(50);
    }
    
    /**
     * 当前日期推前一天
     */
    private String getPrevDate()
    {
    	Calendar c = Calendar.getInstance(); 
    	c.add(Calendar.DATE, -1); 
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
    	return sdf.format(c.getTime()); 
    }
    
    /**
     * 处方类型  1:军人处方 ,2: 医保处方 ,3:自费处方  ,9 :其他
     * 判断处方类型 
     * @param type
     * @return
     */
    private String getPrescType(String type)
    {
        boolean flag  = false;
        String result = "9";
        String JR     = Config.getParamValue("JZPRESC");                                        // 军人处方 xxx,xxx,....,
        String ZF     = Config.getParamValue("ZFPRESC");                                        // 自费处方 xxx,xxx,....,
        String YB     = Config.getParamValue("YBPRESC");                                        // 医保处方 xxx,xxx,....,
        if(!flag)
        {
            if(JR.indexOf(type + ",") != -1) 
            {
                result = "1";
                flag = true;
            }
        }
        if(!flag)
        {
            if(ZF.indexOf(type + ",") != -1) 
            {
                result = "2";
                flag = true;
            }
        }
        if(!flag)
        {
            if(YB.indexOf(type + ",") != -1) 
            {
                result = "3";
                flag = true;
            }
        }
        return result;
    }
    
    /**
     * 门诊病历记录 
     * 
     *  outp_mr(病人标识号,,就诊时间,就诊序号,主诉,既往史,家族史,婚姻史,个人史,月经史,简要病史,查体,诊断,,嘱咐,医生,顺序号,手术记录,病程记录),
     *
     * @param hisQuery
     * @param patient_id
     * @param visit_no
     * @param ADate
     * @return
     */
    @SuppressWarnings ("unchecked")
    private TCommonRecord getOUTP_MR(JDBCQueryImpl hisQuery , String patient_id ,String visit_no, String ADate)
    {
        List<TCommonRecord> results =  hisQuery.query("select * from outpdoct.OUTP_MR where PATIENT_ID = ? " +
                "and to_char(VISIT_DATE,'yyyy-mm-dd') = ?  order by VISIT_DATE desc "
                ,new Object[]{patient_id,ADate}, new CommonMapper());
        TCommonRecord tCom = new TCommonRecord();
        DictCache dCache = DictCache.getNewInstance();
        String diag_desc = "";
        String diag_code = "";
        for(int i = 0 ; i < results.size() ; i++ )
        {
            TCommonRecord t = results.get(i);
            if(i == 0)
            {
                diag_desc += t.get("diag_desc");
                if(!"".equals(t.get("diag_code")))
                {
                    diag_code += t.get("diag_code");    
                }
                else
                {
                    diag_code += dCache.getDiagnosisByName(t.get("diag_desc")).get("diagnosis_code");
                }
            }
            else
            {
                diag_desc += ";" + t.get("diag_desc");
                if(!"".equals(t.get("diag_code")))
                {
                    diag_code += ";" + t.get("diag_code");    
                }
                else
                {
                    diag_code += ";" + dCache.getDiagnosisByName(t.get("diag_desc")).get("diagnosis_code");
                }
            }
        }
        tCom.set("diag_desc", diag_desc);
        tCom.set("diag_code", diag_code);
        return tCom;
    }
    
    /**
     * 获得诊断名称 
     * @param PatientId
     * @param VisitNo
     * @return
     */
    @SuppressWarnings ("unchecked")
    private String[] getDiagnosisId(JDBCQueryImpl hisQuery , String PatientId, String VisitNo, String ADate)
    {
        List<TCommonRecord> results =  hisQuery.query("select DIAGNOSIS_NO,DIAGNOSIS_DESC from medrec.DIAGNOSIS where PATIENT_ID = ? " +
        		" and VISIT_ID = ? "
                ,new Object[]{PatientId,VisitNo}, new CommonMapper());
        StringBuffer diagnosisCode = new StringBuffer();
        StringBuffer diagnosisDesc = new StringBuffer();
        for(TCommonRecord result : results )
        {
            diagnosisCode.append(result.get("DIAGNOSIS_NO")).append(";");
            diagnosisDesc.append(result.get("DIAGNOSIS_DESC")).append(";");
        }
        if(diagnosisCode.length() > 0)
            diagnosisCode.deleteCharAt(diagnosisCode.length() -1);
        if(diagnosisDesc.length() > 0)
            diagnosisDesc.deleteCharAt(diagnosisDesc.length() -1);
    	String[] values = new String[2];
    	values[0] = diagnosisCode.toString();
    	values[1] = diagnosisDesc.toString();
    	return values;
    }
    
    /**
     * 住院处方药品记录
     * @param id
     * @param DrugCode
     * @param DrugName
     * @param DrugType
     * @param Adate
     * @return
     * @throws Exception
     */
    public TCommonRecord Data2InsertSub(String id, String DrugCode, String DrugName, String DrugType,String Adate) throws Exception
    {
        String sql = "insert into PRESC_DETAIL(PRESC_ID, DRUG_CODE, DRUG_NAME, DRUG_TYPE,PRESCDATE) values (" + 
                "'" + id       + "', " +
                "'" + DrugCode + "', " +
                "'" + DrugName + "', " + 
                "'" + DrugType + "', " +
                "'" +  Adate   + "'  " +
                ")";
        if (query.update(sql) == 0)
        {
            // 此处要记录失败的语句
            // 并抛出异常，使得本次操作回滚
            Log("处方明细插入失败：" + DrugCode + "__"  + DrugName);
            throw new Exception("添加数据出现问题");
        }
        else
        {
        	// 导入计数器+1
        	CounterI2++;
        }
        return null;
    }
    
    @SuppressWarnings ("unchecked")
    public TCommonRecord getMainRecord(JDBCQueryImpl hisQuery, String PatientID, String VisitID, String OrderNo, String OrderSubNo)
    {
        String sql = "select * from ordadm.orders where " +
        	"patient_id   = '" + PatientID  + "' and " +
    		"visit_id     = '" + VisitID    + "' and " + 
    		"Order_No     = '" + OrderNo    + "' and " + 
    		"Order_Sub_No = '" + OrderSubNo + "'    ";
        List<TCommonRecord> crMain = (List<TCommonRecord>)hisQuery.query(sql, new CommonMapper());
        if (crMain.size() == 0)
        {
        	Log("获取医嘱主记录发生错误：" + PatientID + "__" + VisitID + "  记录不存在!");
        	return null;
        }
        return crMain.get(0);
    }
 
    /**
     * 保存错误信息
     * @param crm
     * @param ExcSql
     * @param orderType
     */
    private void InsertDrawException(TCommonRecord crm,String ExcSql,String orderType)
    {
        String id = UUID.randomUUID().toString();
        String sql = "insert into exceptiondatainfo(exceptionid,patient_id,visit_id,excdate,exceptionsql,orderType) values(?,?,?,?,?,?)";
        String visit = "";
        if("1".equals(orderType))
            visit = crm.get("VISIT_NO");
        else
            visit = crm.get("VISIT_ID");
        query.update(sql, new Object[]{id,crm.get("patient_id"),visit,getPrevDate(),ExcSql,orderType});
    }
    
    /**
     * 插入draw信息
     */
    public void InsertDrawInfo()
    {
        String id = UUID.randomUUID().toString();
        
        String FileName = saveLog("APPLOG\\peaas\\peaas_" + prevDate + ".log");
        String sql = "insert into drawinfo(drawid,exccounter,finistcounter,drawdate,path) values(?,?,?,?,?)";
        query.update(sql,new Object[]{id, CounterS1 + CounterS2, CounterI1 + CounterI2, getPrevDate(), FileName});
    }
    
    /**
     * 用于流水号使用
     * @return
     */
    private String getDateId(String ADate)
    {
        String myDate = getPrevDate();
        if(ADate != null)
        {
            myDate = ADate;
        }
        return myDate.replace("-","");
    }
    
    /**
     * 
     * 直接输出自定义流水号  ID 2011111600000001\2011111600000002\2011111600000003
     * @return
     */
    private String getDecimalFormateID(String ADate , String prescType)
    {
        counter++;
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("0000000");
        return getDateId(ADate) + prescType + df.format(counter);
    }
    
    /* 用来计算流水号 计数器*/
    private static long counter       = 0;
    // 门诊处方主记录计数器
    private long CounterM1            = 0;
    // 门诊处方药品记录计数器
    private long CounterS1            = 0;
    // 门诊处方药品导入计数器
    private long CounterI1            = 0;
    // 住院处方主记录计数器
    private long CounterM2            = 0;
    // 住院处方药品记录计数器
    private long CounterS2            = 0;
    // 住院处方药品导入计数器
    private long CounterI2            = 0;
    private String prevDate           = "";
    
    public boolean canRun()
    {
		try
		{
	    	if (super.canRun())
	    	{
	    		Calendar cal = Calendar.getInstance();
	    		int hour = cal.get(Calendar.HOUR_OF_DAY);
	    		JDBCQueryImpl peaasQuery = DBQueryFactory.getQuery("PEAAS");
	            @SuppressWarnings("unchecked")
	    		List<TCommonRecord> list = peaasQuery.query("select rulecode,rulevalue from ruleparameter where rulecode = 'PEAASDataFetcherTime'", new CommonMapper());
	            peaasQuery = null;
	            if ((list != null) && (list.size() > 0))
	            {
	            	try
	            	{
	            		TCommonRecord cr = list.get(0);
	            		System.out.println("门诊DataFetcher启动时间为：" + cr.get("rulevalue"));
	            		return hour == Integer.parseInt(cr.get("rulevalue"));
	            	}
	            	catch (Exception ex)
	            	{
	            		ex.printStackTrace();
	            	}
	            }
	    		return false;
    		}
    	}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
    	return false;
    }
    
    @Override
    public void run()
    {
        try
        {
            if (!canRun()) return;
//            Thread.sleep(10000);
            Log("准备开始处理数据...");
            counter       = 0;
            getQuery("PEAAS");
            setDebugLevel(Config.getIntParamValue("IASDebugLevel"));
            JDBCQueryImpl hisQuery = DBQueryFactory.getQuery("HIS");
            Log("数据库连接准备完毕...");
            Log("辅助数据准备完毕...");
            prevDate = getPrevDate();
            Log("提取日期：" + prevDate);
            /* 提取住院处方 */ 
            Log("开始提取住院处方...");
            try {
                //FetchData2(hisQuery,prevDate);
                FetchDataHospital(hisQuery, prevDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            /* 提取门诊处方 */
            Log("开始提取门诊处方...");
            FetchDataOut(hisQuery, prevDate);
            //FetchData1(hisQuery, prevDate);
            /* 保存操作日志 */
            Log("提取结束...");
            BuildReport(prevDate, "PEAAS", vctLog);
            InsertDrawInfo();
            if(Config.getParamValue("PrescCheckFlag").equals("true"))
            {
                Log("对处方进行审核开始....");
                PrescCheck(prevDate);
                Log("对处方进行审核结束!");    
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     *  选择 抓取门诊处方 出去药 药房  
     * @return
     */
    private String getFetchDeptCode()  
    {
        String result = Config.getParamValue("FetchDataOutDept");
        if(result.length() > 0)
        {
            String[] depts = Config.getParamValue("FetchDataOutDept").split(";");
            System.out.println("FetchDataOutDept" + depts.length);
            if(depts.length > 0)
            {
                StringBuffer sbfr = new StringBuffer();
                for(String s : depts)
                {
                    sbfr.append("'").append(s).append("',");
                }
                if(sbfr.length() > 0 )
                    sbfr.deleteCharAt(sbfr.length() - 1 );
                result = " and DISPENSARY in (" + sbfr.toString() + ")";    
            }
        }
        return result;
    }

    /**
     * 获取病人信息 
     * @param PatientId
     * @return
     */
    private TCommonRecord getPatientDetail( JDBCQueryImpl hisQuery , String PatientId, String ADate,TCommonRecord crm)
    {
        TCommonRecord  tCom = (TCommonRecord)hisQuery.queryForObject("select * from medrec.PAT_MASTER_INDEX where PATIENT_ID = ? "
                ,new Object[]{PatientId}, new CommonMapper());
        String nowDate = ADate;
        try
        {
            nowDate = nowDate.substring(0,nowDate.indexOf("-")); 
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        if (tCom == null)
        {
        	Log("获取病人基本信息错误：" + PatientId);
        	tCom = new TCommonRecord();
        	tCom.set("pat_age", "0");
        	tCom.set("name", crm.get("name"));
            tCom.set("charge_type", crm.get("charge_type"));
            tCom.set("pat_age", "0");
            return tCom;
        }
        String dateFoBirth = tCom.get("DATE_OF_BIRTH");
        if(dateFoBirth == null || "".equals(dateFoBirth))
        {
            dateFoBirth = "1990-01-01";
        }
        try
        {
            dateFoBirth        = dateFoBirth.substring(0,dateFoBirth.indexOf("-"));
            tCom.set("pat_age", String.valueOf(Integer.parseInt(nowDate) - Integer.parseInt(dateFoBirth)));    
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    	return tCom;
    }

    /**
     * 新住院处方
     * @param query
     * @param ADate
     */
    @SuppressWarnings ("unchecked")
    public void FetchDataHospital(JDBCQueryImpl hisQuery , String ADate)
    {
        Log("开始提取住院处方...");
        CommonMapper cmr = new CommonMapper();
        StringBuffer sql = new StringBuffer();
        sql.append("select * from v_dcdt_DRUG_PRESC_MASTER where PRESC_SOURCE  = 1 and to_char(PRESC_DATE,'yyyy-mm-dd') = '").append(ADate).append("'")
            .append("order by patient_id ");
        try
        { 
            List<TCommonRecord> list = hisQuery.query(sql.toString(), cmr);
            CounterM2 = list.size();
            Log(ADate + "共有处方：" + CounterM2 + "个");
            for(TCommonRecord tCom : list )
            {
                try
                {
                    Log("开始提取住院处方：" + tCom.get("presc_No"));
                    double Cost = tCom.getDouble("costs");
                    if(Cost < 0)
                    {
                        continue;
                    }
                    /* 整理住院 处方并添加  */
                    FetchDataHospitalDetail(tCom,hisQuery,ADate);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            cmr = null;
            Log("住院处方提取结束，总计药品总数：" + CounterS2 + ",导入：" + CounterI2 + ";处方主记录：" + CounterM2);
        }
    }
    
    /**
     * 新住院处方用药详细 
     * @param tCom
     * @param hisQuery
     * @param ADate
     * @param idx
     */
    @SuppressWarnings ("unchecked")
    private void FetchDataHospitalDetail(TCommonRecord crm , JDBCQueryImpl hisQuery,String ADate)
    {
        DecimalFormat d = new DecimalFormat("#0.00");
        CommonMapper cmr = new CommonMapper();
        String sql = null;
        HashMap<String, String> pzMap = new HashMap<String, String>();
        try
        {
            int impDrugCount = 0;
            /* 处方关联 id */
            String id = getDecimalFormateID(ADate,"2");
            // 药物品种数
            int DrugCount = 0;
            // 基本药品数
            int baseDrugCount = 0;
            // 是否有抗菌药
            boolean HasKJ = false;
            // 是否有注射剂
            boolean HasZS = false;
            // 中药注射剂 
            boolean CDZS  = false;
            // 检查处方中是否有二类精神药物 
            boolean ELJSY = false;
            //  检查处方中 需要毒性药品 
            boolean DDrug = false;
            //  检查处方中 需要麻醉性药品
            boolean MDrug = false;
            //  检查处方中 需要放射性药品
            boolean FSDrug = false;
            //  检查处方中 需要一类精神药品
            boolean YLJSY  = false;
            // 检查处方中 需要 贵重药品
            boolean GZDrug = false;
            // 检查处方中 毒麻药品
            boolean DMDrug = false;
            StringBuffer sqlbfr = new StringBuffer();
            sqlbfr.append("select * from v_dcdt_drug_presc_detail where presc_no = '").append(crm.get("presc_no")).append("' and to_char(PRESC_DATE,'yyyy-mm-dd') = '").append(ADate).append("'");
            List<TCommonRecord> crsS = hisQuery.query(sqlbfr.toString(), cmr);
            CounterS2 += crsS.size();
            if(crsS.size() == 0) return ;  
            Log(40, "处方日期:" + crm.get("PRESC_DATE") + "," +crm.get("PRESC_NO") + "下共有药品信息" + crsS.size() + "个");
            for(TCommonRecord crs : crsS)
            {                
                /* 临时处理  */
                tmpDataDisposal(crs);
                /* 药品代码  */
                String drug = crs.get("DRUG_CODE");
                String DrugToxiProperty = DrugUtils.getDrugToxiProperty(drug, crs.get("Drug_Spec"));
                // 判断二类精神药物
                ELJSY = DrugToxiProperty.indexOf("精二") >= 0;
                // 判断毒性药品
                DDrug = DrugToxiProperty.indexOf("毒药") >= 0;
                // 判断麻醉药品
                MDrug = DrugToxiProperty.indexOf("麻药") >= 0;
                //判断放射药品
                FSDrug = DrugToxiProperty.indexOf("放射") >= 0;
                //判断一类精神药品
                YLJSY = DrugToxiProperty.indexOf("精一") >= 0;
                //判断贵重药品
                GZDrug = DrugToxiProperty.indexOf("贵重") >= 0;
                //判断毒麻药品
                DMDrug = DrugToxiProperty.indexOf("毒麻") >= 0;
                /* 药品类型  */
                String drugType = DrugUtils.getDrugType(crs.get("DRUG_CODE"),crs.get("DRUG_Spec"),crs.get("ADMINISTRATION"));
                sql = "insert into PRESC_DETAIL(PRESC_ID, DRUG_CODE, DRUG_NAME, DRUG_TYPE,PRESCDATE,ITEM_CLASS,DRUG_SPEC," +
                        "FIRM_ID,UNITS,AMOUNT,DOSAGE,DOSAGE_UNITS,ADMINISTRATION,FREQUENCY,ORDER_NO,ORDER_SUB_NO,DISPENSARY,FREQ_DETAIL,ToxiProperty,Costs,CHARGES," +
                        "PACKAGE_SPEC,CENTERDRUGZS,ANTIDRUGFLAG,NORMDRUGZS,ORDER_TYPE) values (" + 
                    "'" + id                        + "', " +
                    "'" + drug                      + "', " +
                    "'" + crs.get("DRUG_NAME")      + "', " +
                    "'" + drugType                  + "', " +
                    "'" + ADate                     + "', " +
                    "'" + getDrugItemType(drug)     + "', " +
                    "'" + crs.get("DRUG_SPEC")      + "', " + // 药品规格
                    "'" + crs.get("FIRM_ID")        + "', " +
                    "'" + crs.get("PACKAGE_UNITS")  + "', " + // 单位
                    "'" + crs.get("QUANTITY")       + "', " + // 数量
                    "'" + crs.get("dosage_each")    + "', " + // 单次剂量
                    "'" + crs.get("dosage_units")   + "', " + // 剂量单位
                    "'" + crs.get("ADMINISTRATION") + "', " +
                    "'" + crs.get("FREQUENCY")      + "', " +
                    "'" + crs.get("ORDER_NO")       + "', " +
                    "'" + crs.get("ORDER_SUB_NO")   + "', " +
                    "'" + crs.get("DISPENSARY")     + "', " +
                    "'" + crs.get("FREQ_DETAIL")    + "', " +
                    "'" + DrugToxiProperty          + "', " +
                    "'" + crs.get("Costs")          + "', " +
                    "'" + crs.get("PAYMENTS")       + "', " +
                    "'" + crs.get("PACKAGE_SPEC")   + "', " + // 包装规格
                    "'" + (DrugUtils.isCenterDrugZS(crs.get("drug_code"),crs.get("DRUG_Spec"))?"1":"0") + "', " + // 中药注射剂
                    "'" + (DrugUtils.isKJDrug(crs.get("drug_code"))?"1":"0")                            + "', " + // 抗菌药物
                    "'" + (DrugUtils.isZSNormDrug(crs.get("drug_code"))?"1":"0")                        + "', " + // 标准注射
                    "'" + 2                         + "'  " +
                    ")";
                impDrugCount++;
                if (query.update(sql) == 0)
                {
                    // 此处要记录失败的语句
                    // 并抛出异常，使得本次操作回滚
                    InsertDrawException(crm, sql, "1");
                    Log("处方明细插入失败：" + crs.get("DRUG_CODE") + "__"  + crs.get("DRUG_NAME"));
                    throw new Exception("添加数据出现问题");
                }
                else
                {
                    // 导入计数器+1
                    CounterI2++;
                }
                if(Config.getIntParamValue("PZ") >= drug.length())
                    drug = drug.substring(0, Config.getIntParamValue("PZ"));
                pzMap.put(drug, null);
                // 判断抗菌药
                if (!HasKJ) HasKJ = DrugUtils.isKJDrug(crs.get("DRUG_CODE"));
                // 判断注射剂
                if (!HasZS) HasZS = DrugUtils.isZSNormDrug(crs.get("DRUG_CODE"));
                //中药注射剂
                if(!CDZS)    CDZS = DrugUtils.isCenterDrugZS(crs.get("drug_code"),crs.get("DRUG_Spec"));
                // 判断基本药品数
                if (true) baseDrugCount ++;
            }
            /* 药品种数  */
            DrugCount = pzMap.size();
            Log(40, "共导入药品信息" + impDrugCount + "个;药物品种数" + DrugCount + ";基本药物品种数:" + baseDrugCount + ";抗菌药:" + (HasKJ?"有":"无") + ";注射剂:" + (HasZS?"有":"无"));
            /* 组织诊断信息    */
            String visit_id = crm.get("visit_id"); 
            if("".equals(visit_id))
            {
                /* 获得住院号  */
                visit_id = getLastPatVisit(hisQuery,crm.get("patient_id")) ;
            }
            String [] values = getDiagnosisId(hisQuery,crm.get("PATIENT_ID"), visit_id,ADate);
            /* 病历记录  */
            String dt = crm.get("PRESC_DATE");
            dt = dt.substring(0, 10);
            /* 病人基本信息 */  
            TCommonRecord tCom = getPatientDetail(hisQuery, crm.get("PATIENT_ID"), ADate,crm);
            /* 处方类型 (费别) */
            String prescType   = this.getPrescType(tCom.get("CHARGE_TYPE")); 
            String patAge = tCom.get("pat_age");
            sql = "insert into PRESC(ID, PATIENT_ID, PATIENT_AGE, VISIT_NO, ORG_CODE, " +
                "ORG_NAME, DOCTOR_CODE, DOCTOR_NAME, ORDER_DATE, AMOUNT, DIAGNOSIS_CODES, " +
                "DIAGNOSIS_NAMES, Drug_Count, BaseDrug_Count, HasKJ, HasZS,CDZS, ELJSY,DDRUG,MDrug,FSDrug,YLJSY,GZDrug,DMDrug,PRESCTYPE," + 
                "PRESCTYPENAME,PRESCCHECKINFO,PRESCCHECKFLAG,PATIENT_NAME,PATIENT_SEX,PATIENT_BIRTH,CHARGES,DISPENSARY,DISPENSARYNAME,IDENTITY,ORDER_TYPE) values (" + 
                "'" + id                                             + "', " + // ID
                "'" + crm.get("PATIENT_ID")                          + "', " + // PATIENT_ID      病人id
                "'" + patAge                                         + "', " + // PATIENT_AGE     病人年龄
                "'" + crm.get("VISIT_NO")                            + "', " + // VISIT_NO        就诊id
                "'" + crm.get("ORDERED_BY")                          + "', " + // ORG_CODE        诊断部门代码
                "'" + DictCache.getNewInstance().getDeptName(hisQuery, crm.get("ORDERED_BY"))      + "', " + // ORG_NAME        诊断部门名称
                "'" + DictCache.getNewInstance().getDoctorCode(hisQuery, crm.get("PRESCRIBED_BY")) + "', " + // DOCTOR_CODE     医生代码
                "'" + crm.get("PRESCRIBED_BY")                       + "', " + // DOCTOR_NAME     医生名称
                " '" + dt                                            + "', " + // ORDER_DATE      医嘱日期
                " " + crm.get("Costs")                               + " , " + // AMOUNT          总计花费
                "'" + values[0]                                      + "', " + // DIAGNOSIS_CODES 诊断代码
                "'" + values[1]                                      + "', " + // DIAGNOSIS_NAMES 诊断名称
                " " + DrugCount                                      + ",  " + // 品种数
                " " + baseDrugCount                                  + ",  " + // 基本药品种数
                "'" + (HasKJ ? "1" : "0")                            + "', " + // 是否有抗菌药
                "'" + (HasZS ? "1" : "0")                            + "', " + // 是否有注射剂
                "'" + (CDZS  ? "1" : "0")                            + "', " + // 是否有中药注射剂
                "'" + (ELJSY ? "1" : "0")                            + "', " + // 是否有二类精神药物
                "'" + (DDrug ? "1" : "0")                            + "', " + // 是否有毒性药物
                "'" + (MDrug ? "1" : "0")                            + "', " + // 是否有麻醉药品
                "'" + (FSDrug ? "1" : "0")                           + "', " + // 是否有放射药品
                "'" + (YLJSY  ? "1" : "0")                           + "', " + // 是否有一类精神药品  
                "'" + (GZDrug ? "1" : "0")                           + "', " + // 是否有贵重药品
                "'" + (DMDrug ? "1" : "0")                           + "', " + // 是否有毒麻药品
                "'" + prescType                                      + "', " + // 处方类型  1:军人处方 ,2: 医保处方 ,3:自费处方  ,9 :其他
                "'" + crm.get("CHARGE_TYPE")                         + "', " + // 费别名字
                "'" + ""                                             + "', " + // 处方审查结果信息
                "'" + ""                                             + "', " + // 处方审查标示 0 ：没有问题 1:为有问题
                "'" + tCom.get("NAME")                               + "', " + // 病人名称
                "'" + tCom.get("SEX")                                + "', " + // 病人性别
                "'" + tCom.get("DATE_OF_BIRTH")                      + "', " + // 病人出生日期
                "'" + crm.get("PAYMENTS")                            + "', " + // 实际收取费用
                "'" + crm.get("DISPENSARY")                          + "', " + // 发药药局
                "'" + DictCache.getNewInstance().getDeptName(hisQuery, crm.get("DISPENSARY")) + "', " + // 发药药局
                "'" + crm.get("IDENTITY")                            + "', " + // 病人身份
                "'" + "2"                                            + "'  " + // ORDER_TYPE    医嘱类型，住院or门诊
                ")";
            if (query.update(sql) == 0)
            {
                // 此处要记录失败的语句
                // 并抛出异常，使得本次操作回滚
                Log("住院处方记主录插入失败：" + crm.get("PATIENT_ID") + "__"  + crm.get("VISIT_NO") + "__" + crm.get("Serial_No"));
                InsertDrawException(crm, sql, "1");
                throw new Exception("添加数据出现问题");
            }
            else
            {
                // 导入计数器+1
                CounterI2++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            cmr   = null;
            pzMap = null;
        }
    }

    /**
     * 返回住院visit_ID号.
     * @param hisQuery
     * @param patientId
     * @return
     */
    private String getLastPatVisit(JDBCQueryImpl  hisQuery, String patientId)
    {
        CommonMapper cmr = new CommonMapper();
        TCommonRecord tCom = null;
        try
        {
            String sql = "select * from MEDREC.PAT_VISIT where patient_id ='" + patientId + "' order by visit_id desc ";
            tCom = (TCommonRecord)hisQuery.queryForObject(sql, cmr);    
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            cmr = null;
        }
        return tCom.get("Visit_id");
    }

    /**
     *  返回该药品 归类 
     * @param DrugCode
     * @return
     
    private String getDrugToxiProperty(String DrugCode)
    {
        //判断一类精神药品 
        if(Config.getParamValue("YLJSY").indexOf(DrugCode + ",") != -1) return "YLJSY";
        // 判断二类精神药物
        if(Config.getParamValue("ELJSY").indexOf(DrugCode + ",") != -1) return "ELJSY";
        // 判断毒性药品 
        if(Config.getParamValue("DDrug").indexOf(DrugCode + ",") != -1) return "DDrug";
        // 判断麻醉药品
        if(Config.getParamValue("MDrug").indexOf(DrugCode + ",") != -1) return "MDrug";
        //判断放射药品 
        if(Config.getParamValue("FSDrug").indexOf(DrugCode + ",") != -1) return "FSDrug";
        //判断贵重药品 
        if(Config.getParamValue("GZDrug").indexOf(DrugCode + ",") != -1) return "GZDrug";
        //判断贵重药品 
        if(Config.getParamValue("DMDrug").indexOf(DrugCode + ",") != -1) return "DMDrug";
        return "";
    }*/
    
    /**
     * 
     *  用于 判断 概要是 属于何类型药品 (中药 中成药 西药 )
     *  发现字段表中有相应的字段描述
     * @param drugCode
     * @return
     */
    private String getDrugItemType(String drugCode)
    {
        DictCache d = DictCache.getNewInstance();
        TCommonRecord drug = d.getDrugDictInfo(drugCode);
        String indicator = drug.get("drug_indicator");
        if("1".equals(indicator))
        {
            return "A";
        }
        else if ("3".equals(indicator))
        {
            return "B";
        }
        else if ("2".equals(indicator))
        {
            return "C";
        }
        return "" ;
        
        /* 中药 (草药 )*/
//        String [] CenterDrug = Config.getParamValue("CenterDrug").split(";");
//        for (String v: CenterDrug)
//        {
//            if (v.trim().equalsIgnoreCase(drugCode.trim()))
//            {
//                TCommonRecord t = d.getDrugDictInfo(drugCode);
//                if("饮片".equals(t.get("drug_from")))
//                {
//                    return "C";    
//                }
//            }
//        }
        /* 中成药 */
//        String [] ZCDrug = Config.getParamValue("ZhongChengDrug").split(";");
//        for (String v: ZCDrug)
//        {
//            //if (v.trim().equalsIgnoreCase(drugCode.trim()))
//            if (drugCode.toUpperCase().startsWith(v.toUpperCase()))  
//                return "B";
//        }
        /* 西药*/
//        String[] Eestdrug = Config.getParamValue("EestDrug").split(";");
//        for (String v: Eestdrug)
//        {
//            if (v.trim().equalsIgnoreCase(drugCode.trim()))
//                return "A";
//        }
//        return "A";
    }

    /**
     * 新门诊处方
     * @param query
     * @param ADate
     */
    @SuppressWarnings ("unchecked")
    public void FetchDataOut(JDBCQueryImpl hisQuery , String ADate)
    {
        Log("开始提取门诊处方...");
        CommonMapper cmr = new CommonMapper();
        StringBuffer sql = new StringBuffer();
        sql.append("select * from v_dcdt_DRUG_PRESC_MASTER where PRESC_SOURCE  = 0 and to_char(PRESC_DATE,'yyyy-mm-dd') = '").append(ADate).append("'");
        sql.append(getFetchDeptCode());
        sql.append(" order by patient_id ");
        try
        { 
            List<TCommonRecord> list = hisQuery.query(sql.toString(), cmr);
            CounterM1 = list.size();
            Log(ADate + "共有门诊处方：" + CounterM1 + "个");
            for(TCommonRecord tCom : list )
            {
                try
                {
                    Log("开始提取门诊处方：" + tCom.get("presc_No"));
                    double Cost = tCom.getDouble("costs");
                    if(Cost < 0)
                    {
                        continue;
                    }
                    /* 整理住院 处方并添加  */
                    FetchDataOutDetail(tCom,hisQuery,ADate);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            cmr = null;
            Log("门诊处方提取结束，总计药品总数：" + CounterS1 + ",导入：" + CounterI1 + ";处方主记录：" + CounterM1);
        }
    }
    
    /**
     * 新住门诊处方用药详细 
     * @param tCom
     * @param hisQuery
     * @param ADate
     * @param idx
     */
    @SuppressWarnings ("unchecked")
    private void FetchDataOutDetail(TCommonRecord crm , JDBCQueryImpl hisQuery,String ADate)
    {
        DecimalFormat d = new DecimalFormat("#0.00");
        CommonMapper cmr = new CommonMapper();
        String sql = null;
        HashMap<String, String> pzMap = new HashMap<String, String>();
        try
        {
            int impDrugCount = 0;
            /* 处方关联 id */
            String id = getDecimalFormateID(ADate,"2");
            // 药物品种数
            int DrugCount = 0;
            // 基本药品数
            int baseDrugCount = 0;
            // 是否有抗菌药
            boolean HasKJ = false;
            // 是否有注射剂
            boolean HasZS = false;
            // 中药注射剂 
            boolean CDZS  = false;
            // 检查处方中是否有二类精神药物 
            boolean ELJSY = false;
            //  检查处方中 需要毒性药品 
            boolean DDrug = false;
            //  检查处方中 需要麻醉性药品
            boolean MDrug = false;
            //  检查处方中 需要放射性药品
            boolean FSDrug = false;
            //  检查处方中 需要一类精神药品
            boolean YLJSY  = false;
            // 检查处方中 需要 贵重药品
            boolean GZDrug = false;
            // 检查处方中 毒麻药品
            boolean DMDrug = false;
            StringBuffer sqlbfr = new StringBuffer();
            sqlbfr.append("select * from v_dcdt_drug_presc_detail where presc_no = '").append(crm.get("presc_no")).append("' and to_char(PRESC_DATE,'yyyy-mm-dd') = '").append(ADate).append("'");
            List<TCommonRecord> crsS = hisQuery.query(sqlbfr.toString(), cmr);
            CounterS1 += crsS.size();
            if(crsS.size() == 0) return ;  
            Log(40, "处方日期:" + crm.get("PRESC_DATE") + "," +crm.get("PRESC_NO") + "下共有药品信息" + crsS.size() + "个");
            for(TCommonRecord crs : crsS)
            {           
                /* 临时处理  */
                tmpDataDisposal(crs);
                /* 药品代码  */
                String drug = crs.get("DRUG_CODE");
                String DrugToxiProperty = DrugUtils.getDrugToxiProperty(drug, crs.get("Drug_Spec"));
                // 判断二类精神药物
                ELJSY = DrugToxiProperty.indexOf("精二") >= 0;
                // 判断毒性药品
                DDrug = DrugToxiProperty.indexOf("毒药") >= 0;
                // 判断麻醉药品
                MDrug = DrugToxiProperty.indexOf("麻药") >= 0;
                //判断放射药品
                FSDrug = DrugToxiProperty.indexOf("放射") >= 0;
                //判断一类精神药品
                YLJSY = DrugToxiProperty.indexOf("精一") >= 0;
                //判断贵重药品
                GZDrug = DrugToxiProperty.indexOf("贵重") >= 0;
                //判断毒麻药品
                DMDrug = DrugToxiProperty.indexOf("毒麻") >= 0;
                /* */
                String drugType = DrugUtils.getDrugType(crs.get("DRUG_CODE"),crs.get("DRUG_Spec"), crs.get("ADMINISTRATION"));
                sql = "insert into PRESC_DETAIL(PRESC_ID, DRUG_CODE, DRUG_NAME, DRUG_TYPE,PRESCDATE,ITEM_CLASS,DRUG_SPEC," +
                        "FIRM_ID,UNITS,AMOUNT,DOSAGE,DOSAGE_UNITS,ADMINISTRATION,FREQUENCY,ORDER_NO,ORDER_SUB_NO,DISPENSARY,FREQ_DETAIL," +
                        "ToxiProperty,Costs,CHARGES,PACKAGE_SPEC,CENTERDRUGZS,ANTIDRUGFLAG,NORMDRUGZS,ORDER_TYPE) values (" + 
                    "'" + id                        + "', " +
                    "'" + drug                      + "', " +
                    "'" + crs.get("DRUG_NAME")      + "', " +
                    "'" + drugType                  + "', " +
                    "'" + ADate                     + "', " +
                    "'" + getDrugItemType(drug)     + "', " +
                    "'" + crs.get("DRUG_SPEC")      + "', " + // 药品规格
                    "'" + crs.get("FIRM_ID")        + "', " +
                    "'" + crs.get("PACKAGE_UNITS")  + "', " + // 单位
                    "'" + crs.get("QUANTITY")       + "', " + // 数量
                    "'" + crs.get("dosage_each")    + "', " + // 单次剂量
                    "'" + crs.get("dosage_units")   + "', " + // 剂量单位
                    "'" + crs.get("ADMINISTRATION") + "', " +
                    "'" + crs.get("FREQUENCY")      + "', " +
                    "'" + crs.get("ORDER_NO")       + "', " +
                    "'" + crs.get("ORDER_SUB_NO")   + "', " +
                    "'" + crs.get("DISPENSARY")     + "', " +
                    "'" + crs.get("FREQ_DETAIL")    + "', " +
                    "'" + DrugToxiProperty          + "', " +
                    "'" + crs.get("Costs")          + "', " +
                    "'" + crs.get("PAYMENTS")       + "', " +
                    "'" + crs.get("PACKAGE_SPEC")   + "', " +  //包装规格 
                    "'" + (DrugUtils.isCenterDrugZS(crs.get("drug_code"),crs.get("DRUG_Spec"))?"1":"0") + "', " +  // 中药注射剂
                    "'" + (DrugUtils.isKJDrug(crs.get("drug_code"))?"1":"0")       + "', " +  // 抗菌药物
                    "'" + (DrugUtils.isZSNormDrug(crs.get("drug_code"))?"1":"0")   + "', " +  // 标准注射
                    "'" + 1                         + "'  " +
                    ")";
                impDrugCount++;
                if (query.update(sql) == 0)
                {
                    // 此处要记录失败的语句
                    // 并抛出异常，使得本次操作回滚
                    InsertDrawException(crm, sql, "1");
                    Log("处方明细插入失败：" + crs.get("DRUG_CODE") + "__"  + crs.get("DRUG_NAME"));
                    throw new Exception("添加数据出现问题");
                }
                else
                {
                    System.out.print("P=");
                    // 导入计数器+1
                    CounterI1++;
                }
                if(Config.getIntParamValue("PZ") >= drug.length())
                    drug = drug.substring(0, Config.getIntParamValue("PZ"));
                pzMap.put(drug, null);
                // 判断抗菌药
                if (!HasKJ) HasKJ = DrugUtils.isKJDrug(crs.get("DRUG_CODE"));
                // 判断注射剂
                if (!HasZS) HasZS = DrugUtils.isZSNormDrug(crs.get("DRUG_CODE"));
                //中药注射剂
                if(!CDZS)    CDZS = DrugUtils.isCenterDrugZS(crs.get("drug_code"),crs.get("DRUG_Spec"));
                // 判断基本药品数
                if (true) baseDrugCount ++;
            }
            /* 药品种数  */
            DrugCount = pzMap.size();
            Log(40, "共导入药品信息" + impDrugCount + "个;药物品种数" + DrugCount + ";基本药物品种数:" + baseDrugCount + ";抗菌药:" + (HasKJ?"有":"无") + ";注射剂:" + (HasZS?"有":"无"));
            /* 组织诊断信息    */
            TCommonRecord diag = getOUTP_MR(hisQuery, crm.get("patient_id"), "", ADate);
            /* 门诊病历记录  */
            String dt = crm.get("PRESC_DATE");
            dt = dt.substring(0, 10);
            /* 病人基本信息 */  
            TCommonRecord tCom = getPatientDetail(hisQuery, crm.get("PATIENT_ID"), ADate,crm);
            /* 处方类型 (费别) */
            String prescType   = this.getPrescType(tCom.get("CHARGE_TYPE")); 
            String patAge = tCom.get("pat_age");
            sql = "insert into PRESC(ID, PATIENT_ID, PATIENT_AGE, VISIT_NO, ORG_CODE, " +
                "ORG_NAME, DOCTOR_CODE, DOCTOR_NAME, ORDER_DATE, AMOUNT, DIAGNOSIS_CODES, " +
                "DIAGNOSIS_NAMES, Drug_Count, BaseDrug_Count, HasKJ, HasZS, CDZS,ELJSY,DDRUG,MDrug,FSDrug,YLJSY,GZDrug,DMDrug,PRESCTYPE," + 
                "PRESCTYPENAME,PRESCCHECKINFO,PRESCCHECKFLAG,PATIENT_NAME,PATIENT_SEX,PATIENT_BIRTH,CHARGES,DISPENSARY,DISPENSARYNAME,IDENTITY,ORDER_TYPE) values (" + 
                "'" + id                                             + "', " + // ID
                "'" + crm.get("PATIENT_ID")                          + "', " + // PATIENT_ID      病人id
                "'" + patAge                                         + "', " + // PATIENT_AGE     病人年龄
                "'" + crm.get("VISIT_NO")                            + "', " + // VISIT_NO        就诊id
                "'" + crm.get("ORDERED_BY")                          + "', " + // ORG_CODE        诊断部门代码
                "'" + DictCache.getNewInstance().getDeptName(hisQuery, crm.get("ORDERED_BY"))      + "', " + // ORG_NAME        诊断部门名称
                "'" + DictCache.getNewInstance().getDoctorCode(hisQuery, crm.get("PRESCRIBED_BY")) + "', " + // DOCTOR_CODE     医生代码
                "'" + crm.get("PRESCRIBED_BY")                       + "', " + // DOCTOR_NAME     医生名称
                " '" + dt                                            + "', " + // ORDER_DATE      医嘱日期
                " " + crm.get("costs")                               + " , " + // AMOUNT          总计花费
                "'" + diag.get("diag_code")                          + "', " + // DIAGNOSIS_CODES 诊断代码
                "'" + diag.get("diag_desc")                          + "', " + // DIAGNOSIS_NAMES 诊断名称
                " " + DrugCount                                      + ",  " + // 品种数
                " " + baseDrugCount                                  + ",  " + // 基本药品种数
                "'" + (HasKJ ? "1" : "0")                            + "', " + // 是否有抗菌药
                "'" + (HasZS ? "1" : "0")                            + "', " + // 是否有注射剂
                "'" + (CDZS  ? "1" : "0")                            + "', " + // 是否有中药注射剂
                "'" + (ELJSY ? "1" : "0")                            + "', " + // 是否有二类精神药物
                "'" + (DDrug ? "1" : "0")                            + "', " + // 是否有毒性药物
                "'" + (MDrug ? "1" : "0")                            + "', " + // 是否有麻醉药品
                "'" + (FSDrug ? "1" : "0")                           + "', " + // 是否有放射药品
                "'" + (YLJSY  ? "1" : "0")                           + "', " + // 是否有一类精神药品  
                "'" + (GZDrug ? "1" : "0")                           + "', " + // 是否有贵重药品
                "'" + (DMDrug ? "1" : "0")                           + "', " + // 是否有毒麻药品
                "'" + prescType                                      + "', " + // 处方类型  1:军人处方 ,2: 医保处方 ,3:自费处方  ,9 :其他
                "'" + crm.get("CHARGE_TYPE")                         + "', " + // 费别名字
                "'" + ""                                             + "', " + // 处方审查结果信息
                "'" + ""                                             + "', " + // 处方审查标示 0 ：没有问题 1:为有问题
                "'" + tCom.get("NAME")                               + "', " + // 病人名称
                "'" + tCom.get("SEX")                                + "', " + // 病人性别
                "'" + tCom.get("DATE_OF_BIRTH")                      + "', " + // 病人出生日期
                "'" + crm.get("PAYMENTS")                            + "', " + // 实际收取费用
                "'" + crm.get("DISPENSARY")                          + "', " + // 发药药局
                "'" + DictCache.getNewInstance().getDeptName(hisQuery, crm.get("DISPENSARY")) + "', " + // 发药药局
                "'" + crm.get("IDENTITY")                            + "', " + // 病人身份
                "'" + "1"                                            + "'  " + // ORDER_TYPE    医嘱类型，住院or门诊
                ")";
            if (query.update(sql) == 0)
            {
                // 此处要记录失败的语句
                // 并抛出异常，使得本次操作回滚
                Log("门诊处方记主录插入失败：" + crm.get("PATIENT_ID") + "__"  + crm.get("VISIT_NO") + "__" + crm.get("Serial_No"));
                InsertDrawException(crm, sql, "1");
                throw new Exception("添加数据出现问题");
            }
            else
            {
                // 导入计数器+1
                CounterI1++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            cmr   = null;
            pzMap = null;
        }
    }
    
    /**
     * 使用于北京军总数据整理 
     * @param tCom
     */
    private void tmpDataDisposal(TCommonRecord tCom)
    {
        String tmpA = tCom.get("ADMINISTRATION");
        if(tmpA != null )
        {
            String[] parms = tmpA.split(";");
            if(parms.length > 1)
            {
                tCom.set("dosage_each", parms[0]);
                tCom.set("dosage_units", parms[1]);
                tCom.set("ADMINISTRATION", parms[2]);
                tCom.set("FREQUENCY", parms[3]);
            }
        }
    }
    
    /**
     * 对提取的处方进行药品审核 
     * @param hisQuery
     * @param ADate
     */
    int CheckCount = 0 ;
    @SuppressWarnings ("unchecked")
    public void PrescCheck(String ADate)
    {
        getQuery("PEAAS");
        CheckCount = 0 ;
        CommonMapper cmr = new CommonMapper();
        String sql = null;
        try
        {
            sql = "select * from presc where order_date = '"+ ADate + "'";
            List<TCommonRecord> listPrescs = (List<TCommonRecord>)query.query(sql.toString(), cmr);
            Log("审核处方-开方日期 " + ADate + " , 要审查的处方数 " + listPrescs.size());
            /* 处方评价 功能 */
            IPrescReviewChecker prescChecker = (IPrescReviewChecker)SpringBeanUtil.getBean("prescReviewCheckerBean");
            for(TCommonRecord t : listPrescs)
            {
                try
                {
                    Log("审核处方号: " + t.get("id") );
                    sql = "select * from presc_detail where presc_id = '" + t.get("id") + "'";
                    List<TCommonRecord> presc =  query.query(sql, cmr);
                    /* 处方进行评价 */
                    TCommonRecord tCom = prescChecker.PrescReviewCheck(t, presc);
                    if(tCom.getKeys().size() > 1 )
                    {
                        String CheckUUID = null;
                        StringBuffer sbfr = new StringBuffer();
                        for(String key : tCom.getKeys())
                        {
                            //PRESCCHECKINFO  PRESCCHECKFLAG 0 没有问题  1 有问题
                            if("UUID".equals(key))
                            {
                                CheckUUID = tCom.get("uuid");
                            }
                            else
                            {
                                sbfr.append(tCom.get(key)).append(",");
                            }
                        }
                        if(sbfr.length() > 0) sbfr.deleteCharAt(sbfr.length() - 1); 
                        sql = "update presc set Presccheckid = '" + CheckUUID + "' , PRESCCHECKFLAG = '1' , PRESCCHECKINFO = '" + sbfr.toString() + "' where id = '" + t.get("id") + "'" ;
                    }
                    else
                    {
                        sql = "update presc set PRESCCHECKFLAG = '0' where id = '" + t.get("id") + "'";
                    }
                    if(query.update(sql) == 0)
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
                catch(Exception e)
                {
                    Log(" 问题处方id号 ：" + t.get("id"));
                    e.printStackTrace();
                }
            }
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            cmr = null;
            Log(" 处方审查完成.... " + CheckCount + "个评价完成 ");
        }
    }
}
