package com.proxy.app.cron;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

/**
 * @author Frank F
 * @description:
 * @create 2019-09-20 17:46
 */

@Configuration
@EnableScheduling
public class CronScheduleTask {
    private final static Logger logger = LoggerFactory.getLogger(CronScheduleTask.class);

    private Map<String, Object> airPortCitysMap = new HashMap<>();

    private Map<String, Object> hotelMap = new HashMap<>();

    @Autowired
    private CTRestClientConfig ctRestClientConfig;

    @Autowired
    private CTESBForwardService ctesbForwardService;

    @Scheduled(cron = "0/5 * * * * ?")
    private void printTask() {
        logger.info("now date is {}", new Date());
    }

    /**
     * "https://corpsz.ctrip.com/flightBaseData/queryAirPort"
     * "https://corpsz.ctrip.com/flightBaseData/queryCity"
     * "https://ct.ctrip.com/corpopenapi/HotelCity/GetCountry"
     * "https://ct.ctrip.com/corpopenapi/HotelCity/GetCountryCityExtend"
     *
     * @return : void
     * @author : Frank F
     * @date : 2019/9/23 14:05
     */

    public void getAirPortCity() {
        ResponseEntity<String> res = ctRestClientConfig.requestWithSign("https://corpsz.ctrip.com/flightBaseData/queryCity", "json", "{\"auth\":{\"appKey\":\"YF_APPKEY\",\"ticket\":\"YF_TICKET\" }}");
        if (res.getStatusCodeValue() == 200) {
            JSONObject json = JSONObject.parseObject(res.getBody());
            logger.info("返回数据: {}", json.toJSONString());
            if (json.containsKey("datas") && json.get("datas") instanceof JSONArray) {
                JSONArray arr = json.getJSONArray("datas");

                for (Object obj : arr) {
                    JSONObject js = JSONObject.parseObject(String.valueOf(obj));
                    if (js.containsKey("cityID")) {
                        if (airPortCitysMap.containsKey(js.getString("cityID"))) {
                            logger.info("城市号重复 {}", js.getString("cityID"));
                        }
                        airPortCitysMap.put(js.getString("cityID"), js);
                    }
                }
            }
            logger.info("airPortCitysMap size is {}", airPortCitysMap.size());
            List<Object> result = new ArrayList<>();
            int i = 0;
            for (Object obj : airPortCitysMap.values()) {
                if (i == 50) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("datas", result);
                    ctesbForwardService.postXmlToESB(JSONObject.toJSONString(map));
                    result.clear();
                    i = 0;
                }
                result.add(obj);
                i++;
            }
        }
    }

    public void getAirPort() {
        Map<String, String> map = new HashMap<>();
        ResponseEntity<String> res = ctRestClientConfig.requestWithSign("https://corpsz.ctrip.com/flightBaseData/queryAirPort", "json", "{\"auth\":{\"appKey\":\"YF_APPKEY\",\"ticket\":\"YF_TICKET\" }}");
        if (res.getStatusCodeValue() == 200) {
//            logger.info(res.getBody());
            JSONObject json = JSONObject.parseObject(res.getBody());

            if (json.containsKey("datas") && json.get("datas") instanceof JSONArray) {
                JSONArray arr = json.getJSONArray("datas");

                for (Object obj : arr) {
                    JSONObject js = JSONObject.parseObject(String.valueOf(obj));
                    if (js.containsKey("code")) {
                        if (map.containsKey(js.getString("code"))) {
                            logger.info("机场代码重复 {}", js.getString("code"));
                        }
                        map.put(js.getString("code"), js.toJSONString());
                    }
                }
            }
            logger.info("map size is {}", map.size());
        }
    }


    public Map<String, String> getHotelCountry() {
        Map<String, String> map = new HashMap<>();
        ResponseEntity<String> res = ctRestClientConfig.requestWithSign("https://ct.ctrip.com/corpopenapi/HotelCity/GetCountry", "json", "{\"auth\":{\"appKey\":\"YF_APPKEY\",\"ticket\":\"YF_TICKET\" }}");
        if (res.getStatusCodeValue() == 200) {
//            logger.info(res.getBody());
            JSONObject json = JSONObject.parseObject(res.getBody());
            if (json.containsKey("Data") && json.get("Data") instanceof JSONArray) {
                JSONArray arr = json.getJSONArray("Data");

                for (Object obj : arr) {
                    JSONObject js = JSONObject.parseObject(String.valueOf(obj));
                    if (js.containsKey("Country")) {
                        if (map.containsKey(js.getString("Country"))) {
                            logger.info("国家编号重复 {}", js.getString("Country"));
                        }
                        map.put(js.getString("Country"), js.toJSONString());
                    }
                }
            }
            logger.info("map size is {}", map.size());
        }
        return map;
    }

    public void getHotelCountryCityExtend(String countryID) {
        ResponseEntity<String> res = ctRestClientConfig.requestWithSign("https://ct.ctrip.com/corpopenapi/HotelCity/GetCountryCityExtend",
                "json", "{\"auth\":{\"appKey\":\"YF_APPKEY\",\"ticket\":\"YF_TICKET\" },\"CountryId\":" + countryID + "}");
        if (res.getStatusCodeValue() == 200) {
//            logger.info(res.getBody());
            JSONObject json = JSONObject.parseObject(res.getBody());
            if (json.containsKey("Data") && json.get("Data") instanceof JSONArray) {
                JSONArray arr = json.getJSONArray("Data");

                for (Object obj : arr) {
                    JSONObject js = JSONObject.parseObject(String.valueOf(obj));
                    if (js.containsKey("City")) {
                        if (hotelMap.containsKey(js.getString("City"))) {
                            logger.info("国家号 {} 酒店城市 编号重复 {} 城市名称 {}", countryID, js.getString("City"), js.getString("CityName"));
                        }
                        hotelMap.put(js.getString("City"), js.toJSONString());
                    }
                }
            }
            logger.info("map size is {}", hotelMap.size());
        }
    }

    public Map<String, Object> getHotelMap() {
        return hotelMap;
    }
}
