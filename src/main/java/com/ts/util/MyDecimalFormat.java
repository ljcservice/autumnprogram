package com.ts.util;

import java.text.DecimalFormat;

public class MyDecimalFormat {
	private static DecimalFormat decimalFormat = new DecimalFormat("#,###,##0.00");
	
	public static String format(Double b){
		if(b==null){
			return "0";
		}
		return decimalFormat.format(b);
	}
	public static void main(String[] args) {
		System.out.println(format(new Double(0)));
	}
}
