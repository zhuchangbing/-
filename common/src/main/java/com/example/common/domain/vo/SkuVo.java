package com.example.common.domain.vo;

import com.example.common.domain.entity.GoodsSpec;
import com.example.common.domain.entity.Pic;
import com.example.common.domain.entity.Sku;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SkuVo extends Sku {
    private String search;
    private List<Pic> pics;
    private List<GoodsSpec> goodsSpecs;
    private Integer category1Id;
    private Integer category2Id;
    private Integer category3Id;
}
