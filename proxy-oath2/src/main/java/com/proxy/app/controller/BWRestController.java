package com.proxy.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.proxy.config.BRestClientConfig;
import com.proxy.utils.R;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Frank F
 * @description: B Rest服务
 * @create 2019-09-07 14:42
 */

@RestController
@RequestMapping(value = "/bw")
public class BWRestController {
    private final static Logger logger = LoggerFactory.getLogger(BWRestController.class);
    @Autowired
    private BRestClientConfig restClientConfig;

    @PostMapping(value = "rest")
    public Object getRemoteUrl(@RequestHeader HttpHeaders headers, @RequestParam(value = "method", required = true) String method,
                               @RequestParam(value = "format", required = false) String format,
                               @RequestParam(value = "version", required = false) String version,
                               @RequestBody JSONObject json) {
        logger.info("request header [{}]", JSONObject.toJSONString(headers));
        if (null == format) {
            format = "json";
        }
        if (null == version) {
            version = "3.0";
        }
        logger.info("method is [{}] format is [{}] post body is [{}]", method, format, json.toJSONString());
        switch (format) {
            case "json":
            case "xml":
                break;
            default:
                return R.error(HttpStatus.SC_BAD_REQUEST, "get data format must be json/xml");
        }
        return restClientConfig.requestWithSign(method, json.toJSONString(), format, version);
    }
}
