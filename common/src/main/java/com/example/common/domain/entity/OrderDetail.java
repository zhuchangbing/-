package com.example.common.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class OrderDetail implements Serializable {
    private Integer id;
    private String orderId;
    private Integer skuId;
    private Integer count;

    private Sku sku;    //关联查询
}
