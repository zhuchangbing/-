package com.example.userservice.controller;

import com.example.common.domain.entity.Address;
import com.example.common.domain.entity.User;
import com.example.common.domain.vo.Result;
import com.example.common.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addr")
@RequiredArgsConstructor
public class AddressController {
    private static final Logger log = LoggerFactory.getLogger(AddressController.class);
    private final AddressService addressService;

    @GetMapping
    public Result getUserAddress(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return Result.success(addressService.getUserAllAddress(user.getId()));
    }

    @PutMapping
    public Result updateUserAddress(Authentication authentication,
                                    @RequestBody Address address) {
        User user = (User) authentication.getPrincipal();
        address.setUserId(user.getId());
        try {
            addressService.editUserAddress(address);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 用户添加新的地址
    @PostMapping
    public Result addUserAddress(@RequestBody Address address
            , Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        address.setUserId(user.getId());
        try {
            return Result.success(addressService.addUserAddress(address));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 用户删除地址
    @DeleteMapping
    public Result deleteUserAddress(Integer id) {
        try {
            addressService.deleteUserAddress(id);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result updateUserAddress(@PathVariable("id") Integer id) {
        try {
            addressService.updateDefaultAddress(id);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}




















