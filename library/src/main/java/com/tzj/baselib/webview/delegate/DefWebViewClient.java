package com.tzj.baselib.webview.delegate;

import android.webkit.WebViewClient;

import com.tzj.baselib.webview.TzjWebView;


/**
 */
public abstract class DefWebViewClient extends WebViewClient{

    protected TzjWebView mWebView;

    public void setmWebView(TzjWebView mWebView) {
        this.mWebView = mWebView;
    }
}
