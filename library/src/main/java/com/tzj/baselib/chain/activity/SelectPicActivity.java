package com.tzj.baselib.chain.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;

import com.tzj.baselib.chain.activity.permission.Permission;
import com.tzj.baselib.chain.activity.start.ActivityResult;
import com.tzj.baselib.chain.activity.start.IResult;
import com.tzj.baselib.chain.activity.start.StartActivity;
import com.tzj.baselib.chain.dia.BaseDialog;
import com.tzj.baselib.chain.dia.DefaultCreateDialog;
import com.tzj.baselib.chain.dia.ListDialog;
import com.tzj.baselib.utils.UtilUri;
import com.tzj.recyclerview.adapter.TzjAdapter;
import com.zhihu.matisse.Glide4Engine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择图片文件
 */
public class SelectPicActivity extends StartActivity {

    private static DefaultCreateDialog createDialog = new DefaultCreateDialog() {
        @Override
        public BaseDialog createDialog(Context ctx) {
            ListDialog listDialog = new ListDialog(ctx);
            listDialog.add("相册");
            listDialog.add("拍照");
            return listDialog;
        }
    };

    public static void setCreateDialog(DefaultCreateDialog createDialog) {
        SelectPicActivity.createDialog = createDialog;
    }

    /**
     * 打开选择 拍照/图库
     */
    public void openChoice(final Result res) {
        openPermission()
                .add(Manifest.permission.CAMERA)
                .add(Manifest.permission.READ_EXTERNAL_STORAGE)
                .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .call(new Permission.CallBack() {
                    private boolean err = true;//作为标记
                    @Override
                    public void accept() {
                        if (createDialog != null) {
                            final ListDialog dialog = (ListDialog) createDialog.createDialog(SelectPicActivity.this);
                            dialog.setItemClickListener(new TzjAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(TzjAdapter tzjAdapter, View view, int i, Object o) {
                                    err = false;
                                    if (i == 0) {
                                        openAlbum(res);
                                    } else if (i == 1) {
                                        openCamera(res);
                                    }
                                    dialog.dismiss();
                                }
                            }).setDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    if (err) {
                                        res.err();//如果什么也没选，必须调用下 err(),否则 WebView 会调用不了选择文件
                                    }
                                }
                            }).show();
                        } else {
                            //创建ChooserIntent
                            Intent intent = new Intent(Intent.ACTION_CHOOSER);
                            //创建相机Intent
                            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                            file = new File(file.getAbsolutePath(), System.currentTimeMillis() + ".jpg");
                            Uri uri = UtilUri.parUri(SelectPicActivity.this, file);
                            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            //创建相册Intent
                            Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                            albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                            Intent albumIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                            albumIntent.addCategory(Intent.CATEGORY_OPENABLE);
                            albumIntent.setType("image/*");
                            //将相机Intent以数组形式放入Intent.EXTRA_INITIAL_INTENTS
                            intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{captureIntent});
                            //将相册Intent放入Intent.EXTRA_INTENT
                            intent.putExtra(Intent.EXTRA_INTENT, albumIntent);
//                            intent.setType("image/*");//这里不能加

                            res.setFile(file);
                            res.setResolver(getContentResolver());
                            start(intent, res);
                        }
                    }
                });
    }

    /**
     * 知乎的图片选择
     */
    protected void openZhihuChoice(final int maxCount, final Result result) {
        openPermission()
                .add(Manifest.permission.CAMERA)
                .add(Manifest.permission.READ_EXTERNAL_STORAGE)
                .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .call(new Permission.CallBack() {
                    @Override
                    public void accept() {
                        Matisse.from(SelectPicActivity.this)
                                // 选择 mime 的类型
                                .choose(MimeType.ofAll(), false)
                                .countable(true)
                                // 图片选择的最多数量
                                .maxSelectable(maxCount)
//                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.zhihu_grid_expected_size))
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                // 缩略图的比例
                                .thumbnailScale(0.85f)
                                // 使用的图片加载引擎
                                .imageEngine(new Glide4Engine())
                                // 设置作为标记的请求码
                                .forResult(poutResult(result));
                    }
                });
    }

    /**
     * 打开相机
     */
    public void openCamera(final Result res) {
        openPermission()
                .add(Manifest.permission.CAMERA)
                .add(Manifest.permission.READ_EXTERNAL_STORAGE)
                .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .call(new Permission.CallBack() {
                    @Override
                    public void accept() {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        file = new File(file.getAbsolutePath(), System.currentTimeMillis() + ".jpg");
                        Uri uri = UtilUri.parUri(SelectPicActivity.this, file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                        res.setFile(file);
                        res.setResolver(getContentResolver());
                        start(intent, res);
                    }
                });
    }

    /**
     * 打开相册
     */
    public void openAlbum(final Result res) {
        openPermission()
                .add(Manifest.permission.CAMERA)
                .add(Manifest.permission.READ_EXTERNAL_STORAGE)
                .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .call(new Permission.CallBack() {
                    @Override
                    public void accept() {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        start(intent, res);
                    }
                });
    }

    /**
     * 截屏
     */
    public void screenCapture(final Result res) {
        openPermission()
                .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .call(new Permission.CallBack() {
                    @Override
                    public void accept() {
                        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                            @Override
                            public void run() {
                                //找到当前页面的跟布局
                                View view = getWindow().getDecorView().getRootView();
                                //设置缓存
                                view.setDrawingCacheEnabled(true);
                                view.buildDrawingCache();
                                //从缓存中获取当前屏幕的图片
                                Bitmap bitmap = view.getDrawingCache();
                                try {
                                    String temp = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, Environment.DIRECTORY_DCIM, "temp");
                                    final List<Uri> list = new ArrayList<>();
                                    final Uri uri = Uri.parse(temp);
                                    if (uri != null) {
                                        list.add(uri);
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            res.ok(list);
                                        }
                                    });
                                } catch (Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            res.err();
                                        }
                                    });
                                } finally {
                                    bitmap.recycle();
                                }
                            }
                        });
                    }
                });
    }

    public static abstract class Result implements IResult {
        private File file;
        private ContentResolver resolver;

        public void setFile(File file) {
            this.file = file;
        }

        public void setResolver(ContentResolver resolver) {
            this.resolver = resolver;
        }

        @Override
        public void onActivityResult(final ActivityResult result) {
            AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    Uri uri = null;
                    if (file != null) {//相机
                        try {
                            //todo 这里图片可能会挺大的，而且会出现两张一样的
                            String temp = MediaStore.Images.Media.insertImage(resolver, file.getPath(), file.getName(), "temp");
                            uri = Uri.parse(temp);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    if (result.resultOk() && result.getData() != null) {//选择
                        uri = result.getData().getData();
                    }
                    final List<Uri> temp = new ArrayList<>();
                    if (uri != null) {
                        temp.add(uri);
                    }
                    //如果还空，用知乎的图片选择库
                    if (result.resultOk() && temp.size() == 0) {
                        List<Uri> mSelected = Matisse.obtainResult(result.getData());
                        temp.addAll(mSelected);
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (temp != null) {
                                ok(temp);
                            } else {
                                err();
                            }
                        }
                    });
                }
            });
        }

        public abstract void ok(List<Uri> uris);

        public void err() {
        }
    }
}
