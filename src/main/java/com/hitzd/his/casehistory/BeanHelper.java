package com.hitzd.his.casehistory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
//import org.apache.commons.beanutils.PropertyUtils;

public class BeanHelper {

	// 属性复制中排除在外的属性名称
	private static String[] exceptString = { "class", "value",
			"servletWrapper", "servlet", "multipartRequestHandler" };
	/**
	 * 复制相同名称属性，支持数据类型自动转换 String to Integer 使用方法 StrToInteger(String s) Integer
	 * to String 使用方法 ObjectToString(Object src) String to Long 使用方法
	 * StrToLong(String s) Long to String 使用方法 ObjectToString(Object src) String
	 * to Double 使用方法 StrToDouble(String s) Double to String 使用方法
	 * ObjectToString(Object src) Sting to Date 使用方法 StringtoDate(String date)
	 * 默认转换形式yyyy-MM-dd，可以通过setDateformat(String dateformat)形式更改转换格式
	 * Util.datefmt;默认格式 Util.datetimefmt;日期时间格式 Date to String 使用方法
	 * DatetoString(Date dt) 转换格式同上
	 * 
	 * @param src
	 *            源对象
	 * @param dest
	 *            目标对象 1.PropertyDescriptor[] 描述 Java Bean 通过一对存储器方法导出的一个属性
	 *            2.pdsrc[i].getPropertyType() 获取 java Bean 的属性类型
	 *            3.pddest[j].getName() 获取 java Bean 的属性名称
	 *            4.pdsrc[i].getPropertyType().getName() 获取 java Bean 的属性类型名称
	 *            5.IllegalAccessException异常
	 *            当应用程序试图创建一个实例（而不是数组）、设置或获取一个字段，或者调用一个方法
	 *            ，但当前正在执行的方法无法访问指定类、字段、方法或构造方法的定义时，抛出 IllegalAccessException。
	 *            6.InvocationTargetException异常 是一种包装由调用方法或构造方法所抛出异常的经过检查的异常。
	 *            7.NoSuchMethodException异常 无法找到某一特定方法时，抛出该异常
	 *            8.PropertyUtils.getPropertyDescriptors(对象)
	 *            将对象的属性返回成PropertyDescriptor[]数组
	 *            9.PropertyUtils.setProperty(对象，对象属性名称，对象属性值)
	 *            //将参数中的对象属性值赋进参数中的对象属性中
	 *            10.PropertyUtils.getProperty(对象，对象属性名称) //获取指定对象属性里的值
	 * 
	 */
	public void copySameProperties(Object src, Object dest) {
		/*
		boolean test = true;
		PropertyDescriptor[] pdsrc = PropertyUtils.getPropertyDescriptors(src);
		PropertyDescriptor[] pddest = PropertyUtils
				.getPropertyDescriptors(dest);
		// 将源对象属性循环
		for (int i = 0; i < pdsrc.length; i++) {
			// 不在复制目标内的属性名称
			for (int es = 0; es < exceptString.length; es++) {
				// 如果当前对象的属性等于，不在复制目标内的。
				// test为假，不进行复制操作。并跳出循环，反之TEST为真
				if (pdsrc[i].getName().equals(exceptString[es])) {
					test = false;
					break;
				} else
					test = true;
			}
			// 如果test为真的话进行属性值复制操作
			if (test) {
				// 进行目标属性循环
				for (int j = 0; j < pddest.length; j++) {
					// 如果源对象属性名与目标属性名相等。进行下一步判断
					if (pdsrc[i].getName().equals(pddest[j].getName())) {
						// 如果源对象属性与目标属性类型相等。进行下一步操作
						if (pdsrc[i].getPropertyType() == pddest[j]
								.getPropertyType()) {
							// 将源对像属性值复制到目标对象属性值里面
							try {
								PropertyUtils.setProperty(
										dest,
										pddest[j].getName(),
										PropertyUtils.getProperty(src,
												pdsrc[i].getName()));
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							}
							// 如果源对象属性与目标属性类型不相等 ，进行属性类型转换为相等
						} else {
							try {
								// 如果源对象属性类型名是日期型并且目标对象属性类型名是字符串型
								if (pdsrc[i].getPropertyType().getName()
										.equals("java.util.Date")
										&& pddest[j].getPropertyType()
												.getName()
												.equals("java.lang.String"))
									// 将转换为字符串型的源对像属性值复制到目标对象属性值里面
									PropertyUtils
											.setProperty(
													dest,
													pddest[j].getName(),
													DatetoString((Date) PropertyUtils
															.getProperty(
																	src,
																	pdsrc[i].getName())));
								// 如果源对象属性类型名是字符串型并且目标对象属性类型名是日期型
								if (pdsrc[i].getPropertyType().getName()
										.equals("java.lang.String")
										&& pddest[j].getPropertyType()
												.getName()
												.equals("java.util.Date"))
									// 将转换为日期型的源对像属性值复制到目标对象属性值里面
									PropertyUtils
											.setProperty(
													dest,
													pddest[j].getName(),
													StringtoDate((String) PropertyUtils
															.getProperty(
																	src,
																	pdsrc[i].getName())));
								// 如果源对象属性类型名是浮点型并且目标对象属性类型名是字符串型
								if (pdsrc[i].getPropertyType().getName()
										.equals("java.lang.Double")
										&& pddest[j].getPropertyType()
												.getName()
												.equals("java.lang.String"))
									// 将转换为字符串型的源对像属性值复制到目标对象属性值里面
									PropertyUtils
											.setProperty(
													dest,
													pddest[j].getName(),
													ObjectToString(PropertyUtils
															.getProperty(
																	src,
																	pdsrc[i].getName())));
								// 如果源对象属性类型名是字符串型并且目标对象属性类型名是浮点型
								if (pdsrc[i].getPropertyType().getName()
										.equals("java.lang.String")
										&& pddest[j].getPropertyType()
												.getName()
												.equals("java.lang.Double"))
									// 将转换为浮点型的源对像属性值复制到目标对象属性值里面
									PropertyUtils
											.setProperty(
													dest,
													pddest[j].getName(),
													StrToDouble((String) PropertyUtils
															.getProperty(
																	src,
																	pdsrc[i].getName())));
								// 如果源对象属性类型名是整型并且目标对象属性类型名是字符串型
								if (pdsrc[i].getPropertyType().getName()
										.equals("java.lang.Integer")
										&& pddest[j].getPropertyType()
												.getName()
												.equals("java.lang.String"))
									// 将转换为字符串型的源对像属性值复制到目标对象属性值里面
									PropertyUtils
											.setProperty(
													dest,
													pddest[j].getName(),
													ObjectToString(PropertyUtils
															.getProperty(
																	src,
																	pdsrc[i].getName())));
								// 如果源对象属性类型名是字符串型并且目标对象属性类型名是整型
								if (pdsrc[i].getPropertyType().getName()
										.equals("java.lang.String")
										&& pddest[j].getPropertyType()
												.getName()
												.equals("java.lang.Integer"))
									// 将转换为整型的源对像属性值复制到目标对象属性值里面
									PropertyUtils
											.setProperty(
													dest,
													pddest[j].getName(),
													StrToInteger((String) PropertyUtils
															.getProperty(
																	src,
																	pdsrc[i].getName())));
								// 如果源对象属性类型名是长整型并且目标对象属性类型名是字符串型
								if (pdsrc[i].getPropertyType().getName()
										.equals("java.lang.Long")
										&& pddest[j].getPropertyType()
												.getName()
												.equals("java.lang.String"))
									// 将转换为字符串型的源对像属性值复制到目标对象属性值里面
									PropertyUtils
											.setProperty(
													dest,
													pddest[j].getName(),
													ObjectToString(PropertyUtils
															.getProperty(
																	src,
																	pdsrc[i].getName())));
								// 如果源对象属性类型名是字符串型并且目标对象属性类型名是长整型
								if (pdsrc[i].getPropertyType().getName()
										.equals("java.lang.String")
										&& pddest[j].getPropertyType()
												.getName()
												.equals("java.lang.Long"))
									// 将转换为长整型的源对像属性值复制到目标对象属性值里面
									PropertyUtils
											.setProperty(
													dest,
													pddest[j].getName(),
													StrToLong((String) PropertyUtils
															.getProperty(
																	src,
																	pdsrc[i].getName())));
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		*/
	}

	@SuppressWarnings("rawtypes")
	public void copyMapToBean(HashMap src, Object dest) {
		/*
		PropertyDescriptor[] pddest = PropertyUtils
				.getPropertyDescriptors(dest);

		for (int j = 0; j < pddest.length; j++) {
			String value = (String) src.get(pddest[j].getName());
			if (pddest[j].getPropertyType().getName()
					.equals("java.lang.String")) {
				try {
					PropertyUtils.setProperty(dest, pddest[j].getName(), value);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			} else {
				try {
					if (pddest[j].getPropertyType().getName()
							.equals("java.util.Date"))
						PropertyUtils.setProperty(dest, pddest[j].getName(),
								StringtoDate(value));
					if (pddest[j].getPropertyType().getName()
							.equals("java.lang.Double"))
						PropertyUtils.setProperty(dest, pddest[j].getName(),
								StrToDouble(value));
					if (pddest[j].getPropertyType().getName()
							.equals("java.lang.Integer"))
						PropertyUtils.setProperty(dest, pddest[j].getName(),
								StrToInteger(value));
					if (pddest[j].getPropertyType().getName()
							.equals("java.lang.Long"))
						PropertyUtils.setProperty(dest, pddest[j].getName(),
								StrToLong(value));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
		*/
	}

	/**
	 * 默认日期转换格式
	 */
	public static String datefmt = "yyyy-MM-dd";
	/**
	 * 日期时间格式
	 */
	public static String datetimefmt = "yyyy-MM-dd hh:mm:ss";

	public String dateformat = datefmt;

	/**
	 * 日期转换为字符串
	 * 
	 * @param dt
	 *            日期型参数
	 */
	public String DatetoString(Date dt) {
		if (dt == null) {
			return "";
		}
		SimpleDateFormat sf = new SimpleDateFormat(dateformat);
		return sf.format(dt);
	}

	/**
	 * 字符串转换为Date
	 * 
	 * @param date
	 *            String型参数
	 */
	public Date StringtoDate(String date) {
		try {
			if (date == null || date.trim().length() == 0) {
				return null;
			}
			SimpleDateFormat df = new SimpleDateFormat(dateformat);
			return df.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 字符串转换为整形
	 * 
	 * @param s
	 *            字符型参数
	 */
	public Integer StrToInteger(String s) {
		try {
			return Integer.valueOf(s);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 字符串转换为长整形
	 * 
	 * @param s
	 *            字符型参数
	 */
	public Long StrToLong(String s) {
		try {
			return Long.valueOf(s);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 字符串转换为双精度型
	 * 
	 * @param s
	 *            字符型参数
	 */
	public Double StrToDouble(String s) {
		try {
			return Double.valueOf(s);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * object转换为Sting
	 * 
	 * @param src
	 *            对象参数
	 */
	public String ObjectToString(Object src) {
		if (src != null)
			return String.valueOf(src);
		else
			return null;
	}

	/**
	 * 返回日期转换格式
	 * 
	 * @param dateformat
	 */
	public String getDateformat() {
		return dateformat;
	}

	/**
	 * 设置日期转换格式
	 * 
	 * @param dateformat
	 */
	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}

	/**
	 * pojo对象名称与属性值输出
	 * 
	 * @param obj
	 *            pojo对象或存放pojo对象的list
	 * @param wholeWord
	 *            整字输出，ture是，false否，选择false只输出前30个文字
	 */
	@SuppressWarnings("rawtypes")
	public static void logOut(Object obj, boolean wholeWord) {
		// 如果对象不为空的话进行下一部操作
		if (obj != null) {
			// 初使化标识
			boolean showTitle = true;
			// 如果对象的类型名称是集合的话，进行下一步操作
			if (obj.getClass().getName().equals("java.util.ArrayList")
					|| obj.getClass().getName().equals("java.util.List")) {
				showTitle = true;
				// 将对象转成集合
				List list = (List) obj;
				// 迭带集合
				for (int i = 0; i < list.size(); i++) {
					// 将迭带出来的元素存入object对象中去
					Object o = list.get(i);
					// 运行organizStr（object对象,输入长度标识，标题标识）输出对象内容
					organizStr(o, wholeWord, showTitle);
					showTitle = false;
				}
			}
			// 否则 如果对象的类型名称是 map
			else if (obj.getClass().getName().equals("java.util.HashSet")) {
				showTitle = true;
				// 将对象转成set集合
				Set set = (Set) obj;
				// 将set集合转成 名为 it的 迭带器
				Iterator it = set.iterator();
				// 迭带迭带器
				while (it.hasNext()) {
					// 将迭带出来的元素存入object对象中去
					Object o = it.next();
					// 运行organizStr（object对象,输入长度标识，标题标识）输出对象内容
					organizStr(o, wholeWord, showTitle);
					showTitle = false;
				}
				// 否则 运行organizStr（object对象,输入长度标识，标题标识）输出对象内容
			} else {
				organizStr(obj, wholeWord, true);
			}
		}
	}

	/**
	 * 组织输出字符串
	 * 
	 * @param o
	 *            要进行输出的对象
	 * @param wholeWord
	 *            整字输出，ture是，false否，选择false只输出前30个文字
	 * @param showTitle
	 *            是否输出类名和pojo标题
	 */
	private static void organizStr(Object o, boolean wholeWord,
			boolean showTitle) {
		/*
		String result = "";
		String key = "";
		String value = "";
		// 将对象的属性转换成属性数组
		PropertyDescriptor[] pdout = PropertyUtils.getPropertyDescriptors(o);
		// 如果标识为真。。。。。。。打印对象类型名称
		//if (showTitle)
			//log.info("**********************" + o.getClass().getName()
			//		+ "*************************" + showTitle);
		// 初始化标识
		boolean test = true;
		// 初始化集合
		List<Object> nextlist = new ArrayList<Object>();
		// 循环属性数组
		for (int j = 0; j < pdout.length; j++) {
			// 循环不在打印属性的字符串数组
			for (int es = 0; es < exceptString.length; es++) {
				// 如果属性名称等于不打印的属性名称,标识为假并退了。否则标识为真
				if (pdout[j].getName().equals(exceptString[es])) {
					test = false;
					break;
				} else
					test = true;
			}
			// 如果标识为真
			if (test) {
				// 如果标题标识为真
				if (showTitle) {
					// 将属性名称追加字符串变量里
					key += addorSubKey(pdout[j].getName(), wholeWord) + "|";
				}
				try {
					// 将属性值 存放于object变量里
					Object getobj = PropertyUtils.getProperty(o,
							pdout[j].getName());
					// 将属性值 追加字符中变量里
					value += addorSubValue(getobj, wholeWord) + "|";
					// 如果object对象不为空的 进行下一步操作
					if (getobj != null) {
						// 将object 对象的类型名称放入字符串变量里
						String type = getobj.getClass().getName();
						// 字符串变量的值如果包含 HashSet 或 HashSet或 ArrayList 进行下一步操作
						if (type.indexOf("HashSet") != -1
								|| type.indexOf("HashSet") != -1
								|| type.indexOf("ArrayList") != -1) {
							// 如果object对象是集合的话。置入集合中
							nextlist.add(getobj);
						}
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}

			}
		}
		// 字符串换行
		key += "\n";
		// 获取字符串的长度值
		int kl = key.length();
		// 循环字符串的长度
		for (int lenKey = 0; lenKey < kl; lenKey++) {
			// 在字符串变量中追 加 -
			key += "-";
		}
		// 如果标题标识为真
		if (showTitle) {
			// 字符串变量 追加 换行追加 属性名字符串变量 追加 换行 追加 属性值字符串变量
			result += "\n" + key + "\n" + value;
		} else {
			// 字符串变量 追加 属性值字符串变量
			result += value;
		}
		// System.out.println(result);
		// 迭带集合
		for (int n = 0; n < nextlist.size(); n++) {
			// 将迭带元素存入object对象中
			Object nextobj = nextlist.get(n);
			// logOut(对象名 , 长度限制标识)
			logOut(nextobj, wholeWord);
		}
		*/
	}

	/**
	 * pojo对象属性名添加空格与分隔符
	 * 
	 * @param str
	 *            要输出的字符串
	 * @param wholeWord
	 *            整字输出，ture是，false否，选择false只输出前30个文字
	 */
	private static String addorSubKey(Object str, boolean wholeWord) {

		int showLength = 30;
		String result = "";

		result = String.valueOf(str);
		int len = result.length();
		if (len < showLength) {
			for (int i = 0; i < (showLength - len) / 2; i++) {
				result = " " + result;
			}

			for (int i = 0; i < (showLength - len) / 2 + 1; i++) {
				result += " ";
			}
			int oddOrEven = (showLength - len) % 2;
			if (oddOrEven == 1) {
				result += " ";
			}
		} else if (len > showLength) {
			if (!wholeWord) {
				result = result.substring(0, showLength + 1);
			}
		}

		return result;
	}

	/**
	 * pojo对象属性值添加空格与分隔符
	 * 
	 * @param str
	 *            要输出的字符串
	 * @param wholeWord
	 *            整字输出，ture是，false否，选择false只输出前30个文字
	 */
	private static String addorSubValue(Object str, boolean wholeWord) {

		int showLength = 30;
		String result = "";

		if (str != null) {
			result = String.valueOf(str);

			int len = result.length();
			if (len < showLength) {
				for (int i = 0; i <= showLength - len; i++) {
					result += " ";
				}
			} else if (len > showLength) {
				if (!wholeWord) {
					result = result.substring(0, showLength + 1);
				}
			}
		} else {
			result += "<NULL>";
			for (int i = 6; i <= showLength; i++) {
				result += " ";
			}
		}

		return result;
	}

	public static void main(String s[]) 
	{
		/*
		PatVisit pmi = new PatVisit();
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(pmi);
		for (PropertyDescriptor pd : pds)
		{
			//if (pd.isPreferred())
				System.out.println(pd.getDisplayName() + " " + pd.getName() + " " + pd.getShortDescription());
		}
		*/
	}
}