package com.ts.FetcherHander.InHospital.dayReprot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.Transaction.TransaCallback;
import com.hitzd.Transaction.TransactionTemp;
import com.hitzd.Utils.StringUtils;
import com.hitzd.his.DDD.DDDUtils;
import com.hitzd.his.ReportBuilder.Interfaces.IReportBuilder;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.his.casehistory.helper.CaseHistoryFactory;
import com.hitzd.his.casehistory.helper.CaseHistoryFunction;
import com.hitzd.his.casehistory.helper.CaseHistoryHelperUtils;
import com.hitzd.his.casehistory.helper.CaseHistoryUtils;
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;
import com.hitzd.his.task.Task;
import com.ts.util.LoggerFileSaveUtil;
import com.ts.util.UuidUtil;

/**
 * 适用于307医院,304医院，北京军区总院，上海长征医院
 * 
 * @author 
 *
 */
public class DrDeptDoctDrugEx implements IReportBuilder
{
    private Logger logger    = Logger.getLogger("DrDeptDoctDrugEx");
    private List<TCommonRecord> resultSet = new ArrayList<TCommonRecord>();
    private List<TCommonRecord> supplier  = new ArrayList<TCommonRecord>();

    public String BuildReportWithCR(String ADate, TCommonRecord crPatInfo,
            Task AOwner)
    {
        JDBCQueryImpl query = DBQueryFactory.getQuery("HIS");
        logger.info("处理药品数据附加信息:" + ADate + "日报数据");

        for (TCommonRecord cr : this.resultSet)
        {
            if (supplier == null || supplier.size() == 0)
            {
                supplier = StringUtils.getSupplier();
            }
            StringUtils.execCF(supplier, cr);
            // 是否抗菌药
            if (DrugUtils.isKJDrug(cr.get("item_code")))
            {
                cr.set("is_anti", "1");
            }
            // 国家药物基本目录
            cr.set("is_basedrug", "0");
            if (DrugUtils.isCountryBase(cr.get("item_code"),
                    cr.get("item_spec")))
            {
                cr.set("is_basedrug", "1");
            }
            setDoctor(query, cr);
        }
        return "";
    }

    public void setDoctor(JDBCQueryImpl query, TCommonRecord cr)
    {
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
        if ("".equals(cr.get("doctor_name")))
        {
            List<TCommonRecord> list = null;
            String strFields = "DOCTOR_IN_CHARGE";
            List<TCommonRecord> lsWheres1 = new ArrayList<TCommonRecord>();
            TCommonRecord where1 = CaseHistoryHelperUtils.genWhereCR(
                    "patient_id", cr.get("patient_id"), "Char", "", "", "");
            lsWheres1.add(where1);
            where1 = CaseHistoryHelperUtils.genWhereCR("visit_id",
                    cr.get("visit_id"), "Char", "", "", "");
            lsWheres1.add(where1);
            try
            {
                list = chhr.fetchPatVisit2CR(strFields, lsWheres1, null, null,
                        null);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            String doctor = "";
            if ((list != null) && (list.size() > 0))
            {
                if (!"".equals(
                        ((TCommonRecord) list.get(0)).get("DOCTOR_IN_CHARGE")))
                    doctor = ((TCommonRecord) list.get(0))
                            .get("DOCTOR_IN_CHARGE");
            }
            if ("".equals(doctor))
            {
                try
                {
                    list = chhr.fetchOrders2CR(cr.get("patient_id"),
                            cr.get("visit_id"), null).subList(0, 1);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                if ((list != null) && (list.size() > 0))
                    doctor = ((TCommonRecord) list.get(0)).get("doctor");
            }
            cr.set("doctor_Name", doctor);
            chhr = null;
        }
    }

    public String BuildReport(String ADate, Task AOwner)
    {
        buildBegin(ADate, AOwner);
        logger.info("正在汇总" + ADate + "的账单记录！药品统计");
        JDBCQueryImpl queryHIS = DBQueryFactory.getQuery("HIS");
        getResultSetByMiddleWare(ADate, queryHIS);

        String ErrorInfo = "";
        if ((this.resultSet != null) && (this.resultSet.size() > 0))
        {
            logger.info(ADate + "共有" + this.resultSet.size() + "条汇总记录！药品统计");
            ErrorInfo = BuildReportWithCR(ADate, null, AOwner);
            buildOver(ADate, AOwner);
        }
        queryHIS = null;
        if (ErrorInfo.length() == 0)
        {
            return "构建日报结束";
        }
        return ErrorInfo;
    }

    private void getResultSetByMiddleWare(String ADate, JDBCQueryImpl hisQuery)
    {
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
        DictCache dc = DictCache.getNewInstance();
        // 2013-12-11 为青岛添加FIRM_ID查询字段，在已经在field_config中添加映射字段，军卫的HIS请直接设置为''
        List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
        String fields = CaseHistoryFunction.genSum("inpbill.inp_bill_detail",
                "costs", "costs")
                + ","
                + CaseHistoryFunction.genSum("inpbill.inp_bill_detail",
                        "charges", "charges")
                + ","
                + CaseHistoryFunction.genSum("inpbill.inp_bill_detail",
                        "amount", "amount")
                + ","
                + "ordered_by,item_name,item_code,item_spec,units,patient_id,visit_id,firm_id";
        TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("item_class",
                Config.getParamValue("Drug_In_Order"), "Char", "", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR(
                CaseHistoryFunction.genToChar("inpbill.inp_bill_detail",
                        "billing_date_time", "yyyy-MM-dd", ""),
                ADate, "Char", ">=", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR(
                CaseHistoryFunction.genToChar("inpbill.inp_bill_detail",
                        "billing_date_time", "yyyy-MM-dd", ""),
                DateUtils.getDateAdded(1, ADate), "Char", "<", "", "");
        lsWheres.add(where);
        List<TCommonRecord> lsGroups = new ArrayList<TCommonRecord>();
        TCommonRecord group = CaseHistoryHelperUtils.genGroupCR("ordered_by");
        lsGroups.add(group);
        group = CaseHistoryHelperUtils.genGroupCR("item_name");
        lsGroups.add(group);
        group = CaseHistoryHelperUtils.genGroupCR("item_code");
        lsGroups.add(group);
        group = CaseHistoryHelperUtils.genGroupCR("item_spec");
        lsGroups.add(group);
        group = CaseHistoryHelperUtils.genGroupCR("patient_id");
        lsGroups.add(group);
        group = CaseHistoryHelperUtils.genGroupCR("visit_id");
        lsGroups.add(group);
        group = CaseHistoryHelperUtils.genGroupCR("units");
        lsGroups.add(group);
        group = CaseHistoryHelperUtils.genGroupCR("firm_id");
        lsGroups.add(group);
        try
        {
            this.resultSet = chhr.fetchInpBillDetail2CR(fields, lsWheres,
                    lsGroups, null, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // 填入部门信息
        for (TCommonRecord data : resultSet)
        {
            data.set("dept_code", data.get("ordered_by"));
            data.set("dept_name", dc.getDeptName(data.get("ordered_by")));
        }

        String strFields = "";
        // SQLServer 关键字
        if ("SQLServer".equals(CaseHistoryUtils
                .getTableConfig("MedRec.pat_visit").get("DB_NAME")))
        {
            strFields = "'identity',charge_type,attending_doctor";
        }
        else
        {
            strFields = "identity,charge_type,attending_doctor";
        }

        List<TCommonRecord> lsWheres1 = new ArrayList<TCommonRecord>();
        List<TCommonRecord> patVisitList = null;
        // 填入身份、医生、费别信息
        for (TCommonRecord data : resultSet)
        {
            TCommonRecord where1 = CaseHistoryHelperUtils.genWhereCR(
                    "patient_id", data.get("patient_id"), "Char", "", "", "");
            lsWheres1.add(where1);
            where1 = CaseHistoryHelperUtils.genWhereCR("visit_id",
                    data.get("visit_id"), "Char", "", "", "");
            lsWheres1.add(where1);
            try
            {
                patVisitList = chhr.fetchPatVisit2CR(strFields, lsWheres1, null,
                        null, null);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            data.set("doctor_name",
                    getDoctorInCharge(data.get("patient_id"),
                            data.get("visit_id"), data.get("ordered_by"))
                                    .get("DOCTOR_IN_CHARGE"));
            if (patVisitList.size() > 0)
            {
                data.set("identity", patVisitList.get(0).get("identity"));
                data.set("charge_type", patVisitList.get(0).get("charge_type"));
                if ("".equals(data.get("doctor_name")))
                {
                    data.set("doctor_name",
                            patVisitList.get(0).get("attending_doctor"));
                }
            }
            lsWheres1.clear();
        }

        // 填入药品信息
        for (TCommonRecord data : resultSet)
        {
            data.set("drug_form",
                    dc.getDrugDictInfo(data.get("item_code")).get("drug_form"));
        }
    }

    public TCommonRecord getDoctorInCharge(String p_id, String v_id,
            String deptCode)
    {
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
        List<TCommonRecord> list = new ArrayList<TCommonRecord>();
        try
        {
            String strFields = "*";
            List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
            TCommonRecord where = CaseHistoryHelperUtils
                    .genWhereCR("PATIENT_ID", p_id, "Char", "", "", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("VISIT_ID", v_id, "Char",
                    "", "", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("DEPT_STAYED", deptCode,
                    "Char", "", "", "");
            lsWheres.add(where);
            list = chhr.fetchTransfer2CR(strFields, lsWheres, null, null, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            chhr = null;
        }
        return (list == null || list.size() == 0) ? new TCommonRecord()
                : list.get(0);
    }

    public void saveDeptDrug(JDBCQueryImpl queryIAS)
    {
        logger.info("保存部门药品使用统计日报");
        String sql = " insert into dr_Dept_Drug (is_basedrug ,CHARGES, Rpt_Date, Dept_Code, Dept_Name,  Drug_Code, Drug_Name, costs, Amount, Drug_Units, DRUG_SPEC ,IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form)  select is_basedrug ,sum(CHARGES)CHARGES, Rpt_Date, Dept_Code, Dept_Name,  Drug_Code, Drug_Name, sum(costs)costs, sum(Amount)amount, Drug_Units, DRUG_SPEC ,IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form  from DR_DRUG_SUMMARY_OUT t group by Rpt_Date, Dept_Code, Dept_Name,  Drug_Code, Drug_Name, Drug_Units, DRUG_SPEC ,IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form ,is_basedrug";

        queryIAS.update(sql);

        sql = " insert into dr_Dept_Drug_anti (is_basedrug ,CHARGES, Rpt_Date, Dept_Code, Dept_Name,  Drug_Code, Drug_Name, costs, Amount, Drug_Units, DRUG_SPEC, IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form)  select is_basedrug ,sum(CHARGES)CHARGES, Rpt_Date, Dept_Code, Dept_Name,  Drug_Code, Drug_Name, sum(costs)costs, sum(Amount)amount, Drug_Units, DRUG_SPEC, IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form from DR_DRUG_SUMMARY_OUT t where is_anti=1  group by Rpt_Date, Dept_Code, Dept_Name,  Drug_Code, Drug_Name,Drug_Units, DRUG_SPEC, IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form ,is_basedrug";

        queryIAS.update(sql);
        sql = null;
    }

    public void saveDeptDoctDrug(JDBCQueryImpl queryIAS)
    {
        logger.info("保存部门医生药品使用统计日报");
        String sql = "insert into dr_Dept_doctor_Drug (is_basedrug ,CHARGES, Rpt_Date, doctor_code, doctor_Name, Dept_Code, Dept_Name,  Drug_Code, Drug_Name, costs, Amount, Drug_Units, DRUG_SPEC, IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form)  select is_basedrug ,sum(CHARGES)CHARGES, Rpt_Date, doctor_code, doctor_Name, Dept_Code, Dept_Name,  Drug_Code, Drug_Name, sum(costs)costs, sum(Amount)amount, Drug_Units, DRUG_SPEC, IDENTITY,CHARGE_TYPE, FIRM_ID , drug_form from DR_DRUG_SUMMARY_OUT group by Rpt_Date, doctor_code, doctor_Name, Dept_Code, Dept_Name,  Drug_Code, Drug_Name, Drug_Units, DRUG_SPEC, IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form ,is_basedrug";

        queryIAS.update(sql);

        sql = "insert into dr_Dept_doctor_Drug_anti (is_basedrug ,CHARGES, Rpt_Date, doctor_code, doctor_Name, Dept_Code, Dept_Name, Drug_Code, Drug_Name, costs, Amount, Drug_Units, DRUG_SPEC, IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form) select is_basedrug ,sum(CHARGES)CHARGES, Rpt_Date, doctor_code, doctor_Name, Dept_Code, Dept_Name, Drug_Code, Drug_Name, sum(costs)costs, sum(Amount)amount, Drug_Units, DRUG_SPEC, IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form from DR_DRUG_SUMMARY_OUT where is_anti=1  group by Rpt_Date, doctor_code, doctor_Name, Dept_Code, Dept_Name, Drug_Code, Drug_Name, Drug_Units, DRUG_SPEC, IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form ,is_basedrug";

        queryIAS.update(sql);
        sql = null;
    }

    public void saveDrug(JDBCQueryImpl queryIAS)
    {
        logger.info("保存药品使用统计日报");
        String sql = "insert into dr_Drug (is_basedrug ,CHARGES, Rpt_Date, Drug_Code, Drug_Name, costs, Amount, Drug_Units, DRUG_SPEC, IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form) select is_basedrug ,sum(CHARGES)CHARGES, Rpt_Date, Drug_Code, Drug_Name, sum(costs)costs, sum(Amount)amount, Drug_Units, DRUG_SPEC, IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form from DR_DRUG_SUMMARY_OUT  group by Rpt_Date, Drug_Code, Drug_Name, Drug_Units, DRUG_SPEC, IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form ,is_basedrug";

        queryIAS.update(sql);
        sql = "insert into dr_Drug_anti (is_basedrug ,CHARGES, Rpt_Date, Drug_Code, Drug_Name, costs, Amount, Drug_Units, DRUG_SPEC, IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form)  select is_basedrug ,sum(CHARGES)CHARGES, Rpt_Date, Drug_Code, Drug_Name, sum(costs)costs, sum(Amount)amount, Drug_Units, DRUG_SPEC, IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form from DR_DRUG_SUMMARY_OUT  where is_anti = 1 group by Rpt_Date, Drug_Code, Drug_Name, Drug_Units, DRUG_SPEC, IDENTITY,CHARGE_TYPE, FIRM_ID, drug_form ,is_basedrug";

        queryIAS.update(sql);
        sql = null;
    }

    public void saveAllCosts(JDBCQueryImpl queryIAS)
    {
        logger.info("保存金额总计");
        String sql = "insert into DR_ALL_COSTS (CHARGES,COSTS, Rpt_Date) select sum(CHARGES)CHARGES, sum(COSTS)COSTS, Rpt_Date from DR_DRUG_SUMMARY_OUT group by Rpt_Date";
        queryIAS.update(sql);
        sql = null;
    }

    public void saveDr_Drug_Summary(JDBCQueryImpl queryIAS)
    {
        logger.info("保存患者每天用药信息");
        String sql = " insert into dr_drug_summary(FIRM_ID, drug_code, DRUG_FORM, drug_name, patient_id, visit_id, costs, drug_units, amount, rpt_date, doctor_code, doctor_name, dept_code,  dept_name, drug_spec,  charges, identity, charge_type, is_basedrug ,is_anti,ddd_value)"
                + "select FIRM_ID, drug_code, DRUG_FORM, drug_name, patient_id, visit_id, costs, drug_units, amount, rpt_date, doctor_code, doctor_name, dept_code,  dept_name, drug_spec,  charges, identity, charge_type, is_basedrug ,is_anti ,ddd_value from dr_drug_summary_out";
        queryIAS.update(sql);
        sql = null;
    }

    public void deleteDR_DRUG_SUMMARY_OUT(JDBCQueryImpl queryIAS)
    {
        queryIAS.update("delete DR_DRUG_SUMMARY_OUT");
    }

    public void deleteDR(JDBCQueryImpl query, String Adate)
    {
        logger.info("删除旧数据");
        String where = "where rpt_date = to_date('" + Adate + "','yyyy-mm-dd')";
//        query.update("delete dr_Drug  " + where);
//        query.update("delete dr_Drug_anti  " + where);
        query.update("delete dr_drug_summary  " + where);
//        query.update("delete dr_Dept_doctor_Drug  " + where);
//        query.update("delete dr_Dept_doctor_Drug_anti  " + where);
//        query.update("delete dr_Dept_Drug_anti  " + where);
//        query.update("delete dr_Dept_Drug  " + where);
//        query.update("delete DR_ALL_COSTS  " + where);
        where = null;
    }

    public String getLogFileName()
    {
        return null;
    }

    public void buildBegin(String ADate, Task AOwner)
    {
        LoggerFileSaveUtil.LogFileSave(logger,
                "APPLOG\\dr\\DR_" + ADate + ".log");
    }

    private void saveResultSet(String ADate)
    {
        // 保存过程 修改。
        TCommonRecord tc = new TCommonRecord();
        tc.set("ADate", ADate);
        TransactionTemp tt = new TransactionTemp("IAS");
        tt.execute(new TransaCallback(tc) {
            public void ExceuteSqlRecord()
            {
                JDBCQueryImpl Jquery = DBQueryFactory.getQuery("IAS");
                DrDeptDoctDrugEx.this.deleteDR(Jquery,getTranParm().get("ADate"));
                int i = 1;
                for (TCommonRecord cr : DrDeptDoctDrugEx.this.resultSet)
                {
                    logger.info("第-----" + i + "-----共-----"
                            + DrDeptDoctDrugEx.this.resultSet.size() + "药品统计");
                    List<Object> sqlParams = new ArrayList<Object>();
                    String sql = " insert into dr_drug_summary(drug_code, FIRM_ID, DRUG_FORM, drug_name, patient_id, visit_id, costs, drug_units, amount, rpt_date, doctor_code, doctor_name, dept_code,  dept_name, drug_spec,  charges, identity, charge_type, is_basedrug ,is_anti,DDD_VALUE,"
                            + " is_exhilarant,is_injection,is_oral,anti_level,is_impregnant,IS_NOCHINESEDRUG,is_external,is_chinesedrug,is_tumor,is_poison,is_psychotic,is_habitforming,is_radiation,is_precious,is_danger,is_assist,is_albumin,id ) "
                            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    sqlParams.add(cr.get("item_code"));
                    sqlParams.add(cr.get("FIRM_ID"));
                    sqlParams.add(cr.get("drug_FORM"));
                    sqlParams.add(cr.get("item_name"));
                    sqlParams.add(cr.get("patient_id"));
                    sqlParams.add(cr.get("visit_id"));
                    sqlParams.add(cr.getDouble("costs"));
                    sqlParams.add(cr.get("units"));
                    sqlParams.add(cr.getDouble("amount"));
                    Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(getTranParm().get("ADate")).getTime());
                    sqlParams.add(dateTime);
                    sqlParams.add(cr.get("doctor_code"));
                    sqlParams.add(cr.get("doctor_name"));
                    sqlParams.add(cr.get("dept_code"));
                    sqlParams.add(cr.get("dept_name"));
                    sqlParams.add(cr.get("item_spec"));
                    sqlParams.add(cr.getDouble("charges"));
                    sqlParams.add(cr.get("identity"));
                    sqlParams.add(cr.get("charge_type"));
                    sqlParams.add(cr.get("is_basedrug"));
                    sqlParams.add(cr.getInt("is_anti"));
                    double dddValue  = 0d;
                    if (cr.getInt("is_anti") == 1)
                    {
                        dddValue = DDDUtils.CalcDDD(
                                cr.get("item_code"),
                                cr.get("item_spec"), cr.get("units"),
                                cr.get("Firm_ID"), cr.get("amount"),
                                cr.get("COSTS"));
                        logger.info("存在抗菌药" + cr.get("item_code") + "●●●" + cr.get("item_spec") + "●●●" + dddValue);
                    }
                    // 计算出ddd值 
                    sqlParams.add(dddValue);
                    
                    sqlParams.add(getflag(DrugUtils.isExhilarantDrug(cr.get("item_code"))));       // is_exhilarant '兴奋剂标识，0是非兴奋剂，1是兴奋剂';
                    sqlParams.add(getflag(DrugUtils.isZSDrug(cr.get("item_code"))));               // is_injection '注射剂标识，0是非注射剂，1是注射剂';
                    sqlParams.add(getflag(DrugUtils.IsOralDrug(cr.get("item_code"))));             // is_oral '口服制剂标识，0是非口服，1是口服';
                    sqlParams.add(DrugUtils.getDrugAntiByLevel(cr.get("item_code")));              // anti_level'抗菌药级别，1是非限制用药，2是限制用药，3是特殊用药';
                    sqlParams.add(getflag(DrugUtils.isImpregnant(cr.get("item_code"))) );          // is_impregnant '溶剂标识，0是非溶剂，1是溶剂';
                    sqlParams.add(getflag(!DrugUtils.isChineseDrug(cr.get("item_code"))) );        // IS_NOCHINESEDRUG '药理分类， 0 饮片 1 非饮片 ';
                    sqlParams.add(getflag(DrugUtils.isExternalDrug(cr.get("item_code"))) );        // is_external '外用标识，0是非外用，1是外用';
                    sqlParams.add(getflag(DrugUtils.isChineseDrug(cr.get("item_code"))) );         // is_chinesedrug '中药标识，0是非中药，1是中药';
                    sqlParams.add(getflag(DrugUtils.isAntiAllergyDrug(cr.get("item_code"))) );     // is_allergy '抗过敏标识，0是非抗过敏药物，1是抗过敏药物';
                    sqlParams.add(getflag(DrugUtils.isPatentDrug(cr.get("item_code"))) );          // is_patentdrug  '中成药标识，0是非中成药，1是中成药';
                    sqlParams.add(getflag(DrugUtils.isTumor(cr.get("item_code"))) );               // is_tumor  '抗肿瘤标识，0是非抗肿瘤药，1是抗肿瘤药';
                    sqlParams.add(getflag(DrugUtils.isDDrug(cr.get("item_code"))) );               // is_poison '毒药标识，0是非毒药，1是毒药';
                    sqlParams.add(getflag(DrugUtils.isJSDrug(cr.get("item_code"))) );              // is_psychotic'精神药标识，0是非精神药，1是精神药,';
                    sqlParams.add(getflag(DrugUtils.isMDrug(cr.get("item_code"))) );               // is_habitforming '麻药标识，0是非麻药，1是麻药';
                    sqlParams.add(getflag(DrugUtils.isFSDrug(cr.get("item_code"))) );              // is_radiation '放射标识，0是非放射药，1是放射药';
                    sqlParams.add(getflag(DrugUtils.isGZDrug(cr.get("item_code"))) );              // is_precious '贵重药标识，0是非贵重药，1是贵重药';
                    sqlParams.add(getflag(DrugUtils.isDanger(cr.get("item_code"))) );              // is_danger '危险级别： 0-不是 1-是危险药品;
                    sqlParams.add(getflag(DrugUtils.IsAssist(cr.get("item_code"))) );              // is_assist '辅助用药 0 否 1 是';
                    sqlParams.add(getflag(DrugUtils.isAlbumin(cr.get("item_code"))) );             // is_albumin '白蛋白 0 否 1 是';
                    sqlParams.add(UuidUtil.get32UUID() );//  表主键id
                    Jquery.update(sql, sqlParams.toArray());
                    i++;
                }
                
                // 注释不必要处理 
//                DrDeptDoctDrugEx.this.saveDeptDrug(Jquery);
//
//                DrDeptDoctDrugEx.this.saveDeptDoctDrug(Jquery);
//
//                DrDeptDoctDrugEx.this.saveDrug(Jquery);
//
//                DrDeptDoctDrugEx.this.saveAllCosts(Jquery);

//                DrDeptDoctDrugEx.this.saveDr_Drug_Summary(Jquery);

//                DrDeptDoctDrugEx.this.deleteDR_DRUG_SUMMARY_OUT(Jquery);
                Jquery = null;
            }
        });
    }

    private String getflag(boolean rs)
    {
        return  rs ? "1":"0";
    }
    
    public void buildOver(String ADate, Task AOwner)
    {
        logger.info("开始保存日报");
        saveResultSet(ADate);
        logger.info("结束保存日报");
    }
}