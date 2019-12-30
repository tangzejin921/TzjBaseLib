package com.tzj.baselib.dataBinding;
import android.arch.lifecycle.Observer;
import android.databinding.BaseObservable;
import android.support.annotation.Nullable;

import java.io.Serializable;


/**
 * 苏宁物流集团
 *
 * @Author 19043910
 * @CreateTime 2019/7/5 11:08
 * @Description:
 */
public class ViewData extends BaseObservable implements Observer, Serializable {
    private String text = "null";

     public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyChange();
    }

    @Override
    public void onChanged(@Nullable Object o) {
        setText(o.toString());
    }
}
