package com.tzj.baselib.chain.fragment;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tzj.baselib.R;
import com.tzj.baselib.chain.IStep;

/**
 * 按照一定步骤来
 */
public class StepFragment extends ActivityFragment implements IStep {

    private SmartRefreshLayout mRefreshLayout;

    protected SmartRefreshLayout getLoadLayout() {
        if (mRefreshLayout == null && mRootView != null) {
            mRefreshLayout = mRootView.findViewById(R.id.refreshLayout);
            if (mRefreshLayout != null) {
                mRefreshLayout.setOnRefreshListener(this);
                mRefreshLayout.setOnLoadMoreListener(this);
            }
        }
        return mRefreshLayout;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void refresh() {
        super.refresh();
        if (getLoadLayout() != null) {
            mRefreshLayout.autoRefresh();
        } else {
            showProgress();
            onRefresh();
        }
    }

    @Override
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

    @Override
    public void loadFinish() {
        if (getLoadLayout() != null) {
            mRefreshLayout.finishRefresh();
            mRefreshLayout.finishLoadMore();
        } else {
            dismissProgress();
        }
    }



}
