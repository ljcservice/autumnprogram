package com.ts.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomUtil {
	/** 
     * 生成随机文件名：当前年月日时分秒+六位随机数 
     *  
     * @return 
     */  
    public static String getRandomId() {  
  
        SimpleDateFormat simpleDateFormat;  
  
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");  
  
        Date date = new Date();  
  
        String str = simpleDateFormat.format(date);  
  
        Random random = new Random();  
  
        int rannum = (int) (random.nextDouble() * (999999 - 100000 + 1)) + 100000;// 获取4位随机数  
  
        return rannum + str;// 当前时间  
    }  
    
    public static void main(String[] args) {  
    	  
        String fileName = RandomUtil.getRandomId();  
  
        System.out.println(fileName);//8835920140307  
    }  
}
