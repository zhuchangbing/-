package com.example.orderservice.service;

import com.example.common.domain.entity.SaveSku;
import com.example.common.service.CollectionService;
import com.example.common.service.SkuService;
import com.example.orderservice.mapper.CollectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectMapper collectMapper;

    @DubboReference
    private SkuService skuService;


    @Override
    public List<SaveSku> getUserSaveSkus(Integer userId) {
        List<SaveSku> skus = collectMapper.selectUserSaveSkus(userId);
        skus.stream().forEach(sku -> {
            sku.setSku(skuService.getSku(sku.getSkuId()));
        });
        return skus;
    }

    /**
     * 添加用户收藏的商品
     *
     * @param saveSku
     * @return
     */
    @Override
    public int addUserSaveSku(SaveSku saveSku) {
        return collectMapper.insertUserSaveSku(saveSku);
    }

    @Override
    public SaveSku getUserSaveSkuById(SaveSku saveSku) {
        return collectMapper.selectSaveSkuById(saveSku);
    }

    @Override
    public int removeUserSaveSkuById(SaveSku saveSku) throws Exception {
        SaveSku sku = collectMapper.selectSaveSkuById(saveSku);
        if (ObjectUtils.isEmpty(sku)) {
            throw new Exception("该删除收藏商品不存在");
        }
        return collectMapper.deleteUserSaveSkuById(saveSku);
    }

    @Override
    public int removeBatch(List ids, Integer userId) throws Exception {
        ids.forEach(id -> {
            SaveSku saveSku = collectMapper.selectById((Integer) id);
            if (ObjectUtils.isEmpty(saveSku)) {
                throw new RuntimeException("无效的收藏商品信息");
            }
        });
        return collectMapper.deleteBatch(ids);
    }

}
