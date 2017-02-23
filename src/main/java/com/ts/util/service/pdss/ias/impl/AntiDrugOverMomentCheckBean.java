package com.ts.service.pdss.ias.impl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.ias.Beans.TAntiDrugInput;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugResult;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;
import com.ts.service.pdss.ias.manager.IAntiDrugOverMomentCheck;

/**
 * 
 * 超使用时机（预防使用）抗菌药物监测与提示
 * @author Administrator
 *
 */
@Service
@Transactional
public class AntiDrugOverMomentCheckBean extends Persistent4DB implements IAntiDrugOverMomentCheck 
{
	@Override
	public TAntiDrugSecurityCheckResult Checker(TAntiDrugInput antiDrugInp) 
	{
		TAntiDrugSecurityCheckResult antiDrug = new TAntiDrugSecurityCheckResult();
		try
		{
		    antiDrug.setDrug_ID(antiDrugInp.getDrugID());
	        antiDrug.setOrder_No(antiDrugInp.getRecMainNo());
	        antiDrug.setOrder_Sub_No(antiDrugInp.getRecSubNo());
	        boolean momentFlag = false;
	        for(String diag : antiDrugInp.getDiagnosis())
	        {
	        	if(diag == null)continue;
	        	if(diag.length() > 0)
	        	{
		            if("A".equals(diag.substring(0, 1).toString()))
		            {
		                momentFlag = true;
		                break;
		            }
	        	}
	        }
	        if(antiDrugInp.getOperator() != null )
	        {
	            momentFlag = true;
	        }
	        if(momentFlag)
	        {
	            antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugOverMomentCheck, "未超使用时机抗菌药物", true,"抗菌药"));
	        }
	        else
	        {
	            antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugOverMomentCheck, "超使用时机抗菌药物", false,"抗菌药"));   
	        }
		}
		catch(Exception e)
		{
		    e.printStackTrace();
		}
		finally{}
		return antiDrug;
	}
}
