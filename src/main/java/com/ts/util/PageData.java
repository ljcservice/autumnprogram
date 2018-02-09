package com.ts.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
/** 
 * 说明：参数封装Map
 * 创建人：
 * 修改时间：2014年9月20日
 * @version
 */
public class PageData extends HashMap implements Map{
	
	private static final long serialVersionUID = 1L;
	
	Map map = null;
	HttpServletRequest request;
	public PageData(HttpServletRequest request){
		this.request = request;
		Map properties = request.getParameterMap();
		Map returnMap = new HashMap(); 
		Iterator entries = properties.entrySet().iterator(); 
		Map.Entry entry; 
		String name = "";  
		String value = "";  
		while (entries.hasNext()) {
		    value = "";  
			entry = (Map.Entry) entries.next(); 
			name = (String) entry.getKey(); 
			Object valueObj = entry.getValue(); 
			if(null == valueObj){ 
				value = ""; 
			}else if(valueObj instanceof String[]){ 
				String[] values = (String[])valueObj;
				for(int i=0;i<values.length;i++){ 
					 value += values[i] + ",";
				}
				value = value.substring(0, value.length()-1); 
			}else{
				value = valueObj.toString(); 
			}
			name = name.toUpperCase();
			returnMap.put(name, value.trim()); 
		}
		map = returnMap;
	}
	
	public PageData() {
		map = new HashMap();
	}
	
	@Override
	public Object get(Object key) {
		Object obj = null;
		if(map.get(key) instanceof Object[]) {
			Object[] arr = (Object[])map.get(key);
			obj = request == null ? arr:(request.getParameter((String)key.toString().toUpperCase()) == null ? arr:arr[0]);
		} else {
		    key = key.toString().toUpperCase();
			obj = map.get(key);
		}
		return obj;
	}
	
	public String getString(Object key) {
		String rs = (String)get(key);
		if(rs == null || "".equals(rs)) rs = "";
		return rs ;
	}
	
	public String getObjectString(Object key)
	{
	    Object rs = get(key);
	    Class clazz = String.class;
	    String rsString = "" ;
	    if(rs.getClass().equals(String.class)){
	        rsString  = (String)rs;
	    }
	    else{
	        rsString = rs.toString();
	    }
	    return rsString;
	}
	public Integer getInt(Object key) {
		Object rs =  get(key);
		if(rs == null || "".equals(rs.toString())) return null;
		return Integer.parseInt(rs.toString()) ;
	}
	
	public Double getDouble(Object key) {
		Object rs =  get(key);
		if(rs == null ) return null;
		return Double.parseDouble(rs.toString()) ;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object put(Object key, Object value) {
	    if( key instanceof String)
	    {
	        key = key.toString().toUpperCase();
	    }
		return map.put(key, value);
	}
	
	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return map.containsValue(value);
	}

	public Set entrySet() {
		// TODO Auto-generated method stub
		return map.entrySet();
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return map.isEmpty();
	}

	public Set keySet() {
		// TODO Auto-generated method stub
		return map.keySet();
	}

	@SuppressWarnings("unchecked")
	public void putAll(Map t) {
		// TODO Auto-generated method stub
		map.putAll(t);
	}

	public int size() {
		// TODO Auto-generated method stub
		return map.size();
	}

	public Collection values() {
		// TODO Auto-generated method stub
		return map.values();
	}
	
	public static void main(String[] args) {
		PageData w = new PageData();
		Set<String> s =w.keySet(); 
		System.out.println(s.contains("22"));
	}
}
