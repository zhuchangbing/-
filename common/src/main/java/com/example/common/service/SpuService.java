package com.example.common.service;

import com.example.common.domain.entity.Spu;

import java.util.List;

public interface SpuService {
    List<Spu> get1Sku(Integer category1Id);

    List<Spu> get2Sku(Integer category2Id);

    List<Spu> get3Sku(Integer category3Id);

    List<Spu> getSkus();
}
