package com.tzj.baselib.env;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.tzj.baselib.R;
import com.tzj.baselib.utils.UtilLog;
import com.tzj.baselib.env.config.AppConfig;

/**
 * Created by tzj on 2018/6/5.
 */
public class AppEnv {
    private static Handler mHandler = new Handler();
    private static Toast mToast;
    private static Context appCtx;
    public static float density;

    public static void init(Context ctx){
        AppEnv.appCtx = ctx.getApplicationContext();
        density = ctx.getResources().getDisplayMetrics().density;
//        AppEnv.mToast = Toast.makeText(AppEnv.appCtx,"",Toast.LENGTH_LONG);
        AppEnv.mToast = new Toast(AppEnv.appCtx);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setView(View.inflate(AppEnv.appCtx, R.layout.transient_notification,null));
    }

    public static Context getAppCtx(){
        if(appCtx==null){
            throw new RuntimeException("请先调用 AppEnv.init");
        }
        return appCtx;
    }

    public static Toast getToast() {
        if(mToast==null){
            throw new RuntimeException("请先调用 AppEnv.init");
        }
        return mToast;
    }

    public static void post(Runnable r){
        post(r,0);
    }
    public static void post(Runnable r,int time){
        try {
            if (AppConfig.isAndroid()){
                mHandler.postDelayed(r, time);
            }else{
                r.run();//这里没有延时
            }
        } catch (Exception e) {
            UtilLog.err(e);
        }
    }
    public static void clearPost(){
        mHandler.removeMessages(0);
    }
}
