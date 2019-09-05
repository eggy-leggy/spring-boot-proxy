package com.proxy.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author : Frank F
 * @ClassName: EncryptionUtils
 * @Descripsion :
 * @Date : 2019-09-01 21:23
 * @Version :  0.0.1
 */


public class EncryptionUtils {
    /**
     * md5+sha-1加密
     *
     * @param inputText 要加密的内容（明文密码+用户盐值）
     */
    public static String md5AndSha(String inputText) {
        return sha(md5(inputText));
    }

    /**
     * md5加密
     */
    public static String md5(String inputText) {
        return encrypt(inputText, "md5");
    }

    /**
     * sha加密
     */
    public static String sha(String inputText) {
        return encrypt(inputText, "sha-1");
    }


    /**
     * md5或者sha-1加密
     *
     * @param inputText     要加密的内容
     * @param algorithmName 加密算法名称：md5或者sha-1，不区分大小写
     */
    private static String encrypt(String inputText, String algorithmName) {
        if (inputText == null || "".equals(inputText.trim())) {
            throw new IllegalArgumentException("请输入要加密的内容");
        }
        if (algorithmName == null || "".equals(algorithmName.trim())) {
            algorithmName = "md5";
        }
        String encryptText = null;
        try {
            MessageDigest m = MessageDigest.getInstance(algorithmName);
            m.update(inputText.getBytes(StandardCharsets.UTF_8));
            byte[] s = m.digest();
            return hex(s);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptText;
    }

    /**
     * 返回十六进制字符串
     */
    private static String hex(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }
}
