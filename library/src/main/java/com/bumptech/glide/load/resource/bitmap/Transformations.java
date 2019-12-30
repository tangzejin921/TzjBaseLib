package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于Glide
 * 1.xml 里 ImageView 设置 centerCrop 会对效果有影响
 * 2.transform 处理过的bitmap是会保存的，所以要通过重新安装apk看transform代码的改动
 */
public class Transformations extends BitmapTransformation {
    private static final String ID = Transformations.class.getName();
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    protected List<BitmapTransformation> list = new ArrayList<>();

    /**
     * 具体的代码加入顺序不一样效果会不一样
     */
    public Transformations add(BitmapTransformation transformation){
        list.add(transformation);
        return this;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        for (BitmapTransformation transformation: list) {
            toTransform = transformation.transform(pool,toTransform,outWidth,outHeight);
        }
        return toTransform;
    }
    @Override
    public boolean equals(Object o) {
        for (BitmapTransformation transformation: list) {
            if (!transformation.equals(o)){
                return false;
            }
        }
        return o instanceof Transformations;
    }

    @Override
    public int hashCode() {
        int code = ID.hashCode();
        for (BitmapTransformation transformation: list) {
            code += transformation.hashCode();
        }
        return code;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        for (BitmapTransformation transformation: list) {
            transformation.updateDiskCacheKey(messageDigest);
        }
        messageDigest.update(ID_BYTES);
    }

}
