package com.ts.test;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisTool {
	public static JedisPool pool = JedisResource.getPool();
    public static Jedis jedis = pool.getResource();
	public static void put(String key, String value) {
		jedis.set(key, value);
		//管道 发布
//		jedis.publish(key, value);
		pool.returnResource(jedis);
		
	}
	public static String get(String key) {
		return (String)jedis.get(key);
	}
	
	public static void putSet(String key) {
		long a = jedis.sadd("xxx", "111");//新增
		long b = jedis.sadd("xxx", "111");
		long c = jedis.sadd("xxx", "1112");
		Set<String> set = jedis.smembers("xxx");
		
		//jedis.expire("xxx", 3);//设置数据过期时间
		jedis.sadd("xxx", "111");
		//long cc = jedis.del("xxx");//移除set中的一个key
		Set<String> set2 = jedis.smembers("xxx");
		
		//jedis.scard("");//查询set总数长度
		//jedis.srem("xxx", "111");//移除
		String s = jedis.srandmember("xxx");
		System.out.println(a+" "+b);
	}
	
	public static void main(String[] args) {
//		JedisTool.put("a","aaa");		
//		System.out.println(JedisTool.get("a"));
		JedisTool.putSet("a");
	}
}
