package com.proxy.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * @author : Frank F
 * @ClassName: SignUtils
 * @Descripsion :
 * @Date : 2019-09-03 21:52
 * @Version :  0.0.1
 */


public class SignUtils {

    private static Logger logger = LoggerFactory.getLogger(SignUtils.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 给TOP请求签名。
     *
     * @param params 所有字符型的TOP请求参数
     * @param secret 签名密钥
     * @return 签名
     * @throws Exception
     */
    public static String signTopRequest(Map<String, Object> params, String secret, String body) throws Exception {
        // 第一步：检查参数是否已经排序
        ArrayList<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        query.append(secret);
        for (String key : keys) {
            String value = String.valueOf(params.get(key));
            if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
                query.append(key).append(value);
            }
        }

        logger.info("签名公共参数 [{}]",query.toString());

        // 2017.11.02新增，验签包括request body，API version 3.0
        JsonNode node = mapper.readTree(body);
        body = mapper.writeValueAsString(node);
        query.append(body);
        query.append(secret);
        // 第三步：使用MD5加密
        byte[] bytes;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ignored) {
            throw new Exception(ignored);
        }

        logger.info("sign签名全部字符串 [{}]",query.toString());

        bytes = md5.digest(query.toString().getBytes(StandardCharsets.UTF_8));
        // 第四步：把二进制转化为大写的十六进制
        StringBuilder sign = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
}
