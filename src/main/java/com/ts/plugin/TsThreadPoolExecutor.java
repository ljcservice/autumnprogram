package com.ts.plugin;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class TsThreadPoolExecutor {
	
	private static final Logger log=Logger.getLogger(TsThreadPoolExecutor.class);
	
	//线程池
	public static int MAX_THREAD_NUMS = 3;
	public static int CORE_THREAD_NUMS = 1;
	public static long KEEP_ALIVE_TIME = 0L;
	public static int WORKTASK_QUEUE_LENGTH = 10;
	public static ThreadPoolExecutor threadPoolExecutor;
	
	public static void buildExecutor() {
		if (threadPoolExecutor == null) {
			threadPoolExecutor = new ThreadPoolExecutor(
					CORE_THREAD_NUMS,
					MAX_THREAD_NUMS, 
					KEEP_ALIVE_TIME, 
					TimeUnit.MINUTES,
					new LinkedBlockingQueue(WORKTASK_QUEUE_LENGTH),
					new CallerRunsPolicy());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				log.error(e,e);
			}
		}
	}
	
	public static void shutdownNow() {
		try{
			threadPoolExecutor.shutdownNow();
		}catch( Exception e ){
			log.error(e,e);
		}
	}
}
