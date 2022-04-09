package com.huqz.model;

import lombok.Data;

@Data
public class Image {
    private Integer id;
    private String userId;
    private String url;
    private String urn;
    private String thumbUrn;
    private String categoryId;
    private String tags;
    private String status;
    private String confirm;
    private String createTime;
}
