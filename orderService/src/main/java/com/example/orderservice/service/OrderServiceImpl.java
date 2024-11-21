package com.example.orderservice.service;

import com.alipay.api.domain.AlipayTradePagePayModel;
import com.example.common.domain.entity.Order;
import com.example.common.domain.entity.Sku;
import com.example.common.domain.entity.User;
import com.example.common.domain.vo.OrderVo;
import com.example.common.service.OrderService;
import com.example.common.service.SkuService;
import com.example.common.service.UserService;
import com.example.common.utils.RedisUtil;
import com.example.common.utils.SnowflakeUtil;
import com.example.orderservice.mapper.OrderMapper;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@RequiredArgsConstructor
@DubboService
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final SnowflakeUtil snowflakeUtil;

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @DubboReference
    private SkuService skuService;
    @DubboReference
    private UserService userService;

   /* @Override
    public int addOrder(Order order) {
        return orderMapper.insertOrder(order);
    }*/

    /*@Override
    public int updateOrder(Order order) throws Exception{  //查一下该商品是不是被出售了
        Sku ss = skuService.getSku(order.getSkuId());
        if(ss.getIsSold().equals(1)) {
            throw new Exception("该商品已被出售，下次早点来吧亲");
        }
        return orderMapper.updateOrder(order);
    }*/

    // 生成订单
    /*@Override
    @GlobalTransactional
    public String addOrder(OrderVo orderVo,  Integer userId) {
        Order order = orderVo;
        // 雪花算法 计算 订单的Id
        order.setId(snowflakeUtil.nextId().toString());
        order.setUserDetail(orderVo.getDetailUserInfo());
        OrderDetail orderDetail = new OrderDetail();
        orderMapper.insertOrder(order);   // 添加tb_order
        List ids = new ArrayList();
        orderVo.getSkus().forEach(sku -> {
            ids.add(sku.getId());
            orderDetail.setOrderId(order.getId());
            orderDetail.setSkuId(sku.getId());
            orderDetailMapper.insertOrderDetail(orderDetail);   // 添加 tb_order_detail
        });

        // 删除 收藏项
        collectMapper.deleteByUserIdAndSkuIds(userId,ids);
        return order.getId();
    }*/


    /* 添加订单 */
    @Override
    @GlobalTransactional
    public String addOrder(User user, OrderVo orderVo) throws Exception {
        Order sorder = orderMapper.selectUserOrderById(user.getId(), orderVo.getSkuId());
        if (!ObjectUtils.isEmpty(sorder)) {
            throw new Exception("你已经将此商品下单了！！！");
        }
        Order order = orderVo;
        // 雪花算法 计算 订单的Id
        order.setId(snowflakeUtil.nextId().toString());
        order.setUserDetail(orderVo.getDetailUserInfo());
        orderMapper.insertOrder(order);
        System.out.println("-----------------------------------------");

        Message message = MessageBuilder
                .withBody(order.getId().getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();
        rabbitTemplate.convertAndSend("ttl.direct", "ttl", message);

        log.info("订单加入队列");
        System.out.println("-----------------------------------------");
        return order.getId();
    }


    // id 查询订单
    @Override
    public Order getOrder(String id) {
        Order order = orderMapper.selectOrderById(id);
        System.out.println(order);
        /*order.getOrderDetails().forEach(orderDetail -> {
            orderDetail.setSku(skuService.getSku(orderDetail.getSkuId()));
        });*/
        order.setSku(skuService.getSku(order.getSkuId()));
        return order;
    }


    // 查询用户所有的订单
    @Override
    public List<Order> getUserAllOrders(Integer userId) {
        List<Order> orders = orderMapper.selectUserAllOrders(userId);
        orders.forEach(order -> {
            order.setSku(skuService.getSku(order.getSkuId()));
        });
        /*orders.forEach(order -> {
            order.getOrderDetails().forEach(orderDetail -> {
                orderDetail.setSku(skuService.getSku(orderDetail.getSkuId()));
            });
        });*/
        return orders;
    }

    //
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public int PayOrder(OrderVo order) throws Exception {
        User user = userService.getUserById(order.getUserId());
        if (!passwordEncoder.matches(order.getPayPassword(), user.getPayPassword())) {
            throw new Exception("支付密码不正确");
        }
        Sku sku = skuService.getSku1(order.getSkuId());

        /*****      Redisson     *****/
        RLock lock = redissonClient.getLock("lock::" + order.getSkuId());
        lock.lock();
        try {
            if (sku.getIsSold() == 1) {
                throw new Exception("该商品已经售出，您来晚啦！");
            }
            orderMapper.updateOrderStatus(order);
            skuService.updateSkuStatus(order.getSkuId());
            user.setAccount(user.getAccount().subtract(order.getSku().getPrice()));
            if (user.getAccount().compareTo(BigDecimal.ZERO) == -1) {
                throw new Exception("当前账户余额不足，请先充值账户");
            }
            userService.updateUserInfo(user);
        } finally {
            lock.unlock();
        }
        return 0;
    }

    /* 更新订单号 状态 */
    @Override
    public int updateOrder(Order order) throws Exception {
        Order sorder = orderMapper.selectOrderById(order.getId());
        if (ObjectUtils.isEmpty(sorder)) {
            throw new Exception("订单不存在，请检查或刷新！");
        }
        return orderMapper.updateOrder(order);
    }

    @Override
    public List<Order> getPUserOrders(Integer puserId) {
        List<Order> orders = orderMapper.selectPUserOrdersByPUserId(puserId);
        orders.forEach(order -> {
            order.setSku(skuService.getSku(order.getSkuId()));
        });
        return orders;
    }

    @Override
    public int updateOrderStatus(String orderId) throws Exception {
        Order order = orderMapper.selectOrderById(orderId);
        if (order.getStatus() != 0) throw new Exception("订单状态有误，请检查订单状态");
        order.setStatus(1);
        return orderMapper.updateOrderStatus(order);
    }


    /*@Override
    public PageInfo getUserOrder(Integer page,Integer size,Integer userId) {
        PageHelper.startPage(page,size);
        List<Order> orderslist = orderMapper.selectUserOrders(userId);
        for(Order o : orderslist) {
            o.setSku(skuService.getSku(o.getSkuId()));
        }
        return new PageInfo(orderslist);
    }*/
    //支付宝支付
    @Override
    @Transactional
    public AlipayTradePagePayModel createAlipayMode(String orderId) throws Exception {
        Order order = orderMapper.selectOrderById(orderId);
        Sku sku = skuService.getSku1(order.getSkuId());
        if (sku.getIsSold() == 1) {
            throw new Exception("该商品已经售出了");
        }
        order.setSku(skuService.getSku(order.getSkuId()));
        if (ObjectUtils.isEmpty(order)) throw new Exception("订单不存在");
        if (order.getStatus() != 0) throw new Exception("订单状态异常");
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(orderId);
        int count = 1;
        BigDecimal bigDecimal = order.getSku().getPrice();
        String subject = "您购买了" + count + "件商品，合计：￥" + bigDecimal + "元！";
        // 设置支付金额
        model.setTotalAmount(bigDecimal.toString());
        // 设置支付信息  从订单上生成
        model.setSubject(subject);
        // 固定的支付的模式
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        //  更新数据库
        return model;
    }

    @Override
    public Order getUserSkuOrder(Integer userId, Integer skuId) {
        return null;
    }

    @Override
    public int UpdateOrderTrackingNumber(Order order) {
        return orderMapper.updateOrderTrackingNumber(order);
    }
}
