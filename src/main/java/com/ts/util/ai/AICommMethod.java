package com.ts.util.ai;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ts.plugin.TsHttpClient;

import net.sf.json.JSONObject;

/**
 * AI常用方法
 * @ClassName: AIComMethod 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年11月30日 下午1:03:20 
 *
 */
public class AICommMethod {
	
	public static int counter=0;
	
	public static boolean isNotEmpty(String str) {
		if (str == null || "".equals(str.trim()) || "null".equalsIgnoreCase(str)||str.length()==0)
			return false;
	
		return true;
	}
	
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim()) || "null".equalsIgnoreCase(str)||str.length()==0)
			return true;
		
		return false;
	}
	
	public static boolean isEmpty(Object obj) {
		if (obj == null)
			return true;	
		return false;
	}
	public static boolean isNotEmpty(Object obj) {
		if (obj == null)
			return false;	
		return true;
	}
	
	public static boolean isEmpty(List list) {
		if ( list == null || list.size() == 0 )  return true;
		
		return false;
	}
	
	public static boolean isNotEmpty(List list) {
		if ( list == null )  return false;
		if ( list.size() == 0 )  return false;
		
		return true;
	}
	
	/**
	 * <p>Object转String ,空字符串返回"", 非空返回自己</p>	.
	 * @returnS
	 * @throws Exception
	 */
	public static String nvl(Object obj) {
		String ifNull = "";
		String str = String.valueOf(obj);
		if ( str == null ) return ifNull;
		str = str.trim();
		if (  str.equals("") || "null".equals(str)) {
			return ifNull;
		} else {
			return str;
		}
	}
	
	/**
	 * <p>空字符串返回"", 非空返回自己</p>	.
	 * @returnS
	 * @throws Exception
	 */
	public static String nvl(String str) {
		String ifNull = "";
		if ( str == null ) return ifNull;
		str = str.trim();
		if ( str.equals("") || "null".equals(str)) {
			return ifNull;
		} else {
			return str;
		}
	}
	
	/**
	 * <p>Object转String ,空字符串返回ifNull, 非空返回自己</p>	.
	 * @returnS
	 * @throws Exception
	 */
	public static String nvl(String str, String ifNull) {
		if ( str == null ) return ifNull;
		str = str.trim();
		if ( str.equals("") || "null".equals(str)) {
			return ifNull;
		} else {
			return str;
		}
	}
	// 字符拼串， spe为间隔符
	public static String getS(String msg, String str, String spe) {
		if ( AICommMethod.isEmpty(msg) )
			return str;
		else if ( AICommMethod.isEmpty(str) )
			return msg;
		else
			return msg + spe + str;
	}
	//去掉开始字符
	public static String sideTrimStart(String stream, String trimstr) {
		// null或者空字符串的时候不处理
	    if (stream == null || stream.length() == 0 || trimstr == null || trimstr.length() == 0) {
	      return stream;
	    }
	 
	    // 结束位置
	    int epos = 0;
	    
	    // 正规表达式
	    String regpattern = "[" + trimstr + "]*+";
	    Pattern pattern = Pattern.compile(regpattern, Pattern.CASE_INSENSITIVE);
	    
	    // 去掉开头的指定字符 
	    Matcher matcher = pattern.matcher(stream);
	    if (matcher.lookingAt()) {
	      epos = matcher.end();
	      stream = stream.substring(epos);
	    }
	 
	    // 返回处理后的字符串
	    return stream;
	}
	//去掉结束字符
	public static String sideTrimEnd(String stream, String trimstr) {
		// null或者空字符串的时候不处理
	    if (stream == null || stream.length() == 0 || trimstr == null || trimstr.length() == 0) {
	      return stream;
	    }
	 
	    // 结束位置
	    int epos = 0;
	    
	    // 正规表达式
	    String regpattern = "[" + trimstr + "]*+";
	    Pattern pattern = Pattern.compile(regpattern, Pattern.CASE_INSENSITIVE);
	 
	    // 去掉结尾的指定字符 
	    StringBuffer buffer = new StringBuffer(stream).reverse();
	    Matcher matcher = pattern.matcher(buffer);
	    if (matcher.lookingAt()) {
	      epos = matcher.end();
	      stream = new StringBuffer(buffer.substring(epos)).reverse().toString();
	    }
	    // 返回处理后的字符串
	    return stream;
	}
	/**
	 * 获取NLP切词结果
	 * @Title: nlpCtrl 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param url
	 * @param @param o_name
	 * @param @return
	 * @param @throws UnsupportedEncodingException    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public static String nlpCtrl(String url,String o_name) {
        String res="";
		try {
	        Map<String, String> m=new HashMap<>();
	        m.put("s", o_name);
			String str=TsHttpClient.postRequest(url, m);
			//结果为json格式的数据
			JSONObject j=JSONObject.fromObject(str);
	        Iterator iterator = j.keys();
	        while(iterator.hasNext()){
	            String key = (String) iterator.next();//术语类型  疾病
	            if(key.equals("entity")){
	            	JSONObject entity=JSONObject.fromObject(j.getString(key));
	            	Iterator e_iterator = entity.keys();
	    	        while(e_iterator.hasNext()){
	    	        	String e = (String) e_iterator.next();//术语类型  疾病
	    	        	String value =entity.getString(e) ;//切词结果  ["高血压病","糖尿病","心房纤颤"]
	 		            value=value.substring(2, value.length()-2).replaceAll("\",\"", ";");
	 			        if(res=="")
	 			        	res=value;
	 			        else res=res+";"+value;
	    	        }
//	            	res=j.getString(key);
			    }
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return res;
	}
	/**
	 * 或者s在str中的数量
	 * @Title: stringNumbers 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param str
	 * @param @param s
	 * @param @return    设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	public static int stringNumbers(String str,String s)  
    {  
		if (str.indexOf(s)==-1)  
        {  
            return 0;  
        }  
        else if(str.indexOf(s) != -1)  
        {  
            counter++;  
            stringNumbers(str.substring(str.indexOf(s)+4),s);  
            return counter;  
        }  
        return 0;  
    }  
}
