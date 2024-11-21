package com.example.common.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Option {
    private Integer id;
    private Integer specId;
    private String value;
}
