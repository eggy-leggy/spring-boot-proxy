package com.proxy.app.service;

import com.alibaba.fastjson.JSONObject;
import com.proxy.app.bean.PostToESBInfoBean;
import com.proxy.config.ESBRestClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

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

    public boolean postXmlToESB(String url, Collection<Object> collection) {
        List<Object> result = new ArrayList<>();
        int i = 0;
        for (Object obj : collection) {
            if (i == 50) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jsonReq = new JSONObject();
                jsonReq.put("datas", result);
                ResponseEntity<String> res = esbRestClientConfig.postXmlToESB(url, jsonReq.toJSONString());
                result.clear();
                i = 0;
            }
            result.add(obj);
            i++;
        }
        if (!result.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            JSONObject jsonReq = new JSONObject();
            jsonReq.put("datas", result);
            ResponseEntity<String> res = esbRestClientConfig.postXmlToESB(url, jsonReq.toJSONString());
        }
        return false;
    }
}
