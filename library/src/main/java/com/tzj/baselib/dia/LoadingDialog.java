package com.tzj.baselib.dia;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tzj.baselib.R;


public class LoadingDialog extends BaseDialog {
    public LoadingDialog(Context context) {
        super(context);
    }
    @Override
    protected View createView() {
        return View.inflate(getContext(), R.layout.dia_loading,null);
    }

    @Override
    protected void init() {
        super.init();
        clearBack();
        View v = findViewById(R.id.loading_view);
        if (v instanceof ImageView){
            Glide.with(getContext())
                    .asGif()
                    .load(R.drawable.gif_loading)
                    .into(((ImageView) v));
        }
    }
}
