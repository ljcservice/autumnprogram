package com.ts.FetcherHander.OutPatient.dayReport;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hitzd.DBUtils.CommonMapper;
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
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;
import com.hitzd.his.task.Task;
import com.hitzd.persistent.Persistent4DB;
import com.ts.FetcherHander.OutPatient.DataFetcherNew;
import com.ts.util.UuidUtil;

/**
 * 药品统计支持部队医院 军委一号的情况 
 * 从门诊费用账单出获取的数据。
 * @author jingcong
 *
 */
public class DR_OutDrugCostInfo_BillItems extends Persistent4DB implements IReportBuilder
{
    private static final Logger logger = Logger.getLogger(DR_OutDrugCostInfo_BillItems.class);
    @Override
    public String getLogFileName()
    {
        // TODO Auto-generated method stub
        return null;
    }
    /* 拆分厂家与规格 */
    List<TCommonRecord> supplier = new ArrayList<TCommonRecord>();
    @Override
    public String BuildReport(String ADate, Task AOwner)
    {
        try
        {
            if(supplier == null || supplier.size() == 0)
            {
                supplier = StringUtils.getSupplier();
            }
            DrugDayCost(ADate);
            DrugDoctorDeptCost(ADate);
            DrugPatDDCost(ADate);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "true";
    }
    
    /**
     * 按天单药品费用统计
     * @param ADate
     */
    private void DrugDayCost(final String ADate)
    {
        
        TransactionTemp tt = new TransactionTemp("ph"); 
        tt.execute(new TransaCallback(null) 
        {
            @SuppressWarnings ("unchecked")
            @Override
            public void ExceuteSqlRecord()
            {
                DictCache dc = DictCache.getNewInstance();
                CommonMapper cmr = new CommonMapper();
//                JDBCQueryImpl his = DBQueryFactory.getQuery("HIS");
                JDBCQueryImpl query = DBQueryFactory.getQuery("ph");
                /*
                ICaseHistoryHelper ichr = CaseHistoryFactory.getCaseHistoryHelper();
                String strFields = " t.VISIT_DATE,t.VISIT_NO,t.RCPT_NO,t.ITEM_NO,t.ITEM_CLASS,t.CLASS_ON_RCPT,t.ITEM_CODE,t.ITEM_NAME,t.ITEM_SPEC,t.AMOUNT,t.UNITS,t.PERFORMED_BY,t.COSTS,t.CHARGES"
                        + ",b.patient_id,b.ordered_by_dept,b.ordered_by_doctor";
                String strTables = " outpbill.outp_bill_items t,outpbill.outp_order_desc b";
                List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
                List<TCommonRecord> lsGroups = new ArrayList<TCommonRecord>();
                TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("t.item_class", Config.getParamValue("Drug_In_Order"), "Char", "", "1", "");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR("t.item_class", Config.getParamValue("Drug_In_Order_Chinese"), "Char", "", "1", "or");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR(
                        "t.visit_date"
                        , CaseHistoryFunction.genRToDate("outpbill.outp_bill_items", "t.visit_date", "'" + ADate + "'", "yyyy-mm-dd")
                        , "", ">=", "", "");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR(
                        "t.visit_date"
                        ,CaseHistoryFunction.genRToDate("outpbill.outp_bill_items", "t.visit_date", "'" + DateUtils.getDateAdded(1,ADate) + "'", "yyyy-mm-dd")
                        , "", "<", "", "");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR("t.visit_date", "b.visit_date", "", "=", "", "");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR("t.visit_no", "b.visit_no", "", "=", "", "");
                lsWheres.add(where);
                String gensql = "[" + ichr.genSQL(strFields, strTables, lsWheres, lsGroups, null) + "] m";
                strFields = "m.item_name,m.item_code,m.item_spec,m.units,m.ordered_by_dept,m.performed_by,sum(m.COSTS) costs,sum(m.amount) amount" ;
                TCommonRecord group = CaseHistoryHelperUtils.genGroupCR("m.item_name");
                lsGroups.add(group); 
                group = CaseHistoryHelperUtils.genGroupCR(" m.item_code ");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.item_spec");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.units");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.ordered_by_dept");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.performed_by");
                lsGroups.add(group);
                gensql = ichr.genSQL(strFields, gensql, null, lsGroups, null);
                List<TCommonRecord> listDDC = his.query(gensql, cmr);*/
                StringBuffer strBuff  = new StringBuffer();
                strBuff.append("select m.item_name,m.item_code,m.item_spec,m.units,m.ordered_by_dept_code as ordered_by_dept ,m.performed_by_code as performed_by,sum(m.COSTS) costs,sum(m.amount) amount from ");
                strBuff.append("(");
                strBuff.append("select t.VISIT_DATE,");
                strBuff.append("       t.VISIT_NO,");
                strBuff.append("       t.RCPT_NO,");
                strBuff.append("       t.ITEM_NO,");
                strBuff.append("       t.ITEM_CLASS,");
                strBuff.append("       t.CLASS_ON_RCPT,");
                strBuff.append("       t.ITEM_CODE,");
                strBuff.append("       t.ITEM_NAME,");
                strBuff.append("       t.ITEM_SPEC,");
                strBuff.append("       t.AMOUNT,");
                strBuff.append("       t.UNITS,");
                strBuff.append("       t.PERFORMED_BY_code,");
                strBuff.append("       t.COSTS,");
                strBuff.append("       t.CHARGES,");
                strBuff.append("       t.patient_id,");
                strBuff.append("       t.ordered_by_dept_code,");
                strBuff.append("       t.ordered_by_doctor");
                strBuff.append("  from outp_orders_costs t");
                strBuff.append("  where t.visit_date >= to_date('" + ADate + "','yyyy-mm-dd') and t.visit_date < to_date('" + DateUtils.getDateAdded(1,ADate) + "','yyyy-mm-dd')");
                strBuff.append("  and t.item_class in ('" + Config.getParamValue("Drug_In_Order") + "','" + Config.getParamValue("Drug_In_Order_Chinese") + "')");
                strBuff.append(") m ");
                strBuff.append("group by m.item_name, m.item_code ,m.item_spec,m.units,m.ordered_by_dept_code,m.performed_by_code");
                List<TCommonRecord> listDDC = query.query(strBuff.toString(), cmr);
                query.update("delete dr_drugDayCost where dr_date = to_date('" + ADate + "','yyyy-mm-dd')");
                System.out.println(" dr_drugDayCost-总条数:" + listDDC.size());
                int i = 0;
                for(TCommonRecord t : listDDC)
                {
                    List<Object> sqlParams = new ArrayList<Object>();
                    StringUtils.execCF(supplier, t);
                    t.set("is_anti","0" );
                    double dddValue = 0d;
                    dddValue = AntiDDDSum(t, dddValue);
                    TCommonRecord tCom = dc.getDrugDictInfo(t.get("item_code"));
                    String sql = "insert into dr_drugDayCost(dr_date,drug_code, drug_name, cost, package_spec, amount, firm_id, dept_code, dept_name, dispensary, toxi_property, is_anti, dispensary_name, units,ddd_value"
                            + ",is_base,is_exhilarant,is_injection,is_oral,anti_level,is_impregnant,IS_NOCHINESEDRUG,is_external,is_chinesedrug,IS_ALLERGY, is_patentdrug,is_tumor,is_poison,is_psychotic,is_habitforming,is_radiation,is_precious,is_danger,is_assist,is_albumin,id ) values ( "
                        + "to_date('"  + ADate + "','yyyy-mm-dd')"
                        + ",'" + t.get("item_code",true) + "'"
                        + ",'" + t.get("item_name",true) + "'"
                        + ",'" + t.get("costs",true) + "'"
                        + ",'" + t.get("item_spec",true) + "'"
                        + ",'" + t.get("amount",true) + "'"
                        + ",'" + t.get("firm_id",true) + "'"
                        + ",'" + t.get("ordered_by_dept",true) + "'"
                        + ",'" + dc.getDeptName(t.get("ordered_by_dept")) + "'"
                        + ",'" + t.get("performed_by",true) + "'"
                        + ",'" + tCom.get("toxi_property",true) + "'"
                        + ",'" + t.get("is_anti",true) + "'"
                        + ",'" + dc.getDeptName(t.get("performed_by")) + "'"
                        + ",'" + t.get("units",true) + "'"
                        + ",'" + dddValue + "'"
                        + ",'" + getflag(DrugUtils.isCountryBase(t.get("item_code")))  + "'" // is_base '基药剂标识，0是非基药，1是基药';
                        + ",'" + getflag(DrugUtils.isExhilarantDrug(t.get("item_code")))  + "'" // is_exhilarant '兴奋剂标识，0是非兴奋剂，1是兴奋剂';
                        + ",'" + getflag(DrugUtils.isZSDrug(t.get("item_code"))) + "'"// is_injection '注射剂标识，0是非注射剂，1是注射剂';
                        + ",'" + getflag(DrugUtils.IsOralDrug(t.get("item_code"))) + "'"// is_oral '口服制剂标识，0是非口服，1是口服';
                        + ",'" + DrugUtils.getDrugAntiByLevel(t.get("item_code")) + "'" // anti_level'抗菌药级别，1是非限制用药，2是限制用药，3是特殊用药';
                        + ",'" + getflag(DrugUtils.isImpregnant(t.get("item_code")))  + "'"// is_impregnant '溶剂标识，0是非溶剂，1是溶剂';
                        + ",'" + getflag(!DrugUtils.isChineseDrug(t.get("item_code")))  + "'"// IS_NOCHINESEDRUG '药理分类， 0 饮片 1 非饮片 ';
//                        + ",'" + getflag(DrugUtils.isExhilarantDrug(t.get("item_code")))  + "'"// drug_catalog '药品的分类标识，例如青霉素类、头孢类。。。。';
                        + ",'" + getflag(DrugUtils.isExternalDrug(t.get("item_code")))  + "'"// is_external '外用标识，0是非外用，1是外用';
                        + ",'" + getflag(DrugUtils.isChineseDrug(t.get("item_code")))  + "'"// is_chinesedrug '中药标识，0是非中药，1是中药';
                        + ",'" + getflag(DrugUtils.isAntiAllergyDrug(t.get("item_code")))  + "'"// is_allergy '抗过敏标识，0是非抗过敏药物，1是抗过敏药物';
//                        + ",'" + getflag(DrugUtils.isExhilarantDrug(t.get("item_code")))  + "'"// is_medcare_country '国家医保标识，0是非医保，1是甲类医保，2是乙类医保';
//                        + ",'" + getflag(DrugUtils.isExhilarantDrug(t.get("item_code")))  + "'"// is_medcare_local '地方医保标识，0是非医保，1是甲类医保，2是乙类医保';
                        + ",'" + getflag(DrugUtils.isPatentDrug(t.get("item_code")))  + "'"// is_patentdrug  '中成药标识，0是非中成药，1是中成药';
                        + ",'" + getflag(DrugUtils.isTumor(t.get("item_code")))  + "'"// is_tumor  '抗肿瘤标识，0是非抗肿瘤药，1是抗肿瘤药';
                        + ",'" + getflag(DrugUtils.isDDrug(t.get("item_code")))  + "'"// is_poison '毒药标识，0是非毒药，1是毒药';
                        + ",'" + getflag(DrugUtils.isJSDrug(t.get("item_code")))  + "'"// is_psychotic'精神药标识，0是非精神药，1是精神药,';
                        + ",'" + getflag(DrugUtils.isMDrug(t.get("item_code")))  + "'"// is_habitforming '麻药标识，0是非麻药，1是麻药';
                        + ",'" + getflag(DrugUtils.isFSDrug(t.get("item_code")))  + "'"// is_radiation '放射标识，0是非放射药，1是放射药';
                        + ",'" + getflag(DrugUtils.isGZDrug(t.get("item_code")))  + "'"// is_precious '贵重药标识，0是非贵重药，1是贵重药';
                        + ",'" + getflag(DrugUtils.isDanger(t.get("item_code")))  + "'"// is_danger '危险级别： 0-不是 1-是危险药品;
                        + ",'" + getflag(DrugUtils.IsAssist(t.get("item_code")))  + "'"// is_assist '辅助用药 0 否 1 是';
                        + ",'" + getflag(DrugUtils.isAlbumin(t.get("item_code")))  + "'"// is_albumin '白蛋白 0 否 1 是';
                        + ",'" + UuidUtil.get32UUID()  + "'"//  表主键id

                        + " )";
                    query.update(sql);
                    logger.debug("=====添加数量为" + listDDC.size() + "/" + (++i) + "====");
                }
                cmr       = null;
                query     = null;
                listDDC   = null;
            }
        });
    }
    
    private String getflag(boolean rs)
    {
        return  rs ? "1":"0";
    }
    
    /**
     * 按天医生单药品费用统计
     * @param ADate
     */
    private void DrugDoctorDeptCost(final String ADate)
    {
        TransactionTemp tt = new TransactionTemp("ph");
        tt.execute(new TransaCallback(null) 
        {
            @SuppressWarnings ("unchecked")
            @Override
            public void ExceuteSqlRecord()
            {
                DictCache           dc = DictCache.getNewInstance();
                CommonMapper       cmr = new CommonMapper();
//                JDBCQueryImpl    his = DBQueryFactory.getQuery("HIS");
                JDBCQueryImpl query = DBQueryFactory.getQuery("ph");
                /*
                ICaseHistoryHelper ichr = CaseHistoryFactory.getCaseHistoryHelper();
                String strFields = "t.VISIT_DATE,t.VISIT_NO,t.RCPT_NO,t.ITEM_NO,t.ITEM_CLASS,t.CLASS_ON_RCPT,t.ITEM_CODE,t.ITEM_NAME,t.ITEM_SPEC,t.AMOUNT,t.UNITS,t.PERFORMED_BY,t.COSTS,t.CHARGES"
                        + ",b.patient_id,b.ordered_by_dept,b.ordered_by_doctor";
                String strTables = "outpbill.outp_bill_items t,outpbill.outp_order_desc b";
                List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
                List<TCommonRecord> lsGroups = new ArrayList<TCommonRecord>();
                TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("t.item_class", Config.getParamValue("Drug_In_Order"), "Char", "", "1", "");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR("t.item_class", Config.getParamValue("Drug_In_Order_Chinese"), "Char", "", "1", "or");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR(
                        "t.visit_date"
                        , CaseHistoryFunction.genRToDate("outpbill.outp_bill_items", "t.visit_date", "'" + ADate + "'", "yyyy-mm-dd")
                        , "", ">=", "", "");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR(
                        "t.visit_date"
                        ,CaseHistoryFunction.genRToDate("outpbill.outp_bill_items", "t.visit_date", "'" + DateUtils.getDateAdded(1,ADate) + "'", "yyyy-mm-dd")
                        , "", "<", "", "");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR("t.visit_date", "b.visit_date", "", "=", "", "");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR("t.visit_no", "b.visit_no", "", "=", "", "");
                lsWheres.add(where);
                String gensql = "[" + ichr.genSQL(strFields, strTables, lsWheres, lsGroups, null) + "] m";
                strFields = "m.item_name,m.item_code,m.item_spec,m.units,m.ordered_by_dept,m.performed_by,sum(m.COSTS) costs,sum(m.amount) amount,m.ordered_by_doctor" ;
                TCommonRecord group = CaseHistoryHelperUtils.genGroupCR("m.item_name");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR(" m.item_code ");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.item_spec");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.units");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.ordered_by_dept");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.performed_by");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.ordered_by_doctor");
                lsGroups.add(group);
                gensql = ichr.genSQL(strFields, gensql, null, lsGroups, null);
                List<TCommonRecord> listDDDC = his.query(gensql, cmr);*/
                StringBuffer strBuff  = new StringBuffer();
                strBuff.append("select m.item_name,");
                strBuff.append("       m.item_code,");
                strBuff.append("       m.item_spec,");
                strBuff.append("       m.units,");
                strBuff.append("       m.ordered_by_dept_code ordered_by_dept ,");
                strBuff.append("       m.performed_by_code performed_by ,");
                strBuff.append("       sum(m.COSTS) costs,");
                strBuff.append("       sum(m.amount) amount,");
                strBuff.append("       m.ordered_by_doctor");
                strBuff.append("  from (select t.VISIT_DATE,");
                strBuff.append("               t.VISIT_NO,");
                strBuff.append("               t.RCPT_NO,");
                strBuff.append("               t.ITEM_NO,");
                strBuff.append("               t.ITEM_CLASS,");
                strBuff.append("               t.CLASS_ON_RCPT,");
                strBuff.append("               t.ITEM_CODE,");
                strBuff.append("               t.ITEM_NAME,");
                strBuff.append("               t.ITEM_SPEC,");
                strBuff.append("               t.AMOUNT,");
                strBuff.append("               t.UNITS,");
                strBuff.append("               t.PERFORMED_BY_code,");
                strBuff.append("               t.COSTS,");
                strBuff.append("               t.CHARGES,");
                strBuff.append("               t.patient_id,");
                strBuff.append("               t.ordered_by_dept_code,");
                strBuff.append("               t.ordered_by_doctor");
                strBuff.append("          from outp_orders_costs t");
                strBuff.append("  where t.visit_date >= to_date('" + ADate + "','yyyy-mm-dd') and t.visit_date < to_date('" + DateUtils.getDateAdded(1,ADate) + "','yyyy-mm-dd')");
                strBuff.append("  and t.item_class in ('" + Config.getParamValue("Drug_In_Order") + "','" + Config.getParamValue("Drug_In_Order_Chinese") + "')");
                strBuff.append(") m ");
                strBuff.append(" group by m.item_name,");
                strBuff.append("          m.item_code,");
                strBuff.append("          m.item_spec,");
                strBuff.append("          m.units,");
                strBuff.append("          m.ordered_by_dept_code,");
                strBuff.append("          m.performed_by_code,");
                strBuff.append("          m.ordered_by_doctor");
                List<TCommonRecord> listDDDC = query.query(strBuff.toString(), cmr);
                query.update("delete DR_DRUGDOCTORDEPTCOST where dr_date = to_date('" + ADate + "','yyyy-mm-dd')");
                System.out.println(" DR_DRUGDOCTORDEPTCOST-总条数:" + listDDDC.size());
                int i = 0;
                for(TCommonRecord t : listDDDC)
                {
                    StringUtils.execCF(supplier, t);
                    t.set("is_anti","0" );
                    double dddValue = 0d;
                    dddValue = AntiDDDSum(t, dddValue);
                    TCommonRecord tCom = dc.getDrugDictInfo(t.get("item_code"));
                    String sql = "insert into DR_DRUGDOCTORDEPTCOST(" 
                            + " dr_date"        
                            + ",drug_code"      
                            + ",drug_name "     
                            + ",doctor_code"    
                            + ",doctor_name "   
                            + ",amount"         
                            + ",cost"           
                            + ",package_spec"   
                            + ",dept_code"      
                            + ",dept_name"      
                            + ",toxi_property"  
                            + ",firm_id"        
                            + ",is_anti"        
                            + ",dispensary"     
                            + ",dispensary_name"
                            + ",units"
                            + ",ddd_value"
                            + ",is_base,is_exhilarant,is_injection,is_oral,anti_level,is_impregnant,IS_NOCHINESEDRUG,is_external,is_chinesedrug,IS_ALLERGY, is_patentdrug,is_tumor,is_poison,is_psychotic,is_habitforming,is_radiation,is_precious,is_danger,is_assist,is_albumin ,id) " + 
                            " values(" +
                            "to_date('" + ADate  + "','yyyy-mm-dd')" +
                            ",'" + t.get("item_code",true)        + "'" +
                            ",'" + t.get("item_name",true)        + "'" +
                            ",'" + t.get("doctor_code",true)      + "'" +
                            ",'" + (!"".equals(t.get("ordered_by_doctor"))?t.get("ordered_by_doctor",true):"无") + "'" +
                            ",'" + t.get("amount",true)           + "'" +
                            ",'" + t.get("costs",true)            + "'" +
                            ",'" + t.get("item_spec",true)     + "'" +
                            ",'" + t.get("ordered_by_dept",true)  + "'" +
                            ",'" + dc.getDeptName(t.get("ordered_by_dept")) + "'" +
                            ",'" + tCom.get("toxi_property",true) + "'" +
                            ",'" + t.get("firm_id",true)          + "'" +
                            ",'" + t.get("is_anti",true)     + "'" +
                            ",'" + t.get("performed_by",true)       + "'" +
                            ",'" + dc.getDeptName(t.get("performed_by"))  + "'" +
                            ",'" + t.get("units",true)            + "'" +
                            ",'" + dddValue + "'" 
                            + ",'" + getflag(DrugUtils.isCountryBase(t.get("item_code")))  + "'" // is_base '基药剂标识，0是非基药，1是基药';
                            + ",'" + getflag(DrugUtils.isExhilarantDrug(t.get("item_code")))  + "'" // is_exhilarant '兴奋剂标识，0是非兴奋剂，1是兴奋剂';
                            + ",'" + getflag(DrugUtils.isZSDrug(t.get("item_code"))) + "'"// is_injection '注射剂标识，0是非注射剂，1是注射剂';
                            + ",'" + getflag(DrugUtils.IsOralDrug(t.get("item_code"))) + "'"// is_oral '口服制剂标识，0是非口服，1是口服';
                            + ",'" + DrugUtils.getDrugAntiByLevel(t.get("item_code")) + "'" // anti_level'抗菌药级别，1是非限制用药，2是限制用药，3是特殊用药';
                            + ",'" + getflag(DrugUtils.isImpregnant(t.get("item_code")))  + "'"// is_impregnant '溶剂标识，0是非溶剂，1是溶剂';
                            + ",'" + getflag(!DrugUtils.isChineseDrug(t.get("item_code")))  + "'"// IS_NOCHINESEDRUG '药理分类， 0 饮片 1 非饮片 ';
    //                            + ",'" + getflag(DrugUtils.isExhilarantDrug(t.get("item_code")))  + "'"// drug_catalog '药品的分类标识，例如青霉素类、头孢类。。。。';
                            + ",'" + getflag(DrugUtils.isExternalDrug(t.get("item_code")))  + "'"// is_external '外用标识，0是非外用，1是外用';
                            + ",'" + getflag(DrugUtils.isChineseDrug(t.get("item_code")))  + "'"// is_chinesedrug '中药标识，0是非中药，1是中药';
                            + ",'" + getflag(DrugUtils.isAntiAllergyDrug(t.get("item_code")))  + "'"// is_allergy '抗过敏标识，0是非抗过敏药物，1是抗过敏药物';
    //                            + ",'" + getflag(DrugUtils.isExhilarantDrug(t.get("item_code")))  + "'"// is_medcare_country '国家医保标识，0是非医保，1是甲类医保，2是乙类医保';
    //                            + ",'" + getflag(DrugUtils.isExhilarantDrug(t.get("item_code")))  + "'"// is_medcare_local '地方医保标识，0是非医保，1是甲类医保，2是乙类医保';
                            + ",'" + getflag(DrugUtils.isPatentDrug(t.get("item_code")))  + "'"// is_patentdrug  '中成药标识，0是非中成药，1是中成药';
                            + ",'" + getflag(DrugUtils.isTumor(t.get("item_code")))  + "'"// is_tumor  '抗肿瘤标识，0是非抗肿瘤药，1是抗肿瘤药';
                            + ",'" + getflag(DrugUtils.isDDrug(t.get("item_code")))  + "'"// is_poison '毒药标识，0是非毒药，1是毒药';
                            + ",'" + getflag(DrugUtils.isJSDrug(t.get("item_code")))  + "'"// is_psychotic'精神药标识，0是非精神药，1是精神药,';
                            + ",'" + getflag(DrugUtils.isMDrug(t.get("item_code")))  + "'"// is_habitforming '麻药标识，0是非麻药，1是麻药';
                            + ",'" + getflag(DrugUtils.isFSDrug(t.get("item_code")))  + "'"// is_radiation '放射标识，0是非放射药，1是放射药';
                            + ",'" + getflag(DrugUtils.isGZDrug(t.get("item_code")))  + "'"// is_precious '贵重药标识，0是非贵重药，1是贵重药';
                            + ",'" + getflag(DrugUtils.isDanger(t.get("item_code")))  + "'"// is_danger '危险级别： 0-不是 1-是危险药品;
                            + ",'" + getflag(DrugUtils.IsAssist(t.get("item_code")))  + "'"// is_assist '辅助用药 0 否 1 是';
                            + ",'" + getflag(DrugUtils.isAlbumin(t.get("item_code")))  + "'"// is_albumin '白蛋白 0 否 1 是';
                            + ",'" + UuidUtil.get32UUID()  + "'"//  表主键id
                            + ")";
                    query.update(sql);
                    logger.debug("=====添加数量为" + listDDDC.size() + "/" + (++i) + "====");
                }
                
                cmr       = null;
                query     = null;
                listDDDC  = null;
            }
        });
    }
    
    /**
     * 按天病人单药品费
     * @param ADate
     */
    private void DrugPatDDCost(final String ADate)
    {
        TransactionTemp tt = new TransactionTemp("ph");
        tt.execute(new TransaCallback(null)
        {
            @SuppressWarnings ("unchecked")
            @Override
            public void ExceuteSqlRecord()
            {
                DictCache           dc = DictCache.getNewInstance();
                CommonMapper       cmr = new CommonMapper();
//                JDBCQueryImpl    his = DBQueryFactory.getQuery("HIS");
                JDBCQueryImpl query = DBQueryFactory.getQuery("ph");
                /*
                ICaseHistoryHelper ichr = CaseHistoryFactory.getCaseHistoryHelper();
                String strFields = "t.VISIT_DATE,t.VISIT_NO,t.RCPT_NO,t.ITEM_NO,t.ITEM_CLASS,t.CLASS_ON_RCPT,t.ITEM_CODE,t.ITEM_NAME,t.ITEM_SPEC,t.AMOUNT,t.UNITS,t.PERFORMED_BY,t.COSTS,t.CHARGES"
                        + ",b.patient_id,b.ordered_by_dept,b.ordered_by_doctor"
                        + ",r.name,r.charge_type,r.sex,r.date_of_birth";
                String strTables = " outpbill.outp_bill_items t,outpbill.outp_order_desc b,medrec.pat_master_index r";
                List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
                List<TCommonRecord> lsGroups = new ArrayList<TCommonRecord>();
                TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("t.item_class", Config.getParamValue("Drug_In_Order"), "Char", "", "1", "");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR("t.item_class", Config.getParamValue("Drug_In_Order_Chinese"), "Char", "", "1", "or");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR(
                        "t.visit_date"
                        , CaseHistoryFunction.genRToDate("outpbill.outp_bill_items", "t.visit_date", "'" + ADate + "'", "yyyy-mm-dd")
                        , "", ">=", "", "");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR(
                        "t.visit_date"
                        ,CaseHistoryFunction.genRToDate("outpbill.outp_bill_items", "t.visit_date", "'" + DateUtils.getDateAdded(1,ADate) + "'", "yyyy-mm-dd")
                        , "", "<", "", "");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR("t.visit_date", "b.visit_date", "", "=", "", "");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR("t.visit_no", "b.visit_no", "", "=", "", "");
                lsWheres.add(where);
                where = CaseHistoryHelperUtils.genWhereCR("b.patient_id", "r.patient_id", "", "=", "", "");
                lsWheres.add(where);
                String gensql = "[" + ichr.genSQL(strFields, strTables, lsWheres, lsGroups, null) + "] m";
                strFields = "m.item_name,m.item_code,m.item_spec,m.units,m.ordered_by_dept,m.performed_by,sum(m.COSTS) costs,sum(m.amount) amount"
                        + ",m.ordered_by_doctor,m.patient_id,m.name,m.charge_type,m.sex,m.date_of_birth" ;
                TCommonRecord group = CaseHistoryHelperUtils.genGroupCR("m.item_name");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR(" m.item_code ");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.item_spec");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.units");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.ordered_by_dept");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.performed_by");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.ordered_by_doctor");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.patient_id");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.name");
                lsGroups.add(group);
//                group = CaseHistoryHelperUtils.genGroupCR("m.identity");
//                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.charge_type");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.sex");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.date_of_birth");
                lsGroups.add(group);
                gensql = ichr.genSQL(strFields, gensql, null, lsGroups, null);
                List<TCommonRecord> listDDDC = his.query(gensql, cmr);*/
                StringBuffer strBuff = new StringBuffer();
                strBuff.append("select m.item_name,");
                strBuff.append("       m.item_code,");
                strBuff.append("       m.item_spec,");
                strBuff.append("       m.units,");
                strBuff.append("       m.ordered_by_dept_code ordered_by_dept,");
                strBuff.append("       m.performed_by_code performed_by,");
                strBuff.append("       sum(m.COSTS) costs,");
                strBuff.append("       sum(m.amount) amount,");
                strBuff.append("       m.ordered_by_doctor,");
                strBuff.append("       m.patient_id,");
                strBuff.append("       r.name,");
                strBuff.append("       r.charge_type,");
                strBuff.append("       r.sex,");
                strBuff.append("       r.date_of_birth");
                strBuff.append("  from (select t.VISIT_DATE,");
                strBuff.append("               t.VISIT_NO,");
                strBuff.append("               t.RCPT_NO,");
                strBuff.append("               t.ITEM_NO,");
                strBuff.append("               t.ITEM_CLASS,");
                strBuff.append("               t.CLASS_ON_RCPT,");
                strBuff.append("               t.ITEM_CODE,");
                strBuff.append("               t.ITEM_NAME,");
                strBuff.append("               t.ITEM_SPEC,");
                strBuff.append("               t.AMOUNT,");
                strBuff.append("               t.UNITS,");
                strBuff.append("               t.PERFORMED_BY_code,");
                strBuff.append("               t.COSTS,");
                strBuff.append("               t.CHARGES,");
                strBuff.append("               t.patient_id,");
                strBuff.append("               t.ordered_by_dept_code,");
                strBuff.append("               t.ordered_by_doctor");
                strBuff.append("          from patienthistory.outp_orders_costs t");
                strBuff.append("         where t.visit_date >= to_date('" + ADate + "', 'yyyy-mm-dd')");
                strBuff.append("           and t.visit_date < to_date('" +  DateUtils.getDateAdded(1,ADate)  + "', 'yyyy-mm-dd')");
                strBuff.append("           and t.item_class in ('" + Config.getParamValue("Drug_In_Order") + "','" + Config.getParamValue("Drug_In_Order_Chinese") + "')");
                strBuff.append("       ) m,");
                strBuff.append("       patienthistory.pat_master_index r");
                strBuff.append(" where m.patient_id = r.patient_id");
                strBuff.append(" group by m.item_name,");
                strBuff.append("          m.item_code,");
                strBuff.append("          m.item_spec,");
                strBuff.append("          m.units,");
                strBuff.append("          m.ordered_by_dept_code,");
                strBuff.append("          m.performed_by_code,");
                strBuff.append("          m.ordered_by_doctor,");
                strBuff.append("          m.patient_id,");
                strBuff.append("          r.name,");
                strBuff.append("          r.charge_type,");
                strBuff.append("          r.sex,");
                strBuff.append("          r.date_of_birth");
                List<TCommonRecord> listDDDC = query.query(strBuff.toString(), cmr);
                query.update("delete DR_DRUGPATDDCOST where  dr_date = to_date('" + ADate + "','yyyy-mm-dd')");
                logger.debug(" DR_DRUGPATDDCOST-总条数:" + listDDDC.size());
                int i = 0;
                for(TCommonRecord t : listDDDC)
                {
                    StringUtils.execCF(supplier, t);
                    t.set("is_anti","0" );
                    double dddValue = 0d;
                    dddValue = AntiDDDSum(t, dddValue);
                    TCommonRecord tCom = dc.getDrugDictInfo(t.get("item_code"));
                    String sql = "insert into DR_DRUGPATDDCOST(" 
                            + "dr_date"     
                            + ",drug_code"      
                            + ",drug_name"      
                            + ",patient_id"     
                            + ",pat_name"       
                            + ",pat_sex"        
                            + ",pat_age"        
                            + ",identity"       
                            + ",doctor_code"    
                            + ",doctor_name"    
                            + ",amount"         
                            + ",cost"           
                            + ",package_spec"   
                            + ",units"          
                            + ",dept_code"      
                            + ",dept_name"      
                            + ",firm_id"        
                            + ",charge_type"    
                            + ",is_anti"        
                            + ",toxi_property"  
                            + ",dispensary"     
                            + ",dispensary_name"
                            + ",ddd_value"
                            + ",is_base,is_exhilarant,is_injection,is_oral,anti_level,is_impregnant,IS_NOCHINESEDRUG,is_external,is_chinesedrug,IS_ALLERGY, is_patentdrug,is_tumor,is_poison,is_psychotic,is_habitforming,is_radiation,is_precious,is_danger,is_assist,is_albumin,id ) values(" 
                            + "to_date('"  + ADate     + "','yyyy-mm-dd')"
                            + ",'" + t.get("item_code",true)      + "'"
                            + ",'" + t.get("item_name",true)      + "'"
                            + ",'" + (!"".equals(t.get("patient_id"))?t.get("patient_id",true):"无") + "'"
                            + ",'" + t.get("name",true)   + "'"
                            + ",'" + t.get("sex",true)    + "'"
                            + ",'" + DateUtils.calcuateAgeByTwoDates(t.get("date_of_birth",true), null, "yyyy-MM-dd") + "'"
                            + ",'" + t.get("identity",true)       + "'"
                            + ",'" + t.get("doctor_code",true)    + "'"
                            + ",'" + (!"".equals(t.get("ordered_by_doctor"))?t.get("ordered_by_doctor",true):"无") + "'" 
                            + ",'" + t.get("amount",true)         + "'"
                            + ",'" + t.get("costs",true)          + "'"
                            + ",'" + t.get("item_spec",true)   + "'"
                            + ",'" + t.get("units",true)          + "'"
                            + ",'" + t.get("ordered_by_dept",true)       + "'"
                            + ",'" + dc.getDeptName(t.get("ordered_by_dept"))      + "'"
                            + ",'" + t.get("firm_id",true)        + "'"
                            + ",'" + t.get("charge_type",true)  + "'" 
                            + ",'" + t.get("is_anti",true)   + "'"
                            + ",'" + tCom.get("toxi_property",true) + "'"
                            + ",'" + t.get("performed_by",true)     + "'"
                            + ",'" + dc.getDeptName( t.get("performed_by")) + "'"
                            + ",'" + dddValue + "'"
                            + ",'" + getflag(DrugUtils.isCountryBase(t.get("item_code")))  + "'" // is_base '基药剂标识，0是非基药，1是基药';
                            +",'" + getflag(DrugUtils.isExhilarantDrug(t.get("item_code")))  + "'" // is_exhilarant '兴奋剂标识，0是非兴奋剂，1是兴奋剂';
                            + ",'" + getflag(DrugUtils.isZSDrug(t.get("item_code"))) + "'"// is_injection '注射剂标识，0是非注射剂，1是注射剂';
                            + ",'" + getflag(DrugUtils.IsOralDrug(t.get("item_code"))) + "'"// is_oral '口服制剂标识，0是非口服，1是口服';
                            + ",'" + DrugUtils.getDrugAntiByLevel(t.get("item_code")) + "'" // anti_level'抗菌药级别，1是非限制用药，2是限制用药，3是特殊用药';
                            + ",'" + getflag(DrugUtils.isImpregnant(t.get("item_code")))  + "'"// is_impregnant '溶剂标识，0是非溶剂，1是溶剂';
                            + ",'" + getflag(!DrugUtils.isChineseDrug(t.get("item_code")))  + "'"// IS_NOCHINESEDRUG '药理分类， 0 饮片 1 非饮片 ';
//                            + ",'" + getflag(DrugUtils.isExhilarantDrug(t.get("item_code")))  + "'"// drug_catalog '药品的分类标识，例如青霉素类、头孢类。。。。';
                            + ",'" + getflag(DrugUtils.isExternalDrug(t.get("item_code")))  + "'"// is_external '外用标识，0是非外用，1是外用';
                            + ",'" + getflag(DrugUtils.isChineseDrug(t.get("item_code")))  + "'"// is_chinesedrug '中药标识，0是非中药，1是中药';
                            + ",'" + getflag(DrugUtils.isAntiAllergyDrug(t.get("item_code")))  + "'"// is_allergy '抗过敏标识，0是非抗过敏药物，1是抗过敏药物';
//                            + ",'" + getflag(DrugUtils.isExhilarantDrug(t.get("item_code")))  + "'"// is_medcare_country '国家医保标识，0是非医保，1是甲类医保，2是乙类医保';
//                            + ",'" + getflag(DrugUtils.isExhilarantDrug(t.get("item_code")))  + "'"// is_medcare_local '地方医保标识，0是非医保，1是甲类医保，2是乙类医保';
                            + ",'" + getflag(DrugUtils.isPatentDrug(t.get("item_code")))  + "'"// is_patentdrug  '中成药标识，0是非中成药，1是中成药';
                            + ",'" + getflag(DrugUtils.isTumor(t.get("item_code")))  + "'"// is_tumor  '抗肿瘤标识，0是非抗肿瘤药，1是抗肿瘤药';
                            + ",'" + getflag(DrugUtils.isDDrug(t.get("item_code")))  + "'"// is_poison '毒药标识，0是非毒药，1是毒药';
                            + ",'" + getflag(DrugUtils.isJSDrug(t.get("item_code")))  + "'"// is_psychotic'精神药标识，0是非精神药，1是精神药,';
                            + ",'" + getflag(DrugUtils.isMDrug(t.get("item_code")))  + "'"// is_habitforming '麻药标识，0是非麻药，1是麻药';
                            + ",'" + getflag(DrugUtils.isFSDrug(t.get("item_code")))  + "'"// is_radiation '放射标识，0是非放射药，1是放射药';
                            + ",'" + getflag(DrugUtils.isGZDrug(t.get("item_code")))  + "'"// is_precious '贵重药标识，0是非贵重药，1是贵重药';
                            + ",'" + getflag(DrugUtils.isDanger(t.get("item_code")))  + "'"// is_danger '危险级别： 0-不是 1-是危险药品;
                            + ",'" + getflag(DrugUtils.IsAssist(t.get("item_code")))  + "'"// is_assist '辅助用药 0 否 1 是';
                            + ",'" + getflag(DrugUtils.isAlbumin(t.get("item_code")))  + "'"// is_albumin '白蛋白 0 否 1 是';
                            + ",'" + UuidUtil.get32UUID()  + "'"//  表主键id
                            + ")";
                    query.update(sql);
                    logger.info("=====添加数量为" + listDDDC.size() + "/" + (++i) + "====");
                }
                cmr       = null;
                query     = null;
                listDDDC  = null;
            }

            
        });
    }
    
    private double AntiDDDSum(TCommonRecord t, double dddValue)
    {
        if(DrugUtils.isKJDrug(t.get("item_code")))
        {
            dddValue = DDDUtils.CalcDDD(
                    t.get("item_code"),
                    t.get("item_spec"), t.get("units"),
                    t.get("Firm_ID"), t.get("amount"),
                    t.get("COSTS"));
            t.set("is_anti", "1");
            logger.info("存在抗菌药" + t.get("item_code") + "●●●"
                    + t.get("item_spec") + "●●●" + dddValue);
        }
        return dddValue;
    }
    
    /**
     *  选择 抓取门诊处方 出去药 药房  
     * @return
     
    private String getFetchDeptCode()
    {
        String[] depts = Config.getParamValue("FetchDataOut").split(";");
        StringBuffer sbfr = new StringBuffer();
        for(String s : depts)
        {
            sbfr.append("'").append(s).append("',");
        }
        if(sbfr.length() > 0 )
            sbfr.deleteCharAt(sbfr.length() - 1 );
        return sbfr.toString();
    }
    */
    
    @Override
    public String BuildReportWithCR(String ADate, TCommonRecord crPatInfo, Task AOwner)
    {
        return null;
    }

    @Override
    public void buildBegin(String ADate, Task AOwner)
    {
    }

    @Override
    public void buildOver(String ADate, Task AOwner)
    {
    }

}
