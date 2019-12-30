package com.tzj.baselib.chain;

import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.github.moduth.blockcanary.AppBlockCanaryContext;
import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;
import com.tzj.baselib.env.TzjAppEnv;
import com.tzj.baselib.env.config.TzjBuildConfig;
import com.tzj.baselib.utils.UtilSystem;

public abstract class TzjLibApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        TzjAppEnv.init(this);
        LeakCanary.install(this);
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        if (TzjBuildConfig.isDebug()) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
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
