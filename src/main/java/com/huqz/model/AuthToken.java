package com.huqz.model;

import lombok.Data;

@Data
public class AuthToken {
    private Integer id;
    private String userId;
    private String token;
    private String osName;
    private String browserName;
    private String browserVersion;
    private String expiration;
    private String remark;
    private String createTime;
}
