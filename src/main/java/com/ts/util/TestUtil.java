package com.ts.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestUtil {
	private Object getObject(String param, Class class1,int type) throws Exception {
    	Field[] fs =class1.getDeclaredFields();
    	if(type==1){
    		Object result = class1.newInstance();
    		JSONObject obj = JSONObject.fromObject(param);
    		for(Object o : obj.keySet()){
    			for(Field f:fs){
    				Class<?> clsType = f.getType();
    				String name = f.getName();
    				if(o instanceof String && name.equals((String)o)){
    					String strSet = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
    					Method methodSet = class1.getDeclaredMethod(strSet,	clsType);
    					methodSet.setAccessible(true);
						Object objValue = typeConversion(clsType,obj.get(o).toString());
						methodSet.invoke(result, objValue);
    				}
    			}
    		}
    		return result;
    	}else if(type==2){
    		List<Object> s = new ArrayList<Object>();
    		JSONArray arrys = JSONArray.fromObject(param);
			for(int i=0;i<arrys.size();i++){
				JSONObject obj= (JSONObject) arrys.get(i);
				Object result = class1.newInstance();
	    		for(Object o : obj.keySet()){
	    			for(Field f:fs){
	    				String name = f.getName();
	    				if(o instanceof String && name.equals((String)o)){
	    					Class<?> clsType = f.getType();
	    					String strSet = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
	    					Method methodSet = class1.getDeclaredMethod(strSet,	clsType);
							Object objValue = typeConversion(clsType,obj.get(o).toString());
							methodSet.invoke(result, objValue);
							break;
	    				}
	    			}
	    		}
	    		s.add(result) ;
    		}

    		return s.toArray();
    	}
		return null;
	}

    /**
     * 得到类型值
     */
	public static Object typeConversion(Class<?> cls, String str) {
	    Object obj = null;
	    if("".equals(str)||str==null)
	    	return null;
	    String nameType = cls.getSimpleName();
	    if ("Integer".equals(nameType)) {
	        obj = Integer.valueOf(str);
	    }else if ("String".equals(nameType)) {
	        obj = str;
	    }else if ("Float".equals(nameType)) {
	        obj = Float.valueOf(str);
	    }else if ("Double".equals(nameType)) {
	        obj = Double.valueOf(str);
	    }else if ("Boolean".equals(nameType)) {
	        obj = Boolean.valueOf(str.equals("1")?true:false);
	    }else if ("Long".equals(nameType)) {
	        obj = Long.valueOf(str);
	    }else if ("Short".equals(nameType)) {
	        obj = Short.valueOf(str);
	    }else if ("Character".equals(nameType)) {
	        obj = str.charAt(1);
	    }else if ("Date".equals(nameType)) {
	        try {
				obj = new SimpleDateFormat("yyyy-MM-dd").parse(str) ;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	
	    return obj;
	}
}
