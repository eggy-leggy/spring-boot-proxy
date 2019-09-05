package com.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Frank F
 * @description: 主函数
 * @create 2019-09-03 17:48
 */

@RestController
@SpringBootApplication
public class Application {

    @RequestMapping(value = "/hello")
    public String hello() {
        return "hello world";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
