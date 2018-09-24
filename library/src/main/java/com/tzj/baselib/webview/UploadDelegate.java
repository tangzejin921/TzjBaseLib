package com.tzj.baselib.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.tzj.baselib.chain.activity.BaseLibActivity;
import com.tzj.baselib.chain.activity.start.ActivityResult;
import com.tzj.baselib.chain.activity.start.IResult;


/**
 * 上传文件
 */
public class UploadDelegate extends DefWebChromeClient{
    private ValueCallback<Uri[]> fielCallback;
    private ValueCallback<Uri> oldFielCallback;

    private static final int CHOISE_PIC =3541;
    private static final int CHOISE_PIC_OLD =3542;


    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CHOISE_PIC == requestCode){
            if (null != fielCallback){
                if (resultCode == Activity.RESULT_OK){
                    Uri result = data.getData();
                    fielCallback.onReceiveValue(new Uri[]{result});
                }else{
                    fielCallback.onReceiveValue(null);
                }
                fielCallback = null;
            }
            return true;
        }else if (CHOISE_PIC_OLD==requestCode){
            if (null != oldFielCallback){
                if (resultCode == Activity.RESULT_OK){
                    Uri result = data.getData();
                    oldFielCallback.onReceiveValue(result);
                }else{
                    oldFielCallback.onReceiveValue(null);
                }
                oldFielCallback = null;
            }
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        fielCallback = filePathCallback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        Context context = mWebView.getContext();
        if (context instanceof BaseLibActivity){
            ((BaseLibActivity)context).start(Intent.createChooser(intent, "请选择文件"), new IResult() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (null != fielCallback){
                        if (result.resultOk()){
                            Uri uri = result.getData().getData();
                            fielCallback.onReceiveValue(new Uri[]{uri});
                        }else{
                            fielCallback.onReceiveValue(null);
                        }
                        fielCallback = null;
                    }
                }
            });
            return true;
        }else if (context instanceof Activity){
            ((Activity) context).startActivityForResult(Intent.createChooser(intent, "请选择文件"), CHOISE_PIC);
            return true;
        }
        return false;
    }

    public boolean MyopenFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        oldFielCallback = uploadFile;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (!TextUtils.isEmpty(acceptType)) {
            intent.setType(acceptType);
        } else {
            intent.setType("image/*");
        }
        Context context = mWebView.getContext();
        if (context instanceof BaseLibActivity){
            ((BaseLibActivity)context).start(Intent.createChooser(intent, "请选择文件"), new IResult() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (null != fielCallback){
                        if (result.resultOk()){
                            Uri uri = result.getData().getData();
                            oldFielCallback.onReceiveValue(uri);
                        }else{
                            oldFielCallback.onReceiveValue(null);
                        }
                        oldFielCallback = null;
                    }
                }
            });
            return true;
        }else if (context instanceof Activity){
            ((Activity) context).startActivityForResult(Intent.createChooser(intent, "请选择文件"), CHOISE_PIC_OLD);
            return true;
        }
        return false;
    }
}
