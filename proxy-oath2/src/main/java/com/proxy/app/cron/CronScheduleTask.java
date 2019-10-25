package com.proxy.app.cron;

import com.proxy.app.bean.PostToESBInfoBean;
import com.proxy.app.common.GeneralConstant;
import com.proxy.app.service.CTNetRestService;
import com.proxy.config.ESBRestClientConfig;
import com.proxy.utils.DataFormatUtils;
import com.proxy.utils.FileObjectUtils;
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
            logger.info("发送结果 {}", res.getBody());
            if (res.getStatusCodeValue() != 200 || !Objects.requireNonNull(res.getBody()).contains("0")) {
                logger.info("发送失败，放入队列等待重新发送");
                queue.add(bean);
            }
        }
    }

    /**
     * 酒店 城市信息同步
     */
    @Scheduled(cron = "${cron.ct.hotelCity}")
    public void getHotelCityTask() {
        logger.info("开始同步酒店-城市信息");
        Map<String, Object> hc = ctNetRestService.getHotelCountry();
        List<Object> postList = new ArrayList<>();
        for (String countryID : hc.keySet()) {
            Map<String, Object> map = ctNetRestService.getHotelCountryCityExtend(countryID);
            if (map.isEmpty()) {
                continue;
            }
            Map<String, Object> baseMap = (Map<String, Object>) FileObjectUtils.readObjectFromFile(filePath, String.format(GeneralConstant.HOTEL_CITY_FILE, countryID));
            if (null != baseMap) {
                for (String key : map.keySet()) {
                    listTool(postList, baseMap, map, key, hotelCityUrl);
                }
                if (postList.size() > 0) {
                    String xmlStr = DataFormatUtils.listjson2xml(postList);
                    xmlStr = DataFormatUtils.xmlAttachBase(xmlStr);
                    queue.add(new PostToESBInfoBean(airPortCityUrl, xmlStr));
                    postList.clear();
                }
            }

            FileObjectUtils.writeObjectToFile(filePath, String.format(GeneralConstant.HOTEL_CITY_FILE, countryID), map);
        }

    }

    /**
     * 机场 城市信息同步
     */
    @Scheduled(cron = "${cron.ct.airPortCity}")
    public void getAirPortCityTask() {
        logger.info("开始同步机场-城市信息");
        Map<String, Object> map = ctNetRestService.getAirPortCity();
        Map<String, Object> baseMap = (Map<String, Object>) FileObjectUtils.readObjectFromFile(filePath, GeneralConstant.AIRPORT_CITY_FILE);
        if (baseMap != null) {
            List<Object> postList = new ArrayList<>();
            for (String key : map.keySet()) {
                listTool(postList, baseMap, map, key, airPortCityUrl);
            }
            if (postList.size() > 0) {
                String xmlStr = DataFormatUtils.listjson2xml(postList);
                xmlStr = DataFormatUtils.xmlAttachBase(xmlStr);
                queue.add(new PostToESBInfoBean(airPortCityUrl, xmlStr));
            }
        }
        FileObjectUtils.writeObjectToFile(filePath, GeneralConstant.AIRPORT_CITY_FILE, map);
    }

    private void listTool(List<Object> postList, Map baseMap, Map map, String key, String url) {
        if (baseMap.containsKey(key)) {
            if (!map.get(key).equals(baseMap.get(key))) {
                if (null != map.get(key)) {
                    postList.add(map.get(key));
                }
            }
        } else {
            if (null != map.get(key)) {
                postList.add(map.get(key));
            }
        }
        if (postList.size() >= 10) {
            String xmlStr = DataFormatUtils.listjson2xml(postList);
            xmlStr = DataFormatUtils.xmlAttachBase(xmlStr);
            queue.add(new PostToESBInfoBean(url, xmlStr));
            postList.clear();
        }
    }

}
