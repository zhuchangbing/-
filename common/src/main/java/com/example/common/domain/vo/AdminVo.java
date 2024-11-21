package com.example.common.domain.vo;

import com.example.common.domain.entity.Admin;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AdminVo extends Admin {
    Integer savepwd;
    String jwt;
    String captcha;
}
