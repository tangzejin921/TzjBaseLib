package com.tzj.baselib.chain.activity.start;

import android.content.Intent;
import android.util.LruCache;

import com.tzj.baselib.chain.activity.permission.PermissionActivity;


/**
 * 加了带返回的 start 方法
 */
public class StartActivity extends PermissionActivity {
    protected LruCache<Integer,IResult> resultMap = new LruCache(2);//原来static WeakHashMap 出现返回不刷新



    public void start(Intent intent, IResult result) {
        startActivityForResult(intent,poutResult(result));
    }

    /**
     * 保存 IResult
     * @param result
     * @return requestCode
     */
    protected int poutResult(IResult result){
        int requestCode = -1;
        if (result != null){
            resultMap.put(requestCode = result.hashCode()%65530,result);
        }
        return requestCode;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IResult remove = resultMap.remove(requestCode);
        if(remove!=null){
            remove.onActivityResult(new ActivityResult(resultCode,data));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resultMap.evictAll();
        resultMap = null;
    }
}
