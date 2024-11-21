package com.example.orderservice.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.common.domain.entity.Order;
import com.example.common.service.OrderService;
import com.example.common.service.SkuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.DelegatingApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝的支付接口
 * 1. 用户使用支付宝的接口
 * 2. 支付宝支付成功的回调
 * 3. 支付过程完成的接口
 */

@RestController
@RequestMapping("/alipay")
@RequiredArgsConstructor
public class AliPayController {
    // 用户使用支付宝支付  订单是哪一个  金额

    private final OrderService orderService;
    private static final String URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String APPID = "9021000140696341";
    // 私钥
    private static final String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDa5qP5f8isqI04qE6347lrsuJSCGb3bir0QOQ08oMfD54DQT3WhTqAQnCxK6o4TTByCWv5VOt3pkzW+JCsVd+r/TwDfqlicEyHuYVT3+zEtPQCGwpZ5JR/1SiGcLOBKSECKy5JEjsJbw9rYzNQfbD4YKRizSbCRqeqMuc51OjvO+KRTaydRi3w/JRHI+9MXqNGYWmU5uqKNEurrSwfqEsLuldA285WrIQ8tG6uXJLjuVgkcA6KaL900dMHCJKg3l2xlUtbfW3nZ7rCuJRr+7RfZP+EFoSVDRhV/5rO+LlS3fZ9gCduf4lN8dvf0kzs12E4sd0A5d5bZlyBMuEQcZVrAgMBAAECggEBAMTiBv/EJ/jrTMF11/9bSg2NAjQoLHxFUUsJBnh82uRYBpYE2xqUD88GcxdXH62t93HJ+Zxy/apLH/eF75GhVyD5Dj5ScQUB3kOA4GNVFz7BRmqSaZf6mHn1fZKV0U3NcUvjDKlaRVEx29uJiMDPnBl5ysC2pjK8m9bgG8qPLcblGvoUVsg4B8rS2Pg5ulz+ACa77fh3WUkwxW0QFnq4ATMvteKHS5w+WfRlzyhjBYosCJkYXXUj0gCZ9J/sCA0tLdQ0CZLce9C3OslFfqxJ/OO4p2sm0222aFpSNtnPeGRpUvN/OqfcixGIKFEwuXUxuMy+4qW1ID83SD8FxOJA40kCgYEA8JWvmgdkdNpk6AV/M/AuQixbl+oZ071B5zwnWiIQOmtnaDbyf/0GZkIvH0lA/z+KBkmC6z5tLViNxaV921bShuu8LTXUVZ4ZoL5tRor0Qu1gc97d5Vl1vRHozTS/Cv33jsAZnxhfaaevA3Kt0K5ozZXbtL8cWFi0pLtbQRIimHcCgYEA6O1GgTD+cYQGgOvcefob1PCtPWT1notFch8KL1GgDnVQt+7sR8o1GPfDkcXjFlS9V5vwgpA4HcfNf+K1nvMymJ0cbm7OPOWxcAdBQBYkOCPtfNu0FUu2mZks36khE6sJXpFxpDE2k9EI3vYJ/o6Qfu0tdqi+fO+68MGke1nkG60CgYEAqfwJC5qHhuy7+RfRLYBZHtY6jRaR0wJYqyncYBnLP4udj4urQXUkbWH7624Vp4NcYV7VSqNb/nsgCHMKfsxUiZHL7mGnOisvzlxd10bcmOEhkhz4yVCxGLqrszm+deTFYi4mrDtLRtp8lMIVP2nLkN5+UrHpNB7Q28epDUvj97cCgYBWCc9RSVezdqEwXe9C3zAIqB5V30UbGza7wNtyPyRwMsVuK8L7gaW7Y0nKPt5zETofcmqUz9DqrzMpWgTcgdjepqop4KQtumqghw2DLqqSUjMXO9B5TU5Vzk7VDxQ4v6TmWbMwgz01vLEaKqfmb6LQP6vbeNlkLOlQpTasmbe4wQKBgAZJ0RgAt9V8UPESqaecDcqZvMnEcfz6YhTnYrXJ2WApvpQgRC9Xq+AKwiO/nzhCbH0LDr22GCosCHTrmalHMZ5t+3gj/djMCNXXKOhizBXe291jIPfc9pAQWd+f7RlHy21Bfvj8MTCinvjYLvaIrqJNosDuqISsDI4XAVY+56dx";
    private static final String FORMAT = "json";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";
    // 支付宝公钥
    private static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhlLhHv69lJoR2l9lW9kidenN5dvfouf2OWAleCjpmwdiyzThAgxeGqVQ+oJ9As8nVeApmcw4duAHeUks+tqIqSiVClHYc0k7I/p+/uNaYUkw9NbaipV0IswcgeboHuz8cmG+MPTM/PJJhOxGlF2JeRxO/BJgt2OgEnHBH/FdLIy8Cy2oU+LlwdLb+HTGFQTEFCV8CdXbEymUO4ipAZGCeC8UpuMLM8P6vL7t9fsSpDlj9NoZUCG5uWz6O+VpJqouOJ2EST0RWFXXcc19/yv8F8i5ctglOwfMWqiIdS9x2/lpT3L+sdzr3M9nIG5q1k1be/o2WH+tLJfiz23oIM1WJwIDAQAB";

    // 需要内网穿透
    private static final String NOTIFY_URL = "http://ey59ix.natappfree.cc/alipay/notify";
    private static final String RETURN_URL = "http://127.0.0.1:5173/center/orders";
    private final SkuService skuService;

    @GetMapping("/pay")
    public void pay(String orderId, Authentication authentication,
                    HttpServletResponse httpServletResponse) throws Exception {
        // 验证 略

        // 计算订单金额

        // 创建一个配置对象
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(URL);
        alipayConfig.setCharset(CHARSET);
        alipayConfig.setSignType(SIGN_TYPE);
        alipayConfig.setAppId(APPID);
        alipayConfig.setPrivateKey(PRIVATE_KEY);
        alipayConfig.setAlipayPublicKey(ALIPAY_PUBLIC_KEY);
        alipayConfig.setFormat(FORMAT);

        // 实例化一个Ali的客户端
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);

        // 设置回调地址
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(RETURN_URL);
        request.setNotifyUrl(NOTIFY_URL);
        // 支付信息
        AlipayTradePagePayModel model = orderService.createAlipayMode(orderId);
        // 支付确认支付唯一的标志
        request.setBizModel(model);
        // 请求支付宝的服务器
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "POST");
        if (response.isSuccess()) {
            System.err.println("请求成功");

        } else {
            System.err.println("请求支付页面失败");
        }
        String html = response.getBody();
        httpServletResponse.setContentType("text/html;charset=" + CHARSET);
        httpServletResponse.getWriter().write(html);
    }


    @PostMapping("/notify")
    public String payNotify(HttpServletRequest request) throws Exception {
        String status = request.getParameter("trade_status");
        Map<String, String[]> params;
        if ("TRADE_SUCCESS".equals(status)) {
            params = request.getParameterMap();
            String sign = request.getParameter("sign");
            // 支付宝签名校验 确实是来自支付宝的服务器 防止黑客伪装支付宝服务器 回调我们的成功的接口
            Map<String, String> checkParams = new HashMap<String, String>();
            for (String key : params.keySet()) {
                checkParams.put(key, request.getParameter(key));
            }
            String content =
                    AlipaySignature.getSignCheckContentV1(checkParams);
            boolean success = AlipaySignature.rsa256CheckContent(content, sign, ALIPAY_PUBLIC_KEY, CHARSET);
            if (success) {
                String subject = request.getParameter("subject");
                String outTradeNo = request.getParameter("out_trade_no");
                String gmtPayment = request.getParameter("gmt_payment");
                String totalAmount = request.getParameter("total_amount");
                String tradeNo = request.getParameter("trade_no");
                Order order = orderService.getOrder(outTradeNo);
                orderService.updateOrderStatus(outTradeNo);
                skuService.updateSkuStatus(order.getSkuId());
            }

        }
        return "fhsdiof";
    }
}










