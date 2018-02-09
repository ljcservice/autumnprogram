package com.ts.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.slf4j.LoggerFactory;

public class CommonUtils {

	// public static String sPath = System.getProperty("user.dir");
    public static org.slf4j.Logger logger = LoggerFactory.getLogger(CommonUtils.class);
	public static InputStream in = null;

	public static OutputStream out = null;

	public static void createFolder(String path) {
		File file = new File(path);
		// 如果文件夹不存在则创建
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
	}

	public static String getPropertiesValue(String proPath, String value) {
		Properties prop = new Properties();
		String result = null;
		String sPath = getAbsolutePath();
		logger.info("系统路径================"+sPath);
		try {
			in = new FileInputStream(sPath + File.separator + proPath);
			prop.load(in);
			result = prop.getProperty(value).trim();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String getPropValue(String proPath, String value) {
		Properties prop = new Properties();
		String result = null;
		String sPath = getAbsolutePath();
//		logger.info("系统路径================"+sPath);
		try {
//			in = new FileInputStream(sPath + File.separator + proPath);
			in = CommonUtils.class.getClassLoader().getResourceAsStream(proPath);
			prop.load(in);
			//System.out.println("value=======>>>"+value);
			result = prop.getProperty(value).trim();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void editPropertiesValue(String proPath, String pKey, String pValue) {
		Properties prop = new Properties();
		String sPath = getAbsolutePath();
		try {
			in = new FileInputStream(sPath + File.separator + proPath);
			// 从输入流中读取属性列表（键和元素对）
			prop.load(in);
			// 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
			// 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
			out = new FileOutputStream(sPath + File.separator + proPath);
			prop.setProperty(pKey, pValue);
			// 以适合使用 load 方法加载到 Properties 表中的格式，
			// 将此 Properties 表中的属性列表（键和元素对）写入输出流
			prop.store(out, "Update " + pKey + " name");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean folderIsEmpty(String path) {
		boolean result = true;
		File file = new File(path);
		if (file.isDirectory()) {
			String[] files = file.list();
			if (files.length > 0) {
				result = false;
			}
		}
		return result;
	}

	public static String getAbsolutePath() {
		return System.getProperty("user.dir");
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}
	
	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
	}
}
