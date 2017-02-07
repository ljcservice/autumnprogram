package com.ts.plugin;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * 初始化bean执行
 */
@Service
public class InitializingRunBean implements InitializingBean, DisposableBean {
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		TsThreadPoolExecutor.buildExecutor();
	}
	
	@Override
	public void destroy() throws Exception {
		TsThreadPoolExecutor.shutdownNow();
	}
}
