package com.tzj.baselib.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.tzj.baselib.webviewinteface.OnFullscreenPlayVideo;
import com.tzj.baselib.webviewinteface.OnWebLoadingProgressLisntener;

public class TzjWebView extends WebView {
    private Activity mActivity;
    private WebViewClientDelegate webViewClient = new WebViewClientDelegate(this);
    private WebChromeClientDelegate webChromeClient = new WebChromeClientDelegate(this);

    /**
     * 第一次加载的url
     */
    private String originalUrl;
    protected String currentUrl;
    //进度条
    private ProgressBar progressBar;

    /**
     * 视频全屏参数
     */

    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    // 全屏时视频加载view
    private FrameLayout video_fullView;
    private View xCustomView;
    private WebChromeClient.CustomViewCallback xCustomViewCallback;

    public void setCanOneKeyBack(boolean canOneKeyBack) {
        isCanOneKeyBack = canOneKeyBack;
    }

    //一键退出
    private boolean isCanOneKeyBack = false;

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
        //初始化进度条
        progressBar = new ProgressBar(getContext());
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 4));

        progressBar.setProgress(10);
        //把进度条加到Webview中
        addView(progressBar);
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
        webChromeClient.setOnWebLoadingProgressLisntener(new OnWebLoadingProgressLisntener() {
            @Override
            public void onProgressChange(WebView view, int progress) {
                if (progress == 100) {
                    //加载完毕进度条消失
                    progressBar.setVisibility(View.GONE);
                } else {
                    //更新进度
                    progressBar.setProgress(progress);

                }


            }
        });

        webChromeClient.setOnFullscreenPlayVideo(new OnFullscreenPlayVideo() {
            @Override
            public void onEnterFullScreenPlay(View view, WebChromeClient.CustomViewCallback callback) {
                //旋转全屏
                Activity activity = (Activity) getContext();
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                TzjWebView.this.setVisibility(View.INVISIBLE);
                // 如果一个视图已经存在，那么立刻终止并新建一个
                if (xCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }
                FrameLayout decor = (FrameLayout) activity.getWindow().getDecorView();
                video_fullView = new FrameLayout(activity);
                video_fullView.addView(view, COVER_SCREEN_PARAMS);
                decor.addView(video_fullView, COVER_SCREEN_PARAMS);
                xCustomView = view;
                xCustomViewCallback = callback;
                video_fullView.setVisibility(View.VISIBLE);
                video_fullView.bringToFront();


            }

            @Override
            public void onExitFullScreenPlay() {
                if (xCustomView == null) {
                    // 不是全屏播放状态
                    return;
                }
                //退出全屏
                Activity activity = (Activity) getContext();
                activity.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                FrameLayout decor = (FrameLayout) activity.getWindow().getDecorView();
                decor.removeView(video_fullView);
                video_fullView = null;
                xCustomView = null;
                xCustomViewCallback.onCustomViewHidden();
                TzjWebView.this.setVisibility(View.VISIBLE);
            }
        });
    }

    public void loadJs(String js) {
        super.loadUrl(js);
    }

    public void loadData(String data) {
        super.loadData(data, "text/html; charset=UTF-8", null);
    }

    @Override
    public void loadUrl(String url) {
        if (url.toLowerCase().startsWith("http")) {
            if (originalUrl == null) {
                originalUrl = url;
            }
            super.loadUrl(currentUrl = url);
        } else {
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
        return webChromeClient.onActivityResult(requestCode, resultCode, data);
    }


    public void onBackPressed() {
        if (canGoBack()) {
            goBack();
        } else if (mActivity != null && !mActivity.isFinishing()) {
            mActivity.finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeTimers();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isCanOneKeyBack) {
            if (canGoBack()) {
                goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
