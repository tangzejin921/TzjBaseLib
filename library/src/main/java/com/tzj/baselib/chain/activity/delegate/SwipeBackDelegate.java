package com.tzj.baselib.chain.activity.delegate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * 滑动返回
 */
public class SwipeBackDelegate extends ActivityDelegate implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    public SwipeBackDelegate(AppCompatActivity activity) {
        super(activity);
        mHelper = new SwipeBackActivityHelper(mActivity.get());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper.onActivityCreate();
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        mHelper.onPostCreate();
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return (T)mHelper.findViewById(id);
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(mActivity.get());
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHelper = null;
    }
}
