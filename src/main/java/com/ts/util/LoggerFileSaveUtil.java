package com.ts.util;

import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.varia.LevelRangeFilter;

/**
 * 此工具类只适合在 IReportBuilder 的子类中使用
 * 用做将抓取类的日志保存在日志文件中
 * @author IvyLea
 *
 */
public class LoggerFileSaveUtil {
	
	/**
	 * 建议在buildBegin 方法中使用此方法 或者构造方法
	 * @param logger 此参数建议在类的成员变量中按照后面缩写的方法法初始化：  private Logger logger = Logger.getLogger(ClassName.class);
	 * @param filePathAndName 日志将要保存的相对路径 例如药品日报的日志保存地址为 ：
	 * APPLOG\\dr\\DR_#ADate#.log 其中两个#号中间的是构建日报的日期，这样做的目的是将日期加入日志名称中来区分每天的日志
	 */
	public  static void LogFileSave(Logger logger,String filePathAndName) {
		String loggerName = logger.getName();
		String currentClassName =  loggerName.indexOf(".") > 0?loggerName.substring(loggerName.lastIndexOf(".") + 1):loggerName;
		if(null != logger.getAppender(currentClassName + "FileAppender")) {
			logger.getAppender(currentClassName + "FileAppender").close();
			logger.removeAppender(currentClassName + "FileAppender");
		}
		PatternLayout layout =  new  PatternLayout();
		layout.setConversionPattern("%d [%c] - %m%n");
		FileAppender appender =  null;
		try {
			appender = new FileAppender(layout,filePathAndName);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//本日志只记录INFO级别的
		LevelRangeFilter filter = new LevelRangeFilter();
		filter.setLevelMin(Level.INFO);
		filter.setLevelMax(Level.INFO);
		appender.addFilter(filter);
		appender.setName(currentClassName + "FileAppender");
		logger.addAppender(appender);
	}
}
