package com.tzj.baselib.chain.fragment;

import android.support.v4.app.Fragment;

/**
 * Copyright © 2019 健康无忧网络科技有限公司<br>
 * Author:      唐泽金 tangzejin921@qq.com<br>
 * Version:     1.0.0<br>
 * Date:        2019/4/11 09:57<br>
 * Description: fragmeng 管理
 */
public interface IFragManager {

    void loadFragment(Fragment fragment);
    void loadFragment(int r, Fragment fragment);
    void addFragment( Fragment fragment);
    void addFragment(int r, Fragment fragment);
    boolean removeFragment();
    void onDestroy();

}
