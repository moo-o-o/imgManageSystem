package com.huqz.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ApiKey {
    private Integer id;
    private Integer userId;
    private Integer categoryId;
    private String token;
    private Timestamp createTime;
}
