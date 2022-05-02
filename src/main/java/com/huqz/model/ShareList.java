package com.huqz.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ShareList {
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    private Integer userId;
    private Integer categoryId;
    private String type;
    private Boolean status;
    private String code;
    private String description;
    private Timestamp expireTime;
    private Timestamp createTime;
}
