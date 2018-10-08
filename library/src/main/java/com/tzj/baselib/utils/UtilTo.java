package com.tzj.baselib.utils;

/**
 * 转换
 * 如单位，保留小数点
 */
public class UtilTo {

    /**
     * 保留 2位小数
     */
    public static String format(double d){
        String format = String .format("%.2f",d);
//        format = new DecimalFormat("#.00").format(d);// '.3' 会转成  .3
        return format;
    }



}
