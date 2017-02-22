package com.hitzd.his.ReportBuilder.PEAAS.Day;

import java.util.ArrayList;
import java.util.List;

import com.hitzd.DBUtils.CommonMapper;
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
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;
import com.hitzd.his.task.Task;
import com.hitzd.persistent.Persistent4DB;

/**
 * 药品统计支持部队医院 军委一号的情况 
 * @author jingcong
 *
 */
public class DR_OutDrugCostInfo_BillItems extends Persistent4DB implements IReportBuilder
{
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
        
        TransactionTemp tt = new TransactionTemp("PEAAS"); 
        tt.execute(new TransaCallback(null) 
        {
            @SuppressWarnings ("unchecked")
            @Override
            public void ExceuteSqlRecord()
            {
                DictCache dc = DictCache.getNewInstance();
                CommonMapper cmr = new CommonMapper();
                JDBCQueryImpl his = DBQueryFactory.getQuery("HIS");
                JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
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
                List<TCommonRecord> listDDC = his.query(gensql, cmr);
                query.update("delete dr_drugDayCost where dr_date = to_date('" + ADate + "','yyyy-mm-dd')");
                System.out.println(" dr_drugDayCost-总条数:" + listDDC.size());
                int i = 0;
                for(TCommonRecord t : listDDC)
                {
                    StringUtils.execCF(supplier, t);
                    t.set("is_anti","0" );
                    if(DrugUtils.isKJDrug(t.get("item_code")))
                        t.set("is_anti", "1");
                    TCommonRecord tCom = dc.getDrugDictInfo(t.get("item_code"));
                    String sql = "insert into dr_drugDayCost(dr_date,drug_code, drug_name, cost, package_spec, amount, firm_id, dept_code, dept_name, dispensary, toxi_property, is_anti, dispensary_name, units) values ( "
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
                        + " )";
                    query.update(sql);
                    System.out.println("=====添加数量为" + listDDC.size() + "/" + (++i) + "====");
                }
                cmr       = null;
                query     = null;
                listDDC   = null;
            }
        });
    }
    
    /**
     * 按天医生单药品费用统计
     * @param ADate
     */
    private void DrugDoctorDeptCost(final String ADate)
    {
        TransactionTemp tt = new TransactionTemp("PEAAS");
        tt.execute(new TransaCallback(null) 
        {
            @SuppressWarnings ("unchecked")
            @Override
            public void ExceuteSqlRecord()
            {
                DictCache           dc = DictCache.getNewInstance();
                CommonMapper       cmr = new CommonMapper();
                JDBCQueryImpl    his = DBQueryFactory.getQuery("HIS");
                JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
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
                List<TCommonRecord> listDDDC = his.query(gensql, cmr);
                query.update("delete DR_DRUGDOCTORDEPTCOST where dr_date = to_date('" + ADate + "','yyyy-mm-dd')");
                System.out.println(" DR_DRUGDOCTORDEPTCOST-总条数:" + listDDDC.size());
                int i = 0;
                for(TCommonRecord t : listDDDC)
                {
                    StringUtils.execCF(supplier, t);
                    t.set("is_anti","0" );
                    if(DrugUtils.isKJDrug(t.get("item_code")))
                        t.set("is_anti", "1");
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
                            + ",units ) " + 
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
                            ",'" + dc.getDeptName(t.get("ordered_by_dept"))         + "'" +
                            ",'" + tCom.get("toxi_property",true) + "'" +
                            ",'" + t.get("firm_id",true)          + "'" +
                            ",'" + t.get("is_anti",true)     + "'" +
                            ",'" + t.get("performed_by",true)       + "'" +
                            ",'" + dc.getDeptName(t.get("performed_by"))  + "'" +
                            ",'" + t.get("units",true)            + "'" +
                            ")";
                    query.update(sql);
                    System.out.println("=====添加数量为" + listDDDC.size() + "/" + (++i) + "====");
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
        TransactionTemp tt = new TransactionTemp("PEAAS");
        tt.execute(new TransaCallback(null)
        {
            @SuppressWarnings ("unchecked")
            @Override
            public void ExceuteSqlRecord()
            {
                DictCache           dc = DictCache.getNewInstance();
                CommonMapper       cmr = new CommonMapper();
                JDBCQueryImpl    his = DBQueryFactory.getQuery("HIS");
                JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
                ICaseHistoryHelper ichr = CaseHistoryFactory.getCaseHistoryHelper();
                String strFields = "t.VISIT_DATE,t.VISIT_NO,t.RCPT_NO,t.ITEM_NO,t.ITEM_CLASS,t.CLASS_ON_RCPT,t.ITEM_CODE,t.ITEM_NAME,t.ITEM_SPEC,t.AMOUNT,t.UNITS,t.PERFORMED_BY,t.COSTS,t.CHARGES"
                        + ",b.patient_id,b.ordered_by_dept,b.ordered_by_doctor"
                        + ",r.identity,r.name,r.charge_type,r.sex,r.date_of_birth";
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
                        + ",m.ordered_by_doctor,m.patient_id,m.name,m.identity,m.charge_type,m.sex,m.date_of_birth" ;
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
                group = CaseHistoryHelperUtils.genGroupCR("m.identity");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.charge_type");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.sex");
                lsGroups.add(group);
                group = CaseHistoryHelperUtils.genGroupCR("m.date_of_birth");
                lsGroups.add(group);
                gensql = ichr.genSQL(strFields, gensql, null, lsGroups, null);
                List<TCommonRecord> listDDDC = his.query(gensql, cmr);
                query.update("delete DR_DRUGPATDDCOST where  dr_date = to_date('" + ADate + "','yyyy-mm-dd')");
                System.out.println(" DR_DRUGPATDDCOST-总条数:" + listDDDC.size());
                int i = 0;
                for(TCommonRecord t : listDDDC)
                {
                    StringUtils.execCF(supplier, t);
                    t.set("is_anti","0" );
                    if(DrugUtils.isKJDrug(t.get("item_code")))
                        t.set("is_anti", "1");
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
                            +
                            ") values(" 
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
                            + ")";
                    query.update(sql);
                    System.out.println("=====添加数量为" + listDDDC.size() + "/" + (++i) + "====");
                }
                cmr       = null;
                query     = null;
                listDDDC  = null;
            }
        });
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void buildBegin(String ADate, Task AOwner)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void buildOver(String ADate, Task AOwner)
    {
        // TODO Auto-generated method stub

    }

}
