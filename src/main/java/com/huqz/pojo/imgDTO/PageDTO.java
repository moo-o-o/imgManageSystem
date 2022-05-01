package com.huqz.pojo.imgDTO;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Data
@Validated
public class PageDTO {

    @NotEmpty
    private Integer pageSize;
    @NotEmpty
    private Integer pageNumber;
    private Integer categoryId;
    private String tag;
    private String sort;
    private Boolean diy;
}
