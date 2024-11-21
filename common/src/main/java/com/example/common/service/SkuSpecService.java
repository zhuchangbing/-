package com.example.common.service;

import com.example.common.domain.entity.GoodsSpec;
import com.example.common.domain.entity.Sku;
import com.example.common.domain.entity.SkuSpec;

import java.util.List;

public interface SkuSpecService {
    List<SkuSpec> getSkuSpecList(Integer id);

    int insertSkuSpec(List<SkuSpec> skuSpec, Integer skuId); //插入的属性

    // 查找某一品牌的所有属性

    List<GoodsSpec> getSkuSpecListByGoodsId(Integer goodsId);

    // 更新商品的属性
    int updateSkuSpec(SkuSpec skuSpec) throws RuntimeException;
}
