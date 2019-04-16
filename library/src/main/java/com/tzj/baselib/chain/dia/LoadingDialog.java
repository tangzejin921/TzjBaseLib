package com.tzj.baselib.chain.dia;

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
        int identifier = v.getResources().getIdentifier("gif_loading", "drawable", v.getContext().getPackageName());
        if (identifier == 0){
            identifier = R.drawable.gif_loading;
        }
        if (v instanceof ImageView){
            Glide.with(getContext())
                    .asGif()
                    .load(identifier)
                    .into(((ImageView) v));
        }
    }
}
