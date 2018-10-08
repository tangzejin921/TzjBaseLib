package com.tzj.baselib.widget.webview.delegate;

import android.content.Context;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.tzj.baselib.chain.activity.BaseLibActivity;
import com.tzj.baselib.chain.activity.SelectPicActivity;


/**
 * 上传文件,不放在 BaseLibActivity 中将无效
 */
public class UploadDelegate extends DefWebChromeClient {
    private ValueCallback<Uri[]> fielCallback;
    private ValueCallback<Uri> oldFielCallback;

//    private static final int CHOISE_PIC =3541;
//    private static final int CHOISE_PIC_OLD =3542;
//
//
//    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (CHOISE_PIC == requestCode){
//            if (null != fielCallback){
//                if (resultCode == Activity.RESULT_OK){
//                    Uri result = data.getData();
//                    fielCallback.onReceiveValue(new Uri[]{result});
//                }else{
//                    fielCallback.onReceiveValue(null);
//                }
//                fielCallback = null;
//            }
//            return true;
//        }else if (CHOISE_PIC_OLD==requestCode){
//            if (null != oldFielCallback){
//                if (resultCode == Activity.RESULT_OK){
//                    Uri result = data.getData();
//                    oldFielCallback.onReceiveValue(result);
//                }else{
//                    oldFielCallback.onReceiveValue(null);
//                }
//                oldFielCallback = null;
//            }
//            return true;
//        }else{
//            return false;
//        }
//    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, final FileChooserParams fileChooserParams) {
        fielCallback = filePathCallback;
        Context context = mWebView.getContext();
        if (context instanceof BaseLibActivity){
            ((BaseLibActivity)context).openChoice(new SelectPicActivity.Result() {
                @Override
                public void ok(Uri uri) {
                    fielCallback.onReceiveValue(new Uri[]{uri});
                    fielCallback = null;
                }
                @Override
                public void err() {
                    if (fielCallback!=null){
                        fielCallback.onReceiveValue(null);
                        fielCallback = null;
                    }
                }
            });
            return true;
        }
        return false;
    }

    public boolean MyopenFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        oldFielCallback = uploadFile;
        Context context = mWebView.getContext();
        if (context instanceof BaseLibActivity){
            ((BaseLibActivity)context).openChoice(new SelectPicActivity.Result() {
                @Override
                public void ok(Uri uri) {
                    oldFielCallback.onReceiveValue(uri);
                    oldFielCallback = null;
                }
                @Override
                public void err() {
                    if (oldFielCallback!=null){
                        oldFielCallback.onReceiveValue(null);
                        oldFielCallback = null;
                    }
                }
            });
            return true;
        }
        return false;
    }




}
