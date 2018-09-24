package com.tzj.baselib.chain.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.tzj.baselib.chain.activity.BaseLibActivity;
import com.tzj.baselib.chain.activity.StepActivity;
import com.tzj.baselib.chain.activity.permission.Permission;
import com.tzj.baselib.chain.activity.start.IResult;
import com.tzj.baselib.chain.fragment.delegate.DelegateFragment;


/**
 * 有加载中，和 Toast 都是调用Activity 的
 */
public class ActivityFragment extends DelegateFragment {

    public void start(Intent intent, IResult result) {
        FragmentActivity activity = getActivity();
        if (activity!= null && activity instanceof StepActivity){
            ((BaseLibActivity) activity).start(intent,result);
        }
    }
    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        FragmentActivity activity = getActivity();
        if (activity!= null && activity instanceof StepActivity){
            ((BaseLibActivity) activity).startActivity(intent,options);
        }else{
            super.startActivity(intent,options);
        }
    }
    public Permission openPermission() {
        FragmentActivity activity = getActivity();
        if (activity!= null && activity instanceof StepActivity){
            return ((BaseLibActivity) activity).openPermission();
        }else{
            throw new RuntimeException();
        }
    }


    public void showProgress() {
        //todo 这里如果 fragment 被系统移除将不会调用 dismissProgress
        FragmentActivity activity = getActivity();
        if (activity!= null && activity instanceof StepActivity){
            ((BaseLibActivity) activity).showProgress();
        }
    }
    public void dismissProgress() {
        FragmentActivity activity = getActivity();
        if (activity!= null && activity instanceof StepActivity){
            ((BaseLibActivity) activity).dismissProgress();
        }
    }

    public void toast(Object obj){
        FragmentActivity activity = getActivity();
        if (activity!= null && activity instanceof StepActivity){
            ((BaseLibActivity) activity).toast(obj);
        }
    }
    public void toast(int r){
        FragmentActivity activity = getActivity();
        if (activity!= null && activity instanceof StepActivity){
            ((BaseLibActivity) activity).toast(r);
        }
    }

}
