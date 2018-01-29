package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import com.homechart.app.R;
import com.homechart.app.home.base.BaseFragment;

@SuppressLint("ValidFragment")
public class HomeFaXianFragment
        extends BaseFragment {

    private FragmentManager fragmentManager;


    public HomeFaXianFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public HomeFaXianFragment() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_faxian;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

}
