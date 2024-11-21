package com.example.userservice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

// 使能提供微服务
@EnableDubbo
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.example.userservice",
        "com.example.common.security",
        "com.example.common.filter",
        "com.example.common.utils"
})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
