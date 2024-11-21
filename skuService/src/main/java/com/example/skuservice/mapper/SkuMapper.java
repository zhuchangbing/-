package com.example.skuservice.mapper;

import com.example.common.domain.entity.Pic;
import com.example.common.domain.entity.Sku;
import com.example.common.domain.vo.SkuVo;
import org.apache.ibatis.annotations.*;

import java.util.List;


//查询所有商品
@Mapper
public interface SkuMapper {
    @Select({
            "<script>",
            "select * from tb_sku",
            "<where>",
            "<if test = 'name != null and name.length > 0'>name like concat('%',#{name},'%')</if>",
            "</where>",
            "</script>"
    })
    @ResultMap("pic")
    List<Sku> selectAllSku(SkuVo skuVo);

    /**
     * 查询单个商品的详细信息 用于详情页
     *
     * @param id 商品的Id
     * @return 商品的详细信息
     */
    @Select({
            "select * from tb_sku where id = #{id}"
    })
    @ResultMap("pic")
    Sku selectSku(Integer id);       //查看单个商品的详情

    @Select({
            "select * from tb_sku where id = #{id} for update"
    })
    @ResultMap("pic")
    Sku selectSku1(Integer id);

    @Select({
            "<script>",
            "select * from tb_sku",
            "<where>",
            "<if test='search!=null and search.length>0'>or name like concat('%',#{search},'%')</if>",
            "<if test='search!=null and search.length>0'>or description like concat('%',#{search},'%')</if>",
            "</where>",
            "</script>"
    })
    List<Sku> selectSkuByCondition(SkuVo sku);   //条件查询

    @Update({
            "<script>",
            "update tb_sku",
            "<set>",
            "<if test='name!=null and name.length>0'>name = #{name},</if>",
            "<if test='price!=null'>price = #{price},</if>",
            "<if test='isFree!=null'>is_free = #{isFree},</if>",
            "<if test='description!=null and description.length>0'>description = #{description},</if>",
            "<if test='isSold!=null'>is_sold = #{isSold},</if>",
            "<if test='freshLevel!=null'>fresh_level = #{freshLevel},</if>",
            "</set>",
            "where id = #{id}",
            "</script>"
    })
    int updateSku(Sku sku);    // 修改商品


    @Insert({
            "insert into tb_sku(name,price,description,goods_id,is_free,fresh_level,category_id,puser_id)",
            "values(#{name},#{price},#{description},#{goodsId},#{isFree},#{freshLevel},#{categoryId},#{puserId})",

    })
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insertSku(SkuVo sku);    //插入商品


    //插入图片
    @Insert({
            "<script>",
            "insert into tb_pic(url,sku_id)",
            "values",
            "<foreach collection = 'pics' item = 'pic' separator = ','>",
            "(#{pic.url},#{skuId})",
            "</foreach>",
            "</script>"
    })
    int insertPic(List<Pic> pics, Integer skuId);

    //查询商品的图片
    @Select({
            "select * from tb_pic where sku_id = #{skuId}"
    })
    List<Pic> selectPicBySkuId(Integer skuId);

    //根据商品Id查询商品  间接查询出来商品的图片
    @Select({
            "select * from tb_sku t1 where ",
            "exists(select 1 from tb_spu t2 ",
            "where t1.goods_id=t2.id and t2.category1_id = #{category1Id})"
    })
    @Results(id = "pic", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "picList",
                    many = @Many(select = "selectPicBySkuId")),
            @Result(column = "category_id", property = "categoryId"),
            @Result(column = "category_id", property = "categoryName",
                    one = @One(select = "com.example.skuservice.mapper.CategoryMapper.selectCategoryNameById"))
    })
    List<Sku> selectSkuByGoodsId(Integer category1Id);


    // 查询二级商品

    @Select({
            "select * from tb_sku t1 where ",
            "exists(select 1 from tb_spu t2 ",
            "where t1.goods_id=t2.id and t2.category2_id = #{category2Id})"
    })
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "picList",
                    many = @Many(select = "selectPicBySkuId"))
    })
    List<Sku> selectSkuByCategory(Integer category2Id);

    //查询三级商品
    @Select({
            "select * from tb_sku t1 where ",
            "exists(select 1 from tb_spu t2 ",
            "where t1.goods_id=t2.id and t2.category3_id = #{category3Id})"
    })
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "picList",
                    many = @Many(select = "selectPicBySkuId"))
    })
    List<Sku> selectThreeSku(Integer category3Id);


    // 查询一级分类的商品

    @Select({
            "<script>",
            "select * from tb_sku where goods_id in",
            "(select id from tb_spu where category1_id = #{category1Id})",
            "<if test = 'name!=null and name.length>0'>and name like concat('%',#{name},'%')</if>",
            "</script>",
    })
    @ResultMap("pic")
    List<Sku> selectFirstSku(SkuVo skuVo);

    // 查询二级分类的商品

    @Select({
            "<script>",
            "select * from tb_sku where goods_id in",
            "(select id from tb_spu where category2_id = #{category2Id})",
            "<if test = 'name!=null and name.length>0'>and name like concat('%',#{name},'%')</if>",
            "</script>",
    })
    @ResultMap("pic")
    List<Sku> selectSecondSku(SkuVo skuVo);

    // 查询三级分类的商品

    @Select({
            "<script>",
            "select * from tb_sku where goods_id in",
            "(select id from tb_spu where category3_id = #{category3Id})",
            "<if test = 'name!=null and name.length>0'>and name like concat('%',#{name},'%')</if>",
            "</script>",
    })
    @ResultMap("pic")
    List<Sku> selectThirdSku(SkuVo skuVo);


    // 根据商品Id查询商品信息 属性数组 和 图片数组

    // 批量获取商品信息
    @Select({
            "<script>",
            "select * from tb_sku",
            "where id in",
            "<foreach collection = 'ids' item = 'id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    @ResultMap("pic")
    List<Sku> selectBatchSku(Integer[] ids);

    // 查询用户发布的商品
    @Select({
            "select * from tb_sku",
            "where puser_id = #{puserId}"
    })
    @ResultMap("pic")
    List<Sku> selectSkuByPUserId(Integer puserId);


    // 删除商品
    @Delete({
            "delete from tb_sku",
            "where id = #{id}"
    })
    int deletePUserSku(Integer id);


    // 修改订单状态
    @Update({
            "update tb_sku",
            "set is_sold = 1",
            "where id = #{id}"
    })
    int updateSkuStatus(Integer id);


}
