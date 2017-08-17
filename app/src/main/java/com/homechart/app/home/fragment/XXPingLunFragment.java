package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseFragment;

@SuppressLint("ValidFragment")
public class XXPingLunFragment
        extends BaseFragment {

    private FragmentManager fragmentManager;
    public XXPingLunFragment() {
    }
    public XXPingLunFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_xx_pinglun;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

}