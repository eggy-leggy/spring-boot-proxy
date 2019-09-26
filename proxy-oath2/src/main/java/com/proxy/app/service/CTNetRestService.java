package com.proxy.app.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.proxy.config.CTRestClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CTNetRestService {
    private final static Logger logger = LoggerFactory.getLogger(CTNetRestService.class);

    @Autowired
    private CTRestClientConfig ctRestClientConfig;

    @Autowired
    private CTESBForwardService ctesbForwardService;


    @Value(value = "${ct.url.passenger-ticket.airPort}")
    private String PASSENGER_TICKET_AIRPORT_URL;

    @Value(value = "${ct.url.passenger-ticket.airPortCity}")
    private String PASSENGER_TICKET_CITY_URL;

    @Value(value = "${ct.url.hotel.country}")
    private String HOTEL_COUNTRY_URL;

    @Value(value = "${ct.url.hotel.city}")
    private String HOTEL_CITY_URL;


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

    public Map<String, Object> getAirPortCity() {
        Map<String, Object> airPortCitysMap = new HashMap<>();
        ResponseEntity<String> res = ctRestClientConfig.requestWithSign(PASSENGER_TICKET_CITY_URL, "json", "{\"auth\":{\"appKey\":\"YF_APPKEY\",\"ticket\":\"YF_TICKET\" }}");
        if (res.getStatusCodeValue() == 200) {
            JSONObject json = JSONObject.parseObject(res.getBody());
            logger.trace("返回数据: {}", json.toJSONString());
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
        }
        return airPortCitysMap;
    }

    public Map<String, Object> getAirPort() {
        Map<String, Object> airPortsMap = new HashMap<>();

        ResponseEntity<String> res = ctRestClientConfig.requestWithSign(PASSENGER_TICKET_AIRPORT_URL, "json", "{\"auth\":{\"appKey\":\"YF_APPKEY\",\"ticket\":\"YF_TICKET\" }}");
        if (res.getStatusCodeValue() == 200) {
//            logger.info(res.getBody());
            JSONObject json = JSONObject.parseObject(res.getBody());

            if (json.containsKey("datas") && json.get("datas") instanceof JSONArray) {
                JSONArray arr = json.getJSONArray("datas");

                for (Object obj : arr) {
                    JSONObject js = JSONObject.parseObject(String.valueOf(obj));
                    if (js.containsKey("code")) {
                        if (airPortsMap.containsKey(js.getString("code"))) {
                            logger.info("机场代码重复 {}", js.getString("code"));
                        }
                        airPortsMap.put(js.getString("code"), js);
                    }
                }
            }
            logger.info("airPortsMap size is {}", airPortsMap.size());
        }
        return airPortsMap;
    }


    public Map<String, Object> getHotelCountry() {
        Map<String, Object> hotelCountriesMap = new HashMap<>();

        ResponseEntity<String> res = ctRestClientConfig.requestWithSign(HOTEL_COUNTRY_URL, "json", "{\"auth\":{\"appKey\":\"YF_APPKEY\",\"ticket\":\"YF_TICKET\" }}");
        if (res.getStatusCodeValue() == 200) {
//            logger.info(res.getBody());
            JSONObject json = JSONObject.parseObject(res.getBody());
            if (json.containsKey("Data") && json.get("Data") instanceof JSONArray) {
                JSONArray arr = json.getJSONArray("Data");

                for (Object obj : arr) {
                    JSONObject js = JSONObject.parseObject(String.valueOf(obj));
                    if (js.containsKey("Country")) {
                        if (hotelCountriesMap.containsKey(js.getString("Country"))) {
                            logger.info("国家编号重复 {}", js.getString("Country"));
                        }
                        hotelCountriesMap.put(js.getString("Country"), js);
                    }
                }
            }
            logger.info("hotelCountriesMap size is {}", hotelCountriesMap.size());
        }
        return hotelCountriesMap;
    }

    public Map<String, Object> getHotelCountryCityExtend(String countryID) {
        Map<String, Object> hotelMap = new HashMap<>();

        ResponseEntity<String> res = ctRestClientConfig.requestWithSign(HOTEL_CITY_URL,
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
                        hotelMap.put(js.getString("City"), js);
                    }
                }
            }
            logger.info("map size is {}", hotelMap.size());
        }
        return hotelMap;
    }
}
