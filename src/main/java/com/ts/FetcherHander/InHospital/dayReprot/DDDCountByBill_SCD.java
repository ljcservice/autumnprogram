package com.ts.FetcherHander.InHospital.dayReprot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.Transaction.TransaCallback;
import com.hitzd.Transaction.TransactionTemp;
import com.hitzd.Utils.CalcuateAgeUtil;
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
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;
import com.hitzd.his.task.Task;
import com.ts.util.LoggerFileSaveUtil;

/**
 * 日报 数据来源 费用明细 计算ddd 住院天数
 * 
 */
public class DDDCountByBill_SCD implements IReportBuilder
{
    private Logger              logger         = LogManager
            .getLogger(DDDCountByBill_SCD.class);

    private List<TCommonRecord> resultSet      = null;
    private List<TCommonRecord> supplier       = null;
    /* 病人转科室分解数据集 */
    private List<TCommonRecord> rsSplit        = null;
    // 所有的额账单信息避免多次查询
    private List<TCommonRecord> allBillDetials = null;

    @Override
    public String BuildReportWithCR(String ADate, TCommonRecord crPatInfo,
            Task AOwner)
    {
        logger.info("处理数据:" + ADate + "DDD数据");
        Map<String, TCommonRecord> antiDrugInfo = new HashMap<String, TCommonRecord>();
        JDBCQueryImpl queryHIS = DBQueryFactory.getQuery("HIS");
        Map<String, Double> zl = new HashMap<String, Double>(); // 每个病人用了多少种抗菌药物
        for (TCommonRecord cr : resultSet)
        {
            List<TCommonRecord> BillDetail = this.getInpBillDetail(queryHIS,
                    cr);
            // 病人过程唯一号码
            String patFuncNo = ADate + "-" + cr.get("patient_id") + "-"
                    + cr.get("visit_id") + "-"
                    + UUID.randomUUID().toString().replace("-", "");
            int antiCount = 0;
            double specDDDS = 0; // 特殊级抗生素使用频度
            double limitDDDS = 0; // 限制级抗生素使用频度
            double ordinaryDDDS = 0; // 普通级抗生素使用频度
            double ddd = 0; // 全部抗生素使用频度
            double charges = 0; // 应收全部费用
            double costs = 0; // 实收全部费用
            double anti_costs = 0; // 抗生素应收费用
            double drug_costs = 0; // 全部应收药费--中药西药
            double anti_charges = 0; // 抗生素实收费用
            double drug_charges = 0; // 全部实收是非--中药西药

            double outdrug_costs = 0; // 局部应收药费--中药西药
            double outdrug_charges = 0; // 局部实收是非--中药西药
            double outAntiDDD = 0d; // 局部抗菌药物
            double outAntiCosts = 0d; // 局部抗菌药物费用
            double outAntiCharges = 0d; // 局部抗菌药实收费用
            double outspecDDDS = 0; // 局部特殊级抗生素使用频度
            double outlimitDDDS = 0; // 局部限制级抗生素使用频度
            double outordinaryDDDS = 0; // 局部普通级抗生素使用频度
            boolean adminJMSY = false; // 是否  静脉输液
            double  adminJMSYCost = 0d; //
            boolean adminAntiJMSY = false;// 是否 抗菌药物静脉输液
            double adminAntiJMSYCost = 0d; //
            boolean injection = false;    // 是否 注射药物
            double  injectionCost = 0d;   // 注射药物  金额
            boolean injectionAnti = false;  // 是否  抗菌 注射药物
            double  injectionAntiCost = 0d; // 抗菌注射 金额 

            if (BillDetail != null && BillDetail.size() > 0)
            {
                /* 计算联用情况 */
                String billing_date_time = "";
                String drDept = "";
                boolean is_scd = false;
                TCommonRecord anti = new TCommonRecord();
                logger.info("patient_id: " + cr.get("patient_id")
                        + " visit_id: " + cr.get("visit_id") + "的病人 共有"
                        + BillDetail.size() + "账单明细");
                for (int i = 0; i < BillDetail.size(); i++) // 此循环中计算中药费用，西药费用，抗生素相关信息
                {
                    TCommonRecord bill = BillDetail.get(i);
                    bill.set("is_anti", "0");
                    // 不同科室 查询一下 静脉输液 药品 
                    if(!drDept.equals(bill.get("ordered_by")))
                    {
                        this.getAdministratorjmsy(queryHIS, bill);
                    }
                    if (!"".equals(drDept)
                            && !drDept.equals(bill.get("ordered_by")))
                    {
                        if (!is_scd)
                            is_scd = true;
                        /* 复制对象 */
                        TCommonRecord rs = (TCommonRecord) cr.deepClone();
                        // 给药途径静脉输液
                        rs.set("adminJMSY", adminJMSY ? "1" : "0");
                        rs.set("adminJMSYCost", adminJMSYCost + "");
                        // 抗菌药物  给药途径静脉输液
                        rs.set("adminAntiJMSY", adminAntiJMSY ? "1" : "0");
                        rs.set("adminAntiJMSYCost", adminAntiJMSYCost + "");
                        
                        // 注射药物
                        rs.set("injection", injection ? "1":"0");
                        rs.set("injectionCost", injectionCost + "");
                        rs.set("injectionAnti", injectionAnti ? "1" :"0");
                        rs.set("injectionAntiCost", injectionAntiCost + "");
                        // 转科标示
                        rs.set("IS_SCD", is_scd ? "1" : "0");
                        // 病人过程唯一号
                        rs.set("PAT_FUNCT_NO", patFuncNo);
                        // 过程科室code
                        rs.set("FUNCDEPTCODE", drDept);
                        // 过程科室名称
                        rs.set("FUNCDEPTNAME",DictCache.getNewInstance().getDeptName(drDept));
                        /* 设置病人计算信息 */
                        SetPatInfo(ADate, queryHIS, zl, antiCount, specDDDS,
                                limitDDDS, ordinaryDDDS, ddd, charges, costs,
                                anti_costs, drug_costs, anti_charges,
                                drug_charges, outAntiDDD, outAntiCosts,
                                outAntiCharges, outspecDDDS, outlimitDDDS,
                                outordinaryDDDS, rs, outdrug_costs,
                                outdrug_charges, antiDrugInfo);
                        /* 添加病人信息 */
                        this.rsSplit.add(rs);
                        antiCount = 0;
                        specDDDS = 0; // 特殊级抗生素使用频度
                        limitDDDS = 0; // 限制级抗生素使用频度
                        ordinaryDDDS = 0; // 普通级抗生素使用频度
                        ddd = 0; // 全部抗生素使用频度
                        charges = 0; // 实收全部费用
                        costs = 0; // 实收全部费用
                        anti_costs = 0; // 抗生素应收费用
                        drug_costs = 0; // 全部应收药费--中药西药
                        anti_charges = 0; // 抗生素实收费用
                        drug_charges = 0; // 全部实收是非--中药西药
                        outAntiDDD = 0d; // 局部抗菌药物
                        outAntiCosts = 0d; // 局部抗菌药物费用
                        outAntiCharges = 0d; // 局部抗菌药实收费用
                        outspecDDDS = 0; // 局部特殊级抗生素使用频度
                        outlimitDDDS = 0; // 局部限制级抗生素使用频度
                        outordinaryDDDS = 0; // 局部普通级抗生素使用频度
                        outdrug_costs = 0; // 局部应收药费--中药西药
                        outdrug_charges = 0; // 局部实收是非--中药西药
                        adminAntiJMSY = false; // 静脉输液抗菌药 
                        adminAntiJMSYCost = 0 ;// 静脉输液抗菌药金额
                        adminJMSY = false ; // 静脉输液
                        adminJMSYCost = 0; // 静脉输液金额 ;
                        injection = false;  // 是否注射剂
                        injectionCost = 0 ; // 注射剂金额
                        injectionAnti = false; // 是否抗菌药物注射
                        injectionAntiCost = 0 ; /// 抗菌药物注射金额
                        
                    } // End 添加病人信息
                    costs += bill.getDouble("costs");
                    charges += bill.getDouble("charges");
                    if (Config.getParamValue("Drug_In_Order_Chinese")
                            .equals(bill.get("item_class"))) // 中药费用
                    {
                        drug_charges += bill.getDouble("charges");
                        drug_costs += bill.getDouble("costs");
                    }
                    else if (Config.getParamValue("Drug_In_Order").equals(bill.get("item_class"))) // 西药费用
                    {
                        
                        // 注射剂药物
                        if(DrugUtils.isZSDrug(bill.get("item_code"), bill.get("item_spec")))
                        {
                            injection = true;
                            injectionCost += bill.getDouble("item_code");
                        }
                        // 静脉输液
                        if(this.adminOrderMap.containsKey(bill.getObj("item_code"))) 
                        {
                            // 静脉输液金额
                            adminJMSYCost += bill.getDouble("costs");
                            adminJMSY = true;
                        }
                        
                        boolean isOutAnti = false;
                        if (DrugUtils.isExternalDrug(bill.get("item_code"),
                                bill.get("item_spec")))
                        {
                            // 外用药应该费用
                            outdrug_costs += bill.getDouble("costs");
                            // 外用实收费用
                            outdrug_charges += bill.getDouble("charges");
                            isOutAnti = true;
                        }
                        drug_charges += bill.getDouble("charges");
                        drug_costs += bill.getDouble("costs");

                        if (DrugUtils.isKJDrug(bill.get("item_code")))
                        {
                            
                            // 注射剂药物
                            if(DrugUtils.isZSDrug(bill.get("item_code"), bill.get("item_spec")))
                            {
                                injectionAnti = true;
                                injectionAntiCost += bill.getDouble("item_code");
                            }
                            // 静脉输液
                            if(this.adminOrderMap.containsKey(bill.get("item_code"))) 
                            {
                                //静脉输液
                                adminAntiJMSYCost += bill.getDouble("costs");
                                adminAntiJMSY = true;
                            }
                            // 外用抗菌药物
                            if (isOutAnti)
                            {
                                // 应该费用
                                outAntiCosts += bill.getDouble("costs");
                                // 实收费用
                                outAntiCharges += bill.getDouble("charges");
                            }
                            anti_costs += bill.getDouble("costs");
                            anti_charges += bill.getDouble("charges");

                            if (!zl.containsKey(bill.get("item_code")))
                            {
                                zl.put(bill.get("item_code"),
                                        bill.getDouble("amount"));
                            }
                            else
                            {
                                zl.put(bill.get("item_code"),
                                        zl.get(bill.get("item_code"))
                                                + bill.getDouble("amount"));
                            }
                            // 计算联用数
                            if (!billing_date_time
                                    .equals(bill.get("billing_date_time")))
                            {
                                billing_date_time = bill
                                        .getDateString("billing_date_time");
                                anti.set("anti", bill.get("item_code"));
                                antiCount = 1;
                            }
                            else
                            {
                                if (!anti.get("anti")
                                        .equals((bill.get("item_code"))))
                                {
                                    anti.set("anti", bill.get("item_code"));
                                    antiCount++;
                                }
                            }
                            if (supplier == null || supplier.size() == 0)
                            {
                                supplier = StringUtils.getSupplier();
                            }
                            StringUtils.execCF(supplier, bill);
                            double dddValue = DDDUtils.CalcDDD(
                                    bill.get("item_code"),
                                    bill.get("item_spec"), bill.get("units"),
                                    bill.get("Firm_ID"), bill.get("amount"),
                                    bill.get("COSTS"));
                            bill.set("is_anti", "1");
                            bill.set("DDD", dddValue + "");
                            String mapKey = buildMapKey(bill.get("item_code"), bill.get("item_spec"));initializeExternalAntiDrugMap(mapKey, antiDrugInfo, bill);
                            logger.info("存在抗菌药" + bill.get("item_code") + "●●●" + bill.get("item_spec") + "●●●" + dddValue);
                            ddd += dddValue;
                            antiDrugInfo.get(mapKey).set("OUTANTIDDD",dddValue + antiDrugInfo.get(mapKey).getDouble("OUTANTIDDD") + "");
                            // 外用抗菌药物
                            if (isOutAnti)
                            {
                                outAntiDDD += dddValue;
                            }
                            // 普通级抗菌药
                            if ("1".equals(DrugUtils.getDrugLevel(
                                    bill.get("item_code"),
                                    bill.get("item_spec"))))
                            {
                                ordinaryDDDS += dddValue;
                                antiDrugInfo.get(mapKey).set("OUTORDINARYDDDS",
                                        dddValue + antiDrugInfo.get(mapKey)
                                                .getDouble("OUTORDINARYDDDS")
                                                + "");
                                if (isOutAnti)
                                {
                                    outordinaryDDDS += dddValue;
                                }
                                logger.info("存在普通级" + bill.get("item_code")
                                        + "●●●" + bill.get("item_spec") + "●●●"
                                        + ordinaryDDDS);
                            } // 特殊级抗菌药
                            else if (DrugUtils.isSpecDrug(bill.get("item_code"),
                                    bill.get("item_spec")))
                            {
                                specDDDS += dddValue;
                                antiDrugInfo.get(mapKey).set("OUTSPECDDDS",
                                        dddValue + antiDrugInfo.get(mapKey)
                                                .getDouble("OUTSPECDDDS") + "");
                                if (isOutAnti)
                                {
                                    outspecDDDS += dddValue;
                                }
                                logger.info("存在特殊级" + bill.get("item_code")
                                        + "●●●" + bill.get("item_spec") + "●●●"
                                        + specDDDS);
                            } // 限制抗菌药
                            else if (DrugUtils.isLimitDrug(
                                    bill.get("item_code"),
                                    bill.get("item_spec")))
                            {
                                limitDDDS += dddValue;
                                antiDrugInfo.get(mapKey).set("OUTLIMITDDDS",
                                        dddValue + antiDrugInfo.get(mapKey)
                                                .getDouble("OUTLIMITDDDS")
                                                + "");
                                if (isOutAnti)
                                {
                                    outlimitDDDS += dddValue;
                                }
                                logger.info("存在限制级" + bill.get("item_code")
                                        + "●●●" + bill.get("item_spec") + "●●●"
                                        + limitDDDS);
                            }

                        }
                    } //
                      // 标注科室
                    drDept = bill.get("ordered_by");
                    if ((i + 1) == BillDetail.size())
                    {
                    	/* 复制对象 */
                        TCommonRecord rs = (TCommonRecord) cr.deepClone();
                        // 给药途径静脉输液
                        rs.set("adminJMSY", adminJMSY ? "1" : "0");
                        rs.set("adminJMSYCost", adminJMSYCost + "");
                        // 抗菌药物  给药途径静脉输液
                        rs.set("adminAntiJMSY", adminAntiJMSY ? "1" : "0");
                        rs.set("adminAntiJMSYCost", adminAntiJMSYCost + "");
                        
                        // 注射药物
                        rs.set("injection", injection ? "1":"0");
                        rs.set("injectionCost", injectionCost + "");
                        rs.set("injectionAnti", injectionAnti ? "1" :"0");
                        rs.set("injectionAntiCost", injectionAntiCost + "");
                       
                        
                        // 转科标示
                        rs.set("IS_SCD", is_scd ? "1" : "0");
                        // 病人过程唯一号
                        rs.set("PAT_FUNCT_NO", patFuncNo);
                        // 过程科室code
                        rs.set("FUNCDEPTCODE", drDept);
                        // 过程科室名称
                        rs.set("FUNCDEPTNAME",DictCache.getNewInstance().getDeptName(drDept));
                        /* 设置病人计算信息 */
                        SetPatInfo(ADate, queryHIS, zl, antiCount, specDDDS,
                                limitDDDS, ordinaryDDDS, ddd, charges, costs,
                                anti_costs, drug_costs, anti_charges,
                                drug_charges, outAntiDDD, outAntiCosts,
                                outAntiCharges, outspecDDDS, outlimitDDDS,
                                outordinaryDDDS, rs, outdrug_costs,
                                outdrug_charges, antiDrugInfo);
                        /* 添加病人信息 */
                        this.rsSplit.add(rs);
                    } // End 添加病人信息
                } // End for(bill.size())
                allBillDetials.addAll(BillDetail);
            }
            else
            {
                cr.set("PAT_FUNCT_NO", patFuncNo);
                SetPatInfo(ADate, queryHIS, zl, antiCount, specDDDS, limitDDDS,
                        ordinaryDDDS, ddd, charges, costs, anti_costs,
                        drug_costs, anti_charges, drug_charges, outAntiDDD,
                        outAntiCosts, outAntiCharges, outspecDDDS, outlimitDDDS,
                        outordinaryDDDS, cr, outdrug_costs, outdrug_charges,
                        antiDrugInfo);
                // 没有任何信息情况
                this.rsSplit.add(cr);
            }
        }
        return "";

    }

    @SuppressWarnings ("unchecked")
    private String buildMapKey(String drugCode, String drugSpec)
    {
        JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
        List<TCommonRecord> crList = query
                .query("select DDD_PER_UNIT,DDD_UNIT,Dose_Per_Unit,Dose_Units from Drug_Map where Drug_No_Local = '"
                        + drugCode + "' and Drug_Spec = '" + drugSpec
                        + "' and IS_ANTI = 1", new CommonMapper());
        if (crList.size() > 0)
        {
            TCommonRecord crAntiDrug = crList.get(0);
            // 考虑地方医院数据库可能没有 Dose_Per_Unit ， Dose_Units
            String dosePerUnit = crAntiDrug.getDouble("DDD_PER_UNIT") != 0
                    ? crAntiDrug.get("DDD_PER_UNIT")
                    : crAntiDrug.get("Dose_Per_Unit");
            // 取出基本剂量的单位，可能是克、毫克、g、mg、ml、毫升、万单位、单位、万u、万iu。。。。。。
            String doseUnits = crAntiDrug.getDouble("DDD_PER_UNIT") != 0
                    ? crAntiDrug.get("DDD_UNIT")
                    : crAntiDrug.get("Dose_Units").trim();
            return drugCode + "_" + dosePerUnit + "_" + doseUnits;
        }
        logger.debug("无所定药品 drug_code:" + drugCode + "-drugSpec:" + drugSpec);
        return "___";
    }

    /**
     * 初始化外用抗菌药信息map
     * 
     * @param mapKey
     * @param externalAntiDrugInfoMap
     * @param billInfo
     */
    private void initializeExternalAntiDrugMap(String mapKey,
            Map<String, TCommonRecord> externalAntiDrugInfoMap,
            TCommonRecord billInfo)
    {
        if ("".equals(mapKey))
            return;
        if (!externalAntiDrugInfoMap.containsKey(mapKey))
        {
            TCommonRecord content = new TCommonRecord();
            content.set("OUTANTIAMOUNT", billInfo.get("amount"));
            content.set("OUTANTICOSTS", billInfo.get("costs"));
            content.set("OUTANTICHARGES", billInfo.get("charges"));
            content.set("OUTANTIDDD", "0");
            content.set("OUTSPECDDDS", "0");
            content.set("OUTLIMITDDDS", "0");
            content.set("OUTORDINARYDDDS", "0");
            externalAntiDrugInfoMap.put(mapKey, content);
        }
        else
        {
            TCommonRecord content = externalAntiDrugInfoMap.get(mapKey);
            content.set("OUTANTIAMOUNT", billInfo.getDouble("amount")
                    + content.getDouble("OUTANTIAMOUNT") + "");
            content.set("OUTANTICOSTS", billInfo.getDouble("costs")
                    + content.getDouble("OUTANTICOSTS") + "");
            content.set("OUTANTICHARGES", billInfo.getDouble("charges")
                    + content.getDouble("OUTANTICHARGES") + "");
        }
    }

    /**
     * 将传入的时间单位转换成以天为基础的单位
     * 
     * @param timeUnit
     * @return
     */
    private double timeUnitToDay(String timeUnit, String freqInterval)
    {
        if (null == timeUnit || "".equals(timeUnit))
        {
            return 0;
        }
        TCommonRecord timeUnitMap = new TCommonRecord();
        timeUnitMap.set("年", "365");
        timeUnitMap.set("月", "30");
        timeUnitMap.set("周", "7");
        timeUnitMap.set("日", "6");
        timeUnitMap.set("小时", "5");
        timeUnitMap.set("分", "4");
        timeUnitMap.set("秒", "3");
        timeUnitMap.set("毫秒", "2");
        int tempVal = Integer.parseInt(freqInterval);
        double rtnVal = 0;
        switch (timeUnitMap.getInt(timeUnit))
        {
            case 365:
                rtnVal = tempVal / 365;
                break;
            case 30:
                rtnVal = tempVal / 30;
                break;
            case 7:
                rtnVal = tempVal / 7;
                break;
            case 6:
                rtnVal = tempVal;
                break;
            case 5:
                rtnVal = 24 / tempVal;
                break;
            case 4:
                rtnVal = 24 * 60 / tempVal;
                break;
            case 3:
                rtnVal = 24 * 60 * 60 / tempVal;
                break;
            case 2:
                rtnVal = 24 * 60 * 60 * 1000 / tempVal;
                break;
            default:
                rtnVal = tempVal;
                break;
        }
        return rtnVal;
    }

    /**
     * 查询并计算外用抗菌药相关值
     * 
     * @param externalAntiDrugInfoMap
     * @param orders
     * @param rs
     */
    private void handleOutUseAntiDrug(
            Map<String, TCommonRecord> externalAntiDrugInfoMap,
            List<TCommonRecord> orders, TCommonRecord rs)
    {
        if ("close".equals(
                Config.getParamValue("external_antidrug_administration")))
            return; // 参数值为close时，直接跳过
        if (externalAntiDrugInfoMap.size() == 0 || orders.size() == 0)
        {
            return;// 无抗菌药的情况直接返回
        }
        logger.info("查询 patient_id: " + orders.get(0).get("patient_id")
                + ",visit_id:" + orders.get(0).get("visit_id") + ",科室编码："
                + orders.get(0).get("FUNCDEPTCODE") + "外用抗菌药");
        List<TCommonRecord> antiDrugList = new ArrayList<TCommonRecord>(); // 外用抗菌药医嘱列表
        String antiDrugCodeList = externalAntiDrugInfoMap.keySet().toString(); // 所有已经添加过的抗菌药代码<这样会更合理一些>
        String andministratins = Config
                .getParamValue("external_antidrug_administration");
        String drugFlag = Config.getParamValue("Drug_In_Order");
        // 模拟数据库的查询
        for (TCommonRecord data : orders)
        {
            boolean isExternalAntiDrugFlag = data.get("order_class")
                    .equals(drugFlag) // 西药
                    && "2".endsWith(data.get("order_status"))// 已经执行过的医嘱
                    && "0".endsWith(data.get("billing_attr"))// 正常计价的医嘱
                    && (("".equals(data.get("administration")) ? false
                            : andministratins
                                    .contains(data.get("administration")))
                            || ("".equals(data.get("freq_detail")) ? false
                                    : andministratins
                                            .contains(data.get("freq_detail"))))// 配置的外用抗菌药使用途径
                    && antiDrugCodeList.contains(data.get("order_code")); // 抗菌药
            if (isExternalAntiDrugFlag)
            {
                antiDrugList.add(data);
                logger.info("#########patient_id: " + data.get("patient_id")
                        + ",visit_id: " + data.get("visit_id") + ",drug_code: "
                        + data.get("order_code") + " 是外用抗菌药,用药途径为： "
                        + ("".equals(data.get("administration"))
                                ? data.get("freq_detail")
                                : data.get("administration"))
                        + "#######");
            }
        }
        if (antiDrugList.size() > 0)
        {
            logger.info("查询 patient_id: " + orders.get(0).get("patient_id")
                    + ",visit_id: " + orders.get(0).get("visit_id") + ",科室编码："
                    + orders.get(0).get("FUNCDEPTCODE") + ",通过医嘱查询到的外用抗菌药有：  "
                    + antiDrugList.size() + " 个");
            double allExternalCosts = 0, allExternalCharges = 0,
                    allExternalDDD = 0, allExternalSpecDDDs = 0,
                    allExternalLimitDDDs = 0, allExternalOrdingaryDDDs = 0;
            for (String str : externalAntiDrugInfoMap.keySet())
            {
                for (TCommonRecord data : antiDrugList)
                {
                    if (str.indexOf(data.get("order_code")) >= 0)
                    {
                        int amount = 0;
                        if (str.split("_")[2]
                                .endsWith(data.get("dosage_units")))
                        {
                            amount = (int) (Math
                                    .ceil(data.getDouble("dosage") / Double
                                            .parseDouble(str.split("_")[1])) // 单次使用个数
                                    * (DateUtils.daysBetweenTwoDate(
                                            data.get("start_date_time"),
                                            data.get("stop_date_time")) + 1) // 使用天数
                                    * timeUnitToDay(
                                            data.get("freq_interval_unit"),
                                            data.get("freq_interval"))// 换算成天
                                    * data.getDouble("freq_counter"));// 使用频次
                        }
                        else
                        {
                            // 如果医嘱中的使用单位与药品使用单位不一致时，药品使用个数默认为1
                            amount = 1;
                        }
                        TCommonRecord content = externalAntiDrugInfoMap
                                .get(str);
                        if (amount > content.getInt("OUTANTIAMOUNT"))
                        {
                            allExternalCosts += content
                                    .getDouble("OUTANTICOSTS");
                            allExternalCharges += content
                                    .getDouble("OUTANTICHARGES");
                            allExternalDDD += content.getDouble("OUTANTIDDD");
                            allExternalSpecDDDs += content
                                    .getDouble("OUTSPECDDDS");
                            allExternalLimitDDDs += content
                                    .getDouble("OUTLIMITDDDS");
                            allExternalOrdingaryDDDs += content
                                    .getDouble("OUTORDINARYDDDS");
                        }
                        allExternalCosts += content.getDouble("OUTANTICOSTS")
                                / content.getDouble("OUTANTIAMOUNT") * amount;
                        allExternalCharges += content
                                .getDouble("OUTANTICHARGES")
                                / content.getDouble("OUTANTIAMOUNT") * amount;
                        allExternalDDD += content.getDouble("OUTANTIDDD")
                                / content.getDouble("OUTANTIAMOUNT") * amount;
                        allExternalSpecDDDs += content.getDouble("OUTSPECDDDS")
                                / content.getDouble("OUTANTIAMOUNT") * amount;
                        allExternalLimitDDDs += content
                                .getDouble("OUTLIMITDDDS")
                                / content.getDouble("OUTANTIAMOUNT") * amount;
                        allExternalOrdingaryDDDs += content
                                .getDouble("OUTORDINARYDDDS")
                                / content.getDouble("OUTANTIAMOUNT") * amount;
                    }
                }
            }
            logger.info("#########patient_id: "
                    + orders.get(0).get("patient_id") + ",visit_id:"
                    + orders.get(0).get("visit_id") + ",由医嘱查询到的局部用抗菌药costs: "
                    + allExternalCosts + ",charges: " + allExternalCharges
                    + ",ddd: " + allExternalDDD + ",specddds: "
                    + allExternalSpecDDDs + ",limitddds: "
                    + allExternalLimitDDDs + ",OrdingaryDDDs: "
                    + allExternalOrdingaryDDDs);
            rs.set("outdrug_costs",
                    allExternalCosts + rs.getDouble("outdrug_costs") + ""); // 局部药物费用
            rs.set("outdrug_charges",
                    allExternalCharges + rs.getDouble("outdrug_charges") + ""); // 局部药物实收费用
            rs.set("is_out", "1"); // 是否使用外用抗菌药物
            rs.set("outAntiDDD",
                    allExternalDDD + rs.getDouble("outAntiDDD") + ""); // 局部抗菌药物
            rs.set("outAntiCosts",
                    allExternalCosts + rs.getDouble("outAntiCosts") + ""); // 局部抗菌药物费用
            rs.set("outAntiCharges",
                    allExternalCharges + rs.getDouble("outAntiCharges") + ""); // 局部抗菌药实收费用
            rs.set("outspecDDDS",
                    allExternalSpecDDDs + rs.getDouble("outspecDDDS") + ""); // 局部特殊级抗生素使用频度
            rs.set("outlimitDDDS",
                    allExternalLimitDDDs + rs.getDouble("outlimitDDDS") + ""); // 局部限制级抗生素使用频度
            rs.set("outordinaryDDDS", allExternalOrdingaryDDDs
                    + rs.getDouble("outordinaryDDDS") + ""); // 局部普通级抗生素使用频度
            orders.clear();
            externalAntiDrugInfoMap.clear();
        }
    }

    /**
     * 设置病人计算信息
     */
    private void SetPatInfo(String ADate, JDBCQueryImpl queryHIS,
            Map<String, Double> zl, int antiCount, double specDDDS,
            double limitDDDS, double ordinaryDDDS, double ddd, double charges,
            double costs, double anti_costs, double drug_costs,
            double anti_charges, double drug_charges, double outAntiDDD,
            double outAntiCosts, double outAntiCharges, double outspecDDDS,
            double outlimitDDDS, double outordinaryDDDS, TCommonRecord rs,
            double outdrug_costs, double outdrug_charges,
            Map<String, TCommonRecord> externalAntiDrugInfoMap)
    {
        /* 抗生素使用数量大于0的为已经使用过了，等于或小于0记为退药 */
        int species = 0;
        if (zl != null && zl.size() > 0)
        {
            for (String key : zl.keySet())
            {
                if (zl.get(key) > 0)
                {
                    species = species + 1;
                }
            }
        }
        logger.info("处理数据:" + ADate + "病原学送检");
        List<TCommonRecord> orders = new ArrayList<TCommonRecord>();
        int iEtiologySubmitCount = this.getEtiologySubmitCount(queryHIS, rs,
                orders);
        rs.set("submit_count", (anti_costs > 0.0
                ? Integer.toString(iEtiologySubmitCount) : "0"));

        rs.set("species", species + ""); // 患者用药总数
        zl.clear();
        rs.set("charges", charges + ""); // 实付费用
        rs.set("costs", costs + ""); // 应付费用
        rs.set("anti_costs", anti_costs + ""); // 抗菌药应付费用
        rs.set("anti_charges", anti_charges + ""); // 抗菌药实付费用
        rs.set("drug_costs", drug_costs + ""); // 药费应付费用
        rs.set("drug_charges", drug_charges + ""); // 药费实付费用

        rs.set("outdrug_costs", outdrug_costs + ""); // 局部药物费用
        rs.set("outdrug_charges", outdrug_charges + ""); // 局部药物实收费用
        rs.set("outAntiDDD", outAntiDDD + ""); // 局部抗菌药物
        rs.set("outAntiCosts", outAntiCosts + ""); // 局部抗菌药物费用
        rs.set("outAntiCharges", outAntiCharges + ""); // 局部抗菌药实收费用
        rs.set("outspecDDDS", outspecDDDS + ""); // 局部特殊级抗生素使用频度
        rs.set("outlimitDDDS", outlimitDDDS + ""); // 局部限制级抗生素使用频度
        rs.set("outordinaryDDDS", outordinaryDDDS + ""); // 局部普通级抗生素使用频度
        rs.set("is_out", (outAntiCosts == 0 ? 0 : 1) + ""); // 是否使用外用抗菌药物

        rs.set("max_drug_aday", antiCount + ""); // 最大抗菌药联用数
        rs.set("is_anti", (anti_costs == 0 ? 0 : 1) + ""); // 是否使用抗菌药物
        rs.set("DDD_Value", ddd + ""); // 抗菌药DDD值
        rs.set("ordinaryDDDS", ordinaryDDDS + ""); // 普通抗菌药DDD值
        rs.set("Limit_DDD_Value", limitDDDS + ""); // 限制级抗菌药DDD值
        rs.set("Spec_DDD_Value", specDDDS + ""); // 特殊级抗菌药DDD值
        rs.set("ADate", ADate);
        rs.set("operation", this.getOperation(queryHIS, rs));
        handleOutUseAntiDrug(externalAntiDrugInfoMap, orders, rs);
    }

    @Override
    public String BuildReport(String ADate, Task AOwner)
    {
        this.buildBegin(ADate, AOwner);
        logger.info("正在汇总" + ADate + "的出院病人记录！");
        JDBCQueryImpl queryHIS = DBQueryFactory.getQuery("HIS");

        /* 用中间层替换之前的逻辑 */
        buildReportMiddleWare(ADate, queryHIS);
        String ErrorInfo = "";
        if (resultSet != null && resultSet.size() > 0)
        {
            logger.info(ADate + "共有" + resultSet.size() + "条出院病人记录！");
            ErrorInfo = BuildReportWithCR(ADate, null, AOwner);
            this.buildOver(ADate, AOwner);
        }
        if (ErrorInfo.length() == 0)
            return "处理出院病人抗菌药DDD结束";
        else
            return ErrorInfo;
    }

    @SuppressWarnings ("unchecked")
    public void buildReportMiddleWare(String ADate, JDBCQueryImpl queryHIS)
    {
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
        rsSplit = new ArrayList<TCommonRecord>();
        String strFields = "";
        // 过滤SQLServer关键字
        if ("SQLServer"
                .equals(Config.getTableCofig("MedRec.pat_visit").getDbName()))
        {
            strFields = "t.ATTENDING_DOCTOR doctor,t.'identity',t.charge_type,t.patient_id,t.visit_id,t.DEPT_DISCHARGE_FROM dept_code,t.discharge_date_time,m.date_of_birth,t.discharge_date_time,t.admission_date_time,dg.diagnosis_desc";
        }
        else
        {
            strFields = "t.ATTENDING_DOCTOR doctor,t.identity,t.charge_type,t.patient_id,t.visit_id,t.DEPT_DISCHARGE_FROM dept_code,t.discharge_date_time,m.date_of_birth,t.discharge_date_time,t.admission_date_time,dg.diagnosis_desc";
        }
        String tableNames = "MedRec.pat_visit t,medrec.pat_master_index m,medrec.diagnosis dg";
        List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
        TCommonRecord where = CaseHistoryHelperUtils.genWhereCR(
                "t.discharge_date_time",
                CaseHistoryFunction.genRToDate("MedRec.pat_visit",
                        "discharge_date_time", "'" + ADate + "'", "yyyy-mm-dd"),
                "", ">=", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("t.discharge_date_time",
                CaseHistoryFunction.genRToDate("MedRec.pat_visit",
                        "discharge_date_time",
                        "'" + DateUtils.getDateAdded(1, ADate) + "'",
                        "yyyy-mm-dd"),
                "", "<", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("t.patient_id",
                "m.patient_id", "", "join", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("t.patient_id",
                "dg.patient_id", "", "join", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("t.visit_id", "dg.visit_id",
                "", "join", "", "");
        lsWheres.add(where);
        // TODO 2016 年 修改 diagnosis_type 3 -> 1
        where = CaseHistoryHelperUtils.genWhereCR("dg.diagnosis_type", "1",
                "Char", "=", "", "");
        lsWheres.add(where);
        String sql = chhr.genSQL(strFields, tableNames, lsWheres, null, null);
        try
        {
            resultSet = queryHIS.query(sql, new CommonMapper());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        /* 从缓存中查找并填充科室名称 */
        DictCache dc = DictCache.getNewInstance();
        for (TCommonRecord data : resultSet)
        {
            data.set("dept_name", dc.getDeptName(data.get("dept_code")));
        }
        /* 计算年龄 */
        for (TCommonRecord data : resultSet)
        {
            data.set("age",
                    CalcuateAgeUtil.calcuateAgeByTwoDates(
                            data.get("date_of_birth"),
                            data.get("discharge_date_time"), "yyyy-MM-dd"));
        }
        /* 计算天数 */
        for (TCommonRecord data : resultSet)
        {
            data.set("days",
                    CalcuateAgeUtil.getQuot(data.get("admission_date_time"),
                            data.get("discharge_date_time")) + "");
        }

    }

    /***
     * 查询病人费用使用明细
     * 
     * @param query
     * @param cr
     * @return
     */
    public List<TCommonRecord> getInpBillDetail(JDBCQueryImpl query,
            TCommonRecord cr)
    {
        logger.info("查询 patient_id=" + cr.get("patient_id") + " visit_id="
                + cr.get("visit_id") + " 账单明细");
        /* 使用中间层 替换 ----李果 */
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
        String strFields = "*";
        // String strFields =
        // "costs,charges,item_code,item_spec,units,amount,ordered_by," +
        // CaseHistoryFunction.genTrunc("inpbill.Inp_Bill_Detail",
        // "billing_date_time", "billing_date_time") + ",item_class";
        List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
        TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("patient_id",
                cr.get("patient_id"), "Char", "=", "", "");
        lsWheres.add(where);
        where = CaseHistoryHelperUtils.genWhereCR("visit_id",
                cr.get("visit_id"), "Char", "", "", "");
        lsWheres.add(where);
        List<TCommonRecord> lsOrders = new ArrayList<TCommonRecord>();
        TCommonRecord order = CaseHistoryHelperUtils.genOrderCR("ordered_by",
                null);
        lsOrders.add(order);
        List<TCommonRecord> resultData = Collections.emptyList();
        try
        {
            resultData = chhr.fetchInpBillDetail2CR(strFields, lsWheres,
                    resultData, lsOrders, query);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return resultData;
    }

    
    //静脉输液 药品集合
    private Map<String, TCommonRecord> adminOrderMap = new HashMap<String, TCommonRecord>();
    /**
     * 选取静脉输液药品
     * @param query
     * @param cr
     * @param orders
     */
    public void getAdministratorjmsy(JDBCQueryImpl query, TCommonRecord cr)
    {
        logger.info("查询 patient_id=" + cr.get("patient_id") + " visit_id=" + cr.get("visit_id") + ",过程科室=" + cr.get("ordered_by")
                + " 静脉输液");
        // 中间层替换——李果
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
        List<TCommonRecord> result = Collections.emptyList();
        try
        {
            String strFields = " * ";
            List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
            TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("patient_id", cr.get("patient_id"), "Char", "", "", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("visit_id", cr.get("visit_id"), "Char", "", "", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("ordering_dept", cr.get("ordered_by"), "Char", "", "", "");
            lsWheres.add(where);
//            CaseHistoryHelperUtils.genWhereGbkCR("", cr.get(""), "char", "in", "", "");
            List<TCommonRecord> lsOrders = new ArrayList<TCommonRecord>();
            TCommonRecord order = CaseHistoryHelperUtils.genOrderCR("start_date_time", "");
            lsOrders.add(order);
            result = chhr.fetchOrders2CR(strFields, lsWheres, null, lsOrders, query);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            chhr = null;
        }
        
        String[] adminName = Config.getParamValue("IntravenousInfusion").split(",");
        for(int i = 0; i < result.size(); i++)
        {
            TCommonRecord r = result.get(i);
            {
                for(String s :adminName)
                {
                    if(r.get("administration").contains(s))
                    {
                        if(!adminOrderMap.containsKey(r.get("order_code")))
                        {
                           adminOrderMap.put(r.get("order_code"), r); 
                        }
                        break;
                    }
                }    
            }
        }
        
    }
    
    /***
     * 查询病人病原学送检例数 以及 确认专科医生
     * 
     * @param query
     * @param cr
     * @return 病原学送检例数
     * @date 20130809
     */
    public int getEtiologySubmitCount(JDBCQueryImpl query, TCommonRecord cr,
            List<TCommonRecord> orders)
    {
        logger.info("查询 patient_id=" + cr.get("patient_id") + " visit_id=" + cr.get("visit_id") + ",过程科室=" + cr.get("FUNCDEPTCODE")
                + " 病原学送检例数");
        // 中间层替换——李果
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
        List<TCommonRecord> result = Collections.emptyList();
        try
        {
            String strFields = " * ";
            List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
            TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("patient_id", cr.get("patient_id"), "Char", "", "", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("visit_id", cr.get("visit_id"), "Char", "", "", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("ordering_dept", cr.get("FUNCDEPTCODE"), "Char", "", "", "");
            lsWheres.add(where);
            List<TCommonRecord> lsOrders = new ArrayList<TCommonRecord>();
            TCommonRecord order = CaseHistoryHelperUtils.genOrderCR("start_date_time", "");
            lsOrders.add(order);
            result = chhr.fetchOrders2CR(strFields, lsWheres, null, lsOrders, query);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            chhr = null;
        }
        orders.addAll(result);
        String beginDate = "";
        String endDate = "";
        String DeptDoct = "";
        String isOrderFlag = "0";
        boolean tranBool = this.getTransfer(cr.get("FUNCDEPTCODE"),cr.get("patient_id"), cr.get("visit_id"), cr);
        String[] etiologys = Config.getParamValue("EtiologySend").split(",");
        int iCount = 0;
        for (int i = 0; i < result.size(); i++)
        {
            isOrderFlag = "1";
            TCommonRecord r = result.get(i);
            if (iCount == 0)
            {
                for (String s : etiologys)
                {
                    if (r.get("order_text").contains(s))
                    {
                        iCount = 1;
                        break;
                    }
                }
            }
            /* 如果查到专科记录 则不需要之后的计算 */
            if (tranBool)
                continue;
            if (i == 0)
            {
                beginDate = r.get("START_DATE_TIME");
                if (!"".equals(endDate))
                {
                    Date admiDateTime = DateUtils.getDateFromString(cr.get("admission_date_time"),DateUtils.FORMAT_DATETIME);
                    Date beginDatetime = DateUtils.getDateFromString(beginDate,DateUtils.FORMAT_DATETIME);
                    if (admiDateTime.getTime() > beginDatetime.getTime())
                        beginDate = cr.get("admission_date_time");
                }
                DeptDoct = r.get("doctor");
            }
            if ((i + 1) == result.size())
            {
                endDate = "".equals(r.get("STOP_DATE_TIME"))? r.get("START_DATE_TIME") : r.get("STOP_DATE_TIME");
                if (!"".equals(endDate))
                {
                    Date disDateTime = DateUtils.getDateFromString(cr.get("discharge_Date_time"),DateUtils.FORMAT_DATETIME);
                    Date endDatetime = DateUtils.getDateFromString(endDate,DateUtils.FORMAT_DATETIME);
                    if (disDateTime.getTime() < endDatetime.getTime())
                        endDate = cr.get("discharge_Date_time");
                }
            }
            DeptDoct = r.get("doctor");
        }
        if (!tranBool)
        {
            if (result != null && result.size() > 0)
            {
                // 过程开始时间
                cr.set("FUNCDEPTBEGINDATE", beginDate);
                // 过程结束时间
                cr.set("FUNCDEPTENDDATE", endDate);
                // 过程科室住院天数
                cr.set("FUNCDAYS", String.valueOf(CalcuateAgeUtil.getQuot(beginDate, endDate)));
                // 根据医嘱确定医生
                cr.set("DOCTOR", DeptDoct);
            }
            else
            {
                // 又该收费 但没有医嘱 很可能是因为 介入手术的情况。
                cr.set("DOCTOR", "");
            }
        }
        cr.set("IS_ORDERFLAG", isOrderFlag);
        logger.info("查询 patient_id=" + cr.get("patient_id") + " visit_id=" + cr.get("visit_id") + " 病原学送检例数 :" + iCount);
        return iCount;
    }

    public boolean getTransfer(String deptCode, String p_id, String v_id,
            TCommonRecord cr)
    {
        boolean ret = false;
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
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
            List<TCommonRecord> list = chhr.fetchTransfer2CR(strFields,
                    lsWheres, null, null, null);
            // 如果没找记录直接返回
            if (list == null || list.size() == 0)
                return ret;
            ret = true;
            TCommonRecord tComm = list.get(0);
            cr.set("FUNCDEPTBEGINDATE",
                    tComm.getDateTimeString("ADMISSION_DATE_TIME"));
            cr.set("FUNCDEPTENDDATE",
                    tComm.getDateTimeString("DISCHARGE_DATE_TIME"));
            String funcDays = "";
            int days = 0;
            for (TCommonRecord data : list)
            {
                days += CalcuateAgeUtil.getQuot(
                        data.getDateTimeString("ADMISSION_DATE_TIME"),
                        data.getDateTimeString("DISCHARGE_DATE_TIME"));
            }
            funcDays = String.valueOf(days);
            cr.set("FUNCDAYS", funcDays);
            cr.set("DOCTOR", tComm.get("DOCTOR_IN_CHARGE"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            chhr = null;
        }
        return ret;
    }

    /***
     * 查询患者是否存在手术信息
     * 
     * @param query
     * @param cr
     * @return 存在1 不存在0
     */
    public String getOperation(JDBCQueryImpl query, TCommonRecord cr)
    {
        String counter = "0";
        try
        {
            // 中间层替换
            ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
            List<TCommonRecord> resultList = chhr.fetchOperation2CR(
                    cr.get("patient_id"), cr.get("visit_id"), query);

            logger.info("查询 patient_id=" + cr.get("patient_id") + " visit_id="
                    + cr.get("visit_id") + " 存在" + resultList.size() + "条手术信息");
            counter = resultList.size() > 0 ? "1" : counter;
            // this.saveOperationResultSet(resultList, cr);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return counter;
    }

    /***
     * 插入出院患者手术记录
     */
    public void saveOperationResultSet(List<TCommonRecord> list,
            TCommonRecord tcr)
    {
        TCommonRecord tc = new TCommonRecord();
        tc.setObj("tcr", tcr);
        tc.setObj("list", list);
        TransactionTemp tt = new TransactionTemp("PatientHistory");
        tt.execute(new TransaCallback(tc) {
            @SuppressWarnings ("unchecked")
            @Override
            public void ExceuteSqlRecord()
            {
                JDBCQueryImpl ph = DBQueryFactory.getQuery("ph");
                DDDCountByBill_SCD.this.deleteOldOperation(ph,
                        (TCommonRecord) (this.getTranParm().getObj("tcr")));
                int has_anti = 0;
                List<TCommonRecord> list = (List<TCommonRecord>) this
                        .getTranParm().getObj("list");
                for (int j = 0; j < list.size(); j++)
                {
                    logger.info("第-----------------" + (j + 1)
                            + "---------------共---------------" + list.size()
                            + "手术");
                    TCommonRecord o = list.get(j);
                    List<TCommonRecord> order = null;
                    ICaseHistoryHelper chhr = CaseHistoryFactory
                            .getCaseHistoryHelper();
                    String strFields = "patient_id,visit_id,order_no,order_sub_no,order_code,start_date_time,repeat_indicator";
                    List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
                    TCommonRecord where = CaseHistoryHelperUtils.genWhereCR(
                            "order_class",
                            Config.getParamValue("Drug_In_Order"), "Char", "",
                            "", "");
                    lsWheres.add(where);
                    where = CaseHistoryHelperUtils.genWhereCR("order_status",
                            "2", "", "", "1", "");
                    lsWheres.add(where);
                    where = CaseHistoryHelperUtils.genWhereCR("order_status",
                            "3", "", "", "1", "or");
                    lsWheres.add(where);
                    where = CaseHistoryHelperUtils.genWhereCR("patient_id",
                            o.get("patient_id"), "Char", "", "", "");
                    lsWheres.add(where);
                    where = CaseHistoryHelperUtils.genWhereCR("visit_id",
                            o.get("visit_id"), "Char", "", "", "");
                    lsWheres.add(where);
                    where = CaseHistoryHelperUtils.genWhereCR(
                            "repeat_indicator", "0", "Char", "", "2", "");
                    lsWheres.add(where);
                    where = CaseHistoryHelperUtils.genWhereCR("start_date_time",
                            CaseHistoryFunction.genRToDate("ordadm.orders",
                                    "start_date_time", "'"
                                            + DateUtils.getDateAdded(-1,
                                                    ((TCommonRecord) getTranParm()
                                                            .getObj("tcr"))
                                                                    .get("ADate"))
                                            + "'",
                                    "yyyy-mm-dd"),
                            "", ">=", "2", "");
                    lsWheres.add(where);
                    where = CaseHistoryHelperUtils.genWhereCR(
                            "repeat_indicator", "1", "Char", "", "3", "or");
                    lsWheres.add(where);
                    where = CaseHistoryHelperUtils.genWhereCR("stop_date_time",
                            CaseHistoryFunction.genRToDate("ordadm.orders",
                                    "stop_date_time", "'"
                                            + DateUtils.getDateAdded(-1,
                                                    ((TCommonRecord) getTranParm()
                                                            .getObj("tcr"))
                                                                    .get("ADate"))
                                            + "'",
                                    "yyyy-mm-dd"),
                            "", ">=", "3", "");
                    lsWheres.add(where);
                    where = CaseHistoryHelperUtils.genWhereCR("start_date_time",
                            CaseHistoryFunction.genRToDate("ordadm.orders",
                                    "start_date_time", "'"
                                            + DateUtils.getDateAdded(-1,
                                                    ((TCommonRecord) getTranParm()
                                                            .getObj("tcr"))
                                                                    .get("ADate"))
                                            + "'",
                                    "yyyy-mm-dd"),
                            "", "<=", "3", "");
                    lsWheres.add(where);
                    try
                    {
                        order = chhr.fetchOrders2CR(strFields, lsWheres, null,
                                null, null);
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
                    List<Object> sqlParams = new ArrayList<Object>();
                    String dstSQL = "Insert into Operation_out (PATIENT_ID, VISIT_ID,OPERATION_NO,has_anti , OPERATION_DESC, OPERATION_CODE, HEAL, WOUND_GRADE, "
                            + "OPERATING_DATE, ANAESTHESIA_METHOD, OPERATOR, FIRST_ASSISTANT, SECOND_ASSISTANT, ANESTHESIA_DOCTOR ,DEPT_CODE,DEPT_NAME , LINK_DATE) "
                            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    sqlParams.add(o.get("PATIENT_ID"));
                    sqlParams.add(o.get("VISIT_ID"));
                    sqlParams.add(o.get("OPERATION_NO"));
                    sqlParams.add(has_anti);
                    sqlParams.add(o.get("OPERATION_DESC").replace("'", ""));
                    sqlParams.add(o.get("OPERATION_CODE"));
                    sqlParams.add(o.get("HEAL"));
                    sqlParams.add(o.get("WOUND_GRADE"));
                    Timestamp dateTime = new Timestamp(
                            DateUtils.getDateFromString(o.get("OPERATING_DATE"))
                                    .getTime());
                    sqlParams.add(dateTime);
                    sqlParams.add(o.get("ANAESTHESIA_METHOD"));
                    sqlParams.add(o.get("OPERATOR"));
                    sqlParams.add(o.get("FIRST_ASSISTANT"));
                    sqlParams.add(o.get("SECOND_ASSISTANT"));
                    sqlParams.add(o.get("ANESTHESIA_DOCTOR").replace("'", ""));
                    sqlParams.add(o.get("dept_code"));
                    sqlParams.add(o.get("Dept_Name"));
                    dateTime = new Timestamp(
                            DateUtils
                                    .getDateFromString(
                                            ((TCommonRecord) getTranParm()
                                                    .getObj("tcr"))
                                                            .get("ADate"))
                                    .getTime());
                    sqlParams.add(dateTime);

                    ph.update(dstSQL, sqlParams.toArray());
                    has_anti = 0;
                }
                ph = null;
            }
        });
    }

    public void deleteOldOperation(JDBCQueryImpl queryPH, TCommonRecord tcr)
    {
        logger.info("删除Operation_out旧数据");
        queryPH.update("delete Operation_out t where  t.link_date = to_date('"
                + tcr.get("ADate") + "','yyyy-mm-dd') and patient_id='"
                + tcr.get("patient_id") + "' and visit_id='"
                + tcr.get("visit_id") + "'");
    }

    @Override
    public String getLogFileName()
    {
        return null;
    }

    // 处理日志
    public void buildBegin(String ADate, Task AOwner)
    {
        resultSet = new ArrayList<TCommonRecord>();
        supplier = new ArrayList<TCommonRecord>();
        /* 病人转科室分解数据集 */
        rsSplit = new ArrayList<TCommonRecord>();
        // 所有的额账单信息避免多次查询
        allBillDetials = new ArrayList<TCommonRecord>();

        LoggerFileSaveUtil.LogFileSave(logger,
                "APPLOG\\ddd\\DR_" + ADate + "DDD.log");
    }

    private void DeleteRsSplit(JDBCQueryImpl queryIas, String date)
    {
        logger.info("删除ias_pat_info_scd旧数据");
        queryIas.update("delete ias_pat_info_scd where RPT_DATE = to_date('"
                + date + "','yyyy-mm-dd')");
    }

    private void saveResultSet(String ADate)
    {
        TCommonRecord tc = new TCommonRecord();
        tc.set("ADate", ADate);
        TransactionTemp tt = new TransactionTemp("ph");
        tt.execute(new TransaCallback(tc) {
            @Override
            public void ExceuteSqlRecord()
            {
                JDBCQueryImpl Jquery = DBQueryFactory.getQuery("ph");
                /* 删除旧数据 */
                DDDCountByBill_SCD.this.DeleteRsSplit(Jquery,
                        this.getTranParm().get("ADate"));
                int i = 1;
                Map<String, String> tempSqlMapping = new HashMap<String, String>();
                for (TCommonRecord cr : rsSplit)
                {
                    logger.info("第-----" + i + "-----共-----" + rsSplit.size()
                            + "DDD值");
                    List<Object> sqlParams = new ArrayList<Object>();
                    StringBuffer sql = new StringBuffer();
                    sql.append(
                            " insert into ias_pat_info_scd ( RPT_DATE, patient_id, visit_id, species, dept_name, dept_code, age, days, ddd_value,  spec_ddd_value, ");
                    sql.append(
                            " limit_ddd_value, doctor, max_drug_aday, ordinaryDDDS, is_anti, identity, charge_type, costs, charges, drug_costs, drug_charges, anti_costs, anti_charges, "
                                    + "outdrug_costs,outdrug_charges,outAntiDDD,outAntiCosts,outAntiCharges,outspecDDDS,outlimitDDDS,outordinaryDDDS,is_out,"
                                    + "FUNCDEPTCODE,FUNCDEPTNAME,FUNCDEPTBEGINDATE,FUNCDEPTENDDATE,FUNCDAYS,IS_SCD,PAT_FUNCT_NO,IS_ORDERFLAG,submit_count,first_diagnosis,ADMINJMSY,ADMINANTIJMSY,adminJMSYCost,adminAntiJMSYCost,injection,injectionCost,injectionAnti,injectionAntiCost");
                    sql.append(
                            ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    Timestamp dateTime = new Timestamp(DateUtils
                            .getDateFromString(getTranParm().get("ADate"))
                            .getTime());
                    sqlParams.add(dateTime);
                    sqlParams.add(cr.get("patient_id"));
                    sqlParams.add(cr.get("visit_id"));
                    sqlParams.add(cr.get("species"));
                    sqlParams.add(cr.get("dept_name"));
                    sqlParams.add(cr.get("dept_code"));
                    sqlParams.add(cr.get("age"));
                    sqlParams.add(cr.getDouble("days"));
                    sqlParams.add(cr.get("ddd_value"));
                    sqlParams.add(cr.get("spec_ddd_value"));
                    sqlParams.add(cr.get("limit_ddd_value"));
                    sqlParams.add(cr.get("doctor").replace("'", ""));
                    sqlParams.add(cr.get("max_drug_aday"));
                    sqlParams.add(cr.get("ordinaryDDDS"));
                    sqlParams.add(cr.get("is_anti"));
                    sqlParams.add(cr.get("identity"));
                    sqlParams.add(cr.get("charge_type"));
                    sqlParams.add(cr.get("costs"));
                    sqlParams.add(cr.get("charges"));
                    sqlParams.add(cr.get("drug_costs"));
                    sqlParams.add(cr.get("drug_charges"));
                    sqlParams.add(cr.get("anti_costs"));
                    sqlParams.add(cr.get("anti_charges"));
                    sqlParams.add(cr.get("outdrug_costs")); // 局部药物费用
                    sqlParams.add(cr.get("outdrug_charges")); // 局部实收费用
                    sqlParams.add(cr.get("outAntiDDD")); // 局部抗菌药物
                    sqlParams.add(cr.get("outAntiCosts")); // 局部抗菌药物费用
                    sqlParams.add(cr.get("outAntiCharges")); // 局部抗菌药实收费用
                    sqlParams.add(cr.get("outspecDDDS")); // 局部特殊级抗生素使用频度
                    sqlParams.add(cr.get("outlimitDDDS")); // 局部限制级抗生素使用频度
                    sqlParams.add(cr.get("outordinaryDDDS")); // 局部普通级抗生素使用频度
                    sqlParams.add(cr.get("is_out")); // 是否使用外用抗菌药物
                    sqlParams.add(cr.get("FUNCDEPTCODE")); // 过程科室code
                    sqlParams.add(cr.get("FUNCDEPTNAME")); // 过程科室名称
                    dateTime = new Timestamp(DateUtils
                            .getDateFromString(
                                    cr.getDateTimeString("FUNCDEPTBEGINDATE"))
                            .getTime());
                    sqlParams.add(dateTime);
                    dateTime = new Timestamp(DateUtils
                            .getDateFromString(
                                    cr.getDateTimeString("FUNCDEPTENDDATE"))
                            .getTime());
                    sqlParams.add(dateTime);
                    sqlParams.add(cr.get("FUNCDAYS")); // 过程中住院天数
                    sqlParams.add(cr.get("IS_SCD")); // 转科标示
                    sqlParams.add(cr.get("PAT_FUNCT_NO")); // 病人过程唯一号码 no =
                                                           // date-pid-vid
                    sqlParams.add(cr.get("IS_ORDERFLAG")); // 病人过程唯一号码 no =
                                                           // date-pid-vid
                    sqlParams.add(cr.get("submit_count"));
                    sqlParams.add(cr.get("diagnosis_desc"));

                    sqlParams.add(cr.get("ADMINJMSY")); // 静脉输液
                    sqlParams.add(cr.get("ADMINANTIJMSY"));// 静脉输液 抗菌药物
                    
                    sqlParams.add(cr.get("adminJMSYCost"));
                    // 抗菌药物  给药途径静脉输液
                    sqlParams.add(cr.get("adminAntiJMSYCost"));
                    
                    // 注射药物
                    sqlParams.add(cr.get("injection"));
                    sqlParams.add(cr.get("injectionCost"));
                    sqlParams.add(cr.get("injectionAnti"));
                    sqlParams.add(cr.get("injectionAntiCost"));
                    
                    String tempKey = cr.get("patient_id") + "_"
                            + cr.get("visit_id") + "_"
                            + this.getTranParm().get("ADate") + "_"
                            + cr.get("FUNCDEPTCODE");
                    if (null == tempSqlMapping.get(tempKey))
                    {
                        Jquery.update(sql.toString(), sqlParams.toArray());
                        tempSqlMapping.put(tempKey, sql.toString());
                        sql.setLength(0);
                    }
                    i++;
                }
                Jquery = null;
                resultSet = null;
                supplier = null;
            }
        });
    }

    private void deleteBillDetail(JDBCQueryImpl queryIas, String date)
    {
        logger.info("删除ias_inp_bill_detail旧数据");
        queryIas.update("delete ias_inp_bill_detail where RPT_DATE = to_date('"
                + date + "','yyyy-mm-dd')");
    }

    private void saveBillDetail(String ADate)
    {
        TCommonRecord tc = new TCommonRecord();
        tc.set("ADate", ADate);
        TransactionTemp tt = new TransactionTemp("ph");
        tt.execute(new TransaCallback(tc) {
            @Override
            public void ExceuteSqlRecord()
            {
                JDBCQueryImpl Jquery = DBQueryFactory.getQuery("ph");
                /* 删除旧数据 */
                DDDCountByBill_SCD.this.deleteBillDetail(Jquery,
                        this.getTranParm().get("ADate"));
                List<Object> sqlParams = new ArrayList<Object>();
                StringBuffer sql = new StringBuffer();
                for (TCommonRecord cr : allBillDetials)
                {
                    sql.append(
                            " insert into IAS_INP_BILL_DETAIL ( PATIENT_ID,VISIT_ID,ITEM_NO,ITEM_CLASS,ITEM_NAME,ITEM_CODE,ITEM_SPEC,AMOUNT,UNITS,");
                    sql.append(
                            "ORDERED_BY,PERFORMED_BY,COSTS,CHARGES,BILLING_DATE_TIME,OPERATOR_NO,RCPT_NO,ST_DATE,CLASS_ON_INP_RCPT,CLASS_ON_RECKONING,");
                    sql.append(
                            "SUBJ_CODE,CLASS_ON_MR,SPECIAL_CHARGES,ZFBL,SEND_FLAG,INSUR_TRADE_FEE,INSUR_CLASS_FEE,INSUR_SELF_FEE,INSUR_RCPT_CLASS,");
                    sql.append(
                            "REFUND_ITEM_NO,PRICE_LIMIT,FREE_LIMIT,DOCTOR_IN_CHARGE,IS_ANTI,DDD_VALUE,RPT_DATE");
                    sql.append(
                            ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    sqlParams.add(cr.get("patient_id"));
                    sqlParams.add(cr.get("visit_id"));
                    sqlParams.add(cr.get("ITEM_NO"));
                    sqlParams.add(cr.get("ITEM_CLASS"));
                    sqlParams.add(cr.get("ITEM_NAME"));
                    sqlParams.add(cr.get("ITEM_CODE"));
                    sqlParams.add(cr.get("ITEM_SPEC"));
                    sqlParams.add(cr.get("AMOUNT"));
                    sqlParams.add(cr.get("UNITS"));
                    sqlParams.add(cr.get("ORDERED_BY"));
                    sqlParams.add(cr.get("PERFORMED_BY"));
                    sqlParams.add(cr.get("COSTS"));
                    sqlParams.add(cr.get("CHARGES"));
                    Timestamp dateTime = new Timestamp(DateUtils
                            .getDateFromString(cr.get("BILLING_DATE_TIME"))
                            .getTime());
                    sqlParams.add(dateTime);
                    sqlParams.add(cr.get("OPERATOR_NO"));
                    sqlParams.add(cr.get("RCPT_NO"));
                    dateTime = new Timestamp(DateUtils
                            .getDateFromString(cr.get("ST_DATE")).getTime());
                    sqlParams.add(dateTime);
                    sqlParams.add(cr.get("CLASS_ON_INP_RCPT"));
                    sqlParams.add(cr.get("CLASS_ON_RECKONING"));
                    sqlParams.add(cr.get("SUBJ_CODE"));
                    sqlParams.add(cr.get("CLASS_ON_MR"));
                    sqlParams.add(cr.get("SPECIAL_CHARGES"));
                    sqlParams.add(cr.get("ZFBL"));
                    sqlParams.add(cr.get("SEND_FLAG"));
                    sqlParams.add(cr.get("INSUR_TRADE_FEE"));
                    sqlParams.add(cr.get("INSUR_CLASS_FEE"));
                    sqlParams.add(cr.get("INSUR_SELF_FEE"));
                    sqlParams.add(cr.get("INSUR_RCPT_CLASS"));
                    sqlParams.add(cr.get("REFUND_ITEM_NO"));
                    sqlParams.add(cr.get("PRICE_LIMIT"));
                    sqlParams.add(cr.get("FREE_LIMIT"));
                    sqlParams.add(cr.get("DOCTOR_IN_CHARGE"));
                    sqlParams.add(cr.get("IS_ANTI"));
                    sqlParams.add(cr.get("DDD"));
                    dateTime = new Timestamp(DateUtils
                            .getDateFromString(getTranParm().get("ADate"))
                            .getTime());
                    sqlParams.add(dateTime);

                    Jquery.update(sql.toString(), sqlParams.toArray());
                    sqlParams.clear();
                    sql.setLength(0);
                }
            }
        });
    }

    public void buildOver(String ADate, Task AOwner)
    {
        logger.info("开始保存日报");
        this.saveResultSet(ADate);
        logger.info("保存日报结束");

        logger.info("开始保存账单详细");
        this.saveBillDetail(ADate);
        allBillDetials = null;
        rsSplit = null;
        logger.info("保存账单详细结束");

    }
}