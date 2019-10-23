package com.proxy.utils;

import com.alibaba.fastjson.JSONObject;
import com.proxy.entity.BTokenParamNew;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * @author : Frank F
 * @ClassName: FormParamUtils
 * @Descripsion : java对象转换为表单参数
 * @Date : 2019-09-04 23:04
 * @Version :  0.0.1
 */


public class FormParamUtils {

    public static String parseObject2FormParam(Object obj) {
        StringBuffer sb = new StringBuffer();

        JSONObject json = (JSONObject) JSONObject.toJSON(obj);
        for (String key : json.keySet()) {
            if (null != json.get(key)) {
                sb.append(key);
                sb.append("=");
                sb.append(json.getString(key));
                sb.append("&");
            }
        }
        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 1);
        }
        return null;
    }

    public static String parseMap2FormParam(Map<String, Object> map) {
        // 获取对象keys 并按字母顺序排序
        ArrayList<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys);
        // 拼接表单参数
        StringBuffer sb = new StringBuffer();
        for (String key : keys) {
            if (null != map.get(key)) {
                sb.append(key);
                sb.append("=");
                sb.append(map.get(key));
                sb.append("&");
            }
        }
        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 1);
        }
        return null;
    }

    public static void main(String[] args) {
        BTokenParamNew tp = new BTokenParamNew();
        tp.setClientId("10000031");
        tp.setClientSecret("dauhsdasdhaksdhjasdha");
        tp.setGrantType("123");
        tp.setMethod("sync");

        System.out.println(FormParamUtils.parseObject2FormParam(tp));
    }
}
