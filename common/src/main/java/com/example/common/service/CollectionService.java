package com.example.common.service;

import com.example.common.domain.entity.SaveSku;

import java.util.List;

public interface CollectionService {
    // 查询用户的所有收藏
    List<SaveSku> getUserSaveSkus(Integer userId);

    // 添加收藏
    int addUserSaveSku(SaveSku saveSku);

    // 获取用户是否收藏某件商品
    SaveSku getUserSaveSkuById(SaveSku saveSku);

    // 删除某个收藏的商品
    int removeUserSaveSkuById(SaveSku saveSku) throws Exception;

    int removeBatch(List ids, Integer userId) throws Exception;
}
