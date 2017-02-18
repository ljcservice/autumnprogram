package com.ts.service.pdss.ias.Cahce;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ts.entity.pdss.ias.Beans.TAntiDrugICDMap;
import com.ts.entity.pdss.ias.Beans.TAntiDrugUseRule;
import com.ts.entity.pdss.pdss.Beans.TAntiDrug;

/**
 * 抗菌药物审查缓存
 * @author Administrator
 *
 */
public class BeanCache
{
	/* 超授权字典表   */
	private static Map<String, TAntiDrugUseRule>      antiDrugUR = new LinkedHashMap<String, TAntiDrugUseRule>();
	/* 药编码转换成标准药品码 */
	private static Map<String, List<TAntiDrugICDMap>> antiDrugIM = new LinkedHashMap<String, List<TAntiDrugICDMap>>();
	/* 抗菌药物字典 */
	private static Map<String, TAntiDrug>              antiDrug  = new LinkedHashMap<String, TAntiDrug>();

	/**
	 * 放置抗菌药物
	 * @param antidru
	 */
	public static void setAntiDrug(TAntiDrug  antidrug)
	{
		if(antidrug != null && antiDrug.get(antidrug.getDRUG_NO_LOCAL())!= null)
		{
			antiDrug.put(antidrug.getDRUG_NO_LOCAL(), antidrug);
		}
	}
	
	/**
	 * 获得抗菌药物
	 * @param drugid
	 * @return
	 */
	public static TAntiDrug getAntiDrug(String drugid)
	{
		return antiDrug.get(drugid);
	}
	
	/**
	 * 获得 drug 对照icd10诊断码
	 * @param drugID
	 * @return
	 */
	public static List<TAntiDrugICDMap> getAntiDrugICDMap(String drugID)
	{
		return antiDrugIM.get(drugID);
	}
	
	/**
	 * 存放 drug 对照icd10诊断码
	 * @param antiIM
	 * @param drugID
	 */
	public static void setAntiDrugICDMap(List<TAntiDrugICDMap> antiIM, String drugID )
	{
		if(antiIM != null && antiDrugIM.get(drugID) != null)
		{
			antiDrugIM.put(drugID, antiIM);
		}
	}
	
	/**
	 * 超授权字典表
	 * @param antiDUR
	 */
	public static void setAntiDrugUseRult(TAntiDrugUseRule antiDUR)
	{
		if(antiDUR != null && antiDrugUR.get(antiDUR.getDrug_ID()) == null)
		{
			antiDrugUR.put(antiDUR.getDrug_ID(), antiDUR);
		}
	}
	
	/**
	 * 超授权字典表
	 * @param drug_id
	 * @return
	 */
	public static TAntiDrugUseRule getDrugUseRule(String drug_id)
	{
		return antiDrugUR.get(drug_id);
	}
}
