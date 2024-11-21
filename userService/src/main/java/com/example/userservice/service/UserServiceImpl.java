package com.example.userservice.service;

import com.example.common.domain.entity.Address;
import com.example.common.domain.entity.User;
import com.example.common.domain.vo.UserVo;
import com.example.common.service.UserService;
import com.example.common.utils.JWTUtil;
import com.example.userservice.mapper.AddressMapper;
import com.example.userservice.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Service
@DubboService   // 提供微服务
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public PageInfo getUserList(Integer page, Integer size, UserVo user) {
        PageHelper.startPage(page, size);
        List<User> list = userMapper.getUserByCondition(user);
        return new PageInfo(list);
    }

    @Override
    public int updateUserInfo(User user) throws Exception {
        User sUser = userMapper.getUserById(user.getId());
        if (ObjectUtils.isEmpty(sUser)) {
            throw new Exception("该用户不存在");
        }
        return userMapper.updateUserInfo(user);
    }

    @Override
    public int insertUser(User user) throws Exception {
        User sUser = userMapper.selectUserByEmail(user.getEmail());
        if (ObjectUtils.isEmpty(sUser)) {
            String salt = UUID.randomUUID().toString().substring(0, 6);
            user.setSalt(salt);
            return userMapper.insertNewUser(user);
        }
        throw new Exception("该邮箱已被注册");
    }

    @Override
    public int insertAddress(Address address) throws Exception {
        Address sAddress = addressMapper.getAddress(address);
        if (ObjectUtils.isEmpty(sAddress)) {
            return addressMapper.insertNewAddress(address);
        }
        throw new Exception("该地址已经存在");
    }

    @Override
    public int updateAddress(Address address) throws Exception {
        Address sAddress = addressMapper.getAddressById(address.getId());
        if (ObjectUtils.isEmpty(sAddress)) {
            throw new Exception("该地址不存在");
        }
        return addressMapper.updateUserAddress(address);
    }

    @Override
    public int deleteAddress(Integer id) throws Exception {
        Address sAddress = addressMapper.getAddressById(id);
        if (ObjectUtils.isEmpty(sAddress)) {
            throw new Exception("该地址不存在");
        }
        return addressMapper.deleteUselessAddress(id);
    }

    @Override
    public List<Address> getUserAddr(Integer userId) {
        return addressMapper.selectUserAddress(userId);
    }

    @Override
    public User login(UserVo user) throws Exception {
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken
                (user.getUsername(), user.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            User loginUser = (User) authentication.getPrincipal();
            return loginUser;
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("邮箱或密码错误");
        }
    }

    @Override
    public int reg(User user) throws Exception {
        User sUser = userMapper.selectUserByEmail(user.getEmail());
        if (!ObjectUtils.isEmpty(sUser)) {
            throw new Exception("该邮箱已被注册");
        }
        return userMapper.insertNewUser(user);
    }


}
