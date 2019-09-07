package com.proxy.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.proxy.entity.BPubParameter;
import com.proxy.entity.BTokenParamNew;
import com.proxy.factory.HttpsClientRequestFactory;
import com.proxy.properties.BUserAccountProperties;
import com.proxy.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Frank F
 * @ClassName: OathTokenConfig
 * @Descripsion :
 * @Date : 2019-09-04 23:24
 * @Version :  0.0.1
 */


@Configuration
public class CTRestClientConfig {
    private final static Logger logger = LoggerFactory.getLogger(CTRestClientConfig.class);

    private
    String baseUrl = "https://ct.ctrip.com/SwitchAPI/Order/Ticket";

    // 过期时间固定2小时
    private long expiresIn = 60 * 60 * 2 * 1000;

    private String appKey = "";

    private String appSecurity = "";

    private String accessToken;

    private Map<String, String> auth = new HashMap<>();

    private long getTokenTimestamp = 0L;


    private boolean getNewToken() throws Exception {
        getTokenTimestamp = new Date().getTime();
        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        Map<String, Object> map = new HashMap<>();
        map.put("appKey", appKey);
        map.put("appSecurity", appSecurity);
        HttpEntity<String> request = new HttpEntity<>(JSONObject.toJSONString(map), headers);
        logger.info(request.getBody());
        ResponseEntity<String> res = restTemplate.postForEntity(baseUrl, request, String.class);
        if (res.getStatusCode() == HttpStatus.OK) {
            JSONObject json = JSON.parseObject(res.getBody());
            logger.info(json.toJSONString());
            if (json.containsKey("Ticket")) {
                accessToken = json.getString("Ticket");
                auth.put("appKey", appKey);
                auth.put("Ticket", accessToken);
                return true;
            }
        }
        return false;

    }

    private R tokenIsExpired() {
        if (new Date().getTime() - getTokenTimestamp >= expiresIn) {
            try {
                boolean rc = this.getNewToken();
                if (rc) {
                    return R.ok();
                }
            } catch (Exception e) {
                logger.info("获取token失败 {}", e.getMessage());
                return R.error("获取token失败 ==>" + e.getMessage());
            }
        }
        return R.error("获取token失败");
    }

    public R requestWithSign(String url, JSONObject body, String format) {
        R r = tokenIsExpired();
        if (!String.valueOf(r.get("code")).equals("0")) {
            return r;
        }
        body.put("auth", auth);
        logger.info("accessToken is {}", accessToken);
        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());

        ResponseEntity<String> res = restTemplate.postForEntity(url, body.toJSONString(), String.class);
        if (res.getStatusCode() == HttpStatus.OK) {
            return R.ok(res.getBody());
        }
        return R.error(res.getStatusCodeValue(), res.getBody());
    }


}
