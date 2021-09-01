package com.tzj.baselib.widget;

import android.content.Context;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Switch;

import com.tzj.baselib.R;


/**
 * １．区分是手动点击还是代码设置的
 * ２．设置点击事件触发点击事件 -> 异步处理逻辑- > clickToggle
 */
public class MySwitch extends Switch {
    /**
     * 是否可以手动点击
     */
    private boolean toggleAble = true;

    public MySwitch(Context context) {
        this(context, null);
    }

    public MySwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 请用clickToggle代替此方法
     * 代码直接调用时 isClickAndTouch 将返回true
     * 如果设置了点击事件此方法调用将无效,请调用clickToggle
     */
    @Deprecated
    @Override
    public void toggle() {
        if (toggleAble) {
            clickToggle();
        }
    }

    /**
     * 代码直接调用时 isClickAndTouch 将返回true
     */
    public void clickToggle() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            post(this::clickToggle);
            return;
        }
        setTag(R.id.switch_tag, true);
        super.toggle();
    }

    /**
     * 代码直接调用时 isClickAndTouch 将返回false
     */
    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
    }

    /**
     * 是否是手动触发的,
     * 注意：调用一次有效
     */
    public boolean isClickAndTouch() {
        boolean b = getTag(R.id.switch_tag) == Boolean.TRUE;
        setTag(R.id.switch_tag, null);
        return b;
    }

    /**
     * 设置点击后将无法滑动,也不会响应toggle
     * 需要手动调用 setChecked
     */
    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        setToggleAble(onClickListener == null);
        super.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onFilterTouchEventForSecurity(MotionEvent event) {
        //这里防止设置了不可点击但是可以滑动
        if (!toggleAble && event.getAction() == MotionEvent.ACTION_MOVE) {
            return false;
        }
        return super.onFilterTouchEventForSecurity(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //这里处理滑动超出view范围情况
                boolean b = getTag(R.id.switch_tag) == Boolean.TRUE;
                if (!b) {
                    setTag(R.id.switch_tag, true);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 点击是否有效
     */
    public MySwitch setToggleAble(boolean toggleAble) {
        this.toggleAble = toggleAble;
        return this;
    }
}
