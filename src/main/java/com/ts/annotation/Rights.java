package com.ts.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Rights
{
	/**
	 * 功能编码，内容需要与系统中权限管理中的编码一致，不需要带.do和.action
	 * 正确格式如：user/list 
	 */
    public abstract String code() default "";

    /**
     * 请求类型，正常action、ajax
     * 为action时无权限则跳转到无权限页面，为ajax无权限时，则按照dataType类型输出“当前用户无该功能权限！”
     */
    public abstract String type() default "action";

    /**
     * ajax 请求时该字段有效。用于判断需求的返回值，如果是text则返回文本字符串“当前用户无该功能权限”，如果是json则返回键与值[reslut:'当前用户无该功能权限！']
     * 默认text
     */
    public abstract String dataType() default "text";

    /**
     * 如果为false则不进行权限校验
     * 默认为true
     */
    public abstract boolean required() default true;
    

}