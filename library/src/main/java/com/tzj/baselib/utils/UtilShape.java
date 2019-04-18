package com.tzj.baselib.utils;

import android.graphics.drawable.GradientDrawable;

import com.tzj.baselib.env.AppEnv;

/**
 *  xml 定义的bitmap
 */
public class UtilShape {
    public static ShapeDrawable get(int color,int radius){
        return new ShapeDrawable()
                .setColor(color)
                .setRadius(radius);
    }

    public static class ShapeDrawable{
        private GradientDrawable drawable = new GradientDrawable();

        public ShapeDrawable() {
            drawable.setShape(GradientDrawable.RECTANGLE);//纯色填充
        }

        /**
         * 颜色
         */
        public ShapeDrawable setColor(int argb) {
            if (argb>0 && 0x7f000000 == (argb&0x7f000000)){
                argb = AppEnv.getAppCtx().getResources().getColor(argb);
            }
            drawable.setColor(argb);
            return this;
        }
        /**
         * 导角
         */
        public ShapeDrawable setRadius(float radius) {
            drawable.setCornerRadius(radius);
            return this;
        }
        /**
         * 边框
         */
        public ShapeDrawable setStroke(int width, int color) {
            drawable.setStroke(width, color);
            return this;
        }
        /**
         * 形状
         */
        public ShapeDrawable setShape(int shape) {
            drawable.setShape(shape);
            return this;
        }
        public GradientDrawable draw(){
            return drawable;
        }
    }
}
