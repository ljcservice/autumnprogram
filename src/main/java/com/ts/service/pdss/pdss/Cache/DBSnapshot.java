package com.ts.service.pdss.pdss.Cache;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.jdbc.core.RowMapper;

import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.Utils.DictCache;
import com.ts.entity.pdss.mas.Beans.TMemo;
import com.ts.entity.pdss.pdss.Beans.TDrugDiagRel;
import com.ts.entity.pdss.pdss.Beans.TDrugInteractionInfo;
import com.ts.entity.pdss.pdss.Beans.TDrugIvEffect;


/**
 * 药品本地药品码注册 
 * @author Administrator
 */
public class DBSnapshot 
{
	private static List<String>            drugNoLocals = new ArrayList<String>();
	private static List<TDrugInteractionInfo> diiLocals = new ArrayList<TDrugInteractionInfo>();
	private static List<TDrugDiagRel>         ddrLocals = new ArrayList<TDrugDiagRel>();
	private static List<TDrugIvEffect>        dieLocals = new ArrayList<TDrugIvEffect>();
	private static List<TMemo>                 mmLocals = new ArrayList<TMemo>();
	private static List<String>        allergenIDLocals = new ArrayList<String>();
	private static List<String>               dudLocals = new ArrayList<String>();
	private static List<String>        drugDosageLocals = new ArrayList<String>();
	private static List<String>           dirRsltLocals = new ArrayList<String>();
	/* 医保用药审查 快照 */
	private static List<String>          medicareLocals = new ArrayList<String>();
	
	
	public static boolean DrugInteractionRsltInDB(String Key1, String Key2)
	{
		return dirRsltLocals.contains(Key1.toUpperCase() + "__" + Key2.toUpperCase());
	}
	
	public static boolean DosageInDB(String key)
	{
		return drugDosageLocals.contains(key);
	}
	
	public static boolean DrugUseDetailInDB(String drugClassID)
	{
		return dudLocals.contains(drugClassID);
	}
	
	public static boolean AllergenIDInDB(String allergenID)
	{
		return allergenIDLocals.contains(allergenID);
	}
	
	/**
	 * 返回是否存在快照信息 (医保 )
	 * @param drugNoLocal
	 * @return
	 */
	public static boolean MedicareLocalInDB(String drugNoLocal)
	{
	    return medicareLocals.contains(drugNoLocal);
	}
	
	public static boolean DrugNoLocalInDB(String drugNoLocal)
	{
		return drugNoLocals.contains(drugNoLocal);
	}
	
	public static boolean DrugDiagRelInDB(TDrugDiagRel ddr)
	{
		return ddrLocals.contains(ddr);
	}
	
	public static boolean DrugInteractionInfoInDB(TDrugInteractionInfo dii)
	{
		return diiLocals.contains(dii);
	}
	
	public static boolean DrugIvEffectInDB(TDrugIvEffect die)
	{
		return dieLocals.contains(die);
	}
	
	public static boolean MemoInDB(TMemo mm)
	{
		return mmLocals.contains(mm);
	}
	
	static
	{
//		 
//		loadSnapshot();
//		Timer timer = new Timer();
//		timer.schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//				DBSnapshot.RefreshSnapshot();
//			}
//		}, 0, 3600000);

	}
    public static boolean canRun()
    {
   		Calendar cal = Calendar.getInstance();
   		int hour = cal.get(Calendar.HOUR_OF_DAY);
   		return hour == 0;
    }

	public static void RefreshSnapshot()
	{
		if (canRun())
			loadSnapshot();
	}
	public static void wakeMe()
	{
		
	}
	
	@SuppressWarnings("unchecked")
	public static void loadSnapshot()  
	{
    	JDBCQueryImpl query = DBQueryFactory.getQuery("PDSS");
    	try
		{
    		drugNoLocals = null;
    		drugNoLocals = (ArrayList<String>)query.query("select Drug_No_Local from drug_map where (Drug_No_Local is not null) or (Drug_No_Local != '')", new RowMapper(){
    			@Override
    			public Object mapRow(ResultSet rs, int arg1) throws SQLException
    			{
    				return rs.getString("Drug_No_Local");
    			}
    		});
    		System.out.println("提取本地药品码快照:" + drugNoLocals.size());
    		
    		diiLocals = null;
    		diiLocals = (ArrayList<TDrugInteractionInfo>)query.query("select INGR_CLASS_ID1, INGR_CLASS_ID2 from DRUG_INTERACTION_INFO", new RowMapper(){
				@Override
				public Object mapRow(ResultSet rs, int arg1) throws SQLException 
				{
					TDrugInteractionInfo dii = new TDrugInteractionInfo();
					dii.setINGR_CLASS_CODE1(rs.getString("INGR_CLASS_ID1"));
					dii.setINGR_CLASS_CODE2(rs.getString("INGR_CLASS_ID2"));
					return dii;
				}
    		});
    		System.out.println("提取相互作用主键快照：" + diiLocals.size());
//    		
    		ddrLocals = null;
    		ddrLocals = (ArrayList<TDrugDiagRel>)query.query("select DRUG_CLASS_ID,ADMINISTRATION_ID from DRUG_DIAG_REL", new RowMapper(){
				public Object mapRow(ResultSet rs, int arg1) throws SQLException 
				{
					TDrugDiagRel ddr = new TDrugDiagRel();
					ddr.setDRUG_CLASS_ID(rs.getString("DRUG_CLASS_ID"));
					ddr.setADMINISTRATION_ID(rs.getString("ADMINISTRATION_ID"));
					return ddr;
				}
    		});
    		System.out.println("提取禁忌症快照：" + ddrLocals.size());
    		
    		dieLocals = null;
    		dieLocals = (ArrayList<TDrugIvEffect>)query.query("select IV_CLASS_CODE1, IV_CLASS_CODE2 from DRUG_IV_EFFECT", new RowMapper(){
				@Override
				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					TDrugIvEffect die = new TDrugIvEffect();
					die.setIV_CLASS_CODE1(rs.getString("IV_CLASS_CODE1"));
					die.setIV_CLASS_CODE1(rs.getString("IV_CLASS_CODE1"));
					return die;
				}
    		});
    		System.out.println("提取配伍禁忌快照：" + dieLocals.size());
    		
    		mmLocals = null;
    		mmLocals = (ArrayList<TMemo>)query.query("select DRUG_ID from memo", new RowMapper(){
				@Override
				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					TMemo mm = new TMemo();
					mm.setDRUG_ID(rs.getString("DRUG_ID"));
					return mm;
				}
    		});
    		System.out.println("提取Memo快照：" + mmLocals.size());
    		
    		allergenIDLocals = null;
    		allergenIDLocals = (ArrayList<String>)query.query("select drug_allergen_id from view_allergen", new RowMapper(){
				@Override
				public Object mapRow(ResultSet rs, int arg1) throws SQLException 
				{
					return rs.getString("drug_allergen_id");
				}
    		});
    		
    		System.out.println("提取药物成分、药敏、药物分类快照：" + allergenIDLocals.size());
    		
    		dudLocals = null;
    		dudLocals = (ArrayList<String>)query.query("select drug_class_id from DRUG_USE_DETAIL ", new RowMapper(){
				@Override
				public Object mapRow(ResultSet rs, int arg1) throws SQLException 
				{
					return rs.getString("drug_class_id");
				}
    		});
    		System.out.println("提取TDrugUseDetail快照：" + dudLocals.size());
    		
    		drugDosageLocals = null;
    		drugDosageLocals = (ArrayList<String>)query.query("select dose_class_id, administration_id from DRUG_DOSAGE", new RowMapper(){
				@Override
				public Object mapRow(ResultSet rs, int arg1) throws SQLException 
				{
					return rs.getString("dose_class_id") + "+" + rs.getString("administration_id");
				}
    		});
    		System.out.println("提取药品剂量快照：" + drugDosageLocals.size());
    		
    		dirRsltLocals = null;
    		dirRsltLocals = (ArrayList<String>)query.query("select drug_no_locala, drug_no_localb from DRUG_Interaction_Map", new RowMapper(){
				@Override
				public Object mapRow(ResultSet rs, int arg1) throws SQLException 
				{
					return rs.getString("drug_no_locala").toUpperCase() + "__" + rs.getString("drug_no_localb").toUpperCase();
				}
    		});
    		System.out.println("提取药品药品相互作用结果快照：" + dirRsltLocals.size());
    		
    		medicareLocals = null;
    		medicareLocals = (List<String>)query.query("select drug_id from medicare_catalog ", new RowMapper()
    		{
    		    @Override
    		    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    		    {
    		        return rs.getString("drug_id");
    		    }
    		});
    		System.out.println("提取医保用药结果快照：" + medicareLocals.size());
    		DictCache.getNewInstance();
    		System.out.println("快照构建完成!");
    		
		}
    	finally
    	{
    		query = null;
    	}
	}
}