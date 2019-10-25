package com.proxy.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.proxy.factory.HttpsClientRequestFactory;
import com.proxy.utils.*;
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
 * @ClassName: OathTokenConfig
 * @Descripsion :
 * @Date : 2019-09-04 23:24
 * @Version :  0.0.1
 */


@Configuration
public class CTRestClientConfig {
    private final static Logger logger = LoggerFactory.getLogger(CTRestClientConfig.class);

    @Value(value = "${ct.account.ticketUrl}")
    private String ticketUrl;

    // 过期时间固定2小时
    private long expiresIn = 60 * 60 * 2 * 1000;

    @Value(value = "${ct.account.appKey}")
    private String appKey;

    @Value(value = "${ct.account.appSecurity}")
    private String appSecurity;

    private String accessToken;

    private final static String YF_APPKEY = "YF_APPKEY";

    private final static String YF_TICKET = "YF_TICKET";

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
        ResponseEntity<String> res = restTemplate.postForEntity(ticketUrl, request, String.class);
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
        return R.ok();
    }

    public ResponseEntity<String> requestWithSign(String url, String format, String postBody) {
        R r = tokenIsExpired();
        if (!String.valueOf(r.get("code")).equals("0")) {
            return new ResponseEntity<String>("sign 签名失败", HttpStatus.UNAUTHORIZED);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        postBody = postBody.replace(YF_APPKEY, appKey).replace(YF_TICKET, accessToken);
        HttpEntity<String> entity = new HttpEntity<>(postBody, headers);
        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
        logger.info("Ctrip 请求 url [{}] post body [{}]", url, entity);
        ResponseEntity<String> res = restTemplate.postForEntity(url, entity, String.class);


        String result = res.getBody();
        logger.info("Ctrip 返回数据 [{}]", result);
        if ("xml".equals(format)) {
            result = DataFormatUtils.xmlAttachBase(DataFormatUtils.json2xml(result));
        }
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "text/plain;charset=UTF-8");
        resHeaders.add("Date", new Date().toString());
        resHeaders.setVary(res.getHeaders().getVary());
        return new ResponseEntity<String>(result, resHeaders, res.getStatusCode());
    }

    public ResponseEntity<String> requestWithSignNoLog(String url, String format, String postBody) {
        R r = tokenIsExpired();
        if (!String.valueOf(r.get("code")).equals("0")) {
            return new ResponseEntity<String>("sign 签名失败", HttpStatus.UNAUTHORIZED);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        postBody = postBody.replace(YF_APPKEY, appKey).replace(YF_TICKET, accessToken);
        HttpEntity<String> entity = new HttpEntity<>(postBody, headers);
        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
//        logger.trace("Ctrip 请求 url [{}] post body [{}]", url, entity);
        ResponseEntity<String> res = restTemplate.postForEntity(url, entity, String.class);
        String result = res.getBody();
//        logger.trace("Ctrip 返回数据 [{}]", result);
        if ("xml".equals(format)) {
            result = DataFormatUtils.xmlAttachBase(DataFormatUtils.json2xml(result));
        }
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "text/plain;charset=UTF-8");
        resHeaders.add("Date", new Date().toString());
        resHeaders.setVary(res.getHeaders().getVary());
        return new ResponseEntity<String>(result, resHeaders, res.getStatusCode());
    }
}
