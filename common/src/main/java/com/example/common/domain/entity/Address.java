package com.example.common.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Address {
    Integer id;
    Integer userId;  //用户id
    String province;
    String city;
    String area;
    String detailAddr;
    Integer isSelected;
    String contact;
    String phone;
}
