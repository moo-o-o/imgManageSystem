package com.huqz.utils;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

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

    public String genShareCode() {
        String s = UUID.randomUUID().toString().replaceAll("-", "");
        return s.substring(28);
    }

    public static void main(String[] args) {
        System.out.println(new CodeUtils().genCode());
    }
}
