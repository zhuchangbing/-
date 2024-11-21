package com.example.userservice.service;

import com.example.common.domain.entity.User;
import com.example.userservice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User suser = userMapper.selectUserByEmail(username);
        if (ObjectUtils.isEmpty(suser)) {
            throw new UsernameNotFoundException("邮箱不存在");
        }
        return suser;
    }
}
