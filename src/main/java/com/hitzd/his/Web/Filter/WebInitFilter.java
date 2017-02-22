package com.hitzd.his.Web.Filter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


/**
 * 用来初始化 并 拦截基本 页面
 * @author Administrator
 *
 */
public class WebInitFilter implements Filter {
	static{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);//23点初始化
		long millisOfDay = 24*3600*1000;
		long delayMillis = calendar.getTimeInMillis()-Calendar.getInstance().getTimeInMillis();		
		ScheduledExecutorService  service = Executors.newScheduledThreadPool(1);
		service.scheduleAtFixedRate(new LogTask(),delayMillis,millisOfDay,TimeUnit.MILLISECONDS);
		new Thread(new LogTask()).start();
	}

    public WebInitFilter() 
    {
    	
    }
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException
	{
		// TODO: 增加加密狗验证
	    HttpServletRequest request = (HttpServletRequest) req;
	    if(request.getRequestURI().indexOf("/services/") == -1)
	    {
	    	request.setCharacterEncoding("gbk");
		    res.setCharacterEncoding("gbk");	
	    }
		chain.doFilter(request, res);
	}
	@Override
	public void destroy() {	}
	/**
	 * 日志输出回记录所有控制台输出日志
	 * 初始化日志输出
	 * 1.清空时间为每天晚上23:00~24:00
	 * 2.文件名称：out.log; err.log
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException 
	{
	}
}
class TeeStream extends PrintStream
{
    PrintStream out;
    
    public TeeStream(PrintStream out1, PrintStream out2) 
    {
        super(out1);
        this.out = out2;
    }
    @Override
	public void println(String x) {
    	out.println(x);
		super.println(x);
	}
    @Override
	public void print(String x) {
    	out.print(x);
		super.print(x);
	}
	public void flush() 
    {
        super.flush();
        out.flush();
    }
}
class LogTask implements Runnable
{
	@Override
	public void run()
	{
		try 
		{			
			System.out.println("===========================控制台输出日志记录清空==========================================");
	        PrintStream out = new PrintStream(new FileOutputStream("out.log"));
	        PrintStream tee = new TeeStream(System.out, out);
	        System.setOut(tee);
	        
	        PrintStream err = new PrintStream(new FileOutputStream("err.log"));
	        tee = new TeeStream(System.err, err);
	        System.setErr(tee);
	        System.out.println("===========================控制台日志记录中，文件[输出:out.log][错误:err.log]==============================");
	    }
		catch (FileNotFoundException e) 
	    {
	    	e.printStackTrace();
	    }
	}
}
