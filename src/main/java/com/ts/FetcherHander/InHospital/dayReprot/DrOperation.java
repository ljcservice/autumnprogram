package com.ts.FetcherHander.InHospital.dayReprot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.Transaction.TransaCallback;
import com.hitzd.Transaction.TransactionTemp;
import com.hitzd.Utils.CalcuateAgeUtil;
import com.hitzd.his.ReportBuilder.Interfaces.IReportBuilder;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.his.casehistory.helper.CaseHistoryFactory;
import com.hitzd.his.casehistory.helper.CaseHistoryFunction;
import com.hitzd.his.casehistory.helper.CaseHistoryHelperUtils;
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;
import com.hitzd.his.task.Task;
import com.ts.util.LoggerFileSaveUtil;

public class DrOperation implements IReportBuilder
{
    private Logger              logger              = Logger
            .getLogger(DrOperation.class);
    private List<TCommonRecord> operationArray      = new ArrayList<TCommonRecord>();
    private List<TCommonRecord> operationMasterList = new ArrayList<TCommonRecord>();

    @Override
    public String BuildReport(String ADate, Task AOwner)
    {
        this.buildBegin(ADate, AOwner);
        logger.info("正在查询" + ADate + "手术记录");
        JDBCQueryImpl his = DBQueryFactory.getQuery("HIS");
        // 使用中间层
        genSqlByMiddleWareToMethodBuildReport(ADate, his);
        // 处理手术信息
        handleOperationInfo(ADate, his);
        String ErrorInfo = "";
        if (operationArray != null && operationArray.size() > 0)
        {
            logger.info(ADate + "共有" + operationArray.size() + "条手术记录！");
            this.buildOver(ADate, AOwner);
        }
        his = null;
        if (ErrorInfo.length() == 0)
            return "处理手术信息";
        else
            return ErrorInfo;
    }

    /**
     * 抓取出院病人手术信息
     * 
     * @param ADate
     * @param his
     */
    @SuppressWarnings ("unchecked")
    private void genSqlByMiddleWareToMethodBuildReport(String ADate,
            JDBCQueryImpl his)
    {
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
        String fields = "o.PATIENT_ID, o.VISIT_ID, o.OPERATION_NO, o.OPERATION_DESC, o.OPERATION_CODE, o.HEAL, o.WOUND_GRADE,o.OPERATING_DATE, o.ANAESTHESIA_METHOD, o.OPERATOR, o.FIRST_ASSISTANT, o.SECOND_ASSISTANT, o.ANESTHESIA_DOCTOR,p.DEPT_ADMISSION_TO";
        String tableNames = "medrec.operation o,medrec.pat_visit p";
        List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
        TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("o.patient_id",
                "p.patient_id", "", "join", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("o.visit_id", "p.visit_id",
                "", "join", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("p.discharge_date_time",
                CaseHistoryFunction.genRToDate("medrec.pat_visit",
                        "discharge_date_time", "'" + ADate + "'", "yyyy-mm-dd"),
                "", ">=", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("p.discharge_date_time",
                CaseHistoryFunction.genRToDate("medrec.pat_visit",
                        "discharge_date_time",
                        "'" + DateUtils.getDateAdded(1, ADate) + "'",
                        "yyyy-mm-dd"),
                "", "<", "", "");
        lsWheres.add(where);

        String sql = chhr.genSQL(fields, tableNames, lsWheres, null, null);
        operationArray = his.query(sql.toString(), new CommonMapper());
        DictCache deptCache = DictCache.getNewInstance();
        for (TCommonRecord data : operationArray)
        {
            // 避免转科做的手术无法准确统计到指定科室上面
            getTransfer(data.get("OPERATING_DATE"), data.get("patient_id"), data.get("visit_id"), data);
            String deptCode = !"".equals(data.get("DEPT_STAYED"))?data.get("DEPT_STAYED"):data.get("dept_admission_to");
//            String doctor   = "";
            data.set("id", getUUID());// 提前加入ID方便后续使用
            data.set("dept_code", deptCode);
            data.set("dept_name", deptCache.getDeptName(deptCode));
        }
    }

    /**
     * 转科信息
     * @param opertionDate 手术时间 
     * @param p_id
     * @param v_id
     * @param cr
     * @return
     */
    public void getTransfer(String opertionDate, String p_id, String v_id,
            TCommonRecord cr)
    {
        boolean ret = false;
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
        try
        {
            Date  dd = DateUtils.getDateFromString(opertionDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dd);
            
            String strFields = "*";
            List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
            TCommonRecord where = CaseHistoryHelperUtils
                    .genWhereCR("PATIENT_ID", p_id, "Char", "", "", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("VISIT_ID", v_id, "Char",
                    "", "", "");
            lsWheres.add(where);
            // 出科时间
            where = CaseHistoryHelperUtils.genWhereCR("DISCHARGE_DATE_TIME",  
                    CaseHistoryFunction.genRToDate("MEDREC.TRANSFER", "DISCHARGE_DATE_TIME", "'" + DateUtils.getDate(cal) + "'", "yyyy-mm-dd"),
                    "", ">=", "", "");
            lsWheres.add(where);
            // 入科时间 
            where = CaseHistoryHelperUtils.genWhereCR("ADMISSION_DATE_TIME",  
                    CaseHistoryFunction.genRToDate("MEDREC.TRANSFER", "ADMISSION_DATE_TIME", "'" + DateUtils.getDate(cal) + "'", "yyyy-mm-dd"),
                    "", "<=", "", "");
            lsWheres.add(where);
            List<TCommonRecord> list = chhr.fetchTransfer2CR(strFields,
                    lsWheres, null, null, null);
            // 如果没找记录直接返回
            if (list == null || list.size() == 0)
                return ;
            TCommonRecord tComm = list.get(0);
            cr.set("DEPT_STAYED",
                    tComm.getDateTimeString("DEPT_STAYED"));
            cr.set("DOCTOR_IN_CHARGE",
                    tComm.getDateTimeString("DOCTOR_IN_CHARGE"));
        }
        catch (Exception e)
        {
            logger.error("DrOperation.class,mothed=getTransfer()" + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            chhr = null;
        }
        return ;
    }
    
    
    /**
     * 处理手术信息 包括抓取手术主表中的信息,计算手术中是否存在抗菌药
     * 
     */
    private void handleOperationInfo(String ADate, JDBCQueryImpl his)
    {
        int has_anti = 0;
        for (TCommonRecord o : operationArray)
        {
            // 2014-08-28 修改liujc 预防使用抗菌药物时间识别为 手术当天以及前一天 出现抗菌药物 记为预防使用抗菌药物
            List<TCommonRecord> order = null;
            ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
            String strFields = "patient_id,visit_id,order_no,order_sub_no,order_code,start_date_time,repeat_indicator";
            List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
            TCommonRecord where = CaseHistoryHelperUtils.genWhereCR(
                    "order_class", Config.getParamValue("Drug_In_Order"),
                    "Char", "", "", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("order_status", "2", "",
                    "", "1", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("order_status", "3", "",
                    "", "1", "or");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("patient_id",
                    o.get("patient_id"), "Char", "", "", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("visit_id",
                    o.get("visit_id"), "Char", "", "", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("repeat_indicator", "0",
                    "Char", "", "2", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("start_date_time",CaseHistoryFunction.genRToDate("ordadm.orders","start_date_time"
                            , "'" + DateUtils.getDateAdded(-1,o.getDateString("OPERATING_DATE"))+ "'","yyyy-mm-dd"),"", ">=", "2", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils
                    .genWhereCR("start_date_time",
                            CaseHistoryFunction.genRToDate("ordadm.orders",
                                    "start_date_time", "'"
                                            + DateUtils.getDateAdded(1,
                                                    o.getDateString(
                                                            "OPERATING_DATE"))
                                            + "'",
                                    "yyyy-mm-dd"),
                            "", "<=", "2", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("repeat_indicator", "1",
                    "Char", "", "2", "or");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils
                    .genWhereCR("stop_date_time",
                            CaseHistoryFunction.genRToDate("ordadm.orders",
                                    "stop_date_time", "'"
                                            + DateUtils.getDateAdded(-1,
                                                    o.getDateString(
                                                            "OPERATING_DATE"))
                                            + "'",
                                    "yyyy-mm-dd"),
                            "", ">=", "2", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils
                    .genWhereCR("start_date_time",
                            CaseHistoryFunction.genRToDate("ordadm.orders",
                                    "start_date_time", "'"
                                            + DateUtils.getDateAdded(1,
                                                    o.getDateString(
                                                            "OPERATING_DATE"))
                                            + "'",
                                    "yyyy-mm-dd"),
                            "", "<=", "2", "");
            lsWheres.add(where);
            try
            {
                order = chhr.fetchOrders2CR(strFields, lsWheres, null, null,
                        null);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            if (order != null && order.size() != 0)
            {
                for (int i = 0; i < order.size(); i++)
                {
                    TCommonRecord drug = order.get(i);
                    if (DrugUtils.isKJDrug(drug.get("order_code")))
                    {
                        has_anti = 1;
                        break;
                    }
                }
            }
            o.set("has_anti", has_anti + "");
            has_anti = 0;
            // 临时关掉 liujc 2018-05-20
            //fetchOpertaionMaster(o, chhr, his);
        }
    }

    @SuppressWarnings ("unchecked")
    private void fetchOpertaionMaster(TCommonRecord opertionInfo,
            ICaseHistoryHelper chhr, JDBCQueryImpl his)
    {
        List<TCommonRecord> curPatientOperationMaster = new ArrayList<TCommonRecord>();
        String fields = "m.THIRD_SUPPLY_NURSE,m.END_INDICATOR,m.ORDER_TRANSFER,m.CHARGE_TRANSFER,m.RECK_GROUP,"
                + "m.BLOOD_WHOLE_SELF,m.BLOOD_WHOLE,m.BLOOD_RBC,m.BLOOD_PLASM,m.BLOOD_OTHER,m.RESERVED9,m.PATIENT_ID,m.VISIT_ID,m.OPER_ID,"
                + "m.DEPT_STAYED,m.OPERATING_ROOM,m.OPERATING_ROOM_NO,m.DIAG_BEFORE_OPERATION,m.PATIENT_CONDITION,m.OPERATION_SCALE,m.DIAG_AFTER_OPERATION,"
                + "m.EMERGENCY_INDICATOR,m.ISOLATION_INDICATOR,m.OPERATION_CLASS,m.OPERATING_DEPT,m.SURGEON,m.FIRST_ASSISTANT,m.SECOND_ASSISTANT,"
                + "m.THIRD_ASSISTANT,m.FOURTH_ASSISTANT,m.ANESTHESIA_METHOD,m.ANESTHESIA_DOCTOR,m.ANESTHESIA_ASSISTANT,m.BLOOD_TRAN_DOCTOR,"
                + "m.FIRST_OPERATION_NURSE,m.SECOND_OPERATION_NURSE,m.FIRST_SUPPLY_NURSE,m.SECOND_SUPPLY_NURSE,m.NURSE_SHIFT_INDICATOR,"
                + "m.START_DATE_TIME,m.END_DATE_TIME,m.SATISFACTION_DEGREE,m.SMOOTH_INDICATOR,m.IN_FLUIDS_AMOUNT,m.OUT_FLUIDS_AMOUNT,m.BLOOD_LOSSED,"
                + "m.BLOOD_TRANSFERED,m.ENTERED_BY,m.ANES_START_DATE_TIME,m.RETURN_DATE_TIME,m.OPER_STATUS,m.SECOND_ANESTHESIA_ASSISTANT,"
                + "m.THIRD_ANESTHESIA_ASSISTANT,m.FOURTH_ANESTHESIA_ASSISTANT,m.OPERATION_POSITION,m.OPERATION_EQUIP_INDICATOR,m.SECOND_ANESTHESIA_DOCTOR,"
                + "m.THIRD_ANESTHESIA_DOCTOR,m.OTHER_IN_AMOUNT,m.OTHER_OUT_AMOUNT,m.IN_DATE_TIME,m.OUT_DATE_TIME,m.RESERVED1,m.RESERVED2,"
                + "m.SPECIAL_EQUIPMENT,m.SPECIAL_INFECT,m.HEPATITIS_INDICATOR,n.wound_grade,n.operation";
        String tableNames = "SURGERY.operation_master m , SURGERY.Operation_Name n";
        List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
        TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("m.patient_id",
                "n.patient_id", "", "join", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("m.visit_id", "n.visit_id",
                "", "join", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("m.oper_id", "n.oper_id", "",
                "join", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("m.patient_id",
                opertionInfo.get("patient_id"), "Char", "=", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("m.visit_id",
                opertionInfo.get("visit_id"), "Char", "=", "", "");
        lsWheres.add(where);
        String sql = chhr.genSQL(fields, tableNames, lsWheres, null, null);
        curPatientOperationMaster = his.query(sql.toString(),
                new CommonMapper());
        if (null != curPatientOperationMaster
                && curPatientOperationMaster.size() > 0)
        {
            DictCache deptCache = DictCache.getNewInstance();
            for (TCommonRecord data : curPatientOperationMaster)
            {
                data.set("dept_name",
                        deptCache.getDeptName(data.get("operating_dept")));
                data.set("has_anti", opertionInfo.get("has_anti"));
                if (data.getDateString("start_date_time")
                        .equals(opertionInfo.getDateString("operating_date"))
                        && data.get("SURGEON")
                                .equals(opertionInfo.get("operator")))
                {
                    if (!"".equals(data.get("DEPT_STAYED")))
                    {
                        opertionInfo.set("dept_code", data.get("DEPT_STAYED")); // 设置病人所在科室
                        opertionInfo.set("dept_name",
                                deptCache.getDeptName(data.get("DEPT_STAYED")));// 设置病人所在科室名称
                    }
                    data.set("operation_id", opertionInfo.get("id"));// 将operation与operation_master表关联
                }
            }
            operationMasterList.addAll(curPatientOperationMaster);
        }
    }

    private void saveResultSet(String ADate)
    {
        TCommonRecord tc = new TCommonRecord();
        tc.set("ADate", ADate);
        TransactionTemp tt = new TransactionTemp("ph");
        tt.execute(new TransaCallback(tc) {
            public void ExceuteSqlRecord()
            {
                JDBCQueryImpl ph = DBQueryFactory.getQuery("ph");
                for (int j = 0; j < operationArray.size(); j++)
                {
                    logger.info("保存第-----------------" + (j + 1)
                            + "---------------共---------------"
                            + operationArray.size() + "手术信息");
                    TCommonRecord o = operationArray.get(j);
                    int result = ph.queryForInt(
                            "select count(*) from Operation where PATIENT_ID='"
                                    + o.get("PATIENT_ID") + "' and VISIT_ID ='"
                                    + o.get("VISIT_ID") + "' and OPERATION_NO='"
                                    + o.get("OPERATION_NO") + "'");
                    if (result == 0)
                    {
                        List<Object> sqlParams = new ArrayList<Object>();
                        String dstSQL = "Insert into Operation (ID,PATIENT_ID, VISIT_ID,OPERATION_NO,has_anti , OPERATION_DESC, OPERATION_CODE, HEAL, WOUND_GRADE, "
                                + "OPERATING_DATE, ANAESTHESIA_METHOD, OPERATOR, FIRST_ASSISTANT, SECOND_ASSISTANT, ANESTHESIA_DOCTOR ,DEPT_CODE,DEPT_NAME , LINK_DATE) "
                                + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        sqlParams.add(o.get("id"));
                        sqlParams.add(o.get("PATIENT_ID"));
                        sqlParams.add(o.get("VISIT_ID"));
                        sqlParams.add(o.get("OPERATION_NO"));
                        sqlParams.add(o.get("has_anti"));
                        sqlParams.add(o.get("OPERATION_DESC"));
                        sqlParams.add(o.get("OPERATION_CODE"));
                        sqlParams.add(o.get("HEAL"));
                        sqlParams.add(o.get("WOUND_GRADE"));
                        Timestamp dateTime = new Timestamp(DateUtils
                                .getDateFromString(o.get("OPERATING_DATE"))
                                .getTime());
                        sqlParams.add(dateTime);
                        sqlParams.add(o.get("ANAESTHESIA_METHOD"));
                        sqlParams.add(o.get("OPERATOR"));
                        sqlParams.add(o.get("FIRST_ASSISTANT"));
                        sqlParams.add(o.get("SECOND_ASSISTANT"));
                        sqlParams.add(
                                o.get("ANESTHESIA_DOCTOR").replace("'", ""));
                        sqlParams.add(o.get("dept_code"));
                        sqlParams.add(o.get("DEPT_NAME"));
                        dateTime = new Timestamp(DateUtils
                                .getDateFromString(getTranParm().get("ADate"))
                                .getTime());
                        sqlParams.add(dateTime);
                        ph.update(dstSQL, sqlParams.toArray());
                    }
                }
                ph = null;
            }
        });
    }

    private void saveOperationMaster(String ADate)
    {
        TCommonRecord tc = new TCommonRecord();
        tc.set("ADate", ADate);
        TransactionTemp tt = new TransactionTemp("ph");
        tt.execute(new TransaCallback(tc) {
            @Override
            public void ExceuteSqlRecord()
            {
                JDBCQueryImpl ph = DBQueryFactory.getQuery("ph");
                for (int j = 0; j < operationMasterList.size(); j++)
                {
                    logger.info("第--------" + (j + 1) + "--------共--------"
                            + operationMasterList.size()
                            + "手术Operation_Master");
                    TCommonRecord o = operationMasterList.get(j);
                    List<Object> sqlParams = new ArrayList<Object>();
                    String dstSQL = "Insert into Operation_Master ("
                            + "PATIENT_ID, VISIT_ID, OPER_ID, DEPT_STAYED, OPERATING_ROOM, OPERATING_ROOM_NO, DIAG_BEFORE_OPERATION, "
                            + "PATIENT_CONDITION, OPERATION_SCALE, DIAG_AFTER_OPERATION, EMERGENCY_INDICATOR, ISOLATION_INDICATOR, OPERATION_CLASS, "
                            + "OPERATING_DEPT, DEPT_NAME, CLINIC_ATTR, OUTP_OR_INP, INTERNAL_OR_SERGERY, SURGEON, FIRST_ASSISTANT, SECOND_ASSISTANT, THIRD_ASSISTANT, FOURTH_ASSISTANT, ANESTHESIA_METHOD, "
                            + "ANESTHESIA_DOCTOR, ANESTHESIA_ASSISTANT, BLOOD_TRAN_DOCTOR, FIRST_OPERATION_NURSE, SECOND_OPERATION_NURSE, FIRST_SUPPLY_NURSE, "
                            + "SECOND_SUPPLY_NURSE, NURSE_SHIFT_INDICATOR, START_DATE_TIME, END_DATE_TIME, SATISFACTION_DEGREE, SMOOTH_INDICATOR, "
                            + "IN_FLUIDS_AMOUNT, OUT_FLUIDS_AMOUNT, BLOOD_LOSSED, BLOOD_TRANSFERED, ENTERED_BY,has_anti,wound_grade,operationName, LINK_DATE,OPERATION_ID) "
                            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    sqlParams.add(o.get("PATIENT_ID"));
                    sqlParams.add(o.get("VISIT_ID"));
                    sqlParams.add(o.get("OPER_ID"));
                    sqlParams.add(o.get("DEPT_STAYED"));
                    sqlParams.add(o.get("OPERATING_ROOM"));
                    sqlParams.add(o.get("OPERATING_ROOM_NO"));
                    sqlParams.add(
                            o.get("DIAG_BEFORE_OPERATION").replace("'", ""));
                    sqlParams.add(o.get("PATIENT_CONDITION"));
                    sqlParams.add(o.get("OPERATION_SCALE"));
                    sqlParams.add(o.get("DIAG_AFTER_OPERATION"));
                    sqlParams.add(o.get("EMERGENCY_INDICATOR"));
                    sqlParams.add(o.get("ISOLATION_INDICATOR"));
                    sqlParams.add(o.get("OPERATION_CLASS"));
                    sqlParams.add(o.get("OPERATING_DEPT"));
                    sqlParams.add(o.get("DEPT_NAME"));
                    sqlParams.add(o.get("CLINIC_ATTR"));
                    sqlParams.add(o.get("OUTP_OR_INP"));
                    sqlParams.add(o.get("INTERNAL_OR_SERGERY"));
                    sqlParams.add(o.get("SURGEON"));
                    sqlParams.add(o.get("FIRST_ASSISTANT"));
                    sqlParams.add(o.get("SECOND_ASSISTANT"));
                    sqlParams.add(o.get("THIRD_ASSISTANT"));
                    sqlParams.add(o.get("FOURTH_ASSISTANT"));
                    sqlParams.add(o.get("ANESTHESIA_METHOD"));
                    sqlParams.add(o.get("ANESTHESIA_DOCTOR"));
                    sqlParams.add(o.get("ANESTHESIA_ASSISTANT"));
                    sqlParams.add(o.get("BLOOD_TRAN_DOCTOR"));
                    sqlParams.add(o.get("FIRST_OPERATION_NURSE"));
                    sqlParams.add(o.get("SECOND_OPERATION_NURSE"));
                    sqlParams.add(o.get("FIRST_SUPPLY_NURSE"));
                    sqlParams.add(o.get("SECOND_SUPPLY_NURSE"));
                    sqlParams.add(o.get("NURSE_SHIFT_INDICATOR"));
                    Timestamp dateTime = new Timestamp(
                            DateUtils
                                    .getDateFromString(o.get("START_DATE_TIME"),
                                            DateUtils.FORMAT_DATETIME)
                                    .getTime());
                    sqlParams.add(dateTime);
                    dateTime = new Timestamp(
                            DateUtils
                                    .getDateFromString(o.get("END_DATE_TIME"),
                                            DateUtils.FORMAT_DATETIME)
                                    .getTime());
                    sqlParams.add(dateTime);
                    sqlParams.add(o.get("SATISFACTION_DEGREE"));
                    sqlParams.add(o.get("SMOOTH_INDICATOR"));
                    sqlParams.add(o.get("IN_FLUIDS_AMOUNT"));
                    sqlParams.add(o.get("OUT_FLUIDS_AMOUNT"));
                    sqlParams.add(o.get("BLOOD_LOSSED"));
                    sqlParams.add(o.get("BLOOD_TRANSFERED"));
                    sqlParams.add(o.get("ENTERED_BY"));
                    sqlParams.add(o.get("has_anti"));
                    sqlParams.add(o.get("wound_grade"));
                    sqlParams.add(o.get("operation"));
                    dateTime = new Timestamp(DateUtils
                            .getDateFromString(getTranParm().get("ADate"))
                            .getTime());
                    sqlParams.add(dateTime);
                    sqlParams.add(o.get("operation_id"));

                    ph.update(dstSQL, sqlParams.toArray());
                }
                ph = null;
            }
        });
        operationMasterList.clear();
    }

    private void deleteRepeatOperationMasterInfo()
    {
        Map<String, TCommonRecord> resultMap = new HashMap<String, TCommonRecord>();
        for (TCommonRecord tCommonRecord : operationMasterList)
        {
            resultMap.put(tCommonRecord.get("patient_id") + "_"
                    + tCommonRecord.get("visit_id") + "_"
                    + tCommonRecord.get("oper_id"), tCommonRecord);
        }
        operationMasterList.clear();
        operationMasterList.addAll(resultMap.values());
        resultMap.clear();
    }

    private void deleteOldOperationMaster(JDBCQueryImpl queryPH, String ADate)
    {
        logger.info("删除Operation_Master旧数据");
        queryPH.update("delete Operation_Master t where t.link_date = to_date('"
                + ADate + "','yyyy-mm-dd')");
    }

    public void deleteOldOperation(JDBCQueryImpl queryPH, String ADate)
    {
        logger.info("删除手术旧数据");
        queryPH.update("delete Operation t where t.link_date = to_date('"
                + ADate + "','yyyy-mm-dd')");
    }

    @Override
    public String getLogFileName()
    {
        return null;
    }

    @Override
    public void buildBegin(String ADate, Task owner)
    {
        LoggerFileSaveUtil.LogFileSave(logger,
                "APPLOG\\DR_Operaction\\DR_Operaction" + ADate + ".log");
    }

    @Override
    public void buildOver(String ADate, Task owner)
    {
        JDBCQueryImpl ph = DBQueryFactory.getQuery("ph");
        deleteOldOperation(ph, ADate);

        logger.info("开始保存手术数据....");
        this.saveResultSet(ADate);
        logger.info("手术数据保存结束....");
// 暂时关掉 liujc 20180520 
//        deleteOldOperationMaster(ph, ADate);
//        deleteRepeatOperationMasterInfo();// 去除operation_master记录中重复的项
//        logger.info("开始保存Operation_Master数据....");
//        this.saveOperationMaster(ADate);
//        logger.info("Operation_Master数据保存结束....");
    }

    @Override
    public String BuildReportWithCR(String ADate, TCommonRecord crPatInfo,
            Task AOwner)
    {
        return null;
    }

    /**
     * 获取UUID的函数
     * 
     * @return
     */
    public static String getUUID()
    {
        String uuid = UUID.randomUUID().toString();// 获取随机唯一标识符
        // 去掉标识符中的"-"
        uuid = uuid.substring(0, 8) + uuid.substring(9, 13)
                + uuid.substring(14, 18) + uuid.substring(19, 23)
                + uuid.substring(24);
        return uuid;
    }
}
