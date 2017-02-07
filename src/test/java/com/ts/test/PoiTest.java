package com.ts.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpsURL;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.ts.plugin.TsHttpClient;





public class PoiTest {

    public static String getsFetch4Page() {  
        StringBuffer temp = new StringBuffer();  
        try {  
           // System.out.println(leibie);  
//            System.out.println(num);  
            
            
            
            String url = "http://www.baidu.com";
            HttpsURLConnection uc = (HttpsURLConnection)new URL(url).openConnection();  
            uc.setConnectTimeout(10000);  
            uc.setDoOutput(true);  
            uc.setRequestMethod("POST");
            uc.setInstanceFollowRedirects(true);
            uc.addRequestProperty("Accept-Charset", "UTF-8;");  
            uc.setRequestProperty("accept-language", "zh-CN");
            uc.addRequestProperty("User-Agent",  
                    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8"); 
            uc.setUseCaches(false);  
            uc.connect();
            //DataOutputStream out = new DataOutputStream(uc.getOutputStream()); 
            /* 要传的参数  
            String s = URLEncoder.encode("ra", "GB2312") + "=" +  
                       URLEncoder.encode(leibie, "GB2312");  
            s += "&" + URLEncoder.encode("keyword", "GB2312") + "=" +  
                    URLEncoder.encode(num, "GB2312");  
            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面  
            s = "";*/
            //out.writeBytes(s);  
            //out.flush();  
            //out.close();  
            InputStream in = new BufferedInputStream(uc.getInputStream());  
            Reader rd = new InputStreamReader(in, "utf-8");  
            int c = 0;  
            while ((c = rd.read()) != -1) {  
                temp.append((char) c);  
            }  
            System.out.println("－－－" + temp.toString());  
            in.close();  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return temp.toString();  
    }  
    
    /**
     * 
     * @param leibie
     * @param num
     * @return
     */
    public static String getFetch4Page() {  
        StringBuffer temp = new StringBuffer();  
        try {  
           // System.out.println(leibie);  
//            System.out.println(num);  
            
            
            String url = "https://10.10.50.13/app/oauth/verify.do";
            HttpURLConnection uc = (HttpURLConnection)new URL(url).openConnection();  
            
            uc.setConnectTimeout(10000);  
            uc.setDoOutput(true);  
            uc.setRequestMethod("POST");
            uc.setInstanceFollowRedirects(true);
            uc.addRequestProperty("Accept-Charset", "UTF-8;");  
            uc.setRequestProperty("accept-language", "zh-CN");
            uc.addRequestProperty("User-Agent",  
                    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8"); 
            uc.setUseCaches(false);  
            uc.connect();
            //DataOutputStream out = new DataOutputStream(uc.getOutputStream()); 
            /* 要传的参数  
            String s = URLEncoder.encode("ra", "GB2312") + "=" +  
                       URLEncoder.encode(leibie, "GB2312");  
            s += "&" + URLEncoder.encode("keyword", "GB2312") + "=" +  
                    URLEncoder.encode(num, "GB2312");  
            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面  
            s = "";*/
            //out.writeBytes(s);  
            //out.flush();  
            //out.close();  
            InputStream in = new BufferedInputStream(uc.getInputStream());  
            Reader rd = new InputStreamReader(in, "utf-8");  
            int c = 0;  
            while ((c = rd.read()) != -1) {  
                temp.append((char) c);  
            }  
            System.out.println("－－－" + temp.toString());  
            in.close();  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return temp.toString();  
    }  
    
    
    
    @SuppressWarnings ("deprecation")
    public static InputStream getRequestInputStream(String url,Map<String,String> requestHeadMap,String encoding, int connTime,int soTime){
        
        InputStream retStream = null;
        
        ProtocolSocketFactory fcty = new SSLProtocolSocketFactory();
        Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
        
        if(url==null || "".equals(url)) return null;
        HttpClient client = new HttpClient();

        client.getHttpConnectionManager().getParams().setConnectionTimeout(connTime);
        client.getHttpConnectionManager().getParams().setSoTimeout(soTime);
        
        client.getParams().setContentCharset(encoding);
        PostMethod method = new PostMethod(url);
//        GetMethod method = new GetMethod(url);
        if("UTF-8".equalsIgnoreCase(encoding)){
            method.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        }else{
            method.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=GBK");
        }
        method.addRequestHeader("Connection", "close");
        method.setRequestBody("ddd");
        
        try
        {
            RequestEntity reqEntity =  new StringRequestEntity("{\"user\":\"kl\",\"pwd\":\"1\"}", "application/json", "UTF-8");
            method.setRequestEntity(reqEntity);
            
        }
        catch (UnsupportedEncodingException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
//        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        
//        if(requestHeadMap!=null){
//            Set<Map.Entry<String, String>> set = requestHeadMap.entrySet();
//            for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
//                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
//                method.addRequestHeader(entry.getKey(),entry.getValue());
//            }
//        }
        String retstring=null;
        try {
            int result = client.executeMethod(method);

            if (result == HttpStatus.SC_OK) {
                retStream = method.getResponseBodyAsStream();
            } 
        } catch (IOException e) {
            e.printStackTrace();
            retstring="-1";
        } 
        return retStream;
    }
    
    public static void main(String[]args) throws Exception {
        
//        getsFetch4Page();
        
        String url = "https://10.10.50.13/app/oauth/verify.do";
        url = "https://10.10.50.13/main/index";
        Map<String, String> requestHeadMap = null;
        InputStream is = getRequestInputStream(url, requestHeadMap, "UTF-8", 5000, 5000);
        System.out.println("-----:" +  is);
        
        if(true) return ; 
        
        String ss = "TaskAllotMapper.questionDiagChildPage";
        System.out.println(ss.matches(".*listPage.*|.*Page.*|.*ListPage.*"));
        
        
        if(true) return ;
//    	ResourceLoader  resourceLoader = new DefaultResourceLoader();
//    	Resource r = resourceLoader.getResource("excel/11.xls");
//    	HSSFWorkbook s =  new HSSFWorkbook(r.getInputStream());
//    	System.out.println(resourceLoader);
    	HSSFWorkbook workbook = new HSSFWorkbook();
    	Sheet s =  workbook.createSheet();
		HSSFCellStyle contentStyle = workbook.createCellStyle(); //内容样式
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		contentStyle.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
		Row  r = s.createRow(0);
		Cell cell = r.createCell(0);
		cell.setCellStyle(contentStyle);
		File f = new File("D:/22.xls");
//		workbook.write(f);
    }
}

