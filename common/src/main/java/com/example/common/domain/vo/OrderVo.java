package com.example.common.domain.vo;

import com.example.common.domain.entity.Address;
import com.example.common.domain.entity.Order;
import com.example.common.domain.entity.Sku;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderVo extends Order {
    private List<Sku> skus;
    private Address address;
    private String payPassword;

    public String getDetailUserInfo() {
        return address.getProvince() + "-" + address.getCity()
                + "-" + address.getArea() + "-" + address.getDetailAddr() + "-" +
                address.getContact() + "-" + address.getPhone();
    }
}
