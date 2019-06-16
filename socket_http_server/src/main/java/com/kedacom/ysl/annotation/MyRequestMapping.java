package com.kedacom.ysl.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestMapping {
	/**
	 * 表示访问该方法的url
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 请求参数格式
	 * 
	 * @author 95488
	 * @createDate 2019年6月14日
	 * @return
	 */
	String consume() default "application/x-www-form-urlencoded";

	/**
	 * 请求方法
	 * 
	 * @author 95488
	 * @createDate 2019年6月16日
	 * @return
	 */
	String method() default "";

}
