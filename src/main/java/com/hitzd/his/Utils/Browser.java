package com.hitzd.his.Utils;

import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

public class Browser
{
	protected static HttpServletRequest request;
	/*  */
	protected static String userAgent = "";
	/* 制造商名称 */
	protected static String company = "";
	/* 浏览器名称 */
	protected static String name = "";
	/* 版本 */
	protected static String version = "" ;
	/* 主要版本 */
	protected static String mainVersion = "";
	/* 次要版本 */
	protected static String minorVersion = "";
	/* 操作系统 */
	protected static String os = "";
	/* 语言编码标准 */
	protected static String language = "de";
	/* 本地版本对象 */
	protected static Locale locale;
	/* 语言设置 */
	/* 支持的语言 */
	private static Hashtable<String, String> supportedLanguages;

	public Browser(HttpServletRequest request)
	{
		initialize();
		this.request = request;
		setUserAgent(request.getHeader("User-Agent"));
		setCompany();
		setName();
		setVersion();
		setMainVersion();
		name += mainVersion;
		setMinorVersion();
		setOs();
		setLanguage();
		setLocale();
	}
	
	public static String getBrowserName(HttpServletRequest req)
	{
		initialize();
		request = req;
		setUserAgent(request.getHeader("User-Agent"));
		setCompany();
		setName();
		setVersion();
		setMainVersion();
		name += mainVersion;
		setMinorVersion();
		setOs();
		setLanguage();
		setLocale();
		return getName();
	}

	public static void initialize()
	{
		
		supportedLanguages = new Hashtable<String, String>(2);
		supportedLanguages.put("en", "");
		supportedLanguages.put("de", "");
	}

	public static void setUserAgent(String httpUserAgent)
	{
		userAgent = httpUserAgent.toLowerCase();
	}

	public static String getUserAgent()
	{
		return userAgent;
	}

	private static void setCompany()
	{
		if (userAgent.indexOf("msie") > -1)
		{
			company = "Microsoft";
		}
		else if (userAgent.indexOf("opera") > -1)
		{
			company = "Opera Software";
		}
		else if (userAgent.indexOf("mozilla") > -1)
		{
			company = "Netscape Communications";
		}
		else
		{
			company = "unknown";
		}
	}

	/**
	 * 提供使用的浏览器制造商的公司名称
	 */
	public static String getCompany()
	{
		return company;
	}

	private static void setName()
	{
		if ("Microsoft".equals(company))
		{
			name = "ie";
		}
		else if ("Netscape Communications".equals(company))
		{
			if (userAgent.indexOf("chrome") > -1)
				name = "chrome";
			else if (userAgent.indexOf("firefox") > -1)
				name = "firefox";
		}
		else if ("Opera Software".equals(company))
		{
			name = "opera";
		}
		else
		{
			name = "unknown";
		}
	}

	/**
	 * 获取使用的浏览器的名称
	 */
	public static String getName()
	{
		return name;
	}

	private static void setVersion()
	{
		if ("Microsoft".equals(company))
		{
			String str = userAgent.substring(userAgent.indexOf("msie") + 5);
			version = str.substring(0, str.indexOf(";"));
		}
		else if ("Netscape Communications".equals(company))
		{
			if ("chrome".equals(name))
			{
				String tmp = userAgent.substring(userAgent.indexOf("chrome") + 7, userAgent.length());
				version = tmp.substring(0, tmp.indexOf(" "));
			}
			else if ("firefox".equals(name))
			{
				version = userAgent.substring(userAgent.lastIndexOf("/") + 1, userAgent.length());
			}
		}
		else if ("Opera Software".equals(company))
		{
			version = userAgent.substring(userAgent.lastIndexOf("/") + 1, userAgent.length());
		}
	}

	/**
	 * 返回使用的浏览器的版本号
	 */
	public static String getVersion()
	{
		return version;
	}

	private static void setMainVersion()
	{
		mainVersion = version.length() > 0 ? version.substring(0, version.indexOf(".") == -1 ?0:version.indexOf(".")):"";
	}

	/**
	 * 返回使用的浏览器的主版本号
	 */
	public static String getMainVersion()
	{
		return mainVersion;
	}

	private static void setMinorVersion()
	{
		minorVersion =  version.length() > 0 ? version.substring(0, version.indexOf(".") == -1 ?0:version.indexOf(".")):"";
	}

	/**
	 * 返回使用的浏览器的次版本号
	 */
	public static String getMinorVersion()
	{
		return minorVersion;
	}

	private static void setOs()
	{
		if (userAgent.indexOf("win") > -1)
		{
			if (userAgent.indexOf("windows 95") > -1 || userAgent.indexOf("win95") > -1)
			{
				os = "Windows 95";
			}
			if (userAgent.indexOf("windows 98") > -1 || userAgent.indexOf("win98") > -1)
			{
				os = "Windows 98";
			}
			if (userAgent.indexOf("windows nt") > -1 || userAgent.indexOf("winnt") > -1)
			{
				os = "Windows NT";
			}
			if (userAgent.indexOf("win16") > -1 || userAgent.indexOf("windows 3.") > -1)
			{
				os = "Windows 3.x";
			}
		}
	}

	/**
	 * 获取操作系统的名称
	 */
	public static String getOs()
	{
		return os;
	}

	private static void setLanguage()
	{
		String prefLanguage = request.getHeader("Accept-Language");
		if (prefLanguage != null)
		{
			String language = null;
			StringTokenizer st = new StringTokenizer(prefLanguage, ",");
			int elements = st.countTokens();
			for (int idx = 0; idx < elements; idx++)
			{
				if (supportedLanguages.containsKey((language = st.nextToken())))
				{
					language = parseLocale(language);
				}
			}
		}
	}

	/**
	 * setLanguage()辅助功能
	 */
	private static String parseLocale(String language)
	{
		StringTokenizer st = new StringTokenizer(language, "-");
		if (st.countTokens() == 2)
		{
			return st.nextToken();
		}
		else
		{
			return language;
		}
	}

	/**
	 * 返回用户的首选语言的国家代码
	 */
	public static String getLanguage()
	{
		return language;
	}

	private static void setLocale()
	{
		locale = new Locale(language, "");
	}

	public static Locale getLocale()
	{
		return locale;
	}
}