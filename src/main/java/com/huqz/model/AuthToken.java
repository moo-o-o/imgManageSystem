package com.huqz.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AuthToken {
    private Integer id;
    private Integer userId;
    private String token;
    private String osName;
    private String browserName;
    private String browserVersion;
    private String expiration;
    private String remark;
    private Timestamp createTime;
}
