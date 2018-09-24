package com.tzj.baselib.chain.fragment;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.tzj.baselib.R;
import com.tzj.baselib.chain.IStep;

/**
 * 按照一定步骤来
 */
public class StepFragment extends ActivityFragment implements IStep {

    private SwipeToLoadLayout mLoadLayout;

    protected SwipeToLoadLayout getLoadLayout(){
        if (mLoadLayout==null && mRootView!=null){
            mLoadLayout = mRootView.findViewById(R.id.loadLayout);
            if (mLoadLayout!=null){
                mLoadLayout.setOnRefreshListener(this);
            }
        }
        return mLoadLayout;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void refresh() {
        super.refresh();
        if (getLoadLayout()!=null){
            mLoadLayout.setRefreshing(true);
        }else{
            showProgress();
            onRefresh();
        }
    }

    @Override
    public void loadMore() {
        if (getLoadLayout()!=null){
            mLoadLayout.setLoadingMore(true);
        }else{
            onLoadMore();
        }
    }

    @Override
    public void onRefresh() {
        loadFinish();
    }

    @Override
    public void onLoadMore() {
        loadFinish();
    }

    @Override
    public void loadFinish() {
        if (getLoadLayout()!=null){
            mLoadLayout.setRefreshing(false);
            mLoadLayout.setLoadingMore(false);
        }else{
            dismissProgress();
        }
    }



}
