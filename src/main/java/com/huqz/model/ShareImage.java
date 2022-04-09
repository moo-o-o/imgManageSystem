package com.huqz.model;

import lombok.Data;

@Data
public class ShareImage {
    private Integer id;
    private String imgId;
    private String shareListId;
    private String urn;
    private Boolean status;
    private String createTime;

}
