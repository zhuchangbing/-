package com.example.common.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Pic implements Serializable {
    private Integer id;
    private Integer skuId;
    private String url;
}
