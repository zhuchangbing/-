package com.example.orderservice.mapper;

import com.example.common.domain.entity.OrderDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    @Insert({
            "insert into tb_order_detail(sku_id, order_id)",
            "values(#{skuId},#{orderId})"
    })
    int insertOrderDetail(OrderDetail orderDetail);


    // 查询订单中的所有 OrderDetail
    @Select({
            "select * from tb_order_detail",
            "where order_id = #{orderId}"
    })
    List<OrderDetail> selectOrderDetailByOrderId(String orderId);
}
