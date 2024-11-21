package com.example.common.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaveSku {
    Integer id;
    Integer userId;
    Integer skuId;

    User user;            // 此订单的用户
    Sku sku;              // 一个订单一个商品
}
