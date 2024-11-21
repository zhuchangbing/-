package com.example.orderservice.controller;

import com.example.common.domain.entity.SaveSku;
import com.example.common.domain.entity.User;
import com.example.common.domain.vo.Result;
import com.example.common.service.CollectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/collect")
@RequiredArgsConstructor
@Slf4j
public class CollectController {
    private final CollectionService collectionService;

    @GetMapping("/getUserCollect")
    public Result getCollect(Authentication authentication) {
        Integer userId = ((User) authentication.getPrincipal()).getId();
        return Result.success(collectionService.getUserSaveSkus(userId));
    }

    // 添加收藏
    @PostMapping("/addCollect")
    public Result addCollect(@RequestBody SaveSku saveSku, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        System.out.println(user);
        saveSku.setUserId(user.getId());
        System.out.println(saveSku);
        return Result.success(collectionService.addUserSaveSku(saveSku));
    }

    // 获取是否收藏
    @GetMapping("/getssku")
    public Result getssku(SaveSku saveSku, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        saveSku.setUserId(user.getId());
        return Result.success(collectionService.getUserSaveSkuById(saveSku));
    }

    // 删除收藏的商品
    @DeleteMapping("/{skuId}")
    public Result delete(@PathVariable("skuId") Integer skuId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        SaveSku saveSku = new SaveSku();
        saveSku.setUserId(user.getId());
        saveSku.setSkuId(skuId);
        try {
            return Result.success(collectionService.removeUserSaveSkuById(saveSku));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping
    public Result deleteBatch(@RequestBody HashMap<String, ArrayList> params
            , Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List ids = params.get("ids");
        try {
            collectionService.removeBatch(ids, user.getId());
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
