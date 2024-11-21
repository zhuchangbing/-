package com.example.common.service;

import com.example.common.domain.entity.Admin;
import com.example.common.domain.vo.AdminVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AdminService {
    Admin getAdminByEmail(String email);

    Admin login(AdminVo admin) throws Exception;

    int add(Admin admin) throws Exception;

    int edit(Admin admin) throws Exception;

    List<Admin> search(Admin admin);

    PageInfo searchForPage(Integer page, Integer size, Admin admin);

    Admin getById(Integer id);

}
