package com.ts.service.cache;

public interface CacheProcessor<T> {

	/**
	 * 具体的业务逻辑，获取结果T
	 * 
	 * @return
	 */
	T handle();
}
