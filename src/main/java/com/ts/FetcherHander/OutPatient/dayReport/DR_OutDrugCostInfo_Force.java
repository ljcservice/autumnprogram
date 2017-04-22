package com.ts.FetcherHander.OutPatient.dayReport;

import java.util.List;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.Transaction.*;
import com.hitzd.his.ReportBuilder.Interfaces.IReportBuilder;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.his.task.Task;
import com.hitzd.persistent.Persistent4DB;

/**
 * 药品统计支持部队医院 军委一号的情况  (药房表)
 * @author jingcong
 *
 */
public class DR_OutDrugCostInfo_Force extends Persistent4DB implements IReportBuilder
{
    @Override
    public String getLogFileName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String BuildReport(String ADate, Task AOwner)
    {
        try
        {
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
                StringBuffer sqlBuffer = new StringBuffer();
                sqlBuffer.append(" SELECT d.drug_name,d.drug_code,d.drug_spec,d.package_units units,d.firm_id,sum(t.costs)costs,sum(d.quantity)amount,t.ordered_by,t.dispensary");
                sqlBuffer.append(" FROM PHARMACY.DRUG_PRESC_MASTER T ,PHARMACY.DRUG_PRESC_DETAIL D");
                sqlBuffer.append(" WHERE T.PRESC_NO=D.PRESC_NO AND T.PRESC_DATE=D.PRESC_DATE"); 
                sqlBuffer.append(" AND T.PRESC_DATE>=TO_DATE('"+ADate+"','YYYY-MM-DD') ");
                sqlBuffer.append(" AND T.PRESC_DATE<TO_DATE('" + DateUtils.getDateAdded(1, ADate)+"','YYYY-MM-DD') and t.presc_source=0 ");
                sqlBuffer.append(" group by d.drug_name,d.drug_code,d.drug_spec,d.package_units,d.firm_id,t.ordered_by,t.dispensary");
                
                List<TCommonRecord> listDDC = his.query(sqlBuffer.toString(), cmr);
                query.update("delete dr_drugDayCost where dr_date = to_date('" + ADate + "','yyyy-mm-dd')");
                for(TCommonRecord t : listDDC)
                {
                    t.set("is_anti","0" );
                    if(DrugUtils.isKJDrug(t.get("drug_code")))
                        t.set("is_anti", "1");
                    TCommonRecord tCom = dc.getDrugDictInfo(t.get("Drug_code"));
                    String sql = "insert into dr_drugDayCost(dr_date,drug_code, drug_name, cost, package_spec, amount, firm_id, dept_code, dept_name, dispensary, toxi_property, is_anti, dispensary_name, units) values ( "
                        + "to_date('"  + ADate + "','yyyy-mm-dd')"
                        + ",'" + t.get("drug_code",true) + "'"
                        + ",'" + t.get("drug_name",true) + "'"
                        + ",'" + t.get("costs",true) + "'"
                        + ",'" + t.get("drug_spec",true) + "'"
                        + ",'" + t.get("amount",true) + "'"
                        + ",'" + t.get("firm_id",true) + "'"
                        + ",'" + t.get("ordered_by",true) + "'"
                        + ",'" + dc.getDeptName(t.get("ordered_by")) + "'"
                        + ",'" + t.get("dispensary",true) + "'"
                        + ",'" + tCom.get("toxi_property",true) + "'"
                        + ",'" + t.get("is_anti",true) + "'"
                        + ",'" + dc.getDeptName(t.get("dispensary")) + "'"
                        + ",'" + t.get("units",true) + "'"
                        + " )";
                    query.update(sql);
                }
                sqlBuffer = null;
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
                StringBuffer sqlBuffer = new StringBuffer();
                sqlBuffer.append(" SELECT d.drug_name,d.drug_code,d.drug_spec,d.package_units units,d.firm_id,sum(t.costs)costs,sum(d.quantity)amount,t.ordered_by,t.dispensary,t.prescribed_by");
                sqlBuffer.append(" FROM PHARMACY.DRUG_PRESC_MASTER T ,PHARMACY.DRUG_PRESC_DETAIL D");
                sqlBuffer.append(" WHERE T.PRESC_NO=D.PRESC_NO AND T.PRESC_DATE=D.PRESC_DATE"); 
                sqlBuffer.append(" AND T.PRESC_DATE>=TO_DATE('"+ADate+"','YYYY-MM-DD') ");
                sqlBuffer.append(" AND T.PRESC_DATE<TO_DATE('"+DateUtils.getDateAdded(1, ADate)+"','YYYY-MM-DD') and t.presc_source=0 ");
                sqlBuffer.append(" group by d.drug_name,d.drug_code,d.drug_spec,d.package_units,d.firm_id,t.ordered_by,t.dispensary,t.prescribed_by");
                List<TCommonRecord> listDDDC = his.query(sqlBuffer.toString(), cmr);
                query.update("delete DR_DRUGDOCTORDEPTCOST where dr_date = to_date('" + ADate + "','yyyy-mm-dd')");
                for(TCommonRecord t : listDDDC)
                {
                    t.set("is_anti","0" );
                    if(DrugUtils.isKJDrug(t.get("drug_code")))
                        t.set("is_anti", "1");
                    TCommonRecord tCom = dc.getDrugDictInfo(t.get("drug_code"));
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
                            ",'" + t.get("drug_code",true)        + "'" +
                            ",'" + t.get("drug_name",true)        + "'" +
                            ",'" + t.get("doctor_code",true)      + "'" +
                            ",'" + (!"".equals(t.get("prescribed_by"))?t.get("prescribed_by",true):"无") + "'" +
                            ",'" + t.get("amount",true)           + "'" +
                            ",'" + t.get("costs",true)            + "'" +
                            ",'" + t.get("drug_spec",true)     + "'" +
                            ",'" + t.get("ordered_by",true)  + "'" +
                            ",'" + dc.getDeptName(t.get("ordered_by"))         + "'" +
                            ",'" + tCom.get("toxi_property",true) + "'" +
                            ",'" + t.get("firm_id",true)          + "'" +
                            ",'" + t.get("is_anti",true)     + "'" +
                            ",'" + t.get("dispensary",true)       + "'" +
                            ",'" + dc.getDeptName(t.get("dispensary"))  + "'" +
                            ",'" + t.get("units",true)            + "'" +
                            ")";
                    query.update(sql);
                }
                cmr       = null;
                query     = null;
                sqlBuffer = null;
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
                StringBuffer sqlBuffer = new StringBuffer();
                sqlBuffer.append(" SELECT t.patient_id,name,identity,charge_type,d.drug_name,d.drug_code,d.drug_spec,d.package_units units,d.firm_id,sum(t.costs)costs,sum(d.quantity)amount,t.ordered_by,t.dispensary,t.prescribed_by");
                sqlBuffer.append(" FROM PHARMACY.DRUG_PRESC_MASTER T ,PHARMACY.DRUG_PRESC_DETAIL D");
                sqlBuffer.append(" WHERE T.PRESC_NO=D.PRESC_NO AND T.PRESC_DATE=D.PRESC_DATE"); 
                sqlBuffer.append(" AND T.PRESC_DATE>=TO_DATE('"+ADate+"','YYYY-MM-DD') ");
                sqlBuffer.append(" AND T.PRESC_DATE<TO_DATE('"+DateUtils.getDateAdded(1, ADate)+"','YYYY-MM-DD') and t.presc_source=0 ");
                sqlBuffer.append(" group by d.drug_name,d.drug_code,d.drug_spec,d.package_units,d.firm_id,t.ordered_by,t.dispensary,t.prescribed_by,t.patient_id,name,identity,charge_type");
                
                List<TCommonRecord> listDDDC = his.query(sqlBuffer.toString(), cmr);
                query.update("delete DR_DRUGPATDDCOST where  dr_date = to_date('" + ADate + "','yyyy-mm-dd')");
                for(TCommonRecord t : listDDDC)
                {
                    t.set("is_anti","0" );
                    if(DrugUtils.isKJDrug(t.get("drug_code")))
                        t.set("is_anti", "1");
                    TCommonRecord tCom = dc.getDrugDictInfo(t.get("drug_code"));
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
                            + ",'" + t.get("drug_code",true)      + "'"
                            + ",'" + t.get("drug_name",true)      + "'"
                            + ",'" + (!"".equals(t.get("patient_id"))?t.get("patient_id",true):"无") + "'"
                            + ",'" + t.get("name",true)   + "'"
                            + ",'" + t.get("sex",true)    + "'"
                            + ",'" + t.get("age",true)    + "'"
                            + ",'" + t.get("identity",true)       + "'"
                            + ",'" + t.get("doctor_code",true)    + "'"
                            + ",'" + (!"".equals(t.get("prescribed_by"))?t.get("prescribed_by",true):"无") + "'" 
                            + ",'" + t.get("amount",true)         + "'"
                            + ",'" + t.get("costs",true)          + "'"
                            + ",'" + t.get("drug_spec",true)   + "'"
                            + ",'" + t.get("units",true)          + "'"
                            + ",'" + t.get("ordered_by",true)       + "'"
                            + ",'" + dc.getDeptName(t.get("ordered_by"))      + "'"
                            + ",'" + t.get("firm_id",true)        + "'"
                            + ",'" + t.get("charge_type",true)  + "'" 
                            + ",'" + t.get("is_anti",true)   + "'"
                            + ",'" + tCom.get("toxi_property",true) + "'"
                            + ",'" + t.get("dispensary",true)     + "'"
                            + ",'" + dc.getDeptName( t.get("dispensary")) + "'"
                            + ")";
                    query.update(sql);
                }
                cmr       = null;
                query     = null;
                sqlBuffer = null;
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
