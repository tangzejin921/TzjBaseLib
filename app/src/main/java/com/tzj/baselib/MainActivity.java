package com.tzj.baselib;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;

import com.tzj.baselib.chain.activity.BaseLibActivity;
import com.tzj.baselib.demo.R;
import com.tzj.baselib.webview.TzjWebView;


public class MainActivity extends BaseLibActivity {

    private TzjWebView webView;
    private ViewPager viewPager;
    private BaseLibFragmentAdapter adapter;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    public void initView() {
        super.initView();
        viewPager = findViewById(R.id.viewPagers);
        webView = findViewById(R.id.webView);

        webView.setCanOneKeyBack(false);
        webView.loadUrl("https://www.baidu.com");
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
            }
        });

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

    @Override
    public void onRefresh() {
        super.onRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }




}
