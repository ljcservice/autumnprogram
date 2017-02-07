package com.ts.test;

import java.util.ResourceBundle;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class JedisResource {
	private static JedisPool	pool;
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("redis");
		if (bundle == null) {
			throw new IllegalArgumentException("[redis.properties] is not found!");
		}
		JedisPoolConfig config = new JedisPoolConfig();
//		config.setMaxActive (Integer.valueOf(bundle.getString("redis.pool.maxActive")));
		config.setMaxIdle(Integer.valueOf(bundle.getString("redis.maxIdle")));
		config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.maxWait")));
		config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.testOnBorrow")));
//		config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.testOnReturn")));
		pool = new JedisPool(config, bundle.getString("redis.host"), Integer.valueOf(bundle.getString("redis.port")));
	}
	
	public static JedisPool getPool() {
		return pool;
	}
}
