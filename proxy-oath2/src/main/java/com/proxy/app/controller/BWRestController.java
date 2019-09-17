package com.proxy.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.proxy.config.BRestClientConfig;
import com.proxy.utils.DataFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping(value = "rest", produces = "text/plain;charset=utf-8")
    public Object getRemoteUrl(@RequestParam(value = "method", required = true) String method,
                               @RequestParam(value = "format", required = false) String format,
                               @RequestParam(value = "version", required = false) String version,
                               @RequestBody String body) {
        if (null == format) {
            format = "json";
        }
        if (null == version) {
            version = "3.0";
        }
        logger.info("method is [{}] format is [{}] post body is [{}]", method, format, body);
        switch (format) {
            case "json":
                break;
            case "xml":
                body = DataFormatUtils.xml2json(body);
                break;
            default:
                return new ResponseEntity<String>("请求数据格式只能是 json 或 xml", HttpStatus.BAD_REQUEST);
        }
        return restClientConfig.requestWithSign(method, body, format, version);
    }
}
