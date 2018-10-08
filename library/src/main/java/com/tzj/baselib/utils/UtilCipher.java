package com.tzj.baselib.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * 加密/转换 相关
 */
public class UtilCipher {

    public static class AES{

    }

    /**
     * Base64
     */
    public static class Base64{
        /**
         * Base64 编码
         */
        public static String encode(byte[] input) {
            try {
                return new String(android.util.Base64.encode(input, android.util.Base64.NO_WRAP), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Base64 转换失败:"+e.getMessage());
            }
        }
        /**
         * Base64 解码
         */
        public static byte[] decode(String str) {
            try {
                return android.util.Base64.decode(str.getBytes("UTF-8"), android.util.Base64.NO_WRAP);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Base64 转换失败:"+e.getMessage());
            }
        }
    }

    /**
     * md5
     */
    public static String MD5(String str){
        try {
            byte[] btInput = str.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aMd : md) {
                int val = ((int) aMd) & 0xff;
                if (val < 16)
                    sb.append("0");
                sb.append(Integer.toHexString(val));
            }
            return sb.toString().toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException("MD5 操作失败:"+e.getMessage());
        }
    }

    /**
     * 转 hex
     */
    public static String toHex(byte[] bytes) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            int j = bytes.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = bytes[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            throw new RuntimeException("转 Hex 失败"+e.getMessage());
        }
    }
    /**
     * 保留 前3后4 脱敏
     */
    public static String transformation(String s){
        if (s==null){
            return "";
        }
        if (s.length()<=7){
            return s;
        }
        String star="";
        for (int i=0;i<s.length()-7;i++){
            star+="*";
        }
        s = s.substring(0,3)+star+s.substring(s.length()-4,s.length());
        return s;
    }
}
