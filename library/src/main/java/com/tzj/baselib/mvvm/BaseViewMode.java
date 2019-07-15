package com.tzj.baselib.mvvm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

/**
 * 苏宁物流集团
 *
 * @Author 19043910
 * @CreateTime 2019/7/8 14:20
 * @Description: 存储和管理与UI相关的数据，本质是 HashMap,管理体现在Activity/Fragment的生命周期上
 */
public class BaseViewMode<T> extends AndroidViewModel {
    protected MutableLiveData<T> liveData = new MutableLiveData();

    public BaseViewMode(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData getLiveData() {
        return liveData;
    }

    public void postValue(T value) {
        liveData.postValue(value);
    }

    public void setValue(T value) {
        liveData.setValue(value);
    }

    public void observe(LifecycleOwner owner, Observer<T> observer){
        liveData.observe(owner,observer);
    }
    public void observeForever(Observer<T> observer){
        liveData.observeForever(observer);
    }
    public void removeObservers(final LifecycleOwner owner){
        liveData.removeObservers(owner);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        liveData = null;
    }
}
