package com.example.userservice.controller;

import com.example.common.domain.vo.Result;
import com.example.common.utils.CaptchaUtil;
import com.example.common.utils.JWTUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    @GetMapping
    public Result captcha() {
        // 生成随机的验证码字符串
        String captcha = CaptchaUtil.randomCode(4);
        // 生成base64的图片
        String base64 = CaptchaUtil.getBase64(CaptchaUtil.createImage(100, 32, captcha));
        // 返回 Base64图片 + Token
        // 将字符串加密生成token
        String token = JWTUtil.createJWT(captcha);
        return Result.success(200, token, base64);
    }
}












