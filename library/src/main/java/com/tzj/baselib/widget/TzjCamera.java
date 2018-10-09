package com.tzj.baselib.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.List;

/**
 *  注意：用完别忘了关闭相机资源{@link TzjCamera#closeCamera()}}
 * <p>uses-permission android:name="android.permission.CAMERA"/>
 * <p>uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 * <p>uses-permission android:name="android.permission.RECORD_AUDIO"/>
 * <p></p>
 * <p>uses-feature android:name="android.permission.CAMERA"/>
 * <p>uses-feature android:name="android.hardware.camera"/>
 * <p>uses-feature android:name="android.hardware.camera.autofocus"/>
 */
public class TzjCamera extends FrameLayout implements PictureCallback{
	private Camera mCamera;
	private MyCameraPreview mPreview;
	
	private byte[] data;//最近一次拍到的图片内容
	/**正在拍照*/
	private boolean isTake = false;
	
	public TzjCamera(Context context) {
		super(context);
		init();
	}

	public TzjCamera(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TzjCamera(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	
	private void init(){
	}
	//==============================================================================
	/**
	 * 打开相机
	 * 0--主相机
	 * 1--从相机
	 * 用完别忘了关闭相机资源{@link TzjCamera#closeCamera()}}
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public boolean openCamera(int i){
		int num = Camera.getNumberOfCameras();
		if (i<0||i>num-1) {
			return false;
		}
		if (mCamera!=null) {
			return true;
		}
		try {
			mCamera = Camera.open(i);
			if (mCamera != null) {
				initCamera();
				Mlog.running("打开相机");
				setFocus(Parameters.FOCUS_MODE_AUTO);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 对焦
	 * <p>AutoFocusCallback——对焦完成后你想干什么
	 */
	public void focus(final AutoFocusCallback callBack){
		if (mCamera==null || isTake) {
			return ;
		}
		Mlog.running("对焦");
		if (callBack!=null) {
			mCamera.autoFocus(callBack);
		}else {
			mCamera.autoFocus(new AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean success, Camera camera) {
					Mlog.running("对焦完成");
				}
			});
		}
	}
	/**
	 * 拍照
	 * <p>focusCallback——在对焦完成拍照之前给个机会给你干你想干的事
	 * <p>PictureCallback——拍照完成后你想干什么(可以为空)
	 * <p>注意：PictureCallback 中做完你的事后你要调用一下{@link TzjCamera#startPreview(Camera)}
	 * <p>注意：当PictureCallback为空时你调用{@link TzjCamera#getBitmap(byte[])}你应当等待拍照完成，不然的话你将得到上次拍照的内容/NULL
	 */
	public void takePicture(final AutoFocusCallback focusCallback,final PictureCallback callback){
		if (mCamera == null || isTake) {
			return ;
		}
		focus(new AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				if (focusCallback != null) {
					focusCallback.onAutoFocus(success, camera);
				}
				if (mCamera!=null ) {
					isTake = true;
					Mlog.running("拍照");
					if (callback == null) {
						data = null;
						mCamera.takePicture(null, null, TzjCamera.this);
					}else {
						mCamera.takePicture(null, null, callback);
					}
				}
			}
		});
	}
	/**
	 * 关闭相机资源
	 */
	public void closeCamera(){
		if (mCamera!=null) {
			Mlog.running("关闭相机资源");
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
			removeView(mPreview);
		}
	}
	/**
	 * bytes————当为NULL时返回最近一次你未处理的data对应的Bitmap
	 */
	public Bitmap getBitmap(byte[] bytes){
		if (bytes == null) {
			bytes = data;
		}
		if (bytes == null) {
			return null;
		}
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}
	
	/**
	 * 开始预览
	 */
	public void startPreview(Camera camera){
		Mlog.running("预览");
		try {
			camera.startPreview();
			isTake = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 变焦
	 */
	public void setZoom(int size){
		if (mCamera==null||!isSupportZoom()) {
			return ;
		}
		Parameters params = mCamera.getParameters();
        final int MAX = params.getMaxZoom();
        int zoomValue = params.getZoom();
        zoomValue +=size;
        if (zoomValue>MAX) {
        	zoomValue = MAX;
		}else if(zoomValue<0){
			zoomValue = 0;
		}
        params.setZoom(zoomValue);
        mCamera.setParameters(params);
	}
	/**
	 * 设置闪光类型
	 * @param mode	Parameters.FLASH_MODE_
	 * @return: void
	 */
	public void setFlash(String mode){
		if (mCamera==null) {
			return ;
		}
		Parameters parameters = mCamera.getParameters();
		
		List<String> flashModes = parameters.getSupportedFlashModes();
		// Check if camera flash exists
		if (flashModes == null ||flashModes.size() == 0) {
			return;
		}
		String flashMode = parameters.getFlashMode();
		if (flashMode.equals(mode)) {
			return ;
		}
		// Turn on the flash
		if (flashModes.contains(mode)) {
			parameters.setFlashMode(mode);
			mCamera.setParameters(parameters);
		}
	}
	/**
	 * 设置对交类型
	 * @param mode	Parameters.FOCUS_MODE_
	 * @return: void
	 */
	public void setFocus(String mode){
		if (mCamera==null) {
			return ;
		}
		Parameters parameters = mCamera.getParameters();
		
		List<String> focusModes = parameters.getSupportedFocusModes();
		// Check if camera flash exists
		if (focusModes == null ||focusModes.size() == 0) {
			return;
		}
		String focusMode = parameters.getFocusMode();
		if (focusMode.equals(mode)) {
			return ;
		}
		if (focusModes.contains(mode)) {
			parameters.setFlashMode(mode);
			mCamera.setParameters(parameters);
		}
	}
	
	MediaPlayer shootMP;
	/**
	 * 播放系统拍照声音
	 */
	public void shootSound() {
		try {
			if (shootMP == null) {
//				shootMP = new MediaPlayer();
//				shootMP.setDataSource("file:///system/media/audio/ui/camera_click.ogg");
//				shootMP.setVolume(10, 10);
//				shootMP.prepare();
				shootMP = MediaPlayer.create(getContext(), Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
				shootMP.setAudioStreamType(AudioManager.STREAM_MUSIC);
			}else {
				shootMP.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	//=====================================================================
	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		this.data = data;
		startPreview(camera);
	}
	
	private void initCamera(){
		Parameters param = mCamera.getParameters();
	    if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){ 
	    	mCamera.setDisplayOrientation(90); 
	    }else{
	    	mCamera.setDisplayOrientation(0);               
	    }
	    
//	    //首先获取系统设备支持的所有颜色特效，有复合我们的，则设置；否则不设置 
//        List<String> colorEffects = param.getSupportedColorEffects(); 
//        Iterator<String> colorItor = colorEffects.iterator(); 
//        while(colorItor.hasNext()){ 
//            String currColor = colorItor.next(); 
//            if(currColor.equals(Camera.Parameters.EFFECT_SOLARIZE)){ 
//                param.setColorEffect(Camera.Parameters.EFFECT_SOLARIZE); 
//                break; 
//            } 
//        } 
//        //设置完成需要再次调用setParameter方法才能生效 
//        mCamera.setParameters(param);
	    
		mPreview = new MyCameraPreview(getContext(), mCamera);
		addView(mPreview);
	}
	/**
	 * 是否支持变焦
	 */
	private boolean isSupportZoom() {
		return mCamera.getParameters().isSmoothZoomSupported();
	}
	
	
	
	/**
	 * SurfaceView用于预览的的内部类
	 */
	class MyCameraPreview extends SurfaceView implements SurfaceHolder.Callback {
		
		private Camera mCamera;
		private SurfaceHolder mHolder;
		
		public MyCameraPreview(Context context,Camera c) {
			super(context);
			mCamera = c;
			mHolder = getHolder();
			mHolder.addCallback(this);
	        // 设置Surface不维护自己的缓冲区，而是等待屏幕的渲染引擎将内容推送到界面 在Android3.0之后弃用
	        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
	        	if (mCamera !=null) {
	            	mCamera.setPreviewDisplay(holder);
	                mCamera.startPreview();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			if (mHolder.getSurface()==null) {
				return ;
			}
			try {
				if (mCamera!=null) {
					mCamera.stopPreview();
					mCamera.setPreviewDisplay(mHolder);
					mCamera.startPreview();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			holder.removeCallback(this);
		}
	}
	private static class Mlog{
		public static void running(String str){
			Log.e("Camera:",str);
		}
	}
}














