package com.example.emailservice.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class KafkaReceiver {
    @Autowired
    JavaMailSender javaMailSender;

    @KafkaListener(topics = "code")
    public void receive(ConsumerRecord<String, String> record) {
        String email = record.value();
        String verificationCode = record.key(); // 验证码
        System.out.println(email);
        System.out.println(verificationCode);
        if (email == null || email.isEmpty()) {
            System.err.println("收到的邮箱地址为空，无法发送邮件。");
            return; // 直接返回，不进行邮件发送
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("易购商城注册验证码");
        message.setText(verificationCode); // 验证码
        message.setFrom("2484383545@qq.com");
        message.setTo(email); // 往哪个邮箱发送

        try {
            javaMailSender.send(message);
            System.out.println("邮件已发送到: " + email);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("发送邮件失败: " + e.getMessage());
        }
    }

}
