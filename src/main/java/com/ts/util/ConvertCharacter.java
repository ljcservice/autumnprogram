package com.ts.util;

import java.util.HashMap;
import java.util.Map;

public class ConvertCharacter {
	private static Map<String,String> map = new HashMap<String,String>();
	static{
		map.put("<", "&lt;");
		map.put(">", "&gt;");
		map.put("/", "&frasl;");
		map.put("\"", "&quot;");
	}
	public static String specilConvertCharacter(String content){
		if(content==null) return content;
		for(String s: map.keySet()){
			content = content.replaceAll(s, map.get(s));
		}
		return content;
	}
}
