
package com.tzj.baselib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * 滑动时通知外部,可以实现拉链的效果
 */
public class DiscrollView extends ScrollView {

    private ViewGroup mContent;

    public DiscrollView(Context context) {
        super(context);
    }

    public DiscrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiscrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setupFirstView();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContent = (ViewGroup) getChildAt(0);
    }

    private void setupFirstView() {
        View first = mContent.getChildAt(0);
        if (first != null) {
            first.getLayoutParams().height = getHeight();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        onScrollChanged(t);
    }

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    private int getAbsoluteBottom() {
        View last = getChildAt(getChildCount() - 1);
        if (last == null) {
            return 0;
        }
        return last.getBottom();
    }

    private void onScrollChanged(int top) {
        int scrollViewHeight = getHeight();
        int scrollViewBottom = getAbsoluteBottom();
        int scrollViewHalfHeight = scrollViewHeight / 2;

        for (int index = 1; index < mContent.getChildCount(); index++) {
            View child = mContent.getChildAt(index);
            if (!(child instanceof Discrollvable)) {
                continue;
            }
            Discrollvable discrollvable = (Discrollvable) child;
            int discrollvableTop = child.getTop();
            int discrollvableHeight = child.getHeight();
            int discrollvableAbsoluteTop = discrollvableTop - top;

            if (scrollViewBottom - child.getBottom() < discrollvableHeight + scrollViewHalfHeight) {
                if (discrollvableAbsoluteTop <= scrollViewHeight) {
                    int visibleGap = scrollViewHeight - discrollvableAbsoluteTop;
                    discrollvable.onDiscrollve(clamp(visibleGap / (float) discrollvableHeight,
                            0.0f, 1.0f));
                } else {
                    discrollvable.onResetDiscrollve();
                }
            } else {
                if (discrollvableAbsoluteTop <= scrollViewHalfHeight) {
                    int visibleGap = scrollViewHalfHeight - discrollvableAbsoluteTop;
                    discrollvable.onDiscrollve(clamp(visibleGap / (float) discrollvableHeight,
                            0.0f, 1.0f));
                } else {
                    discrollvable.onResetDiscrollve();
                }
            }
        }
    }
    
    public interface Discrollvable {
        void onDiscrollve(float ratio);
        void onResetDiscrollve();
    }
    
}
