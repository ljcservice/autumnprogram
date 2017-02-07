package com.ts.util;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import com.ts.entity.system.User;

/** 
 * 说明：IP处理
 * 创建人：
 * 修改时间：2014年9月20日
 * @version
 */
public class PublicUtil {
	
	public static void main(String[] args) {
		System.out.println("本机的ip=" + PublicUtil.getIp());
	}
	
	public static String getPorjectPath(){
		String nowpath = "";
		nowpath=System.getProperty("user.dir")+"/";
		
		return nowpath;
	}
	
	/**
	 * 获取本机访问地址
	 * @return
	 */
	public static String getIp(){
		String ip = "";
		try {
			InetAddress inet = InetAddress.getLocalHost();
			ip = inet.getHostAddress();
			//System.out.println("本机的ip=" + ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return ip;
	}
	
	public static String getObjectString(Object obj) {
		if(obj == null){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		try {
			if(obj instanceof Map){
				@SuppressWarnings("rawtypes")
				Map map = (Map)obj;
				for(Object o:map.keySet()){
					sb.append(o.toString());
					sb.append(":");
					sb.append(map.get(o)==null?"":map.get(o).toString());
					sb.append("；");
				}
			}else{
				Field[] fields = obj.getClass().getDeclaredFields();
				for(Field field:fields){
					field.setAccessible(true);
					sb.append(field.getName());
					sb.append(":");
					sb.append(field.get(obj)==null?"":field.get(obj).toString());
					sb.append("；");
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}