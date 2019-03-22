package com.tzj.baselib.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.TelephonyManager;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.tzj.baselib.env.AppEnv;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * 系统相关
 */
public class UtilSystem {
    /**
     * 获取手机IMEI码 串号
     */
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static String getPhoneIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
//        if (deviceId == null || deviceId.trim().length() == 0 || deviceId.matches("0+")) {
//            deviceId = (new StringBuilder("EMU")).append((new Random(System.currentTimeMillis())).nextLong()).toString();
//        }
        return tm.getDeviceId();
    }

    /**
     * 没权限会抛
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static String getDeviceId(Context ctx) throws IOException {
        File file = Environment.getExternalStorageDirectory();
        File android = new File(file, ".Android");
        if (!android.exists()){
            android.mkdirs();
        }
        File uuid = new File(android, "UUID");
        String uuidStr = null;
        if (!uuid.exists()){
            uuid.createNewFile();
            uuidStr = UUID.randomUUID().toString().replace("-","");
            FileOutputStream fos = new FileOutputStream(uuid);
            fos.write(uuidStr.getBytes());
            fos.close();
        }else{
            FileInputStream fis = new FileInputStream(uuid);
            BufferedReader br = new BufferedReader(new FileReader(uuid));
            uuidStr = br.readLine();
        }
        return uuidStr;
    }

    /**
     * 获取本地机器IP
     */
    @RequiresPermission(android.Manifest.permission.INTERNET)
    public static String getIpAddress() {
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
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_WIFI_STATE)
    public static String getIpAddress(Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        int i = wifiManager.getConnectionInfo().getIpAddress();
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    /**
     * 读取mainfast文件内容 注意"_",应为数字字符串会被转成double
     */
    public static String getMetaData(Context context, String kayName) {
        Bundle metaData = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                String appKey = metaData.getString(kayName);
                if (metaData != null) {
                    Object keyO = metaData.get(kayName);
                    return keyO.toString().replace("_", "");
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("获取签名失败:" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取应用签名
     */
    public static String getSignature(Context context) {
        try {
            PackageInfo pis = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            return UtilCipher.MD5(pis.signatures[0].toCharsString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
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
     * <uses-permission android:name="android.permission.CALL_PHONE" />
     */
    public static void call(Context ctx, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    /**
     * 跳到/打开市场
     */
    public static void market(Context ctx){
        try {
            Uri uri = Uri.parse("market://details?id=" + ctx.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开设置
     */
    public static void openSetting(Context ctx) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", ctx.getPackageName(), null);
        intent.setData(uri);
        ctx.startActivity(intent);
    }

    /**
     * 跳转通知设置界面
     */
    public static boolean openNotifi(final Context ctx) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(ctx);
        boolean isOpened = manager.areNotificationsEnabled();
        if (isOpened) {
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
     * 屏幕是否亮着
     */
    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return pm.isInteractive();
        }
        return pm.isScreenOn();
    }

    /**
     * 获得当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    public static int getScreenMode(Context ctx) {
        int screenMode = -1;
        try {
            screenMode = Settings.System.getInt(ctx.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenMode;
    }

    /**
     * 设置当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    public static void setScreenMode(Context ctx, int paramInt) {
        try {
            Settings.System.putInt(ctx.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置电源状态
     * <uses-permission android:name="android.permission.WAKE_LOCK"/>
     * cpu       screen       keyboard
     * PARTIAL_WAKE_LOCK                on         off           off
     * SCREEN_DIM_WAKE_LOCK         	on         dim           off
     * SCREEN_BRIGHT_WAKE_LOCK   		on         bright        off
     * FULL_WAKE_LOCK                   on         bright        bright
     */
    public static void setScreenLight(Context ctx, boolean isLight, int flag) {
        PowerManager manager = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock newWakeLock = manager.newWakeLock(flag, "May Video Tag");
        if (isLight) {
            newWakeLock.acquire();//保持
        } else {
            newWakeLock.release();//释放
        }
    }

    /**
     * 设置当前activity的屏幕亮度
     */
    public static void setActivityBrightness(float paramFloat, Activity activity) {
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams params = localWindow.getAttributes();
        params.screenBrightness = paramFloat;
        localWindow.setAttributes(params);
    }

    /**
     * 获取当前activity的屏幕亮度
     */
    public static float getActivityBrightness(Activity activity) {
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams params = localWindow.getAttributes();
        return params.screenBrightness;
    }

    /**
     * 安装apk
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(UtilUri.parUri(context, file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 卸载程序.
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.parse("package:" + packageName);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 静默安装要root权限
     *
     * @return -1无root权限
     * -2 管理root时没同意
     * 0成功安装
     **/
    public static int installApk(String pathName) {
        String cmd = "pm install -r " + pathName;
        return excuteSuCMD(cmd);
    }

    /**
     * 静默卸载要root权限
     *
     * @param packageName
     * @return -1无root权限
     * -2 管理root时没同意
     * 0成功卸载
     */
    public static int unInstallApk(String packageName) {
        String cmd = "pm uninstall -k " + packageName;
        return excuteSuCMD(cmd);
    }

    /**
     * 运行root命令
     */
    public static int excuteSuCMD(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(process.getOutputStream());
            // 部分手机Root之后Library path 丢失，导入path可解决该问题
            dos.writeBytes("export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
            cmd = String.valueOf(cmd);
            dos.writeBytes((cmd + "\n"));
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            process.waitFor();
            String s = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                s += line + "\n";
            }
            if (s.equals("")) {//用户root管理没同意
                return -2;
            }
            int result = process.exitValue();
            return result;//只要有root权限就是0
        } catch (Exception localException) {
            localException.printStackTrace();
            return -1;//没有root权限时
        }
    }

    /**
     * 运行命令
     * "am start -n " + packageName + "/" + activityName + " \n"//启动app
     */
    public static String exec(String cmd) {
        String s = "";
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                s += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return s;
    }

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
     * 判断当前应用程序是否后台运行
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                // 后台运行
// 前台运行
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
            }
        }
        return false;
    }

    /**
     * 用来判断服务是否运行.
     */
    public static boolean isServiceRunning(Context ctx, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
        Iterator<ActivityManager.RunningServiceInfo> l = servicesList.iterator();
        while (l.hasNext()) {
            ActivityManager.RunningServiceInfo si = l.next();
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    /**
     * 停止服务.
     */
    public static boolean stopRunningService(Context ctx, String className) {
        Intent intent_service = null;
        boolean ret = false;
        try {
            intent_service = new Intent(ctx, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent_service != null) {
            ret = ctx.stopService(intent_service);
        }
        return ret;
    }

    /**
     * Gps是否打开
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断网络是否有效.
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    private boolean isConnected(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        // 连接上网络
// 没有连接上
        return activeNetwork != null && activeNetwork.isConnected();
    }

    /**
     * 是否WIFI
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWIFI(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 打开键盘.
     */
    public static void showSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 关闭键盘
     */
    public static void closeSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && context instanceof Activity) {
            View currentFocus = ((Activity) context).getCurrentFocus();
            if (currentFocus != null) {
                IBinder windowToken = currentFocus.getWindowToken();
                if (windowToken != null) {
                    inputMethodManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    /**
     * 设置光标位置到最后
     */
    public static void setCursorPosition(TextView editText) {
        CharSequence text = editText.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }

    /**
     * 写入剪切板
     */
    public static void setClipData(Context ctx, String content) {
        String label = "label";
        ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        if (TextUtils.isEmpty(content)) {
            label = null;
        }
        ClipData cd = ClipData.newPlainText(label, content);
        cm.setPrimaryClip(cd);
    }

    /**
     * 读剪切板
     */
    public static String getClipData(Context ctx) {
        String ret = "";
        ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        try {
            if (cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                ClipData cd = cm.getPrimaryClip();
                ClipData.Item item = cd.getItemAt(0);
                CharSequence text = item.getText();
                if (text == null) {
                    return ret;
                }
                ret = text.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 截屏 可以放到其他线程操作
     */
    public static Bitmap screenShot(Activity ctx) {
        //找到当前页面的跟布局
        View view = ctx.getWindow().getDecorView().getRootView();
        //设置缓存
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //从缓存中获取当前屏幕的图片
        return view.getDrawingCache();
    }

    /**
     * 刷新系统相册
     */
    public static void refreshSysAlbum(Context context, String filePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(
                    context,
                    new String[]{filePath},
                    null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    }
            );
        } else {
            context.sendBroadcast(
                    new Intent(Intent.ACTION_MEDIA_MOUNTED,
                            Uri.parse("file://" + filePath))
            );
        }
    }

    /**
     * 通知
     */
    public static <T extends Activity> void notifi(Context context, String title, String text, String info, int icon, Class<T> clas) {
        Intent intent = new Intent(context, clas);
        PendingIntent intent2 = PendingIntent.getActivity(context, 100, intent, 0); // 点击通知，执行startActivityForResult,

        NotificationManager notifiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //最低SDK为10--2.3.3
        Notification notifi = (new NotificationCompat.Builder(context))
                .setContentTitle(title)
                .setContentText(text)
//				.setStyle(inboxStyle)
                .setContentIntent(intent2)
                .setContentInfo(info)
                .setSmallIcon(icon)
                .setAutoCancel(true)// 设置可以清除
                .setTicker("ticker")
                .setWhen(System.currentTimeMillis())
                .setDefaults(
                        Notification.DEFAULT_LIGHTS//灯
                                | Notification.DEFAULT_SOUND//声音
                                | Notification.DEFAULT_VIBRATE//震动
                )
                .setVibrate(new long[]{0, 100, 300, 100, 100, 300})
                .build();
        notifi.flags = Notification.FLAG_AUTO_CANCEL; //点击后自动消失

        notifiManager.notify(0, notifi);
    }

    /**
     * 要权限 <uses-permission android:name="android.permission.VIBRATE"/>
     *
     * @param pattern 为空时 {0,100,300,100,100,300}
     */
    @RequiresPermission(android.Manifest.permission.VIBRATE)
    public static void vibrator(long[] pattern) {
        if (pattern == null) {
            pattern = new long[]{0, 100, 300, 100, 100, 300};
        }
        Vibrator vibrator = (Vibrator) AppEnv.getAppCtx().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, -1);
    }


    private static SoundPool pool;
    private static int soundID;
    private static String soundName;

    /**
     * 如 assets目录下"rock.mp3", "rock.mp3"
     */
    @SuppressWarnings("deprecation")
    public static void sound(String name, String path) {
        try {
            if (pool == null) {
                pool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
//				pool = new SoundPool.Builder().build();
            }
            if (!name.equals(soundName)) {
                if (path.contains("/")) {
                    soundID = pool.load(path, 1);
                } else {
                    soundID = pool.load(AppEnv.getAppCtx().getAssets().openFd(path), 1);
                }
                soundName = name;
                AppEnv.post(new Runnable() {
                    @Override
                    public void run() {
                        pool.play(soundID, 1, 1, 0, 0, 1);
                    }
                }, 500);
            } else {
                pool.play(soundID, 1, 1, 0, 0, 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放声音
     */
    public static void playVoice(String path, MediaPlayer.OnCompletionListener listener) {
        try {
            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource(path);
            if (listener != null) {
                mp.setOnCompletionListener(listener);
            }
            mp.prepare();
            mp.start();
        } catch (IOException e) {
        }
    }

    /**
     * 判断手机是否处于睡眠
     */
    public static boolean isSleeping(Context context) {
        KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
        return isSleeping;
    }

    /**
     * 获取系统版本
     *
     * @return 形如2.3.3
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 调用系统发送短信
     */
    public static void sendSMS(Context cxt, String smsBody) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", smsBody);
        cxt.startActivity(intent);
    }

    /**
     * 有无相机
     */
    public static boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * 回到home，后台运行
     */
    public static void goHome(Context context) {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
    }

    /**
     * 获取设备的可用内存大小
     */
    public static int getDeviceUsableMemory(Context cxt) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // 返回当前系统的可用内存
        return (int) (mi.availMem / (1024 * 1024));
    }

    /**
     * 调用浏览器下载
     */
    public static void downLoad(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        AppEnv.getAppCtx().startActivity(intent);
    }

    /**
     * 下载,下载完没监听
     * <p>
     * UtilSystem.downLoad(
     * "http://www.baidu.com/img/bdlogo.png",
     * "xiaoniu/download",
     * "bdlog.png",
     * "图片");
     *
     * @param url
     * @param name  文件名
     * @param title 通知显示的标题
     */
    @Deprecated
    public static long downLoad(String url, String name, String title) {
        if (name == null) {
            name = UtilUri.getLast(url, "/");
        }
        DownloadManager download = (DownloadManager) AppEnv.getAppCtx().getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(true);
        // 设置文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);

        // 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
//		request.setShowRunningNotification(notif);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        // 不显示下载界面
        request.setVisibleInDownloadsUi(true);

        // sdcard的目录下的download文件夹   "android.permission.WRITE_EXTERNAL_STORAGE"
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
        request.setTitle(title);
        //将下载请求放入队列
        return download.enqueue(request);
    }

    /**
     * 设置声音大小 调一次声音改变一次
     */
    public static void setVolume(Context ctx, boolean add) {
        AudioManager audioManager = (AudioManager) ctx.getSystemService(Service.AUDIO_SERVICE);
        if (add) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        } else {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
        }
    }

    /**
     * 录音
     */
    public static MutableBoolean recorde(Context ctx, String path, final IrecordeCallback c) {
        final MutableBoolean isRecording = new MutableBoolean(false);
        final MediaRecorder recorder = new MediaRecorder();
        final long startTime = System.currentTimeMillis();
        if (!path.endsWith(".amr")) {
            path += ".amr";
        }
        final File file = new File(path);
        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setAudioChannels(1); // MONO
            recorder.setAudioSamplingRate(8000); // 8000Hz
            recorder.setAudioEncodingBitRate(64); // seems if change this to
            // 128, still got same file
            // size.
            recorder.setOutputFile(file.getAbsolutePath());
            recorder.prepare();
            isRecording.value = true;
            recorder.start();
        } catch (IOException e) {
            recorder.stop();
            recorder.release();
            file.deleteOnExit();
            c.err(e);
        }
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRecording.value) {
                        int what = recorder.getMaxAmplitude() * 13 / 0x7FFF;
                        c.recordeing(what);
                        SystemClock.sleep(100);
                    }
                    recorder.stop();
                    recorder.release();
                    file.deleteOnExit();
                    int time = (int) (System.currentTimeMillis() - startTime) / 1000;
                    c.stop(file.getAbsolutePath(), time);
                } catch (Exception e) {
                    recorder.stop();
                    recorder.release();
                    file.deleteOnExit();
                    c.err(e);
                }
            }
        });
        return isRecording;
    }


    /**
     * 录音的回调接口
     */
    public interface IrecordeCallback {
        void err(Exception e);

        /**
         * @param i 振幅
         */
        void recordeing(int i);

        /**
         * @param path 文件路径
         * @param time 文件时长
         */
        void stop(String path, int time);
    }

    /**
     * 获取库Phon表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.Contacts.Photo.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
    };
    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;
    /**
     * 头像ID
     **/
    private static final int PHONES_PHOTO_ID_INDEX = 2;
    /**
     * 联系人的ID
     **/
    private static final int PHONES_CONTACT_ID_INDEX = 3;

    /**
     * 得到手机SIM卡联系人人信息
     **/
    public static List<Contacts> getPhoneContacts(Context ctx) {
        List<Contacts> list = new ArrayList<>();
        ContentResolver resolver = ctx.getContentResolver();
        // 得到手机SIM卡联系人人信息
        Cursor phoneCursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION,
                null,
                null,
                null
        );
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                // 得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
                // 得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
                // 得到联系人头像Bitamp
                Bitmap contactPhoto = null;
                // photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if (photoid > 0) {
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                }
                list.add(new Contacts(contactName, phoneNumber, contactPhoto));
            }
            phoneCursor.close();
        }
        //得到手机SIM卡联系人人信息
        Uri uri = Uri.parse("content://icc/adn");
        phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                // Sim卡中没有联系人头像
                Contacts contacts = new Contacts(contactName, phoneNumber, null);
                if (!list.contains(contacts)) {
                    list.add(contacts);
                }
            }
            phoneCursor.close();
        }
        return list;
    }

    /**
     * 联系人
     */
    public static class Contacts {
        public String name;
        public String phoneNumber;
        public Bitmap head;

        public Contacts(String name, String phoneNumber, Bitmap head) {
            super();
            this.name = name;
            phoneNumber = phoneNumber.replace("+86", "");
            phoneNumber = phoneNumber.replace(" ", "");
            this.phoneNumber = phoneNumber;
            this.head = head;
        }

        @Override
        public String toString() {
            return phoneNumber;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    public static class MutableBoolean {
        public boolean value;

        public MutableBoolean(boolean value) {
            this.value = value;
        }
    }
}
