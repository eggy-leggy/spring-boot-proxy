package com.proxy.app.cron;

import com.proxy.app.enums.ESBSyncStatus;
import com.proxy.config.CTRestClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author ChunqiangFan
 * @description:
 * @create 2019-09-20 17:46
 */

@Configuration
@EnableScheduling
public class CronScheduleTask {
    private final static Logger logger = LoggerFactory.getLogger(CronScheduleTask.class);

    @Autowired
    private CTRestClientConfig ctRestClientConfig;

    @Scheduled(cron = "0/5 * * * * ?")
    private void printTask() {
        logger.info("now date is {}", new Date());
    }

    public void printTas1k() {

        ResponseEntity<String> res = ctRestClientConfig.requestWithSign("https://corpsz.ctrip.com/flightBaseData/queryCity", "json", "{\"auth\":{\"appKey\":\"YF_APPKEY\",\"ticket\":\"YF_TICKET\" }}");
        if (res.getStatusCodeValue() == 200) {
            logger.info(res.getBody());
        }
    }
}
