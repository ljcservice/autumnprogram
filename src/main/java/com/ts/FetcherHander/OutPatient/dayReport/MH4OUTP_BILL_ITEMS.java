package com.ts.FetcherHander.OutPatient.dayReport;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.hitzd.Annotations.MHPerformProp;
import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.DDD.DDDUtils;
import com.hitzd.his.SysLog.SysLog4Dcdt;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.his.casehistory.helper.CaseHistoryFactory;
import com.hitzd.his.casehistory.helper.CaseHistoryFunction;
import com.hitzd.his.casehistory.helper.CaseHistoryHelperUtils;
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;
import com.ts.util.Logger;
import com.ts.util.UuidUtil;

public class MH4OUTP_BILL_ITEMS extends SysLog4Dcdt
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
            List<String> list = new ArrayList<String>();
                for(TCommonRecord t: results)
                {
                    try
                    {
                		List<Object> sqlParams = new ArrayList<Object>();
                        sql = "INSERT INTO OUTP_ORDERS_COSTS " +
                                "(PATIENT_ID, VISIT_DATE, VISIT_NO, SERIAL_NO, ORDER_CLASS, ORDER_NO, ORDER_SUB_NO,ITEM_CLASS, ITEM_NO, ITEM_NAME, ITEM_CODE, ITEM_SPEC, UNITS, REPETITION, " +
                                "AMOUNT, ORDERED_BY_DEPT, ORDERED_BY_DOCTOR, PERFORMED_BY, CLASS_ON_RCPT, COSTS, CHARGES, RCPT_NO, BILL_DESC_NO, BILL_ITEM_NO, "
                                + "ORDERED_BY_DEPT_CODE,PERFORMED_BY_CODE,is_anti,CHARGE_INDICATOR,ddd_value,OOC_ID)" +
                                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                                logger.info("门诊 存在抗菌药" + t.get("item_code") + "●●●"
                                        + t.get("item_spec") + "●●●" + dddValue);
                            }
                        }
                        sqlParams.add(is_anti);
                        sqlParams.add("1");
                        sqlParams.add(dddValue);    
                        sqlParams.add(UuidUtil.get32UUID());
                        query.update(sql,sqlParams.toArray());
                        if(!list.contains(t.get("PATIENT_ID"))) list.add(t.get("PATIENT_ID"));
                    }
                    catch (Exception e) 
                    {
                        e.printStackTrace();
                    }
                }
                for(String patId : list)
                {
                    if("".equals(patId) ) continue;
                    //查询病人信息
                    SavePatMasterIndex(hisQuery,query,patId);
                }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            chhr = null;
            //Log("门诊处置提取结束");
        }
    }
    
    /**
     * 保存病人信息至病历系统
     * @param hisQuery
     * @param patQuery
     * @param PatientID
     * @throws Exception 
     */
    public void SavePatMasterIndex(JDBCQueryImpl hisQuery, JDBCQueryImpl patQuery, String PatientID) throws Exception
    {

        String srcSQL = "select * from Pat_Master_Index where Patient_ID=?";
        List<TCommonRecord> list = patQuery.query(srcSQL, new Object[] {PatientID},new CommonMapper());
        if(list != null && list.size() > 0 ) return ;
        ICaseHistoryHelper ch = CaseHistoryFactory.getCaseHistoryHelper();
        //获得病人信息
        TCommonRecord cr  = ch.fetchPatInfo2CR(PatientID, hisQuery);
        //清理老的数据
        srcSQL = "delete from Pat_Master_Index where Patient_ID=?";
        patQuery.update(srcSQL, new Object[]{PatientID});
        Log(30, srcSQL);
        
        List<Object> sqlParams = new ArrayList<Object>();
        String dstSQL = "Insert into Pat_Master_Index(PATIENT_ID, INP_NO, NAME, NAME_PHONETIC, SEX, DATE_OF_BIRTH, " +
            "BIRTH_PLACE, CITIZENSHIP, NATION, ID_NO, IDENTITY, CHARGE_TYPE, UNIT_IN_CONTRACT, MAILING_ADDRESS, " +
            "ZIP_CODE, PHONE_NUMBER_HOME, PHONE_NUMBER_BUSINESS, NEXT_OF_KIN, RELATIONSHIP, NEXT_OF_KIN_ADDR, " +
            "NEXT_OF_KIN_ZIP_CODE, NEXT_OF_KIN_PHONE, LAST_VISIT_DATE, VIP_INDICATOR, CREATE_DATE, OPERATOR, " +
            "INSURANCE_ID, INSURANCE_CARD_NO, INSURANCE_ID_NO, LINK_DATE) "
            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        sqlParams.add(cr.get("PATIENT_ID")                        );
        sqlParams.add(cr.get("INP_NO")                            );
        sqlParams.add(cr.get("NAME")                              ); 
        sqlParams.add(cr.get("NAME_PHONETIC")                     ); 
        sqlParams.add(cr.get("SEX")                               ); 
        Timestamp dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("DATE_OF_BIRTH")).getTime());
        sqlParams.add(dateTime);
        sqlParams.add(cr.get("BIRTH_PLACE")                       ); 
        sqlParams.add(cr.get("CITIZENSHIP")                       ); 
        sqlParams.add(cr.get("NATION")                            ); 
        sqlParams.add(cr.get("ID_NO")                             ); 
        sqlParams.add(cr.get("IDENTITY")                          ); 
        sqlParams.add(cr.get("CHARGE_TYPE")                       ); 
        sqlParams.add(cr.get("UNIT_IN_CONTRACT")                  ); 
        sqlParams.add(cr.get("MAILING_ADDRESS")                   ); 
        sqlParams.add(cr.get("ZIP_CODE")                          ); 
        sqlParams.add(cr.get("PHONE_NUMBER_HOME")                 ); 
        sqlParams.add(cr.get("PHONE_NUMBER_BUSINESS")             ); 
        sqlParams.add(cr.get("NEXT_OF_KIN")                       ); 
        sqlParams.add(cr.get("RELATIONSHIP")                      ); 
        sqlParams.add(cr.get("NEXT_OF_KIN_ADDR")                  ); 
        sqlParams.add(cr.get("NEXT_OF_KIN_ZIP_CODE")              ); 
        sqlParams.add(cr.get("NEXT_OF_KIN_PHONE")                 ); 
        dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("LAST_VISIT_DATE"),DateUtils.FORMAT_DATETIME).getTime());
        sqlParams.add(dateTime);
        sqlParams.add(cr.get("VIP_INDICATOR")                     ); 
        dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("CREATE_DATE"),DateUtils.FORMAT_DATETIME).getTime());
        sqlParams.add(dateTime);
        sqlParams.add(cr.get("OPERATOR")                          ); 
        sqlParams.add(cr.get("INSURANCE_ID")                      ); 
        sqlParams.add(cr.get("INSURANCE_CARD_NO")                 ); 
        sqlParams.add(cr.get("INSURANCE_ID_NO")                   );
        dateTime = new Timestamp(DateUtils.getDateFromString(cr.get("CREATE_DATE"),DateUtils.FORMAT_DATETIME).getTime());
        sqlParams.add(dateTime);
        Log(30, dstSQL);
        try
        {
            if (patQuery.update(dstSQL,sqlParams.toArray()) == 0)
            {
//                errorList.add(dstSQL);
                this.Log(40, "病人信息保存失败，PatientID:" + PatientID);
            }
            else
            {
//                crCount.set("新病人信息", (crCount.getInt("新病人信息") + 1) + "");
            }
        }
        catch (Exception ex)
        {
//            errorList.add(dstSQL);
            this.Log(40, "病人信息保存失败，PatientID:" + PatientID + ", Exception:" + ex.getMessage());
            ex.printStackTrace();
        }
        dstSQL = null;
    }
}
