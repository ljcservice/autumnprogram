package com.ts.util.app;

import java.util.concurrent.ConcurrentHashMap;
import com.ts.entity.app.AppToken;

/**
 * Map缓存
 * 
 * @author popof
 *
 */
public class SessionAppMap {
	private static ConcurrentHashMap<String, AppToken> map = new ConcurrentHashMap<String, AppToken>();

	public static void setAppUser(AppToken at) {
		map.put(at.getAccess_token(), at);
	}

	/**
	 * 获得tokenMAP
	 * 
	 * @param accessToken
	 * @return rs
	 */
	public static AppToken getAppUser(String accessToken) {
		AppToken rs = null;

		if (map.containsKey(accessToken)) {
			rs = (AppToken) map.get(accessToken);
		}
		return rs;
	}

	/**
	 * 判断有效时间的Token，并删除过期token
	 */
	public static void reMovetimeOut() {
		for (String key : map.keySet()) {
			AppToken apptoken = map.get(key);
			long dates = System.currentTimeMillis(); // 获取当前时间戳
			long exp = apptoken.getExpires_in();
			if (exp < dates) {
				reMove(apptoken.getAccess_token());
			}
		}
	}

	/**
	 * 判断有效时间
	 * 
	 * @param accessToken
	 * @return
	 */
	public static boolean isTimeOut(String accessToken) {
		boolean rs = false;
		AppToken apptoken = map.get(accessToken);
		long dates = System.currentTimeMillis(); // 获取当前时间戳
		long exp = apptoken.getExpires_in();
		if (exp > dates) {
			rs = true;
		}
		return rs;
	}

	public static void reMove(String accessToken) {
		map.remove(accessToken);
	}

	/**
	 * 判断安全token是否存在或有敿
	 * 
	 * @param key
	 *            安全令牌
	 * @return
	 */
	public static boolean isTokenvalider(String key) {
		boolean rs = false;
		if (map.containsKey(key)) {
			rs = true;
		}
		return rs;
	}

	public static int getTokenSize() {
		return map.size();
	}

}
