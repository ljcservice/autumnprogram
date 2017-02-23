package com.ts.service.pdss.CustomAudit.serviceBeans;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hitzd.his.Beans.TPatOrderDiagnosis;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.CustomAudit.Beans.TCustomAuditInfo;
import com.ts.entity.pdss.CustomAudit.RSBeans.TCustomCheckRslt;
import com.ts.entity.pdss.CustomAudit.RSBeans.TDrugCustomSecurityRslt;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.service.pdss.CustomAudit.RowMapper.CustomAuditInfoRowMapper;
import com.ts.service.pdss.CustomAudit.services.IDrugCustomChkService;
import com.ts.service.pdss.pdss.Utils.QuerySingleUtils;
/**
 * 自定义审核 主方法 
 * @author Administrator
 *
 */
@Service
public class DrugCustomChkServiceBean extends Persistent4DB implements IDrugCustomChkService
{

    @Override
    public TDrugCustomSecurityRslt CheckAll(TPatientOrder po,String Resctype)
    {
        setQueryCode("PDSS");
        TDrugCustomSecurityRslt rslt = new TDrugCustomSecurityRslt();
        rslt.setPo(po);
        TPatOrderDrug[]  pods = po.getPatOrderDrugs();
        for(int i = 0 ; i < pods.length ; i++)
        {
            TCustomCheckRslt ccrslt = new TCustomCheckRslt();
            TPatOrderDrug pod1 = pods[i];
            TDrug drug1 = QuerySingleUtils.queryDrug(pod1.getDrugID(), query);
            ccrslt.setDrug(drug1);
            
            for(int j = i + 1 ; j < pods.length  ; j++)
            {
                TPatOrderDrug pod2 = pods[j];
                TDrug drug2 = QuerySingleUtils.queryDrug(pod2.getDrugID(), query);
                /* 相互作用审核 */
                CheckEventType01(drug1.getDRUG_NO_LOCAL(), drug2.getDRUG_NO_LOCAL(), ccrslt);
            }
            
            CheckEventType03(drug1.getDRUG_NO_LOCAL(), po.getPatOrderDiagnosiss(), ccrslt);
            rslt.addDrugCustomCheckRslt(pod1.getDrugID() + "," 
                    + pod1.getRecMainNo() + "," + pod1.getRecSubNo(), ccrslt);
        }
        return rslt;
    }

    private static String CustomSql = "select CA_ID,DRUG_NO1,DRUG_NO2,DRUG_NO3,DRUG_NO4,ADMIN_ID,DIAGNOSIS,ALLERG,SIDE,EVENT_TYPE,EVENT_RES,EVENT_DESC,REF_INFO,MIN_DOS_PER,MAX_DOS_PER,MIN_DOS_DAY,MAX_DOS_DAY,MIN_FEQ_DAY,MAX_FEQ_DAY,MIN_DAYS,MAX_DAYS,LAST_UPDATER,LAST_UPDATE_TIME,LAST_CHECKER,LAST_CHECK_TIME from CUSTOM_AUDIT_INFO ";
  
    /**
     *  1--相互作用
     * @param ccrslt
     */
    private void CheckEventType01(String drugCodeA , String drugCodeB ,TCustomCheckRslt ccrslt)
    {
        CustomAuditInfoRowMapper caimr = new CustomAuditInfoRowMapper();
        try
        {
            String sql =  CustomSql + " where EVENT_TYPE = 1 and DRUG_NO1 = ?  and DRUG_NO2 = ?";
            List<TCustomAuditInfo> listcais = query.query(sql,new Object[]{drugCodeA,drugCodeB} ,caimr );
            ccrslt.addCustomAuditInfoList(listcais);
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            caimr = null;
        }
    }
    
    /**
     * 2--配伍禁忌
     * @param ccrslt
     */
    private void CheckEventType02(TCustomCheckRslt ccrslt)
    {
        CustomAuditInfoRowMapper caimr = new CustomAuditInfoRowMapper();
        try
        {
            String sql = CustomSql + " where event_type = 2 and ";
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }
        finally
        {
            caimr = null;
        }
    }
    
    /**
     *3--禁忌症
     * @param ccrslt
     */
    @SuppressWarnings ("unchecked")
    private void CheckEventType03(String drug , TPatOrderDiagnosis[] diag , TCustomCheckRslt ccrslt)
    {
        CustomAuditInfoRowMapper caimr = new CustomAuditInfoRowMapper();
        try
        {
            String sql = CustomSql + " where event_type = 3 and drug_no1 = ? " ;
            List<TCustomAuditInfo> listCust = query.query(sql,new Object[]{drug}, caimr);
            for(TPatOrderDiagnosis t : diag)
            {
                for(TCustomAuditInfo c : listCust)
                {
                    if(("," + c.getDIAGNOSIS() + ",").indexOf("," + t.getDiagnosisDictID() + ",") != -1 )
                    {
                        ccrslt.addCustomAuditInfo(c);
                    }
                }
            }
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }
        finally
        {
            caimr = null;
        }
    }
    
    /**
     * 4--儿童
     * @param ccrslt
     */
    private void CheckEventType04(String drug , TCustomCheckRslt ccrslt)
    {
        CustomAuditInfoRowMapper caimr = new CustomAuditInfoRowMapper();
        try
        {
            String sql = CustomSql + " where event_type = 4 and drug_no1 = ? " ;
            ccrslt.addCustomAuditInfoList(query.query(sql,new Object[]{drug}, caimr));
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }
        finally
        {
            caimr = null;
        }
    }
    
    /**
     *  5--老人
     * @param ccrslt
     */
    private void CheckEventType05(String drug ,TCustomCheckRslt ccrslt)
    {
        CustomAuditInfoRowMapper caimr = new CustomAuditInfoRowMapper();
        try
        {
            String sql = CustomSql + " where event_type = 5 and drug_no1 = ? " ;
            ccrslt.addCustomAuditInfoList(query.query(sql,new Object[]{drug}, caimr));
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }
        finally
        {
            caimr = null;
        }
    }
    
    /**
     * 6--孕妇
     * @param ccrslt
     */
    private void CheckEventType06(String drug ,TCustomCheckRslt ccrslt)
    {
        CustomAuditInfoRowMapper caimr = new CustomAuditInfoRowMapper();
        try
        {
            String sql = CustomSql + " where event_type = 6 and drug_no1 = ? " ;
            ccrslt.addCustomAuditInfoList(query.query(sql,new Object[]{drug}, caimr));
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }
        finally
        {
            caimr = null;
        }
    }
    
    /**
     * 7--哺乳
     * @param ccrslt
     */
    private void CheckEventType07(String drug , TCustomCheckRslt ccrslt)
    {
        CustomAuditInfoRowMapper caimr = new CustomAuditInfoRowMapper();
        try
        {
            String sql = CustomSql + " where event_type = 7 and drug_no1 = ? " ;
            ccrslt.addCustomAuditInfoList(query.query(sql,new Object[]{drug}, caimr));
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }
        finally
        {
            caimr = null;
        }
    }
    
    /**
     *  8--肝功
     * @param ccrslt
     */
    private void CheckEventType08(String drug , TCustomCheckRslt ccrslt)
    {
        CustomAuditInfoRowMapper caimr = new CustomAuditInfoRowMapper();
        try
        {
            String sql = CustomSql + " where event_type = 8 and drug_no1 = ? " ;
            ccrslt.addCustomAuditInfoList(query.query(sql,new Object[]{drug}, caimr));
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }
        finally
        {
            caimr = null;
        }
    }
    
    /**
     * 9--肾功
     * @param ccrslt
     */
    private void CheckEventType09(String drug , TCustomCheckRslt ccrslt)
    {
        CustomAuditInfoRowMapper caimr = new CustomAuditInfoRowMapper();
        try
        {
            String sql = CustomSql + " where event_type = 9 and drug_no1 = ? " ;
            ccrslt.addCustomAuditInfoList(query.query(sql,new Object[]{drug}, caimr));
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }
        finally
        {
            caimr = null;
        }
    }
    
    /**
     * 10--用药途径
     * @param ccrslt
     */
    private void CheckEventType10(String drug , String ad , TCustomCheckRslt ccrslt)
    {
        CustomAuditInfoRowMapper caimr = new CustomAuditInfoRowMapper();
        try
        {
            String sql = CustomSql + " where event_type = 10 and drug_no1 = ? and admin_id = ?" ;
            ccrslt.addCustomAuditInfoList(query.query(sql,new Object[]{drug,ad}, caimr));
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }
        finally
        {
            caimr = null;
        }
    }
    
    /**
     * 11--重复成分
     * @param ccrslt
     */
    private void CheckEventType11(String drug , TCustomCheckRslt ccrslt)
    {
        CustomAuditInfoRowMapper caimr = new CustomAuditInfoRowMapper();
        try
        {
            String sql = CustomSql + " where event_type = 5 and drug_no1 = ? and (drug_no2 is not null or drug_no3 is not null or drug_no4 is not null)" ;
            ccrslt.addCustomAuditInfoList(query.query(sql,new Object[]{drug}, caimr));
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }
        finally
        {
            caimr = null;
        }
    }
    
    /**
     * 12--过敏
     * @param ccrslt
     */
    private void CheckEventType12(TCustomCheckRslt ccrslt)
    {
        
    }
    
    /**
     * 13--剂量
     * @param ccrslt
     */
    private void CheckEventType13(String drug , TCustomCheckRslt ccrslt)
    {
        CustomAuditInfoRowMapper caimr = new CustomAuditInfoRowMapper();
        try
        {
            String sql = CustomSql + " where event_type = 13 and drug_no1 = ? and admin_id = ?" ;
            ccrslt.addCustomAuditInfoList(query.query(sql,new Object[]{drug}, caimr));
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }
        finally
        {
            caimr = null;
        }
    }
    
    /**
     * 14--异常信号
     * @param ccrslt
     */
    private void CheckEventType14(String drug , TPatOrderDiagnosis[] diag , TCustomCheckRslt ccrslt)
    {
        CustomAuditInfoRowMapper caimr = new CustomAuditInfoRowMapper();
        try
        {
            String sql = CustomSql + " where event_type = 14 and drug_no1 = ? " ;
            List<TCustomAuditInfo> listCust = query.query(sql,new Object[]{drug}, caimr);
            for(TPatOrderDiagnosis t : diag)
            {
                for(TCustomAuditInfo c : listCust)
                {
                    if(("," + c.getDIAGNOSIS() + ",").indexOf("," + t.getDiagnosisDictID() + ",") != -1 )
                    {
                        ccrslt.addCustomAuditInfo(c);
                    }
                }
            }
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }
        finally
        {
            caimr = null;
        }
    }
    
    /**
     * 15--禁用症
     * @param ccrslt
     */
    private void CheckEventType15(String drug , TPatOrderDiagnosis[] diag ,TCustomCheckRslt ccrslt)
    {
        CustomAuditInfoRowMapper caimr = new CustomAuditInfoRowMapper();
        try
        {
            String sql = CustomSql + " where event_type = 15 and drug_no1 = ? " ;
            List<TCustomAuditInfo> listCust = query.query(sql,new Object[]{drug}, caimr);
            for(TPatOrderDiagnosis t : diag)
            {
                for(TCustomAuditInfo c : listCust)
                {
                    if(("," + c.getDIAGNOSIS() + ",").indexOf("," + t.getDiagnosisDictID() + ",") != -1 )
                    {
                        ccrslt.addCustomAuditInfo(c);
                    }
                }
            }
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }
        finally
        {
            caimr = null;
        }
    }
    
    /**
     * 16--慎用症
     * @param ccrslt
     */
    private void CheckEventType16(String drug ,TPatOrderDiagnosis[] diag, TCustomCheckRslt ccrslt)
    {
        CustomAuditInfoRowMapper caimr = new CustomAuditInfoRowMapper();
        try
        {
            String sql = CustomSql + " where event_type = 16 and drug_no1 = ? " ;
            List<TCustomAuditInfo> listCust = query.query(sql,new Object[]{drug}, caimr);
            for(TPatOrderDiagnosis t : diag)
            {
                for(TCustomAuditInfo c : listCust)
                {
                    if(("," + c.getDIAGNOSIS() + ",").indexOf("," + t.getDiagnosisDictID() + ",") != -1 )
                    {
                        ccrslt.addCustomAuditInfo(c);
                    }
                }
            }
        }
        catch(Exception  e)
        {
            e.printStackTrace();
        }
        finally
        {
            caimr = null;
        }
    }
    
}
