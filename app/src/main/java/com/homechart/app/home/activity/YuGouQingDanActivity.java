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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.fragment.ShouCangArticleFragment;
import com.homechart.app.home.fragment.ShouCangPicFragment;
import com.homechart.app.home.fragment.ShouCangShopFragment;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gumenghao on 17/6/7.
 */

public class YuGouQingDanActivity extends BaseActivity
        implements View.OnClickListener,
        ShouCangPicFragment.ChangeUI,
        ShouCangArticleFragment.ChangeArticleUI,
        ShouCangShopFragment.ChangeShopUI {

    private ImageButton mIBBack;
    private TextView mTVTital;
    private final String[] mTitles = {"商品"};
    private CustomViewPagerTab mViewPager;
    private SlidingTabLayout mTabLayout;
    private ShouCangPicFragment shouCangPicFragment;
    private ShouCangArticleFragment ShouCangArticleFragment;
    //页卡标题集合
    private List<Fragment> mFragmentsList = new ArrayList<>();//页卡视图集合
    private TextView tv_content_right;
    private String user_id;
    private MyPagerAdapter mViewPagerAdapter;
    private boolean ifAllowScroll = true;
    private ShouCangShopFragment shouCangShopFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_yugouqingdan_list;
    }

    @Override
    protected void initView() {
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);

        mViewPager = (CustomViewPagerTab) findViewById(R.id.vp_view);
        mViewPager.setScanScroll(false);
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
        shouCangShopFragment = new ShouCangShopFragment(user_id, this);
        mFragmentsList.add(shouCangShopFragment);
    }

    @Override
    public void ifShowDelete(boolean bool) {
        if (bool) {
            //打开管理
            tv_content_right.setText("取消");
            mViewPager.setScanScroll(false);
            mTabLayout.setCanScrool(false);
        } else {
            //关闭管理
            tv_content_right.setText("管理");
            mViewPager.setScanScroll(true);
            mTabLayout.setCanScrool(true);
        }
    }

    @Override
    public void ifShowArticleDelete(boolean bool) {
        if (bool) {
            //打开管理
            tv_content_right.setText("取消");
            mViewPager.setScanScroll(false);
            mTabLayout.setCanScrool(false);
        } else {
            //关闭管理
            tv_content_right.setText("管理");
            mViewPager.setScanScroll(true);
            mTabLayout.setCanScrool(true);
        }
    }

    @Override
    public void ifShowShopDelete(boolean bool) {
        if (bool) {
            //打开管理
            tv_content_right.setText("取消");
            mViewPager.setScanScroll(false);
            mTabLayout.setCanScrool(false);
        } else {
            //关闭管理
            tv_content_right.setText("管理");
            mViewPager.setScanScroll(true);
            mTabLayout.setCanScrool(true);
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

                if(position == 2){
                    //友盟统计
                    HashMap<String, String> map4 = new HashMap<String, String>();
                    map4.put("evenname", "收藏商品查看");
                    map4.put("even", "收藏商品查看");
                    MobclickAgent.onEvent(YuGouQingDanActivity.this, "shijian22", map4);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("收藏商品查看")  //事件类别
                            .setAction("收藏商品查看")      //事件操作
                            .build());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTVTital.setText("预购清单");
        tv_content_right.setText("管理");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                YuGouQingDanActivity.this.finish();
                break;
            case R.id.tv_content_right:
                    if (ifAllowScroll) {
                        if (shouCangShopFragment.ifHasData()) {
                            mViewPager.setScanScroll(false);
                            mTabLayout.setCanScrool(false);
                            ifAllowScroll = false;
                            shouCangShopFragment.clickRightGuanLi();
                        } else {
                            ToastUtils.showCenter(YuGouQingDanActivity.this, "先去收藏一些商品吧");
                        }
                    } else {
                        mViewPager.setScanScroll(true);
                        mTabLayout.setCanScrool(true);
                        ifAllowScroll = true;
                        shouCangShopFragment.clickRightGuanLi();
                    }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("欲购清单列表页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("欲购清单列表页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("欲购清单列表页");
        MobclickAgent.onPause(this);
    }

}
