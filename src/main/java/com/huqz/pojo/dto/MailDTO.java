package com.huqz.pojo.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@Validated
public class MailDTO implements Serializable {
    @NotEmpty
    @Pattern(
            regexp = "^[a-zA-Z0-9\\_\\-]{4,16}$",
            message = "用户名不合法")
    private String username;

    @NotEmpty
    @Pattern(
            regexp = "^([a-zA-Z0-9]+[-|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[-|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$",
            message = "邮箱不合法")
    private String mail;

    private String nickname;

    private String mailCode;

}
