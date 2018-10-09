package com.tzj.baselib.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 *	对图片进行正方形截图
 * 	savePic保存图片
 */
public class CatPicView extends ImageView{
	
	/** 中心点*/
	private float x=-1;
	/** 中心点*/
	private float y=-1;
	
	/** 框原始大小*/
	private float length=200;
	
	private float rectTop ;
	private float rectBotton;
	private float rectLeft;
	private float rectRight;
	
	private Paint paint = new Paint();
	
	public CatPicView(Context context) {
		super(context);
		init();
	}

	public CatPicView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CatPicView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		paint.setFilterBitmap(true);
		paint.setDither(true);
//		paint.setColor(Color.parseColor("#ff000000"));
		paint.setAlpha(150);
		setClickable(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		MLog.i.running("onDraw-----"+getClass().getName());
		super.onDraw(canvas);
		
//		Drawable drawable = getDrawable();
//		if (drawable == null ) {
//			return ;
//		}
//		
//		if (getWidth()<=0||getHeight()<=0) {
//			return ;
//		}
//		Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
//		float xPoint = (getWidth()-bitmap.getWidth())/2+(getWidth()-bitmap.getWidth())%2;
//		float yPoint = (getHeight()-bitmap.getHeight())/2+(getHeight()-bitmap.getHeight())%2;
//		canvas.drawBitmap(bitmap, xPoint,yPoint, null);
		
		if (x==-1&&y==-1) {
			x=getWidth()/2;
			y = getHeight()/2;
		}
		
		if (length > canvas.getWidth()) {
			length = canvas.getWidth();
		}
		if (length < 50) {
			length = 50;
		}
		//=========================固定框的范围(大小)
		float bian = length/2+length%2;
		if (x<= bian) {
			x = bian;
		}
		if (x>=getWidth()-bian) {
			x = getWidth()-bian;
		}
		if (y<=bian) {
			y=bian;
		}
		if (y>=getHeight()-bian) {
			y = getHeight()-bian;
		}
		//============================================画出阴影内容
		rectTop = y-(length/2+length%2);
		rectBotton = y+(length/2+length%2);
		rectLeft = x-(length/2+length%2);
		rectRight = x+(length/2+length%2);
		
		RectF r= new RectF(0, 0, getWidth(), rectTop);
		canvas.drawRect(r, paint);
		r= new RectF(0, rectTop, rectLeft, rectBotton);
		canvas.drawRect(r, paint);
		r= new RectF(rectRight, rectTop, getWidth(), rectBotton);
		canvas.drawRect(r, paint);
		r= new RectF(0, rectBotton, getWidth(), getHeight());
		canvas.drawRect(r, paint);
	}
	/**
	 * 
	 * <p>name: getBitmap<／p>
	 * <p>Description: 得到选中部分的BitMap<／p>
	 * @return
	 * @return: Bitmap
	 */
	public Bitmap getBitmap() {
		setDrawingCacheEnabled(true);
		buildDrawingCache();
		Bitmap bitmap = getDrawingCache();
		Bitmap createBitmap = Bitmap.createBitmap(bitmap, (int)rectLeft, (int)rectTop, (int)(rectRight-rectLeft), (int)(rectBotton-rectTop));
		destroyDrawingCache();
		return createBitmap;
	}
	
	private float xPoint;
	private float yPoint;
	private float xlength;
	private float ylength;
	private int count;
	/**
	 * 要调用此处的onTouchEvent图框才会动
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if(event.getPointerCount() == 2){
			if (action == MotionEvent.ACTION_POINTER_2_DOWN) {
				count = 2;
				xlength = Math.abs(event.getX(0)-event.getX(1));
				ylength = Math.abs(event.getY(0)-event.getY(1));
			}
			if (action == MotionEvent.ACTION_MOVE) {
				length += ((Math.abs(event.getX(0)-event.getX(1)) - xlength) +(Math.abs(event.getY(0)-event.getY(1)) - ylength))/2;
				xlength = Math.abs(event.getX(0)-event.getX(1));
				ylength = Math.abs(event.getY(0)-event.getY(1));
				this.invalidate();
			}

		}
		if(event.getPointerCount() == 1){
			if (action == MotionEvent.ACTION_DOWN) {
				xPoint = event.getX();
				yPoint = event.getY();
				if (count > 1) {
					count--;
				}
			}
			
			if (action == MotionEvent.ACTION_MOVE) {
				if (count > 1) {
					return super.onTouchEvent(event);
				}
				x += event.getX()-xPoint;
				y += event.getY()-yPoint;
				xPoint = event.getX();
				yPoint = event.getY();
				this.invalidate();
			}
			if (action == MotionEvent.ACTION_UP) {
				xPoint = event.getX();
				yPoint = event.getY();
			}
			
		}
		return super.onTouchEvent(event);
	}
}
