package com.tzj.baselib.utils;


import android.text.Html;
import android.text.Spanned;

/**
 * 转换
 */
public class UtilTo {

    /**
     * byte[]数组转换为16进制的Hex字符串
     */
    public static String toHexString(byte[] data){
        char[] hexDigits = "0123456789abcdef".toCharArray();
        StringBuilder sb = new StringBuilder(2 * data.length);
        for (byte b : data) {
            sb.append(hexDigits[(b >> 4 & 0xF)]).append(hexDigits[(b & 0xF)]);
        }
        return sb.toString();
    }
    /**
     * 16进制表示的字符串转换为字节数组。
     */
    public static byte[] toHexBytes(String s) {
        if (s==null) {
            return new byte[0];
        }
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return d;
    }

    /**
     * 转成(ASCII码)对应的字符串
     * 不如转成Hex省一半空间
     */
    public static String toASCString(byte[] src){
        StringBuilder string = new StringBuilder("");
        if (src == null) return string.toString();
        for (byte aSrc : src) {
            string.append((char) aSrc);
        }
        return string.toString();
    }
    /**
     * 字符串转成(ASCII码)对应的byte
     * 只适用于单字节的
     * 不如用Hex省一半空间
     */
    public static byte[] toASCBytes(String s){
        if (s==null) {
            return new byte[0];
        }
        return s.getBytes();
    }
    /**
     * 得到指定颜色的文字
     */
    public static Spanned getColor(String s, String color){
        s = "<font color='"+color+"'><b>"+s+"</b></font>";
        return Html.fromHtml(s);
    }

}
