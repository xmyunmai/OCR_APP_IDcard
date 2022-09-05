package com.yunmai.android.bcr.other;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


//���룺�������<>�ڲ�����wt.codeHtml()��������ⲿ��htmlBean.Unhtmlcode(),����wt.Unhtmlcode(str,len)

@SuppressWarnings("unchecked")
public class CommonFunctions implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5446692091310481310L;
	private static String[][] urlCode = new String[][] { { ":", "%3A" },
			{ "/", "%2F" }, { "?", "%3F" }, { "&", "%26" }, { "=", "%3D" },
			{ "_", "%99" } };
	public static String DefaultRandomStr = "abcdefghijklmnopqrstuvwxyz";
	public static String DefaultRandomNum = "0123456789";
	public static String DefaultRandom = "0123456789abcdefghijklmnopqrstuvwxyz";

	public static final String BookmarkMD5Key = "w7_i0&ow";

	public CommonFunctions() {
	}

	public static CommonFunctions webTool = new CommonFunctions();

	public static CommonFunctions getInstance() {
		return webTool;
	}



	public static boolean isNNull(String str) {
		return !isNull(str);
	}

	public static boolean isNNull(String s1, String s2) {
		return isNNull(s1) && isNNull(s2);
	}

	public static boolean isNNull(String s1, String s2, String s3) {
		return isNNull(s1, s2) && isNNull(s3);
	}

	public static boolean isNull(String str) {
		return str == null || (str.trim()).equals("");
	}

	public static boolean isNull(byte[] bytes) {
		return bytes == null || bytes.length == 0;
	}

	public static boolean isNNull(byte[] bytes) {
		return bytes != null && bytes.length > 0;
	}

	public static boolean isNull(String[] arr) {
		return arr == null || arr.length == 0;
	}

	public static int length(Object obj) {
		if (obj == null)
			return 0;
		Class<?> c = obj.getClass();
		if (c == String.class) {
			return ((String) obj).length();
		} else if (c == byte[].class) {
			return ((byte[]) obj).length;
		} else if (c == String[].class) {
			return ((String[]) obj).length;
		} else if (c == int[].class) {
			return ((int[]) obj).length;
		} else if (c == long[].class) {
			return ((long[]) obj).length;
		} else if (List.class == c || ArrayList.class == c || Vector.class == c) {
			List<?> arr = (List<?>) obj;
			return arr.size();
		} else if (Map.class == c || HashMap.class == c) {
			Map<?, ?> arr = (Map<?, ?>) obj;
			return arr.size();
		}

		try {
			return obj.toString().length();
		} catch (Exception e) {
			System.out.println(">> CF.length Exception, object=" + obj);
			e.printStackTrace();
			return 0;
		}
	}

	public static boolean isNNull(String[] arr) {
		return arr != null && arr.length > 0;
	}

	public static boolean isNull(List<?> list) {
		return list == null || list.size() == 0;
	}

	public static boolean isNNull(List<?> list) {
		return !isNull(list);
	}

	public static boolean isNull(String s1, String s2) {
		return isNull(s1) || isNull(s2);
	}

	public static boolean isNull(String s1, String s2, String s3) {
		return isNull(s1) || isNull(s2) || isNull(s3);
	}

	public static String strNullToNbsp(String str) {
		return isNullD(str, "&nbsp;");
	}

	public static String isNullD(String str, String defaultStr) {
		return (str == null || str.trim().equals("")) ? defaultStr : str;
	}

	public static String strNull(String str, String defaultStr) {
		return (str == null || str.trim().equals("")) ? defaultStr : str;
	}

	public static String strNull(String str) {
		return str == null ? "" : str;
	}



	public static boolean equalsIgnoreCase(String str, String str1, String str2) {
		return strNull(str).equalsIgnoreCase(strNull(str1))
				|| strNull(str).equalsIgnoreCase(strNull(str2));
	}

	public static boolean equalsIgnoreCase(String str, String[] list) {
		if (isNull(str) || list == null || list.length == 0) {
			if (isNull(str) && (list == null || list.length == 0)) {
				return true;
			}
			return false;
		}
		for (int idx = 0; idx < list.length; idx++) {
			if (str.equalsIgnoreCase(strNull(list[idx]))) {
				return true;
			}
		}
		return false;
	}

	public static boolean equalsIgnoreCase(String str, String str1,
			String str2, String str3) {
		return equalsIgnoreCase(str, str1, str2)
				|| strNull(str).equalsIgnoreCase(strNull(str3));
	}

	public static boolean equalsIgnoreCase(String str, String str1,
			String str2, String str3, String str4) {
		return equalsIgnoreCase(str, str1, str2)
				|| equalsIgnoreCase(str, str3, str4);
	}


	public static String uncodeHtml(String str) {
		str = strReplace(str, "&quot;", "\"");
		return strReplace(str, "\'", "''");
	}

	public static String uncodeHtml1(String str) {
		return strReplace(str, "\'", "''");
	}

	public static String getLimitLengthString(String str, int len, String symbol) {
		if(len==0) {
			return str;
		}
		str = Unhtmlcode_(str);
		// System.out.println("str=" + str);
		int counterOfDoubleByte = 0;
		byte b[];
		try {
			b = str.getBytes("GBK");
			if (b.length <= len) {
				return str;
			}
			boolean isLastFu = false;
			for (int i = 0; i < len - (symbol.getBytes("GBK")).length; i++) {
				if (b[i] < 0) {
					isLastFu = !isLastFu;
					counterOfDoubleByte++;
				} else {
					if (isLastFu) {
						isLastFu = false;
						counterOfDoubleByte++;
					}
				}
			}
			if (counterOfDoubleByte % 2 == 0) {
				return new String(b, 0, len - symbol.length() < 0 ? 0 : len
						- symbol.length(), "GBK")
						+ symbol;
			} else {
				return new String(b, 0, len - 1 - symbol.length() < 0 ? 0 : len
						- 1 - symbol.length(), "GBK")
						+ symbol;
			}
		} catch (UnsupportedEncodingException ex) {
			return "";
		}
	}

	public static String htmlCode(String str) {
		if (isNull(str)) {
			return "";
		}
		str = replace(str, "\"", "&quot;");
		str = replace(str, " ", "&nbsp;");
		str = replace(str, "<", "&lt;");
		str = replace(str, ">", "&gt;");
		return str;
	}

	public static String Unhtmlcode(String str, int len, String para) {
		if (!isNull(str)) {
			if (len != 0) {
				str = getLimitLengthString(str, len, para);
			}
			str = htmlCode(str);
		} else {
		}
		return strNullToNbsp(str);
	}

	public static String Unhtmlcode_(String str) {
		String newstr = "";
		if (isNull(str)) {
			return "";
		}
		newstr = str;
		newstr = strReplace(newstr, "&quot;", "\"");
		newstr = strReplace(newstr, "&nbsp;", " ");
		newstr = strReplace(newstr, "&lt;", "<");
		newstr = strReplace(newstr, "&gt;", ">");
		return newstr;
	}

	public static String Unhtmlcode(String str, int len) {
		return Unhtmlcode(str, len, "");
	}

	public static String codeHtml(String str) {
		return strReplace(str, "\"", "&quot;");
	}

	public static String codeHtml(String str, int len, String para) {
		if (!isNull(str)) {
			if (len != 0) {
				str = getLimitLengthString(str, len, para);
			}
			str = strReplace(str, "\"", "&quot;");
		}
		return strNullToNbsp(str);
	}

	public static String codeHtml1(String str) {
		return strReplace(str, "\"", "\\\"");
	}

	public static String codeHtml2(String str) {
		return strReplace(strReplace(str, "'", "\\'"), "\"", "&quot;");
	}

	public static String codeHtml3(String str) {
		return strReplace(strReplace(str, "'", "\\'"), "\"", "\\\"");
	}

	public static String strReplace(String str) {
		return strReplace(str, "\\", "/");
	}

	public static String replace(String str, String sub1, String sub2) {
		return strReplace(str, sub1, sub2);
	}

	public static String replaceA(String str, String[] arr, String sub) {
		if (isNull(str) || arr == null || sub == null || arr.length == 0)
			return strNull(str);
		for (String s : arr) {
			str = replace(str, s, sub);
		}
		return str;
	}

	public static String replaceN(String str) {
		return replace(str, "\r\n", "<br>");
	}

	public static String strReplace(String str, String sub1, String sub2) {
		if (str == null || str.equals("")) {
			return "";
		}

		if (sub1 == null || sub1.equals("")) {
			sub1 = "\\";
		}
		if (sub2 == null || sub2.equals("")) {
			// sub1 = "/";
		}
		int idx = -1;
		int start = 0;
		// System.out.println(sub1.length()+":"+sub2.length());
		// int buff=0;
		if (str != null && str != "") {
			while (start < str.length()) { // && buff<10)
				idx = (str.substring(start).toLowerCase()).indexOf(sub1
						.toLowerCase());
				if (idx < 0) {
					break;
				}
				idx = start + idx;
				if (idx == 0) {
					if (str.length() > 1) {
						str = sub2 + str.substring(sub1.length());
						start = sub2.length();
						continue;
					} else {
						str = sub2;
						break;
					}
				} else if (idx == (str.length() - 1)) {
					str = str.substring(0, str.length() - 1) + sub2;
					break;
				} else {
					str = str.substring(0, idx) + sub2
							+ str.substring(idx + sub1.length());
					start = idx + sub2.length();
				}
			}
		}
		return str;
	}

	public static int o2i(Object o) {
		return o2i(o, 0);
	}

	// ������ȡ��ȥ��С��㣬������������
	public static int o2i(Object o, int defaultI) {
		if (o != null && o.toString() != null) {
			int idx = o.toString().indexOf(".");
			if (idx >= 0) {
				String str = o.toString();
				if (chkNum(str, true) && idx == str.lastIndexOf(".")) {
					if (idx == 0) {
						return 0;
					} else {
						o = str.substring(0, idx);
					}
				} else {
					return defaultI;
				}
			}
			return s2i(o.toString(), 0);
		} else {
			return defaultI;
		}
	}

	public static int s2i(Object str, int defaultI) {
		int i = 0;
		try {
			i = Integer.parseInt(str.toString().trim());
		} catch (Exception e) {
			i = defaultI;
		}
		return i;
	}

	public static int s2i(Object str) {
		return s2i(str, 0);
	}

	public static int s2i(String str) {
		return s2i(str, 0);
	}

	public static float s2f(Object str) {
		float f = 0;
		try {
			f = Float.parseFloat(str.toString());
		} catch (Exception e) {
			f = 0;
		}
		return f;
	}

	public static float s2f(String str) {
		float f = 0;
		try {
			f = Float.parseFloat(str);
		} catch (Exception e) {
			f = 0;
		}
		return f;
	}

	public static long tol(Object o) {
		return s2l(String.valueOf(o));
	}

	public static long o2l(Object o) {
		return tol(o);
	}

	public static long s2l(String str) {
		return s2l(str, 0);
	}
	public static long s2l(String str,long def){
		if (isNull(str)) {
			return def;
		}
		long i = def;
		try {
			i = Long.parseLong(str);
		} catch (Exception e) {
		}
		return i;
	}

	public static float getMaxWidth(Vector<?> vec, float ZhBei, float charWidth,
			float minWidth) {
		float retu = minWidth;
		float buff = 0;
		ZhBei = (ZhBei > 1 && ZhBei < 2.6) ? ZhBei : 2;
		for (int i = 0; vec != null && i < vec.size(); i++) {
			String str = strNull((String) vec.elementAt(i));
			int len = str.length();
			if (len != 0) {
				buff = getZhNum(str);
				buff = (len + buff * (ZhBei - 1)) * charWidth;
				if (buff > minWidth && buff > retu) {
					retu = buff;
				}
			}
		}
		return retu;
	}

	public static int getZhNum(String s) {
		// System.out.println("\n\nnew String:->"+s);
		int num = 0;
		byte[] b;
		try {
			b = s.getBytes("GBK");
		} catch (Exception e) {
			return 0;
		}

		for (int i = 0; i < b.length; i++) {
			if (b[i] < 0) {
				num++;
				num++;
				i++;
			}
		}
		num /= 2;

		return num;
	}

	public static int getBytesLength(String s) {
		byte[] b;
		try {
			b = s.getBytes("GBK");
		} catch (Exception e) {
			return 0;
		}
		return b.length;
	}

	public static int getRandomInt() {
		return getRandomInt(0, 99);
	}

	public static int getRandomInt(int min, int max) {
		return min + (int) (Math.random() * (max - min + 1));
	}

	public static String getRandomString(int size, String str) {
		StringBuffer retu = new StringBuffer(size);
		int j;
		int strLen = str.length() - 1;
		for (int i = 0; i < size; i++) {
			j = getRandomInt(0, strLen);
			retu.append(str.substring(j, j + 1));
		}
		return retu.toString();
	}

	public static String getRandomString(int size) {
		return getRandomString(size, DefaultRandomStr);
	}

	public static String getRandomNum(int size) {
		return getRandomString(size, DefaultRandomNum);
	}

	public static String getRandom(int size) {
		return getRandomString(size, DefaultRandom);
	}

	public static boolean chkNum(String NUM, boolean isPoint) {
		int i, j;
		String strTemp = ".0123456789";
		if (!isPoint) {
			strTemp = "0123456789";
		}
		if (NUM == null || NUM.length() == 0) {
			return false;
		}
		for (i = 0; i < NUM.length(); i++) {
			j = strTemp.indexOf(NUM.substring(i, i + 1));
			if (j < 0) {
				// ˵�����ַ�������
				return false;
			}
		}
		// ˵��������
		return true;
	}

	public static boolean chkMobile(String value) {
		if (isNull(value) || value.length() != 11 || value.indexOf("1") != 0
				|| !chkNum(value, false)) {
			return false;
		}
		return true;
	}

	// ֻ�����ֻ���룬Ϊ1����01��ͷ�����ҳ��ȷֱ�Ϊ11��12λ
	public static boolean chkUsername(String value) {
		if (isNull(value) || value.length() != 11 || value.indexOf("1") != 0
				|| !chkNum(value, false)) {
			return false;
		}
		return true;
	}

	// ģ�����ֻ����
	public static boolean chkMobileCommon(String value) {
		if (isNull(value)) {
			return false;
		}
		String chkstr = "+0123456789,-";
		return chkStr(chkstr, value.toLowerCase());
	}

	public static boolean chkEMail(String value) {
		// alert("chkEmail");
		if (value == null || value.equals("")) {
			return false;
		}
		value = loginIdDecode(value);
		boolean retu = false;
		int i = value.length();
		int temp = value.indexOf("@");
		int tempd = value.indexOf(".");
		if (temp > 1) {
			if ((i - temp) > 3) {
				if (((i - tempd) > 0) && (tempd > 0)) {
					retu = true;
				}
			}
		}

		if (retu == true) {
			String chkstr = "abcdefghijklmnopqrstuvwxyz@_0123456789.";
			retu = chkStr(chkstr, value.toLowerCase());
			if (value.indexOf("@") != value.lastIndexOf("@")
					||
					// value.indexOf(".") != value.lastIndexOf(".") ||
					value.indexOf(".") == value.lastIndexOf("@") + 1
					|| value.indexOf(".") == value.length() - 1) {
				retu = false;
			}
		}
		// System.out.println("chkemail:"+retu+" "+value);
		return retu;
	}

	public static int chkEMailList(String value) {
		return chkEMailList(value, ",");
	}

	public static int chkEMailList(String value, String split) {
		if (isNull(value) || isNull(split)) {
			return 0;
		}
		if (value.indexOf(split) < 0) {
			return chkEMail(value) ? 1 : 0;
		}
		int retu = 0;
		String[] ss = value.split(split);

		for (int i = 0; i < ss.length; i++) {
			if (chkEMail(ss[i])) {
				retu++;
			}
		}
		return retu;
	}

	public static String getChkEMailList(String value) {
		return getChkEMailList(value, ",");
	}

	public static String getChkEMailList(String value, String split) {
		String retu = "";
		if (isNull(value) || isNull(split)) {
			return "";
		}
		if (value.indexOf(split) < 0) {
			return chkEMail(value) ? value : "";
		}
		String[] ss = value.split(split);
		for (int i = 0; i < ss.length; i++) {
			if (chkEMail(ss[i])) {
				retu = retu + "," + ss[i];
			}
		}
		return trimComma(retu);
	}

	public static boolean chkStr(String chkStr, String str) {
		if (isNull(chkStr, str)) {
			return false;
		}
		int i, j;
		chkStr = chkStr.toUpperCase();
		str = str.toUpperCase();

		for (i = 0; i < str.length(); i++) {
			j = chkStr.indexOf(str.substring(i, i + 1));
			if (j == -1) {
				return false;
			}
		}
		return true;
	}

	public static int getStrSLenth(String s[]) {
		if (s == null) {
			return 0;
		}
		int len = s.length;
		for (int i = 0; i < len; i++) {
			if (s[i] == null || s[i].equals("")) {
				len--;
			}
		}
		return len;
	}

	public static String trimComma(String str) {
		if (str != null) {
			if (str.length() > 0 && str.substring(0, 1).equals(",")) {
				str = str.substring(1);
			}
			if (str.length() > 0 && str.substring(str.length() - 1).equals(",")) {
				str = str.substring(0, str.length() - 1);
			}
		} else {
			return "";
		}
		return str.trim();
	}

	public static String trimComma_1(String str) {
		if (isNull(str)) {
			return "";
		}
		if (str != null && str.length() > 0) {
			String[] ss = str.split(",");
			// System.out.print(ss.length+":");
			if (ss.length > 0) {
				str = "";
				for (int i = 0; i < ss.length; i++) {
					if (!isNull(ss[i].trim())) {
						str = str + ss[i].trim() + ",";
					}
				}
				str = trimComma(str);
			} else {
				str = "";
			}
		}
		return str.trim();
	}

	public static void remove(List<?> l, int start, int end) {
		if (end > l.size()) {
			return;
		}
		for (int idx = end - 1; idx >= start; idx--) {
			l.remove(idx);
		}
	}

	// ���ö���","�������ַ��б�ָ����vec����
	public static void s2v(String str, Vector<String> vec) {
		sl2v(str, vec);
	}

	public static void s2v(String str, Vector<String> vec, boolean IsDeleteSame) {
		sl2v(str, vec, IsDeleteSame);
	}

	// ���ö���","�������ַ��б�ָ����vec����
	public static void sl2v(String str, Vector<String> vec) {
		sl2v(str, vec, false);
	}

	public static void sl2v(String str, Vector<String> vec, boolean IsDeleteSame) {
		str = strNull(str);
		if (isNull(str) || vec == null) {
			return;
		}
		if (vec.size() > 0) {
			// System.out.println(" clear Vector() ");
			vec.clear();
		}
		String[] ss = str.split(",");
		if (ss.length > 0) {
			for (int i = 0; i < ss.length; i++) {
				if (!isNull(ss[i].trim())) {
					boolean isAdd = true;
					if (IsDeleteSame) {
						for (int idx = 0; idx < vec.size(); idx++) {
							if (vec.elementAt(idx).equals(ss[i].trim())) {
								isAdd = false;
							}
						}
					}
					if (isAdd) {
						vec.addElement(ss[i].trim());
					}
				}
			}
		}
	}

	public static String[] v2arr(Vector<String> vec) {
		if (vec.size() == 0) {
			return new String[0];
		}
		String[] retu = new String[vec.size()];
		for (int idx = 0; idx < vec.size(); idx++) {
			retu[idx] = vec.elementAt(idx);
		}
		return retu;
	}

	public static String s2s(String s) {
		if (s == null)
			return "";
		String[] arr = s2a(s);
		if (arr == null || arr.length == 0)
			return "";
		StringBuffer sb = new StringBuffer(arr.length);
		for (int idx = 0; idx < arr.length; idx++) {
			sb.append(arr[idx]);
			if (idx < arr.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public static long[] a2l(String[] arr) {
		return a2l(arr, false);
	}

	public static long[] a2l(String[] arr, boolean IsZero) {
		if (arr == null || arr.length == 0)
			return new long[0];
		int size = 0;
		for (String str : arr) {
			if (IsZero || s2l(str) > 0) {
				size++;
			}
		}
		long[] retu = new long[size];
		size = 0;
		for (int idx = 0; idx < arr.length; idx++) {
			if (IsZero || s2l(arr[idx]) > 0) {
				retu[size++] = s2l(arr[idx]);
			}
		}
		return retu;
	}

	public static long[] s2al(String str) {
		return s2al(str, false);
	}

	public static long[] s2al(String str, boolean IsZero) {
		// return s2arr(str);
		if (str == null)
			return null;
		String[] arr = str.split(",");
		int size = 0;
		for (int idx = 0; idx < arr.length; idx++) {
			if (isNNull(arr[idx]) && (IsZero || s2l(arr[idx]) > 0)) {
				size++;
			}
		}
		long[] retu = new long[size];
		size = 0;
		for (int idx = 0; idx < arr.length; idx++) {
			if (isNNull(arr[idx]) && (IsZero || s2l(arr[idx]) > 0)) {
				retu[size++] = s2l(arr[idx]);
			}
		}

		return retu;
	}

	public static String trimLastComma(String str) {
		if (str == null)
			return "";
		int idx = str.lastIndexOf(",");
		while (idx > -1 && idx == str.length() - 1) {
			str = str.substring(0, idx);
			idx = str.lastIndexOf(",");
		}
		return str;
	}

	public static String a2s(String[] arr) {
		return a2s(arr, ",");
	}

	public static String a2s(String[] arr, String SP) {
		StringBuffer sb = new StringBuffer();
		for (int idx = 0; arr != null && idx < arr.length; idx++) {
			sb.append(arr[idx] == null ? "" : arr[idx].trim());
			if (idx != arr.length - 1) {
				sb.append(SP);
			}
		}
		return sb.toString();
	}

	public static String[] s2a(String str) {
		// return s2arr(str);
		if (str == null)
			return null;
		String[] arr = str.split(",");
		int size = 0;
		for (int idx = 0; idx < arr.length; idx++) {
			if (isNNull(arr[idx])) {
				size++;
			}
		}
		String[] retu = new String[size];
		size = 0;
		for (int idx = 0; idx < arr.length; idx++) {
			if (isNNull(arr[idx])) {
				retu[size++] = arr[idx];
			}
		}
		return retu;
	}

	public static String[] s2arr(String str) {
		Vector<String> vec = new Vector<String>();
		s2v(str, vec);
		String[] retu = v2arr(vec);
		vec.clear();
		return retu;
	}

	public static int getCi(String str, String substr) {
		int retu = 0;
		if (str != null && substr != null && !substr.equals("")) {
			int idx = str.indexOf(substr);
			while (idx >= 0) {
				retu++;
				idx = str.indexOf(substr, idx + substr.length());
			}
		}
		return retu;
	}

	public static int[] s2aI(String str) {
		str = strNull(str);
		if (isNull(str)) {
			return null;
		}
		int[] retu = new int[getCi(str, ",") + 1];
		String[] ss = str.split(",");
		int idx = 0;
		if (ss != null && ss.length > 0) {
			for (int i = 0; i < ss.length; i++) {
				if (isNNull(ss[i].trim())) {
					retu[idx++] = s2i(ss[i].trim());
				}
			}
		}
		return retu;
	}

	public static String getNbsp(int i) {
		String str = "";
		i = i < 0 ? 0 : i;
		for (int j = 0; j < i; j++) {
			str += "&nbsp;";
		}
		return str;
	}

	public static String getStrInStrs(String str, String[] ss, String def) {
		return getStrInStrs(str, ss, def, false);
	}

	public static String getStrInStrs(String str, String[] ss, String def,
			boolean isIgnoreCase) {
		if (isNull(str) || ss == null || ss.length < 1) {
			return def;
		}
		boolean isIn = false;
		String buff;
		for (int i = 0; i < ss.length; i++) {
			buff = ss[i];
			if (ss[i] == null) {
				buff = "";
			}
			if ((isIgnoreCase && str.equalsIgnoreCase(buff))
					|| (!isIgnoreCase && str.equals(buff))) {
				// System.out.println("\nin : "+str+":"+buff);
				isIn = true;
				break;
			}
		}
		return isIn ? str : def;
	}

	public static int compDateStr(String date1, String date2) {
		if (isNull(date1, date2) || date1.length() < 16 || date2.length() < 16) {
			return -2;
		}
		String y1 = date1.substring(0, 4);
		String y2 = date2.substring(0, 4);

		String m1 = date1.substring(5, 7);
		String m2 = date2.substring(5, 7);

		String d1 = date1.substring(8, 10);
		String d2 = date2.substring(8, 10);

		String h1 = date1.substring(11, 13);
		String h2 = date2.substring(11, 13);

		String min1 = date1.substring(14, 16);
		String min2 = date2.substring(14, 16);

		if ((s2i(y1) > s2i(y2))
				|| (s2i(y1) == s2i(y2) && s2i(m1) > s2i(m2))
				|| (s2i(y1) == s2i(y2) && s2i(m1) == s2i(m2) && s2i(d1) > s2i(d2))
				|| (s2i(y1) == s2i(y2) && s2i(m1) == s2i(m2)
						&& s2i(d1) == s2i(d2) && s2i(h1) > s2i(h2))
				|| (s2i(y1) == s2i(y2) && s2i(m1) == s2i(m2)
						&& s2i(d1) == s2i(d2) && s2i(h1) == s2i(h2) && s2i(min1) > s2i(min2))) {
			return 1;
		} else if (s2i(y1) == s2i(y2) && s2i(m1) == s2i(m2)
				&& s2i(d1) == s2i(d2) && s2i(h1) == s2i(h2)
				&& s2i(min1) == s2i(min2)) {
			return 0;
		} else {
			return -1;
		}

	}


	public static String get_(int i) {
		if (i < 0) {
			i = 0;
		}
		String retu = "";
		for (int j = 0; j < i; j++) {
			retu += "-";
		}
		return retu;
	}

	public static String getSQLNull(String str) {
		return getSQLNull(str, true);
	}

	public static String getSQLNull(String str, boolean isTrim) {
		if (isNull(str)) {
			return "";
		}
		if (isTrim) {
			return " ltrim(rtrim(isnull(" + str + ",''))) ";
		} else {
			return " isnull(" + str + ",'') ";
		}
	}

	public static void insertSortVecArr2(Vector<?> vec, String[] str) {
		insertSortVecArr2(vec, str, false);
	}

	public static void insertSortVecArr2(Vector vec, String[] str,
			boolean IsLong) {
		if (vec == null) {
			return;
		}
		if (vec.size() == 0) {
			vec.addElement(str);
			return;
		}
		String s1;
		s1 = str[0];
		boolean isFind = false;
		long comp;
		int size = vec.size();
		int idx = size, mid;
		int bottom = 0, top = size - 1;
		String bottomStr;
		String topStr, midStr;
		int i = 0;
		for (; i < size; i++) {
			mid = (bottom + top) / 2;

			bottomStr = ((String[]) vec.elementAt(bottom))[0];
			topStr = ((String[]) vec.elementAt(top))[0];
			midStr = ((String[]) vec.elementAt(mid))[0];
			if (bottom == top - 1 || bottom == top) {
				if (IsLong ? (s2l(s1) - s2l(bottomStr) >= 0 && s2l(s1)
						- s2l(topStr) < 0)
						: (s1.compareTo(bottomStr) >= 0 && s1.compareTo(topStr) < 0)) {
					idx = top;
					isFind = true;
				}
				break;
			}

			comp = (IsLong ? (s2l(s1) - s2l(midStr)) : s1.compareTo(midStr));
			if (comp >= 0) {
				bottom = mid;
			} else {
				top = mid;
			}
		}
		if (!isFind) {
			midStr = ((String[]) vec.elementAt(0))[0];

			comp = IsLong ? (s2l(s1) - s2l(midStr)) : s1.compareTo(midStr);
			if (comp < 0) {
				idx = 0;
			} else {
				idx = size;
			}
		}
		//System.out.println("cishu:**"+i+"**:--"+str[0]+"--:--"+str[1]+"--\n");
		vec.add(idx, new String[] { str[0], str[1] });
	}

	public static String getArr2WithVec(Vector<?> vec, String str) {
		if (vec == null || isNull(str)) {
			return "";
		} else if (vec.size() == 0) {
			return str;
		}
		str = str.trim();
		String s1, retu = str;
		int size = vec.size();
		int bottom = 0, top = size - 1, comp, mid;
		int bottomEnd = 0, topEnd = 0;
		String sign = "0";
		for (int i = 0; i < size; i++) {
			mid = (bottom + top) / 2;
			if (sign.equals("0") && bottom == top - 1) {
				bottomEnd = bottom;
				topEnd = top;
				if (mid == bottom) {
					sign = "-1";
				} else {
					sign = "1";
				}
			} else if (sign.equals("-1")) {
				mid = topEnd;
				sign = "2";
			} else if (sign.equals("1")) {
				mid = bottomEnd;
				sign = "2";
			} else if (sign.equals("2")) {
				break;
			}
			// System.out.println("bottom:"+bottom+"--top:"+top+"--mid:"+mid);
			s1 = (((String[]) vec.elementAt(mid))[0]).trim();
			// System.out.println("bijiao:"+str+":"+s1);
			if (str.equalsIgnoreCase(s1)) {
				comp = 0;
			} else {
				comp = str.compareTo(s1);
			}

			if (comp == 0) {
				retu = (((String[]) vec.elementAt(mid))[1]).trim();
				// System.out.println("�ҵ���"+(i+1)+"�Σ�");
				break;
			} else if (comp < 0) {
				top = mid;
			} else if (comp > 0) {
				bottom = mid;
			}
		}
		//System.out.println("result:cishu:**"+i+"**:--"+str+"--:--"+retu+"--\n"
		// );
		return strNull(retu);
	}

	public static String getSortType_(String str) {
		if (str.equalsIgnoreCase("desc")) {
			return "asc";
		} else {
			return "desc";
		}
	}

	public static String getNullField(long l, String str, boolean isLong,
			String fieldName, String fieldType, boolean isAnd) {
		boolean isSet = true;
		String para = "";
		String val = "";
		if (fieldType.equalsIgnoreCase("String")) {
			para = "'";
		}
		if (isLong) {
			isSet = l > 0;
			val = String.valueOf(l);
		} else {
			isSet = !isNull(str);
			val = str;
		}
		return isSet ? (" " + fieldName + "=" + para + val + para + (isAnd ? " and "
				: ""))
				: "";
	}

	public static String getNullField(String str, String fieldName) {
		return getNullField(0l, str, false, fieldName, "long", true);
	}

	public static String getNullField(long l, String fieldName) {
		return getNullField(l, "", true, fieldName, "long", true);
	}

	public static String getNullField(int i, String fieldName) {
		return getNullField(i, "", true, fieldName, "long", true);
	}

	public static String getNullField(String str, String fieldName,
			String fieldType) {
		return getNullField(0l, str, false, fieldName, fieldType, true);
	}

	public static String buling(String str, int len) {
		String retu = str;
		int idx = len - str.length();
		for (int i = 0; i < idx; i++) {
			retu = "0" + retu;
		}
		return retu;
	}

	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j)) {
				tmp.append(j);
			} else if (j < 256) {
				tmp.append("%");
				if (j < 16) {
					tmp.append("0");
				}
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src
							.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src
							.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	public static String getLogicNo(boolean a) {
		return a ? "1" : "0";
	}

	public static String getLogicNo(boolean a, boolean b) {
		return getLogicNo(a) + getLogicNo(b);
	}

	public static String getLogicNo(boolean a, boolean b, boolean c) {
		return getLogicNo(a) + getLogicNo(b, c);
	}


	// 123456 to 123,456
	public static String formatLong(long l) {
		String retu = "";
		String str = String.valueOf(l);
		if (l < 1000) {
			retu = str;
		} else {
			int len = str.length();
			str = strD(str);
			String end = str;
			Vector<String> vec = new Vector<String>();
			for (int idx = 0; idx < len; idx++) {
				if ((idx + 1) % 3 == 0) {
					vec.addElement(str.substring(idx - 2, idx + 1));
					if (idx + 1 != len) {
						end = str.substring(idx + 1);
					} else {
						end = "";
					}
				}
			}
			if (!isNull(end)) {
				vec.addElement(end);
			}
			for (int idx = 0; idx < vec.size(); idx++) {
				retu += (String) vec.elementAt(idx);
				if (idx != vec.size() - 1) {
					retu += ",";
				}
			}
			retu = strD(retu);
		}
		return retu;
	}

	// abc to cba
	public static String strD(String str) {
		if (isNull(str)) {
			return "";
		}
		String retu = "";
		for (int i = 0; i < str.length(); i++) {
			retu = str.substring(i, i + 1) + retu;
		}
		return retu;
	}

	public static boolean chkPicExt(String ext) {
		if (isNull(ext)) {
			return false;
		}
		if (equalsIgnoreCase(ext, "jpg", "gif", "bmp", "png")
				|| ext.equalsIgnoreCase("jpeg")) {
			return true;
		}
		return false;
	}

	//���ַ�str�У�ɾ����s1��ʼ��s2��������ַ�����s1,s2�Լ�,���para��Ϊ�գ��������s1��s2
	// ����para�Ż�ɾ��
	public static String deleteStr(String str, String s1, String s2, String para) {
		String retu = str;
		final String oldStr = str;
		final String strStart = s1;
		final String strEnd = s2;
		if (isNull(str, s1, s2)) {
			return "";
		}
		try {
			int idx = str.indexOf(strStart);
			int max = 0;
			while (idx >= 0 && max < 1000) {
				String strBuff1 = str.substring(0, idx);
				String strBuff2 = str.substring(idx + strStart.length());
				int idx2 = strBuff2.indexOf(strEnd);
				if (idx2 >= 0) {
					if (isNull(para)
							|| (!isNull(para) && str.substring(idx,
									idx + strStart.length() + idx2).indexOf(
									para) >= 0)) {
						str = strBuff1 + strBuff2.substring(idx2 + s2.length());
					}
					retu = str;
				} else { // log
					System.out
							.println("\r\nɾ���ַ�����쳣����WebTool��deleteStr�У�ԭ����ֻ�ҵ��˿�ͷ�ַ�"
									+ "û���ҵ���β�ַ��������ַ��ȴ���s1��s2ǰ�����ص������ַ��ǣ�\r\n"
									+ oldStr);
					return retu;
				}
				idx = str.indexOf(strStart, idx + s1.length());
			}
			if (max >= 1000) {
				System.out.println("\r\nWebTool��deleteStr:�п��ܳ�����ѭ����");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retu;
	}


	public static boolean isFileExists(String path) {
		if (isNull(path)) {
			System.out.println(" >>> wt.isFileExists.path=null!");
			return false;
		}
		File file = new File(path);
		return file.exists();
	}

	public static boolean isIn(long[] ls, long id) {
		for (int idx = 0; idx < ls.length; idx++) {
			if (ls[idx] == id) {
				return true;
			}
		}
		return false;
	}

	public static boolean isIn(Vector<?> vec, long id) {
		if (vec == null || vec.size() == 0) {
			return false;
		}
		for (int idx = 0; idx < vec.size(); idx++) {
			if (((Long) vec.elementAt(idx)).longValue() == id) {
				return true;
			}
		}
		return false;
	}

	public static boolean isIn(String str, String sub) {
		if (isNull(str))
			return false;
		if (isNull(sub))
			return true;
		return str.toLowerCase().indexOf(sub.toLowerCase()) > -1;
	}

	public static boolean isIn(String str, String sub1, String sub2) {
		return isIn(str, sub1) || isIn(str, sub2);
	}

	public static boolean isIn(String str, String sub1, String sub2, String sub3) {
		return isIn(str, sub1) || isIn(str, sub2) || isIn(str, sub3);
	}

	public static boolean isIn(String str, String sub1, String sub2,
			String sub3, String sub4) {
		return isIn(str, sub1) || isIn(str, sub2) || isIn(str, sub3)
				|| isIn(str, sub4);
	}

	public static boolean isIn(String str, String sub1, String sub2,
			String sub3, String sub4, String sub5) {
		return isIn(str, sub1) || isIn(str, sub2) || isIn(str, sub3)
				|| isIn(str, sub4) || isIn(str, sub5);
	}

	public static boolean isIn(String str, String[] list) {
		if (isNull(str) || list == null || list.length == 0) {
			if (isNull(str) && (list == null || list.length == 0)) {
				return true;
			}
			return false;
		}
		for (int idx = 0; idx < list.length; idx++) {
			if (isIn(str, list[idx])) {
				return true;
			}
		}
		return false;
	}

	// dog%26%20=��-+()%25!~%60@%23at.jpg
	// dog& =��-+()%!~`@#at.jpg

	// dog',.;%5E$%5B%5D%7B%7D.jpg
	// dog',.;^$[]{}.jpg
	public static String codeFileName(String str) {
		if (isNull(str)) {
			return str;
		}
		str = strReplace(str, " ", "%20");
		str = strReplace(str, "#", "%23");
		str = strReplace(str, "!", "%25");
		str = strReplace(str, "&", "%26");
		str = strReplace(str, "`", "%60");
		str = strReplace(str, "^", "%5E");
		str = strReplace(str, "[", "%5B");
		str = strReplace(str, "]", "%5D");
		str = strReplace(str, "{", "%7B");
		str = strReplace(str, "}", "%7D");
		return str;
	}


	// ����2����Σ��ڶ��������������ڵ㣬�����˶�ά��������һά����
	// ��xml���и����Ӽ��㣬����תΪ��ά���飬������һ������һ���������͵ģ��������û���ظ���
	// ע�Ᵽ֤xml�ڶ���ṹ��˳��һ���ԣ���������extension��Ӧ����mime-type֮ǰ��
	/*
	 * <web-app> <mime-mapping> <extension>abs</extension>
	 * <mime-type>audio/x-mpeg</mime-type> </mime-mapping>
	 */

	public static String[][] getXMLContent2(String file, int len) {
		String[][] retu = null;
		Vector vec = new Vector();
		getXMLContent2(file, vec);
		if (vec != null && vec.size() > 0) {
			retu = new String[vec.size()][len];
		}
		for (int idx = 0; vec != null && idx < vec.size(); idx++) {
			Vector buff = (Vector) vec.elementAt(idx);
			for (int i = 0; buff != null && i < len && i < buff.size(); i++) {
				retu[idx][i] = (String) buff.elementAt(i);
			}
		}
		return retu;
	}

	public static void getXMLContent2(String file, Vector vec) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments(true);
		dbf.setIgnoringElementContentWhitespace(true);
		Document document;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			// Document document = db.parse(new File(file));
			document =  db.parse(file);

		} catch (Exception e) {
			System.out.println("WebTool : MIMEBean ���س��?�������ļ�û�ҵ���");
			return;
		}
		Node root = document.getDocumentElement();
		// System.out.println("root=" + root.getNodeName());
		NodeList nodes = root.getChildNodes();
		// System.out.println("root first Length=" + nodes.getLength());
		// System.out.println("\n\n" + root.getNodeName());
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			Node n = nodes.item(idx);
			// System.out.println("Ԫ��
			// "+idx+"=
			// '"+n.getNodeName()+"'\tvalue='"+n.getNodeValue()+"'\tattr='"+n.getAttributes()+"'\ttype='"+n.getNodeType()+"'");
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				// System.out.println("\t+-- " + n.getNodeName() + "'\tvalue='"
				// + n.getFirstChild().getNodeValue() + "'");
				// System.out.println("\t+-- " + n.getNodeName()); // +
				// "'\tvalue='" + n.getFirstChild().getNodeValue() + "'");
				if (n.hasChildNodes()) {
					NodeList childs = n.getChildNodes();
					Vector buff = new Vector();
					for (int point = 0; point < childs.getLength(); point++) {
						Node node = childs.item(point);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							// System.out.println("\t\t+-- " +
							// node.getNodeName() + "\tvalue='"
							// +node.getFirstChild().getNodeValue() + "'");
							buff
									.addElement(node.getFirstChild()
											.getNodeValue());
						}
					}
					if (buff != null && buff.size() > 0) {
						vec.addElement(buff);
					}
				}
			}

			/*
			 * if(n.getNodeType()==Node.ENTITY_NODE){ System.out.println("\t+==
			 * "+n.getNodeName()); }
			 */
		}
		// System.out.println("Max Length=" + vec.size());
	}

	public static int f2i(float f) {
		String str = String.valueOf(f);
		String intStr = str;
		// System.out.println(str);
		int i = 0, idx = 0;
		boolean isAddOne = false;
		idx = str.indexOf(".");
		if (idx > 0) {
			intStr = str.substring(0, idx);
			if (idx != str.length() - 1) {
				// System.out.println("\t\t"+str.substring(idx, idx + 1));
				if (s2i(str.substring(idx + 1, idx + 2)) > 4) {
					isAddOne = true;
				}
			}
			i = s2i(intStr) + (isAddOne ? 1 : 0);
		}
		return i;
	}

	public static String fF(float floatV) {
		DecimalFormat df = new DecimalFormat("########0.00");
		return df.format(floatV);
	}

	public static String fF(String str) {
		return fF(s2f(str));
	}


	public static void copyList(List<?> vec, List tagVec) {
		if (vec == null || tagVec == null || vec.size() == 0) {
			return;
		}
		for (int idx = 0; idx < vec.size(); idx++) {
			tagVec.add(vec.get(idx));
		}
	}

	public static String getExtList(String[] ss) {
		String retu = "";
		if (ss != null) {
			for (int idx = 0; idx < ss.length; idx++) {
				retu += "." + ss[idx];
				if (idx != ss.length - 1) {
					retu += ", ";
				}
			}
		}
		return retu;
	}

	public static String getExt(String Name) {
		String ext = "";
		int idx = Name.lastIndexOf(".");
		if (idx >= 0 && idx < Name.length() - 1) {
			ext = Name.substring(idx + 1);
		}
		return ext;
	}

	public static String delteStrSame(String str) {
		if (isNull(str)) {
			return "";
		}
		StringBuffer sb = new StringBuffer(",");
		String[] ss = str.split(",");
		// System.out.print(ss.length+":");
		if (ss.length > 0) {
			str = "";
			for (int i = 0; i < ss.length; i++) {
				if (!isNull(ss[i].trim())
						&& sb.toString().indexOf("," + ss[i].trim() + ",") == -1) {
					sb.append(ss[i].trim() + ",");
				}
			}
		} else {
			str = "";
		}
		return trimComma(sb.toString());
	}

	public static void delteVecStrSame(Vector<String> vec) {
		if (vec == null || vec.size() == 1) {
			return;
		}
		for (int idx = 0; idx < vec.size();) {
			String thisNum = vec.elementAt(idx);
			boolean IsRemove = false;
			for (int point = idx + 1; point < vec.size(); point++) {
				if (thisNum.equals(vec.elementAt(point))) {
					IsRemove = true;
					break;
				}
			}
			if (IsRemove) {
				vec.removeElementAt(idx);
			} else {
				idx++;
			}
		}
	}

	public static boolean IsLike(String Search, String str) {
		if (strNull(str).toUpperCase().indexOf(strNull(Search).toUpperCase()) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean IsLike(String Search, String[] ss) {
		if (isNull(Search)) {
			return true;
		}
		if (ss == null || ss.length == 0) {
			return false;
		}
		String buff;
		for (int idx = 0; idx < ss.length; idx++) {
			buff = ss[idx];
			if (isNull(buff)) {
				continue;
			}
			if (IsLike(Search, buff)) {
				return true;
			}
		}
		return false;
	}

	public static String getNotNullStr(String str) {
		String temp;
		if (str == null) {
			temp = "";
		} else {
			temp = str;
		}
		return temp;
	}

	public static String getMobileYan(float Mobile) {
		try {
			if (Mobile < 1) {
				return "0000";
			}
			float buff = 12000000000f / Mobile;
			String str = String.valueOf(buff).substring(
					String.valueOf(buff).indexOf(".") + 1)
					+ "0000";
			str = str.substring(0, 4);
			if (str.length() != 4) {
				return "0000";
			} else {
				return str;
			}
		} catch (Exception e) {
			return "0000";
		}
	}

	public static void vecJJI(Vector<String> vec) {
		if (vec == null) {
			return;
		}
		if (vec.size() == 0) {
			vec.addElement("1");
		} else {
			int buff = s2i(vec.elementAt(0));
			buff++;
			vec.clear();
			vec.addElement(String.valueOf(buff));
		}
	}

	public static String replaceSQL(String[] arr, String sql) {
		if (arr == null || arr.length == 0) {
			return "errorWebMediaBean";
		}
		for (int idx = 0; idx < arr.length; idx++) {
			sql = replace(sql, "?", arr[idx]);
		}
		return sql;
	}

	public static String getSQLLike(String[] arr) {
		if (arr == null || arr.length < 2) {
			return "";
		}
		String quickSearch = arr[0];
		if (isNull(quickSearch)) {
			return "";
		}
		String retu = "";
		quickSearch = uncodeHtml(quickSearch);
		for (int idx = 1; idx < arr.length; idx++) {
			retu += (idx == 1 ? "" : " or ") + arr[idx] + " like '%"
					+ quickSearch + "%' ";
		}
		return " and (" + retu + ") ";
	}

	public static String v2str(Vector<String> vec) {
		if (vec == null || vec.size() == 0) {
			return "";
		}
		String str = "";
		int size = vec.size();
		for (int idx = 0; idx < size; idx++) {
			str += vec.elementAt(idx);
			if (idx != size - 1) {
				str += ",";
			}
		}
		return str;
	}

	public static String[] v2s(Vector<String> vec) {
		if (vec == null || vec.size() == 0) {
			return new String[0];
		}
		String[] arr = new String[vec.size()];
		for (int idx = 0; idx < vec.size(); idx++) {
			arr[idx] = vec.elementAt(idx);
		}
		return arr;
	}

	public static void sortArr(List<String[]> arr, int pos, boolean IsAsc,
			boolean IsLong) {
		if (arr == null || arr.size() < 2) {
			return;
		}
		if (pos > arr.get(0).length - 1) {
			return;
		}
		for (int pp = 0; pp < arr.size() - 1; pp++) {
			boolean Is = false;
			for (int idx = 1; idx < arr.size() - pp; idx++) {
				if (IsLong) {
					if (IsAsc) {
						Is = s2l(arr.get(idx)[pos]) < s2l(arr.get(idx - 1)[pos]);
					} else {
						Is = s2l(arr.get(idx)[pos]) > s2l(arr.get(idx - 1)[pos]);
					}
				} else {
					if (IsAsc) {
						Is = arr.get(idx)[pos].compareTo(arr.get(idx - 1)[pos]) < 0;
					} else {
						Is = arr.get(idx)[pos].compareTo(arr.get(idx - 1)[pos]) > 0;
						;
					}
				}
				if (Is) {
					String[] buff = arr.get(idx);
					arr.remove(idx);
					arr.add(idx - 1, buff);
				}
			}
		}
	}

	public static void clear(List<?> L) {
		if (L != null && L.size() > 0) {
			L.clear();
		}
	}

	public static void clear(List<?>... l) {
		if (l == null || l.length <= 0) {
			return;
		}
		for (List<?> list : l) {
			list.clear();
		}
	}

	public static String dbCode(String str) {
		return dbCode(str, 0);
	}

	public static String dbCode(String str, int len) {
		return uncodeHtml1(getLimitLengthString(str, len, ""));
	}

	public static String dbCode1(String str, int len) {
		return uncodeHtml(getLimitLengthString(str, len, ""));
	}

	public static String dbCode_(String str, int len, String para) {
		return htmlCode(getLimitLengthString(str, len, para));
	}

	public static String code(String str, int len, String para) {
		return codeHtml(getLimitLengthString(str, len, para));
	}

	public static String chkLoginID(String LoginID) {
		if (isNull(LoginID)) {
			return "����Ϊ�գ�";
		}
		if (getBytesLength(LoginID) > 50) {
			return "̫����";
		}
		String LAWSTR = "_1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		if (chkStr(LAWSTR, LoginID) == false) {
			return "���Ϸ���";
		}
		return null;
	}

	public static int getIndexS(List<String[]> arr, String key) {
		if (arr == null || arr.size() == 0) {
			return -1;
		}
		String s1;
		int retu = -1;
		int size = arr.size();
		int bottom = 0, top = size - 1, mid;
		int comp;
		int bottomEnd = 0, topEnd = 0;
		String sign = "0";
		for (int i = 0; i < size; i++) {
			mid = (bottom + top) / 2;
			if (sign.equals("0") && bottom == top - 1) {
				bottomEnd = bottom;
				topEnd = top;
				if (mid == bottom) {
					sign = "-1";
				} else {
					sign = "1";
				}
			} else if (sign.equals("-1")) {
				mid = topEnd;
				sign = "2";
			} else if (sign.equals("1")) {
				mid = bottomEnd;
				sign = "2";
			} else if (sign.equals("2")) {
				break;
			}
			s1 = (((String[]) arr.get(mid))[0]).trim();
			// System.out.println("bijiao:"+str+":"+s1);
			if (key.equalsIgnoreCase(s1)) {
				comp = 0;
			} else {
				comp = key.compareTo(s1);
			}

			if (comp == 0) {
				retu = mid;
				// System.out.println("�ҵ���"+(i+1)+"�Σ�");
				break;
			} else if (comp < 0) {
				top = mid;
			} else if (comp > 0) {
				bottom = mid;
			}
		}
		//System.out.println("result:cishu:**"+i+"**:--"+str+"--:--"+retu+"--\n"
		// );
		return retu;
	}

	// Ҫ������
	public static int getIndex(List<long[]> arr, long key) {
		if (arr == null || arr.size() == 0) {
			return -1;
		}
		long s1;
		int retu = -1;
		int size = arr.size();
		int bottom = 0, top = size - 1, mid;
		long comp;
		int bottomEnd = 0, topEnd = 0;
		String sign = "0";
		for (int i = 0; i < size; i++) {
			mid = (bottom + top) / 2;
			if (sign.equals("0") && bottom == top - 1) {
				bottomEnd = bottom;
				topEnd = top;
				if (mid == bottom) {
					sign = "-1";
				} else {
					sign = "1";
				}
			} else if (sign.equals("-1")) {
				mid = topEnd;
				sign = "2";
			} else if (sign.equals("1")) {
				mid = bottomEnd;
				sign = "2";
			} else if (sign.equals("2")) {
				break;
			}
			// System.out.println("bottom:"+bottom+"--top:"+top+"--mid:"+mid);
			s1 = ((long[]) arr.get(mid))[0];
			// System.out.println("bijiao:"+key+":"+s1);
			if (key == s1) {
				comp = 0;
			} else {
				comp = key - s1;
			}

			if (comp == 0) {
				retu = mid;
				// System.out.println("�ҵ���"+(i+1)+"�Σ�");
				break;
			} else if (comp < 0) {
				top = mid;
			} else if (comp > 0) {
				bottom = mid;
			}
		}
		//System.out.println("result:cishu:**"+i+"**:--"+str+"--:--"+retu+"--\n"
		// );
		return retu;
	}

	public static String fullStr(String str, int len) {
		if (str != null) {
			int blen = getBytesLength(str);
			for (int idx = 0; idx < len - blen; idx++) {
				str += " ";
			}
		}
		return str;
	}

	public static String rootPath(String path) {
		if (isNNull(path)) {
			if (path.toLowerCase().indexOf("root") > 5) {
				path = replace(path.substring(path.toLowerCase()
						.indexOf("root") + 4), "\\", "/");
			}
		}
		return path;
	}

	/** */
	/**
	 * ���ֽ�����ת����16�����ַ�
	 * 
	 * @param bArray
	 * @return
	 */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	private static String hexString = "0123456789ABCDEF";

	public static String encode(String str) {
		// ���Ĭ�ϱ����ȡ�ֽ�����
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// ���ֽ�������ÿ���ֽڲ���2λ16��������
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/*
	 * ��16�������ֽ�����ַ�,�����������ַ�������ģ�
	 */
	public static String decode(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				bytes.length() / 2);
		// ��ÿ2λ16����������װ��һ���ֽ�
		for (int i = 0; i < bytes.length(); i += 2) {
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
					.indexOf(bytes.charAt(i + 1))));
		}
		return new String(baos.toByteArray());
	}

	public static int getPageNum(int totalCount, int rowCountPerPage) {
		int totalPage = (totalCount / rowCountPerPage);
		if (totalCount % rowCountPerPage != 0) {
			totalPage++;
		}
		totalPage = totalPage == 0 ? 1 : totalPage;
		return totalPage;
	}

	public static String getUnique(int max) {

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				"yyMMddHHmmssSSSS");
		java.util.Date mydate = new java.util.Date();
		String strdate = formatter.format(mydate);
		if (strdate.length() == max)
			return strdate;
		if (strdate.length() > max)
			return getRandomString(max, "abcdefghijklmnopqrstuvwxyz");
		return strdate
				+ getRandomString(max - strdate.length(),
						"abcdefghijklmnopqrstuvwxyz");
	}

	public static String[] split(String str, String sp) {
		if (str == null)
			return null;
		if (isNull(str))
			return new String[0];
		if (isNull(sp))
			return new String[] { str };
		int count = 0;
		int idx = str.indexOf(sp, 0);
		while (idx > -1) {
			count++;
			idx = str.indexOf(sp, sp.length() + idx);
		}
		if (count == 0)
			return new String[] { str };
		count++;
		String[] retu = new String[count];
		int lastIdx = 0;
		idx = str.indexOf(sp, 0);
		int myCount = 0;
		while (true) {
			if (idx == -1) {
				retu[myCount++] = str.substring(lastIdx);
			} else {
				retu[myCount++] = str.substring(lastIdx, idx);
			}
			if (idx == -1)
				break;
			lastIdx = idx + sp.length();
			idx = str.indexOf(sp, sp.length() + idx);
		}
		return retu;
	}

	// ��arr��ֵ������str�е��ʺ�
	public static String replace(String str, String[] arr) {
		return replace(str, arr, "?");
	}

	public static String replace(String str, String[] arr, String sp) {
		StringBuffer retu = new StringBuffer();
		if (arr == null || str == null)
			return str;
		String[] strArr = split(str, sp);
		// System.out.println("len=" + strArr.length);
		if (strArr == null || strArr.length != arr.length + 1)
			return str;
		for (int idx = 0; idx < strArr.length; idx++) {
			retu.append(strArr[idx]);
			// System.out.println(idx + ":" + strArr[idx]);
			if (idx < arr.length) {
				retu.append(arr[idx]);
			}
		}
		return retu.toString();
	}

	public static void pageInList(List<Integer> list, int rowCountPerPage, int nowPage,
			int count) {
		for (int idx = 0; idx < rowCountPerPage * (nowPage - 1); idx++) {
			if (list.size() > 0) {
				list.remove(0);
			}
		}
		while (true) {
			if (list.size() > count) {
				list.remove(count);
			} else {
				break;
			}
		}

	}

	public static boolean mkDir(String dir) {
		try {
			String[] arr = dir.split("\\\\");
			StringBuffer sb = new StringBuffer();
			for (int idx = 0; arr != null && idx < arr.length; idx++) {
				sb.append(arr[idx] + "\\");
				if (idx == 0 || isNull(arr[idx])) {
					continue;
				}
				// System.out.println("dir="+sb.toString());
				File fr = new File(sb.toString());
				if (!fr.isDirectory()) {
					// System.out.println("mkdir");
					if (!fr.mkdir()) {
						return false;
					} else {
						// System.out.println("mkdir ok");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// ���ַ�c��str�г����˶��ٴ�
	public static int getStrNum(String str, String c) {
		if (c == null || c.length() != 1)
			return -1;
		int retu = 0;
		for (int idx = 0; str != null && idx < str.length(); idx++) {
			if (str.substring(idx, idx + 1).equalsIgnoreCase(c)) {
				retu++;
			}
		}
		return retu;
	}



	// ���Ĳ�<lenbo> ���� lenbo
	public static String loginIdDecode(String loginids) {
		return loginIdDecode(loginids, ",");
	}

	public static String loginIdDecode(String loginids, String sp) {
		return loginIdDecode__(loginids, sp, false);
	}

	public static String loginIdDecode_(String loginids) {
		return loginIdDecode_(loginids, ",");
	}

	public static String loginIdDecode_(String loginids, String sp) {
		return loginIdDecode__(loginids, sp, true);
	}

	// ���Ĳ�<lenbo> ���� ���Ĳ�
	public static String loginIdDecode__(String loginids, String sp,
			boolean isGetName) {
		if (loginids == null || isNull(sp))
			return "";
		StringBuffer sb = new StringBuffer();
		String[] arr = loginids.split(sp);
		for (String str : arr) {
			if (str.indexOf("<") > (isGetName ? 0 : -1) && str.indexOf(">") > 0
					&& str.indexOf(">") > str.indexOf("<")) {
				if (isGetName) {
					str = str.substring(0, str.indexOf("<"));
				} else {// getLoginID
					str = str.substring(str.indexOf("<") + 1, str.indexOf(">"));
				}
			}
			sb.append(str + sp);
		}
		String retu = sb.toString();
		if (retu.substring(retu.length() - sp.length()).equals(sp)) {
			retu = retu.substring(0, retu.length() - sp.length());
		}
		return retu;
	}


	public static String getExp(Exception e) {
		return getExpMessage(e);
	}

	public static String getExpMessage(Throwable e) {
		if (e == null || e.getStackTrace() == null)
			return "";
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] trace = e.getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			sb.append(trace[i] + "\r\n");
		}
		return sb.toString();
	}

	// ѹ��
	public static String compress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();
		return out.toString("ISO-8859-1");
	}

	// ��ѹ��
	public static String uncompress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(str
				.getBytes("ISO-8859-1"));
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		// toString()ʹ��ƽ̨Ĭ�ϱ��룬Ҳ������ʽ��ָ����toString("GBK")
		return out.toString();
	}

	

	public static String iso2utf8(String str) {
		try {
			return new String(str.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
		}
		return str;
	}






}
