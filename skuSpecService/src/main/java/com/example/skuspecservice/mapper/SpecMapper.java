package com.example.skuspecservice.mapper;

import com.example.common.domain.entity.SkuSpec;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SpecMapper {
    @Select({
            "select name from tb_goods_specification where id = #{id}"
    })
    public String selectSpecNameById(Integer id);

}
