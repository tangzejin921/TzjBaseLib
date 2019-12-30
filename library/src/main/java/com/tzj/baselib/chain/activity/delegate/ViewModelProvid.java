package com.tzj.baselib.chain.activity.delegate;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.ViewModelStore;
import android.arch.lifecycle.ViewModelStores;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.tzj.baselib.env.TzjAppEnv;

import java.util.LinkedHashMap;

/**
 * 保存静态变量
 */
public class ViewModelProvid extends ActivityDelegate{
    private static LinkedHashMap<Class<? extends Activity>, ViewModelStore> map = new LinkedHashMap<>(20);
    public ViewModelProvid(AppCompatActivity activity) {
        super(activity);
    }

    /**
     * 将ViewModelStore保存到静态变量中，生命周期将跟随 Activity
     * 注意：
     *  请在调用此方法的页面不要调用setLifecycleOwner,
     *  不然去其他界面再返回后数据不会更新,
     *  具体什么原因要没深究
     */
    public static ViewModelProvider ofStatic(FragmentActivity act) {
        ViewModelStore viewModelStore = map.get(act.getClass());
        if (viewModelStore == null){
            viewModelStore = ViewModelStores.of(act);
            map.put(act.getClass(),viewModelStore);
        }
        return new ViewModelProvider(viewModelStore,ViewModelProvider.AndroidViewModelFactory.getInstance(act.getApplication()));
    }

    /**
     * 用于获取其他界面的ViewModel(数据)，
     * 实现跨界面获取数据
     */
    public static ViewModelProvider of(Class<? extends FragmentActivity> clazz) {
        ViewModelStore viewModelStore = map.get(clazz);
        if (viewModelStore == null){
            throw new RuntimeException("界面 "+clazz.getSimpleName()+" 并不存在，可能已经释放");
        }
        return new ViewModelProvider(viewModelStore,ViewModelProvider.AndroidViewModelFactory.getInstance(TzjAppEnv.getAppCtx()));
    }

    @Override
    public void onDestroy() {
        AppCompatActivity appCompatActivity = mActivity.get();
        if (appCompatActivity != null){
            map.remove(appCompatActivity.getClass());
        }
        super.onDestroy();
    }

    public static ViewModelProvider of(FragmentActivity activity) {
        return ViewModelProviders.of(activity, null);
    }
    public static ViewModelProvider of(Fragment fragment) {
        return ViewModelProviders.of(fragment, null);
    }

}
