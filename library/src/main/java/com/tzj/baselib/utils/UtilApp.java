package com.tzj.baselib.utils;


import android.content.Context;
import android.graphics.Point;
import android.os.Looper;
import android.view.WindowManager;

import com.tzj.baselib.env.AppEnv;


/**
 * app 相关
 */
public class UtilApp {

    public static void show(int res){
        show(AppEnv.getAppCtx().getResources().getString(res));
    }
    public static void show(final CharSequence c){
        if (Looper.myLooper() == Looper.getMainLooper()){
            AppEnv.getToast().setText(c);
            AppEnv.getToast().show();
        }else{
            AppEnv.post(new Runnable() {
                @Override
                public void run() {
                    AppEnv.getToast().setText(c);
                    AppEnv.getToast().show();
                }
            });
        }
    }
    /**
     * 获取屏幕分辨率
     */
    public static Point getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        if(windowManager!=null){
            windowManager.getDefaultDisplay().getSize(outSize);
        }
        return outSize;
    }

    /**
     * 得到资源ID
     */
    public static int getRid(String type,String r){
        return getRid(AppEnv.getAppCtx(), type, r);
    }

    public static int getRid(Context ctx, String type, String r){
        return ctx.getResources().getIdentifier(r, type, ctx.getPackageName());
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(double dp) {
        return (int) (dp * AppEnv.density + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(double px) {
        return (int) (px / AppEnv.density + 0.5f);
    }

}
