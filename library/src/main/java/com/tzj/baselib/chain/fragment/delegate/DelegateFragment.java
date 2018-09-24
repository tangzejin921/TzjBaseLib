package com.tzj.baselib.chain.fragment.delegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tzj.baselib.chain.fragment.LazyCacheFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DelegateFragment extends LazyCacheFragment {
    private List<FragmentDelegate> mList = new ArrayList<>();

    public void addDelegate(List<FragmentDelegate> list){
        list.add(new LogDelegate(this));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        addDelegate(mList);
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onAttach(context);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onCreate(savedInstanceState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View temp = super.onCreateView(inflater, container, savedInstanceState);
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onCreateView(inflater,container,savedInstanceState);
        }
        return temp;
    }

    @Override
    public void initView() {
        super.initView();
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().initView();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onActivityCreated(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onStart();
        }
    }

    @Override
    public void refresh() {
        super.refresh();
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onResume();
        }
    }

    @Override
    public void onVisible() {
        super.onVisible();
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onVisible();
        }
    }

    @Override
    public void onGone() {
        super.onGone();
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onGone();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDestroyView();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDestroy();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDetach();
            iterator.remove();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Iterator<FragmentDelegate> iterator = mList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onActivityResult(requestCode,resultCode,data);
        }
    }

}
