package com.ts.util.ai;

import com.ts.util.PageData;

/**
 * 规则
 * @ClassName: AIRulesTool 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhy 
 * @date 2016年11月30日 下午1:33:22 
 *
 */
public class AIRulesTool {
	//  是否为亚通诊断扩展词
	public static boolean isAgDiagExt( PageData pd)   throws Exception {
		String MAIN_CODE = AICommMethod.nvl(pd.get("MAIN_CODE"));
		String ADDI_CODE = AICommMethod.nvl(pd.get("ADDI_CODE"));
//		if ( isCodeRange(pd) ) {//此部分的校验放到外面
//			return true;
//		} else 
		if ( getAvalidIcdCodeLen(MAIN_CODE) >=8 || getAvalidIcdCodeLen(ADDI_CODE) >=8   ) {//亚通扩展的码不允许扩
			return true;
		}else {
			return false;
		}
	}
	//判断是否为编码范围   
	public static boolean isCodeRange(PageData pd)  throws Exception {
		//	目前没具体规律，包含 “-”，则判断为编码范围, 或者 MAIN_CODE_TYPE =1 
		String MAIN_CODE = AICommMethod.nvl(pd.get("MAIN_CODE"));
		String ADD_CODE = AICommMethod.nvl(pd.get("ADDI_CODE"));
		if ( MAIN_CODE.indexOf("-") > -1) {
			return true;
		} else if ( ADD_CODE.indexOf("-") > -1) {
			return true;
		} {
			return false;
		}
	}
	//获取有效ICD码 length    去掉  *  .  + 
		public static int getAvalidIcdCodeLen(String icdCode) {
			int len = 0;
			if ( AICommMethod.isEmpty(icdCode))  return 0;
			//编码后面可能含有  *  +  2个符号，不算位数， [ 不算 小数点和 * + ]
			icdCode = icdCode.replaceAll( "\\.", "").replaceAll( "\\*", "").replaceAll( "\\+", "");
			len = icdCode.length();
			return len;
		}
		
		//获取诊疗码 length 
		public static int getAvalidExamCodeLen(String examCode) {
			int len = 0;
			if ( AICommMethod.isEmpty(examCode))  return 0;
			//编码后面可能含有  *  +  2个符号，不算位数， [ 不算 小数点和 * + ]
//			examCode = examCode.replaceAll( "\\.", "").replaceAll( "\\*", "").replaceAll( "\\+", "");
			len = examCode.length();
			return len;
		}
		
	    //诊断形态学编码判断
		public static boolean isXintaixueCode(String code)   throws Exception {
			if ( code.startsWith("M") && ( code.indexOf("/") > -1 || code.indexOf("-") > -1 ) ) {
				
				return true;
			}	else {
				return false;
			}
		}
}
