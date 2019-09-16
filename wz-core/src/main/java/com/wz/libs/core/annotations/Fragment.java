package com.wz.libs.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/10/29
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Fragment {

    int value();

    boolean isPullRefesh() default false;
}
