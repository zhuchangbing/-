package com.example.skuservice.es;

import com.example.common.service.SkuService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务 完成对es数据的初始化
 */
@Configuration
@EnableScheduling // 开启定时任务
@Slf4j
public class ESInitTask {
    @Autowired
    private SkuService skuService;

    @PostConstruct  // 在构造该类的对象后，调用该方法
    public void init() throws Exception {
        System.err.println("ES数据初始化");
        skuService.initESData();
    }

}
