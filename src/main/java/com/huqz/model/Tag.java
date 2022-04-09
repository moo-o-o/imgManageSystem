package com.huqz.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Tag {
    private Integer id;
    private String tagName;
    private Timestamp createTime;
    private Timestamp updateTime;

    public Tag setTagName(String tagName) {
        this.tagName = tagName;
        return this;
    }
}
