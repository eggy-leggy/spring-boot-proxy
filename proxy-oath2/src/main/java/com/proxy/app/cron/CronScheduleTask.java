package com.proxy.app.cron;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.proxy.app.bean.PostToESBInfoBean;
import com.proxy.app.service.CTESBForwardService;
import com.proxy.config.CTRestClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    LinkedBlockingQueue<PostToESBInfoBean> queue = new LinkedBlockingQueue<>();

    @Scheduled(cron = "* 0/5 * * * ?")
    private void printTask() {
        logger.info("now date is {}", new Date());
    }

}
