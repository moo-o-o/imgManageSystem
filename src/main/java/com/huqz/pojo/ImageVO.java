package com.huqz.pojo;

import com.huqz.model.Image;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ImageVO {
    private Integer id;
    private Integer userId;
    private String url;
    private String urn;
    private String thumbUrn;
    private Integer categoryId;
    private String status;
    private String confirm;
    private Timestamp createTime;
    private List<String> tags;

    public ImageVO() {
    }

    public ImageVO(Image image) {
        this.id = image.getId();
        this.userId = image.getUserId();
        this.url = image.getUrl();
        this.urn = image.getUrn();
        this.thumbUrn = image.getThumbUrn();
        this.categoryId = image.getCategoryId();
        this.status = image.getStatus();
        this.confirm = image.getConfirm();
        this.createTime = image.getCreateTime();
    }
}
