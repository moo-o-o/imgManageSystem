package com.huqz.core;

public enum ResultCode {
    SUCCESS(200),
    FAIL(400),
    INVALID_ARGS(401),
    UNAUTHORIZED(402),
    FORBIDDEN(403),
    NOT_FOUNT(404),
    INTERNAL_SERVER_ERROR(500),

    INVALID_ACCOUNT(1000),            // 不存在的用户账号
    PASSWORD_ERROR(1001),             // 密码错误
    THE_SAME_PASSWORD(1002),          // 修改的密码和原密码相同
    INVALID_NEW_PASSWORD(1003),       // 密码长度过低/过高
    EXISTING_MAIL(1004),              // 存在的邮箱地址
    EXISTING_USERNAME(1005),          // 存在的用户名
    SEND_MAIL_FAILED(1006),           // 邮件发送失败
    EXPIRED_MAIL_CODE(1007),          // 验证码过期
    INVALID_MAIL_CODE(1008),          // 验证码错误
    ERROR_REGISTER_DATA(1009),         // 邮箱验证数据异常
    ERROR_ACCOUNT_OR_EMAIL(1010),     // 无效的邮箱或者帐号
    UNKNOWN_CATEGORY_ID(1011),            // 不存在的分类ID
    BAD_OBJECT_ID(1012),              // 无效的object_id
    UNKNOWN_IMG_ID(1013),             // 无效的图片id
    MAX_IMG_TAG(1014),                // 标签超出上限
    EXISTS_IMG_TAG(1015),             // 已存在的标签
    EXISTING_CATEGORY_NAME(1016),         // 已存在的分类
    SHARED_CATEGORY_ID(1017),             // 该分类已经分享
    NOT_EXISTED_ANY_IMG(1018),        // 该分类下无任何图片
    INVALID_SHARE_ID(1019),           // 无效的请求分享ID
    EXPIRED_SHARE(1020),              // 该分享已失效
    REPEAT_CONFIRMED(1021),           // 重复申请恢复图片访问
    INVALID_FILE(1022),               // 无效的文件
    INVALID_FILE_SUFFIX(1023);        // 无效的文件后缀





    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }



}
