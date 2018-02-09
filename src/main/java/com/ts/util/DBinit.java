package com.ts.util;


/**
 * 初始化，模拟加载所有的配置文件
 * 
 * @author Ran
 * 
 */
public class DBinit {
	// 初始化
	public static Thread dbSKInit() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				IConnectionPool pool = initSKPool();
				while (pool == null || !pool.isActive()) {
					pool = initSKPool();
				}
			}
		});
		return t;
	}

	public static IConnectionPool initSKPool() {
		return ConnectionPoolManager.getInstance().getPool("skPool");
	}

}
