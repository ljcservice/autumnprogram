package com.ts.util;
public class MiUtils
{
	public static String getMedcareValue(int pos, String medcareStr)
	{
		if (medcareStr != null && medcareStr.length() >= pos)
			return medcareStr.substring(pos - 1, pos);
		return "";
	}
}