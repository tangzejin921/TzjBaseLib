package com.tzj.baselib;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.tzj.baselib.chain.fragment.BaseLibFragment;

import java.util.ArrayList;
import java.util.List;

public class BaseLibFragmentAdapter extends FragmentPagerAdapter {
    protected List<BaseLibFragment> mList = new ArrayList<>();

    public BaseLibFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(BaseLibFragment fragment){
        mList.add(fragment);
    }

    public List<BaseLibFragment> getList() {
        return mList;
    }

    private boolean refresh;

    /**
     * @param refreshCurrent 是否刷新当前 fragment
     */
    public void notifyDataSetChanged(boolean refreshCurrent) {
        super.notifyDataSetChanged();
        refresh = refreshCurrent;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (refresh){
            refresh = false;
            // 刷新当前页
            BaseLibFragment fragment = (BaseLibFragment)object;
            if (fragment != null) {
                fragment.refresh();
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getTitle();
    }
}
