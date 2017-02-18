package com.ts.service.pdss.peaas.timer.MHControl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.hitzd.Annotations.MHPerformProp;
import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.his.casehistory.helper.CaseHistoryFactory;
import com.hitzd.his.casehistory.helper.CaseHistoryFunction;
import com.hitzd.his.casehistory.helper.CaseHistoryHelperUtils;
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;

public class MH4OUTP_ORDERS_COSTS 
{
    @MHPerformProp(MethodParam={String.class,JDBCQueryImpl.class,JDBCQueryImpl.class})
    public void run(String aDate,JDBCQueryImpl hisQuery,JDBCQueryImpl query)
    {
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
        String strFields = "*";
        List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
        try {
            
            TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("VISIT_DATE",
                    CaseHistoryFunction.genRToDate("outpdoct.OUTP_ORDERS_COSTS", "VISIT_DATE", "'" + aDate + "'", "yyyy-mm-dd")
                    , "", ">=", "", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("VISIT_DATE",
                    CaseHistoryFunction.genRToDate("outpdoct.OUTP_ORDERS_COSTS", "VISIT_DATE", "'" + DateUtils.getDateAdded(1,aDate) + "'", "yyyy-mm-dd")
                    , "", "<", "", "");
            lsWheres.add(where);
            String sql = chhr.genSQL(strFields, "outpdoct.OUTP_ORDERS_COSTS", lsWheres, null,null);
            List<TCommonRecord> results = hisQuery.query(sql, new CommonMapper());
                for(TCommonRecord t: results)
                {
                    try
                    {
                		List<Object> sqlParams = new ArrayList<Object>();
                        sql = "INSERT INTO PEAAS.OUTP_ORDERS_COSTS " +
                                "(PATIENT_ID, VISIT_DATE, VISIT_NO, SERIAL_NO, ORDER_CLASS, ORDER_NO, ORDER_SUB_NO,ITEM_CLASS, ITEM_NO, ITEM_NAME, ITEM_CODE, ITEM_SPEC, UNITS, REPETITION, " +
                                "AMOUNT, ORDERED_BY_DEPT, ORDERED_BY_DOCTOR, PERFORMED_BY, CLASS_ON_RCPT, COSTS, CHARGES, RCPT_NO, BILL_DESC_NO, BILL_ITEM_NO, "
                                + "ORDERED_BY_DEPT_CODE,PERFORMED_BY_CODE,is_anti,CHARGE_INDICATOR)" +
                                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        sqlParams.add(t.get("PATIENT_ID"));
                        Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(t.getDateString("VISIT_DATE")).getTime());
    					sqlParams.add(dateTime);
                        sqlParams.add(t.get("VISIT_NO")            );
                        sqlParams.add(t.get("SERIAL_NO")           );
                        sqlParams.add(t.get("ORDER_CLASS")         );
                        sqlParams.add(t.get("ORDER_NO")            );
                        sqlParams.add(t.get("ORDER_SUB_NO")        );
                        sqlParams.add(t.get("ITEM_CLASS")          );
                        sqlParams.add(t.get("ITEM_NO")             );
                        sqlParams.add(t.get("ITEM_NAME")           );
                        sqlParams.add(t.get("ITEM_CODE")           );
                        sqlParams.add(t.get("ITEM_SPEC")           );
                        sqlParams.add(t.get("UNITS")               );
                        sqlParams.add(t.get("REPETITION")          );
                        sqlParams.add(t.get("AMOUNT")              );
                        sqlParams.add(DictCache.getNewInstance().getDeptName(t.get("ORDERED_BY_DEPT")) );
                        sqlParams.add(t.get("ORDERED_BY_DOCTOR")   );
                        sqlParams.add(DictCache.getNewInstance().getDeptName(t.get("PERFORMED_BY")) );
                        sqlParams.add(t.get("CLASS_ON_RCPT")       );
                        sqlParams.add(t.get("COSTS")               );
                        sqlParams.add(t.get("CHARGES")             );
                        sqlParams.add(t.get("RCPT_NO")             );
                        sqlParams.add(t.get("BILL_DESC_NO")        );
                        sqlParams.add(t.get("BILL_ITEM_NO")        );
                        sqlParams.add(t.get("ORDERED_BY_DEPT")     );
                        sqlParams.add(t.get("PERFORMED_BY")        );
                        sqlParams.add((t.get("ITEM_CLASS").equals(Config.getParamValue("Drug_In_Order"))?(DrugUtils.isKJDrug(t.get("ITEM_CODE"))?"1":"0"):"0") );
                        sqlParams.add(t.get("CHARGE_INDICATOR")    );
                        
                        query.update(sql,sqlParams.toArray());
                    }
                    catch (Exception e) 
                    {
                        e.printStackTrace();
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            chhr = null;
            //Log("门诊处置提取结束");
        }
    }
}
