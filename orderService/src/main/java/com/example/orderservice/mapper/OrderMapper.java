package com.example.orderservice.mapper;

import com.example.common.domain.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {
    //用户查看自己的订单
    @Select({
            "select * from tb_order",
            "where user_id = #{userId}"
    })
    List<Order> selectUserOrders(Integer userId);

    /*// 支付完毕后添加订单
    @Insert({
            "insert into tb_order(sku_id,user_id)",
            "values(#{skuId},#{userId})"
    })
    int insertOrder(Order order);*/

    @Update({
            "<script>",
            "update tb_order",
            "<set>",
            "<if test = 'status != null'>status = #{status},</if>",
            "</set>",
            "where id=#{id}",
            "</script>"
    })
    int updateOrder(Order order);//商家发货 更新状态 添加订单号，用户签收订单更新状态


    // 添加订单    没问题
    @Insert({
            "insert into tb_order(user_id,id,user_detail,puser_id,sku_id)",
            "values (#{userId},#{id},#{userDetail},#{puserId},#{skuId})"
    })
    int insertOrder(Order order);

    // 查询订单详情
    /*@Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "orderDetails",
     many = @Many(select = "com.example.orderservice.mapper.OrderDetailMapper.selectOrderDetailByOrderId"))
    })*/
    @Select({
            "select * from tb_order",
            "where id = #{id}"
    })
    Order selectOrderById(String id);


    // 查询一个用户的所有订单
    @Select({
            "select * from tb_order",
            "where user_id = #{userId}"
    })
    /*@Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "orderDetails",
    many = @Many(select = "com.example.orderservice.mapper.OrderDetailMapper.selectOrderDetailByOrderId"))
    })*/
    List<Order> selectUserAllOrders(Integer userId);


    // 更新订单状态
    @Update({
            "update tb_order",
            "set status = #{status}",
            "where id = #{id}"
    })
    int updateOrderStatus(Order order);


    // 查询发布者视角的订单
    @Select({
            "select * from tb_order",
            "where puser_id = #{puserId}"
    })
    List<Order> selectPUserOrdersByPUserId(Integer puserId);

    @Select({
            "select * from tb_order",
            "where user_id = #{userId} and sku_id = #{skuId}"
    })
    Order selectUserOrderById(Integer userId, Integer skuId);


    // 商家更新订单信息 添加快递单号
    @Update({
            "update tb_order set tracking_number = #{trackingNumber}",
            ", status = 2",
            "where id = #{id}"
    })
    int updateOrderTrackingNumber(Order order);
}
