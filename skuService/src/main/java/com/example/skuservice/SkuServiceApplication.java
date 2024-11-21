package com.example.skuservice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableDubbo
@ComponentScan(basePackages = {
        "com.example.skuservice",
        "com.example.common.security",
        "com.example.common.filter",
        "com.example.common.utils"
})
public class SkuServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkuServiceApplication.class, args);
    }

}
