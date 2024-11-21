package com.example.common.service;

import com.example.common.domain.entity.Category;
import com.example.common.domain.entity.Sku;
import com.example.common.domain.entity.SkuSpec;
import com.example.common.domain.entity.Spu;
import com.example.common.domain.vo.Result;
import com.example.common.domain.vo.SkuVo;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface SkuService {
    List<Sku> getSkuByCondition(SkuVo sku);   //条件查询商品 名字和描述有相同字符

    Sku getSku(Integer id);    //查找单个商品

    Sku getSku1(Integer id);    //查找单个商品

    int addSku(SkuVo sku) throws Exception;
    /*int updateSku(Integer id) throws Exception;*/

    // 类型查询商品
    List<Spu> selectCategorySpu(Integer parentId);

    //查询根类别
    List<Category> selectRootCategory();

    //查询二级分类
    List<Spu> selectTwoCategory(Integer category1Id);

    //查询三级分类
    List<Spu> selectThreeCategory(Integer category2Id);


    //查询一二三分类的商品
    PageInfo selectFirstSku(Integer page, Integer size, SkuVo skuVo);

    PageInfo selectSecondSku(Integer page, Integer size, SkuVo skuVo);

    PageInfo selectThirdSku(Integer page, Integer size, SkuVo skuVo);

    // 查询所有商品
    PageInfo selectAll(Integer page, Integer size, SkuVo skuVo);


    // 查询所有商品分类 按照父子结构
    PageInfo selectAllCategory(Integer page, Integer size);

    // 查询非父子结构的 分类信息
    PageInfo selectChildenCategory(Integer page, Integer size, Category category);

    //更新分类的信息
    int updateCategory(Category category) throws Exception;

    int addNewCategory(Category category) throws Exception;


    PageInfo searchForPage(Integer page, Integer size, SkuVo skuVo);

    // 批量获取商品
    List<Sku> getBatchSku(Integer[] ids);


    // 插入商品表 图片表（id） 商品属性表（id）  商品插入以后有id
    int insertSkuPicSpec(SkuVo sku);

    // 查询用户的发布商品
    List<Sku> getPUserSkus(Integer puserId);

    // 用户撤销发布商品
    int deletePUserSku(Integer id) throws Exception;

    // 用户修改商品 信息 及 商品属性
    int updatePUserSku(Sku sku) throws Exception;

    // 修改商品表的状态
    int updateSkuStatus(Integer id) throws Exception;

    // ES初始化
    Object initESData() throws Exception;

    PageInfo<Sku> esSearchForPage(Integer page, Integer size, SkuVo sku) throws IOException;
}
