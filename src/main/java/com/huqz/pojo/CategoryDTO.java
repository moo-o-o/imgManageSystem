package com.huqz.pojo;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Data
@Validated
public class CategoryDTO {

    @NotEmpty
    private String categoryName;
    @NotEmpty
    private Integer categoryId;
}
