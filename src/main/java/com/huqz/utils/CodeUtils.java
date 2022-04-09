package com.huqz.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CodeUtils {

    private final int length = 6;

    public String genCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int num = new Random().nextInt(8) + 1;   // 1-9 不包括0
            sb.append(num);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(new CodeUtils().genCode());
    }
}
