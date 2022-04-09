package com.huqz.pojo.userDTO;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegDTO {
    @NotEmpty
    @Pattern(
            regexp = "^[a-zA-Z0-9\\_\\-]{4,16}$",
            message = "用户名不合法"
    )
    private String username;

    @NotEmpty
    @Pattern(
            regexp = "^[a-zA-Z0-9\\_\\-\\.\\+]{6,20}$",
            message = "密码不合法"
    )
    private String password;

    @NotEmpty
    @Pattern(
            regexp = "^([\\.a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{1,4}){1,2})$",
            message = "邮箱不合法"
    )
    private String mail;

    @NotEmpty
    @NotEmpty(message = "验证码不能为空")
    private String mailCode;

    @NotEmpty
    @NotEmpty(message = "token不能为空")
    private String token;

    @Size(max = 32)
    private String nickname;
}
