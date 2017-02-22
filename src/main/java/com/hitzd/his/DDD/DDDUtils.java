package com.hitzd.his.DDD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.Utils.DictCache;

public class DDDUtils  
{
	
	public static long HoursBetween(String Start, String Stop)
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
		    Date beginTime = df.parse(Start);
		    Date endTime = df.parse(Stop);
		    long dayInterval = 0;
		    dayInterval = (endTime.getTime() - beginTime.getTime()) / (24 * 60 * 60 * 1000) + 1;
		    // 当天开药，当天结束，算1次
		    dayInterval = dayInterval == 0 ? 1 : dayInterval; 
		    return dayInterval * 24;
		}
		catch (Exception ex)
		{
			return 0;
		}
		
	}
	
	public static String getDrugDDDUnit(String DDD_ID, JDBCQueryImpl query)
	{
		String sql = "select * from dddd where DDD_ID = '" + DDD_ID + "'";
		@SuppressWarnings("unchecked")
		List<String> listx = (List<String>)query.query(sql, new RowMapper()
		{
			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException 
			{
				return rs.getString("DDD_Value");
			}
		});
		if (listx.size() > 0)
			return listx.get(0);
		else return "";
	}
	
	public static String getAdministrationStdCode(String Administration, JDBCQueryImpl query)
	{
		String sql = "select * from comm.Administration_Dict where administration_name = '" + Administration + "'";
		@SuppressWarnings("unchecked")
		List<TCommonRecord> list = query.query(sql, new CommonMapper());
		if (list.size() > 0)
		{
			TCommonRecord cr = list.get(0);
			if (cr.get("IS_Injection").equals("1"))
			{
				return "P";
			}
		}
		return "O";
	}
	
	/**
	 * 计算DDD的另一种方法，该方法只考虑药品中的数量之比，不考虑单位，我们假定登记的DDD单位和摆药中出现的单位是一致的
	 * 如此计算的好处是不需要进行单位转换，但是要求医院在填写DDD的时候，将国际规定的DDD单位转换成实际使用中的单位。
	 * 举例：
	 *     药品A，国际规定DDD为3.6g，而实际使用时可能单位为支，每支含药品A1.8g，这样，药品A每日最大剂量是2支，因此药品配对里填写2即可，
	 *     否则按原来的方式，药品配对里填写3.6，同时要填写单位转换表，支->克的转换单位为1.8g/支，计算复杂。
	 *     
	 * 再举例
	 *     药品B，国际规定DDD为2g，实际使用单位为万单位，每100万单位含要药品B 0.8g，因此，药品B每日最大剂量是250万单位，药品配对里填写250即可。
	 *     
	 * @param DrugCode
	 * @param DrugSpec
	 * @param DrugUnits
	 * @param FirmID
	 * @param Amount
	 * @param Costs
	 * @return
	 */
	public static double CalcDDDEx(String DrugCode, String DrugSpec, String DrugUnits, String FirmID, String Amount, String Costs)
	{
		double dddValue        = 0;
		TCommonRecord DrugInfo = DictCache.getNewInstance().getDrugInfoByCodeUnitSpec(DrugCode, DrugUnits, DrugSpec);
		if (DrugInfo == null)
		{
			String s = "药品：" + DrugCode + " " + DrugSpec + " " + DrugUnits + " 未能找到对应记录，不能计算DDD";
			System.out.println(s);
			return 0;
		}
		double Total           = Integer.parseInt(Amount);
		JDBCQueryImpl query    = DBQueryFactory.getQuery("PDSS");
	    @SuppressWarnings("unchecked")
		List<TCommonRecord> crList = query.query("select * from Drug_Map where Drug_No_Local = '" + DrugCode + "' and Drug_Spec = '" + DrugSpec + "'", new CommonMapper());
	    if (crList.size() > 0)
	    {
	    	TCommonRecord crAntiDrug = crList.get(0);
	    	double dddUnit = crAntiDrug.getDouble("DDD_Value");
	    	if (DrugUnits.equalsIgnoreCase(crAntiDrug.get("DDD_Unit")))
	    	{
		    	if (dddUnit == 0)
		    		return 0;
		    	dddValue = Total /dddUnit;
	    	}
	    	else
	    	if (DrugUnits.equalsIgnoreCase(crAntiDrug.get("DDD_Package_Unit")))
	    	{
		    	if (dddUnit == 0)
		    		return 0;
		    	dddValue = Total * crAntiDrug.getInt("DDD_Package_Value") / dddUnit;
	    	}
	    	else
	    	{
	    		if (dddUnit == 0)
	    		{
	    			String s = "药品：" + DrugCode + " " + DrugSpec + " " + DrugUnits + " 未能找到对应记录，不能计算DDD";
	    			System.out.println(s);
	    			return 0;
	    		}
		    	dddValue = Total /dddUnit;
	    	}
	    }		
		return dddValue;
	}
	
	@SuppressWarnings("unchecked")
	public static double CalcDDD(String DrugCode, String DrugSpec, String DrugUnits, String FirmID, String Amount, String Costs)
	{
		double dddValue        = 0;
		JDBCQueryImpl query    = DBQueryFactory.getQuery("PDSS");
		List<TCommonRecord> crList = query.query("select * from Drug_Map where Drug_No_Local = '" + DrugCode + "' and Drug_Spec = '" + DrugSpec + "' and IS_ANTI = 1", new CommonMapper());
		if (crList.size() > 0)
	    {
	    	TCommonRecord crAntiDrug = crList.get(0);
	    	//2014-04-30 liujc 修改  数据增加 DDD_PER_UNIT 字段   计算ddd时候 需要重新整理计算基数，为了不影响医院数据故此处理，必须兼容已经实施的医院 
	    	// 考虑地方医院数据库可能没有 Dose_Per_Unit ， Dose_Units 
	    	double DosePerUnit = crAntiDrug.getDouble("DDD_PER_UNIT") != 0 ? crAntiDrug.getDouble("DDD_PER_UNIT") : crAntiDrug.getDouble("Dose_Per_Unit");
			// 取出基本剂量的单位，可能是克、毫克、g、mg、ml、毫升、万单位、单位、万u、万iu。。。。。。
			String DoseUnit    = crAntiDrug.getDouble("DDD_PER_UNIT") != 0 ? crAntiDrug.get("DDD_UNIT") : crAntiDrug.get("Dose_Units").trim();
	    	double dddUnit     = crAntiDrug.getDouble("DDD_Value");
	    	// 取出每单位的基本剂量
			double Total       = DosePerUnit * Double.parseDouble(Amount);
	    	if (dddUnit == 0 || dddUnit < 0)
	    		return 0;
	    	// 单位统一转换成mg，目前应该有三种单位:克、单位、毫升
	    	if (DoseUnit.equals("克") || DoseUnit.equals("毫克") || 
	    		DoseUnit.equalsIgnoreCase("g") || DoseUnit.equalsIgnoreCase("mg"))
	    	{
	    		// 毫克转换为克
	    		if (DoseUnit.equals("毫克") || DoseUnit.equalsIgnoreCase("mg"))
	    		{
	    			Total = Total / 1000;
	    		}
	    	}
			else
			{
				return 0;
			}
	    	dddValue = Total /dddUnit;
	    }		
		return dddValue;
	}
}
