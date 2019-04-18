package com.tzj.baselib.utils;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import com.tzj.baselib.env.AppEnv;


/**
 * app 相关
 */
public class UtilApp {

    public static void show(int res) {
        if (res >0 && (res & 0x7f000000) == 0x7f000000){
            show(AppEnv.getAppCtx().getResources().getString(res));
        }else{
            show(res+"");
        }
    }

    public static void show(final CharSequence c) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            AppEnv.getToast().setText(c);
            AppEnv.getToast().show();
        } else {
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
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getSize(outSize);
        }
        return outSize;
    }

    /**
     * 得到资源ID
     */
    public static int getRid(String type, String r) {
        return getRid(AppEnv.getAppCtx(), type, r);
    }

    public static int getRid(Context ctx, String type, String r) {
        return ctx.getResources().getIdentifier(r, type, ctx.getPackageName());
    }

    /**
     * dp 转 px
     */
    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, AppEnv.displayMetrics);
    }

    /**
     * px 转 dp
     */
    public static int px2dp(float px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, AppEnv.displayMetrics);
    }

    /**
     * 状态栏颜色
     */
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));
                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 状态栏颜色
     */
    public static void setWindowStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
