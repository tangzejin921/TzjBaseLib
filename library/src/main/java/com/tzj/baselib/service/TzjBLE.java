package com.tzj.baselib.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.tzj.baselib.utils.UtilTo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 *  uses-permission android:name="android.permission.BLUETOOTH"/>
 * <p> uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
 * <p>注意：用完之后别忘了关闭
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("MissingPermission")
public class TzjBLE {
	private static TzjBLE mBle;
	/**
	 * 它可以得到	BluetoothAdapter
	 */
	private BluetoothManager mBluetoothManager;//它可以得到	BluetoothAdapter
	/**
	 * 它可以对蓝牙联接方面的操作
	 * 如	开，关，扫描
	 */
	private BluetoothAdapter mBluetoothAdapter;
	private String mAddress;
	/**
	 * 以Gatt方式
	 * 联接，断开，读，写
	 */
	private BluetoothGatt mBluetoothGatt;
	private Application mContext;
	

	private LeScanCallback mScanCallback;
	private BluetoothGattCallback mGattCallback;
	//=================================================================================
	private TzjBLE(Application ctx){
		mContext = ctx;
	}
	
	public static TzjBLE get(Application ctx){
		if (mBle == null) {
			mBle = new TzjBLE(ctx);
		}
		return mBle;
	}
	public void clear(){
		closeGatt();
		closeBluetooth();
		mBle = null;
	}
	/** 打开失败有可能没有蓝牙设备*/
	public static final int OPEN_RET_ERR = -1;
	/** 打开成功但不支持BLE*/
	public static final int OPEN_RET_UNSUPPORT_BLE = 0;
	/** 打开成功并支持BLE*/
	public static final int OPEN_RET_SUPPORT_BLE = 1;
	/**
	 * 打开蓝牙
	 * <p>		OPEN_RET_ERR--不支持蓝牙/打开失败 
	 * <p>		OPEN_RET_UNSUPPORT_BLE---不支持蓝牙BLE
	 * <p>		OPEN_RET_SUPPORT_BLE---支持蓝牙BLE
	 * @return
	 * @return: int
	 */
	public int openBluetooth(){
		if (isOpen()) {
			return OPEN_RET_SUPPORT_BLE;
		}
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
        	Mlog.running("你的设备不支持BLE");
            Toast.makeText(mContext, "你的设备不支持BLE", Toast.LENGTH_SHORT).show();
            return OPEN_RET_UNSUPPORT_BLE;
        }
        
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Mlog.running("你的设备不支持蓝牙");
				Toast.makeText(mContext, "你的设备不支持蓝牙", Toast.LENGTH_SHORT).show();
				return OPEN_RET_ERR;
			}
		}
		
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Mlog.running("你的设备不支持蓝牙");
			Toast.makeText(mContext, "你的设备不支持蓝牙", Toast.LENGTH_SHORT).show();
			return OPEN_RET_ERR;
		}
		
//        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//        	Mlog.running("你的设备不支持BLE");
//            Toast.makeText(mContext, "你的设备不支持BLE", Toast.LENGTH_SHORT).show();
//            ret = OPEN_RET_UNSUPPORT_BLE;
//        }else {
//        	Mlog.running("你的设备支持BLE");
//			ret = OPEN_RET_SUPPORT_BLE;
//		}
		if (mBluetoothAdapter.enable()) {
			Mlog.running("打开蓝牙成功");
			initCallback();
			return OPEN_RET_SUPPORT_BLE;
		}else {
			Mlog.running("打开蓝牙失败");
			return OPEN_RET_ERR;
		}
	}
	public BluetoothAdapter getBluetoothAdapter() {
		return mBluetoothAdapter;
	}
	public BluetoothGatt getBluetoothGett() {
		return mBluetoothGatt;
	}
	
	/**
	 * 关闭
	 * @return
	 * @return: boolean
	 */
	public boolean closeBluetooth(){
		closeGatt();
		if (mBluetoothAdapter != null) {
			Mlog.running("关闭蓝牙");
			return mBluetoothAdapter.disable();
		}
		return false;
	}
	private boolean mScanning = false;
	/**
	 * 是否正在扫描
	 */
	public boolean isScan(){
//		return mBluetoothAdapter.isDiscovering();
		return mScanning;
	}
	/**
	 * 蓝牙是否打开
	 */
	public boolean isOpen() {
		return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
	}
	
	/**
	 * 扫描非扫描的相反操作
	 * <p>在它之前你应该调用{@link TzjBLE#addOnGattCallBack(OnGattCallBack)}
	 * @param uuid 为空时扫描所有(不为空时扫描不出来不知为什么)
	 * @return
	 * @return: boolean true--扫描
	 * 					false--非扫描
	 */
	public boolean scanStartOrStop(String[] uuid){
		if (mScanning) {
			stopLeScan();
		}else{
			startLeScan(uuid);
		}
		return mScanning;
	}
	
	private Handler mHandler = new Handler();
	
	/**
	 * 扫描
	 * <p>uuid 为空时扫描所有(不为空时扫描指定的但有问题)
	 * <p>注意：在它之前你应该调用{@link TzjBLE#addOnGattCallBack(OnGattCallBack)}
	 * <p>说明:默认情况下，扫描一分钟后将结束扫描
	 */
	public boolean startLeScan(final String[] uuid){
		if (mBluetoothAdapter!=null /*&& callBack != null*/) {
			if (!mScanning) {
				mScanning = true;
				Mlog.running("开始扫描");
//				mHandler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						stopLeScan();
//						mHandler.removeCallbacks(this);
//					}
//				}, 60000);
				if (uuid!=null) {
					UUID[] serviceUuids = new UUID[uuid.length];
					for (int i = 0; i < uuid.length; i++) {
						serviceUuids[i] = UUID.fromString(uuid[i]);
					}
					return mBluetoothAdapter.startLeScan(serviceUuids, mScanCallback);
				}
				return mBluetoothAdapter.startLeScan(mScanCallback);
			}
		}
		return false;
	}
	/**
	 * 结束扫描
	 */
	public void stopLeScan(){
		if (mBluetoothAdapter!=null /*&& callBack != null*/) {
			if (mScanning) {
				mScanning = false;
				Mlog.running("结束扫描");
				mBluetoothAdapter.stopLeScan(mScanCallback);
			}
		}
	}
	/**
	 * 在它之前你应该调用{@link TzjBLE#scanStartOrStop(String[])}
	 * <p>或是{@link TzjBLE#startLeScan(String[])}
	 * <p>等回调完成后有了BluetoothDevice之后调用
	 */
	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null) {
			Mlog.running("openBluetooth ERR");
			return false;
		}

		// Previously connected device. Try to reconnect.
		if (mAddress != null && address.equals(mAddress) && mBluetoothGatt != null) {
			Mlog.running("使用已有的连接");
			if (mBluetoothGatt.connect()) {
				mGattCallback.onConnectionStateChange(mBluetoothGatt, BluetoothGatt.GATT_SUCCESS, BluetoothProfile.STATE_CONNECTED);
				Mlog.running("连接成功");
				return true;
			} else {
				Mlog.running("连接失败");
				return false;
			}
		}
		final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		if (device == null) {
			Mlog.running("无此设备，连接失败");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		Mlog.running("创建新的联接");
		mBluetoothGatt = device.connectGatt(mContext, true, mGattCallback);//触 发onConnectionStateChange
		
		if (mBluetoothGatt!=null) {
			mAddress = address;
			return true;
		}
		return false;
	}

	/**
	 * Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Mlog.running("openBluetooth ERR");
			return;
		}
		Mlog.running("断开连接");
		mBluetoothGatt.disconnect();
	}

	/**
	 * 关闭GATT连接
	 * After using a given BLE device, the app must call this method to ensure
	 * resources are released properly.
	 */
	public void closeGatt() {
		stopLeScan();
		if (mBluetoothGatt == null) {
			return;
		}
		Mlog.running("关闭GATT");
		disconnect();
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}
	/**
	 * 读操作
	 * Request a read on a given {@code BluetoothGattCharacteristic}. The read
	 * result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
	 * callback.
	 * 
	 * @param characteristic
	 *            The characteristic to read from.
	 */
	public boolean readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Mlog.running("openBluetooth ERR");
			return false;
		}
		Mlog.running("读操作:"+ UtilTo.toASCString(characteristic.getValue()));
		return mBluetoothGatt.readCharacteristic(characteristic);
	}
	public boolean readDescriptor(BluetoothGattDescriptor descriptor) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Mlog.running("openBluetooth ERR");
			return false;
		}
		Mlog.running("读操作:"+ UtilTo.toASCString(descriptor.getValue()));
		return mBluetoothGatt.readDescriptor(descriptor);
	}
	/**
	 * 写操作
	 */
	public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Mlog.running("openBluetooth ERR");
			return false;
		}
		Mlog.running("写操作:"+new String(characteristic.getValue()));
		return mBluetoothGatt.writeCharacteristic(characteristic);
	}
	
	public boolean writeDescriptor(BluetoothGattDescriptor descriptor) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Mlog.running("openBluetooth ERR");
			return false;
		}
		Mlog.running("写操作:"+new String(descriptor.getValue()));
		return mBluetoothGatt.writeDescriptor(descriptor);
	}
	/**
	 * 是否启用通知
	 * 设置当characteristic改变时通知，用来从外设获得发来的值
	 * Enables or disables notification on a give characteristic.
	 * 
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 */
	public boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Mlog.running("openBluetooth ERR");
			return false;
		}
		Mlog.running("setCharacteristicNotification :"+enabled);
		return mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
	}



	/**
	 * Retrieves a list of supported GATT services on the connected device. This
	 * should be invoked only after {@code BluetoothGatt#discoverServices()}
	 * completes successfully.
	 * 
	 * @return A {@code List} of supported services.
	 */
	public List<BluetoothGattService> getSupportedGattServices() {
		if (mBluetoothGatt == null)
			return null;
		return mBluetoothGatt.getServices();
	}
	
	/**
	 * 配对
	 * @return
	 * @return: boolean
	 */
	public static boolean createBond(BluetoothDevice device){
		if (device==null) {
			return false;
		}
		Class<BluetoothDevice> c = BluetoothDevice.class;
		Method method;
		try {
			method = c.getMethod("createBond");
			method.setAccessible(true);
			Mlog.running("弹出配对框");
			return (Boolean) method.invoke(device);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false ;
	}
	/**
	 * 感觉没效果
	 * @param device
	 * @param pin
	 * @return
	 * @return: boolean
	 */
	
	public static boolean setPin(BluetoothDevice device,String pin){
		if (device==null||pin == null) {
			return false;
		}
		try {
			Class<BluetoothDevice> c = BluetoothDevice.class;
			Method method = c.getMethod("setPin", new Class[]{byte[].class});
			method.setAccessible(true);
			Mlog.running("设置配对pin："+pin);
			return (Boolean) method.invoke(device, new Object[]{pin.getBytes("UTF-8")});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 取消输入
	 * @param device
	 * @return
	 * @return: boolean
	 */
	public static boolean cancelPairingUserInput(BluetoothDevice device){
		if (device==null) {
			return false;
		}
		try {
			Class<BluetoothDevice> c = BluetoothDevice.class;
			Method method = c.getMethod("cancelPairingUserInput");
			method.setAccessible(true);
			Mlog.running("取消输入");
			return (Boolean) method.invoke(device);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 取消配对
	 * @param device
	 * @return
	 * @return: boolean
	 */
	public static boolean cancelBondProcess(BluetoothDevice device){
		if (device==null) {
			return false;
		}
		try {
			Class<BluetoothDevice> c = BluetoothDevice.class;
			Method method = c.getMethod("cancelBondProcess");
			method.setAccessible(true);
			Mlog.running("取消配对框");
			return (Boolean) method.invoke(device);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	//===================================================================================
	private void initCallback(){
		mScanCallback = new LeScanCallback() {
			@Override
			public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {//scanRecord[29] 是什么
				for (int i = 0; i < listCallBack.size(); i++) {
					listCallBack.get(i).onLeScan(device, rssi, scanRecord);
				}
//				callBack.onLeScan(device, rssi, scanRecord);
			}
		};
		
		mGattCallback = new BluetoothGattCallback() {
			
			/**
			 * 连接状态改变的回调
			 */
			
			@Override
			public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
				if (newState == BluetoothProfile.STATE_CONNECTED) {
					Mlog.running("连接成功");
					for (int i = 0; i < listCallBack.size(); i++) {
						listCallBack.get(i).onConnect(gatt, status, newState);
					}
//					callBack.onConnect(gatt, status, newState);
					mBluetoothGatt.discoverServices();//搜索连接设备所支持的service。触发 onServicesDiscovered
				} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
					closeGatt();
					for (int i = 0; i < listCallBack.size(); i++) {
						listCallBack.get(i).onDisconnect(gatt, status, newState);
					}
//					callBack.onDisconnect(gatt, status, newState);
				}else {
					Mlog.running("onConnectionStateChange status: " + status+"--newState:"+newState);
				}
			}
			/**
			 * 发现
			 */
			@Override
			public void onServicesDiscovered(BluetoothGatt gatt, int status) {
				if (status == BluetoothGatt.GATT_SUCCESS) {
					for (int i = 0; i < listCallBack.size(); i++) {
						listCallBack.get(i).onServiceDiscover(gatt, status,getSupportedGattServices());
					}
//					callBack.onServiceDiscover(gatt, status,getSupportedGattServices());
				} else {
					Mlog.running("onServicesDiscovered received: " + status);
				}
			}
			/**
			 * 读事件的回调
			 */
			@Override
			public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			    Mlog.running("BondState:"+gatt.getDevice().getBondState());
				for (int i = 0; i < listCallBack.size(); i++) {
					listCallBack.get(i).onCharacteristicRead(gatt, characteristic, status);
				}
//				callBack.onCharacteristicRead(gatt, characteristic, status);
			}
			@Override
			public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
				for (int i = 0; i < listCallBack.size(); i++) {
					listCallBack.get(i).onCharacteristicWrite(gatt, characteristic, status);
				}
//				callBack.onCharacteristicWrite(gatt, characteristic, status);
			}

			/**
			 * 设备发来notification回调函数
			 */
			@Override
			public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
				for (int i = 0; i < listCallBack.size(); i++) {
					listCallBack.get(i).onCharacteristicChanged(gatt, characteristic);
				}
//				callBack.onCharacteristicChanged(gatt, characteristic);
			}
		};
	}
	//=================================================================================
	
	private List<OnGattCallBack> listCallBack = new ArrayList<>();
	
	//要放在扫描{@link TzjBLE#scanStartOrStop( String[])}或是{@link TzjBLE#startLeScan( String[])}之前
	/**
	 * 要在{@link TzjBLE#scanStartOrStop( String[])}或是{@link TzjBLE#startLeScan( String[])}之前至少调用一次
	 * @param gattCallBack
	 * @return: void
	 */
	public void addOnGattCallBack(OnGattCallBack gattCallBack) {
		if (gattCallBack!=null) {
			listCallBack.add(gattCallBack);
//			callBack = gattCallBack;
		}
	}
	public void removeOnGattCallBack(OnGattCallBack gattCallBack) {
		if (gattCallBack!=null) {
			listCallBack.remove(gattCallBack);
		}
	}
	
	
	
	
	
	
	public interface OnGattCallBack {
		/**
		 * 扫描的回调接口
		 */
		void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord);//scanRecord[29] 是什么
		
		/** 
		 * 	连接成功回调
		 * 	<p>newState已经判断过了为
		 * 	<p>BluetoothProfile.STATE_CONNECTED
		 */
		void onConnect(BluetoothGatt gatt, int status, int newState);

		/** 
		 * 	断开连接回调
		 * 	<p>newState已经判断过了为
		 * 	<p>BluetoothProfile.STATE_DISCONNECTED
		 */
		void onDisconnect(BluetoothGatt gatt, int status, int newState);
		
//		for (int i = 0; i < listService.size(); i++) {
//			BluetoothGattService service = listService.get(i);
//			if (service.getUuid() == null) {
//				
//			}
//			List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
//			for (int j = 0; j < characteristics.size(); j++) {
//				BluetoothGattCharacteristic characteristic = characteristics.get(j);
//				if (characteristic.getUuid() == null) {
//
//				}
//				List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
//		
//				TzjBLE.setCharacteristicNotification(characteristic, true);
//				for (int k = 0; k < descriptors.size(); k++) {
//					BluetoothGattDescriptor descriptor = descriptors.get(k);
//					if (descriptor.getUuid() == null) {//通知
//						descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//						TzjBLE.writeDescriptor(descriptor);
//					}
//				}
//			}
//		}
		/** 
		 * 	发现新连接回调 由 discoverServices触发
		 * 	<p>status已经判断过了为
		 * 	<p>BluetoothGatt.GATT_SUCCESS
		 * 	<p>搜索到BLE终端服务的事件
	     * 	<p>(发现BLE终端的Service时回调)
		 */
		void onServiceDiscover(BluetoothGatt gatt, int status, List<BluetoothGattService> listService);

		/** 读的回调 */
		void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status);

		/** 写的回调 */
		void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status);

		/**notification 回调 */
		void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);
	}

	private static class Mlog{
		public static void running(String str){
			Log.e("BLE:",str);
		}
	}

}
