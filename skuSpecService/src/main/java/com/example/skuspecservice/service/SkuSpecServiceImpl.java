package com.example.skuspecservice.service;

import com.example.common.domain.entity.GoodsSpec;
import com.example.common.domain.entity.Sku;
import com.example.common.domain.entity.SkuSpec;
import com.example.common.service.SkuSpecService;
import com.example.skuspecservice.mapper.SkuSpecMapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@DubboService
@RequiredArgsConstructor
public class SkuSpecServiceImpl implements SkuSpecService {
    private final SkuSpecMapper skuSpecMapper;

    @Override
    public List<SkuSpec> getSkuSpecList(Integer id) {
        return skuSpecMapper.selectSkuSpecList(id);
    }

    @Override
    public int insertSkuSpec(List<SkuSpec> skuSpec, Integer skuId) {
        return skuSpecMapper.insertSkuSpec(skuSpec, skuId);
        //插入商品判断商品是否存在，此处无需判断了
    }

    @Override
    public List<GoodsSpec> getSkuSpecListByGoodsId(Integer goodsId) {
        return skuSpecMapper.selectGoodsSpec(goodsId);
    }

    @Override
    public int updateSkuSpec(SkuSpec skuSpec) throws RuntimeException {
        SkuSpec skuSpec1 = skuSpecMapper.selectSkuSpecById(skuSpec.getId());
        if (ObjectUtils.isEmpty(skuSpec1)) {
            throw new RuntimeException("修改的属性不存在，请检查");
        }
        return skuSpecMapper.updateSkuAttrs(skuSpec);
    }

}
