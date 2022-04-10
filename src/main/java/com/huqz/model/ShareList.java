package com.huqz.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ShareList {
    private Integer id;
    private Integer userId;
    private Integer categoryId;
    private String type;
    private Boolean status;
    private Timestamp createTime;
}
