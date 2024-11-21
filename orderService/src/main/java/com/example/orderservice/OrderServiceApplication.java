package com.example.orderservice;

import com.example.common.utils.SnowflakeUtil;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.example.orderservice",
        "com.example.common.security",
        "com.example.common.filter"
})
@EnableDubbo
public class OrderServiceApplication {
    @Value("${snow.workId}")
    private Long workId;

    @Value("${snow.datacenter}")
    private Long datecenterId;

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    // 生成一个雪花的对象
    @Bean
    public SnowflakeUtil snowflakeUtil() {
        return new SnowflakeUtil(workId, datecenterId);
    }
}














