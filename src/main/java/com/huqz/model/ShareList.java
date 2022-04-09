package com.huqz.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ShareList {
    private Integer id;
    private Integer userId;
    private Boolean type;
    private String status;
    private Integer categoryId;
    private Timestamp createTime;
}
