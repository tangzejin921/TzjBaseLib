package com.tzj.baselib.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v4.util.SimpleArrayMap;

import java.io.File;

/**
 * uri 相关
 */
public class UtilUri {
    /**
     * 适配 FileProvider
     * 记得intent 加上
     * intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
     * 安装apk不能用 Build.VERSION_CODES.M
     */
    public static Uri parUri(Context ctx, File cameraFile) {
        Uri imageUri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".fileprovider", cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }

    /**
     * 得到最后一个what向后的内容不含what
     */
    public static String getLast(String str, String what) {
        if (str == null) {
            return null;
        }
        int lastIndexOf = str.lastIndexOf(what);
        int start = lastIndexOf + what.length();
        if (start < str.length()) {
            return str.substring(start, str.length());
        }
        return null;
    }

    /**
     * 得到第一个start 到第一个end 的内容</p>
     * start为空时为从最前面
     */
    public static String getFirst(String str, String start, String end) {
        if (str == null || end == null) {
            return null;
        }
        int indexStart = 0;
        if (start != null) {
            indexStart = str.indexOf(start) + start.length();
        }
        int indexEnd = str.indexOf(end);
        if (indexStart < indexEnd) {
            return str.substring(indexStart, indexEnd);
        }
        return null;
    }

    /**
     * 得到url里的参数
     */
    public static SimpleArrayMap<String, String> fromData(String url) {
        SimpleArrayMap<String, String> ret = new SimpleArrayMap<>();
        String[] split = url.split("\\?");
        if (split.length > 1) {
            split = split[1].split("#");
            split = split[0].split("&");
            String[] temp;
            for (String s : split) {
                temp = s.split("=");
                if (temp.length < 2) {
                    ret.put(temp[0], "");
                } else {
                    String value = "";
                    for (int i = 1; i < temp.length; i++) {
                        value += temp[i];
                    }
                    ret.put(temp[0], value);
                }
            }
        }
        return ret;
    }

    /**
     * url里加参数
     */
    public static final String fromAdd(String url, String key, String value) {
        String item = key + "=" + value;
        if (url == null) {
            return url;
        }
        if (url.contains(item)) {
            return url;
        } else if (url.contains("#/")) {
            if (!url.contains("?")) {
                url += "?" + item;
            } else {
                url += "&" + item;
            }
        } else if (url.contains("#")) {
            if (url.contains("?")) {
                url = url.replace("#", "&" + item + "#");
            } else {
                url = url.replace("#", "?" + item + "#");
            }
        } else {
            if (url.contains("?")) {
                url += "&" + item;
            } else {
                url = url + "?" + item;
            }
        }
        return url;
    }

}
