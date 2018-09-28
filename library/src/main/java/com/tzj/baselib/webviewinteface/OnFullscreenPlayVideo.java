package com.tzj.baselib.webviewinteface;

import android.view.View;
import android.webkit.WebChromeClient;

/**
 * 全屏播放接口
 */
public interface OnFullscreenPlayVideo {

    /**
     * 进入全屏播放
     * @param view
     * @param callback
     */
    void onEnterFullScreenPlay(View view, WebChromeClient.CustomViewCallback callback);

    /**
     * 退出全屏播放
     */
    void onExitFullScreenPlay();

}
