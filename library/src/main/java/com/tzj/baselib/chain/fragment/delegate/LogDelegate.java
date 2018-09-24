package com.tzj.baselib.chain.fragment.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * 打印
 */
public class LogDelegate extends FragmentDelegate{

    public LogDelegate(DelegateFragment fragment) {
        super(fragment);
    }


    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("test",mFragment.getClass().getSimpleName());
    }


}
