package com.ts.util.doctor;

import java.util.HashMap;
import java.util.Map;

public class DoctorConst {
	public static Map<String,String> rstypeMap = new HashMap<String,String>(); 
	static{
		rstypeMap.put("diaginfo","禁");
		rstypeMap.put("dosage","法");
		rstypeMap.put("ingredien","重");
		rstypeMap.put("interaction","相");
		rstypeMap.put("iv_effect","配");
		rstypeMap.put("side","反");
		rstypeMap.put("administrator","途");
		rstypeMap.put("specpeople","特");
		rstypeMap.put("manager","管");
		rstypeMap.put("manager4Two", "管");
	}
	public static Map<String,String> rstypeColorMap = new HashMap<String,String>(); 
	static{
		rstypeColorMap.put("diaginfo","btn-pink");
		rstypeColorMap.put("dosage","btn-warning");
		rstypeColorMap.put("ingredien","btn-success");
		rstypeColorMap.put("interaction","btn-yellow");
		rstypeColorMap.put("iv_effect","btn-grey");
		rstypeColorMap.put("side","btn-danger");
		rstypeColorMap.put("administrator","btn-info");
		rstypeColorMap.put("specpeople","btn-purple");
		rstypeColorMap.put("manager","btn-success");
		rstypeColorMap.put("manager4Two","btn-success"); 
	}
}
