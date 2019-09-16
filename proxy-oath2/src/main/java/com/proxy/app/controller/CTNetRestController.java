package com.proxy.app.controller;


import com.alibaba.fastjson.JSONObject;
import com.proxy.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 外网访问接口
 */
@RestController
@RequestMapping(value = "/ctnet")
public class CTNetRestController {
    private final static Logger logger = LoggerFactory.getLogger(CTNetRestController.class);


    @RequestMapping(value = "hello")
    public Object InvoicePush(@RequestBody JSONObject jsonObject) {
        return R.ok();
    }

}
