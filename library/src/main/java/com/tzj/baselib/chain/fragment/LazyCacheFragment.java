package com.tzj.baselib.chain.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * 懒加载并且缓存View
 */
public class LazyCacheFragment extends Fragment {
    protected View mRootView;
    protected boolean isInitView;

    protected int layoutId(){
        return 0;
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(mRootView == null){
            mRootView = inflater.inflate(layoutId(),null);
            initView();
            if(getUserVisibleHint() && !isInitView){
                isInitView = true;
                refresh();
            }
        }else{
            ViewParent parent = mRootView.getParent();
            if(parent!=null&&parent instanceof ViewGroup){
                ((ViewGroup) parent).removeView(mRootView);
            }
        }
        return mRootView;
    }

    /**
     * 创建view 时调用，只调用一次
     */
    public void initView() {

    }
    /**
     * 第一次显示的时候调用
     */
    public void refresh() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        boolean pro = getUserVisibleHint();
        super.setUserVisibleHint(isVisibleToUser);
        if(mRootView==null){
            return;
        }
        if(getUserVisibleHint() && !isInitView){//没有初始化
            isInitView = true;
            refresh();
        }
        if(!pro && isVisibleToUser){
            onVisible();
        }
        if (pro && !isVisibleToUser){
            onGone();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()){
            onVisible();
        }
    }

    /**
     * fragment 为当前界面时调用
     */
    public void onVisible(){

    }
    /**
     * fragment 不是当前界面时调用
     */
    public void onGone(){

    }

    @Override
    public void onPause() {
        if (getUserVisibleHint()){//屏幕熄灭
            onGone();
        }
        super.onPause();
    }

    public String getTitle(){
        return getClass().getSimpleName();
    }

    public <T extends View> T findViewById(int r){
        return mRootView.findViewById(r);
    }

    public void finish(){
        if(mRootView!=null){
            Context context = mRootView.getContext();
            if(context!=null&&context  instanceof Activity){
                ((Activity)context).finish();
            }
        }
    }
}
