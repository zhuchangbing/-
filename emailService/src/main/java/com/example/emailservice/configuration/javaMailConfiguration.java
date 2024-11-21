package com.example.emailservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class javaMailConfiguration {
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.qq.com");  // QQ 邮箱 SMTP 服务器
        mailSender.setPort(465);  // 使用 465 端口，并启用 SSL
        mailSender.setUsername("2484383545@qq.com");
        mailSender.setPassword("fympjidxwukleade");  // 这里填写你的授权码而不是密码

        // 配置 SSL
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");  // 启用 STARTTLS
        props.put("mail.smtp.ssl.enable", "true");  // 启用 SSL

        return mailSender;
    }

}
