package com.proxy.app.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.proxy.config.BRestClientConfig;
import com.proxy.utils.DataFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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

        switch (format) {
            case "json":
                logger.info("method is [{}] format is [{}] post body is [{}]", method, format, body);
                break;
            case "xml":
                try {
                    body = DataFormatUtils.xml2json(body);
                    logger.info("method is [{}] format is [{}] post body is [{}]", method, format, body);
                    JSONObject json = JSONObject.parseObject(body);
                    if (json.containsKey("Request")) {
                        json = json.getJSONObject("Request");
                    }
                    if (json.containsKey("invoiceDetailsList")) {
                        if (json.getJSONObject("invoiceDetailsList").containsKey("Item")) {
                            if (json.getJSONObject("invoiceDetailsList").get("Item") instanceof JSONArray) {
                                json.put("invoiceDetailsList", json.getJSONObject("invoiceDetailsList").getJSONArray("Item"));
                            } else if (json.getJSONObject("invoiceDetailsList").get("Item") instanceof JSONObject) {
                                List<JSONObject> list = new ArrayList<>();
                                list.add(json.getJSONObject("invoiceDetailsList").getJSONObject("Item"));
                                json.put("invoiceDetailsList", list);
                            }
                        }
                    }
                    body = json.toJSONString();
                } catch (Exception e) {
                    logger.warn("请求参数不正确 {}", e.getMessage());
                    return new ResponseEntity<String>("请求业务数据xml格式不正确", HttpStatus.BAD_REQUEST);
                }
                break;
            default:
                return new ResponseEntity<String>("请求数据格式只能是 json 或 xml", HttpStatus.BAD_REQUEST);
        }
        return restClientConfig.requestWithSign(method, body, format, version);
    }
}
