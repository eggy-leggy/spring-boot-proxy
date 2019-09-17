package com.proxy.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ChunqiangFan
 * @description: 数据格式转换工具
 * @create 2019-09-17 17:27
 */

public class DataFormatUtils {
    private final static Logger logger = LoggerFactory.getLogger(DataFormatUtils.class);

    public static String xml2json(String xmlStr) {
        //converting xml to json
        try {
            JSONObject obj = XML.toJSONObject(xmlStr);
            return obj.toString();
        } catch (JSONException e) {
            logger.warn("XML 字符串格式不正确 {}", e.getMessage());
            return null;
        }
    }

    public static String json2xml(String jsonStr) {
        try {
            JSONObject obj = new JSONObject(jsonStr);
            //converting json to xml
            String xmlStr = XML.toString(obj);
            return xmlStr;
        } catch (JSONException e) {
            logger.warn("JSON 字符串格式不正确 {}", e.getMessage());
            return null;
        }

    }

    public static void main(String[] args) {
        System.out.println(DataFormatUtils.xml2json("<student><name>Neeraj Mishra</name><age>22</age></student>"));
        System.out.println(DataFormatUtils.json2xml("{\"student\":[{\"name\":\"Neeraj Mishra\", \"age\":\"22\"},{\"name\":\"Neeraj Mishra\", \"age\":\"22\"},{\"name\":\"Neeraj Mishra\", \"age\":\"22\"}]}"));
    }


}
