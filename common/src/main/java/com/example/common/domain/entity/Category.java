package com.example.common.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Category {
    private Integer id;
    private String name;
    private Integer fathId;
    private String dscp;
    private String recom;        // 1 推荐  1 不推荐
    private Integer status;     //  0正常   1 不正常

    private List<Category> children;
    private String fathName;

}
