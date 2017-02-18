package com.ts.service.pdss.ias.impl;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.his.Utils.DrugUtils;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.ias.Beans.TAntiDrugICDMap;
import com.ts.entity.pdss.ias.Beans.TAntiDrugInput;
import com.ts.entity.pdss.ias.Beans.TOperator;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugResult;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;
import com.ts.service.pdss.ias.Utils.QueryUtils;
import com.ts.service.pdss.ias.manager.IAntiDrugOverQuotaCheck;

/**
 * 超分线使用抗菌药物监测与提示
 * @author Administrator
 *
 */
@Service
@Transactional
public class AntiDrugOverQuotaCheckBean extends Persistent4DB implements IAntiDrugOverQuotaCheck {

	
	@Override
	public TAntiDrugSecurityCheckResult Checker(TAntiDrugInput antiDrugInp)
	{
		setQueryCode("IAS");
		TAntiDrugSecurityCheckResult antiDrug = new TAntiDrugSecurityCheckResult();
		try
		{
			/* 判断是否是抗菌药物 */
			if(!DrugUtils.isKJDrug(antiDrugInp.getDrugID()))
			{
				return antiDrug ;
			}
			TOperator operator = antiDrugInp.getOperator();
			antiDrug.setDrug_ID(antiDrugInp.getDrugID());
			antiDrug.setDrugStandID("");
			antiDrug.setOrder_No(antiDrugInp.getRecMainNo());
			antiDrug.setOrder_Sub_No(antiDrugInp.getRecSubNo());
			boolean flag = false;
			/* 此处使用的是本地药物编码 */
			List<TAntiDrugICDMap> antidrugIM =  QueryUtils.getAntiDrugICDMaps(antiDrugInp.getDrugID(), query);
			if(antidrugIM == null) 
			{
				return antiDrug;
			}
			for(TAntiDrugICDMap t : antidrugIM)
			{
				if(t.getICDCode().equals(operator))
				{
					flag = true;
					break;
				}
				for(String diagnosis : antiDrugInp.getDiagnosis())
				{
					if(t.getICDCode().equals(diagnosis))
					{
						flag = true;
						break;
					}
				}
			}
			if(!flag)
			{
				antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugOverQuotaCheck,"超分线使用抗菌药物",false,"抗菌药"));
			}
			else
			{
				antiDrug.addAntiDrugResult(new TAntiDrugResult(TAntiDrugResult.AntiDrugOverQuotaCheck,"未超分线使用抗菌药物",true,"抗菌药"));
			}
//			for(TAntiDrugICDMap Anti : antidrug)
//			{
//				TAntiDrugOverQuotaResult entity = new TAntiDrugOverQuotaResult();
//				entity.setAndidrug(Anti);
//				List<Ticd10> ticds = query.query("Select * from ICD10 where icd_code = '" + Anti.getICDCode() + "'", new Ticd10Mapper());
//				entity.setTicd10((Ticd10[])ticds.toArray(new Ticd10[0]));
//				adoq.add(entity);
//			}
		}
		catch(Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
		}
		return antiDrug;
	}
}
