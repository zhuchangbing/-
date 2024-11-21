package com.example.skuspecservice.mapper;

import com.example.common.domain.entity.GoodsSpec;
import com.example.common.domain.entity.Option;
import com.example.common.domain.entity.SkuSpec;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SkuSpecMapper {
    // 关联查询出来商品的所有属性以及值
    @Select({
            "select * from tb_sku_specification",
            "where sku_id = #{skuId}"
    })
    @Results({
            @Result(column = "spec_id", property = "specId"),
            @Result(column = "option_id", property = "optionId"),
            @Result(column = "spec_id", property = "specName",
                    one = @One(select = "com.example.skuspecservice.mapper.SpecMapper.selectSpecNameById")),
            @Result(column = "option_id", property = "optionValue",
                    one = @One(select = "com.example.skuspecservice.mapper.OptionMapper.selectSkuValue"))
    })
    public List<SkuSpec> selectSkuSpecList(Integer skuId);


    // 插入属性
    @Insert({
            "<script>",
            "insert into tb_sku_specification(sku_id,spec_id,option_id)",
            "values",
            "<foreach collection = 'skuSpecs' item = 'skuSpec' separator = ','>",
            "(#{skuId},#{skuSpec.specId},#{skuSpec.optionId})",
            "</foreach>",
            "</script>"
    })
    public int insertSkuSpec(List<SkuSpec> skuSpecs, Integer skuId);


    @Select({
            "select * from tb_option",
            "where spec_id = #{specId}"
    })
    public List<Option> selectOptionList(Integer specId);

    // 查询某一品牌的所有属性以及对应的所有值
    @Select({
            "select * from tb_goods_specification",
            "where goods_id = #{goodsId}"
    })
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "optionsList",
                    many = @Many(select = "selectOptionList"))
    })
    public List<GoodsSpec> selectGoodsSpec(Integer goodsId);


    // 更新商品的属性
    @Update({
            "update tb_sku_specification",
            "set option_id = #{optionId}",
            "where id = #{id}"
    })
    int updateSkuAttrs(SkuSpec skuSpec);

    // 查询属性是否存在
    @Select({
            "select * from tb_sku_specification",
            "where id = #{id}"
    })
    SkuSpec selectSkuSpecById(Integer id);

}
