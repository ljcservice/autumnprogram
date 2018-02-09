package com.ts.service.cache;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ts.dao.redis.RedisDao;
import com.ts.util.SizeOfObject;
import com.ts.util.Tools;

import oracle.net.aso.l;


/**
 * 
 * @author silong.xing
 *
 */
@Component
public class CacheTemplate {
    @Autowired
    private RedisDao redisDao;
    
    public static ConcurrentMap<String, String>  map = new ConcurrentHashMap<String ,String>(); 
    
	private static final Logger log = Logger.getLogger("CacheTemplate");

	public <T> T cache(String keyV, CacheProcessor<T> proccessor) throws Exception {
		return this.cache(keyV, null,true, 1 * 24 * 60 * 60, proccessor);
	}
	public <T> T cache(String keyV,int seconds, CacheProcessor<T> proccessor) throws Exception {
		return this.cache(keyV, null,true, seconds, proccessor);
	}
	public <T> T cache(String keyV,boolean useCache, CacheProcessor<T> proccessor ) throws Exception {
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
//			log.debug(keyName + ",start");
			cacheFrt = true;	
		}
		StopWatch watch = new StopWatch();
		watch.start();
		if (cacheFrt) {
			try {
				Object cacheObj = null;
				if (!Tools.isEmpty(keyName)) {
				    //  从快照中获得 key 增加快照信息 只存放 key 
			        if(map.containsKey(keyName))
			            cacheObj = redisDao.getObject(keyName);
				}
				if (cacheObj != null) {
//					log.info(" from cache, key:"+key + ",query cache cost => " + watch.getTime() + "ms");
//				    log.debug(" from cache, key:"+key + ",query cache cost => " + watch.getTime() + "ms");
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
					// 增加快照缓存 
					addLocadCacheByKey(keyName);
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
	public String setObject(String key, String keyAppend, int seconds, Object obj)  {
		String keyName = "KEY:# " + key + (keyAppend == null ? "" : ("," + keyAppend)) + " #";
		try
        {
		    // 增加快照 缓存信息 
		    addLocadCacheByKey(keyName);
            return redisDao.setObject(keyName, seconds, obj);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            log.info("设置错误",e);
        }
		return "";
	}
	public String setObject(String key, int seconds, Object obj)  {
	       return setObject(key,null, seconds, obj);
	}
	
	/**
	 * 从缓存获取数据，存放什么类型取出时强转什么类型
	 * @param key
	 * @return Object
	 */
	public Object getObject(String key,String keyAppend) {
		String keyName = "KEY:# " + key + (keyAppend == null ? "" : ("," + keyAppend)) + " #";
		// 增加 快照过滤 
		if(!map.containsKey(keyName)) return null;
		return redisDao.getObject(keyName);
	}
	public Object getObject(String key) {
		return getObject(key,null) ;
	}
	
	/**
	 * 加载 快照缓存 key
	 * @throws Exception
	 */
	public void loalCache() throws Exception
	{
	    
	    long l = System.currentTimeMillis();
//	    System.gc();
//	    long total=Runtime.getRuntime().totalMemory();// byte
//	    long m1=Runtime.getRuntime().freeMemory();
//	    System.out.println("before:"+(total-m1));
	    for(String s : getKeys(""))
	    {
	        map.put(s, s);
	    }
	    log.debug("加载时间:" + (System.currentTimeMillis() - l));
	    
//	    String key = "KEY:# DiseageVsDiag,M80.884M #";
//	    long  l = System.currentTimeMillis();
//	    map.containsKey(key);
//	    log.debug(System.currentTimeMillis() - l);
//	    l = System.currentTimeMillis();
//	    cache("DiseageVsDiag","M80.884M",null);
//        log.debug(System.currentTimeMillis() - l);
        
//        long total1 = Runtime.getRuntime().totalMemory();
//        long m2=Runtime.getRuntime().freeMemory();
//        System.out.println("after:" +(total1 - m2));

	}
	
	/**
	 * 增加快照key
	 * @param key
	 */
	private void addLocadCacheByKey(String key)
	{
	    map.put(key, key);
	}
	
	private void delLocalCacheByKey(String key)
	{
	    map.remove(key);
	}
	
	private void delLocalCacheByKeys(String val)
	{
	    for(String k : getKeys(val))
	    {
	        delLocalCacheByKey(k);
	    }
	}
	
	/**
	 * 获得key
	 * @param val 关键 键值 
	 * @return
	 */
	public List<String> getKeys(String val)
	{
	    String[] ks = redisDao.getListByKey(val);
	    return Arrays.asList(ks); 
	}
	
	/**
	 * 删除单个key 
	 * @param val
	 */
	public void delKey(String key)
	{
	    redisDao.delete(key);
	    //删除本地快照 
	    delLocalCacheByKey(key);
	}
	
	/**
	 * 删除多个key
	 * @param val 关键 键值 
	 */
	public void delKeys(String val)
	{
	    redisDao.deletes(val); 
	    // 删除本地快照 
	    delLocalCacheByKeys(val);
	}
	
}
