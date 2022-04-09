package com.huqz.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Image {
    private Integer id;
    private Integer userId;
    private String url;
    private String urn;
    private String thumbUrn;
    private Integer categoryId;
    private String status;
    private String confirm;
    private Timestamp createTime;
}
