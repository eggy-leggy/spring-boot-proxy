package com.proxy.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.proxy.entity.BPubParameter;
import com.proxy.entity.BTokenParamNew;
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
import java.util.Map;

/**
 * @author : Frank F
 * @ClassName: OathTokenConfig
 * @Descripsion :
 * @Date : 2019-09-04 23:24
 * @Version :  0.0.1
 */


@Configuration
@EnableConfigurationProperties(value = BUserAccountProperties.class)
public class BRestClientConfig {
    private final static Logger logger = LoggerFactory.getLogger(BRestClientConfig.class);

    @Autowired
    private
    BUserAccountProperties properties;

    @Value(value = "${yf.bw.url}")
    private
    String baseUrl;

    private long expiresIn;

    private String accessToken;

    private String refreshToken;

    private long getTokenTimestamp = 0L;


    private boolean getNewToken() throws Exception {
        getTokenTimestamp = new Date().getTime();

        BTokenParamNew tp = new BTokenParamNew();
        tp.setMethod("baiwang.oauth.token");
        tp.setGrantType("password");
        tp.setClientId(properties.getAppKey());
        tp.setClientSecret(properties.getAppSecret());
        tp.setUsername(properties.getUserAccount());
        tp.setPassword(EncryptionUtils.md5AndSha(properties.getPassword() + properties.getSalt()));
        tp.setVersion(properties.getVersion());
        tp.setTimestamp(getTokenTimestamp);

        String formParam = FormParamUtils.parseObject2FormParam(tp);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> res = restTemplate.postForEntity(baseUrl + "?" + formParam, request, String.class);
        if (res.getStatusCode() == HttpStatus.OK) {
            JSONObject json = JSON.parseObject(res.getBody());
            if (json.containsKey("response")) {
                JSONObject tokenJson = json.getJSONObject("response");
                logger.info("bw 获取新的token");
                accessToken = tokenJson.getString("access_token");
                refreshToken = tokenJson.getString("refresh_token");
                expiresIn = tokenJson.getLongValue("expires_in") * 1000L;
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

    public R requestWithSign(String method, String body, String format) {
        R r = tokenIsExpired();
        if (!String.valueOf(r.get("code")).equals("0")) {
            return r;
        }
        BPubParameter pubParameter = new BPubParameter();
        pubParameter.setMethod(method);
        pubParameter.setAppKey(properties.getAppKey());
        pubParameter.setToken(accessToken);
        pubParameter.setTimestamp(String.valueOf(new Date().getTime()));
        pubParameter.setFormat(format);
        pubParameter.setVersion(properties.getVersion());
        pubParameter.setType("sync");


        Map<String, Object> map = EntityUtils.entityToMap(pubParameter);
        String sign;
        try {
            sign = SignUtils.signTopRequest(map, properties.getAppSecret(), body);
        } catch (Exception e) {
            logger.warn("sign 签名失败 {}", e.getMessage());
            return R.error("sign 签名失败");
        }
        pubParameter.setSign(sign);
        String formParam = FormParamUtils.parseObject2FormParam(pubParameter);
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        if (null == body) {
            return R.error("业务参数不能为空");
        }
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> res = restTemplate.postForEntity(baseUrl + "?" + formParam, request, String.class);
        if (res.getStatusCode() == HttpStatus.OK) {
            return R.ok(res.getBody());
        }
        return R.error(res.getStatusCodeValue(), res.getBody());
    }


}
