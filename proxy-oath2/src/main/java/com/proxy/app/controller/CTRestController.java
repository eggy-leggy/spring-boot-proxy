package com.proxy.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.proxy.app.service.CTNetRestService;
import com.proxy.config.CTRestClientConfig;
import com.proxy.utils.DataFormatUtils;
import com.proxy.utils.FileObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @Value(value = "${ct.esb.cache.filePath}")
    private String filePath;


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

    @RequestMapping(value = "save/hotelCountry", produces = "text/plain;charset=utf-8")
    public Object getHotelCountry() {

        Map<String, Object> citysMap = new HashMap<>();
        Map<String, Object> hc = ctNetRestService.getHotelCountry();
        for (String countryID : hc.keySet()) {
            Map<String, Object> hcce = ctNetRestService.getHotelCountryCityExtend(countryID);
            if (!hcce.isEmpty()) {
                citysMap.putAll(hcce);
            }
            break;
        }
        logger.info(citysMap.toString());
        return null;
    }

    @RequestMapping(value = "save/airPortCity", produces = "text/plain;charset=utf-8")
    public Object getAirPortCity() {
        Map<String, Object> map = ctNetRestService.getAirPortCity();
        if (map.isEmpty()) {
            return new ResponseEntity<String>("未查询到数据", HttpStatus.NO_CONTENT);
        }
        boolean rc = FileObjectUtils.writeObjectToFile(filePath + "/airPortCity", map);
        if (rc) {
            Map<String, Object> res = (Map<String, Object>) FileObjectUtils.readObjectFromFile(filePath + "/airPortCity");
            logger.info(res.toString());
            return JSONObject.toJSONString(res);
        }

        return null;
    }

}
