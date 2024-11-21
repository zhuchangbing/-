package com.example.skuspecservice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDubbo
@ComponentScan(basePackages = {
        "com.example.skuspecservice",
        "com.example.common.security",
        "com.example.common.filter"
})
public class SkuSpecServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SkuSpecServiceApplication.class, args);
    }
}
