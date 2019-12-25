package com.tzj.baselib.chain.dia;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;

import com.tzj.baselib.R;

import java.util.HashSet;
import java.util.Set;

/**
 * 请使用 DialogFragment 代替 Dialog
 * Dialog 崩溃率比较高
 * DialogFragment,相对安全点，因为有Fragment的生命周期
 */
public class BaseFragDialogV2 extends DialogFragment {

    protected View mRoot;
    protected Dialog.OnCancelListener cancelListener;
    protected int windowAnimations;
    protected boolean clearBack;
    protected int gravity = Gravity.CENTER;
    protected int left, top, right, bottom;
    protected int width, height;
    protected boolean fromCenter;

    /**
     * 放正在显示的 Dialog
     */
    private static Set<Class> map = new HashSet<>();

    public BaseFragDialogV2() {
        setStyle(R.style.dialog_base, R.style.dialog_base);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRoot == null) {
            mRoot = createView(inflater, container);
        } else {
            ViewParent parent = mRoot.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mRoot);
            }
        }
        return mRoot;
    }

    /**
     * 创建View
     * 初始化View请放到 {@link DialogFragment#onViewCreated(View, Bundle)}
     */
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        if (windowAnimations != 0) {
            window.setWindowAnimations(windowAnimations);
        }
        if (clearBack) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND | WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        if (fromCenter) {
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            WindowManager wm = (WindowManager) dialog.getContext().getSystemService(Context.WINDOW_SERVICE);
            Point outSize = new Point();
            wm.getDefaultDisplay().getSize(outSize);
            setPadding((int) (outSize.x * 0.1));
        }
        window.setGravity(gravity);
        window.getDecorView().setPadding(left, top, right, bottom);
        if (width != 0 && height != 0) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = width;
            lp.height = height;
            window.setAttributes(lp);
        }
        return dialog;
    }

    public void show(FragmentManager manager) {
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

    /**
     * 设置动画
     */
    public BaseFragDialogV2 setWindowAnimations(int a) {
        this.windowAnimations = a;
        return this;
    }

    /**
     * 无背景色
     */
    public BaseFragDialogV2 clearBack() {
        clearBack = true;
        return this;
    }

    /**
     * 设置位置
     */
    public BaseFragDialogV2 setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    /**
     * 设置内边距
     */
    public BaseFragDialogV2 setPadding(int p) {
        setPadding(p, p, p, p);
        return this;
    }
    /**
     * 设置内边距
     */
    public BaseFragDialogV2 setPadding(int l, int t, int r, int b) {
        this.left = l;
        this.top = t;
        this.right = r;
        this.bottom = b;
        return this;
    }

    /**
     * 设置宽高
     */
    public BaseFragDialogV2 setLayout(int w, int h) {
        this.width = w;
        this.height = h;
        return this;
    }

    /**
     * 从底部弹出，并且宽度撑满
     */
    public BaseFragDialogV2 setFromBottom() {
        setPadding(0);
        setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setGravity(Gravity.BOTTOM);
        setWindowAnimations(R.style.animation_bottom);
        return this;
    }

    /**
     * 设置后会在屏幕的左右留白
     */
    public BaseFragDialogV2 setFromCenter() {
        fromCenter = true;
        return this;
    }

    /**
     * 点击空白是否可以消失
     */
    public BaseFragDialogV2 cancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    /**
     * 正在显示
     */
    public boolean isShowing() {
        return super.isVisible();
    }


    public <T extends View> T findViewById(int r) {
        return mRoot.findViewById(r);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (cancelListener != null) {
            cancelListener.onCancel(dialog);
        }
    }

    /**
     * 消失的监听器
     */
    public BaseFragDialogV2 setDismissListener(Dialog.OnDismissListener listener) {
//        super.setOnDismissListener(listener);
        this.cancelListener = cancelListener;
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.remove(this.getClass());
    }
}
