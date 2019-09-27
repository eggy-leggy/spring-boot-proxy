package com.proxy.app.controller;

import com.proxy.app.common.GeneralConstant;
import com.proxy.app.service.CTNetRestService;
import com.proxy.config.CTRestClientConfig;
import com.proxy.utils.DataFormatUtils;
import com.proxy.utils.FileObjectUtils;
import com.proxy.utils.JsonToSqlUtils;
import com.proxy.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Frank F
 * @description: Ct rest 服务
 * @create 2019-09-07 14:43
 */

@RestController
@RequestMapping(value = "/ct")
public class CTRestController {
    private final static Logger logger = LoggerFactory.getLogger(CTRestController.class);

    @Autowired
    private CTRestClientConfig ctRestClientConfig;

    @Autowired
    private CTNetRestService ctNetRestService;


    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

    @Value(value = "${ct.esb.cache.filePath}")
    private String filePath;

    @Value(value = "${ct.esb.table.hotelCity}")
    private String hotelCityTable;

    @Value(value = "${ct.esb.table.airPortCity}")
    private String airportCityTable;

    @RequestMapping(value = "rest", produces = "text/plain;charset=utf-8")
    public Object gerRemoteUrl(@RequestParam(value = "url", required = true) String url,
                               @RequestParam(value = "format", required = false) String format,
                               @RequestBody String body) {

        logger.info("url is [{}] post body is [{}]", url, body);

        if (null == format) {
            format = "json";
        }
        switch (format) {
            case "json":
                break;
            case "xml":
                body = DataFormatUtils.xml2json(body);
                break;
            default:
                return new ResponseEntity<String>("请求数据格式只能是 json 或 xml", HttpStatus.BAD_REQUEST);
        }

        return ctRestClientConfig.requestWithSign(url, format, body);
    }

    private static int maxCount = 20000;

    @RequestMapping(value = "save/hotelCity")
    public Object getHotelCity() {

        Map<String, Object> citysMap = new HashMap<>();
        Map<String, Object> hc = ctNetRestService.getHotelCountry();
        int index = 1;
        for (String countryID : hc.keySet()) {
            Map<String, Object> hcce = ctNetRestService.getHotelCountryCityExtend(countryID);
            FileObjectUtils.writeObjectToFile(filePath, String.format(GeneralConstant.HOTEL_CITY_FILE, countryID), hcce);
            if (!hcce.isEmpty()) {
                citysMap.putAll(hcce);
            }
            if (citysMap.size() > maxCount) {
                saveJsonToSqlFile(citysMap, hotelCityTable, null, String.format("hotelCityINIT_%s_%d.sql", sf.format(new Date()), index), true);
                index++;
                citysMap.clear();
            }
        }
        if (citysMap.size() > maxCount) {
            saveJsonToSqlFile(citysMap, hotelCityTable, null, String.format("hotelCityINIT_%s_%d.sql", sf.format(new Date()), index), true);
            citysMap.clear();
        }
        return R.ok();
    }

//    public static String cols = "code,countryID,cityID,name,provinceID,countryCode,countryName";

    @GetMapping(value = "save/airPortCity")
    public Object getAirPortCity() {
        Map<String, Object> map = ctNetRestService.getAirPortCity();
        if (map.isEmpty()) {
            return ResponseEntity.noContent();
        }
        FileObjectUtils.writeObjectToFile(filePath, GeneralConstant.AIRPORT_CITY_FILE, map);
        boolean rc = saveJsonToSqlFile(map, airportCityTable, null, String.format("airPortCityINIT_%s.sql", sf.format(new Date())), false);
        if (rc) {
            return R.ok();
        }
        return R.error();
    }


    private Boolean saveJsonToSqlFile(Map<String, Object> map, String tableName, String cols, String fileName, Boolean isAppend) {
        StringBuffer sb = new StringBuffer();
        for (Object jsonObj : map.values()) {
            String content = JsonToSqlUtils.JsonToInsertSql(tableName, cols, jsonObj);
            if (null != content) {
                sb.append(content);
                sb.append("\r\n");
            }
        }
        try {
            FileObjectUtils.writeFile(filePath, fileName, sb.toString(), isAppend);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
