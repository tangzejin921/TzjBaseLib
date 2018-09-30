package com.tzj.baselib.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.tzj.baselib.chain.activity.BaseLibActivity;
import com.tzj.baselib.chain.activity.start.ActivityResult;
import com.tzj.baselib.chain.activity.start.IResult;

import java.io.File;
import java.io.FileNotFoundException;


/**
 * 要读写权限
 * 上传文件
 */
public class UploadDelegate extends DefWebChromeClient{
    private File file;
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
        file = null;
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);

        //创建ChooserIntent
        Intent intent = new Intent(Intent.ACTION_CHOOSER);
        //创建相机Intent
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        file = new File(file.getAbsolutePath(),System.currentTimeMillis() + ".jpg");
        Uri uri = Uri.fromFile(file);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //创建相册Intent
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        //将相机Intent以数组形式放入Intent.EXTRA_INITIAL_INTENTS
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{captureIntent});
        //将相册Intent放入Intent.EXTRA_INTENT
        intent.putExtra(Intent.EXTRA_INTENT, albumIntent);

        Context context = mWebView.getContext();
        if (context instanceof BaseLibActivity){
            ((BaseLibActivity)context).start(Intent.createChooser(intent, "请选择文件"), new IResult() {
                private Uri uri;
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (file!=null){
                        try {
                            String temp = MediaStore.Images.Media.insertImage(mWebView.getContext().getContentResolver(), file.getPath(), file.getName(), "temp");
                            uri = Uri.parse(temp);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != fielCallback){
                        if (uri != null){
                            fielCallback.onReceiveValue(new Uri[]{uri});
                        }else if (result.resultOk()&&result.getData()!=null){
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
        file = null;
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);

        //创建ChooserIntent
        Intent intent = new Intent(Intent.ACTION_CHOOSER);
        //创建相机Intent
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        file = new File(file,System.currentTimeMillis() + ".jpg");
        Uri uri = Uri.fromFile(file);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //创建相册Intent
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        //将相机Intent以数组形式放入Intent.EXTRA_INITIAL_INTENTS
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{captureIntent});
        //将相册Intent放入Intent.EXTRA_INTENT
        intent.putExtra(Intent.EXTRA_INTENT, albumIntent);


        Context context = mWebView.getContext();
        if (context instanceof BaseLibActivity){
            ((BaseLibActivity)context).start(Intent.createChooser(intent, "请选择文件"), new IResult() {
                private Uri uri;
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (file!=null){
                        try {
                            String temp = MediaStore.Images.Media.insertImage(mWebView.getContext().getContentResolver(), file.getPath(), file.getName(), "temp");
                            uri = Uri.parse(temp);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != oldFielCallback){
                        if (uri!=null){
                            oldFielCallback.onReceiveValue(uri);
                        }else if (result.resultOk()&&result.getData()!=null){
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
