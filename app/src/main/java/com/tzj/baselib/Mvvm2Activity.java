package com.tzj.baselib;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tzj.baselib.demo.R;
import com.tzj.baselib.chain.activity.BaseLibActivity;
import com.tzj.baselib.chain.activity.delegate.ViewModelProvid;
import com.tzj.baselib.dataBinding.DataViewMode;
import com.tzj.baselib.demo.databinding.ActivityMvvm2Binding;

public class Mvvm2Activity extends BaseLibActivity {
    public static void start(Context context) {
        Intent starter = new Intent(context, Mvvm2Activity.class);
        context.startActivity(starter);
    }
    private ActivityMvvm2Binding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding =  DataBindingUtil.setContentView(this,R.layout.activity_mvvm_2);
        binding.setLifecycleOwner(this);
        binding.setViewData(ViewModelProvid.of(MvvmActivity.class).get(DataViewMode.class).getViewData());
    }

}
