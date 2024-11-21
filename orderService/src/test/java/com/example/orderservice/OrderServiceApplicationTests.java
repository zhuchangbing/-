package com.example.orderservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;

@Slf4j
@SpringBootTest
public class OrderServiceApplicationTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testTTLMessage() {
        Message message = MessageBuilder
                .withBody("Hello, ttl message".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();
        rabbitTemplate.convertAndSend("ttl.direct", "ttl", message);
        log.info("消息已经成功发送！");
    }


}
