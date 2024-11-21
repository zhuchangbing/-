package com.example.skuspecservice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OptionMapper {
    @Select({
            "select value from tb_option where id = #{id}"
    })
    public String selectSkuValue(Integer id);
}
