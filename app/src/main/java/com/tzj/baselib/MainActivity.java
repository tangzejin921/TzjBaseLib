package com.tzj.baselib;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tzj.baselib.chain.adapter.BaseLibFragmentAdapter;
import com.tzj.baselib.chain.activity.BaseLibActivity;
import com.tzj.baselib.demo.R;
import com.tzj.baselib.widget.refresh.GifHeader;
import com.tzj.baselib.widget.webview.TzjWebView;


public class MainActivity extends BaseLibActivity {
    static {
        GifHeader.init();
    }
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
        webView.loadUrl("https://www.baidu.com/");
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
