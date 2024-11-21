package com.example.orderservice.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TTLMessageConfig {
    // 死信队列的交换机对象
    @Bean
    public DirectExchange ttlDirectExchange() {
        return new DirectExchange("ttl.direct");
    }

    //死信队列 绑定过期时间 以及 RoutingKey
    @Bean
    public Queue ttlQueue() {
        return QueueBuilder
                .durable("ttl.queue")  // 订单刚来时放入的队列
                .ttl(15 * 60 * 1000)         //
                .deadLetterExchange("dl.direct")   // 绑定死信交换机
                .deadLetterRoutingKey("dl")
                .build();
    }

    //
    @Bean
    public Binding ttlBinding() {
        return BindingBuilder.bind(ttlQueue())
                .to(ttlDirectExchange())
                .with("ttl");
    }
}














