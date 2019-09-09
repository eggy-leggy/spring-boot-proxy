package com.proxy.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * @author : Frank F
 * @ClassName: SignUtils
 * @Descripsion :
 * @Date : 2019-09-03 21:52
 * @Version :  0.0.1
 */


public class SignUtils {

    private static Logger logger = LoggerFactory.getLogger(SignUtils.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 给TOP请求签名。
     *
     * @param params 所有字符型的TOP请求参数
     * @param secret 签名密钥
     * @return 签名
     * @throws Exception
     */
    public static String signTopRequest(Map<String, Object> params, String secret, String body) throws Exception {
        // 第一步：检查参数是否已经排序
        ArrayList<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        query.append(secret);
        for (String key : keys) {
            String value = String.valueOf(params.get(key));
            if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
                query.append(key).append(value);
            }
        }

        logger.info("签名公共参数 [{}]",query.toString());

        // 2017.11.02新增，验签包括request body，API version 3.0
        JsonNode node = mapper.readTree(body);
        body = mapper.writeValueAsString(node);
        query.append(body);
        query.append(secret);
        // 第三步：使用MD5加密
        byte[] bytes;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ignored) {
            throw new Exception(ignored);
        }

        logger.info("sign签名全部字符串 [{}]",query.toString());

        bytes = md5.digest(query.toString().getBytes(StandardCharsets.UTF_8));
        // 第四步：把二进制转化为大写的十六进制
        StringBuilder sign = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }


    public static void main(String[] args) {
        String str = "a8a4438e-1583-4031-b516-7472b4d1040eappKey1000371formatjsonmethodbaiwang.invoice.uploadtimestamp1567994373508tokenf0650496-e0e3-42df-af7d-0f8ad89a4ba4typesyncversion3.1{\"requestId\":\"9b07e840-e984-44fc-9a9b-e5debec92afe\",\"invoiceUploadType\":\"3\",\"machineNo\":\"499098936901\",\"autoOpen\":\"\",\"pushChannelFlag\":\"\",\"invoiceCode\":\"043001600511\",\"organizationName\":\"\",\"invoiceNo\":\"84597967\",\"invoiceDate\":\"2018-08-22 10:00:00\",\"invoiceClosingDate\":\"2018-08-22 10:00:00\",\"returnType\":\"\",\"taxControlCode\":\"03*6+808+<28/75077++87<28*98769*7>+380685*6+2+3+847+599+6/96*256+<8<>440517059003<<79>860+2/1<016/46195>4-+0>282\",\"invoiceCheckCode\":\"10637211788999063509\",\"invoiceQrCode\":\"\",\"sellerTaxNo\":\"91430102553032598N\",\"sellerName\":\"湖南万家丽集团国际购物广场有限公司\",\"sellerAddressPhone\":\"长沙市芙蓉区火星路68号第1栋（万家丽路一段99号）0731-85909688\",\"sellerBankAccount\":\"\",\"deviceType\":\"0\",\"organizationCode\":\"\",\"serialNo\":\"1062000520322\",\"invoiceTypeCode\":\"026\",\"invoiceTerminalCode\":\"wjl026\",\"buyerTaxNo\":\"\",\"buyerName\":\"浏阳金科置业有限公司\",\"buyerAddressPhone\":\"0731-82716655\",\"buyerBankAccount\":\"1901026809025993868\",\"drawer\":\"王璐\",\"checker\":\"王璐\",\"payee\":\"王璐\",\"invoiceType\":\"0\",\"invoiceListMark\":\"0\",\"redInfoNo\":\"\",\"originalInvoiceCode\":\"\",\"originalInvoiceNo\":\"\",\"taxationMode\":\"0\",\"deductibleAmount\":\"\",\"invoiceTotalPrice\":\"862.07\",\"invoiceTotalTax\":\"137.93\",\"invoiceTotalPriceTax\":\"1000.00\",\"signatureParameter\":\"0000004282000000\",\"taxDiskNo\":\"\",\"taxDiskKey\":\"\",\"taxDiskPassword\":\"\",\"goodsCodeVersion\":\"\",\"consolidatedTaxRate\":\"\",\"notificationNo\":\"\",\"remarks\":\"\",\"invoiceDetailsList\":[{\"goodsLineNo\":\"1\",\"goodsLineNature\":\"0\",\"goodsCode\":\"1030308010000000000\",\"goodsExtendCode\":\"\",\"goodsName\":\"*茶*小罐茶品鉴装\",\"goodsTaxItem\":\"\",\"goodsSpecification\":\"\",\"goodsUnit\":\"\",\"goodsQuantity\":\"10.0\",\"goodsPrice\":\"86.206897\",\"goodsTotalPrice\":\"862.07\",\"goodsTotalTax\":\"137.93\",\"goodsTaxRate\":\"0.16\",\"goodsDiscountLineNo\":\"\",\"priceTaxMark\":\"0\",\"vatSpecialManagement\":\"\",\"freeTaxMark\":\"\",\"preferentialMark\":\"0\"}],\"apiName\":\"baiwang.invoice.upload\",\"taxNo\":\"91430102553032598N\",\"methodCode\":\"1009\"}a8a4438e-1583-4031-b516-7472b4d1040e";

        // 第三步：使用MD5加密
        byte[] bytes;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ignored) {
            try {
                throw new Exception(ignored);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        logger.info("sign签名全部字符串 [{}]",str);

        bytes = md5.digest(str.getBytes(StandardCharsets.UTF_8));
        // 第四步：把二进制转化为大写的十六进制
        StringBuilder sign = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        System.out.println(sign.toString());

    }
}
