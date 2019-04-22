package com.tzj.baselib.chain;

import android.support.multidex.MultiDexApplication;

import com.tzj.baselib.env.AppEnv;
import com.tzj.baselib.utils.UtilSystem;

public abstract class TzjLibApplication extends MultiDexApplication {

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
        AppEnv.init(this);
    }
    /**
     * 其他进程要执行的内容
     */
    protected void otherProcessOnCreate() {

    }
}
