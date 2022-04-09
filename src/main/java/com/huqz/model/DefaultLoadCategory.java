package com.huqz.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DefaultLoadCategory {
    private Integer id;
    private Integer userId;
    private Integer categoryId;
    private Timestamp createTime;
}
