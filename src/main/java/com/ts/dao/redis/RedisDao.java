package com.ts.dao.redis;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;


/** 
 * 请不要直接调用该类，请使用CacheTemplate类操作redis
 * @author silong.xing
 *
 */
public interface RedisDao {
	
	/**新增(存储字符串)
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean addString(String key, String value);
	
	/**拼接字符串
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean appendString(String key, String value);
	
	/**新增(存储Map)
	 * @param key
	 * @param map
	 * @return
	 */
	public String addMap(String key, Map<String, String> map);
	
	/**获取map
	 * @param key
	 * @return
	 */
	public Map<String,String> getMap(String key);
	
	/**新增(存储List)
	 * @param key
	 * @param list
	 * @return
	 */
	public void addList(String key, List<String> list);
	
	/**获取List
	 * @param key
	 * @return
	 */
	public List<String> getList(String key);
	
	/**
     *  使用的keys * 命令
     * @param pattern
     * @return
     */
    public String[] getListByKey(String pattern);
	
	/**新增(存储set)
	 * @param key
	 * @param set
	 */
	public void addSet(String key, Set<String> set);
	
	/**获取Set
	 * @param key
	 * @return
	 */
	public Set<String> getSet(String key);
	
	/**删除
	 * @param key
	 */
	public boolean delete(String key); 
	/**删除多个 
     * @param key
     */
    public boolean deletes(String key); 
	
	/**修改
	 * @param pd
	 * @return
	 */
	public boolean eidt(String key, String value);
	
	/**通过ket获取数据
	 * @param keyId
	 * @return
	 */
	public String get(String keyId);

	/**
	 * 存放数据，存放什么类型取出时强转什么类型
	 * @param key	存储的key
	 * @param seconds 单位为秒
	 * @param obj	存储的值
	 * @return
	 */
	public String setObject(String key, int seconds, Object obj) throws UnsupportedEncodingException;
	
	/**
	 * 从缓存获取数据，存放什么类型取出时强转什么类型
	 * @param key
	 * @return Object
	 */
	public Object getObject(String key) ;
}
