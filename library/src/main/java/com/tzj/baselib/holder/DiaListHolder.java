package com.tzj.baselib.holder;

import android.view.View;
import android.widget.TextView;

import com.tzj.baselib.R;
import com.tzj.baselib.dia.ListDialog;
import com.tzj.recyclerview.adapter.TzjAdapter;
import com.tzj.recyclerview.holder.TzjViewHolder;

public class DiaListHolder extends TzjViewHolder{

    private TextView textView;
    public DiaListHolder(View itemView) {
        super(itemView);
        textView = (TextView) bind(R.id.text);
    }

    @Override
    public void onBind(TzjAdapter adapter, Object o, int position) {
        super.onBind(adapter, o, position);
        if (o instanceof ListDialog.Show){
            textView.setText(((ListDialog.Show) o).diaStr());
        }else{
            textView.setText(o.toString());
        }
    }
}
