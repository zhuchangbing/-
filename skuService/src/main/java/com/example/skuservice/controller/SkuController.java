package com.example.skuservice.controller;

import com.example.common.domain.entity.*;
import com.example.common.domain.vo.Result;
import com.example.common.domain.vo.SkuVo;
import com.example.common.service.SkuService;
import com.example.common.utils.RedisUtil;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/sku")
@RequiredArgsConstructor
@Slf4j
public class SkuController {
    private final SkuService skuService;
    private final DataSourceAutoConfiguration dataSourceAutoConfiguration;

    /*@GetMapping
    public Result getAllSku() {
        return Result.success(skuService.getAllSku());
    }

    @GetMapping("/{id}")
    public Result getAllSkuSpec(@PathVariable("id") Integer id) {
        return Result.success(skuService.getSku(id));
    }*/

    //增
    //改


    //  **这是主页的商品查询**  查询对应种类的商品
    @GetMapping("/getcategorysku")
    public Result getCategorySku(@RequestParam("parentId") Integer parentId) {
        return Result.success(skuService.selectCategorySpu(parentId));
    }

    @GetMapping("/getcategory")
    public Result getCategory() {
        return Result.success(skuService.selectRootCategory());
    }

    @GetMapping("/gettwoc")
    public Result getTwoCategory(Integer category1Id) {
        return Result.success(skuService.selectTwoCategory(category1Id));
    }

    @GetMapping("/getthreec")
    public Result getThreeCategory(Integer category2Id) {
        return Result.success(skuService.selectThreeCategory(category2Id));
    }


    @GetMapping("/getfirst")
    public Result getFirst(Integer page, Integer size, SkuVo skuVo) {
        return Result.success(skuService.selectFirstSku(page, size, skuVo));
    }

    @GetMapping("/getsecond")
    public Result getSecond(Integer page, Integer size, SkuVo skuVo) {
        return Result.success(skuService.selectSecondSku(page, size, skuVo));
    }

    @GetMapping("/getthird")
    public Result getThird(Integer page, Integer size, SkuVo skuVo) {
        return Result.success(skuService.selectThirdSku(page, size, skuVo));
    }

    @GetMapping("/getall")
    public Result getAll(Integer page, Integer size, SkuVo skuVo) {
        return Result.success(skuService.selectAll(page, size, skuVo));
    }

    @GetMapping("/getsku")
    public Result getSku(Integer skuId) {
        return Result.success(skuService.getSku(skuId));
    }

    @GetMapping("/getallc")
    public Result getAllC(Integer page, Integer size) {
        return Result.success(skuService.selectAllCategory(page, size));
    }

    @GetMapping("/getcategorylist")
    public Result getCategoryList(Integer page, Integer size, Category category) {
        return Result.success(skuService.selectChildenCategory(page, size, category));
    }

    @PutMapping("/updatecategory")
    public Result updateCategory(@RequestBody Category category) {
        try {
            return Result.success(skuService.updateCategory(category));
        } catch (Exception e) {
            log.warn(Arrays.toString(e.getStackTrace()));
            return Result.error(e.getMessage());
        }
    }

    @PostMapping
    public Result addCategory(@RequestBody Category category) {
        try {
            return Result.success(skuService.addNewCategory(category));
        } catch (Exception e) {
            log.warn(Arrays.toString(e.getStackTrace()));
            return Result.error(e.getMessage());
        }
    }


    @GetMapping("/searchgoods")
    public Result searchGoods(Integer page, Integer size, SkuVo skuVo) {
        return Result.success(skuService.searchForPage(page, size, skuVo));
    }

    @GetMapping("/getBatch")
    public Result getBatch(Integer[] ids) {
        List<Sku> skus = skuService.getBatchSku(ids);
        return Result.success(skus);
    }


    // 插入商品
    @PostMapping("/addsku")
    public Result addSku(@RequestBody SkuVo sku, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        sku.setPuserId(user.getId());
        System.out.println(sku);
        List<Pic> pics = sku.getPics();
        List<SkuSpec> specList = sku.getSpecList();
        for (Pic p : pics) {
            System.out.println(p);
        }
        for (SkuSpec s : specList) {
            System.out.println(s);
        }
        return Result.success(skuService.insertSkuPicSpec(sku));
    }

    // 获取用户发布的商品
    @GetMapping("/getpuserskus")
    public Result getPuserSkus(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return Result.success(skuService.getPUserSkus(user.getId()));
    }

    // 删除商品
    @DeleteMapping
    public Result deleteSku(Integer id) {
        try {
            return Result.success(skuService.deletePUserSku(id));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 修改商品
    @PutMapping("/updatesku")
    public Result updateSku(@RequestBody Sku sku) {
        try {
            return Result.success(skuService.updatePUserSku(sku));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 查询ES中的数据
    @GetMapping("/es")
    public Result getES(Integer page, Integer size, SkuVo sku) throws IOException {
        return Result.success(skuService.esSearchForPage(page, size, sku));
    }
}
