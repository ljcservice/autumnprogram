package com.ts.util.ailService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Encoder;
import javax.crypto.Mac;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Sender {
    /*
     * 计算MD5+BASE64
     */
    public static String MD5Base64(String s) {
        if (s == null)
            return null;
        String encodeStr = "";
        byte[] utfBytes = s.getBytes();
        MessageDigest mdTemp;
        try {
            mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(utfBytes);
            byte[] md5Bytes = mdTemp.digest();
            BASE64Encoder b64Encoder = new BASE64Encoder();
            encodeStr = b64Encoder.encode(md5Bytes);
        } catch (Exception e) {
            throw new Error("Failed to generate MD5 : " + e.getMessage());
        }
        return encodeStr;
    }
    /*
     * 计算 HMAC-SHA1
     */
    public static String HMACSha1(String data, String key) {
        String result;
        try {
            // System.out.println("data: " + data);
            // System.out.println("key: " + key);
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = (new BASE64Encoder()).encode(rawHmac);
        } catch (Exception e) {
            throw new Error("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }
    /*
     * 等同于javaScript中的 new Date().toUTCString();
     */
    public static String toGMTString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }
    /*
     * 发送POST请求
     */
    public static String sendPost(String url, String body, String ak_id, String ak_secret) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            /*
             * http header 参数
             */
            String method = "POST";
            String accept = "json";
            String content_type = "application/json";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());
            // 1.对body做MD5+BASE64加密
            String bodyMd5 = MD5Base64(body);
            String stringToSign = method + "\n" + accept + "\n" + bodyMd5 + "\n" + content_type + "\n" + date + "\n"
                    + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            // 3.得到 authorization header
            String authHeader = "Dataplus " + ak_id + ":" + signature;
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", accept);
            conn.setRequestProperty("content-type", content_type);
            conn.setRequestProperty("date", date);
            conn.setRequestProperty("Authorization", authHeader);
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(body);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            InputStream is;
            HttpURLConnection httpconn = (HttpURLConnection) conn;
            if (httpconn.getResponseCode() == 200) {
                is = httpconn.getInputStream();
            } else {
                is = httpconn.getErrorStream();
            }
            InputStreamReader sr = new InputStreamReader(is,"utf-8");
            in = new BufferedReader(sr);
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    public static void main( String[] args ) throws Exception {
        //EntityCheck();
        
        fc();
        //sbpj();
    }
    
    private static  void sbpj() throws Exception
    {
        JSONObject postBodyJson = new JSONObject();
        // text: 要处理的文本
        postBodyJson.put("text", "面料舒适，款式好，只是尺码偏小，好在我看了其他买家的评价，在原尺码上加了一号，正合适，很满意！给满分！服务好，发货快！");
        // cate: 文本所属的类别，比如服装、酒类、汽车
        postBodyJson.put("cate", "clothing");
        // Sender代码参考 https://help.aliyun.com/document_detail/shujia/OCR/ocr-api/sender.html
         //serviceURL: https://dtplus-cn-shanghai.data.aliyuncs.com/{org_code}/nlp/api/Sentiment/{Domain}                                               
         String serviceURL = "https://dtplus-cn-shanghai.data.aliyuncs.com/dt_ng_1646870980837502/nlp/api/ReviewAnalysis/ecommerce";
         
         String akID = "LTAInG2mNjX5WzZ6";
         String akSecret = "JXdoH2lfaWLmsrXZAXF8VVAPx78ARc";
         String result = AESDecode.sendPost(serviceURL, postBodyJson.toJSONString(), akID, akSecret);    
         System.out.println(result);
         try {
             JSONObject resultJson = JSON.parseObject(result);
             JSONObject dataObj = resultJson.getObject("result", JSONObject.class);
             System.out.println(dataObj);
             String polarity = dataObj.getString("textPolarity ");
         }catch (Exception e){
             e.printStackTrace();
         }
    }
    
    
    private static void fc()
    {
        // serviceURL: https://dtplus-cn-shanghai.data.aliyuncs.com/{org_code}/nlp/api/WordSegment/{Domain}
        String serviceURL = "https://dtplus-cn-shanghai.data.aliyuncs.com/dt_ng_1646870980837502/nlp/api/WordSegment/general";
        String akID = "LTAInG2mNjX5WzZ6";
        String akSecret = "JXdoH2lfaWLmsrXZAXF8VVAPx78ARc";
        // 填充请求body
        // String postBody = "{\"text\":\"Iphone专用数据线\"}";
        JSONObject postBodyJson = new JSONObject();
        postBodyJson.put("text", "#马来酸桂哌齐特注射液(医乙)限缺血性脑血管病雷诺工");
        // Sender代码参考 https://help.aliyun.com/document_detail/shujia/OCR/ocr-api/sender.html
        // String result = Sender.sendPost(serviceURL, postBody, akID, akSecret);
        String result = Sender.sendPost(serviceURL, postBodyJson.toJSONString(), akID, akSecret);
        System.out.println(result);
        try {
            JSONObject resultJson = JSON.parseObject(result);
            JSONArray wordObjs = resultJson.getObject("data", JSONArray.class);
            for(Object wordObj : wordObjs){
                JSONObject wordJson = JSON.parseObject(wordObj.toString());
                Integer id = wordJson.getInteger("id"); // 分词编号
                String word = wordJson.getString("word"); // 词
                System.out.printf("id: %d, word: %s\n", id, word);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    private static void EntityCheck()
    {
        // serviceURL: https://dtplus-cn-shanghai.data.aliyuncs.com/{org_code}/nlp/api/Entity/{Domain}
        String serviceURL = "https://dtplus-cn-shanghai.data.aliyuncs.com/dt_ng_1646870980837502/nlp/api/Entity/ecommerce";
        String akID = "LTAInG2mNjX5WzZ6";
        String akSecret = "JXdoH2lfaWLmsrXZAXF8VVAPx78ARc";
        // 填充请求body
        // String postBody = "{\"text\":\"真丝韩都衣舍连衣裙\", \"type\": \"simple\"}";
        JSONObject postBodyJson = new JSONObject();
        // 要处理的文本
        postBodyJson.put("text", "#马来酸桂哌齐特注射液(医乙)限缺血性脑血管病雷诺工");
        // type: 控制输出样式，"simple" 简单输出; "full" 详细输出，
        // 简单输出包括实体名称和实体类别，详细输出包括实体名称、实体类别、权重和近义词。
        postBodyJson.put("type", "full");
        // Sender代码参考 https://help.aliyun.com/document_detail/shujia/OCR/ocr-api/sender.html
        // String result = Sender.sendPost(serviceURL, postBody, akID, akSecret);
        String result = Sender.sendPost(serviceURL, postBodyJson.toJSONString(), akID, akSecret);
        System.out.println(result);
        try {
            JSONObject resultJson = JSON.parseObject(result);
            JSONArray wordObjs = resultJson.getObject("data", JSONArray.class);
            for(Object wordObj : wordObjs){
                JSONObject wordJson = JSON.parseObject(wordObj.toString());
                String word = wordJson.getString("word"); // 实体名称
                String tag = wordJson.getString("tag"); // 实体类别
                String synonym = wordJson.getString("synonym"); // 近义词
                String weight = wordJson.getString("weight"); // 权重
                System.out.printf("word: %s, tag: %s, synonym: %s, weight: %s\n",
                        word, tag, synonym, weight);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
