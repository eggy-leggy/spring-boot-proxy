package com.proxy;

import com.proxy.utils.EncryptionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : Frank F
 * @ClassName: MainTest
 * @Descripsion :
 * @Date : 2019-09-01 21:20
 * @Version :  0.0.1
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTest {
    private static final Logger logger = LoggerFactory.getLogger(MainTest.class);


    @Test
    public void test() {


        logger.info("{}", EncryptionUtils.md5("{\"tenantId\":\"3000000006346\",\"userAccount\":\"admin_3000000064534\"}"));
//
//        RestTemplate restTemplate = new RestTemplate();
//        logger.info(EncryptionUtils.md5AndSha("a123456" + "c1d06b9cea934decb4ed8e01f0f2cd8f"));
//        String res = restTemplate.getForObject("https://www.baidu.com", String.class);
//        logger.info(res);
    }
}
