package com.proxy.app.cron;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.proxy.app.bean.PostToESBInfoBean;
import com.proxy.app.service.CTESBForwardService;
import com.proxy.config.CTRestClientConfig;
import com.proxy.config.ESBRestClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Frank F
 * @description:
 * @create 2019-09-20 17:46
 */

@Configuration
@EnableScheduling
public class CronScheduleTask {
    private final static Logger logger = LoggerFactory.getLogger(CronScheduleTask.class);

    private static LinkedBlockingQueue<PostToESBInfoBean> queue = new LinkedBlockingQueue<>();

    @Autowired
    private ESBRestClientConfig esbRestClientConfig;

    @Value(value = "${ct.esb.cache.filePath}")
    private String filePath;

    @Scheduled(cron = "${cron.ct.esbPush}")
    private void printTask() {
        boolean flag = true;
        PostToESBInfoBean bean = queue.poll();
        if (bean != null) {
            ResponseEntity<String> res = esbRestClientConfig.postXmlToESB(bean.getUrl(), bean.getBody());
            if (res.getStatusCodeValue() != 200) {
                queue.add(bean);
            }
        }
        logger.info("now date is {}", new Date());
    }

}
