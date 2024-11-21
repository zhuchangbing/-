package com.example.common.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Data
@NoArgsConstructor
public class GoodsSpec {      //商品的特征属性 数组
    private Integer id;       //spec_id
    private Integer goodsId;   //一类商品的id 此表展示一类商品的   **属性**
    private String name;

    private List<Option> optionsList;// 此属性对应的可选属性列表，根据id-spec_id查询
    private Integer optionId;         // 商家确定选择的属性


}
