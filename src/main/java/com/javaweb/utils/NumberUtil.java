package com.javaweb.utils;

public class NumberUtil {
	public static boolean isNumber(String value) {
		return value != null && value.matches("\\d+");
	}
}
