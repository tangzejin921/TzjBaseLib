package com.tzj.baselib;

import android.view.View;
import android.widget.TextView;

import com.tzj.baselib.demo.R;
import com.tzj.recyclerview.adapter.TzjAdapter;
import com.tzj.recyclerview.holder.TzjViewHolder;

public class StringHolder extends TzjViewHolder<String> {
    private TextView textView;
    public StringHolder(View itemView) {
        super(itemView);
        textView = bind(R.id.text);
    }

    @Override
    public void onBind(TzjAdapter adapter, String s, int position) {
        textView.setText(s);
        setOnClickListener(textView,position);
    }
}
