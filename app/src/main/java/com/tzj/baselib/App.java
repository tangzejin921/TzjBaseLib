package com.tzj.baselib;


import com.tzj.baselib.chain.TzjLibApplication;
import com.tzj.baselib.env.TzjAppEnv;

public class App extends TzjLibApplication{

    @Override
    protected void mainProcessOnCreate() {
        super.mainProcessOnCreate();
        TzjAppEnv.init(this);
    }
}
