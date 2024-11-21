package com.example.skuservice.mapper;

import com.example.common.domain.entity.Category;
import com.example.common.domain.entity.Spu;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SpuMapper {
    // 查询一级分类的名字
    @Select({
            "select name from tb_category where id = #{id}"
    })
    String selectCategoryName(Integer id);


    // 查询一级分类（数组）下的全部商品 以及 一级分类名
    @Select({
            "<script>",
            "select category1_id, id from tb_spu t1 ",
            "where exists (select 1 from tb_category t2 where t2.recom = 1)",
            "group by category1_id",
            "</script>"
    })
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "category1_id", property = "category1Id"),
            @Result(column = "category1_id", property = "category1Name", one = @One(select = "selectCategoryName")),
            @Result(column = "category1_id", property = "skuList",
                    many = @Many(select = "com.example.skuservice.mapper.SkuMapper.selectSkuByGoodsId"))
    })
    List<Spu> selectCategorySku(@Param("parentId") Integer parentId);


    @Select({
            "select * from tb_category where fath_id = 0"
    })
    List<Category> selectRootCategory();


    //首页的推荐商品
    // 查询二级分类商品
    @Select({
            "<script>",
            "select id,category2_id ,category1_id from tb_spu",
            "<where>",
            "<if test = 'category1Id != null'>and category1Id = #{category1Id}</if>",
            "</where>",
            "group by category2_id",
            "</script>"
    })
    @Results({
            @Result(column = "category2_id", property = "category2Id"),
            @Result(column = "category2_id", property = "category2Name", one = @One(select = "selectCategoryName")),
            @Result(column = "category1_id", property = "category1Name", one = @One(select = "selectCategoryName")),
            @Result(column = "category2_id", property = "skuList",
                    many = @Many(select = "com.example.skuservice.mapper.SkuMapper.selectSkuByCategory")),
    })
    List<Spu> selectTwoSpu(@Param("category1Id") Integer category1Id);

    //查询三级分类商品
    @Select({
            "<script>",
            "select id,category3_id, category1_id,category2_id from tb_spu",
            "<where>",
            "<if test = 'category2Id != null'>and category2Id = #{category2Id}</if>",
            "</where>",
            "group by category3_id",
            "</script>"
    })
    @Results({
            @Result(column = "category3_id", property = "category3Id"),
            @Result(column = "category3_id", property = "category3Name", one = @One(select = "selectCategoryName")),
            @Result(column = "category1_id", property = "category1Name", one = @One(select = "selectCategoryName")),
            @Result(column = "category2_id", property = "category2Name", one = @One(select = "selectCategoryName")),
            @Result(column = "category3_id", property = "skuList",
                    many = @Many(select = "com.example.skuservice.mapper.SkuMapper.selectThreeSku")),
    })
    List<Spu> selectThreeSpu(@Param("category2Id") Integer category2Id);


    // 查询一级分类下的spu
    @Select({
            "select * from tb_spu",
            "where category1_id = #{category1Id}"
    })
    List<Spu> selectOneSpus(Integer category1Id);

    @Select({
            "select * from tb_spu",
            "where category2_id = #{category2Id}"
    })
    List<Spu> selectTwoSpus(Integer category2Id);

    @Select({
            "select * from tb_spu",
            "where category3_id = #{category13d}"
    })
    List<Spu> selectThreeSpus(Integer category3Id);

    @Select({
            "select * from tb_spu",
    })
    List<Spu> selectSpus();
}
