package com.huqz.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ShareImage {
    private Integer id;
    private Integer imgId;
    private Integer shareListId;
    private String urn;
    private Boolean status;
    private Timestamp createTime;

}
