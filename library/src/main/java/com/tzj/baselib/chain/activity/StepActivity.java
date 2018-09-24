package com.tzj.baselib.chain.activity;


import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.tzj.baselib.R;
import com.tzj.baselib.chain.IStep;

/**
 * 按照一定步骤来
 */
public class StepActivity extends LoadingActivity implements IStep {
    private SwipeToLoadLayout mLoadLayout;

    protected SwipeToLoadLayout getLoadLayout(){
        if (mLoadLayout==null){
            mLoadLayout = findViewById(R.id.loadLayout);
            if (mLoadLayout!=null){
                mLoadLayout.setOnRefreshListener(this);
            }
        }
        return mLoadLayout;
    }

    /**
     * 初始化View
     */
    public void initView(){
    }

    /**
     * 刷新数据
     */
    public void refresh(){
        if (getLoadLayout()!=null){
            mLoadLayout.setRefreshing(true);
        }else{
            showProgress();
            onRefresh();
        }
    }
    /**
     * 加载数据
     */
    public void loadMore(){
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
    public void loadFinish(){
        if (getLoadLayout()!=null){
            mLoadLayout.setRefreshing(false);
            mLoadLayout.setLoadingMore(false);
        }else{
            dismissProgress();
        }
    }
}
