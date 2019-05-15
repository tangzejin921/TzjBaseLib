package com.tzj.baselib.env.config;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tzj.baselib.env.TzjAppEnv;
import com.tzj.baselib.utils.UtilField;

import java.lang.reflect.Field;

public class TzjBuildConfig {

    public static boolean isDebug(){
        try {
            return (boolean) getValue("DEBUG");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return com.tzj.baselib.BuildConfig.DEBUG;
    }

    public static String getApplicationId() {
        try {
            return getValue("APPLICATION_ID").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return com.tzj.baselib.BuildConfig.APPLICATION_ID;
    }
    public static String getBuildType() {
        try {
            return getValue("BUILD_TYPE").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return com.tzj.baselib.BuildConfig.BUILD_TYPE;
    }
    /**
     * 获取版本号
     */
    public static String getFlavor() {
        try {
            return getValue("FLAVOR").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return com.tzj.baselib.BuildConfig.FLAVOR;
    }
    /**
     * 获取版本号
     */
    public static int getVersionCode() {
        try {
            return (int)getValue("VERSION_CODE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return com.tzj.baselib.BuildConfig.VERSION_CODE;
    }
    public static String getVersionName() {
        try {
            return getValue("VERSION_NAME").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return com.tzj.baselib.BuildConfig.VERSION_NAME;
    }

    public static Object getValue(String name) throws Exception {
        String clspath = TzjAppEnv.getAppCtx().getPackageName()+".TzjBuildConfig";
        Class<?> c = Class.forName(clspath);
        Object o = c.newInstance();
        Field field = UtilField.getField(c, name);
        return field.get(o);
    }

    /**
     * APP 的名称
     */
    public static String getAppName(){
        try {
            PackageManager packageManager = TzjAppEnv.getAppCtx().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(TzjAppEnv.getAppCtx().getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return TzjAppEnv.getAppCtx().getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
