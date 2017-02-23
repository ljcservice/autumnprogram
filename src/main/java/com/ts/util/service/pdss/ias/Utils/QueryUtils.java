package com.ts.service.pdss.ias.Utils;

import java.util.List;

import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.Factory.DBQueryFactory;
import com.ts.entity.pdss.ias.Beans.TAntiDrugICDMap;
import com.ts.entity.pdss.ias.Beans.TAntiDrugUseRule;
import com.ts.entity.pdss.pdss.Beans.TAntiDrug;
import com.ts.service.pdss.ias.Cahce.BeanCache;
import com.ts.service.pdss.ias.RowMapper.TAntiDrugICDMapMapper;
import com.ts.service.pdss.ias.RowMapper.TAntiDrugUseRuleMapper;
import com.ts.service.pdss.pdss.RowMapper.AntiDrugMapper;

/**
 * 缓存查询
 * @author Administrator
 *
 */
public class QueryUtils 
{

	/**
	 * 超授权字典表
	 * @param drug_id 为本地药品 
	 * @param query
	 * @return
	 */
	public static TAntiDrugUseRule getAntiDrugUseRule(String drug_id,JDBCQueryImpl query)
	{
	    if(query == null) 
	    {
	        query = DBQueryFactory.getQuery("");
	    }
		TAntiDrugUseRule antidur = BeanCache.getDrugUseRule(drug_id);
		if(antidur == null)
		{
			StringBuffer sql = new StringBuffer();
			sql.append("select SERIAL_NO,Drug_ID,Use_Dept,Use_Level,Auditor,Audit_Date,Audit_Res,Memo from Anti_Drug_Use_Rule where Drug_ID = '")
				.append(drug_id).append("'");
			antidur = (TAntiDrugUseRule) query.queryForObject(sql.toString(), new TAntiDrugUseRuleMapper());
			if(antidur != null)
			{
				BeanCache.setAntiDrugUseRult(antidur);	antidur.setDrug_ID(drug_id);
			}
			
		}
		return antidur;
	}
	
	/**
	 * ICD10诊断码
	 * @param drugID 本地药品码
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<TAntiDrugICDMap> getAntiDrugICDMaps(String drugID ,JDBCQueryImpl query)
	{
		List<TAntiDrugICDMap> antiDrugIM = BeanCache.getAntiDrugICDMap(drugID);
		if(antiDrugIM == null)
		{
			StringBuffer sql = new StringBuffer();
			sql.append("select * from anti_drug_icd_map where drug_id = '").append(drugID).append("'");
			antiDrugIM = (List<TAntiDrugICDMap>)query.query(sql.toString(), new TAntiDrugICDMapMapper());
			if(antiDrugIM != null)
			{
				BeanCache.setAntiDrugICDMap(antiDrugIM, drugID);
			}

		}
		return antiDrugIM;
	}	 
	
	/**
	 * 抗菌药物 缓存 
	 * @param drugid
	 * @param query
	 * @return
	 */
	public static TAntiDrug getTAntiDrug(String drugid ,JDBCQueryImpl query)
	{
		TAntiDrug antiDrug = BeanCache.getAntiDrug(drugid);
		if(antiDrug == null)
		{
			StringBuffer sql = new StringBuffer();
			sql.append("select serial_no,item_class, drug_no_local, item_spec, units, dose_per_unit, dose_units, std_dosage, std_indicator, infect_class, drug_class, drug_sens, drug_use_condition, ispre, max_days, min_days ,drug_name_local from anti_drug where ")
					.append(" drug_no_local = '").append(drugid).append("' and rownum <=1 ");
			antiDrug = (TAntiDrug)query.queryForObject(sql.toString(), new AntiDrugMapper());
			if(antiDrug != null)
			{
				BeanCache.setAntiDrug(antiDrug);
			}
		}
		return antiDrug;
	}
}
