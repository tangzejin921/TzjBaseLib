package com.tzj.baselib.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;

/**
 * 系统相关
 */
public class UtilSystem {
    /**
     * 得到当前进程名
     */
    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }
    /**
     * 获取手机IMEI码
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     * 6.0 需要权限
     */
    public static String getPhoneIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            throw new RuntimeException("请获取权限");
            return "000000000000000";
        }
//        // If running on an emulator
//        if (deviceId == null || deviceId.trim().length() == 0 || deviceId.matches("0+")) {
//            deviceId = (new StringBuilder("EMU")).append((new Random(System.currentTimeMillis())).nextLong()).toString();
//        }
        return tm.getDeviceId();
    }
    /**
     * 获取本地机器IP
     * <uses-permission android:name="android.permission.INTERNET" >
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String value = inetAddress.getHostAddress();
                        if (value.contains("."))
                            return value;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 得到ip
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     */
    public static String intToIp(Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        int i = wifiManager.getConnectionInfo().getIpAddress();
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    /**
     * 读取mainfast文件内容 注意"_",应为数字字符串会被转成double
     */
    public static String getAppKey(Context context, String kayName) {
        Bundle metaData = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                String appKey = metaData.getString(kayName);
                if (metaData != null) {
                    Object keyO = metaData.get(kayName);
                    return keyO.toString().replace("_","");
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("获取签名失败:"+e.getMessage());
        }
        return null;
    }

    /**
     * 生成 uuid ，每次不一样的
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        str = str.replace("-", "");
        return str;
    }

    /**
     * 跳转打电话界面
     */
    public static void call(Context ctx,String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    /**
     * 打开设置
     */
    public static void openSetting(Context ctx){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", ctx.getPackageName(), null);
        intent.setData(uri);
        ctx.startActivity(intent);
    }
    /**
     * 跳转通知设置界面
     */
    public static boolean openNotifi(final Context ctx){
        NotificationManagerCompat manager = NotificationManagerCompat.from(ctx);
        boolean isOpened = manager.areNotificationsEnabled();
        if (isOpened){
            return isOpened;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setIcon(ctx.getApplicationInfo().icon)
                .setTitle(ctx.getApplicationInfo().name)
                .setMessage("通知被关闭，您将收不到通知\n请允许通知")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                            intent.putExtra("app_package", ctx.getPackageName());
                            intent.putExtra("app_uid", ctx.getApplicationInfo().uid);
                            ctx.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            openSetting(ctx);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
        return isOpened;
    }

    /**
     * 清除 cookies
     */
    public static void clearCookies(Context ctx){
        CookieSyncManager.createInstance(ctx.getApplicationContext());
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeAllCookie();
        CookieSyncManager.getInstance().stopSync();
    }

    /**
     * 设置当前activity的屏幕亮度
     *
     * @param paramFloat 0-1.0f
     * @param activity   需要调整亮度的activity
     */
    public static void setActivityBrightness(float paramFloat, Activity activity) {
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams params = localWindow.getAttributes();
        params.screenBrightness = paramFloat;
        localWindow.setAttributes(params);
    }

    /**
     * 获取当前activity的屏幕亮度
     *
     * @param activity 当前的activity对象
     * @return 亮度值范围为0-0.1f，如果为-1.0，则亮度与全局同步。
     */
    public static float getActivityBrightness(Activity activity) {
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams params = localWindow.getAttributes();
        return params.screenBrightness;
    }
}
