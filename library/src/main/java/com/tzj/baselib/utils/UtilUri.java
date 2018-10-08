package com.tzj.baselib.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * uri 相关
 */
public class UtilUri {
    /**
     * 适配 FileProvider
     */
    public static Uri parUri(Context ctx,File cameraFile) {
        Uri imageUri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".fileprovider", cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }
}
