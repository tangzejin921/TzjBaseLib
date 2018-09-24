package com.tzj.baselib.chain.fragment.delegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentDelegate {
    protected DelegateFragment mFragment;
    public FragmentDelegate(DelegateFragment fragment) {
        mFragment = fragment;
    }

    public void onAttach(Context context) {
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    public void initView() {
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    }

    public void onStart() {
    }

    /**
     * 第一次显示
     */
    public void refresh() {
    }

    public void onResume() {
    }

    public void onVisible(){
    }

    public void onGone(){
    }

    public void onPause() {
    }

    public void onStop() {
    }

    public void onDestroyView() {
    }
    public void onDestroy() {
    }

    public void onDetach() {
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
