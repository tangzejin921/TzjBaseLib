package com.tzj.baselib.chain.activity.start;

import android.app.Activity;
import android.content.Intent;

public class ActivityResult {
    public static final int REFRESH = "REFRESH".hashCode()%65530;
    private int resultCode;
    private Intent data;

    public ActivityResult(int resultCode, Intent data) {
        this.resultCode = resultCode;
        this.data = data;
    }

    public boolean refresh(){
        return REFRESH == resultCode;
    }

    public boolean resultOk(){
        return Activity.RESULT_OK == resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public Intent getData() {
        return data;
    }
}
