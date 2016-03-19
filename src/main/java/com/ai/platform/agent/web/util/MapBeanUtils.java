package com.ai.platform.agent.web.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MapBeanUtils {
	public static Logger log = LogManager.getLogger(MapBeanUtils.class);
			
	public static Map<String, String> bean2map(Object javaBean) {
		Map<String, String> result = new HashMap<String, String>();
		Method[] methods = javaBean.getClass().getDeclaredMethods();
		for (Method method : methods) {
			try {
				if (method.getName().startsWith("get")) {
					String field = method.getName();
					field = field.substring(field.indexOf("get") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1); // 转换成小写
					Object value = method.invoke(javaBean, (Object[]) null);
					if (value != null) {
						result.put(field, value.toString());
					}
				}
			} catch (Exception e) {
			}
		}
		return result;
	}
	/**
	 * 
	 * @Company asiainfo.com
	 * @Author zhangzhongde
	 * @Date 2016年3月19日 下午4:20:27
	 * @Describtion 普通getter and setter方式 根据类型判断转换相应的属性信息
	 */
	public static void map2bean(Object javabean, Map<String, String> map) {
//		for (Entry<String, String> entry : System.getenv().entrySet()) {
//		  log.info("---->"+entry.getKey() + ": " + entry.getValue());
//		}
		Method[] methods = javabean.getClass().getDeclaredMethods();
		for (Method method : methods) {
			try {
				if (method.getName().startsWith("set")) {
					String field = method.getName();
					field = field.substring(field.indexOf("set") + 3);
					String getterName="get"+field;
					field = field.toLowerCase().charAt(0) + field.substring(1);
					String value=map.get(field);
					//System.getenv();
					Method getter=javabean.getClass().getMethod(getterName);
					String methodType=getter.getReturnType().getName();
					Object[] cache = new Object[1];
					if(methodType.equalsIgnoreCase("long")) {
						cache[0] = new Long(value);
					} else if(methodType.equalsIgnoreCase("int")||methodType.equalsIgnoreCase("java.lang.Integer")) {
						cache[0] = new Integer(value);
					} else if(methodType.equalsIgnoreCase("short")) {
						cache[0] = new Short(value);
					} else if(methodType.equalsIgnoreCase("float")) {
						cache[0] = new Float(value);
					} else if(methodType.equalsIgnoreCase("double")) {
						cache[0] = new Double(value);
					} else if(methodType.equalsIgnoreCase("boolean")) {
						cache[0] = new Boolean(value);
					} else if(methodType.equalsIgnoreCase("java.lang.String")) {
						cache[0] = value;
					} else if(methodType.equalsIgnoreCase("java.io.InputStream")) {
						
					} else if(methodType.equalsIgnoreCase("char")) {
						cache[0] = (Character.valueOf(value.charAt(0)));
					}
					method.invoke(javabean, cache);
				}
			} catch (Exception e) {
			}
		}
	}
	/**
	 * 
	 * @Company asiainfo.com
	 * @Author zhangzhongde
	 * @Date 2016年3月19日 下午4:13:33
	 * @Describtion 泛型实现map2bean
	 */
	public static <T> T map2Bean(Map<String, String> map, Class<T> beanClass) {  
        T bean = null;  
        try {  
            bean = beanClass.newInstance();  
            BeanUtils.populate(bean, map);  
        } catch (InstantiationException e) {  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        } catch (InvocationTargetException e) {  
            e.printStackTrace();  
        }  
        return bean;  
    }  
	
}
