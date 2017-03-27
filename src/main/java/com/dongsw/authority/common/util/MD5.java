package com.dongsw.authority.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class MD5 {

    private static final Logger logger = LoggerFactory.getLogger(MD5.class);


    private MD5() {
    }

    /**
     * 获取加密后的字符串
     * @return
     */
    public static String stringMD5(String pw) {
        try {

            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 输入的字符串转换成字节数组
            byte[] inputByteArray = pw.getBytes();
            // inputByteArray是输入字符串转换得到的字节数组
            messageDigest.update(inputByteArray);
            // 转换并返回结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 字符数组转换成字符串返回
            return byteArrayToHex(resultByteArray);
        } catch (Exception  e) {
            logger.error("md5转化异常", e);
            return null;
        }
    }

    public static String byteArrayToHex(byte[] byteArray) {

        // 首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'a','b','c','d','e','f' };
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray =new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b& 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }

    public static String getPassword(String pw){
        String pass = stringMD5(pw);
        if(pass!=null&&pass.length() == 32){
            String[] a = new String[pass.length()/8];
            for (int i = 0;i<(a.length);i++){
                a[i]=pass.substring(i*8,i*8+7);
            }
            StringBuilder b = new StringBuilder();
            b.append(a[2]);
            b.append(a[1]);
            b.append(a[3]);
            b.append(a[0]);

            return b.toString();
        }
        return pass;
    }
}


