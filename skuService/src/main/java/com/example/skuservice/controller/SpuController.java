package com.example.skuservice.controller;

import com.example.common.domain.vo.Result;
import com.example.common.service.SpuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spu")
@RequiredArgsConstructor
public class SpuController {
    private final SpuService spuService;

    @GetMapping("/get1")
    public Result sku1(Integer category1Id) {
        return Result.success(spuService.get1Sku(category1Id));
    }

    @GetMapping("/get2")
    public Result sku2(Integer category2Id) {
        return Result.success(spuService.get2Sku(category2Id));
    }

    @GetMapping("/get3")
    public Result sku3(Integer category3Id) {
        return Result.success(spuService.get3Sku(category3Id));
    }

    @GetMapping("/spus")
    public Result skus() {
        return Result.success(spuService.getSkus());
    }
}
