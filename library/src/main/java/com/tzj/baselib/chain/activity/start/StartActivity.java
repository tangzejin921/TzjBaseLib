package com.tzj.baselib.chain.activity.start;

import android.content.Intent;
import android.util.LruCache;

import com.tzj.baselib.chain.activity.permission.PermissionActivity;


/**
 * 加了带返回的 start 方法
 */
public class StartActivity extends PermissionActivity {
    private LruCache<Integer,IResult> map = new LruCache(2);//原来static WeakHashMap 出现返回不刷新

    public void start(Intent intent, IResult result) {
        int requestCode = -1;
        if (result != null){
            map.put(requestCode = result.hashCode()%65530,result);
        }
        startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IResult remove = map.remove(requestCode);
        if(remove!=null){
            remove.onActivityResult(new ActivityResult(resultCode,data));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.evictAll();
        map = null;
    }
}
