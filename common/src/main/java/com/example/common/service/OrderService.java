package com.example.common.service;

import com.alipay.api.domain.AlipayTradePagePayModel;
import com.example.common.domain.entity.Order;
import com.example.common.domain.entity.User;
import com.example.common.domain.vo.OrderVo;

import java.util.List;


public interface OrderService {
    //增
    /*public int addOrder(Order order);*/
    //改
    /*public int updateOrder(Order order) throws Exception;*/
    //查
    /*public PageInfo getUserOrder(Integer page,Integer size,Integer userId);*/

    // 插入新的订单
    public String addOrder(User user, OrderVo orderVo) throws Exception;
    //public String addOrder(OrderVo orderVo, Integer userId);

    // 查询订单详情
    Order getOrder(String id);

    // 查询用户所有的订单
    List<Order> getUserAllOrders(Integer userId);


    // 支付订单
    int PayOrder(OrderVo order) throws Exception;

    int updateOrder(Order order) throws Exception;

    // 查询发布者视角的订单
    List<Order> getPUserOrders(Integer puserId);

    int updateOrderStatus(String orderId) throws Exception;

    AlipayTradePagePayModel createAlipayMode(String orderId) throws Exception;

    Order getUserSkuOrder(Integer userId, Integer skuId);

    // 商家添加订单号 更新订单状态
    int UpdateOrderTrackingNumber(Order order);
}
