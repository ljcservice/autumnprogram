package com.ts.service.cache;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ts.dao.redis.RedisDao;
import com.ts.util.Tools;


@Component
public class CacheTemplate {
    @Autowired
    private RedisDao redisDao;
    
	private static final Logger log = Logger.getLogger(CacheTemplate.class);

	public <T> T cache(String keyV, boolean useCache,int seconds, CacheProcessor<T> proccessor) throws Exception {
		return this.cache(keyV, useCache,null, seconds, proccessor);
	}

	public <T> T cache(String keyV, boolean useCache,String keyAppend, CacheProcessor<T> proccessor) throws Exception {
		return this.cache(keyV,useCache, keyAppend, 1 * 24 * 60 * 60, proccessor);
	}
	
	/**
	 * 默认使用缓存
	 * @param keyV
	 * @param proccessor
	 * @return
	 * @throws Exception 
	 */
	public <T> T cache(String keyV, CacheProcessor<T> proccessor) throws Exception {
		return this.cache(keyV,true, null, 1 * 24 * 60 * 60, proccessor);
	}
	/**
	 * 默认使用缓存
	 * @param keyV
	 * @param keyAppend
	 * @param proccessor
	 * @return
	 * @throws Exception 
	 */
	public <T> T cache(String keyV, String keyAppend ,CacheProcessor<T> proccessor) throws Exception {
		return this.cache(keyV,true, keyAppend, 1 * 24 * 60 * 60, proccessor);
	}
	
	public <T> T cache(String keyV,boolean useCache, CacheProcessor<T> proccessor) throws Exception {
		return this.cache(keyV,useCache, null, 1 * 24 * 60 * 60, proccessor);
	}

	@SuppressWarnings("unchecked")
	public <T> T cache(String key,boolean useCache, String keyAppend, int seconds, CacheProcessor<T> proccessor) throws Exception {
		String keyName = "KEY: # " + key + (keyAppend == null ? "" : ("," + keyAppend)) + " #";
		boolean cacheFrt = false;
		if( useCache ){
			log.debug(keyName + ",start");
			cacheFrt = true;	
		}
		StopWatch watch = new StopWatch();
		watch.start();
		if (cacheFrt) {
			try {
				Object cacheObj = null;
				if (!Tools.isEmpty(key)) {
					cacheObj = redisDao.getObject(key);
				}
				if (cacheObj != null) {
					log.info(" from cache, key:"+key + ",query cache cost => " + watch.getTime() + "ms");
					return (T) cacheObj;
				}
			} catch (Exception e) {
				log.error(keyName + ",from  cache is error", e);
			}
		}

		T ret = proccessor.handle();
		watch.stop();
		log.debug(keyName + ",query cost => " + watch.getTime() + "ms");
		if (cacheFrt) {
			try {
				if (!Tools.isEmpty(key)) {
					Object obj = ret;
//					DiamondJedisManager.setObject(key, seconds, obj);
					redisDao.setObject(key,  seconds, obj);
				}
			} catch (Exception e) {
				log.error(keyName + ",set cache is error", e);
			}
		}
		log.debug(keyName + ",end");
		return ret;
	}

}
