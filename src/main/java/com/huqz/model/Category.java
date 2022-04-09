package com.huqz.model;

import lombok.Data;

@Data
public class Category {
    private Integer id;
    private String categoryName;
    private String shareId;
    private Boolean shared;
    private String userId;
    private String createTime;


}
