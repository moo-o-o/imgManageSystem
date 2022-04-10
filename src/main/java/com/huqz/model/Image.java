package com.huqz.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Image {
    private Integer id;
    private Integer userId;
    private String url;
    @JsonIgnore
    private String urn;
    private String thumbUrn;
    private Integer categoryId;
    private String status;
    private String confirm;
    private Timestamp createTime;

    // 用于返回前端展示标签
    @TableField(exist = false)
    private List<String> tags;
}
