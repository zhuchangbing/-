package com.example.orderservice.mapper;

import com.example.common.domain.entity.SaveSku;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CollectMapper {
    // 通过 id 查找 收藏的商品

    @Select({
            "select * from tb_user_save_sku",
            "where id = #{id}"
    })
    SaveSku selectById(Integer id);

    // 获取 用户收藏的商品  关联查询商品的信息
    @Select({
            "select * from tb_user_save_sku",
            "where user_id = #{userId}"
    })
    List<SaveSku> selectUserSaveSkus(Integer userId);


    // 用户添加收藏商品
    @Insert({
            "insert into tb_user_save_sku(user_id, sku_id)",
            "values (#{userId}, #{skuId})"
    })
    int insertUserSaveSku(SaveSku saveSku);


    // 获取用户是否收藏某件商品
    @Select({
            "select * from tb_user_save_sku",
            "where user_id = #{userId} and sku_id = #{skuId}"
    })
    SaveSku selectSaveSkuById(SaveSku saveSku);

    // 用户不再收藏某件商品
    @Delete({
            "delete from tb_user_save_sku",
            "where user_id = #{userId} and sku_id = #{skuId}"
    })
    int deleteUserSaveSkuById(SaveSku saveSku);


    // 批量删除
    @Delete({
            "<script>",
            "delete from tb_user_save_sku",
            "where id in ",
            "<foreach collection = 'ids' item = 'id' open = '(' separator = ',' close = ')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    int deleteBatch(List ids);


    // 根据用户 id 和商品 id 批量删除
    @Delete({
            "<script>",
            "delete from tb_user_save_sku",
            "where user_id = #{userId} and sku_id in ",
            "<foreach collection = 'ids' item = 'id' open = '(' separator = ',' close = ')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    int deleteByUserIdAndSkuIds(Integer userId, List ids);
}
