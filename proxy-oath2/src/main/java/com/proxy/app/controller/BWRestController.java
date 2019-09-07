package com.proxy.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.proxy.config.BRestClientConfig;
import com.proxy.utils.R;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Object getRemoteUrl(@RequestParam(value = "method", required = true) String method,
                               @RequestParam(value = "format", required = false) String format,
                               @RequestBody JSONObject json) {
        if (null == format) {
            format = "json";
        }
        logger.info("method is [{}] format is [{}] post body is [{}]", method, format, json.toJSONString());
        switch (format) {
            case "json":
            case "xml":
                break;
            default:
                return R.error(HttpStatus.SC_BAD_REQUEST, "get data format must be json/xml");
        }
        return restClientConfig.requestWithSign(method, json.toJSONString(), format);
    }
}
