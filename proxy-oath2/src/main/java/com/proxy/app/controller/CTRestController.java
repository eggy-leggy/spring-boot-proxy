package com.proxy.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping(value = "rest/url")
    public Object gerRemoteUrl() {

        return "hellp CT";
    }
}
