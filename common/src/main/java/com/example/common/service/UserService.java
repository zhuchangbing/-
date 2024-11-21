package com.example.common.service;

import com.example.common.domain.entity.Address;
import com.example.common.domain.entity.User;
import com.example.common.domain.vo.UserVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {
    User getUserById(Integer id);

    PageInfo getUserList(Integer page, Integer size, UserVo user);

    int updateUserInfo(User user) throws Exception;

    int insertUser(User user) throws Exception;

    //  地址 address
    int insertAddress(Address address) throws Exception;

    int updateAddress(Address address) throws Exception;

    int deleteAddress(Integer id) throws Exception;

    List<Address> getUserAddr(Integer userId);

    User login(UserVo user) throws Exception;

    int reg(User user) throws Exception;
}
