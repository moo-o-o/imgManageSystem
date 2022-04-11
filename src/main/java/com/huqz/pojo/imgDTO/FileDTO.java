package com.huqz.pojo.imgDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class FileDTO {
    private String filename;
    @JsonIgnore
    private String uploadTime;
    private String path;
    private Boolean flag;
    @JsonIgnore
    private String absPath;
}
