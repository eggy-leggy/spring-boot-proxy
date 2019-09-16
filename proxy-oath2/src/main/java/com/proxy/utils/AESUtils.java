package com.proxy.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESUtils {

    private final static Logger log = LoggerFactory.getLogger(AESUtils.class);

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    public static String decrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            byte[] res = cipher.doFinal(Base64.decodeBase64(content));
            return new String(res, StandardCharsets.UTF_8);
        } catch (NoSuchPaddingException e) {
            log.warn("未发现此原型 {}", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.warn("未发现此算法 {}", e.getMessage());
        } catch (InvalidKeyException e) {
            log.warn("无效的key {}", e.getMessage());
        } catch (BadPaddingException e) {
            log.warn("错误的原型 {}", e.getMessage());
        } catch (IllegalBlockSizeException e) {
            log.warn("非法的块大小 {}", e.getMessage());
        }
        return null;
    }

    //    生成加密密钥
    public static SecretKeySpec getSecretKey(final String password) {
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            kg.init(secureRandom);
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            log.warn("生成加密秘钥失败 {}", ex.getMessage());
        }
        return null;
    }
}
