package com.ts.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化，模拟加载所有的配置文件
 * 
 * @author Ran
 * 
 */
public class DBInitInfo {
	public static List<DBbean> beans = null;

	static String skDriverName;
	static String skUrl;
	static String skUsername;
	static String skPassword;
	static int skMinConn;
	static int skMaxConn;

	static String mzghDriverName;
	static String mzghUrl;
	static String mzghUsername;
	static String mzghPassword;
	static int mzghMinConn;
	static int mzghMaxConn;

	static {
		skDriverName = CommonUtils.getPropValue("dbconfig.properties", "driverClassName_mts");
		skUrl = CommonUtils.getPropValue("dbconfig.properties", "url_mts");
		skUsername = CommonUtils.getPropValue("dbconfig.properties", "username_mts");
		skPassword = CommonUtils.getPropValue("dbconfig.properties", "password_mts");
		// skMinConn =
		// Integer.parseInt(CommonUtils.getPropertiesValue("dbconfig.properties",
		// "sk.minConn"));
		// skMaxConn =
		// Integer.parseInt(CommonUtils.getPropertiesValue("dbconfig.properties",
		// "sk.maxConn"));
		// mzghDriverName =
		// CommonUtils.getPropertiesValue("dbconfig.properties",
		// "mzgh.driverName");
		// mzghUrl = CommonUtils.getPropertiesValue("dbconfig.properties",
		// "mzgh.url");
		// mzghUsername = CommonUtils.getPropertiesValue("dbconfig.properties",
		// "mzgh.username");
		// mzghPassword = CommonUtils.getPropertiesValue("dbconfig.properties",
		// "mzgh.password");
		// mzghMinConn =
		// Integer.parseInt(CommonUtils.getPropertiesValue("dbconfig.properties",
		// "mzgh.minConn"));
		// mzghMaxConn =
		// Integer.parseInt(CommonUtils.getPropertiesValue("dbconfig.properties",
		// "mzgh.maxConn"));

		beans = new ArrayList<DBbean>();
		// 这里数据 可以从xml 等配置文件进行获取
		// 为了测试，这里我直接写死
		DBbean skDBbean = new DBbean();
		skDBbean.setDriverName(skDriverName);
		skDBbean.setUrl(skUrl);
		skDBbean.setUserName(skUsername);
		skDBbean.setPassword(skPassword);
		// skDBbean.setMinConnections(skMinConn);
		// skDBbean.setMaxConnections(skMaxConn);
		skDBbean.setPoolName("skPool");
		beans.add(skDBbean);

		// DBbean mzghDBbean = new DBbean();
		// mzghDBbean.setDriverName(mzghDriverName);
		// mzghDBbean.setUrl(mzghUrl);
		// mzghDBbean.setUserName(mzghUsername);
		// mzghDBbean.setPassword(mzghPassword);
		// mzghDBbean.setMinConnections(mzghMinConn);
		// mzghDBbean.setMaxConnections(mzghMaxConn);
		// mzghDBbean.setPoolName("mzghPool");
		// beans.add(mzghDBbean);
	}
}
