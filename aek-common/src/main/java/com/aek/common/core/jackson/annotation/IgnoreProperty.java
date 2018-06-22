package com.aek.common.core.jackson.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于注解json过滤pojo内的属性，其他的属性都会被序列化成字符串 
 *
 * @author HongHui
 * @date   2017年7月20日
 */
@Documented  
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
public @interface IgnoreProperty {

	/** 
     * 要忽略字段的POJO
     * @return 
     */  
    Class<?> pojo();  
  
    /** 
     * 要忽略的字段名 <br> 
     * @return 
     */  
    String[] name();  
  
    /** 
     * 字段名，无论是哪种 <br> 
     * @return 
     */  
    //  String value() default "";  
  
    /** 
     * 最大迭代层次
     * 当注解了pojo和name值时，该值表示遍历bean属性的最大曾次数，此注解一般用于自关联的bean类， 
     * 如果循环层次大于等于maxLevel时则不再读取属性
     * 如果maxIterationLevel为0，则不限制迭代层次
     * 如果maxIterationLevel为1，则迭代读取属性一次
     * @return 
     */  
    //  int maxIterationLevel() default 0;  
    
}
