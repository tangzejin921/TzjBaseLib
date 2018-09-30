package com.tzj.baselib.chain.activity;


import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tzj.baselib.R;
import com.tzj.baselib.chain.IStep;

/**
 * 按照一定步骤来
 */
public class StepActivity extends LoadingActivity implements IStep {
    private SmartRefreshLayout mRefreshLayout;

    protected SmartRefreshLayout getLoadLayout() {
        if (mRefreshLayout == null) {
            mRefreshLayout = findViewById(R.id.refreshLayout);
            if (mRefreshLayout != null) {
                mRefreshLayout.setOnRefreshListener(this);
                mRefreshLayout.setOnLoadMoreListener(this);
            }
        }
        return mRefreshLayout;
    }

    /**
     * 初始化View
     */
    public void initView() {
    }

    /**
     * 刷新数据
     */
    public void refresh() {
        if (getLoadLayout() != null) {
            mRefreshLayout.autoRefresh();
        } else {
            showProgress();
            onRefresh();
        }
    }

    /**
     * 加载数据
     */
    public void loadMore() {
        if (getLoadLayout() != null) {
            mRefreshLayout.autoLoadMore();
        } else {
            onLoadMore();
        }
    }

    @Override
    public final void onRefresh(RefreshLayout refreshLayout) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        loadFinish();
    }

    @Override
    public final void onLoadMore(RefreshLayout refreshLayout) {
        onLoadMore();
    }

    @Override
    public void onLoadMore() {
        loadFinish();
    }

    public void loadFinish() {
        if (getLoadLayout() != null) {
            mRefreshLayout.finishRefresh();
            mRefreshLayout.finishLoadMore();
        } else {
            dismissProgress();
        }
    }
}
