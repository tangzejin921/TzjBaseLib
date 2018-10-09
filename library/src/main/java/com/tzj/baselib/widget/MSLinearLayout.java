package com.tzj.baselib.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

/**
 * 让LinearLayout可以像ios那样上下有弹性
 * NewApi  较高api用时请注意
*/
public class MSLinearLayout extends LinearLayout{
	
	float density;
	/**
	 * @param context
	 */
	public MSLinearLayout(Context context) {
		super(context);
		init();
	}

	public MSLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MSLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		density = getContext().getResources().getDisplayMetrics().density;
//		this.interpolator = new BounceInterpolator();
	}
	
	/** 记录y的增量*/
	private float mLastY = -1; 
	/** 记录x的增量*/
	private float mLastX = -1;
	
	/** 可以向上滑动？*/
	private boolean Btop = true;
	/** 可以向下滑动？*/
	private boolean Bbottom = true;
	/** 可以向左滑动？*/
	private boolean Bleft = true;
	/** 可以向右滑动？*/
	private boolean Bright = true;
	/** 防双指*/
	private boolean doublePoint = false;
	/** 是否拦截*/
	private boolean intercept = true;
	/** 执行move的多少次后拦截*/
	private int InterceptNum =2;
	
	/** 动画*/
	private DecelerateInterpolator interpolator = new DecelerateInterpolator(4);
	/** 动画时间*/
	private int time = 800;
	/** 上下移动的距离*/
	private int mCurrentY = 0;
	/** 左右移动的距离*/
	private int mCurrentX = 0;
	/** 初始位置*/
	private int oldY = 0;
	/** 初始位置*/
	private int oldX = 0;
	/** 滑动比例*/
	private static float offset_radto = 1.8f;
	
	/** 移动后返回原位置吗*/
	private boolean backY = true;
	/** 移动后返回原位置吗*/
	private boolean backX = true;
	/** 回退中*/
	private boolean backIng = false;
	private OnScroller scroller = new OnScroller() {
		@Override
		public boolean scrollerY(float dy, int totaly) {
			return true;
		}
		
		@Override
		public boolean scrollerX(float dx, int totalx) {
			return true;
		}
		
		@Override
		public void finish() {}
	};
	
	/**
	 * 滑动比例
	 */
	public void setRatio(float f){
		offset_radto = f;
	}
	/**
	 * 设置是否可以上下左右
	 */
	public void setDirection(boolean top,boolean bottom,boolean left,boolean right){
		this.Btop = top;
		this.Bbottom = bottom;
		this.Bleft = left;
		this.Bright = right;
	}
	/**
	 *  设置拦截情况
	 * @param intercapt 
	 * 			是否拦截
	 * @param num
	 * 			执行move的多少次后拦截
	 */
	public void setIntercept(boolean intercapt,int num){
		this.intercept = intercapt;
		this.InterceptNum = num;
//		this.InterceptTemp = 0;
	}
	
	/**
	 * 设置初始位置(相对)
	 */
	public void setROldPoint(int x,int y){
		setOldPoint(mCurrentX+x, mCurrentY+y);
	}
	/**
	 * 设置初始位置
	 */
	public void setOldPoint(int x,int y){
		oldY = x;
		mCurrentY = x;
		oldX = y;
		mCurrentX = y;
		requestLayout();
	}
	/**
	 * 位置(相对值)
	 */
	public void toRLocation(int x,int y){
		toLocation(mCurrentX+x, mCurrentY+y);
	}
	/**
	 * 位置(绝对值)
	 */
	public void toLocation(int x,int y){
		ValueAnimator animatorY ;
		ValueAnimator animatorX ;
		AnimatorListener listener = new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {}
			@Override
			public void onAnimationRepeat(Animator animation) {}
			@Override
			public void onAnimationEnd(Animator animation) {
				scroller.finish();
				backIng = false;
			}
			@Override
			public void onAnimationCancel(Animator animation) {}
		};
		if (Btop||Bbottom) {
			if (backY) {
				animatorY = ObjectAnimator.ofInt(this, "Y", mCurrentY,y);
				animatorY.setDuration((int)(time/density));
				animatorY.addUpdateListener(new AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						mCurrentY = (int) animation.getAnimatedValue();
						requestLayout();
					}
				});
				if (interpolator!=null) 
					animatorY.setInterpolator(interpolator);
				animatorY.addListener(listener);
				animatorY.start();
			}
		}
		if(Bleft||Bright) {
			if (backX) {
				animatorX = ObjectAnimator.ofInt(this, "X", mCurrentX,x);
				animatorX.setDuration((int)(time/density));
				animatorX.addUpdateListener(new AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						mCurrentX = (int) animation.getAnimatedValue();
						requestLayout();
					}
				});
				if (interpolator!=null) 
					animatorX.setInterpolator(interpolator);
				if (!Btop&&!Bright&&!backY) 
					animatorX.addListener(listener);
				animatorX.start();
			}
		}
		backIng = true;
	}
	/**
	 * 设置监听
	 * OnScroller 返回false将不回到原位置
	 */
	public void setonScroller(OnScroller scroller){
		if (scroller!=null) 
			this.scroller = scroller;
	}
	/**
	 * 设置动画性质及时间
	 * BounceInterpolator 弹跳插值器
	*/
	public void setAnima(DecelerateInterpolator interpolator,int time){
		if (interpolator!=null) 
			this.interpolator = interpolator;
		this.time = time;
	}
	/**
	 * 复原
	*/
	public void setRestore(){
		mCurrentY = oldY;
		mCurrentX = oldX;	
		requestLayout();
	}
	/**
	 * 得到移动跳高
	 */
	public int getDX(){
		return mCurrentX-oldX;
	}
	/**
	 * 得到移动跳高
	 */
	public int getDY(){
		return mCurrentY-oldY;
	}
	private int InterceptTemp=0;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (Btop||Bbottom) 
				mLastY = ev.getRawY();
			if(Bleft||Bright)
				mLastX = ev.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			if (++InterceptTemp > InterceptNum && intercept)
				return true;
			break;
		case MotionEvent.ACTION_UP:
			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	/**
	 * android:descendantFocusability="beforeDescend ants"
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return onMTouchEvent(ev) || super.onTouchEvent(ev);
	}
	
	public boolean onMTouchEvent(MotionEvent ev) {
		if (/*backIng||*/ev.getPointerCount()>1) {
			if (!doublePoint) {
				doublePoint = true;
			}
			return false;
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (Btop||Bbottom) 
				mLastY = ev.getRawY();
			if(Bleft||Bright) 
				mLastX = ev.getRawX();
			return true;
		case MotionEvent.ACTION_MOVE:
			if (doublePoint) {
				return true;
			}
			float temp;
			if (Btop||Bbottom) {
				float deltaY = ev.getRawY() - mLastY;
				mLastY = ev.getRawY();
				temp = deltaY / offset_radto;
				mCurrentY += temp;
				if (!Btop) {//不允许向上
					if (mCurrentY < oldY) {
						mCurrentY = oldY;
					}
				}
				if (!Bbottom) {
					if (mCurrentY > oldY) {
						mCurrentY = oldY;
					}
				}
				backY = scroller.scrollerY(temp, mCurrentY-oldY);
			}
			if(Bleft||Bright) {
				float deltaX = ev.getRawX() - mLastX;
				mLastX = ev.getRawX();
				temp = deltaX / offset_radto;
				mCurrentX += temp;
				if (!Bleft) {//不允许
					if (mCurrentX < oldX) {
						mCurrentX = oldX;
					}
				}
				if (!Bright) {
					if (mCurrentX > oldX) {
						mCurrentX = oldX;
					}
				}
				backX = scroller.scrollerX(temp, mCurrentX-oldX);
			}
			requestLayout();
			return true;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			InterceptTemp = 0 ;
			doublePoint = false;
			toLocation(oldX, oldY);
			break;
		}
		return false;
	}
	
	
	
	
	//=========================================================
	
	/**
	 * 布局子控件
	 */
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		int count = getChildCount();
		int left;
		int top;
		int right;
		int bottom;
		for (int i = 0; i < count; i++) {
			View v = getChildAt(i);
	        left = v.getLeft();
	        top = v.getTop();
	        right = v.getRight();
	        bottom = v.getBottom();
	        v.layout(left+mCurrentX, top+mCurrentY, right+mCurrentX-oldX, bottom+mCurrentY-oldY);
		}
	}
	
	
	public interface OnScroller{
		/**
		 * totaly>0上，totaly<0下
		 * 再结合dy,可得当前是什么情况
		 * (dy是增量)
		 * @return 是否回原位置
		*/
		boolean scrollerY(float dy, int totaly);
		/**
		 * totalx>0上，totalx<0下
		 * 再结合dx,可得当前是什么情况
		 * (dx是增量)
		 * @return 是否回原位置
		*/
		boolean scrollerX(float dx, int totalx);
		void finish();
	}
	
	
	
}




