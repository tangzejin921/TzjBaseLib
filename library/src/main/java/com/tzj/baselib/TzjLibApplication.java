package com.tzj.baselib;

import android.app.Application;

import com.tzj.baselib.utils.UtilSystem;

public abstract class TzjLibApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        if(getPackageName().equalsIgnoreCase(UtilSystem.getProcessName(this))){// 进程判断
            mainProcessOnCreate();
        }else{
            otherProcessOnCreate();
        }
    }
    /**
     * 主进程要自行的东西
     */
    protected void mainProcessOnCreate() {

    }
    /**
     * 其他进程要执行的内容
     */
    protected void otherProcessOnCreate() {

    }
}
