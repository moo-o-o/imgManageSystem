package com.huqz.pojo;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class CategoryDTO {

    @NotEmpty
    @NotNull
    private String categoryName;
    @NotEmpty
    @NotNull
    @Min(value = 0)
    private Integer categoryId;
}
