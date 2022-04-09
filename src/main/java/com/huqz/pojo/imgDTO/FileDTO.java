package com.huqz.pojo.imgDTO;

import lombok.Data;

@Data
public class FileDTO {
    private String filename;
    private String uploadTime;
    private String path;
    private Boolean flag;
    private String absPath;
}
