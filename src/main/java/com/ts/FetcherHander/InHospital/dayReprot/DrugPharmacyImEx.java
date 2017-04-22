package com.ts.FetcherHander.InHospital.dayReprot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.Transaction.TransaCallback;
import com.hitzd.Transaction.TransactionTemp;
import com.hitzd.his.ReportBuilder.Interfaces.IReportBuilder;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.his.casehistory.helper.CaseHistoryFactory;
import com.hitzd.his.casehistory.helper.CaseHistoryFunction;
import com.hitzd.his.casehistory.helper.CaseHistoryHelperUtils;
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;
import com.hitzd.his.task.Task;
import com.ts.util.LoggerFileSaveUtil;

/**
 * 药品出入库明细
 *
 */
public class DrugPharmacyImEx implements IReportBuilder
{
    private Logger              logger     = Logger
            .getLogger(DrugPharmacyImEx.class);
    private List<TCommonRecord> importList = new ArrayList<TCommonRecord>();
    private List<TCommonRecord> exportList = new ArrayList<TCommonRecord>();

    @Override
    public String BuildReportWithCR(String ADate, TCommonRecord crPatInfo,
            Task AOwner)
    {
        logger.info("处理数据:" + ADate + "出入库附加信息");
        JDBCQueryImpl his = DBQueryFactory.getQuery("HIS");
        DictCache d = DictCache.getNewInstance();
        if (importList != null && importList.size() > 0)
        {
            for (TCommonRecord t : importList)
            {
                t.set("is_anti", "0");
                if (DrugUtils.isKJDrug(t.get("drug_code")))
                    t.set("is_anti", "1");
                t.set("drug_name", d.getDrugDictInfo(his, t.get("drug_code"))
                        .get("drug_name"));
            }
        }
        if (exportList != null && exportList.size() > 0)
        {
            for (TCommonRecord t : exportList)
            {
                t.set("is_anti", "0");
                if (DrugUtils.isKJDrug(t.get("drug_code")))
                    t.set("is_anti", "1");
                t.set("drug_name", d.getDrugDictInfo(his, t.get("drug_code"))
                        .get("drug_name"));
            }
        }
        return "";
    }

    @Override
    public String BuildReport(String ADate, Task AOwner)
    {
        this.buildBegin(ADate, AOwner);
        logger.info("正在查询" + ADate + "的药品入库情况！");
        JDBCQueryImpl queryHIS = DBQueryFactory.getQuery("HIS");

        getImportListByMiddleWare(ADate, queryHIS);
        getExportListByMiddleWare(ADate, queryHIS);
        queryHIS = null;
        String ErrorInfo = "";
        if (importList != null && importList.size() > 0)
        {
            logger.info(ADate + "共有" + importList.size() + "条入库记录！");
            ErrorInfo = BuildReportWithCR(ADate, null, AOwner);
            this.buildOver(ADate, AOwner);
        }

        if (exportList != null && exportList.size() > 0)
        {
            logger.info(ADate + "共有" + exportList.size() + "条出库记录！");
            ErrorInfo = BuildReportWithCR(ADate, null, AOwner);
            this.buildOver(ADate, AOwner);
        }
        if (ErrorInfo.length() == 0)
            return "处理出入库完毕";
        else
            return ErrorInfo;
    }

    @SuppressWarnings ("unchecked")
    private void getImportListByMiddleWare(String ADate, JDBCQueryImpl his)
    {
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
        String fields = "d.document_no,d.drug_code, d.drug_spec, d.units, d.firm_id, d.quantity, d.purchase_price, d.retail_price, m.sub_storage, m.import_class, d.package_units, d.package_spec ";
        String tableNames = "pharmacy.drug_import_master m, pharmacy.drug_import_detail d";
        List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
        TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("m.document_no",
                "d.document_no", "", "join", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("m.import_date",
                CaseHistoryFunction.genRToDate("pharmacy.drug_import_master",
                        "import_date", "'" + ADate + "'", "yyyy-mm-dd"),
                "", ">=", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("m.import_date",
                CaseHistoryFunction.genRToDate("pharmacy.drug_import_master",
                        "import_date",
                        "'" + DateUtils.getDateAdded(1, ADate) + "'",
                        "yyyy-mm-dd"),
                "", "<", "", "");
        lsWheres.add(where);
        String sql = chhr.genSQL(fields, tableNames, lsWheres, null, null);
        importList = his.query(sql.toString(), new CommonMapper());
    }

    @SuppressWarnings ("unchecked")
    private void getExportListByMiddleWare(String ADate, JDBCQueryImpl his)
    {
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
        String fields = "d.document_no,d.drug_code,d.drug_spec,d.units,d.firm_id,d.quantity,d.purchase_price,d.retail_price,m.sub_storage,m.export_class,d.package_units,d.package_spec";
        String tableNames = "pharmacy.drug_export_master m, pharmacy.drug_export_detail d";
        List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
        TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("m.document_no",
                "d.document_no", "", "join", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("m.export_date",
                CaseHistoryFunction.genRToDate("pharmacy.drug_export_master",
                        "export_date", "'" + ADate + "'", "yyyy-mm-dd"),
                "", ">=", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("m.export_date",
                CaseHistoryFunction.genRToDate("pharmacy.drug_export_master",
                        "export_date",
                        "'" + DateUtils.getDateAdded(1, ADate) + "'",
                        "yyyy-mm-dd"),
                "", "<", "", "");
        lsWheres.add(where);
        String sql = chhr.genSQL(fields, tableNames, lsWheres, null, null);
        exportList = his.query(sql.toString(), new CommonMapper());
    }

    @Override
    public String getLogFileName()
    {
        return null;
    }

    public void buildBegin(String ADate, Task AOwner)
    {
        LoggerFileSaveUtil.LogFileSave(logger,
                "APPLOG\\export_import\\export_import_" + ADate + ".log");
    }

    private void saveList(String ADate)
    {
        TCommonRecord tc = new TCommonRecord();
        tc.set("ADate", ADate);
        TransactionTemp tt = new TransactionTemp("IAS");
        tt.execute(new TransaCallback(tc) {
            @Override
            public void ExceuteSqlRecord()
            {
                JDBCQueryImpl ias = DBQueryFactory.getQuery("IAS");
                StringBuffer sql = new StringBuffer();
                int i = 1;
                for (TCommonRecord cr : importList)
                {
                    logger.info("第-----" + i + "-----共-----" + importList.size()
                            + "入库记录");
                    List<Object> sqlParams = new ArrayList<Object>();
                    sql.append(
                            " insert into drug_import(rpt_date, document_no, drug_code, drug_name, drug_spec, units, firm_id, quantity, ");
                    sql.append(
                            " purchase_price, retail_price, sub_storage, import_class, package_units, package_spec, is_anti)");
                    sql.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    Timestamp dateTime = new Timestamp(DateUtils
                            .getDateFromString(getTranParm().get("ADate"))
                            .getTime());
                    sqlParams.add(dateTime);
                    sqlParams.add(cr.get("document_no"));
                    sqlParams.add(cr.get("drug_code"));
                    sqlParams.add(cr.get("drug_name"));
                    sqlParams.add(cr.get("drug_spec"));
                    sqlParams.add(cr.get("units"));
                    sqlParams.add(cr.get("firm_id"));
                    sqlParams.add(cr.get("quantity"));
                    sqlParams.add(cr.get("purchase_price"));
                    sqlParams.add(cr.get("retail_price"));
                    sqlParams.add(cr.get("sub_storage"));
                    sqlParams.add(cr.get("import_class"));
                    sqlParams.add(cr.get("package_units"));
                    sqlParams.add(cr.get("package_spec"));
                    sqlParams.add(cr.get("is_anti"));

                    ias.update(sql.toString(), sqlParams.toArray());
                    sql.setLength(0);
                    i++;
                }
                importList = null;
                i = 1;
                for (TCommonRecord cr : exportList)
                {
                    logger.info("第-----" + i + "-----共-----" + exportList.size()
                            + "出库记录");
                    List<Object> sqlParams = new ArrayList<Object>();
                    sql.append(
                            " insert into drug_export(rpt_date, document_no, drug_code, drug_name, drug_spec, units, firm_id, quantity, ");
                    sql.append(
                            " purchase_price, retail_price, sub_storage, export_class, package_units, package_spec, is_anti)");
                    sql.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    Timestamp dateTime = new Timestamp(DateUtils
                            .getDateFromString(getTranParm().get("ADate"))
                            .getTime());
                    sqlParams.add(dateTime);
                    sqlParams.add(cr.get("document_no"));
                    sqlParams.add(cr.get("drug_code"));
                    sqlParams.add(cr.get("drug_name"));
                    sqlParams.add(cr.get("drug_spec"));
                    sqlParams.add(cr.get("units"));
                    sqlParams.add(cr.get("firm_id"));
                    sqlParams.add(cr.get("quantity"));
                    sqlParams.add(cr.get("purchase_price"));
                    sqlParams.add(cr.get("retail_price"));
                    sqlParams.add(cr.get("sub_storage"));
                    sqlParams.add(cr.get("export_class"));
                    sqlParams.add(cr.get("package_units"));
                    sqlParams.add(cr.get("package_spec"));
                    sqlParams.add(cr.get("is_anti"));

                    ias.update(sql.toString(), sqlParams.toArray());
                    sql.setLength(0);
                    i++;
                }
                ias = null;
                exportList = null;
            }
        });
    }

    public void buildOver(String ADate, Task AOwner)
    {
        logger.info("开始保存日报");
        this.saveList(ADate);
        logger.info("日报保存完成");
    }
}