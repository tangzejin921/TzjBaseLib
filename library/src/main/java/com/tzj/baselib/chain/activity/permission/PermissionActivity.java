package com.tzj.baselib.chain.activity.permission;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.LruCache;
import android.widget.Toast;

import com.tzj.baselib.chain.activity.rule.RuleActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 方便调用权限
 */
public class PermissionActivity extends RuleActivity {
    private LruCache<Integer, Permission.CallBack> map = new LruCache(2);

    public Permission openPermission() {
        return new Permission(this) {
            @Override
            public void requestPermissions(Activity act, CallBack callBack, List<String> lists) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    for (String s : lists) {
                        boolean permissionGranted = PermissionChecker.checkCallingOrSelfPermission(act, s) == PackageManager.PERMISSION_GRANTED;
                        if (!permissionGranted) {//只要有一个没权限的，请求全部权限
                            map.put(callBack.hashCode() % 65530, callBack);
                            ActivityCompat.requestPermissions(act, lists.toArray(new String[lists.size()]), callBack.hashCode() % 65530);
                            return;
                        }
                    }
                }
                callBack.ok();
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permission.CallBack remove = map.remove(requestCode);
        if (remove == null) {
            return;
        }
        List<String> denides = new ArrayList<>();//拒绝  弹窗告知没权限
        List<String> rememberDenides = new ArrayList<>();//拒绝并且记住   跳到设置界面
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//拒绝了
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    denides.add(permissions[i]);
                } else {//记住了
                    rememberDenides.add(permissions[i]);
                }
            }
        }
        if (rememberDenides.size() > 0) {
            onPermissionDead(rememberDenides);
        } else if (denides.size() > 0) {
            onPermissionCancle(denides);
        } else {
            remove.ok();
        }
    }

    /**
     * 权限被拒绝并且记住了
     */
    protected void onPermissionDead(List<String> list) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.putExtra("app_package", getPackageName());
        intent.putExtra("app_uid", getApplicationInfo().uid);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
        Toast.makeText(this, "请打开相关权限", Toast.LENGTH_LONG).show();
    }

    /**
     * 权限被关闭
     */
    protected void onPermissionCancle(List<String> list) {
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.evictAll();
        map = null;
    }
}
