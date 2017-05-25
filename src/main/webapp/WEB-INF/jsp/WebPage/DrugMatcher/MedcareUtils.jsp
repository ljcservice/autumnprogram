<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%!
	String getMedcareValue(int pos, String medcareStr)
	{
		if (medcareStr != null && medcareStr.length() >= pos)
			return medcareStr.substring(pos - 1, pos);
		return "";
	}
%>