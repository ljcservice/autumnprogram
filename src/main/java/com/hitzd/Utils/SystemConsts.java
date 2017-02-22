package com.hitzd.Utils;

import java.util.List;
import java.util.Map;

public class SystemConsts 
{
	private static String ABCAS           = "抗菌药物临床应用管理系统";
	private static String ACSS            = "军人费用统计平台";
	private static String ADDM            = "抗菌药数据上报系统";
	private static String ADR             = "不良反应自动监控与报表系统";
	private static String Config          = "信息科技术支持平台";
	private static String CPMS            = "临床药学监管平台";
	private static String DAMS            = "临床科主任监管平台";
	private static String EMR             = "电子药历与药师工作数/质量平台";
	private static String HIMS            = "感控科抗菌药物临床应用监管平台"; 
	private static String IFMIX           = "医药知识在线查询平台";
	private static String MPSS            = "医保监管平台";
	private static String PH              = "病人病历查看平台";
	private static String PDSS            = "合理用药安全审核系统";
	private static String PEAAS           = "药品使用统计汇总分析系统";
	private static String Portal          = "";
	private static String PrescEvaluate   = "门诊处方点评预警系统";
	private static String PrescReview     = "医生工作站医嘱安全监管平台";
	private static String RDUM            = "合理用药数据上报系统";
	private static String WPAMS           = "住院药房监管平台";
	private static String BOSS            = "院长(医务机关)监管平台";
	private static String GLMS            = "微生物室细菌耐药监管平台";
	private static String DrugManager     = "药品安全与知识信息维护中心";
	private static String DrugMatchWeb    = "网络药品对照";
	private static String MatcherIntranet = "医院内网配码";
	
	public static List<Map<String, String>> getSystemCodeorName()
	{
	   
	    return null;
	}
	
	/**
	 * 返回平台名称 
	 * @param URI
	 * @return
	 */
	public static String getSystemName(String URI)  
	{
		if ("/ABCAS".equalsIgnoreCase(URI)) return ABCAS;
		if ("/ACSS".equalsIgnoreCase(URI)) return ACSS;
		if ("/ADDM".equalsIgnoreCase(URI)) return ADDM;
		if ("/ADR".equalsIgnoreCase(URI)) return ADR;
		if ("/Config".equalsIgnoreCase(URI)) return Config;
		if ("/CPMS".equalsIgnoreCase(URI)) return CPMS;
		if ("/DAMS".equalsIgnoreCase(URI)) return DAMS;
		if ("/EMR".equalsIgnoreCase(URI)) return EMR;
		if ("/HIMS".equalsIgnoreCase(URI)) return HIMS;
		if ("/".equalsIgnoreCase(URI)) return IFMIX;
		if ("/MPSS".equalsIgnoreCase(URI)) return MPSS;
		if ("/PatientHistory".equalsIgnoreCase(URI)) return PH;
		if ("/HIS_EPH".equalsIgnoreCase(URI)) return PH;
		if ("/PDSS".equalsIgnoreCase(URI)) return PDSS;
		if ("/PEAAS".equalsIgnoreCase(URI)) return PEAAS;
		if ("/Portal".equalsIgnoreCase(URI)) return Portal;
		if ("/PrescEvaluate".equalsIgnoreCase(URI)) return PrescEvaluate;
		if ("/PrescReview".equalsIgnoreCase(URI)) return PrescReview;
		if ("/RDUM".equalsIgnoreCase(URI)) return RDUM;
		if ("/WPAMS".equalsIgnoreCase(URI)) return WPAMS;
		if ("/BOSS".equalsIgnoreCase(URI)) return BOSS;
		if ("/GLMS".equalsIgnoreCase(URI)) return GLMS;
		if ("/DrugManager".equalsIgnoreCase(URI)) return DrugManager;
		if ("/DrugMatchWeb".equalsIgnoreCase(URI)) return DrugMatchWeb;
		if ("/MatcherIntranet".equalsIgnoreCase(URI)) return MatcherIntranet;
		return "医院药事管理信息化解决方案";
	}
	
	private static String PFURI = "/WebPage/MainFrame.jsp";
	
	/**
	 * 返回跨平台地址
	 * @param URI
	 * @return
	 */
	public static String getPlatFormUrl(String URI)
	{
	    if ("/ABCAS".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/ACSS".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/ADDM".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/ADR".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/Config".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/CPMS".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/DAMS".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/EMR".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/HIMS".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/".equalsIgnoreCase(URI)) return IFMIX;
        if ("/MPSS".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/PatientHistory".equalsIgnoreCase(URI)) return URI;
        if ("/HIS_EPH".equalsIgnoreCase(URI)) return URI;
        if ("/PDSS".equalsIgnoreCase(URI)) return PDSS;
        if ("/PEAAS".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/Portal".equalsIgnoreCase(URI)) return Portal;
        if ("/PrescEvaluate".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/PrescReview".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/RDUM".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/WPAMS".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/BOSS".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/GLMS".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/DrugManager".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/DrugMatchWeb".equalsIgnoreCase(URI)) return URI + PFURI;
        if ("/MatcherIntranet".equalsIgnoreCase(URI)) return URI + PFURI;
        return "/AllLogin.jsp"; 
	}
}
