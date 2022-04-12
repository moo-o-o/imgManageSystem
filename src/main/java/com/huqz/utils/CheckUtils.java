package com.huqz.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {

    public static boolean checkMail(String email) {
        String str = "^([a-zA-Z0-9]*[-_]?[.a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean checkPassword(String password) {
        String str = "^[a-zA-Z0-9_-]{4,16}$";
        Pattern p = Pattern.compile(str);
        Matcher matcher = p.matcher(password);
        return matcher.matches();
    }

    public static boolean checkUsername(String username) {
        String str = "^[a-zA-Z0-9_-]{4,16}$";
        Pattern p = Pattern.compile(str);
        Matcher matcher = p.matcher(username);
        return matcher.matches();
    }

    public static void main(String[] args) {
        System.out.println(checkMail("xx.xx@qq.com"));
        System.out.println(checkPassword("123-44_009ad"));
    }

}
