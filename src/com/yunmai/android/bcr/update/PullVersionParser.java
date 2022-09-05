package com.yunmai.android.bcr.update;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;


import android.util.Xml;

public class PullVersionParser {

    private List<String> chineseSimplifiedContent;
    private List<String> englishContent;
    private List<String> chineseTraditionalContent;
	public VersionEntity parse(InputStream is) throws Exception {  
        VersionEntity versionEntity = null;  
        
        XmlPullParser parser = Xml.newPullParser(); //由android.util.Xml创建一个XmlPullParser实例  
        parser.setInput(is, "UTF-8");                              //设置输入流 并指明编码方式  
  
        int eventType = parser.getEventType();  
        while (eventType != XmlPullParser.END_DOCUMENT) {  
            switch (eventType) {  
            case XmlPullParser.START_DOCUMENT:  
                break;  
            case XmlPullParser.START_TAG:  
                if (parser.getName().equals("upgrade")) {  
                	versionEntity = new VersionEntity();  
                } else if (parser.getName().equals("VersionCode")) {  
                    eventType = parser.next();  
                    versionEntity.setVersionCode(parser.getText());  
                } else if (parser.getName().equals("VersionName")) {  
                    eventType = parser.next();  
                    versionEntity.setVersionName(parser.getText());  
                } else if (parser.getName().equals("Mandatory")) {  
                    eventType = parser.next();  
                    versionEntity.setMandatory(parser.getText());  
                } else if (parser.getName().equals("CNSContent")) {  
                    eventType = parser.next();  
                    chineseSimplifiedContent = new ArrayList<String>();
                }  else if (parser.getName().equals("CNSCitem")) {  
                    eventType = parser.next();  
                    chineseSimplifiedContent.add(parser.getText());
                }else if (parser.getName().equals("ENContent")) {  
                    eventType = parser.next();  
                    englishContent = new ArrayList<String>();
                }  else if (parser.getName().equals("ENCitem")) {  
                    eventType = parser.next();  
                    englishContent.add(parser.getText());
                } else if (parser.getName().equals("CNTContent")) {  
                    eventType = parser.next();  
                    chineseTraditionalContent = new ArrayList<String>();
                }  else if (parser.getName().equals("CNTCitem")) {  
                    eventType = parser.next();  
                    chineseTraditionalContent.add(parser.getText());
                }  
                else if (parser.getName().equals("DownloadUrl")) {  
                    eventType = parser.next();  
                    versionEntity.setDownloadUrl(parser.getText());  
                }  
                else if (parser.getName().equals("Size")) {  
                    eventType = parser.next();  
                    versionEntity.setSize(parser.getText());  
                }  
                else if (parser.getName().equals("Date")) {  
                    eventType = parser.next();  
                    versionEntity.setDate(parser.getText());  
                } 
                break;  
            case XmlPullParser.END_TAG:  
                if (parser.getName().equals("CNSContent")) {  
                	  versionEntity.setChineseSimplifiedContent(chineseSimplifiedContent); 
                }
                if (parser.getName().equals("ENContent")) {  
              	  versionEntity.setEnglishContent(englishContent); 
              }  
                if (parser.getName().equals("CNTContent")) {  
              	  versionEntity.setChineseTraditionalContent(chineseTraditionalContent); 
              }  
                break;  
            }  
           eventType = parser.next();  
        }  
        return versionEntity;  
    }  
}
