package com.proxy.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 文件 java对象读写工具类
 */
public class FileObjectUtils {
    private final static Logger logger = LoggerFactory.getLogger(FileObjectUtils.class);

    public static boolean writeObjectToFile(String filePath, Object obj) {
        File file = new File(filePath);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            return true;
        } catch (IOException e) {
            logger.warn("java对象写入文件失败 {}", e.getMessage());
            return false;
        }
    }

    public static Object readObjectFromFile(String filePath) {
        Object temp = null;
        File file = new File(filePath);
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(in);
            temp = objIn.readObject();
            objIn.close();
        } catch (IOException e) {
            logger.warn("读取文件失败 {}", e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.warn("未发现对象文件 {}", e.getMessage());
        }
        return temp;
    }

}
