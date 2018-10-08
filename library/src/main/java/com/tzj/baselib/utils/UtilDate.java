package com.tzj.baselib.utils;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间
 */
public class UtilDate {
    public static final String YMD = "yyyy-MM-dd";
    public static final String HM = "HH:mm";
    public static final String HMS = "HH:mm:ss";
    public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 按格式把string转date
     */
    public static Date toDate(String str,String from){
        SimpleDateFormat format = new SimpleDateFormat(from);
        try {
            return format.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }
    /**
     * 按格式把date转string
     */
    public static String toStr(Date date, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 日期格式  yyyy-MM-dd
     */
    public static boolean isYMD(String str){
        return checkFormat(str,YMD);
    }
    /**
     * 日期格式  hh:mm:ss
     */
    public static boolean isHMS(String str){
        return checkFormat(str,HMS);
    }
    /**
     * 日期格式  yyyy-MM-dd hh:mm:ss
     */
    public static boolean isStandard(String str){
        return checkFormat(str,YMD_HMS);
    }
    public static boolean checkFormat(String str,String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            dateFormat.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
