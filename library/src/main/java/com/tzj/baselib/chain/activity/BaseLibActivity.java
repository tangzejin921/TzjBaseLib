package com.tzj.baselib.chain.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tzj.baselib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 这里什么也不要写
 * 做为继承链路的尾部
 * 可以修改他的继承类
 */
public class BaseLibActivity extends StepActivity {

    protected void loadFragment(Fragment fragment){
        loadFragment(R.id.fragment,fragment);
    }
    protected void loadFragment(int r, Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(r,fragment,fragment.getClass().getName());
        fragmentTransaction.commitAllowingStateLoss();
        fragmentlist.clear();
        fragmentlist.add(fragment);
    }
    private List<Fragment> fragmentlist = new ArrayList<>();
    protected void addFragment( Fragment fragment){
        addFragment(R.id.fragment,fragment);
    }
    protected void addFragment(int r, Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(fragmentlist.size()>0){
            Fragment temp = fragmentlist.get(fragmentlist.size() - 1);
            fragmentTransaction.hide(temp);
        }
        fragmentlist.add(fragment);
        fragmentTransaction.add(r,fragment,fragment.getClass().getName());
        fragmentTransaction.commitAllowingStateLoss();
    }
    protected boolean removeFragment(){
        boolean ret = fragmentlist.size()>0;
        FragmentManager fragmentManager = getSupportFragmentManager();
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
            finish();
        }
        return ret;
    }

}


