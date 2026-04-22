package com.javaweb.utils;

import java.util.Map;

public class MapUtil {
	public static <T> T getObject(Map<String, Object> params, String key, Class<T> tClass) {
//		Object obj = params.getOrDefault(key, null);
//		if (obj != null && !obj.toString().isEmpty()) {
//			if (tClass.getName().equals("java.lang.Long")) {
//				obj = Long.valueOf(obj.toString());
//			} else if (tClass.getName().equals("java.lang.Integer")) {
//				obj = Integer.valueOf(obj.toString());
//			} else if (tClass.getName().equals("java.lang.String")) {
//				obj = obj.toString();
//			}
//			return tClass.cast(obj);
//		}
		Object obj = params.get(key); // getOrDefault(key, null) thực ra bằng get(key)
		if (obj == null || obj.toString().trim().isEmpty()) {
			return null;
		}
		String valStr = obj.toString().trim(); // Trim để loại bỏ khoảng trắng thừa
		if (tClass == Long.class) {
			return tClass.cast(Long.valueOf(valStr));
		}
		if (tClass == Integer.class) {
			return tClass.cast(Integer.valueOf(valStr));
		}
		if (tClass == String.class) {
			return tClass.cast(valStr);
		}
		return null;
	}
}