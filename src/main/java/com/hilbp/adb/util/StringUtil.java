package com.hilbp.adb.util;

public class StringUtil {
	
	public static boolean isEmpty(String str) {
		
		if(str == null || str.length() == 0)
			return true;
		return false;
	}
	
	public static boolean isNotEmpty(String str) {
		
		return !isEmpty(str);
	}
}
