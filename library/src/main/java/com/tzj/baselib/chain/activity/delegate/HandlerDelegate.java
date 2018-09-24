package com.tzj.baselib.chain.activity.delegate;

import android.app.Activity;
import android.os.Handler;

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
    public HandlerDelegate(Activity activity) {
        super(activity);
    }

    @Override
    public void onClear() {
        super.onClear();
        for (Handler handler:HANDLERS) {
            handler.removeMessages(0);
        }
    }
}
