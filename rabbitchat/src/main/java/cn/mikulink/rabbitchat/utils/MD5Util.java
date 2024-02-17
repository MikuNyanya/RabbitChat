package cn.mikulink.rabbitchat.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MikuLink created in 2024/2/18 3:50
 * For the Reisen
 *
 * md5加密算法
 */
public class MD5Util {

    //md5加密
    public static String md5(String str) throws NoSuchAlgorithmException {
        return md5(str,"");
    }

    //加盐的md5
    public static String md5(String str, String salt) throws NoSuchAlgorithmException {
        // 创建MD5消息摘要对象
        MessageDigest md = MessageDigest.getInstance("MD5");

        // 计算消息的摘要
        byte[] digest = md.digest((str + salt).getBytes());

        // 将摘要转换为十六进制字符串
        return bytesToHex(digest);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
