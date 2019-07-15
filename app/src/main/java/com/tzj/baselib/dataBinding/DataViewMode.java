package com.tzj.baselib.dataBinding;

import android.app.Application;
import android.support.annotation.NonNull;
import android.view.View;

import com.tzj.baselib.Mvvm2Activity;
import com.tzj.baselib.mvvm.BaseViewMode;


/**
 * 苏宁物流集团
 *
 * @Author 19043910
 * @CreateTime 2019/7/2 19:49
 * @Description:
 */
public class DataViewMode extends BaseViewMode {
    private ViewData viewData = new ViewData();

    public DataViewMode(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        viewData = null;
    }

    public void setViewData(ViewData viewData) {
        this.viewData = viewData;
    }

    public ViewData getViewData() {
        return viewData;
    }


    public void onClick(View v){
        viewData.setText("");
    }

    public void toNext(View v){
        Mvvm2Activity.start(v.getContext());
    }
}
