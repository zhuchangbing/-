package com.example.skuspecservice.controller;

import com.example.common.domain.vo.Result;
import com.example.common.service.SkuSpecService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/skuspec")
@RequiredArgsConstructor
public class SkuSpecController {
    private final SkuSpecService skuSpecService;
    /*@GetMapping("/{id}")
    public Result getSkuSpecList(@PathVariable("id") Integer id) {
        return Result.success(skuSpecService.getSkuSpecList(id));
    }*/


    // 查找某一品牌的所有属性
    @GetMapping("/attrs")
    public Result attrs(Integer goodsId) {
        return Result.success(skuSpecService.getSkuSpecListByGoodsId(goodsId));
    }

}
