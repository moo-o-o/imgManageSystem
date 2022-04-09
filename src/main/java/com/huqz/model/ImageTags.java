package com.huqz.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ImageTags {
    private Integer id;
    private Integer imgId;
    private Integer tagId;
    private Timestamp createTime;
    private Timestamp updateTime;

    public ImageTags setImgId(Integer imgId) {
        this.imgId = imgId;
        return this;
    }

    public ImageTags setTagId(Integer tagId) {
        this.tagId = tagId;
        return this;
    }
}
