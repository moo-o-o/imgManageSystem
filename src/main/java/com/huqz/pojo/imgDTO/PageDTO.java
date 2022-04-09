package com.huqz.pojo.imgDTO;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Data
@Validated
public class PageDTO {

    @NotEmpty
    private String pageSize;
    @NotEmpty
    private String pageNumber;
    @NotEmpty
    private Integer categoryId;
    private String tag;
}
