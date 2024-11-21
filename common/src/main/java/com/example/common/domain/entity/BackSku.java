package com.example.common.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BackSku {
    private Integer id;
    private Integer backRe;
    private Integer skuId;
    private String detailRe;
}
