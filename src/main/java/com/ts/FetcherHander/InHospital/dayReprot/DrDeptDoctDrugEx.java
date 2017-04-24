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

/**
 * 适用于307医院,304医院，北京军区总院，上海长征医院
 * 
 * @author tangyl
 *
 */
public class DrDeptDoctDrugEx implements IReportBuilder
{
    private Logger              logger    = Logger
            .getLogger(DrDeptDoctDrugEx.class);
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
        String sql = " insert into dr_drug_summary(FIRM_ID, drug_code, DRUG_FORM, drug_name, patient_id, visit_id, costs, drug_units, amount, rpt_date, doctor_code, doctor_name, dept_code,  dept_name, drug_spec,  charges, identity, charge_type, is_basedrug ,is_anti)"
                + "select FIRM_ID, drug_code, DRUG_FORM, drug_name, patient_id, visit_id, costs, drug_units, amount, rpt_date, doctor_code, doctor_name, dept_code,  dept_name, drug_spec,  charges, identity, charge_type, is_basedrug ,is_anti from dr_drug_summary_out";
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
        query.update("delete dr_Drug  " + where);
        query.update("delete dr_Drug_anti  " + where);
        query.update("delete dr_drug_summary  " + where);
        query.update("delete dr_Dept_doctor_Drug  " + where);
        query.update("delete dr_Dept_doctor_Drug_anti  " + where);
        query.update("delete dr_Dept_Drug_anti  " + where);
        query.update("delete dr_Dept_Drug  " + where);
        query.update("delete DR_ALL_COSTS  " + where);
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
        TCommonRecord tc = new TCommonRecord();
        tc.set("ADate", ADate);
        TransactionTemp tt = new TransactionTemp("IAS");
        tt.execute(new TransaCallback(tc) {
            public void ExceuteSqlRecord()
            {
                JDBCQueryImpl Jquery = DBQueryFactory.getQuery("IAS");
                int i = 1;
                for (TCommonRecord cr : DrDeptDoctDrugEx.this.resultSet)
                {
                    logger.info("第-----" + i + "-----共-----"
                            + DrDeptDoctDrugEx.this.resultSet.size() + "药品统计");
                    List<Object> sqlParams = new ArrayList<Object>();
                    String sql = " insert into dr_drug_summary_out(drug_code, FIRM_ID, DRUG_FORM, drug_name, patient_id, visit_id, costs, drug_units, amount, rpt_date, doctor_code, doctor_name, dept_code,  dept_name, drug_spec,  charges, identity, charge_type, is_basedrug ,is_anti) "
                            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    sqlParams.add(cr.get("item_code"));
                    sqlParams.add(cr.get("FIRM_ID"));
                    sqlParams.add(cr.get("drug_FORM"));
                    sqlParams.add(cr.get("item_name"));
                    sqlParams.add(cr.get("patient_id"));
                    sqlParams.add(cr.get("visit_id"));
                    sqlParams.add(cr.getDouble("costs"));
                    sqlParams.add(cr.get("units"));
                    sqlParams.add(cr.getDouble("amount"));
                    Timestamp dateTime = new Timestamp(DateUtils
                            .getDateFromString(getTranParm().get("ADate"))
                            .getTime());
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

                    Jquery.update(sql, sqlParams.toArray());
                    i++;
                }
                DrDeptDoctDrugEx.this.deleteDR(Jquery,
                        getTranParm().get("ADate"));

                DrDeptDoctDrugEx.this.saveDeptDrug(Jquery);

                DrDeptDoctDrugEx.this.saveDeptDoctDrug(Jquery);

                DrDeptDoctDrugEx.this.saveDrug(Jquery);

                DrDeptDoctDrugEx.this.saveAllCosts(Jquery);

                DrDeptDoctDrugEx.this.saveDr_Drug_Summary(Jquery);

                DrDeptDoctDrugEx.this.deleteDR_DRUG_SUMMARY_OUT(Jquery);
                Jquery = null;
            }
        });
    }

    public void buildOver(String ADate, Task AOwner)
    {
        logger.info("开始保存日报");
        saveResultSet(ADate);
        logger.info("结束保存日报");
    }
}