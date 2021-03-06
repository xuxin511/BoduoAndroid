package com.liansu.boduowms.utils;

import android.util.Xml;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.bean.update.ApkInfo;
import com.liansu.boduowms.utils.function.GsonUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ParseXmlService {

    public HashMap<String, String> parseXml(InputStream inputStream) {
        HashMap<String, String> hashMap = null;
        boolean flag = true;
        try {
            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setInput(inputStream, "GB2312");
            int event = pullParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        hashMap = new HashMap<String, String>();
                        break;
                    case XmlPullParser.START_TAG:
                        flag = true;
                        String name = pullParser.getName();
                        if ("VERSIONCODE".equalsIgnoreCase(name) && flag == true) {
                            hashMap.put("versionCode", pullParser.nextText().trim());
                        } else if ("FILENAME".equalsIgnoreCase(name) && flag == true) {
                            hashMap.put("fileName", pullParser.nextText().trim());
                        } else if ("LOADURL".equalsIgnoreCase(name) && flag == true) {
                            hashMap.put("loadUrl", pullParser.nextText().trim());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        flag = false;
                        break;
                }
                event = pullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashMap;
    }


    public HashMap<String, String> parseJson(String json) {
        HashMap<String, String> hashMap = null;
        boolean flag = true;
        try {
            ApkInfo apkInfo = GsonUtil.getGsonUtil().fromJson(json, new TypeToken<ApkInfo>() {
            }.getType());
            if (apkInfo != null) {
                hashMap=new HashMap<>();
                hashMap.put("versionCode", apkInfo.getVersionCode() + "");
                hashMap.put("fileName", apkInfo.getFileName());
                hashMap.put("loadUrl", apkInfo.getDownloadUrl());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashMap;
    }

}  
