package com.tzj.baselib.chain.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tzj.baselib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2019 健康无忧网络科技有限公司<br>
 * Author:      唐泽金 tangzejin921@qq.com<br>
 * Version:     1.0.0<br>
 * Date:        2019/4/11 09:58<br>
 * Description: fragmeng 管理
 */
public class FragManagerImp implements IFragManager{
    private Object obj;
    public FragManagerImp(Object obj) {
        this.obj = obj;
    }

    @Override
    public void loadFragment(Fragment fragment){
        loadFragment(R.id.fragment,fragment);
    }
    @Override
    public void loadFragment(int r, Fragment fragment){
        FragmentManager fragmentManager = fragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(r,fragment,fragment.getClass().getName());
        fragmentTransaction.commitAllowingStateLoss();
        fragmentlist.clear();
        fragmentlist.add(fragment);
    }
    private List<Fragment> fragmentlist = new ArrayList<>();
    @Override
    public void addFragment( Fragment fragment){
        addFragment(R.id.fragment,fragment);
    }
    @Override
    public void addFragment(int r, Fragment fragment){
        FragmentManager fragmentManager = fragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(fragmentlist.size()>0){
            Fragment temp = fragmentlist.get(fragmentlist.size() - 1);
            fragmentTransaction.hide(temp);
        }
        fragmentlist.add(fragment);
        fragmentTransaction.add(r,fragment,fragment.getClass().getName());
        fragmentTransaction.commitAllowingStateLoss();
    }
    @Override
    public boolean removeFragment(){
        boolean ret = fragmentlist.size()>0;
        FragmentManager fragmentManager = fragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(fragmentlist.size()>0){
            Fragment fragment = fragmentlist.remove(fragmentlist.size() - 1);
            fragmentTransaction.remove(fragment);
            if(fragmentlist.size()>0){
                Fragment temp = fragmentlist.get(fragmentlist.size() - 1);
                fragmentTransaction.show(temp);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
        if(fragmentlist.size()==0){
            onRemoveEnd();
        }
        return ret;
    }

    private FragmentManager fragmentManager(){
        if (obj instanceof FragmentActivity){
            return ((FragmentActivity) obj).getSupportFragmentManager();
        }
        if (obj instanceof Fragment){
            return ((Fragment) obj).getChildFragmentManager();
        }
        return null;
    }

    private void onRemoveEnd(){
        if (obj instanceof Activity){
            ((Activity) obj).finish();
        }
    }

    @Override
    public void onDestroy() {
        fragmentlist.clear();
    }

}
