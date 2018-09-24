package com.tzj.baselib.webview;

import android.webkit.WebViewClient;


/**
 */
public class DefWebViewClient extends WebViewClient{

    protected TzjWebView mWebView;

    public void setmWebView(TzjWebView mWebView) {
        this.mWebView = mWebView;
    }
}
