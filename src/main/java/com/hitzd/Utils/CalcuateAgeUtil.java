package com.hitzd.Utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.hitzd.his.Utils.Config;

public class CalcuateAgeUtil 
{
    /**
     * 计算出院天数 
     * @param startDate 
     * @param endDate
     * @return
     */
	public static double getQuot(String startDate, String endDate)
	{
		if(null == startDate || "".equals(startDate) || null == endDate || "".equals(endDate)) {
			return 0;
		}
		long quot = 0;
		double dayVal = 0;
		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		/* 按整天或 按小时计算 天数 */
		if("days".equals(Config.getParamValue("days")))
			dateFormat = "yyyy-MM-dd";
		SimpleDateFormat ft = new SimpleDateFormat(dateFormat);
		try
		{
			Date date1 = ft.parse(startDate);
			Date date2 = ft.parse(endDate);
			quot = date2.getTime() - date1.getTime();
			dayVal = Double.parseDouble(quot + "");
			dayVal = dayVal / 1000 / 60 / 60 / 24 + 1;//需要加上一天
		} 
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return dayVal;
	}

	/**
	 * 计算患者年龄年龄
	 * @param birthDate
	 * @param someDate
	 * @param strFormat
	 * @return
	 */
	public static String calcuateAgeByTwoDates(String birthDate,String someDate, String strFormat) 
	{
		if (null == birthDate || "".equals(birthDate)) 
		{
			return null;
		}
		String result = "";
		try 
		{
			if (null == someDate || "".equals(someDate)) 
			{
				result = calcuateAge(parseDate(birthDate, strFormat),new Date());
			} 
			else
			{
				result = calcuateAge(parseDate(birthDate, strFormat),parseDate(someDate, strFormat));
			}
		} 
		catch (Exception e)
		{ 
		    e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param birthDay
	 * @param someDay
	 * @return
	 * @throws Exception
	 */
	private static String calcuateAge(Date birthDay, Date someDay)	throws Exception
	{
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthDay))
		{
			throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
		}
		cal.setTime(someDay);
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
		int age = yearNow - yearBirth;
		if (monthNow <= monthBirth) 
		{
			if (monthNow == monthBirth) 
			{
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth)
				{
					age--;
				}
			} 
			else
			{
				// monthNow>monthBirth
				age--;
			}
		}
		return age + "";
	}

	private static Date parseDate(String dateValue, String strFormat)
	{
		if (dateValue == null || "".endsWith(dateValue))
			return null;
		if (strFormat == null || "".equals(strFormat))
			strFormat = "yyyy-MM-dd";
		SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
		Date newDate = null;
		try 
		{
			newDate = dateFormat.parse(dateValue);
		} 
		catch (ParseException pe) 
		{
		    pe.printStackTrace();
			newDate = null;
		}
		return newDate;
	}
}
