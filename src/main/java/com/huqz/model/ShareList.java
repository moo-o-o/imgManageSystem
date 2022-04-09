package com.huqz.model;

import lombok.Data;

@Data
public class ShareList {
    private Integer id;
    private String userId;
    private Boolean type;
    private String status;
    private String categoryId;
    private String createTime;
}
