package com.ts.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

/* 
 * 利用HttpClient进行post请求的工具类 
 */
public class HttpClientUtil {
	public String doPost(String url, JSONObject json, String charset) {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpPost = new HttpPost(url);
			// 设置参数
			StringEntity se = new StringEntity(json.toString());
			se.setContentEncoding(charset);
			se.setContentType("application/json");// 发送json数据需要设置contentType
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	public String doGet(String url, JSONObject json, String charset) {
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpGet = new HttpGet(url);
			// 设置参数
			StringEntity se = new StringEntity(json.toString());
			se.setContentEncoding(charset);
			se.setContentType("application/json");// 发送json数据需要设置contentType
//			httpGet.setParams("");
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
}