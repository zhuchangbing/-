package com.example.skuservice.service;

import com.example.common.domain.entity.Spu;
import com.example.common.service.SpuService;
import com.example.skuservice.mapper.SkuMapper;
import com.example.skuservice.mapper.SpuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpuServiceImpl implements SpuService {
    private final SpuMapper spuMapper;
    private final SkuMapper skuMapper;

    @Override
    public List<Spu> get1Sku(Integer category1Id) {
        return spuMapper.selectOneSpus(category1Id);
    }

    @Override
    public List<Spu> get2Sku(Integer category2Id) {
        return spuMapper.selectTwoSpus(category2Id);
    }

    @Override
    public List<Spu> get3Sku(Integer category3Id) {
        return spuMapper.selectThreeSpus(category3Id);
    }

    @Override
    public List<Spu> getSkus() {
        return spuMapper.selectSpus();
    }
}
