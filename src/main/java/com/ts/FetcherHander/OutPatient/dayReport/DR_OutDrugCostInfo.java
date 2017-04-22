package com.ts.FetcherHander.OutPatient.dayReport;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.Transaction.TransaCallback;
import com.hitzd.Transaction.TransactionTemp;
import com.hitzd.his.ReportBuilder.Interfaces.IReportBuilder;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.his.task.Task;
import com.hitzd.persistent.Persistent4DB;

/**
 *  门诊药品使用 费用情况 日报 
 * @author Administrator
 *
 */

public class DR_OutDrugCostInfo extends Persistent4DB implements IReportBuilder
{
    @Override
    public String getLogFileName()
    {
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
                JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
                StringBuffer sqlBuffer = new StringBuffer();
                sqlBuffer.append("select t.order_date,");
                sqlBuffer.append("          p.drug_code,");
                sqlBuffer.append("          p.drug_name,");
                sqlBuffer.append("          p.package_spec,");
                sqlBuffer.append("          p.units,");
                sqlBuffer.append("          p.firm_id,");
                sqlBuffer.append("          p.antidrugflag,");
                sqlBuffer.append("          t.dispensary,");
                sqlBuffer.append("          t.dispensaryname,");
                sqlBuffer.append("          t.org_code,");
                sqlBuffer.append("          t.org_name,");
                sqlBuffer.append("       sum(p.amount) amouts,");
                sqlBuffer.append("       sum(p.costs) costs");
                sqlBuffer.append("  from presc t, presc_detail p");
                sqlBuffer.append(" where t.id = p.presc_id");
                sqlBuffer.append("   and  t.order_type = '1' and  t.order_date = '").append(ADate).append("'");
                sqlBuffer.append(" group by t.order_date,");
                sqlBuffer.append("          p.drug_code,");
                sqlBuffer.append("          p.drug_name,");
                sqlBuffer.append("          p.package_spec,");
                sqlBuffer.append("          p.units,");
                sqlBuffer.append("          p.firm_id,");
                sqlBuffer.append("          p.antidrugflag,");
                sqlBuffer.append("          t.dispensary,");
                sqlBuffer.append("          t.dispensaryname,");
                sqlBuffer.append("          t.org_code,");
                sqlBuffer.append("          t.org_name");
                sqlBuffer.append("");
                List<TCommonRecord> listDDC = query.query(sqlBuffer.toString(), cmr);
                query.update("delete dr_drugDayCost where dr_date = to_date('" + ADate + "','yyyy-mm-dd')");
                for(TCommonRecord t : listDDC)
                {
                    TCommonRecord tCom = dc.getDrugDictInfo(t.get("Drug_code"));
            		List<Object> sqlParams = new ArrayList<Object>();
                    String sql = "insert into dr_drugDayCost(dr_date,drug_code, drug_name, cost, package_spec, amount, firm_id, dept_code, dept_name, dispensary, toxi_property, is_anti, dispensary_name, units) "
                    		   + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(t.get("order_date")).getTime());
					sqlParams.add(dateTime);
                    sqlParams.add(t.get("drug_code"));
                    sqlParams.add(t.get("drug_name"));
                    sqlParams.add(t.get("costs"));
                    sqlParams.add(t.get("package_spec"));
                    sqlParams.add(t.get("amouts"));
                    sqlParams.add(t.get("firm_id"));
                    sqlParams.add(t.get("org_code"));
                    sqlParams.add(t.get("org_name"));
                    sqlParams.add(t.get("dispensary"));
                    sqlParams.add(tCom.get("toxi_property"));
                    sqlParams.add(t.get("antidrugflag"));
                    sqlParams.add(t.get("dispensaryname"));
                    sqlParams.add(t.get("units"));
                    
                    query.update(sql,sqlParams.toArray());
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
                JDBCQueryImpl    query = DBQueryFactory.getQuery("PEAAS");
                StringBuffer sqlBuffer = new StringBuffer();
                sqlBuffer.append("select t.order_date,");
                sqlBuffer.append("          p.drug_code,");
                sqlBuffer.append("          p.drug_name,");
                sqlBuffer.append("          p.package_spec,");
                sqlBuffer.append("          p.units,");
                sqlBuffer.append("          p.firm_id,");
                sqlBuffer.append("          t.doctor_code,");
                sqlBuffer.append("          t.doctor_name,");
                sqlBuffer.append("          p.antidrugflag,");
                sqlBuffer.append("          t.dispensary,");
                sqlBuffer.append("          t.dispensaryname,");
                sqlBuffer.append("          t.org_code,");
                sqlBuffer.append("          t.org_name,");
                sqlBuffer.append("       sum(p.amount) amouts,");
                sqlBuffer.append("       sum(p.costs) costs");
                sqlBuffer.append("  from presc t, presc_detail p");
                sqlBuffer.append(" where t.id = p.presc_id");
                sqlBuffer.append("   and t.order_type = '1' and t.order_date = '").append(ADate).append("'");
                sqlBuffer.append(" group by t.order_date,");
                sqlBuffer.append("          p.drug_code,");
                sqlBuffer.append("          p.drug_name,");
                sqlBuffer.append("          p.package_spec,");
                sqlBuffer.append("          p.units,");
                sqlBuffer.append("          p.firm_id,");
                sqlBuffer.append("          p.antidrugflag,");
                sqlBuffer.append("          t.dispensary,");
                sqlBuffer.append("          t.dispensaryname,");
                sqlBuffer.append("          t.doctor_code,");
                sqlBuffer.append("          t.doctor_name,");
                sqlBuffer.append("          t.org_code,");
                sqlBuffer.append("          t.org_name");
                List<TCommonRecord> listDDDC = query.query(sqlBuffer.toString(), cmr);
                query.update("delete DR_DRUGDOCTORDEPTCOST where dr_date = to_date('" + ADate + "','yyyy-mm-dd')");
                for(TCommonRecord t : listDDDC)
                {
                    TCommonRecord tCom = dc.getDrugDictInfo(t.get("drug_code"));
            		List<Object> sqlParams = new ArrayList<Object>();
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
                            + ",units) " + 
                            " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(t.get("order_date")).getTime());
					sqlParams.add(dateTime);
                    sqlParams.add(t.get("drug_code")        );
                    sqlParams.add(t.get("drug_name")        );
                    sqlParams.add(t.get("doctor_code")      );
                    sqlParams.add((!"".equals(t.get("doctor_name"))?t.get("doctor_name"):"无") );
                    sqlParams.add(t.get("amouts")           );
                    sqlParams.add(t.get("costs")            );
                    sqlParams.add(t.get("package_spec")     );
                    sqlParams.add(t.get("org_code")         );
                    sqlParams.add(t.get("org_name")         );
                    sqlParams.add(tCom.get("toxi_property") );
                    sqlParams.add(t.get("firm_id")          );
                    sqlParams.add(t.get("antidrugflag")     );
                    sqlParams.add(t.get("dispensary")       );
                    sqlParams.add(t.get("dispensaryname")   );
                    sqlParams.add(t.get("units")            );
                    
                    query.update(sql,sqlParams.toArray());
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
                JDBCQueryImpl    query = DBQueryFactory.getQuery("PEAAS");
                StringBuffer sqlBuffer = new StringBuffer();
                sqlBuffer.append("select t.order_date,");
                sqlBuffer.append("          p.drug_code,");
                sqlBuffer.append("          p.drug_name,");
                sqlBuffer.append("          p.package_spec,");
                sqlBuffer.append("          p.units,");
                sqlBuffer.append("          p.firm_id,");
                sqlBuffer.append("          t.doctor_code,");
                sqlBuffer.append("          t.doctor_name,");
                sqlBuffer.append("          p.antidrugflag,");
                sqlBuffer.append("          t.patient_id,");
                sqlBuffer.append("          t.identity,");
                sqlBuffer.append("          t.presctypename,");
                sqlBuffer.append("          t.patient_age,");
                sqlBuffer.append("          t.patient_name,");
                sqlBuffer.append("          t.patient_sex,");
                sqlBuffer.append("          t.patient_birth,");
                sqlBuffer.append("          t.dispensary,");
                sqlBuffer.append("          t.dispensaryname,");
                sqlBuffer.append("          t.org_code,");
                sqlBuffer.append("          t.org_name,");
                sqlBuffer.append("       sum(p.amount) amouts,");
                sqlBuffer.append("       sum(p.costs) costs");
                sqlBuffer.append("  from presc t, presc_detail p");
                sqlBuffer.append(" where t.id = p.presc_id");
                sqlBuffer.append("   and t.order_type = '1' and t.order_date = '").append(ADate).append("'");
                sqlBuffer.append(" group by t.order_date,");
                sqlBuffer.append("          p.drug_code,");
                sqlBuffer.append("          p.drug_name,");
                sqlBuffer.append("          p.package_spec,");
                sqlBuffer.append("          p.units,");
                sqlBuffer.append("          p.firm_id,");
                sqlBuffer.append("          p.antidrugflag,");
                sqlBuffer.append("          t.patient_id,");
                sqlBuffer.append("          t.identity,");
                sqlBuffer.append("          t.presctypename,");
                sqlBuffer.append("          t.patient_age,");
                sqlBuffer.append("          t.patient_name,");
                sqlBuffer.append("          t.patient_sex,");
                sqlBuffer.append("          t.patient_birth,");
                sqlBuffer.append("          t.dispensary,");
                sqlBuffer.append("          t.dispensaryname,");
                sqlBuffer.append("          t.doctor_code,");
                sqlBuffer.append("          t.doctor_name,");
                sqlBuffer.append("          t.org_code,");
                sqlBuffer.append("          t.org_name");
                List<TCommonRecord> listDDDC = query.query(sqlBuffer.toString(), cmr);
                query.update("delete DR_DRUGPATDDCOST where dr_date = to_date('" + ADate + "','yyyy-mm-dd')");
                for(TCommonRecord t : listDDDC)
                {
                    TCommonRecord tCom = dc.getDrugDictInfo(t.get("drug_code"));
            		List<Object> sqlParams = new ArrayList<Object>();
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
                            + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(t.get("order_date")).getTime());
					sqlParams.add(dateTime);
                    sqlParams.add(t.get("drug_code")      );
                    sqlParams.add(t.get("drug_name")      );
                    sqlParams.add((!"".equals(t.get("patient_id"))?t.get("patient_id"):"无") );
                    sqlParams.add(t.get("patient_name")   );
                    sqlParams.add(t.get("patient_sex")    );
                    sqlParams.add(t.get("patient_age")    );
                    sqlParams.add(t.get("identity")       );
                    sqlParams.add(t.get("doctor_code")    );
                    sqlParams.add((!"".equals(t.get("doctor_name"))?t.get("doctor_name"):"无") ); 
                    sqlParams.add(t.get("amouts")         );
                    sqlParams.add(t.get("costs")          );
                    sqlParams.add(t.get("package_spec")   );
                    sqlParams.add(t.get("units")          );
                    sqlParams.add(t.get("org_code")       );
                    sqlParams.add(t.get("org_name")       );
                    sqlParams.add(t.get("firm_id")        );
                    sqlParams.add(t.get("presctypename")  ); 
                    sqlParams.add(t.get("antidrugflag")   );
                    sqlParams.add(tCom.get("toxi_property") );
                    sqlParams.add(t.get("dispensary")     );
                    sqlParams.add(t.get("dispensaryname") ); 
                    
                    query.update(sql,sqlParams.toArray());
                }
                cmr       = null;
                query     = null;
                sqlBuffer = null;
                listDDDC  = null;
            }
        });
    }
    
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
