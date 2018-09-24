package com.tzj.baselib.chain.activity.delegate;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


/**
 *
 */
public class ActivityDelegate {
    protected Activity mActivity;
    public ActivityDelegate(Activity activity) {
        mActivity = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
    }

    public void onStart() {
    }

    public void onPostCreate(@Nullable Bundle savedInstanceState) {
    }

    public void onResume() {
    }

    /**
     * onResume 方法彻底执行完毕的回调
     */
    public void onPostResume() {
    }

    public void onPause() {
    }

    public void onStop() {
    }

    /**
     * 清除异步调用
     * onDestroy 清除有问题
     * onDestroy 会在 新启的 activity onCreate之后调用
     */
    public void onClear() {
    }

    public void onDestroy() {
        mActivity = null;
    }

    /**
     *
     */
    public void onNewIntent(Intent intent) {
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }

    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        return false;
    }

    public boolean onBackPressed() {
        return false;
    }

    public Resources getResources() {
        return null;
    }

    public <T extends View> T findViewById(int id) {
        return null;
    }
}
