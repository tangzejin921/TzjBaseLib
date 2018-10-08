package com.tzj.baselib.widget.webview.delegate;

import android.webkit.WebViewClient;

import com.tzj.baselib.widget.webview.TzjWebView;


/**
 */
public abstract class DefWebViewClient extends WebViewClient{

    protected TzjWebView mWebView;

    public void setmWebView(TzjWebView mWebView) {
        this.mWebView = mWebView;
    }
}
