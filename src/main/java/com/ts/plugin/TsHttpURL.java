package com.ts.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class TsHttpURL {
	
	public static String post(String url, Map<String, String> parameters,Map<String,String> requestHeadMap,String body,String encoding) throws IOException{
		URL u = new URL(url);
		HttpURLConnection urlcon = (HttpURLConnection) u.openConnection();
        if(requestHeadMap!=null){
        	Set<Map.Entry<String, String>> set = requestHeadMap.entrySet();
        	for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                urlcon.setRequestProperty(entry.getKey(),entry.getValue());
            }
        }
        urlcon.setRequestProperty("Charsert", encoding); //设置请求编码
        urlcon.setRequestProperty("Accept-Charset", encoding);
        urlcon.setRequestMethod("POST");
		urlcon.setDoOutput(true);
		urlcon.setDoInput(true);
		urlcon.setUseCaches(false);
		urlcon.setRequestProperty("Content-Type","application/json;charset=UTF-8");
//		urlcon.addRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8"); 
        if(StringUtils.isNotBlank(body)){
        	OutputStream out = urlcon.getOutputStream();
        	out.write(body.getBytes(encoding));
        	out.close();
        }	
		urlcon.connect(); // 获取连接
		InputStream is = urlcon.getInputStream();
		String s = IOUtils.toString(is, encoding);
		is.close();
		urlcon.disconnect();
		return s;
	}
	
}
