package com.ts.service.pdss.pdss.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Beans.TPatOrderDiagnosis;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.persistent.Persistent4DB;
import com.ts.dao.DaoSupportPdss;
import com.ts.entity.pdss.pdss.Beans.TAdministration;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugSideDict;
import com.ts.entity.pdss.pdss.RSBeans.TDrugHarmfulRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.service.pdss.pdss.Cache.PdssCache;
import com.ts.service.pdss.pdss.RowMapper.DrugSideDictMapper;
import com.ts.service.pdss.pdss.Utils.CommonUtils;
import com.ts.service.pdss.pdss.Utils.QuerySingleUtils;
import com.ts.service.pdss.pdss.Utils.QueryUtils;
import com.ts.service.pdss.pdss.manager.IDrugSideChecker;
import com.ts.util.PageData;

/**
 * 异常信号审查子模块 不良反应审查
 * @author Administrator
 * 
 */
@Service
@Transactional
public class DrugSideCheckerBean extends Persistent4DB implements IDrugSideChecker{
	@Resource(name = "pdssCache")
	private PdssCache pdssCache;
	@Resource(name = "daoSupportPdss")
	private DaoSupportPdss dao;
	private final static Logger log = Logger.getLogger(DrugSideCheckerBean.class);
    /**
     * 不良反应
     */
    @SuppressWarnings ("unchecked")
    @Override
    public TDrugSecurityRslt Check(TPatientOrder po)
    {
    	try
    	{
	        //setQueryCode("PDSS");
	        TDrugSecurityRslt result = new TDrugSecurityRslt();
	        if (po == null || po.getPatOrderDrugs() == null
	                || po.getPatOrderDrugs().length == 0
	                || po.getPatOrderDrugSensitives() == null
	                || po.getPatOrderDrugSensitives().length == 0)
	        {
	            return new TDrugSecurityRslt();
	        }
	        TPatOrderDrug[] pods = po.getPatOrderDrugs();
	        /* 
	         * 医嘱诊断信息 
	         * */
//	        StringBuffer orderdiagids = new StringBuffer();
//	        for (int i = 0; i < po.getPatOrderDrugSensitives().length; i++)
//	        {
//	            orderdiagids.append("'").append( po.getPatOrderDrugSensitives()[i].getPatOrderDrugSensitiveID()).append("',");
//	        }
	        /* 诊断id*/
            TPatOrderDiagnosis[] patOds = po.getPatOrderDiagnosiss();
            String[] diagnosis = new String[patOds.length];
            for (int i = 0; i < patOds.length; i++)
            {
                diagnosis[i] = patOds[i].getDiagnosisDictID();
            }
            /* 疾病诊断码sql组装     queryDiagDictMap 转换 诊断码 */
            PageData pd = new PageData();
            pd.put("diagnosis", diagnosis);
            List<String> list = (List<String>) dao.findForList("queryDiagDictMap", pd);
//            String orderdiagids = CommonUtils.makeWheres(list.toArray(new String[0]),true);
            
	        String[] drugids = CommonUtils.getPoDrugIDs(po);
	        //List<TDrug> drugs = QueryUtils.queryDrug(drugids, null, query);
	        List<TDrug> drugs = pdssCache.queryDrugListByIds(drugids);
	        
	        /* 药品类码 */
	        //StringBuffer drugClassids = new StringBuffer();
	        List<String> drugClassids = new ArrayList<String>();
	        for (int i = 0; i < drugs.size(); i++)
	        {
	            drugClassids.add(drugs.get(i).getDRUG_CLASS_ID());
	        }
	        
	        /* 获得药品途径标准码 */
	        String[] drugAdmin = new String[po.getPatOrderDrugs().length];
	        for (int i = 0; i < po.getPatOrderDrugs().length; i++)
	        {
	            drugAdmin[i] = po.getPatOrderDrugs()[i].getAdministrationID();
	        }
	        //List<TAdministration> administration = QueryUtils.queryAdministration(drugAdmin, null, query);
	        List<TAdministration> administration = pdssCache.queryAdministrations(drugAdmin);
	        
	        List<String> adminiids = new ArrayList<String>();
//	        StringBuffer adminiids = new StringBuffer();
	        for(int i = 0 ;i<administration.size();i++)
	        {
	        	adminiids.add(administration.get(i).getADMINISTRATION_ID());
	        }
	        //TODO 是否可以找到关联中的 联合主键 如果有可以做缓冲 
//	        String sideSql = "select SIDE_ID,DIAGNOSIS_DICT_ID,SEQ_NO,DIAGNOSIS_DESC,SIDE_GROUP_ID,SEVERITY,DRUG_CLASS_ID,ADMINISTRATION_ID " +
//	        		        "from view_side where DRUG_CLASS_ID in (" +(drugClassids.length() <= 0 ? "''":drugClassids.toString()) + ") ";
//	        if (orderdiagids.length() > 0)
//	        {
//	            sideSql += " and DIAGNOSIS_DICT_ID in (" + orderdiagids.toString() + ")";
//	        }
//	        if(adminiids.length() > 0 )
//	        {
//	            adminiids.deleteCharAt(adminiids.length() - 1);
//	            sideSql +=" and ADMINISTRATION_ID in (" + adminiids.toString() + ")";
//	        }
	        /* 不良反应 所有信息*/
	        //List<TDrugSideDict> drugSides = query.query(sideSql, new DrugSideDictMapper());
            PageData param = new PageData();
            param.put("DRUG_CLASS_ID", drugClassids);
            param.put("DIAGNOSIS_DICT_ID", list);
            param.put("ADMINISTRATION_ID", adminiids);
	        List<TDrugSideDict> drugSides = (List<TDrugSideDict>) dao.findForList("DrugMapper.getTDrugSideDictList",param);
	        
	        for(TPatOrderDrug pod : pods)
	        {
	            TDrug drug = CommonUtils.getDrugInfoOne(drugs, pod);
	            TAdministration admin = pdssCache.queryAdministration(pod.getAdministrationID());
	            TDrugSideDict drugsideDict = null;
	            if(drug == null)
	                continue;
	            for(TDrugSideDict drugSide : drugSides)
	            {
	            	try {
	            		if(drugSide.getDRUG_CLASS_ID().equals(drug.getDRUG_CLASS_ID()) 
		                        && drugSide.getADMINISTRATION_ID().equals(admin.getADMINISTRATION_ID()))
		                {
		                    drugsideDict = drugSide;
		                    continue ;
		                }
					} catch (NullPointerException e) {
						continue;
					}
	            }
	            if(drugsideDict == null)
	                continue;
	            TDrugHarmfulRslt harmfulRslt = new TDrugHarmfulRslt();
	            /* 对每一个返回的药品标注上 医嘱序号 */
	            drug.setRecMainNo(pod.getRecMainNo());
	            drug.setRecSubNo(pod.getRecSubNo());
	            harmfulRslt.addDrugSide(drugsideDict,drug);
	            harmfulRslt.setRecMainNo(pod.getRecMainNo());
	            harmfulRslt.setRecSubNo(pod.getRecSubNo());
	            result.regDrugHarmfulCheckResult(drug, harmfulRslt);
	        }
	        return result;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		log.warn(e.getMessage());
    		return new TDrugSecurityRslt();
    	}
    }

	@Override
	public TDrugSecurityRslt Check(String[] drugIds, String[] adminIds,
			String[] sensitIds) {
		// TODO Auto-generated method stub
		return null;
	}
}
