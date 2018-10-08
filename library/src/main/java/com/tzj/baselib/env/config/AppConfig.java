package com.tzj.baselib.env.config;

import android.content.Context;

import com.tzj.baselib.R;
import com.tzj.baselib.env.AppEnv;

/**
 * 静态配置
 */
public class AppConfig {
    /**
     * android平台
     */
    private static boolean isAndroid = false;
    static {
        String property = System.getProperty("http.agent");
        if (property != null && property.contains("Android")) {
            isAndroid = true;
        }
    }

    private static Context getAppContext() {
        return AppEnv.getAppCtx();
    }

    public static boolean isAndroid(){
        return isAndroid;
    }

    /**
     * 获取产品ID
     *
     * @return productId
     */
    public static String getProductId() {
        return getAppContext().getString(R.string.productId);
    }

    /**
     * 获取医院编码
     *
     * @return hospitalCode
     */
    public static String getHosCode() {
        return getAppContext().getString(R.string.hosCode);
    }

    public static String getHosName() {
        return getAppContext().getString(R.string.hosName);
    }

    public static String getAreaCode() {
        return getAppContext().getString(R.string.areaCode);
    }

    public static String getAliYunKey() {
        return getAppContext().getString(R.string.aliyun_key);
    }

    public static String getAliYunSecret() {
        return getAppContext().getString(R.string.aliyun_secret);
    }
    public static String getSVersion(){
        return "1.00.01";
    }
    public static String getPltID(){
        return "02";
    }

}
