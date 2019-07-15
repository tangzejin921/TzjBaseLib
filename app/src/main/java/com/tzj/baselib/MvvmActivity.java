package com.tzj.baselib;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.tzj.baselib.chain.activity.BaseLibActivity;
import com.tzj.baselib.chain.activity.delegate.ViewModelProvid;
import com.tzj.baselib.dataBinding.DataViewMode;
import com.tzj.baselib.demo.R;
import com.tzj.baselib.demo.databinding.ActivityMvvmBinding;

public class MvvmActivity extends BaseLibActivity {
    private ActivityMvvmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_mvvm);
//        binding.setLifecycleOwner(this);//如果是静态请不要调用这个,不然去其他界面返回后数据不会更新了
        binding.setViewMode(ViewModelProvid.ofStatic(this).get(DataViewMode.class));
        binding.setViewData(binding.getViewMode().getViewData());
    }


}
