package com.aek.common.core.jackson.impl;

import java.lang.reflect.Method;

/**
 * 过滤属性处理器
 *	
 * @author HongHui
 * @date   2017年7月20日
 */
public interface FilterPropertyHandler {
	
	/**
	 * 通过传入调用方法和返回值过滤属性
	 * @param method 调用方法
	 * @param object 方法返回值
	 * @return       过滤后属性的值
	 */
	public Object filterProperties(Method method, Object object);
	
}
