package com.hitzd.Annotations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
//import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.hitzd.DBUtils.LobCreatingPSCallBeck;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Utils.DateUtils;
import com.ts.util.HTTP.HttpRequest;

import net.sf.json.JSONObject;

public class testDemo
{
    class ThreeThread implements Callable<Integer>
    {
        int i = 0 ;
        @Override
        public Integer call() throws Exception
        {
            for(;i<20 ;i++)
            {
                
            }
            // TODO Auto-generated method stub
            return i;
        }
        
    }
    
    private void pubc() throws InterruptedException, ExecutionException
    {
        ThreeThread  tt = new ThreeThread();
        FutureTask<Integer> ft = new FutureTask<>(tt);
        Thread tt2 = new Thread(ft, "");
        tt2.start();
        
        int rs = ft.get();

    }
    
    private java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock(); 
    private static ThreadLocal<Integer> count = new ThreadLocal<Integer>();
    private void test()
    {
        lock.lock();
        lock.unlock();
        
        
        
        
    }
//    public HttpResult doPost(String url, Map<String, String> params) throws IOException {
//        // 创建http POST请求
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.setConfig(this.requestConfig);
//        if (params != null) {
//            // 设置2个post参数，一个是scope、一个是q
//            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//            for (String key : params.keySet()) {
//                parameters.add(new BasicNameValuePair(key, params.get(key)));
//            }
//            // 构造一个form表单式的实体
//            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
//            // 将请求实体设置到httpPost对象中
//            httpPost.setEntity(formEntity);
//        }
//
//        CloseableHttpResponse response = null;
//        try {
//            // 执行请求
//            response = HttpClient.execute(httpPost);
//            // 结果集
//            HttpResult hr = new HttpResult();
//            hr.setStatus(response.getStatusLine().getStatusCode());
//            hr.setData(EntityUtils.toString(response.getEntity(), "UTF-8"));
//            return hr;
//        } finally {
//            if (response != null) {
//                response.close();
//            }
//        }
//    }

    /**
     * httpPost
     * @param url  路径
     * @param jsonParam 参数
     * @return
     */
    public static JSONObject httpPost(String url,JSONObject jsonParam){
        
        return httpPost(url, jsonParam, false);
    }
    /**
     * post请求
     * @param url         url地址
     * @param jsonParam     参数
     * @param noNeedResponse    不需要返回结果
     * @return
     */
    public static JSONObject httpPost(String url,JSONObject jsonParam, boolean noNeedResponse){
        //post请求返回结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        HttpPost method = new HttpPost(url);
        try {
            if (null != jsonParam) {
                System.out.println(jsonParam.toString());
                //解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
                System.out.println(entity);
            }
            HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());
                    if (noNeedResponse) {
                        return null;
                    }
                    /**把json字符串转换成json对象**/
                    jsonResult = JSONObject.fromObject(str);
                } catch (Exception e) {
//                    logger.error("post请求提交失败:" + url, e);
                    System.out.println("post请求提交失败:" + url +  e);
                }
            }
        } catch (IOException e) {
//            logger.error("post请求提交失败:" + url, e);
            System.out.println("post请求提交失败:" + url + e);

        }
        return jsonResult;
    }
    
//    public  String nlpCtrl(String url,String o_name) {
//        url ="http://10.10.41.25:10011";
//        String res="";     
//        JSONObject jsop = new JSONObject();
//        List<String> nnlp=new ArrayList<String>();
//        List<String> ss=new ArrayList<String>();
//        try {
//            Map<String, String> m=new HashMap<>();
//            m.put("s", o_name);
//            HttpPost  http = new HttpPost(url);
//            httpResult httpResult =   httpClientService.doPost(url, m);
//            Integer status = httpResult.getStatus();
//            String str = httpResult.getData();
//            //结果为json格式的数据
//            if(null == str || "".equals(str)){
//                        return "";
//            }
//
//            JSONObject j=JSONObject.fromObject(str);        
//            Iterator iterator = j.keys();
//            while(iterator.hasNext()){
//                String key = (String) iterator.next();//术语类型  疾病
//                  if(key.equals("entity")){
//                      //"entity" : "急性支气管炎【疾病】<br/>咳嗽【症状】<br/>气道高反应【症状】<br/>【转换】性障碍【症状】",
//
//                      String entity=j.getString(key);  
//                      String[] nlpstrs=entity.split("<br/>");
//                      for(int k=0;k<nlpstrs.length;k++){
//                          
//                          if(!(nlpstrs[k]==""||"".equals(nlpstrs[k]))){
//                             nnlp.add(nlpstrs[k]);
//                             ss.add(nlpstrs[k].substring(0,nlpstrs[k].lastIndexOf("【")));                       
//                            }                         
//                       }     
//
//                }
//            }
//            
//            jsop.put("terms", ss);
//            jsop.put("nlptype", nnlp);
//            System.out.println("切分后词语："+jsop.toString());
//            } catch (Exception e) {
//                
//                e.printStackTrace();
//            } 
//            return jsop.toString();
//        }
    
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
//        Date  dd = DateUtils.getDateFromString("2018-06-23");
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dd);
//        System.out.println(DateUtils.getDate(cal));
//         if(true ) return ;
//        String s1 = "2型糖尿病伴外伤";
//        String s2 = "外伤";
//        System.out.println(s1.length());
//        System.out.println(s1.indexOf(s2));
        
//        String sts = null ;
//        String b = String.valueOf(sts);
//        System.out.println(sts == null);
//        System.out.println("null".equals(b));
//        
//        if(true) return ;
        List<String> nnlp = new ArrayList<String>();
        List<String> ss = new ArrayList<String>();
        
        try
        {
//            Calendar cal = Calendar.getInstance();
//            // 每月中的天
//            int day = cal.get(Calendar.DAY_OF_MONTH);
//            System.out.println(day);
//            // 月份。
//            int month = cal.get(Calendar.MONTH);
//            System.out.println(month);
//            // 年 天
//            int dayOfyear = cal.get(Calendar.DAY_OF_YEAR);
//            System.out.println(dayOfyear);
//            
            
            
//            if(true) return ;
            
            String url = "http://10.10.41.25:10011";
            String value = "孕12周+..终止妊娠";
//            value = "复方氨基酸（6AA）"  ; 
            //value ="#脂肪乳氨基酸(17)葡萄糖注射液/医乙限重症患者肠功能";
//            JSONObject jsonpara = new JSONObject();
//            jsonpara.put("s", value);
//            System.out.println(httpPost(url, jsonpara).toString());
            String rs = HttpRequest.sendPost(url, "s=" + value);
            System.out.println(rs);
            JSONObject j = JSONObject.fromObject(rs);
            System.out.println(j);
            Iterator iterator = j.keys();
            while (iterator.hasNext())
            {
                String key = (String) iterator.next();// 术语类型 疾病
                if (key.equals("entity"))
                {
                    // "entity" :
                    // "急性支气管炎【疾病】<br/>咳嗽【症状】<br/>气道高反应【症状】<br/>【转换】性障碍【症状】",
                    String entity = j.getString(key);
                    String[] nlpstrs = entity.split("<br/>");
                    for (int k = 0; k < nlpstrs.length; k++)
                    {

                        if (!(nlpstrs[k] == "" || "".equals(nlpstrs[k])))
                        {
                            nnlp.add(nlpstrs[k]);
                            ss.add(nlpstrs[k].substring(0,nlpstrs[k].lastIndexOf("【")));
                        }
                    }

                }
            }
            
            System.out.println(nnlp.toString());
            System.out.println(ss.toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
//        String x = "()";
//        System.out.println(x.indexOf(")"));
        
        
        if(true) return ;
        
        String str = "1.0*10万IU/g兆科药业";

        String reg = "[\u4e00-\u9fa5]";
        String reg1 = "[^\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(reg);  

        Matcher mat=pat.matcher(str); 

        String repickStr = mat.replaceAll("");
        String getStrChina = str.replaceAll(reg1, "");

        System.out.println("去中文后:"+repickStr);
        System.out.println("汉字：" + getStrChina);
        
        
        if(true) return ;
        Pattern p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
//        String str="狮";
//        System.out.println(str.length());
//        Matcher m = p_str.matcher(str);
//        if(m.find()&&m.group(0).equals(str))
//        {
//            System.out.println("Ok");
//        }else
//        {
//            System.out.println("No");
//        }
        
        
//        long l1 = System.nanoTime();
//          for(int i = 0 ; i<10000000;i++)
//          {
//              try{
//                  
//              }
//              catch(Exception e)
//              {
//                  e.printStackTrace();
//              }
//              
//          }
//          System.out.println((System.nanoTime() - l1));
         
        if(true) return ;
        
        
          try
          {
              long l2 = System.nanoTime();  
              for(int i = 0 ; i<10000000;i++)
              {
                  
              }
              System.out.println((System.nanoTime() - l2));
          }
          catch(Exception e)
          {
              e.printStackTrace();
          }
          

        
//        String s1 = "33dffxx";
//        String s2 = "33dffxx";
//        System.out.println(s1.equalsIgnoreCase(s2));
//        System.out.println(s1.compareToIgnoreCase(s2));
        
        //System.out.println("dssdf".indexOf(""));
        if(true)return;
        String datetime = "2015/7/5 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d");
        
         try
        {
              Date d = sdf.parse(datetime);
              Long l = d.getTime();
              System.out.println(l);
        }
        catch (ParseException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        
        if(true) return ;
        
        
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
//            System.out.println(Date.valueOf(date2).getTime());
            
            
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
//            FileWriter o = new FileWriter(new File(s));
//            for (String x: new String[]{"3434","2323423dsf","dfsdf","sdfsdfsd","sdfsdf","sdfsdf","sdfsd","sdfsdf","sdfsdf"})
//                o.write(x + "\n");
//            o.flush();
//            o.close();
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
