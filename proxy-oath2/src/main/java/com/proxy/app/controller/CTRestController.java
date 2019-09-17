package com.proxy.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.proxy.config.CTRestClientConfig;
import com.proxy.utils.DataFormatUtils;
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

    @RequestMapping(value = "rest", produces = "text/plain;charset=utf-8")
    public Object gerRemoteUrl(@RequestParam(value = "url", required = true) String url,
                               @RequestParam(value = "format", required = false) String format,
                               @RequestBody String body) {

        logger.info("url is [{}] post body is [{}]", url, body);

        if (null == format) {
            format = "json";
        }
        switch (format) {
            case "json":
                break;
            case "xml":
                body = DataFormatUtils.xml2json(body);
                break;
            default:
                return new ResponseEntity<String>("请求数据格式只能是 json 或 xml", HttpStatus.BAD_REQUEST);
        }

        return ctRestClientConfig.requestWithSign(url, format, body);
    }
}
