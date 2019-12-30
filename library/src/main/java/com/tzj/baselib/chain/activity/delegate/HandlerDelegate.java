package com.tzj.baselib.chain.activity.delegate;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * activity 关闭时会清除handler 的任务
 */
public class HandlerDelegate extends ActivityDelegate{
    /**
     * 只是清除post 发送的 Runable
     */
    public static List<Handler> HANDLERS = new ArrayList();
    public HandlerDelegate(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void onClear() {
        super.onClear();
        //每一个界面关了都会清楚所有的 handler
        for (Handler handler:HANDLERS) {
            handler.removeMessages(0);
        }
    }
}
