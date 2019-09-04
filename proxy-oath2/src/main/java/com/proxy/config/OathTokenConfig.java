package com.proxy.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.proxy.entity.BTokenParamNew;
import com.proxy.properties.BUserAccountProperties;
import com.proxy.utils.EncryptionUtils;
import com.proxy.utils.FormParamUtils;
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

/**
 * @author : Frank F
 * @ClassName: OathTokenConfig
 * @Descripsion :
 * @Date : 2019-09-04 23:24
 * @Version :  0.0.1
 */


@Configuration
@EnableConfigurationProperties(value = BUserAccountProperties.class)
public class OathTokenConfig {

    @Autowired
    private BUserAccountProperties properties;

    @Value(value = "${yf.bw.url}")
    private String baseUrl;

    private long expiresIn;

    private String accessToken;

    private String refreshToken;

    private long getTokenTimestamp;


    public void getNewToken() {
        RestTemplate restTemplate = new RestTemplate();
        BTokenParamNew tp = new BTokenParamNew();
        tp.setMethod("baiwang.oauth.token");
        tp.setGrantType("password");
        tp.setClientId(properties.getAppKey());
        tp.setClientSecret(properties.getAppSecret());
        tp.setUsername(properties.getUserAccount());
        tp.setPassword(EncryptionUtils.md5AndSha(properties.getPassword() + properties.getSalt()));
        tp.setVersion(properties.getVersion());
        tp.setTimestamp(new Date().getTime());

        String formParam = FormParamUtils.parseObject2FormParam(tp);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity request = new HttpEntity(requestBody, headers);
        System.out.printf(baseUrl);
        ResponseEntity<String> res = restTemplate.postForEntity(baseUrl + "?" + formParam, request, String.class);
        if (res.getStatusCode() == HttpStatus.OK) {
            JSONObject json = JSON.parseObject(res.getBody());

//            "access_token" -> "a1a66c77-3afa-4b2f-8341-537c67582e6e"
//            "refresh_token" -> "e9fdfc06-bd45-4a5c-9add-0392fb7b5e06"
//            "scope" -> "server"
//            "token_type" -> "bearer"
//            "expires_in" -> {Integer@6010} 9882

            if (json.containsKey("response")) {
                JSONObject tokenJson = json.getJSONObject("response");
                accessToken = tokenJson.getString("access_token");
                refreshToken = tokenJson.getString("refresh_token");
                expiresIn = tokenJson.getLongValue("expires_in") * 1000L;
            }
        }

    }


}
