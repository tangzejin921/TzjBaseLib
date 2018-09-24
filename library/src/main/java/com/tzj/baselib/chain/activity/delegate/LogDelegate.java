package com.tzj.baselib.chain.activity.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * 打印
 */
public class LogDelegate extends ActivityDelegate{
    public LogDelegate(Activity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("test",mActivity.getClass().getSimpleName());
        super.onCreate(savedInstanceState);
    }
}
