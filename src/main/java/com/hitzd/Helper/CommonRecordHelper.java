package com.hitzd.Helper;

import java.util.List;

import com.hitzd.DBUtils.TCommonRecord;

public class CommonRecordHelper 
{
	/**
	 * 把list里的数据变成Json格式
	 * @param list
	 * @return
	 */
	public static String toJson(List<TCommonRecord> list)
	{
		String Result = "[";
		//[{"Date":"2011-01-01","Value":500},{"Date":"2011-01-02","Value":700}]
		for (TCommonRecord cr : list)
		{
			if ("[".equals(Result))
				Result += toJson(cr);
			else 
				Result += ", " + toJson(cr);
		}
		Result += "]";
		return Result;
	}
	
	/**
	 * 把TCommonRecord变成Json格式
	 * @param cr
	 * @return
	 */
	public static String toJson(TCommonRecord cr)
	{
		String Result = "{";
		List<String> keys = cr.getKeys();
		//["Date":"2011-01-01","Value":500]
		for (String key: keys)
		{
			if ("{".equals(Result))
				Result += "\"" + key + "\": \"" + cr.get(key) + "\"";
			else
				Result += ", " + "\"" + key + "\": \"" + cr.get(key) + "\"";
		}
		Result += "}";
		return Result;
	}
}