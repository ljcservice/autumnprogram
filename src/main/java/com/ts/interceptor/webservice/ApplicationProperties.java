package com.ts.interceptor.webservice;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

/**
 * @author xiaowang
 * 
 */
public final class ApplicationProperties {
	private static final String resourceName = "/application.properties";
	private static Properties property = new Properties();
	
	static {
		ApplicationProperties.init();
		String hosturl = ApplicationProperties.getPropertyValue("host.url");
		if(StringUtils.isNotEmpty(hosturl)) {
//			ComConstants.setPAGE_HOST_URL(hosturl);
		}
	}
	
	/**
	 * 初始化
	 */
	public static void init() {
		ApplicationProperties.setProperty(PropertiesLoaderUtils.loadUrlProperties(resourceName));
		 if(null==System.getProperty("org.apache.cxf.binding.soap.messageFactoryClassName")||"".equals(System.getProperty("org.apache.cxf.binding.soap.messageFactoryClassName"))){  
             
	            System.setProperty("org.apache.cxf.binding.soap.messageFactoryClassName","com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl");  
	       }  
	}

	/**
	 * @return Properties
	 */
	public static Properties getProperty() {
		if (property == null || property.isEmpty() || property.size() == 0) {
			ApplicationProperties.init();
		}
		return property;
	}

	public static void setProperty(Properties property) {
		ApplicationProperties.property = property;
	}

	/**
	 * 获取属性值
	 * @param key
	 * @return String
	 */
	public static String getPropertyValue(String key) {
		if (StringUtils.isEmpty(key)) {
			return "";
		}
		return (String) ApplicationProperties.getProperty().get(key);
	}
}