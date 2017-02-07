package com.ts.util.ai;

/**
 * AI常量
 * @ClassName: AIConst 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年11月3日 下午1:58:51 
 *
 */
public class AIConst {
	//字典类型定义
	public static final int AI_DICT_TASK_TYPE = 84;		
	public static final int AI_DICT_TASK_TYPE_CHILD = 85;
	public static final int AI_DICT_MTS_MATCH_TYPE = 24;//MTS匹配规则
	public static final int AI_DICT_ORIGIN = 70;//数据来源
	public static final int AI_DICT_DIAG_NONTERM = 60;//无法干预类型-诊断
	public static final int AI_DICT_DIAG_TYC = 50;//同义词类型-诊断
	public static final int AI_DICT_DIAG_SY = 40;//术语类型-诊断
	
	//MTS未匹配上标志
	public static final String AI_MTS_CODE_UNM = "UNM";
	
	//任务类型定义
	public static final int AI_TASK_DIAG =84002;
	public static final int AI_TASK_DRUG =84001;
	
	//任务子类型定义
	public static final int AI_TASK_CHILD_DIAG =85001;
	public static final int AI_TASK_CHILD_NLP =85002;
	
	
	//NLP条数
	public static final int NLP_TIAOSHU= 10;
	
	
	
	
}
