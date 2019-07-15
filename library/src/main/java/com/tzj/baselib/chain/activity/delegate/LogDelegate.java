package com.tzj.baselib.chain.activity.delegate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * 打印
 */
public class LogDelegate extends ActivityDelegate{
    public LogDelegate(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("test",mActivity.get().getClass().getSimpleName());
        super.onCreate(savedInstanceState);
    }
}
