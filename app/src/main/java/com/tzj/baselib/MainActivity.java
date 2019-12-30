package com.tzj.baselib;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tzj.baselib.chain.activity.BaseLibActivity;
import com.tzj.baselib.demo.R;
import com.tzj.baselib.widget.refresh.GifHeader;

import java.util.List;


public class MainActivity extends BaseLibActivity implements View.OnClickListener{
    static {
        GifHeader.init();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.listRefresh:
                start(new Intent(this,RefreshListActivity.class),refresh);
                break;
            case R.id.mvvm:
                start(new Intent(this,MvvmActivity.class),refresh);
                break;
            case R.id.pic:
                openZhihuChoice(1, new Result() {
                    @Override
                    public void ok(List<Uri> uris) {
                        ImageViewActivity.start(MainActivity.this,uris.get(0));
                    }
                });
                break;
                default:
        }
    }
}
