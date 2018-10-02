package com.tzj.baselib.chain.activity.permission;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public abstract class Permission {
    private Activity act;
    private List<String> lists = new ArrayList<>();

    public Permission(Activity act) {
        this.act = act;
    }

    public Permission add(String permission) {
        lists.add(permission);
        return this;
    }

    public <T extends CallBack> void call(T callBack) {
        requestPermissions(act, callBack, lists);
    }

    public abstract void requestPermissions(Activity act, CallBack callBack, List<String> lists);

    public static abstract class CallBack {
        public abstract void accept();
        public void refuse(){
        }
        public void end(){
        }
    }

}
