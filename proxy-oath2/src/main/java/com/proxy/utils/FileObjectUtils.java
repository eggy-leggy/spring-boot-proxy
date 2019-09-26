package com.proxy.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 文件 java对象读写工具类
 */
public class FileObjectUtils {
    private final static Logger logger = LoggerFactory.getLogger(FileObjectUtils.class);

    public static void writeObjectToFile(String filePath, String fileName, Object obj) {
        File file = new File(filePath, fileName);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
        } catch (IOException e) {
            logger.warn("java对象写入文件失败 {}", e.getMessage());
        }
    }

    public static Object readObjectFromFile(String filePath, String fileName) {
        Object temp = null;
        File file = new File(filePath, fileName);
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


    /**
     * 添加内容到指定文件 如果该文件不存在，则创建并添加内容 如果该文件已存在，则添加内容到已有内容最后
     * flag为true，则向现有文件中添加内容，否则覆盖原有内容
     */
    public static void writeFile(String filePath, String fileName, String fileContent,
                                 boolean flag) throws IOException {
        if (null == fileContent || fileContent.length() < 1)
            return;
        File file = new File(filePath, fileName);

        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file, flag);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
        osw.write(fileContent);
        osw.flush();
        osw.close();
    }
}
