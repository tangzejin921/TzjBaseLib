package com.tzj.baselib.env;

import android.app.Application;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.tzj.baselib.R;
import com.tzj.baselib.utils.UtilLog;
import com.tzj.baselib.env.config.TzjAppConfig;

/**
 * Created by tzj on 2018/6/5.
 */
public class TzjAppEnv {
    private static Handler mHandler = new Handler();
    private static Toast mToast;
    private static Application appCtx;
    public static DisplayMetrics displayMetrics;

    public static void init(Application app){
        TzjAppEnv.appCtx = app;
        displayMetrics = app.getResources().getDisplayMetrics();
//        TzjAppEnv.mToast = Toast.makeText(TzjAppEnv.appCtx,"",Toast.LENGTH_LONG);
        TzjAppEnv.mToast = new Toast(TzjAppEnv.appCtx);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setView(View.inflate(TzjAppEnv.appCtx, R.layout.transient_notification,null));
    }

    public static Application getAppCtx(){
        if(appCtx==null){
            throw new RuntimeException("请先调用 TzjAppEnv.init");
        }
        return appCtx;
    }

    public static Toast getToast() {
        if(mToast==null){
            throw new RuntimeException("请先调用 TzjAppEnv.init");
        }
        return mToast;
    }

    public static void post(Runnable r){
        post(r,0);
    }
    public static void post(Runnable r,int time){
        try {
            if (TzjAppConfig.isAndroid()){
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
