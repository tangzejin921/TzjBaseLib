package com.tzj.baselib.chain.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import com.tzj.baselib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 这里什么也不要写
 * 做为继承链路的尾部
 * 可以修改他的继承类
 */
public class BaseLibActivity extends StepActivity {

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setTitle(title);
            // 显示返回按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            // 去掉logo图标
            actionBar.setDisplayShowHomeEnabled(false);
        }
    }

    /**
     * 控制 title 隐藏显示
     */
    public void setTitleVisibility(int visibility){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            if (visibility == View.GONE){
                actionBar.hide();
            }else{
                actionBar.show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


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


