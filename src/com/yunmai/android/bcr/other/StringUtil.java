/*
 * File Name: 		StringManager.java
 * 
 * Copyright(c) 2011 Yunmai Co.,Ltd.
 * 
 * 		 All rights reserved.
 * 					
 */

package com.yunmai.android.bcr.other;

import java.io.UnsupportedEncodingException;




/**
 * @purpose: A utility class to translate code set or string format between OS
 *           or platforms.
 * 
 * @author: chongxishen
 * 
 * @date: Jan. 2009
 */
public class StringUtil {
	
	private static final String TAG = "StringUtil";

	// ////////////////////////////////////////////////
	// Constructor/Destructor

	public StringUtil() {

	}

	@Override
	public void finalize() {

	}

	// //////////////////////////////////////////////////
	// Operations

	/**
	 * convert GBK into UNICODE
	 * 
	 * @param array
	 *            GBK code in byte array
	 * 
	 * @return UNICODE string, null if error occur
	 */
	public static String convertGbkToUnicode(byte[] array) {
		String str = null;

		try {
			// filter the 0xd
			byte[] text = filterAndCut(array);
			if (text != null)
				str = new String(text, "GBK");
			else
				str = "";
		} catch (UnsupportedEncodingException e) {
		}

		return str.trim();
	}

	public static String convertAscIIToUnicode(byte[] array) {
		String str = null;

		try {
			// filter the 0xd
			byte[] text = filterAndCut(array);
			if (text != null)
				str = new String(text, "ISO-8859-1");
			else
				str = "";
		} catch (UnsupportedEncodingException e) {
		}

		return str.trim();
	}

	public static String convertAscIIToUnicodeJP(byte[] array) {
		String str = null;

		try {
			// filter the 0xd
			byte[] text = filterAndCut(array);
			if (text != null)
				str = new String(text, "Shift_JIS");
			else
				str = "";
		} catch (UnsupportedEncodingException e) {
		}

		return str.trim();
	}
	
	/**
	 * in Windows, the string is end with "\r\n" for each row, but "\n" for
	 * Android, the filter parse the Windows' string into Android's
	 * 
	 * @param array
	 *            Windows' string format
	 * 
	 * @return Android's string
	 */
	public static byte[] filter(byte[] array) {
		int len = array.length;
		byte[] filter = new byte[len];
		for (int i = 0, cnt = 0; i < len; i++) {
			if (array[i] == (byte) 0xd)
				continue;
			filter[cnt++] = array[i];
		}
		return filter;
	}

	/**
	 * in Windows, the string is end with "\r\n" for each row, but "\n" for
	 * Android, the filter parse the Windows' string into Android's. At the same
	 * time, the string array is end of '\0' for non-UNICODE, so ignore the
	 * elements after '\0'
	 * 
	 * @param array
	 *            Windows' string format
	 * 
	 * @return Android's string
	 * 
	 * @remark suit for NON-UNICODE
	 */
	public static byte[] filterAndCut(byte[] array) {
		int len = strlen(array);
		if (len < 1)
			return null;
		byte[] filter = new byte[len];
		for (int i = 0, cnt = 0; i < len; i++) {
			if (array[i] == (byte) 0xd)
				continue;
			filter[cnt++] = array[i];
		}
		return filter;
	}

	/**
	 * count the real length of a string array
	 * 
	 * @param array
	 *            string array.
	 * 
	 * @return real length of array not include the null terminator.
	 */
	public static int strlen(byte[] array) {
		int len = -1;
		if (array != null) {
			if (array.length == 0)
				len = 0;
			else {
				for (int i = 0; i < array.length; i++) {
					if (array[i] == (byte) 0) {
						len = i;
						break;
					}
				}
			}
		}
		return len;
	}

	/**
	 * convert UNICODE into ASCII.
	 * 
	 * @param str
	 *            UNICODE string
	 * 
	 * @return ASCII code in byte array. null if error occur.
	 */
	public static byte[] convertUnicodeToAscii(String str) {
		byte[] result = null;

		try {
			int cnt = str.length();
			byte[] res = str.getBytes("US-ASCII");
			result = new byte[cnt + 1];

			for (int i = 0; i < cnt; i++) {
				result[i] = res[i];
			}
			result[cnt] = 0; // we must add it manually.
		} catch (UnsupportedEncodingException e) {
			result = null;
		}

		return result;
	}

	public static String convertBig5ToUnicode(byte[] array) {
		String str = null;

		try {
			byte[] text = filterAndCut(array);
			if (text != null)
				str = new String(text, "big5");
			else
				str = "";
		} catch (UnsupportedEncodingException e) {
		}

		return str.trim();
	}

	/**
	 * convert UNICODE into GBK.
	 * 
	 * @param str
	 *            UNICODE string
	 * 
	 * @return GBK code in byte array. null if error occur.
	 */
	public static byte[] convertUnicodeToGbk(String str) {
		byte[] result = null;

		try {
			byte[] res = str.getBytes("GBK");
			int cnt = res.length; // str.length();
			result = new byte[cnt + 1];

			for (int i = 0; i < cnt; i++) {
				result[i] = res[i];
			}
			result[cnt] = 0; // we must add it manually.
		} catch (UnsupportedEncodingException e) {
			result = null;
		}

		return result;
	}

	public static byte[] convertToUnicode(String str) {
		byte[] result = null;

		try {
			byte[] res = str.getBytes("utf-8");
			int cnt = res.length; // str.length();
			result = new byte[cnt + 1];

			for (int i = 0; i < cnt; i++) {
				result[i] = res[i];
			}
			result[cnt] = 0; // we must add it manually.
		} catch (UnsupportedEncodingException e) {
			result = null;
		}

		return result;
	}
}
