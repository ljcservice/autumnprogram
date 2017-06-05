package com.ts.FetcherHander.OutPatient.dayReport;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.hitzd.Annotations.MHPerformProp;
import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.DDD.DDDUtils;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.his.casehistory.helper.CaseHistoryFactory;
import com.hitzd.his.casehistory.helper.CaseHistoryFunction;
import com.hitzd.his.casehistory.helper.CaseHistoryHelperUtils;
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;
import com.ts.util.Logger;

public class MH4OUTP_BILL_ITEMS
{
    Logger logger = Logger.getLogger("MH4OUTP_BILL_ITEMS");
    
    @MHPerformProp(MethodParam={String.class,JDBCQueryImpl.class,JDBCQueryImpl.class})
    public void run(String aDate,JDBCQueryImpl hisQuery,JDBCQueryImpl query)
    {
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
        String strFields = "t.VISIT_DATE,t.VISIT_NO,t.RCPT_NO,t.ITEM_NO,t.ITEM_CLASS,t.CLASS_ON_RCPT,t.ITEM_CODE,t.ITEM_NAME,t.ITEM_SPEC,t.AMOUNT,t.UNITS,t.PERFORMED_BY,t.COSTS,t.CHARGES"
                + ",B.VISIT_DATE,B.VISIT_NO,B.PATIENT_ID,B.PRESC_INDICATOR,B.ORDERED_BY_DEPT,B.ORDERED_BY_DOCTOR,B.RCPT_NO";
        List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
        try {
            
            TCommonRecord where = CaseHistoryHelperUtils.genWhereCR("t.VISIT_DATE",
                    CaseHistoryFunction.genRToDate("outpbill.outp_bill_items", "t.VISIT_DATE", "'" + aDate + "'", "yyyy-mm-dd")
                    , "", ">=", "", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("t.VISIT_DATE",
                    CaseHistoryFunction.genRToDate("outpbill.outp_bill_items", "t.VISIT_DATE", "'" + DateUtils.getDateAdded(1,aDate) + "'", "yyyy-mm-dd")
                    , "", "<", "", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("t.VISIT_DATE","b.VISIT_DATE", "", "=", "", "");
            lsWheres.add(where);
            where = CaseHistoryHelperUtils.genWhereCR("t.visit_no","b.visit_no", "", "=", "", "");
            lsWheres.add(where);
            String sql = chhr.genSQL(strFields, "outpbill.outp_bill_items t,outpbill.outp_order_desc b", lsWheres, null,null);
            List<TCommonRecord> results = hisQuery.query(sql, new CommonMapper());
                for(TCommonRecord t: results)
                {
                    try
                    {
                		List<Object> sqlParams = new ArrayList<Object>();
                        sql = "INSERT INTO OUTP_ORDERS_COSTS " +
                                "(PATIENT_ID, VISIT_DATE, VISIT_NO, SERIAL_NO, ORDER_CLASS, ORDER_NO, ORDER_SUB_NO,ITEM_CLASS, ITEM_NO, ITEM_NAME, ITEM_CODE, ITEM_SPEC, UNITS, REPETITION, " +
                                "AMOUNT, ORDERED_BY_DEPT, ORDERED_BY_DOCTOR, PERFORMED_BY, CLASS_ON_RCPT, COSTS, CHARGES, RCPT_NO, BILL_DESC_NO, BILL_ITEM_NO, "
                                + "ORDERED_BY_DEPT_CODE,PERFORMED_BY_CODE,is_anti,CHARGE_INDICATOR,ddd_value)" +
                                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        				sqlParams.add(t.get("PATIENT_ID"));
        				Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(t.getDateString("VISIT_DATE")).getTime());
    					sqlParams.add(dateTime);
                        sqlParams.add(t.get("VISIT_NO")            );
                        sqlParams.add(t.get("SERIAL_NO")           );
                        sqlParams.add(t.get("ITEM_CLASS")          );
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
                        String is_anti = "0";
                        double dddValue = 0d;
                        if(t.get("ITEM_CLASS").equals(Config.getParamValue("Drug_In_Order")))
                        {
                            if(DrugUtils.isKJDrug(t.get("ITEM_CODE")))
                            {
                                is_anti = "1";
                                dddValue = DDDUtils.CalcDDD(
                                        t.get("item_code"),
                                        t.get("item_spec"), t.get("units"),
                                        t.get("Firm_ID"), t.get("amount"),
                                        t.get("COSTS"));
                                logger.info("存在抗菌药" + t.get("item_code") + "●●●"
                                        + t.get("item_spec") + "●●●" + dddValue);
                            }
                        }
                        sqlParams.add(is_anti);
                        sqlParams.add("1");
                        sqlParams.add(dddValue);    
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
