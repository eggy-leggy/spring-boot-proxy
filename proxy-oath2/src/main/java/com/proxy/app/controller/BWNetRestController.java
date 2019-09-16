package com.proxy.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.proxy.config.BRestClientConfig;
import com.proxy.utils.AESUtils;
import com.proxy.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author Frank F
 * @description: baiwang 外网访问接口
 * @create 2019-09-07 14:42
 */

@RestController
@RequestMapping(value = "/bwnet")
public class BWNetRestController {
    private final static Logger logger = LoggerFactory.getLogger(BWNetRestController.class);

    @Value(value = "${bw.aes.password}")
    private String password;

    @Value(value = "${bw.esb.url.invoicePush}")
    private String invoicePushURL;

    @Autowired
    private BRestClientConfig restClientConfig;

    @PostMapping(value = "invoice/push")
    public Object InvoicePush(@RequestBody JSONObject json) {
        if (json.containsKey("data")) {
            String aseStr = json.getString("data");
            String aesResult = AESUtils.decrypt(aseStr, password);
            if (null == aesResult) {
                return new ResponseEntity<String>("AES 解密失败", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json;charset=utf-8");
            HttpEntity<String> request = new HttpEntity<>(aesResult, headers);
            logger.info("请求URL [{}] post body [{}]", invoicePushURL, aesResult);
            return restTemplate.postForEntity(invoicePushURL, request, String.class);
        } else {
            return new ResponseEntity<String>("未发现data", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "index.html")
    public Object hello() {
        return "welcome to yf";
    }
}
