package com.homechart.app.home.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.fragment.ImageDetailFragment;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/26.
 */

public class ImageDetailScrollActivity
        extends BaseActivity
        implements View.OnClickListener {
    private String item_id;
    private String mUserId;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private int mPosition;
    private List<String> mItemIdList;
    private ViewPager mViewPager;
    private MyImagePageAdater mAdapter;
    private TextView tv_content_right;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_detail_scroll;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        item_id = getIntent().getStringExtra("item_id");
        mPosition = getIntent().getIntExtra("position", -1);
        mItemIdList = (List<String>) getIntent().getSerializableExtra("item_id_list");
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initView() {

        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        mViewPager = (ViewPager) findViewById(R.id.vp_viewpager);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        tv_tital_comment.setText("图片详情");
        tv_content_right.setText("编辑");
        mAdapter = new MyImagePageAdater(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ImageDetailScrollActivity.this.finish();
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("图片详情页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("图片详情页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("图片详情页");
        MobclickAgent.onPause(this);
    }

    public class MyImagePageAdater extends FragmentStatePagerAdapter {

        public MyImagePageAdater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ImageDetailFragment(mItemIdList.get(position));
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return mItemIdList.size();
        }
    }
}
