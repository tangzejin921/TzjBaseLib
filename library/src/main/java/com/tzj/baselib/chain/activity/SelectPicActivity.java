package com.tzj.baselib.chain.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.tzj.baselib.chain.activity.start.ActivityResult;
import com.tzj.baselib.chain.activity.start.IResult;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 选择图片文件
 */
public class SelectPicActivity extends BaseLibActivity{
    private File file;
    private IResult result = new IResult() {
        private Uri uri;
        @Override
        public void onActivityResult(ActivityResult result) {
            if (file!=null){
                try {
                    String temp = MediaStore.Images.Media.insertImage(getContentResolver(), file.getPath(), file.getName(), "temp");
                    uri = Uri.parse(temp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    /**
     *  打开选择 拍照/图库
     */
    public void openChoice(){
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
        intent.setType("image/*");

        start(Intent.createChooser(intent, "请选择文件"), result);
    }

    /**
     * 打开相机
     */
    public void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        file = new File(file.getAbsolutePath(),System.currentTimeMillis() + ".jpg");
        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        start(Intent.createChooser(intent, "请选择文件"), result);
    }
    /**
     * 打开相册
     */
    public void openAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        start(Intent.createChooser(intent, "请选择文件"), result);
    }
    /**
     * 截屏
     */
    public void screenCapture(){

    }
}
