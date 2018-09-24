package com.tzj.baselib;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;


public class LoadingDialog extends BaseDialog{
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
            ((ImageView) v).setImageResource(R.drawable.loading);
        }
    }
}
