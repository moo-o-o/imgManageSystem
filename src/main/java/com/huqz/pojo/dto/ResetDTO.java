package com.huqz.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class ResetDTO {
    @NotEmpty
    private String token;

    @NotEmpty
    private String mailCode;

    @NotEmpty
    @Pattern(
            regexp = "^[a-zA-Z0-9\\_\\-\\.\\+]{6,20}$",
            message = "密码不合法"
    )
    private String password;
}
