package com.huqz.utils;

import java.util.Date;
import java.util.Stack;

public class UrnUtils {
    private static final char[] array = { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g','h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm', '8', '5', '2', '7', '3', '6', '4', '0', '9', '1', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '+', '-' };
    private static Integer radix = 64;

    public static String encode(double number) {
        double rest = number;
        // 创建栈
        Stack<Character> stack = new Stack<Character>();
        StringBuilder result = new StringBuilder(0);
        while (rest >= 1) {
            // 进栈,
            // 也可以使用(rest - (rest / 64) * 64)作为求余算法
            stack.add(array[new Double(rest % radix).intValue()]);
            rest = rest / 64;
        }
        for (; !stack.isEmpty();) {
            // 出栈
            result.append(stack.pop());
        }
        return result.toString();
    }

    public static String genUrn() {
        return encode(new Date().getTime());
    }

    public static void main(String[] args) {
        System.out.println(encode(new Date().getTime()));
    }
}
