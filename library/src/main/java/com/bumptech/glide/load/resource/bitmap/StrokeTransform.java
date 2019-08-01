package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * 描边+倒角
 * 被坑死了，因为被改过的图片效果是被保存的，所以运行一遍后，
 * 再怎么改 transform 里的方法都没反应，
 * 注意 CenterCrop
 */
public class StrokeTransform extends CenterCrop {

    private static final String ID = StrokeTransform.class.getName();
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private int roundingRadius;
    private int width;
    private int color;

    /**
     * @param roundingRadius 圆角
     * @param width          描边的宽度
     * @param color          描边的颜色
     */
    public StrokeTransform(int roundingRadius, int width, int color) {
        this.roundingRadius = roundingRadius;
        this.width = width;
        this.color = color;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap inBitmap, int outWidth, int outHeight) {
        Bitmap toTransform = getAlphaSafeBitmap(pool, inBitmap);
        Bitmap result = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        result.setHasAlpha(true);

        //画背景
        Paint paintBg = new Paint();
        paintBg.setAntiAlias(true);
        paintBg.setColor(color);
        paintBg.setStrokeWidth(width);
        paintBg.setStyle(Paint.Style.STROKE);
        paintBg.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        //画图片
        BitmapShader shader = new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        RectF rect = new RectF(width/2, width/2, result.getWidth() - width/2, result.getHeight() - width/2);
        TransformationUtils.getBitmapDrawableLock().lock();
        try {
            Canvas canvas = new Canvas(result);
            //清楚所有
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            //画背景
            canvas.drawRoundRect(rect, roundingRadius, roundingRadius, paintBg);
            //画图片
            canvas.drawRoundRect(rect, roundingRadius, roundingRadius, paint);
            canvas.setBitmap(null);
        } finally {
            TransformationUtils.getBitmapDrawableLock().unlock();
        }

        if (!toTransform.equals(inBitmap)) {
            pool.put(toTransform);
        }

        return result;
    }

    public static Bitmap getAlphaSafeBitmap(BitmapPool pool, Bitmap maybeAlphaSafe) {
        if (Bitmap.Config.ARGB_8888.equals(maybeAlphaSafe.getConfig())) {
            return maybeAlphaSafe;
        }

        Bitmap argbBitmap = pool.get(maybeAlphaSafe.getWidth(), maybeAlphaSafe.getHeight(), Bitmap.Config.ARGB_8888);
        new Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0, 0, null);

        return argbBitmap;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StrokeTransform) {
            StrokeTransform other = (StrokeTransform) o;
            return other.roundingRadius == roundingRadius &&
                    other.width == width &&
                    other.color == color;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + roundingRadius + width + color;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        byte[] radiusData = ByteBuffer.allocate(12)
                .putInt(roundingRadius)
                .putInt(width)
                .putInt(color)
                .array();
        messageDigest.update(radiusData);
    }
}
