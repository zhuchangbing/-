package com.example.common.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
// es的注解
@Document(indexName = Sku.ES_INDEX)
// 分片和副本
@Setting(shards = 2, replicas = 0)
// Json序列化和反序列化的时候， 属性为空忽略
@JsonIgnoreProperties(ignoreUnknown = true)

public class Sku implements Serializable {
    public static final String ES_INDEX = "mail-sku";

    @Id // 不是es的注解， spring-data的注解 用id作为主键 不随机生成 使用id作为主键
    @Field(
            type = FieldType.Keyword,// 设置字段的类型
            index = true // es中需要为该字段创建索引，能不能进行查询

    )
    private Integer id;
    @Field(
            type = FieldType.Text,
            analyzer = "ik_max_word" // 配置ik分词器
    )
    private String name;
    private Integer goodsId;      //具有哪些属性
    private BigDecimal price;
    private Integer wants;
    private Integer isFree;  // 是否包邮
    private Integer freshLevel;   //商品质量  0全新  1 几乎全新  2轻微磨损  3有明显使用痕迹
    @Field(
            type = FieldType.Text,
            analyzer = "ik_max_word" // 配置ik分词器
    )
    private String description;
    private Integer isSold;   // 0表示未卖出  1表示卖出了
    private Integer categoryId;  // 三级分类的id
    @Field(
            type = FieldType.Text,
            analyzer = "ik_max_word" // 配置ik分词器
    )
    private String categoryName;
    private BigDecimal score;
    private List<SkuSpec> specList;   //商品的属性列表
    private List<Pic> picList;        //商品的图片属性
    private Integer isApproved;       //商品是否审核通过
    private Integer isForced;         //商品是否被强制下架

    private Integer puserId;     // 发布者的 ID
    private User puser;          // 发布者的信息
}
