package com.ts.service.pdss.pdss.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Beans.TPatOrderDiagnosis;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.his.Utils.Config;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.pdss.Beans.TAdministration;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugDiagInfo;
import com.ts.entity.pdss.pdss.Beans.TDrugDiagRel;
import com.ts.entity.pdss.pdss.RSBeans.TDrugDiagRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.service.pdss.pdss.RowMapper.DrugDiagInfoMapper;
import com.ts.service.pdss.pdss.Utils.CommonUtils;
import com.ts.service.pdss.pdss.Utils.QueryUtils;
import com.ts.service.pdss.pdss.manager.IDrugDiagChecker;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 *  禁忌症审查子模块
 * @author liujc
 *
 */
@Service
@Transactional
public class DrugDiagCheckerBean extends Persistent4DB implements IDrugDiagChecker
{
	private final static Logger log = Logger.getLogger(DrugDiagCheckerBean.class);
	
    @SuppressWarnings("unchecked")
    @Override
    public TDrugSecurityRslt Check( TPatientOrder po)//TPatientOrder po
    {
    	try
    	{
//    		JSONObject obj = JSONObject.fromObject(param);
	        /* 获得用药信息 */
	        TPatOrderDrug[] pods = po.getPatOrderDrugs() ;//(TPatOrderDrug[]) JSONArray.toArray(obj.getJSONArray("patOrderDrugs"),TPatOrderDrug.class);
	        /* 药品id*/
	        String[] drugs = new String[pods.length];
	        /* 用药途径  */
	        String[] admini = new String[pods.length];
	        for (int i = 0; i < pods.length; i++)
	        {
	            drugs[i]  = pods[i].getDrugID();
	            admini[i] = pods[i].getAdministrationID();
	        }
	        /* 诊断id*/
	        TPatOrderDiagnosis[] patOds = po.getPatOrderDiagnosiss();
//	        TPatOrderDiagnosis[] patOds = (TPatOrderDiagnosis[]) JSONArray.toArray(obj.getJSONArray("patOrderDiagnosiss"),TPatOrderDiagnosis.class);
	        String[] diagnosis          = new String[patOds.length];
	        for (int i = 0; i < patOds.length; i++)
	        {
	            diagnosis[i] = patOds[i].getDiagnosisDictID();
	            System.out.println("诊断信息 ：" + diagnosis[i]);
	        }
	        //判断传入的是否icd码 如果不是执行 
	        if(!Config.getParamValue("diag_icd9_conv_flag").equals("1") && !Config.getParamValue("diag_icd10_conv_flag").equals("1") )
	        {
    	        /* 疾病诊断码sql组装     queryDiagDictMap 转换 诊断码 */
    	        diagnosis = QueryUtils.queryDiagDictMap(diagnosis,query);
	        }
	        /* 检查是否有值 */
	        if(diagnosis.length == 0)
	            return new TDrugSecurityRslt();
	        // 诊断对应的疾病 sql 组装 
	        StringBuffer DiaeVsDiag = new StringBuffer();
	        Map<String, List<TCommonRecord>> diseVsdiagMap = new HashMap<String, List<TCommonRecord>>();
	        for(String str : diagnosis)
	        {
	            List<TCommonRecord> list = QueryUtils.queryDiseageVsdiag(str, query);
//	            diseVsdiagMap.put(str, list);
	            if(list == null || list.size() == 0 ) continue;
	            for(TCommonRecord t : list)
	            {
	                DiaeVsDiag.append(t.get("disease_id")).append(",");
	            }
	        }
	        if(DiaeVsDiag.length() > 0 ) 
	            DiaeVsDiag.deleteCharAt(DiaeVsDiag.length() - 1);
	        else 
	            return  new TDrugSecurityRslt();
	        /* 所有药品信息 */
	        List<TDrug> drugslist  = QueryUtils.queryDrug(drugs, null, query);
	        /* 用药途径 */
	        List<TAdministration> adstraion = QueryUtils.queryAdministration(admini, null, query);
	        /* 药品类别  组装 */
	        String[] drugClassID  = new String[drugslist.size()];
	        for(int i = 0 ;i<drugslist.size();i++)
	        {
	            TDrug drug      = drugslist.get(i);
	            if(drug.getDRUG_CLASS_ID()!=null)
	                drugClassID[i] = drug.getDRUG_CLASS_ID();
	        }
	        admini = new String[adstraion.size()];
	        for(int i = 0 ;i<adstraion.size();i++)
	        {
	            admini[i] = adstraion.get(i).getADMINISTRATION_ID();
	        }
	        /* 药物禁忌症对应信息 */
	        List<TDrugDiagRel> drugDiagRels = new ArrayList<TDrugDiagRel>();
	        if(drugClassID.length > 0)
	        {
	            drugDiagRels = QueryUtils.queryDrugDiagRel(drugClassID, admini, null, query);
	        }
	        /*  药物禁忌症对应CONTRAIND_ID 组装sql*/
	        StringBuffer drugDiagRelIds = new StringBuffer();
	        for(TDrugDiagRel entry : drugDiagRels)
	        {
	            drugDiagRelIds.append("'").append(entry.getCONTRAIND_ID()).append("',");
	        }
	        /* 检查是否有值 */
	        if(drugDiagRelIds.length() == 0)
	        	return new TDrugSecurityRslt();
	        /* 药物禁忌症信息 */
	        String strSql = "select DRUG_DIAG_INFO_ID,DIAGNOSIS_DICT_ID,DRUG_DIAG_REL_ID,SEQ_ID,INTER_INDI,DIAG_DESC,DRUG_REF_SOURCE,CONTRAIND_ID from DRUG_DIAG_INFO where " +
	        " disease_id in (" + DiaeVsDiag.toString() + ") ";
	        if(drugDiagRelIds.length() > 0)
	        {
	            drugDiagRelIds.deleteCharAt(drugDiagRelIds.length()-1);
	            strSql += " and contraind_id in (" + drugDiagRelIds.toString() + ")";
	        }
	        List<TDrugDiagInfo> drugDiagInfos = (List<TDrugDiagInfo>) query.query(strSql, new DrugDiagInfoMapper());
	        /* 整理返回数据结果 */
	        TDrugSecurityRslt result = new TDrugSecurityRslt();
	        for(int i = 0 ;i<pods.length ; i++)
	        {
	            TPatOrderDrug pod = pods[i];
	            TDrug drug = CommonUtils.getDrugInfoOne(drugslist, pod);
	            TAdministration  adt =  CommonUtils.getAdministrationInfoOne(adstraion, pod);
	            if(drug == null || adt == null)
	                continue;
	            /* 对每一个返回的药品标注上 医嘱序号 */
	            drug.setRecMainNo(pod.getRecMainNo());
	            drug.setRecSubNo(pod.getRecSubNo());
	            /* 药物禁忌症对应 */
	            TDrugDiagRel ddr = null;
	            for(TDrugDiagRel entry : drugDiagRels)
	            {
	                if(entry.getDRUG_CLASS_ID().equals(drug.getDRUG_CLASS_ID()) && entry.getADMINISTRATION_ID().equals(adt.getADMINISTRATION_ID()))
	                {
	                    ddr = entry;
	                    drugDiagRels.remove(entry);
	                    break;
	                }
	            }
	            if(ddr == null)
	                continue;
	            /* 获得药物禁忌症信息 
	             * TODO 在 drugdiaginfo 中说明该字段为 DRUG_DIAG_REL_ID 不可重复 请确认。 
	             * */
	            TDrugDiagInfo ddi = null;
	            for(int j = 0 ; j<drugDiagInfos.size();j++)
	            {
	                if(ddr.getCONTRAIND_ID().equals(drugDiagInfos.get(j).getCONTRAIND_ID()))
	                {
	                    ddi = drugDiagInfos.get(j);
	                    break;
	                }
	            }
	            if(ddi == null)
	                continue;
	            TDrugDiagRslt diarslt = new TDrugDiagRslt();
	            diarslt.addDrugDiag(drug, ddr, ddi);
	            diarslt.setRecMainNo(pod.getRecMainNo());
	            diarslt.setRecSubNo(pod.getRecSubNo());
	            result.regDiagCheckResult(drug, diarslt);
	        }
	        drugslist = null;
	        return result;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		log.warn(e.getMessage());
    		return new TDrugSecurityRslt();
    	}
    }

    @SuppressWarnings({ "unchecked", "static-access" })
    @Override
    public TDrugSecurityRslt Check(String[] drugs, String[] diagnosis)
    {
        /* 设置访问数据库代码 */
        this.setQueryCode("PDSS");
        /* 疾病诊断码sql组装 */
        String diagnosi       = CommonUtils.makeWheres(diagnosis);
        /* 所有药品信息 */
        QueryUtils queryUtils = new QueryUtils();
        List<TDrug> drugslist  = queryUtils.queryDrug(drugs, null, query);
        /* 药品类吗  组装 */
        String[] drugClassID  = new String[drugslist.size()];
        for(int i = 0 ;i<drugslist.size();i++)
        {
            TDrug drug      = drugslist.get(i);
            if(drug.getDRUG_CLASS_ID()!=null)
                drugClassID[i] = drug.getDRUG_CLASS_ID();
        }
        /* 药物禁忌症对应信息 */
        //TODO 这里有的药品类码 但是 药品类码可能会重复
        List<TDrugDiagRel> drugDiagRels = new ArrayList<TDrugDiagRel>();
        if(drugClassID.length > 0)
        {
//            drugDiagRels = queryUtils.queryDrugDiagRel(drugClassID, null, query);
        }
        /*  药物禁忌症对应id 组装sql*/
        StringBuffer drugDiagRelIds = new StringBuffer();
        for(TDrugDiagRel entry : drugDiagRels)
        {
            drugDiagRelIds.append(entry.getDRUG_DIAG_REL_ID()).append(",");
        }
        /* 药物禁忌症信息 */
        String strSql = "select DRUG_DIAG_INFO_ID,DIAGNOSIS_DICT_ID,DRUG_DIAG_REL_ID,SEQ_ID,INTER_INDI,DIAG_DESC,DRUG_REF_SOURCE,CONTRAIND_ID from DRUG_DIAG_INFO where " +
        " DIAGNOSIS_DICT_ID in (" + diagnosi.toString() + ") ";
        if(drugDiagRelIds.length() > 0)
        {
            drugDiagRelIds.deleteCharAt(drugDiagRelIds.length()-1);
            strSql += " and DRUG_DIAG_REL_ID in (" + drugDiagRelIds.toString() + ")";
        }
        List<TDrugDiagInfo> drugDiagInfos = (List<TDrugDiagInfo>) query.query(strSql                
                , new DrugDiagInfoMapper());
        TDrugSecurityRslt result = new TDrugSecurityRslt();
        /* 整理数据结果返回  */
        for(TDrugDiagInfo entity : drugDiagInfos)
        {
            TDrugDiagRel ddr = null;
            TDrug       drug = null;
            /* 装载 药物禁忌症信息  */
            /* 装载 药物禁忌症对应*/
            for(TDrugDiagRel drugDiagRel : drugDiagRels)
            {
                if (entity.getDRUG_DIAG_REL_ID().equals(drugDiagRel.getDRUG_DIAG_REL_ID()))
                {
                    ddr = drugDiagRel ;
                    drugDiagRels.remove(drugDiagRel);
                    break;
                }
            }
            /* 装载 药品信息  */
            for(TDrug _drug :drugslist)
            {
                
                if (ddr.getDRUG_CLASS_ID().equals(_drug.getDRUG_CLASS_ID()))
                {
                    drug = _drug;
                    drugslist.remove(_drug);
                    break;
                }
            }
            TDrugDiagRslt diarslt = new TDrugDiagRslt();
            diarslt.addDrugDiag(drug, ddr, entity);
            result.regDiagCheckResult(drug, diarslt);
        }
        return result;
    }
}
