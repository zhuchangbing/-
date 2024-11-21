package com.example.userservice.controller;

import com.example.common.domain.vo.Result;
import com.example.common.utils.RedisUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/vercode")
public class EmailVecodeController {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private RedisUtil redisUtil;

    public static String generateVerificationCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(random.nextInt(10)); // 生成0-9的随机数
        }
        return sb.toString();
    }

    @GetMapping("/send")
    public Result Vecode(String email, HttpSession session) {
        System.out.println(email);
        String key1 = "Vercode" + email;
        Duration duration = Duration.ofSeconds(60);
        if (!redisUtil.set(key1, 1, duration)) {
            return Result.error("请等待一分钟后重新发送");
        }
        String key = generateVerificationCode();
        CompletableFuture<SendResult<String, Object>> completable
                = kafkaTemplate.send("code", key, email);

        completable.whenCompleteAsync(
                (result, exception) -> {
                    if (!ObjectUtils.isEmpty(exception)) {
                        System.out.println("发送任务失败" + result);
                        exception.printStackTrace();
                    } else {
                        System.out.println(result);
                    }
                }
        );
        session.setAttribute("vercode", key);
        if (!redisUtil.set(email, key, duration)) {
            return Result.error("验证码发送失败请重试");
        }
        return Result.success("发送验证码成功");
    }


}
