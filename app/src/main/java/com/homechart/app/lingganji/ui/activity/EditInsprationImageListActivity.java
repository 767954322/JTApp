package com.homechart.app.lingganji.ui.activity;

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
import com.homechart.app.home.fragment.InspirationImagePicFragment;
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

public class EditInsprationImageListActivity extends BaseActivity
        implements View.OnClickListener,
        InspirationImagePicFragment.ChangeUI{

    private ImageButton mIBBack;
    private TextView mTVTital;
    private final String[] mTitles = {"晒家"};
    private CustomViewPagerTab mViewPager;
    private SlidingTabLayout mTabLayout;
    //页卡标题集合
    private List<Fragment> mFragmentsList = new ArrayList<>();//页卡视图集合
    private TextView tv_content_right;
    private String user_id;
    private MyPagerAdapter mViewPagerAdapter;
    private boolean ifAllowScroll = true;
    private InspirationImagePicFragment inspirationImagePicFragment;
    private TextView mLeftText;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_inspiration_editimage;
    }

    @Override
    protected void initView() {
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        mLeftText = (TextView) findViewById(R.id.nav_left_textView);
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
    }

    private void initFragment() {
        inspirationImagePicFragment = new InspirationImagePicFragment(user_id, this);
        mFragmentsList.add(inspirationImagePicFragment);
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
        mLeftText.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mIBBack.setVisibility(View.GONE);
        mLeftText.setVisibility(View.VISIBLE);
        mTVTital.setText("管理图片");
//        tv_content_right.setText("管理");
        mLeftText.setText("取消");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_textView:
                EditInsprationImageListActivity.this.finish();
                this.overridePendingTransition(R.anim.pop_exit_anim, 0);
                break;
            case R.id.tv_content_right:
                if (mViewPager.getCurrentItem() == 0) {
                    if (ifAllowScroll) {
                        if (inspirationImagePicFragment.ifHasData()) {
                            mViewPager.setScanScroll(false);
                            mTabLayout.setCanScrool(false);
                            ifAllowScroll = false;
                            inspirationImagePicFragment.clickRightGuanLi();
                        } else {
                            ToastUtils.showCenter(EditInsprationImageListActivity.this, "先去收藏一些图片吧");
                        }
                    } else {
                        mViewPager.setScanScroll(true);
                        mTabLayout.setCanScrool(true);
                        ifAllowScroll = true;
                        inspirationImagePicFragment.clickRightGuanLi();
                    }
                } else if (mViewPager.getCurrentItem() == 1) {

                } else if (mViewPager.getCurrentItem() == 2) {

                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("个人中心收藏列表页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("个人中心收藏列表页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("个人中心收藏列表页");
        MobclickAgent.onPause(this);
    }

}
