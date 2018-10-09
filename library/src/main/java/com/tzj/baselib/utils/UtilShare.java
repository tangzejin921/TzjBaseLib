//package com.tzj.baselib.utils;
//
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.tencent.connect.share.QQShare;
//import com.tencent.connect.share.QzoneShare;
//import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.sdk.modelmsg.WXTextObject;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.Tencent;
//import com.tzj.baselib.env.AppEnv;
//
//import java.io.File;
//import java.util.ArrayList;
//
//a
//
//public class UtilShare {
//
//	/**
//	 * 分享qq
//	 * <uses-permission android:name="android.permission.INTERNET" />
//	 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
//		 	<activity
//	            android:name="com.tencent.tauth.AuthActivity"
//	            android:launchMode="singleTask"
//	            android:noHistory="true" >
//	            <intent-filter>
//	                <action android:name="android.intent.action.VIEW" />
//	                <category android:name="android.intent.category.DEFAULT" />
//	                <category android:name="android.intent.category.BROWSABLE" />
//	                <data android:scheme="tencent222222" />
//	            </intent-filter>
//	        </activity>
//	        <activity
//            	android:name="com.tencent.connect.common.AssistActivity"
//            	android:configChanges="orientation|keyboardHidden|screenSize"
//            	android:theme="@android:style/Theme.Translucent.NoTitleBar" />
//	 */
//	public static void shareQQ(Activity act,String appID,Bundle bundle,IUiListener listener){
//		Tencent mTencent = Tencent.createInstance(appID, act);
//		bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
//		bundle.putString(QQShare.SHARE_TO_QQ_TITLE, "标题");
//		bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "https://www.baidu.com/s?wd=这个不能为空");
//		bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
//		bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, "摘要");
//		bundle.putString(QQShare.SHARE_TO_QQ_SITE, "2222");
//		bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "在线一起走");
//		bundle.putString(QQShare.SHARE_TO_QQ_EXT_INT,  "其他附加功能");
//		mTencent.shareToQQ(act, bundle , listener);
//	}
//	/**
//	 * 分享空间
//	 */
//	public static void shareToQzone(Activity act, String appID, Bundle bundle, IUiListener listener) {
//		Tencent mTencent = Tencent.createInstance(appID, act);
//		bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
//		bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题");
//		bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "https://www.baidu.com/s?wd=这个不能为空");
//		bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "摘要");
//		bundle.putString(QzoneShare.SHARE_TO_QQ_SITE, "2222");
//		bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, "在线一起走");
//		bundle.putString(QzoneShare.SHARE_TO_QQ_EXT_INT,  "其他附加功能");
//		ArrayList<String> arrayList = new ArrayList<String>();
//		arrayList.add("http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
//		bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,arrayList);
//		mTencent.shareToQzone(act, bundle, listener);
//	}
//
//
//	/**
//	 * 微信
//	 */
//	public static void shareWX(Activity act,String appID,String text){
//		IWXAPI api = WXAPIFactory.createWXAPI(act, appID, true);
//    	api.registerApp(appID);
//
//    	WXTextObject textObj = new WXTextObject();
//		textObj.text = text;
//
//    	final WXMediaMessage msg = new WXMediaMessage();
//		msg.mediaObject = textObj;
//		msg.description = text;
//
//    	SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.transaction = "text"+System.currentTimeMillis();
//		req.message = msg;
//		req.scene = SendMessageToWX.Req.WXSceneSession;//或 微信圈SendMessageToWX.Req.WXSceneTimeline;
//		api.sendReq(req);
//	}
//
//	/**
//	 * 向其他app发送文字
//	 */
//	public static void shareStr(Activity act,String content){
//		Intent intent = new Intent(Intent.ACTION_SEND);
//	    intent.putExtra(Intent.EXTRA_TEXT, content); //正文
//	    intent.setType("text/plain"); //纯文本则用text/plain的mime
//		act.startActivity(intent);
//	}
//	/**
//	 * 向其他app发送图片
//	 */
//	public static void shareImage(Activity act,String imagePath){
//		Intent intent = new Intent(Intent.ACTION_SEND);
//		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//	    intent.putExtra(Intent.EXTRA_STREAM, UtilUri.parUri(AppEnv.getAppCtx(),new File(imagePath))); //添加附件，附件为file对象
//	    intent.setType("image/*");
//		act.startActivity(intent);
//	}
//	/**
//	 * 向其他app发送文件
//	 */
//	public static void shareFile(Activity act,String filePath){
//		Intent intent = new Intent(Intent.ACTION_SEND);
//		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//	    intent.putExtra(Intent.EXTRA_STREAM, UtilUri.parUri(AppEnv.getAppCtx(),new File(filePath))); //添加附件，附件为file对象
//	    intent.setType("file/*");
//		act.startActivity(intent);
//	}
//
//
//
//
//}
