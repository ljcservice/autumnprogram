package com.ts.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 正则规则使用
 * @author autumn
 *
 */
public class PatternUtils
{
    /**
     * 去掉字符串中汉字
     * @param value
     * @return
     */
    public static String delChinaChar(String value)
    {
        if(value == null ||"".equals(value))return  "";
        String reg = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(reg);  
        Matcher mat=pat.matcher(value); 
        return mat.replaceAll("");
    }
    
    /**
     * 获得字符串中的汉字
     * @param value
     * @return
     */
    public static String getChinaChar(String value)
    {
        if(value == null ||"".equals(value))return  "";
        String reg1 = "[^\u4e00-\u9fa5]";
        return value.replaceAll(reg1, "");
    }
    
}
