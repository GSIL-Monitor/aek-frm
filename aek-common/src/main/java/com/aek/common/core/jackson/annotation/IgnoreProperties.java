package com.aek.common.core.jackson.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreProperties {
	/**
	 * 要过滤的属性
	 *
	 * @return
	 */
	IgnoreProperty[] value() default @IgnoreProperty(pojo = Object.class, name = "");

	/**
	 * 允许的属性
	 *
	 * @return
	 */
	AllowProperty[] allow() default @AllowProperty(pojo = Object.class, name = "");
}
