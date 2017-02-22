package com.hitzd.Annotations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hitzd.DBUtils.LobCreatingPSCallBeck;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Scheduler.ModelHandler;
import com.hitzd.his.Utils.DateUtils;

public class testDemo
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        
        
        List<TCommonRecord> listc = new ArrayList<TCommonRecord>();
        TCommonRecord t = new TCommonRecord();
        t.set("1-t1","1");
        t.set("1-t2","2");
        t.set("1-t3","3");
        t.set("1-t4","3");
        listc.add(t);
        t = new TCommonRecord();
        t.set("2-t1","1");
        t.set("2-t2","2");
        t.set("2-t3","3");
        t.set("2-t4","3");
        listc.add(t);
        System.out.println("list : " + listc.size());
        
        TCommonRecord tc = listc.get(0);
        System.out.println(tc.get("1-t4"));
        TCommonRecord tClone = (TCommonRecord) tc.deepClone();
        tClone.set("1-t4", "1111111111111111111");
        System.out.println(tClone.get("1-t4"));
        System.out.println(tc.get("1-t4"));
        try
        {
            
            
            
            
            if(true) return ; 
            
            LobCreatingPSCallBeck fx = new LobCreatingPSCallBeck("");
            fx.LobCreatingPreparedSCallback();
            System.out.println("℃");
            String date1 = "2013-01-04";
            String date2 = "2013-01-04";
            System.out.println(DateUtils.getDateFromString(date1).getTime());
           // System.out.println(Date.valueOf(date1).getTime());
            System.out.println(Date.valueOf(date2).getTime());
            
            
            if(true) return ; 
            String strTables = "TabliConfig[select,save,test]";
            Map<String, String> tablesMap = new HashMap<String, String>();
            Pattern pattern = Pattern.compile("(\\[)(.*?)(\\])");
            Matcher matcher = pattern.matcher(strTables);
            List<String> list = new ArrayList<String>();
            while (matcher.find()) { 
                
                strTables = strTables.replace(matcher.group(2), "tempTable");
                System.out.println(matcher.group(0));
                list.add(matcher.group(2));
            }
            System.out.println("strtable - " + strTables);
            if(true)return ;
            
            String[] tablesWithAliases = strTables.split(",");
            int cnt = 0;
            for (String tableWithAliases : tablesWithAliases)
            {
                String [] tables = tableWithAliases.split(" ");
                if (tables.length > 1)
                {
                    if (tables[0].indexOf("[") >= 0)
                        tablesMap.put(tables[1], "(" + list.get(cnt++) + ")");
                    else
                        tablesMap.put(tables[1], tables[0]);
                }
                else
                    tablesMap.put("imtmp", tables[0]);
            }
            
            for(String key : tablesMap.keySet())
            {
                System.out.println("key - " + key + ": value - " + tablesMap.get(key));
            }
            
            if(true) return ;
            String s = "APPLOG\\ias\\xxxxx.log";
            File f = new File(s);
            
            System.out.println(f.getParentFile());
            System.out.println(s);
            if(!f.exists())
            {
                f.mkdirs();
                //throw new RuntimeException("未找到的该文件:\"" + f.getCanonicalPath() + "\"");
            }
            FileWriter o = new FileWriter(new File(s));
            for (String x: new String[]{"3434","2323423dsf","dfsdf","sdfsdfsd","sdfsdf","sdfsdf","sdfsd","sdfsdf","sdfsdf"})
                o.write(x + "\n");
            o.flush();
            o.close();
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        if(true) return ;
        Object [] o  = new Object[]{1,12};
        String d = new String();
        System.out.println(o.getClass().getSimpleName());
        
        TCommonRecord  tx = new TCommonRecord();
        t.set("set1","v'v''");
        System.out.println(tx.get("set1", "'"));
    }

}
