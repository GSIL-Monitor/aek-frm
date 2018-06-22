package com.aek.common.core.jackson.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 只允许pojo内的属性序列化成json，对于同一个pojo该注解是与IgnoreProperty是冲突的。
 *	
 * @author HongHui
 * @date   2017年7月20日
 */
@Documented  
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
public @interface AllowProperty {

	/**
	 * 目标POJO
	 * @return
	 */
	Class<?> pojo();  
    
    Class<?>[] view() default { };
  
    /**
     * 允许序列化的属性名
     * @return
     */
    String[] name() default { };  
    
}
