package com.huqz.core;

import lombok.Data;

@Data
public class Result {
    private Integer code;
    private String message;
    private Object data;
    private String token;

    public Result setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

    public Result setToken(String token) {
        this.token = token;
        return this;
    }
}
