package com.siweidg.comm.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

public class BeanUtils extends org.springframework.beans.BeanUtils {
	public static void copyProperties(Object source, Object target)
			throws BeansException {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source
						.getClass(), targetPd.getName());
				if (sourcePd != null) {
					try {
						Method readMethod;
						if (sourcePd.getReadMethod() != null) {
							readMethod = sourcePd.getReadMethod();
						} else if (sourcePd.getReadMethod() == null
								&& sourcePd.getPropertyType().toString()
										.equals("class java.lang.Boolean")) {
							// modified by Squid Hex: 处理一下Boolean型的get方法无法取得的问题
							Class<?> c = source.getClass();
							String capFieldName = sourcePd.getName().substring(
									0, 1).toUpperCase()
									+ sourcePd.getName().substring(1);
							readMethod = c.getMethod("is" + capFieldName);
						} else {
							continue;
						}
						if (!Modifier.isPublic(readMethod.getDeclaringClass()
								.getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						// 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
						if ((value != null && !(value instanceof String) ) || ( (value instanceof String)&& StringUtils.isNotBlank(value.toString())) ) {
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod
									.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
					} catch (Throwable ex) {
						throw new FatalBeanException(
								"Could not copy properties from source to target : "+targetPd.getName(),
								ex);
					}
				}
			}
		}
	}

	public static void sumProperties(Map<String, Object> map, Object source, String... sumCol) throws BeansException {
		for (String col : sumCol) {
			PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), col);
			if (sourcePd != null && sourcePd.getReadMethod() != null) {
				try {
					Method readMethod = sourcePd.getReadMethod();
					if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
						readMethod.setAccessible(true);
					}
					Object value = readMethod.invoke(source);
					if (value != null) {
						if (map.get(col) != null) {
							map.put(col, Double.parseDouble(map.get(col).toString())
									+ Double.parseDouble(value.toString()));
						} else {
							map.put(col, Double.parseDouble(value.toString()));
						}
					}
				} catch (Throwable ex) {
					throw new FatalBeanException("Could not read properties from source to target", ex);
				}
			}
		}
	}

}
