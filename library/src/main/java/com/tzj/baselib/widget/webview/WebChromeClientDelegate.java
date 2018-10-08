package com.tzj.baselib.widget.webview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

import com.tzj.baselib.chain.activity.permission.Permission;
import com.tzj.baselib.chain.activity.permission.PermissionActivity;
import com.tzj.baselib.widget.webview.delegate.DefWebChromeClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 */
public final class WebChromeClientDelegate extends WebChromeClient {
    private TzjWebView mWebView;
    private List<DefWebChromeClient> mList = new ArrayList<>();

    public WebChromeClientDelegate(TzjWebView mWebView) {
        this.mWebView = mWebView;
    }

    public void addDelegate(DefWebChromeClient delegate) {
        delegate.setmWebView(mWebView);
        mList.add(delegate);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onProgressChanged(view,newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onReceivedTitle(view,title);
        }
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onReceivedIcon(view,icon);
        }
    }

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        super.onReceivedTouchIconUrl(view, url, precomposed);
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onReceivedTouchIconUrl(view,url,precomposed);
        }
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        super.onShowCustomView(view, callback);
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onShowCustomView(view,callback);
        }
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        super.onShowCustomView(view, requestedOrientation, callback);
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onShowCustomView(view,requestedOrientation,callback);
        }
    }

    @Override
    public void onHideCustomView() {
        super.onHideCustomView();
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onHideCustomView();
        }
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            if(iterator.next().onCreateWindow(view,isDialog,isUserGesture,resultMsg)){
                return true;
            }
        }
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public void onRequestFocus(WebView view) {
        super.onRequestFocus(view);
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onRequestFocus(view);
        }
    }

    @Override
    public void onCloseWindow(WebView window) {
        super.onCloseWindow(window);
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onCloseWindow(window);
        }
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            if(iterator.next().onJsAlert(view, url, message, result)){
                return true;
            }
        }
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            if(iterator.next().onJsConfirm(view, url, message, result)){
                return true;
            }
        }
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, JsPromptResult result) {
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onJsPrompt(view, url, message, defaultValue, result)){
                return true;
            }
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message,
                                    JsResult result) {
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onJsBeforeUnload(view, url, message, result)){
                return true;
            };
        }
        return super.onJsBeforeUnload(view, url, message, result);
    }

    @Override
    @Deprecated
    public void onExceededDatabaseQuota(String url, String databaseIdentifier,
                                        long quota, long estimatedDatabaseSize, long totalQuota,
                                        WebStorage.QuotaUpdater quotaUpdater) {
        // This default implementation passes the current quota back to WebCore.
        // WebCore will interpret this that new quota was declined.
        super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
        }
    }

    @Override
    @Deprecated
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota,
                                         WebStorage.QuotaUpdater quotaUpdater) {
        super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
        }
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin,
                                                   GeolocationPermissions.Callback callback) {
        super.onGeolocationPermissionsShowPrompt(origin, callback);
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onGeolocationPermissionsShowPrompt(origin, callback);
        }
    }

    /**
     * notify the host application that a request for Geolocation permissions,
     * made with a previous call to
     * {@link #onGeolocationPermissionsShowPrompt(String, GeolocationPermissions.Callback) onGeolocationPermissionsShowPrompt()}
     * has been canceled. Any related UI should therefore be hidden.
     */
    @Override
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onGeolocationPermissionsHidePrompt();
        }
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onPermissionRequest(PermissionRequest request) {
        super.onPermissionRequest(request);
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onPermissionRequest(request);
        }
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onPermissionRequestCanceled(PermissionRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onPermissionRequestCanceled(request);
            Iterator<DefWebChromeClient> iterator = mList.iterator();
            while (iterator.hasNext()) {
                iterator.next().onPermissionRequestCanceled(request);
            }
        }
    }

    @Override
    public boolean onJsTimeout() {
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onJsTimeout()){
                return true;
            }
        }
        return super.onJsTimeout();
    }

    @Override
    @Deprecated
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        super.onConsoleMessage(message, lineNumber, sourceID);
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onConsoleMessage(message, lineNumber, sourceID);
        }
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        /*onConsoleMessage(consoleMessage.message(), consoleMessage.lineNumber(),
                consoleMessage.sourceId());*/
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onConsoleMessage(consoleMessage)){
                return true;
            }
        }
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            Bitmap defaultVideoPoster = iterator.next().getDefaultVideoPoster();
            if (defaultVideoPoster!=null){
                return defaultVideoPoster;
            }
        }
        return super.getDefaultVideoPoster();
    }

    @Override
    public View getVideoLoadingProgressView() {
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            View videoLoadingProgressView = iterator.next().getVideoLoadingProgressView();
            if (videoLoadingProgressView!=null){
                return videoLoadingProgressView;
            }
        }
        return super.getVideoLoadingProgressView();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        super.getVisitedHistory(callback);
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().getVisitedHistory(callback);
        }
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean onShowFileChooser(final WebView webView, final ValueCallback<Uri[]> filePathCallback,
                                     final FileChooserParams fileChooserParams) {
        Context context = webView.getContext();
        if (context instanceof PermissionActivity){
            ((PermissionActivity) context).openPermission()
                    .add(Manifest.permission.CAMERA)
                    .add(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .call(new Permission.CallBack() {
                        @Override
                        public void accept() {
                            Iterator<DefWebChromeClient> iterator = mList.iterator();
                            while (iterator.hasNext()) {
                                if (iterator.next().onShowFileChooser(webView, filePathCallback, fileChooserParams)){
                                    break;
                                }
                            }
                        }
                        @Override
                        public void refuse() {
                            super.refuse();
                            filePathCallback.onReceiveValue(null);
                        }
                    });
            return true;
        }else{
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
    }

    //  Android < 3.0
    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        MyopenFileChooser(valueCallback, null, null);
    }
    //  Android  >= 3.0
    public void openFileChooser(ValueCallback valueCallback, String acceptType) {
        MyopenFileChooser(valueCallback, acceptType, null);
    }
    // Android  >= 4.1
    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        MyopenFileChooser(uploadFile, acceptType, capture);
    }
    public boolean MyopenFileChooser(final ValueCallback<Uri> uploadFile,final String acceptType, final String capture) {
        Context context = mWebView.getContext();
        if (context instanceof PermissionActivity){
            ((PermissionActivity) context).openPermission()
                    .add(Manifest.permission.CAMERA)
                    .add(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .call(new Permission.CallBack() {
                        @Override
                        public void accept() {
                            Iterator<DefWebChromeClient> iterator = mList.iterator();
                            while (iterator.hasNext()) {
                                if (iterator.next().MyopenFileChooser(uploadFile,acceptType,capture)){
                                    break;
                                }
                            }
                        }
                        @Override
                        public void refuse() {
                            super.refuse();
                            uploadFile.onReceiveValue(null);
                        }
                    });
            return true;
        }else{
            return false;
        }
    }
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        Iterator<DefWebChromeClient> iterator = mList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onActivityResult(requestCode,resultCode,data)){
                return true;
            }
        }
        return false;
    }
}
