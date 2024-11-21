package com.example.common.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SkuSpec implements Serializable {
    private Integer id;
    private Integer skuId;
    private Integer specId;
    private Integer optionId;

    private String specName;       // 查询结果中的数据
    private String optionValue;
}
