package com.hitzd.his.casehistory.helper;

import java.util.HashMap;
import java.util.Map;

import com.hitzd.his.Beans.Middle.TTableConfig;

/**
 * 中间层函数库
 * @author Crystal
 */
public class CaseHistoryFunction
{
	/**
	 * 加:+
	 */
	public static final String PLUS = "+";
	/**
	 * 减:-
	 */
	public static final String MINUS = "-";
	/**
	 * 乘:*
	 */
	public static final String MULTIPLY = "*";
	/**
	 * 除:/
	 */
	public static final String DIVIDED = "/";
	
	/**
	 * 日期+时间格式：2013-10-29 16:20:45
	 */
	public static final String DATETIME = "yyyy-mm-dd hh24:mi:ss";
	
	/**
	 * 日期格式：2013-10-29
	 */
	public static final String DATE = "yyyy-mm-dd";
	
	/**
	 * 时间格式：16:20:45
	 */
	public static final String TIME = "hh24:mi:ss";
	
	/**
	 * SQLServer日期格式
	 */
	private static Map<String, String> SQLServerDateFmt = null;
	
	/**
	 * MySQL日期格式
	 */
	private static Map<String, String> MySQLDateFmt = null;
	
	private static Map<String, TTableConfig> tables = CaseHistoryFactory.getTableMap();

	static
	{
	    /* 初始化参数 */
		SQLServerDateFmt = new HashMap<String, String>();
		SQLServerDateFmt.put(DATETIME, "20");
		SQLServerDateFmt.put(DATE, "20");
		SQLServerDateFmt.put(TIME, "108");
		MySQLDateFmt = new HashMap<String, String>();
		MySQLDateFmt.put(DATETIME, "%Y-%m-%d %H:%i:%s");
		MySQLDateFmt.put(DATE, "%Y-%m-%d");
		MySQLDateFmt.put(TIME, "%H:%i:%s");
	}
	
	/**
	 * 获取目标字段
	 * @param tableName 表名
	 * @param fieldName 原始字段
	 * @return 目标字段
	 */
	private static String getTargetField(String tableName, String fieldName)
	{
		/* 初始化参数 */
		tableName = tableName == null ? "" : tableName.trim();
		fieldName = fieldName == null ? "" : fieldName.trim();
		// 如果存在表名，则表示该转换为标准转换；否则，被转换的字段为临时字段
		if (tableName != null && !"".equals(tableName))
		{
		    String[] f = depenTargetField(fieldName);
			if (tables.containsKey(tableName.toUpperCase()) && tables.get(tableName.toUpperCase()).getFieldMap().containsKey(f[1].toUpperCase()))
			{
				String targetField = tables.get(tableName.toUpperCase()).getFieldMap().get(f[1].toUpperCase()).getTargetField();
				if (!"".equals(targetField))
				{
				    if(!"".equals(f[0])) targetField = f[0] + "." + targetField;
				    return targetField;
				}
			}
			return fieldName;
		}
		return fieldName;
	}
	
	private static String[] depenTargetField(String fieldName)
	{
	    String[] rs = {"",fieldName};
	    if(fieldName.indexOf(".") != -1)
	    {
	        rs[0] = fieldName.substring(0,fieldName.indexOf("."));
            rs[1] = fieldName.substring(fieldName.indexOf(".") + 1,fieldName.length());
	    }
	    return rs;
	}
	
	/**
	 * 获取表对应的数据库：Oracle/SQLServer/MySQL
	 * @param tableName 必须参数，不能为空
	 * @return
	 */
	private static String getDBName(String tableName)
	{
		if (tables.containsKey(tableName.toUpperCase()))
			return tables.get(tableName.toUpperCase()).getDbName();
		return "";
	}
	
	/**
	 * 表达式，可返回简单加减乘除表达式或函数
	 * @param tableName1  表1
	 * @param fieldName1  表1的字段
	 * @param t1                        表1的别名
	 * @param tableName2  表2
	 * @param fieldName2  表2的字段
	 * @param t2                       表2的别名
	 * @param operators  运算符，可以调用常量加、减、乘、除。注意是运算符前面是fieldName1，运算符后面是fieldName2
	 * @param as         别名，如果为函数，可以增加别名
	 * @param isFunction 是否函数格式
	 * @return           规则：如果isFunction为true，返回以fn:开头，:end结尾的函数形式；否则，返回表达式
	 */
	public static String genExpression(String tableName1, String fieldName1, String t1, String tableName2, String fieldName2, String t2, String operators, String as, boolean isFunction)
	{
		/* 初始化参数 */
		t1 = t1 == null ? "" : t1.trim();
		t2 = t2 == null ? "" : t2.trim();
		as = as == null ? "" : as.trim();
		String field1 = getTargetField(tableName1, fieldName1);
		if (t1 != null && !"".equals(t1) && t1.endsWith("."))
			field1 = t1 + "." + field1;
		String field2 = getTargetField(tableName2, fieldName2);
		if (t2 != null && !"".equals(t2) && t2.endsWith("."))
			field2 = t2 + "." + field2;
		if (isFunction)
			return "fn:(" + field1 + operators + field2 + ")" + (as != null && !"".equals(as) && !as.endsWith(".") ? " as " + as + ":end" : ":end");
		return field1 + operators + field2;
	}
	
	/**
	 * 函数：to_char
	 * @param tableName 字段所属表名
	 * @param fieldName 字段名
	 * @param fmt       格式化格式，如yyyy-mm-dd
	 * @param as        别名，可以是以下三种格式：
	 *                  1. 表名或表别名.  即fieldName若是多表联合查询中某个表的字段，需要以表名或临时表名加“.”来引用，可以放在as参数里，
	 *                                   如to_char(a.start_date_time, 'yyyy-mm-dd')，其中a.即可作为as参数传入；
	 *                  2. 字段别名                 即可以为转换为函数格式后的新字段声明别名，如to_char(start_date_time, 'yyyy-mm-dd') sdt，其中sdt即可作为as参数传入；
	 *                  3. 表名.字段别名    即可以同时传入表名和字段别名，以示意方式传入，如to_char(a.start_date_time, 'yyyy-mm-dd') sdt，其中a.sdt即可作为as参数传入；
	 * @return          规则：返回以fn:开头，:end结尾的函数形式
	 */
	public static String genToChar(String tableName, String fieldName, String fmt, String as)
	{
		
		fmt = fmt == null ? DATE : fmt.trim().toLowerCase();
		as = as == null ? "" : as.trim();
		String db_name = getDBName(tableName);
		if ("Oracle".equalsIgnoreCase(db_name))
		{
			if (!"".equals(as))
			{
				// 字段别名
				if (as.indexOf(".") < 0)
				{
					return "fn:to_char(" + getTargetField(tableName, fieldName) + ", '" + fmt + "') as " + as + ":end";
				}
				else if (as.indexOf(".") > 0)
				{
					// 表名.
					if (as.endsWith("."))
						return "fn:to_char(" + as + getTargetField(tableName, fieldName) + ", '" + fmt + "'):end";
					// 表名.字段别名
					else if (as.indexOf(".") < as.length())
						return "fn:to_char(" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
							   ", '" + fmt + "') as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
				}
			}
			return "fn:to_char(" + getTargetField(tableName, fieldName) + ", '" + fmt + "'):end";
		}
		else if ("SQLServer".equalsIgnoreCase(db_name))
		{
			if (!"".equals(as))
			{
				// 字段别名
				if (as.indexOf(".") < 0)
				{
					return "fn:convert(char(" + fmt.length() + "), " + getTargetField(tableName, fieldName) + ", " + SQLServerDateFmt.get(fmt) + ") as " + as + ":end";
				}
				else if (as.indexOf(".") > 0)
				{
					// 表名.
					if (as.endsWith("."))
						return "fn:convert(char(" + fmt.length() + "), " + as + getTargetField(tableName, fieldName) + ", " + SQLServerDateFmt.get(fmt) + "):end";
					// 表名.字段别名
					else if (as.indexOf(".") < as.length())
						return "fn:convert(char(" + fmt.length() + "), " + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
							   ", " + SQLServerDateFmt.get(fmt) + ") as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
				}
			}
			return "fn:convert(char(" + fmt.length() + "), " + getTargetField(tableName, fieldName) + ", " + SQLServerDateFmt.get(fmt) + "):end";
		}
		else if ("MySQL".equalsIgnoreCase(db_name))
		{
			if (!"".equals(as))
			{
				// 字段别名
				if (as.indexOf(".") < 0)
				{
					return "fn:date_format(" + getTargetField(tableName, fieldName) + ", '" + MySQLDateFmt.get(fmt) + "') as " + as + ":end";
				}
				else if (as.indexOf(".") > 0)
				{
					// 表名.
					if (as.endsWith("."))
						return "fn:date_format(" + as + getTargetField(tableName, fieldName) + ", '" + MySQLDateFmt.get(fmt) + "'):end";
					// 表名.字段别名
					else if (as.indexOf(".") < as.length())
						return "fn:date_format(" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
							   ", '" + MySQLDateFmt.get(fmt) + "') as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
				}
			}
			return "fn:date_format(" + getTargetField(tableName, fieldName) + ", '" + MySQLDateFmt.get(fmt) + "'):end";
		}
		if (!"".equals(as))
		{
			// 字段别名
			if (as.indexOf(".") < 0)
			{
				return "fn:" + getTargetField(tableName, fieldName) + " as " + as + ":end";
			}
			else if (as.indexOf(".") > 0)
			{
				// 表名.
				if (as.endsWith("."))
					return "fn:" + as + getTargetField(tableName, fieldName) + ":end";
				// 表名.字段别名
				else
					return "fn:" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
						   " as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
			}
		}
		return "fn:" + getTargetField(tableName, fieldName) + ":end";
	}
	
	/**
	 * 函数：to_date
	 * @param tableName 字段所属表名
	 * @param fieldName 字段名
	 * @param fmt       格式化格式，如yyyy-mm-dd
	 * @param as        别名，可以是以下三种格式：
	 *                  1. 表名或表别名.  即fieldName若是多表联合查询中某个表的字段，需要以表名或临时表名加“.”来引用，可以放在as参数里，
	 *                                   如to_char(a.start_date_time, 'yyyy-mm-dd')，其中a.即可作为as参数传入；
	 *                  2. 字段别名                 即可以为转换为函数格式后的新字段声明别名，如to_char(start_date_time, 'yyyy-mm-dd') sdt，其中sdt即可作为as参数传入；
	 *                  3. 表名.字段别名    即可以同时传入表名和字段别名，以示意方式传入，如to_char(a.start_date_time, 'yyyy-mm-dd') sdt，其中a.sdt即可作为as参数传入；
	 * @return          规则：返回以fn:开头，:end结尾的函数形式
	 */
	public static String genToDate(String tableName, String fieldName, String fmt, String as)
	{
		fmt = fmt == null ? DATE : fmt.trim().toLowerCase();
		as = as == null ? "" : as.trim();
		String db_name = getDBName(tableName);
		if ("Oracle".equalsIgnoreCase(db_name))
		{
			if (!"".equals(as))
			{
				// 字段别名
				if (as.indexOf(".") < 0)
				{
					return "fn:to_date(" + getTargetField(tableName, fieldName) + ", '" + fmt + "') as " + as + ":end";
				}
				else if (as.indexOf(".") > 0)
				{
					// 表名.
					if (as.endsWith("."))
						return "fn:to_date(" + as + getTargetField(tableName, fieldName) + ", '" + fmt + "'):end";
					// 表名.字段别名
					else
						return "fn:to_date(" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
							   ", '" + fmt + "') as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
				}
			}
			return "fn:to_date(" + getTargetField(tableName, fieldName) + ", '" + fmt + "'):end";
		}
		else if ("SQLServer".equalsIgnoreCase(db_name))
		{
			if (!"".equals(as))
			{
				// 字段别名
				if (as.indexOf(".") < 0)
				{
					return "fn:convert(char(" + fmt.length() + "), " + getTargetField(tableName, fieldName) + ", " + SQLServerDateFmt.get(fmt) + ") as " + as + ":end";
				}
				else if (as.indexOf(".") > 0)
				{
					// 表名.
					if (as.endsWith("."))
						return "fn:convert(char(" + fmt.length() + "), " + as + getTargetField(tableName, fieldName) + ", " + SQLServerDateFmt.get(fmt) + "):end";
					// 表名.字段别名
					else if (as.indexOf(".") < as.length())
						return "fn:convert(char(" + fmt.length() + "), " + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
							   ", " + SQLServerDateFmt.get(fmt) + ") as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
				}
			}
			return "fn:convert(char(" + fmt.length() + "), " + getTargetField(tableName, fieldName) + ", " + SQLServerDateFmt.get(fmt) + "):end";
		}
		else if ("MySQL".equalsIgnoreCase(db_name))
		{
			if (!"".equals(as))
			{
				// 字段别名
				if (as.indexOf(".") < 0)
				{
					return "fn:str_to_date(" + getTargetField(tableName, fieldName) + ", '" + MySQLDateFmt.get(fmt) + "') as " + as + ":end";
				}
				else if (as.indexOf(".") > 0)
				{
					// 表名.
					if (as.endsWith("."))
						return "fn:str_to_date(" + as + getTargetField(tableName, fieldName) + ", '" + MySQLDateFmt.get(fmt) + "'):end";
					// 表名.字段别名
					else
						return "fn:str_to_date(" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
							   ", '" + MySQLDateFmt.get(fmt) + "') as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
				}
			}
			return "fn:str_to_date(" + getTargetField(tableName, fieldName) + ", '" + MySQLDateFmt.get(fmt) + "'):end";
		}
		if (!"".equals(as))
		{
			// 字段别名
			if (as.indexOf(".") < 0)
			{
				return "fn:" + getTargetField(tableName, fieldName) + " as " + as + ":end";
			}
			else if (as.indexOf(".") > 0)
			{
				// 表名.
				if (as.endsWith("."))
					return "fn:" + as + getTargetField(tableName, fieldName) + ":end";
				// 表名.字段别名
				else
					return "fn:" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
						   " as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
			}
		}
		return "fn:" + getTargetField(tableName, fieldName) + ":end";
	}
	
	/**
	 * 函数：to_number
	 * @param tableName 字段所属表名
	 * @param fieldName 字段名
	 * @param as        别名，可以是以下三种格式：
	 *                  1. 表名或表别名.  即fieldName若是多表联合查询中某个表的字段，需要以表名或临时表名加“.”来引用，可以放在as参数里，
	 *                                   如to_char(a.start_date_time, 'yyyy-mm-dd')，其中a.即可作为as参数传入；
	 *                  2. 字段别名                 即可以为转换为函数格式后的新字段声明别名，如to_char(start_date_time, 'yyyy-mm-dd') sdt，其中sdt即可作为as参数传入；
	 *                  3. 表名.字段别名    即可以同时传入表名和字段别名，以示意方式传入，如to_char(a.start_date_time, 'yyyy-mm-dd') sdt，其中a.sdt即可作为as参数传入；
	 * @return          规则：返回以fn:开头，:end结尾的函数形式
	 */
	public static String genToNumber(String tableName, String fieldName, String as)
	{
		/* 初始化参数 */
		as = as == null ? "" : as.trim();
		String db_name = getDBName(tableName);
		if ("Oracle".equalsIgnoreCase(db_name))
		{
			if (!"".equals(as))
			{
				// 字段别名
				if (as.indexOf(".") < 0)
				{
					return "fn:to_number(" + getTargetField(tableName, fieldName) + ") as " + as + ":end";
				}
				else if (as.indexOf(".") > 0)
				{
					// 表名.
					if (as.endsWith("."))
						return "fn:to_number(" + as + getTargetField(tableName, fieldName) + "):end";
					// 表名.字段别名
					else
						return "fn:to_number(" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
							   ") as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
				}
			}
			return "fn:to_number(" + getTargetField(tableName, fieldName) + "):end";
		}
		else if ("SQLServer".equalsIgnoreCase(db_name))
		{
			if (!"".equals(as))
			{
				// 字段别名
				if (as.indexOf(".") < 0)
				{
					return "fn:cast(" + getTargetField(tableName, fieldName) + " as float) as " + as + ":end";
				}
				else if (as.indexOf(".") > 0)
				{
					// 表名.
					if (as.endsWith("."))
						return "fn:cast(" + as + getTargetField(tableName, fieldName) + " as float):end";
					// 表名.字段别名
					else
						return "fn:cast(" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
							   " as float) as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
				}
			}
			return "fn:cast(" + getTargetField(tableName, fieldName) + " as float):end";
		}
		else if ("MySQL".equalsIgnoreCase(db_name))
		{
			if (!"".equals(as))
			{
				// 字段别名
				if (as.indexOf(".") < 0)
				{
					return "fn:cast(" + getTargetField(tableName, fieldName) + " as DECIMAL(20,2)) as " + as + ":end";
				}
				else if (as.indexOf(".") > 0)
				{
					// 表名.
					if (as.endsWith("."))
						return "fn:cast(" + as + getTargetField(tableName, fieldName) + " as DECIMAL(20,2)):end";
					// 表名.字段别名
					else
						return "fn:cast(" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
							   " as DECIMAL(20,2)) as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
				}
			}
			return "fn:cast(" + getTargetField(tableName, fieldName) + " as DECIMAL(20,2)):end";
		}
		if (!"".equals(as))
		{
			// 字段别名
			if (as.indexOf(".") < 0)
			{
				return "fn:" + getTargetField(tableName, fieldName) + " as " + as + ":end";
			}
			else if (as.indexOf(".") > 0)
			{
				// 表名.
				if (as.endsWith("."))
					return "fn:" + as + getTargetField(tableName, fieldName) + ":end";
				// 表名.字段别名
				else
					return "fn:" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
						   " as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
			}
		}
		return "fn:" + getTargetField(tableName, fieldName) + ":end";
	}
	
	/**
	 * 函数：sum
	 * @param tableName 字段所属表名
	 * @param fieldName 字段名
	 * @param as        别名，可以是以下三种格式：
	 *                  1. 表名或表别名.  即fieldName若是多表联合查询中某个表的字段，需要以表名或临时表名加“.”来引用，可以放在as参数里，
	 *                                   如to_char(a.start_date_time, 'yyyy-mm-dd')，其中a.即可作为as参数传入；
	 *                  2. 字段别名                 即可以为转换为函数格式后的新字段声明别名，如to_char(start_date_time, 'yyyy-mm-dd') sdt，其中sdt即可作为as参数传入；
	 *                  3. 表名.字段别名    即可以同时传入表名和字段别名，以示意方式传入，如to_char(a.start_date_time, 'yyyy-mm-dd') sdt，其中a.sdt即可作为as参数传入；
	 * @return          规则：返回以fn:开头，:end结尾的函数形式
	 */
	public static String genSum(String tableName, String fieldName, String as)
	{
		/* 初始化参数 */
		as = as == null ? "" : as.trim();
		if (!"".equals(as))
		{
			// 字段别名
			if (as.indexOf(".") < 0)
			{
				return "fn:sum(" + getTargetField(tableName, fieldName) + ") as " + as + ":end";
			}
			else if (as.indexOf(".") > 0)
			{
				// 表名.
				if (as.endsWith("."))
					return "fn:sum(" + as + getTargetField(tableName, fieldName) + "):end";
				// 表名.字段别名
				else
					return "fn:sum(" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
						   ") as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
			}
		}
		return "fn:sum(" + getTargetField(tableName, fieldName) + "):end";
	}
	
	/**
	 * 函数：count
	 * @param tableName 字段所属表名
	 * @param fieldName 字段名
	 * @param as        别名，可以是以下三种格式：
	 *                  1. 表名或表别名.  即fieldName若是多表联合查询中某个表的字段，需要以表名或临时表名加“.”来引用，可以放在as参数里，
	 *                                   如to_char(a.start_date_time, 'yyyy-mm-dd')，其中a.即可作为as参数传入；
	 *                  2. 字段别名                 即可以为转换为函数格式后的新字段声明别名，如to_char(start_date_time, 'yyyy-mm-dd') sdt，其中sdt即可作为as参数传入；
	 *                  3. 表名.字段别名    即可以同时传入表名和字段别名，以示意方式传入，如to_char(a.start_date_time, 'yyyy-mm-dd') sdt，其中a.sdt即可作为as参数传入；
	 * @return          规则：返回以fn:开头，:end结尾的函数形式
	 */
	public static String genCount(String tableName, String fieldName, String as)
	{
		/* 初始化参数 */
		as = as == null ? "" : as.trim();
		if (!"".equals(as))
		{
			// 字段别名
			if (as.indexOf(".") < 0)
			{
				return "fn:count(" + getTargetField(tableName, fieldName) + ") as " + as + ":end";
			}
			else if (as.indexOf(".") > 0)
			{
				// 表名.
				if (as.endsWith("."))
					return "fn:count(" + as + getTargetField(tableName, fieldName) + "):end";
				// 表名.字段别名
				else
					return "fn:count(" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
						   ") as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
			}
		}
		return "fn:count(" + getTargetField(tableName, fieldName) + "):end";
	}
	
	/**
	 * 函数：floor
	 * @param tableName 字段所属表名
	 * @param fieldName 字段名
	 * @param as        别名，可以是以下三种格式：
	 *                  1. 表名或表别名.  即fieldName若是多表联合查询中某个表的字段，需要以表名或临时表名加“.”来引用，可以放在as参数里，
	 *                                   如to_char(a.start_date_time, 'yyyy-mm-dd')，其中a.即可作为as参数传入；
	 *                  2. 字段别名                 即可以为转换为函数格式后的新字段声明别名，如to_char(start_date_time, 'yyyy-mm-dd') sdt，其中sdt即可作为as参数传入；
	 *                  3. 表名.字段别名    即可以同时传入表名和字段别名，以示意方式传入，如to_char(a.start_date_time, 'yyyy-mm-dd') sdt，其中a.sdt即可作为as参数传入；
	 * @return          规则：返回以fn:开头，:end结尾的函数形式
	 */
	public static String genFloor(String tableName, String fieldName, String as)
	{
		/* 初始化参数 */
		as = as == null ? "" : as.trim();
		if (!"".equals(as))
		{
			// 字段别名
			if (as.indexOf(".") < 0)
			{
				return "fn:floor(" + getTargetField(tableName, fieldName) + ") as " + as + ":end";
			}
			else if (as.indexOf(".") > 0)
			{
				// 表名.
				if (as.endsWith("."))
					return "fn:floor(" + as + getTargetField(tableName, fieldName) + "):end";
				// 表名.字段别名
				else
					return "fn:floor(" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
						   ") as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
			}
		}
		return "fn:floor(" + getTargetField(tableName, fieldName) + "):end";
	}
	
	/**
	 * 函数：trunc
	 * @param tableName 字段所属表名
	 * @param fieldName 字段名
	 * @param as        别名，可以是以下三种格式：
	 *                  1. 表名或表别名.  即fieldName若是多表联合查询中某个表的字段，需要以表名或临时表名加“.”来引用，可以放在as参数里，
	 *                                   如to_char(a.start_date_time, 'yyyy-mm-dd')，其中a.即可作为as参数传入；
	 *                  2. 字段别名                 即可以为转换为函数格式后的新字段声明别名，如to_char(start_date_time, 'yyyy-mm-dd') sdt，其中sdt即可作为as参数传入；
	 *                  3. 表名.字段别名    即可以同时传入表名和字段别名，以示意方式传入，如to_char(a.start_date_time, 'yyyy-mm-dd') sdt，其中a.sdt即可作为as参数传入；
	 * @return          规则：返回以fn:开头，:end结尾的函数形式
	 */
	public static String genTrunc(String tableName, String fieldName, String as)
	{
		as = as == null ? "" : as.trim();
		String db_name = getDBName(tableName);
		if ("Oracle".equalsIgnoreCase(db_name))
		{
			if (!"".equals(as))
			{
				// 字段别名
				if (as.indexOf(".") < 0)
				{
					return "fn:trunc(" + getTargetField(tableName, fieldName) + ") as " + as + ":end";
				}
				else if (as.indexOf(".") > 0)
				{
					// 表名.
					if (as.endsWith("."))
						return "fn:trunc(" + as + getTargetField(tableName, fieldName) + "):end";
					// 表名.字段别名
					else
						return "fn:trunc(" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
							   ") as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
				}
			}
			return "fn:trunc(" + getTargetField(tableName, fieldName) + "):end";
		}
		else if ("SQLServer".equalsIgnoreCase(db_name))
		{
			if (!"".equals(as))
			{
				// 字段别名
				if (as.indexOf(".") < 0)
				{
					return "fn:convert(char(" + DATE.length() + "), " + getTargetField(tableName, fieldName) + ", " + SQLServerDateFmt.get(DATE) + ") as " + as + ":end";
				}
				else if (as.indexOf(".") > 0)
				{
					// 表名.
					if (as.endsWith("."))
						return "fn:convert(char(" + DATE.length() + "), " + as + getTargetField(tableName, fieldName) + ", " + SQLServerDateFmt.get(DATE) + "):end";
					// 表名.字段别名
					else if (as.indexOf(".") < as.length())
						return "fn:convert(char(" + DATE.length() + "), " + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
							   ", " + SQLServerDateFmt.get(DATE) + ") as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
				}
			}
			return "fn:convert(char(" + DATE.length() + "), " + getTargetField(tableName, fieldName) + ", " + SQLServerDateFmt.get(DATE) + "):end";
		}
		else if ("MySQL".equalsIgnoreCase(db_name))
		{
			if (!"".equals(as))
			{
				// 字段别名
				if (as.indexOf(".") < 0)
				{
					return "fn:str_to_date(" + getTargetField(tableName, fieldName) + ", '" + MySQLDateFmt.get(DATE) + "') as " + as + ":end";
				}
				else if (as.indexOf(".") > 0)
				{
					// 表名.
					if (as.endsWith("."))
						return "fn:str_to_date(" + as + getTargetField(tableName, fieldName) + ", '" + MySQLDateFmt.get(DATE) + "'):end";
					// 表名.字段别名
					else
						return "fn:str_to_date(" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
							   ", '" + MySQLDateFmt.get(DATE) + "') as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
				}
			}
			return "fn:str_to_date(" + getTargetField(tableName, fieldName) + ", '" + MySQLDateFmt.get(DATE) + "'):end";
		}
		if (!"".equals(as))
		{
			// 字段别名
			if (as.indexOf(".") < 0)
			{
				return "fn:" + getTargetField(tableName, fieldName) + " as " + as + ":end";
			}
			else if (as.indexOf(".") > 0)
			{
				// 表名.
				if (as.endsWith("."))
					return "fn:" + as + getTargetField(tableName, fieldName) + ":end";
				// 表名.字段别名
				else
					return "fn:" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
						   " as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
			}
		}
		return "fn:" + getTargetField(tableName, fieldName) + ":end";
	}
	
	/**
	 * Oracle函数：max
	 * @param tableName 字段所属表名
	 * @param fieldName 字段名
	 * @param as        别名，可以是以下三种格式：
	 *                  1. 表名或表别名.  即fieldName若是多表联合查询中某个表的字段，需要以表名或临时表名加“.”来引用，可以放在as参数里，
	 *                                   如to_char(a.start_date_time, 'yyyy-mm-dd')，其中a.即可作为as参数传入；
	 *                  2. 字段别名                 即可以为转换为函数格式后的新字段声明别名，如to_char(start_date_time, 'yyyy-mm-dd') sdt，其中sdt即可作为as参数传入；
	 *                  3. 表名.字段别名    即可以同时传入表名和字段别名，以示意方式传入，如to_char(a.start_date_time, 'yyyy-mm-dd') sdt，其中a.sdt即可作为as参数传入；
	 * @return          规则：返回以fn:开头，:end结尾的函数形式
	 */
	public static String genMax(String tableName, String fieldName, String as)
	{
		/* 初始化参数 */
		as = as == null ? "" : as.trim();
		if (!"".equals(as))
		{
			// 字段别名
			if (as.indexOf(".") < 0)
			{
				return "fn:max(" + getTargetField(tableName, fieldName) + ") as " + as + ":end";
			}
			else if (as.indexOf(".") > 0)
			{
				// 表名.
				if (as.endsWith("."))
					return "fn:max(" + as + getTargetField(tableName, fieldName) + "):end";
				// 表名.字段别名
				else
					return "fn:max(" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
						   ") as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
			}
		}
		return "fn:max(" + getTargetField(tableName, fieldName) + "):end";
	}
	
	/**
	 * Oracle函数：min
	 * @param tableName 字段所属表名
	 * @param fieldName 字段名
	 * @param as        别名，可以是以下三种格式：
	 *                  1. 表名或表别名.  即fieldName若是多表联合查询中某个表的字段，需要以表名或临时表名加“.”来引用，可以放在as参数里，
	 *                                   如to_char(a.start_date_time, 'yyyy-mm-dd')，其中a.即可作为as参数传入；
	 *                  2. 字段别名                 即可以为转换为函数格式后的新字段声明别名，如to_char(start_date_time, 'yyyy-mm-dd') sdt，其中sdt即可作为as参数传入；
	 *                  3. 表名.字段别名    即可以同时传入表名和字段别名，以示意方式传入，如to_char(a.start_date_time, 'yyyy-mm-dd') sdt，其中a.sdt即可作为as参数传入；
	 * @return          规则：返回以fn:开头，:end结尾的函数形式
	 */
	public static String genMin(String tableName, String fieldName, String as)
	{
		/* 初始化参数 */
		as = as == null ? "" : as.trim();
		if (!"".equals(as))
		{
			// 字段别名
			if (as.indexOf(".") < 0)
			{
				return "fn:min(" + getTargetField(tableName, fieldName) + ") as " + as + ":end";
			}
			else if (as.indexOf(".") > 0)
			{
				// 表名.
				if (as.endsWith("."))
					return "fn:min(" + as + getTargetField(tableName, fieldName) + "):end";
				// 表名.字段别名
				else
					return "fn:min(" + as.substring(0, as.indexOf(".") + 1) + getTargetField(tableName, fieldName) + 
						   ") as " + as.substring(as.indexOf(".") + 1, as.length()) + ":end";
			}
		}
		return "fn:min(" + getTargetField(tableName, fieldName) + "):end";
	}
	
	/**
	 * 过滤 Sql语句中
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	public static String genRToDate(String tableName,String fieldName,String value ,String fmt)
	{
	    fmt = fmt == null ? DATE : fmt.trim().toLowerCase();
        String db_name = getDBName(tableName);
        if ("Oracle".equalsIgnoreCase(db_name))
        {
            return " to_date(" + value + ", '" + fmt + "') ";
        }
        else if ("SQLServer".equalsIgnoreCase(db_name))
        {
            return " convert(char(" + fmt.length() + "), " + value + ", " + SQLServerDateFmt.get(fmt) + ")";
        }
        else if ("MySQL".equalsIgnoreCase(db_name))
        {
            return "str_to_date(" + value + ", '" + MySQLDateFmt.get(fmt) + "') ";
        }
        return value;
	}
	
	/**
	 * 
	 * @param tableName
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public static String genRToNumber(String tableName , String fieldName , String value )
	{
        String db_name = getDBName(tableName);
        if ("Oracle".equalsIgnoreCase(db_name))
        {
            return "to_number(" + value + ")";
        }
        else if ("SQLServer".equalsIgnoreCase(db_name))
        {
            return "cast(" + value + " as float)";
        }
        else if ("MySQL".equalsIgnoreCase(db_name))
        {
            return "cast(" + value + " as DECIMAL(20,2))";
        }
        return value;
	}

}