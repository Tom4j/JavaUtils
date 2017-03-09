package com.siweidg.comm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.siweidg.comm.init.Constants;


@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AddLog {
	Constants.LOG_TYPE type();
	String objectKey();
	String desc();
}
