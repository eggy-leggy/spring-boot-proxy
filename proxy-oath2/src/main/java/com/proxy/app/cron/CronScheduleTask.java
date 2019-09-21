package com.proxy.app.cron;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.proxy.config.CTRestClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> map = new HashMap<>();
        ResponseEntity<String> res = ctRestClientConfig.requestWithSign("https://corpsz.ctrip.com/flightBaseData/queryCity", "json", "{\"auth\":{\"appKey\":\"YF_APPKEY\",\"ticket\":\"YF_TICKET\" }}");
        if (res.getStatusCodeValue() == 200) {
            logger.info(res.getBody());
            JSONObject json = JSONObject.parseObject(res.getBody());

            if (json.containsKey("datas") && json.get("datas") instanceof JSONArray) {
                JSONArray arr = json.getJSONArray("datas");

                for (Object obj : arr) {
                    JSONObject js = JSONObject.parseObject(String.valueOf(obj));
                    if (js.containsKey("cityID")) {
                        if (map.containsKey(js.getString("cityID"))) {
                            logger.info("城市号重复 {}", js.getString("cityID"));
                        }
                        map.put(js.getString("cityID"), js.toJSONString());
                    }
                }
            }
            logger.info("map size is {}", map.size());
        }
    }
}
