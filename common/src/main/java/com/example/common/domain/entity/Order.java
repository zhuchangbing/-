package com.example.common.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Order implements Serializable {
    private String id;
    private Integer userId;
    private Integer status;       //0 表示支付完毕等待发货 、 1运送中订单号  、 2已送达   、 3已签收 '、4 用户删除软删除
    //private String orderNumber;   // 订单号
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Integer skuId;
    private Sku sku;
    private Integer puserId;

    private String userDetail;
    private String trackingNumber;
    List<OrderDetail> orderDetails;
}
