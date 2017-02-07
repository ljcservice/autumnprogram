package com.ts.util.ontology;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.ts.util.Logger;
import com.ts.util.PageData;
import com.ts.util.Tools;

/**
 * 编码工具类
 * @author silong.xing
 *
 */
public class CodeUtil {
	private static Logger logger = Logger.getLogger(CodeUtil.class);
	
    public static final String letterUpperChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String letterLowerChar = "abcdefghijklmnopqrstuvwxyz";
    public static final String letterChar = letterLowerChar + letterUpperChar;
    public static final String numberChar = "0123456789";
    //除去I和O的小写字母
    public static final String lowerChar = "abcdefghjklmnpqrstuvwxyz";
    
    /*	诊断编码规则
	    ICD-10编码规范。 ICD-10一般只有编码为六位的规范词才能增加子节点，不允许在六位编码以上的层级添加新的子节点。当类目下没有亚目及六位码规范词、亚目下没有六位码规范词时，该节点可以增加子节点。
	    1.	增加的子节点只允许最多编码为8位，即增加的子节点不允许再扩展再下一位的子节点。
	    2.	增加子节点编码必须包含其父节点的六位编码，增加的七八位码以大写英文字母表示，从AA开始，AB、AC……ZY，至ZZ结束。
	    3.	增加在类目下的子节点编码包含了类目的编码，第四位为“x”，五六位为“00”，七八位码以大写英文字母表示，从AA开始，AB、AC……ZY，至ZZ结束。
	    4.	增加在亚目下的子节点编码包含了该亚目的编码，五六位为“00”，七八位码以大写英文字母表示，从AA开始，AB、AC……ZY，至ZZ结束。
	    5.	父节点为带星号的附加码，该节点的编码也必须有星号，必须提示再选择一个带主要编码的父节点，并且编码生成“+”。 （选择某章的附加编码为父节点，不能在该章里再选主要编码；B95-B97、V-Y不能选）
	    	示例：1型糖尿病性单一神经病变，选择父节点“糖尿病单一神经病变（亚目） [G59.0*]”后，需再选择一个带主要编码的父节点“1型糖尿病伴神经并发症  [E10.400]”，生成编码“G59.000AA*；E10.400AA+”
	    6.	亚通标准术语的父节点有主要编码和附加编码两个编码，分别生成相应的易保编码。父节点为“+、*”编码时，需基于其“+、*”分别生成相应的“+、*”码。
	*/
    
    /*
     * 手术编码规则
     * 3位码下无4为码则可以扩充，第4位为x，567位为英文字母
     * 默认4位码下可以扩充，在原父节点上扩充3位吗
     */
    
    /*
     * 科室编码为KS001开始，二级节点为KS001.001,三级节点为KS001.001.001
     */
    
    /**
     * 生产编码工厂
     * @param ontotype 本体类型
     * @param lastAgCode 最后的编码，可以为空
     * @param p_ext_code 父节点编码
     * @return
     * @throws Exception
     */
    public static String makeAgCodeByIcd(String ontotype, String lastAgCode, String p_ext_code)  throws Exception {
		//取不到扩码的字段值，返回 
    	if (!OsynConst.DEP.equals(ontotype) &&  Tools.isEmpty(lastAgCode) && Tools.isEmpty(p_ext_code))  return "";
    	if (!OsynConst.DEP.equals(ontotype) && Tools.isEmpty(p_ext_code))  return "";
//    	if ( isXintaixueCode(p_ext_code) ) return "";//【M98900/3  M90-99】格式的为形态学编码不能被扩展
    	String agcode = "";
		String icdCode = getAvalidIcdCode(p_ext_code);//获取有效ICD码值 去掉  [* +]
		String default_ext_code = "";
		
		if ( OsynConst.DIAG.equals(ontotype) ) {//  扩2位	
			default_ext_code = "AA";
		} else if ( OsynConst.OP.equals(ontotype) ) {	// 扩3位		
			default_ext_code = "00A";
		} else if( OsynConst.DEP.equals(ontotype) ){
			default_ext_code = "001";
		}else{
			return ""; //暂时只处理手术、诊断
		}
		if ( !Tools.isEmpty(lastAgCode) ) {
			String ext_agcode = getAvalidIcdCode(lastAgCode);//获取有效ICD码值 去掉  [* +]
    		if ( OsynConst.DIAG.equals(ontotype) ) {
    			// 诊断编码  扩2位	
    			String first = ext_agcode.charAt(ext_agcode.length()-2) + "";
    	        String last = ext_agcode.charAt(ext_agcode.length()-1) + "";
    	        int first_idx = CodeUtil.letterUpperChar.indexOf(first) ;
    	        int last_idx =  CodeUtil.letterUpperChar.indexOf(last) ;
    	        if ( last_idx < 25 ) {
    	        	last =  CodeUtil.letterUpperChar.charAt(last_idx+1) + "";
    	        } else {
    	        	last =  CodeUtil.letterUpperChar.charAt(0) + "";
    	        	first = CodeUtil.letterUpperChar.charAt(first_idx+1) + "";
    	        }
    	        agcode = ext_agcode.substring(0,ext_agcode.length()-2) + first + last;
    		} else if ( OsynConst.OP.equals(ontotype) ) {
    			// 手术编码。扩3位		
    			String first = ext_agcode.charAt(ext_agcode.length()-3) + "";
    			String mid = ext_agcode.charAt(ext_agcode.length()-2) + "";//默认是0为占位符，后面扩A,B,C...
    	        String last = ext_agcode.charAt(ext_agcode.length()-1) + "";
    	        int first_idx = CodeUtil.letterUpperChar.indexOf(first) ;
    	        int last_idx =  CodeUtil.letterUpperChar.indexOf(last) ;
    	        if ( last_idx < 25 ) {
    	        	last =  CodeUtil.letterUpperChar.charAt(last_idx+1) + "";
    	        } else {
    	        	if ( mid.equals("0") ) {
    	        		mid = CodeUtil.letterUpperChar.charAt(0) + "";
	    	        } else {//默认是0为占位符，后面扩A,B,C...
	    	        	int mid_idx = CodeUtil.letterUpperChar.indexOf(mid) ;		    	        	
	    	        	if ( mid_idx < 25 ) {
	    	        		mid =  CodeUtil.letterUpperChar.charAt(mid_idx+1) + "";
	    	        	} else {
	    	        		if ( first.equals("0") ) {
	    	        			first = CodeUtil.letterUpperChar.charAt(0) + "";
	    	        		} else {
	    	        			first =  CodeUtil.letterUpperChar.charAt(first_idx+1) + "";
	    	        		}
	    	        		mid =  CodeUtil.letterUpperChar.charAt(0) + "";
	    	        	}
	    	        	
	    	        }
    	        	last =  CodeUtil.letterUpperChar.charAt(0) + "";
//    	        	first = CodeUtil.letterUpperChar.charAt(first_idx+1) + "";
    	        }
    	        agcode = ext_agcode.substring(0,ext_agcode.length()-3)  + first + mid + last;
    		}else if( OsynConst.DEP.equals(ontotype) ){
    			//科室编码
    			if(lastAgCode.contains(".")){
    				String[] codes = lastAgCode.split("\\.");
    				String code = codes[codes.length-1];
    				int c = Integer.valueOf(code);
    				if(c<10){
    					agcode = icdCode + ".00" + (c+1);
    				}else if(c<100){
    					agcode = icdCode + ".0" + (c+1);
    				}else if(c<1000){
    					agcode = icdCode + "." + (c+1);
    				}else{
    					logger.error("dep code is out of bounds 999.");
    					return null;
    				}
    			}else if(lastAgCode.contains("KS")){
    				String code = lastAgCode.replaceAll("KS", "");
    				int c = Integer.valueOf(code);
    				if(c<10){
    					agcode = "KS00" + (c+1);
    				}else if(c<100){
    					agcode = "KS0" + (c+1);
    				}else if(c<1000){
    					agcode = "KS" + (c+1);
    				}else{
    					logger.error("dep code is out of bounds 999.");
    					return null;
    				}
    			}else{
    				logger.error("dep root can not code error.");
    				return "";
    			}
    		}
    	   
    	} else {
    		//没有子节点情况处理
    		if ( OsynConst.DIAG.equals(ontotype) ) {
	        	/**
	        	 * 当类目下没有亚目及六位码规范词、亚目下没有六位码规范词时，该节点可以增加子节点。
	        	 * 增加在类目下的子节点编码包含了类目的编码，第四位为“x”，五六位为“00”，七八位码以大写英文字母表示，从AA开始，AB、AC……ZY，至ZZ结束。
	        	 * 增加在亚目下的子节点编码包含了该亚目的编码，五六位为“00”，七八位码以大写英文字母表示，从AA开始，AB、AC……ZY，至ZZ结束。
	        	*/
	        	if(icdCode.length() == 3){
	    			return icdCode + ".x00AA";
	    		}else if(icdCode.length() == 5){
	    			return icdCode + "00AA";
	    		}else{
	    			//非类目亚目 增加下为词情况，正常处理
	    			agcode = icdCode + default_ext_code;
	    		}
    		} else if ( OsynConst.OP.equals(ontotype) ) {
	        	/**
	        	 * 3位码下午4位码。可以扩充，第四位为x，在补充三位00A
	        	 * 4位码可以扩充，补充三位00A
	        	*/
    			if(icdCode.length() == 4){
    				agcode = icdCode + "x" + default_ext_code;
    			}else if(icdCode.length() == 5){
    				agcode = icdCode + default_ext_code;
    			}
    			
    		} else if( OsynConst.DEP.equals(ontotype) ){
    			//科室编码
    			if(icdCode==null){
    				//父节点为空，子节点也为空情况下
    				agcode = "KS001";
    			}else{
    				agcode = icdCode + "." + default_ext_code;
    			}
    		}
    	}
    	
    	if (p_ext_code!=null && p_ext_code.indexOf("*") > -1 ) {
    		agcode += "*";
    	} else if ( p_ext_code!=null && p_ext_code.indexOf("+") > -1 ) {
    		agcode += "+";
    	}
		return agcode;
	}
	
    /**
     * 规则5.父节点为带星号的附加码，该节点的编码也必须有星号，必须提示再选择一个带主要编码的父节点，并且编码生成“+”。 
     * @param pd
     */
    public static void setSpecialCode(PageData pd){
    	if(pd == null) return;
    	if(Tools.isEmpty(pd.getString("MAIN_CODE"))) return;
    	if(Tools.isEmpty(pd.getString("ADD_CODE"))) return;
    	if(pd.getString("ADD_CODE").indexOf("*")==-1) return;
    	if(pd.getString("MAIN_CODE").indexOf("+")!=-1) return;
    	pd.put("MAIN_CODE", pd.getString("MAIN_CODE")+"+");
    }
    
	//   诊断形态学编码判断
	public static boolean isXintaixueCode(String code)   throws Exception {
		if ( code.startsWith("M") && ( code.indexOf("/") > -1 || code.indexOf("-") > -1 ) ) {
			return true;
		}	else {
			return false;
		}
	}
	
	//获取有效ICD码值 去掉  [* +] 
	public static String getAvalidIcdCode(String icdCode) {
		if(icdCode == null){
			return null;
		}
		//编码后面可能含有  *  +  2个符号，不算位数， [ 不算 小数点和 * + ]
		icdCode = icdCode.replaceAll("\\+", "").replaceAll( "\\*", "");		
		return icdCode;
	}
	
	//获取有效ICD码 length    去掉  *  .  + 
	public static int getAvalidIcdCodeLen(String icdCode) {
		int len = 0;
		if ( Tools.isEmpty(icdCode))  return 0;
		//编码后面可能含有  *  +  2个符号，不算位数， [ 不算 小数点和 * + ]
		icdCode = icdCode.replaceAll( "\\.", "").replaceAll( "\\*", "").replaceAll( "\\+", "");
		len = icdCode.length();
		return len;
	}
	
	/*
	 * 对数据进行大小排序、大-->小 mapKey 按照哪个字段排序 keyType排序字段的类型 sortType :ASC还是desc
	 */

	public List<Map<String, Object>> sortList(
			List<Map<String, Object>> list, final String mapKey, final String keyType,
			final String sortType) {
		if (list != null && list.size() > 0) {
//			String key = mapKey;
			Comparator<Map<String, Object>> mapComprator = new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> o1,Map<String, Object> o2) {
					int yesVal = 1;
					int noVal = -1;
					if ("desc".equals( sortType.toLowerCase())) {
						yesVal = 1;
						noVal = -1;
					} else {
						yesVal = -1;
						noVal = 1;
					}

					int retVal = noVal;
					String o1_key = String.valueOf(o1.get(mapKey));
					String o2_key = String.valueOf(o2.get(mapKey));
					if ("int".equals(keyType)) {
						o1_key = CodeUtil.nvl(o1_key, "0");
						o2_key = CodeUtil.nvl(o2_key, "0");
						if (Integer.valueOf(o1_key) < Integer.valueOf(o2_key)) {
							retVal = yesVal;
						}
					} else if ("String".equals(keyType)) {
						o1_key = CodeUtil.nvl(o1_key, "");
						o2_key = CodeUtil.nvl(o2_key, "");
						if (o1_key.compareTo(o2_key) < 0){
							retVal = yesVal;	    		
				    	}
					} else if ("float".equals(keyType)) {
						o1_key = CodeUtil.nvl(o1_key, "0");
						o2_key = CodeUtil.nvl(o2_key, "0");
						if (Float.valueOf(o1_key) < Float.valueOf(o2_key)) {
							retVal = yesVal;
						}
					} else if ("long".equals(keyType)) {
						o1_key = CodeUtil.nvl(o1_key, "0");
						o2_key = CodeUtil.nvl(o2_key, "0");
						if (Long.valueOf(o1_key) < Long.valueOf(o2_key)) {
							retVal = yesVal;
						}
					}
					return retVal;
				}
			};
			Collections.sort(list, mapComprator);
		} else {
			new Exception("排序没有取到数据");
		}
		return list;
	}
	
	/**
	 * <p>Object转String ,空字符串返回"", 非空返回自己</p>	.
	 * @returnS
	 * @throws Exception
	 */
	public static String nvl(Object obj) {
		String ifNull = "";
		String str = String.valueOf(obj);
		if ( str == null ) return ifNull;
		str = str.trim();
		if (  str.equals("") || "null".equals(str)) {
			return ifNull;
		} else {
			return str;
		}
	}
	
	/**
	 * <p>空字符串返回"", 非空返回自己</p>	.
	 * @returnS
	 * @throws Exception
	 */
	public static String nvl(String str) {
		String ifNull = "";
		if ( str == null ) return ifNull;
		str = str.trim();
		if ( str.equals("") || "null".equals(str)) {
			return ifNull;
		} else {
			return str;
		}
	}
	
	/**
	 * <p>类似oracle  nvl函数 Object转String ,空字符串返回 ifNull, 非空返回自己</p>	.
	 * @returnS
	 * @throws Exception
	 */
	public static String nvl(Object obj, String ifNull) {
		String str = String.valueOf(obj);
		if ( str == null ) return ifNull;
		str = str.trim();
		if (  str.equals("") || "null".equals(str)) {
			return ifNull;
		} else {
			return str;
		}
	}

	
	/**
	 * <p>Object转String ,空字符串返回ifNull, 非空返回自己</p>	.
	 * @returnS
	 * @throws Exception
	 */
	public static String nvl(String str, String ifNull) {
		if ( str == null ) return ifNull;
		str = str.trim();
		if ( str.equals("") || "null".equals(str)) {
			return ifNull;
		} else {
			return str;
		}
	}
}
