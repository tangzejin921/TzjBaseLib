package com.tzj.baselib.chain.activity;


import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import com.tzj.baselib.chain.fragment.FragManagerImp;
import com.tzj.baselib.chain.fragment.IFragManager;


/**
 * 这里什么也不要写
 * 做为继承链路的尾部
 * 可以修改他的继承类
 */
public class BaseLibActivity extends StepActivity implements IFragManager {

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        showBack();
    }

    /**
     * 显示返回
     */
    public void showBack() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 显示返回按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            // 去掉logo图标
            actionBar.setDisplayShowHomeEnabled(false);
        }
    }

    /**
     * 控制 title 隐藏显示
     */
    public void setTitleVisibility(int visibility) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (visibility == View.GONE) {
                actionBar.hide();
            } else {
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

    protected IFragManager fragManager = new FragManagerImp(this);

    @Override
    public void loadFragment(Fragment fragment) {
        fragManager.loadFragment(fragment);
    }

    @Override
    public void loadFragment(int r, Fragment fragment) {
        fragManager.loadFragment(r, fragment);
    }

    @Override
    public void addFragment(Fragment fragment) {
        fragManager.addFragment(fragment);
    }

    @Override
    public void addFragment(int r, Fragment fragment) {
        fragManager.addFragment(r, fragment);
    }

    @Override
    public boolean removeFragment() {
        return fragManager.removeFragment();
    }

    @Override
    public void onDestroy() {
        fragManager.onDestroy();
        super.onDestroy();
    }
}


