package com.tzj.baselib.dia;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tzj.baselib.R;
import com.tzj.baselib.chain.dia.BaseDialog;

/**
 * Copyright © 2019 健康无忧网络科技有限公司<br>
 * Author:      唐泽金 tangzejin921@qq.com<br>
 * Version:     1.0.0<br>
 * Date:        2019/4/9 10:44<br>
 * Description: 消息弹窗
 */
public class MsgDia extends BaseDialog {

    private Type type;
    private String msg,hint;
    private boolean b;

    private ImageView ico;
    private TextView msgTv,hintTv;
    private Button cancle,sure;
    private String cancleStr;
    private View.OnClickListener cancleListener;
    private String sureStr;
    private View.OnClickListener sureListener;

    public MsgDia(Context context, Type type, String msg, String hint) {
        super(context);
        this.type = type;
        this.msg = msg;
        this.hint = hint;
        this.b = b;
    }

    @Override
    protected View createView() {
        return getLayoutInflater().inflate(R.layout.dia_msg, null);
    }

    @Override
    protected void init() {
        super.init();
        setFromCenter();
        ico = findViewById(R.id.ico);
        msgTv = findViewById(R.id.msg);
        hintTv = findViewById(R.id.hint);
        cancle = findViewById(R.id.cancle);
        sure = findViewById(R.id.sure);
        ico.setImageResource(type.getRes());
        msgTv.setText(msg);
        hintTv.setText(hint);
        if (!TextUtils.isEmpty(cancleStr)){
            cancle.setText(cancleStr);
        }
        if (!TextUtils.isEmpty(sureStr)){
            sure.setText(sureStr);
        }

        if (cancleListener != null){
            cancle.setVisibility(View.VISIBLE);
            cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancleListener.onClick(v);
                    dismiss();
                }
            });
        }
        if (sureListener != null){
            sure.setVisibility(View.VISIBLE);
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sureListener.onClick(v);
                    dismiss();
                }
            });
        }
    }

    public MsgDia setCancleListener(String str,View.OnClickListener cancleListener) {
        this.cancleStr = str;
        this.cancleListener = cancleListener;
        return this;
    }

    public MsgDia setSureListener(String str,View.OnClickListener sureListener) {
        this.sureStr = str;
        this.sureListener = sureListener;
        return this;
    }

    public enum Type {
        警告(R.drawable.ico_warning),
        出错(R.drawable.ico_err),
        进行中(R.drawable.ico_wait),
        完成(R.drawable.ico_ok);
        int res;

        Type(int res) {
            this.res = res;
        }

        public int getRes() {
            return res;
        }
    }

}
