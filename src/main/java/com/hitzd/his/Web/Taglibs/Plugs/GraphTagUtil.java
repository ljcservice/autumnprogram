package com.hitzd.his.Web.Taglibs.Plugs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GraphTagUtil 
{
	public static String getMapValue(Map<String, String> value)
	{
		StringBuffer sb = new StringBuffer();
		if(value.size() == 0)
		{
			return "";
		}
		sb.append("[");
		for(String key : value.keySet())
		{
			sb.append("['").append(key).append("',").append(value.get(key)).append("],");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
    }
	/**
	 * 通过两个List拼装js数组
	 * @param key
	 * @param value
	 * @return
	 */
	public static String get2ListValue(List<String> key, List<String> value)
	{
		StringBuffer sb = new StringBuffer();
		if(value.size() == 0)
		{
			return "";
		}
		sb.append("[");
		for(int i=0; i<key.size(); i++)
		{
			sb.append("['").append(key.get(i)).append("',").append(value.get(i)).append("],");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}
	public static String convertList2JsArray(List<String> list)
	{
		StringBuffer s = new StringBuffer();
		s.append("[");
		for(String str: list){
			s.append("'" + str + "',");
		}
		if(list.size()>0)s.deleteCharAt(s.length()-1);//删除最后一个
		s.append("]");
		return s.toString();
	}
	
	public static String convertList2JsArrayWithoutComma(List<String> list)
	{
		StringBuffer s = new StringBuffer();
		s.append("[");
		for(String str: list){
			s.append("" + str + ",");
		}
		if(list.size()>0)s.deleteCharAt(s.length()-1);//删除最后一个
		s.append("]");
		return s.toString();
	}
	
	//test  getMapValue
	public static void main(String[] args)
	{
		LinkedHashMap orderMap1 = new LinkedHashMap();
		orderMap1.put("1","100");
		orderMap1.put("2","100");
		orderMap1.put("3","100");
		orderMap1.put("4","100");
		orderMap1.put("5","100");
		LinkedHashMap orderMap2 = new LinkedHashMap();
		LinkedHashMap orderMap3 = new LinkedHashMap();
		orderMap2.put("1","80");
		orderMap2.put("2","80");
		orderMap2.put("3","80");
		orderMap2.put("4","80");
		orderMap2.put("5","8");
		
		List<String> test = new ArrayList<String>();
		test.add("你");
		test.add("我");
		test.add("他");
		System.out.println(convertList2JsArray(test));
		
		List<String> test1 = new ArrayList<String>();
		test1.add("1");
		test1.add("2");
		test1.add("3");
		
		System.out.println(get2ListValue(test, test1));
	}
}
