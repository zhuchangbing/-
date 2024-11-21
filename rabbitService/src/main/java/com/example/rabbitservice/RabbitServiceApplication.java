package com.example.rabbitservice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.example.rabbitservice.rabbit",
        "com.example.common.security",
        "com.example.common.filter"
})
@EnableDubbo
public class RabbitServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitServiceApplication.class, args);
    }

}
