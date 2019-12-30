package com.tzj.baselib.chain.dia;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;

import com.tzj.baselib.R;
import com.tzj.baselib.chain.activity.BaseLibActivity;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;


/**
 * dialog
 */
public abstract class BaseFragDialog extends DialogFragment {
    private WeakReference<Context> ctx;
    protected View mRoot;
    protected Dialog.OnCancelListener cancelListener;
    /**
     * 放正在显示的 Dialog
     */
    private static Set<Class> map = new HashSet<>();

    public BaseFragDialog() {
        setStyle(R.style.dialog_base,R.style.dialog_base);
    }
    public BaseFragDialog setContext(Context context) {
        ctx = new WeakReference<>(context);
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(mRoot == null){
            mRoot = createView();
            init();
        }else{
            ViewParent parent = mRoot.getParent();
            if(parent!=null&&parent instanceof ViewGroup){
                ((ViewGroup) parent).removeView(mRoot);
            }
        }
        return mRoot;
    }

    protected abstract View createView();

    protected void init() {
//        setContentView(mRoot = createView());
    }

    protected void setWindowAnimations(int a){
        Window window = getWindow();
        if (window!=null){
            window.setWindowAnimations(a);
        }
    }
    /**
     * 无背景色
     */
    public void clearBack(){
        Window window = getWindow();
        if (window!=null){
            window.clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND | WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }
    /**
     *
     */
    protected void setGravity(int gravity) {
        Window window = getWindow();
        if (window!=null){
            window.setGravity(Gravity.BOTTOM);
        }
    }
    protected void setPadding(int p){
        setPadding(p,p,p,p);
    }
    protected void setPadding(int l,int t,int r,int b){
        Window window = this.getWindow();
        //去掉dialog默认的padding
        if (window!=null){
            window.getDecorView().setPadding(l, t, r, b);
        }
    }

    /**
     * 设置宽高
     */
    protected void setLayout(int w,int h){
        Window window = this.getWindow();
        if (window!=null){
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = w;
            lp.height = h;
            window.setAttributes(lp);
        }
    }
    /**
     * 从底部
     */
    protected void setFromBottom(){
        setPadding(0);
        setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        setGravity(Gravity.BOTTOM);
        setWindowAnimations(R.style.animation_bottom);
    }
    protected void setFromCenter(){
        setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        wm.getDefaultDisplay().getSize(outSize);
        setPadding((int) (outSize.x * 0.1));
    }

    public FragmentActivity gitActivity(){
        if (ctx != null){
            Context context = ctx.get();
            if (context == null){
                context = getActivity();
            }
            if (context != null && context instanceof FragmentActivity){
                return ((FragmentActivity) context);
            }
        }
        return null;
    }

    public Window getWindow(){
        FragmentActivity activity = gitActivity();
        if (activity != null){
            return activity.getWindow();
        }
        return null;
    }

    public boolean isShowing(){
        return super.isVisible();
    }

    /**
     * 请设置 context
     */
    public void show(){
        FragmentActivity activity = gitActivity();
        if (activity != null){
            show(activity.getSupportFragmentManager(),getClass().getSimpleName());
        }
    }

    /**
     * 请设置 context
     */
    public void show(boolean cancelable){
        setCancelable(cancelable);
        show();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (map.contains(this.getClass())) {
            return;
        }
        //网上说 把 commit 改为 commitAllowingStateLoss
        try {
            super.show(manager, getClass().getName());
            map.add(this.getClass());
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    public <T extends View> T findViewById(@IdRes int r){
        return mRoot.findViewById(r);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (cancelListener != null){
            cancelListener.onCancel(dialog);
        }
    }

    public BaseFragDialog setDismissListener(@Nullable Dialog.OnDismissListener listener) {
//        super.setOnDismissListener(listener);
        this.cancelListener = cancelListener;
        return this;
    }

    //====================
    public void showProgress(){
        Activity activity = getActivity();
        if (activity != null && activity instanceof BaseLibActivity){
            ((BaseLibActivity) activity).showProgress();
        }
    }
    public void dismissProgress(){
        Activity activity = getActivity();
        if (activity != null && activity instanceof BaseLibActivity){
            ((BaseLibActivity) activity).dismissProgress();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.remove(this.getClass());
    }
}
