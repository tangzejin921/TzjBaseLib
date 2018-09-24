package com.tzj.baselib.chain.activity.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * 滑动删除
 */
public class SwipeBackDelegate extends ActivityDelegate implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    public SwipeBackDelegate(Activity activity) {
        super(activity);
        mHelper = new SwipeBackActivityHelper(mActivity);
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
        Utils.convertActivityToTranslucent(mActivity);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHelper = null;
    }
}
