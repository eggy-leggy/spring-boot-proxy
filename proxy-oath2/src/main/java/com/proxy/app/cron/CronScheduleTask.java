package com.proxy.app.cron;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.proxy.app.bean.PostToESBInfoBean;
import com.proxy.app.service.CTESBForwardService;
import com.proxy.app.service.CTNetRestService;
import com.proxy.config.CTRestClientConfig;
import com.proxy.config.ESBRestClientConfig;
import com.proxy.utils.DataFormatUtils;
import com.proxy.utils.FileObjectUtils;
import com.proxy.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;

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


    @Autowired
    private CTNetRestService ctNetRestService;

    @Value(value = "${ct.esb.cache.filePath}")
    private String filePath;

    @Value(value = "${ct.esb.url.hotelCity}")
    private String hotelCityUrl;

    @Value(value = "${ct.esb.url.airPortCity}")
    private String airPortCityUrl;


    @Scheduled(cron = "${cron.ct.esbPush}")
    private void printTask() {
        PostToESBInfoBean bean = queue.poll();
        if (bean != null) {
            logger.info("开始发送数据 {} {}", bean.getUrl(), bean.getBody());
            ResponseEntity<String> res = esbRestClientConfig.postXmlToESB(bean.getUrl(), bean.getBody());
            if (res.getStatusCodeValue() != 200 || !Objects.requireNonNull(res.getBody()).contains("0")) {
                queue.add(bean);
            }
        }
    }

//    @Scheduled(cron = "")
    public void getHotelCity() {
        Map<String, Object> hc = ctNetRestService.getHotelCountry();
        for (String countryID : hc.keySet()) {
            Map<String, Object> map = ctNetRestService.getHotelCountryCityExtend(countryID);
            if (map.isEmpty()) {
                continue;
            }
            Map<String, Object> baseMap = (Map<String, Object>) FileObjectUtils.readObjectFromFile(filePath, String.format("hotelCity-%s.dat", countryID));
            if (null != baseMap) {
                List<Object> postList = new ArrayList<>();
                for (String key : map.keySet()) {
                    if (baseMap.containsKey(key)) {
                        if (!map.get(key).equals(baseMap.get(key))) {
                            postList.add(map.get(key));
                        }
                    } else {
                        postList.add(map.get(key));
                    }
                    if (postList.size() >= 10) {
                        String xmlStr = DataFormatUtils.listjson2xml(postList);
                        xmlStr = DataFormatUtils.xmlAttachBase(xmlStr);
                        queue.add(new PostToESBInfoBean(hotelCityUrl, xmlStr));
                        postList.clear();
                    }
                }
                if (postList.size() > 0) {
                    String xmlStr = DataFormatUtils.listjson2xml(postList);
                    xmlStr = DataFormatUtils.xmlAttachBase(xmlStr);
                    queue.add(new PostToESBInfoBean(hotelCityUrl, xmlStr));
                }
            }
            FileObjectUtils.writeObjectToFile(filePath, String.format("hotelCity-%s.dat", countryID), map);
        }
    }

//    @Scheduled(cron = "")
    public void getAirPortCity() {
        Map<String, Object> map = ctNetRestService.getAirPortCity();
        Map<String, Object> baseMap = (Map<String, Object>) FileObjectUtils.readObjectFromFile(filePath, "airPortCityFile.dat");
        if (baseMap != null) {
            List<Object> postList = new ArrayList<>();
            for (String key : map.keySet()) {
                if (baseMap.containsKey(key)) {
                    if (!map.get(key).equals(baseMap.get(key))) {
                        postList.add(map.get(key));
                    }
                } else {
                    postList.add(map.get(key));
                }
                if (postList.size() >= 10) {
                    String xmlStr = DataFormatUtils.listjson2xml(postList);
                    xmlStr = DataFormatUtils.xmlAttachBase(xmlStr);
                    queue.add(new PostToESBInfoBean(airPortCityUrl, xmlStr));
                    postList.clear();
                }
            }
            if (postList.size() > 0) {
                String xmlStr = DataFormatUtils.listjson2xml(postList);
                xmlStr = DataFormatUtils.xmlAttachBase(xmlStr);
                queue.add(new PostToESBInfoBean(airPortCityUrl, xmlStr));
            }
        }
        FileObjectUtils.writeObjectToFile(filePath, "airPortCityFile.dat", map);
    }
}
