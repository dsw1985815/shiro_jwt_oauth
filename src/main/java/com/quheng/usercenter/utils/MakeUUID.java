package com.quheng.usercenter.utils;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/2/20
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */

import java.util.UUID;

public class MakeUUID {

    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        return s;
    }

    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = getUUID();
        }
        return ss;
    }
}