package com.dongsw.authority;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author dongshuangwei
 * @Name RandomStringUtil
 * @Descr 生成随机字符串
 * @date 2017年3月15日下午2:36:05
 */
public class RandomStringUtil {

    /**
     * @param passLength : 要生成多少长度的字符串
     * @param type       : 需要哪种类型
     * @return 根据传入的type来判定
     */

    // 可以根据自己需求来删减下面的代码，不要要的类型可以删掉

    // type=0：纯数字(0-9)
    // type=1：全小写字母(a-z)
    // type=2：全大写字母(A-Z)
    // type=3: 数字+小写字母
    // type=4: 数字+大写字母
    // type=5：大写字母+小写字母
    // type=6：数字+大写字母+小写字母
    // type=7：固定长度33位：根据UUID拿到的随机字符串，去掉了四个"-"(相当于长度33位的小写字母加数字)
    public static String getRandomCode(int passLength, int type, boolean seed) {
        Random r = new Random();
        if (seed) {
            r.setSeed(new Date().getTime());
        }
        StringBuilder buffer = initddinitsb(type, r);
        return randomStr(passLength, type, buffer, r);

    }

    private static StringBuilder initddinitsb(int type, Random r) {

        String result;
        String num = "0123456789";
        String strL = "abcdefghijklmnopqrstuvwxyz";
        String strU = strL.toUpperCase();

        String[] items = new String[]{
                num,
                strL,
                strU,
                num+strL,
                num+strU,
                strL+strU,
                strL+strU+num};

        if(type>=0 && type<=6){
            result = items[type];
        }else{
            result = UUID.randomUUID().toString();
        }
        return new StringBuilder(result);

    }

    private static String randomStr(int l, int type, StringBuilder buffer, Random r) {
        int passLength = l;
        StringBuilder sb = new StringBuilder();

        if (type == 6) {
            sb.append(buffer.charAt(r.nextInt(buffer.length() - 10)));
            passLength -= 1;
        }
        if (type > 6 && type < 0) {
            sb.append(buffer.subSequence(0, 8))
                    .append(buffer.subSequence(9, 13))
                    .append(buffer.subSequence(14, 18))
                    .append(buffer.subSequence(19, 23))
                    .append(buffer.subSequence(24, 25));
        }
        if (type >= 0 && type <= 6) {
            int range = buffer.length();
            for (int i = 0; i < passLength; ++i) {
                sb.append(buffer.charAt(r.nextInt(range)));
            }
        }

        return sb.toString();
    }
}
