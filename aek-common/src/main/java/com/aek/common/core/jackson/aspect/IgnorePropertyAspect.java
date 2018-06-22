package com.aek.common.core.jackson.aspect;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import com.aek.common.core.jackson.annotation.IgnoreProperties;
import com.aek.common.core.jackson.impl.FilterPropertyHandler;
import com.aek.common.core.jackson.impl.JavassistFilterPropertyHandler;

/**
 * 启动mvc对aop的支持,使用aspectj代理
 *	
 * @author HongHui
 * @date   2017年7月20日
 */
//@Aspect
//@Component
//@EnableAspectJAutoProxy(proxyTargetClass = true)
public class IgnorePropertyAspect {

	private static final Logger logger = LogManager.getLogger(IgnorePropertyAspect.class);

	public IgnorePropertyAspect() {
	}

	@Pointcut("@annotation(com.aek.common.core.jackson.annotation.IgnoreProperties)")
	private void anyMethod() {

	}

	@Around("anyMethod()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		Object returnVal = pjp.proceed(); // 返回源结果
		try {
			FilterPropertyHandler filterPropertyHandler = new JavassistFilterPropertyHandler(true);
			Method method = ((MethodSignature) pjp.getSignature()).getMethod();
			// 方法标注过滤的才进行过滤
			if (method.isAnnotationPresent(IgnoreProperties.class)) {
				returnVal = filterPropertyHandler.filterProperties(method, returnVal);
			}
		} catch (Exception e) {
			logger.error("filter json error.", e);
		}
		return returnVal;
	}

	// @AfterThrowing(pointcut = "anyMethod()", throwing = "e")
	// public void doAfterThrowing(Exception e) {
	// logger.error("filter json exception.", e);
	// }
	
}
