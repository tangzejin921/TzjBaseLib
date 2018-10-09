package com.tzj.baselib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 这是一个滑动删除的类   在listview 因复用view导致高度问题还没解决
 * 放在listView 中 onitemclick无效这点注意
 * 可以滑动就无法点击,好像两者无法同时存在，等高人解答
 * 另外一点当超过屏幕存在view时也使得无法点击
 * 如果listview 要想加onitemclick的话要么想办法在adapter上加
 * 要么自己重写listview类，我是不想多一个类出来
 * 
 */
public class MDLinearLayout extends LinearLayout {

	private float mLastMotionX;// 记住上次触摸屏的位置
	private int dX;
	private int back_width;//滑动显示组件的宽度
//	private float downX;
//	private int itemClickMin = 5;//判断onItemClick的最大距离
	
	public MDLinearLayout(Context context) {
		this(context, null);
	}

	public MDLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@SuppressLint("NewApi")
	private void init(Context context) {
		if (getChildCount()<=1) {
			return ;
		}
		setOrientation(HORIZONTAL);
		addOnLayoutChangeListener(new OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
					int oldBottom) {
				gone();
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int count = getChildCount();
		if (count<=1) {
			return;
		}
		int measuredHeight = getChildAt(0).getMeasuredHeight();
		for (int i = 0; i < count; i++) {
			if (i > 0) {
				View childAt = getChildAt(i);
				if (childAt.getVisibility()==View.GONE) {
					continue;
				}
				android.view.ViewGroup.LayoutParams lp = childAt.getLayoutParams();
				lp.height = measuredHeight;
				childAt.setLayoutParams(lp);
				measureChild(getChildAt(i), widthMeasureSpec, measuredHeight);
				back_width += getChildAt(i).getMeasuredWidth();
			}else{
				back_width=0;
			}
		}
	}
	
	public void gone(){
		scrollTo(0, 0);
	}
	public void show(int s){
		scrollTo(s, 0);
	}
	public void showBy(int s){
		scrollBy(s, 0);
	}
	
	private float mMove;// 记住上次触摸屏的位置
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		float x = ev.getX();
		if (ev.getAction()==MotionEvent.ACTION_DOWN) {
			mMove = x;
		}else if (ev.getAction()==MotionEvent.ACTION_MOVE) {
			if (Math.abs(mMove-x)>5) {
				return true;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getChildCount()<=1) {
			return super.onTouchEvent(event);
		}
		float x = event.getX();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
//			downX = x;
			return true;
		case MotionEvent.ACTION_MOVE:
			dX = (int) (mLastMotionX - x);
			mLastMotionX = x;
			int scrollx = getScrollX() + dX;
			if (scrollx > 0 && scrollx < back_width) {
				showBy(dX);
				getParent().requestDisallowInterceptTouchEvent(true);
			} else if (scrollx > back_width) {
				show(back_width);
			} else if (scrollx < 0) {
				gone();
			}
			break;
		case MotionEvent.ACTION_UP:
//			if (Math.abs(x - downX) < itemClickMin) {// 这里根据点击距离来判断是否是itemClick
//				return false;
//			}
			int scroll = getScrollX();
			if (dX > 0) {
				if (scroll > back_width / 4) {
					show(back_width);
				} else {
					gone();
				}
			} else {
				if (scroll > back_width * 3 / 4) {
					show(back_width);
				} else {
					gone();
				}
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			gone();
			break;
		}
		return super.onTouchEvent(event);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		int margeLeft = 0;
		int size = getChildCount();
		for (int i = 0; i < size; i++) {
			View view = getChildAt(i);
			if (view.getVisibility() != View.GONE) {
				int childWidth = view.getMeasuredWidth();
				// 将内部子孩子横排排列
				view.layout(margeLeft, 0, margeLeft + childWidth,getChildAt(0).getMeasuredHeight());
				margeLeft += childWidth;
			}
		}
	}
}
