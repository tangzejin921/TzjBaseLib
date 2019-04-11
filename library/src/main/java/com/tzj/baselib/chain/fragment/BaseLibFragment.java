package com.tzj.baselib.chain.fragment;


import android.support.v4.app.Fragment;

import com.tzj.baselib.chain.activity.start.ActivityResult;
import com.tzj.baselib.chain.activity.start.IResult;

public class BaseLibFragment extends StepFragment implements IFragManager {
    protected IResult refresh = new IResult() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.refresh()){
                refresh();
            }
        }
    };

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
