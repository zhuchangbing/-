package com.example.orderservice.controller;

import com.example.common.domain.entity.Order;
import com.example.common.domain.entity.User;
import com.example.common.domain.vo.OrderVo;
import com.example.common.domain.vo.Result;
import com.example.common.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public Result addOrder(@RequestBody OrderVo orderVo, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        orderVo.setUserId(user.getId());
        try {
            return Result.success(orderService.addOrder(user, orderVo));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @GetMapping
    public Result getOrderById(String id) {
        return Result.success(orderService.getOrder(id));
    }

    @GetMapping("/getorders")
    public Result getOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return Result.success(orderService.getUserAllOrders(user.getId()));
    }

    @PutMapping("/pay")
    public Result payOrder(@RequestBody OrderVo order, Authentication authentication) {
        try {
            return Result.success(orderService.PayOrder(order));
        } catch (Exception e) {
            e.printStackTrace();
            //log.error(e.getMessage());
            return Result.error(e.getCause().getMessage());
        }
    }


    @GetMapping("/getporders")
    public Result getPOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return Result.success(orderService.getPUserOrders(user.getId()));
    }

    @PutMapping("/updateOrderTrackingNumber")
    public Result updateOrderTrackingNumber(@RequestBody Order order) {
        return Result.success(orderService.UpdateOrderTrackingNumber(order));
    }


}
