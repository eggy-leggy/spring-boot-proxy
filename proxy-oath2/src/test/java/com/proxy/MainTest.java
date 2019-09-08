package com.proxy;

import com.alibaba.fastjson.JSONObject;
import com.proxy.config.BRestClientConfig;
import com.proxy.config.CTRestClientConfig;
import com.proxy.utils.EncryptionUtils;
import com.proxy.utils.R;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author : Frank F
 * @ClassName: MainTest
 * @Descripsion :
 * @Date : 2019-09-01 21:20
 * @Version :  0.0.1
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTest {
    private static final Logger logger = LoggerFactory.getLogger(MainTest.class);

    @Autowired
    BRestClientConfig tokenConfig;

    @Autowired
    CTRestClientConfig ctRestClientConfig;

    @Test
    public void test() {

//        ctRestClientConfig.requestWithSign("s", "json", "no");
//        System.exit(0);

        logger.info("{}", EncryptionUtils.md5("{\"tenantId\":\"3000000006346\",\"userAccount\":\"admin_3000000064534\"}"));

        R r = tokenConfig.requestWithSign("baiwang.invoice.upload", "{\"requestId\":\"9b07e840-e984-44fc-9a9b-e5debec92afe\",\"invoiceUploadType\":\"3\",\"machineNo\":\"499098936901\",\"autoOpen\":\"\",\"pushChannelFlag\":\"\",\"invoiceCode\":\"043001600511\",\"organizationName\":\"\",\"invoiceNo\":\"84597967\",\"invoiceDate\":\"2018-08-22 10:00:00\",\"invoiceClosingDate\":\"2018-08-22 10:00:00\",\"returnType\":\"\",\"taxControlCode\":\"03*6+808+<28/75077++87<28*98769*7>+380685*6+2+3+847+599+6/96*256+<8<>440517059003<<79>860+2/1<016/46195>4-+0>282\",\"invoiceCheckCode\":\"10637211788999063509\",\"invoiceQrCode\":\"\",\"sellerTaxNo\":\"91430102553032598N\",\"sellerName\":\"湖南万家丽集团国际购物广场有限公司\",\"sellerAddressPhone\":\"长沙市芙蓉区火星路68号第1栋（万家丽路一段99号）0731-85909688\",\"sellerBankAccount\":\"\",\"deviceType\":\"0\",\"organizationCode\":\"\",\"serialNo\":\"1062000520322\",\"invoiceTypeCode\":\"026\",\"invoiceTerminalCode\":\"wjl026\",\"buyerTaxNo\":\"\",\"buyerName\":\"浏阳金科置业有限公司\",\"buyerAddressPhone\":\"0731-82716655\",\"buyerBankAccount\":\"1901026809025993868\",\"drawer\":\"王璐\",\"checker\":\"王璐\",\"payee\":\"王璐\",\"invoiceType\":\"0\",\"invoiceListMark\":\"0\",\"redInfoNo\":\"\",\"originalInvoiceCode\":\"\",\"originalInvoiceNo\":\"\",\"taxationMode\":\"0\",\"deductibleAmount\":\"\",\"invoiceTotalPrice\":\"862.07\",\"invoiceTotalTax\":\"137.93\",\"invoiceTotalPriceTax\":\"1000.00\",\"signatureParameter\":\"0000004282000000\",\"taxDiskNo\":\"\",\"taxDiskKey\":\"\",\"taxDiskPassword\":\"\",\"goodsCodeVersion\":\"\",\"consolidatedTaxRate\":\"\",\"notificationNo\":\"\",\"remarks\":\"\",\"invoiceDetailsList\":[{\"goodsLineNo\":\"1\",\"goodsLineNature\":\"0\",\"goodsCode\":\"1030308010000000000\",\"goodsExtendCode\":\"\",\"goodsName\":\"*茶*小罐茶品鉴装\",\"goodsTaxItem\":\"\",\"goodsSpecification\":\"\",\"goodsUnit\":\"\",\"goodsQuantity\":\"10.0\",\"goodsPrice\":\"86.206897\",\"goodsTotalPrice\":\"862.07\",\"goodsTotalTax\":\"137.93\",\"goodsTaxRate\":\"0.16\",\"goodsDiscountLineNo\":\"\",\"priceTaxMark\":\"0\",\"vatSpecialManagement\":\"\",\"freeTaxMark\":\"\",\"preferentialMark\":\"0\"}],\"apiName\":\"baiwang.invoice.upload\",\"taxNo\":\"91430102553032598N\",\"methodCode\":\"1009\"}", "json");
        System.out.println(JSONObject.toJSONString(r));

//
//        RestTemplate restTemplate = new RestTemplate();
//        logger.info(EncryptionUtils.md5AndSha("a123456" + "c1d06b9cea934decb4ed8e01f0f2cd8f"));
//        String res = restTemplate.getForObject("https://www.baidu.com", String.class);
//        logger.info(res);
    }
}
