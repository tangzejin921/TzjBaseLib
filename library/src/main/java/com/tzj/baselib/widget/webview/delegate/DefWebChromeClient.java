package com.tzj.baselib.widget.webview.delegate;

import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

import com.tzj.baselib.widget.webview.TzjWebView;

/**
 *
 */
public abstract class DefWebChromeClient extends WebChromeClient{
    protected TzjWebView mWebView;

    public void setmWebView(TzjWebView mWebView) {
        this.mWebView = mWebView;
    }

    public boolean MyopenFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        return false;
    }
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }
}
