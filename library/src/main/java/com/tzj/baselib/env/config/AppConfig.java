package com.tzj.baselib.env.config;

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

    public static boolean isAndroid(){
        return isAndroid;
    }


}
