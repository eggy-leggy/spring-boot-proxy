package com.proxy.app.service;

import com.proxy.config.ESBRestClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Frank F
 * @description: 推向ESB的服务
 * @create 2019-09-23 15:14
 */

@Service
public class CTESBForwardService {
    private final static Logger logger = LoggerFactory.getLogger(CTESBForwardService.class);

    @Autowired
    private ESBRestClientConfig esbRestClientConfig;

    public boolean postXmlToESB(String json) {
        esbRestClientConfig.postXmlToESB("",json);
        return false;
    }
}
