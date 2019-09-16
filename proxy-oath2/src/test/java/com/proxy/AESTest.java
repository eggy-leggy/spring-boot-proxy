package com.proxy;

import com.proxy.utils.AESUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author : Frank F
 * @ClassName: AESTest
 * @Descripsion :
 * @Date : 2019-09-10 23:15
 * @Version :  0.0.1
 */


public class AESTest {
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    public static final String content1 = "H42NR37LSgd7j5Ut3WihJnggExMjlKPT21kQH2Dr5xWI8vsyVOkitZJjIc3MmC49+v mF93mHPJP0PvZwyY9TeXw1lmGY/JTxSDw2ACOC5La+2feYT7p8Yy0yxLfkHGj3tkTYQNToXSDZy + dZSaxciA/y64EfqWLeANKI6mPoucqDluEoBLRUG4uc55IdIvsLRjjqeApCQEOBu3hKvpmswquz hRHrpZn/Itg+2EQQJ2We/EDq3PQ1O68hL86N+MqRyO8xaTnU96kBpvMoTcMW5GUxbRsC68mP/m5 R0/ddvbuQPFLA16eqEEocySce7kYtCqLZ5LUgshhWFJvC89RSLScvymoOfkpaoR4LFD5kl5DYOX vlbulE7QC5EWcnLZo/r8ssh4wbFOsu3o3+0L8H9RnyzQqmTT0w07BvlyK6/M0kiETEd3hGBl4U2 01CPSRVxhOHjzyDNx+ER3j4H6PRvnjNzofGPpqRPqTwvjgi56iqAFLdq2FWHyClB8TbFQDCd/SD fMm0AdIbj2SfY7lHgPBz+IJR19lUEZljj06uewXGFPIRHxuRh8aq27S4k0wuRni2Ncjeeisk9Kd 8 ZDCozd/g1xSS2WKFfcdOSFzheafIkR9CVRLEAGUGRkUj0UXwXnPknd11I4fsnDqQbuniCu9zDE fGcnw+ObXoVV1PYP14Qsujd0Dn/kXHct7SbCVWRpD0feVgPSSMkWQuMacdiD2JC9Y5ToulpBjsx 1FROqGhEJp+AK/dS8T/wqgjswDN8xRF531FCT4ipixQee3BV62x3j8Za0yaM8Qcp3uIXXPH/vve p1RhroUgOpmhteVIEhiEpYtqLPmrmab0mvbbZTuJmvPSzwlRP8Q0voyuwIX5Qu1E29Ig5jLRFL6 8 vAvQ30LjY/SFiiNp0EIZTRUAOwRUEVWbFFRew7pDnms3r8IhYjhQooo88FzeqHPYTiXocuRyyy +TirjLk/g/bQj6f4LeinfRINvEbpb0Lg+miknQwSJkRhHW4jWVruk2efuutEIqL4HAZbRBYopgy uxS7dwYS6VtUVmlbkwt5ncn/WbEwdEGzkyedbZD0WR4JJfj2bEO+vDL8mdVyeG1pifRQxBsbpVG bEZbc+3dV2KXnsgARDC7Yb2qMbbs8L7Fh2NwWsb2Fb3VQXdI4YAaG9b4LgeKSHMd7QyKpVf1WdY tosYMZqWeRD39FpKeWIo9lO9BKNOoi56q2VBuFvC7vLgoMrZDcqFYUefRX23aT2geUvh87RREoW I/jMW1 UYhz+0GGnZdZVogVR0CuJAoSmTdvfE0UHjFgR0K0z4sFcGgFFt6wOyG1giA04p87nGqkL Mvb0xNzXIqUBwQjkUbVEm/ooiaHXhJrzr9z/bVO1ec56wNRcc2cb66ilBGR+fi5tJ47t5XTnQ8dpbHd3DWoXXDLjEtdk7fHRQw6Vp7FH7Ftb0gFK9fUg5LmNifq0YORjKyujGYIn7rqTumgnBykSXw TkgOVtIl2I+qJOYHP8J0zN0CMzuxyLQ6AzcUyMLv/H/5GWIhZUKrxB3Z4wZdDi40tdo15HZPp/D tTagrKvDdiZCZEq7KGPxyw8BC/w3cCtc/70nKrqkHppFl/e+YawufeRc5gijWs06YnoQZt05x0y 5 CDr3FiLf/HfxCkq7RiWl4vgKYQ92kuBGepucpUU9z8owfYjrJKHkRJ+Ui4ichGGb9wDIJlscOc Eiiotbes0b4HSNMLOfK3itWLbFe8ZMQ7M1fxyqTUDsWCiyDwcoM4gYiv7+U5d7P/wfb3 vdkHg8M 2 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 ZEBKaSu";
    @Test
    public void test() {
        String res = AESUtils.decrypt(content1,"ZCBW0824");
        System.out.println(res);
    }

    public static String decrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,getSecretKey(password));
            byte[] res = cipher.doFinal(Base64.decodeBase64(content));
            return new String(res,"utf-8");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //    生成加密密钥
    private static SecretKeySpec getSecretKey(final String password) {
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            kg.init(secureRandom);
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESTest.class.getName()).log(Level.SEVERE
                    , null, ex);
        }
        return null;
    }

}
