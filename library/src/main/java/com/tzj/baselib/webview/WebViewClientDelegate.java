package com.tzj.baselib.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 */
public final class WebViewClientDelegate extends WebViewClient {
    private TzjWebView mWebView;
    private List<DefWebViewClient> mList = new ArrayList<>();

    public WebViewClientDelegate(TzjWebView mWebView) {
        this.mWebView = mWebView;
    }

    public void addDelegate(DefWebViewClient delegate){
        delegate.setmWebView(mWebView);
        mList.add(delegate);
    }

    @Override
    @Deprecated
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Iterator<DefWebViewClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            if(iterator.next().shouldOverrideUrlLoading(view, url)){
                return true;
            }
        }
        return super.shouldOverrideUrlLoading(view, url);
    }
    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Iterator<DefWebViewClient> iterator = mList.iterator();
            while (iterator.hasNext()) {
                if(iterator.next().shouldOverrideUrlLoading(view, request)){
                    return true;
                }
            }
            return super.shouldOverrideUrlLoading(view, request);
        }
        return false;
    }
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Iterator<DefWebViewClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onPageStarted(view, url, favicon);
        }
    }
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Iterator<DefWebViewClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onPageFinished(view, url);
        }
    }
    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        Iterator<DefWebViewClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onLoadResource(view, url);
        }
    }
    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onPageCommitVisible(WebView view, String url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            super.onPageCommitVisible(view, url);
            Iterator<DefWebViewClient> iterator = mList.iterator();
            while (iterator.hasNext()) {
                iterator.next().onPageCommitVisible(view, url);
            }
        }
    }
    @Override
    @Deprecated
    public WebResourceResponse shouldInterceptRequest(WebView view,
                                                      String url) {
        Iterator<DefWebViewClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            WebResourceResponse webResourceResponse = iterator.next().shouldInterceptRequest(view, url);
            if(webResourceResponse!=null){
                return webResourceResponse;
            }
        }
        return super.shouldInterceptRequest(view, url);
    }
    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WebResourceResponse shouldInterceptRequest(WebView view,
                                                      WebResourceRequest request) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Iterator<DefWebViewClient> iterator = mList.iterator();
            while (iterator.hasNext()) {
                WebResourceResponse webResourceResponse = iterator.next().shouldInterceptRequest(view, request);
                if(webResourceResponse!=null){
                    return webResourceResponse;
                }
            }
            return super.shouldInterceptRequest(view, request);
        }else{
            return null;
        }
    }
    @Override
    @Deprecated
    public void onTooManyRedirects(WebView view, Message cancelMsg,
                                   Message continueMsg) {
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
        Iterator<DefWebViewClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onTooManyRedirects(view, cancelMsg, continueMsg);
        }
    }
    @Override
    @Deprecated
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        Iterator<DefWebViewClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onReceivedError(view, errorCode, description, failingUrl);
        }
    }
    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            super.onReceivedError(view, request, error);
            Iterator<DefWebViewClient> iterator = mList.iterator();
            while (iterator.hasNext()) {
                iterator.next().onReceivedError(view, request, error);
            }
        }
    }
    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onReceivedHttpError(
            WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            super.onReceivedHttpError(view, request, errorResponse);
            Iterator<DefWebViewClient> iterator = mList.iterator();
            while (iterator.hasNext()) {
                iterator.next().onReceivedHttpError(view, request, errorResponse);
            }
        }
    }
    @Override
    public void onFormResubmission(WebView view, Message dontResend,
                                   Message resend) {
        super.onFormResubmission(view, dontResend, resend);
        Iterator<DefWebViewClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onFormResubmission(view, dontResend, resend);
        }
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url,
                                       boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
        Iterator<DefWebViewClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().doUpdateVisitedHistory(view, url, isReload);
        }
    }
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                   SslError error) {
        super.onReceivedSslError(view, handler, error);
        Iterator<DefWebViewClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onReceivedSslError(view, handler, error);
        }
    }
    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onReceivedClientCertRequest(view, request);
            Iterator<DefWebViewClient> iterator = mList.iterator();
            while (iterator.hasNext()) {
                iterator.next().onReceivedClientCertRequest(view, request);
            }
        }
    }
    @Override
    public void onReceivedHttpAuthRequest(WebView view,
                                          HttpAuthHandler handler, String host, String realm) {
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
        Iterator<DefWebViewClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onReceivedHttpAuthRequest(view, handler, host, realm);
        }
    }
    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        Iterator<DefWebViewClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            if(iterator.next().shouldOverrideKeyEvent(view, event)){
                return true;
            }
        }
        return super.shouldOverrideKeyEvent(view, event);
    }
    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        super.onUnhandledKeyEvent(view, event);
        Iterator<DefWebViewClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onUnhandledKeyEvent(view, event);
        }
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
        Iterator<DefWebViewClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onScaleChanged(view, oldScale, newScale);
        }
    }
    @Override
    public void onReceivedLoginRequest(WebView view, String realm,
                                       String account, String args) {
        super.onReceivedLoginRequest(view, realm, account, args);
        Iterator<DefWebViewClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onReceivedLoginRequest(view, realm, account, args);
        }
    }
}
