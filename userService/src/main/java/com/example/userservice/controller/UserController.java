package com.example.userservice.controller;


import com.example.common.domain.entity.Address;
import com.example.common.domain.entity.User;
import com.example.common.domain.vo.Result;
import com.example.common.domain.vo.UserVo;
import com.example.common.service.UserService;
import com.example.common.utils.JWTUtil;
import com.example.common.utils.RedisUtil;
import com.example.userservice.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisUtil redisUtil;

    //注册
    @PostMapping("/reg")
    public Result reg(@RequestBody UserVo user, HttpSession session) {
        //String tvercode = (String) session.getAttribute("vercode");
        String tvercode = (String) redisUtil.get(user.getEmail());
        System.out.println(user.getEmail());
        System.out.println(tvercode);
        String vercode = user.getVercode();
        if (!vercode.equals(tvercode)) {
            return Result.error("验证码输入有误");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userService.reg(user);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    // 登录
    @PostMapping("/login")
    public Result login(@RequestBody UserVo user) {
        // 验证码校验
        String sCaptcha = null;
        try {
            Claims claims = JWTUtil.parseJWT(user.getJwt());
            sCaptcha = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("验证码已过期");
        }
        try {
            if (!user.getCaptcha().equalsIgnoreCase(sCaptcha)) {

                throw new Exception("验证码错误");
            }
            User xuser = userService.login(user);
            String token = JWTUtil.createJWT(xuser);
            xuser.setPassword(null);
            xuser.setPayPassword(null);
            return Result.success(200, token, xuser);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    //增加用户
    @PostMapping("/adduser")
    public Result addUser(@RequestBody User user) {
        try {
            return Result.success(userService.insertUser(user));
        } catch (Exception e) {
            log.warn(Arrays.toString(e.getStackTrace()));
            return Result.error(e.getMessage());
        }
    }

    // 修改用户
    @PutMapping("/edituser")
    public Result editUser(@RequestBody User user) {
        try {
            return Result.success(userService.updateUserInfo(user));
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(Arrays.toString(e.getStackTrace()));
            return Result.error(e.getMessage());
        }
    }

    // 查询用户
    @GetMapping("/getuser")
    public Result getUser(Integer page, Integer size, UserVo userVo) {
        return Result.success(userService.getUserList(page, size, userVo));
    }


    //用户增加地址
    @PostMapping("/addaddr")
    public Result addAddr(@RequestBody Address address) {
        try {
            return Result.success(userService.insertAddress(address));
        } catch (Exception e) {
            log.warn(Arrays.toString(e.getStackTrace()));
            return Result.error(e.getMessage());
        }
    }

    // 用户删除地址
    @DeleteMapping
    public Result deleteUserAddr(Integer id) {
        try {
            return Result.success(userService.deleteAddress(id));
        } catch (Exception e) {
            log.warn(Arrays.toString(e.getStackTrace()));
            return Result.error(e.getMessage());
        }
    }

    // 修改用户某个地址
    @PutMapping("/editaddr")
    public Result editAddr(@RequestBody Address address) {
        try {
            return Result.success(userService.updateAddress(address));
        } catch (Exception e) {
            log.warn(Arrays.toString(e.getStackTrace()));
            return Result.error(e.getMessage());
        }
    }

    // 查询用户所有地址
    @GetMapping("/getaddr")
    public Result getAddr(Integer userId) {
        return Result.success(userService.getUserAddr(userId));
    }

    @GetMapping("/getuserbyid")
    public Result getUserById(Integer id) {
        return Result.success(userService.getUserById(id));
    }

}
