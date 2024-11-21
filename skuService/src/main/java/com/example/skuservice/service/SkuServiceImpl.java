package com.example.skuservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.util.ObjectBuilder;
import com.example.common.domain.entity.*;
import com.example.common.domain.vo.SkuVo;
import com.example.common.service.SkuService;
import com.example.common.service.SkuSpecService;
import com.example.common.service.UserService;
import com.example.common.utils.RedisUtil;
import com.example.skuservice.es.SkuESRepository;
import com.example.skuservice.mapper.CategoryMapper;
import com.example.skuservice.mapper.SkuMapper;
import com.example.skuservice.mapper.SpuMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@DubboService
@RequiredArgsConstructor
public class SkuServiceImpl implements SkuService {
    private final SkuMapper skuMapper;
    private final SpuMapper spuMapper;
    private final CategoryMapper categoryMapper;
    private final SkuESRepository skuESRepository;
    private final ElasticsearchClient esClient;
    @DubboReference
    private SkuSpecService skuSpecService;
    @DubboReference
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    // 批量获取商品
    @Override
    public List<Sku> getBatchSku(Integer[] ids) {
        List<Sku> skus = skuMapper.selectBatchSku(ids);
        for (Sku sku : skus) {
            sku.setSpecList(skuSpecService.getSkuSpecList(sku.getId()));
        }
        return skus;
    }

    @Override
    @GlobalTransactional
    public int insertSkuPicSpec(SkuVo sku) {
        skuMapper.insertSku(sku);
        System.out.println("skuId = " + sku.getId());
        for (Pic pic : sku.getPics()) {
            pic.setSkuId(sku.getId());
        }
        for (SkuSpec skuSpec : sku.getSpecList()) {
            skuSpec.setSkuId(sku.getId());
        }
        skuMapper.insertPic(sku.getPics(), sku.getId());

        return skuSpecService.insertSkuSpec(sku.getSpecList(), sku.getId());
    }

    @Override
    public List<Sku> getPUserSkus(Integer puserId) {
        List<Sku> list = skuMapper.selectSkuByPUserId(puserId);
        list.forEach(l -> {
            l.setSpecList(skuSpecService.getSkuSpecList(l.getId()));
        });
        return list;
    }

    // 删除商品
    @Override
    public int deletePUserSku(Integer id) throws Exception {
        Sku sku = skuMapper.selectSku(id);
        if (ObjectUtils.isEmpty(sku)) {
            throw new Exception("待删除的商品不存在");
        }
        Sku s = skuMapper.selectSku(id);
        skuESRepository.delete(s);  // 删除ES数据库中的数据
        return skuMapper.deletePUserSku(id);
    }

    @Override
    @GlobalTransactional
    public int updatePUserSku(Sku sku) throws Exception {
        Sku s = skuMapper.selectSku(sku.getId());
        if (ObjectUtils.isEmpty(s)) {
            throw new Exception("待修改的商品不存在，请检查");
        }
        skuMapper.updateSku(sku);
        sku.getSpecList().forEach(skuSpec -> {
            try {
                skuSpecService.updateSkuSpec(skuSpec);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        return 0;
    }

    @Override
    public int updateSkuStatus(Integer id) throws Exception {
        Sku s = skuMapper.selectSku(id);
        if (ObjectUtils.isEmpty(s)) {
            throw new Exception("待修改商品不存在");
        }
        Sku ss = skuMapper.selectSku(id);
        skuESRepository.save(ss);   // 写入到es中
        return skuMapper.updateSkuStatus(id);
    }


    @Override
    public List<Sku> getSkuByCondition(SkuVo sku) {
        return skuMapper.selectSkuByCondition(sku);
    }

    // 查找单个商品的详情
    @Override
    public Sku getSku(Integer id) {
        Sku s = skuMapper.selectSku(id);
        s.setSpecList(skuSpecService.getSkuSpecList(id));
        s.setPuser(userService.getUserById(s.getPuserId()));
        return s;
    }

    @Override
    public Sku getSku1(Integer id) {
        return skuMapper.selectSku1(id);
    }

    @Override
    @Transactional   //加事务 一个失败了全部需要回滚
    public int addSku(SkuVo sku) throws Exception { //skuVo中有图片信息和商品属性
        int res = skuMapper.insertSku(sku);
        skuMapper.insertPic(sku.getPics(), sku.getId());   //保存图片
        skuSpecService.insertSkuSpec(sku.getSpecList(), sku.getId()); //保存属性
        Sku s = skuMapper.selectSku(sku.getId());
        skuESRepository.save(s);   // 写入到es中
        return res;
    }

    /*@Override
    public int updateSku(Integer id) throws Exception{
        return skuMapper.up;
    }*/


    // 分页查询一级分类商品
    @Override
    public List<Spu> selectCategorySpu(Integer parentId) {
        String key = "categoryid:" + parentId;
        List<Spu> spulist1 = (List<Spu>) redisUtil.get(key);
        if (!ObjectUtils.isEmpty(spulist1)) {
            return spulist1;
        }
        List<Spu> spulist = spuMapper.selectCategorySku(parentId);

        for (Spu spu : spulist) {
            for (Sku sku : spu.getSkuList()) {
                sku.setSpecList(skuSpecService.getSkuSpecList(sku.getId()));
            }
        }
        redisUtil.set(key, spulist);
        return spulist;
    }

    @Override
    public List<Category> selectRootCategory() {
        return spuMapper.selectRootCategory();
    }

    // 分页查询一级分类商品
    @Override
    public List<Spu> selectTwoCategory(@RequestParam("category1Id") Integer category1Id) {
        return spuMapper.selectTwoSpu(category1Id);
    }

    @Override
    public List<Spu> selectThreeCategory(Integer category2Id) {
        return spuMapper.selectThreeSpu(category2Id);
    }

    @Override
    public PageInfo selectFirstSku(Integer page, Integer size, SkuVo skuVo) {
        PageHelper.startPage(page, size);
        List<Sku> list = skuMapper.selectFirstSku(skuVo);
        return new PageInfo(list);
    }

    @Override
    public PageInfo selectSecondSku(Integer page, Integer size, SkuVo skuVo) {
        PageHelper.startPage(page, size);
        List<Sku> list = skuMapper.selectSecondSku(skuVo);
        return new PageInfo(list);
    }

    @Override
    public PageInfo selectThirdSku(Integer page, Integer size, SkuVo skuVo) {
        PageHelper.startPage(page, size);
        List<Sku> list = skuMapper.selectThirdSku(skuVo);
        return new PageInfo(list);
    }

    @Override
    public PageInfo selectAll(Integer page, Integer size, SkuVo skuVo) {
        PageHelper.startPage(page, size);
        List<Sku> list = skuMapper.selectAllSku(skuVo);
        return new PageInfo(list);
    }

    @Override
    public PageInfo selectAllCategory(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Category> list = categoryMapper.getCategory();
        return new PageInfo(list);
    }

    @Override
    public PageInfo selectChildenCategory(Integer page, Integer size, Category category) {
        PageHelper.startPage(page, size);
        List<Category> list = categoryMapper.getCategoryList(category);
        return new PageInfo(list);
    }


    @Override
    public int updateCategory(Category category) throws Exception {
        Category sc = categoryMapper.selectCategoryById(category.getId());
        if (!ObjectUtils.isEmpty(sc)) {
            return categoryMapper.updateCategory(category);
        }
        throw new Exception("待修改的分类不存在");
    }

    @Override
    public int addNewCategory(Category category) throws Exception {
        Category sc = categoryMapper.getCategoryByName(category.getName());
        if (ObjectUtils.isEmpty(sc)) {
            return categoryMapper.addCategory(category);
        }
        throw new Exception("该分类已经存在");
    }

    @Override
    public PageInfo searchForPage(Integer page, Integer size, SkuVo skuVo) {
        PageHelper.startPage(page, size);
        for (int i = 0; i < 50; i++) {
            System.out.println(skuVo);
        }
        List<Sku> list;
        if (!ObjectUtils.isEmpty(skuVo.getCategory3Id())) {
            list = skuMapper.selectThirdSku(skuVo);
            return new PageInfo(list);
        }
        if (!ObjectUtils.isEmpty(skuVo.getCategory2Id())) {
            list = skuMapper.selectSecondSku(skuVo);
            return new PageInfo(list);
        }
        if (!ObjectUtils.isEmpty(skuVo.getCategory1Id())) {
            list = skuMapper.selectFirstSku(skuVo);
            return new PageInfo(list);
        }
        return selectAll(page, size, skuVo);
    }


    // 初始化ES数据库的方法
    // 借助Spring的定时任务
    @Override
    public Object initESData() throws Exception {
        // 先从MySQL中查询所有数据，在写入ES中     $如果数据量太大，需要分段读取$
        List<Sku> skuList = skuMapper.selectAllSku(new SkuVo());
        return skuESRepository.saveAll(skuList);  // 返回Iterable
    }

    @Override
    public PageInfo<Sku> esSearchForPage(Integer page, Integer size, SkuVo sku) throws IOException {
        PageInfo<Sku> pageInfo = new PageInfo<>();
        pageInfo.setPages(page);
        pageInfo.setSize(size);
        SearchResponse<Sku> response = esClient.search(
                searchReq -> searchReq.index(Sku.ES_INDEX)
                        .query(
                                query -> query.bool(boolQuery -> getBoolQuery(boolQuery, sku))
                        )
                        .sort(
                                sort -> sort.field(sortField -> sortField.field("price").order(SortOrder.Desc))
                        )
                        .from((page - 1) * size)
                        .size(size)
                , Sku.class
        );
        List<Sku> skulist = new ArrayList<Sku>();
        response.hits().hits().forEach(hit -> {
            Sku s = hit.source();
            skulist.add(s);
        });
        pageInfo.setTotal(response.hits().total().value());
        pageInfo.setList(skulist);
        return pageInfo;
    }

    private ObjectBuilder<BoolQuery> getBoolQuery(BoolQuery.Builder boolQuery, SkuVo sku) {
        if (StringUtils.hasText(sku.getName())) {
            Query byKey = MultiMatchQuery.of(
                    field -> field.fields("name", "description")
                            .analyzer("ik_max_word")
                            .query(sku.getName())
            )._toQuery();
            boolQuery.filter(byKey);
        }
        return boolQuery;
    }


    // es 分页查询数据
}












