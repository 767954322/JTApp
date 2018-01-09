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
import com.homechart.app.home.fragment.XXAlbumFragment;
import com.homechart.app.home.fragment.XXDingYueFragment;
import com.homechart.app.home.fragment.XXGuanZhuFragment;
import com.homechart.app.home.fragment.XXMessageFragment;
import com.homechart.app.home.fragment.XXPingLunFragment;
import com.homechart.app.home.fragment.XXShouCangFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
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
    private final String[] mTitles = {"关注", "订阅", "收藏", "评论", "系统消息"};
    //页卡视图集合
    private List<Fragment> mFragmentsList = new ArrayList<>();
    private CustomViewPagerTab vp_viewpager;
    private XXGuanZhuFragment xxGuanZhuFragment;
    private XXDingYueFragment xxDingYueFragment;
    private MyPagerAdapter myPagerAdapter;
    private XXAlbumFragment xxAlbumFragment;
    private XXPingLunFragment xxPingLunFragment;
    private XXMessageFragment xxMessageFragment;
    private int notice_num;
    private int follow_notice;
    private int collect_notice;
    private int comment_notice;
    private int system_notice;
    private int subscribe_notice;
    private int addToAlbum_notice;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_message_list;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        notice_num = getIntent().getIntExtra("notice_num", 0);
        follow_notice = getIntent().getIntExtra("follow_notice", 0);
        collect_notice = getIntent().getIntExtra("collect_notice", 0);
        comment_notice = getIntent().getIntExtra("comment_notice", 0);
        system_notice = getIntent().getIntExtra("system_notice", 0);
        subscribe_notice = getIntent().getIntExtra("subscribe_notice", 0);
        addToAlbum_notice = getIntent().getIntExtra("addToAlbum_notice", 0);

    }

    @Override
    protected void initView() {
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        stl_tab = (SlidingTabLayout) findViewById(R.id.stl_tab);
        vp_viewpager = (CustomViewPagerTab) findViewById(R.id.vp_viewpager);
        vp_viewpager.setScanScroll(true);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mIBBack.setOnClickListener(this);
        stl_tab.setOnTabSelectListener(this);
        vp_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                stl_tab.hideMsg(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
        stl_tab.setCurrentTab(4);
        if (notice_num > 0) {
            if (follow_notice > 0) {
                stl_tab.showMsg(0, follow_notice);
            }
            if (subscribe_notice > 0) {
                stl_tab.showMsg(1, subscribe_notice);
            }
            if (addToAlbum_notice > 0) {
                stl_tab.showMsg(2, addToAlbum_notice);
            }
            if (comment_notice > 0) {
                stl_tab.showMsg(3, comment_notice);
            }
//            if (system_notice > 0) {
//                stl_tab.showMsg(3, system_notice);
//            }
        }
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
        xxDingYueFragment = new XXDingYueFragment(getSupportFragmentManager());
        xxAlbumFragment = new XXAlbumFragment(getSupportFragmentManager());
        xxPingLunFragment = new XXPingLunFragment(getSupportFragmentManager());
        xxMessageFragment = new XXMessageFragment(getSupportFragmentManager());
        mFragmentsList.add(xxGuanZhuFragment);
        mFragmentsList.add(xxDingYueFragment);
        mFragmentsList.add(xxAlbumFragment);
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

        if (position == 0) {
        } else if (position == 1) {
        } else if (position == 2) {
        }

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
