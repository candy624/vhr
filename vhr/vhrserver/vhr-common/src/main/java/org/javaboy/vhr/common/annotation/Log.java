package org.javaboy.vhr.common.annotation;

import org.javaboy.vhr.common.enums.BusinessType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * Created by candy on 2020/11/2.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD}) //作用于参数或方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 模块
     */
    String title() default "";


    /**
     * 功能
     */
    BusinessType businessType() default BusinessType.OTHER;


}
