package com.tzj.baselib.utils;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 加密/转换 相关
 */
public class UtilCipher {

    /**
     * 产生随机字符串
     */
    public static String random(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(62);// [0,62)
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 返回可逆算法DES的密钥
     *
     * @param key 前8字节将被用来生成密钥。
     */
    public static Key getDESKey(byte[] key) throws Exception {
        if (key==null) {
            key = random(8).getBytes();
        }
        DESKeySpec des = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(des);
    }


    /**
     * 根据指定的密钥及算法对指定字符串进行可逆加密。
     * @return 加密后的结果将由byte[]数组转换为16进制表示的数组。如果加密过程失败，将返回null。
     */
    public static String encrypt(String data, Key key, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return UtilTo.toHexString(cipher.doFinal(data.getBytes("utf-8")));
    }

    /**
     * 根据指定的密钥及算法，将字符串进行解密。
     * @return 解密后的结果。它由解密后的byte[]重新创建为String对象。如果解密失败，将返回null。
     */
    public static String decrypt(String data, Key key, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        String result = new String(cipher.doFinal(UtilTo.toHexBytes(data)), "utf-8");
        return result;
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
     * SHA加密
     */
    public static String shaDigst(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] b = md.digest(str.getBytes());
            return new String(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return str;
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
