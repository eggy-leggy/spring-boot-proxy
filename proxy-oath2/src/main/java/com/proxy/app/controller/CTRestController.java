package com.proxy.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.proxy.config.CTRestClientConfig;
import com.proxy.utils.R;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    CTRestClientConfig ctRestClientConfig;

    @RequestMapping(value = "rest")
    public R gerRemoteUrl(@RequestParam(value = "url", required = true) String url,
                          @RequestParam(value = "format", required = false) String format,
                          @RequestBody JSONObject json) {
        if (null == format) {
            format = "json";
        }
        logger.info("url is [{}] format is [{}] post body is [{}]", url, format, json.toJSONString());
        switch (format) {
            case "json":
            case "xml":
                break;
            default:
                return R.error(HttpStatus.SC_BAD_REQUEST, "get data format must be json/xml");
        }
        return ctRestClientConfig.requestWithSign(url, json, format);
    }
}
