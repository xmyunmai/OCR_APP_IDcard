package com.yunmai.android.bcr.other;

import java.util.ArrayList;


public class XMLFunctions {
	public static String code(String str){
		str = CommonFunctions.replace(str, "<", "&lt;");
		str = CommonFunctions.replace(str, ">", "&gt;");
		return str;
	}
	public static String decode(String str){
		str = CommonFunctions.replace(str, "&lt;", "<");
		str = CommonFunctions.replace(str, "&gt;", ">");
		return str;
	}
	public static String getXMLValue(String xml, String tag) {
		return getXMLValue(xml, "<" + tag + ">", "</" + tag + ">");
	}

	public static int getXMLValueI(String xml, String tag) {
		return CommonFunctions.s2i(getXMLValue(xml, tag));
	}

	public static int getXMLValueI(String xml, String tag1, String tag2) {
		return CommonFunctions.s2i(getXMLValue(xml, tag1, tag2));
	}

	public static String getXMLValue(String xml, String tag1, String tag2) {
		String retu = "";
		int p1 = xml.indexOf(tag1);
		int p2 = xml.indexOf(tag2);
		if (p1 >= 0 && p2 >= 0 && p2 > p1) {
			retu = xml.substring(p1, p2);
			retu = retu.substring(tag1.length());
		}
		return retu;
	}

	// xml:<a>1</a><a>2</a><a>3</a>返回new String{"1","2","3"}
	public static String[] getXMLValues(String xml, String tag) {
		return getXMLValues(xml, "<" + tag + ">", "</" + tag + ">");
	}
	public static String[] getXMLValues(String xml, String tag1, String tag2) {
		String[] retu = new String[0];
		int max = 0;
		String xmlOld = xml;
		int p1 = xml.indexOf(tag1);
		int p2 = xml.indexOf(tag2);
		while (CommonFunctions.isNNull(xml)) {
			if (p1 >= 0 && p2 >= 0 && p2 > p1) {
				xml = xml.substring(p2 + tag2.length());
				max++;
			} else {
				xml = "";
			}
			p1 = xml.indexOf(tag1);
			p2 = xml.indexOf(tag2);
		}
		xml=xmlOld;
		if(max>0){
			retu = new String[max];
			max=0;
			p1 = xml.indexOf(tag1);
			p2 = xml.indexOf(tag2);
			while (CommonFunctions.isNNull(xml)) {
				if (p1 >= 0 && p2 >= 0 && p2 > p1) {
					String buff=xml.substring(p1,p2);
					buff=buff.substring(tag1.length());
					retu[max]=buff;
					max++;
					xml = xml.substring(p2 + tag2.length());
				} else {
					xml = "";
				}
				p1 = xml.indexOf(tag1);
				p2 = xml.indexOf(tag2);
			}
		}
		return retu;
	}
	public static ArrayList<String> parseRow(String str,String tag){
		String p1="<"+tag+">";
		String p2="</"+tag+">";
		String value=str;
		ArrayList<String> list=new ArrayList<String>();
		int index=value.indexOf(p1);
		for(;index!=-1;){
			int index2=value.indexOf(p2);
			if(index2==-1)break;
			String row=value.substring(index+p1.length(),index2);
			list.add(row);
			value=value.substring(index2+p2.length());
			index=value.indexOf(p1);
		}
		return list;
	}
	//parseList(modValue,"_c_row","_c_it");
	public static ArrayList<String[]> parseList(String modValue,String rowTag,String fieldTag){
		ArrayList<String[]> list=new ArrayList<String[]>();
		String value=modValue;

		ArrayList<String> listRow=parseRow(value,rowTag);
		for(int idx=0;idx<listRow.size();idx++){
			ArrayList<String> detail=parseRow(listRow.get(idx),fieldTag);
			if(detail.size()>0){
				String [] arr=new String [detail.size()];
				for(int a=0;a<detail.size();a++){
					arr[a]=detail.get(a);
				}
				list.add(arr);
			}
			detail.clear();
			detail=null;
		}
		listRow.clear();
		listRow=null;
		return list;
	}
	public static void main(String[] args) {
		String[] arr=getXMLValues("<a><c>cc</c><d>cc</d></a><a><c></c><d>cc</d></a>","a");
		for(int idx=0;arr!=null && idx<arr.length;idx++){
			System.out.println(arr.length+":"+arr[idx]);
		}
	}

}
