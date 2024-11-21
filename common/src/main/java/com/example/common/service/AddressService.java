package com.example.common.service;

import com.example.common.domain.entity.Address;

import java.util.List;

public interface AddressService {
    // 查询用户所有的地址
    List<Address> getUserAllAddress(Integer userId);

    // 编辑用户的地址
    int editUserAddress(Address address) throws Exception;

    // 用户添加新的地址
    int addUserAddress(Address address) throws Exception;

    // 用户删除地址
    int deleteUserAddress(Integer id) throws Exception;

    // 修改默认地址
    int updateDefaultAddress(Integer id) throws Exception;
}
