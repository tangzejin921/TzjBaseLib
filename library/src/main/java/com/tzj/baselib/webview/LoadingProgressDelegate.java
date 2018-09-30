package com.tzj.baselib.webview;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * 加载进度
 */
public class LoadingProgressDelegate extends DefWebChromeClient{
    //进度条
    private View progress;
    @Override
    public void setmWebView(TzjWebView mWebView) {
        super.setmWebView(mWebView);
        //初始化进度条
        progress = new View(mWebView.getContext());
        progress.setBackgroundColor(0xff2470f5);
        progress.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 8));
        //把进度条加到Webview中
        mWebView.addView(progress);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (newProgress == 100) {
            //加载完毕进度条消失
            progress.setVisibility(View.GONE);
        } else {
            //更新进度
            progress.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams lp = progress.getLayoutParams();
            lp.width = view.getWidth()/100*newProgress;
            progress.setLayoutParams(lp);
        }
    }

}
