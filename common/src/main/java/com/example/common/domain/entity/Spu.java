package com.example.common.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class Spu implements Serializable {
    private Integer id;
    private String name;
    private String brand;
    private Integer category1Id;
    private Integer category2Id;
    private Integer category3Id;

    private String category1Name;
    private String category2Name;
    private String category3Name;
    private List<Sku> skuList;    // 该一级分类的商品


    private Integer recom;    //  0表示不推荐 1表示推荐
    private Integer status;   //  0表示上架   1表示不上架

}
