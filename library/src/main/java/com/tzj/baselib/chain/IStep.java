package com.tzj.baselib.chain;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;

/**
 * activity 的一般步骤
 */
public interface IStep extends OnRefreshListener,OnLoadMoreListener {
    /**
     * 初始化View
     */
    void initView();
    /**
     * 请求/刷新数据 入口
     */
    void refresh();
    /**
     * 分页加载更多数据 入口
     */
    void loadMore();
    /**
     * 请求/刷新数据
     */
    void onRefresh();

    /**
     * 分页加载更多数据
     */
    void onLoadMore() ;

    /**
     * 加载结束
     */
    void loadFinish();
}
