package com.ts.service.pdss.pdss.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Beans.TPatOrderDiagnosis;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.mas.Beans.TMedicareCatalog;
import com.ts.entity.pdss.mas.Beans.TMemo;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.entity.pdss.pdss.RSBeans.TMedicareRslt;
import com.ts.service.pdss.pdss.Cache.PdssCache;
import com.ts.service.pdss.pdss.Utils.QuerySingleUtils;
import com.ts.service.pdss.pdss.Utils.QueryUtils;
import com.ts.service.pdss.pdss.manager.IMedicareChecker;

/**
 * 医保审查子模块
 * @author liujc
 */
@Service
@Transactional
public class MedicareChecker extends Persistent4DB implements IMedicareChecker
{
	@Resource(name = "pdssCache")
	private PdssCache pdssCache;
	
    private final static Logger log = Logger.getLogger(MedicareChecker.class);
    /**
     * 医保药品审查
     * 改造完成
     */
    public TDrugSecurityRslt Check(TPatientOrder po)
    {
        //setQueryCode("PDSS");
        try
        {
            TDrugSecurityRslt result = new TDrugSecurityRslt();
            /* 用药信息 */
            TPatOrderDrug[] pods     =  po.getPatOrderDrugs();
            /* 诊断信息 */
            TPatOrderDiagnosis[] patoderids = po.getPatOrderDiagnosiss();
//            Map<String, TDrug> drugMap = (Map<String, TDrug>)QueryUtils.queryDrug(pods, null, query);
            for(int i = 0 ;i<pods.length;i++)
            {
            	TDrug drug = pdssCache.queryDrugById(pods[i].getDrugID());//drugMap.get(pods[i].getDrugID());//QuerySingleUtils.queryDrug(pods[i].getDrugID(),query);
            	if(drug == null) continue;
                /* 业务结果缓存中去数据*/
                //TMedicareRslt mdrsl = BeanRSCache.getDrugMedicareRslt(pods[i].getDrugID());
                TMedicareRslt mdrsl = pdssCache.getDrugMedicareRslt(pods[i].getDrugID());
                if(mdrsl == null) continue;
                /* 根据诊断整理数据  */
                TMedicareRslt tmResult = DataArrange(patoderids, mdrsl);
                tmResult.setRecMainNo(pods[i].getRecMainNo());
                tmResult.setRecSubNo(pods[i].getRecSubNo());
                /* 对每一个返回的药品标注上 医嘱序号 */
                drug.setRecMainNo(pods[i].getRecMainNo());
                drug.setRecSubNo(pods[i].getRecSubNo());
                result.regMedicareCheckResult(drug, tmResult);
            }
            return result;
        } catch(Exception e) {
            e.printStackTrace();
            log.warn(this.getClass().toString() + ":" + e.getMessage());
            return new TDrugSecurityRslt();
        }
    }
    
    /**
     * 根据诊断过滤数据
     * @param patoderids
     * @param mdrsl
     */
    private TMedicareRslt DataArrange(TPatOrderDiagnosis[] patoderids,TMedicareRslt mdrsl)
    {
        
        /* 建立对象用于复制 */
        TMedicareRslt rsvalue = new TMedicareRslt();
        rsvalue.setAlertInfo(mdrsl.getAlertInfo());
        rsvalue.setDrug(mdrsl.getDrug());
        rsvalue.setMemo(mdrsl.getListTMemo());
        rsvalue.setMedicareCatalog(mdrsl.getMedicareCatalog());
        rsvalue.setFlag(mdrsl.isFlag());
        /* 判断是否为医保用药  */
        if(rsvalue.isFlag())
        {
            if(patoderids.length == 0 )
            {
                rsvalue.setAlertInfo("所有适应症");    
            }
            else
            {
                List<TMemo>  addMemos = new ArrayList<TMemo>();
                TMemo[] memos         = rsvalue.getMemo();
                for(int i = 0 ;i<memos.length ; i++)
                {
                    TMemo memo = memos[i];
                    for(int j=0 ;j<patoderids.length ;j++)
                    {
                        TPatOrderDiagnosis diagnosisid = patoderids[j];
                        /* 适应列表 和诊断 做相等 比较 */
                        if(diagnosisid != null && diagnosisid.getDiagnosisDictID() != null &&  memo != null 
                                && diagnosisid.getDiagnosisDictID().equals(memo.getCHECK_ITEM_CODE()))
                        {
                            addMemos.add(memo);
                            break;
                        }
                    }
                }
                /* 是否有适应列表数据 */
                if(addMemos.size() == 0 )
                {
                    rsvalue.setAlertInfo("无适应症用药");
                }
                else
                {
                    rsvalue.setAlertInfo("有适应症用药");
                }
                rsvalue.setMemo(addMemos);
            }
        }
        return rsvalue;
    }
    
    
	@Override
	public TMedicareRslt Check(String DrugID, String DiagnoseCode) 
	{
        this.setQueryCode("MAS");
    	TMedicareRslt Result = new TMedicareRslt();
    	//根据drug_code判断药品编码是否存在于表medicare_catalog并且apply_type不等于”1”，
    	//如果存在则该药为医保内用药，如果不存在则该药为医保外用药，提示“该药为医保外用药”；
    	
    	List<TMedicareCatalog> list = QueryUtils.queryMedicareCatalog(new String[]{DrugID}, "", query);
    	if (list.size() == 0)
    	{
    		Result.setAlertInfo("该药为医保外用药");
    	}
    	else
    	{
    		/* 诊断编码列表为空，
    		 * 提示该药的所有适应症( 通过memo.drug_id=medicare_catalog.drug_id查询memo表得到适应症列表，
    		 * 显示字段check_item_name)，如果备注信息不为空，同时显示备注信息（medicare_catalog.remark）
    		 */
    		if ((DiagnoseCode == null) || (DiagnoseCode.length() == 0))
    		{
//    			List<TMemo> lstMemo = QueryUtils.queryMemo(new String[]{DrugID}, "", query);
    			// TODO: 此处代码未写完
    		}
    		else
    		{
    			/* 2.2诊断编码不为空
    			 * 判断诊断编码列表是否在该药品的适应症列表中
    			 * （通过memo.drug_id=medicare_catalog.drug_id查询memo表得到适应症列表，
    			 * 其中check_item_code为适应症编码）
    			 * 如果不存在提示“无适应症用药”，
    			 * 如果存在提示备注信息（medicare_catalog.remark）；
    			 */
    			// TODO: 此处代码未写完
    		}
    		/*
    		 *3.备注信息：
			 *	在导出医保审核字典时设置备注信息，对于部分自费的医保项给出自费比例说明，
			 *	对于有天数限制的医保项给出天数说明，有化验指标要求的给出化验指标要求说明。
			 *4	诊断编码不为空
			 *	判断病人的费用是否已经超过医保规定的上限、
			 *	药费占病人总费用的比例是否查过医保设定的上限。
    		 */
			// TODO: 此处代码未写完
    	}
		return Result;
	}
}

















