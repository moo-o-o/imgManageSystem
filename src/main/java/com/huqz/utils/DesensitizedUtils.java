package com.huqz.utils;


public class DesensitizedUtils {

    private final static String USERNAME_REGEX = "^(\\w{2})\\w*(\\w{1})$";

    private final static String EMAIL_REGEX = "(.{2}).+(.{2}@.+)";


    /**
     * 对字符串进行脱敏操作
     *
     */
    public static String desValue(String origin, String template) {
        if (origin == null) return null;
        return origin.replaceAll(template, "$1****$2");
    }

    public static String deUsername(String username) {
        return desValue(username, USERNAME_REGEX);
    }

    public static String deEmail(String mail) {
        return desValue(mail, EMAIL_REGEX);
    }

    public static void main(String[] args) {
        System.out.println(deUsername("20220411"));
        System.out.println(deEmail("1720868421@qq.com"));
    }
}
