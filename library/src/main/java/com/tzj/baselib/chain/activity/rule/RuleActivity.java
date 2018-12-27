package com.tzj.baselib.chain.activity.rule;

import android.content.ComponentName;
import android.content.Intent;

import com.tzj.baselib.chain.activity.delegate.DelegateActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 路由拦截
 */
public class RuleActivity extends DelegateActivity {
    protected static Map<String,ICheck> mInterception = new HashMap();

    @Override
    public void startActivity(Intent intent) {
        if (!intercept(intent,-1)){
            super.startActivity(intent);
        }
    }
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (!intercept(intent,requestCode)){
            super.startActivityForResult(intent,requestCode);
        }
    }

    /**
     * 拦截判断
     */
    private boolean intercept(Intent intent,int requestCode){
        ComponentName component = intent.getComponent();
        if (component!=null){
            String className = component.getClassName();
            ICheck check = mInterception.get(className);
            return check != null && check.call(this, intent, requestCode);
        }
        return false;
    }

}
