package com.example.adminservice.service;

import com.example.adminservice.mapper.AdminMapper;
import com.example.common.domain.entity.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminMapper.selectAdminByEmail(username);
        if (ObjectUtils.isEmpty(admin)) {
            throw new UsernameNotFoundException("邮箱不存在");
        }

        return admin;
    }
}
