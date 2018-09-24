package com.tzj.baselib.webview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

public class HrefWebViewClient extends DefWebViewClient{

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            if (url.startsWith("alipay")) {//支付
                try {
                    mWebView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception e) {
                    new AlertDialog.Builder(mWebView.getContext())
                            .setMessage("未检测到支付宝客户端，请安装后重试。")
                            .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Uri alipayUrl = Uri.parse("https://d.alipay.com");
                                    mWebView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, alipayUrl));
                                }
                            }).setNegativeButton("取消", null).show();
                }
            } else if (url.startsWith("http://a.app.qq.com")) {//qq 市场
                Uri uri = Uri.parse("market://details?id=" + mWebView.getContext().getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mWebView.getContext().startActivity(intent);
            } else if (!url.startsWith("http")) {//mailto: geo: tel:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mWebView.getContext().startActivity(intent);
            } else {//todo 这里如果是支付
                return super.shouldOverrideUrlLoading(view, mWebView.currentUrl = url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
