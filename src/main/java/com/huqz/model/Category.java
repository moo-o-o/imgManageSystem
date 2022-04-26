package com.huqz.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Category {
    private Integer id;
    private String categoryName;
    private String shareId;
    private Boolean shared;
    private Integer userId;
    private Timestamp createTime;


}
