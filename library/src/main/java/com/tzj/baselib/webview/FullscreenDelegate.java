package com.tzj.baselib.webview;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;

/**
 * 全屏播放
 */
public class FullscreenDelegate extends DefWebChromeClient{
    /**
     * 全屏时视频加载view
      */
    private FrameLayout video_fullView;
    private View xCustomView;
    private WebChromeClient.CustomViewCallback xCustomViewCallback;


    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        super.onShowCustomView(view, callback);
        //旋转全屏
        Context context = view.getContext();
        if (context instanceof Activity && !((Activity)context).isFinishing()){
            Activity act = (Activity) context;
            act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mWebView.setVisibility(View.INVISIBLE);
            // 如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            FrameLayout decor = (FrameLayout) act.getWindow().getDecorView();
            video_fullView = new FrameLayout(act);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            video_fullView.addView(view, lp);
            decor.addView(video_fullView, lp);
            xCustomView = view;
            xCustomViewCallback = callback;
            video_fullView.setVisibility(View.VISIBLE);
            video_fullView.bringToFront();
        }
    }

    @Override
    public void onHideCustomView() {
        super.onHideCustomView();
        if (xCustomView == null) {
            // 不是全屏播放状态
            return;
        }
        //退出全屏
        Context context = mWebView.getContext();
        if (context instanceof Activity && !((Activity)context).isFinishing()){
            Activity act = (Activity) context;
            act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            FrameLayout decor = (FrameLayout) act.getWindow().getDecorView();
            decor.removeView(video_fullView);
            video_fullView = null;
            xCustomView = null;
            xCustomViewCallback.onCustomViewHidden();
            mWebView.setVisibility(View.VISIBLE);
        }
    }

}
