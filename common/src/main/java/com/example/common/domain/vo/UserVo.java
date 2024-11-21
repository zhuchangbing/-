package com.example.common.domain.vo;

import com.example.common.domain.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserVo extends User {
    String address;
    String captcha;
    String jwt;
    String vercode;
}
