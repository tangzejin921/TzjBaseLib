package com.tzj.baselib.chain.activity;

import android.support.design.widget.Snackbar;

import com.tzj.baselib.BaseDialog;
import com.tzj.baselib.LoadingDialog;
import com.tzj.baselib.chain.activity.start.StartActivity;

/**
 * 有加载中，和 Toast
 */
public class LoadingActivity extends StartActivity {

    private BaseDialog dialog;
    private volatile Integer number = 0;

    public void showProgress() {
        synchronized (this){
            if (dialog==null) {
                number = 0;
                dialog = new LoadingDialog(this);
            }
            if (!dialog.isShowing()){
                dialog.show(false);
            }
            ++number;
        }
    }
    public void dismissProgress() {
        synchronized (this){
            --number;
            if (number>0||dialog == null) {
                return ;
            }
            dialog.dismiss();
            dialog=null;
        }
    }

    public void toast(Object obj){
        Snackbar.make(getWindow().getDecorView(),"",Snackbar.LENGTH_LONG)
                .setText(obj+"").show();
    }
    public void toast(int r){
        if ((r&0x7f000000)==0x7f000000){
            Snackbar.make(getWindow().getDecorView(),"",Snackbar.LENGTH_LONG)
                    .setText(r).show();
        }else{
            toast(r+"");
        }
    }
}
