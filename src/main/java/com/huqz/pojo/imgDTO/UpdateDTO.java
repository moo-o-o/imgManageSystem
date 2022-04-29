package com.huqz.pojo.imgDTO;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateDTO {

    @NotEmpty
    private Integer categoryId;
    @NotNull
    private List<String> tags;
}
