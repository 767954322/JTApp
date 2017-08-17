package com.homechart.app.home.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flyco.tablayout.CustomViewPagerTab;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.fragment.ShouCangArticleFragment;
import com.homechart.app.home.fragment.ShouCangPicFragment;
import com.homechart.app.home.fragment.XXGuanZhuFragment;
import com.homechart.app.home.fragment.XXMessageFragment;
import com.homechart.app.home.fragment.XXPingLunFragment;
import com.homechart.app.home.fragment.XXShouCangFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gumenghao on 17/6/7.
 */

public class MessagesListActivity extends BaseActivity
        implements View.OnClickListener,
        OnTabSelectListener {

    private ImageButton mIBBack;
    private TextView mTVTital;
    private SlidingTabLayout stl_tab;
    //页卡标题集合
    private final String[] mTitles = {"关注", "收藏", "评论", "系统消息"};
    //页卡视图集合
    private List<Fragment> mFragmentsList = new ArrayList<>();
    private CustomViewPagerTab vp_viewpager;
    private XXGuanZhuFragment xxGuanZhuFragment;
    private MyPagerAdapter myPagerAdapter;
    private XXShouCangFragment xxShouCangFragment;
    private XXPingLunFragment xxPingLunFragment;
    private XXMessageFragment xxMessageFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_message_list;
    }

    @Override
    protected void initView() {
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        stl_tab = (SlidingTabLayout) findViewById(R.id.stl_tab);
        vp_viewpager = (CustomViewPagerTab) findViewById(R.id.vp_viewpager);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mIBBack.setOnClickListener(this);
        stl_tab.setOnTabSelectListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTVTital.setText("消息");

        initFragment();
        //设置下划线的高度
        stl_tab.setIndicatorHeight(4f);
        stl_tab.setIndicatorWidth(70f);
        //设置tab的字体大小
        stl_tab.setTextsize(14f);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp_viewpager.setAdapter(myPagerAdapter);
        stl_tab.setViewPager(vp_viewpager);

//        stl_tab.showMsg(2, 100);
//        stl_tab.showMsg(0, 2);
//
//        stl_tab.hideMsg(0);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                MessagesListActivity.this.finish();
                break;
        }
    }

    private void initFragment() {
        xxGuanZhuFragment = new XXGuanZhuFragment(getSupportFragmentManager());
        xxShouCangFragment = new XXShouCangFragment(getSupportFragmentManager());
        xxPingLunFragment = new XXPingLunFragment(getSupportFragmentManager());
        xxMessageFragment = new XXMessageFragment(getSupportFragmentManager());
        mFragmentsList.add(xxGuanZhuFragment);
        mFragmentsList.add(xxShouCangFragment);
        mFragmentsList.add(xxPingLunFragment);
        mFragmentsList.add(xxMessageFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("系统消息页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("系统消息页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("系统消息页");
        MobclickAgent.onPause(this);
    }

    @Override
    public void onTabSelect(int position) {

        stl_tab.hideMsg(position);

    }

    @Override
    public void onTabReselect(int position) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }
    }
}
