package com.example.skuservice.mapper;

import com.example.common.domain.entity.Category;
import com.example.common.domain.entity.Spu;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 根据id查询分类名 确定商品的三级分类
    @Select({
            "select name from tb_category where id = #{categoryId}"
    })
    String selectCategoryNameById(Integer categoryId);


    // 按父子层级查询所有的分类信息
    @Select({
            "select * from tb_category",
            "where fath_id = 0"
    })
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "children",
                    many = @Many(select = "getTwoCategory"))
    })
    List<Category> getCategory();

    // 获取父分类的名字
    @Select({
            "select name from tb_category",
            "where id = #{fathId}"
    })
    String getFathName(Integer fathId);

    // 关联查询二级分类下的信息
    @Select({
            "select * from tb_category",
            "where fath_id = #{fathId}"
    })
    @Results({
            @Result(column = "fath_id", property = "fathId"),
            @Result(column = "fath_id", property = "fathName", one = @One(select = "getFathName")),
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "children",
                    many = @Many(select = "getThreeCategory"))
    })
    List<Category> getTwoCategory(Integer fathId);

    // 关联查询三级分类
    @Select({
            "select * from tb_category",
            "where fath_id = #{fathId}"
    })
    @Results({
            @Result(column = "fath_id", property = "fathId"),
            @Result(column = "fath_id", property = "fathName", one = @One(select = "getFathName")),
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "children",
                    many = @Many(select = "getThreeCategory"))
    })
    List<Category> getThreeCategory(Integer fathId);


    //  非父子结构的查询
    @Select({
            "<script>",
            "select * from tb_category",
            "<where>",
            "<if test = 'name != null and name.length > 0'>and name like concat('%',#{name},'%')</if>",
            "<if test = 'id != null'>and fath_id = #{id} or id = #{id}</if>",
            "<if test = 'recom != null'>and recom = #{recom}</if>",
            "<if test = 'status != null'>and status = #{status}</if>",
            "</where>",
            "</script>"
    })
    @Results({
            @Result(column = "fath_id", property = "fathId"),
            @Result(column = "fath_id", property = "fathName", one = @One(select = "getFathName")),
    })
    List<Category> getCategoryList(Category c);

    // 根据id查询分类   以便判定是否存在
    @Select({
            "select * from tb_category where id = #{id}"
    })
    Category selectCategoryById(Integer id);

    // 更新分类的信息
    @Update({
            "<script>",
            "update tb_category",
            "<set>",
            "<if test = 'name != null and name.length > 0'>name = #{name},</if>",
            "<if test = 'dscp != null and dscp.length > 0'>dscp = #{dscp},</if>",
            "<if test = 'recom != null'>recom = #{recom},</if>",
            "<if test = 'status != null'>status = #{status},</if>",
            "</set>",
            "where id = #{id}",
            "</script>"
    })
    int updateCategory(Category c);


    @Insert({
            "insert into tb_category(name,fath_id,dscp,recom,status)",
            "values(#{name},#{fathId},#{dscp},#{recom},#{status})",
    })
    int addCategory(Category c);

    //根据名字查询分类以免重复
    @Select({
            "select * from tb_category",
            "where name = #{name}"
    })
    Category getCategoryByName(String name);
}
