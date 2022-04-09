package com.huqz.core;

public class ResultGenerator {

    public static Result ok() {
        return new Result()
                .setCode(ResultCode.SUCCESS.getCode())
                .setMessage("操作成功！");
    }

    public static Result ok(Object data) {
        return ok().setData(data);
    }

    public static Result fail() {
        return new Result()
                .setCode(ResultCode.FAIL.getCode())
                .setMessage("操作失败!");
    }

    public static Result fail(ResultCode code, String msg) {
        return new Result()
                .setCode(code.getCode())
                .setMessage(msg);
    }

    public static Result token(String message, String token) {
        return new Result()
                .setCode(ResultCode.SUCCESS.getCode())
                .setMessage(message)
                .setToken(token);
    }
}
