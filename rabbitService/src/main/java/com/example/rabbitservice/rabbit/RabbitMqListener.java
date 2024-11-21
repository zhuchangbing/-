package com.example.rabbitservice.rabbit;

import com.example.common.domain.entity.Order;
import com.example.common.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMqListener {
    @DubboReference
    private OrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "dl.queue", durable = "true"),
            exchange = @Exchange(name = "dl.direct"),
            key = "dl"
    ))
    public void listenDlQueue(String id) {
        try {
            System.out.println("接受到消息");
            log.info("消费者接收到了dl.queue的消息：" + id);

            Order order = orderService.getOrder(id);
            if (order != null && order.getStatus() == 0) {
                order.setStatus(7);
                orderService.updateOrder(order);
                System.out.println("订单删除成功！！！！！！！！！！！！");
            } else {
                log.warn("未找到订单");
            }
        } catch (Exception e) {
            log.error("处理消息失败，订单ID: {}，错误: {}", id, e.getMessage(), e);
        }
    }

}
