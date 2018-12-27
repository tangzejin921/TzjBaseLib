package com.tzj.baselib.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间
 * HH/24h
 * hh/12h
 */
public class UtilDate {
    public static final String YMD = "yyyy-MM-dd";
    public static final String HM = "HH:mm";
    public static final String HMS = "HH:mm:ss";
    public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";


    /**
     * 当前时间
     */
    public static String currentDate() {
        return currentDate(null);
    }

    /**
     * 当前时间
     */
    public static String currentDate(String from) {
        if (from == null) {
            from = YMD_HMS;
        }
        return toStr(new Date(), from);
    }

    /**
     * 按格式把string转date
     */
    public static Date toDate(String str, String from) {
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
    public static String toStr(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 日期格式  yyyy-MM-dd
     */
    public static boolean isYMD(String str) {
        return checkFormat(str, YMD);
    }

    /**
     * 日期格式  hh:mm:ss
     */
    public static boolean isHMS(String str) {
        return checkFormat(str, HMS);
    }

    /**
     * 日期格式  yyyy-MM-dd hh:mm:ss
     */
    public static boolean isStandard(String str) {
        return checkFormat(str, YMD_HMS);
    }

    /**
     * 验证时间格式
     */
    public static boolean checkFormat(String str, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            dateFormat.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 是上午
     */
    public static boolean isAM(Date date) {
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        return Calendar.AM == c.get(Calendar.AM_PM);
    }

    /**
     * 偏移(单位分钟)
     */
    public Date getDateByOffset(Date date, int offset) {
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.add(Calendar.MINUTE, offset);
        return c.getTime();
    }

    /**
     * 偏移
     */
    public Date getDateByOffset(Date date, int field, int offset) {
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.add(field, offset);
        return c.getTime();
    }

    /**
     * 相差分钟数
     */
    public static int getOffectMinute(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(d1.getTime());
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(d2.getTime());
        int m1 = c1.get(Calendar.MINUTE);
        int m2 = c2.get(Calendar.MINUTE);
        return m1 - m2;
    }


    /**
     * 获取本周一
     */
    public static String getFirstDayOfWeek() {
        return getDayOfWeek(Calendar.MONDAY);
    }

    /**
     * 获取本周日
     */
    public static String getLastDayOfWeek(String format) {
        return getDayOfWeek(Calendar.SUNDAY);
    }

    /**
     * 获取本周的某一天
     */
    public static String getDayOfWeek(int calendarField) {
        Calendar c = new GregorianCalendar();
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(YMD);
        int week = c.get(Calendar.DAY_OF_WEEK);
        if (week == calendarField) {
            return mSimpleDateFormat.format(c.getTime());
        } else {
            int offectDay = calendarField - week;
            if (calendarField == Calendar.SUNDAY) {
                offectDay = 7 - Math.abs(offectDay);
            }
            c.add(Calendar.DATE, offectDay);
            return mSimpleDateFormat.format(c.getTime());
        }
    }


    /**
     * 获取本月第一天
     */
    public static String getFirstDayOfMonth() {
        Calendar c = new GregorianCalendar();
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(YMD);
        c.set(GregorianCalendar.DAY_OF_MONTH, 1);
        return mSimpleDateFormat.format(c.getTime());
    }

    /**
     * 获取本月最后一天
     */
    public static String getLastDayOfMonth(String format) {
        Calendar c = new GregorianCalendar();
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(YMD);
        c.set(GregorianCalendar.DATE, 1);
        c.roll(Calendar.DATE, -1);
        return mSimpleDateFormat.format(c.getTime());
    }
    /**
     * 一个月中的第几天
     */
    public static int getDayOfMon(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    /**
     * 一周中的第几天
     */
    public static int getDayofWeek(Date date){
        Calendar monday = Calendar.getInstance();
        monday.setTime(date);
        return monday.get(Calendar.DAY_OF_WEEK) - 1;
    }
    /**
     * 获取表示当前日期的0点时间毫秒数
     */
    public static long getFirstTimeOfDay() {
        String currentDate = currentDate(YMD);
        return toDate(currentDate + " 00:00:00", YMD_HMS).getTime();
    }

    /**
     * 获取表示当前日期24点时间毫秒数
     */
    public static long getLastTimeOfDay() {
        String currentDate = currentDate(YMD);
        return toDate(currentDate + " 24:00:00", YMD_HMS).getTime();
    }

    /**
     * 判断是否是闰年
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 400 != 0) || year % 400 == 0;
    }

    /**
     * 相对当前时间的描述.
     */
    public static String desc(Date date) {
        int offectMinute = getOffectMinute(new Date(), date);
        int s = 1000 * 60;
        int m = s * 60;
        int h = m * 60;
        int d = h * 24;
        int w = d * 7;
        int M = d * 30;
        int y = d * 365;

        int abs = Math.abs(offectMinute);
        String str = "刚刚";
        if (s < abs && abs < m) {
            return str;
        } else if (m < abs && abs < h) {
            str = abs / m + "分钟";
        } else if (h < abs && abs < d) {
            str = abs / h + "小时";
        } else if (d < abs && abs < w) {
            str = abs / d + "天";
        } else if (w < abs && abs < M) {
            str = abs / w + "周";
        } else if (M < abs && abs < y) {
            str = abs / M + "月";
        } else if (y < abs) {
            str = abs / y + "年";
        }
        if (offectMinute > 0) {
            str += "前";
        } else {
            str += "后";
        }
        return str;
    }
}
