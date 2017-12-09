package wang.igood.gmvc.util;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import wang.igood.gmvc.context.RequestContext;

public class ParamUtil {
	public static Map<String, String[]> getParams(RequestContext beat) {
		return beat.getRequest().getParameterMap();
	}

	public static String getString(RequestContext beat, String key) {
		return beat.getRequest().getParameter(key);
	}

	public static int getInt(RequestContext beat, String key, int def) {
		String value = beat.getRequest().getParameter(key);
		if (value == null || value.equals("")) {
			return def;
		}

		return Integer.parseInt(value);
	}

	public static long getLong(RequestContext beat, String key, long def) {
		String value = beat.getRequest().getParameter(key);
		if (value == null || value.equals("")) {
			return def;
		}

		return Long.parseLong(value);
	}

	public static <T> T getBean(RequestContext context, Class<T> c) {
		try {
			HttpServletRequest request = context.getRequest();
			T obj = c.newInstance();
			Map<String, String[]> enu = request.getParameterMap();
			SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
			Field[] fields = c.getDeclaredFields();
			for(Field field : fields) {
				field.setAccessible(true);
				if(enu.containsKey(field.getName())) {
					String value = request.getParameter(field.getName());
					switch (field.getType().getSimpleName()) {
					case "Integer":
					case "int":
						field.set(obj, Integer.parseInt(value));
						break;
					case "Float":
					case "float":
						field.set(obj, Float.parseFloat(value));
						break;
					case "Double":
					case "double":
						field.set(obj, Double.parseDouble(value));
						break;
					case "Long":
					case "long":
						field.set(obj, Long.parseLong(value));
						break;
					case "Byte":
					case "byte":
						field.set(obj, Byte.parseByte(value));
						break;
					case "Short":
					case "short":
						field.set(obj, Short.parseShort(value));
						break;
					case "Boolean":
					case "boolean":
						field.set(obj, Boolean.parseBoolean(value));
						break;
					case "Date":
						try {
							 if (value.length() > 10) {  
						        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
						    } else {  
						        sdf = new SimpleDateFormat("yyyy-MM-dd");  
						    }  
							field.set(obj, sdf.parse(value));	
						}catch(Exception e) {
							e.printStackTrace();
						}
						break;
					default:
						field.set(obj, value);
						break;
					}
				}
			}
			return obj;

		} catch (InstantiationException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	public static <T> T getBeanByMap(Map<String,String> map, Class<T> c) {
		try {
			T obj = c.newInstance();
			Field[] fields = c.getDeclaredFields();
			for(Field field : fields) {
				field.setAccessible(true);
				if(map.containsKey(field.getName())) {
					String value = map.get(field.getName());
					if(!wang.igood.gmvc.util.StringUtils.isEmpty(value)) {
						switch (field.getType().getSimpleName()) {
						case "Integer":
						case "int":
							field.set(obj, Integer.parseInt(value));
							break;
						case "Float":
						case "float":
							field.set(obj, Float.parseFloat(value));
							break;
						case "Double":
						case "double":
							field.set(obj, Double.parseDouble(value));
							break;
						case "Long":
						case "long":
							field.set(obj, Long.parseLong(value));
							break;
						case "Boolean":
						case "boolean":
							field.set(obj, Boolean.parseBoolean(value));
							break;
						default:
							field.set(obj, value);
							break;
						}
					}
				}
			}
			return obj;

		} catch (InstantiationException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
