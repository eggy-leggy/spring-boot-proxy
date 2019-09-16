package com.proxy.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.proxy.config.CTRestClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author Frank F
 * @description: Ct rest 服务
 * @create 2019-09-07 14:43
 */

@RestController
@RequestMapping(value = "/ct")
public class CTRestController {
    private final static Logger logger = LoggerFactory.getLogger(CTRestController.class);

    @Autowired
    private CTRestClientConfig ctRestClientConfig;

    @RequestMapping(value = "rest")
    public Object gerRemoteUrl(@RequestParam(value = "url", required = true) String url,
                               @RequestBody JSONObject json) {

        logger.info("url is [{}] post body is [{}]", url, json.toJSONString());

        return ctRestClientConfig.requestWithSign(url, json.toJSONString());
    }
}
