package com.proxy.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.proxy.factory.HttpsClientRequestFactory;
import com.proxy.utils.DataFormatUtils;
import com.proxy.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Frank F
 * @ClassName: ESBRestClientConfig
 * @Descripsion :
 * @Date : 2019-09-04 23:24
 * @Version :  0.0.1
 */


@Configuration
public class ESBRestClientConfig {
    private final static Logger logger = LoggerFactory.getLogger(ESBRestClientConfig.class);

    public ResponseEntity<String> postXmlToESB(String url, String postBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain;charset=UTF-8");
        headers.setDate(new Date().getTime());
        HttpEntity<String> entity = new HttpEntity<>(postBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        logger.info("url is [{}] post body is [{}]", url, entity);
        return restTemplate.postForEntity(url, entity, String.class);
//        return null;
    }
}
