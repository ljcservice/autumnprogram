package com.hitzd.his.Utils;

import java.util.Calendar;

public class IDGenerator 
{
	@SuppressWarnings("static-access")
	public static synchronized String getDateTimeAsID()
	{
		try 
		{
			Thread.currentThread().sleep(1);
		}
		catch (java.lang.InterruptedException e)
		{
		} // ignore exception

        Calendar now = Calendar.getInstance();    //取得系统时间
        int year, month, date;
        int h, m, s, ms;
        year  = now.get(Calendar.YEAR);            //取得 YEAR 字段的值
        month = now.get(Calendar.MONTH) + 1;       //取得 MONTH 字段的值
        date  = now.get(Calendar.DATE);            //取得 DATE 字段的值
        h     = now.get(Calendar.HOUR_OF_DAY);
        m     = now.get(Calendar.MINUTE);
        s     = now.get(Calendar.SECOND);
        ms    = now.get(Calendar.MILLISECOND);

        String smonth = month < 10 ? "0" + month : "" + month;
        String sdate  = date < 10 ? "0" + date : "" + date;
        String sh     = h < 10 ? "0" + h : "" + h;
        String sm     = m < 10 ? "0" + m : "" + m;
        String ss     = s < 10 ? "0" + s : "" + s;
        String sms    = "";
        if (ms < 10)
        {
            sms = "00" + ms;
        }
        else
        if (ms < 100)
        {
            sms = "0" + ms;
        }
        else
        {
            sms = "" + ms;
        }
        String CurDate = "";
        CurDate += year;
        CurDate += smonth;
        CurDate += sdate;
        CurDate += sh;
        CurDate += sm;
        CurDate += ss;
        CurDate += sms;
        return CurDate;
	}

}
