package com.ts.service.pdss.ias.impl;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.ias.Beans.TAntiDrugInput;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugResult;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;
import com.ts.entity.pdss.pdss.Beans.TAntiDrug;
import com.ts.service.pdss.ias.Utils.QueryUtils;
import com.ts.service.pdss.ias.manager.IAntiDrugOverTimeCheck;

/**
 * 超疗程抗菌药物使用监测与提示
 * @author Administrator
 *
 */
@Service
@Transactional
public class AntiDrugOverTimeCheckBean extends Persistent4DB implements	IAntiDrugOverTimeCheck 
{
	@Override
	public TAntiDrugSecurityCheckResult Checker(TAntiDrugInput antiDrugInp)
	{
		setQueryCode("PDSS");
		TAntiDrugSecurityCheckResult antiDrug =  new TAntiDrugSecurityCheckResult();
		try
		{
			if(!DrugUtils.isKJDrug(antiDrugInp.getDrugID()))
			{
				return antiDrug;
			}
			antiDrug.setDrug_ID(antiDrugInp.getDrugID());
			antiDrug.setDrugStandID("");
			antiDrug.setOrder_No(antiDrugInp.getRecMainNo());
			antiDrug.setOrder_Sub_No(antiDrugInp.getRecSubNo());
			/*  抗菌药物 */
			TAntiDrug antidrug = QueryUtils.getTAntiDrug(antiDrugInp.getDrugID(), query); 
			/* 判断是否超疗程 */
			if(antidrug != null )
			{
				if("-1".equals(antidrug.getMax_Days()))
				{
					antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugOverTimeCheck,"没有使用天数限制",true,"抗菌药"));
				}
				else
				{
					int useDay = DateUtils.selectDateDiff(antiDrugInp.getStartDateTime(), antiDrugInp.getStopDateTime());
					if(useDay > Integer.parseInt(antidrug.getMax_Days()== null ? "0":antidrug.getMax_Days())) 
					{
						antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugOverTimeCheck,"超疗程抗菌药物使用",false,"抗菌药"));
					}
					else
					{
						antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugOverTimeCheck,"未超疗程抗菌药物使用",true,"抗菌药"));
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return antiDrug;
	}
}
