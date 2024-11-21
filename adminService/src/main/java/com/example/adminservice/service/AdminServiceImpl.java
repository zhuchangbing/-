package com.example.adminservice.service;

import com.example.adminservice.mapper.AdminMapper;
import com.example.common.domain.entity.Admin;
import com.example.common.domain.vo.AdminVo;
import com.example.common.service.AdminService;
import com.example.common.utils.MD5Util;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminMapper adminMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Admin getAdminByEmail(String email) {
        return adminMapper.selectAdminByEmail(email);
    }

    @Override
    public Admin login(AdminVo admin) throws Exception {
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken
                (admin.getUsername(), admin.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            Admin loginAdmin = (Admin) authentication.getPrincipal();
            return loginAdmin;
        } catch (BadCredentialsException e) {
            throw new Exception("邮箱或密码错误");
        }
    }


    @Override
    public int add(Admin admin) throws Exception {
        // 邮箱不可重复
        Admin sAdmin = adminMapper.selectAdminByEmail(admin.getEmail());
        if (!ObjectUtils.isEmpty(sAdmin)) {
            throw new Exception("用户名不可用");
        }
        // 保存到数据库
        String salt = UUID.randomUUID().toString().substring(0, 8);
        admin.setSalt(salt);
        // 加密   双重随机盐加密
        admin.setPassword(MD5Util.getDBMD5(admin.getPassword(), salt));
        return adminMapper.insert(admin);
    }

    @Override
    public int edit(Admin admin) throws Exception {
        Admin searchAdmin = adminMapper.selectAdminByEmail(admin.getEmail());
        if (ObjectUtils.isEmpty(searchAdmin)) {
            throw new Exception("无效的ID");
        }
        // 如果修改密码
        if (!ObjectUtils.isEmpty(admin.getPassword())) {
            // 在随机一个盐 重新加密
            admin.setSalt(UUID.randomUUID().toString().substring(0, 8));
            admin.setPassword(MD5Util.getDBMD5(admin.getPassword(), admin.getSalt()));
        }
        // 写入数据库
        return adminMapper.update(admin);
    }

    @Override
    public List<Admin> search(Admin admin) {
        return adminMapper.select(admin);
    }

    @Override
    public PageInfo searchForPage(Integer page, Integer size, Admin admin) {
        PageHelper.startPage(page, size);
        List<Admin> admins = adminMapper.select(admin);
        return new PageInfo(admins);
    }

    @Override
    public Admin getById(Integer id) {
        return adminMapper.selectAdminById(id);
    }


}
