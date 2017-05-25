package com.ts.util;
/**
 * 主要翻译科室信息
 * @author dcdt
 *
 */
public class DeptUtils {
	/* CLINIC_ATTR */
	private static final String CA0 = "临床";
	private static final String CA1 = "辅诊";
	private static final String CA2 = "护理单元";
	private static final String CA3 = "机关";
	private static final String CA9 = "其他";
	/* OUTP_OR_INP */
	private static final String OI0 = "门诊";
	private static final String OI1 = "住院";
	private static final String OI2 = "门诊住院";
	private static final String OI9 = "其他";
	/* INTERNAL_OR_SERGERY */
	private static final String IS0 = "内科";
	private static final String IS1 = "外科";
	private static final String IS9 = "其他";
	/**
	 * 翻译Clinic_attr字段
	 * @param num
	 * @return 返回转义后的字符串，如果不能识别，则返回传入参数
	 */
	public static String deptClinicAttr(String num){
		if(num == null || "".equals(num.trim())){
			System.err.println("参数不合法");
			return "";
		}
		num = num.trim();
		if("0".equals(num)){
			return CA0;
		}else if("1".equals(num)){
			return CA1;
		}else if("2".equals(num)){
			return CA2;
		}else if("3".equals(num)){
			return CA3;
		}else if("9".equals(num)){
			return CA9;
		}else{
			return num;
		}
	}
	/**
	 * 反转科室属性
	 * @param clinicAttr 科室属性
	 * @return 编号
	 */
	public static String reverseClinicAttr(String clinicAttr){
		if(clinicAttr == null || "".equals(clinicAttr.trim())){
			System.err.println("参数不合法");
			return "";
		}
		if(CA0.equals(clinicAttr)){
			return "0";
		}else if(CA1.equals(clinicAttr)){
			return "1";
		}else if(CA2.equals(clinicAttr)){
			return "2";
		}else if(CA3.equals(clinicAttr)){
			return "3";
		}else if(CA9.equals(clinicAttr)){
			return "9";
		}else{
			return clinicAttr;
		}
	}
	/**
	 * 翻译OUTP_OR_INP字段
	 * @param num
	 * @return 返回转义后的字符串，如果不能识别，则返回传入参数
	 */
	public static String deptOutpOrInp(String num){
		if(num == null || "".equals(num.trim())){
			System.err.println("参数不合法");
			return "";
		}
		num = num.trim();
		if("0".equals(num)){
			return OI0;
		}else if("1".equals(num)){
			return OI1;
		}else if("2".equals(num)){
			return OI2;
		}else if("9".equals(num)){
			return OI9;
		}else{
			return num;
		}
	}
	/**
	 * 反转门诊住院标识
	 * @param outpOrInp 出院住院标识
	 * @return 编号
	 */
	public static String reversepOutOrInp(String outpOrInp){
		if(outpOrInp == null || "".equals(outpOrInp.trim())){
			System.err.println("参数不合法");
			return "";
		}
		if(OI0.equals(outpOrInp)){
			return "0";
		}else if(OI1.equals(outpOrInp)){
			return "1";
		}else if(OI2.equals(outpOrInp)){
			return "2";
		}else if(OI9.equals(outpOrInp)){
			return "9";
		}else{
			return outpOrInp;
		}
	}
	/**
	 * 翻译INTERNAL_OR_SERGERY字段
	 * @param num
	 * @return 返回转义后的字符串，如果不能识别，则返回传入参数
	 */
	public static String deptInternalOrSergery(String num){
		if(num == null || "".equals(num.trim())){
			System.err.println("参数不合法");
			return "";
		}
		num = num.trim();
		if("0".equals(num)){
			return IS0;
		}else if("1".equals(num)){
			return IS1;
		}else if("9".equals(num)){
			return IS9;
		}else{
			return num;
		}
	}
	/**
	 * 反转内科外科标识
	 * @param internalOrSergery 内外科标识
	 * @return 编号
	 */
	public static String reverseInternalOrSergery(String internalOrSergery){
		if(internalOrSergery == null || "".equals(internalOrSergery.trim())){
			System.err.println("参数不合法");
			return "";
		}
		if(IS0.equals(internalOrSergery)){
			return "0";
		}else if(IS1.equals(internalOrSergery)){
			return "1";
		}else if(IS9.equals(internalOrSergery)){
			return "9";
		}else{
			return internalOrSergery;
		}
	}
}
