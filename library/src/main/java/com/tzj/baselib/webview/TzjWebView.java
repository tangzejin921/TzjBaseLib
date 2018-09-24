package com.tzj.baselib.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class TzjWebView extends WebView {
    private Activity mActivity;
    private WebViewClientDelegate webViewClient = new WebViewClientDelegate(this);
    private WebChromeClientDelegate webChromeClient = new WebChromeClientDelegate(this);

    /**
     * 第一次加载的url
     */
    private String originalUrl;
    protected String currentUrl;

    public TzjWebView(Context context) {
        super(context);
        init();
    }

    public TzjWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TzjWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TzjWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init();
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    protected void init() {
        if ((getContext() instanceof Activity)) {
            mActivity = (Activity) getContext();
        }
        getSettings().setLoadsImagesAutomatically(true);//自动加载图片
//        getSettings().setBlockNetworkLoads(true);//不加载网络图片
        getSettings().setUseWideViewPort(false);//将图片调整到适合webview的大小
        getSettings().setDomStorageEnabled(true);//用于持久化的本地存储
        getSettings().setDatabaseEnabled(true);
        getSettings().setAppCacheEnabled(true);//缓存
        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        String appCachePath = getContext().getCacheDir().getAbsolutePath();
        //启用地理定位
        getSettings().setGeolocationEnabled(true);
        getSettings().setGeolocationDatabasePath(appCachePath);
        getSettings().setAppCachePath(appCachePath);
        getSettings().setAppCacheMaxSize(1024 * 1024 * 5);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//https的URL时在5.0以上加载不了
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getSettings().setAllowFileAccessFromFileURLs(true);
        }
//        getSettings().setSupportZoom(true);//zoom
        getSettings().setBuiltInZoomControls(true);//设置支持缩放
        getSettings().setUseWideViewPort(true);//扩大比例的缩放
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        getSettings().setJavaScriptEnabled(true);//js
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        getSettings().setAllowFileAccess(true);//启用或禁止WebView访问文件数据
        //下载
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (null != url) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    getContext().startActivity(intent);
                }
            }
        });

        addWebViewClient(new HrefWebViewClient());
        addWebChromeClient(new UploadDelegate());
        setWebViewClient(webViewClient);
        setWebChromeClient(webChromeClient);
    }


    public void addWebViewClient(DefWebViewClient client) {
        webViewClient.addDelegate(client);
    }

    public void addWebChromeClient(DefWebChromeClient client) {
        webChromeClient.addDelegate(client);
    }
    public void loadJs(String js){
        super.loadUrl(js);
    }
    public void loadData(String data) {
        super.loadData(data, "text/html; charset=UTF-8",null);
    }

    @Override
    public void loadUrl(String url) {
        if (url.toLowerCase().startsWith("http")){
            if (originalUrl==null){
                originalUrl = url;
            }
            super.loadUrl(currentUrl = url);
        }else{
            super.loadUrl(url);
        }
    }

    @Override
    public String getOriginalUrl() {
        return originalUrl;
    }
    @Override
    public String getUrl() {
        return currentUrl;
    }

    /**
     *
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return webChromeClient.onActivityResult(requestCode,resultCode,data);
    }

    public void onBackPressed(){
        if (canGoBack()){
            goBack();
        }else if (mActivity!=null && !mActivity.isFinishing()){
            mActivity.finish();
        }
    }

    @Override
    public void destroy() {
        setWebViewClient(webViewClient = null);
        setWebChromeClient(webChromeClient = null);
        mActivity = null;

        loadUrl("about:blank");
        zoomOut();
        setVisibility(GONE);
        removeAllViews();
        super.destroy();
    }

}
