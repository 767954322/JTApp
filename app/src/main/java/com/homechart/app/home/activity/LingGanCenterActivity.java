package com.homechart.app.home.activity;

import android.app.ListActivity;
import android.content.Intent;
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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.fragment.OtherLingGuanLiFragment;
import com.homechart.app.home.fragment.MyLingGuanLiFragment;
import com.homechart.app.lingganji.ui.activity.InspirationCreateActivity;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gumenghao on 17/6/7.
 */

public class LingGanCenterActivity extends BaseActivity
        implements View.OnClickListener,
        OtherLingGuanLiFragment.ChangeUI {

    private ImageButton mIBBack;
    private TextView mTVTital;
    private final String[] mTitles = {"我的灵感辑", "收藏灵感辑"};
    private CustomViewPagerTab mViewPager;
    //页卡标题集合
    private List<Fragment> mFragmentsList = new ArrayList<>();//页卡视图集合
    private TextView tv_content_right;
    private String user_id;
    private MyPagerAdapter mViewPagerAdapter;
    private boolean ifAllowScroll = true;
    private MyLingGuanLiFragment myLingGuanLiFragment;
    private OtherLingGuanLiFragment otherLingGuanLiFragment;
    private SlidingTabLayout mTabLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_linggan_center;
    }

    @Override
    protected void initView() {
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);

        mViewPager = (CustomViewPagerTab) findViewById(R.id.vp_view);
        mViewPager.setScanScroll(true);
        mTabLayout = (SlidingTabLayout) findViewById(R.id.tly_slidingtablayout);
        //设置下划线的高度
        mTabLayout.setIndicatorHeight(4f);
        mTabLayout.setIndicatorWidth(70f);
        //设置tab的字体大小
        mTabLayout.setTextsize(14f);
        initFragment();
        mViewPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setViewPager(mViewPager);
        //初始位置
        mViewPager.setCurrentItem(0);
    }

    private void initFragment() {
        myLingGuanLiFragment = new MyLingGuanLiFragment(user_id);
        otherLingGuanLiFragment = new OtherLingGuanLiFragment(user_id, this);
        mFragmentsList.add(myLingGuanLiFragment);
        mFragmentsList.add(otherLingGuanLiFragment);
    }

    @Override
    public void ifShowDelete(boolean bool) {
        if (bool) {
            //打开管理
            tv_content_right.setText("取消");
            mViewPager.setScanScroll(false);
        } else {
            //关闭管理
            tv_content_right.setText("管理");
            mViewPager.setScanScroll(true);
        }
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

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        user_id = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mIBBack.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    //友盟统计
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("evenname", "查看我的灵感辑");
                    map.put("even", "点击我的-灵感辑-我的灵感辑下的内容的次数");
                    MobclickAgent.onEvent(LingGanCenterActivity.this, "shijian36", map);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("点击我的-灵感辑-我的灵感辑下的内容的次数")  //事件类别
                            .setAction("查看我的灵感辑")      //事件操作
                            .build());
                    tv_content_right.setText("新建");
                } else if (position == 1) {
                    //友盟统计
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("evenname", "查看收藏灵感辑");
                    map.put("even", "点击我的-灵感辑-收藏灵感辑下的内容的次数");
                    MobclickAgent.onEvent(LingGanCenterActivity.this, "shijian37", map);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("点击我的-灵感辑-收藏灵感辑下的内容的次数")  //事件类别
                            .setAction("查看收藏灵感辑")      //事件操作
                            .build());
                    tv_content_right.setText("管理");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTVTital.setText("灵感辑");
        tv_content_right.setText("新建");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                LingGanCenterActivity.this.finish();
                break;
            case R.id.tv_content_right:
                if (mViewPager.getCurrentItem() == 0) {
                    Intent intent = new Intent(LingGanCenterActivity.this, InspirationCreateActivity.class);
                    intent.putExtra("userid", user_id);
                    startActivityForResult(intent, 1);
                } else if (mViewPager.getCurrentItem() == 1) {
                    if (ifAllowScroll) {
                        if (otherLingGuanLiFragment.ifHasData()) {
                            mViewPager.setScanScroll(false);
                            ifAllowScroll = false;
                            otherLingGuanLiFragment.clickRightGuanLi();
                        } else {
                            ToastUtils.showCenter(LingGanCenterActivity.this, "先去收藏一些灵感辑吧");
                        }
                    } else {
                        mViewPager.setScanScroll(true);
                        ifAllowScroll = true;
                        otherLingGuanLiFragment.clickRightGuanLi();
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("灵感辑个人中心");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("灵感辑个人中心");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("灵感辑个人中心");
        MobclickAgent.onPause(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            myLingGuanLiFragment.onRefresh();
        } else if (requestCode == 2 && resultCode == 2) {
            otherLingGuanLiFragment.onRefresh();
        } else if (requestCode == 3) {
            myLingGuanLiFragment.onRefresh();
        }
    }

}
