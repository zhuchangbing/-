package com.example.adminservice.controller;

import com.example.adminservice.utils.JWTUtils;
import com.example.common.domain.entity.Admin;
import com.example.common.domain.vo.AdminVo;
import com.example.common.domain.vo.Result;
import com.example.common.service.AdminService;
import com.example.common.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController  //SpringMVC 的注解    @Controller是Spring的注解，放到Spring的容器中
@RequiredArgsConstructor  // lombok 添加一个包含所有final属性的构造器
@RequestMapping("/admin")   // 为当前中类中所有的处理器方法添加公共的路径映射的前缀
@Slf4j    // lombok的注解  自动添加一个叫log的对象，用于对象的打印
public class AdminController {
    private final AdminService adminService;

    /*@PostMapping("/logout")
    public Result logout(HttpSession session) {
        session.invalidate();
        return Result.success();
    }

    // 登录成功后返回登录对象
    @PostMapping("/autologin")
    public Result autoLogin(HttpServletRequest request,
                            HttpServletResponse response) {
        String token = request.getHeader("token");
        if(! ObjectUtils.isEmpty(token) && JWTUtils.verify(token)) {
            String email = JWTUtils.getString(token,"email");
            String password = JWTUtils.getString(token,"password");
            Admin lAdmin = new Admin();
            lAdmin.setEmail(email); lAdmin.setPassword(password);
            try {
                Admin sAdmin = adminService.login(lAdmin);
                Map<String, Object> map = new HashMap<>();
                map.put("email", lAdmin.getEmail());
                map.put("password", lAdmin.getPassword());
                token = JWTUtils.sign(map);
                response.setHeader("token", token);
                return Result.success(sAdmin);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error(e.getMessage());
            }
        }
        return Result.error("token为空");
    }

    @PostMapping("/getlogin")
    public Result getlogin(HttpSession session){
        Admin admin = (Admin) session.getAttribute("Admin");
        if(! ObjectUtils.isEmpty(admin)) {
            return Result.success();
        }
        return Result.error("当前用户未登录");
    }

    @PostMapping("/login")
    public Result login(@RequestBody AdminVo admin, HttpSession session,
                        HttpServletRequest request, HttpServletResponse response) {
        try {
            Admin sAdmin = adminService.login(admin);
            session.setAttribute("Admin", sAdmin);
            if(admin.getSavepwd() == 1) {
                Map<String, Object> map = new HashMap<>();  // 生成一个新token
                map.put("email", sAdmin.getEmail());
                map.put("password", sAdmin.getPassword());
                String token = JWTUtils.sign(map);
                response.setHeader("token", token);
            }
            return Result.success(sAdmin);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }*/


    @PostMapping("/login")
    public Result login(@RequestBody AdminVo admin) {
        String sCaptcha = null;
        try {
            Claims claims = JWTUtil.parseJWT(admin.getJwt());
            sCaptcha = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("验证码已经过期");
        }
        try {
            if (!admin.getCaptcha().equalsIgnoreCase(sCaptcha)) {
                throw new Exception("验证码错误");
            }
            Admin dadmin = adminService.login(admin);
            dadmin.setPassword(null);

            // 返回token
            String token = JWTUtil.createJWT(dadmin);
            return Result.success(200, token, dadmin);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/searchpage")
    public Result searchForPage(Integer page, Integer size, Admin admin) {
        if (ObjectUtils.isEmpty(page)) {
            return Result.success(adminService.search(admin));
        }
        return Result.success(adminService.searchForPage(page, size, admin));
    }

    // RESTful风格API 对同一资源的访问相同  == @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addadmin")
    public Result add(@RequestBody Admin admin) {
        try {
            return Result.success(adminService.add(admin));
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(Arrays.toString(e.getStackTrace()));
            return Result.error(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/editadmin")
    public Result edit(@RequestBody Admin admin) {
        try {
            return Result.success(adminService.edit(admin));
        } catch (Exception e) {
            log.warn(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Integer id) {
        return Result.success(adminService.getById(id));
    }
}







