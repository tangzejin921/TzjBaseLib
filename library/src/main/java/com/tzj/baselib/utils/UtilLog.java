package com.tzj.baselib.utils;

import android.util.Log;

import com.tzj.baselib.env.config.TzjAppConfig;
import com.tzj.baselib.env.config.TzjBuildConfig;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * log 打印
 */
public class UtilLog {
    public static boolean debug= TzjBuildConfig.isDebug();

    /**
     * 异常
     */
    public static void err(Exception e) {
        if (debug) {
            throw new RuntimeException(e);
        }else {
            if (e!=null) {
                UtilLog.e("error",getStackTraceString(e));
            }
        }
    }

    //===================================
    public static void test(Object... objs){
        e("test", objs);
    }
    //====================================================
    // log可以直接打 map,list,
    //	可以直接+“”进行转换
    //=====================================================
    private static void i(String tag,Object... objs){
        if (!debug) {
            return;
        }
        if (objs==null) {
            logi(tag, "null");
            return;
        }
        String temp ="";
        for (Object obj : objs) {
            temp += obj + "\n";
        }
        logi(tag, temp);
    }

    private static void e(String tag,Object... objs){
        if (!debug) {
            return;
        }
        if (objs==null) {
            loge(tag, "null");
            return;
        }
        String temp ="";
        for (Object obj : objs) {
            temp += obj + "\n";
        }
        loge(tag, temp);
    }


    /**
     * 分断打印超长的文字
     */
    private static final int length = 2000;
    public static void iL(String tag,String str){
        try {
            int index = 0;
            String substr;
            while (str.length()>(index+1)*length){
                substr = str.substring(index*length, (index+1)*length);
                logi(tag,substr);
                index++;
            }
            substr = str.substring(index*length, str.length());
            logi(tag,substr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 异常转字符串(来自Log类)
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }


    private static void logi(String tag,String substr){
        if (TzjAppConfig.isAndroid()){
            Log.i(tag,substr);
        }else {
            System.out.println(substr);
        }
    }


    private static void loge(String tag,String substr){
        if (TzjAppConfig.isAndroid()){
            Log.e(tag,substr);
        }else {
            System.err.println(substr);
        }
    }

}
