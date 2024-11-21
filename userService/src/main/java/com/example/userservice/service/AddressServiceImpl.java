package com.example.userservice.service;

import com.example.common.domain.entity.Address;
import com.example.common.service.AddressService;
import com.example.userservice.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressMapper addressMapper;

    @Override
    public List<Address> getUserAllAddress(Integer userId) {
        return addressMapper.selectUserAddress(userId);
    }

    /**
     * 编辑用户的地址
     *
     * @param address
     * @return
     */
    @Override
    public int editUserAddress(Address address) throws Exception {
        Address s = addressMapper.getAddressById(address.getId());
        if (ObjectUtils.isEmpty(s)) {
            throw new Exception("待修改地址不存在，请刷新页面~");
        }
        return addressMapper.updateUserAddress(address);
    }

    @Override
    public int addUserAddress(Address address) throws Exception {
        Address s = addressMapper.getAddress(address);
        if (!ObjectUtils.isEmpty(s)) {
            throw new Exception("该地址已存在");
        }
        return addressMapper.insertNewAddress(address);
    }

    @Override
    public int deleteUserAddress(Integer id) throws Exception {
        Address s = addressMapper.getAddressById(id);
        if (ObjectUtils.isEmpty(s)) {
            throw new Exception("该地址不存在，请刷新页面~");
        }
        return addressMapper.deleteUselessAddress(id);
    }

    @Override
    public int updateDefaultAddress(Integer id) throws Exception {
        Address s = addressMapper.getAddressById(id);
        if (ObjectUtils.isEmpty(s)) {
            throw new Exception("该地址不存在 请刷新页面~");
        }
        Integer defaultId = addressMapper.selectDefaultAddressId();
        if (!ObjectUtils.isEmpty(defaultId)) {
            addressMapper.updateUserDefaultAddress(defaultId, 0);
        }
        return addressMapper.updateUserDefaultAddress(id, 1);
    }
}
