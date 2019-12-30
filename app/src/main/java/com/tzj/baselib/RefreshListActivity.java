package com.tzj.baselib;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.tzj.baselib.chain.activity.BaseLibActivity;
import com.tzj.baselib.chain.adapter.BaseLibFragmentAdapter;
import com.tzj.baselib.demo.R;
import com.tzj.baselib.utils.UtilShape;

public class RefreshListActivity extends BaseLibActivity {

    private ViewPager viewPager;
    private BaseLibFragmentAdapter adapter;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_list);

        initView();
    }

    @Override
    public void initView() {
        super.initView();
        viewPager = findViewById(com.tzj.baselib.demo.R.id.viewPagers);
        Button button = findViewById(R.id.button);
        button.setBackground(UtilShape.get(0,0).setStroke(2,0).draw());
        button.setOnClickListener(v->adapter.notifyDataSetChanged());

        adapter = new BaseLibFragmentAdapter(getSupportFragmentManager());
        MainFragment mainFragment = new MainFragment();
        adapter.addFragment(mainFragment);
        adapter.addFragment(new MainFragment());
        mainFragment = new MainFragment();
        adapter.addFragment(mainFragment);
        adapter.addFragment(new MainFragment());
        adapter.addFragment(new MainFragment());
        adapter.addFragment(new MainFragment());
        viewPager.setAdapter(adapter);
    }

}
