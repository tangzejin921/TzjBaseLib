package com.tzj.baselib.chain.activity.rule;


import android.app.Activity;
import android.content.Intent;

public interface ICheck{
    /**
     * @return 是否拦截了,拦截了将拦截跳转
     */
    boolean call(Activity act, Intent intent, int requestCode);
}
