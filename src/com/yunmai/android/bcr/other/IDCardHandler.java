package com.yunmai.android.bcr.other;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class IDCardHandler extends DefaultHandler{

	private StringBuffer resultSB = new StringBuffer("");
	private String result = "";
	
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if(localName.equals("name")){
			resultSB.append("姓名：").append(result).append("\r\n");
		}else if(localName.equals("cardno")){
			resultSB.append("证号：").append(result).append("\r\n");
		}else if(localName.equals("sex")){
			resultSB.append("性别：").append(result).append("\r\n");
		}else if(localName.equals("folk")){
			resultSB.append("民族：").append(result).append("\r\n");
		}else if(localName.equals("birthday")){
			resultSB.append("生日：").append(result).append("\r\n");
		}else if(localName.equals("address")){
			resultSB.append("地址：").append(result).append("\r\n");
		}else if(localName.equals("issue_authority")){
			resultSB.append("签发单位：").append(result).append("\r\n");
		}else if(localName.equals("valid_period")){
			resultSB.append("有效期：").append(result).append("\r\n");
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		result = new String(ch, start, length);
	}

	
	public String getResult(){
		return resultSB.toString();
	}
}
