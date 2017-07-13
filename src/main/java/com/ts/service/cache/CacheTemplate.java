package com.ts.service.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ts.dao.redis.RedisDao;
import com.ts.util.Tools;


/**
 * 
 * @author silong.xing
 *
 */
@Component
public class CacheTemplate {
    @Autowired
    private RedisDao redisDao;
    
	private static final Logger log = Logger.getLogger(CacheTemplate.class);

	public <T> T cache(String keyV, CacheProcessor<T> proccessor) throws Exception {
		return this.cache(keyV, null,true, 1 * 24 * 60 * 60, proccessor);
	}
	public <T> T cache(String keyV,int seconds, CacheProcessor<T> proccessor) throws Exception {
		return this.cache(keyV, null,true, seconds, proccessor);
	}
	public <T> T cache(String keyV,boolean useCache, CacheProcessor<T> proccessor) throws Exception {
		return this.cache(keyV,null,useCache,  1 * 24 * 60 * 60, proccessor);
	}
	public <T> T cache(String keyV, boolean useCache,int seconds, CacheProcessor<T> proccessor) throws Exception {
		return this.cache(keyV,null, useCache, seconds, proccessor);
	}
	
	public <T> T cache(String keyV, String keyAppend ,int seconds,CacheProcessor<T> proccessor) throws Exception {
		return this.cache(keyV,keyAppend,true,  seconds, proccessor);
	}
	public <T> T cache(String keyV, String keyAppend ,CacheProcessor<T> proccessor) throws Exception {
		return this.cache(keyV,keyAppend,true,  1 * 24 * 60 * 60, proccessor);
	}
	public <T> T cache(String keyV, String keyAppend,boolean useCache, CacheProcessor<T> proccessor) throws Exception {
		return this.cache(keyV,keyAppend,useCache,  1 * 24 * 60 * 60, proccessor);
	}


	@SuppressWarnings("unchecked")
	public <T> T cache(String key,String keyAppend,boolean useCache,  int seconds, CacheProcessor<T> proccessor) throws Exception {
		String keyName = "KEY:# " + key + (keyAppend == null ? "" : ("," + keyAppend)) + " #";
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
				if (!Tools.isEmpty(keyName)) {
					cacheObj = redisDao.getObject(keyName);
				}
				if (cacheObj != null) {
					log.info(" from cache, key:"+key + ",query cache cost => " + watch.getTime() + "ms");
					return (T) cacheObj;
				}
			} catch (Exception e) {
				log.error(keyName + ",from  cache is error", e);
			}
		}

		if(proccessor  == null) {
			watch.stop();
			log.debug(keyName + ",query cost => " + watch.getTime() + "ms");
			return null;
		}
		T ret = proccessor.handle();
		watch.stop();
		log.debug(keyName + ",query cost => " + watch.getTime() + "ms");
		if (cacheFrt) {
			try {
				if (!Tools.isEmpty(keyName)) {
					Object obj = ret;
//					DiamondJedisManager.setObject(key, seconds, obj);
					redisDao.setObject(keyName,  seconds, obj);
				}
			} catch (Exception e) {
				log.error(keyName + ",set cache is error", e);
			}
		}
		log.debug(keyName + ",end");
		return ret;
	}

	/**
	 * 存放数据，存放什么类型取出时强转什么类型
	 * @param key	存储的key
	 * @param seconds 单位为秒
	 * @param obj	存储的值
	 * @return
	 */
	public String setObject(String key, String keyAppend, int seconds, Object obj) {
		String keyName = "KEY:# " + key + (keyAppend == null ? "" : ("," + keyAppend)) + " #";
		return redisDao.setObject(keyName, seconds, obj);
	}
	public String setObject(String key, int seconds, Object obj) {
		return setObject(key,null, seconds, obj);
	}
	
	/**
	 * 从缓存获取数据，存放什么类型取出时强转什么类型
	 * @param key
	 * @return Object
	 */
	public Object getObject(String key,String keyAppend) {
		String keyName = "KEY:# " + key + (keyAppend == null ? "" : ("," + keyAppend)) + " #";
		return redisDao.getObject(keyName);
	}
	public Object getObject(String key) {
		return getObject(key,null) ;
	}
	
	/**
	 * 获得
	 * @param val
	 * @return
	 */
	public List<String> getKeys(String val)
	{
	    String[] ks = redisDao.getListByKey(val);
	    return Arrays.asList(ks); 
	}
	
	/**
	 * 
	 * @param val
	 */
	public void delKey(String key)
	{
	    redisDao.delete(key);
	}
	
	/**
	 * 
	 * @param val
	 */
	public void delKeys(String val)
	{
	    redisDao.delete(getKeys(val));
	}
	
}
