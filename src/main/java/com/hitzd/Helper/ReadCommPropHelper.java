package com.hitzd.Helper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 获得公共参数类 
 * @author Administrator
 */
public class ReadCommPropHelper
{
    private static Map<String, String> commProp = new HashMap<String, String>();
    static
    {
        try
        {
            Properties prop = new Properties();
            File f = new File("commonConfig.properties");
            if(!f.exists())
            {
                throw new RuntimeException("未找到的该文件:\"" + f.getCanonicalPath() + "\"");
            }
            InputStream input = new BufferedInputStream(new FileInputStream(f));
            prop.load(input);
            for(Object key : prop.keySet())
            {
                commProp.put(key.toString(), prop.getProperty(key.toString()));
            }
        }
        catch(Exception e )
         {
            e.printStackTrace();
        }
    }
    
    /**
     * 获得指定参数 
     * @param key
     * @return
     */
    public static String getPropertiesValue(String key)
    {
        return commProp.get(key);
    }
    
    /**
     * 获得所有参数
     * @return
     */
    public static  Map<String,String> getPropertiesAll()
    {
        Map<String, String> result = new HashMap<String, String>();
        for(String key : commProp.keySet())
        {
            result.put(key, commProp.get(key));
        }
        return result ;
    }
}
