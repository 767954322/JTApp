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
        implements View.OnClickListener {

    private ImageButton mIBBack;
    private TextView mTVTital;
    private final String[] mTitles = {""};
    private CustomViewPagerTab mViewPager;
    private SlidingTabLayout mTabLayout;
    //页卡标题集合
    private List<Fragment> mFragmentsList = new ArrayList<>();//页卡视图集合
    private TextView tv_content_right;
    private MyPagerAdapter mViewPagerAdapter;
    private boolean ifAllowScroll = true;
    private InspirationImagePicFragment inspirationImagePicFragment;
    private TextView mLeftText;
    private String mAlbumId;
    private String mMyUserId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_inspiration_editimage;
    }

    @Override
    protected void initView() {
        mMyUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
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
        inspirationImagePicFragment = new InspirationImagePicFragment(mAlbumId, mMyUserId);
        mFragmentsList.add(inspirationImagePicFragment);
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
        mAlbumId = getIntent().getStringExtra("albumId");
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
        mLeftText.setText("取消");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_textView:
                EditInsprationImageListActivity.this.setResult(2,getIntent());
                EditInsprationImageListActivity.this.finish();
                this.overridePendingTransition(R.anim.pop_exit_anim, 0);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("管理专辑图片页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("管理专辑图片页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("管理专辑图片页");
        MobclickAgent.onPause(this);
    }

}
