package com.siweidg.comm.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DeptLimitUtils
{
  public static Map convertBean(Object bean, String prefix)
    throws IntrospectionException, IllegalAccessException, InvocationTargetException
  {
    Class type = bean.getClass();
    Map returnMap = new HashMap();
    BeanInfo beanInfo = Introspector.getBeanInfo(type);

    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
    for (int i = 0; i < propertyDescriptors.length; i++) {
      PropertyDescriptor descriptor = propertyDescriptors[i];
      String propertyName = descriptor.getName();
      if (!propertyName.equals("class")) {
        Method readMethod = descriptor.getReadMethod();
        Object result = readMethod.invoke(bean, new Object[0]);

        if (result != null)
        {
          if (isEntityType(result))
          {
            returnMap.putAll(convertBean(result, prefix + propertyName + "."));
          }
          else
          {
            returnMap.put(prefix + propertyName, result);
          }
        }
        else {
          returnMap.put(prefix + propertyName, "");
        }
      }
    }

    return returnMap;
  }

  public static Map convertBean(Object bean, int x)
    throws IntrospectionException, IllegalAccessException, InvocationTargetException
  {
    Map returnMap = new HashMap();

    Class type = bean.getClass();

    BeanInfo beanInfo = Introspector.getBeanInfo(type);

    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
    for (int i = 0; i < propertyDescriptors.length; i++) {
      PropertyDescriptor descriptor = propertyDescriptors[i];
      String propertyName = descriptor.getName();
      if (!propertyName.equals("class")) {
        Method readMethod = descriptor.getReadMethod();
        Object result = readMethod.invoke(bean, new Object[0]);

        if (result != null)
        {
          if (isEntityType(result))
          {
            if (x < 2) {
              returnMap.put(propertyName, convertBean(result, x + 1));
            }

          }
          else if ((result instanceof Collection)) {
            if (x < 2)
            {
              returnMap.put(propertyName, convertCollection((Collection)result, x + 1));
            }
          }
          else {
            returnMap.put(propertyName, result);
          }
        }
        else
        {
          returnMap.put(propertyName, "");
        }
      }
    }

    return returnMap;
  }

  public static List convertCollection(Collection list, int x)
    throws IntrospectionException, IllegalAccessException, InvocationTargetException
  {
    List list2 = new ArrayList();

    for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { 
    	Object bean = localIterator.next();

      Map map = convertBean(bean, x + 1);
      list2.add(map);
    }

    return list2;
  }

  private static boolean isEntityType(Object object)
  {
    if ((object instanceof String))
    {
      return false;
    }
    if ((object instanceof Long))
    {
      return false;
    }
    if ((object instanceof Integer))
    {
      return false;
    }
    if ((object instanceof Date))
    {
      return false;
    }
    if ((object instanceof Collection))
    {
      return false;
    }
    if ((object instanceof Boolean))
    {
      return false;
    }
    if ((object instanceof Double))
    {
      return false;
    }
    return true;
  }
}