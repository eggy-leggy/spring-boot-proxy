package com.proxy.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JsonToSqlUtils {


    public static String insertStr = "INSERT INTO %s (%s) VALUES (%s);";


    public static String JsonToInsertSql(String tableName, String cols, Object jsonObj) {
        StringBuffer sb = new StringBuffer();
        JSONObject json = (JSONObject) jsonObj;
        Set<String> colums;
        if (cols == null) {
            colums = json.keySet();

        } else {
            colums = new HashSet<>(Arrays.asList(cols.split(",")));
        }
        StringBuffer colsb = new StringBuffer();
        for (String col : colums) {
            colsb.append(col);
            colsb.append(",");
            if (json.containsKey(col)) {
                sb.append("'");
                sb.append(json.getString(col).replaceAll("'", "''"));
                sb.append("',");
            } else {
                return null;
            }
        }
        sb = sb.deleteCharAt(sb.lastIndexOf(","));
        colsb = colsb.deleteCharAt(colsb.lastIndexOf(","));
        return String.format(insertStr, tableName, colsb, sb.toString());

    }
}
